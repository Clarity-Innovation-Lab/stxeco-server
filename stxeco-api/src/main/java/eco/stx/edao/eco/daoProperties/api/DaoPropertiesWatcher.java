package eco.stx.edao.eco.daoProperties.api;

import java.util.List;
import java.util.Optional;

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

import eco.stx.edao.eco.daoProperties.api.model.DaoPropertyType;
import eco.stx.edao.eco.daoProperties.service.DaoPropertyRepository;
import eco.stx.edao.eco.daoProperties.service.domain.DaoProperty;
import eco.stx.edao.stacks.ApiHelper;
import eco.stx.edao.stacks.PostData;
import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ReadResult;

@Configuration
@EnableScheduling
public class DaoPropertiesWatcher {

	public static String DP_ID = "dao-properties";
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private DaoPropertyRepository daoPropertyRepository;
	@Value("${eco-stx.stax.daojsapi}") String basePath;
	@Value("${stacks.dao.deployer}") String contractAddress;
	@Autowired private RestOperations restTemplate;
	private static String governanceTokenContract = "ede000-governance-token";
	private static String proposalSubmissionContract = "ede002-threshold-proposal-submission";


	@Scheduled(fixedDelay=60000)
	public void process() throws JsonProcessingException {
		fetchParam(governanceTokenContract, "get-total-supply", null);
		fetchParam(proposalSubmissionContract, "get-parameter", "minimum-proposal-start-delay");
		fetchParam(proposalSubmissionContract, "get-parameter", "maximum-proposal-start-delay");
		fetchParam(proposalSubmissionContract, "get-parameter", "proposal-duration");
		fetchParam(proposalSubmissionContract, "get-parameter", "propose-factor");
	}
	
	private List<DaoProperty> getDP() {
		List<DaoProperty> dp = daoPropertyRepository.findAll();
		return dp;
	}
		
	@Async
	private void fetchParam(String contractName, String functionName, String arg) throws JsonProcessingException {
		try {
			Optional<DaoProperty> dps = Optional.empty();
			if (arg != null) dps = daoPropertyRepository.findById(arg);
			Object value = fetchParameterValue(contractName, functionName, arg);
			DaoProperty dp = null;
			if (dps.isPresent()) {
				dp = dps.get();
				dp.setValue((String)value);
			} else {
				if (arg == null) {
					// if no argument ie dao parameter then use the function name as key
					dp = new DaoProperty(functionName, (String)value, contractName, functionName);
				} else {
					dp = new DaoProperty(arg, (String)value, contractName, functionName);
				}
			}
			daoPropertyRepository.save(dp);
		} catch (Exception e) {
			// parameter maybe unsupported on current dao branch;
		}
 	}
	
	private Object fetchParameterValue(String contractName, String functionName, String arg) throws JsonProcessingException {
		PostData postd = new PostData();
		postd.setArguments(addArgument(functionName, arg));
		postd.setSender(contractAddress);
        String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
		ApiFetchConfig principal = new ApiFetchConfig("POST", path, postd);
		String json = apiHelper.fetchFromApi(principal);
		Object extd = deserialise(functionName, contractAddress, json);
		return extd;
 	}
	
	private String[] addArgument(String functionName, String arg) {
		if (arg == null) return new String[] {};
		else {
			String param = "/string-ascii/" + arg;
			String arg0 = apiHelper.cvConversion(param);
			return new String[] { arg0 };
		}
	}
	
	private Object deserialise(String functionName, String contractId, String json) throws JsonMappingException, JsonProcessingException {
		ReadResult contractRead = (ReadResult)mapper.readValue(json, new TypeReference<ReadResult>() {});
		String param = "/to-json/" + contractRead.getResult();
		json = apiHelper.cvConversion(param);
		DaoPropertyType typeValue = (DaoPropertyType)mapper.readValue(json, new TypeReference<DaoPropertyType>() {});
		if (typeValue.getValue() == null) return null;
		return (Object)typeValue.getValue().getValue();
	}

}
