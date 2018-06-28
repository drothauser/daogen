/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.jdom2.Document;

import com.rothsmith.dao.core.ArtifactGenerator;
import com.rothsmith.dao.core.GeneratorException;
import com.rothsmith.dao.core.VelocityGenerator;
import com.rothsmith.dao.dto.DtoGenerator;

/**
 * Class for generating all the various types of Spring DAO artifacts. Note that
 * this class is a composite of {@link ArtifactGenerator}s that are run one by
 * one.
 * 
 * @author drothauser
 */
public class SpringArtifactGenerator implements ArtifactGenerator {

	/**
	 * {@link ArtifactGenerator} {@link List} to execute.
	 */
	private final List<ArtifactGenerator> generatorList;

	/**
	 * Construct with the given JDBC {@link DataSource}.
	 * 
	 * @param dataSource
	 *            JDBC {@link DataSource}
	 * @param defFileDoc
	 *            JDom document for the DAO definitions XML file e.g.
	 *            daodefs.xml.
	 * @param velocityGenerator
	 *            {@link VelocityGenerator} used to generating DAO artifacts.
	 * @throws GeneratorException
	 *             possible error deserializing the definitions file.
	 */
	public SpringArtifactGenerator(DataSource dataSource, Document defFileDoc,
	    VelocityGenerator velocityGenerator) throws GeneratorException {

		// CHECKSTYLE:OFF Magic number 4 ok here.
		generatorList = new ArrayList<ArtifactGenerator>(4);
		// CHECKSTYLE:ON
		generatorList
		    .add(new DtoGenerator(dataSource, defFileDoc, velocityGenerator));
		generatorList
		    .add(new DaoCfgArtifactGenerator(defFileDoc, velocityGenerator));
		generatorList
		    .add(new AppCfgArtifactGenerator(defFileDoc, velocityGenerator));
		generatorList.add(new SpringJunitGenerator(dataSource, defFileDoc,
		    velocityGenerator));

	}

	/**
	 * Constructor that initializes the datasource and DAO generation
	 * parameters.
	 * 
	 * @param artifactGenerators
	 *            {@link ArtifactGenerator} {@link List} for generating Spring
	 *            DAO
	 * 
	 * @throws GeneratorException
	 *             possible definitions file generation error
	 */
	public SpringArtifactGenerator(List<ArtifactGenerator> artifactGenerators)
	        throws GeneratorException {

		this.generatorList = artifactGenerators;
	}

	/**
	 * Constructor that initializes the datasource and DAO generation
	 * parameters.
	 * 
	 * @param generators
	 *            {@link ArtifactGenerator} {@link List} for generating Spring
	 *            DAO
	 * 
	 * @throws GeneratorException
	 *             possible definitions file generation error
	 */
	public SpringArtifactGenerator(ArtifactGenerator... generators)
	        throws GeneratorException {

		this.generatorList = Arrays.asList(generators);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void generate() throws GeneratorException {

		for (ArtifactGenerator artifactGenerator : generatorList) {
			artifactGenerator.generate();
		}

	}
}
