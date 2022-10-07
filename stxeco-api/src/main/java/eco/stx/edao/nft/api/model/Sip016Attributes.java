package eco.stx.edao.nft.api.model;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TypeAlias(value = "Sip016Attributes")
public class Sip016Attributes {

	private List<Sip016Trait> traits;
}
