package com.github.deShortOne.Budget;

import com.github.deShortOne.DataObjects.BaseDataValue;

public abstract class BillDataValue implements BaseDataValue, MoneyTableCell {

	private double actual;
	private double planned;

	public BillDataValue(double actual, double planned) {
		this.actual = actual;
		this.planned = planned;
	}

	public abstract void updatePlanned(double amount);
	
	public void setPlanned(double amount) {
		this.planned = amount;
	}

	public double getPlanned() {
		return planned;
	}

	public double getActual() {
		return actual;
	}

	public double getDifference() {
		return actual - planned;
	}

}
