package eco.stx.edao.nft.api.model;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TypeAlias(value = "Sip016Localisation")
public class Sip016Localisation {

	private String uri;
	@JsonProperty("default") private String _default = "en";
	private String[] locales = new String[]{"en"};
}
