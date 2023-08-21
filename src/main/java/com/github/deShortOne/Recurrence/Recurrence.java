package com.github.deShortOne.Recurrence;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.YearMonth;

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
	 * Mandatory. Set the date for the first occurrence.
	 */
	private LocalDate startDate = null;

	/**
	 * Optional, set the date for the last occurrence.
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
	 * @throws NullPointerException             if either frequencyType is null
	 * 
	 * @throws NullPointerException             if temporal is null and
	 *                                          frequencyType frequency is not
	 *                                          ONE_TIME or DAILY
	 * @throws NullPointerException             if startDate or dueDate is null
	 * @throws UnsupportedTemporalTypeException if temporal is not
	 *                                          {@code DayOfWeek}, {@code DayNumber}
	 *                                          or {@code MonthDay}
	 */
	public Recurrence(FrequencyType frequencyType, TemporalAccessor temporal, LocalDate startDate, LocalDate endDate,
			LocalDate dueDate) {
		if (dueDate == null)
			throw new NullPointerException("Due date cannot be null");
		updateSelf(frequencyType, temporal, startDate, endDate, dueDate);
	}

	/**
	 * Generates a Recurrence object based on {@code convertToString}.
	 * 
	 * @param text
	 */
	public Recurrence(String text) {
		String[] fields = text.split(";");
		frequencyType = FrequencyType.of(Integer.parseInt(fields[0]));
		frequency = frequencyType.getFrequency();
		skips = frequencyType.getSkips();

		startDate = LocalDate.parse(fields[1]);
		if (fields[2] != "") {
			endDate = LocalDate.parse(fields[2]);
		}
		if (fields[3] != "") {
			dueDate = LocalDate.parse(fields[3]);
		}

		switch (frequency) {
		case ONE_TIME:
		case DAILY:
			break;
		case WEEKLY:
			dayOfWeek = DayOfWeek.of(Integer.parseInt(fields[4]));
			break;
		case MONTHLY:
			dayOfMonth = DayNumber.of(Integer.parseInt(fields[4]));
			break;
		case YEARLY:
			dayMonthOfYear = MonthDay.parse(fields[4]);
			break;
		}
	}

	private void updateSelf(FrequencyType frequencyType, TemporalAccessor temporal, LocalDate startDate,
			LocalDate endDate, LocalDate dueDate) {
		resetVariables();

		if (frequencyType == null) {
			throw new NullPointerException("FrequencyType cannot be null");
		}
		if (temporal == null && frequencyType.getFrequency() != Frequency.ONE_TIME
				&& frequencyType.getFrequency() != Frequency.DAILY) {
			throw new NullPointerException(
					"Temporal cannot be null unless FrequencyType is Frequency.ONE_TIME or Frequency.DAILY");
		}

		if (startDate == null) {
			throw new NullPointerException("Start date cannot be null");
		}

		this.frequencyType = frequencyType;
		this.frequency = frequencyType.getFrequency();
		this.skips = frequencyType.getSkips();

		if (temporal instanceof DayOfWeek) {
			if (frequency != Frequency.WEEKLY) {
				throw new IllegalArgumentException("DayOfWeek parameter for temporal is used for weekly frequency");
			}
			dayOfWeek = (DayOfWeek) temporal;
		} else if (temporal instanceof DayNumber) {
			if (frequency != Frequency.MONTHLY) {
				throw new IllegalArgumentException("DayNumber parameter for temporal is used for monthly frequency");
			}
			dayOfMonth = (DayNumber) temporal;
		} else if (temporal instanceof MonthDay) {
			if (frequency != Frequency.YEARLY) {
				throw new IllegalArgumentException("MonthDay parameter for temporal is used for yearly frequency");
			}
			dayMonthOfYear = (MonthDay) temporal;
		} else if (temporal == null) {
			if (frequency != Frequency.ONE_TIME && frequency != Frequency.DAILY) {
				throw new IllegalArgumentException(
						"Null parameter for temporal is used for one_time or daily frequency");
			}
		} else {
			throw new UnsupportedTemporalTypeException("Invalid value for temporal: " + temporal);
		}

		this.startDate = startDate;
		this.endDate = endDate;
		this.dueDate = dueDate;
	}

	public String getFrequency() {
		return frequencyType.getName();
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	/**
	 * Gets the next due date using due date as date to be evaluated.
	 * 
	 * @return next date or null if there is no date after
	 * @throws NullPointerException if due date is not set. Either run
	 *                              {@code setDueDate(LocalDate nextPayDate)} or
	 *                              {@code getNextDate(LocalDate nextPayDate)}
	 */
	public LocalDate getNextDueDate() {
		if (dueDate == null) {
			throw new NullPointerException("Due date is null");
		}
		return getNextDate(dueDate);
	}

	/**
	 * Gets the next date according to set frequency.
	 * 
	 * If passed in date is before start date, start date according the the set
	 * fields will be returned. E.g. If set to weekly on Wednesday and the first day
	 * is Sunday (1-1-2023), then the returned date will be Wednesday (4-1-2023).
	 * 
	 * @param ld date to be evaluated, not null
	 * @return next date
	 * @throw NullPointerException if ld is null
	 */
	public LocalDate getNextDate(LocalDate ld) {
		if (ld == null) {
			throw new NullPointerException("ld cannot be null");
		}
		if (startDate != null && ld.isBefore(startDate)) {
			switch (frequency) {
			case WEEKLY:
				TemporalAdjuster ta = TemporalAdjusters.next(dayOfWeek);
				while (ld.isBefore(startDate)) {
					ld = ld.with(ta);
				}
				return ld;
			case MONTHLY:
				ld = ld.withMonth(startDate.getMonthValue());
				if (ld.getDayOfMonth() >= dayOfMonth.getValue()) {
					ld = ld.plusMonths(1);
				}
				int daysInMonth = YearMonth.of(ld.getYear(), ld.getMonth()).lengthOfMonth();
				ld = ld.withDayOfMonth(Math.min(daysInMonth, dayOfMonth.getValue()));
				return ld;
			case YEARLY:
				LocalDate ld2 = LocalDate.of(startDate.getYear(), dayMonthOfYear.getMonthValue(),
						dayMonthOfYear.getDayOfMonth());
				ld = ld.withYear(startDate.getYear());
				if (ld2.isBefore(ld)) {
					ld = ld.plusYears(1);
				}
				return ld.with(dayMonthOfYear);
			default: // skips ONE_TIME, DAILY
				return startDate;
			}
		}

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
			LocalDate ld2 = LocalDate.of(ld.getYear(), dayMonthOfYear.getMonthValue(), dayMonthOfYear.getDayOfMonth());
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
	 * Set the start date.
	 * 
	 * @param startDate date of first event
	 * @return true if start date is before end date else false
	 */
	public void setStartDate(LocalDate startDate) {
		if (startDate == null) {
			throw new NullPointerException("Start date cannot be null");
		} else if (endDate != null && startDate.isAfter(endDate)) {
			throw new DateTimeException("Start date cannot be after end date" + endDate.toString());
		}
		this.startDate = startDate;
	}

	/**
	 * Gets currently set start date.
	 * 
	 * @return start date
	 */
	public LocalDate getStartDate() {
		return this.startDate;
	}

	/**
	 * Sets the end date. Null end date means the frequency will never stop.
	 * 
	 * @param endDate date of the last event
	 * @return true if end date is after start date else false
	 */
	public void setEndDate(LocalDate endDate) {
		if (startDate != null && startDate.isAfter(endDate)) {
			throw new DateTimeException("End date cannot be before start date - " + startDate.toString());
		}
		this.endDate = endDate;
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
	 * Set the current date.
	 * 
	 * @param dueDate date to set as due date
	 */
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Sets the due date to next. Returns null if due date is has passed end date.
	 * 
	 * @return the next date/ currently set date
	 */
	public LocalDate updateDueDate() {
		if (dueDate == null)
			return null;
		dueDate = getNextDate(dueDate);
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
			.append(startDate == null ? "" : startDate.toString())
			.append(";")
			.append(endDate == null ? "" : endDate.toString())
			.append(";")
			.append(dueDate == null ? "" : dueDate.toString())
			.append(";");

		switch (frequency) {
		case WEEKLY:
			genericConversionString.append(dayOfWeek.getValue());
			break;
		case MONTHLY:
			genericConversionString.append(dayOfMonth.getValue());
			break;
		case YEARLY:
			genericConversionString.append(dayMonthOfYear.toString());
			break;
		default: // skip ONE_TIME and DAILY
			break;
		}
		genericConversionString.append(";EOF");
		return genericConversionString.toString();
	}

	public void updateRecurrence(FrequencyType frequencyType, TemporalAccessor temporal, LocalDate startDate,
			LocalDate endDate, LocalDate nextPayDate) {
		updateSelf(frequencyType, temporal, startDate, endDate, nextPayDate);
	}

	private void resetVariables() {
		this.skips = 0;
		this.dayOfWeek = null;
		this.dayOfMonth = null;
		this.dayMonthOfYear = null;
	}
}
