package com.prashant.database;

import java.math.BigDecimal;
import java.sql.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import org.apache.log4j.Logger;

import com.prashant.dblayer.DBQueries;

import com.prashant.util.TickerUtils;
import com.prashant.util.Utils;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Quote;
import com.zerodhatech.models.Tick;

public class DBUtil {
	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	// DB connection
	private Connection conn = null;
	private int countException = 0;

	private HashMap<Long, Double> tokenLastVolume = new HashMap<Long, Double>();
	// Quote Table Name
	private static String quoteTable = "tickdataoct";
	public static String tickDataTableName = getTickDataTableName();
	final static Logger logger = Logger.getLogger(DBUtil.class);

	/**
	 * createDaysStreamingQuoteTable - method to create streaming quote table for
	 * the day
	 * 
	 * @param quoteTable
	 */
	private static String getTickDataTableName() {
		LocalDate localDate = LocalDate.now(); 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		return DBQueries.TICK_TABLENAME +  localDate.format(formatter);
	}
	
	public DBUtil () {
		initializeJDBCConn();
	}
	public void createDaysStreamingTickTable(String quoteTableName) {
		quoteTable = quoteTableName;
		if (conn != null) {
			Statement stmt;
			try {

				
				
				stmt = conn.createStatement();

				String sql = "CREATE TABLE " + tickDataTableName +DBQueries.CREATE_TICK_TABLE_FIELDS;

				stmt.executeUpdate(sql);
				logger.info("createDaysStreamingQuoteTable(): SQL table for Streaming quote created, table name: ["
						+ quoteTable + "]");
			} catch (SQLException e) {
				logger.error("createDaysStreamingQuoteTable(): ERROR: SQLException on creating Table, cause: "
						+ e.toString());
			}
		} else {
			logger.error("StreamingQuoteDAOModeFull.createDaysStreamingQuoteTable(): ERROR: DB conn is null !!!");
		}
	}

	/**
	 * 
	 * @param ticks
	 */
	public void storeTickData(ArrayList<Tick> ticks) {

		try {
			// SQL query
			String sql = "INSERT INTO " + tickDataTableName +DBQueries.INSERT_TICK_TABLE_FIELDS;

			PreparedStatement prepStmt = conn.prepareStatement(sql);

			long systime = System.currentTimeMillis();
			for (Tick tick : ticks) {
				double tickVOl = getLastTradedVolume(tick);

				if (tickVOl > 0) {

					// prepare statement
					int index = 0;
					
					prepStmt.setLong(++index, systime);
					prepStmt.setTime(++index, new Time(tick.getLastTradedTime().getTime()));
					// prepStmt.setLong(++index, tick.getInstrumentToken());
					prepStmt.setString(++index, TickerUtils.tokenMap.get(tick.getInstrumentToken()).substring(4));
					prepStmt.setDouble(++index, tick.getLastTradedPrice());
					// prepStmt.setDouble(++index, tick.getLastTradedQuantity());
					prepStmt.setDouble(++index, tickVOl);
					prepStmt.setDouble(++index, tick.getVolumeTradedToday());
					prepStmt.setDouble(++index, tick.getTotalBuyQuantity());
					prepStmt.setDouble(++index, tick.getTotalSellQuantity());

					prepStmt.addBatch();
				}
			}

			logger.debug("prepstmt is " + prepStmt.toString());

			prepStmt.executeBatch();
			conn.commit();
			prepStmt.close();
		} catch (SQLException e) {
			countException++;
			logger.error("ERROR: SQLException on Storing data to Table:: [SQLException Cause]: " + e.getMessage());
			// e.printStackTrace();
		}

	}

