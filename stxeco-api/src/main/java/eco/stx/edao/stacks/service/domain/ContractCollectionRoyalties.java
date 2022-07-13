package eco.stx.edao.stacks.service.domain;

import java.util.List;
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
@TypeAlias(value = "ContractCollectionRoyalties")
public class ContractCollectionRoyalties {
	
	private List<ClarityType> mintAddresses;
	private List<ClarityType> mintShares;
	private List<ClarityType> addresses;
	private List<ClarityType> shares;
	private List<ClarityType> secondaries;
	
	public static ContractCollectionRoyalties fromMap(Map<String, Object> map1) {
		
		ContractCollectionRoyalties t = new ContractCollectionRoyalties();
		if (map1.containsKey("get-collection-beneficiaries")) {
			Map<String, Object> map = (Map<String, Object>) map1.get("get-collection-beneficiaries");
			if (map.containsKey("collection-mint-addresses")) {
				try {
					List<ClarityType> o = (List<ClarityType>) map.get("collection-mint-addresses");
					t.mintAddresses = o;
				} catch (Exception e) {
					// if no benneficiaries returns ClarityType(type=9) OptionalNone.
					// this happens when this token is edition 2 or greater.
				}
			}
			if (map.containsKey("collection-mint-shares")) {
				try {
					List<ClarityType> o = (List<ClarityType>) map.get("collection-mint-shares");
					t.mintShares = o;
				} catch (Exception e) {
				}
			}
			if (map.containsKey("collection-addresses")) {
				try {
					List<ClarityType> o = (List<ClarityType>) map.get("collection-addresses");
					t.addresses = o;
				} catch (Exception e) {
				}
			}
			if (map.containsKey("collection-shares")) {
				try {
					List<ClarityType> o = (List<ClarityType>) map.get("collection-shares");
					t.shares = o;
				} catch (Exception e) {
				}
			}
			if (map.containsKey("collection-secondaries")) {
				try {
					List<ClarityType> o = (List<ClarityType>) map.get("collection-secondaries");
					t.secondaries = o;
				} catch (Exception e) {
				}
			}
		}

		return t;
	}
}