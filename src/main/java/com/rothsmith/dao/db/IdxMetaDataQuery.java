/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.lang.StringUtils;

/**
 * Class for retrieving index information of a table.
 * 
 * @author drothauser
 *
 */
public class IdxMetaDataQuery
        implements DbMetaDataQuery<Map<String, List<IdxMetaData>>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public Map<String, List<IdxMetaData>> fetchMetaData(
	    DbMetaDataQueryParams params) throws SQLException {

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
			Map<String, List<IdxMetaData>> indexMap =
			    new TreeMap<String, List<IdxMetaData>>();

			String catalog = params.getCatalog();
			String schemaPattern = params.getSchemaPattern();
			rs = databaseMetaData.getIndexInfo(catalog, schemaPattern, table,
			    false, true);

			BeanProcessor beanProcessor = new GenerousBeanProcessor();

			String lastIndexname = null;

			List<IdxMetaData> idxMetaDataList = new ArrayList<IdxMetaData>();

			while (rs.next()) {
				String rsIndex = rs.getString("INDEX_NAME");
				if (StringUtils.isEmpty(rsIndex)) {
					continue;
				}
				if (!StringUtils.equals(lastIndexname, rsIndex)) {
					if (lastIndexname != null) {
						indexMap.put(lastIndexname, idxMetaDataList);
						idxMetaDataList = new ArrayList<IdxMetaData>();
					}
					lastIndexname = rsIndex;
				}
				IdxMetaData idxMetaData =
				    beanProcessor.toBean(rs, IdxMetaData.class);
				idxMetaDataList.add(idxMetaData);
			}

			if (lastIndexname != null) {
				indexMap.put(lastIndexname, idxMetaDataList);
			}

			return indexMap;

		} finally {
			DbUtils.close(rs);
			DbUtils.closeQuietly(conn);
		}
	}
}
