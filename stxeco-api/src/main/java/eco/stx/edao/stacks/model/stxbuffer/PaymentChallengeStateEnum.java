package eco.stx.edao.stacks.model.stxbuffer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PaymentChallengeStateEnumSerializer.class)
public enum PaymentChallengeStateEnum
{
	NEW(-1, "PurchaseOrder pending." ),
	EXPIRED(-3, "Payment expired." ),
	PRE_BUYER_TX(3, "PurchaseOrder started." ),
	BUYER_TX_CONFIRMING(4, "Buyer has sent payment."),
	BUYER_TX_CONFIRMED(5, "Buyers payment confirmed"),
	UPSTREAM_TX_CONFIRMING(6, "Payment to seller is being confirmed"),
	UPSTREAM_TX_CONFIRMED(7, "Seller has been paid"),
	TRANSFER_COMPLETE(8, "PurchaseOrder complete." ),
	FREE_CREDITS(9, "Credits by login offer." ),
	PAID_ETHER(10, "Invoice paid using ether." );
	
	private int status;
	private String description;
	
	private PaymentChallengeStateEnum(int status, String description)
	{
		this.status = status;
		this.description = description;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
