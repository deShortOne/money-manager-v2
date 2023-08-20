package com.github.deShortOne.Engine;

import java.util.HashMap;

public class MoneyManager {

	private static HashMap<Integer, Account> accounts = DataHandler.getAccounts();
	private static HashMap<Integer, Category> categories = DataHandler.getCategories();
	private static HashMap<Integer, Payment> paymentMethods = DataHandler.getPaymentMethods();

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
