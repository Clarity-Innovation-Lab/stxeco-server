package eco.stx.edao.twoone.method1;

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
@TypeAlias(value = "Method1DataWrapper")
public class Method1DataWrapper {

	private List<Method1Vote> list;
    private Integer page;
    private Integer page_total;
    private Integer pagesize;
    private Integer total_count;
}
