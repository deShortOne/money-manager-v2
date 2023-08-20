package com.github.deShortOne.Engine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.github.deShortOne.SQL.SQLExecutor;

public class DataHandler {

	public static HashMap<Integer, Account> getAccounts() {
		String getAccounts = new StringBuilder().append("SELECT ID, ")
			.append("	AccountName, ")
			.append("	SortCode, ")
			.append("	AccountNumber ")
			.append("FROM accounts;")
			.toString();

		HashMap<Integer, Account> output = new HashMap<>();
		try {
			ResultSet results = SQLExecutor.getTable(getAccounts);
			while (results.next()) {
				output.put(results.getInt("ID"), new Account(results));
			}
			results.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}

	public static HashMap<Integer, Category> getCategories() {
		String getCategories = new StringBuilder().append("SELECT ID, ")
			.append("	Name, ")
			.append("	IsIncome ")
			.append("FROM categories;")
			.toString();

		HashMap<Integer, Category> output = new HashMap<>();
		try {
			ResultSet results = SQLExecutor.getTable(getCategories);
			while (results.next()) {
				output.put(results.getInt("ID"), new Category(results));
			}
			results.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}

	public static HashMap<Integer, Payment> getPaymentMethods() {
		String getCategories = new StringBuilder().append("SELECT ID, ")
			.append("	Name ")
			.append("FROM payment_methods;")
			.toString();

		HashMap<Integer, Payment> output = new HashMap<>();
		try {
			ResultSet results = SQLExecutor.getTable(getCategories);
			while (results.next()) {
				output.put(results.getInt("ID"), new Payment(results));
			}
			results.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}
}
