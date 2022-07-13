package eco.stx.edao.eco.proposals.service.domain.clarity;

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
@TypeAlias(value = "ClarityProposalData")
public class ClarityProposalData {

	public CTypeValue concluded;
	public CTypeValue passed;
	public CTypeValue proposer;
	@JsonProperty("end-block-height") public CTypeValue endBlockHeight;
	@JsonProperty("start-block-height") public CTypeValue startBlockHeight;
	@JsonProperty("votes-against") public CTypeValue votesAgainst;
	@JsonProperty("votes-for") public CTypeValue votesFor;	

}
