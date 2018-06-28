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

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.lang.StringUtils;

/**
 * @author drothauser
 *
 */
public class TblMetaDataQuery implements DbMetaDataQuery<List<TblMetaData>> {

	/**
	 * Return a {@link List} of {@link TblMetaData} objects for the given
	 * parameters.
	 * 
	 * @param dbMetaDataQueryParams
	 *            {@link DbMetaDataQueryParams}
	 * @return {@link List} of {@link TblMetaData}
	 * @throws SQLException
	 *             possible SQL error
	 */
	public List<TblMetaData> fetchMetaData(
	    DbMetaDataQueryParams dbMetaDataQueryParams) throws SQLException {

		DataSource dataSource = dbMetaDataQueryParams.getDataSource();
		if (dataSource == null) {
			throw new IllegalArgumentException("DataSource argument is null");
		}
		String catalog = dbMetaDataQueryParams.getCatalog();
		String schemaPattern = dbMetaDataQueryParams.getSchemaPattern();
		String namePattern = dbMetaDataQueryParams.getTableNames();
		String types = dbMetaDataQueryParams.getTypes();

		Connection conn = null;
		ResultSet resultSet = null;

		try {
			conn = dataSource.getConnection();

			DatabaseMetaData databaseMetaData = conn.getMetaData();

			String[] typesArray =
			    StringUtils.split(StringUtils.replace(types, " ", ""), ",");

			String[] namesArray = StringUtils
			    .split(StringUtils.replace(namePattern, " ", ""), ",");

			List<TblMetaData> tblMetaDataList = new ArrayList<TblMetaData>();

			BeanProcessor beanProcessor = new GenerousBeanProcessor();

			for (int i = 0; i < namesArray.length; i++) {
				String tableName = StringUtils.strip(namesArray[i], "\"'");
				resultSet = databaseMetaData.getTables(catalog, schemaPattern,
				    StringUtils.trim(tableName), typesArray);
				List<TblMetaData> beanList =
				    beanProcessor.toBeanList(resultSet, TblMetaData.class);
				if (CollectionUtils.isEmpty(beanList)) {
					throw new SQLException(String.format(
					    "Cannot find database object for '%s' "
					        + "in schema '%s' for these type(s): '%s'",
					    tableName, schemaPattern, types));
				}
				CollectionUtils.addAll(tblMetaDataList, beanList.iterator());
			}

			return tblMetaDataList;

		} finally {
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(conn);
		}
	}
}
