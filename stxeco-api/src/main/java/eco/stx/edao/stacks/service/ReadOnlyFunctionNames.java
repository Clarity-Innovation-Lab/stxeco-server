package eco.stx.edao.stacks.service;

public enum ReadOnlyFunctionNames {

	GET_TOKEN_BY_INDEX("get-token-by-index"),
	GET_TOKEN_BY_HASH("get-token-by-hash"),
	GET_MINT_COUNTER("get-mint-counter"),
	GET_APP("get-app"),
	GET_CONTRACT_DATA("get-contract-data"),
	GET_OFFER_AT_INDEX("get-offer-at-index"),
	GET_BID_AT_INDEX("get-bid-at-index"),
	IS_COLLECTION("is-collection"),
	GET_COLLECTION_ROYALTIES("get-collection-beneficiaries"),
	GET_APP_COUNTER("get-app-counter");

	private String name;
	
	private ReadOnlyFunctionNames(String name)
	{
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
