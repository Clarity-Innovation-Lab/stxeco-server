package eco.stx.edao.twoone.method2;

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
@TypeAlias(value = "TwoOneEvents")
public class TwoOneEvents {

    private Long total;
    private Long limit;
    private Long offset;
    private List<TwoOneEvent> results;
}
