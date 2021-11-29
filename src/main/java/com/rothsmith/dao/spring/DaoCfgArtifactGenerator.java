/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.spring;

import java.io.IOException;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.DateTool;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * This class generates a Spring context file containing DAO beans.
 * 
 * @author drothauser
 */
public final class DaoCfgArtifactGenerator implements ArtifactGenerator {

	/**
	 * Logger for PropsGenerator.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(DaoCfgArtifactGenerator.class);

	/**
	 * {@link DaoCfgMetaDataQuery} for retrieving data from the definitions file
	 * for building Spring DAO beans.
	 */
	private final DaoCfgMetaDataQuery daoCfgMetaDataQuery =
	    new DaoCfgMetaDataQuery();

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
	public DaoCfgArtifactGenerator(Document defFileDoc,
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

			List<DaoCfgMetaData> daoCfgMetaDataList =
			    daoCfgMetaDataQuery.fetchMetaData(root);

			VelocityContext context = new VelocityContext();
			context.put("daoCfgMetaDataList", daoCfgMetaDataList);
			context.put("date", new DateTool());

			String daoContextFile = root.getChildTextTrim("spring-dao-ctxfile");

			String templateFileName =
			    root.getChildTextTrim("spring-dao-ctx-template");

			LOGGER.info("Generating " + daoContextFile);

			velocityGenerator.generate(daoContextFile, templateFileName,
			    context);

		} catch (JDOMException | IOException e) {
			String msg = String.format("*** Exception caught while "
			    + "generating Spring DAO bean config file: %s", e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);
		}
	}
}
