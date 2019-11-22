package com.emarsys.so.ho;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.logging.Logger;

public class DueDateCalculator {
	private static final Logger	LOGGER	= Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final EnumSet<DayOfWeek> WEEKEND_DAYS = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

	/**
	 * @param submitDateTime
	 * @param turnaroundTime
	 * @return the date/time when the issue is resolved
	 **/
	public LocalDateTime calculateDueDate(final LocalDateTime submitDateTime, final Duration turnaroundTime) {
		LOGGER.info(String.format("calculateDueDate start submitDateTime:%s turnaroundTime:%s", submitDateTime, turnaroundTime));
		return submitDateTime;
	}
}
