package eco.stx.edao.stacks.model.stxbuffer;

import java.io.IOException;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@TypeAlias(value = "StacksResponse")
public class StacksResponse {

	private String result;
	private String okay;
	private int value;

	public StacksResponse() {
		super();
	}
	public static class Deserializer extends StdDeserializer<StacksResponse> {

		private static final long serialVersionUID = 1L;

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public StacksResponse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			StacksResponse im = new StacksResponse();
			im.setResult(node.get("result").asText());
			im.setOkay(node.get("okay").asText());
			// construct buffer reader..
			return im;
		}
	}
	
	public String getResultWithoutPrefix() {
		if (result == null) {
			return result;
		}
		String hexWithoutPrefix = result.startsWith("0x") ? result.substring(2) : result;
		return hexWithoutPrefix;
	}
}
