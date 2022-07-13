package eco.stx.edao.stacks.service.domain.metadata;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import eco.stx.edao.stacks.service.domain.Token;
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
@TypeAlias(value = "Sip016NFTMetaData")
@Document
public class Sip016NFTMetaData {

	@Id private String id;
	private String contractId;
	private Long nftIndex;
	private Token contractAsset;
	private String version;
	private String name;
	private String description;
	private String image;
	private List<Sip016Trait> attributes;
	private Sip016Properties properties;
	private Sip016Localisation localization;

}
