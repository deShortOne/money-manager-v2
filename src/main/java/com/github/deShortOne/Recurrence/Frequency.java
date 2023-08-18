package com.github.deShortOne.Recurrence;

// listed below are possible ideas, I'm going for the simpler idea as complex is
// not needed
public enum Frequency {
	// all can have end dates, all can have how many x in between each

	// must have "end date"
	ONE_TIME,

	// can need how many days gap in between
	DAILY,

	// day of week, how many weeks between
	WEEKLY,

	// day of month, how many months between,
	// "2nd tuesday every month", "last day of month"
	MONTHLY,

	// day of year
	YEARLY
}
