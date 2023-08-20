package com.github.deShortOne.Bills;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.deShortOne.Engine.Account;
import com.github.deShortOne.Engine.Category;
import com.github.deShortOne.Engine.Payment;
import com.github.deShortOne.SQL.SQLExecutor;

public class DataHandler {

	public static ArrayList<BillInfo> getBills() {
		String getBills = new StringBuilder().append("SELECT bills.ID, ")
			.append("	PayerAccount, ")
			.append("	PayeeAccount, ")
			.append("	Amount, ")
			.append("	Frequency, ")
			.append("	PaymentID, ")
			.append("	CategoryID ")
			.append("FROM bills ")
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
			.append("PaymentID = %d, ")
			.append("CategoryID = %d ")
			.append("WHERE ID = %d")
			.toString();

		boolean isSuccess;
		try {
			SQLExecutor.changeTable(String.format(updateBill, updatedBill.getPayerAccount().getId(),
					updatedBill.getPayeeAccount().getId(), updatedBill.getAmount(),
					updatedBill.getPaymentMethod().getId(), updatedBill.getCategory().getId(), updatedBill.getID()));
			isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
			isSuccess = false;
		}

		return isSuccess;
	}

	public static BillInfo addNewBill(Account payerAccount, Account payeeAccount, double amount, Category category,
			Payment payment) {
		String addBill = new StringBuilder().append("INSERT INTO bills ")
			.append("(PayerAccount, PayeeAccount, Amount, CategoryID, PaymentID)")
			.append("VALUES")
			.append("(%d, %d, %f, %d, %d);")
			.toString();

		String getBill = new StringBuilder().append("SELECT bills.ID, ")
			.append("	PayerAccount, ")
			.append("	PayeeAccount, ")
			.append("	Amount, ")
			.append("	Frequency, ")
			.append("	PaymentID, ")
			.append("	CategoryID ")
			.append("FROM bills ")
			.append("WHERE ID = %d")
			.toString();

		BillInfo bi = null;
		try {
			SQLExecutor.changeTable(String.format(addBill, payerAccount.getId(), payeeAccount.getId(), amount,
					category.getId(), payment.getId()));

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
}
