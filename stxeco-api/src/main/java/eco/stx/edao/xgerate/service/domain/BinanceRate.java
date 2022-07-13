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
@TypeAlias(value = "BinanceRate")
public class BinanceRate {

	private String symbol;
	private Double askPrice;
	private Double askQty;
	private Double bidPrice;
	private Double bidQty;
	private Double priceChange;
	private Double priceChangePercent;
	private Double prevClosePrice;
	private Double lastPrice;
	private Double lastQty;
	private Double openPrice;
	private Double highPrice;
	private Double lowPrice;
	private Double quoteVolume;
	private Double volume;
	private Double weightedAvgPrice;
	private Long openTime;
	private Long closeTime;
	private Long firstId;
	private Long lastId;
	private Long count;

	public BinanceRate() {
		super();
	}

	public static class Deserializer extends StdDeserializer<BinanceRate> {

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public BinanceRate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			BinanceRate im = new BinanceRate();
			im.setSymbol(node.get("symbol").asText());
			im.setAskPrice(node.get("askPrice").asDouble());
			im.setAskQty(node.get("askQty").asDouble());
			im.setBidPrice(node.get("bidPrice").asDouble());
			im.setBidQty(node.get("bidQty").asDouble());
			im.setPriceChange(node.get("priceChange").asDouble());
			im.setPriceChangePercent(node.get("priceChangePercent").asDouble());
			im.setPrevClosePrice(node.get("prevClosePrice").asDouble());
			im.setLastPrice(node.get("lastPrice").asDouble());
			im.setOpenPrice(node.get("openPrice").asDouble());
			im.setHighPrice(node.get("highPrice").asDouble());
			im.setLowPrice(node.get("lowPrice").asDouble());
			im.setQuoteVolume(node.get("quoteVolume").asDouble());
			im.setVolume(node.get("volume").asDouble());
			im.setWeightedAvgPrice(node.get("weightedAvgPrice").asDouble());
			im.setOpenTime(node.get("openTime").asLong());
			im.setCloseTime(node.get("closeTime").asLong());
			im.setFirstId(node.get("firstId").asLong());
			im.setLastId(node.get("lastId").asLong());
			im.setCount(node.get("count").asLong());
			return im;
		}
	}
}
