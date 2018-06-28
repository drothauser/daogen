/*
 * Copyright (c) 2013 Rothsmith, LLC, All rights reserved.
 */
package com.rothsmith.dao.db;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * SQL statement metadata from ResultSetMetaData for generating a DTO class.
 * 
 * @author drothauser
 */
// CHECKSTYLE:OFF Excessive method count (>35) ok in this class
@SuppressWarnings("PMD.TooManyFields")
public final class SelectMetaData implements Serializable {
	// CHECKSTYLE:ON

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1359211558777822392L;

	/**
	 * catalog name.
	 */
	private String catalogName;

	/**
	 * The fully-qualified name of the Java class whose instances are
	 * manufactured if the method <code>ResultSet.getObject</code> is called to
	 * retrieve a value from the column. <code>ResultSet.getObject</code> may
	 * return a subclass of the class returned by this method.
	 */
	private String columnClassName;

	/**
	 * The designated column's normal maximum width in characters.
	 */
	private Integer columnDisplaySize;

	/**
	 * The designated column's suggested title for use in printouts and
	 * displays. The suggested title is usually specified by the SQL
	 * <code>AS</code> clause. If a SQL <code>AS</code> is not specified, the
	 * value returned from <code>getColumnLabel</code> will be the same as the
	 * value returned by the <code>getColumnName</code> method.
	 */
	private String columnLabel;

	/**
	 * The designated column's name.
	 */
	private String columnName;

	/**
	 * The designated column's SQL type.
	 */
	private Integer columnType;

	/**
	 * The designated column's database-specific type name.
	 */
	private String columnTypeName;

	/**
	 * The designated column's specified column size. For numeric data, this is
	 * the maximum precision. For character data, this is the length in
	 * characters. For datetime datatypes, this is the length in characters of
	 * the String representation (assuming the maximum allowed precision of the
	 * fractional seconds component). For binary data, this is the length in
	 * bytes. For the ROWID datatype, this is the length in bytes. 0 is returned
	 * for data types where the column size is not applicable.
	 */
	private Integer precision;

	/**
	 * the designated column's number of digits to right of the decimal point. 0
	 * is returned for data types where the scale is not applicable.
	 */
	private Integer scale;

	/**
	 * The designated column's table's schema.
	 */
	private String schemaName;

	/**
	 * The designated column's table name.
	 */
	private String tableName;

	/**
	 * Indicates whether the designated column is automatically numbered.
	 */
	private Boolean autoIncrement;

	/**
	 * Indicates whether a column's case matters.
	 */
	private Boolean caseSensitive;

	/**
	 * Indicates whether the designated column is a cash value.
	 */
	private Boolean currency;

	/**
	 * Indicates whether a write on the designated column will definitely
	 * succeed.
	 */
	private Boolean definitelyWritable;

	/**
	 * Indicates the nullability of values in the designated column.
	 */
	private Integer nullable;

	/**
	 * Indicates whether the designated column is definitely not writable.
	 */
	private Boolean readOnly;

	/**
	 * Indicates whether the designated column can be used in a where clause.
	 */
	private Boolean searchable;

	/**
	 * Indicates whether values in the designated column are signed numbers.
	 */
	private Boolean signed;

	/**
	 * Indicates whether the designated column is definitely not writable.
	 */
	private Boolean writable;

