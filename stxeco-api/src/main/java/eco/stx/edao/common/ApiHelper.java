package eco.stx.edao.common;

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
	@Value("${eco-stx.stax.base-path}") String basePath;
	@Value("${eco-stx.stax.blockchain-api-path}") String sidecarPath;
	@Value("${eco-stx.stax.stacks-path}") String remoteBasePath;
	@Value("${eco-stx.stax.daojsapi}") String daojsapi;

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

	public String fetchFromApi(Principal principal) throws JsonProcessingException {
		ResponseEntity<String> response = null;
		String url = getUrl(principal, true);
		try {
			if (principal.getHttpMethod() != null && principal.getHttpMethod().equalsIgnoreCase("POST")) {
				response = restTemplate.exchange(url, HttpMethod.POST, getRequestEntity(principal), String.class);
			} else {
				response = restTemplate.exchange(url, HttpMethod.GET, getRequestEntity(principal), String.class);
			}
			//response = getRespFromStacks(principal);
		} catch (Exception e) {
			response = getRespFromStacks(principal);
		}
		return response.getBody();
	}
	
	public String fetchFromApi(Principal principal, String data) throws JsonProcessingException {
		ResponseEntity<String> response = null;
		String url = getUrl(principal, true);
		try {
			if (principal.getHttpMethod() != null && principal.getHttpMethod().equalsIgnoreCase("POST")) {
				response = restTemplate.exchange(url, HttpMethod.POST, getRequestEntity(principal), String.class, data);
			} else {
				// response = restTemplate.exchange(url, HttpMethod.GET, getRequestEntity(principal), String.class, data);
//				org.apache.commons.codec.net.URLCodec codec = new org.apache.commons.codec.net.URLCodec();
//				String url1 = url + codec.encode(uriVar);
				return restTemplate.getForObject(url, String.class, data);
			}
			response = getRespFromStacks(principal);
		} catch (Exception e) {
			response = getRespFromStacks(principal);
		}
		return response.getBody();
	}
	
	private ResponseEntity<String> getRespFromStacks(Principal principal) {
		String url = remoteBasePath + principal.getPath();
		if (principal.getHttpMethod() != null && principal.getHttpMethod().equalsIgnoreCase("POST")) {
			return restTemplate.exchange(url, HttpMethod.POST, getRequestEntity(principal), String.class);
		} else {
			return restTemplate.exchange(url, HttpMethod.GET, getRequestEntity(principal), String.class);
		}
	}
	
	private String getUrl(Principal principal, boolean local) {
		String url = basePath + principal.getPath();
		if (local) {
			if (principal.getPath().indexOf("/extended/v1") > -1) {
				url = sidecarPath + principal.getPath();
			}
		} else {
			url = remoteBasePath + principal.getPath();
			if (remoteBasePath.indexOf("20443") > -1 && principal.getPath().indexOf("/extended/v1") > -1) {
				url = url.replace("3999", "20443");
			}
		}
		return url;
	}

	private HttpEntity<String> getRequestEntity(Principal principal) {
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
