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
}
