package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YearlyTest {
	private Recurrence rec;

	private LocalDate dueDate;
	private LocalDate nextDueDate;

	private static final LocalDate start = LocalDate.of(1970, 1, 1);

	@BeforeEach
	public void startEach() {
		long days = ChronoUnit.DAYS.between(start, LocalDate.now().plusYears(10));
		LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));

		dueDate = randomDate;
		nextDueDate = dueDate.plusYears(1);
		rec = new Recurrence(FrequencyType.YEARLY, dueDate, null);
	}

	@Test
	public void afterEndDate() {
		rec.updateRecurrence(FrequencyType.YEARLY, dueDate, dueDate.minusDays(1));
		assertEquals(null, rec.getNextDueDate());
	}

	@Test
	public void onEndDate() {
		rec.updateRecurrence(FrequencyType.YEARLY, dueDate, dueDate);
		assertEquals(null, rec.getNextDueDate());
	}

	@Test
	public void normalUsage() {
		assertEquals(dueDate, rec.getDueDate());
		assertEquals(nextDueDate, rec.getNextDueDate());
	}

	@Test
	public void useCurrDate() {
		assertEquals(nextDueDate, rec.getNextDueDate());

		assertEquals(nextDueDate, rec.updateDueDate());
		assertEquals(nextDueDate.plusYears(1), rec.updateDueDate());
		assertEquals(nextDueDate.plusYears(2), rec.updateDueDate());
	}

	// simulate user changing a frequency multiple times
	@Test
	public void changeDateOfYear() {
		assertEquals(nextDueDate, rec.getNextDueDate());

		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
	}

	@Test
	public void everyOtherYears() {
		rec = new Recurrence(FrequencyType.EVERY_OTHER_YEAR, dueDate, null);

		assertEquals(nextDueDate.plusYears(1), rec.updateDueDate());
		assertEquals(nextDueDate.plusYears(3), rec.updateDueDate());
		assertEquals(nextDueDate.plusYears(5), rec.updateDueDate());
		assertEquals(nextDueDate.plusYears(7), rec.updateDueDate());
	}

	@Test
	public void stringConversion() {
		String textToDatabase = rec.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase);
		assertEquals(nextDueDate, r2.getNextDueDate());
		assertEquals(nextDueDate, r2.getNextDueDate());
	}
}
