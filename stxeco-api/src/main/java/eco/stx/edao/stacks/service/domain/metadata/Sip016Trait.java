package eco.stx.edao.stacks.service.domain.metadata;

import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TypeAlias(value = "Sip016Trait")
public class Sip016Trait {

	private String display_type;
	private String trait_type;
	private Object value;
}
