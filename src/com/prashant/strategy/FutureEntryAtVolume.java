package com.prashant.strategy;


import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.prashant.ordermgmt.InformationThroughKiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Quote;
import com.zerodhatech.models.Tick;

public class FutureEntryAtVolume extends AbstractStrategy {

	final static Logger logger = Logger.getLogger(FutureEntryAtVolume.class);

	public static void main(String[] args) throws Exception, KiteException {

		FutureEntryAtVolume futureEntryAtVolume = new FutureEntryAtVolume();
		futureEntryAtVolume.createPosition();

	}

	public void createPosition() {
		if (waitForVolumeTrgigger(strategyData.orderParamList.get(0))) {
			createOrdersAfterEntryCondition();
		}

	}

	public FutureEntryAtVolume() {

		init();
	}

	public void init() {
		strategyData = new StrategyData();
		strategyData.setOrderParamList(getOrderParamsList());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public List<OrderParams> getOrderParamsList() {
		OrderParams orderParams = new OrderParams();

		orderParams.orderType = Constants.ORDER_TYPE_LIMIT;

		orderParams.product = Constants.PRODUCT_MIS;
		orderParams.exchange = Constants.EXCHANGE_NFO;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
		orderParams.validity = Constants.VALIDITY_DAY;

		orderParams.tradingsymbol = "BANKNIFTY19NOVFUT";
		orderParams.quantity = 40;
		orderParams.price = 28000.0;

		orderParams.trailingStoploss = 10.0;
		orderParams.stoploss = 50.0;
		orderParams.squareoff = 50.0;
		orderParams.triggerPrice = 0.0;

		// orderParams.tag = "myTag";

		List<OrderParams> orderParamsList = new LinkedList<OrderParams>();
		orderParamsList.add(orderParams);
		// orderParamsList.add(orderParams2);
		// orderParamsList.add(orderParams3);
		// orderParamsList.add(orderParams4);

		return orderParamsList;

	}

	public void createOrdersAfterEntryCondition() {

		OrderParams orderParams = strategyData.getOrderParamList().get(0);

		orderParams.price = InformationThroughKiteConnect.getLTP(orderParams.tradingsymbol);

		if (orderParams.transactionType.equals(Constants.TRANSACTION_TYPE_BUY)) {
			orderParams.price = orderParams.price + 10;
		} else {
			orderParams.price = orderParams.price - 10;
		}

		Order bracketOrder = placeBracketOrder(orderParams);
		strategyOrders.add(bracketOrder);

	}

	public boolean waitForVolumeTrgigger(OrderParams orderParams) {

		double lastVolume, totalVolume, volume = 0;
		Tick tick = InformationThroughKiteConnect.getLastTick(orderParams.tradingsymbol);

		totalVolume = tick.getVolumeTradedToday();

		do {

			try {
				Thread.sleep(59000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastVolume = totalVolume;
			tick = InformationThroughKiteConnect.getLastTick(orderParams.tradingsymbol);
			totalVolume = tick.getVolumeTradedToday();
			volume = totalVolume - lastVolume;
			logger.debug("Volume in last unit is " + volume);

		} while (volume < 24000);

		if (orderParams.transactionType.equals(Constants.TRANSACTION_TYPE_BUY)) {
			logger.info("Volume high. Transaction buy. Checking if price dipped");
			if (tick.getLastTradedPrice() > orderParams.price) {
				logger.info("last price is higher than specified. Not triggering the buy call");
				return false;
			}

		}

		if (orderParams.transactionType.equals(Constants.TRANSACTION_TYPE_SELL)) {
			logger.info("Volume high. Transaction Sell. Checking if price rose");
			if (tick.getLastTradedPrice() > orderParams.price) {
				logger.info("last price is lower than specified. Not triggering the sell call");
				return false;
			}

		}

		return true;
	}

}
