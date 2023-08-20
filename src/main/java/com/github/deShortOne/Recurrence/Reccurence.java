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

public class Reccurence {

	/**
	 * Mandatory.
	 */
	private FrequencyType frequencyType = null;

	/**
	 * Mandatory.
	 */
	private Frequency frequency = null;

	/**
	 * Defines how many weeks, months or years in between each event. Not used for
	 * daily.
	 */
	private int skips = 0;

	/**
	 * Used for weekly, defines which day.
	 */
	private DayOfWeek dayOfWeek = null;

	/**
	 * Used for monthly and yearly, defines which day of month.
	 */
	private DayNumber dayOfMonth = null;

	/**
	 * Used for yearly, defines which day in the year.
	 */
	private MonthDay dayMonthOfYear = null;

	/**
	 * Optional, set the date for the first occurrence.
	 */
	private LocalDate startDate = null;

	/**
	 * Optional, set the date for the last occurrence.
	 */
	private LocalDate endDate = null;

	/**
	 * Options, date this frequency is at currently.
	 */
	private LocalDate currDate = null;

	/**
	 * Creates a reccurence object.
	 * 
	 * @param frequencyType
	 * @param temporal      must be {@code DayOfWeek}, {@code DayNumber} or
	 *                      {@code MonthDay}
	 * @throws NullPointerException             if either frequencyType is null or
	 *                                          temporal is null and frequencyType
	 *                                          frequency is not ONE_TIME or DAILY
	 * @throws UnsupportedTemporalTypeException if temporal is not
	 *                                          {@code DayOfWeek}, {@code DayNumber}
	 *                                          or {@code MonthDay}
	 */
	public Reccurence(FrequencyType frequencyType, TemporalAccessor temporal) {
		if (frequencyType == null) {
			throw new NullPointerException("FrequencyType cannot be null");
		}
		if (temporal == null && frequencyType.getFrequency() != Frequency.DAILY) {
			throw new NullPointerException(
					"Temporal cannot be null unless FrequencyType is Frequency.ONE_TIME or Frequency.DAILY");
		}

		this.frequencyType = frequencyType;
		this.frequency = frequencyType.getFrequency();
		this.skips = frequencyType.getSkips();
		if (temporal instanceof DayOfWeek) {
			dayOfWeek = (DayOfWeek) temporal;
		} else if (temporal instanceof DayNumber) {
			dayOfMonth = (DayNumber) temporal;
		} else if (temporal instanceof MonthDay) {
			dayMonthOfYear = (MonthDay) temporal;
		} else if (temporal == null){
			
		} else {
			throw new UnsupportedTemporalTypeException("Invalid value for temporal: " + temporal);
		}
	}

