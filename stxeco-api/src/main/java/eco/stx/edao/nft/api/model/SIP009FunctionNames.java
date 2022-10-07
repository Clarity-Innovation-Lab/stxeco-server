package eco.stx.edao.nft.api.model;

public enum SIP009FunctionNames {

	GET_LAST_TOKEN_ID("get-last-token-id"),
	GET_LISTING_USTX("get-listing-in-ustx"),
	GET_LISTING_TOKEN("get-listing-in-token"),
	GET_TOKEN_URI("get-token-uri"),
	GET_TOKEN_OWNER("get-owner");

	private String name;
	
	private SIP009FunctionNames(String name)
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
