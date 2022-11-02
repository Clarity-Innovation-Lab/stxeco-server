package eco.stx.edao.nft.api;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.contracts.service.ContractEventRepository;
import eco.stx.edao.contracts.service.ContractEventsService;
import eco.stx.edao.contracts.service.domain.ApiValueBean;
import eco.stx.edao.nft.api.model.NFTHoldingEvent;
import eco.stx.edao.nft.api.model.NFTHoldingEvents;
import eco.stx.edao.nft.api.model.Sip016NFTMetaData;
import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;
import eco.stx.edao.stacks.PostData;
import eco.stx.edao.stacks.service.ClarityDeserialiser;
import eco.stx.edao.stacks.service.ClaritySerialiser;
import eco.stx.edao.stacks.service.ClarityType;
import eco.stx.edao.stacks.service.SIP009FunctionNames;

@RestController
@EnableAsync
@EnableScheduling
public class NFTController {
	
	private @Value("${stacks.dao.deployer}") String contractAddress;
	@Autowired private ContractEventsService contractEventsService;
	@Autowired private ContractEventRepository contractEventRepository;
	@Autowired
	private ClarityDeserialiser clarityDeserialiser;
	@Autowired
	private ClaritySerialiser claritySerialiser;
	private static String subPath = "/v2/contracts/call-read/";
	private static String SLASH = "/";
	private static final Logger logger = LogManager.getLogger(NFTController.class);
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private RestOperations restTemplate;
	// https://arweave.net/Z4ygyXm-fERGzKEB2bvE7gx98SHcoaP8qdZQo0Kxm6Y/2.png
	// https://arweave.net/II4z2ziYyqG7-kWDa98lWGfjxRdYOx9Zdld9P_I_kzE/2.json
	private static final String HTTPS_ARWEAVE_CLOUD = "https://arweave.net/";
	private static final String HTTPS_HASHONE_MYPINATA_CLOUD_IPFS = "https://hashone.mypinata.cloud/ipfs/";
	private static final String ID = "{id}";
	private static final String ARW1 = "ar://";
	private static final String ARW2 = "ar/";
	private static final String IPFS = "ipfs://";
	private static final String IPFS2 = "ipfs/";
	private static Map<String, String> assetIdMap = new HashMap<String, String>();

	@GetMapping(value = "/v2/nft/assets/{stxAddress}")
	public Set<String> identifiers(@PathVariable String stxAddress) throws JsonProcessingException {
		NFTHoldingEvents events = null;
		long offset = 0;
		long limit = 50;
		Set<String> holdings = new HashSet<String>();
		do {
			events = fetchHoldings(stxAddress, null, limit, offset);
			for (NFTHoldingEvent event : events.getResults()) {
				holdings.add(event.getAsset_identifier());
			}
			offset += 50;
		}  while (events.getTotal() > offset);
		return holdings;
	}
		
	@GetMapping(value = "/v2/nft/{stxAddress}/{assetId}/{offset}/{limit}")
	public NFTHoldingEvents holdings(@PathVariable String stxAddress, @PathVariable String assetId, @PathVariable Long offset, @PathVariable Long limit) throws JsonProcessingException {
		NFTHoldingEvents events = null;
		events = fetchHoldings(stxAddress, assetId, limit, offset);
		for (NFTHoldingEvent event : events.getResults()) {
			populate(event, stxAddress);
		}
		return events;
	}
		
	private void populate(NFTHoldingEvent event, String stxAddress) {
		try {
			if (event.getAsset_identifier().indexOf("bns::names") > -1) return;
			Map<String, String> tokenIdMap = getTokenId(event.getValue());
			String tokenUri = null; //assetIdMap.get(event.getAsset_identifier());
			if (tokenUri == null) {
				tokenUri = readTokenUri(event.getAsset_identifier().split("::")[0], stxAddress, tokenIdMap.get("tokenId"));
				assetIdMap.put(event.getAsset_identifier(), tokenUri);
			}
			event.setTokenIdMap(tokenIdMap);
			tokenUri = normaliseTokenUri(tokenIdMap.get("tokenId"), tokenUri);
			event.setToken_uri(tokenUri);
			if (tokenUri.indexOf("thisisnumberone") == -1) {
				event.setMetaData(readMetaData(tokenUri));
			}
		} catch (Exception e) {
			logger.error("Error populating event: ", event);
		}
	}

	private Map<String, String> getTokenId(ApiValueBean value) {
		Map<String, String> map = new HashMap<String, String>();
		if (value.getRepr().indexOf("tuple") > -1) {
			if (value.getRepr().indexOf("namespace") > -1) {
			    String part1 = value.getRepr().split("name ")[1];
	    	    part1 = part1.split("\\)")[0];
	    	    String part2 = value.getRepr().split("namespace ")[1];
	    	    part2 = part2.split("\\)")[0];
				map.put("name", part1);
				map.put("namespace", part2);
			} else {
			    String part1 = value.getRepr().split("owner ")[1];
	    	    part1 = part1.split("\\)")[0];
	    	    if (part1.startsWith("'")) part1 = part1.substring(1);
	    	    String part2 = value.getRepr().split("token-id ")[1];
	    	    part2 = part2.split("'")[0].substring(1);
	    	    part2 = part2.split("\\)")[0];
				map.put("owner", part1);
				map.put("tokenId", part2);
			}
		} else {
			map.put("tokenId", value.getRepr().substring(1));
		}
		return map;
	}

