package com.emarsys.so.ho;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class LoggerInitializer {

	public static void initRootLogger() {
		Logger globalLogger = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
		globalLogger.setLevel(Level.INFO);
		globalLogger.setUseParentHandlers(false);
		globalLogger.addHandler(new StreamHandler(System.out, new SimpleFormatter()) {
			@Override
			public synchronized void publish(final LogRecord record) {
				super.publish(record);
				super.flush();
			}
		});
	}

}
