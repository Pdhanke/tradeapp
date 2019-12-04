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

public class FutureEntryAtPrice extends AbstractStrategy {

	final static Logger logger = Logger.getLogger(FutureEntryAtPrice.class);

	public static void main(String[] args) throws Exception, KiteException {

		FutureEntryAtPrice futureEntryAtPrice = new FutureEntryAtPrice();
		futureEntryAtPrice.createPosition();
		
	}

	public void createPosition() {
		waitForEntryPrice(strategyData.orderParamList.get(0));
		createOrdersAfterEntryCondition();

	}

	public FutureEntryAtPrice() {
		// TODO Auto-generated constructor stub

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
		orderParams.quantity = 40;
		orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
		orderParams.tradingsymbol = "BANKNIFTY19NOVFUT";
		orderParams.product = Constants.PRODUCT_MIS;
		orderParams.exchange = Constants.EXCHANGE_NFO;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
		orderParams.validity = Constants.VALIDITY_DAY;
		orderParams.price = 31000.0;
		orderParams.triggerPrice = 0.0;


		List<OrderParams> orderParamsList = new LinkedList<OrderParams>();
		orderParamsList.add(orderParams);


		return orderParamsList;

	}

	public boolean waitForEntryPrice(OrderParams orderParams) {
		double ltp = 0.0;
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ltp = InformationThroughKiteConnect.getLTP(orderParams.tradingsymbol);

			if (orderParams.transactionType.equals(Constants.TRANSACTION_TYPE_BUY)) {
				if (ltp <= orderParams.price) {
					logger.info("ltp is below entryprice. ltp is : " + ltp);
					break;
				}
			}

			if (orderParams.transactionType.equals(Constants.TRANSACTION_TYPE_SELL)) {
				if (ltp >= orderParams.price) {
					logger.info("ltp is above entryprice. ltp is : " + ltp);
					break;
				}
			}

		}

		return true;

	}

	public void createOrdersAfterEntryCondition() {

		OrderParams orderParams = strategyData.getOrderParamList().get(0);


		orderParams.trailingStoploss = 50.0;
		orderParams.stoploss = 50.0;
		orderParams.squareoff = 50.0;
		Order bracketOrder = placeBracketOrder(orderParams);
		strategyOrders.add(bracketOrder);

	}

	/*
	 * TODO
	 */
	public void trailPosition() {



	}


}
