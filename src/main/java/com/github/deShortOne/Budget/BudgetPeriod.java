package com.github.deShortOne.Budget;

import java.time.LocalDate;

public enum BudgetPeriod {

	NEXT_YEAR("Next Year"),
	CURRENT_YEAR("Current Year"),
	NEXT_MONTH("Next Month"),
	CURRENT_MONTH("Current Month"),
	LAST_THIRTY_DAYS(""),
	LAST_MONTH(""),
	SPECIFIC_MONTH(null);

	BudgetPeriod(String name) {

	}

	/**
	 * Inclusive first day, exclusive last day.
	 * 
	 * @param bp
	 * @param ld
	 * @return
	 */
	public static LocalDate[] getStartEndDates(BudgetPeriod bp, LocalDate ld) {
		switch (bp) {
		case NEXT_YEAR:
			return new LocalDate[] { LocalDate.of(ld.getYear() + 1, 1, 1), LocalDate.of(ld.getYear() + 2, 1, 1) };
		case CURRENT_YEAR:
			return new LocalDate[] { LocalDate.of(ld.getYear(), 1, 1), LocalDate.of(ld.getYear() + 1, 1, 1) };
		case NEXT_MONTH:
			return new LocalDate[] { ld.withDayOfMonth(1).plusMonths(1), ld.withDayOfMonth(1).plusMonths(2) };
		case CURRENT_MONTH:
			return new LocalDate[] { ld.withDayOfMonth(1), ld.withDayOfMonth(1).plusMonths(1) };
		case LAST_THIRTY_DAYS:
			// might need to check validity of -30 days
			return new LocalDate[] { ld.minusDays(30), ld.plusDays(1) };
		case LAST_MONTH:
			return new LocalDate[] { LocalDate.of(ld.getYear(), ld.getMonthValue() - 1, 1),
					LocalDate.of(ld.getYear() + 1, ld.getMonth(), 1) };
		default:
			return null;
		}
	}
}
