/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.db;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.PKMetaData;
import com.rothsmith.dao.db.PKMetaDataQuery;
import com.rothsmith.dao.spring.BaseSpringTest;

/**
 * Tests for getting Primary Key information from a table.
 * 
 * @author drothauser
 *
 */
public class PrimaryKeyMetaDataQueryTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for PrimaryKeyMetaDataTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(PrimaryKeyMetaDataQueryTest.class);

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
	 * Test method for {@link PKMetaData#fetchMetaData(DbMetaDataQueryParams)}.
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test
	public void testCommentPKMetaData() throws SQLException {
		String namePattern = "STATE";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withSchemaPattern("TEST").withTableNames(namePattern).build();

		PKMetaDataQuery metaDataQuery = new PKMetaDataQuery();
		Map<Integer, PKMetaData> metaData = metaDataQuery.fetchMetaData(params);

		assertEquals(1, metaData.size());

		metaData.entrySet().forEach(md -> LOGGER.info(String
		    .format("%nKey Sequence: %d%n%s", md.getKey(), md.getValue())));

	}

	/**
	 * Test method for {@link PKMetaData#fetchMetaData(DbMetaDataQueryParams)}
	 * for a table with a composite primary key.
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test
	public void testPersonPKMetaData() throws SQLException {
		String namePattern = "PRESIDENT";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withSchemaPattern("TEST").withTableNames(namePattern).build();

		PKMetaDataQuery metaDataQuery = new PKMetaDataQuery();
		Map<Integer, PKMetaData> metaData = metaDataQuery.fetchMetaData(params);

		assertEquals(2, metaData.size());

		for (Map.Entry<Integer, PKMetaData> pkEntry : metaData.entrySet()) {
			Integer key = pkEntry.getKey();
			PKMetaData pKMetaData = pkEntry.getValue();
			LOGGER.info(String.format("%nKey Sequence: %d%nKey Data:%n%s", key,
			    pKMetaData));
		}

	}

	/**
	 * Test method for {@link PKMetaData#fetchMetaData(DbMetaDataQueryParams)}
	 * giving two table names. This will cause the expected
	 * IllegalArgumentException to be thrown.
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFetchMetaData2Tables() throws SQLException {
		String namePattern = "STATE, PARTY";
		DbMetaDataQueryParams params =
		    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
		        .withDataSource(dataSource).withTableNames(namePattern).build();

		PKMetaDataQuery metaDataQuery = new PKMetaDataQuery();
		metaDataQuery.fetchMetaData(params);
	}

}
