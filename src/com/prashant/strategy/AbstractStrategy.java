package com.prashant.strategy;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.prashant.ordermgmt.InformationThroughKiteConnect;
import com.prashant.ordermgmt.KiteConnectManager;
import com.prashant.ordermgmt.OrderThroughKiteConnect;
import com.prashant.rabbitmq.BroadCastTick;
import com.prashant.util.TickerUtils;
import com.prashant.util.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Position;

public abstract class AbstractStrategy implements Runnable {

	final static Logger logger = Logger.getLogger(OrderThroughKiteConnect.class);
	 private static final String EXCHANGE_NAME = "topic_logs";

	protected StrategyData strategyData;
	protected List<Order> strategyOrders = new LinkedList<Order>();

	public StrategyData getStrategyData() {
		return strategyData;
	}

	public void setStrategyData(StrategyData strategyData) {
		this.strategyData = strategyData;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int strategyID;
	private int state;

	private long waitTimeForCreatePosition = 2000;
	private int currentQuantity = 0;
	
	public AbstractStrategy () {
		init();
	}
	
	public void init () {
		
	}
	
	public void initMessageTicks () throws IOException, TimeoutException, KiteException
	{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueDeclare("Ticks", false, false, false, null);
        String queueName = "Ticks";
        
        
       String tradingSymbol = strategyData.getOrderParamList().get(0).tradingsymbol;
       String bindingKey = "" + TickerUtils.getInstrumentTokenForTradingSymbol(tradingSymbol);
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            updateTick(8.0,9.0);
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}
	
	public void updateTick(double price, double volume) {
		
	}

	public List<OrderParams> geOrderParamsfromProperties() {
		return null;
	}

	public double getCurrentPnL() {
		double pnl = InformationThroughKiteConnect.getPnL();
		logger.debug("Current pnl is : " + pnl);
		return pnl;
	}

	/**
	 * Creates Position and places SL orders if mentioned.
	 * 
	 * @param orderParams
	 * @return TODO
	 */
	public void createPosition() {
		List<OrderParams> orderParams = strategyData.getOrderParamList();
		List<Order> orders = new LinkedList<Order>();
		int incrementOrderSize = 40;
		if (orderParams.get(0).tradingsymbol.startsWith("N")) {
			incrementOrderSize = 150;
		}

		int totalQuantity = orderParams.get(0).quantity;
		int iterRequired = totalQuantity / incrementOrderSize;

		logger.debug("Total quantiity and iteration required are : " + totalQuantity + " , " + iterRequired);

		for (int i = 0; i < iterRequired; i++) {
			for (OrderParams orderParam : orderParams) {
				logger.debug("Iteration number : " + i + " for : " + orderParam.tradingsymbol);
				orderParam.quantity = incrementOrderSize;
				orderParam.orderType = Constants.ORDER_TYPE_LIMIT;
				
				orderParam.price = getLtp(orderParam);

				Order order = placeOrder(orderParam);
				strategyOrders.add(order);
			}

			try {
				Thread.sleep(waitTimeForCreatePosition);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		/**
		 * Convert limit orders to marketS
		 */
		try {
			Thread.sleep(2*waitTimeForCreatePosition);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		convertLimitToMarket(orderParams);

		/**
		 * Restore the oderParam quantity to original
		 */
		for (OrderParams orderParam : orderParams) {
			orderParam.quantity = totalQuantity;

		}
		
		

	}

	public void partialBook(List<OrderParams> orderParams, int factor, int mode) {
		
		
		int incrementOrderSize = 20;
		if (orderParams.get(0).tradingsymbol.startsWith("N")) {
			incrementOrderSize = 150;
		}

		int totalQuantity = orderParams.get(0).quantity / factor;
		int iterRequired = totalQuantity / incrementOrderSize;

		/**
		 * mode = 0 implies order to be executed immediately
		 */
		if (mode == 0) {
			totalQuantity = orderParams.get(0).quantity;
			iterRequired = 1;
		}

		logger.debug("Partial quantiity and iteration required are : " + totalQuantity + " , " + iterRequired);

		for (OrderParams orderParam : orderParams) {
			Utils.printOrderparams(orderParam);
			if(mode!=0) {
			orderParam.quantity = incrementOrderSize;
			}
			if (orderParam.transactionType == "BUY") {
				orderParam.transactionType = "SELL";
			} else {
				orderParam.transactionType = "BUY";
			}
		}
		for (int i = 0; i < iterRequired; i++) {
			for (OrderParams orderParam : orderParams) {
				logger.debug("Iteration number : " + i + " for : " + orderParam.tradingsymbol);
				orderParam.orderType = Constants.ORDER_TYPE_MARKET;
				

				if (mode != 0) {
					orderParam.orderType = Constants.ORDER_TYPE_LIMIT;
					orderParam.price = 190.0;
					orderParam.price = getLtp(orderParam);
				}

				Utils.printOrderparams(orderParam);
				Order order = placeOrder(orderParam);
				strategyOrders.add(order);

			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
		/**
		 * Convert limit orders to marketS
		 */
		
		convertLimitToMarket(orderParams);
		
		for (OrderParams orderParam : orderParams) {
			Utils.printOrderparams(orderParam);

			if (orderParam.transactionType == "BUY") {
				orderParam.transactionType = "SELL";
			} else {
				orderParam.transactionType = "BUY";
			}
		}
		
		/**
		 * Restore the oderParam quantity to original
		 */
		for (OrderParams orderParam : orderParams) {
			orderParam.quantity = totalQuantity * factor;
			
		}
		
	

	}

	public Order placeOrder(OrderParams orderParams) {
		return OrderThroughKiteConnect.placeOrder(orderParams);
	}
	
	public Order placeBracketOrder(OrderParams orderParams) {
		return OrderThroughKiteConnect.placeBracketOrder(orderParams);
	}

	public double getLtp(OrderParams orderParams) {
		logger.debug("Getting ltp for"+  orderParams.exchange + ":" +orderParams.tradingsymbol);
	//	double ltp = InformationThroughKiteConnect.getLTP(orderParams.exchange + ":" +orderParams.tradingsymbol);
	
		double ltp = InformationThroughKiteConnect.getLTP(orderParams.tradingsymbol);
		logger.debug("LTP for " + orderParams.tradingsymbol + " : " + ltp);
		return ltp;
	}

	private void convertLimitToMarket(List<OrderParams> orderParamList) {
		
		OrderThroughKiteConnect.convertLimitToMarket(orderParamList);
	}

	public void monitorForLossProfit() {

		while (currentQuantity > 0) {

			if (getPnlForPositions() < -5000) {
				closePositions();
			}

			if (getPnlForPositions() > 5000) {
				// move SL and trail
			}

		}

		try {
			Thread.sleep(waitTimeForCreatePosition);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<Position> getPositionsCreated() {
		/*
		 * get Positions created for Strategy
		 */
		return null;
	}

	public long getPnlForPositions() {

		return 0;
	}

	public void monitor(List<OrderParams> orderParams)  {
		
		int profitBookFactor = 4;
		double bookLossAt = -8000;
		while (true) {

			double pnl = getCurrentPnL();
//			if (pnl > 15000 && profitBookFactor>3) {
//				partialBook(orderParams, 4, 1);
//				profitBookFactor--;
//				bookLossAt = 4000;
//			}
//			if (pnl > 19000 && profitBookFactor>2) {
//				partialBook(orderParams, 2, 1);
//				profitBookFactor--;
//				bookLossAt = 9000;
//			}
//			if (pnl < bookLossAt) {
//				partialBook(orderParams, 1, 1);
//				break;
//			}
			
			if (pnl > 8000 ) {
				partialBook(orderParams, 2, 1);
				profitBookFactor--;
				bookLossAt = 1000;
				break;
			}
			if (pnl < bookLossAt) {
				partialBook(orderParams, 1, 1);
				break;
			}

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// if(pnl<strategyData.getInitialStopLoss()) {
			// System.out.println("BNWStrangleStrategy.monitor()");
			// }
			//
			// Thread.sleep(strategyData.getWaitTimeForCreatePosition());
		}

	}

	public void closePositions() {

		List<OrderParams> orderParamList = strategyData.getOrderParamList();

		for (OrderParams orderParams : orderParamList) {
			if (orderParams.orderType.equals("SL")) {
				int totalquant = orderParams.quantity;

			}
		}
		
	}

		/**
		 * Cancel SL orders Initiate Close Orders.
		 */
		protected void cancelOpenOrdedsForStrategy (List<Order> orders) throws JSONException, IOException, KiteException {
			
			for ( Order order : orders) {
				logger.debug("Cancelling order :" + order.orderId);
				OrderThroughKiteConnect.cancelOrder(order);
			}
		}
	
}
