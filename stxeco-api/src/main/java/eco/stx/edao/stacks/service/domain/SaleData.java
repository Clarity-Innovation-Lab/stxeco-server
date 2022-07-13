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
@TypeAlias(value = "SaleData")
public class SaleData {

	private Integer saleType;
	private Integer saleCycleIndex;
	private Long buyNowOrStartingPrice;
	private Long incrementPrice;
	private Long reservePrice;
	private Long biddingEndTime;

	public static SaleData fromMap(Map<String, Object> map) {
		
		SaleData sd = new SaleData();

		try {

			ClarityType ct = (ClarityType)map.get("bidding-end-time");
			sd.setBiddingEndTime(((BigInteger)ct.getValue()).longValue());
			
			ct = (ClarityType)map.get("amount-stx");
			sd.setBuyNowOrStartingPrice(((BigInteger)ct.getValue()).longValue());
			
			ct = (ClarityType)map.get("increment-stx");
			sd.setIncrementPrice(((BigInteger)ct.getValue()).longValue());

			ct = (ClarityType)map.get("reserve-stx");
			sd.setReservePrice(((BigInteger)ct.getValue()).longValue());

			ct = (ClarityType)map.get("sale-cycle-index");
			sd.setSaleCycleIndex(((BigInteger)ct.getValue()).intValue());

			ct = (ClarityType)map.get("sale-type");
			sd.setSaleType(((BigInteger)ct.getValue()).intValue());

		} catch (Exception e) {
			// empty map
		}

		return sd;
	}

}
