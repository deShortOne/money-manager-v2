package com.github.deShortOne.Budget;

import java.util.ArrayList;

public class MainBudget {

	public static void main(String[] args) {
		ArrayList<BudgetGroup> lis = DataHandler.getCat();

		for (BudgetGroup bg : lis) {
			System.out.println(bg.getName());
			for (BudgetCategory bc : bg.getCategoryList()) {
				System.out.println(String.format(" > %s : £%.2f : £%.2f", bc.getCategory().getName(), bc.getPlanned(),
						bc.getActual()));
			}
			System.out.println();
		}
	}
}
