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

public class InsertYAMLAction extends AbstractYAMLAction {

	private String yamlTargetPosition;
	private String key;
	private String value;
	private String yamlDownloadPath;
	private String updatedYAML;

	private YamlOperations yamlOperations = new YamlOperations();

	public InsertYAMLAction() {
		addOption(Constants.YAML_TARGET_POSITION, true, "Yaml Target Position");
		addOption(Constants.KEY, true, "Key");
		addOption(Constants.VALUE, true, "Value");
		addOption(Constants.YAML_DOWNLOAD_PATH, false, "Yaml Download Path");
	}

	@Override
	protected void executeSpecific() throws AutomicException {
		prepareAndValidateInputs();
		try {
			updatedYAML = yamlOperations.write(content, yamlTargetPosition, key, value);
			if (CommonUtil.checkNotEmpty(yamlDownloadPath)) {
				YamlUtils.writeContentToFile(yamlDownloadPath, updatedYAML);
			}
			ConsoleWriter.writeln(
					Constants.DEFAULT_VARIABLE + "::=\n" + updatedYAML);
			ConsoleWriter.writeln("========================");
		} catch (Exception exception) {
			ConsoleWriter.writeln(exception);
			throw new AutomicException("Exception occurred while inserting data in YAML");
		}
	}

	private void prepareAndValidateInputs() throws AutomicException {
		yamlTargetPosition = getOptionValue(Constants.YAML_TARGET_POSITION);
		if (!CommonUtil.checkNotEmpty(yamlTargetPosition)) {
			throw new AutomicException(ExceptionConstants.YAML_TARGET_POSITION_EMPTY_MSG);
		}
		key = getOptionValue(Constants.KEY);
		if (!CommonUtil.checkNotEmpty(key)) {
			throw new AutomicException(ExceptionConstants.KEY_CANNOT_BE_EMPTY);
		}
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


