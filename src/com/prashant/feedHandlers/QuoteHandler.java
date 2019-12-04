package com.prashant.feedHandlers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.prashant.database.DBUtil;
import com.prashant.ordermgmt.InformationThroughKiteConnect;
import com.prashant.util.TickerUtils;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Quote;

public class QuoteHandler {

	public final static Logger logger = Logger.getLogger(QuoteHandler.class);


	private List<String> tradingSymbols;
	private Map<String, Quote> quoteMap;
	
	private DBUtil dbUtil;
	
	public QuoteHandler ()
	{
		dbUtil = new DBUtil();
		dbUtil.initializeJDBCConn();
	}

	public void handleQuotes() throws KiteException {

		tradingSymbols = TickerUtils.getRelevantSymbols();
		String[] tradingSymbolArray = tradingSymbols.toArray(new String[tradingSymbols.size()]);
		
//		quoteMap = getQuoteMap(tradingSymbolArray);
//		storeQuotesinDB(quoteMap);
		
		LocalTime startTime = LocalTime.parse("09:15:00");
		LocalTime endTime = LocalTime.parse("19:30:00");
		LocalTime target = LocalTime.now();
		
		
		while (target.isAfter(startTime) && target.isBefore(endTime)) {
			
			
			quoteMap = getQuoteMap(tradingSymbolArray);
			storeQuotesinDB(quoteMap);
			try {
				Thread.sleep(58000);
			} catch (InterruptedException e) {
				logger.error("Thread Error in handleQoutes"  + e.toString());
				
			}
			
			target = LocalTime.now();
		}
			

	}

	public Map<String, Quote> getQuoteMap(String[] tradingSymbols) throws KiteException {

		Map<String, Quote> quoteMap = InformationThroughKiteConnect.getQuotes(tradingSymbols);

		return quoteMap;
	}



	public void storeQuotesinDB(Map<String, Quote> quoteMap) {

		dbUtil.storeQuoteData(quoteMap);

	}

}
