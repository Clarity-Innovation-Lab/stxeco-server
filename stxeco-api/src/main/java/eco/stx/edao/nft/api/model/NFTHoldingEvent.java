package eco.stx.edao.nft.api.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import eco.stx.edao.contracts.service.domain.ApiValueBean;
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
@TypeAlias(value = "NFTHoldingEvent")
@Document
public class NFTHoldingEvent {

	@Id private String id;
    private String asset_identifier;
    private Map<String, String> tokenIdMap;
    private Long block_height;
    private String tx_id;
    private String token_uri;
    private Sip016NFTMetaData metaData;
    private ApiValueBean value;
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asset_identifier == null) ? 0 : asset_identifier.hashCode());
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
		NFTHoldingEvent other = (NFTHoldingEvent) obj;
		if (asset_identifier == null) {
			if (other.asset_identifier != null)
				return false;
		} else if (!asset_identifier.equals(other.asset_identifier))
			return false;
		return true;
	}
    
}
