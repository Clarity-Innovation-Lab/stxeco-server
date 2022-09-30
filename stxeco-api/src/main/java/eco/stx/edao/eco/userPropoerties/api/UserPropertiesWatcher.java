package eco.stx.edao.eco.userPropoerties.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.eco.daoProperties.api.model.DaoPropertyType;
import eco.stx.edao.eco.daoProperties.api.model.DaoPropertyTypeValue;
import eco.stx.edao.eco.daoProperties.service.DaoPropertyRepository;
import eco.stx.edao.eco.daoProperties.service.domain.DaoProperty;
import eco.stx.edao.eco.userPropoerties.service.UserPropertyRepository;
import eco.stx.edao.eco.userPropoerties.service.domain.UserProperty;
import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;
import eco.stx.edao.stacks.PostData;
import eco.stx.edao.stacks.ReadResult;

@Configuration
@EnableScheduling
public class UserPropertiesWatcher {

	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private DaoPropertyRepository daoPropertyRepository;
	@Autowired private UserPropertyRepository userPropertyRepository;
	@Value("${stacks.dao.deployer}") String contractAddress;
	private static String governanceTokenContract = "ede000-governance-token";
	private static String emergencyProposals = "ede003-emergency-proposals";
	private static String emergencyExecute = "ede004-emergency-execute";

	//@Scheduled(fixedDelay=60000)
	public List<UserProperty> process(String stxAddress) throws JsonProcessingException {
		List<UserProperty> upm = new ArrayList<UserProperty>();
		fetchParam(upm, governanceTokenContract, "edg-get-total-delegated", new String[] {stxAddress + "::principal" });
		fetchParam(upm, governanceTokenContract, "edg-get-locked", new String[] {stxAddress + "::principal"});
		fetchParam(upm, governanceTokenContract, "edg-get-balance", new String[] {stxAddress + "::principal"});
		fetchParam(upm, governanceTokenContract, "edg-get-balance", new String[] {stxAddress + "::principal"});
		fetchParam(upm, emergencyProposals, "is-emergency-team-member", new String[] {stxAddress + "::principal"});
		fetchParam(upm, emergencyExecute, "is-executive-team-member", new String[] {stxAddress + "::principal"});
		Optional<DaoProperty> dp = daoPropertyRepository.findById("propose-factor");
		if (dp.isPresent()) {
			fetchParam(upm, governanceTokenContract, "edg-has-percentage-balance", new String[] {stxAddress + "::principal", dp.get().getValue() + "::uint"});
		}
		return upm;
	}
	
	// @Async
	private UserProperty fetchParam(List<UserProperty> upm, String contractName, String functionName, String[] args) throws JsonProcessingException {
		try {
			Optional<UserProperty> dps = Optional.empty();
			dps = userPropertyRepository.findByStxAddressAndFunctionName(args[0].split("::")[0], functionName);
			String[] serArgs = serialiseArguments(args);
			DaoPropertyTypeValue value = fetchParameterValue(contractName, functionName, serArgs);
			UserProperty dp = null;
			if (dps.isPresent()) {
				dp = dps.get();
				dp.setValue(value);
			} else {
				dp = new UserProperty();
				dp.setContractName(contractName);
				dp.setFunctionName(functionName);
				dp.setStxAddress(args[0].split("::")[0]);
				dp.setValue(value);
			}
			dp = userPropertyRepository.save(dp);
			upm.add(dp);
			return dp;
		} catch (Exception e) {
			// parameter maybe unsupported on current dao branch;
			return null;
		}
 	}
	
	private DaoPropertyTypeValue fetchParameterValue(String contractName, String functionName, String[] serialisedArgs) throws JsonProcessingException {
		PostData postd = new PostData();
		postd.setArguments(serialisedArgs);
		postd.setSender(contractAddress);
        String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
		ApiFetchConfig principal = new ApiFetchConfig("POST", path, postd);
		String json = apiHelper.fetchFromApi(principal);
		DaoPropertyTypeValue extd = deserialise(functionName, contractAddress, json);
		return extd;
 	}
	
	private String[] serialiseArguments(String[] args) {
		if (args == null || args.length == 0) return new String[] {};
		else {
			String[] serArgs = new String[args.length];
			int counter = 0;
			for (String arg : args) {
				String argument = arg.split("::")[0];
				String type = "/" + arg.split("::")[1] + "/";
				String param = type + argument;
				serArgs[counter] = apiHelper.cvConversion(param);
				counter++;
			}
			return serArgs;
		}
	}
	
	private DaoPropertyTypeValue deserialise(String functionName, String contractId, String json) throws JsonMappingException, JsonProcessingException {
		ReadResult contractRead = (ReadResult)mapper.readValue(json, new TypeReference<ReadResult>() {});
		if (contractRead.getResult() == null) return null;
		String param = "/to-json/" + contractRead.getResult();
		json = apiHelper.cvConversion(param);
		if (json.indexOf("response") > -1) {
			DaoPropertyType typeValue = (DaoPropertyType)mapper.readValue(json, new TypeReference<DaoPropertyType>() {});
			return typeValue.getValue();
		} else {
			DaoPropertyTypeValue typeValue = (DaoPropertyTypeValue)mapper.readValue(json, new TypeReference<DaoPropertyTypeValue>() {});
			return typeValue;
		}
	}

}
