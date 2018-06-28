/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.generator;

import javax.sql.DataSource;

import org.jdom2.Document;

import com.rothsmith.dao.core.ArtifactGenerator;
import com.rothsmith.dao.core.GeneratorException;
import com.rothsmith.dao.core.Params;
import com.rothsmith.dao.core.VelocityGenerator;
import com.rothsmith.dao.def.DefGenerator;

/**
 * Runs a DAO generator.
 * 
 * @author drothauser
 */
public final class DaoGenerator {

	/**
	 * {@link VelocityGenerator} for generating DAO artifacts.
	 */
	private final VelocityGenerator velocityGenerator = new VelocityGenerator();

	/**
	 * {@link GeneratorFactory} for creating a generator based on the DAO
	 * generation parameters.
	 */
	private final GeneratorFactory generatorFactory =
	    new GeneratorFactory("/genSelectorRules.properties", velocityGenerator);

	/**
	 * {@link DefGenerator} for generating the DAO definitions file.
	 */
	private final DefGenerator defGenerator =
	    new DefGenerator(velocityGenerator);

	/**
	 * @param params
	 *            {@link Params} - Parameters for generation
	 * @throws GeneratorException
	 *             possible error during generation
	 */
	public void generate(Params params) throws GeneratorException {

		DataSource dataSource = DataSourceFactory.create(params);

		Document defFileDoc = defGenerator.generate(dataSource, params);

		ArtifactGenerator artifactGenerator =
		    generatorFactory.create(dataSource, defFileDoc);

		artifactGenerator.generate();

	}

}
