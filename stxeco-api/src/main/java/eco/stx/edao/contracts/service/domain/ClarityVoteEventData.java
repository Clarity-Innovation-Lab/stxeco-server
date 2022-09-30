package eco.stx.edao.contracts.service.domain;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.annotation.JsonProperty;

import eco.stx.edao.eco.proposals.service.domain.clarity.CTypeValue;
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
@TypeAlias(value = "ClarityVoteEventData")
public class ClarityVoteEventData {

    private CTypeValue amount;
    private CTypeValue event;
    @JsonProperty("for") private CTypeValue voteFor;
    private CTypeValue proposal;
	private CTypeValue voter;

}
