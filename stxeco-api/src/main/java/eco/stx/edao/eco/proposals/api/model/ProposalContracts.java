package eco.stx.edao.eco.proposals.api.model;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.edao.eco.proposals.service.domain.ProposalContract;
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
@TypeAlias(value = "ExtensionContracts")
public class ProposalContracts {

	public int limit;
	public int offset;
	public List<ProposalContract> results;
}
