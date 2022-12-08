package eco.stx.edao.twoone.method1;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

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
@TypeAlias(value = "Method1Vote")
@Document
public class Method1Vote {

	@Id private String id;
	private String btcAddress;
	private String stxAddress;
	private StxBalance stxBalance;
    private Long block_height;
    private Long block_time;
    private Long balance_diff;
    private List<Method1Input> inputs;
    private List<Method1Output> outputs;
}
