package eco.stx.edao.contracts.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eco.stx.edao.contracts.service.ContractEventRepository;
import eco.stx.edao.contracts.service.ContractEventsService;
import eco.stx.edao.contracts.service.domain.ContractEvent;

@RestController
@EnableAsync
@EnableScheduling
public class ContractEventController {
	
	private @Value("${stacks.dao.deployer}") String contractAddress;
	private static List<String> contracts = new ArrayList<String>();
	@Autowired private ContractEventsService contractEventsService;
	@Autowired private ContractEventRepository contractsRepository;
	
	static {
		contracts.add("ede000-governance-token");
		contracts.add("ede001-proposal-voting");
	}

	
	@GetMapping(value = "/v2/contract/events/{contractId}")
	public List<ContractEvent> contractEvents(@PathVariable String contractId) {
		return contractsRepository.findByContractId(contractId);
	}
	
	@GetMapping(value = "/v2/contract/consume-events")
	public void contractEvents() {
		readEvents();
	}
	
	@Scheduled(fixedDelay=3600000)
	public void readContractEvents() throws JsonProcessingException {
		readEvents();
	}

	private void readEvents() {
		try {
			contractEventsService.consumeContractEvents(contractAddress + "." + contracts.get(0));
			contractEventsService.consumeContractEvents(contractAddress + "." + contracts.get(1));
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
