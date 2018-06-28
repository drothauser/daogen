/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.core;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for generating a file using the Velocity template engine.
 * 
 * @author drothauser
 */
public final class VelocityGenerator {

	/**
	 * SLF4J Logger for VelocityGenerator.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(VelocityGenerator.class);

	/**
	 * Velocity Engine.
	 */
	private static final VelocityEngine VE = initVelocity();

	/**
	 * {@link BooleanUtils} for context.
	 */
	private static final BooleanUtils BOOLEAN_UTILS = new BooleanUtils();

	/**
	 * {@link StringUtils} for context.
	 */
	private static final StringUtils STRING_UTILS = new StringUtils();

	/**
	 * {@link WordUtils} for context.
	 */
	private static final WordUtils WORD_UTILS = new WordUtils();

	/**
	 * {@link DateTool} for context.
	 */
	private static final DateTool DATE_TOOL = new DateTool();

	/**
	 * This method initializes the Velocity engine.
	 * 
	 * @return {@link VelocityEngine}
	 */
	@SuppressWarnings("PMD.AvoidCatchingGenericException")
	private static VelocityEngine initVelocity() {

		InputStream in = null;

		try {

			in = ClassLoader.getSystemClassLoader()
			    .getResourceAsStream("velocity.properties");

			Properties properties = new Properties();
			properties.load(in);

			return new VelocityEngine(properties);

		} catch (Exception e) {

			throw new IllegalStateException(
			    "Unable to initialize Velocity engine " + e, e);

		} finally {

			IOUtils.closeQuietly(in);

		}

	}

	/**
	 * 
	 * @param outputFileName
	 *            the DAO definitions file to generate
	 * @param templateFile
	 *            The Velocity template for generating the artifact
	 * @param context
	 *            The {@link VelocityContext} object that will be merged with
	 *            the Velocity template to produce the output file.
	 * @throws GeneratorException
	 *             possible Velocity templating error
	 */
	@SuppressWarnings("PMD.AvoidCatchingGenericException")
	public void generate(String outputFileName, String templateFile,
	    VelocityContext context) throws GeneratorException {

		// TODO Create Globally for Velocity
		context.put("DateTool", DATE_TOOL);
		context.put("WordUtils", WORD_UTILS);
		context.put("StringUtils", STRING_UTILS);
		context.put("BooleanUtils", BOOLEAN_UTILS);

		BufferedWriter writer = null;

		try {
			Template template = VE.getTemplate(templateFile);

			if (template != null) {

				writer = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(outputFileName)));

				template.merge(context, writer);
			}
		} catch (Exception e) {
			String msg = String
			    .format("*** Exception caught while generating DTO: " + e, e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);

		} finally {

			IOUtils.closeQuietly(writer);

		}
	}
}
