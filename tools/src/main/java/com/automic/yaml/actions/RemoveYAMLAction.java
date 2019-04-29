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
 * @author priyadarshnitripathi
 *
 */
public class RemoveYAMLAction extends AbstractYAMLAction {
	
	private String yamlElementPath;
	private boolean isFail=true;
	private String yamlDownloadPath;
	

	private YamlOperations yamlOperations = new YamlOperations();

	/**
	 * Initializes a newly created {@code SendMessageAction}
	 */
	public RemoveYAMLAction() {
		addOption(Constants.YAML_ELEMENT_PATH, true, "Path to Yaml Element");
		addOption(Constants.IS_FAIL, false, "Fail if path does not exist");
		addOption(Constants.YAML_DOWNLOAD_PATH, false, "Download Path");

	}
	
	@Override
	protected void executeSpecific() throws AutomicException {
		String output=null;
		prepareAndValidateInputs();
		try {

			output = yamlOperations.remove(content, yamlElementPath, isFail);
			if(output==null)
				output=content;
			if (CommonUtil.checkNotEmpty(yamlDownloadPath)) {
				YamlUtils.writeContentToFile(yamlDownloadPath, output);
			}
			
			ConsoleWriter.writeln("UC4RB_CONTENT::=========="+"\n" + output);
			ConsoleWriter.writeln("========================");
		} catch (Exception exception) {
			ConsoleWriter.writeln(exception);
			throw new AutomicException("Exception occurred while removing data from YAML");
		}
	}

	private void prepareAndValidateInputs() throws AutomicException {
		yamlElementPath = getOptionValue(Constants.YAML_ELEMENT_PATH);
		if (!CommonUtil.checkNotEmpty(yamlElementPath)) {
			throw new AutomicException(ExceptionConstants.UPDATE_YAML_PATH_EMPTY_MSG);
		}
		
		isFail=CommonUtil.convert2Bool(getOptionValue(Constants.IS_FAIL));
		yamlDownloadPath=getOptionValue(Constants.YAML_DOWNLOAD_PATH);
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
