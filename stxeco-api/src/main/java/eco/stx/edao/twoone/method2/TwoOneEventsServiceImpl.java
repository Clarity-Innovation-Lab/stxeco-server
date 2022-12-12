package eco.stx.edao.twoone.method2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.contracts.service.domain.TokenTransfer;
import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;

@Service
public class TwoOneEventsServiceImpl implements TwoOneEventsService {

    private static final Logger logger = LogManager.getLogger(TwoOneEventsServiceImpl.class);
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private TwoOneEventRepository twoOneEventRepository;
	@Autowired private MongoTemplate mongoTemplate;
	private static final String YES_ADDRESS = "SP00000000000003SCNSJTCHE66N2PXHX";
	private static final String NO_ADDRESS = "SP00000000000000DSQJTCHE66XE1NHQ";
	private static final int CYCLE_46_START = 82914;
	private static final int CYCLE_46_END = 85014;
	private static final int CYCLE_47_START = 85014;
	private static final int CYCLE_47_END = 87114;

	@Override
	public StxBalance fetchStacksBalance(String principal, long until) {
		String path = "/extended/v1/address/" + principal + "/stx?until_block=ub123";
		path = path.replaceAll("ub123", String.valueOf(until));
		String json = read(path);
		try {
			StxBalance stxBalance = (StxBalance)  mapper.readValue(json, new TypeReference<StxBalance>() {});
			stxBalance.setQuery_height(Long.valueOf(until));
			return stxBalance;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void getTwoOneVotes(String principal, int until) throws JsonMappingException, JsonProcessingException {
		String path = "/extended/v1/address/" + principal + "/transactions?limit=50&until_block=ub123&offset=";
		path = path.replaceAll("ub123", String.valueOf(until));
		boolean read = true;
		TwoOneEvents events = null;
		int offset = 0;
		while (read) {
			String json = read(path + offset);
			try {
				events = (TwoOneEvents)  mapper.readValue(json, new TypeReference<TwoOneEvents>() {});
				if (events.getResults() != null && events.getResults().size() > 0) {
					for (TwoOneEvent ce : events.getResults()) {
						twoOneEventRepository.save(ce);
					}
					offset += 50;
				} else {
					read = false;
				}
			} catch (Exception e) {
				read = false;
			}
		}
	}
	
	@Override
	@Async
	public void getLockedBalance(Pageable pageable, int until, String voteAddress) throws JsonMappingException, JsonProcessingException, InterruptedException {
		Query query = new Query();
		//query.addCriteria(Criteria.where("token_transfer.recipient_address").is(voteAddress));
		query.with(pageable);
		
		List<TwoOneEvent> events = mongoTemplate.find(query, TwoOneEvent.class);
		for (TwoOneEvent event : events) {
			Thread.sleep(1000);
			if (until == CYCLE_46_START && event.getStxBalance1() == null) {
				event.setStxBalance1(fetchStacksBalance(event.getSender_address(), until));
			} else if (until == CYCLE_46_END && event.getStxBalance2() == null) {
				event.setStxBalance2(fetchStacksBalance(event.getSender_address(), until));
			} else if (until == CYCLE_47_START && event.getStxBalance3() == null) {
				event.setStxBalance3(fetchStacksBalance(event.getSender_address(), until));
			} else if (until == CYCLE_47_END && event.getStxBalance4() == null) {
				event.setStxBalance4(fetchStacksBalance(event.getSender_address(), until));
			} else {
				event.setStxBalance(fetchStacksBalance(event.getSender_address(), event.getBlock_height()));
			}
			twoOneEventRepository.save(event);
		}

	}
	
	@Override
	public Method2VoteCount votingInfo(String balanceKey) {
		int numbTransactions = twoOneEventRepository.findAll().size();
		int numbVotesFor = 0;
		int numbVotesAgainst = 0;
		int numbUniqueVotesAllPerAddress = 0;
		long earlyVotes = 0;
		long countFor = 0;
		long countAgainst = 0;
		long countForAllPerAddress = 0;
		long countAgainstAllPerAddress = 0;
		Method2VoteCount vd = new Method2VoteCount();
		Map<String, List<TwoOneEvent>> map = listUnique(balanceKey);
		
		Set<String> mapkeys = map.keySet();
		for (String key : mapkeys) {
			List<TwoOneEvent> events = map.get(key);
			boolean counted = false;
			for (TwoOneEvent votePerAddress : events) {
				if (votePerAddress.getStxBalance().getLocked() > 0) {
					vd.setLockedAtHeight(votePerAddress.getStxBalance().getQuery_height());
					if (votePerAddress.getBlock_height() < CYCLE_46_START || votePerAddress.getBlock_height() > CYCLE_47_END) {
						earlyVotes++;
					} else {
						if (!counted) {
							if (votePerAddress.getToken_transfer().getRecipient_address().equals(YES_ADDRESS)) {
								numbVotesFor++;
								countFor += votePerAddress.getStxBalance().getLocked();
							} else {
								numbVotesAgainst++;
								countAgainst += votePerAddress.getStxBalance().getLocked();
							}
						}
						numbUniqueVotesAllPerAddress++;
						if (votePerAddress.getToken_transfer().getRecipient_address().equals(YES_ADDRESS)) {
							countForAllPerAddress += votePerAddress.getStxBalance().getLocked();
						} else {
							countAgainstAllPerAddress += votePerAddress.getStxBalance().getLocked();
						}
						counted = true;
					}
				}
			}
		}
		
		vd.setEarlyVotes(earlyVotes);
		vd.setCountAgainst(countAgainst);
		vd.setCountAgainstIncludingDuplicates(countAgainstAllPerAddress);
		vd.setCountFor(countFor);
		vd.setCountForIncludingDuplicates(countForAllPerAddress);
		vd.setNumbVotesFor(numbVotesFor);
		vd.setNumbVotesAgainst(numbVotesAgainst);
		vd.setNumbUniqueVotesIncludingDuplicates(numbUniqueVotesAllPerAddress);
		vd.setNumbTransactions(numbTransactions);
		vd.setVotes(map);
		return vd;
	}
	
	private TwoOneEvent convertDocument(Document d, String balanceKey) {
		TwoOneEvent t = new TwoOneEvent();
		t.setTx_status(d.getString("tx_status"));
		t.setTx_id(d.getString("tx_id"));
		t.setSender_address(d.getString("sender_address"));
		t.setBlock_height(d.getLong("block_height"));
		Document ttD = (Document) d.get("token_transfer");
		TokenTransfer tt = new TokenTransfer();
		tt.setAmount(ttD.getString("amount"));
		tt.setRecipient_address(ttD.getString("recipient_address"));
		StxBalance sb = new StxBalance();
		Document ttS = (Document) d.get(balanceKey);
		sb.setQuery_height(ttS.getLong("query_height"));
		sb.setBalance(ttS.getLong("balance"));
		sb.setLocked(ttS.getLong("locked"));
		sb.setLock_height(ttS.getLong("lock_height"));
		t.setStxBalance(sb);
		t.setToken_transfer(tt);
		return t;
	}

	
	private String read(String path) {
		ApiFetchConfig p = new ApiFetchConfig();
		p.setHttpMethod("GET");
		p.setPath(path);
		try {
			String json = apiHelper.fetchFromApi(p);
			return json;
		} catch (Exception e) {
			return null;
		}
	}

	private Map<String, List<TwoOneEvent>> listUnique(String balanceKey) {
		
		TypedAggregation<TwoOneEvent> myAggregation = Aggregation.newAggregation(TwoOneEvent.class,
	               Aggregation.group("sender_address").
	               push("$$ROOT").as("events"));
		 
		AggregationResults<TwoOneEvent> results2 = mongoTemplate.aggregate(myAggregation, TwoOneEvent.class);

		Document votes = results2.getRawResults();
		return convert(votes, balanceKey);

	}
	
	private Map<String, List<TwoOneEvent>> convert(Document votes, String balanceKey) {
		Map<String, List<TwoOneEvent>> map = new HashMap<String, List<TwoOneEvent>>();
		Set<String> keys = votes.keySet();
		for (String key : keys) {
			Set<Entry<String, Object>> entries = votes.entrySet();
			for (Entry<String, Object> e : entries) {
				//Document eventsD = (Document)e.getValue();
				try {
					for (Document d : (List<Document>)e.getValue()) {
						Set<Entry<String, Object>> l2Entries = d.entrySet();
						String stxAddress = null;
						List<TwoOneEvent> twoOnes = new ArrayList<TwoOneEvent>();
						// Grouping returns 2 entries
						for (Entry<String, Object> l2Entry : l2Entries) {
							String l2Key = l2Entry.getKey();
							if (l2Key.equals("_id")) {
								stxAddress = (String)l2Entry.getValue();
							} else if (l2Key.equals("events")) {
								for (Document twoOneDoc : (List<Document>)l2Entry.getValue()) {
									twoOnes.add(convertDocument(twoOneDoc, balanceKey));
								}
							}
						}
						map.put(stxAddress, twoOnes);
					}
				} catch (Exception e1) {
					logger.info(key + " : " + e1.getMessage());
				}
			}
		}
		return map;
	}

}