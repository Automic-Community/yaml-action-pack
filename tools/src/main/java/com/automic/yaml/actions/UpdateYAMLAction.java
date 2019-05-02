package com.automic.yaml.actions;

import java.io.File;
import java.io.IOException;

import com.automic.yaml.base.AbstractYAMLAction;
import com.automic.yaml.constants.Constants;
import com.automic.yaml.constants.ExceptionConstants;
import com.automic.yaml.exception.AutomicException;
import com.automic.yaml.util.CommonUtil;
import com.automic.yaml.util.ConsoleWriter;
import com.automic.yaml.util.YamlOperations;
import com.automic.yaml.util.YamlUtils;

/**
 * 
 * @author vijendraparmar
 *
 */
public class UpdateYAMLAction extends AbstractYAMLAction {

	private String yamlElementPath;
	private boolean isArray = false;
	private String value;
	private String yamlDownloadPath;

	private YamlOperations yamlOperations = new YamlOperations();

	/**
	 * Initializes a newly created {@code UpdateYAMLAction}
	 */
	public UpdateYAMLAction() {
		addOption(Constants.YAML_ELEMENT_PATH, true, "Yaml Element Path");
		addOption(Constants.IS_ARRAY, true, "Is Array");
		addOption(Constants.VALUE, true, "Value");
		addOption(Constants.YAML_DOWNLOAD_PATH, false, "Yaml Download Path");

	}

	@Override
	protected void executeSpecific() throws AutomicException {
		prepareAndValidateInputs();
		try {

			String output = yamlOperations.update(content, yamlElementPath, value, isArray);

			if (CommonUtil.checkNotEmpty(yamlDownloadPath)) {
				YamlUtils.writeContentToFile(yamlDownloadPath, output);
			}

			ConsoleWriter.writeln("UPDATED_YAML::==========\n" + output);
			ConsoleWriter.writeln("========================");
		} catch (Exception exception) {
			ConsoleWriter.writeln(exception);
			throw new AutomicException("YAML could not be updated successfully.");
		}
	}

	private void prepareAndValidateInputs() throws AutomicException {
		yamlElementPath = getOptionValue(Constants.YAML_ELEMENT_PATH);

		if (!CommonUtil.checkNotEmpty(yamlElementPath)) {
			throw new AutomicException(ExceptionConstants.UPDATE_YAML_PATH_EMPTY_MSG);
		}

		isArray = CommonUtil.convert2Bool(getOptionValue(Constants.IS_ARRAY));

		String temp = getOptionValue(Constants.VALUE);
		if (!CommonUtil.checkNotEmpty(temp)) {
			throw new AutomicException(ExceptionConstants.VALUE_CANNOT_BE_EMPTY);
		}
		File vFile = new File(temp);
		CommonUtil.checkFileExists(vFile, "Value");
		if (vFile.length() == 0) {
			throw new AutomicException(ExceptionConstants.VALUE_CANNOT_BE_EMPTY);
		}

		value = CommonUtil.readFromFile(temp, "Value");

		yamlDownloadPath = getOptionValue(Constants.YAML_DOWNLOAD_PATH);
		if (CommonUtil.checkNotEmpty(yamlDownloadPath)) {
			try {

				File file = new File(yamlDownloadPath);
				file.createNewFile();
			} catch (IOException e) {
				ConsoleWriter.writeln(e);
				throw new AutomicException(String.format(ExceptionConstants.UNABLE_TO_CREATE_FILE, yamlDownloadPath));
			}
		}
	}
}
