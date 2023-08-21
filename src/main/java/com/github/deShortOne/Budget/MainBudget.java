package com.github.deShortOne.Budget;

import java.time.LocalDate;
import java.util.HashMap;

import com.github.deShortOne.Engine.MoneyManager;

public class MainBudget {

	public static void main(String[] args) {
		System.out.println(String.format("%20s%15s", "Category", "Total Amount"));
		HashMap<Integer, Double> categoryToAmount = DataHandler.getCategoryToAmount(LocalDate.of(2023, 1, 1),
				LocalDate.of(2024, 1, 1));
		for (int i : categoryToAmount.keySet()) {
			System.out
				.println(String.format("%20s%15s", MoneyManager.getCategory(i).getName(), categoryToAmount.get(i)));
		}
		
		LocalDate[] ld = BudgetPeriod.getStartEndDates(BudgetPeriod.CURRENT_MONTH, LocalDate.now());
		System.out.println(ld[0]);
		System.out.println(ld[1]); 
	}
}
