package eco.stx.edao.twoone.method2;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@EnableAsync
@EnableScheduling
public class TwoOneEventController {

	private static final String YES_ADDRESS = "SP00000000000003SCNSJTCHE66N2PXHX";
	private static final String NO_ADDRESS = "SP00000000000000DSQJTCHE66XE1NHQ";
	private static final int FROM_BLOCK = 83287;
	private static final int UNTIL_BLOCK = 87319;
	@Autowired private TwoOneEventsService twoOneEventsService;
	@Autowired private TwoOneEventRepository twoOneEventRepository;
	@Autowired private MongoTemplate mongoTemplate;

	@GetMapping(value = "/v2/twoone/all-votes")
	public List<TwoOneEvent> findAll() {
		return twoOneEventRepository.findAll();
	}

	@GetMapping(value = "/v2/twoone/results/method2")
	public Method2VoteCount votingInfo(HttpServletRequest request) {
		String balanceKey = "stxBalance";
		if (request.getParameter("balanceKey") != null) {
			balanceKey = request.getParameter("balanceKey");
		}
		return twoOneEventsService.votingInfo(balanceKey);
	}
	
	@GetMapping(value = "/v2/twoone/voter/{principal}")
	public List<TwoOneEvent> findByVoter(HttpServletRequest request, @PathVariable String principal) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sender_address").is(principal));
		applyFilterYaesNayes(request.getParameter("yaes"), request.getParameter("nayes"), query);
		applyPaging(request);
		query.with(applyPaging(request));
		List<TwoOneEvent> events = mongoTemplate.find(query, TwoOneEvent.class);
		return events;
	}

	@GetMapping(value = "/v2/twoone/votes")
	public List<TwoOneEvent> findVotes(HttpServletRequest request) {
		Query query = new Query();
		query.addCriteria(Criteria.where("stxBalance.locked").gt(0));
		applyFilterYaesNayes(request.getParameter("yaes"), request.getParameter("nayes"), query);
		query.with(applyPaging(request));
		return mongoTemplate.find(query, TwoOneEvent.class);
	}
	
	@GetMapping(value = "/v2/twoone/voting/read-votes")
	public void contractEvents(HttpServletRequest request) {
		int from = FROM_BLOCK;
		int until = UNTIL_BLOCK;
		if (request.getParameter("until_block") != null) {
			until = Integer.valueOf(request.getParameter("until_block"));
		}
		if (request.getParameter("from_block") != null) {
			from = Integer.valueOf(request.getParameter("from_block"));
		}
		readEvents(until, from);
	}

	@GetMapping(value = "/v2/twoone/voting/read-stacking")
	public void stackers(HttpServletRequest request) {
		Boolean nayes = false;
		if (request.getParameter("nayes") != null) {
			nayes = true;
		}
		Boolean yaes = false;
		if (request.getParameter("yaes") != null) {
			yaes = true;
		}
		int until = UNTIL_BLOCK;
		if (request.getParameter("until_block") != null) {
			until = Integer.valueOf(request.getParameter("until_block"));
		}
		if (nayes) {
			saveStxBalanceData(request, until, NO_ADDRESS);
		} else if (yaes) {
			saveStxBalanceData(request, until, YES_ADDRESS);
		} else {
			saveStxBalanceData(request, until, NO_ADDRESS);
			saveStxBalanceData(request, until, YES_ADDRESS);
		}
	}

	@Scheduled(fixedDelay = 1800000) // every 30 mins
	public void readContractEvents() throws JsonProcessingException {
		// readEvents(0l);
	}


	private void readEvents(int until, int from) {
		try {
			twoOneEventsService.getTwoOneVotes(YES_ADDRESS, until);
			twoOneEventsService.getTwoOneVotes(NO_ADDRESS, until);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveStxBalanceData(HttpServletRequest request, int until, String voteAddress) {
		try {
			
			twoOneEventsService.getLockedBalance(applyPaging(request), until, voteAddress);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void applyFilterYaesNayes(String yaes, String nayes, Query query) {
		if (nayes != null) {
			query.addCriteria(Criteria.where("token_transfer.recipient_address").is(NO_ADDRESS));
		}
		if (yaes != null) {
			query.addCriteria(Criteria.where("token_transfer.recipient_address").is(YES_ADDRESS));
		}

	}
	
	private Pageable applyPaging(HttpServletRequest request) {
		Integer page = 0;
		if (request.getParameter("page") != null) {
			page = Integer.valueOf(request.getParameter("page"));
		}
		Integer limit = 1000;
		if (request.getParameter("limit") != null) {
			limit = Integer.valueOf(request.getParameter("limit"));
		}
		final Pageable pageableRequest = PageRequest.of(page, limit, Sort.by("sender_address").ascending());
		return pageableRequest;
	}

}
