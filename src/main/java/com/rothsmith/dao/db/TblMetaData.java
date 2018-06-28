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
 * Table metadata that contains information mapped from a
 * {@link java.sql.Connection#getMetaData()} operation.
 * 
 * @author drothauser
 */
public final class TblMetaData implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Table catalog (may be <code>null</code>).
	 */
	private String tableCat;

	/**
	 * Table schema (may be <code>null</code>).
	 */
	private String tableSchem;

	/**
	 * Table name.
	 */
	private String tableName;

	/**
	 * Table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL
	 * TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	 */
	private String tableType;

	/**
	 * Explanatory comment on the table.
	 */
	private String remarks;

	/**
	 * The types catalog (may be <code>null</code>).
	 */
	private String typeCat;

	/**
	 * The types schema.
	 */
	private String typeSchem;

	/**
	 * Name of the designated "identifier" column of a typed table (may be
	 * <code>null</code>).
	 */
	private String selfReferencingColName;

	/**
	 * Specifies how values in SELF_REFERENCING_COL_NAME are created. Values are
	 * "SYSTEM", "USER", "DERIVED" (may be <code>null</code>).
	 */
	private String refGeneration;

	/**
	 * 
	 * @return Values in SELF_REFERENCING_COL_NAME are created. Values are
	 *         "SYSTEM", "USER", "DERIVED" (may be <code>null</code>).
	 */
	public String getRefGeneration() {
		return refGeneration;
	}

	/**
	 * 
	 * @param refGeneration
	 *            Values in SELF_REFERENCING_COL_NAME are created. Values are
	 *            "SYSTEM", "USER", "DERIVED" (may be <code>null</code>).
	 */
	public void setRefGeneration(final String refGeneration) {
		this.refGeneration = refGeneration;
	}

	/**
	 * 
	 * @return Explanatory comment on the table.
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 
	 * @param remarks
	 *            Explanatory comment on the table.
	 */
	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 
	 * @return Name of the designated "identifier" column of a typed table (may
	 *         be <code>null</code>).
	 */
	public String getSelfReferencingColName() {
		return selfReferencingColName;
	}

	/**
	 * 
	 * @param selfReferencingColName
	 *            Name of the designated "identifier" column of a typed table
	 *            (may be <code>null</code>).
	 */
	public void setSelfReferencingColName(final String selfReferencingColName) {
		this.selfReferencingColName = selfReferencingColName;
	}

	/**
	 * 
	 * @return Table catalog (may be <code>null</code>).
	 */
	public String getTableCat() {
		return tableCat;
	}

	/**
	 * 
	 * @param tableCat
	 *            Table catalog (may be <code>null</code>).
	 */
	public void setTableCat(final String tableCat) {
		this.tableCat = tableCat;
	}

	/**
	 * 
	 * @return table name.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 
	 * @param tableName
	 *            table name.
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 
	 * @return table schema.
	 */
	public String getTableSchem() {
		return tableSchem;
	}

	/**
	 * 
	 * @param tableSchem
	 *            table schema.
	 */
	public void setTableSchem(final String tableSchem) {
		this.tableSchem = tableSchem;
	}

	/**
	 * 
	 * @return Table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE",
	 *         "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"
	 */
	public String getTableType() {
		return tableType;
	}

	/**
	 * 
	 * @param tableType
	 *            type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE",
	 *            "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"
	 */
	public void setTableType(final String tableType) {
		this.tableType = tableType;
	}

	/**
	 * 
	 * @return The types catalog (may be <code>null</code>).
	 */
	public String getTypeCat() {
		return typeCat;
	}

	/**
	 * 
	 * @param typeCat
	 *            The types catalog (may be <code>null</code>).
	 */
	public void setTypeCat(final String typeCat) {
		this.typeCat = typeCat;
	}

	/**
	 * 
	 * @return The types schema.
	 */
	public String getTypeSchem() {
		return typeSchem;
	}

	/**
	 * 
	 * @param typeSchem
	 *            The types schema.
	 */
	public void setTypeSchem(final String typeSchem) {
		this.typeSchem = typeSchem;
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
		    .append(tableName).append(tableType).append(remarks).append(typeCat)
		    .append(typeSchem).append(selfReferencingColName).toHashCode();
	}
}