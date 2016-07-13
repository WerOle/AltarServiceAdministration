package asa.address.logger;

import java.io.*;

/**
 * Connection between Logger.java and logger.txt
 * 
 * @author Ole Werger
 */
public class WriteLogger {

	/**
	 *
	 * @param fileName
	 *            name of file
	 * @param charSet
	 *            character setting for en-/coding
	 * @return String that is already in file
	 */
	public String readTextFile(String fileName, String charSet) {
		String result = null;
		try {
			InputStreamReader reader;
			if (charSet != null)
				reader = new InputStreamReader(new FileInputStream(fileName), charSet);
			else
				reader = new InputStreamReader(new FileInputStream(fileName));
			BufferedReader in = new BufferedReader(reader);
			StringBuilder buffer = new StringBuilder();
			int c;
			while ((c = in.read()) >= 0) {
				buffer.append((char) c);
			}
			in.close();
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/**
	 *
	 * @param data
	 *            new input text as a string
	 * @param fileName
	 *            name of file
	 * @param charSet
	 *            character settings for en-/coding
	 * @return success
	 */
	public boolean writeTextFile(String data, String fileName, String charSet) {
		boolean result = true;
		try {
			OutputStreamWriter writer;
			if (charSet != null)
				writer = new OutputStreamWriter(new FileOutputStream(fileName), charSet);
			else
				writer = new OutputStreamWriter(new FileOutputStream(fileName));
			BufferedWriter out = new BufferedWriter(writer);
			out.write(data, 0, data.length());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
}
