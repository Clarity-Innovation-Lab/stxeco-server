package eco.stx.edao.stacks.service.domain;

import java.math.BigInteger;
import java.util.ArrayList;
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
@TypeAlias(value = "TokenContract")
public class TokenContract {
	
	private String administrator;
	private String tokenName;
	private String tokenSymbol;
	private String tokenUri;
	private Long mintCounter;
	private Long mintPrice;
	private List<Token> tokens = new ArrayList();

	public static TokenContract fromMap(Map<String, Object> registry) {
		
		TokenContract tc = new TokenContract();
		
		ClarityType ct = (ClarityType)registry.get("get-last-token-id");
		tc.setMintCounter(((BigInteger)ct.getValue()).longValue());

		return tc;
	}
}
