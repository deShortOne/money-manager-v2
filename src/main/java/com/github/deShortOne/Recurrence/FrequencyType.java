package com.github.deShortOne.Recurrence;

import com.github.deShortOne.DataObjects.BaseDataValue;

public enum FrequencyType
		implements
		BaseDataValue {
	ONCE("Only Once", Frequency.ONE_TIME, 0, 0),
	DAILY("Daily", Frequency.DAILY, 0, 1),
	WEEKLY("Weekly", Frequency.WEEKLY, 0, 2),
	EVERY_OTHER_WEEK("Every other week", Frequency.WEEKLY, 1, 3),
	EVERY_FOUR_WEEKS("Every four weeks", Frequency.WEEKLY, 3, 4),
	MONTHLY("Monthly", Frequency.MONTHLY, 0, 5),
	EVERY_OTHER_MONTH("Every other month", Frequency.MONTHLY, 1, 6),
	EVERY_THREE_MONTHS("Every three months", Frequency.MONTHLY, 2, 7),
	EVERY_FOUR_MONTHS("Every four months", Frequency.MONTHLY, 3, 8),
	YEARLY("Yearly", Frequency.YEARLY, 0, 9),
	EVERY_OTHER_YEAR("Every other year", Frequency.YEARLY, 1, 10);

	private final String name;
	private final Frequency frequency;
	private final int skips;

	// don't use ordinal because I want to keep order of enums and new/ more enums
	// should be
	// placed in the correct order.
	private final int id;

	FrequencyType(String name, Frequency frequency, int skips, int id) {
		this.name = name;
		this.frequency = frequency;
		this.skips = skips;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Frequency getFrequency() {
		return frequency;
	}

	public int getSkips() {
		return skips;
	}

	public int getID() {
		return id;
	}

	public static FrequencyType of(int pos) {
		return FrequencyType.values()[pos];
	}

	@Override
	public String getTableCellValue() {
		return getName();
	}
}
