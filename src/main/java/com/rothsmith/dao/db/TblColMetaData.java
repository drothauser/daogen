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
 * Column metadata.
 * 
 * Description of table columns available in the specified catalog.
 * 
 * <P>
 * Only column descriptions matching the catalog, schema, table and column name
 * criteria are returned. They are ordered by <code>TABLE_CAT</code>,
 * <code>TABLE_SCHEM</code>, <code>TABLE_NAME</code>, and
 * <code>ORDINAL_POSITION</code>.
 * 
 * <P>
 * Each column description has the following columns:
 * <OL>
 * <LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
 * <LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
 * <LI><B>TABLE_NAME</B> String => table name
 * <LI><B>COLUMN_NAME</B> String => column name
 * <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
 * <LI><B>TYPE_NAME</B> String => Data source dependent type name, for a UDT the
 * type name is fully qualified
 * <LI><B>COLUMN_SIZE</B> int => column size.
 * <LI><B>BUFFER_LENGTH</B> is not used.
 * <LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits. Null is
 * returned for data types where DECIMAL_DIGITS is not applicable.
 * <LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
 * <LI><B>NULLABLE</B> int => is NULL allowed.
 * <UL>
 * <LI>columnNoNulls - might not allow <code>NULL</code> values
 * <LI>columnNullable - definitely allows <code>NULL</code> values
 * <LI>columnNullableUnknown - nullability unknown
 * </UL>
 * <LI><B>REMARKS</B> String => comment describing column (may be
 * <code>null</code>)
 * <LI><B>COLUMN_DEF</B> String => default value for the column, which should be
 * interpreted as a string when the value is enclosed in single quotes (may be
 * <code>null</code>)
 * <LI><B>SQL_DATA_TYPE</B> int => unused
 * <LI><B>SQL_DATETIME_SUB</B> int => unused
 * <LI><B>CHAR_OCTET_LENGTH</B> int => for char types the maximum number of
 * bytes in the column
 * <LI><B>ORDINAL_POSITION</B> int => index of column in table (starting at 1)
 * <LI><B>IS_NULLABLE</B> String => ISO rules are used to determine the
 * nullability for a column.
 * <UL>
 * <LI>YES --- if the parameter can include NULLs
 * <LI>NO --- if the parameter cannot include NULLs
 * <LI>empty string --- if the nullability for the parameter is unknown
 * </UL>
 * <LI><B>SCOPE_CATLOG</B> String => catalog of table that is the scope of a
 * reference attribute (<code>null</code> if DATA_TYPE isn't REF)
 * <LI><B>SCOPE_SCHEMA</B> String => schema of table that is the scope of a
 * reference attribute (<code>null</code> if the DATA_TYPE isn't REF)
 * <LI><B>SCOPE_TABLE</B> String => table name that this the scope of a
 * reference attribure (<code>null</code> if the DATA_TYPE isn't REF)
 * <LI><B>SOURCE_DATA_TYPE</B> short => source type of a distinct type or
 * user-generated Ref type, SQL type from java.sql.Types (<code>null</code> if
 * DATA_TYPE isn't DISTINCT or user-generated REF)
 * <LI><B>IS_AUTOINCREMENT</B> String => Indicates whether this column is auto
 * incremented
 * <UL>
 * <LI>YES --- if the column is auto incremented
 * <LI>NO --- if the column is not auto incremented
 * <LI>empty string --- if it cannot be determined whether the column is auto
 * incremented parameter is unknown
 * </UL>
 * </OL>
 * 
 * <p>
 * The COLUMN_SIZE column the specified column size for the given column. For
 * numeric data, this is the maximum precision. For character data, this is the
 * length in characters. For datetime datatypes, this is the length in
 * characters of the String representation (assuming the maximum allowed
 * precision of the fractional seconds component). For binary data, this is the
 * length in bytes. For the ROWID datatype, this is the length in bytes. Null is
 * returned for data types where the column size is not applicable.
 * </p>
 * 
 * @author drothauser
 */
// CHECKSTYLE:OFF Excessive method count (>35) ok in this class
@SuppressWarnings({ "PMD.TooManyFields", "PMD.ExcessivePublicCount" })
public final class TblColMetaData implements Serializable {
	// CHECKSTYLE:ON

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -1290461557046479032L;

	/**
	 * table catalog.
	 */
	private String tableCat;

	/**
	 * table schema.
	 */
	private String tableSchem;

	/**
	 * table name.
	 */
	private String tableName;

	/**
	 * Column name.
	 */
	private String columnName;

	/**
	 * Field used in a JDBC PreparedStatement. This is generally the same as
	 * {@link #columnName} except for cases where the column name is a Java
	 * reserved word e.g. &quot;class&quot;.
	 */
	private String javaColumnName;

	/**
	 * SQL type from java.sql.Types.
	 */
	private Integer dataType;

	/**
	 * Data source dependent type name, for a UDT the type name is fully
	 * qualified.
	 */
	private String typeName;

	/**
	 * column size.
	 */
	private Integer columnSize;

	/**
	 * <B>BUFFER_LENGTH</B> is not used.
	 */
	private Integer bufferLength;

	/**
	 * The number of fractional digits. Null is returned for data types where
	 * DECIMAL_DIGITS is not applicable.
	 */
	private Integer decimalDigits;

	/**
	 * Radix (typically either 10 or 2).
	 */
	private Integer numPrecRadix;

	/**
	 * ISO rules are used to determine the nullability for a column.
	 */
	private Integer nullable;

	/**
	 * Comment describing column (may be <code>null</code>).
	 */
	private String remarks;

	/**
	 * Default value for the column, which should be interpreted as a string
	 * when the value is enclosed in single quotes (may be <code>null</code>).
	 * 
	 * columnDefault -&gt; COLUMN_DEF didn't map corrector using
	 * GenerousBeanProcessor.
	 */
	private String columnDefault;

	/**
	 * SQL data type.
	 */
	private Integer sqlDataType;

	/**
	 * SQL Date time sub.
	 */
	private Integer sqlDatetimeSub;

	/**
	 * For char types, the maximum number of bytes in the column.
	 */
	private Integer charOctetLength;

	/**
	 * index of column in table (starting at 1).
	 */
	private Integer ordinalPosition;

	/**
	 * ISO rules are used to determine the nullability for a column.
	 * <UL>
	 * <LI>YES --- if the parameter can include NULLs
	 * <LI>NO --- if the parameter cannot include NULLs
	 * <LI>empty string --- if the nullability for the parameter is unknown
	 * </UL>
	 */
	private String isNullable;

	/**
	 * catalog of table that is the scope of a reference attribute (
	 * <code>null</code> if DATA_TYPE isn't REF).
	 */
	private String scopeCatalog;

	/**
	 * schema of table that is the scope of a reference attribute (
	 * <code>null</code> if the DATA_TYPE isn't REF).
	 */
	private String scopeSchema;

	/**
	 * table name that this scope of a reference attribute (<code>null</code> if
	 * the DATA_TYPE isn't REF).
	 */
	private String scopeTable;

	/**
	 * source type of a distinct type or user-generated Ref type, SQL type from
	 * java.sql.Types (<code>null</code> if DATA_TYPE isn't DISTINCT or
	 * user-generated REF).
	 */
	private Integer sourceDataType;

	/**
	 * Indicates whether this column is auto incremented.
	 * <UL>
	 * <LI>YES --- if the column is auto incremented
	 * <LI>NO --- if the column is not auto incremented
	 * <LI>empty string --- if it cannot be determined whether the column is
	 * auto incremented parameter is unknown
	 * </UL>
	 */
	private String isAutoIncrement;

	/**
	 * 
	 * @return the buffer length.
	 */
	public Integer getBufferLength() {
		return bufferLength;
	}

	/**
	 * 
	 * @param bufferLength
	 *            the buffer length to set.
	 */
	public void setBufferLength(final Integer bufferLength) {
		this.bufferLength = bufferLength;
	}

	/**
	 * For char types, the maximum number of bytes in the column.
	 * 
	 * @return the maximum number of bytes in the column.
	 */
	public Integer getCharOctetLength() {
		return charOctetLength;
	}

	/**
	 * For char types, the maximum number of bytes in the column.
	 * 
	 * @param charOctetLength
	 *            the maximum number of bytes in the column to set.
	 */
	public void setCharOctetLength(final Integer charOctetLength) {
		this.charOctetLength = charOctetLength;
	}

	/**
	 * 
	 * @return the default value for the column, which should be interpreted as
	 *         a string when the value is enclosed in single quotes (may be
	 *         <code>null</code>).
	 */
	public String getColumnDefault() {
		return columnDefault;
	}

	/**
	 * 
	 * @param columnDef
	 *            set the default value for the column, which should be
	 *            interpreted as a string when the value is enclosed in single
	 *            quotes (may be <code>null</code>).
	 */
	public void setColumnDefault(final String columnDef) {
		this.columnDefault = columnDef;
	}

	/**
	 * 
	 * @return the column name.
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * 
	 * @param columnName
	 *            the column name to set.
	 */
	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	/**
	 * 
	 * @return the column size.
	 */
	public Integer getColumnSize() {
		return columnSize;
	}

	/**
	 * 
	 * @param columnSize
	 *            the column size to set.
	 */
	public void setColumnSize(final Integer columnSize) {
		this.columnSize = columnSize;
	}

	/**
	 * 
	 * @return the SQL type from java.sql.Types.
	 */
	public Integer getDataType() {
		return dataType;
	}

	/**
	 * 
	 * @param dataType
	 *            the SQL type from java.sql.Types to set.
	 */
	public void setDataType(final Integer dataType) {
		this.dataType = dataType;
	}

	/**
	 * 
	 * @return The number of fractional digits. Null is returned for data types
	 *         where DECIMAL_DIGITS is not applicable.
	 */
	public Integer getDecimalDigits() {
		return decimalDigits;
	}

	/**
	 * Set the number of fractional digits. Null is returned for data types
	 * where DECIMAL_DIGITS is not applicable.
	 * 
	 * @param decimalDigits
	 *            the number of fractional digits.
	 */
	public void setDecimalDigits(final Integer decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	/**
	 * Indicates whether this column is auto incremented.
	 * <UL>
	 * <LI>YES --- if the column is auto incremented
	 * <LI>NO --- if the column is not auto incremented
	 * <LI>empty string --- if it cannot be determined whether the column is
	 * auto incremented parameter is unknown
	 * </UL>
	 * 
	 * @return the is auto increment flag.
	 */
	public String getIsAutoIncrement() {
		return isAutoIncrement;
	}

	/**
	 * Indicates whether this column is auto incremented.
	 * <UL>
	 * <LI>YES --- if the column is auto incremented
	 * <LI>NO --- if the column is not auto incremented
	 * <LI>empty string --- if it cannot be determined whether the column is
	 * auto incremented parameter is unknown
	 * </UL>
	 * 
	 * @param isAutoIncrement
	 *            the autoincrement flag to set.
	 */
	public void setIsAutoIncrement(final String isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	/**
	 * ISO rules are used to determine the nullability for a column.
	 * <UL>
	 * <LI>YES --- if the parameter can include NULLs
	 * <LI>NO --- if the parameter cannot include NULLs
	 * <LI>empty string --- if the nullability for the parameter is unknown
	 * </UL>
	 * 
	 * @return is nullable.
	 */
	public String getIsNullable() {
		return isNullable;
	}

	/**
	 * ISO rules are used to determine the nullability for a column.
	 * <UL>
	 * <LI>YES --- if the parameter can include NULLs
	 * <LI>NO --- if the parameter cannot include NULLs
	 * <LI>empty string --- if the nullability for the parameter is unknown
	 * </UL>
	 * 
	 * @param isNullable
	 *            the nullable flag to set.
	 */
	public void setIsNullable(final String isNullable) {
		this.isNullable = isNullable;
	}

	/**
	 * Get Nullable - ISO rules are used to determine the nullability for a
	 * column.
	 * 
	 * @return nullable.
	 */
	public Integer getNullable() {
		return nullable;
	}

	/**
	 * Set Nullable - ISO rules are used to determine the nullability for a
	 * column.
	 * 
	 * @param nullable
	 *            set nullable.
	 */
	public void setNullable(final Integer nullable) {
		this.nullable = nullable;
	}

	/**
	 * 
	 * @return the Radix (typically either 10 or 2).
	 */
	public Integer getNumPrecRadix() {
		return numPrecRadix;
	}

	/**
	 * 
	 * @param numPrecRadix
	 *            the Radix (typically either 10 or 2) to set.
	 */
	public void setNumPrecRadix(final Integer numPrecRadix) {
		this.numPrecRadix = numPrecRadix;
	}

	/**
	 * 
	 * @return the index of column in table (starting at 1).
	 */
	public Integer getOrdinalPosition() {
		return ordinalPosition;
	}

	/**
	 * 
	 * @param ordinalPosition
	 *            the index of column in table (starting at 1) to set.
	 */
	public void setOrdinalPosition(final Integer ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	/**
	 * 
	 * @return the comment describing column (may be <code>null</code>).
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 
	 * @param remarks
	 *            set the comment describing column (may be <code>null</code> ).
	 */
	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	/**
	 * Get the catalog of table that is the scope of a reference attribute (
	 * <code>null</code> if DATA_TYPE isn't REF).
	 * 
	 * @return scope catalog
	 */
	public String getScopeCatalog() {
		return scopeCatalog;
	}

	/**
	 * Set the catalog of table that is the scope of a reference attribute (
	 * <code>null</code> if DATA_TYPE isn't REF).
	 * 
	 * @param scopeCatalog
	 *            the scope catalog to set.
	 */
	public void setScopeCatalog(final String scopeCatalog) {
		this.scopeCatalog = scopeCatalog;
	}

	/**
	 * Get the schema of table that is the scope of a reference attribute (
	 * <code>null</code> if the DATA_TYPE isn't REF).
	 * 
	 * @return the scope schema
	 */
	public String getScopeSchema() {
		return scopeSchema;
	}

	/**
	 * Set the schema of table that is the scope of a reference attribute (
	 * <code>null</code> if the DATA_TYPE isn't REF).
	 * 
	 * @param scopeSchema
	 *            the scope schema to set
	 */
	public void setScopeSchema(final String scopeSchema) {
		this.scopeSchema = scopeSchema;
	}

	/**
	 * Get the table name that this scope of a reference attribute (
	 * <code>null</code> if the DATA_TYPE isn't REF).
	 * 
	 * @return the scope table.
	 */
	public String getScopeTable() {
		return scopeTable;
	}

	/**
	 * Set the table name that this scope of a reference attribute (
	 * <code>null</code> if the DATA_TYPE isn't REF).
	 * 
	 * @param scopeTable
	 *            the scope table to set.
	 */
	public void setScopeTable(final String scopeTable) {
		this.scopeTable = scopeTable;
	}

	/**
	 * Get the source type of a distinct type or user-generated Ref type, SQL
	 * type from java.sql.Types (<code>null</code> if DATA_TYPE isn't DISTINCT
	 * or user-generated REF).
	 * 
	 * @return the source data type
	 */
	public Integer getSourceDataType() {
		return sourceDataType;
	}

	/**
	 * Set the source type of a distinct type or user-generated Ref type, SQL
	 * type from java.sql.Types (<code>null</code> if DATA_TYPE isn't DISTINCT
	 * or user-generated REF).
	 * 
	 * @param sourceDataType
	 *            the source data type to set.
	 */
	public void setSourceDataType(final Integer sourceDataType) {
		this.sourceDataType = sourceDataType;
	}

	/**
	 * 
	 * @return the SQL data type.
	 */
	public Integer getSqlDataType() {
		return sqlDataType;
	}

	/**
	 * 
	 * @param sqlDataType
	 *            the SQL data type to set.
	 */
	public void setSqlDataType(final Integer sqlDataType) {
		this.sqlDataType = sqlDataType;
	}

	/**
	 * 
	 * @return the SQL Date time sub.
	 */
	public Integer getSqlDatetimeSub() {
		return sqlDatetimeSub;
	}

	/**
	 * 
	 * @param sqlDatetimeSub
	 *            the SQL Date time sub to set.
	 */
	public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
		this.sqlDatetimeSub = sqlDatetimeSub;
	}

	/**
	 * 
	 * @return the table catalog.
	 */
	public String getTableCat() {
		return tableCat;
	}

	/**
	 * 
	 * @param tableCat
	 *            the table catalog to set.
	 */
	public void setTableCat(final String tableCat) {
		this.tableCat = tableCat;
	}

	/**
	 * 
	 * @return the table name.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 
	 * @param tableName
	 *            the table name to set.
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 
	 * @return the table schema.
	 */
	public String getTableSchem() {
		return tableSchem;
	}

	/**
	 * 
	 * @param tableSchem
	 *            the table schema to set.
	 */
	public void setTableSchem(final String tableSchem) {
		this.tableSchem = tableSchem;
	}

	/**
	 * Get the type name: Data source dependent type name, for a UDT the type
	 * name is fully qualified.
	 * 
	 * @return the type name.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Set the type name: Data source dependent type name, for a UDT the type
	 * name is fully qualified.
	 * 
	 * @param typeName
	 *            the type name to set.
	 */
	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the field used in a JDBC PreparedStatement. This is generally the
	 *         same as {@link #columnName} except for cases where the column
	 *         name is a Java reserved word e.g. &quot;class&quot;.
	 */
	public String getJavaColumnName() {
		return javaColumnName;
	}

	/**
	 * Set the Java column name. This will be used in a JDBC PreparedStatement.
	 * This is generally the same as {@link #columnName} except for cases where
	 * the column name is a Java reserved word e.g. &quot;class&quot;.
	 * 
	 * @param javaColumnName
	 *            the Java column name
	 */
	public void setJavaColumnName(final String javaColumnName) {
		this.javaColumnName = javaColumnName;
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

		return new HashCodeBuilder().append(tableCat).append(tableSchem)
		    .append(tableName).append(columnName).append(javaColumnName)
		    .append(dataType).append(typeName).append(columnSize)
		    .append(bufferLength).append(decimalDigits).append(numPrecRadix)
		    .append(nullable).append(remarks).append(columnDefault)
		    .append(sqlDataType).append(sqlDatetimeSub).append(charOctetLength)
		    .append(ordinalPosition).append(isNullable).append(scopeCatalog)
		    .append(scopeSchema).append(scopeTable).append(sourceDataType)
		    .append(isAutoIncrement).toHashCode();
	}
}