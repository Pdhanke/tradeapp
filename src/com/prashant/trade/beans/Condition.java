package com.prashant.trade.beans;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class Condition {
	
	final static Logger logger = Logger.getLogger(Condition.class);
	
	private int type;
	private int value;
	/*
	 * Example1 : FirstPrice, BANKNIFTY19OCTFUT
	 * Example1 : LastMinuteVolume, BANKNIFTY19OCTFUT
	 */
	private HashMap<String, String> symbols;

	public Condition() {
		// TODO Auto-generated constructor stub
	}
	
	public void populateCondition() {
		
	}
	
	public boolean isTrue() {
		
		
		return false;
		
	}

}
