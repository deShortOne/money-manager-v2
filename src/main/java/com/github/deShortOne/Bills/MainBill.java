package com.github.deShortOne.Bills;

import java.time.LocalDate;
import java.util.ArrayList;

import com.github.deShortOne.Engine.Account;
import com.github.deShortOne.Engine.Category;
import com.github.deShortOne.Engine.MoneyManager;
import com.github.deShortOne.Engine.Payment;
import com.github.deShortOne.Recurrence.FrequencyType;
import com.github.deShortOne.Recurrence.Recurrence;

public class MainBill {

	public static void main(String[] args) {
		MoneyManager.getAccount(2).getId(); // !! MUST ENSURE MONEYMANAGER ALREADY INITALISED
		BillInfo bi = getBills();
		bi.setAmount(-5);

		Account payerName = bi.getPayerAccount();
		Account payeeName = bi.getPayeeAccount();
		bi.setPayeeAccount(payerName);
		bi.setPayerAccount(payeeName);

		boolean isSuccess = updateBill(bi);

		if (isSuccess) {
			System.out.println("Update succeeded");
		} else {
			System.out.println("Update Failed");
		}

		getBills();

		BillInfo biNew = addNewBill(MoneyManager.getAccount(2), MoneyManager.getAccount(1), -10, 
				new Recurrence(FrequencyType.DAILY, null, LocalDate.of(2023, 1, 10), LocalDate.of(2023, 1, 10), LocalDate.of(2023, 1, 10)),
				MoneyManager.getCategory(1), MoneyManager.getPayment(1));
		if (biNew == null) {
			System.out.println("Insert failed");
		} else {
			System.out.println("Insert succeeded");
			System.out.println(BillInfo.headers);
			System.out.println(biNew);
			System.out.println("All");
			getBills();
		}
	}

	public static BillInfo getBills() {
		ArrayList<BillInfo> bills = DataHandler.getBills();
		System.out.println(BillInfo.headers);
		for (BillInfo bi : bills) {
			System.out.println(bi);
		}
		return bills.get(0);
	}

	public static boolean updateBill(BillInfo updatedBill) {
		return DataHandler.updateBill(updatedBill);
	}

	public static BillInfo addNewBill(Account payerAccount, Account payeeAccount, double amount, Recurrence frequency,
			Category category, Payment payment) {
		return DataHandler.addNewBill(payerAccount, payeeAccount, amount, frequency, category, payment);
	}
}
