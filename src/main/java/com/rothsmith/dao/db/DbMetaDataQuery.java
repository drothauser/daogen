/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.db;

import java.sql.SQLException;

/**
 * A database metadata query.
 * 
 * @author drothauser
 * 
 * @param <T>
 *            The return type of the metadata being retrieved.
 *
 */
public interface DbMetaDataQuery<T> {

	/**
	 * Method for fetching database metadata of some sort.
	 * 
	 * @param dmdqParams
	 *            see {@link DbMetaDataQueryParams}
	 * 
	 * @return the metadata
	 * @throws SQLException
	 *             possible SQL error
	 */
	T fetchMetaData(DbMetaDataQueryParams dmdqParams) throws SQLException;

}