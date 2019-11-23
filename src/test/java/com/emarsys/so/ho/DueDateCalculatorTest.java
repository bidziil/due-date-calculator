package com.emarsys.so.ho;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

	public static Collection<Object[]> parameters() {
		Collection<Object[]> parameters = new ArrayList<Object[]>();
		
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 06,  9, 00), null,                 LocalDateTime.of(2019, 06, 06,  9, 00) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 06, 17, 00), null,                 LocalDateTime.of(2019, 06, 06, 17, 00) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 06,  9, 59), null,                 LocalDateTime.of(2019, 06, 06,  9, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 06,  9, 59), Duration.ofHours(0),  LocalDateTime.of(2019, 06, 06,  9, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 06,  9, 59), Duration.ofHours(2),  LocalDateTime.of(2019, 06, 06, 11, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 06,  9, 59), Duration.ofHours(20), LocalDateTime.of(2019, 06, 10, 13, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 07,  9, 59), Duration.ofHours(2),  LocalDateTime.of(2019, 06, 07, 11, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 07,  9, 59), Duration.ofHours(20), LocalDateTime.of(2019, 06, 11, 13, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 06, 16, 59), Duration.ofHours(1),  LocalDateTime.of(2019, 06, 07,  9, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 07, 16, 59), Duration.ofHours(2),  LocalDateTime.of(2019, 06, 10, 10, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 06, 28, 16, 59), Duration.ofHours(1),  LocalDateTime.of(2019, 07, 01,  9, 59) });
		parameters.add(new Object[] { LocalDateTime.of(2019, 12, 31, 16, 59), Duration.ofHours(1),  LocalDateTime.of(2020, 01, 01,  9, 59) });
		
		return parameters;
	}

	@ParameterizedTest
	@MethodSource("parameters")
	void testCalculateDueDate(LocalDateTime submitDateTime, Duration turnaroundTime, LocalDateTime expectedResult) {
		try {
			// Given:
			LOGGER.fine(String.format("testCalculateDueDate start %s %s", submitDateTime, turnaroundTime));
			
			// When:
			LocalDateTime result = dueDateCalculator.calculateDueDate(submitDateTime, turnaroundTime);
			
			// Then:
			assertNotNull(result);
			assertEquals(expectedResult, result);
		} catch (Exception ex) {
			System.err.print(ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}

	public static Collection<Object[]> parameters_WrongWorkingHours() {
		Collection<Object[]> parameters = new ArrayList<Object[]>();
		
		parameters.add(new Object[] { null, null });
		parameters.add(new Object[] { LocalDateTime.of(2019, 05, 05, 8, 59), null });
		parameters.add(new Object[] { LocalDateTime.of(2019, 05, 05, 17, 01), null });
		
		return parameters;
	}
	
	@ParameterizedTest
	@MethodSource("parameters_WrongWorkingHours")
	void testCalculateDueDate_WithWrongWorkingHours(LocalDateTime submitDateTime, Duration turnaroundTime) {
		// Given:
		LOGGER.fine(String.format("testCalculateDueDate_WithWrongWorkingHours start %s %s", submitDateTime, turnaroundTime));
		
		// When:
		Executable executable = () -> dueDateCalculator.calculateDueDate(submitDateTime, turnaroundTime);
		
		// Then:
		assertThrows(IllegalArgumentException.class, executable);
	}

}
