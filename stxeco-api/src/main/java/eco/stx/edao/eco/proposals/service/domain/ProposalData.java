package eco.stx.edao.eco.proposals.service.domain;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.edao.eco.proposals.service.domain.clarity.ClarityProposalData;
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
@TypeAlias(value = "ProposalData")
public class ProposalData {

	public Boolean concluded;
	public Boolean passed;
	public String proposer;
	public Integer customMajority;
	public Integer endBlockHeight;
	public Integer startBlockHeight;
	public Long votesAgainst;
	public Long votesFor;
	
	public static ProposalData fromClarity(ClarityProposalData cpd) {
		ProposalData pd = new ProposalData();
		pd.setConcluded((Boolean)cpd.getConcluded().getValue());
		pd.setPassed((Boolean)cpd.getPassed().getValue());
		try {
			pd.setCustomMajority(Integer.valueOf((String)cpd.getCustomMajority().getValue()));
		} catch (Exception e) {
			// skip this param.
		}
		pd.setEndBlockHeight(Integer.valueOf((String)cpd.getEndBlockHeight().getValue()));
		pd.setStartBlockHeight(Integer.valueOf((String)cpd.getStartBlockHeight().getValue()));
		pd.setVotesFor(Long.valueOf((String)cpd.getVotesFor().getValue()));
		pd.setVotesAgainst(Long.valueOf((String)cpd.getVotesAgainst().getValue()));
		pd.setProposer((String)cpd.getProposer().getValue());
		return pd;
	}

}
