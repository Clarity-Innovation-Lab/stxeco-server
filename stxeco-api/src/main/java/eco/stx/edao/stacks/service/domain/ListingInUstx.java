package eco.stx.edao.stacks.service.domain;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.edao.stacks.service.ClarityType;
import eco.stx.edao.stacks.service.SIP009FunctionNames;
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
@TypeAlias(value = "ListingInUstx")
public class ListingInUstx {

	private Long price;
	private Long decimals;
	private String commission;
	private String token;
	private String name;
	private String symbol;

	public static ListingInUstx fromMap(Long salesFeeTotal, Map<String, Object> map, SIP009FunctionNames key) {
		
		ListingInUstx l = new ListingInUstx();
		
		if (salesFeeTotal == null) salesFeeTotal = 10L;

		try {
			Map<String, Object> data = (Map<String, Object>)map.get(key.getName());

			ClarityType ct = (ClarityType)data.get("price");
			l.price = ((BigInteger)ct.getValue()).longValue();
			l.price += (l.price / salesFeeTotal);

			String commission = null;
			ClarityType commissionCT = (ClarityType) data.get("commission");
			if (commissionCT.getType() == 6) {
				commission = (String) ((ClarityType) commissionCT.getValue()).getValueHex();
				commission += "." + (String) ((ClarityType) commissionCT.getValueHex()).getValueHex();
			} else {
				commission = (String) commissionCT.getValueHex();
			}
			l.commission = commission;
			
			if (key == SIP009FunctionNames.GET_LISTING_TOKEN) {
				String token = null;
				ClarityType tokenCT = (ClarityType) data.get("token");
				if (tokenCT.getType() == 6) {
					token = (String) ((ClarityType) tokenCT.getValue()).getValueHex();
					token += "." + (String) ((ClarityType) tokenCT.getValueHex()).getValueHex();
				} else {
					token = (String) tokenCT.getValueHex();
				}
				l.token = token;
			}

		} catch (Exception e) {
			// empty map
		}

		return l;
	}

}
