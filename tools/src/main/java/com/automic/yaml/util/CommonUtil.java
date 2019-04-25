package com.automic.yaml.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.automic.yaml.constants.Constants;
import com.automic.yaml.constants.ExceptionConstants;
import com.automic.yaml.exception.AutomicException;

/**
 * Common Utility class contains basic function(s) required by CA Harvest
 * actions.
 * 
 */
public class CommonUtil {

	private CommonUtil() {
	}

	/**
	 * Method to format error message in the format "ERROR | message"
	 *
	 * @param message
	 * @return formatted message
	 */
	public static final String formatErrorMessage(final String message) {
		final StringBuilder sb = new StringBuilder();
		sb.append("ERROR").append(" | ").append(message);
		return sb.toString();
	}

	/**
	 * Method to check if a String is not empty
	 *
	 * @param field
	 * @return true if String is not empty else false
	 */
	public static final boolean checkNotEmpty(String field) {
		return field != null && !field.isEmpty();
	}

	/**
	 *
	 * Method to parse String containing numeric integer value. If string is not
	 * valid, then it returns the default value as specified.
	 *
	 * @param value
	 * @param defaultValue
	 * @return numeric value
	 */
	public static int parseStringValue(final String value, int defaultValue) {
		int i = defaultValue;
		if (checkNotEmpty(value)) {
			try {
				i = Integer.parseInt(value);
			} catch (final NumberFormatException nfe) {
				i = defaultValue;
			}
		}
		return i;
	}

	/**
	 * Method to convert YES/NO values to boolean true or false
	 * 
	 * @param value
	 * @return true if YES, 1
	 */
	public static final boolean convert2Bool(String value) {
		boolean ret = false;
		if (checkNotEmpty(value)) {
			ret = Constants.YES.equalsIgnoreCase(value) || Constants.TRUE.equalsIgnoreCase(value)
					|| Constants.ONE.equalsIgnoreCase(value);
		}
		return ret;
	}

	/**
	 * Method to read environment value. If not defined then it returns the default
	 * value as specified.
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static final String getEnvParameter(final String paramName, String defaultValue) {
		String val = System.getenv(paramName);
		if (val == null) {
			val = defaultValue;
		}
		return val;
	}

	/**
	 *
	 * Method to read the value as defined in environment. If value is not valid
	 * integer, then it returns the default value as specified.
	 *
	 * @param paramName
	 * @param defaultValue
	 * @return parameter value
	 */
	public static final int getEnvParameter(final String paramName, int defaultValue) {
		String val = System.getenv(paramName);
		int i;
		if (val != null) {
			try {
				i = Integer.parseInt(val);
			} catch (final NumberFormatException nfe) {
				i = defaultValue;
			}
		} else {
			i = defaultValue;
		}
		return i;
	}

	/**
	 * This method check for provide {@code File} file existence
	 * 
	 * @param file
	 *            {@code File} object
	 * @param parameterName
	 *            {@code String} object
	 * @throws AutomicException
	 *             Invalid value for parameter
	 */
	public static void checkFileExists(File file, String parameterName) throws AutomicException {
		if (!(file.exists() && file.isFile())) {
			throw new AutomicException(String.format(ExceptionConstants.INVALID_INPUT_PARAMETER, parameterName, file));
		}
	}

	/**
	 * This method writes content to the provided {@code File}
	 * 
	 * @param file
	 *            {@code File} object
	 * @param parameterName
	 *            {@code String} object
	 * @throws AutomicException
	 *             Invalid value for parameter
	 */
	public static void writeToFile(File filePath, String content, String parameter) throws AutomicException {
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(filePath);
			fWriter.write(content);
		} catch (IOException e) {
			ConsoleWriter.writeln(e);
			throw new AutomicException(String.format(ExceptionConstants.INVALID_INPUT_PARAMETER,parameter, filePath));
		} finally {
			try {
				if (fWriter != null)
					fWriter.close();
			} catch (IOException e) {
				ConsoleWriter.write("Issue in closing the file writer: " + e);
			}
		}
	}

}
