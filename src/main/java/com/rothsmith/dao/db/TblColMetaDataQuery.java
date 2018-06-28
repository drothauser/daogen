/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.lang.StringUtils;

/**
 * Class for getting column meta data for a given table.
 * 
 * @author drothauser
 *
 */
public class TblColMetaDataQuery
        implements DbMetaDataQuery<Map<Integer, TblColMetaData>> {

	/**
	 * Return a {@link Map} of {@link TblColMetaData} objects for the given
	 * parameters.
	 * 
	 * @param params
	 *            {@link DbMetaDataQueryParams}
	 * @return {@link Map} of {@link TblColMetaData} objects
	 * @throws SQLException
	 *             possible SQL error
	 */
	public Map<Integer, TblColMetaData> fetchMetaData(
	    DbMetaDataQueryParams params) throws SQLException {

		String tableNames = params.getTableNames();
		String[] namesArray =
		    StringUtils.split(StringUtils.replace(tableNames, " ", ""), ",");
		if (namesArray.length > 1) {
			throw new IllegalArgumentException("More than 1 table name given.");
		}

		DataSource dataSource = params.getDataSource();
		String catalog = params.getCatalog();
		String schemaPattern = params.getSchemaPattern();

		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();

			DatabaseMetaData databaseMetaData = conn.getMetaData();

			String table = StringUtils.containsIgnoreCase("Oracle",
			    databaseMetaData.getDatabaseProductName())
			        ? StringUtils.upperCase(namesArray[0])
			        : namesArray[0];

			String columnPattern = params.getColumnPattern();

			rs = databaseMetaData.getColumns(catalog, schemaPattern, table,
			    columnPattern);

			BeanProcessor beanProcessor = new GenerousBeanProcessor();

			@SuppressWarnings("PMD.UseConcurrentHashMap")
			Map<Integer, TblColMetaData> columnMap =
			    new TreeMap<Integer, TblColMetaData>();

			while (rs.next()) {

				TblColMetaData tblColMetaData = null;
				tblColMetaData = beanProcessor.toBean(rs, TblColMetaData.class);
				// System.out.println("*** " + rs.getString("COLUMN_DEF"));
				// tblColMetaData.setColumnDefault(rs.getString("COLUMN_DEF"));
				tblColMetaData
				    .setJavaColumnName(tblColMetaData.getColumnName());
				if (StringUtils.equalsIgnoreCase(tblColMetaData.getColumnName(),
				    "class")) {
					continue;
				}

				columnMap.put(rs.getInt("ORDINAL_POSITION"), tblColMetaData);
			}

			return columnMap;

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(conn);
		}
	}
}
