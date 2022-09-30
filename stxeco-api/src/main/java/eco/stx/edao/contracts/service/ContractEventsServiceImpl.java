package eco.stx.edao.contracts.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.contracts.service.domain.ContractEvent;
import eco.stx.edao.contracts.service.domain.ContractEvents;
import eco.stx.edao.contracts.service.domain.VoteEvent;
import eco.stx.edao.contracts.service.domain.VoteEventWrapper;
import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;

@Service
public class ContractEventsServiceImpl implements ContractEventsService {

    private static final Logger logger = LogManager.getLogger(ContractEventsServiceImpl.class);
	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private ContractEventRepository contractEventRepository;

	@Override public void consumeContractEvents(String contractId) throws JsonMappingException, JsonProcessingException {
		//Optional<Long> offsetO = contractEventRepository.countByContract_id(contractId);
		long offset = 0;
		//if (offsetO.isPresent()) offset = offsetO.get();
		String path = "/extended/v1/contract/" + contractId + "/events?limit=50&offset=";
		boolean read = true;
		// contractEventRepository.deleteByContract_id(contractId);
		ContractEvents events = null;
		while (read) {
			String json = read(path + offset);
			try {
				events = (ContractEvents)  mapper.readValue(json, new TypeReference<ContractEvents>() {});
				if (events.getResults() != null && events.getResults().size() > 0) {
					for (ContractEvent ce : events.getResults()) {
						if (ce.getContract_log().getValue().getRepr().indexOf("vote") > -1) {
							ce.setVoteEvent(deserialise(ce.getContract_log().getValue().getHex()));
						}
						// events.getResults().add(ce);
						ContractEvent cedb = contractEventRepository.findOneByHex(ce.getContract_log().getValue().getHex());
						if (cedb != null) {
							ce.setId(cedb.getId());
							logger.info("Found one: " + cedb.getId());
							contractEventRepository.save(ce);
						}
						contractEventRepository.save(ce);
					}
					// Long count = events.getLimit() * events.getOffset() + events.getLimit();
					offset += 50;
				} else {
					read = false;
				}
			} catch (Exception e) {
				read = false;
			}
		}
	}
	
	private String read(String path) {
		ApiFetchConfig p = new ApiFetchConfig();
		p.setHttpMethod("GET");
		p.setPath(path);
		try {
			String json = apiHelper.fetchFromApi(p);
			return json;
		} catch (Exception e) {
			return null;
		}
	}

	private VoteEvent deserialise(String hex)
			throws JsonMappingException, JsonProcessingException {
		try {
			String param = "/to-json/" + hex;
			String json = apiHelper.cvConversion(param);
			VoteEventWrapper typeValue = (VoteEventWrapper) mapper.readValue(json, new TypeReference<VoteEventWrapper>() {});
			if (typeValue.getValue() == null)
				return null;
			return VoteEvent.fromClarity(typeValue.getValue());
		} catch (Exception e) {
			logger.error("deserialise: ", e);
			return null;
		}
	}

//	@Override
//	public MintEventsBean getMintEvents(String contractId, String assetName, int offset, int limit, boolean unanchored, boolean tx_metadata) {
//		String path = "/extended/v1/tokens/nft/mints?asset_identifier=" + contractId + "::" + assetName + "&limit=" + limit + "&offset=" + offset + "&unanchored=" + unanchored + "&tx_metadata=" + tx_metadata;
//		MintEventsBean meb = getMintEvents(path);
//		return meb;
//	}
//
//	@Override
//	public Long countMintEventsByContractId(String contractId, String assetName) {
//		Long count = 0L;
//		String path = "/extended/v1/tokens/nft/mints?asset_identifier=" + contractId + "::" + assetName + "&limit=" + 1;
//		MintEventsBean meb = getMintEvents(path);
//		if (meb != null) count = meb.getTotal();
//		return count;
//	}
	
//	private MintEventsBean getMintEvents(String path) {
//		ApiFetchConfig p = new ApiFetchConfig();
//		p.setHttpMethod("GET");
//		p.setPath(path);
//		try {
//			String json = apiHelper.fetchFromApi(p);
//			return (MintEventsBean)mapper.readValue(json, new TypeReference<MintEventsBean>() {});
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
//	private List<NFTEvent> readContractEvents(String contractId) throws JsonProcessingException {
//		contractEventsService.getMintEvents(contractId, assetName, offset, limit, unanchored, tx_metadata)
//		String path = "/extended/v1/tokens/nft/mints?asset_identifier=" + contractId;
//		MintEventsBean meb = getMintEvents(path);
//		// https://stacks-node-api.mainnet.stacks.co/extended/v1/tokens/nft/holdings
//		// String path = stacksPath + "/extended/v1/address/" + recipient + SLASH + "nft_events?limit=50&offset=";
//		String path = "/extended/v1/tokens/nft/holdings/" + recipient + "?limit=50&offset=";
//        int offset = 0;
//		boolean read = true;
//        List<NFTEvent> nFTEvents = new ArrayList<NFTEvent>();
//        while (read) {
//    		String json = readFromStacks(path + offset);
//            NFTEventsBean nFTEventsBean = (NFTEventsBean) mapper.readValue(json, new TypeReference<NFTEventsBean>() {});
//            if (nFTEventsBean.getResults() != null) nFTEvents.addAll(nFTEventsBean.getResults());
//            Long count = nFTEventsBean.getLimit() * nFTEventsBean.getOffset() + nFTEventsBean.getLimit();
//            if (count >= nFTEventsBean.getTotal()) {
//            	read = false;
//            }
//            offset += 50;
//        }
//        for (NFTEvent event : nFTEvents) {
//        	if (event.getAssetIdentifier().indexOf("bns::names") > -1) {
//        		event.setBnsName(extractBNS(event.getValue().getRepr()));
//        	}
//        }
//        nonFungTokenEventRepository.deleteByRecipient(recipient);
//        nonFungTokenEventRepository.saveAll(nFTEvents);
//		return nFTEvents;
//	}

}