	/**
	 * Generates a Recurrence object based on {@code convertToString}.
	 * @param text
	 */
	public Reccurence(String text) {
		String[] fields = text.split(";");
		frequencyType = FrequencyType.of(Integer.parseInt(fields[0]));
		frequency = frequencyType.getFrequency();
		skips = frequencyType.getSkips();
		
		if (fields[1] != "") {
			startDate = LocalDate.parse(fields[1]);
		}
		if (fields[2] != "") {
			endDate = LocalDate.parse(fields[1]);
		}
		if (fields[3] != "") {
			currDate = LocalDate.parse(fields[1]);
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

	/**
	 * Default frequency. Can be used to create frequency for daily.
	 * 
	 * @param frequency
	 */
	@Deprecated
	public Reccurence(Frequency frequency) {
		this.frequency = frequency;
	}

	/**
	 * Used to create frequency for weekly.
	 * 
	 * @param dayOfWeek
	 */
	@Deprecated
	public Reccurence(DayOfWeek dayOfWeek) {
		setWeeklyFrequency(dayOfWeek);
	}

	/**
	 * Used to create frequency for month.
	 * 
	 * @param day
	 */
	@Deprecated
	public Reccurence(DayNumber day) {
		setMonthlyFrequency(day);
	}

	/**
	 * Used to create frequency for year.
	 * 
	 * @param dayMonthOfYear
	 */
	@Deprecated
	public Reccurence(MonthDay dayMonthOfYear) {
		setYearlyFrequency(dayMonthOfYear);
	}

	public boolean isValid() {
		switch (frequency) {
		case WEEKLY:
			return dayOfWeek != null;
		case MONTHLY:
			return dayOfMonth != null;
		case YEARLY:
			return dayMonthOfYear != null;
		default: // skips DAILY, ONE_TIME
			return true;
		}
	}

	/**
	 * Gets the next date using current date as date to be evaluated.
	 * 
	 * @return
	 */
	public LocalDate getNextDate() {
		if (currDate == null)
			return null;
		return getNextDate(currDate);
	}

	/**
	 * Gets the next date according to set frequency.
	 * 
	 * If passed in date is before start date, start date according the the set
	 * fields will be returned. E.g. If set to weekly on Wednesday and the first day
	 * is Sunday (1-1-2023), then the returned date will be Wednesday (4-1-2023).
	 * 
	 * @param ld date to be evaluated
	 * @return next date
	 */
	public LocalDate getNextDate(LocalDate ld) {
		if (!isValid()) {
			throw new IllegalArgumentException("Key parameters are missing");
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
	 * Set the end date.
	 * 
	 * @param endDate date of the last event
	 * @return true if end date is after start date else false
	 */
	public void setEndDate(LocalDate endDate) {
		if (endDate == null) {
			throw new NullPointerException("End date cannot be null");
		} else if (startDate != null && startDate.isAfter(endDate)) {
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
	 * @param currDate date to set as current
	 */
	public void setCurrDate(LocalDate currDate) {
		this.currDate = currDate;
	}

	/**
	 * Sets the current date to next if current date is not null.
	 * 
	 * @return the next date/ currently set date
	 */
	public LocalDate setCurrDate() {
		if (this.currDate == null)
			throw new NullPointerException("Current date has not yet been set");
		this.currDate = getNextDate(this.currDate);
		return this.currDate;
	}

	/**
	 * Gets the current date.
	 * 
	 * @return current date
	 */
	public LocalDate getCurrDate() {
		return this.currDate;
	}

	/**
	 * Sets the frequency to daily.
	 * 
	 * @return true
	 */
	public void setDayFrequency() {
		resetVariables();
		this.frequency = Frequency.DAILY;
	}

	/**
	 * Sets the frequency to weekly.
	 * 
	 * @param dayOfWeek day the event should occur on
	 * @return
	 */
	public void setWeeklyFrequency(DayOfWeek dayOfWeek) {
		if (dayOfWeek == null)
			throw new NullPointerException("dayOfWeek cannot be null");
		resetVariables();
		this.frequency = Frequency.WEEKLY;
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * Sets the frequency to monthly.
	 * 
	 * If day of month is outside of is not valid for a certain month, e.g. day of
	 * month to 31st but is currently February, then defaults to the nearest valid
	 * date.
	 * 
	 * @param day day the event should occur on
	 * @return true if day is valid
	 */
	public void setMonthlyFrequency(DayNumber day) {
		if (day == null)
			throw new NullPointerException("dayofMonth cannot be null");
		resetVariables();
		this.frequency = Frequency.MONTHLY;
		this.dayOfMonth = day;
	}

	/**
	 * Sets the frequency to yearly.
	 * 
	 * @param dayMonthOfYear day and month the event the should occur on
	 * @return true if day and month are valid
	 */
	public void setYearlyFrequency(MonthDay dayMonthOfYear) {
		if (dayMonthOfYear == null)
			throw new NullPointerException("dayMonthOfYear cannot be null");
		resetVariables();
		this.frequency = Frequency.YEARLY;
		this.dayMonthOfYear = dayMonthOfYear;
	}

	public void setSkip(int skips) {
		this.skips = skips;
	}

	/**
	 * Each part is split by semi-colon. FrequencyTypeID;StartDate;EndDate;CurrDate;
	 * The rest of the string is frequency specific
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
			.append(currDate == null ? "" : currDate.toString())
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

	private void resetVariables() {
		this.skips = 0;
		this.dayOfWeek = null;
		this.dayOfMonth = null;
		this.dayMonthOfYear = null;
	}
}
