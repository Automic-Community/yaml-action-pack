package com.automic.yaml.client;

import com.automic.yaml.base.AbstractAction;
import com.automic.yaml.cli.Cli;
import com.automic.yaml.cli.CliOptions;
import com.automic.yaml.constants.Constants;
import com.automic.yaml.constants.ExceptionConstants;
import com.automic.yaml.exception.AutomicException;
import com.automic.yaml.util.ConsoleWriter;

/**
 * Class to delegate the parameters to the respective classes based on the Action parameter.
 *
 */
public class ClientHelper {
    private static final String ABSOLUTEPATH = "com.automic.yaml.actions";

    private ClientHelper() {
    }

    /**
     * Method to delegate parameters to an instance of {@link AbstractAction} based on the value of Action parameter.
     *
     * @param actionParameters
     *            of options with key as option name and value is option value
     * @throws AutomicException
     */

    public static void executeAction(String[] actionParameters) throws AutomicException{
        String actionName = new Cli(new CliOptions(), actionParameters).getOptionValue(Constants.ACTION);

        AbstractAction action = null;
        try {
            Class<?> classDefinition = Class.forName(getCanonicalName(actionName));
            action = (AbstractAction) classDefinition.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            ConsoleWriter.writeln(e);
            String msg = String.format(ExceptionConstants.INVALID_INPUT_PARAMETER, Constants.ACTION, actionName);
            throw new AutomicException(msg);
        }
        action.executeAction(actionParameters);
    }

    private static String getCanonicalName(String clsName) {
        return ABSOLUTEPATH + "." + clsName;
    }
}
