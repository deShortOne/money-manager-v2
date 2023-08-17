package com.github.deShortOne.Bills;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.github.deShortOne.SQL.SQLExecutor;

public class DataHandler {

	public static ArrayList<BillInfo> getBills() {
		String getBills = new StringBuilder().append("SELECT bills.ID, ")
			.append("	payer_accounts.AccountName AS payer, ")
			.append("	payee_accounts.AccountName AS payee, ")
			.append("	amount ")
			.append("FROM bills ")
			.append("INNER JOIN accounts AS payer_accounts ")
			.append("ON payer_accounts.ID = bills.PayerAccount ")
			.append("INNER JOIN accounts AS payee_accounts ")
			.append("ON payee_accounts.ID = bills.PayeeAccount ")
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
			.append("SET PayerAccount = (SELECT ID FROM accounts WHERE AccountName = '%s'), ")
			.append("PayeeAccount = (SELECT ID FROM accounts WHERE AccountName = '%s'), ")
			.append("Amount = %f ")
			.append("WHERE ID = %d")
			.toString();

		boolean isSuccess;
		try {
			SQLExecutor.changeTable(String.format(updateBill, updatedBill.getPayerName(), updatedBill.getPayeeName(),
					updatedBill.getAmount(), updatedBill.getID()));
			isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
			isSuccess = false;
		}

		return isSuccess;
	}
}
