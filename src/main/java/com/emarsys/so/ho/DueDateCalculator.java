package com.emarsys.so.ho;

import static java.util.Objects.*;
import static java.util.Optional.ofNullable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.logging.Logger;

public class DueDateCalculator {
	private static final Logger	LOGGER	= Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final EnumSet<DayOfWeek> WEEKEND_DAYS = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

	/**
	 * Working hours are from 9AM to 5PM on every working day, Monday to Friday.
	 * • Holidays should be ignored (e.g. A holiday on a Thursday is considered as a
	 * working day. A working Saturday counts as a non-working day.).
	 * • The turnaround time is defined in working hours (e.g. 2 days equal 16 hours).
	 * If a problem was reported at 2:12PM on Tuesday and the turnaround time is
	 * 16 hours, then it is due by 2:12PM on Thursday.
	 * • A problem can only be reported during working hours. (e.g. All submit date
	 * values are set between 9AM to 5PM.)
	 * • Do not use any third-party libraries for date/time calculations (e.g. Moment.js,
	 * Carbon, Joda, etc.) or hidden functionalities of the built-in methods
	 * 
	 * @param submitDateTime
	 * @param turnaroundTime
	 * @return the date/time when the issue is resolved
	 **/
	public LocalDateTime calculateDueDate(final LocalDateTime submitDateTime, final Duration turnaroundTime) {
		LOGGER.info(String.format("calculateDueDate start submitDateTime:%s turnaroundTime:%s", submitDateTime, turnaroundTime));
		
		boolean isReportedDuringWorkingHours = isWorkingHours(submitDateTime);
		LOGGER.finer(String.format("isReportedDuringWorkingHours:%s", isReportedDuringWorkingHours));
		if (!isReportedDuringWorkingHours) {
			throw new IllegalArgumentException(String.format("Problem reported out of working hours (%s)", submitDateTime));
		}
		
		if (isNull(turnaroundTime) || Duration.ZERO.equals(turnaroundTime)) {
			return submitDateTime;
		}
		
		
		return submitDateTime;
	}

	protected boolean isWorkingHours(LocalDateTime dt) {
		int hourOp = ofNullable(dt)
						.map(LocalDateTime::getHour)
						.orElse(0);
		LOGGER.finer(String.format("isWorkingHours:%s", hourOp));
		return (nonNull(dt) && !isWeekend(dt)) && (hourOp >= 9 && hourOp < 17);
	}

	protected boolean isWeekend(LocalDateTime dt) {
		DayOfWeek dayOfWeek = ofNullable(dt)
								.map(LocalDateTime::getDayOfWeek)
								.orElse(null);
		LOGGER.finer(String.format("isWeekend:%s", dayOfWeek));
		return WEEKEND_DAYS.contains(dayOfWeek);
	}
}
