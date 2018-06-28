/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for retrieving column data from a SQL query.
 * 
 * @author drothauser
 *
 */
public class SelectDataQuery implements DbMetaDataQuery<List<SelectMetaData>> {

	/**
	 * SLF4J Logger for SelectDataQuery.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(SelectDataQuery.class);

	/**
	 * This method selects the first row returned by executing the given SQL
	 * statement and returns a {@link List} of {@link SelectMetaData} objects
	 * containing the metadata of each column.
	 * 
	 * @param params
	 *            {@link DbMetaDataQueryParams}
	 * @return a {@link List} of {@link SelectMetaData} objects
	 * @throws SQLException
	 *             possible SQL error
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public List<SelectMetaData> fetchMetaData(DbMetaDataQueryParams params)
	        throws SQLException {

		List<SelectMetaData> list = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DataSource dataSource = params.getDataSource();
		String selectSql = params.getSelectSql();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("%n**** SQL:%n%s%n", selectSql));
		}

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(selectSql);
			ps.setMaxRows(1);
			rs = ps.executeQuery();
			list = fetchColumnMetaDataList(rs);

		} finally {
			DbUtils.closeQuietly(conn, ps, rs);
		}

		return list;
	}

	/**
	 * For each of the selected columns in a query, map its
	 * {@link ResultSetMetaData} to a {@link SelectMetaData} object and add it a
	 * {@link List}.
	 * 
	 * @param rs
	 *            JDBC {@link ResultSet}
	 * @return {@link List} of {@link SelectMetaData} objects containing column
	 *         meta data
	 * @throws SQLException
	 *             possible SQL error
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	private List<SelectMetaData> fetchColumnMetaDataList(final ResultSet rs)
	        throws SQLException {

		List<SelectMetaData> list = new ArrayList<SelectMetaData>();
		ResultSetMetaData rsmd = rs.getMetaData();

		int columnCount = rsmd.getColumnCount();

		SelectMetaData sqlSelectMetaData = null;

		for (int i = 1; i <= columnCount; i++) {

			String columnName = rsmd.getColumnName(i);
			if (StringUtils.equalsIgnoreCase(columnName, "class")) {
				continue;
			}

			sqlSelectMetaData = new SelectMetaData();
			sqlSelectMetaData.setColumnName(columnName);
			sqlSelectMetaData.setAutoIncrement(rsmd.isAutoIncrement(i));
			sqlSelectMetaData.setCaseSensitive(rsmd.isCaseSensitive(i));
			sqlSelectMetaData.setCatalogName(rsmd.getCatalogName(i));
			String columnClassName = rsmd.getColumnClassName(i);
			sqlSelectMetaData.setColumnClassName(
			    StringUtils.upperCase(columnClassName).matches(".*[CB]LOB$(?i)")
			        ? "java.lang.String"
			        : rsmd.getColumnClassName(i));
			sqlSelectMetaData
			    .setColumnDisplaySize(rsmd.getColumnDisplaySize(i));
			sqlSelectMetaData.setColumnLabel(rsmd.getColumnLabel(i));

			sqlSelectMetaData.setColumnType(rsmd.getColumnType(i));
			sqlSelectMetaData.setColumnTypeName(rsmd.getColumnTypeName(i));
			sqlSelectMetaData.setCurrency(rsmd.isCurrency(i));
			sqlSelectMetaData
			    .setDefinitelyWritable(rsmd.isDefinitelyWritable(i));
			sqlSelectMetaData.setNullable(rsmd.isNullable(i));
			sqlSelectMetaData.setPrecision(rsmd.getPrecision(i));
			sqlSelectMetaData.setReadOnly(rsmd.isReadOnly(i));
			sqlSelectMetaData.setScale(rsmd.getScale(i));
			sqlSelectMetaData.setSchemaName(rsmd.getSchemaName(i));
			sqlSelectMetaData.setSearchable(rsmd.isSearchable(i));
			sqlSelectMetaData.setSigned(rsmd.isSigned(i));
			sqlSelectMetaData.setTableName(rsmd.getTableName(i));
			sqlSelectMetaData.setWritable(rsmd.isWritable(i));
			list.add(sqlSelectMetaData);
		}

		return list;
	}
}
