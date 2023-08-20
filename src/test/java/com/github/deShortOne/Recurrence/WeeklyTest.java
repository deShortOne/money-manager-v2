package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeeklyTest {
	private Random random = new Random();

	private Reccurence rec;
	private LocalDate currDate = LocalDate.of(2023, 1, 15);
	private DayOfWeek dayOfWeek;
	private LocalDate nextDate;

	private static HashMap<DayOfWeek, LocalDate> testCases = new HashMap<DayOfWeek, LocalDate>();

	@BeforeAll
	public static void initialisation() {
		testCases.put(DayOfWeek.MONDAY, LocalDate.of(2023, 1, 16));
		testCases.put(DayOfWeek.TUESDAY, LocalDate.of(2023, 1, 17));
		testCases.put(DayOfWeek.WEDNESDAY, LocalDate.of(2023, 1, 18));
		testCases.put(DayOfWeek.THURSDAY, LocalDate.of(2023, 1, 19));
		testCases.put(DayOfWeek.FRIDAY, LocalDate.of(2023, 1, 20));
		testCases.put(DayOfWeek.SATURDAY, LocalDate.of(2023, 1, 21));
		testCases.put(DayOfWeek.SUNDAY, LocalDate.of(2023, 1, 22));
	}

	@BeforeEach
	public void startEach() {
		int randomDayOfWeekInt = random.nextInt(7);

		this.dayOfWeek = DayOfWeek.of(randomDayOfWeekInt + 1);
		this.nextDate = testCases.get(dayOfWeek);

		rec = new Reccurence(this.dayOfWeek);
	}

	@Test
	public void currDateBeforeStartDate() {
		LocalDate startDate = currDate.plusMonths(4);
		assertTrue(startDate.isAfter(currDate));

		rec.setStartDate(startDate);

		LocalDate properNextDate = nextDate;
		while (properNextDate.isBefore(startDate)) {
			properNextDate = properNextDate.plusWeeks(1);
		}

		assertEquals(rec.getNextDate(currDate), properNextDate);
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
		assertEquals(rec.getNextDate(currDate), nextDate);
	}

	@Test
	public void useCurrDate() {
		assertEquals(rec.getNextDate(), null);

		rec.setCurrDate(currDate);
		assertEquals(rec.getNextDate(), nextDate);

		assertEquals(rec.setCurrDate(), nextDate);
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(1));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(2));
	}

	// simulate user changing a frequency multiple times
	@Test
	public void changeWeek() {
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

		Reccurence badWeekly = new Reccurence(Frequency.WEEKLY);
		assertFalse(badWeekly.isValid());
		assertThrows(IllegalArgumentException.class, () -> badWeekly.getNextDate(currDate));
		assertThrows(NullPointerException.class, () -> badWeekly.setCurrDate());

		assertThrows(NullPointerException.class, () -> rec.setWeeklyFrequency(null));
	}
	
	@Test
	public void everyOtherWeek() {
		rec.setSkip(1);
		rec.setCurrDate(currDate);
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(1));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(3));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(5));
	}
	
	@Test
	public void everyFourWeeks() {
		rec.setSkip(3);
		rec.setCurrDate(currDate);
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(3));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(7));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(11));
	}
	
	@Test
	public void stringConversion() {
		Reccurence r = new Reccurence(FrequencyType.WEEKLY, DayOfWeek.of(1));
		String textToDatabase = r.convertToString();
		
		Reccurence r2 = new Reccurence(textToDatabase);
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 9)), LocalDate.of(2023, 4, 10));
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 10)), LocalDate.of(2023, 4, 17));
	}
}
