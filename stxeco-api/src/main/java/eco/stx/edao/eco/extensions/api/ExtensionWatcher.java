package eco.stx.edao.eco.extensions.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.common.ApiHelper;
import eco.stx.edao.common.PostData;
import eco.stx.edao.common.Principal;
import eco.stx.edao.common.ReadResult;
import eco.stx.edao.eco.extensions.api.model.ExtensionContracts;
import eco.stx.edao.eco.extensions.api.model.ExtensionTrait;
import eco.stx.edao.eco.extensions.service.ExtensionRepository;
import eco.stx.edao.eco.extensions.service.domain.Extension;
import eco.stx.edao.eco.extensions.service.domain.ExtensionContract;
import eco.stx.edao.eco.extensions.service.domain.ExtensionData;
import eco.stx.edao.eco.proposals.service.domain.clarity.TypeValue;
import eco.stx.edao.stacks.service.ClarityDeserialiser;

@Configuration
@EnableScheduling
public class ExtensionWatcher {

    private static final Logger logger = LogManager.getLogger(ExtensionWatcher.class);
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private ExtensionRepository extensionRepository;
	@Value("${eco-stx.stax.stacksmate}") String basePath;
	@Autowired private RestOperations restTemplate;
	@Autowired private ClarityDeserialiser clarityDeserialiser;

	//@Scheduled(fixedDelay=60000)
	public void processExtensions() throws JsonProcessingException {
		// Principal path = new Principal("GET", "/extended/v1/contract/by_trait?trait_abi=" + GitHubHelper.encodeValue(ExtensionTrait.trait), null);
		Principal path = new Principal("GET", "/extended/v1/contract/by_trait?trait_abi={trait}", null);
		String json = apiHelper.fetchFromApi(path, ExtensionTrait.trait);
		ExtensionContracts contracts = (ExtensionContracts)mapper.readValue(json, new TypeReference<ExtensionContracts>() {});
		for (ExtensionContract pc : contracts.getResults()) {
			Extension p = merge(pc);
			extensionRepository.save(p);
		}
	}
	
	//@Scheduled(fixedDelay=90000)
	public void processExtensionData() throws JsonProcessingException {
		List<Extension> props = extensionRepository.findAll();
		for (Extension p : props) {
			try {
				checkExtensionSubmission(p);
			} catch (Exception e) {
				logger.error("Error fetching extension data: " + e.getMessage());
			}
		}
	}
	
	public void processExtensionData(String contractId) throws JsonProcessingException {
		Extension p = extensionRepository.findByContractId(contractId);
		try {
			checkExtensionSubmission(p);
		} catch (Exception e) {
			logger.error("Error fetching extension data: " + e.getMessage());
		}
	}
	
	private Extension merge(ExtensionContract pc) {
		Extension prop = extensionRepository.findByContractId(pc.getContractId());
		if (prop == null) {
			prop = new Extension();
		}
		prop.setExtensionContract(pc);
		return prop;
	}
	
	@Async
	private void checkExtensionSubmission(Extension p) throws JsonProcessingException {
		String contractAddress = p.getContractId().split("\\.")[0];
		String contractName = "ede001-extension-voting"; // p.getContractId().split("\\.")[1];
		String functionName = "get-extension-data";
		List<String> functionArgs = new ArrayList<String>();
		String param = "/extension-data/" + contractAddress + "/" + p.getContractId().split("\\.")[1];
		String arg0 = cvConversion(param);
		logger.info("Extension argument: " + arg0);
		functionArgs.add(arg0);

		PostData postd = new PostData();
		postd.setArguments(new String[] { arg0 });
		postd.setSender(contractAddress);
        String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
		Principal principal = new Principal("POST", path, postd);
		String json = apiHelper.fetchFromApi(principal);
		ExtensionData pd = deserialise(functionName, contractAddress, json);
		if (pd != null) {
			p.setExtensionData(pd);
			extensionRepository.save(p);
		}
 	}
	
	private String cvConversion(String param) {
		String url = basePath + "/stacksmate" + param;
		HttpEntity<String> requestEntity = new HttpEntity<String>(new HttpHeaders());
		ResponseEntity<String> response = null;
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		return response.getBody();
	}
	
	private ExtensionData deserialise(String functionName, String contractId, String json) throws JsonMappingException, JsonProcessingException {
		ReadResult contractRead = (ReadResult)mapper.readValue(json, new TypeReference<ReadResult>() {});
		String param = "/to-json/" + contractRead.getResult();
		json = cvConversion(param);
		TypeValue typeValue = (TypeValue)mapper.readValue(json, new TypeReference<TypeValue>() {});
		if (typeValue.getValue() == null) return null;
		//ClarityExtensionData extensionData = (ClarityExtensionData)mapper.readValue(typeValue.getValue(), new TypeReference<ClarityExtensionData>() {});
		return ExtensionData.fromClarity(typeValue.getValue().getValue());
//		ClarityExtensionData p = null;
//		try {
//			Map<String, Object> data = clarityDeserialiser.deserialise("get-extension-data", json);
//			if (data != null) {
//				Map<String, Object> data1 = (Map) data.get(functionName);
//				if (data1 != null) {
//					p = ClarityExtensionData.fromMap((Map) data.get(functionName), contractId);
//				}
//			}
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return p;
	}

}
