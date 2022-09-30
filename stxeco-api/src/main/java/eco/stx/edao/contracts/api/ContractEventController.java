package eco.stx.edao.contracts.api;

import java.util.List;
import java.util.Optional;

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
	@Autowired private ContractEventsService contractEventsService;
	@Autowired private ContractEventRepository contractEventRepository;
	private static String snapshotVoting = "ede007-snapshot-proposal-voting-v2";

	@GetMapping(value = "/v2/events/count/contract/{contractId}")
	public Optional<Long> countByContract(@PathVariable String contractId) {
		return contractEventRepository.countByContract_id(contractId);
	}
	
	@GetMapping(value = "/v2/events/count/proposal/{contractId}/{proposalId}")
	public Optional<Long> countByContractAndProposal(@PathVariable String contractId, @PathVariable String proposalId) {
		return contractEventRepository.countByContract_idAndProposal(contractId, proposalId);
	}
	
	@GetMapping(value = "/v2/events/count/proposal/{proposalId}")
	public Optional<Long> countByProposal(@PathVariable String proposalId) {
		return contractEventRepository.countByProposal(proposalId);
	}
	
	@GetMapping(value = "/v2/events/contract/{contractId}")
	public List<ContractEvent> findByContract(@PathVariable String contractId) {
		return contractEventRepository.findByContract_id(contractId);
	}
	
	@GetMapping(value = "/v2/events/contract/{contractId}/{proposalId}")
	public List<ContractEvent> findByContractAndProposal(@PathVariable String contractId, @PathVariable String proposalId) {
		return contractEventRepository.findByContract_idAndProposal(contractId, proposalId);
	}
	
	@GetMapping(value = "/v2/events/proposal/{proposalId}")
	public List<ContractEvent> findByProposal(@PathVariable String proposalId) {
		return contractEventRepository.findByProposal(proposalId);
	}
	
	@GetMapping(value = "/v2/events/voter/{voter}")
	public List<ContractEvent> findByVoter(@PathVariable String voter) {
		return contractEventRepository.findByVoter(voter);
	}
	
	@GetMapping(value = "/v2/events/voter/{proposalId}/{voter}")
	public List<ContractEvent> findByProposalAndVoter(@PathVariable String proposalId, @PathVariable String voter) {
		return contractEventRepository.findByProposalAndVoter(proposalId, voter);
	}
	
	@GetMapping(value = "/v2/contract/events")
	public void contractEvents() {
		readEvents();
	}
	
	@Scheduled(fixedDelay=180000) // every 3 mins
	public void readContractEvents() throws JsonProcessingException {
		readEvents();
	}

	private void readEvents() {
		try {
			contractEventsService.consumeContractEvents(contractAddress + "." + snapshotVoting);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
