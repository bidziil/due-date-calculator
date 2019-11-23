package com.emarsys.so.ho;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.logging.Logger;

public class DueDateCalculator {
	private static final Logger				LOGGER				= Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final EnumSet<DayOfWeek>	WEEKEND_DAYS		= EnumSet.of(SATURDAY, SUNDAY);
	private static final Duration			WORK_HOUR_PER_DAY	= Duration.ofHours(8);
	private static final LocalTime			START_HOUR			= LocalTime.of(9, 00);
	private static final LocalTime			FINISH_HOUR			= START_HOUR.plus(WORK_HOUR_PER_DAY);

	/**
	 * @param submitDateTime
	 * @param turnaroundTime
	 * @return the date/time when the issue is resolved
	 **/
	public LocalDateTime calculateDueDate(final LocalDateTime submitDateTime, Duration turnaroundTime) {
		LOGGER.info(String.format("calculateDueDate start; submitDateTime:%s turnaroundTime:%s", submitDateTime, turnaroundTime));
		
		boolean isReportedDuringWorkingHours = isWorkingHours(submitDateTime);
		if (!isReportedDuringWorkingHours) {
			throw new IllegalArgumentException(String.format("Problem reported out of working hours (%s)", submitDateTime));
		}
		
		if (isNull(turnaroundTime) || Duration.ZERO.equals(turnaroundTime)) {
			return submitDateTime;
		}
		
		LocalDateTime resolvedDateTime = submitDateTime;
		do {
			if (isTheWorkDoneToday(resolvedDateTime, turnaroundTime)) {
				resolvedDateTime = resolvedDateTime.plus(turnaroundTime);
				turnaroundTime = turnaroundTime.minus(turnaroundTime);
			} else {
				resolvedDateTime = nextWorkStart(resolvedDateTime);
				if (turnaroundTime.compareTo(WORK_HOUR_PER_DAY) > 0) {
					turnaroundTime = turnaroundTime.minus(WORK_HOUR_PER_DAY);
				} else {
					Duration overAchieved = Duration.between(resolvedDateTime.toLocalTime(), FINISH_HOUR);
					resolvedDateTime = LocalDateTime.of(resolvedDateTime.toLocalDate(), START_HOUR).minusSeconds(overAchieved.getSeconds());
				}
			}
			LOGGER.fine(String.format("resolvedDateTime:%s turnaroundTime:%s", resolvedDateTime, turnaroundTime));
		} while (!turnaroundTime.isZero() && !turnaroundTime.isNegative());
		
		LOGGER.info(String.format("calculateDueDate finished; resolvedDateTime:%s", resolvedDateTime));
		return resolvedDateTime;
	}

	/**
	 * @param dt Reference datetime
	 * @param turnaroundTime
	 * @return 
	 **/
	protected boolean isTheWorkDoneToday(LocalDateTime dt, Duration turnaroundTime) {
		return dt.get(MINUTE_OF_DAY) + turnaroundTime.toMinutes() <= FINISH_HOUR.get(MINUTE_OF_DAY);
	}

	/**
	 * Returns the next workday from the given date.
	 * @param from Reference date.
	 * @return The next workday as {@code LocalDate}.
	 */
	protected LocalDateTime nextWorkStart(final LocalDateTime from) {
		LocalDateTime next = from;
		do {
			next = next.plusDays(1);
		} while (this.isWeekend(next));
		return next;
	}

	/**
	 * @param dt
	 * @return {@code true} if the LocalDateTime is from 9AM to 5PM on every working day, Monday to Friday.
	 **/
	protected boolean isWorkingHours(final LocalDateTime dt) {
		LOGGER.finer(String.format("isWorkingHours:%s", ofNullable(dt).map(LocalDateTime::getHour).orElse(0)));
		return (nonNull(dt) && !isWeekend(dt))
			&& dt.toLocalTime().isAfter(START_HOUR.minusNanos(1L))
			&& dt.toLocalTime().isBefore(FINISH_HOUR.plusNanos(1L));
	}

	/**
	 * @param dt
	 * @return {@code true} if the LocalDateTime is saturday or sunday.
	 **/
	protected boolean isWeekend(final LocalDateTime dt) {
		return isWeekend(dt.toLocalDate());
	}
	
	protected boolean isWeekend(LocalDate d) {
		DayOfWeek dayOfWeek = ofNullable(d)
								.map(LocalDate::getDayOfWeek)
								.orElse(null);
		LOGGER.finer(String.format("isWeekend:%s", dayOfWeek));
		return WEEKEND_DAYS.contains(dayOfWeek);
	}
}
