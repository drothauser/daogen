/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.generator;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.jdom2.Document;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Class for finding and constructing the DAO {@link ArtifactGenerator} to use
 * based on XPATH expressions found in the generator rules properties file. Each
 * XPATH expression is evaluated against data in the DAO definitions.
 * 
 * @author drothauser
 *
 */
public class GeneratorFactory {

	/**
	 * SLF4J Logger for GeneratorFactory.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(GeneratorFactory.class);

	/**
	 * {@link Properties} object containing the rules for determining the type
	 * of generator to use
	 */
	private final Properties finderRulesProps;

	/**
	 * {@link VelocityGenerator} for generating DAO artifacts.
	 */
	private final VelocityGenerator velocityGenerator;

	/**
	 * Construct using the given properties file containing the rules that
	 * determine what type of generator to use.
	 * 
	 * @param finderRulesPropsFile
	 *            the name of the file containing the rules that determine what
	 *            type of generator to use
	 * @param velocityGenerator
	 *            {@link VelocityGenerator} used to generate DAO artifacts.
	 * @param velocityGenerator
	 * 
	 */
	public GeneratorFactory(String finderRulesPropsFile,
	    VelocityGenerator velocityGenerator) {

		this.velocityGenerator = velocityGenerator;

		try {
			Properties props = new Properties();
			InputStream is = GeneratorFactory.class
			    .getResourceAsStream(finderRulesPropsFile);
			props.load(is);
			this.finderRulesProps = props;
		} catch (IOException e) {
			String msg = String.format(
			    "*** Exception caught loading generator "
			        + "finder rules from the \"%s\" properties file: %s",
			    finderRulesPropsFile, e);
			LOGGER.error(msg, e);
			throw new IllegalArgumentException(msg, e);
		}
	}

	/**
	 * Method for finding and returning a {@link ArtifactGenerator}.
	 * 
	 * @param dataSource
	 *            {@link DataSource} object used to construct the generator
	 * @param defFileDoc
	 *            DAO definitions file
	 * @return an instance of a GeneratorException
	 * @throws GeneratorException
	 *             possible problem finding or constructing the
	 *             {@link ArtifactGenerator} .
	 */
	public ArtifactGenerator create(DataSource dataSource, Document defFileDoc)
	        throws GeneratorException {
		ArtifactGenerator artifactGenerator = null;

		try {

			for (Entry<Object, Object> entry : finderRulesProps.entrySet()) {
				String xpathExpr = (String) entry.getValue();
				XPathExpression<Boolean> xpath = XPathFactory.instance()
				    .compile(xpathExpr, Filters.fboolean());
				if (xpath.evaluateFirst(defFileDoc)) {
					artifactGenerator =
					    (ArtifactGenerator) ConstructorUtils.invokeConstructor(
					        Class.forName((String) entry.getKey()), dataSource,
					        defFileDoc, velocityGenerator);
				}
			}

		} catch (NoSuchMethodException | IllegalAccessException
		        | InvocationTargetException | InstantiationException
		        | ClassNotFoundException e) {
			String msg = String.format(
			    "*** Exception caught while " + "finding the generator: %s", e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);
		}

		return artifactGenerator;

	}
}
