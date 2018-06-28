/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.runner;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Class that builds an {@link Options} object for handling command line
 * parameters.
 * 
 * @author drothauser
 * 
 */
public class CmdLineOptionBuilder {

	/**
	 * Argument Long Option index.
	 */
	private static final int LONGOPT_IDX = 0;

	/**
	 * Argument Short Option index.
	 */
	private static final int SHORTOPT_IDX = 1;

	/**
	 * Argument requited index.
	 */
	private static final int REQUIRED_IDX = 2;

	/**
	 * Argument description index.
	 */
	private static final int DESCRIPTION_IDX = 3;

	/**
	 * Command line arguments properties.
	 */
	private Properties cmdlineProps;

	/**
	 * Constructor that uses initializes a {@link Properties} object from the
	 * given properties file. This file is expected to be in the classpath.
	 * 
	 * @param cmdlinePropsFile
	 *            Command line properties file. Expected to be in the classpath.
	 */
	public CmdLineOptionBuilder(String cmdlinePropsFile) {

		InputStream is = null;
		try {
			cmdlineProps = new Properties();
			is = CmdLineOptionBuilder.class
			    .getResourceAsStream(cmdlinePropsFile);
			cmdlineProps.load(is);

		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}

	}

	/**
	 * This method builds the command line options expected by the Java command.
	 * 
	 * @return Options - Options represents a collection of Option objects,
	 *         which describe the possible options for a command-line.
	 */
	public Options buildCmdOptions() {
		Options options = new Options();

		addOptsFromPropFile(options);

		addKeyValuePairOpts(options);

		return options;
	}

	/**
	 * Add ability for the command line to accept key value pair options e.g.
	 * foo=bar.
	 * 
	 * @param options
	 *            Options represents a collection of Option objects, which
	 *            describe the possible options for a command-line.
	 */
	@SuppressWarnings("static-access")
	private void addKeyValuePairOpts(Options options) {
		options.addOption(OptionBuilder.withArgName("key=value").hasArgs(2)
		    .withValueSeparator()
		    .withDescription("use value for given property").create("P"));
	}

	/**
	 * Add command line options defined in the
	 * /jobrunner/src/main/resources/cmdline.properties file.
	 * 
	 * @param options
	 *            Options represents a collection of Option objects, which
	 *            describe the possible options for a command-line.
	 */
	@SuppressWarnings("static-access")
	private void addOptsFromPropFile(Options options) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, String> propMap =
		    new HashMap<String, String>((Map) cmdlineProps);

		for (Map.Entry<String, String> entry : propMap.entrySet()) {
			String argName = entry.getKey();
			String[] values =
			    StringUtils.splitPreserveAllTokens(entry.getValue(), ',');
			String longOpt = values[LONGOPT_IDX];
			String shortOpt = values[SHORTOPT_IDX];
			boolean required = Boolean.parseBoolean(values[REQUIRED_IDX]);
			String desc = values[DESCRIPTION_IDX];
			options.addOption(OptionBuilder.withLongOpt(longOpt)
			    .withDescription(desc).hasArg().withArgName(argName)
			    .isRequired(required).create(shortOpt));
		}
	}
}
