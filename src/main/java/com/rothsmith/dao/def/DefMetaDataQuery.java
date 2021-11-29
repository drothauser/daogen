/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.def;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.IdxMetaDataQuery;
import com.rothsmith.dao.db.PKMetaDataQuery;
import com.rothsmith.dao.db.TblColMetaDataQuery;
import com.rothsmith.dao.db.TblMetaData;
import com.rothsmith.dao.db.TblMetaDataQuery;

import net.rothsmith.dao.core.Params;
import net.rothsmith.dao.core.TextUtils;

/**
 * Retrieves information used to generate DAO artifacts (for example package
 * name, class name, etc.).
 * 
 * @author drothauser
 *
 */
public class DefMetaDataQuery {

	/**
	 * JDBC {@link DataSource}.
	 */
	private final DataSource dataSource;

	/**
	 * {@link TblMetaDataQuery} utility for getting table metadata.
	 */
	private final TblMetaDataQuery tblMetaDataQuery = new TblMetaDataQuery();

	/**
	 * {@link TblColMetaDataQuery} utility for getting a table's column
	 * metadata.
	 */
	private final TblColMetaDataQuery tblColMetaDataQuery =
	    new TblColMetaDataQuery();

	/**
	 * {@link PKMetaDataQuery} utility for getting a table's primary key
	 * metadata.
	 */
	private final PKMetaDataQuery pKMetaDataQuery = new PKMetaDataQuery();

	/**
	 * {@link IdxMetaDataQuery} utility for getting a table's index metadata.
	 */
	private final IdxMetaDataQuery idxMetaDataQuery = new IdxMetaDataQuery();

	/**
	 * Construct instance with the given JDBC {@link DataSource}.
	 * 
	 * @param dataSource
	 *            JDBC {@link DataSource}.
	 */
	public DefMetaDataQuery(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Return a {@link List} of {@link DefMetaData} objects for the given
	 * parameters.
	 * 
	 * @param params
	 *            {@link Params}
	 * @return {@link List} of {@link DefMetaData} objects
	 * @throws SQLException
	 *             possible database error
	 */
	public List<DefMetaData> fetchMetaData(Params params) throws SQLException {

		DbMetaDataQueryParams tblListParams = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withSchemaPattern(params.getSchemaPattern())
		    .withTableNames(params.getTableNames())
		    .withTableTypes(params.getTableTypes()).build();

		List<TblMetaData> tblMetaDataList =
		    tblMetaDataQuery.fetchMetaData(tblListParams);

		List<DefMetaData> defMetaDataList = new ArrayList<DefMetaData>();

		for (TblMetaData tblMetaData : tblMetaDataList) {
			String tableName = tblMetaData.getTableName();
			String schema = tblMetaData.getTableSchem();

			DbMetaDataQueryParams colMetaDataParams =
			    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
			        .withDataSource(dataSource).withSchemaPattern(schema)
			        .withTableNames(tableName).build();

			DefMetaData defMetaData =
			    DefMetaDataBuilder.daoMetaData().withTblMetaData(tblMetaData)
			        .withClassName(TextUtils.convertToCamelCase(tableName))
			        .withOutputDir(params.getOutputDir())
			        .withPackageName(params.getDtoPackageName())
			        .withTestOutputDir(params.getTestOutputDir())
			        .withTestPackageName(params.getTestPackageName())
			        .withUser(System.getProperty("user.name"))
			        .withColumnMap(
			            tblColMetaDataQuery.fetchMetaData(colMetaDataParams))
			        .withPkMap(pKMetaDataQuery.fetchMetaData(colMetaDataParams))
			        .withIndexMap(
			            idxMetaDataQuery.fetchMetaData(colMetaDataParams))
			        .build();

			defMetaDataList.add(defMetaData);
		}

		return defMetaDataList;

	}

}
