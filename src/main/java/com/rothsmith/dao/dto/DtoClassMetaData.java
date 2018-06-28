/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.rothsmith.dao.db.SelectMetaData;

/**
 * Metadata for data transfer object generation.
 * 
 * 
 * @author drothauser
 */
public final class DtoClassMetaData implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * Velocity template used to generate the DTO.
	 */
	private String templateFileName;

	/**
	 * The SQL statement from which the DTO is generated from.
	 */
	private String selectSql;

	/**
	 * The Java package for the DTO.
	 */
	private String packageName;

	/**
	 * The DTO class name.
	 */
	private String className;

	/**
	 * The user name used in the Javadoc.
	 */
	private String user;

	/**
	 * The output directory for the DTO.
	 */
	private String outputDir;

	/**
	 * A {@link List} of {@link SelectMetaData} objects.
	 */
	private List<SelectMetaData> colMetaDataList;

	/**
	 * A {@link List} of {@link DtoMethodMetaData} objects.
	 */
	private List<DtoMethodMetaData> dtoMethodMetaDataList;

	/**
	 * {@link Map} of package names.
	 */
	private Map<String, String> packageMap;

	/**
	 * 
	 * @return DTO class name.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 
	 * @param className
	 *            DTO class name.
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * 
	 * @return {@link List} of {@link DtoMethodMetaData} objects
	 */
	public List<DtoMethodMetaData> getDtoMethodMetaDataList() {
		return dtoMethodMetaDataList;
	}

	/**
	 * 
	 * @param dtoMethodMetaDataList
	 *            {@link List} of {@link DtoMethodMetaData} objects
	 */
	public void setDtoMethodMetaDataList(
	    final List<DtoMethodMetaData> dtoMethodMetaDataList) {
		this.dtoMethodMetaDataList = dtoMethodMetaDataList;
	}

	/**
	 * 
	 * @return {@link List} of {@link SelectMetaData} objects
	 */
	public List<SelectMetaData> getColMetaDataList() {
		return colMetaDataList;
	}

	/**
	 * 
	 * @param colMetaDataList
	 *            {@link List} of {@link SelectMetaData} objects
	 */
	public void setColMetaDataList(final List<SelectMetaData> colMetaDataList) {
		this.colMetaDataList = colMetaDataList;
	}

	/**
	 * 
	 * @return output directory e.g. &quot;src&quot;.
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * 
	 * @param outputDir
	 *            output directory e.g. &quot;src&quot;.
	 */
	public void setOutputDir(final String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * 
	 * @return {@link Map} of package names.
	 */
	public Map<String, String> getPackageMap() {
		return packageMap;
	}

	/**
	 * 
	 * @param packageMap
	 *            {@link Map} of package names.
	 */
	public void setPackageMap(final Map<String, String> packageMap) {
		this.packageMap = packageMap;
	}

	/**
	 * 
	 * @return DTO package name
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * 
	 * @param packageName
	 *            DTO package name
	 */
	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 
	 * @return The SQL statement from which the DTO is generated from.
	 */
	public String getSelectSql() {
		return selectSql;
	}

	/**
	 * 
	 * @param selectSql
	 *            The SQL statement from which the DTO is generated from.
	 */
	public void setSelectSql(final String selectSql) {
		this.selectSql = selectSql;
	}

	/**
	 * 
	 * @return The Velocity template file name.
	 */
	public String getTemplateFileName() {
		return templateFileName;
	}

	/**
	 * 
	 * @param templateFileName
	 *            The Velocity template file name.
	 */
	public void setTemplateFileName(final String templateFileName) {
		this.templateFileName = templateFileName;
	}

	/**
	 * 
	 * @return The user name used in the Javadoc.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            The user name used in the Javadoc.
	 */
	public void setUser(final String user) {
		this.user = user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
		    ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {

		return HashCodeBuilder.reflectionHashCode(this);

	}

}