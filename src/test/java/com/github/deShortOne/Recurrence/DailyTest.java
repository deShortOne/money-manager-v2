package com.github.deShortOne.Recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DailyTest {

	private Reccurence rec;

	private LocalDate currDate = LocalDate.of(2023, 1, 15);
	private LocalDate nextDate = currDate.plusDays(1);

	@BeforeEach
	public void startEach() {
		rec = new Reccurence(Frequency.DAILY);
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
		assertEquals(rec.getNextDate(), null);

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
		assertTrue(rec.isValid());
		Reccurence badWeekly = new Reccurence(Frequency.DAILY);
		assertThrows(NullPointerException.class, () -> badWeekly.setCurrDate());
	}
}
