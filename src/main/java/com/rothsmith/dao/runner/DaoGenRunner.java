/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.runner;

import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.generator.DaoGenerator;

import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.Params;
import net.rothsmith.dao.core.ParamsBuilder;

/**
 * Java application that launches {@link DaoGenerator} using the given command
 * line arguments.
 * 
 * @author drothauser
 */
public final class DaoGenRunner {

	/**
	 * Logger for SpringDaoGenRunner.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(DaoGenRunner.class);

	/**
	 * Private constructor to thwart instantiation.
	 */
	private DaoGenRunner() {
	}

	/**
	 * This method generates the DAOs needed by the case import engine.
	 * 
	 * @param args
	 *            an array of arguments for generating the dao(s):
	 *            <ol start="0">
	 *            <li>types e.g. "TABLE" or "VIEW"
	 *            <li>Pattern list: A comma separated list of object name
	 *            patterns e.g. &quot;person&quot or &quot;person,comment&quot;.
	 *            <li>Base output directory e.g. src, gen.;
	 *            <li>Java package for the generated DTO.
	 *            <li>Velocity template file for generating the Spring DAO beans
	 *            config file.
	 *            <li>XML file containing DAO parameters for generating the
	 *            Spring DAO config file and the DTOs.
	 *            <li>Name of the Spring DAO bean context file.
	 *            <li>The main Spring bean configuration (typically
	 *            "applicationContext.xml"). This file will contain an import
	 *            element for the dao Spring configuration file.
	 *            <li>Output directory for test packages (e.g.
	 *            &quot;test&quot;).
	 *            <li>Package for test classes.
	 *            </ol>
	 * @throws SQLException
	 *             possible SQL error
	 * @throws GeneratorException
	 *             possible generation error
	 */
	@SuppressWarnings("PMD.UseVarargs")
	public static void main(final String[] args)
	        throws SQLException, GeneratorException {

		LOGGER.info(String.format("Command:%njava %s %s",
		    DaoGenRunner.class.getName(), StringUtils.join(args, " ")));

		Options options =
		    new CmdLineOptionBuilder("/cmdline.properties").buildCmdOptions();

		try {
			CommandLine cmdLine = new GnuParser().parse(options, args);

			Params params = ParamsBuilder.newInstance()
			    .withJdbcURL(cmdLine.getOptionValue("ju"))
			    .withDbUsername(cmdLine.getOptionValue("du"))
			    .withDbPassword(cmdLine.getOptionValue("dp"))
			    .withJdbcDriverClassName(cmdLine.getOptionValue("dc"))
			    .withTableTypes(cmdLine.getOptionValue("ty"))
			    .withTableNames(cmdLine.getOptionValue("nm"))
			    .withOutputDir(cmdLine.getOptionValue("od"))
			    .withDtoTemplate(cmdLine.getOptionValue("dtotpl"))
			    .withDtoPackageName(cmdLine.getOptionValue("dtp"))
			    .withDefFileTemplate(cmdLine.getOptionValue("dt"))
			    .withDefFile(cmdLine.getOptionValue("df"))
			    .withTestOutputDir(cmdLine.getOptionValue("to"))
			    .withTestPackageName(cmdLine.getOptionValue("tp"))
			    .withSchema(cmdLine.getOptionValue("s"))
			    .withJunitTemplate(cmdLine.getOptionValue("jt"))
			    .withPropsFile(cmdLine.getOptionValue("pf"))
			    .withSpringDaoCtxTemplate(cmdLine.getOptionValue("sdft"))
			    .withSpringDaoCtxFile(cmdLine.getOptionValue("sdf"))
			    .withSpringAppCtxTemplate(cmdLine.getOptionValue("sact"))
			    .withSpringAppCtxFile(cmdLine.getOptionValue("sac"))
			    .withSqlStmts(cmdLine.getOptionValue("sql"))
			    .withDbUtilsPropsTemplate(cmdLine.getOptionValue("dupt"))
			    .withDbUtilsPropsDir(cmdLine.getOptionValue("dupd"))
			    .withJndiName(cmdLine.getOptionValue("jndi")).build();

			LOGGER.info(String
			    .format("Running with the following parameters:%n%s", params));

			DaoGenerator daoGenerator = new DaoGenerator();
			daoGenerator.generate(params);

		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -cp .;daogen.jar;lib/* "
			    + "net.rothsmith.dao.runner.DaoGenRunner", options);
		}

	}
}
