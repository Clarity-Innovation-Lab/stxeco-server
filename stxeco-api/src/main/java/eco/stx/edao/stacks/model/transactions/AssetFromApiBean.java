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
@TypeAlias(value = "AssetFromApiBean")
public class AssetFromApiBean {

	private Long amount;
	private String asset_event_type;
	private String asset_id;
	private String recipient;
	private String sender;
}
