package com.prashant.ordermgmt;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.prashant.database.DBUtil;
import com.prashant.feedHandlers.TickerHandler;
import com.prashant.strategy.BNWStrangleStrategy;
import com.prashant.util.Utils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;

public class OrderThroughKiteConnect {

	public final static Logger logger = Logger.getLogger(OrderThroughKiteConnect.class);

	public static KiteConnect kiteConnect = KiteConnectManager.getKiteConnect();
	
	public static void main(String[] args) throws KiteException, Exception {
		execute(args);
	}

	public static void execute(String[] args) throws Exception, KiteException {

		kiteConnect = KiteConnectManager.getKiteConnect();
		// writeInstrumentTokenSymboltoDB();

		// getHistoricalData(kiteConnect);
		// String tickerToRun = args[0];
		// String tickerToRun = "1";

		// while (true) {
		// BNWFuture bnfFuture = new BNWFuture();
		// try {
		// bnfFuture.startStrategy(getOrderParamsList().get(0));
		// } catch (KiteException e) {
		// logger.error(e.message);
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// Thread.sleep(300000);
		// }

		 BNWStrangleStrategy bnwStrangleStrategy = new BNWStrangleStrategy();
		bnwStrangleStrategy.createPosition();

		bnwStrangleStrategy.monitor();

	}

	public static Order placeOrder(OrderParams orderParams) {

		Order order = null;
		try {

			order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
			logger.info("order id : " + order.orderId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return order;

	}
	
	public static Order placeBracketOrder(OrderParams orderParams) {

		Order order = null;
		try {

			order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_BO);
			logger.info("order id : " + order.orderId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return order;

	}

	public static void placeBulkOrder(List<OrderParams> orderParamList) {

		for (OrderParams orderParams : orderParamList) {
			placeOrder(orderParams);
		}

	}

	public static void cancelOrder(String orderId) throws JSONException, IOException, KiteException {
		logger.info("Cancelling order with id: " + orderId);
		kiteConnect.cancelOrder(orderId, Constants.VARIETY_REGULAR);
	}

	public static void cancelOrder(Order order) throws JSONException, IOException, KiteException {
		logger.info("Cancelling order with id: " + order.orderId);
		if (order.status != "COMPLETE") {
			cancelOrder(order.orderId);
		}
	}

	public static void cancelAllOrders() throws JSONException, IOException, KiteException {
		List<Order> openOrders = kiteConnect.getOrders();
		for (int i = 0; i <openOrders.size(); i ++) {
			Order order = openOrders.get(i);
			logger.debug("Order id : " + order.orderId + " Staus is : " + order.status.equals("REJECTED"));
			if (!(order.status == "COMPLETE" || order.status == "CANCELLED" || order.status == "REJECTED" ) ) {
				
				//cancelOrder(order.orderId);
				
			}

		}
	}

	public static void convertLimitToMarket(List<OrderParams> orderParamList) {

		try {
			List<Order> orders = kiteConnect.getOrders();
			for (Order order : orders) {
				logger.debug(" Checking for order with id " + order.orderId + "Order status" + order.status
						+ "Order type " + order.orderType);
				for (OrderParams orderParams : orderParamList) {
					if (order.orderType.equals(Constants.ORDER_TYPE_LIMIT) && order.status.equals("OPEN")) {
						orderParams.orderType = Constants.ORDER_TYPE_MARKET;
						orderParams.quantity = Integer.valueOf(order.pendingQuantity);
						logger.debug("converting limit to market for " + orderParams.tradingsymbol + " with quantity : "
								+ orderParams.quantity);
						kiteConnect.modifyOrder(order.orderId, orderParams, Constants.VARIETY_REGULAR);

					}

				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void logOut() throws JSONException, IOException, KiteException {
		logger.error("Logging out of Kiteconnect");
		kiteConnect.invalidateAccessToken();
		kiteConnect.logout();
		
	}

}
