package com.github.deShortOne.Budget;

public class SumData implements MoneyTableCell {

	private final String title;
	private Double planned;
	private Double actual;

	public SumData(String title, double planned, double actual) {
		this.title = title;
		this.planned = planned;
		this.actual = actual;
	}

	public String getTitle() {
		return title;
	}

	public Double getPlanned() {
		return planned;
	}

	public Double getActual() {
		return actual;
	}

	public void updateValue(Double planned, Double actual) {
		this.planned = planned;
		this.actual = actual;
	}

	public Double getDifference() {
		return actual - planned;
	}
}
