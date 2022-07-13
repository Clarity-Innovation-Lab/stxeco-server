package eco.stx.edao.eco.proposals.service.domain;

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
@TypeAlias(value = "ProposalContract")
public class ProposalContract {

	public String abi;
	@JsonProperty("tx_id") public String txId;
	@JsonProperty("source_code") public String sourceCode;
	@JsonProperty("block_height") public String blockHeight;
	@JsonProperty("contract_id") public String contractId;
	public boolean canonical;
}
