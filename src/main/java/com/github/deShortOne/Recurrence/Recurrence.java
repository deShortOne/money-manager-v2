package com.github.deShortOne.Recurrence;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class Recurrence {

	/**
	 * Mandatory.
	 */
	private FrequencyType frequencyType = null;

	/**
	 * Mandatory. Will be based off {@code FrequencyType}.
	 */
	private Frequency frequency = null;

	/**
	 * Defines how many weeks, months or years in between each event. Not used for
	 * daily. Will be based off {@code FrequencyType}.
	 */
	private int skips = 0;

	/**
	 * Used for weekly, defines which day.
	 */
	private DayOfWeek dayOfWeek = null;

	/**
	 * Used for monthly, defines which day of month.
	 */
	private DayNumber dayOfMonth = null;

	/**
	 * Used for yearly, defines which day in which month of the year.
	 */
	private MonthDay dayMonthOfYear = null;

	/**
	 * Optional, set the date for the last occurrence. Null means infinite
	 */
	private LocalDate endDate = null;

	/**
	 * Mandatory, but can be null. The next date that occurs in this frequency.
	 */
	private LocalDate dueDate = null;

	/**
	 * Creates a recurrence object.
	 *
	 * @param frequencyType
	 * @param temporal      must be {@code DayOfWeek}, {@code DayNumber} or
	 *                      {@code MonthDay}
	 */
	public Recurrence(FrequencyType frequencyType, LocalDate dueDate, LocalDate endDate) {
		updateSelf(frequencyType, dueDate, endDate);
	}

	/**
	 * Generates a Recurrence object based on {@code convertToString}.
	 *
	 * @param text
	 */
	public Recurrence(String text) {
		String[] fields = text.split(";");
		frequencyType = FrequencyType.of(Integer.parseInt(fields[0]));

		updateSelf(FrequencyType.of(Integer.parseInt(fields[0])), fields[1] == "" ? null : LocalDate.parse(fields[1]),
				fields[2] == "" ? null : LocalDate.parse(fields[2]));
	}

	private void updateSelf(FrequencyType frequencyType, LocalDate dueDate, LocalDate endDate) {
		if (frequencyType == null) {
			throw new NullPointerException("FrequencyType cannot be null");
		}

		this.frequencyType = frequencyType;
		this.frequency = frequencyType.getFrequency();
		this.skips = frequencyType.getSkips();

		if (dueDate != null) {
			if (frequency == Frequency.DAILY || frequency == Frequency.ONE_TIME) {

			} else if (frequency == Frequency.WEEKLY) {
				dayOfWeek = dueDate.getDayOfWeek();
			} else if (frequency == Frequency.MONTHLY) {
				dayOfMonth = DayNumber.of(dueDate.getDayOfMonth());
			} else if (frequency == Frequency.YEARLY) {
				dayMonthOfYear = MonthDay.from(dueDate);
			} else {
				// throw
			}
		}

		this.endDate = endDate;
		this.dueDate = dueDate;
	}

	public FrequencyType getFrequencyType() {
		return frequencyType;
	}

	public String getFrequency() {
		return frequencyType.getName();
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	/**
	 * Gets the next date according to set frequency.
	 *
	 * @param ld date to be evaluated, not null
	 * @return next date
	 * @throw NullPointerException if ld is null
	 */
	public LocalDate getNextDueDate() {
		if (dueDate == null) {
			return null;
		}

		LocalDate ld = dueDate;
		TemporalAdjuster ta;
		switch (frequency) {
		case DAILY:
			ld = ld.plusDays(1);
			break;
		case WEEKLY:
			ta = TemporalAdjusters.next(dayOfWeek);
			ld = ld.with(ta).plusWeeks(skips);
			break;
		case MONTHLY:
			if (ld.getDayOfMonth() >= dayOfMonth.getValue()
					|| YearMonth.of(ld.getYear(), ld.getMonth()).lengthOfMonth() == ld.getDayOfMonth()) {
				ld = ld.plusMonths(1);
			}
			ld = ld.plusMonths(skips);
			int daysInMonth = YearMonth.of(ld.getYear(), ld.getMonth()).lengthOfMonth();
			ld = ld.withDayOfMonth(Math.min(daysInMonth, dayOfMonth.getValue()));
			break;
		case YEARLY:
			LocalDate ld2 = LocalDate.now().with(dayMonthOfYear).withYear(ld.getYear());
			if (ld2.isAfter(ld)) {
				ld = ld.with(dayMonthOfYear).plusYears(skips);
			} else {
				ld = ld.plusYears(skips + 1).with(dayMonthOfYear);
			}
			break;
		default: // skips ONE_TIME
			return null;
		}
		if (endDate != null && ld.isAfter(endDate)) {
			return null;
		}
		return ld;
	}

	/**
	 * Gets currently set end date.
	 *
	 * @return end date
	 */
	public LocalDate getEndDate() {
		return this.endDate;
	}

	/**
	 * Sets the due date to next. Returns null if due date is has passed end date.
	 *
	 * @return the next date/ currently set date
	 */
	public LocalDate updateDueDate() {
		if (dueDate == null)
			return null;
		dueDate = getNextDueDate();
		return dueDate;
	}

	/**
	 * Each part is split by semi-colon.
	 * FrequencyTypeID;StartDate;EndDate;NextPayDate; The rest of the string is
	 * frequency specific
	 *
	 * @return
	 */
	public String convertToString() {
		StringBuilder genericConversionString = new StringBuilder().append(frequencyType.getID())
			.append(";")
			.append(dueDate == null ? "" : dueDate.toString())
			.append(";")
			.append(endDate == null ? "" : endDate.toString());

		genericConversionString.append(";EOF");
		return genericConversionString.toString();
	}

	public void updateRecurrence(FrequencyType frequencyType, LocalDate newDueDate, LocalDate endDate) {
		updateSelf(frequencyType, newDueDate, endDate);
	}
}
