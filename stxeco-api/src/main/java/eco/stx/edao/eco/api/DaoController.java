package eco.stx.edao.eco.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import eco.stx.edao.eco.daoProperties.service.DaoPropertyRepository;
import eco.stx.edao.eco.extensions.service.ExtensionRepository;
import eco.stx.edao.eco.proposals.service.ProposalRepository;
import eco.stx.edao.eco.userPropoerties.api.UserPropertiesWatcher;
import eco.stx.edao.eco.userPropoerties.service.UserPropertyRepository;


@RestController
public class DaoController {

	@Autowired private ProposalRepository proposalRepository;
	@Autowired private ExtensionRepository extensionRepository;
	@Autowired private DaoPropertyRepository daoPropertyRepository;
	@Autowired private UserPropertyRepository userPropertyRepository;
	@Autowired private UserPropertiesWatcher userPropertiesWatcher;

	@GetMapping(value = "/v2/dao-data")
	public Map<String, Object> initUI() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("proposals", proposalRepository.findAll());
		map.put("extensions", extensionRepository.findAll());
		map.put("daoProperties", daoPropertyRepository.findAll());
		return map;
	}

	@GetMapping(value = "/v2/dao-data/{stxAddress}")
	public Map<String, Object> initUI(@PathVariable String stxAddress) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("proposals", proposalRepository.findAll());
		map.put("extensions", extensionRepository.findAll());
		map.put("daoProperties", daoPropertyRepository.findAll());
		map.put("userProperties", userPropertiesWatcher.process(stxAddress));
		return map;
	}

}
