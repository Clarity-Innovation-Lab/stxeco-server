package eco.stx.edao.eco.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;

@RestController
@EnableAsync
@EnableScheduling
public class StacksApiController {

	private static final Logger logger = LogManager.getLogger(StacksApiController.class);
	@Autowired ApiHelper apiHelper;

	/**
	 * Read stuff from the API
	 * Use localhost if available with fallback to Hiro nodes
	 * Use port 3999 if path begins /extended and 20443 if /v2
	 */
	@PostMapping(value = "/v2/accounts")
	public String accounts(HttpServletRequest request, @RequestBody ApiFetchConfig principal)  throws JsonMappingException, JsonProcessingException {
		String json = apiHelper.fetchFromApi(principal);
		return json;
	}

}
