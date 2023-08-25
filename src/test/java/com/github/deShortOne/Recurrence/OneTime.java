package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OneTime {

	private Recurrence rec;
	
	private LocalDate dueDate;

	private static final LocalDate start = LocalDate.of(1970, 1, 1);

	@BeforeEach
	public void startEach() {
	    long days = ChronoUnit.DAYS.between(start, LocalDate.now().plusYears(10));
	    LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
	    
	    dueDate = randomDate;
		rec = new Recurrence(FrequencyType.ONCE, dueDate, null);
	}
	
	@Test
	public void currDateAfterEndDate() {
		assertEquals(null, rec.getNextDueDate());
	}
	
	@Test
	public void normalusage() {
		assertEquals(dueDate, rec.getDueDate());
	}

	@Test
	public void stringConversion() {
		String textToDatabase = rec.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase);
		assertEquals(null, r2.getNextDueDate());
		assertEquals(null, r2.getNextDueDate());
	}
}
