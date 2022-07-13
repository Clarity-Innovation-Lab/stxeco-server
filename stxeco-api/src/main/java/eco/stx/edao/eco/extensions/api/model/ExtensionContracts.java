package eco.stx.edao.eco.extensions.api.model;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.edao.eco.extensions.service.domain.ExtensionContract;
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
public class ExtensionContracts {

	public int limit;
	public int offset;
	public List<ExtensionContract> results;
}
