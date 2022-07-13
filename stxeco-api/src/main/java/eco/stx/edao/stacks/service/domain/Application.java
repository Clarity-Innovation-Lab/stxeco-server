 package eco.stx.edao.stacks.service.domain;

import java.math.BigInteger;
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
@TypeAlias(value = "Application")
@Document
public class Application {
	
	@Id private String id;
	private String contractId;
	private String owner;
	private Long appIndex;
	private int storageModel;
	private int status;
	private TokenContract tokenContract;
	private ContractCollectionRoyalties contractCollectionRoyalties;
	
	public static Application fromMap(long appIndex, Map<String, Object> registry) {
		
		Application a = new Application();
		a.setAppIndex(appIndex);
		try {
			ClarityType ct = (ClarityType) registry.get("status");
			a.setStatus(((BigInteger)ct.getValue()).intValue());
			
			ct = (ClarityType) registry.get("owner");
			a.setOwner((String)ct.getValueHex());
			
			ct = (ClarityType) registry.get("app-contract-id");
			a.setContractId((String)ct.getValue());
		} catch (Exception e) {
			return null;
		}
		
		return a;
	}
}
