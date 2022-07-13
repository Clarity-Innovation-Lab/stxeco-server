package eco.stx.edao.stacks.model.stxbuffer.gaia;

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
@TypeAlias(value = "RootFilePostModel")
public class RootFilePostModel {

	private String appOrigin;
	private String gaiaUsername;

}
