package eco.stx.edao.eco.proposals.api.model;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.edao.eco.api.model.Contract;
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
@TypeAlias(value = "PinataFile")
public class ProposalContracts {

	public int limit;
	public int offset;
	public List<Contract> results;
}
