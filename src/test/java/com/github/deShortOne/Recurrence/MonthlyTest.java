package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonthlyTest {
	private Random random = new Random();

	private Reccurence rec;

	private LocalDate currDate = LocalDate.of(2023, 1, 15);
	private DayNumber dayNumber;
	private LocalDate nextDate;

	@BeforeEach
	public void startEach() {
		int randomDayInMonthInt = random.nextInt(31);
		dayNumber = DayNumber.getDayNumber(randomDayInMonthInt + 1);
		rec = new Reccurence(dayNumber);
		nextDate = currDate.withDayOfMonth(dayNumber.getValue());
		if (dayNumber.getValue() <= currDate.getDayOfMonth()) {
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
		properDate = properDate.withDayOfMonth(Math.min(daysInMonth, dayNumber.getValue()));
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
		assertEquals(rec.getNextDate(currDate), nextDate, currDate + " " + nextDate + " " + dayNumber);
	}

	@Test
	public void useCurrDate() {
		assertEquals(rec.getNextDate(), null);

		rec.setCurrDate(currDate);
		assertEquals(rec.getNextDate(), nextDate);

		assertEquals(rec.setCurrDate(), nextDate);
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(1));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(2));
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
	public void invalidUse() {
		assertTrue(rec.isValid());

		Reccurence badMonthly = new Reccurence(Frequency.MONTHLY);
		assertFalse(badMonthly.isValid());
		assertThrows(IllegalArgumentException.class, () -> badMonthly.getNextDate(currDate));
		assertThrows(NullPointerException.class, () -> badMonthly.setCurrDate());

		assertThrows(NullPointerException.class, () -> rec.setMonthlyFrequency(null));
	}
	
	@Test
	public void everyOtherMonths() {
		rec.setSkip(1);
		rec.setCurrDate(currDate);
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(1));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(3));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(5));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(7));
	}
	
	@Test
	public void everyThreeMonths() {
		rec.setSkip(2);
		rec.setCurrDate(currDate);
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(2));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(5));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(8));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(11));
	}
	
	@Test
	public void everyFourMonths() {
		rec.setSkip(3);
		rec.setCurrDate(currDate);
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(3));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(7));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(11));
		assertEquals(rec.setCurrDate(), nextDate.plusMonths(15));
	}

	@Test
	public void stringConversion() {
		Reccurence r = new Reccurence(FrequencyType.MONTHLY, DayNumber.of(15));
		String textToDatabase = r.convertToString();
		
		Reccurence r2 = new Reccurence(textToDatabase);
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 10)), LocalDate.of(2023, 4, 15));
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 15)), LocalDate.of(2023, 5, 15));
	}
}
