package com.prashant.trade.beans;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "strategy")
@XmlAccessorType(XmlAccessType.FIELD)

public class StrategyData {

	private String id;
	private String strategyName = "BNWCEPESELL";
	private int version = 0;

	private double stopLoss = 20000.0;
	private double partialProfit = 6000.0;
	private double profitTarget = 50000.0;
	private double availableMargin = 300000.0;

	private int incrementOrderSize = 40;
	private long waitTimeForCreatePosition = 10000;
	
	private HashMap<String, Condition> conditions;

	public List<SOrderParams> orderParamList;

	public String getId() {
		return id;
	}

	public HashMap<String, Condition> getConditions() {
		return conditions;
	}

	public void setConditions(HashMap<String, Condition> conditions) {
		this.conditions = conditions;
	}

	public void setAvailableMargin(double availableMargin) {
		this.availableMargin = availableMargin;
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

	public void setOrderParamList(List<SOrderParams> orderParamList) {
		this.orderParamList = orderParamList;
	}

	@Override
	public String toString() {
		return "StrategyData [id=" + id + ", strategyName=" + strategyName + ", version=" + version + ", stopLoss="
				+ stopLoss + ", partialProfit=" + partialProfit + ", profitTarget=" + profitTarget
				+ ", availableMargin=" + availableMargin + ", incrementOrderSize=" + incrementOrderSize
				+ ", waitTimeForCreatePosition=" + waitTimeForCreatePosition + ", orderParamList=" + orderParamList
				+ "]";
	}

	public double getProfitTarget() {
		return profitTarget;
	}

	public void setProfitTarget(double profitTarget) {
		this.profitTarget = profitTarget;
	}



	public double getStopLoss() {
		return stopLoss;
	}

	public void setPartialProfit(double partialProfit) {
		this.partialProfit = partialProfit;
	}

	public List<SOrderParams> getOrderParamList() {
		return orderParamList;
	}
}
