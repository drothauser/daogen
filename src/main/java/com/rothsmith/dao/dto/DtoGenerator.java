/*
 * Copyright (c) 2009 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.dto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.VelocityContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.SelectDataQuery;
import com.rothsmith.dao.db.SelectMetaData;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.TextUtils;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Class to generate DTOs from the DAO definitions file.
 * 
 * @author drothauser
 */
@SuppressWarnings("PMD.UseConcurrentHashMap")
public final class DtoGenerator implements ArtifactGenerator {

	/**
	 * Logger for DtoGenerator.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(DtoGenerator.class);

	/**
	 * {@link SelectDataQuery} for getting column metadata from a SQL query.
	 */
	private final SelectDataQuery selectDataQuery = new SelectDataQuery();

	/**
	 * JDBC {@link DataSource}.
	 */
	private final DataSource dataSource;

	/**
	 * Definitions XML file as JDom {@link Document}.
	 */
	private final Document defFileDoc;

	/**
	 * {@link VelocityGenerator} for generating the DTO Java files.
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
	 *            {@link VelocityGenerator} for generating the DTO Java files.
	 * @throws GeneratorException
	 *             possible error deserializing the definitions file.
	 */
	public DtoGenerator(DataSource dataSource, Document defFileDoc,
	    VelocityGenerator velocityGenerator) throws GeneratorException {

		this.dataSource = dataSource;

		this.defFileDoc = defFileDoc;

		this.velocityGenerator = velocityGenerator;

	}

	/**
	 * Create DTO source DtoClassMetaData containing information about the class
	 * to generate.
	 * 
	 * @throws GeneratorException
	 *             possible generation error
	 */
	@Override
	public void generate() throws GeneratorException {

		Random random = new Random();
		VelocityContext context = new VelocityContext();

		Element root = defFileDoc.getRootElement();

		List<Element> daos = root.getChildren("dao");
		for (Element dao : daos) {

			try {
				DtoClassMetaData dtoClassMetaData = getDto(dao);

				DbMetaDataQueryParams dbMetaDataParams =
				    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
				        .withDataSource(dataSource)
				        .withSelectSql(dtoClassMetaData.getSelectSql()).build();

				List<SelectMetaData> selectMetaDataList =
				    selectDataQuery.fetchMetaData(dbMetaDataParams);

				dtoClassMetaData.setColMetaDataList(selectMetaDataList);

				List<DtoMethodMetaData> methodMetaDataList =
				    getDtoMethodMetaDataList(selectMetaDataList);
				dtoClassMetaData.setDtoMethodMetaDataList(methodMetaDataList);

				dtoClassMetaData.setPackageMap(normalizeImports(
				    dtoClassMetaData.getDtoMethodMetaDataList()));

				long serialVersionUID = random.nextLong();

				context.put("dtoClassMetaData", dtoClassMetaData);
				context.put("serialVersionUID", serialVersionUID);
				context.put("lineSeparator",
				    System.getProperty("line.separator"));

				String outputName =
				    TextUtils.buildFileName(dtoClassMetaData.getOutputDir(),
				        dtoClassMetaData.getPackageName(),
				        dtoClassMetaData.getClassName());
				LOGGER
				    .info("Generating " + FilenameUtils.normalize(outputName));

				velocityGenerator.generate(outputName,
				    dtoClassMetaData.getTemplateFileName(), context);

			} catch (JDOMException | SQLException e) {

				String msg = String
				    .format("*** Exception caught while generating DTO: %s", e);
				LOGGER.error(msg, e);
				throw new GeneratorException(msg, e);

			}

		}
	}

	/**
	 * This method builds a {@link DtoClassMetaData} object from a &lt;dao&gt
	 * element in the DAO definitions XML file.
	 * 
	 * @param daoElement
	 *            a &lt;dao&gt; {@link Element} inside the DAO configuration XML
	 *            file.
	 * 
	 * @return {@link DtoClassMetaData} object
	 * @throws JDOMException
	 *             possible XML parsing error
	 */
	private DtoClassMetaData getDto(final Element daoElement)
	        throws JDOMException {
		DtoClassMetaData dtoClassMetaData = null;

		Element dtoElement = daoElement.getChild("dto");

		if (dtoElement != null) {
			dtoClassMetaData = new DtoClassMetaData();

			dtoClassMetaData
			    .setPackageName(dtoElement.getAttributeValue("package"));
			dtoClassMetaData
			    .setClassName(dtoElement.getAttributeValue("classname"));
			dtoClassMetaData.setSelectSql(dtoElement.getTextTrim());
			dtoClassMetaData.setUser(
			    daoElement.getParentElement().getChildTextTrim("user"));
			dtoClassMetaData.setTemplateFileName(
			    daoElement.getParentElement().getChildTextTrim("dto-template"));
			dtoClassMetaData.setOutputDir(
			    daoElement.getParentElement().getChildTextTrim("output-dir"));
		}
		return dtoClassMetaData;
	}

	/**
	 * Get the metadata for generating getters and setters.
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
}
