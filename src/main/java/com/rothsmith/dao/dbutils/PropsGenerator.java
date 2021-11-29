/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.dbutils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * This class generates a properties file containing SQL statements for Apache
 * DbUtils.
 * 
 * @author drothauser
 */
public final class PropsGenerator implements ArtifactGenerator {

	/**
	 * Logger for PropsGenerator.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(PropsGenerator.class);

	/**
	 * {@link PropsMetaDataQuery} for retrieving data from the definitions file
	 * for building Spring DAO beans.
	 */
	private final PropsMetaDataQuery propsMetaDataQuery =
	    new PropsMetaDataQuery();

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
	public PropsGenerator(Document defFileDoc,
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

			List<PropsMetaData> propsMetaDataList =
			    propsMetaDataQuery.fetchMetaData(root);

			for (PropsMetaData propsMetaData : propsMetaDataList) {

				VelocityContext context = new VelocityContext();
				context.put("propsMetaData", propsMetaData);

				String propsDirName = StringUtils
				    .defaultString(root.getChildTextTrim("dbutils-props-dir"));
				File dirName = new File(propsDirName);
				if (!dirName.exists()) {
					FileUtils.forceMkdir(dirName);
				}

				String sqlPropsFile = FilenameUtils
				    .normalize(String.format("%s/%s.properties", propsDirName,
				        StringUtils.lowerCase(propsMetaData.getClassName())));

				String propsTemplate =
				    root.getChildTextTrim("dbutils-props-template");

				LOGGER.info("Generating " + sqlPropsFile);

				velocityGenerator.generate(sqlPropsFile, propsTemplate,
				    context);
			}

		} catch (JDOMException | IOException e) {
			String msg = String.format("*** Exception caught while "
			    + "generating Spring DAO bean config file: %s", e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);
		}
	}
}
