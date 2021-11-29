/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.spring;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.IdxMetaData;
import com.rothsmith.dao.db.IdxMetaDataQuery;
import com.rothsmith.dao.db.PKMetaData;
import com.rothsmith.dao.db.PKMetaDataQuery;
import com.rothsmith.dao.db.SelectDataQuery;
import com.rothsmith.dao.db.SelectMetaData;
import com.rothsmith.dao.dto.DtoClassMetaData;
import com.rothsmith.dao.dto.DtoMethodMetaData;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.TextUtils;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Class to generate Spring Junit classes.
 * 
 * @author drothauser
 */
public final class SpringJunitGenerator implements ArtifactGenerator {

	/**
	 * Logger for SpringJunitGenerator.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(SpringJunitGenerator.class);

	/**
	 * {@link DaoCfgMetaDataQuery} for retrieving data from the definitions file
	 * for building Spring DAO beans.
	 */
	private final DaoCfgMetaDataQuery daoCfgMetaDataQuery =
	    new DaoCfgMetaDataQuery();

	/**
	 * {@link PKMetaDataQuery} utility for getting a table primary key metadata.
	 */
	private final PKMetaDataQuery pKMetaDataQuery = new PKMetaDataQuery();

	/**
	 * {@link IdxMetaDataQuery} utility for getting table index metadata.
	 * metadata.
	 */
	private final IdxMetaDataQuery idxMetaDataQuery = new IdxMetaDataQuery();

	/**
	 * {@link SelectDataQuery} for getting column metadata from a SQL query.
	 */
	private final SelectDataQuery sqlSelectDataQuery = new SelectDataQuery();

	/**
	 * JDBC {@link DataSource}.
	 */
	private final DataSource dataSource;

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
	 * Construct with the given JDBC {@link DataSource}.
	 * 
	 * @param dataSource
	 *            JDBC {@link DataSource}
	 * @param defFileDoc
	 *            JDom document for the DAO definitions XML file e.g.
	 *            daodefs.xml.
	 * @param velocityGenerator
	 *            {@link VelocityGenerator} used to generate the definitions
	 *            file.
	 * @throws GeneratorException
	 *             possible error deserializing the definitions file
	 */
	public SpringJunitGenerator(DataSource dataSource, Document defFileDoc,
	    VelocityGenerator velocityGenerator) throws GeneratorException {

		this.dataSource = dataSource;

		this.defFileDoc = defFileDoc;

		this.velocityGenerator = velocityGenerator;

	}

	/**
	 * 
	 * @param fieldMetaDataList
	 *            {@link List} of {@link SelectMetaData} objects.
	 * @return {@link List} of {@link DtoMethodMetaData} objects.
	 * @throws SQLException
	 *             possible SQL error
	 */
	private List<DtoMethodMetaData> getDtoMethodMetaDataList(
	    final List<SelectMetaData> fieldMetaDataList) throws SQLException {

		List<DtoMethodMetaData> list = new ArrayList<DtoMethodMetaData>();

		for (SelectMetaData selectMetaData : fieldMetaDataList) {

			@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
			DtoMethodMetaData dtoMethodMetaData = new DtoMethodMetaData();

			String camelCaseName =
			    TextUtils.convertToCamelCase(selectMetaData.getColumnName());

			// Use field name formatted as camelCase for variable name
			dtoMethodMetaData.setVarName(camelCaseName);

			// Store fully qualified class name
			dtoMethodMetaData
			    .setClassTypeName(selectMetaData.getColumnClassName());

			// Use camelCase field name with first character as upper case for
			// method name
			dtoMethodMetaData
			    .setMethodName(camelCaseName.substring(0, 1).toUpperCase()
			        + camelCaseName.substring(1));

			dtoMethodMetaData.setColumnMetaData(selectMetaData);

			list.add(dtoMethodMetaData);
		}

		return list;

	}

