package com.automic.yaml.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.automic.yaml.constants.ExceptionConstants;
import com.automic.yaml.exception.AutomicException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.PathNotFoundException;

public class YamlOperations implements Serializable {

	private static final long serialVersionUID = -7938914235799895810L;

	public String formatConvertion(String content, String format) throws IOException, AutomicException {
		String convertedString = null;

		if (format.equals("JSON")) {
			convertedString = convertToJSON(content);

		} else if (format.equals("YAML")) {
			convertedString = convertToYAML(content);
		} else {
			throw new AutomicException(ExceptionConstants.INVALID_FORMAT_FOR_CONVERSION);

		}
		return convertedString;
	}

	public String convertToYAML(String content) throws AutomicException {
		String resultedYAMLString = "";

		resultedYAMLString = YamlUtils.writeJSONStringToYaml(content);
		return resultedYAMLString;
	}

	public String convertToJSON(String content) throws IOException, AutomicException {
		String resultedJSONString = "";

		Object object = YamlUtils.getJSONObjectMapper().readValue(YamlUtils.writeYAMLStringToJSON(content),
				Object.class);

		if (Objects.nonNull(object) && object instanceof List<?> && ((List<?>) object).size() == 1) {

			resultedJSONString = YamlUtils.getJSONObjectMapper().writeValueAsString(((List<?>) object).get(0));
		} else {
			resultedJSONString = YamlUtils.writeYAMLStringToJSON(content);
		}

		return resultedJSONString;
	}

	public String read(String content, String path, boolean failOnException)
			throws AutomicException, PathNotFoundException {
		return YamlUtils.getValueFromYaml(content, path);
	}

	public String write(String content, String position, String key, String value) throws AutomicException {
		String response = "";
		try {
			response = YamlUtils.addToYaml(content, position, key, value);
		} catch (JsonPathException e) {
			throw new AutomicException(ExceptionConstants.INVALID_PATH_MSG, e);
		} catch (JsonProcessingException e) {
			throw new AutomicException(ExceptionConstants.INVALID_YAML_FORMAT_MSG, e);
		} catch (IOException e) {
			throw new AutomicException(e.getMessage(), e);
		} catch (Exception e) {
			throw new AutomicException(e.getMessage(), e);
		}
		return response;
	}
	public String update(String content, String path, String value, boolean isArray) throws AutomicException {

		try {
			return YamlUtils.updateYaml(content, path, value, isArray);
		} catch (JsonPathException exception) {

			throw new AutomicException(ExceptionConstants.INVALID_PATH_MSG, exception);
		} catch (IOException e) {
			throw new AutomicException(ExceptionConstants.INVALID_YAML_FORMAT_MSG, e);

		} catch (Exception e) {
			throw new AutomicException(ExceptionConstants.UNABLE_UPDATE_CONTENT_MSG, e);
		}
	}

	public String remove(String content, String path, boolean failOnException) throws AutomicException {
		try {
			return YamlUtils.deleteFromYaml(content, path);
		} catch (PathNotFoundException e) {
			if (failOnException)
				throw new AutomicException("Invalid YAML path, YAML does not contain anything on the path " + path
						+ ", " + e.getMessage());
			else {
				ConsoleWriter.writeln("YAML Path does not exist.");
				return null;
			}
		}

	}
}