	/**
	 * Get the flag that indicates whether the designated column is
	 * automatically numbered.
	 * 
	 * @return the auto-increment flag
	 */
	public Boolean getAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * Set the flat that indicates whether the designated column is
	 * automatically numbered.
	 * 
	 * @param autoIncrement
	 *            the auto-increment flag to set.
	 */
	public void setAutoIncrement(final Boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	/**
	 * Get that flag that indicates whether a column's case matters.
	 * 
	 * @return the case-sensitive flag
	 */
	public Boolean getCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * Set that flag that indicates whether a column's case matters.
	 * 
	 * @param caseSensitive
	 *            case-sensitive flag
	 */
	public void setCaseSensitive(final Boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * 
	 * @return the catalog name.
	 */
	public String getCatalogName() {
		return catalogName;
	}

	/**
	 * 
	 * @param catalogName
	 *            the catalog name to set.
	 */
	public void setCatalogName(final String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * Get the fully-qualified name of the Java class whose instances are
	 * manufactured if the method <code>ResultSet.getObject</code> is called to
	 * retrieve a value from the column. <code>ResultSet.getObject</code> may
	 * return a subclass of the class returned by this method.
	 * 
	 * @return column class name
	 */
	public String getColumnClassName() {
		return columnClassName;
	}

	/**
	 * Set the fully-qualified name of the Java class whose instances are
	 * manufactured if the method <code>ResultSet.getObject</code> is called to
	 * retrieve a value from the column. <code>ResultSet.getObject</code> may
	 * return a subclass of the class returned by this method.
	 * 
	 * @param columnClassName
	 *            the column class name to set
	 */
	public void setColumnClassName(final String columnClassName) {
		this.columnClassName = columnClassName;
	}

	/**
	 * 
	 * @return the designated column's normal maximum width in characters.
	 */
	public Integer getColumnDisplaySize() {
		return columnDisplaySize;
	}

	/**
	 * 
	 * @param columnDisplaySize
	 *            the designated column's normal maximum width in characters to
	 *            set.
	 */
	public void setColumnDisplaySize(final Integer columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}

	/**
	 * Gets the designated column's suggested title for use in printouts and
	 * displays. The suggested title is usually specified by the SQL
	 * <code>AS</code> clause. If a SQL <code>AS</code> is not specified, the
	 * value returned from <code>getColumnLabel</code> will be the same as the
	 * value returned by the <code>getColumnName</code> method.
	 * 
	 * @return the column label
	 */
	public String getColumnLabel() {
		return columnLabel;
	}

	/**
	 * Sets the designated column's suggested title for use in printouts and
	 * displays. The suggested title is usually specified by the SQL
	 * <code>AS</code> clause. If a SQL <code>AS</code> is not specified, the
	 * value returned from <code>getColumnLabel</code> will be the same as the
	 * value returned by the <code>getColumnName</code> method.
	 * 
	 * @param columnLabel
	 *            the column label to set
	 */
	public void setColumnLabel(final String columnLabel) {
		this.columnLabel = columnLabel;
	}

	/**
	 * 
	 * @return the designated column's name.
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * 
	 * @param columnName
	 *            the designated column's name to set.
	 */
	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	/**
	 * 
	 * @return the designated column's SQL type.
	 */
	public Integer getColumnType() {
		return columnType;
	}

	/**
	 * 
	 * @param columnType
	 *            the designated column's SQL type to set.
	 */
	public void setColumnType(final Integer columnType) {
		this.columnType = columnType;
	}

	/**
	 * 
	 * @return the designated column's database-specific type name.
	 */
	public String getColumnTypeName() {
		return columnTypeName;
	}

	/**
	 * 
	 * @param columnTypeName
	 *            the designated column's database-specific type name to set.
	 */
	public void setColumnTypeName(final String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	/**
	 * Get the flag that indicates whether the designated column is a cash
	 * value.
	 * 
	 * @return the currency flag
	 */
	public Boolean getCurrency() {
		return currency;
	}

	/**
	 * Set the flag that indicates whether the designated column is a cash
	 * value.
	 * 
	 * @param currency
	 *            the currency flag to set.
	 */
	public void setCurrency(final Boolean currency) {
		this.currency = currency;
	}

	/**
	 * Get the flag that indicates whether a write on the designated column will
	 * definitely succeed.
	 * 
	 * @return the definitelyWritable flag
	 */
	public Boolean getDefinitelyWritable() {
		return definitelyWritable;
	}

	/**
	 * Set the flag that indicates whether a write on the designated column will
	 * definitely succeed.
	 * 
	 * @param definitelyWritable
	 *            the definitelyWritable flag to set.
	 */
	public void setDefinitelyWritable(final Boolean definitelyWritable) {
		this.definitelyWritable = definitelyWritable;
	}

	/**
	 * Get the flag that indicates the nullability of values in the designated
	 * column.
	 * 
	 * @return the nullability flag
	 */
	public Integer getNullable() {
		return nullable;
	}

	/**
	 * Set the flag that indicates the nullability of values in the designated
	 * column.
	 * 
	 * @param nullable
	 *            the nullability flag to set
	 */
	public void setNullable(final Integer nullable) {
		this.nullable = nullable;
	}

	/**
	 * Get the designated column's specified column size. For numeric data, this
	 * is the maximum precision. For character data, this is the length in
	 * characters. For datetime datatypes, this is the length in characters of
	 * the String representation (assuming the maximum allowed precision of the
	 * fractional seconds component). For binary data, this is the length in
	 * bytes. For the ROWID datatype, this is the length in bytes. 0 is returned
	 * for data types where the column size is not applicable.
	 * 
	 * @return the precision
	 */
	public Integer getPrecision() {
		return precision;
	}

	/**
	 * Set the designated column's specified column size. For numeric data, this
	 * is the maximum precision. For character data, this is the length in
	 * characters. For datetime datatypes, this is the length in characters of
	 * the String representation (assuming the maximum allowed precision of the
	 * fractional seconds component). For binary data, this is the length in
	 * bytes. For the ROWID datatype, this is the length in bytes. 0 is returned
	 * for data types where the column size is not applicable.
	 * 
	 * @param precision
	 *            the precision to set
	 */
	public void setPrecision(final Integer precision) {
		this.precision = precision;
	}

	/**
	 * 
	 * @return the flag that indicates whether the designated column is
	 *         definitely not writable.
	 */
	public Boolean getReadOnly() {
		return readOnly;
	}

	/**
	 * 
	 * @param readOnly
	 *            the flag that indicates whether the designated column is
	 *            definitely not writable.
	 */
	public void setReadOnly(final Boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Gets the designated column's number of digits to right of the decimal
	 * point. 0 is returned for data types where the scale is not applicable.
	 * 
	 * @return the scale
	 */
	public Integer getScale() {
		return scale;
	}

	/**
	 * Sets the designated column's number of digits to right of the decimal
	 * point. 0 is returned for data types where the scale is not applicable.
	 * 
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(final Integer scale) {
		this.scale = scale;
	}

	/**
	 * 
	 * @return the designated column's table's schema.
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * 
	 * @param schemaName
	 *            the designated column's table's schema to set.
	 */
	public void setSchemaName(final String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * 
	 * @return the flag that indicates whether the designated column can be used
	 *         in a where clause.
	 */
	public Boolean getSearchable() {
		return searchable;
	}

	/**
	 * 
	 * @param searchable
	 *            the flag that indicates whether the designated column can be
	 *            used in a where clause.
	 */
	public void setSearchable(final Boolean searchable) {
		this.searchable = searchable;
	}

	/**
	 * 
	 * @return the flag that indicates whether values in the designated column
	 *         are signed numbers.
	 */
	public Boolean getSigned() {
		return signed;
	}

	/**
	 * 
	 * @param signed
	 *            the flag that indicates whether values in the designated
	 *            column are signed numbers.
	 */
	public void setSigned(final Boolean signed) {
		this.signed = signed;
	}

	/**
	 * 
	 * @return the designated column's table name.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 
	 * @param tableName
	 *            the designated column's table name to set.
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Get the boolean that indicates whether the designated column is
	 * definitely not writable.
	 * 
	 * @return writable flag
	 */
	public Boolean getWritable() {
		return writable;
	}

	/**
	 * Set the boolean that indicates whether the designated column is
	 * definitely not writable.
	 * 
	 * @param writable
	 *            the writable flag to set.
	 */
	public void setWritable(final Boolean writable) {
		this.writable = writable;
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

		return new HashCodeBuilder().append(catalogName).append(columnClassName)
		    .append(columnDisplaySize).append(columnLabel).append(columnName)
		    .append(columnType).append(columnTypeName).append(precision)
		    .append(scale).append(schemaName).append(tableName)
		    .append(autoIncrement).append(caseSensitive).append(currency)
		    .append(definitelyWritable).append(nullable).append(readOnly)
		    .append(searchable).append(signed).append(writable).toHashCode();
	}

}