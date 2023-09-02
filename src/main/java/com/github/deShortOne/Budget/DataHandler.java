package com.github.deShortOne.Budget;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.SQL.SQLExecutor;

public class DataHandler {

	public static ArrayList<BudgetGroup> getCat() {
		HashMap<Integer, BudgetGroup> idToBudgetGroup = new HashMap<>();
		ArrayList<BudgetGroup> budgetGroupLis = new ArrayList<>();

		String getBudgetGroups = new StringBuilder().append("SELECT ID, ")
			.append("Name ")
			.append("FROM budget_groups ")
			.append("ORDER BY ID ASC ")
			.toString();

		String getCategoryToBudget = new StringBuilder().append("SELECT BudgetGroupID, ")
			.append("budget_to_categories.CategoryID, ")
			.append("Planned, ")
			.append("Actual ")
			.append("FROM budget_to_categories ")
			.append("LEFT JOIN (")
			.append("	SELECT CategoryID,  ")
			.append("	sum(AmountPaid) as Actual ")
			.append("	FROM transactions ")
			.append("	GROUP BY CategoryID ")
			.append(") TotalAmount ")
			.append("on budget_to_categories.CategoryID = TotalAmount.CategoryID ")
			.toString();

		try {
			ResultSet results = SQLExecutor.getTable(getBudgetGroups);
			while (results.next()) {
				BudgetGroup bg = new BudgetGroup(results);
				budgetGroupLis.add(bg);
				idToBudgetGroup.put(bg.getId(), bg);
			}
			results.close();

			results = SQLExecutor.getTable(getCategoryToBudget);
			while (results.next()) {
				BudgetGroup bg = idToBudgetGroup.get(results.getInt("BudgetGroupID"));
				BudgetCategory bc = new BudgetCategory(results, bg);
				bg.addCategory(bc);
			}
			results.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return budgetGroupLis;
	}

	public static void addCategory(BudgetGroup bg, Category category) {
		try {
			String addCategoryToBudgetGroup = new StringBuilder().append("INSERT INTO budget_to_categories ")
				.append("(BudgetGroupID, CategoryID) ")
				.append("VALUES ")
				.append("(%d, %d) ")
				.toString();

			SQLExecutor.changeTable(String.format(addCategoryToBudgetGroup, bg.getId(), category.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void removeCategoryFromBudgetGroup(BudgetGroup bg, Category category) {
		try {
			String addCategoryToBudgetGroup = new StringBuilder().append("DELETE FROM budget_to_categories ")
				.append("WHERE BudgetGroupID = %d AND CategoryID = %d")
				.toString();

			SQLExecutor.changeTable(String.format(addCategoryToBudgetGroup, bg.getId(), category.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void categoryChangeBudgetGroup(BudgetCategory bc, BudgetGroup bg, Category category) {
		try {
			String addCategoryToBudgetGroup = new StringBuilder().append("UPDATE budget_to_categories ")
				.append("set BudgetGroupID = %d, CategoryID = %d ")
				.append("WHERE ")
				.append("BudgetGroupID = %d AND CategoryID = %d ")
				.toString();

			SQLExecutor.changeTable(String.format(addCategoryToBudgetGroup, bg.getId(), category.getId(), bc.getBudgetGroup().getId(), bc.getCategory().getId()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
