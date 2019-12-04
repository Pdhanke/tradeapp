package com.prashant.util;

import com.prashant.database.DBUtil;
import com.prashant.ordermgmt.InformationThroughKiteConnect;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

import com.zerodhatech.models.*;

import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by PD on 15/10/16.
 */
public class TickerUtils {
	private static final String NF_MONTHLY_PREFIX = "NFO:NIFTY19NOV";

	private static final String BNF_MONTHLY_PREFIX = "NFO:BANKNIFTY19NOV";

	private static final String NF_WEEKLY_PREFIX = "NFO:NIFTY19N14";

	private static final String BNF_WEEKLY_PREFIX = "NFO:BANKNIFTY19N14";

	public static int BNFOptionLowLevel = 29500;
	public static int NFOptionLowLevel = 11700;
	public static int BNFOptionHighLevel = BNFOptionLowLevel + 2000;
	public static int NFOptionHighLevel = NFOptionLowLevel + 400;

	private static DBUtil dbutil = new DBUtil();

	final static Logger logger = Logger.getLogger(TickerUtils.class);

	public static HashMap<Long, String> tokenMap = new HashMap<Long, String>();

	public static ArrayList<Long> getInstrumentTokens(int weeklyOrMonthly) throws KiteException {
		logger.info("getting tokens for weekly(0)or monthly (1) :" + weeklyOrMonthly);
		ArrayList<Long> tokens = new ArrayList<Long>();
		List<String> tradingSymbols = getRelevantSymbols();
		// ArrayList<String> tradingSymbols =
		// getTradingSymbolsForHistoricalData(weeklyOrMonthly);
		for (String tradingSymbol : tradingSymbols) {
			long token = getInstrumentTokenForTradingSymbol(tradingSymbol);
			tokens.add(token);
			tokenMap.put(token, tradingSymbol);

		}

		return tokens;
	}

	public static ArrayList<Long> getInstrumentTokensForHistoricalData() throws KiteException {
		logger.info("getting tokens historicaldata");
		ArrayList<Long> tokens = new ArrayList<Long>();
		List<String> tradingSymbols = getRelevantSymbols();
		// ArrayList<String> tradingSymbols =
		// getTradingSymbolsForHistoricalData(weeklyOrMonthly);
		for (int i = 0; i < tradingSymbols.size(); i++) {
			long token = getInstrumentTokenForTradingSymbol(tradingSymbols.get(i));
			tokens.add(token);
			tokenMap.put(token, tradingSymbols.get(i));
		}

		return tokens;
	}

	public static HashMap<Long, String> getTokenMap() {
		return tokenMap;
	}

	public static String getBNFOptionPrefix(int optionExpiry) {
		if (optionExpiry == 0)
			return BNF_WEEKLY_PREFIX;
		else
			return BNF_MONTHLY_PREFIX;
	}

	public static String getNFOptionPrefix(int optionExpiry) {
		if (optionExpiry == 0)
			return NF_WEEKLY_PREFIX;
		else
			return NF_MONTHLY_PREFIX;
	}

	public static double getLTPforTradingSymbol(String tradingSymbol) {

		return InformationThroughKiteConnect.getLTP(tradingSymbol);
	}

	public static long getInstrumentTokenForTradingSymbol(String tradingSymbol) throws KiteException {

		long instrumentToken;
		try {
			instrumentToken = dbutil.getInstrumentTokenForTradingSymbol(tradingSymbol);
		} catch (SQLException e) {
			logger.warn("No intrumenttoken in DB. QUerying from kitconnect now for : " + tradingSymbol);
			// TODO Auto-generated catch block
			e.printStackTrace();
			instrumentToken = InformationThroughKiteConnect.getQuote(tradingSymbol).instrumentToken;
		}

		return instrumentToken;
	}

	public static Map<String, Quote> getQuoteMap(String[] tradingSymbols) throws KiteException {

		Map<String, Quote> quoteMap = InformationThroughKiteConnect.getQuotes(tradingSymbols);

		return quoteMap;
	}

