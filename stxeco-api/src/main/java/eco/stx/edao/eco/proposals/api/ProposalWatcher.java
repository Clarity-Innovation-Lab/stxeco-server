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
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.common.ApiHelper;
import eco.stx.edao.common.PostData;
import eco.stx.edao.common.Principal;
import eco.stx.edao.common.ReadResult;
import eco.stx.edao.eco.proposals.api.model.ProposalContracts;
import eco.stx.edao.eco.proposals.api.model.ProposalTrait;
import eco.stx.edao.eco.proposals.service.ProposalRepository;
import eco.stx.edao.eco.proposals.service.domain.Proposal;
import eco.stx.edao.eco.proposals.service.domain.ProposalContract;
import eco.stx.edao.eco.proposals.service.domain.ProposalData;
import eco.stx.edao.eco.proposals.service.domain.clarity.TypeValue;
import eco.stx.edao.stacks.service.ClarityDeserialiser;

@Configuration
@EnableScheduling
public class ProposalWatcher {

    private static final Logger logger = LogManager.getLogger(ProposalWatcher.class);
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private ProposalRepository proposalRepository;
	@Value("${eco-stx.stax.daojsapi}") String basePath;
	@Autowired private RestOperations restTemplate;
	@Autowired private ClarityDeserialiser clarityDeserialiser;

	@Scheduled(fixedDelay=60000)
	public void processProposals() throws JsonProcessingException {
		// Principal path = new Principal("GET", "/extended/v1/contract/by_trait?trait_abi=" + GitHubHelper.encodeValue(ExtensionTrait.trait), null);
		Principal path = new Principal("GET", "/extended/v1/contract/by_trait?trait_abi={trait}", null);
		String json = apiHelper.fetchFromApi(path, ProposalTrait.trait);
		ProposalContracts contracts = (ProposalContracts)mapper.readValue(json, new TypeReference<ProposalContracts>() {});
		for (ProposalContract pc : contracts.getResults()) {
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
	
	private Proposal merge(ProposalContract pc) {
		Proposal prop = proposalRepository.findByContractId(pc.getContractId());
		if (prop == null) {
			prop = new Proposal();
			prop.setContractId(pc.getContractId());
			prop.setTitle(pc.getContractId().split("\\.")[1]);
			prop.setProposer(pc.getContractId().split("\\.")[0]);
			prop.setStatus("deployed");
		}
		if (prop.getStatus().equals("draft")) prop.setStatus("deployed");
		prop.setProposalContract(pc);
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
		Principal principal = new Principal("POST", path, postd);
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
