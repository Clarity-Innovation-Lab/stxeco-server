package eco.stx.edao.eco.proposals.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import eco.stx.edao.eco.proposals.service.ProposalRepository;
import eco.stx.edao.eco.proposals.service.domain.Proposal;


@RestController
public class ProposalController {

	@Autowired private ProposalRepository proposalRepository;
	@Autowired private ProposalWatcher proposalWatcher;

	@GetMapping(value = "/v2/process-proposals")
	public void proposalRead() throws JsonProcessingException {
 		proposalWatcher.processProposals();
	}

	@GetMapping(value = "/v2/process-proposal-data")
	public void proposalReadData() throws JsonProcessingException {
 		proposalWatcher.processProposalData();
	}

	@GetMapping(value = "/v2/process-proposal-data/{contractId}")
	public void proposalReadData(@PathVariable String contractId) throws JsonProcessingException {
 		proposalWatcher.processProposalData(contractId);
	}

	@GetMapping(value = "/v2/proposals")
	public List<Proposal> fetch() {
		return proposalRepository.findAll();
	}

	@PostMapping(value = "/v2/proposals")
	public Proposal save(@RequestBody Proposal proposal) {
		if (proposal.getId() == null) {
			return proposalRepository.insert(proposal);
		}
		return proposalRepository.save(proposal);
	}

	@DeleteMapping(value = "/v2/proposals/{proposalId}")
	public boolean deletePro(@PathVariable String proposalId) {
		Optional<Proposal> op = proposalRepository.findById(proposalId);
		if (op.isEmpty())
			return false;
		if (op.get().getStatus().equals("draft")) {
			proposalRepository.deleteById(op.get().getId());
		} else {
			return false;
		}
		return true;
	}

	@PutMapping(value = "/v2/proposals/lock")
	public List<Proposal> lock(@RequestBody Proposal proposal) {
		return proposalRepository.findAll();
	}
}