	public void storeQuoteData(Map<String, Quote> quoteMap) {

		try {
			// SQL query
			String sql = DBQueries.INSERT_QUOTE_TABLE;

			logger.debug("SQl is " + sql);
			PreparedStatement prepStmt = conn.prepareStatement(sql);

			Iterator<String> tradingSymbols = quoteMap.keySet().iterator();
			while (tradingSymbols.hasNext()) {

				String tradingSymbol = tradingSymbols.next();
				Quote quote = quoteMap.get(tradingSymbol);

				// prepare statement
				int index = 0;

				prepStmt.setString(++index, tradingSymbol);

				prepStmt.setDouble(++index, quote.lastPrice);
				prepStmt.setDouble(++index, quote.volumeTradedToday);

				prepStmt.setObject(++index, quote.lastTradedTime);
				prepStmt.addBatch();
			}

			logger.debug("prepstmt is " + prepStmt.toString());

			prepStmt.executeBatch();
			conn.commit();
			prepStmt.close();
		} catch (SQLException e) {
			countException++;
			logger.error("ERROR: SQLException on Storing data to Table:: [SQLException Cause]: " + e.getMessage());
			// e.printStackTrace();
		}

	}

	public void storeInstruTokenSymbolMap(List<Instrument> instruments) {
		System.out.println("DBUtil.storeInstruTokenSymbolMap()");
		try {
			// SQL query
			String sql = DBQueries.INSERT_INSTRUMENTTOKEN_TABLE;

			logger.debug("SQl is " + sql);
			PreparedStatement prepStmt = conn.prepareStatement(sql);

			for (int i = 0; i < instruments.size(); i++) {
				Instrument instrument = instruments.get(i);

				// prepare statement
				int index = 0;

				prepStmt.setLong(++index, instrument.instrument_token);
				prepStmt.setString(++index, instrument.tradingsymbol);
				prepStmt.setString(++index, instrument.exchange);
				prepStmt.setObject(++index, instrument.expiry);
				prepStmt.addBatch();

			}

			logger.debug("prepstmt is " + prepStmt.toString());

			prepStmt.executeBatch();
			conn.commit();
			prepStmt.close();
		} catch (SQLException e) {
			countException++;
			logger.error("ERROR: SQLException on Storing data to Table:: [SQLException Cause]: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void storeHistoricalData(String tradingSymbol, List<HistoricalData> historicalDataList) {

		try {
			// SQL query
			String sql = DBQueries.INSERT_HISTORICAL_DATA;

			logger.debug("SQl is " + sql);
			PreparedStatement prepStmt = conn.prepareStatement(sql);

			for (int i = 0; i < historicalDataList.size(); i++) {
				HistoricalData histData = historicalDataList.get(i);

				// prepare statement
				int index = 0;

				prepStmt.setString(++index, tradingSymbol);
				prepStmt.setDouble(++index, histData.open);
				prepStmt.setDouble(++index, histData.high);
				prepStmt.setDouble(++index, histData.low);
				prepStmt.setDouble(++index, histData.close);
				prepStmt.setLong(++index, histData.volume);

				prepStmt.setObject(++index, Utils.simpleDateFormatForHistoricalData(histData.timeStamp));
				prepStmt.addBatch();
			}

			logger.debug("prepstmt is " + prepStmt.toString());

			prepStmt.executeBatch();
			conn.commit();
			prepStmt.close();
		} catch (SQLException e) {
			countException++;
			logger.error("ERROR: SQLException on Storing data to Table:: [SQLException Cause]: " + e.getMessage());
			// e.printStackTrace();
		}

	}

	private double getLastTradedVolume(Tick tick) {

		double lastTotalVolume = 0;
		if (tokenLastVolume.containsKey(tick.getInstrumentToken())) {
			lastTotalVolume = tokenLastVolume.get(tick.getInstrumentToken());

		}

		double currentTotalVolume = tick.getVolumeTradedToday();
		tokenLastVolume.put(tick.getInstrumentToken(), currentTotalVolume);

		return currentTotalVolume - lastTotalVolume;

	}

	/**
	 * initializeJDBCConn - method to create and initialize JDBC connection
	 */

	public void initializeJDBCConn() {
		try {
			logger.info("creating JDBC connection for Streaming Quote...");
			// Class.forName("org.sqlite.JDBC");
			// conn = DriverManager.getConnection("jdbc:sqlite:test.db");
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);
			// Open the connection
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/trade", "root", "admin2");
			conn.setAutoCommit(false);
			// conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			logger.error("initializeJDBCConn(): ClassNotFoundException: " + JDBC_DRIVER + " " + e.toString());

		} catch (SQLException e) {
			logger.error("initializeJDBCConn(): SQLException on getConnection" + e.toString());

		}
	}
	
	public  long getInstrumentTokenForTradingSymbol(String tradingSymbol) throws SQLException {
		
		Statement stmt = conn.createStatement();
		ResultSet rs;
		long instrumentToken = 0;
		String query = "SELECT * FROM InstrumentTokens WHERE tradingSymbol = '" + tradingSymbol;
		logger.debug(query);
		rs = stmt.executeQuery(query);
		while (rs.next()) {

			instrumentToken = rs.getInt("InstrumentToken");

			logger.debug("instrumentToken of tradingsymbol is: " + tradingSymbol + " : " + instrumentToken);
		}
		//conn.close();
		return instrumentToken;
		
	}

	public double getltp(String tradingSymbol) throws SQLException {

		Statement stmt = conn.createStatement();
		ResultSet rs;
		double lastPrice = 0;
		String query = "SELECT * FROM " + DBUtil.tickDataTableName +" WHERE tradingSymbol = '" + tradingSymbol + "' ORDER BY LastTradedTime DESC LIMIT 1";
		logger.debug(query);
		rs = stmt.executeQuery(query);
		while (rs.next()) {

			lastPrice = rs.getDouble("LastTradedPrice");

			logger.debug("Lastprice of tradingsymbol is: " + tradingSymbol + " : " + lastPrice);
		}
		//conn.close();
		return lastPrice;
	}
	
	public Tick getlastTick(String tradingSymbol) throws SQLException {

		Statement stmt = conn.createStatement();
		ResultSet rs;
		
		String query = "SELECT * FROM " + DBUtil.tickDataTableName +" WHERE tradingSymbol = '" + tradingSymbol + "' ORDER BY LastTradedTime DESC LIMIT 1";
		logger.debug(query);
		rs = stmt.executeQuery(query);
		Tick tick = new Tick();
		while (rs.next()) {
			tick.setLastTradedPrice(rs.getDouble("LastTradedPrice"));
			tick.setLastTradedQuantity(rs.getDouble("LastTradedVolume"));
			tick.setVolumeTradedToday(rs.getDouble("VolumeTradedToday"));			
		}
		//conn.close();
		return tick;
	}

	public void initializeJDBCConnPostgres() {

		String instanceConnectionName = "credible-spark-248408:asia-south1:tickdataaug";
		String databaseName = "minuteSep";

		String IP_of_instance = "35.200.148.170";
		String username = "zstreamingquotesdb";
		String password = "admin";

		String jdbcurl = "jdbc:mysql://google/ticksAug?cloudSqlInstance=credible-spark-248408:asia-south1:tickdataaug&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false";
		String jdbcUrl = String.format(
				"jdbc:mysql://%s/%s?cloudSqlInstance=%s"
						+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false",
				IP_of_instance, databaseName, instanceConnectionName);

		try {
			Class.forName("org.postgresql.Driver");

			conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/zstreamingquotesdb", "postgres",
					"postgres");
			// conn = DriverManager.getConnection(jdbcurl, username, password);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("initializeJDBCConnPostgres(): SQLException on getConnection" + e.toString());

		}
	}

	/**
	 * closeJDBCConn - method to close JDBC connection
	 */

	public void closeJDBCConn() {
		if (conn != null) {
			try {
				logger.info("closeJDBCConn(): Closing JDBC connection for Streaming Quote...");
				conn.close();
			} catch (SQLException e) {
				logger.error("closeJDBCConn(): SQLException on conn close \n" + e.toString());

			}
		} else {
			logger.warn(" WARNING: DB connection already null");
		}
	}
}