	public static ArrayList<String> getTradingSymbolsForHistoricalData(int index) {
		ArrayList<String> tradingSymbols = new ArrayList<String>();

		double ltp = getLTPforTradingSymbol(BNF_MONTHLY_PREFIX + "FUT");
		double ltpN = getLTPforTradingSymbol(NF_MONTHLY_PREFIX + "FUT");

		int prefixDigits = (int) (ltp / 100);
		int prefixDigitsN = (int) (ltpN / 100);

		if (index == 1) {
			// prefixDigits = (int) (ltp / 100);
			int hundredsDigit = prefixDigits % 10;
			if (hundredsDigit >= 5) {
				prefixDigits = prefixDigits - hundredsDigit;

			} else {
				prefixDigits = prefixDigits - 5 - hundredsDigit;
			}

			// prefixDigitsN = (int) (ltpN / 100);

			logger.debug("prefix is" + prefixDigits);

			tradingSymbols.add("NFO:SBIN19SEPFUT");
			tradingSymbols.add("NFO:ICICIBANK19SEPFUT");
			tradingSymbols.add("NFO:HDFCBANK19SEPFUT");
			tradingSymbols.add("NFO:RELIANCE19SEPFUT");
			tradingSymbols.add("NFO:TCS19SEPFUT");
			// tradingSymbols.add("NFO:NIFTY19SEPFUT");
			tradingSymbols.add(BNF_MONTHLY_PREFIX + "FUT");
			tradingSymbols.add(NF_MONTHLY_PREFIX + "FUT");

			logger.debug("tradingsymbol size" + tradingSymbols.size());

			return tradingSymbols;

		}

		index = 0;
		prefixDigits = (int) (ltp / 100);
		prefixDigitsN = (int) (ltpN / 100);
		logger.debug("prefix is" + prefixDigits);

		for (int i = 1; i < 10; i++) {

			int nextCE = (prefixDigits - 3 + i) * 100;
			logger.debug("nextCE " + nextCE);

			tradingSymbols.add(getBNFOptionPrefix(index) + nextCE + "CE");

			int nextCEN = (prefixDigitsN - 2) * 100 + i * 50;
			logger.debug("nextCEN " + nextCEN);

			tradingSymbols.add(getNFOptionPrefix(index) + nextCEN + "CE");

			int nextPE = (prefixDigits + 5 - i) * 100;
			logger.debug("nextPE " + nextPE);
			tradingSymbols.add(getBNFOptionPrefix(index) + nextPE + "PE");

			int nextPEN = (prefixDigitsN + 2) * 100 + 100 - i * 50;
			logger.debug("nextPEN " + nextPEN);

			tradingSymbols.add(getNFOptionPrefix(index) + nextPEN + "PE");
		}

		logger.debug("tradingsymbol size" + tradingSymbols.size());

		return tradingSymbols;

	}

	public static List<String> getRelevantSymbols() {

		List<String> tradingSymbols = new LinkedList<String>();
		String ce = "CE";
		String pe = "PE";
		String bnfWeeklyPrefix = TickerUtils.getBNFOptionPrefix(0);
		String nfWeeklyPrefix = TickerUtils.getNFOptionPrefix(0);
		String bnfMonthlyPrefix = TickerUtils.getBNFOptionPrefix(1);
		String nfMonthlyPrefix = TickerUtils.getNFOptionPrefix(1);

		for (int index = BNFOptionLowLevel; index <= BNFOptionHighLevel; index = index + 100) {
			tradingSymbols.add(bnfWeeklyPrefix + index + ce);
			tradingSymbols.add(bnfWeeklyPrefix + index + pe);

		}

		for (int index = BNFOptionLowLevel; index <= BNFOptionHighLevel; index = index + 500) {
			tradingSymbols.add(bnfMonthlyPrefix + index + ce);
			tradingSymbols.add(bnfMonthlyPrefix + index + pe);

		}

		for (int index = NFOptionLowLevel; index <= NFOptionHighLevel; index = index + 50) {
			tradingSymbols.add(nfWeeklyPrefix + index + ce);
			tradingSymbols.add(nfWeeklyPrefix + index + pe);

		}

		for (int index = NFOptionLowLevel; index <= NFOptionHighLevel; index = index + 100) {
			tradingSymbols.add(nfMonthlyPrefix + index + ce);
			tradingSymbols.add(nfMonthlyPrefix + index + pe);

		}

		tradingSymbols.add(BNF_MONTHLY_PREFIX + "FUT");

		tradingSymbols.add(NF_MONTHLY_PREFIX + "FUT");
		logger.info("Quote trading symbols size : " + tradingSymbols.size());

		return tradingSymbols;

	}

}
