package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DailyTest {

	private Recurrence rec;

	private LocalDate currDate = LocalDate.of(2023, 1, 15);
	private LocalDate nextDate = currDate.plusDays(1);

	@BeforeEach
	public void startEach() {
		rec = new Recurrence(FrequencyType.DAILY, null, null, null, null);
	}

	@Test
	public void currDateBeforeStartDate() {
		LocalDate startDate = LocalDate.of(2023, 1, 20);
		assertTrue(startDate.isAfter(currDate));

		rec.setStartDate(startDate);
		assertEquals(rec.getNextDate(currDate), startDate);
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
		assertEquals(rec.setCurrDate(), nextDate.plusDays(1));
		assertEquals(rec.setCurrDate(), nextDate.plusDays(2));
		assertEquals(rec.setCurrDate(), nextDate.plusDays(3));
		assertEquals(rec.setCurrDate(), nextDate.plusDays(4));
	}

	@Test
	public void invalidUse() {
		assertThrows(NullPointerException.class, () -> rec.setCurrDate());
	}

	@Test
	public void stringConversion() {
		Recurrence r = new Recurrence(FrequencyType.DAILY, null, null, null, null);
		String textToDatabase = r.convertToString();

		Recurrence r2 = new Recurrence(textToDatabase);
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 10)), LocalDate.of(2023, 4, 11));
		assertEquals(r2.getNextDate(LocalDate.of(2023, 4, 15)), LocalDate.of(2023, 4, 16));
	}
}
