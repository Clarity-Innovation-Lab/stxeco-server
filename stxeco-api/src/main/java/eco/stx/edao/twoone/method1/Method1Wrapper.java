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
@TypeAlias(value = "Method1Wrapper")
public class Method1Wrapper {

	private Method1DataWrapper data;
    private Integer err_code;
    private Integer err_no;
    private String message;
    private String status;
}
