package eco.stx.edao.stacks.model.stxbuffer.gaia;

import java.util.Map;

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
@TypeAlias(value = "UserAppMaps")
public class UserAppMaps {

	private Map<String, AppsModel> apps;

}
