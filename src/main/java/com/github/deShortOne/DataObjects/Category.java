package com.github.deShortOne.DataObjects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category implements BaseDataValue {

	private final int id;
	private String name;
	private boolean isIncome;

	public Category(ResultSet category) throws SQLException {
		this.id = category.getInt("ID");
		this.name = category.getString("Name");
		this.isIncome = category.getBoolean("IsIncome");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIncome() {
		return isIncome;
	}

	public void setIncome(boolean isIncome) {
		this.isIncome = isIncome;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getTableCellValue() {
		return getName();
	}
}
