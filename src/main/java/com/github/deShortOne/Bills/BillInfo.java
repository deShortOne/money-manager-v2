package com.github.deShortOne.Bills;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BillInfo {

	private final int ID;
	private String payerName;
	private String payeeName;
	private double amount;
	
	public BillInfo(ResultSet bill) throws SQLException {
		this.ID = bill.getInt("ID");
		this.payerName = bill.getString("payer");
		this.payeeName = bill.getString("payee");
		this.amount = bill.getDouble("amount");
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getID() {
		return ID;
	}
}
