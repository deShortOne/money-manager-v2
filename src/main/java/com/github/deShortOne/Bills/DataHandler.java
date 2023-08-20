package com.github.deShortOne.Bills;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.github.deShortOne.Engine.Account;
import com.github.deShortOne.Engine.Category;
import com.github.deShortOne.Engine.Payment;
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
			.append("	StartDate, ")
			.append("	EndDate, ")
			.append("	transactions.DatePaid ")
			.append("FROM bills ")
			.append("LEFT JOIN ( ")
			.append("	SELECT BillID, ")
			.append("	MAX(DatePaid) AS DatePaid ")
			.append("	FROM transactions ")
			.append("	GROUP BY (BillID) ")
			.append(") transactions ")
			.append("on bills.id = transactions.BillID ")
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
			.append("StartDate = str_to_date('%s', '%%Y-%%m-%%d'), ")
			.append("EndDate = str_to_date('%s', '%%Y-%%m-%%d') ")
			.append("WHERE ID = %d;")
			.toString();

		boolean isSuccess;
		try {
			SQLExecutor.changeTable(String.format(updateBill, updatedBill.getPayerAccount().getId(),
					updatedBill.getPayeeAccount().getId(), updatedBill.getAmount(),
					updatedBill.getFrequency().convertToString(), updatedBill.getPaymentMethod().getId(),
					updatedBill.getCategory().getId(), updatedBill.getStartDate(), updatedBill.getEndDate(),
					updatedBill.getID()));
			isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
			isSuccess = false;
		}

		return isSuccess;
	}

	public static BillInfo addNewBill(Account payerAccount, Account payeeAccount, double amount, Recurrence frequency,
			Category category, Payment payment, LocalDate startDate, LocalDate endDate) {
		String addBill = new StringBuilder().append("INSERT INTO bills ")
			.append("(PayerAccount, PayeeAccount, Amount, Frequency, CategoryID, PaymentID, StartDate, EndDate)")
			.append("VALUES")
			.append("(%d, %d, %f, '%s', %d, %d, str_to_date('%s', '%%Y-%%m-%%d'), str_to_date('%s','%%Y-%%m-%%d'));")
			.toString();

		String getBill = new StringBuilder().append("SELECT bills.ID, ")
			.append("	PayerAccount, ")
			.append("	PayeeAccount, ")
			.append("	Amount, ")
			.append("	Frequency, ")
			.append("	transactions.DatePaid, ")
			.append("	PaymentID, ")
			.append("	CategoryID, ")
			.append("	StartDate, ")
			.append("	EndDate ")
			.append("FROM bills ")
			.append("LEFT JOIN ( ")
			.append("	SELECT BillID, ")
			.append("	MAX(DatePaid) AS DatePaid ")
			.append("	FROM transactions ")
			.append("	GROUP BY (BillID) ")
			.append(") transactions ")
			.append("on bills.id = transactions.BillID ")
			.append("WHERE ID = %d")
			.toString();

		BillInfo bi = null;
		try {
			String a = String.format(addBill, payerAccount.getId(), payeeAccount.getId(), amount,
					frequency.convertToString(), category.getId(), payment.getId(), startDate, endDate);
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
}
