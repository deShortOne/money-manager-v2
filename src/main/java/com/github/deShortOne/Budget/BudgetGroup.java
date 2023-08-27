package com.github.deShortOne.Budget;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BudgetGroup extends BillDataValue {

	private final int id;
	private String name;
	private ArrayList<BudgetCategory> categoryLis = new ArrayList<>();

	public BudgetGroup(ResultSet budgetGroup) throws SQLException {
		super(0.0, 0.0);
		this.id = budgetGroup.getInt("ID");
		this.name = budgetGroup.getString("Name");
	}

	public void addCategory(BudgetCategory budgetCategory) {
		categoryLis.add(budgetCategory);
		updatePlanned(budgetCategory.getPlanned());
	}

	public void removeCategory(BudgetCategory budgetCategory) {
		categoryLis.remove(budgetCategory);
		updatePlanned(-budgetCategory.getPlanned());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<BudgetCategory> getCategoryList() {
		return categoryLis;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getTableCellValue() {
		return getName();
	}

	@Override
	public void updatePlanned(double amount) {
		super.setPlanned(getPlanned() + amount);
	}
}
