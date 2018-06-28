/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.def;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.core.GeneratorException;
import com.rothsmith.dao.core.Params;
import com.rothsmith.dao.core.VelocityGenerator;

/**
 * Class for generating a technology agnostic DAO definitions file.
 * 
 * @author drothauser
 */
public class DefGenerator {

	/**
	 * Logger for DefGenerator.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(DefGenerator.class);

	/**
	 * {@link VelocityGenerator} for generating the definitions file from
	 * parameters and DB metadata.
	 */
	private final VelocityGenerator velocityGenerator;

	/**
	 * Construct instance with a {@link VelocityGenerator}.
	 * 
	 * @param velocityGenerator
	 *            {@link VelocityGenerator} used to generate the definitions
	 *            file.
	 */
	public DefGenerator(VelocityGenerator velocityGenerator) {

		this.velocityGenerator = velocityGenerator;

	}

	/**
	 * Create the DAO definitions XML file.
	 * 
	 * @param dataSource
	 *            JDBC Data Source
	 * @param params
	 *            DAO generator parameters
	 * 
	 * @return JDom {@link Document} of the definitions file
	 * 
	 * @throws GeneratorException
	 *             possible error generating the definitions file
	 */
	public Document generate(DataSource dataSource, Params params)
	        throws GeneratorException {

		try {
			DefMetaDataQuery defMetaDataQuery =
			    new DefMetaDataQuery(dataSource);
			List<DefMetaData> defMetaDataList =
			    defMetaDataQuery.fetchMetaData(params);

			if (params.getSqlStmts() != null) {
				String[] sqlStmtsArray =
				    StringUtils.split(params.getSqlStmts(), ";");

				for (int i = 0; i < sqlStmtsArray.length; i++) {
					DefMetaData defMetaData = DefMetaDataBuilder.daoMetaData()
					    .withSqlStmt(StringUtils.strip(sqlStmtsArray[i], "\"'"))
					    .withClassName(String.format("Sql%d", i))
					    .withOutputDir(params.getOutputDir())
					    .withPackageName(params.getDtoPackageName())
					    .withTestOutputDir(params.getTestOutputDir())
					    .withTestPackageName(params.getTestPackageName())
					    .withUser(System.getProperty("user.name")).build();

					defMetaDataList.add(defMetaData);
				}
			}

			VelocityContext context = new VelocityContext();

			context.put("defMetaDataList", defMetaDataList);
			context.put("params", params);
			context.put("delim", new char[] { '_' });

			try (Connection conn = dataSource.getConnection();) {
				context.put("vendor",
				    conn.getMetaData().getDatabaseProductName());
			}

			String defFile = FilenameUtils.normalize(params.getDefFile());

			LOGGER.info(String.format("Generating DAO defininitions file: %s",
			    defFile));

			FileUtils.forceMkdir(new File(FilenameUtils.getFullPath(defFile)));

			velocityGenerator.generate(defFile, params.getDefFileTemplate(),
			    context);

			SAXBuilder builder = new SAXBuilder();

			return builder.build(new File(defFile));

		} catch (IOException | SQLException | JDOMException e) {
			String msg = String.format("*** Exception caught while "
			    + "generating the DAO definitions file: %s", e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);
		}
	}
}
