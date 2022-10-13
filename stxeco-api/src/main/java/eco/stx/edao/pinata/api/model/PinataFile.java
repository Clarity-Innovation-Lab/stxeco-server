package eco.stx.edao.pinata.api.model;

import java.io.File;

import org.json.JSONObject;
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
@TypeAlias(value = "PinataFile")
public class PinataFile {

	public JSONObject metaData;
	public File file;
}
