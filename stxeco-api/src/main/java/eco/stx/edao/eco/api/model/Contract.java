package eco.stx.edao.eco.api.model;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias(value = "Contract")
public class Contract {

	// public String abi;
	@JsonProperty("tx_id") public String txId;
	@JsonProperty("tx_status") public String txStatus;
	@JsonProperty("source_code") public String sourceCode;
	@JsonProperty("block_height") public String blockHeight;
	@JsonProperty("contract_id") public String contractId;
	@JsonProperty("sender_address") public String senderAddress;
	public boolean canonical;
}
