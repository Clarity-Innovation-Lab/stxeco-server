package eco.stx.edao.xgerate.service.domain;

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
public class FeeRate {

	private String feeName;
	private Long fastestFee;
	private Long halfHourFee;
	private Long hourFee;

	public FeeRate() {
		super();
		this.feeName = "fees";
	}
	public static class Deserializer extends StdDeserializer<FeeRate> {

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public FeeRate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			FeeRate im = new FeeRate();
			im.setFastestFee(node.get("fastestFee").asLong());
			im.setHalfHourFee(node.get("halfHourFee").asLong());
			im.setHourFee(node.get("hourFee").asLong());
			return im;
		}
	}
}
