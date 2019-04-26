package com.automic.yaml.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.automic.yaml.constants.ExceptionConstants;
import com.automic.yaml.exception.AutomicException;
import com.automic.yaml.util.YamlUtils.FormatType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.PathNotFoundException;

/***
 * 
 * @author Pawan Kumar(pk029231)
 * 
 *         11-Apr-2019
 */
public class YamlOperations implements Serializable {

	private static final long serialVersionUID = -7938914235799895810L;

	public String formatConvertion(String content, FormatType format, String downloadFilePath)
			throws IOException, AutomicException {
		String convertedString = null;

		if (format == FormatType.JSON) {
			convertedString = convertToJSON(content, downloadFilePath);

		} else if (format == FormatType.YAML) {
			convertedString = convertToYAML(content, downloadFilePath);
		} else {
			throw new AutomicException(ExceptionConstants.INVALID_FORMAT_FOR_CONVERSION);

		}
		return convertedString;
	}

	public String convertToYAML(String content, String downloadFilePath) throws AutomicException {
		String resultedYAMLString = "";

		resultedYAMLString = YamlUtils.writeJSONStringToYaml(content);
		if (!StringUtils.isEmpty(downloadFilePath))
			YamlUtils.writeContentToFile(downloadFilePath, resultedYAMLString);

		return resultedYAMLString;
	}

	public String convertToJSON(String content, String downloadFilePath) throws IOException, AutomicException {
		String resultedJSONString = "";

		Object object = YamlUtils.getJSONObjectMapper().readValue(YamlUtils.writeYAMLStringToJSON(content),
				Object.class);

		if (Objects.nonNull(object) && object instanceof List<?> && ((List<?>) object).size() == 1) {

			resultedJSONString = YamlUtils.getJSONObjectMapper().writeValueAsString(((List<?>) object).get(0));
		} else {
			resultedJSONString = YamlUtils.writeYAMLStringToJSON(content);
		}

		if (!StringUtils.isEmpty(downloadFilePath))
			YamlUtils.writeContentToFile(downloadFilePath, resultedJSONString);

		return resultedJSONString;
	}

	public String read(String content, String path, boolean failOnException) throws AutomicException {

		try {
			return YamlUtils.getValueFromYaml(content, path);
		} catch (AutomicException e) {

			if (failOnException) {

				throw new AutomicException(ExceptionConstants.INVALID_PATH_MSG, e);
			}

		}

		return "";
	}

	public String write(String content, String path, String key, String value, String downloadFilePath)
			throws AutomicException {
		String response = "";
		try {
			response = YamlUtils.addToYaml(content, path, key, value);
			if (!StringUtils.isEmpty(downloadFilePath))
				YamlUtils.writeContentToFile(downloadFilePath, response);
		} catch (JsonPathException e) {
			throw new AutomicException(ExceptionConstants.INVALID_PATH_MSG, e);
		} catch (JsonProcessingException e) {
			throw new AutomicException(ExceptionConstants.INVALID_YAML_FORMAT_MSG, e);
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

	public String remove(String filePath, String yamlString, String path, String downloadFilePath)
			throws AutomicException {

		try {
			if (StringUtils.isEmpty(filePath) && StringUtils.isEmpty(yamlString)) {

				throw new AutomicException("Either YAML file or YAML content is required");
			}
			String input = "";

			if (StringUtils.isNotEmpty(filePath)) {

				input = YamlUtils.readYAMLFromFile(filePath);
			} else {

				input = YamlUtils.readYAMLFromContent(yamlString);
			}

			String updatedYamlString = YamlUtils.deleteFromYaml(input, path);

			if (StringUtils.isNotEmpty(downloadFilePath)) {

				YamlUtils.writeContentToFile(downloadFilePath, updatedYamlString);
			}
			return updatedYamlString;
		} catch (JsonParseException e) {

			throw new AutomicException("Invalid YAML content");
		} catch (PathNotFoundException e) {

			throw new AutomicException(
					"Invalid YAML path, YAML does not contain anything on the path " + path + ", " + e.getMessage());
		} catch (IOException e) {

			throw new AutomicException(e.getMessage());
		}
	}

}