	private String normaliseTokenUri(String tokenId, String tokenUri) {
		if (tokenUri == null) return null;
		if (tokenUri.startsWith(IPFS)) {
			tokenUri = tokenUri.replace(IPFS, HTTPS_HASHONE_MYPINATA_CLOUD_IPFS);
		} else if (tokenUri.startsWith(IPFS2)) {
			tokenUri = tokenUri.replace(IPFS2, HTTPS_HASHONE_MYPINATA_CLOUD_IPFS);
		} else if (tokenUri.startsWith(ARW1)) {
			tokenUri = tokenUri.replace(ARW1, HTTPS_ARWEAVE_CLOUD);
		} else if (tokenUri.startsWith(ARW2)) {
			tokenUri = tokenUri.replace(ARW2, HTTPS_ARWEAVE_CLOUD);
		}
		
		if (tokenUri.indexOf(ID) > -1) {
			tokenUri = tokenUri.replace(ID, tokenId);
		} else if (tokenUri.endsWith("/1.json")) {
			// tokenUri = tokenUri.replace("/1.json", ("/" + tokenId + ".json"));
		} else if (tokenUri.endsWith("/")) {
			// tokenUri = tokenUri + tokenId + ".json";
		} else if (!tokenUri.endsWith("/") && !tokenUri.endsWith("json")) {
			tokenUri = tokenUri + "/";
			tokenUri = tokenUri + tokenId + ".json";
		}
		if (tokenUri.indexOf("ipfs/ipfs") > -1) {
			tokenUri = tokenUri.replace("ipfs/ipfs", "ipfs");
		}
		return tokenUri;
	}
	
	@PostMapping(value = "/v2/meta-data-json")
	public Sip016NFTMetaData readMetaData(String tokenUri) throws JsonMappingException, JsonProcessingException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> he = new HttpEntity<String>(headers);
			HttpEntity<String> response = restTemplate.exchange(tokenUri, HttpMethod.GET, he, String.class);
			String json = response.getBody();
			Sip016NFTMetaData metaData = (Sip016NFTMetaData) mapper.readValue(json, new TypeReference<Sip016NFTMetaData>() {});
			if (metaData.getImage().startsWith(IPFS)) {
				metaData.setImage(metaData.getImage().replace(IPFS, HTTPS_HASHONE_MYPINATA_CLOUD_IPFS));
			} else if (metaData.getImage().startsWith(IPFS2)) {
				metaData.setImage(metaData.getImage().replace(IPFS2, HTTPS_HASHONE_MYPINATA_CLOUD_IPFS));
			} else if (tokenUri.startsWith(ARW1)) {
				metaData.setImage(metaData.getImage().replace(ARW1, HTTPS_ARWEAVE_CLOUD));
			} else if (tokenUri.startsWith(ARW2)) {
				metaData.setImage(metaData.getImage().replace(ARW2, HTTPS_ARWEAVE_CLOUD));
			}
			if (metaData.getImage().indexOf("ipfs/ipfs") > -1) {
				metaData.setImage(metaData.getImage().replace("ipfs/ipfs", "ipfs"));
			}
			return metaData;
		} catch (Exception e) {
			logger.error("Nothing found at: " + tokenUri);
		}
		return null;
	}

	@Cacheable
	private NFTHoldingEvents fetchHoldings(String stxAddress, String assetId, Long limit, Long offset) throws JsonProcessingException {
		String path = "/extended/v1/tokens/nft/holdings?principal=" + stxAddress + "&limit" + limit + "&offset=" + offset;
		if (assetId != null) path += "&asset_identifiers=" + assetId;
		ApiFetchConfig p = new ApiFetchConfig();
		p.setHttpMethod("GET");
		p.setPath(path);
		String json = apiHelper.fetchFromApi(p);
		NFTHoldingEvents events = (NFTHoldingEvents) mapper.readValue(json, new TypeReference<NFTHoldingEvents>() {});
		return events;
	}
	
	@Cacheable
	private String readTokenUri(String contractId, String stxAddress, String tokenId) throws JsonProcessingException {
		try {
//			Application a = applicationRepository.findByContractId(contractId);
//			if (checkTokenContract && a.getTokenContract().getTokenUri() != null) {
//				return a.getTokenContract().getTokenUri();
//			}
			SIP009FunctionNames fname = SIP009FunctionNames.GET_TOKEN_URI;
			String arg1 = claritySerialiser.serialiseUInt(BigInteger.valueOf(Long.parseLong(tokenId)));
			String[] arguments = new String[] {arg1};
			String path = path(contractId, fname.getName());
			ApiFetchConfig p = new ApiFetchConfig();
			p.setHttpMethod("POST");
			p.setPath(path);
			p.setPostData(new PostData(stxAddress, arguments));
			String response = apiHelper.fetchFromApi(p);
			Map<String, Object> data = null;
			data = clarityDeserialiser.deserialise(fname.getName(), response);
			if (data != null) {
				ClarityType data1 = (ClarityType) data.get(fname.getName());
				return (String) data1.getValue();
			}
		} catch (Exception e) {
			logger.error("Error reading from contract: contractId: " + contractId);
		}
		return null;
	}
	
	private String path(String contractId, String function) {
		String[] cparts = contractId.split("\\.");
		String path = subPath + cparts[0] + SLASH + cparts[1] + SLASH + function;
		return path;
	}

}
