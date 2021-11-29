/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.dbutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.jdom2.Document;

import com.rothsmith.dao.dto.DtoGenerator;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Class for generating all the various types of DbUtils DAO artifacts. Note that
 * this class is a composite of {@link ArtifactGenerator}s that are run one by
 * one.
 * 
 * @author drothauser
 */
public class DbUtilsArtifactGenerator implements ArtifactGenerator {

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
	public DbUtilsArtifactGenerator(DataSource dataSource, Document defFileDoc,
	    VelocityGenerator velocityGenerator) throws GeneratorException {

		generatorList = new ArrayList<ArtifactGenerator>(3);

		generatorList
		    .add(new DtoGenerator(dataSource, defFileDoc, velocityGenerator));
		generatorList.add(new PropsGenerator(defFileDoc, velocityGenerator));
		generatorList.add(new DbUtilsJunitGenerator(dataSource, defFileDoc,
		    velocityGenerator));
		generatorList.add(new UnitilsGenerator(defFileDoc, velocityGenerator));
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
	public DbUtilsArtifactGenerator(List<ArtifactGenerator> artifactGenerators)
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
	public DbUtilsArtifactGenerator(ArtifactGenerator... generators)
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
