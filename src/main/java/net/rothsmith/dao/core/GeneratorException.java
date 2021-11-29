/*
 * (c) 2013 Rothsmith, LLC All Rights Reserved.
 */
package net.rothsmith.dao.core;

/**
 * {@link net.rothsmith.dao.spring.core.ArtifactGenerator} checked exception.
 * 
 * @author drothauser
 * 
 */
public class GeneratorException
        extends
        Exception {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -2791468870625123532L;

	/**
	 * @param message
	 *            error message
	 */
	public GeneratorException(String message) {
		super(message);
	}

	/**
	 * @param message
	 *            error message
	 * @param cause
	 *            {@link Throwable} instance.
	 */
	public GeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

}
