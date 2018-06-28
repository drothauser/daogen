/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.rothsmith.dao.dto.DtoClassMetaData;

/**
 * Class for reading the DAOs definitions xml file to retrieve the data
 * necessary to generate the Spring DAOs bean configuration file.
 * 
 * @author drothauser
 */
public final class DaoCfgMetaDataQuery {

	/**
	 * This method builds a list of{@link DaoCfgMetaData} objects from the
	 * &lt;dao&gt element in the DAO configuration XML file.
	 * 
	 * @param defFileDoc
	 *            The first element of the DAO definitions XML file
	 *            (&lt;daos&gt;)
	 * @return {@link List} of {@link DaoCfgMetaData} objects
	 * @throws JDOMException
	 *             possible XML parsing error
	 * @throws IOException
	 *             possible IO error.
	 */
	@SuppressWarnings({ "PMD.AvoidInstantiatingObjectsInLoops",
	    "PMD.UseConcurrentHashMap" })
	public List<DaoCfgMetaData> fetchMetaData(final Element defFileDoc)
	        throws JDOMException, IOException {

		// Get list of all DAOs
		List<Element> daos = defFileDoc.getChildren("dao");

		// List to hold DAO metadata objects
		List<DaoCfgMetaData> daoCfgMetaDataList =
		    new ArrayList<DaoCfgMetaData>();

		List<Element> statements = null;

		// Process all DAOs
		for (Element dao : daos) {

			DaoCfgMetaData daoCfgMetaData = new DaoCfgMetaData();
			daoCfgMetaData.setPackageName(dao.getAttributeValue("package"));
			daoCfgMetaData.setClassName(dao.getAttributeValue("classname"));
			daoCfgMetaData.setUser(defFileDoc.getChildTextTrim("user"));
			daoCfgMetaData.setEmail(defFileDoc.getChildTextTrim("email"));
			daoCfgMetaData.setTemplateFileName(
			    defFileDoc.getChildTextTrim("spring-dao-ctx-template"));
			daoCfgMetaData.setOutputDir(FilenameUtils
			    .normalize(defFileDoc.getChildTextTrim("output-dir")));
			daoCfgMetaData.setTestOutputDir(FilenameUtils
			    .normalize(defFileDoc.getChildTextTrim("test-output-dir")));
			daoCfgMetaData.setTestPackageName(
			    defFileDoc.getChildTextTrim("test-package-name"));
			daoCfgMetaData.setTableName(dao.getChildTextTrim("table"));
			daoCfgMetaData.setSchema(dao.getChildTextTrim("schema"));

			// Get DTO or set to null if not defined
			daoCfgMetaData.setDtoClassMetaData(getDto(dao));

			// Create statement map
			Map<String, String> statementMap = new HashMap<String, String>();

			// Get all statements
			statements = dao.getChild("sql").getChildren();

			// Process all statements
			for (Element sql : statements) {
				statementMap.put(sql.getAttributeValue("id"),
				    sql.getTextTrim());
			}

			// Add to statement map
			daoCfgMetaData.setStatementMap(statementMap);

			// Add DAO metadata to list
			daoCfgMetaDataList.add(daoCfgMetaData);
		}

		return daoCfgMetaDataList;
	}

	/**
	 * This method builds a {@link DtoClassMetaData} object from a &lt;dao&gt
	 * element in the DAO configuration XML file.
	 * 
	 * @param daoElement
	 *            a &lt;dao&gt; {@link Element} inside the DAO configuration XML
	 *            file.
	 * 
	 * @return {@link DtoClassMetaData} object
	 * @throws JDOMException
	 *             possible XML parsing error
	 */
	private DtoClassMetaData getDto(Element daoElement) throws JDOMException {
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
}
