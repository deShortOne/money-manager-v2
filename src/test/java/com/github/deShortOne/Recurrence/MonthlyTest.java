package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonthlyTest {
	private Random random = new Random();

	private Recurrence rec;

	private LocalDate currDate = LocalDate.of(2023, 1, 15);
	private DayNumber dayOfMonth;
	private LocalDate nextDate;

	@BeforeEach
	public void startEach() {
		int randomDayInMonthInt = random.nextInt(31);
		dayOfMonth = DayNumber.getDayNumber(randomDayInMonthInt + 1);
		nextDate = currDate.withDayOfMonth(dayOfMonth.getValue());
		rec = new Recurrence(FrequencyType.MONTHLY, dayOfMonth, currDate.withYear(2020), null, nextDate);
		if (dayOfMonth.getValue() <= currDate.getDayOfMonth()) {
			nextDate = nextDate.plusMonths(1);
		}
	}

	@Test
	public void currDateBeforeStartDate() {
		LocalDate startDate = currDate.plusMonths(4);
		assertTrue(startDate.isAfter(currDate));

		rec.setStartDate(startDate);
		LocalDate properDate = nextDate;
		while (properDate.isBefore(startDate)) {
			properDate = properDate.plusMonths(1);
		}
		int daysInMonth = YearMonth.of(properDate.getYear(), properDate.getMonth()).lengthOfMonth();
		properDate = properDate.withDayOfMonth(Math.min(daysInMonth, dayOfMonth.getValue()));
		assertEquals(rec.getNextDate(currDate), properDate);
	}

	@Test
	public void currDateOnStartDate() {
		rec.setStartDate(currDate);
		assertEquals(rec.getNextDate(currDate), nextDate);
	}

	@Test
	public void currDateAfterEndDate() {
		LocalDate endDate = LocalDate.of(2023, 1, 10);
		assertTrue(endDate.isBefore(currDate));

		rec.setEndDate(endDate);
		assertEquals(rec.getNextDate(currDate), null);
	}

	@Test
	public void currDateOnEndDate() {
		LocalDate endDate = LocalDate.of(2023, 1, 10);
		rec.setEndDate(endDate);
		assertEquals(rec.getNextDate(endDate), null);
	}

	@Test
	public void normalUsage() {
		assertEquals(rec.getNextDate(currDate), nextDate, currDate + " " + nextDate + " " + dayOfMonth);
	}

	@Test
	public void useCurrDate() {
		rec.setDueDate(currDate);
		assertEquals(rec.getNextDueDate(), nextDate);

		assertEquals(rec.updateDueDate(), nextDate);
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(1));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(2));
	}

	// simulate user changing a frequency multiple times
	@Test
	public void changeMonth() {
		assertEquals(rec.getNextDate(currDate), nextDate);

		startEach();
		assertEquals(rec.getNextDate(currDate), nextDate);
		startEach();
		assertEquals(rec.getNextDate(currDate), nextDate);
		startEach();
		assertEquals(rec.getNextDate(currDate), nextDate);
	}

	@Test
	public void everyOtherMonths() {
		rec = new Recurrence(FrequencyType.EVERY_OTHER_MONTH, dayOfMonth, currDate.withYear(2020), null, currDate);

		assertEquals(rec.updateDueDate(), nextDate.plusMonths(1));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(3));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(5));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(7));
	}

	@Test
	public void everyThreeMonths() {
		rec = new Recurrence(FrequencyType.EVERY_THREE_MONTHS, dayOfMonth, currDate.withYear(2020), null, currDate);

		assertEquals(rec.updateDueDate(), nextDate.plusMonths(2));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(5));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(8));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(11));
	}

	@Test
	public void everyFourMonths() {
		rec = new Recurrence(FrequencyType.EVERY_FOUR_MONTHS, dayOfMonth, currDate.withYear(2020), null, currDate);

		assertEquals(rec.updateDueDate(), nextDate.plusMonths(3));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(7));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(11));
		assertEquals(rec.updateDueDate(), nextDate.plusMonths(15));
	}

	@Test
	public void stringConversion() {
		Recurrence r = new Recurrence(FrequencyType.MONTHLY, DayNumber.of(15), currDate.withYear(2020), null, nextDate);
		String textToDatabase = r.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase);
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 10)), LocalDate.of(2023, 4, 15));
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 15)), LocalDate.of(2023, 5, 15));
	}
}
