package eco.stx.edao.eco.proposals.service.domain.clarity;

import org.springframework.data.annotation.TypeAlias;

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
@TypeAlias(value = "VoteEventWrapper")
public class TypeValueInner {

	public String type;
	public ClarityProposalData value;
	
}
