package com.prashant.dblayer;

public class DBQueries {

	public static String TICK_TABLENAME = "tickdataoct";
	public static String ORDER_TABLENAME = "KITEORDERS";
	public static String POSITION_TABLENAME = "POSITIONS";

	public static final String CREATE_TICK_TABLE_FIELDS =  " (CurrentTimeInMsLong BIGINT , "
			+ " LastTradedTime time , " + " TradingSymbol VARCHAR(40) , " + " LastTradedPrice FLOAT , "
			+ " LastTradedVolume FLOAT , " + " VolumeTradedToday FLOAT , " + " TotalBuyQuantity FLOAT , "
			+ " TotalSellQuantity FLOAT , " + " PRIMARY KEY (TradingSymbol, VolumeTradedToday,LastTradedPrice)) "
			+ " ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	public static final String INSERT_TICK_TABLE_FIELDS =  " "
			+ "(CurrentTimeInMsLong, LastTradedTime, TradingSymbol, LastTradedPrice, LastTradedVolume, "
			+ "VolumeTradedToday, TotalBuyQuantity, TotalSellQuantity) " + "values(?,?,?,?,?,?,?,?)";

	public static final String CREATE_ORDER_TABLE_FIELDS = ORDER_TABLENAME + " (orderTableId int NOT NULL AUTO_INCREMENT, exchangeOrderId VARCHAR(40) , "
			+ " disclosedQuantity VARCHAR(40) , " + " validity VARCHAR(40) , " + " tradingSymbol VARCHAR(40) , "
			+ " orderVariety VARCHAR(40) , " + " orderType VARCHAR(40) , " + " triggerPrice VARCHAR(40) , "
			+ " price VARCHAR(40) , " + " status VARCHAR(40) , " + " product VARCHAR(40) , "
			+ " exchange VARCHAR(10) , " + " orderId VARCHAR(40) , " + " symbol VARCHAR(40) , "
			+ " pendingQuantity VARCHAR(40) , " + " orderTimestamp DATETIME , " + " averagePrice VARCHAR(40) , "
			+ " transactionType VARCHAR(40) , " + " filledQuantity VARCHAR(40) , " + " quantity VARCHAR(40) , "
			+ " parentOrderId VARCHAR(40) , " + " PRIMARY KEY (orderId, orderTimestamp)) "
			+ " ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	public static final String INSERT_ORDER_TABLE_FIELDS = ORDER_TABLENAME + " "
			+ "(exchangeOrderId, disclosedQuantity, validity, tradingSymbol, orderVariety, "
			+ "orderType, triggerPrice, price, status, product, "
			+ "exchange, orderId, symbol, pendingQuantity, orderTimestamp, "
			+ "averagePrice, transactionType, filledQuantity, quantity, parentOrderId) "
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String CREATE_POSITION_TABLE_FIELDS = POSITION_TABLENAME + " (positionId int NOT NULL AUTO_INCREMENT, product VARCHAR(40) , "
			+ " exchange VARCHAR(10) , " + " sellValue FLOAT , " + " lastPrice FLOAT , " + " unrealised FLOAT , "
			+ " buyprice FLOAT , " + " sellPrice FLOAT , " + " m2m FLOAT , " + " tradingSymbol VARCHAR(40) , "
			+ " netQuantity INT , " + " sellQuantoty INT , " + " realised FLOAT(40) , " + " buyQuantity INT , "
			+ " netValue FLOAT(40) , " + " buyValue FLOAT , " + " multiplier FLOAT(40) , "
			+ " instrumentToken VARCHAR(40) , " + " closePrice FLOAT , " + " pnl FLOAT , " + " overnightQuantity INT , "
			+ " buym2m FLOAT , " + " sellm2m FLOAT , " + " dayBuyQuantity FLOAT , " + " daySellQuantity FLOAT , "
			+ " dayBuyPrice FLOAT , " + " daySellPrice FLOAT , " + " dayBuyValue FLOAT , " + " daySellValue FLOAT , "
			+ " value FLOAT , " + " timeStamp DATETIME , " + " PRIMARY KEY (tradingSymbol,timestamp)) "
			+ " ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	// pnl = (sellValue - buyValue) + (netQuantity * lastPrice * multiplier);
	public static final String INSERT_POSITION_TABLE_FIELDS = POSITION_TABLENAME + " "
			+ "(product, exchange, sellValue, lastPrice, unrealised, buyprice, "
			+ "sellPrice, m2m, tradingSymbol, netQuantity, sellQuantoty, realised, "
			+ "buyQuantity, netValue, buyValue, multiplier, instrumentToken, closePrice, pnl, overnightQuantity,"
			+ "buym2m, sellm2m, dayBuyQuantity, daySellQuantity, dayBuyPrice, daySellPrice,"
			+ "dayBuyValue, daySellValue, value, timeStamp ) "
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String CREATE_QUOTEDATA_TABLE = "CREATE TABLE QuoteData (TradingSymbol VARCHAR(40) , Close FLOAT , Volume BIGINT , Timestamp DATETIME , PRIMARY KEY (TradingSymbol, Timestamp)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
	public static final String INSERT_QUOTE_TABLE = "INSERT INTO QuoteData " + "(TradingSymbol, Close, "
			+ "Volume, Timestamp) " + "values(?,?,?,?)";

