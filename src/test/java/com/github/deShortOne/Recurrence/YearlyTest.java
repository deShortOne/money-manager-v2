package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YearlyTest {
	private Random random = new Random();

	private Recurrence rec;

	private LocalDate currDate = LocalDate.of(2023, 6, 15);
	private MonthDay monthDay;
	private LocalDate nextDate;

	@BeforeEach
	public void startEach() {
		int randomMonthInYearInt = random.nextInt(12) + 1;
		int year = currDate.getYear();
		if (randomMonthInYearInt < currDate.getMonthValue()) {
			year++;
		}
		int daysInMonth;
		int randomDayInMonthInt;

		if (randomMonthInYearInt != currDate.getMonthValue()) {
			daysInMonth = YearMonth.of(year, randomMonthInYearInt).lengthOfMonth();
			randomDayInMonthInt = random.nextInt(daysInMonth) + 1;
		} else {
			// if the months are equal then will have to decide if the day should be before,
			// on or after the currDate
			// could I take out the guts from this if statement? Yes, can I be bothered?
			// Heck no, been working on this for way too long
			if (random.nextBoolean()) {
				daysInMonth = YearMonth.of(year, randomMonthInYearInt).lengthOfMonth();
				randomDayInMonthInt = random.nextInt(currDate.getDayOfMonth(), daysInMonth) + 1;
			} else {
				year++;
				daysInMonth = YearMonth.of(year, randomMonthInYearInt).lengthOfMonth();
				randomDayInMonthInt = random.nextInt(currDate.getDayOfMonth() - 1) + 1;
			}
		}
		monthDay = MonthDay.of(randomMonthInYearInt, randomDayInMonthInt);

		nextDate = LocalDate.of(year, randomMonthInYearInt, randomDayInMonthInt);
		rec = new Recurrence(FrequencyType.YEARLY, monthDay, currDate.withYear(2020), null, nextDate);
	}

	@Test
	public void currDateBeforeStartDate() {
		LocalDate startDate = currDate.plusYears(5);
		assertTrue(startDate.isAfter(currDate));

		rec.setStartDate(startDate);
		LocalDate properDate = nextDate.withYear(startDate.getYear());
		if (properDate.isBefore(startDate) && properDate.isAfter(currDate.minusDays(1))) {
			properDate = properDate.plusYears(1);
		}
		assertEquals(rec.getNextDate(currDate), properDate, properDate + "");
	}

	@Test
	public void onStartDate() {
		rec.setStartDate(currDate);
		assertEquals(rec.getNextDate(currDate), nextDate);
	}

	@Test
	public void afterEndDate() {
		LocalDate endDate = LocalDate.of(2023, 6, 10);
		assertTrue(endDate.isBefore(currDate));

		rec.setEndDate(endDate);
		assertEquals(rec.getNextDate(currDate), null);
	}

	@Test
	public void onEndDate() {
		LocalDate endDate = LocalDate.of(2023, 1, 10);
		rec.setEndDate(endDate);
		assertEquals(rec.getNextDate(endDate), null);
	}

	@Test
	public void normalUsage() {
		assertEquals(rec.getNextDate(currDate), nextDate);
	}

	@Test
	public void useCurrDate() {
		rec.setDueDate(currDate);
		assertEquals(rec.getNextDueDate(), nextDate);

		assertEquals(rec.updateDueDate(), nextDate);
		assertEquals(rec.updateDueDate(), nextDate.plusYears(1));
		assertEquals(rec.updateDueDate(), nextDate.plusYears(2));
	}

	// simulate user changing a frequency multiple times
	@Test
	public void changeDateOfYear() {
		assertEquals(rec.getNextDate(currDate), nextDate);

		startEach();
		assertEquals(rec.getNextDate(currDate), nextDate);
		startEach();
		assertEquals(rec.getNextDate(currDate), nextDate);
		startEach();
		assertEquals(rec.getNextDate(currDate), nextDate);
	}

	@Test
	public void everyOtherYears() {
		rec = new Recurrence(FrequencyType.EVERY_OTHER_YEAR, monthDay, currDate.withYear(2020), null, nextDate);

		rec.setDueDate(currDate);
		assertEquals(rec.updateDueDate(), nextDate.plusYears(1));
		assertEquals(rec.updateDueDate(), nextDate.plusYears(3));
		assertEquals(rec.updateDueDate(), nextDate.plusYears(5));
		assertEquals(rec.updateDueDate(), nextDate.plusYears(7));
	}

	@Test
	public void stringConversion() {
		Recurrence r = new Recurrence(FrequencyType.YEARLY, MonthDay.of(4, 13), currDate.withYear(2020), null,
				nextDate);
		String textToDatabase = r.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase);
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 10)), LocalDate.of(2023, 4, 13));
		assertEquals(r2.getNextDate(LocalDate.of(2023, 5, 10)), LocalDate.of(2024, 4, 13));
	}
}
