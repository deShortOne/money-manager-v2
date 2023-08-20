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
	private LocalDate startDate;
	private LocalDate endDate;

	public BillInfo(ResultSet bill) throws SQLException {
		this.ID = bill.getInt("ID");
		this.payer = MoneyManager.getAccount(bill.getInt("PayerAccount"));
		this.payee = MoneyManager.getAccount(bill.getInt("PayeeAccount"));
		this.amount = bill.getDouble("Amount");
		this.startDate = bill.getDate("StartDate") == null ? null : bill.getDate("StartDate").toLocalDate();
		this.endDate = bill.getDate("EndDate") == null ? null : bill.getDate("EndDate").toLocalDate();
		this.frequency = new Recurrence(bill.getString("Frequency"), startDate, endDate);
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
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		frequency.setStartDate(startDate);
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		frequency.setEndDate(endDate);
		this.endDate = endDate;
	}

	public LocalDate getDueDate() {
		LocalDate dateToProcess;
		if (lastPaid != null) {
			dateToProcess = lastPaid;
		} else if (frequency.getCurrDate() != null) {
			dateToProcess = frequency.getCurrDate();
		} else if (frequency.getStartDate() != null) {
			dateToProcess = frequency.getStartDate();
		} else {
			dateToProcess = LocalDate.now();
		}
		return frequency.getNextDate(dateToProcess);
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
