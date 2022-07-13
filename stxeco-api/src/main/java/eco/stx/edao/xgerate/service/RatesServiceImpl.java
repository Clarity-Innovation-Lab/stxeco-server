package eco.stx.edao.xgerate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import eco.stx.edao.xgerate.service.domain.BinanceRate;
import eco.stx.edao.xgerate.service.domain.TickerRate;

@Service
public class RatesServiceImpl implements RatesService {

	@Autowired private MongoTemplate mongoTemplate;

	@Override
	public BinanceRate findLatestBinanceRate() {
		Query query = new Query();
		query.addCriteria(Criteria.where("symbol").is("STXBTC"));
		query.with(Sort.by(Sort.Direction.DESC, "closeTime"));
		query.limit(1);
		BinanceRate rate = mongoTemplate.findOne(query, BinanceRate.class);
		return rate;
	}

	@Override
	public BinanceRate findLatestBinanceEthRate() {
		Query query = new Query();
		query.addCriteria(Criteria.where("symbol").is("ETHBTC"));
		query.with(Sort.by(Sort.Direction.DESC, "closeTime"));
		query.limit(1);
		BinanceRate rate = mongoTemplate.findOne(query, BinanceRate.class);
		return rate;
	}

	@Override
	public TickerRate findLatestTickerRate() {
		Query q = new Query().with(Sort.by(Sort.Direction.ASC, "currency")).limit(1);
		TickerRate rate = mongoTemplate.findOne(q, TickerRate.class);
		BinanceRate binanceRate = findLatestBinanceRate();
		BinanceRate binanceEthRate = findLatestBinanceEthRate();
		rate.setStxPrice(rate.getLast() * binanceRate.getWeightedAvgPrice());
		rate.setEthPrice(rate.getLast() * binanceEthRate.getWeightedAvgPrice());
		return rate;
	}

	@Override
	public List<BinanceRate> findBinanceRatesByCloseTime(Integer limit) {
		Query query = new Query();
		query.addCriteria(Criteria.where("symbol").is("STXBTC"));
		query.with(Sort.by(Sort.Direction.DESC, "closeTime"));
		if (limit != null) query.limit(limit);
		List<BinanceRate> rates = mongoTemplate.find(query, BinanceRate.class);
		return rates;
	}

	@Override
	public List<TickerRate> findTickerRatesByCloseTime(Integer limit) {
		BinanceRate binanceRate = findLatestBinanceRate();
		BinanceRate binanceEthRate = findLatestBinanceEthRate();
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.ASC, "currency"));
		if (limit != null) query.limit(limit);
		List<TickerRate> rates = mongoTemplate.find(query, TickerRate.class);
		for (TickerRate tickerRate : rates) {
			tickerRate.setStxPrice(tickerRate.getLast() * binanceRate.getWeightedAvgPrice());
			tickerRate.setEthPrice(tickerRate.getLast() * binanceEthRate.getWeightedAvgPrice());
		}
		return rates;
	}
}
