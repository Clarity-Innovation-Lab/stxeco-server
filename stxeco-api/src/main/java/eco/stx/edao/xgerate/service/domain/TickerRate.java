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
@TypeAlias(value = "TickerRate")
public class TickerRate {
	private String currency;
	private String symbol;
	private Double sell;
	private Double buy;
	private Double last;
	private Double fifteen;
	private Double stxPrice;
	private Double ethPrice;

	public TickerRate() {
		super();
	}
	public static class Deserializer extends StdDeserializer<TickerRate> {

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public TickerRate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			TickerRate im = new TickerRate();
			im.setSymbol(node.get("symbol").asText());
			im.setFifteen(node.get("15m").asDouble());
			im.setLast(node.get("last").asDouble());
			im.setBuy(node.get("buy").asDouble());
			im.setSell(node.get("sell").asDouble());
			return im;
		}
	}
}
