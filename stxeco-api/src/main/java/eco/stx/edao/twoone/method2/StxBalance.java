package eco.stx.edao.twoone.method2;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.edao.contracts.service.domain.ApiValueBean;
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
@TypeAlias(value = "StxBalance")
public class StxBalance {

	private ApiValueBean tx_result;
    private String lock_tx_id;
    private Long query_height;
    private Long balance;
    private Long total_sent;
    private Long total_received;
    private Long locked;
    private Long lock_height;
    private Long burnchain_lock_height;
    private Long burnchain_unlock_height;
}
