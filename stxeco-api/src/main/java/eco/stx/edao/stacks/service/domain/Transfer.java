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
@TypeAlias(value = "Transfer")
public class Transfer {

	private String from;
	private String to;
	private Integer saleCycle;
	private Integer saleType;
	private Long blockHeight;
	private Long amount;

	public static Transfer fromMap(Map<String, Object> map) {
		
		Transfer sd = new Transfer();

		try {

			ClarityType ct = (ClarityType)map.get("from");
			sd.setFrom((String) ((ClarityType)map.get("from")).getValueHex());

			ct = (ClarityType)map.get("to");
			sd.setTo((String) ((ClarityType)map.get("to")).getValueHex());

			ct = (ClarityType)map.get("amount");
			sd.setAmount(((BigInteger)ct.getValue()).longValue());
			
			ct = (ClarityType)map.get("when");
			sd.setBlockHeight(((BigInteger)ct.getValue()).longValue());

			ct = (ClarityType)map.get("sale-cycle");
			sd.setSaleCycle(((BigInteger)ct.getValue()).intValue());

			ct = (ClarityType)map.get("sale-type");
			sd.setSaleType(((BigInteger)ct.getValue()).intValue());

		} catch (Exception e) {
			// empty map
		}

		return sd;
	}

}
