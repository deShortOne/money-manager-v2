package com.github.deShortOne.Budget;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;


public class BudgetCategory extends BudgetDataValue {
	private final int categoryID;
	private BudgetGroup budgetGroup;

	public BudgetCategory(ResultSet budgetCategory, BudgetGroup bg) throws SQLException {
		super(budgetCategory.getDouble("Actual"), budgetCategory.getDouble("Planned"));

		this.categoryID = budgetCategory.getInt("CategoryID");
		this.budgetGroup = bg;
	}

	public Category getCategory() {
		return DataObjects.getCategory(categoryID);
	}

	@Override
	public String getTableCellValue() {
		return getCategory().getName();
	}

	@Override
	public void updatePlanned(double amount) {
		budgetGroup.updatePlanned(amount - getPlanned());
		super.setPlanned(amount);
	}

	public void updateBudgetGroup(BudgetGroup bg) {
		this.budgetGroup = bg;
	}

	public BudgetGroup getBudgetGroup() {
		return budgetGroup;
	}
}
