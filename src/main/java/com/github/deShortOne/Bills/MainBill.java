package com.github.deShortOne.Bills;

import java.util.ArrayList;

public class MainBill {

	public static void main(String[] args) {
		BillInfo bi = getBills();
		bi.setAmount(-5);

		String payerName = bi.getPayerName();
		String payeeName = bi.getPayeeName();
		bi.setPayeeName(payerName);
		bi.setPayerName(payeeName);

		boolean isSuccess = updateBill(bi);

		if (isSuccess) {
			System.out.println("Update succeeded");
		} else {
			System.out.println("Update Failed");
		}

		getBills();

		BillInfo biNew = addNewBill("Acc 2 - payee", "Acc 1 - payer", -10);
		if (biNew == null) {
			System.out.println("Insert failed");
		} else {
			System.out.println("Insert succeeded");
			System.out.println(String.format("%5s%15s%15s%15s", "ID", "Payer", "Payee", "Amount"));
			System.out.println(
					String.format("%5d%15s%15s%15s", bi.getID(), bi.getPayerName(), bi.getPayeeName(), bi.getAmount()));
		}
	}

	public static BillInfo getBills() {
		ArrayList<BillInfo> bills = DataHandler.getBills();
		System.out.println(String.format("%5s%15s%15s%15s", "ID", "Payer", "Payee", "Amount"));
		for (BillInfo bi : bills) {
			System.out.println(
					String.format("%5d%15s%15s%15s", bi.getID(), bi.getPayerName(), bi.getPayeeName(), bi.getAmount()));
		}
		return bills.get(0);
	}

	public static boolean updateBill(BillInfo updatedBill) {
		return DataHandler.updateBill(updatedBill);
	}

	public static BillInfo addNewBill(String payerAccount, String payeeAccount, double amount) {
		return DataHandler.addNewBill(payerAccount, payeeAccount, amount);
	}
}
