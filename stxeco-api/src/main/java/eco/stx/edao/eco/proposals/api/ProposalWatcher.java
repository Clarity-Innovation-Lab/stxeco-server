package eco.stx.edao.eco.proposals.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.eco.api.model.Contract;
import eco.stx.edao.eco.proposals.api.model.ProposalContracts;
import eco.stx.edao.eco.proposals.api.model.ProposalTrait;
import eco.stx.edao.eco.proposals.service.ProposalRepository;
import eco.stx.edao.eco.proposals.service.domain.Proposal;
import eco.stx.edao.eco.proposals.service.domain.ProposalData;
import eco.stx.edao.eco.proposals.service.domain.clarity.TypeValue;
import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;
import eco.stx.edao.stacks.PostData;
import eco.stx.edao.stacks.ReadResult;
import eco.stx.edao.stacks.model.transactions.TransactionFromApiBean;

@Configuration
@EnableScheduling
public class ProposalWatcher {

    private static final Logger logger = LogManager.getLogger(ProposalWatcher.class);
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private ProposalRepository proposalRepository;
	@Value("${eco-stx.stax.daojsapi}") String basePath;

	@Scheduled(fixedDelay=60000)
	public void processSubmissions() throws JsonProcessingException {
		List<Proposal> proposals = proposalRepository.findByStatus("deploying");
		checkTransactionStatus(proposals, "deployed");
		proposals = proposalRepository.findByStatus("submitting");
		checkTransactionStatus(proposals, "submitted");
	}
	
	private void checkTransactionStatus(List<Proposal> proposals, String newStatus) throws JsonProcessingException {
		if (proposals == null || proposals.isEmpty()) return;
		for (Proposal proposal : proposals) {
			String txId = proposal.getDeployTxId();
			if (newStatus.equals("submitted")) txId = proposal.getSubmitTxId();
			if (txId == null) {
				proposal.setStatus("draft");
			} else {
				try {
					TransactionFromApiBean tx = apiHelper.fetchTransaction(txId);
					if (tx.getTx_status().equals("pending")) {
						// do nothing
					} else if (tx.getTx_status().equals("success")) {
						proposal.setStatus(newStatus);
					} else {
						proposal.setStatus("draft");
					}
				} catch (Exception e) {
					// devnet txs come and go - shouldnt happen on mainnet
					proposal.setStatus("draft");
				}
			}
			proposalRepository.save(proposal);
		}
	}
	
	@Scheduled(fixedDelay=3600000)
	public void processProposals() throws JsonProcessingException {
		// ApiFetchConfig path = new ApiFetchConfig("GET", "/extended/v1/contract/by_trait?trait_abi=" + GitHubHelper.encodeValue(ExtensionTrait.trait), null);
		ApiFetchConfig path = new ApiFetchConfig("GET", "/extended/v1/contract/by_trait?trait_abi={trait}", null);
		String json = apiHelper.fetchFromApi(path, ProposalTrait.trait);
		ProposalContracts contracts = (ProposalContracts)mapper.readValue(json, new TypeReference<ProposalContracts>() {});
		for (Contract pc : contracts.getResults()) {
			Proposal p = merge(pc);
			proposalRepository.save(p);
		}
	}
	
	@Scheduled(fixedDelay=90000)
	public void processProposalData() throws JsonProcessingException {
		List<Proposal> props = proposalRepository.findAll();
		for (Proposal p : props) {
			try {
				checkProposalSubmission(p);
			} catch (Exception e) {
				logger.error("Error fetching proposal data: " + e.getMessage());
			}
		}
	}
	
	public void processProposalData(String contractId) throws JsonProcessingException {
		Proposal p = proposalRepository.findByContractId(contractId);
		try {
			checkProposalSubmission(p);
		} catch (Exception e) {
			logger.error("Error fetching proposal data: " + e.getMessage());
		}
	}
	
	private Proposal merge(Contract pc) {
		Proposal prop = proposalRepository.findByContractId(pc.getContractId());
		if (prop == null) {
			prop = new Proposal();
			prop.setContractId(pc.getContractId());
			prop.setTitle(pc.getContractId().split("\\.")[1]);
			prop.setProposer(pc.getContractId().split("\\.")[0]);
			prop.setStatus("deployed");
		}
		if (prop.getStatus().equals("draft")) prop.setStatus("deployed");
		prop.setContract(pc);
		return prop;
	}
	
	@Async
	private void checkProposalSubmission(Proposal p) throws JsonProcessingException {
		String contractAddress = p.getContractId().split("\\.")[0];
		String contractName = "ede001-proposal-voting"; // p.getContractId().split("\\.")[1];
		String functionName = "get-proposal-data";
		String param = "/contract-principal/" + contractAddress + "/" + p.getContractId().split("\\.")[1];
		String arg0 = apiHelper.cvConversion(param);
		logger.info("Extension argument: " + arg0);

		PostData postd = new PostData();
		postd.setArguments(new String[] { arg0 });
		postd.setSender(contractAddress);
        String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
		ApiFetchConfig principal = new ApiFetchConfig("POST", path, postd);
		String json = apiHelper.fetchFromApi(principal);
		ProposalData pd = deserialise(functionName, contractAddress, json);
		if (pd != null) {
			p.setProposalData(pd);
			proposalRepository.save(p);
		}
 	}
		
	private ProposalData deserialise(String functionName, String contractId, String json) throws JsonMappingException, JsonProcessingException {
		ReadResult contractRead = (ReadResult)mapper.readValue(json, new TypeReference<ReadResult>() {});
		String param = "/to-json/" + contractRead.getResult();
		json = apiHelper.cvConversion(param);
		TypeValue typeValue = (TypeValue)mapper.readValue(json, new TypeReference<TypeValue>() {});
		if (typeValue.getValue() == null) return null;
		return ProposalData.fromClarity(typeValue.getValue().getValue());
	}

}
