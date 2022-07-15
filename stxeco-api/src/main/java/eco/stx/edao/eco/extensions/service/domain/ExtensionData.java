package eco.stx.edao.eco.extensions.service.domain;

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
@TypeAlias(value = "ExtensionData")
public class ExtensionData {

	public Boolean valid;
	
	public static ExtensionData fromClarity(ClarityProposalData cpd) {
		ExtensionData extd = new ExtensionData();
		extd.setValid((Boolean)cpd.getConcluded().getValue());
		return extd;
	}

}
