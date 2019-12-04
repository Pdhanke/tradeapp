package com.prashant.feedHandlers;

import com.neovisionaries.ws.client.WebSocketException;
import com.prashant.database.DBUtil;
import com.prashant.ordermgmt.KiteConnectManager;
import com.prashant.ordermgmt.OrderThroughKiteConnect;
import com.prashant.rabbitmq.BroadCastTick;
import com.prashant.util.TickerUtils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;
import com.zerodhatech.ticker.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by PD on 15/10/16.
 */
public class TickerHandler {
	public String TABLENAME = "tickdataoct";
	private static KiteTicker tickerProvider ;
	private BroadCastTick broadCastTick ;
	

	final static Logger logger = Logger.getLogger(TickerHandler.class);

	private static KiteConnect kiteConnect = KiteConnectManager.getKiteConnect();
	int skipCount = 0;
	int downSampleValue = 5;

	public TickerHandler () throws Exception
	{
		broadCastTick= new BroadCastTick();
	}
	
	public static void subscribe(ArrayList<Long> tokens) {
		logger.info("Subscribing tokens");
		tickerProvider.subscribe(tokens);
		
	}
	
	/** Logout user. */
	public void logout(KiteConnect kiteConnect) throws KiteException, IOException {
		/** Logout user and kill session. */
		JSONObject jsonObject10 = kiteConnect.logout();
		System.out.println(jsonObject10);
	}
	


	/**
	 * Demonstrates com.zerodhatech.ticker connection, subcribing for instruments,
	 * unsubscribing for instruments, set mode of tick data, com.zerodhatech.ticker
	 * disconnection
	 */
	public void tickerUsage(ArrayList<Long> tokens) throws IOException, WebSocketException, KiteException {
		/**
		 * To get live price use websocket connection. It is recommended to use only one
		 * websocket connection at any point of time and make sure you stop connection,
		 * once user goes out of app. custom url points to new endpoint which can be
		 * used till complete Kite Connect 3 migration is done.
		 */
		tickerProvider = new KiteTicker(kiteConnect.getAccessToken(), kiteConnect.getApiKey());
		DBUtil dbUtil = new DBUtil();
	
		dbUtil.initializeJDBCConn();
		dbUtil.createDaysStreamingTickTable(TABLENAME);

		tickerProvider.setOnConnectedListener(new OnConnect() {
			@Override
			public void onConnected() {
				/**
				 * Subscribe ticks for token. By default, all tokens are subscribed for
				 * modeQuote.
				 */
				tickerProvider.subscribe(tokens);
				logger.info("subscribing to tokens: " + tokens.size());
				tickerProvider.setMode(tokens, KiteTicker.modeFull);
				// tickerProvider.setMode(tokens, KiteTicker.modeLTP);
			}
		});

		tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
			@Override
			public void onDisconnected() {
				logger.info("On Disconnect");
				// your code goes here
			}
		});

		/** Set listener to get order updates. */
		tickerProvider.setOnOrderUpdateListener(new OnOrderUpdate() {
			@Override
			public void onOrderUpdate(Order order) {
				logger.info("order update " + order.orderId);
				
			}
		});

		/** Set error listener to listen to errors. */
		tickerProvider.setOnErrorListener(new OnError() {
			@Override
			public void onError(Exception exception) {
				// handle here.
			}

			@Override
			public void onError(KiteException kiteException) {
				// handle here.
			}
		});

		tickerProvider.setOnTickerArrivalListener(new OnTicks() {
			@Override
			public void onTicks(ArrayList<Tick> ticks) {
				//skipCount++;
				
				logger.debug("ticks size " + ticks.size());
				int local= 0;
			
				if (ticks.size() > 0 && local==0) {
					dbUtil.storeTickData(ticks);
//					try {
//						broadCastTick.publishTicks(ticks);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					//skipCount = 0;				

				}

			}
		});
		// Make sure this is called before calling connect.
		tickerProvider.setTryReconnection(true);
		// maximum retries and should be greater than 0
		tickerProvider.setMaximumRetries(10);
		// set maximum retry interval in seconds
		tickerProvider.setMaximumRetryInterval(30);

		/**
		 * connects to com.zerodhatech.com.zerodhatech.ticker server for getting live
		 * quotes
		 */
		tickerProvider.connect();

		/**
		 * You can check, if websocket connection is open or not using the following
		 * method.
		 */
		boolean isConnected = tickerProvider.isConnectionOpen();
		logger.info(isConnected);

		/**
		 * set mode is used to set mode in which you need tick for list of tokens.
		 * Ticker allows three modes, modeFull, modeQuote, modeLTP. For getting only
		 * last traded price, use modeLTP For getting last traded price, last traded
		 * quantity, average price, volume traded today, total sell quantity and total
		 * buy quantity, open, high, low, close, change, use modeQuote For getting all
		 * data with depth, use modeFull
		 */
		tickerProvider.setMode(tokens, KiteTicker.modeLTP);

		try {
			Thread.sleep(30000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Unsubscribe for a token.
		tickerProvider.unsubscribe(tokens);
		dbUtil.closeJDBCConn();

		// After using com.zerodhatech.com.zerodhatech.ticker, close websocket
		// connection.
		tickerProvider.disconnect();
	}

	public  void callTickerHandler(String tickerToRun) throws IOException, WebSocketException, KiteException {
		

			logger.info("Running ticker");
	
	    	
	    	ArrayList<Long> tokens = TickerUtils.getInstrumentTokens(0);
	    //	tokens.addAll(TickerUtils.getInstrumentTokens(1));
			this.tickerUsage( tokens);
			
	
		
		
	}

}
