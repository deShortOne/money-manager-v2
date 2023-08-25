package com.github.deShortOne.Bills;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.github.deShortOne.DataObjects.Account;
import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.DataObjects;
import com.github.deShortOne.DataObjects.Payment;
import com.github.deShortOne.Recurrence.FrequencyType;
import com.github.deShortOne.Recurrence.Recurrence;

public class BillInfo {

	private static String stringFormat = "%5s%20s%20s%10s%20s%20s%20s%20s";

	public static final String headers = String.format(stringFormat, "ID", "Payer", "Payee", "Amount", "Frequency",
			"Last Paid", "Due Date", "Category", "Payment Method");

	private final int id;
	private Account payer;
	private Account payee;
	private double amount;
	private Recurrence frequency;
	private LocalDate lastPaid;
	private Category category;
	private Payment paymentMethod;

	public BillInfo(ResultSet bill) throws SQLException {
		this.id = bill.getInt("ID");
		this.payer = DataObjects.getAccount(bill.getInt("PayerAccount"));
		this.payee = DataObjects.getAccount(bill.getInt("PayeeAccount"));
		this.amount = bill.getDouble("Amount");
		this.frequency = new Recurrence(bill.getString("Frequency"));
		this.lastPaid = bill.getDate("LastPaid") == null ? null : bill.getDate("LastPaid").toLocalDate();
		this.category = DataObjects.getCategory(bill.getInt("CategoryID"));
		this.paymentMethod = DataObjects.getPayment(bill.getInt("PaymentID"));
	}

	public void doTransaction(double amountPaid, Category category, Payment paymentMethod) {
		LocalDate datePaid = frequency.getDueDate(); // date should be set to today
		boolean success = DataHandler.addTransaction(this, datePaid, amountPaid, category, paymentMethod);

		if (success) {
			this.lastPaid = datePaid;
			iterateDueDate();
		}
	}

	public void updateBill(Account payer, Account payee, double amount, Category category, Payment paymentMethod,
			FrequencyType frequencyType, LocalDate newDueDate, LocalDate endDate) {
		this.payer = payer;
		this.payee = payee;
		this.amount = amount;
		this.frequency.updateRecurrence(frequencyType, newDueDate, endDate);;
		this.category = category;
		this.paymentMethod = paymentMethod;
		DataHandler.updateBill(this);
	}

	/**
	 * Moves due date to next date. Can also be used to skip. Updates database.
	 */
	public void iterateDueDate() {
		frequency.updateDueDate();
		DataHandler.updateBill(this);
	}

	public Account getPayerAccount() {
		return payer;
	}

	public Account getPayeeAccount() {
		return payee;
	}

	public double getAmount() {
		return amount;
	}

	public Category getCategory() {
		return category;
	}

	public Payment getPaymentMethod() {
		return paymentMethod;
	}

	public Recurrence getFrequency() {
		return frequency;
	}

	public LocalDate getLastPaid() {
		return lastPaid;
	}

	public LocalDate getEndDate() {
		return frequency.getEndDate();
	}

	public LocalDate getDueDate() {
		return frequency.getDueDate();
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format(stringFormat, id, payer.getAccountName(), payee.getAccountName(), amount,
				frequency.getFrequency(), getLastPaid(), getDueDate(), category.getName(), paymentMethod.getName());

	}
}
