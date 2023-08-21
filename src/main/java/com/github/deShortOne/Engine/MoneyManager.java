package com.github.deShortOne.Engine;

import java.util.HashMap;

import com.github.deShortOne.Bills.MainBill;
import com.github.deShortOne.Budget.MainBudget;

public class MoneyManager {

	private static HashMap<Integer, Account> accounts;
	private static HashMap<Integer, Category> categories;
	private static HashMap<Integer, Payment> paymentMethods;

	static {
		refreshTables();
	}

	public static void main(String[] args) {
		MainBill.main(null);
		MainBudget.main(null);
	}

	public static void refreshTables() {
		accounts = DataHandler.getAccounts();
		categories = DataHandler.getCategories();
		paymentMethods = DataHandler.getPaymentMethods();
	}

	public static Account getAccount(int id) {
		return accounts.get(id);
	}

	public static Category getCategory(int id) {
		return categories.get(id);
	}

	public static Payment getPayment(int id) {
		return paymentMethods.get(id);
	}
}
