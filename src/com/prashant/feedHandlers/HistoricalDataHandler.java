package com.prashant.feedHandlers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.prashant.database.DBUtil;
import com.prashant.ordermgmt.KiteConnectManager;
import com.prashant.ordermgmt.OrderThroughKiteConnect;
import com.prashant.util.TickerUtils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Quote;

public class HistoricalDataHandler {

	final static Logger logger = Logger.getLogger(HistoricalDataHandler.class);
	DBUtil dbUtil = new DBUtil();
	private static KiteConnect kiteConnect;

	public static KiteConnect getKiteConnect() {
		return kiteConnect;
	}

	public static void setKiteConnect() throws Exception {
		if (kiteConnect == null)
			HistoricalDataHandler.kiteConnect = KiteConnectManager.getKiteConnect();
	}

	public HistoricalDataHandler() {
		try {
			dbUtil.initializeJDBCConn();
			setKiteConnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// CREATE TABLE Historical (TradingSymbol VARCHAR(40) NOT NULL, Open FLOAT NOT
		// NULL, High FLOAT NOT NULL,Low FLOAT NOT NULL, Close FLOAT NOT NULL, Volume
		// BIGINT NOT NULL, Timestamp DATETIME NOT NULL, PRIMARY KEY (TradingSymbol,
		// Timestamp)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
		// TODO Auto-generated method stub

		HistoricalDataHandler historicalDataHandler = new HistoricalDataHandler();
		// historicalDataHandler.formatTimeStampFOrHistoricalDate("");
		
			historicalDataHandler.getHistoricalDataForAllInstruments();

	}

	public HistoricalData getHistoricalDataForSingleInstrument(KiteConnect kiteConnect, Date from, Date to,
			long instrumentToken, String interval) throws KiteException, IOException {
		/**
		 * Get historical data dump, requires from and to date, intrument token,
		 * interval, continuous (for expired F&O contracts) returns historical data
		 * object which will have list of historical data inside the object.
		 */

		HistoricalData historicalData = kiteConnect.getHistoricalData(from, to, "" + instrumentToken, "minute", false);
		List<HistoricalData> historyList = historicalData.dataArrayList;
		logger.info("getHistoricalData()" + historyList.size());
		
		
		dbUtil.storeHistoricalData(TickerUtils.tokenMap.get(instrumentToken).substring(4), historyList);

		return historicalData;

	}

	public void getHistoricalDataForAllInstruments ()
	  {


		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date from =  new Date();
	        Date to = new Date();
	        try {
	            from = formatter.parse("2019-09-12 15:18:00");
	            to = formatter.parse("2019-09-12 15:30:00");
	        }catch (ParseException e) {
	            e.printStackTrace();
	        }
		  
		  try {
			
		
			ArrayList<Long> instrumentTokens = TickerUtils.getInstrumentTokensForHistoricalData();
			
			for(int i =0; i<instrumentTokens.size(); i ++) {
				getHistoricalDataForSingleInstrument(kiteConnect, from, to, instrumentTokens.get(i), "minute");
				Thread.sleep(401);
			}
			
		} catch (KiteException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

}
