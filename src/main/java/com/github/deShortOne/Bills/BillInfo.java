package com.github.deShortOne.Bills;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.github.deShortOne.Engine.Account;
import com.github.deShortOne.Engine.Category;
import com.github.deShortOne.Engine.MoneyManager;
import com.github.deShortOne.Engine.Payment;
import com.github.deShortOne.Recurrence.Recurrence;

public class BillInfo {

	private static String stringFormat = "%5s%20s%20s%10s%20s%20s%20s%20s";

	public static final String headers = String.format(stringFormat, "ID", "Payer", "Payee", "Amount", "Frequency",
			"Last Paid", "Due Date", "Category", "Payment Method");

	private final int ID;
	private Account payer;
	private Account payee;
	private double amount;
	private Recurrence frequency;
	private LocalDate lastPaid;
	private Category category;
	private Payment paymentMethod;

	public BillInfo(ResultSet bill) throws SQLException {
		this.ID = bill.getInt("ID");
		this.payer = MoneyManager.getAccount(bill.getInt("PayerAccount"));
		this.payee = MoneyManager.getAccount(bill.getInt("PayeeAccount"));
		this.amount = bill.getDouble("Amount");
		this.frequency = new Recurrence(bill.getString("Frequency"));
		this.lastPaid = bill.getDate("DatePaid") == null ? null : bill.getDate("DatePaid").toLocalDate();
		this.category = MoneyManager.getCategory(bill.getInt("CategoryID"));
		this.paymentMethod = MoneyManager.getPayment(bill.getInt("PaymentID"));
	}

	public Account getPayerAccount() {
		return payer;
	}

	public void setPayerAccount(Account payerName) {
		this.payer = payerName;
	}

	public Account getPayeeAccount() {
		return payee;
	}

	public void setPayeeAccount(Account payeeName) {
		this.payee = payeeName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Payment getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Payment paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Recurrence getFrequency() {
		return frequency;
	}

	public void setFrequency(Recurrence frequency) {
		this.frequency = frequency;
	}

	public LocalDate getLastPaid() {
		return lastPaid;
	}

	public void setLastPaid(LocalDate lastPaid) {
		this.lastPaid = lastPaid;
	}

	public LocalDate getStartDate() {
		return frequency.getStartDate();
	}

	public void setStartDate(LocalDate startDate) {
		frequency.setStartDate(startDate);
	}

	public LocalDate getEndDate() {
		return frequency.getEndDate();
	}

	public void setEndDate(LocalDate endDate) {
		frequency.setEndDate(endDate);
	}

	public LocalDate getDueDate() {
		return frequency.getNextDueDate();
	}

	public int getID() {
		return ID;
	}

	@Override
	public String toString() {
		return String.format(stringFormat, ID, payer.getAccountName(), payee.getAccountName(), amount,
				frequency.getFrequency(), lastPaid, getDueDate(), category.getName(), paymentMethod.getName());

	}
}
