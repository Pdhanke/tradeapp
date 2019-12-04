package com.prashant.ordermgmt;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.prashant.util.Utils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;

public class KiteConnectManager {
	final static Logger logger = Logger.getLogger(KiteConnectManager.class);
	
	private static String PROPACCESSTOKEN = "Accesstoken";
	private static String PROPPUBLICTOKEN = "Publictoken";
	
	private static  String accessToken;
	private static String publicToken;
	
	private static KiteConnect kiteConnect;
	
	public static String kitePropertyFileName = "kiteconnect.properties";
	
	
	public static void initializeKiteConnectManager () throws Exception {
		
        // Initialize KiteSdk with your apiKey.
        kiteConnect = new KiteConnect("d5wt29ulgmpkgo8r");

        // Set userId
        kiteConnect.setUserId("DP2095");

        //Enable logs for debugging purpose. This will log request and response.
        kiteConnect.setEnableLogging(false);

        // Get login url
        String url = kiteConnect.getLoginURL();

        // Set session expiry callback.
        kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
            @Override
            public void sessionExpired() {
                logger.warn("session expired .");
            }
        });
        
        readProperties();
       kiteConnect.setAccessToken(accessToken);
       kiteConnect.setPublicToken(publicToken);
       
       logger.debug("Access Token, Public Token : " + accessToken + " , " + publicToken);
		
	}
	
	public static void readProperties () throws Exception
	{
	   Properties p = Utils.readProperties(kitePropertyFileName) ;
	   accessToken = p.getProperty(PROPACCESSTOKEN);  
	   publicToken= p.getProperty(PROPPUBLICTOKEN);  
	   
	}
	
	public static KiteConnect getKiteConnect ()
	{
		if (kiteConnect==null) {
			try {
				initializeKiteConnectManager();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return kiteConnect;
	}

}
