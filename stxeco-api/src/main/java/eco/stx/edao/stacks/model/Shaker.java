package eco.stx.edao.stacks.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Shaker {

	private String privateKey = "ea6335858bdcb5e7a538759dddcc8df6215e1458078924442b379faafb7b997101";
	private String address = "ST3MMDYNCCSYKB9E77KD9QD8RG2QY72X6V444X0RX";
	private String btcAddress = "n2nQmUHj4FoFW6dv7LngnmANs92YwRSu8h";

	public Shaker() {
		super();
	}
}
