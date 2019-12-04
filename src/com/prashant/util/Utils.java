package com.prashant.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.prashant.ordermgmt.OrderThroughKiteConnect;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.OrderParams;

public class Utils {
	

	
	public static Properties readProperties (String propertyFileName) throws IOException 
	{
		FileReader reader;
		
			reader = new FileReader(".\\src\\main\\resources\\" + propertyFileName);
		
	      
	    Properties properties=new Properties();  
	    properties.load(reader);  
	      
	    return properties;
	    
	}
	
	public static void writeProperties (Properties properties, String propertyFileName) throws IOException {

	      
		properties.store(new FileWriter(".\\src\\main\\resources\\" + propertyFileName),"");  

	}
	
	  private static String formatTimeStampForHistoricalDate (String histTimestamp) {
		  //histTimestamp = "2019-08-16T12:59:00+0530";
		  String formatted = histTimestamp.substring(0,10) + " " + histTimestamp.substring(11, 19);
		  System.out.println("HistoricalDataHandler.formatTimeStampFOrHistoricalDate()" + formatted);
		  return formatted;
		  }
	  
	  public static Date simpleDateFormatForHistoricalData (String timeStamp) {
		 timeStamp = formatTimeStampForHistoricalDate(timeStamp); 
	     Date date = new Date();	
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        try {
	            date = formatter.parse(timeStamp);
	        }catch (ParseException e) {
	            e.printStackTrace();
	        }
	        
	        return date;
	  }

	public static void printOrderparams(OrderParams orderparams) {
		
		OrderThroughKiteConnect.logger.debug("printOrderparams()" + " exc " + orderparams.exchange +" type "+ orderparams.orderType + " price " + orderparams.price + " \ntradingsymbol " +  orderparams.tradingsymbol + " transactiontype " +
		orderparams.transactionType +  "\n validity " + orderparams.validity + " quantity " + orderparams.quantity + " stoplloss " + orderparams.stoploss);
		
	}

	public static  List<OrderParams> getOrderParamsList () {
			OrderParams orderParams = new OrderParams();
			orderParams.quantity = 300;
			orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
			orderParams.tradingsymbol = "BANKNIFTY19O2430000CE";
			//orderParams.tradingsymbol = "NIFTY19OCT11800CE";
			orderParams.product = Constants.PRODUCT_NRML	;
			orderParams.exchange = Constants.EXCHANGE_NFO;
			orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
			orderParams.validity = Constants.VALIDITY_DAY;
			orderParams.price = 100.0;
			orderParams.triggerPrice = 0.0;
			//orderParams.tag = "myTag"; 
			
		OrderParams orderParams2 = new OrderParams();
			orderParams2.quantity = 300;
			orderParams2.orderType = Constants.ORDER_TYPE_LIMIT;
			orderParams2.tradingsymbol = "BANKNIFTY19O2428200PE";
			//orderParams.tradingsymbol = "NIFTY19OCT11500PE";
			orderParams2.product = Constants.PRODUCT_NRML;
			orderParams2.exchange = Constants.EXCHANGE_NFO;
			orderParams2.transactionType = Constants.TRANSACTION_TYPE_SELL;
			orderParams2.validity = Constants.VALIDITY_DAY;
			orderParams2.price = 100.00;
			orderParams2.triggerPrice = 0.0;
			//orderParams2.tag = "myTag"; 
			
//			OrderParams orderParams3 = new OrderParams();
//			orderParams.quantity = 480;
//			orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
//			orderParams.tradingsymbol = "BANKNIFTY19OCT28500CE";;
//			orderParams.product = Constants.PRODUCT_MIS;
//			orderParams.exchange = Constants.EXCHANGE_NFO;
//			orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
//			orderParams.validity = Constants.VALIDITY_DAY;
//			orderParams.price = 400.0;
//			orderParams.triggerPrice = 0.0;
//			//orderParams.tag = "myTag"; 
//			
//		OrderParams orderParams4 = new OrderParams();
//			orderParams2.quantity = 480;
//			orderParams2.orderType = Constants.ORDER_TYPE_LIMIT;
//			orderParams2.tradingsymbol = "BANKNIFTY19OCT27500PE";
//			orderParams2.product = Constants.PRODUCT_MIS;
//			orderParams2.exchange = Constants.EXCHANGE_NFO;
//			orderParams2.transactionType = Constants.TRANSACTION_TYPE_BUY;
//			orderParams2.validity = Constants.VALIDITY_DAY;
//			orderParams2.price = 400.00;
//			orderParams2.triggerPrice = 0.0;
			//orderParams2.tag = "myTag"; 
			
			List<OrderParams> orderParamsList = new LinkedList<OrderParams>();
			orderParamsList.add(orderParams);
			orderParamsList.add(orderParams2);
	//		orderParamsList.add(orderParams3);
	//		orderParamsList.add(orderParams4);
			
			return orderParamsList;
	    	
	    }
	
	  
	

}
