package com.stonetolb.game;

/**
 * Exception object that marks a general failure of the Game to execute.
 * 
 * @author james.baiera
 *
 */
public class GeneralGameException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Create new {@link GeneralGameException} with given message.
	 * @param msg - Error Message
	 */
	public GeneralGameException(String msg) {
		super(msg);
	}
	
	/**
	 * Create new {@link GeneralGameException} with given reason.
	 * @param reason - {@link Throwable} which caused this exception.
	 */
	public GeneralGameException(Throwable reason) {
		super(reason);
	}
	
	/**
	 * Create new {@link GeneralGameException} with given message and 
	 * reason for error.
	 * @param msg - Error Message
	 * @param reason - {@link Throwable} which caused this exception.
	 */
	public GeneralGameException(String msg, Throwable reason) {
		super(msg, reason);
	}
}
