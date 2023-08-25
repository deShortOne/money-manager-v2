package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeeklyTest {

	private Recurrence rec;

	private LocalDate dueDate;
	private LocalDate nextDueDate;

	private static final LocalDate start = LocalDate.of(1970, 1, 1);

	@BeforeEach
	public void startEach() {
		long days = ChronoUnit.DAYS.between(start, LocalDate.now().plusYears(10));
		LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));

		dueDate = randomDate;
		nextDueDate = dueDate.plusWeeks(1);
		rec = new Recurrence(FrequencyType.WEEKLY, dueDate, null);
	}

	@Test
	public void currDateAfterEndDate() {
		rec.updateRecurrence(FrequencyType.WEEKLY, dueDate, dueDate.minusDays(1));

		assertEquals(null, rec.getNextDueDate());
	}

	@Test
	public void currDateOnEndDate() {
		rec.updateRecurrence(FrequencyType.WEEKLY, dueDate, dueDate);
		assertEquals(null, rec.getNextDueDate());
	}

	@Test
	public void normalUsage() {
		assertEquals(dueDate, rec.getDueDate());
		assertEquals(nextDueDate, rec.getNextDueDate());
	}

	@Test
	public void useCurrDate() {
		assertEquals(rec.getNextDueDate(), nextDueDate);

		assertEquals(rec.updateDueDate(), nextDueDate);
		assertEquals(rec.updateDueDate(), nextDueDate.plusWeeks(1));
		assertEquals(rec.updateDueDate(), nextDueDate.plusWeeks(2));
	}

	// simulate user changing a frequency multiple times
	@Test
	public void changeWeek() {
		assertEquals(nextDueDate, rec.getNextDueDate());

		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
	}

	@Test
	public void everyOtherWeek() {
		rec = new Recurrence(FrequencyType.EVERY_OTHER_WEEK, dueDate, null);

		assertEquals(rec.updateDueDate(), nextDueDate.plusWeeks(1));
		assertEquals(rec.updateDueDate(), nextDueDate.plusWeeks(3));
		assertEquals(rec.updateDueDate(), nextDueDate.plusWeeks(5));
	}

	@Test
	public void everyFourWeeks() {
		rec = new Recurrence(FrequencyType.EVERY_FOUR_WEEKS, dueDate, null);

		assertEquals(rec.updateDueDate(), nextDueDate.plusWeeks(3));
		assertEquals(rec.updateDueDate(), nextDueDate.plusWeeks(7));
		assertEquals(rec.updateDueDate(), nextDueDate.plusWeeks(11));
	}

	@Test
	public void stringConversion() {
		String textToDatabase = rec.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase);
		assertEquals(nextDueDate, r2.getNextDueDate());
		assertEquals(nextDueDate, r2.getNextDueDate());
	}
}
