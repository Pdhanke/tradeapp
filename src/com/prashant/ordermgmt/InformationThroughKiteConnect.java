package com.prashant.ordermgmt;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.prashant.database.DBUtil;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Quote;
import com.zerodhatech.models.Tick;

public class InformationThroughKiteConnect {

	final static Logger logger = Logger.getLogger(InformationThroughKiteConnect.class);

	private static KiteConnect kiteConnect = KiteConnectManager.getKiteConnect();

	private static void writeInstrumentTokenSymboltoDB() throws KiteException, IOException {
		List<Instrument> instruments = kiteConnect.getInstruments("NFO");
		List<Instrument> bnInstruments = new LinkedList<Instrument>();

		for (Instrument instrument : instruments) {

			if (instrument.tradingsymbol.contains("BANKNIFTY19") || instrument.tradingsymbol.contains("NIFTY19")) {

				bnInstruments.add(instrument);
			}
		}

		System.out.println("OrderThroughKiteConnect.main() bn sizeis " + bnInstruments.size());
		DBUtil dbUtil = new DBUtil();
		dbUtil.initializeJDBCConn();
		dbUtil.storeInstruTokenSymbolMap(bnInstruments);
	}

	public static List<Position> getDayPositions() {

		try {
			List<Position> dayPosition = kiteConnect.getPositions().get("day");
			logger.debug("Position Size (day) : " + dayPosition.size());
			return dayPosition;
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

		return null;
	}


	public static double getPnL() {
		List<Position> positions = getPositions();
		double pnl = 0.0;
		double unrealised = 0.0;
		for (Position position : positions) {

				pnl = pnl + position.pnl;
				unrealised = unrealised + position.unrealised;
		}
		logger.debug("pnl is " + pnl);
		logger.debug("unrealized is "+ unrealised);
		
		return unrealised;

	}
	

	public static double getPnLForOpenPosition() {
		List<Position> positions = getPositions();
		double pnl = 0.0;
		double unrealised = 0.0;
		for (Position position : positions) {
			if (position.netQuantity != 0)
				pnl = pnl + position.pnl;
				unrealised = unrealised + position.unrealised;
		}
		logger.debug("pnl is " + pnl);
		logger.debug("unrealized is "+ unrealised);
		
		return pnl;

	}
	
	public static Tick getLastTick(String tradingsymbol) {

		DBUtil dbutil = new DBUtil();
		Tick tick = new Tick();
		try {
			tick = dbutil.getlastTick(tradingsymbol);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.warn("Tick not found in DB. Getting quote from kiteconnect");
			try {
				Quote quote = getQuote(tradingsymbol);
				tick.setLastTradedPrice(quote.lastPrice);
				tick.setLastTradedQuantity(quote.lastTradedQuantity);
				tick.setVolumeTradedToday(quote.volumeTradedToday);
			} catch (KiteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		return tick;
		}

	public static double getLTP(String tradingsymbol) {

		DBUtil dbutil = new DBUtil();
		double ltp = 0;
		try {
			 ltp = dbutil.getltp(tradingsymbol);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.warn("Tick not found in DB. Getting quote from kiteconnect");
			try {
				ltp = getQuote(tradingsymbol).lastPrice;
			} catch (KiteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
//		String[] instruments = { tradingsymbol };
//		try {
//			return kiteConnect.getLTP(instruments).get(tradingsymbol).lastPrice;
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (KiteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return ltp;
	}





	public static Quote getQuote(String tradingsymbol) throws KiteException {
		// Get quotes returns quote for desired tradingsymbol.
		logger.debug("OrderThroughKiteConnect.getQuote() getting quote for " + tradingsymbol);
		String[] instruments = { tradingsymbol };
		Map<String, Quote> quotes = null;

		try {
			quotes = kiteConnect.getQuote(instruments);
		} catch (JSONException | IOException e) {
			logger.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Instument Token for " + tradingsymbol + " = "
				+ quotes.get(tradingsymbol).instrumentToken);
		return quotes.get(tradingsymbol);

	}

	public static Map<String, Quote> getQuotes(String[] tradingSymbols) throws KiteException {
		Map<String, Quote> quotes = null;
		try {
			quotes = kiteConnect.getQuote(tradingSymbols);
		} catch (JSONException | IOException e) {
			logger.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return quotes;
	}

	public static void writeInstrumentsToCSV(List<Instrument> instruments) {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("IndexInstruments20Aug.csv")) {
			for (Instrument instrument : instruments) {

				writer.append(instrument.tradingsymbol).append(',').append("" + instrument.instrument_token).append(',')
						.append(instrument.expiry.toString()).append(eol);
			}
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace(System.err);

		}
	}

	public static List<Position> getPositions() {

		try {
			List<Position> netPosition = OrderThroughKiteConnect.kiteConnect.getPositions().get("net");
			//List<Position> dayPosition = OrderThroughKiteConnect.kiteConnect.getPositions().get("day");
			//netPosition.addAll(dayPosition);
			return netPosition;
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

		return null;
	}
	


}
