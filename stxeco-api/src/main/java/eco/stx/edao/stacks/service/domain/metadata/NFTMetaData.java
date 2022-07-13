package eco.stx.edao.stacks.service.domain.metadata;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import eco.stx.edao.stacks.service.domain.Token;
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
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias(value = "NFTMetaData")
@Document
public class NFTMetaData {

	@Id private String id;
	private String contractId;
	private Long nftIndex;
	private String name;
	private String description;
	private Long created;
	private Long updated;
	private String owner;
	private String uploader;
	private String assetHash;
	private String image;
	private String external_url;
	private String metaDataUrl;
	private String currentRunKey;
	private String privacy;
	private String artist;
	private String objType;
	private String domain;
	private Token contractAsset;
	private KeywordModel category;
	private List<KeywordModel> keywords;
	private String status;
	private Attributes attributes;
	private Map<String, String> metaData;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contractAsset == null) ? 0 : contractAsset.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NFTMetaData other = (NFTMetaData) obj;
		if (contractAsset == null) {
			if (other.contractAsset != null)
				return false;
		} else if (!contractAsset.equals(other.contractAsset))
			return false;
		return true;
	}
	
}
