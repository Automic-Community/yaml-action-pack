package com.automic.yaml.exception;

/**
 * Exception class thrown to indicate that error has occurred while processing request. It could either be
 * <li>
 * <ul>
 * Business exception for invalid inputs to Actions
 * </ul>
 * </li>
 *
 */
public class AutomicException extends Exception {

    private static final long serialVersionUID = -2593992984664372683L;

    /**
     * Constructor that takes an error message
     *
     * @param message
     */
    public AutomicException(String message) {
        super(message);
    }
    
    public AutomicException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
