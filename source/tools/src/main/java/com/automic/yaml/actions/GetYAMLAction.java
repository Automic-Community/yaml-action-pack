package com.automic.yaml.actions;

import com.automic.yaml.base.AbstractYAMLAction;
import com.automic.yaml.constants.Constants;
import com.automic.yaml.constants.ExceptionConstants;
import com.automic.yaml.exception.AutomicException;
import com.automic.yaml.util.CommonUtil;
import com.automic.yaml.util.ConsoleWriter;
import com.automic.yaml.util.YamlOperations;
import com.jayway.jsonpath.PathNotFoundException;

public class GetYAMLAction extends AbstractYAMLAction {

	private String yamlElementPath;
	private boolean failIfPathDoesNotExist = true;

	private YamlOperations yamlOperations = new YamlOperations();

	public GetYAMLAction() {
		addOption(Constants.YAML_ELEMENT_PATH, true, "Yaml Element Path");
		addOption(Constants.FAIL_IF_PATH_DOES_NOT_EXIST, false, "Fail if Path Does Not Exist");
	}

	@Override
	protected void executeSpecific() throws AutomicException {
		prepareAndValidateInputs();
		try {
			String output = yamlOperations.read(content, yamlElementPath, failIfPathDoesNotExist);
			boolean valueExists = true;
			if (output == null) {
				valueExists = false;
			}
			ConsoleWriter.writeln(Constants.DEFAULT_VARIABLE + "::=\n" + output);
			ConsoleWriter.writeln(Constants.VALUE_EXISTS + "::=" + valueExists);
			ConsoleWriter.writeln("========================");
		} catch (PathNotFoundException e) {
			if (failIfPathDoesNotExist) {
				ConsoleWriter.writeln(e);
				throw new AutomicException("Invalid YAML path " + yamlElementPath + ", " + e.getMessage());
			} else {
				ConsoleWriter.writeln("Provided YAML Path does not exist");
			}
		} catch (Exception exception) {
			ConsoleWriter.writeln(exception);
			throw new AutomicException("Exception occurred while retrieving data from YAML");
		}
	}

	private void prepareAndValidateInputs() throws AutomicException {
		yamlElementPath = getOptionValue(Constants.YAML_ELEMENT_PATH);
		if (!CommonUtil.checkNotEmpty(yamlElementPath)) {
			throw new AutomicException(ExceptionConstants.YAML_PATH_EMPTY_MSG);
		}
		failIfPathDoesNotExist = CommonUtil.convert2Bool(getOptionValue(Constants.FAIL_IF_PATH_DOES_NOT_EXIST));

	}
}