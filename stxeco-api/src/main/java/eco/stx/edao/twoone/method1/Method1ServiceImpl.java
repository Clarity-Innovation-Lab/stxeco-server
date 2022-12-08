package eco.stx.edao.twoone.method1;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;
import eco.stx.edao.twoone.method2.StxBalance;
import eco.stx.edao.twoone.method2.TwoOneEventsService;

@Service
public class Method1ServiceImpl implements Method1Service {

    private static final Logger logger = LogManager.getLogger(Method1ServiceImpl.class);
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private Method1Repository method1Repository;
	@Autowired private TwoOneEventsService twoOneEventsService;
	private static final String YES_ADDRESS = "11111111111111X6zHB1ZC2FmtnqJ";
	private static final String NO_ADDRESS = "1111111111111117CrbcZgemVNFx8";
	private static final String BTC_URL = "https://chain.api.btc.com/v3/address/btc_address/tx?pagesize=50";

	@Override
	public void fetchVotes() throws Exception {
		method1Repository.deleteAll();
		makeApiCall(YES_ADDRESS);
		makeApiCall(NO_ADDRESS);
	}
	
	@Override
	public void fetchStackerInfo(int until) throws Exception {
		List<Method1Vote> votes = method1Repository.findAll();
		for (Method1Vote vote : votes) {
			if (vote.getStxAddress() == null) {
				String param = "/btctostx/" + vote.getBtcAddress();
				String stxAddress = apiHelper.cvConversion(param);
				vote.setStxAddress(stxAddress);
			}
			StxBalance bal = twoOneEventsService.fetchStacksBalance(vote.getStxAddress(), until);
			vote.setStxBalance(bal);
			method1Repository.save(vote);
		}
	}
	
	private void makeApiCall(String address) {
		String path = BTC_URL.replaceAll("btc_address", address);
		String json = read(path);
		try {
			Method1Wrapper method1Wrapper = (Method1Wrapper)  mapper.readValue(json, new TypeReference<Method1Wrapper>() {});
			if (method1Wrapper.getData().getList() == null) return;
			for (Method1Vote vote : method1Wrapper.getData().getList()) {
				vote.setBtcAddress(vote.getInputs().get(0).getPrev_addresses()[0]);
				vote.getInputs().get(0).setPrev_addresses(null);
				method1Repository.save(vote);
			}
		} catch (Exception e) {
			logger.error(e);
			return;
		}
	}

	private String read(String path) {
		ApiFetchConfig p = new ApiFetchConfig();
		p.setHttpMethod("GET");
		p.setPath(path);
		try {
			String json = apiHelper.getDirect(p);
			return json;
		} catch (Exception e) {
			return null;
		}
	}

}