/*
 * Copyright (c) 2009 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.rothsmith.dao.db.SelectMetaData;

/**
 * Metadata for generating DTO getters and setters.
 * 
 * @author drothauser
 */
public final class DtoMethodMetaData implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -7848392782885619154L;

	/**
	 * Method name used after get and set e.g. &quot;Clientnum&quot; This field
	 * will generally be the same as varName but capitalized.
	 */
	private String methodName;

	/**
	 * name used in variables
	 */
	private String varName;

	/**
	 * Type of the variable.
	 */
	private String classTypeName;

	/**
	 * {@link SelectMetaData} object.
	 */
	private SelectMetaData selectMetaData;

	/**
	 * @return the {@link SelectMetaData} object.
	 */
	public SelectMetaData getColumnMetaData() {
		return selectMetaData;
	}

	/**
	 * @param selectMetaData
	 *            the {@link SelectMetaData} object to set.
	 */
	public void setColumnMetaData(final SelectMetaData selectMetaData) {
		this.selectMetaData = selectMetaData;
	}

	/**
	 * 
	 * @return Type of the variable.
	 */
	public String getClassTypeName() {
		return classTypeName;
	}

	/**
	 * 
	 * @param classTypeName
	 *            Type of the variable.
	 */
	public void setClassTypeName(final String classTypeName) {
		this.classTypeName = classTypeName;
	}

	/**
	 * 
	 * @return String used in getter and setter methods e.g.
	 *         &quot;Clientnum&quot; This field will generally be the same as
	 *         varName but capitalized.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * 
	 * @param methodName
	 *            String used in getter and setter methods e.g.
	 *            &quot;Clientnum&quot; This field will generally be the same as
	 *            varName but capitalized.
	 */
	public void setMethodName(final String methodName) {
		this.methodName = methodName;
	}

	/**
	 * 
	 * @return name used in variables.
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 * 
	 * @param varName
	 *            name used in variables.
	 */
	public void setVarName(final String varName) {
		this.varName = varName;
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