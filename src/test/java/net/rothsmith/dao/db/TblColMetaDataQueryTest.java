/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package net.rothsmith.dao.db;

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
import com.rothsmith.dao.db.TblColMetaData;
import com.rothsmith.dao.db.TblColMetaDataQuery;
import com.rothsmith.dao.spring.BaseSpringTest;

/**
 * Tests for getting column metadata..
 * 
 * @author drothauser
 *
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class TblColMetaDataQueryTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for TblColMetaDataQueryTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(TblColMetaDataQueryTest.class);

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
	 * {@link TblColMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} .
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test
	public void testFetchMetaData() throws SQLException {
		String namePattern = "STATE";
		String schema = "TEST";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource).withCatalog(null)
		    .withSchemaPattern(schema).withTableNames(namePattern).build();

		TblColMetaDataQuery metaDataQuery = new TblColMetaDataQuery();

		Map<Integer, TblColMetaData> metaData =
		    metaDataQuery.fetchMetaData(params);

		// CHECKSTYLE:OFF magic number 6 ok here
		assertEquals(3, metaData.size());
		// CHECKSTYLE:ON

		metaData.entrySet().forEach(md -> LOGGER.info(String
		    .format("%nColumn Sequence: %d%n%s", md.getKey(), md.getValue())));
	}

	/**
	 * Test method for
	 * {@link TblColMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} with
	 * exact column name.
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test
	public void testFetchMetaDataColumnName() throws SQLException {
		String namePattern = "PRESIDENT";
		String schema = "TEST";
		String columnName = "FIRSTNAME";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource).withCatalog(null)
		    .withSchemaPattern(schema).withTableNames(namePattern)
		    .withColumnPattern(columnName).build();

		TblColMetaDataQuery metaDataQuery = new TblColMetaDataQuery();

		Map<Integer, TblColMetaData> metaData =
		    metaDataQuery.fetchMetaData(params);

		assertEquals(1, metaData.size());

		metaData.entrySet().forEach(md -> LOGGER.info(String
		    .format("%nColumn Sequence: %d%n%s", md.getKey(), md.getValue())));
	}

	/**
	 * Test method for
	 * {@link TblColMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} with a
	 * column name pattern.
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test
	public void testFetchMetaDataColumnPattern() throws SQLException {
		String namePattern = "PRESIDENT";
		String schema = "TEST";
		String columnName = "%NAME";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource).withCatalog(null)
		    .withSchemaPattern(schema).withTableNames(namePattern)
		    .withColumnPattern(columnName).build();

		TblColMetaDataQuery metaDataQuery = new TblColMetaDataQuery();

		Map<Integer, TblColMetaData> metaData =
		    metaDataQuery.fetchMetaData(params);

		assertEquals(2, metaData.size());

		metaData.entrySet().forEach(md -> LOGGER.info(String
		    .format("%nColumn Sequence: %d%n%s", md.getKey(), md.getValue())));
	}

	/**
	 * Test method for
	 * {@link TblColMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} with two
	 * tables.
	 * 
	 * @throws SQLException
	 *             Possible SQL error
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFetchMetaData2Tables() throws SQLException {
		String namePattern = "PERSON, COMMENT";
		String schema = "TEST";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource).withCatalog(null)
		    .withSchemaPattern(schema).withTableNames(namePattern).build();

		TblColMetaDataQuery metaDataQuery = new TblColMetaDataQuery();

		metaDataQuery.fetchMetaData(params);

	}

}
