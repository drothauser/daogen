/*
 * Copyright (c) 2016 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.dbutils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.velocity.VelocityContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.osjava.sj.loader.SJDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.core.ArtifactGenerator;
import com.rothsmith.dao.core.GeneratorException;
import com.rothsmith.dao.core.VelocityGenerator;
import com.rothsmith.utils.database.JDBCServiceLocator;

/**
 * This class generates a
 * <a href="http://www.unitils.org/summary.html">Unitils</a> properties file
 * containing database connection information for Junit testing.
 * 
 * @author drothauser
 */
public final class UnitilsGenerator implements ArtifactGenerator {

	/**
	 * Logger for UnitilsGenerator.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(UnitilsGenerator.class);

	/**
	 * Definitions XML file as JDom {@link Document}.
	 */
	private final Document defFileDoc;

	/**
	 * {@link VelocityGenerator} for generating the definitions file from
	 * parameters and DB metadata.
	 */
	private final VelocityGenerator velocityGenerator;

	/**
	 * Construct with the given definitions file.
	 * 
	 * @param defFileDoc
	 *            JDom document for the DAO definitions XML file e.g.
	 *            daodefs.xml.
	 * @param velocityGenerator
	 *            {@link VelocityGenerator} used to generate the artifact.
	 * @throws GeneratorException
	 *             possible error deserializing the definitions file.
	 */
	public UnitilsGenerator(Document defFileDoc,
	    VelocityGenerator velocityGenerator) throws GeneratorException {

		this.defFileDoc = defFileDoc;

		this.velocityGenerator = velocityGenerator;

	}

	/**
	 * Generate the Spring DAO bean context file.
	 * 
	 * @throws GeneratorException
	 *             possible error generating the Spring DAO bean config file
	 */
	@Override
	public void generate() throws GeneratorException {

		try {
			Element root = defFileDoc.getRootElement();

			VelocityContext context = new VelocityContext();

			String jdbcUrl = root.getChildTextTrim("jdbc-url");

			if (StringUtils.isEmpty(jdbcUrl)) {
				String jndiName = root.getChildTextTrim("jndi-name");
				populateCtxFromJndi(jndiName, context);
			} else {
				context.put("jdbcUrl", jdbcUrl);
				context.put("jdbcDriver", root.getChildTextTrim("jdbc-driver"));
				context.put("dbUsername", root.getChildTextTrim("db-username"));
				context.put("dbPassword", root.getChildTextTrim("db-password"));
			}

			String testJavaDir = StringUtils
			    .defaultString(root.getChildTextTrim("test-output-dir"));
			String testPropsDir = StringUtils.replacePattern(testJavaDir,
			    "[/\\\\]java", "/resources");
			File propsDir = new File(testPropsDir);
			if (!propsDir.exists()) {
				FileUtils.forceMkdir(propsDir);
			}

			String unitilsPropsFile = FilenameUtils.normalize(
			    String.format("%s/%s.properties", propsDir, "unitils"));

			String unitilsTemplate =
			    root.getChildTextTrim("unitils-props-template");

			LOGGER.info("Generating " + unitilsPropsFile);

			velocityGenerator.generate(unitilsPropsFile, unitilsTemplate,
			    context);

		} catch (IOException | NamingException e) {
			String msg = String.format("*** Exception caught while "
			    + "generating unitils properties file: %s", e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);
		}
	}

	/**
	 * This method tries to extract JDBC connection parameters from a datasource
	 * retrieved from JNDI.
	 * 
	 * @param jndiName
	 *            The JNDI name of the datasource
	 * @param context
	 *            The Velocity context to place the connection parameters on
	 * 
	 * 
	 * @throws NamingException
	 *             possible error resolving JNDI name
	 * @throws GeneratorException
	 *             possible error retrieving JNDI connection properties
	 */
	private void populateCtxFromJndi(String jndiName, VelocityContext context)
	        throws GeneratorException, NamingException {
		SJDataSource sjds = (SJDataSource) JDBCServiceLocator.getInstance()
		    .getDataSource(jndiName);

		try (Connection conn = sjds.getConnection();) {

			context.put("jdbcUrl", conn.getMetaData().getURL());
			context.put("vendor", conn.getMetaData().getDriverName());
			context.put("dbUsername", conn.getMetaData().getUserName());
			context.put("dbUsername",
			    (String) FieldUtils.readField(sjds, "password", true));

		} catch (SQLException | IllegalAccessException e) {
			String msg = String.format("*** Exception caught while "
			    + "generating unitils properties file using JNDI connection: %s",
			    e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);
		}
	}
}