	public static final String CREATE_STRATEGY_TABLE = "CREATE TABLE Strategy (StrategyId int NOT NULL AUTO_INCREMENT , StrategyName VARCHAR(40) , TradingSymbols VARCHAR(120) , Status VARCHAR(40) , PnL Float , Timestamp DATETIME , PRIMARY KEY (StrategyID, Timestamp)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
	public static final String INSERT_STRATEGY_TABLE = "INSERT INTO Strategy "
			+ "(StrategyName, TradingSymbols, Status, PnL, TimeStamp) " + "values(?,?,?,?,?)";

	public static final String CREATE_STRATEGY_ORDER_MAP_TABLE = "CREATE TABLE StrategyOrderMap (StrategyId INT , orderTableId INT) , PRIMARY KEY (StrategyId, orderTableId)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
	public static final String INSERT_STRATEGY_ORDER_MAP_TABLE = "INSERT INTO StrategyOrderMap "
			+ "(StrategyID, orderId) " + "values(?,?)";

	public static final String CREATE_ORDERPARAMS_TABLE = "CREATE TABLE OrderParams (orderParamsId int NOT NULL AUTO_INCREMENT, timeStamp DATETIME , "
			+ " exchange  VARCHAR(10) , " + " tradingSymbol VARCHAR(40) , " + " transactionType VARCHAR(10) , "
			+ " quantity INT , " + " price FLOAT , " + " product VARCHAR(20) , " + " orderType VARCHAR(20) , "
			+ " disclosedQuantity INT, " + " triggerPrice FLOAT, " + " squareoff FLOAT , " + " stoploss FLOAT , "
			+ " trailingStoplos FLOAT , " + " parentOrderId VARCHAR(40), " + " PRIMARY KEY (tradingSymbol, timeStamp)) "
			+ " ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	public static final String INSERT_ORDERPARAMS = "INSERT INTO OrderParams "
			+ "(CurrentTimeInMsLong, LastTradedTime, TradingSymbol, LastTradedPrice, LastTradedVolume, "
			+ "VolumeTradedToday, TotalBuyQuantity, TotalSellQuantity) " + "values(?,?,?,?,?,?,?,?)";

	// ALTER TABLE users ADD id INT UNSIGNED AUTO_INCREMENT,
	// ADD INDEX (id);

	public static final String CREATE_INSTRUMENTTOKEN_TABLE = "CREATE TABLE InstrumentTokens (InstrumentToken BIGINT , TradingSymbol VARCHAR(40) , Exchange VARCHAR(10) , Expiry DATE , PRIMARY KEY (TradingSymbol, InstrumentToken)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
	public static final String INSERT_INSTRUMENTTOKEN_TABLE = "INSERT INTO InstrumentTokens "
			+ "(InstrumentToken, TradingSymbol, Exchange, Expiry) " + "values(?,?,?,?)";

	public static final String CREATE_HISTORICAL_TABLE = "CREATE TABLE HistoricalData (TradingSymbol VARCHAR(40) , Open FLOAT , High FLOAT ,Low FLOAT , Close FLOAT , Volume BIGINT , Timestamp DATETIME , PRIMARY KEY (TradingSymbol, Timestamp)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
	public static final String INSERT_HISTORICAL_DATA = "INSERT INTO HistoricalData "
			+ "(TradingSymbol, Open, High, Low, Close, " + "Volume, Timestamp) " + "values(?,?,?,?,?,?,?)";

	public static final String CREATE_DAILY_TABLE = "CREATE TABLE DailyData (TradingSymbol VARCHAR(40) , Open FLOAT , High FLOAT ,Low FLOAT , Close FLOAT , Average FLOAT , Volume BIGINT , OI BIGINT , DaysToExpiry INT , Timestamp DATETIME , PRIMARY KEY (TradingSymbol, Timestamp)) ENGINE=InnoDB DEFAULT CHARSET=utf8";

}
