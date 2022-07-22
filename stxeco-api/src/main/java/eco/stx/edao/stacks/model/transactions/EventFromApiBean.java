package eco.stx.edao.stacks.model.transactions;

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
@TypeAlias(value = "EventFromApiBean")
public class EventFromApiBean {

	private String event_index;
	private String event_type;
	private String tx_id;
	private AssetFromApiBean asset;
}
