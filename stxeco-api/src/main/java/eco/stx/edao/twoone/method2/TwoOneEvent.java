package eco.stx.edao.twoone.method2;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import eco.stx.edao.contracts.service.domain.ApiValueBean;
import eco.stx.edao.contracts.service.domain.TokenTransfer;
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
@TypeAlias(value = "TwoOneEvent")
@Document
public class TwoOneEvent {

	@Id private String id;
	private ApiValueBean tx_result;
    private String tx_id;
    private String tx_status;
    private String sender_address;
    private Long block_height;
    private TokenTransfer token_transfer;
    private StxBalance stxBalance;
    private StxBalance stxBalance1;
    private StxBalance stxBalance2;
    private StxBalance stxBalance3;
    private StxBalance stxBalance4;
}
