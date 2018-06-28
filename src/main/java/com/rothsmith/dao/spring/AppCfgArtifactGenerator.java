/*
 * Copyright (c) 2009 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.spring;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.VelocityContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.core.ArtifactGenerator;
import com.rothsmith.dao.core.GeneratorException;
import com.rothsmith.dao.core.VelocityGenerator;

/**
 * Class for generating a Spring application context XML file. It uses data from
 * the DAO configuration XML file generated from database metadata.
 * 
 * @author drothauser
 */
public class AppCfgArtifactGenerator implements ArtifactGenerator {

	/**
	 * Logger for AppCfgArtifactGenerator.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(AppCfgArtifactGenerator.class);

	/**
	 * {@link AppCfgMetaDataQuery} for reading the definitions file to retrieve
	 * the data necessary to generate the Spring application configuration file.
	 */
	private final AppCfgMetaDataQuery appCfgMetaDataQuery =
	    new AppCfgMetaDataQuery();

	/**
	 * Definitions XML file as JDom {@link Document}.
	 */
	private final Document defFileDoc;

	/**
	 * {@link VelocityGenerator} for generating the Spring application context
	 * file.
	 */
	private final VelocityGenerator velocityGenerator;

	/**
	 * Construct with the given definitions file.
	 * 
	 * @param defFileDoc
	 *            JDom document for the DAO definitions XML file e.g.
	 *            daodefs.xml.
	 * @param velocityGenerator
	 *            {@link VelocityGenerator} used to generate the definitionas
	 *            file.
	 * @throws GeneratorException
	 *             possible error deserializing the definitions file.
	 */
	public AppCfgArtifactGenerator(Document defFileDoc,
	    VelocityGenerator velocityGenerator) throws GeneratorException {

		this.defFileDoc = defFileDoc;
		this.velocityGenerator = velocityGenerator;

	}

	/**
	 * Create DAO source from List of TblColMetaData Objects and
	 * DtoClassMetaData containing demographics about the class to generate.
	 * 
	 * @throws GeneratorException
	 *             possible Exception
	 */
	@Override
	public void generate() throws GeneratorException {

		Element root = defFileDoc.getRootElement();
		try {
			AppCfgMetaData appCfgMetaData =
			    appCfgMetaDataQuery.fetchMetaData(root);

			VelocityContext context = new VelocityContext();
			context.put("appCfgMetaData", appCfgMetaData);

			String springDaoCtxfile =
			    root.getChildTextTrim("spring-dao-ctxfile");
			String daoCtxClasspath = FilenameUtils.getName(springDaoCtxfile);
			context.put("daoContextFile", daoCtxClasspath);

			String springAppCtxFile = FilenameUtils
			    .normalize(root.getChildTextTrim("spring-app-ctxfile"));

			String springAppCtxTemplate =
			    root.getChildTextTrim("spring-app-ctx-template");

			LOGGER.info(
			    "Generating " + FilenameUtils.normalize(springAppCtxFile));

			velocityGenerator.generate(springAppCtxFile, springAppCtxTemplate,
			    context);

		} catch (JDOMException | IOException e) {
			String msg = String.format("*** Exception caught while "
			    + "generating Spring application config file: %s", e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);
		}

	}
}
