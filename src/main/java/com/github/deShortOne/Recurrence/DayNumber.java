package com.github.deShortOne.Recurrence;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;

import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;

public enum DayNumber
		implements
		TemporalAccessor {
	FIRST,
	SECOND,
	THIRD,
	FOURTH,
	FIFTH,
	SIXTH,
	SEVENTH,
	EIGHTH,
	NINTH,
	TENTH,
	ELEVENTH,
	TWELFTH,
	THIRTEENTH,
	FOURTEENTH,
	FIFTEENTH,
	SIXTEENTH,
	SEVENTEENTH,
	EIGHTEENTH,
	NINETEENTH,
	TWENTIETH,
	TWENTY_FIRST,
	TWENTY_SECOND,
	TWENTY_THIRD,
	TWENTY_FOURTH,
	TWENTY_FIFTH,
	TWENTY_SIXTH,
	TWENTY_SEVENTH,
	TWENTY_EIGHTH,
	TWENTY_NINTH,
	THIRTIETH,
	THIRTY_FIRST;

	private static final DayNumber[] ENUMS = DayNumber.values();

	public static DayNumber of(int dayNumber) {
		if (dayNumber < 1 || dayNumber > 31) {
			throw new DateTimeException("Invalid value for DayNumber: " + dayNumber);
		}
		return ENUMS[dayNumber - 1];
	}

	public int getValue() {
		return ordinal() + 1;
	}

	public static DayNumber getDayNumber(int i) {
		return DayNumber.values()[i - 1];
	}

	@Override
	public boolean isSupported(TemporalField field) {
		if (field instanceof ChronoField) {
			return field == DAY_OF_MONTH;
		}
		return field != null && field.isSupportedBy(this);
	}

	@Override
	public long getLong(TemporalField field) {
		if (field == DAY_OF_MONTH) {
			return getValue();
		} else if (field instanceof ChronoField) {
			throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
		}
		return field.getFrom(this);
	}

}
