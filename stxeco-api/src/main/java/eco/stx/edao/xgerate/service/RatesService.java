package eco.stx.edao.xgerate.service;

import java.util.List;

import eco.stx.edao.xgerate.service.domain.BinanceRate;
import eco.stx.edao.xgerate.service.domain.TickerRate;


public interface RatesService
{
	public BinanceRate findLatestBinanceRate();

	public BinanceRate findLatestBinanceEthRate();

	public TickerRate findLatestTickerRate();

	List<BinanceRate> findBinanceRatesByCloseTime(Integer limit);

	public List<TickerRate> findTickerRatesByCloseTime(Integer limit);
}
