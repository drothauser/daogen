/*
 * Copyright (c) 2014 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.spring;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Metadata used to generate the Spring application context XML file.
 * 
 * @author drothauser
 */
public final class AppCfgMetaData implements Serializable {

	/**
	 * Serial Version Uid.
	 */
	private static final long serialVersionUID = 9179300129579405055L;

	/**
	 * Velocity template file name.
	 */
	private String templateFileName;

	/**
	 * user name.
	 */
	private String user;

	/**
	 * user's email address.
	 */
	private String email;

	/**
	 * output directory.
	 */
	private String outputDir;

	/**
	 * DAO properties file containing DAO information e.g. connection
	 * parameters.
	 */
	private String propsFile;

	/**
	 * 
	 * @return user's email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 *            user's email address.
	 */
	public void setEmail(final String email) {
		this.email = email;
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
	 * @param outputDir
	 *            output directory.
	 */
	public void setOutputDir(final String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * 
	 * @return Velocity template file name.
	 */
	public String getTemplateFileName() {
		return templateFileName;
	}

	/**
	 * 
	 * @param templateFileName
	 *            Velocity template file name.
	 */
	public void setTemplateFileName(final String templateFileName) {
		this.templateFileName = templateFileName;
	}

	/**
	 * 
	 * @return user id.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * get the DAO properties file containing DAO information e.g. connection
	 * parameters.
	 * 
	 * @return the propsFile DAO properties file
	 */
	public String getPropsFile() {
		return propsFile;
	}

	/**
	 * Set the properties file containing DAO information e.g. connection
	 * parameters.
	 * 
	 * @param propsFile
	 *            the DAO properties file to set
	 */
	public void setPropsFile(String propsFile) {
		this.propsFile = propsFile;
	}

	/**
	 * 
	 * @param user
	 *            user id.
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

		return new HashCodeBuilder().append(templateFileName).append(user)
		    .append(email).append(outputDir).toHashCode();
	}

}