package eco.stx.edao.contracts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.contracts.service.domain.ContractEvents;
import eco.stx.edao.stacks.ApiHelper;
import eco.stx.edao.stacks.ApiFetchConfig;

@Service
public class ContractEventsServiceImpl implements ContractEventsService {

	@Autowired private ApiHelper apiHelper;
	@Autowired private ObjectMapper mapper;
	@Autowired private ContractEventRepository contractsRepository;

	@Override public void consumeContractEvents(String contractId) throws JsonMappingException, JsonProcessingException {
		String path = "/extended/v1/contract/" + contractId + "/events?limit=200&offset=";
		boolean read = true;
		int offset = 0;
		ContractEvents events = null;
		while (read) {
			String json = read(path + offset);
			try {
				events = (ContractEvents)  mapper.readValue(json, new TypeReference<ContractEvents>() {});
				if (events.getResults() != null)
					events.getResults().addAll(events.getResults());
				Long count = events.getLimit() * events.getOffset() + events.getLimit();
				if (count >= events.getTotal()) {
					read = false;
				}
				offset += 200;
			} catch (Exception e) {
				// content is null try next..
			}
		}
		contractsRepository.saveAll(events.getResults());
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