package eco.stx.edao.stacks.service.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

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
@TypeAlias(value = "TokenFilter")
@Document
public class TokenFilter {
	
	@Id private String id;
	private Long nftIndex;
	private String stxAddress;
	private String assetHash;
	private String contractId;
	
	public boolean matchesByContractIdAndAssetHash(Token token) {
		return (token.getTokenInfo().getAssetHash().equals(this.assetHash) && 
				token.getContractId().equals(this.contractId));
	}
}
