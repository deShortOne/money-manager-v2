package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonthlyTest {
	private Recurrence rec;

	private LocalDate dueDate;
	private LocalDate nextDueDate;
	
	private static final LocalDate start = LocalDate.of(1970, 1, 1);

	@BeforeEach
	public void startEach() {
	    long days = ChronoUnit.DAYS.between(start, LocalDate.now().plusYears(10));
	    LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
	    
	    dueDate = randomDate;
		nextDueDate = dueDate.plusMonths(1);
		rec = new Recurrence(FrequencyType.MONTHLY, dueDate, null);
	}

	@Test
	public void currDateAfterEndDate() {
		rec.updateRecurrence(FrequencyType.MONTHLY, dueDate, dueDate.minusDays(1));
		assertEquals(null, rec.getNextDueDate());
	}

	@Test
	public void currDateOnEndDate() {
		rec.updateRecurrence(FrequencyType.MONTHLY, dueDate, dueDate);
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
		assertEquals(nextDueDate.plusMonths(1), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(2), rec.updateDueDate());
	}

	// simulate user changing a frequency multiple times
	@Test
	public void changeMonth() {
		assertEquals(nextDueDate, rec.getNextDueDate());

		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
		startEach();
		assertEquals(nextDueDate, rec.getNextDueDate());
	}

	@Test
	public void everyOtherMonths() {
		rec.updateRecurrence(FrequencyType.EVERY_OTHER_MONTH, dueDate, null);

		assertEquals(nextDueDate.plusMonths(1), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(3), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(5), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(7), rec.updateDueDate());
	}

	@Test
	public void everyThreeMonths() {
		rec.updateRecurrence(FrequencyType.EVERY_THREE_MONTHS, dueDate, null);

		assertEquals(nextDueDate.plusMonths(2), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(5), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(8), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(11), rec.updateDueDate());
	}

	@Test
	public void everyFourMonths() {
		rec.updateRecurrence(FrequencyType.EVERY_FOUR_MONTHS, dueDate, null);

		assertEquals(nextDueDate.plusMonths(3), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(7), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(11), rec.updateDueDate());
		assertEquals(nextDueDate.plusMonths(15), rec.updateDueDate());
	}

	@Test
	public void stringConversion() {
		String textToDatabase = rec.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase);
		assertEquals(nextDueDate, r2.updateDueDate());
		assertEquals(nextDueDate.plusMonths(1), r2.updateDueDate());
	}
}
