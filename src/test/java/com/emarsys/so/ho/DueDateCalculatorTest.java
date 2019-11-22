package com.emarsys.so.ho;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DueDateCalculatorTest {
	private static final Logger	LOGGER	= Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	static DueDateCalculator dueDateCalculator;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		LoggerInitializer.initRootLogger();
		dueDateCalculator = new DueDateCalculator();
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testCalculateDueDate() {
		// Given:
		LocalDateTime submitDateTime = LocalDateTime.now();
		Duration turnaroundTime = Duration.ofHours(10);
		LOGGER.fine("testCalculateDueDate start");
		
		// When:
		LocalDateTime result = dueDateCalculator.calculateDueDate(submitDateTime, turnaroundTime);
		
		// Then:
		assertNotNull(result);
	}

}
