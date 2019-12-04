package com.prashant.control;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.neovisionaries.ws.client.WebSocketException;
import com.prashant.feedHandlers.HistoricalDataHandler;
import com.prashant.feedHandlers.QuoteHandler;
import com.prashant.feedHandlers.TickerHandler;
import com.prashant.ordermgmt.OrderThroughKiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

public class KiteMain {
	public final static Logger logger = Logger.getLogger(KiteMain.class);

	public KiteMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws KiteException, Exception {
		callTickerHandler();
		//OrderThroughKiteConnect.execute(args);
		
//		String tickOrQuote = args[0];
//		logger.info("args is" + tickOrQuote );
//		
//		if(tickOrQuote.equals("tick")) {
//			callTickerHandler();
//		}
//		else {
//			callQuoteHandler();
//		}
		

		
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

		// BNWStrangleStrategy bnwStrangleStrategy = new BNWStrangleStrategy();
		// bnwStrangleStrategy.createPosition(getOrderParamsList());

		// bnwStrangleStrategy.monitor(getOrderParamsList());

	}

	public static void callTickerHandler() throws IOException, WebSocketException, KiteException {
		
		TickerHandler tickerHandler = null;
		try {
			tickerHandler = new TickerHandler();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tickerHandler.callTickerHandler("weekly");

	}
	
	public static void callQuoteHandler () throws KiteException {
		
		QuoteHandler quoteHandler = new QuoteHandler();
		quoteHandler.handleQuotes();
	}
	
	public static void historicalDataHandler () throws KiteException {
		
		HistoricalDataHandler historicalDataHandler = new HistoricalDataHandler();
		historicalDataHandler.getHistoricalDataForAllInstruments();
	}



}
