/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package net.rothsmith.dao.core;

/**
 * An generator that creates DAO artifacts.
 * 
 * @author drothauser
 *
 */
public interface ArtifactGenerator {

	/**
	 * This method generates the DAO artifacts.
	 * 
	 * @throws GeneratorException
	 *             possible error during generation
	 */
	void generate() throws GeneratorException;

}