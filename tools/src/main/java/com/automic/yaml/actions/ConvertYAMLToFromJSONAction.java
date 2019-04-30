package com.automic.yaml.actions;

import java.io.File;
import java.io.IOException;

import com.automic.yaml.base.AbstractAction;
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
public class ConvertYAMLToFromJSONAction extends AbstractAction {
	
	private String originalFilePath;
	private String convertToFormat="JSON";
	private String yamlDownloadPath;
	private String content;
	
	private YamlOperations yamlOperations = new YamlOperations();

	/**
	 * Initializes a newly created {@code SendMessageAction}
	 */
	public ConvertYAMLToFromJSONAction() {
		addOption(Constants.ORIGINAL_FILE_PATH, true, "Original File Path");
		addOption(Constants.CONVERT_TYPE, true, "Convert to");
		addOption(Constants.YAML_DOWNLOAD_PATH, false, "Download Path");

	}
	
	@Override
	protected void execute() throws AutomicException {
		// TODO Auto-generated method stub
		prepareAndValidateInputs();
		executeSpecific();
		
	}
	
	protected void executeSpecific() throws AutomicException {
		try{
			String convertedContent=null;
			convertedContent = yamlOperations.formatConvertion(content, convertToFormat);
			if (CommonUtil.checkNotEmpty(yamlDownloadPath)) {
				YamlUtils.writeContentToFile(yamlDownloadPath, convertedContent);
			}
			
			ConsoleWriter.writeln("UC4RB_CONTENT::=========="+"\n" + convertedContent);
			ConsoleWriter.writeln("========================");
		}catch(Exception e){
			ConsoleWriter.writeln(e);
			throw new AutomicException("Unable to convert in "+convertToFormat+" format");
		}
	}

	private void prepareAndValidateInputs() throws AutomicException {
		originalFilePath = getOptionValue(Constants.ORIGINAL_FILE_PATH);
		convertToFormat=getOptionValue(Constants.CONVERT_TYPE);
		yamlDownloadPath=getOptionValue(Constants.YAML_DOWNLOAD_PATH);
		
		if (!CommonUtil.checkNotEmpty(originalFilePath)) {
			throw new AutomicException(
					String.format(ExceptionConstants.INVALID_INPUT_PARAMETER, "Original File Path", originalFilePath));
		}
		File file = new File(originalFilePath);
		CommonUtil.checkFileExists(file, "Original File Path");
		if (file.length() == 0) {
			throw new AutomicException("Provided Original file/content is empty");
		}
		try {
			if(convertToFormat.equals("JSON"))
				content = YamlUtils.readYAMLFromFile(originalFilePath);
			else
				content=YamlUtils.readJSONFromFile(originalFilePath);
		} catch (IOException e) {
			ConsoleWriter.writeln(e);
			throw new AutomicException("Invalid YAML/JSON Content, make sure you are providing valid YAML/JSON data/file");
		}
		
		if (CommonUtil.checkNotEmpty(yamlDownloadPath)) {
			try {

				File downLoadFile = new File(yamlDownloadPath);
				downLoadFile.createNewFile();
			} catch (IOException e) {
				ConsoleWriter.writeln(e);
				throw new AutomicException(String.format(ExceptionConstants.UNABLE_TO_CREATE_FILE, yamlDownloadPath));
			}
		}

	}
	

}
