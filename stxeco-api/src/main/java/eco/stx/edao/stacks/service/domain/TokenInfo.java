package eco.stx.edao.stacks.service.domain;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.edao.stacks.service.ClarityType;
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
@TypeAlias(value = "TokenInfo")
public class TokenInfo {
	
	private Long seriesOriginal;
	private Long edition;
	private Long mintBlockHeight;
	private Long maxEditions;
	private Long editionCost;
	private String assetHash;
	private String metaDataUrl;
	
	public static TokenInfo fromMap(Map<String, Object> map) {
		
		TokenInfo t = new TokenInfo();

		t.assetHash = (String) ((ClarityType)map.get("asset-hash")).getValueHex();

		t.metaDataUrl = (String) ((ClarityType)map.get("meta-data-url")).getValue();

		ClarityType ct = (ClarityType)map.get("max-editions");
		t.maxEditions = ((BigInteger)ct.getValue()).longValue();

		ct = (ClarityType)map.get("edition-cost");
		t.editionCost = ((BigInteger)ct.getValue()).longValue();

		ct = (ClarityType)map.get("edition");
		t.edition = ((BigInteger)ct.getValue()).longValue();

//		ct = (ClarityType)map.get("mint-block-height");
//		if (ct != null) t.mintBlockHeight = ((BigInteger)ct.getValue()).longValue();

		ct = (ClarityType)map.get("series-original");
		t.seriesOriginal = ((BigInteger)ct.getValue()).longValue();

		return t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetHash == null) ? 0 : assetHash.hashCode());
		result = prime * result + ((edition == null) ? 0 : edition.hashCode());
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
		TokenInfo other = (TokenInfo) obj;
		if (assetHash == null) {
			if (other.assetHash != null)
				return false;
		} else if (!assetHash.equals(other.assetHash))
			return false;
		if (edition == null) {
			if (other.edition != null)
				return false;
		} else if (!edition.equals(other.edition))
			return false;
		return true;
	}
}
