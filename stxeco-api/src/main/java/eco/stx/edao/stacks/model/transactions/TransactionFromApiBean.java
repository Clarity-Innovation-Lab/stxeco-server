package eco.stx.edao.stacks.model.transactions;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;

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
@TypeAlias(value = "TransactionIdBean")
public class TransactionFromApiBean {

	private Long block_height;
	private String tx_id;
	private TransactionResultBean tx_result;
	private String tx_status;
	private List<EventFromApiBean> events;
}
