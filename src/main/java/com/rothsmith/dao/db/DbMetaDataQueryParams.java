/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.db;

import javax.sql.DataSource;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Parameter object for {@link DbMetaDataQuery} used to return database
 * metadata.
 * 
 * @author drothauser
 *
 */
public class DbMetaDataQueryParams {

	/**
	 * JDBC {@link DataSource}.
	 */
	private DataSource dataSource;

	/**
	 * A catalog name; must match the catalog name as it is stored in the
	 * database; "" retrieves those without a catalog; <code>null</code> means
	 * that the catalog name should not be used to narrow the search.
	 */
	private String catalog;

	/**
	 * A schema name pattern; must match the schema name as it is stored in the
	 * database; "" retrieves those without a schema; <code>null</code> means
	 * that the schema name should not be used to narrow the search.
	 */
	private String schemaPattern;

	// CHECKSTYLE:OFF @link statement doesn't wrap correctly
	/**
	 * List of table names. Separate each table name by a comma. See
	 * {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[])}
	 * for more information. Tables can be one of the following types: "TABLE",
	 * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
	 * "SYNONYM".
	 */
	// CHECKSTYLE:ON
	private String tableNames;

	/**
	 * A list of table types, which must be from the list of table types
	 * returned from
	 * {@link java.sql.DatabaseMetaData.DatabaseMetaData#getTableTypes} ,to
	 * include; <code>null</code> returns all types.
	 */
	private String types;

	/**
	 * Column name pattern.
	 */
	private String columnPattern;

	/**
	 * Select SQL statement.
	 */
	private String selectSql;

	/**
	 * Get the JDBC {@link DataSource}.
	 * 
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Set the JDBC {@link DataSource}.
	 * 
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Get the catalog name.
	 * 
	 * @return the catalog
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * Set the catalog name; must match the catalog name as it is stored in the
	 * database; "" retrieves those without a catalog; <code>null</code> means
	 * that the catalog name should not be used to narrow the search.
	 * 
	 * @param catalog
	 *            the catalog to set
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	/**
	 * Get the schema name pattern.
	 * 
	 * @return the schemaPattern
	 */
	public String getSchemaPattern() {
		return schemaPattern;
	}

	/**
	 * Set the schema name pattern; must match the schema name as it is stored
	 * in the database; "" retrieves those without a schema; <code>null</code>
	 * means that the schema name should not be used to narrow the search.
	 * 
	 * @param schemaPattern
	 *            the schemaPattern to set
	 */
	public void setSchemaPattern(String schemaPattern) {
		this.schemaPattern = schemaPattern;
	}

	/**
	 * @return the tableNames
	 */
	public String getTableNames() {
		return tableNames;
	}

	// CHECKSTYLE:OFF Javadoc comment doesn't wrap.
	/**
	 * Set the list of table names. Separate each table name by a comma. See
	 * {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[])}
	 * for more information. Tables can be one of the following types: "TABLE",
	 * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
	 * "SYNONYM".
	 * 
	 * @param tableNames
	 *            the tableNames to set
	 */
	// CHECKSTYLE:ON
	public void setTableNames(String tableNames) {
		this.tableNames = tableNames;
	}

	/**
	 * Get the list of table types.
	 * 
	 * @return the types
	 */
	public String getTypes() {
		return types;
	}

	/**
	 * Set the list of table types, which must be from the list of table types
	 * returned from {@link java.sql.DatabaseMetaData#getTableTypes} to include;
	 * <code>null</code> returns all types.
	 * 
	 * @param types
	 *            the types to set
	 */
	public void setTypes(String types) {
		this.types = types;
	}

	/**
	 * Get the column name pattern.
	 * 
	 * @return the columnPattern
	 */
	public String getColumnPattern() {
		return columnPattern;
	}

	/**
	 * Set the column name pattern.
	 * 
	 * @param columnPattern
	 *            the columnPattern to set
	 */
	public void setColumnPattern(String columnPattern) {
		this.columnPattern = columnPattern;
	}

	/**
	 * Get the select SQL statement.
	 * 
	 * @return the selectSql
	 */
	public String getSelectSql() {
		return selectSql;
	}

	/**
	 * Set the select SQL statement.
	 * 
	 * @param selectSql
	 *            the selectSql to set
	 */
	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
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
