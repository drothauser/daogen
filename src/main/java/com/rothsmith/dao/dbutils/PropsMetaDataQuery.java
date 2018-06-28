/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.dbutils;

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
public final class PropsMetaDataQuery {

	/**
	 * This method builds a list of{@link PropsMetaData} objects from the
	 * &lt;dao&gt element in the DAO configuration XML file.
	 * 
	 * @param defFileDoc
	 *            The first element of the DAO definitions XML file
	 *            (&lt;daos&gt;)
	 * @return {@link List} of {@link PropsMetaData} objects
	 * @throws JDOMException
	 *             possible XML parsing error
	 * @throws IOException
	 *             possible IO error.
	 */
	@SuppressWarnings({ "PMD.AvoidInstantiatingObjectsInLoops",
	    "PMD.UseConcurrentHashMap" })
	public List<PropsMetaData> fetchMetaData(final Element defFileDoc)
	        throws JDOMException, IOException {

		// Get list of all DAOs
		List<Element> daos = defFileDoc.getChildren("dao");

		// List to hold DAO metadata objects
		List<PropsMetaData> propsMetaDataList = new ArrayList<PropsMetaData>();

		List<Element> statements = null;

		// Process all DAOs
		for (Element dao : daos) {

			PropsMetaData propsMetaData = new PropsMetaData();
			propsMetaData.setPackageName(dao.getAttributeValue("package"));
			propsMetaData.setClassName(dao.getAttributeValue("classname"));
			propsMetaData.setUser(defFileDoc.getChildTextTrim("user"));
			propsMetaData.setEmail(defFileDoc.getChildTextTrim("email"));
			propsMetaData.setJndiName(defFileDoc.getChildTextTrim("jndi-name"));
			propsMetaData.setOutputDir(FilenameUtils
			    .normalize(defFileDoc.getChildTextTrim("output-dir")));
			propsMetaData.setTestOutputDir(FilenameUtils
			    .normalize(defFileDoc.getChildTextTrim("test-output-dir")));
			propsMetaData.setTestPackageName(
			    defFileDoc.getChildTextTrim("test-package-name"));
			propsMetaData.setTableName(dao.getChildTextTrim("table"));
			propsMetaData.setSchema(dao.getChildTextTrim("schema"));

			// Get DTO or set to null if not defined
			propsMetaData.setDtoClassMetaData(getDto(dao));

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
			propsMetaData.setStatementMap(statementMap);

			// Add DAO metadata to list
			propsMetaDataList.add(propsMetaData);
		}

		return propsMetaDataList;
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
