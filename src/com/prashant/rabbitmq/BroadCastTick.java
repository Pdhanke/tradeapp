package com.prashant.rabbitmq;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zerodhatech.models.Tick;

public class BroadCastTick {

    private static final String EXCHANGE_NAME = "topic_logs";
    private static final String COMMA = ",";
    private static final String BLANK = "";
    private ConnectionFactory factory ;
    private Channel channel;
    private Connection connection;
    
    public BroadCastTick () throws Exception {
    	init();
    }
    
    public void init () throws Exception {

        factory = new ConnectionFactory(); 
        factory.setHost("localhost");
    	
    	
    	
         connection = factory.newConnection();
             channel = connection.createChannel() ;

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            channel.queueDeclare("Ticks", false, false, false, null);
           // String queueName = channel.queueDeclare().getQueue();
        
    	
    }
    		
    
    public void sendMessage(String token, String tickValue) throws UnsupportedEncodingException, IOException {
    	if(channel == null) {
    		System.out.println("EmitLogTopic.sendMessage()");
    	}
    	channel.basicPublish(EXCHANGE_NAME, token, null, tickValue.getBytes("UTF-8"));
    }
    
    public void publishTicks (ArrayList<Tick> ticks) throws UnsupportedEncodingException, IOException {
    	
    	for (Tick tick : ticks) { 
    		double price = tick.getLastTradedPrice();
    		double volume = tick.getVolumeTradedToday();
    		String tickMessage = price + COMMA + volume;
    		sendMessage(tick.getInstrumentToken()+ BLANK, tickMessage);
    	}
    	
    }

    public static void main(String[] argv)  {
    	
    	BroadCastTick broadCastTick = null;
		try {
			broadCastTick = new BroadCastTick();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println("Banknifty" + System.currentTimeMillis()%10 + " " + Math.random()* 1000);
    	
    	
    	while(true)
    	{
    		try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	try {
            	System.out.println("Banknifty" + System.currentTimeMillis()%10 + " " + Math.random()* 1000);

				broadCastTick.sendMessage("Banknifty" + System.currentTimeMillis()%10, "" + Math.random()%100);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}

    }

    private static String getRouting(String[] strings) {
        if (strings.length < 1)
            return "anonymous.info";
        return strings[0];
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 2)
            return "Hello World!";
        return joinStrings(strings, " ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex) {
        int length = strings.length;
        if (length == 0) return "";
        if (length < startIndex) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}