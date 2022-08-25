package eco.stx.edao.stacks;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.eco.proposals.service.domain.ProposalData;
import eco.stx.edao.eco.proposals.service.domain.clarity.TypeValue;
import eco.stx.edao.stacks.model.transactions.TransactionFromApiBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias(value = "GitHubHelper")
public class ApiHelper {

	@Autowired private ObjectMapper mapper;
	@Autowired private RestOperations restTemplate;
	@Value("${spring.profiles.active}") private String activeProfile;
	@Value("${eco-stx.stax.stacks-path-primary}") String stacksPathPrimary;
	@Value("${eco-stx.stax.stacks-path-secondary}") String stacksPathSecondary;
	@Value("${eco-stx.stax.daojsapi}") String daojsapi;
	private static final String port1 = ":20443";
	private static final String port2 = ":3999";

	public static String encodeValue(String value) {
	    try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	public String cvConversion(String param) {
		String url = daojsapi + "/daojsapi" + param;
		HttpEntity<String> requestEntity = new HttpEntity<String>(new HttpHeaders());
		ResponseEntity<String> response = null;
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		return response.getBody();
	}
	
	public TransactionFromApiBean fetchTransaction(String txId) throws JsonProcessingException {
		TransactionFromApiBean pox = null;
		if (txId != null) {
			ApiFetchConfig path = new ApiFetchConfig("GET", "/extended/v1/tx/" + txId, null);
			String json = fetchFromApi(path);
			pox = (TransactionFromApiBean)mapper.readValue(json, new TypeReference<TransactionFromApiBean>() {});
		}
		return pox;
	}

	public ProposalData deserialise(String functionName, String contractId, String json) throws JsonMappingException, JsonProcessingException {
		ReadResult contractRead = (ReadResult)mapper.readValue(json, new TypeReference<ReadResult>() {});
		String param = "/to-json/" + contractRead.getResult();
		json = cvConversion(param);
		TypeValue typeValue = (TypeValue)mapper.readValue(json, new TypeReference<TypeValue>() {});
		if (typeValue.getValue() == null) return null;
		//DaoProperties proposalData = (DaoProperties)mapper.readValue(typeValue.getValue(), new TypeReference<DaoProperties>() {});
		return ProposalData.fromClarity(typeValue.getValue().getValue());
//		DaoProperties p = null;
//		try {
//			Map<String, Object> data = clarityDeserialiser.deserialise("get-proposal-data", json);
//			if (data != null) {
//				Map<String, Object> data1 = (Map) data.get(functionName);
//				if (data1 != null) {
//					p = DaoProperties.fromMap((Map) data.get(functionName), contractId);
//				}
//			}
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return p;
	}

	public String fetchFromApi(ApiFetchConfig principal) throws JsonProcessingException {
		ResponseEntity<String> response = null;
		String url = getUrl(principal, true);
		try {
			if (principal.getHttpMethod() != null && principal.getHttpMethod().equalsIgnoreCase("POST")) {
				response = restTemplate.exchange(url, HttpMethod.POST, getRequestEntity(principal), String.class);
			} else {
				response = restTemplate.exchange(url, HttpMethod.GET, getRequestEntity(principal), String.class);
			}
		} catch (Exception e) {
			response = getRespFromStacks(principal);
		}
		return response.getBody();
	}
	
	public String fetchFromApi(ApiFetchConfig principal, String data) throws JsonProcessingException {
		ResponseEntity<String> response = null;
		String url = getUrl(principal, true);
		try {
			return restTemplate.getForObject(url, String.class, data);
		} catch (Exception e) {
			url = stacksPathSecondary + principal.getPath();
			return restTemplate.getForObject(url, String.class, data);
		}
	}
	
	private ResponseEntity<String> getRespFromStacks(ApiFetchConfig principal) {
		String url = stacksPathSecondary + principal.getPath();
		if (principal.getHttpMethod() != null && principal.getHttpMethod().equalsIgnoreCase("POST")) {
			return restTemplate.exchange(url, HttpMethod.POST, getRequestEntity(principal), String.class);
		} else {
			return restTemplate.exchange(url, HttpMethod.GET, getRequestEntity(principal), String.class);
		}
	}
	
	private String getUrl(ApiFetchConfig principal, boolean local) {
		String url = null;
		if (principal.getPath().indexOf("/extended/v1") > -1) {
			url = stacksPathPrimary + port2 + principal.getPath();
		} else {
			url = stacksPathPrimary + port1 + principal.getPath();
		}
		return url;
	}

	private HttpEntity<String> getRequestEntity(ApiFetchConfig principal) {
		try {
			if (principal.getHttpMethod() == null || !principal.getHttpMethod().equalsIgnoreCase("POST")) {
				return new HttpEntity<String>(new HttpHeaders());
			} else {
				String jsonInString = convertMessage(principal.getPostData());
				return new HttpEntity<String>(jsonInString, getHeaders());
			}
		} catch (Exception e) {
			return null;
		}
	}

	private String convertMessage(Object model) {
		try {
			return mapper.writeValueAsString(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private HttpHeaders getHeaders() {
//		String val = " "; // environment.getProperty("BTC_ACCESS_KEY_ID");
//		String auth = "BTC_ACCESS_KEY_ID" + ":" + val;
//		String encodedAuth = new String(Base64.getEncoder().encode(auth.getBytes(Charset.forName("UTF8"))));
// 		headers.set("Authorization", "Basic " + encodedAuth.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

//	public String getFromStacks(String path) throws JsonProcessingException {
//		HttpEntity<String> e = new HttpEntity<String>(getHeaders());
//		ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.GET, e, String.class);
//		return response.getBody();
//	}

}
