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
@TypeAlias(value = "FeeRatePrediction")
public class FeeRatePrediction {

	private Long minFee;
	private Long maxFee;
	private Long dayCount;
	private Long memCount;
	private Long minDelay;
	private Long maxDelay;
	private Long minMinutes;
	private Long maxMinutes;

	public FeeRatePrediction() {
		super();
	}
	
	public static class Deserializer extends StdDeserializer<FeeRatePrediction> {

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public FeeRatePrediction deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			FeeRatePrediction im = new FeeRatePrediction();
			im.setMinFee(node.get("minFee").asLong());
			im.setMaxFee(node.get("maxFee").asLong());
			im.setDayCount(node.get("dayCount").asLong());
			im.setMemCount(node.get("memCount").asLong());
			im.setMinDelay(node.get("minDelay").asLong());
			im.setMaxDelay(node.get("maxDelay").asLong());
			im.setMinMinutes(node.get("minMinutes").asLong());
			im.setMaxMinutes(node.get("maxMinutes").asLong());
			return im;
		}
	}
}
