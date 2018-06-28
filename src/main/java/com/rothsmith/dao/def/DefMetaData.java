/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.def;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.rothsmith.dao.db.IdxMetaData;
import com.rothsmith.dao.db.PKMetaData;
import com.rothsmith.dao.db.TblColMetaData;
import com.rothsmith.dao.db.TblMetaData;

/**
 * Metadata for generating the DAO definitions file.
 * <p>
 * Note that this class wraps {@link TblMetaData} with information used in the
 * definitions file (for example package name, class name, etc.).
 * </p>
 * 
 * @author drothauser
 */
public final class DefMetaData implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -9215887864634853183L;

	/**
	 * Java package name.
	 */
	private String packageName;

	/**
	 * Class name.
	 */
	private String className;

	/**
	 * user id.
	 */
	private String user;

	/**
	 * SQL Statement.
	 */
	private String sqlStmt;

	/**
	 * output directory.
	 */
	private String outputDir;

	/**
	 * {@link TblMetaData}.
	 */
	private TblMetaData tblMetaData;

	/**
	 * Column map. Key = column number, Value = {@link TblColMetaData}.
	 */
	private Map<Integer, TblColMetaData> columnMap;

	/**
	 * Primary key map. Key = key column number, Value = {@link PKMetaData}.
	 */
	private Map<Integer, PKMetaData> pkMap;

	/**
	 * Index map. Key = index name, Value = {@link List} of {@link IdxMetaData}
	 * objects.
	 */
	private Map<String, List<IdxMetaData>> indexMap;

	/**
	 * Output directory for test packages (e.g. &quot;test&quot;).
	 */
	private String testOutputDir;

	/**
	 * Test package name
	 */
	private String testPackageName;

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
	 * @return Column map. Key = column number, Value = {@link TblColMetaData} .
	 */
	public Map<Integer, TblColMetaData> getColumnMap() {
		return columnMap;
	}

	/**
	 * @return the index map. Key = index name, Value = {@link List} of
	 *         {@link IdxMetaData} objects.
	 */
	public Map<String, List<IdxMetaData>> getIndexMap() {
		return indexMap;
	}

	/**
	 * 
	 * @return output directory
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * 
	 * @return package name.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * 
	 * @return Primary key map. Key = key column number, Value =
	 *         {@link PKMetaData}.
	 */
	public Map<Integer, PKMetaData> getPkMap() {
		return pkMap;
	}

	/**
	 * 
	 * @return {@link TblMetaData} object
	 */
	public TblMetaData getTblMetaData() {
		return tblMetaData;
	}

	/**
	 * @return The output directory for test packages (e.g. &quot;test&quot;).
	 */
	public String getTestOutputDir() {
		return testOutputDir;
	}

	/**
	 * @return test package name
	 */
	public String getTestPackageName() {
		return testPackageName;
	}

	/**
	 * 
	 * @return SQL statement.
	 */
	public String getSqlStmt() {
		return sqlStmt;
	}

	/**
	 * 
	 * @return user id.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(packageName).append(className)
		    .append(user).append(outputDir).append(tblMetaData)
		    .append(columnMap).append(pkMap).toHashCode();
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
	 * @param columnMap
	 *            Column map. Key = column number, Value =
	 *            {@link TblColMetaData}.
	 */
	public void setColumnMap(final Map<Integer, TblColMetaData> columnMap) {
		this.columnMap = columnMap;
	}

	/**
	 * Set Index map. Key = index name, Value = {@link List} of
	 * {@link IdxMetaData} objects.
	 * 
	 * @param indexMap
	 *            the indexMap to set
	 */
	public void setIndexMap(final Map<String, List<IdxMetaData>> indexMap) {
		this.indexMap = indexMap;
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
	 *            package name.
	 */
	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 
	 * @param pkMap
	 *            Primary key map. Key = key column number, Value =
	 *            {@link PKMetaData}.
	 */
	public void setPkMap(final Map<Integer, PKMetaData> pkMap) {
		this.pkMap = pkMap;
	}

	/**
	 * 
	 * @param tblMetaData
	 *            {@link TblMetaData} object
	 */
	public void setTblMetaData(final TblMetaData tblMetaData) {
		this.tblMetaData = tblMetaData;
	}

	/**
	 * @param testOutputDir
	 *            Output directory for test packages (e.g. &quot;test&quot;).
	 */
	public void setTestOutputDir(final String testOutputDir) {
		this.testOutputDir = testOutputDir;
	}

	/**
	 * @param testPackageName
	 *            test package name
	 */
	public void setTestPackageName(final String testPackageName) {
		this.testPackageName = testPackageName;
	}

	/**
	 * 
	 * @param sqlStmt
	 *            SQL Statement
	 */
	public void setSqlStmt(final String sqlStmt) {
		this.sqlStmt = sqlStmt;
	}

	/**
	 * 
	 * @param user
	 *            user id
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