package eco.stx.edao.eco.userPropoerties.service.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import eco.stx.edao.eco.daoProperties.api.model.DaoPropertyTypeValue;
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
@TypeAlias(value = "UserProperty")
@Document
public class UserProperty {

	@Id private String id;
	private String stxAddress;
	private String property;
	public DaoPropertyTypeValue value;
	public String contractName;
	public String functionName;

}
