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
@TypeAlias(value = "Attributes")
public class Attributes {

	private String imageHash;
	private String coverArtist;
	private String collection;
	private MediaObject artworkFile;
	private MediaObject artworkClip;
	private MediaObject coverImage;
	private Long buyNowPrice;
	private Long editions;
	private Long editionCost;
	private Long index;
}