	/**
	 * 
	 * @param dtoMethodMetaDataList
	 *            {@link List} of {@link DtoMethodMetaData} objects
	 * @return {@link Map} containing class type information
	 */
	private Map<String, String> normalizeImports(
	    final List<DtoMethodMetaData> dtoMethodMetaDataList) {

		// Ordered map of package names
		@SuppressWarnings("PMD.UseConcurrentHashMap")
		Map<String, String> packageMap = new TreeMap<String, String>();

		// Add Serializable as import
		packageMap.put("java.io.Serializable", "java.io.Serializable");

		// Build ordered list of unique package names
		for (DtoMethodMetaData dtoMethodMetaData : dtoMethodMetaDataList) {
			// Do not include java.lang or existing packages
			if (!(dtoMethodMetaData.getClassTypeName().startsWith("java.lang"))
			    && !packageMap
			        .containsKey(dtoMethodMetaData.getClassTypeName())) {
				packageMap.put(dtoMethodMetaData.getClassTypeName(),
				    dtoMethodMetaData.getClassTypeName());
			}
		}
		return packageMap;
	}

	/**
	 * Create DAO Junit test classes.
	 * 
	 * @throws GeneratorException
	 *             possible error generating the JUnit classes
	 */
	@Override
	public void generate() throws GeneratorException {

		VelocityContext context = new VelocityContext();
		Random random = new Random();

		Element defFileRoot = defFileDoc.getRootElement();
		String propsFile = defFileRoot.getChildTextTrim("props-file");
		String junitTemplate = defFileRoot.getChildTextTrim("junit-template");

		try {
			List<DaoCfgMetaData> daoCfgMetaDataList =
			    daoCfgMetaDataQuery.fetchMetaData(defFileRoot);

			for (DaoCfgMetaData daoCfgMetaData : daoCfgMetaDataList) {

				DtoClassMetaData dtoClassMetaData =
				    daoCfgMetaData.getDtoClassMetaData();

				DbMetaDataQueryParams metaDataParams =
				    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
				        .withDataSource(dataSource)
				        .withSelectSql(dtoClassMetaData.getSelectSql()).build();

				List<SelectMetaData> fieldMetaDataList =
				    sqlSelectDataQuery.fetchMetaData(metaDataParams);

				// Set field metadata list
				dtoClassMetaData.setColMetaDataList(fieldMetaDataList);

				// Set DTO method metadata list
				List<DtoMethodMetaData> methodMetaDataList =
				    getDtoMethodMetaDataList(fieldMetaDataList);
				dtoClassMetaData.setDtoMethodMetaDataList(methodMetaDataList);

				// Set package map
				dtoClassMetaData.setPackageMap(normalizeImports(
				    dtoClassMetaData.getDtoMethodMetaDataList()));

				// Generate random Serial Version UID
				long serialVersionUID = random.nextLong();

				// Fetch the primary key metadata:
				String tableName = daoCfgMetaData.getTableName();

				if (!StringUtils.isEmpty(tableName)) {
					DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
					    .metaDataQueryParams().withDataSource(dataSource)
					    .withSchemaPattern(daoCfgMetaData.getSchema())
					    .withTableNames(tableName).build();

					Map<Integer, PKMetaData> pkMetaData =
					    pKMetaDataQuery.fetchMetaData(params);

					Map<String, List<IdxMetaData>> idxMetaData =
					    idxMetaDataQuery.fetchMetaData(params);

					context.put("pkMetaData", pkMetaData);
					context.put("indexMetaData", idxMetaData);
				} else {
					context.put("statementMap",
					    daoCfgMetaData.getStatementMap());
				}

				context.put("daoCfgMetaData", daoCfgMetaData);
				context.put("propsFile", propsFile);
				context.put("serialVersionUID", serialVersionUID);
				context.put("lineSeparator",
				    System.getProperty("line.separator"));

				// Build fully qualified output name
				String classname = daoCfgMetaData.getClassName();
				String junitClassname =
				    FilenameUtils.getBaseName(classname) + "Test";
				String outputName =
				    TextUtils.buildFileName(daoCfgMetaData.getTestOutputDir(),
				        daoCfgMetaData.getTestPackageName(), junitClassname);
				LOGGER
				    .info("Generating " + FilenameUtils.normalize(outputName));

				velocityGenerator.generate(outputName, junitTemplate, context);
			}

		} catch (JDOMException | IOException | SQLException e) {
			String msg = String.format(
			    "*** Exception caught while " + "generating Junit classes: %s",
			    e);
			LOGGER.error(msg, e);
			throw new GeneratorException(msg, e);
		}
	}

}
