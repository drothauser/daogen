/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.db;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.IdxMetaData;
import com.rothsmith.dao.db.IdxMetaDataQuery;
import com.rothsmith.dao.spring.BaseSpringTest;

/**
 * Tests for getting index information from a table.
 * 
 * @author drothauser
 *
 */
public class IdxMetaDataQueryTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for IdxMetaDataQueryTest.
	 */
	public static final Logger LOGGER =
	    LoggerFactory.getLogger(IdxMetaDataQueryTest.class);

	/**
	 * Create database objects for testing.
	 * 
	 * @throws IOException
	 *             possible problem loading the properties file
	 * @throws SQLException
	 *             possible SQL error
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws IOException, SQLException {

		setUpDatabase(DERBY_TEST_PROPS);

	}

	/**
	 * Test method for
	 * {@link IdxMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)}.
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test
	public void testFetchMetaData1Index() throws SQLException {
		String namePattern = "STATE";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withSchemaPattern("TEST").withTableNames(namePattern).build();

		IdxMetaDataQuery idxMetaDataQuery = new IdxMetaDataQuery();
		Map<String, List<IdxMetaData>> metaData =
		    idxMetaDataQuery.fetchMetaData(params);

		metaData.entrySet().forEach(md -> LOGGER.info(String
		    .format("%nKey Sequence: %s%n%s", md.getKey(), md.getValue())));

		assertEquals(1, metaData.size());

	}

	/**
	 * Test method for
	 * {@link IdxMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} for a table
	 * with two indexes - one for the primary key and the other for a column.
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test
	public void testFetchMetaData2Indexes() throws SQLException {
		String namePattern = "PRESIDENT";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withSchemaPattern("TEST").withTableNames(namePattern).build();

		IdxMetaDataQuery metaDataQuery = new IdxMetaDataQuery();
		Map<String, List<IdxMetaData>> metaData =
		    metaDataQuery.fetchMetaData(params);

		metaData.entrySet().forEach(md -> LOGGER.info(
		    String.format("%nIndex Name: %s%n%s", md.getKey(), md.getValue())));

		// CHECKSTYLE:OFF Magic number 3 ok here
		assertEquals(5, metaData.size());
		// CHECKSTYLE:ON

	}

	/**
	 * Test method for
	 * {@link IdxMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} giving two
	 * table names. This will cause the expected IllegalArgumentException to be
	 * thrown.
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFetchMetaData2Tables() throws SQLException {
		String namePattern = "STATE,PRESIDENTS_VIEW";
		DbMetaDataQueryParams params =
		    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
		        .withDataSource(dataSource).withTableNames(namePattern).build();

		IdxMetaDataQuery metaDataQuery = new IdxMetaDataQuery();
		metaDataQuery.fetchMetaData(params);

	}

}
