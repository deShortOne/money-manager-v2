package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class OneTime {

	private LocalDate currDate = LocalDate.of(2023, 1, 15);
	private LocalDate startDate = LocalDate.of(2023, 1, 20);

	private Recurrence rec = new Recurrence(FrequencyType.ONCE, null, startDate, null, null);

	@Test
	public void currDateBeforeDate() {
		assertEquals(rec.getNextDate(currDate), startDate);
	}

	@Test
	public void currDateOnDate() {
		assertEquals(rec.getNextDate(startDate), null);
	}

	@Test
	public void currDateAfterDate() {
		assertEquals(rec.getNextDate(startDate.plusDays(1)), null);
	}
	
	@Test
	public void stringConversion() {
		Recurrence r = new Recurrence(FrequencyType.ONCE, null, startDate, null, null);
		String textToDatabase = r.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase, null, null);
		assertEquals(r2.getNextDate(LocalDate.of(2023, 1, 15)), LocalDate.of(2023, 1, 20));
		assertEquals(r2.getNextDate(LocalDate.of(2023, 1, 20)), null);
	}
}
