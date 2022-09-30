package eco.stx.edao.contracts.service.domain;

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
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias(value = "VoteEvent")
public class VoteEvent {

    private Long amount;
    private String event;
    @JsonProperty("for") private boolean voteFor;
    private String proposal;
	private String voter;

	public static VoteEvent fromClarity(ClarityVoteEventData cve) {
		VoteEvent ve = new VoteEvent();
		ve.setVoteFor((Boolean)cve.getVoteFor().getValue());
		ve.setAmount(Long.valueOf((String)cve.getAmount().getValue()));
		ve.setProposal((String)cve.getProposal().getValue());
		ve.setVoter((String)cve.getVoter().getValue());
		return ve;
	}
}
