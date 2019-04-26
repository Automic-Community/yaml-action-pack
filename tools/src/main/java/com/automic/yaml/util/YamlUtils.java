package com.automic.yaml.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.automic.yaml.constants.ExceptionConstants;
import com.automic.yaml.exception.AutomicException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;

import net.minidev.json.JSONArray;

public class YamlUtils {

	public enum FormatType {

		JSON, YAML
	}

	private YamlUtils() {

	}

	/***
	 * 
	 * @return ObjectMapper for YAML Formatting
	 */
	public static ObjectMapper getYAMLObjectMapper() {

		YAMLMapper mapper = new YAMLMapper(new YAMLFactory());
		mapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true);
		return mapper;
	}

	/***
	 * @return ObjectMapper for JSON Formatting
	 */
	public static ObjectMapper getJSONObjectMapper() {
		return getObjectMapper(new JsonFactory());
	}

	/***
	 * 
	 * @param Jsonfactory
	 * @return Return a new ObjectMapper with the provided JsonFactory
	 */
	private static ObjectMapper getObjectMapper(JsonFactory factory) {

		return new ObjectMapper(factory);
	}

	/***
	 * 
	 * @param jsonString
	 * @return Convert the JSON String to YAML String
	 * @throws AutomicException
	 * @throws IOException
	 */
	public static String writeJSONStringToYaml(String jsonString) throws AutomicException {

		String yamlString = "";
		Object jsonObject = null;
		try {
			jsonObject = getJSONObjectMapper().readValue(jsonString, Object.class);
		} catch (JsonParseException e) {
			throw new AutomicException("Unable to parse given YAML content", e);
		} catch (JsonMappingException e) {
			throw new AutomicException("Unable to map given YAML content, this YAML is invalid", e);
		} catch (IOException e) {
			throw new AutomicException(e.getMessage(), e);
		}

		if (Objects.isNull(jsonObject)) {

			return yamlString;
		}
		if (jsonObject instanceof List<?>) {

			for (Object item : (List<?>) jsonObject) {

				try {
					yamlString += getYAMLObjectMapper().writeValueAsString(item);
				} catch (JsonProcessingException e) {
					throw new AutomicException("Unable to process given YAML content", e);
				}
			}
			return yamlString;
		}
		try {
			return getYAMLObjectMapper().writeValueAsString(jsonObject);
		} catch (JsonProcessingException e) {
			throw new AutomicException("Unable to process given YAML content", e);
		}
	}

	/***
	 * 
	 * @param yamlString
	 * @return Convert YAML String to JSON String
	 * @throws IOException
	 */
	public static String writeYAMLStringToJSON(String yamlString) throws AutomicException {
		String jsonString = null;
		try {
			jsonString = getJSONObjectMapper()
					.writeValueAsString(getYAMLObjectMapper().readValue(yamlString, Object.class));
		} catch (JsonParseException e) {
			throw new AutomicException("Unable to parse given YAML content", e);
		} catch (JsonMappingException e) {
			throw new AutomicException("Unable to map given YAML content, this YAML is invalid", e);
		} catch (JsonProcessingException e) {
			throw new AutomicException("Unable to process given YAML content", e);
		} catch (IOException e) {
			throw new AutomicException(e.getMessage(), e);
		}
		return jsonString;
	}

	/***
	 * Write YAML to the file Note: If file already exist on the given path, will be
	 * override
	 * 
	 * @param filePath
	 * @param yamlString
	 * @throws IOException
	 * @throws AutomicException
	 */

	public static void writeContentToFile(String filePath, String yamlString) throws AutomicException {

		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8)) {

			bufferedWriter.write(yamlString);
		} catch (IOException exception) {

			throw new AutomicException(String.format(ExceptionConstants.UNABLE_TO_WRITE, filePath), exception);
		}
	}

	/***
	 * 
	 * @param filePath
	 * @return YAML Sting
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static String readYAMLFromFile(String filePath) throws IOException {

		ObjectMapper mapper = getYAMLObjectMapper();
		return mapper.writeValueAsString(
				mapper.readValues(new YAMLFactory().createParser(new File(filePath)), new TypeReference<Object>() {
				}).readAll());

	}

	public static String readYAMLFromContent(String content) throws IOException {

		ObjectMapper mapper = getYAMLObjectMapper();

		return mapper.writeValueAsString(
				mapper.readValues(new YAMLFactory().createParser(content), new TypeReference<Object>() {
				}).readAll());

	}

	public static String readJSONFromFile(String filePath) throws IOException {

		ObjectMapper mapper = getJSONObjectMapper();
		String jsonString;

		jsonString = mapper.writeValueAsString(mapper.readValue(new File(filePath), Object.class));
		return jsonString;

	}

	public static String readJSONFromContent(String content) throws IOException {

		ObjectMapper mapper = getJSONObjectMapper();
		String jsonString;

		jsonString = mapper.writeValueAsString(mapper.readValue(content, Object.class));

		return jsonString;

	}

	public static String convertObjectToYaml(Object object) throws JsonProcessingException {

		if (object instanceof String) {

			return (String) object;
		}
		return getYAMLObjectMapper().writeValueAsString(object);
	}

	public static <T> T convertYamlToObject(String yaml, Class<T> type) throws IOException {

		return getYAMLObjectMapper().readValue(yaml, type);
	}

	/***
	 * 
	 * @param filePath
	 * @return DocumentContext
	 * @throws IOException
	 * @throws AutomicException
	 */
	public static DocumentContext getDocumentContext(File filePath) throws IOException, AutomicException {

		return getDocumentContext(writeYAMLStringToJSON(readYAMLFromFile(filePath.getAbsolutePath())));
	}

	/***
	 * 
	 * @param jsonString
	 * @return DocumentContext
	 */
	public static DocumentContext getDocumentContext(String jsonString) {

		DocumentContext context = JsonPath.parse(jsonString,
				Configuration.builder().options(Option.REQUIRE_PROPERTIES).build());

		if (Objects.isNull(context)) {

			throw new RuntimeException("Invalid JSON Content");
		}
		JSONArray array = (JSONArray) context.read("$");

		if (array.size() == 1) {

			return JsonPath.parse(context.read("$.[0]"),
					Configuration.builder().options(Option.REQUIRE_PROPERTIES).build());
		}
		return context;
	}

	public static DocumentContext getDocumentContextForYaml(String yamlString) throws AutomicException {

		return getDocumentContext(writeYAMLStringToJSON(yamlString));
	}

	/***
	 * Process YAML String content and return value on provided path
	 * 
	 * @param context
	 * @param path
	 * @return Return formated YAML {String}
	 * @throws AutomicException
	 */
	public static String getValueFromYaml(String yamlString, String path) throws AutomicException {

		DocumentContext context = getDocumentContext(writeYAMLStringToJSON(yamlString));

		if (Objects.isNull(context)) {

			throw new AutomicException("Invalid Yaml file");
		}
		try {

			Object result = context.read(path);

			return (result instanceof String) ? String.valueOf(result)
					: getYAMLObjectMapper().writeValueAsString(result);
		} catch (JsonProcessingException e) {
			throw new AutomicException("Unable to get value for provided path", e);
		} catch (PathNotFoundException e) {

			throw new AutomicException("Invalid YAML path " + path, e);
		}
	}

	/***
	 * 
	 * @param yamlString
	 * @param path
	 * @param value
	 * @param filter
	 * @return Updated YAML String
	 * @throws IOException
	 * @throws YamlException
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static String addToYaml(String yamlString, String path, String key, Object value)
			throws AutomicException, IOException {

		return writeJSONStringToYaml(getDocumentContext(writeYAMLStringToJSON(yamlString))
				.put(path, key, convertYamlToObject(convertObjectToYaml(value), Object.class)).jsonString());
	}

	/***
	 * 
	 * @param yamlString
	 * @param path
	 * @param value
	 * @param filterCondition
	 * @return Updated YAML String
	 * @throws IOException
	 * @throws AutomicException
	 */
	public static String updateYaml(String yamlString, String path, Object value, boolean isArray)
			throws IOException, AutomicException {

		if (isArray) {

			return writeJSONStringToYaml(getDocumentContextForYaml(yamlString)
					.add(path, convertYamlToObject(convertObjectToYaml(value), Object.class)).jsonString());
		}

		return writeJSONStringToYaml(getDocumentContextForYaml(yamlString)
				.set(path, convertYamlToObject(convertObjectToYaml(value), Object.class)).jsonString());

	}

	public static String deleteFromYaml(String yamlString, String path) throws AutomicException {

		return writeJSONStringToYaml(getDocumentContextForYaml(yamlString).delete(path).jsonString());
	}

	public static boolean isFileExist(String filePath) throws AutomicException {

		if (StringUtils.isEmpty(filePath))
			return false;

		File file = new File(filePath);
		if (!file.exists())
			throw new AutomicException("Given file path is invalid");
		return true;
	}

}
