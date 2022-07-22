package eco.stx.edao.eco.api.model;

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
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias(value = "StacksInfo")
public class StacksInfo {

	private String peer_version;
	private String pox_consensus;
	private int burn_block_height;
	private String stable_pox_consensus;
	private int stable_burn_block_height;
	private String server_version;
	private String network_id;
	private String parent_network_id;
	private int stacks_tip_height;
	private String stacks_tip;
	private String stacks_tip_consensus_hash;
	private String genesis_chainstate_hash;
	private String unanchored_tip;
	private String unanchored_seq;
	private String exit_at_block_height;
}
