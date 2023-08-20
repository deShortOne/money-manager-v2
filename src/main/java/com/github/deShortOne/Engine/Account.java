package com.github.deShortOne.Engine;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {

	private final int id;
	private String accountName;
	private String sortCode;
	private String accountNumber;
	
	public Account(ResultSet account) throws SQLException {
		this.id = account.getInt("ID");
		this.accountName = account.getString("AccountName");
		this.sortCode = account.getString("SortCode");
		this.accountNumber = account.getString("AccountNumber");
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getId() {
		return id;
	}
}
