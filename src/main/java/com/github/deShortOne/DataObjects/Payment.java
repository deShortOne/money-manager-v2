package com.github.deShortOne.DataObjects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Payment implements TableCellDataValue {

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

	@Override
	public String getTableCellValue() {
		return getName();
	}
}
