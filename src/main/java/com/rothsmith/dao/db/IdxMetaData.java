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
 * Table Index Metadata DTO.
 * 
 * @author drothauser
 */
public final class IdxMetaData implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 4908420413432208716L;

	/**
	 * table catalog
	 */
	private String tableCat;

	/**
	 * table schema
	 */
	private String tableSchem;

	/**
	 * table name
	 */
	private String tableName;

	/**
	 * Can index values be non-unique. false when TYPE is tableIndexStatistic
	 */
	private Boolean nonUnique;

	/**
	 * index catalog (may be null); null when TYPE is tableIndexStatistic
	 */
	private String indexQualifier;

	/**
	 * index name; null when TYPE is tableIndexStatistic.
	 */
	private String indexName;

	/**
	 * index type:
	 * <ul>
	 * <li>tableIndexStatistic - this identifies table statistics that are
	 * returned in conjuction with a table's index descriptions
	 * <li>tableIndexClustered - this is a clustered index
	 * <li>tableIndexHashed - this is a hashed index
	 * <li>tableIndexOther - this is some other style of index
	 * </ul>
	 * .
	 */
	private Integer type;

	/**
	 * column sequence number within index; zero when TYPE is
	 * tableIndexStatistic.
	 */
	private Integer ordinalPosition;

	/**
	 * column name; null when TYPE is tableIndexStatistic.
	 */
	private String columnName;

	/**
	 * column sort sequence, "A" => ascending, "D" => descending, may be null if
	 * sort sequence is not supported; null when TYPE is tableIndexStatistic.
	 */
	private String ascOrDesc;

	/**
	 * When TYPE is tableIndexStatisic then this is the number of pages used for
	 * the table, otherwise it is the number of pages used for the current
	 * index.
	 */
	private Integer pages;

	/**
	 * Filter condition, if any. (may be null)
	 */
	private String filterCondition;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
		    ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * @return the tableCat
	 */
	public String getTableCat() {
		return tableCat;
	}

	/**
	 * @param tableCat
	 *            the tableCat to set
	 */
	public void setTableCat(final String tableCat) {
		this.tableCat = tableCat;
	}

	/**
	 * @return the tableSchem
	 */
	public String getTableSchem() {
		return tableSchem;
	}

	/**
	 * @param tableSchem
	 *            the tableSchem to set
	 */
	public void setTableSchem(final String tableSchem) {
		this.tableSchem = tableSchem;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the nonUnique
	 */
	public Boolean isNonUnique() {
		return nonUnique;
	}

	/**
	 * @param nonUnique
	 *            the nonUnique to set
	 */
	public void setNonUnique(final Boolean nonUnique) {
		this.nonUnique = nonUnique;
	}

	/**
	 * @return the indexQualifier
	 */
	public String getIndexQualifier() {
		return indexQualifier;
	}

	/**
	 * @param indexQualifier
	 *            the indexQualifier to set
	 */
	public void setIndexQualifier(final String indexQualifier) {
		this.indexQualifier = indexQualifier;
	}

	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * @param indexName
	 *            the indexName to set
	 */
	public void setIndexName(final String indexName) {
		this.indexName = indexName;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final Integer type) {
		this.type = type;
	}

	/**
	 * @return the ordinalPosition
	 */
	public Integer getOrdinalPosition() {
		return ordinalPosition;
	}

	/**
	 * @param ordinalPosition
	 *            the ordinalPosition to set
	 */
	public void setOrdinalPosition(final Integer ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            the columnName to set
	 */
	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the ascOrDesc
	 */
	public String getAscOrDesc() {
		return ascOrDesc;
	}

	/**
	 * @param ascOrDesc
	 *            the ascOrDesc to set
	 */
	public void setAscOrDesc(final String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}

	/**
	 * @return the pages
	 */
	public Integer getPages() {
		return pages;
	}

	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(final Integer pages) {
		this.pages = pages;
	}

	/**
	 * @return the filterCondition
	 */
	public String getFilterCondition() {
		return filterCondition;
	}

	/**
	 * @param filterCondition
	 *            the filterCondition to set
	 */
	public void setFilterCondition(final String filterCondition) {
		this.filterCondition = filterCondition;
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

		return new HashCodeBuilder().append(tableCat).append(tableSchem)
		    .append(tableName).append(nonUnique).append(indexQualifier)
		    .append(indexName).append(type).append(ordinalPosition)
		    .append(columnName).append(ascOrDesc).append(pages)
		    .append(filterCondition).toHashCode();
	}

}