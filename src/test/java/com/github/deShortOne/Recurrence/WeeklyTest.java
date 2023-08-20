package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeeklyTest {
	private Random random = new Random();

	private Recurrence rec;
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

		rec = new Recurrence(FrequencyType.WEEKLY, this.dayOfWeek, null, null, null);
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
		assertThrows(NullPointerException.class, () -> rec.setCurrDate());
	}

	@Test
	public void everyOtherWeek() {
		rec = new Recurrence(FrequencyType.EVERY_OTHER_WEEK, dayOfWeek, null, null, null);
		rec.setCurrDate(currDate);
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(1));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(3));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(5));
	}

	@Test
	public void everyFourWeeks() {
		rec = new Recurrence(FrequencyType.EVERY_FOUR_WEEKS, dayOfWeek, null, null, null);
		rec.setCurrDate(currDate);
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(3));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(7));
		assertEquals(rec.setCurrDate(), nextDate.plusWeeks(11));
	}

	@Test
	public void stringConversion() {
		Recurrence r = new Recurrence(FrequencyType.WEEKLY, DayOfWeek.of(1), null, null, null);
		String textToDatabase = r.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase);
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 9)), LocalDate.of(2023, 4, 10));
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 10)), LocalDate.of(2023, 4, 17));
	}
}
