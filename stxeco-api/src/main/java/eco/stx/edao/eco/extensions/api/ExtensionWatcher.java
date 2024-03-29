package eco.stx.edao.eco.extensions.api;

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
import eco.stx.edao.eco.extensions.api.model.ExtensionTypeValue;
import eco.stx.edao.eco.extensions.service.ExtensionRepository;
import eco.stx.edao.eco.extensions.service.domain.Extension;
import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;
import eco.stx.edao.stacks.PostData;
import eco.stx.edao.stacks.ReadResult;

@Configuration
@EnableScheduling
public class ExtensionWatcher {

    private static final Logger logger = LogManager.getLogger(ExtensionWatcher.class);
	private @Value("${stacks.dao.deployer}") String contractAddress;
	private @Value("${stacks.dao.dao-core-contract}") String coreContractName;
	
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private ExtensionRepository extensionRepository;
	private static String[] EXTENSIONS = new String[] {
			"ede000-governance-token", 
			"ede001-proposal-voting", 
			"ede002-threshold-proposal-submission", 
			"ede003-emergency-proposals", 
			"ede004-emergency-execute", 
			"ede005-dev-fund", 
			"ede006-treasury",
			"ede007-snapshot-proposal-voting-v5", 
			"ede008-funded-proposal-submission-v5",
			"ede009-governance-token-sale"
	};
	
	@Scheduled(fixedDelay=3600000)
	public void processExtensions() throws JsonProcessingException {
		// ApiFetchConfig path = new ApiFetchConfig("GET", "/extended/v1/contract/by_trait?trait_abi=" + PinataHelper.encodeValue(ExtensionTrait.trait), null);
		for (String extension : EXTENSIONS) {
			ApiFetchConfig path = new ApiFetchConfig("GET", "/extended/v1/contract/" + contractAddress + "." + extension, null);
			try {
				String json = apiHelper.fetchFromApi(path);
				Contract contract = (Contract)mapper.readValue(json, new TypeReference<Contract>() {});
				Extension p = merge(contract);
				extensionRepository.save(p);
			} catch (Exception e) {
				// extension not found continue processing
			}
		}
		// https://stacks-node-api.mainnet.stacks.co/extended/v1/contract/{contract_id}
	}
	
	@Scheduled(fixedDelay=3600000)
	public void processExtensionData() throws JsonProcessingException {
		List<Extension> props = extensionRepository.findAll();
		for (Extension p : props) {
			try {
				checkExtensionSubmission(p);
			} catch (Exception e) {
				// logger.error("Error fetching extension data: " + e.getMessage());
			}
		}
	}
	
	public void processExtensionData(String contractId) throws JsonProcessingException {
		Extension p = extensionRepository.findByContractId(contractId);
		try {
			checkExtensionSubmission(p);
		} catch (Exception e) {
			// logger.error("Error fetching extension data: " + e.getMessage());
		}
	}
	
	private Extension merge(Contract pc) {
		Extension extension = extensionRepository.findByContractId(pc.getContractId());
		if (extension == null) {
			extension = new Extension();
		}
		extension.setContractId(pc.getContractId());
		extension.setContract(pc);
		return extension;
	}
	
	@Async
	private void checkExtensionSubmission(Extension ext) throws JsonProcessingException {
		String contractAddress = ext.getContract().getContractId().split("\\.")[0];
		String contractName = coreContractName; // p.getContractId().split("\\.")[1];
		String functionName = "is-extension";
		String param = "/contract-principal/" + contractAddress + "/" + ext.getContract().getContractId().split("\\.")[1];
		String arg0 = apiHelper.cvConversion(param);
		PostData postd = new PostData();
		postd.setArguments(new String[] { arg0 });
		postd.setSender(contractAddress);
        String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
		ApiFetchConfig principal = new ApiFetchConfig("POST", path, postd);
		String json = apiHelper.fetchFromApi(principal);
		boolean valid = deserialise(functionName, contractAddress, json);
		ext.setValid(valid);
		extensionRepository.save(ext);
 	}
	
	private boolean deserialise(String functionName, String contractId, String json) throws JsonMappingException, JsonProcessingException {
		ReadResult contractRead = (ReadResult)mapper.readValue(json, new TypeReference<ReadResult>() {});
		if (contractRead.getResult() == null) return false;
		String param = "/to-json/" + contractRead.getResult();
		json = apiHelper.cvConversion(param);
		ExtensionTypeValue typeValue = (ExtensionTypeValue)mapper.readValue(json, new TypeReference<ExtensionTypeValue>() {});
		return typeValue.getValue();
	}

}
