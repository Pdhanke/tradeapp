package com.prashant.strategy;

import java.util.LinkedList;
import java.util.List;


import org.apache.log4j.Logger;

import com.prashant.ordermgmt.InformationThroughKiteConnect;
import com.prashant.util.Utils;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Quote;

public class OptionEntryAtFuturePrice extends FutureEntryAtPrice {

	final static Logger logger = Logger.getLogger(OptionEntryAtFuturePrice.class);

	
	public static void main(String[] args) throws Exception, KiteException {

		
		
		OptionEntryAtFuturePrice OptionEntryAtFuturePrice = new OptionEntryAtFuturePrice();
		OptionEntryAtFuturePrice.createPosition();
		

	}
	



	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public List<OrderParams> getOrderParamsList() {
		OrderParams orderParams = new OrderParams();
		orderParams.quantity = 100;
		orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
		orderParams.tradingsymbol = "BANKNIFTY19NOVFUT";
		orderParams.product = Constants.PRODUCT_MIS;
		orderParams.exchange = Constants.EXCHANGE_NFO;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
		orderParams.validity = Constants.VALIDITY_DAY;
		orderParams.price = 30718.0;
	//	orderParams.triggerPrice = 0.0;
		// orderParams.tag = "myTag";

		OrderParams orderParams2 = new OrderParams();
		orderParams2.quantity = orderParams.quantity ;
		orderParams2.orderType = Constants.ORDER_TYPE_LIMIT;
		orderParams2.tradingsymbol = "BANKNIFTY19N1430500PE";
		orderParams2.product = orderParams.product;
		orderParams2.exchange = orderParams.exchange;
		orderParams2.transactionType = Constants.TRANSACTION_TYPE_SELL;
		orderParams2.validity = orderParams.validity;
		orderParams2.price = 100.0;
		orderParams2.triggerPrice = 0.0;
		
		OrderParams orderParams3 = new OrderParams();
		orderParams3.quantity = orderParams.quantity ;
		orderParams3.orderType = Constants.ORDER_TYPE_SLM;
		orderParams3.tradingsymbol = "BANKNIFTY19OCTFUT";
		orderParams3.product = orderParams.product;
		orderParams3.exchange = orderParams.exchange;
		orderParams3.transactionType = Constants.TRANSACTION_TYPE_BUY;
		orderParams3.validity = orderParams.validity;
		orderParams3.price = 100.0;
		orderParams3.triggerPrice = 0.0;

		List<OrderParams> orderParamsList = new LinkedList<OrderParams>();
		orderParamsList.add(orderParams);
		orderParamsList.add(orderParams2);
//		orderParamsList.add(orderParams3);
		// orderParamsList.add(orderParams4);

		return orderParamsList;

	}

	public void createOrdersAfterEntryCondition()  {
		

		OrderParams orderParams = strategyData.getOrderParamList().get(1);
		

	
			

			orderParams.trailingStoploss = 20.0;
	        orderParams.stoploss = 20.0;
	        orderParams.squareoff = 20.0;

	        Utils.printOrderparams(orderParams);
	        Order bracketOrder = placeBracketOrder(orderParams);
	        strategyOrders.add(bracketOrder);
		

	}
	/*
	 * TODO
	 */
	public void trailPosition() {
		
		Order targetOrder = strategyOrders.get(1);
		Order slOrder = strategyOrders.get(2);
		
		while(true) {
			
			if(slOrder.status== "done") {
				targetOrder.status="cancel";
			}
		}
		

	}



	private Quote getQoute(OrderParams orderParams) throws KiteException {

		return InformationThroughKiteConnect.getQuote(orderParams.tradingsymbol);

	}

}
