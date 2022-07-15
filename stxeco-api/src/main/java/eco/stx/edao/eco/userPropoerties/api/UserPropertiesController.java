package eco.stx.edao.eco.userPropoerties.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import eco.stx.edao.eco.userPropoerties.service.UserPropertyRepository;
import eco.stx.edao.eco.userPropoerties.service.domain.UserProperty;


@RestController
public class UserPropertiesController {

	@Autowired private UserPropertyRepository userPropertyRepository;
	@Autowired private UserPropertiesWatcher userPropertiesWatcher;

	@GetMapping(value = "/v2/process-user-properties/{stxAddress}")
	public List<UserProperty> process(@PathVariable String stxAddress) throws JsonProcessingException {
		return userPropertiesWatcher.process(stxAddress);
	}

	@GetMapping(value = "/v2/user-properties/{stxAddress}")
	public List<UserProperty> fetch(@PathVariable String stxAddress) {
		List<UserProperty> dpu = userPropertyRepository.findByStxAddress(stxAddress);
		return dpu;
	}
}
