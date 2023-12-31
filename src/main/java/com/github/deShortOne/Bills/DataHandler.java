package com.github.deShortOne.Bills;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.github.deShortOne.DataObjects.Account;
import com.github.deShortOne.DataObjects.Category;
import com.github.deShortOne.DataObjects.Payment;
import com.github.deShortOne.Recurrence.Recurrence;
import com.github.deShortOne.SQL.SQLExecutor;

public class DataHandler {

	public static ArrayList<BillInfo> getBills() {
		String getBills = new StringBuilder().append("SELECT bills.ID, ")
			.append("	PayerAccount, ")
			.append("	PayeeAccount, ")
			.append("	Amount, ")
			.append("	Frequency, ")
			.append("	PaymentID, ")
			.append("	CategoryID, ")
			.append("	LastPaid ")
			.append("FROM bills ")
			.append("WHERE IsActive = TRUE ")
			.toString();

		ArrayList<BillInfo> output = new ArrayList<>();
		try {
			ResultSet results = SQLExecutor.getTable(getBills);
			while (results.next()) {
				output.add(new BillInfo(results));
			}
			results.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}

	public static boolean updateBill(BillInfo updatedBill) {
		String updateBill = new StringBuilder().append("UPDATE bills ")
			.append("SET PayerAccount = %d, ")
			.append("PayeeAccount = %d, ")
			.append("Amount = %f, ")
			.append("Frequency = '%s', ")
			.append("PaymentID = %d, ")
			.append("CategoryID = %d, ")
			.append("LastPaid = %s ")
			.append("WHERE ID = %d;")
			.toString();

		boolean isSuccess;
		try {
			String lastPaidValue;
			if (updatedBill.getLastPaid() == null) {
				lastPaidValue = null;
			} else {
				lastPaidValue = String.format("str_to_date('%s', '%%Y-%%m-%%d')", updatedBill.getLastPaid());
			}

			SQLExecutor.changeTable(String.format(updateBill, updatedBill.getPayerAccount().getId(),
					updatedBill.getPayeeAccount().getId(), updatedBill.getAmount(),
					updatedBill.getFrequency().convertToString(), updatedBill.getPaymentMethod().getId(),
					updatedBill.getCategory().getId(), lastPaidValue, updatedBill.getId()));
			isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
			isSuccess = false;
		}

		return isSuccess;
	}

	public static BillInfo addNewBill(Account payerAccount, Account payeeAccount, double amount, Recurrence frequency,
			Category category, Payment payment) {
		String addBill = new StringBuilder().append("INSERT INTO bills ")
			.append("(PayerAccount, PayeeAccount, Amount, Frequency, CategoryID, PaymentID)")
			.append("VALUES")
			.append("(%d, %d, %f, '%s', %d, %d);")
			.toString();

		String getBill = new StringBuilder().append("SELECT bills.ID, ")
			.append("	PayerAccount, ")
			.append("	PayeeAccount, ")
			.append("	Amount, ")
			.append("	Frequency, ")
			.append("	PaymentID, ")
			.append("	CategoryID, ")
			.append("	LastPaid ")
			.append("FROM bills ")
			.append("WHERE ID = %d")
			.toString();

		BillInfo bi = null;
		try {
			String a = String.format(addBill, payerAccount.getId(), payeeAccount.getId(), amount,
					frequency.convertToString(), category.getId(), payment.getId());
			SQLExecutor.changeTable(a);

			ResultSet results = SQLExecutor.getTable("SELECT LAST_INSERT_ID() AS id;");
			int id = -1;
			if (results.next()) {
				id = results.getInt("id");
			}
			results.close();

			results = SQLExecutor.getTable(String.format(getBill, id));
			if (results.next()) {
				bi = new BillInfo(results);
			}
			results.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bi;
	}

	public static boolean addTransaction(BillInfo bi, LocalDate datePaid, double amountPaid, Category category,
			Payment paymentID) {
		String addTransaction = new StringBuilder().append("INSERT INTO transactions ")
			.append("(BillID, PayerAccount, PayeeAccount, DatePaid, AmountPaid, CategoryID, PaymentID)")
			.append("VALUES")
			.append("(%d, %d, %d, str_to_date('%s', '%%Y-%%m-%%d'), %f, %d, %d);")
			.toString();

		boolean isSuccess;
		try {
			SQLExecutor.changeTable(String.format(addTransaction, bi.getId(), bi.getPayerAccount().getId(),
					bi.getPayeeAccount().getId(), datePaid, amountPaid, category.getId(), paymentID.getId()));
			isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
			isSuccess = false;
		}

		return isSuccess;
	}

	public static void deactivateBill(BillInfo bi) {
		String deactiveBill = new StringBuilder().append("UPDATE bills ")
			.append("SET IsActive = false ")
			.append("WHERE ID = %d")
			.toString();

		try {
			SQLExecutor.changeTable(String.format(deactiveBill, bi.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
