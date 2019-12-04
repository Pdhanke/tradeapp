package com.prashant.control;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.prashant.database.DBUtil;
import com.prashant.ordermgmt.InformationThroughKiteConnect;
import com.prashant.ordermgmt.OrderThroughKiteConnect;
import com.prashant.util.Utils;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Position;

public class DaemonProcess {

	public static double MAXLOSS = -30000.0;

	public static void main(String args[]) throws InterruptedException, JSONException, IOException, KiteException {
		execute(args);

	}

	public static void execute (String args []) throws InterruptedException, JSONException, IOException, KiteException {
		while(true) {
		checkDailyLossLimit();	
			
		Thread.sleep(60000);
		}
	}
	private final static Logger logger = Logger.getLogger(DaemonProcess.class);

	public DaemonProcess() {
		// TODO Auto-generated constructor stub
	}

	public static void checkDailyLossLimit() throws JSONException, IOException, KiteException {

		double pnl = InformationThroughKiteConnect.getPnL();
		if (pnl < MAXLOSS) { // 1. Cancel all open orders
			OrderThroughKiteConnect.cancelAllOrders();

			// 2. Close all open positions
			List<Position> positions = InformationThroughKiteConnect.getPositions();
			if(positions.size() ==0) {
			logger.warn("no positions");
			}
			for (Position position : positions) {
				OrderParams orderParams = new OrderParams();
				orderParams.exchange = position.exchange;
				orderParams.tradingsymbol = position.tradingSymbol;

				orderParams.product = position.product;

				orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
				orderParams.price = position.lastPrice;

				if (position.tradingSymbol.contains("NIFTY")) {
					orderParams.orderType = Constants.ORDER_TYPE_MARKET;
				}

				if (position.netQuantity > 0) {
					orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
					orderParams.quantity = position.netQuantity;
				} else {
					orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
					orderParams.quantity = -1 * position.netQuantity;
				}
				orderParams.validity = Constants.VALIDITY_DAY;
				Utils.printOrderparams(orderParams);
				OrderThroughKiteConnect.placeOrder(orderParams);

			}

		}

	}



}
