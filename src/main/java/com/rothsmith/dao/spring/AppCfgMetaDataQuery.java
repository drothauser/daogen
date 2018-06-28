/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.spring;

import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.JDOMException;

/**
 * Class for reading the DAOs definitions file to retrieve the data necessary to
 * generate the Spring application configuration file.
 * 
 * @author drothauser
 */
public final class AppCfgMetaDataQuery {

	/**
	 * This method returns an {@link AppCfgMetaData} object from the data in the
	 * DAO definitions XML file.
	 * 
	 * @param daosRootElement
	 *            The first element of the DAO definitions XML file
	 *            (&lt;daos&gt;)
	 * @return {@link AppCfgMetaData} object
	 * @throws JDOMException
	 *             possible XML parsing error
	 * @throws IOException
	 *             possible IO error
	 */
	public AppCfgMetaData fetchMetaData(final Element daosRootElement)
	        throws JDOMException, IOException {

		AppCfgMetaData appCfgMetaData = new AppCfgMetaData();

		appCfgMetaData.setUser(daosRootElement.getChildTextTrim("user"));

		appCfgMetaData.setEmail(daosRootElement.getChildTextTrim("email"));

		appCfgMetaData.setTemplateFileName(
		    daosRootElement.getChildTextTrim("spring-app-ctx-template"));

		appCfgMetaData
		    .setOutputDir(daosRootElement.getChildTextTrim("output-dir"));

		appCfgMetaData
		    .setPropsFile(daosRootElement.getChildTextTrim("props-file"));

		return appCfgMetaData;
	}

}
