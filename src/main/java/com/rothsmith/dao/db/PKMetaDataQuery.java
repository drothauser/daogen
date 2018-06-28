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
 * Class for retrieving the primary key information of a table.
 * 
 * @author drothauser
 *
 */
public class PKMetaDataQuery
        implements DbMetaDataQuery<Map<Integer, PKMetaData>> {

	/**
	 * This method gets the primary key data for a table.
	 * 
	 * @param params
	 *            {@link DbMetaDataQueryParams}
	 * @return a {@link Map} where each element contains the following:
	 *         <ul>
	 *         <li>map key = primary key sequence number
	 *         <li>map value = {@link PKMetaData}.
	 *         </ul>
	 * @throws SQLException
	 *             possible SQL error
	 */
	public Map<Integer, PKMetaData> fetchMetaData(DbMetaDataQueryParams params)
	        throws SQLException {

		String tableNames = params.getTableNames();
		String[] namesArray =
		    StringUtils.split(StringUtils.replace(tableNames, " ", ""), ",");
		if (namesArray.length != 1) {
			throw new IllegalArgumentException(String.format(
			    "Expected 1 table name ('%d' supplied).", namesArray.length));
		}

		Connection conn = null;
		ResultSet rs = null;
		DataSource dataSource = params.getDataSource();
		try {
			conn = dataSource.getConnection();

			DatabaseMetaData databaseMetaData = conn.getMetaData();

			String table = StringUtils.containsIgnoreCase("Oracle",
			    databaseMetaData.getDatabaseProductName())
			        ? StringUtils.upperCase(namesArray[0])
			        : namesArray[0];

			@SuppressWarnings("PMD.UseConcurrentHashMap")
			Map<Integer, PKMetaData> pkMap = new TreeMap<Integer, PKMetaData>();
			String catalog = params.getCatalog();
			String schemaPattern = params.getSchemaPattern();
			rs = databaseMetaData.getPrimaryKeys(catalog, schemaPattern, table);

			BeanProcessor beanProcessor = new GenerousBeanProcessor();
			while (rs.next()) {
				PKMetaData pKMetaData =
				    beanProcessor.toBean(rs, PKMetaData.class);
				pkMap.put(rs.getInt("KEY_SEQ"), pKMetaData);
			}

			return pkMap;

		} finally {
			DbUtils.close(rs);
			DbUtils.closeQuietly(conn);
		}
	}
}
