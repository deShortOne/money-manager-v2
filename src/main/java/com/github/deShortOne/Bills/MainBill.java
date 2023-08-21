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

	static ArrayList<BillInfo> bills;
	
	public static void main(String[] args) {
		MoneyManager.getAccount(2).getId(); // !! MUST ENSURE MONEYMANAGER ALREADY INITALISED
		getBills();
		BillInfo bi = bills.get(0);
		bi.setAmount(-5);

		Account payerName = bi.getPayerAccount();
		Account payeeName = bi.getPayeeAccount();
		bi.setPayeeAccount(payerName);
		bi.setPayerAccount(payeeName);

		bi.setStartDate(bi.getStartDate().minusDays(1));

		boolean isSuccess = updateBill(bi);

		if (isSuccess) {
			System.out.println("Update succeeded");
		} else {
			System.out.println("Update Failed");
		}

		getBills();

		BillInfo biNew = addNewBill(MoneyManager.getAccount(2), MoneyManager.getAccount(1), -10,
				new Recurrence(FrequencyType.DAILY, null, LocalDate.of(2020, 1, 1), null, LocalDate.of(2024, 2, 2)),
				MoneyManager.getCategory(1), MoneyManager.getPayment(1), LocalDate.of(2023, 1, 10),
				LocalDate.of(2027, 1, 10));
		if (biNew == null) {
			System.out.println("Insert failed");
			return;
		} else {
			System.out.println("Insert succeeded");
			System.out.println(BillInfo.headers);
			System.out.println(biNew);
			System.out.println("All");
			getBills();
		}
		
		System.out.println("Transactions done");
		bi.doTransaction(20, MoneyManager.getPayment(1));
		printBills(); // or getBills() to confirm that it's in the db
		
		System.out.println("Transactions done for null last paid");
		bills.get(5).doTransaction(50, MoneyManager.getPayment(2));
		printBills(); // or getBills() to confirm that it's in the db
		
		System.out.println("Skip a few due dates");
		for (int i = 0; i < 3; i++) {
			bills.get(5).iterateDueDate(); // skip due dates
		}
		printBills();
	}

	public static void getBills() {
		bills = DataHandler.getBills();
		printBills();
	}
	
	public static void printBills() {
		System.out.println(BillInfo.headers);
		for (BillInfo bi : bills) {
			System.out.println(bi);
		}
	}

	public static boolean updateBill(BillInfo updatedBill) {
		return DataHandler.updateBill(updatedBill);
	}

	public static BillInfo addNewBill(Account payerAccount, Account payeeAccount, double amount, Recurrence frequency,
			Category category, Payment payment, LocalDate startDate, LocalDate endDate) {
		return DataHandler.addNewBill(payerAccount, payeeAccount, amount, frequency, category, payment);
	}
}
