package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DailyTest {

	private Recurrence rec;

	private LocalDate dueDate;
	private LocalDate nextDueDate;

	private static final LocalDate start = LocalDate.of(1970, 1, 1);
	
	@BeforeEach
	public void startup() {
	    long days = ChronoUnit.DAYS.between(start, LocalDate.now().plusYears(10));
	    LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
	    
	    dueDate = randomDate;
		nextDueDate = dueDate.plusDays(1);
		rec = new Recurrence(FrequencyType.DAILY, dueDate, null);
	}

	@Test
	public void currDateAfterEndDate() {
		rec.updateRecurrence(FrequencyType.DAILY, dueDate, dueDate.minusDays(1));
		
		assertEquals(null, rec.getNextDueDate());
	}

	@Test
	public void currDateOnEndDate() {
		rec.updateRecurrence(FrequencyType.DAILY, dueDate, dueDate);
		
		assertEquals(null, rec.getNextDueDate());
	}

	@Test
	public void normalUsage() {
		assertEquals(dueDate, rec.getDueDate());
		assertEquals(nextDueDate, rec.getNextDueDate());
	}

	@Test
	public void multipleUpdateDueDate() {
		assertEquals(nextDueDate, rec.getNextDueDate());

		assertEquals(nextDueDate, rec.updateDueDate());
		assertEquals(nextDueDate.plusDays(1), rec.updateDueDate());
		assertEquals(nextDueDate.plusDays(2), rec.updateDueDate());
		assertEquals(nextDueDate.plusDays(3), rec.updateDueDate());
		assertEquals(nextDueDate.plusDays(4), rec.updateDueDate());
	}

	public void invalidUse() {
		
	}

	@Test
	public void stringConversion() {
		String textToDatabase = rec.convertToString();

		Recurrence rec2 = new Recurrence(textToDatabase);
		assertEquals(nextDueDate, rec2.updateDueDate());
		assertEquals(nextDueDate.plusDays(1), rec2.updateDueDate());
	}
}
