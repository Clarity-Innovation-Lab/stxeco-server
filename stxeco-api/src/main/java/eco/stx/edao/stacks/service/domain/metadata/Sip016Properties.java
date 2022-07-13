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
@TypeAlias(value = "Sip016Properties")
public class Sip016Properties {

	private String collection;
	private String collectionId;
	private String dna;
	private String aspect_ratio;
	private String total_supply;
	private String full_size_image;
	private String external_url;
	private String animation_url;
	private String animation_cdn_url;
	private String assetHash;
}
