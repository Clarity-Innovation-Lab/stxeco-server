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
@TypeAlias(value = "Bid")
public class Bid {

	private String bidder;
	private Integer saleCycle;
	private Long appTimestamp;
	private Long amount;

	public static Bid fromMap(Map<String, Object> map) {
		
		Bid sd = new Bid();

		try {

			ClarityType ct = (ClarityType)map.get("bidder");
			sd.setBidder((String) ((ClarityType)map.get("bidder")).getValueHex());

			ct = (ClarityType)map.get("amount");
			sd.setAmount(((BigInteger)ct.getValue()).longValue());
			
			ct = (ClarityType)map.get("bid-in-block");
			if (ct != null) sd.setAppTimestamp(((BigInteger)ct.getValue()).longValue());

			ct = (ClarityType)map.get("sale-cycle");
			sd.setSaleCycle(((BigInteger)ct.getValue()).intValue());

		} catch (Exception e) {
			// empty map
		}

		return sd;
	}

}
