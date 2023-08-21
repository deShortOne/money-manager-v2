package com.github.deShortOne.Budget;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

import com.github.deShortOne.SQL.SQLExecutor;

public class DataHandler {

	public static HashMap<Integer, Double> getCategoryToAmount(LocalDate startDate, LocalDate endDate) {
		String getBudgetGroups = new StringBuilder().append("SELECT CategoryID, sum(AmountPaid) as TotalAmount ")
			.append("FROM transactions ")
			.append("WHERE DatePaid >= str_to_date('%s', '%%Y-%%m-%%d') ")
			.append("AND DatePaid < str_to_date('%s', '%%Y-%%m-%%d') ")
			.append("GROUP BY CategoryID;")
			.toString();

		HashMap<Integer, Double> categoryToAmount = new HashMap<>();
		try {
			ResultSet results = SQLExecutor.getTable(String.format(getBudgetGroups, startDate, endDate));
			while (results.next()) {
				categoryToAmount.put(results.getInt("CategoryID"), results.getDouble("TotalAmount"));
			}
			results.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoryToAmount;
	}
}
