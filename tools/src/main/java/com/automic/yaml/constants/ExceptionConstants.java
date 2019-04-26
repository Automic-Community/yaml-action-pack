package com.automic.yaml.constants;

/**
 * Exception constants used in the application
 * 
 * @author shrutinambiar
 *
 */
public class ExceptionConstants {

	public static final String UNABLE_TO_FLUSH_STREAM = "Error while flushing stream";
	public static final String INVALID_ARGS = "Improper Args. Possible cause : %s";

	public static final String INVALID_INPUT_PARAMETER = "Invalid value for parameter [%s] : [%s]";

	public static final String INVALID_FILE = "Invalid file [%s], possibly file doesn't exists";
	public static final String GENERIC_ERROR_MSG = "System Error occured.";

	public static final String UNABLE_TO_WRITE = "Unable to write at the given file path [%s]";
	public static final String INVALID_PATH_MSG = "Invalid Path for YAML";
	public static final String INVALID_YAML_FORMAT_MSG = "Invalid YAML Format.\n";
	public static final String UNABLE_UPDATE_CONTENT_MSG = "Unable to update YAML Content.\n";
	public static final String INVALID_FORMAT_FOR_CONVERSION = "Invalid Format for Conversion, either provide JSON or YAML";
	public static final String YAML_PATH_EMPTY_MSG = "Path to YAML element is empty.";
	public static final String UNABLE_TO_CREATE_FILE = "Error in creating file at Download Path : [%s].";
	
	private ExceptionConstants() {
	}
}
