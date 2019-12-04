
import com.prashant.control.DaemonProcess;
import com.prashant.ordermgmt.KiteConnectManager;
import com.prashant.util.Utils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by sujith on 7/10/16. This class has example of how to initialize
 * kiteSdk and make rest api calls to place order, get orders, modify order,
 * cancel order, get positions, get holdings, convert positions, get
 * instruments, logout user, get historical data dump, get trades
 */
public class Test {

	public static void main(String[] args) throws InterruptedException {

		try {

			// First you should get request_token, public_token using kitconnect login and
			// then use request_token, public_token, api_secret to make any kiteConnect api
			// call.
			// Initialize KiteSdk with your apiKey.
			KiteConnect kiteConnect = new KiteConnect("d5wt29ulgmpkgo8r");

			// Set userId
			kiteConnect.setUserId("DP2095");

			// Enable logs for debugging purpose. This will log request and response.
			kiteConnect.setEnableLogging(false);

			// Get login url
			String url = kiteConnect.getLoginURL();

			// Set session expiry callback.
			kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
				@Override
				public void sessionExpired() {
					System.out.println("session expired .?");
				}
			});

			/*
			 * The request token can to be obtained after completion of login process. Check
			 * out https://kite.trade/docs/connect/v1/#login-flow for more information. A
			 * request token is valid for only a couple of minutes and can be used only
			 * once. An access token is valid for one whole day. Don't call this method for
			 * every app run. Once an access token is received it should be stored in
			 * preferences or database for further usage.
			 */
			User user = kiteConnect.generateSession("w0c6vFicbO8ImIvJNQoclmXOYldZMkKQ",
					"t79vantgxj5f303982bchikmnwlehan1");
			System.out.println("Test.main()" + user.accessToken + "    " + user.publicToken);
			kiteConnect.setAccessToken(user.accessToken);
			kiteConnect.setPublicToken(user.publicToken);
			writeTokensToPropertyFile(user.accessToken, user.publicToken);
			DaemonProcess.execute(null);

		} catch (KiteException e) {
			System.out.println(e.message + " " + e.code + " " + e.getClass().getName());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			// } catch (WebSocketException e) {
			// e.printStackTrace();
		}
	}

	public static void writeTokensToPropertyFile(String accessToken, String publicToken) throws IOException {

		Properties prop = new Properties();

		// set the properties value
		prop.setProperty("Accesstoken", accessToken);
		prop.setProperty("Publictoken", publicToken);

		Utils.writeProperties(prop, KiteConnectManager.kitePropertyFileName);

	}

}
