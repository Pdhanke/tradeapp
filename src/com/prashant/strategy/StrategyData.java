package com.prashant.strategy;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.OrderParams;

@XmlRootElement(name = "strategy")
@XmlAccessorType(XmlAccessType.FIELD)

public class StrategyData {
	
	private String id;
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getStrategyName() {
		return strategyName;
	}


	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	public double setStopLoss() {
		return stopLoss;
	}


	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}


	public double getPartialProfit() {
		return partialProfit;
	}


	public void setPartialProfit(long partialProfit) {
		this.partialProfit = partialProfit;
	}


	public double getAvailableMargin() {
		return availableMargin;
	}


	public void setAvailableMargin(long availableMargin) {
		this.availableMargin = availableMargin;
	}


	public double getUtilizedMargin() {
		return utilizedMargin;
	}


	public void setUtilizedMargin(long utilizedMargin) {
		this.utilizedMargin = utilizedMargin;
	}


	public int getIncrementOrderSize() {
		return incrementOrderSize;
	}


	public void setIncrementOrderSize(int incrementOrderSize) {
		this.incrementOrderSize = incrementOrderSize;
	}


	public long getWaitTimeForCreatePosition() {
		return waitTimeForCreatePosition;
	}


	public void setWaitTimeForCreatePosition(long waitTimeForCreatePosition) {
		this.waitTimeForCreatePosition = waitTimeForCreatePosition;
	}


	public void setOrderParamList(List<OrderParams> orderParamList) {
		this.orderParamList = orderParamList;
	}


	private String strategyName = "BNWCEPESELL";
	private int version = 0;
	
	private double stopLoss= 20000.0;
	private double partialProfit= 6000.0;
	private double profitTarget = 50000.0;
	
	private int state = 0;
	
	@Override
	public String toString() {
		return "StrategyData [id=" + id + ", strategyName=" + strategyName + ", version=" + version + ", stopLoss="
				+ stopLoss + ", partialProfit=" + partialProfit + ", profitTarget=" + profitTarget + ", state=" + state
				+ ", availableMargin=" + availableMargin + ", utilizedMargin=" + utilizedMargin
				+ ", incrementOrderSize=" + incrementOrderSize + ", waitTimeForCreatePosition="
				+ waitTimeForCreatePosition + ", orderParamList=" + orderParamList + "]";
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public double getProfitTarget() {
		return profitTarget;
	}


	public void setProfitTarget(double profitTarget) {
		this.profitTarget = profitTarget;
	}


	private double availableMargin = 300000.0;
	private double utilizedMargin = 0.0;
	private int incrementOrderSize = 40;
	private long waitTimeForCreatePosition = 10000;
	
	public  List<OrderParams> orderParamList ;
	private List<String> orderIds = new LinkedList<String>();
	
	
	public List<String> getOrderIds() {
		return orderIds;
	}


	public void setOrderIds(List<String> orderIds) {
		this.orderIds = orderIds;
	}


	public double getStopLoss() {
		return stopLoss;
	}


	public void setPartialProfit(double partialProfit) {
		this.partialProfit = partialProfit;
	}


	public List<OrderParams> getOrderParamList () {
	return orderParamList;
	}
}
