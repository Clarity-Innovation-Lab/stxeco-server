package eco.stx.edao.stacks.service.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

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
@TypeAlias(value = "MempoolSettings")
@Document
public class MempoolSettings {
	
	@Id private String id;
	private Integer threshold1;
	private Integer threshold2;
	private Integer threshold3;
	
}
