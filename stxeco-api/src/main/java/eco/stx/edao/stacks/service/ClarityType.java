package eco.stx.edao.stacks.service;

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
@TypeAlias(value = "ClarityType")
public class ClarityType {

	private int type;
	private int version;
	private Object value;
	private Object valueHex;
	
	public ClarityType(int type) {
		super();
		this.type = type;
	}
	public ClarityType(int type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}
	public ClarityType(int type, Object value, Object valueHex) {
		super();
		this.type = type;
		this.value = value;
		this.valueHex = valueHex;
	}
}
