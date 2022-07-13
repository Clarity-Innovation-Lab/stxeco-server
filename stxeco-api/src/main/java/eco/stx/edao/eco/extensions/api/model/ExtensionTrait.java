package eco.stx.edao.eco.extensions.api.model;

import org.springframework.data.annotation.TypeAlias;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@TypeAlias(value = "ExtensionTrait")
public class ExtensionTrait {

	public static String trait = "{\"maps\":[],\"functions\":[{\"args\":[{\"name\":\"sender\",\"type\":\"principal\"}],\"name\":\"execute\",\"access\":\"public\",\"outputs\":{\"type\":{\"response\":{\"ok\":\"bool\",\"error\":\"uint128\"}}}}],\"variables\":[],\"fungible_tokens\":[],\"non_fungible_tokens\":[]}";
	//public static String trait = "{'maps':[],'functions':[{'args':[{'name':'sender','type':'principal'}],'name':'execute','access':'public','outputs':{'type':{'response':{'ok':'bool','error':'uint128'}}}}],'variables':[],'fungible_tokens':[],'non_fungible_tokens':[]}";
}
