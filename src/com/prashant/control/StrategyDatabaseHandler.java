package com.prashant.control;

public class StrategyDatabaseHandler {

	public StrategyDatabaseHandler() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void readOrderParamsFromDB() {
		
	}
	
	public void upDateOrderStatusinDB() {
		
	}
	
	public void execute() {
		while(true) {
			readOrderParamsFromDB();
			if("newOrderRequestFOund" =="true") {
				//callBrokerHandlerToExecute
			}
			
			
		}
	}
	

}
