package com.github.deShortOne.DataObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class DataObjects {

	private static HashMap<Integer, Account> accounts;
	private static HashMap<Integer, Category> categories;
	private static HashMap<Integer, Payment> paymentMethods;

	static {
		refreshTables();
	}

	public static void refreshTables() {
		accounts = DataHandler.getAccounts();
		categories = DataHandler.getCategories();
		paymentMethods = DataHandler.getPaymentMethods();
	}

	public static Account getAccount(int id) {
		return accounts.get(id);
	}

	public static ArrayList<Account> getAllAccounts() {
		return new ArrayList<>(accounts.values());
	}

	public static Category getCategory(int id) {
		return categories.get(id);
	}

	public static ArrayList<Category> getAllCategories() {
		return new ArrayList<>(categories.values());
	}

	public static Payment getPayment(int id) {
		return paymentMethods.get(id);
	}

	public static ArrayList<Payment> getAllPaymentMethods() {
		return new ArrayList<>(paymentMethods.values());
	}
}
