/*
 * Copyright (c) 2009 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.db;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Primary key metadata.
 * 
 * 
 * @author drothauser
 */
public final class PKMetaData implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 9183688933344060006L;

	/**
	 * 
	 */
	private String tableCat;

	/**
	 * 
	 */
	private String tableSchem;

	/**
	 * 
	 */
	private String tableName;

	/**
	 * 
	 */
	private String columnName;

	/**
	 * 
	 */
	private Integer keySeq;

	/**
	 * 
	 */
	private String pkName;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @return the keySeq
	 */
	public Integer getKeySeq() {
		return keySeq;
	}

	/**
	 * @return the pkName
	 */
	public String getPkName() {
		return pkName;
	}

	/**
	 * @return the tableCat
	 */
	public String getTableCat() {
		return tableCat;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return the tableSchem
	 */
	public String getTableSchem() {
		return tableSchem;
	}

	/**
	 * @param columnName
	 *            the columnName to set
	 */
	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @param keySeq
	 *            the keySeq to set
	 */
	public void setKeySeq(final Integer keySeq) {
		this.keySeq = keySeq;
	}

	/**
	 * @param pkName
	 *            the pkName to set
	 */
	public void setPkName(final String pkName) {
		this.pkName = pkName;
	}

	/**
	 * @param tableCat
	 *            the tableCat to set
	 */
	public void setTableCat(final String tableCat) {
		this.tableCat = tableCat;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @param tableSchem
	 *            the tableSchem to set
	 */
	public void setTableSchem(final String tableSchem) {
		this.tableSchem = tableSchem;
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
	public int hashCode() {

		return new HashCodeBuilder().append(tableCat).append(tableSchem)
		    .append(tableName).append(columnName).append(keySeq).append(pkName)
		    .toHashCode();
	}

}