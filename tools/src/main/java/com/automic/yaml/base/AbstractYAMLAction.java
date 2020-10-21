package com.automic.yaml.base;

import java.io.File;
import java.io.IOException;

import com.automic.yaml.constants.Constants;
import com.automic.yaml.constants.ExceptionConstants;
import com.automic.yaml.exception.AutomicException;
import com.automic.yaml.util.CommonUtil;
import com.automic.yaml.util.ConsoleWriter;
import com.automic.yaml.util.YamlUtils;

/***
 * 
 * @author extpav
 * 
 */
public abstract class AbstractYAMLAction extends AbstractAction {

	protected String content;

	public AbstractYAMLAction() {
		addOption(Constants.YAML_FILE_PATH, true, "YAML File Path");
	}

	/**
	 * This method initializes the arguments and calls the execute method.
	 *
	 * @throws AutomicException exception while executing an action
	 */
	public final void execute() throws AutomicException {
		prepareCommonInputs();
		executeSpecific();
	}

	private void prepareCommonInputs() throws AutomicException {
		String yamlFilePath = getOptionValue(Constants.YAML_FILE_PATH);
		if (!CommonUtil.checkNotEmpty(yamlFilePath)) {
			throw new AutomicException(
					String.format(ExceptionConstants.INVALID_INPUT_PARAMETER, "YAML File Path/Content", yamlFilePath));
		}
		File file = new File(yamlFilePath);
		CommonUtil.checkFileExists(file, "YAML File Path/Content");
		if (file.length() == 0) {
			throw new AutomicException("Provided YAML file/content is empty");
		}
		try {
			content = YamlUtils.readYAMLFromFile(yamlFilePath);
		} catch (IOException e) {
			ConsoleWriter.writeln(e);
			throw new AutomicException("Invalid YAML Content, make sure you are providing valid YAML data/file");
		}

	}

	/**
	 * Method to execute the action.
	 *
	 * @throws AutomicException
	 */
	protected abstract void executeSpecific() throws AutomicException;

}
