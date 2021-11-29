/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package net.rothsmith.dao.db;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.SelectDataQuery;
import com.rothsmith.dao.db.SelectMetaData;
import com.rothsmith.dao.spring.BaseSpringTest;

/**
 * Test generating metadata for SQL query statements.
 * 
 * @author drothauser
 *
 */
public class SelectDataQueryTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for SelectDataQueryTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(SelectDataQueryTest.class);

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
	 * {@link SelectDataQuery#fetchMetaData(DbMetaDataQueryParams)}.
	 * 
	 * @throws SQLException
	 *             possible SQL error
	 */
	@Test
	public void testSelectParty() throws SQLException {
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withSelectSql("SELECT * FROM PARTY").build();

		SelectDataQuery sqlSelectDataQuery = new SelectDataQuery();
		List<SelectMetaData> selectMetaData =
		    sqlSelectDataQuery.fetchMetaData(params);

		// CHECKSTYLE:OFF magic number 6 ok
		assertEquals("Expected 4 columns worth of metadata", 4,
		    selectMetaData.size());
		// CHECKSTYLE:ON

		selectMetaData.forEach(md -> LOGGER.info(md.toString()));

	}

	/**
	 * Test method for
	 * {@link SelectDataQuery#fetchMetaData(DbMetaDataQueryParams)} using schema
	 * prefix with table (<i>dot-noted</i>).
	 * 
	 * @throws SQLException
	 *             possible SQL error
	 */
	@Test
	public void testSelectPartyDotNote() throws SQLException {
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withSelectSql("SELECT * FROM TEST.PARTY").build();

		SelectDataQuery sqlSelectDataQuery = new SelectDataQuery();
		List<SelectMetaData> metaData =
		    sqlSelectDataQuery.fetchMetaData(params);

		// CHECKSTYLE:OFF - magic number 4 ok
		assertEquals(4, metaData.size());
		// CHECKSTYLE:ON

		metaData.forEach(md -> LOGGER.info(md.toString()));

	}

	/**
	 * Test method for
	 * {@link SelectDataQuery#fetchMetaData(DbMetaDataQueryParams)} with a
	 * select statement on a view.
	 * 
	 * @throws SQLException
	 *             possible SQL error
	 */
	@Test
	public void testSelectView() throws SQLException {
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withSelectSql("SELECT * FROM PRESIDENTS_VIEW").build();

		SelectDataQuery sqlSelectDataQuery = new SelectDataQuery();
		List<SelectMetaData> metaData =
		    sqlSelectDataQuery.fetchMetaData(params);

		// CHECKSTYLE:OFF - magic number 3 ok
		assertEquals(6, metaData.size());
		// CHECKSTYLE:ON

		metaData.forEach(md -> LOGGER.info(md.toString()));

	}

	/**
	 * Test method for
	 * {@link SelectDataQuery#fetchMetaData(DbMetaDataQueryParams)} with a
	 * select statement using a join.
	 * 
	 * @throws SQLException
	 *             possible SQL error
	 */
	@Test
	public void testSelectJoin() throws SQLException {
		String query = "SELECT A.LASTNAME, A.FIRSTNAME, "
		    + "B.NAME AS \"STATE\" FROM PRESIDENT A "
		    + "JOIN STATE B ON A.STATE_ID = B.ID";
		LOGGER.info(query);
		DbMetaDataQueryParams params =
		    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
		        .withDataSource(dataSource).withSelectSql(query).build();

		SelectDataQuery sqlSelectDataQuery = new SelectDataQuery();
		List<SelectMetaData> metaData =
		    sqlSelectDataQuery.fetchMetaData(params);

		// CHECKSTYLE:OFF - magic number 3 ok
		assertEquals(3, metaData.size());
		// CHECKSTYLE:ON

		metaData.forEach(md -> LOGGER.info(md.toString()));

	}

}
