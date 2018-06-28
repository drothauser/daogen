/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.dbutils;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.rothsmith.dao.dto.DtoClassMetaData;

/**
 * Metadata for Spring DAO bean generation.
 * 
 * 
 * @author drothauser
 */
public final class PropsMetaData implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -6960329325689556163L;

	/**
	 * DAO class name.
	 */
	private String className;

	/**
	 * {@link DtoClassMetaData} object.
	 */
	private DtoClassMetaData dtoClassMetaData;

	/**
	 * User's email address.
	 */
	private String email;

	/**
	 * Output directory.
	 */
	private String outputDir;

	/**
	 * JNDI name to obtain a JDBC DataSource.
	 */
	private String jndiName;

	/**
	 * Java package for the DAO.
	 */
	private String packageName;

	/**
	 * Schema.
	 */
	private String schema;

	/**
	 * SQL statement {@link Map}.
	 */
	private Map<String, String> statementMap;

	/**
	 * Table name.
	 */
	private String tableName;

	/**
	 * Output directory for test packages (e.g. &quot;test&quot;).
	 */
	private String testOutputDir;

	/**
	 * Test package name
	 */
	private String testPackageName;

	/**
	 * User name.
	 */
	private String user;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * 
	 * @return class name.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 
	 * @return {@link DtoClassMetaData} object.
	 */
	public DtoClassMetaData getDtoClassMetaData() {
		return dtoClassMetaData;
	}

	/**
	 * 
	 * @return email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the JNDI name to obtain a JDBC DataSource.
	 */
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * 
	 * @return output directory.
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * 
	 * @return Java package name.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * 
	 * @return SQL statement {@link Map}.
	 */
	public Map<String, String> getStatementMap() {
		return statementMap;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return the output directory for test packages (e.g. &quot;test&quot;).
	 */
	public String getTestOutputDir() {
		return testOutputDir;
	}

	/**
	 * @return the test package name
	 */
	public String getTestPackageName() {
		return testPackageName;
	}

	/**
	 * 
	 * @return user name.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * 
	 * @param className
	 *            class name.
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * 
	 * @param dtoClassMetaData
	 *            {@link DtoClassMetaData} object.
	 */
	public void setDtoClassMetaData(final DtoClassMetaData dtoClassMetaData) {
		this.dtoClassMetaData = dtoClassMetaData;
	}

	/**
	 * 
	 * @param email
	 *            email address.
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @param jndiName
	 *            the JNDI name to obtain a JDBC DataSource to set
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * 
	 * @param outputDir
	 *            output directory.
	 */
	public void setOutputDir(final String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * 
	 * @param packageName
	 *            Java package name.
	 */
	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * 
	 * @param statementMap
	 *            SQL statement {@link Map}.
	 */
	public void setStatementMap(final Map<String, String> statementMap) {
		this.statementMap = statementMap;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @param testOutputDir
	 *            the Output directory for test packages (e.g.
	 *            &quot;test&quot;).
	 */
	public void setTestOutputDir(final String testOutputDir) {
		this.testOutputDir = testOutputDir;
	}

	/**
	 * @param testPackageName
	 *            the test package name to set
	 */
	public void setTestPackageName(final String testPackageName) {
		this.testPackageName = testPackageName;
	}

	/**
	 * 
	 * @param user
	 *            user name.
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
}