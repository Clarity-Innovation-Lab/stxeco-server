package eco.stx.edao.stacks.service.domain;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

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
@TypeAlias(value = "Token")
@Document
public class Token {
	
	@Id private String id;
	private Long blockHeight;
	private ListingInUstx listingInUstx;
	private String assetName;
	private Long nftIndex;
	private Long bidCounter;
	private Long editionCounter;
	private SaleData saleData;
	private TokenInfo tokenInfo;
	Map<String, List<ClarityType>> beneficiaries;
	private List<Bid> bidHistory;
	private String owner;
	private String contractId;
	
	public static Token fromMap(long tokenIndex, Map<String, Object> map, String contractId) {
		
		Token t = new Token();
		t.nftIndex = tokenIndex;
		t.contractId = contractId;
		ClarityType ownerCT = (ClarityType)map.get("owner");
		if (ownerCT.getType() == 6) {
			t.owner = (String) ((ClarityType)ownerCT.getValue()).getValueHex();
			t.owner += "." + (String) ((ClarityType)ownerCT.getValueHex()).getValueHex();
		} else {
			t.owner = (String) ((ClarityType)map.get("owner")).getValueHex();
		}

		if (map.containsKey("beneficiaryData")) {
			try {
				Map<String, List<ClarityType>> bene = (Map<String, List<ClarityType>>)map.get("beneficiaryData");
				t.beneficiaries = bene;
			} catch (Exception e) {
				// if no benneficiaries returns ClarityType(type=9) OptionalNone.
				// this happens when this token is edition 2 or greater.
			}
		}

		ClarityType ct = (ClarityType)map.get("nftIndex");
		t.nftIndex = ((BigInteger)ct.getValue()).longValue();

		ct = (ClarityType)map.get("bidCounter");
		t.bidCounter = ((BigInteger)ct.getValue()).longValue();

		ct = (ClarityType)map.get("editionCounter");
		t.editionCounter = ((BigInteger)ct.getValue()).longValue();

		Map<String, Object> info = null;

		try {
			info = (Map)map.get("tokenInfo");
			TokenInfo sd = TokenInfo.fromMap(info);
			t.tokenInfo = sd;
		} catch (Exception e1) {
			// empty map
		}

		try {
			info = (Map)map.get("saleData");
			SaleData sd = SaleData.fromMap(info);
			t.saleData = sd;
		} catch (Exception e) {
			// empty map
		}

		return t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + ((nftIndex == null) ? 0 : nftIndex.hashCode());
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
		Token other = (Token) obj;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (nftIndex == null) {
			if (other.nftIndex != null)
				return false;
		} else if (!nftIndex.equals(other.nftIndex))
			return false;
		return true;
	}

}
