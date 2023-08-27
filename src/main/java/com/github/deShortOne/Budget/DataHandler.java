package com.github.deShortOne.Budget;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
}
