package eco.stx.edao.eco.daoProperties.api.model;

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
@TypeAlias(value = "DaoPropertyTypeValue")
public class DaoPropertyTypeValue {

	public String type;
	public Object value;
	
}
