package eco.stx.edao.stacks.service.domain.metadata;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias(value = "OutNFTMetaData")
public class OutNFTMetaData {

	private String version;
	private String name;
	private String description;
	private String image;
	private List<Sip016Trait> attributes;
	private Sip016Properties properties;
	private Sip016Localisation localization;

}
