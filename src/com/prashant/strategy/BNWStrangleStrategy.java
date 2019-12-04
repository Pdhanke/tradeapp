package com.prashant.strategy;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.prashant.ordermgmt.KiteConnectManager;
import com.prashant.ordermgmt.OrderThroughKiteConnect;
import com.prashant.util.XMLWrite;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Position;

public class BNWStrangleStrategy extends AbstractStrategy {
	

	private List<Position> positions;
	
	
	public final static Logger logger = Logger.getLogger(BNWStrangleStrategy.class);
	
	public static void main(String[] args) throws Exception, KiteException {

		
		
		BNWStrangleStrategy bnwStrangleStrategy = new BNWStrangleStrategy();
		bnwStrangleStrategy.createPosition();

		bnwStrangleStrategy.monitor();

	}
	
	public  BNWStrangleStrategy() {
		// TODO Auto-generated constructor stub

		init();
	}
	
	
	public void init () {
		strategyData = new StrategyData();
		strategyData.setOrderParamList(getOrderParamsList());
	}
	
	private   List<OrderParams> getOrderParamsList () {
		OrderParams orderParams = new OrderParams();
		orderParams.quantity = 800;
		orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
		orderParams.tradingsymbol = "BANKNIFTY19O2429500CE";
		//orderParams.tradingsymbol = "NIFTY19OCT11800CE";
		orderParams.product = Constants.PRODUCT_MIS	;
		orderParams.exchange = Constants.EXCHANGE_NFO;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
		orderParams.validity = Constants.VALIDITY_DAY;
		orderParams.price = 100.0;
		orderParams.triggerPrice = 0.0;
		//orderParams.tag = "myTag"; 
		
	OrderParams orderParams2 = new OrderParams();
		orderParams2.quantity = orderParams.quantity;
		orderParams2.orderType = Constants.ORDER_TYPE_LIMIT;
		orderParams2.tradingsymbol = "BANKNIFTY19O2429100PE";
		//orderParams.tradingsymbol = "NIFTY19OCT11500PE";
		orderParams2.product =orderParams.product;
		orderParams2.exchange = orderParams.exchange;
		orderParams2.transactionType = orderParams.transactionType;
		orderParams2.validity = orderParams.validity;
		orderParams2.price = 100.00;
		orderParams2.triggerPrice = 0.0;
		//orderParams2.tag = "myTag"; 
		
		
		List<OrderParams> orderParamsList = new LinkedList<OrderParams>();
		orderParamsList.add(orderParams);
		orderParamsList.add(orderParams2);
//		orderParamsList.add(orderParams3);
//		orderParamsList.add(orderParams4);
		
		return orderParamsList;
    	
    }
	
	public void loadStrategyData() {
		//XMLWrite.getStrategyDatefromXMLFile("strategyfile");
	}
	
	
	public void monitor()  {
		List<OrderParams> orderParams = strategyData.getOrderParamList();
		
		double currentSumOfSoldOptions = 0.0;
		double trailingStopLoss = 20;
		double minSumTillNow = 99999999999999.0;
		double firstTarget = 20;
		
		double ltp1 = getLtp(orderParams.get(0));
		double ltp2 = getLtp(orderParams.get(1));
		double startSum = ltp1 + ltp2;
	
		while (true) {
			
			ltp1 = getLtp(orderParams.get(0));
			ltp2 = getLtp(orderParams.get(1));

			currentSumOfSoldOptions = ltp1 + ltp2;
			if (minSumTillNow > currentSumOfSoldOptions ) {
				minSumTillNow = currentSumOfSoldOptions;
			}
			
			logger.info("ltp1, ltp2, currentsum, min sum are : " + ltp1 + " , " + ltp2 + " , " +currentSumOfSoldOptions +" , " + minSumTillNow);

			//book profit fully
			if (currentSumOfSoldOptions > (minSumTillNow + trailingStopLoss) ) {
				partialBook(orderParams, 1, 0);
				
				
				break;
			}

			//book profit partially
			if (currentSumOfSoldOptions < (startSum - firstTarget) ) {
				partialBook(orderParams,2, 1);
				orderParams.get(0).quantity = orderParams.get(0).quantity/2;
				orderParams.get(0).quantity = orderParams.get(0).quantity/2;
				firstTarget = firstTarget * 2;
				
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	

	}
	
	
	
	
	
	
	
	
	


