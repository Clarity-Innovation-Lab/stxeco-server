package eco.stx.edao.stacksmate.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

@RestController
public class StacksMateController {

	@Autowired private RestOperations restTemplate;
	@Value("${eco-stx.stax.stacksmate}") String basePath;

	@GetMapping(value = "/v2/stacksmate/signme/{nonce}")
	public String save(@PathVariable String nonce) {
		String url = basePath + "/stacksmate/signme/" + nonce;
		HttpEntity<String> requestEntity = new HttpEntity<String>(new HttpHeaders());
		ResponseEntity<String> response = null;
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		return response.getBody();
	}
}
