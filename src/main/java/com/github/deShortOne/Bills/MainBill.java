package com.github.deShortOne.Bills;

import java.util.ArrayList;

public class MainBill {
	
	public static void main(String[] args) {
		getBills();
	}
	
	public static void getBills() {
		ArrayList<BillInfo> bills = DataHandler.getBills();
		System.out.println(String.format("%5s%15s%15s%15s", "ID", "Payer", "Payee", "Amount"));
		for (BillInfo bi : bills) {
			System.out.println(
					String.format("%5d%15s%15s%15s", bi.getID(), bi.getPayerName(), bi.getPayeeName(), bi.getAmount()));
		}
	}
}
