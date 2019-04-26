package com.automic.yaml.actions;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

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
	private boolean isArray;
	private String value;
	private String yamlDownloadPath;
	private String output;

	private YamlOperations yamlOperations = new YamlOperations();

	/**
	 * Initializes a newly created {@code SendMessageAction}
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

			output = yamlOperations.update(content, yamlElementPath, value, isArray, yamlDownloadPath);

			if (CommonUtil.checkNotEmpty(yamlDownloadPath)) {
				YamlUtils.writeContentToFile(yamlDownloadPath, output);
			}

			ConsoleWriter.writeln(Constants.UPDATE_RUN_SUCESSFULL_MSG);
		} catch (Exception exception) {
			ConsoleWriter.writeln(exception);
			throw new AutomicException(exception.getMessage());
		}
	}

	private void prepareAndValidateInputs() throws AutomicException {
		if (StringUtils.isEmpty(yamlElementPath)) {
			throw new AutomicException(ExceptionConstants.YAML_PATH_EMPTY_MSG);
		}
		if (!StringUtils.isEmpty(yamlDownloadPath)) {
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
