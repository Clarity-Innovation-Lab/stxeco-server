package eco.stx.edao.twoone.method1;

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
@TypeAlias(value = "Method1Input")
public class Method1Input {

    private String[] prev_addresses;
    private String prev_tx_hash;
    private String prev_type;
    private Long prev_value;
}
