package com.github.deShortOne.Engine;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Payment {

	private final int id;
	private String name;
	
	public Payment(ResultSet payment) throws SQLException {
		this.id = payment.getInt("ID");
		this.name = payment.getString("Name");
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
}
