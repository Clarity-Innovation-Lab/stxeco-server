package eco.stx.edao.twoone.method1;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import eco.stx.edao.contracts.service.domain.ApiValueBean;
import eco.stx.edao.twoone.method2.StxBalance;
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
@TypeAlias(value = "Method1VoteCount")
@Document
public class Method1VoteCount {

	@Id private String id;
	private ApiValueBean tx_result;
    private String tx_id;
    private String tx_status;
    private String sender_address;
    private Long block_height;
    private StxBalance stxBalance;
}
