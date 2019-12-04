import java.sql.*;
import java.util.Calendar;
public class DBTest {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
	  
Connection conn = null;
           
		try{  
		
				Class.forName("org.postgresql.Driver");
				 
						 conn = DriverManager.getConnection(
			                "jdbc:postgresql://127.0.0.1:5432/zstreamingquotesdb", "postgres", "postgres");

			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}


		Statement stmt;
		try {
			stmt = conn.createStatement();
//			
//			String sql1 = "CREATE TABLE " + "tickdata1"+ " " + "(CurrentTimeInMsLong BIGINT NOT NULL, "
//					+ " LastTradedTime FLOAT NOT NULL, " + " InstrumentToken BIGINT NOT NULL, " + " TradingSymbol FLOAT NOT NULL, "
//					+ " LastTradedPrice FLOAT NOT NULL, " + " LastTradedQuantity FLOAT NOT NULL, "
//					+ " LastTradedVolume FLOAT NOT NULL, " + " VolumeTradedToday FLOAT NOT NULL, "
//					+ " TotalBuyQuantity FLOAT NOT NULL, " + " TotalSellQuantity FLOAT NOT NULL, "
//					+ " PRIMARY KEY (InstrumentToken, LastTradedTime, VolumeTradedToday)) "
//					+ " ";
//			stmt.execute(sql1);
			String sql = "INSERT INTO " + "tickdata1" + " "
					+ "(CurrentTimeInMsLong, LastTradedTime, InstrumentToken, TradingSymbol, LastTradedPrice, LastTradedQuantity, LastTradedVolume, "
					+	"VolumeTradedToday, TotalBuyQuantity, TotalSellQuantity) " 
					+  "values(2,3,4,5,6,7,8,9,10,11)";
			stmt.execute(sql);
			
			System.out.println(
					"StreamingQuoteDAOModeFull.createDaysStreamingQuoteTable(): SQL table for Streaming quote created, table name: ["
							 + "]");
		} catch (SQLException e) {
			System.out.println(
					"StreamingQuoteDAOModeFull.createDaysStreamingQuoteTable(): ERROR: SQLException on creating Table, cause: "
							+ e.getMessage());
			e.printStackTrace();
		}
	
	      
	      conn.close();
	    } 

	

	}


