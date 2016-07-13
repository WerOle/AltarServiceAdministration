package asa.address.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Write in a txt-File what happend with the program
 * 
 * @author Ole Werger
 */
public class Logger {
	public static void logAdd(String s) {
		WriteLogger wL = new WriteLogger();
		String logger = wL.readTextFile("resources/logger.txt", "iso-8859-15");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss ");
		Date currentTime = new Date();
		String lineSeparator = System.getProperty("line.separator");
		String s2 = formatter.format(currentTime) + " " + s + lineSeparator;

		logger = logger + s2;
		wL.writeTextFile(logger, "resources/logger.txt", "iso-8859-15");

	}

}
