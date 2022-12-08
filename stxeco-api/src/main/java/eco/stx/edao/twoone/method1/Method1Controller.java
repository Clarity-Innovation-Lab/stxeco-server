package eco.stx.edao.twoone.method1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAsync
@EnableScheduling
public class Method1Controller {

	@Autowired private Method1Service method1Service;
	@Autowired private Method1Repository method1Repository;
	private static final int UNTIL_BLOCK = 82914;

	@GetMapping(value = "/v2/twoone/method1/votes")
	public List<Method1Vote> findAll() {
		return method1Repository.findAll();
	}

	@GetMapping(value = "/v2/twoone/method1/read-votes")
	public void readVotes() throws Exception {
		method1Service.fetchVotes();
	}

	@GetMapping(value = "/v2/twoone/method1/read-stacks")
	public void readStacks(HttpServletRequest request) throws Exception {
		int until = UNTIL_BLOCK;
		if (request.getParameter("until_block") != null) {
			until = Integer.valueOf(request.getParameter("until_block"));
		}
		method1Service.fetchStackerInfo(until);
	}


}
