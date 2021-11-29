/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.def;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rothsmith.dao.def.DefMetaData;
import com.rothsmith.dao.def.DefMetaDataQuery;
import com.rothsmith.dao.spring.BaseSpringTest;

/**
 * Tests for {@link DefMetaDataQuery} that retrieves a list of
 * {@link DefMetaData} objects that contain information used to generate DAO
 * artifacts (for example package name, class name, etc.).
 * 
 * @author drothauser
 *
 */
public class DefMetaDataQueryTest
        extends BaseSpringTest {

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
	 * {@link DefMetaDataQuery#fetchMetaData(net.rothsmith.dao.core.Params)} .
	 * 
	 * @throws SQLException
	 *             possible database error
	 */
	@Test
	public void testDaoMetaData() throws SQLException {
		DefMetaDataQuery defMetaDataQuery = new DefMetaDataQuery(dataSource);
		List<DefMetaData> defMetaData =
		    defMetaDataQuery.fetchMetaData(testParams);
		assertNotNull(defMetaData);
	}

	/**
	 * Test method for
	 * {@link DefMetaDataQuery#fetchMetaData(net.rothsmith.dao.core.Params)} with an
	 * invalid datasource.
	 * 
	 * @throws SQLException
	 *             possible database error
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDaoMetaDataBadDataSource() throws SQLException {
		DefMetaDataQuery defMetaDataQuery = new DefMetaDataQuery(null);
		defMetaDataQuery.fetchMetaData(testParams);
	}

	/**
	 * Test method for
	 * {@link DefMetaDataQuery#fetchMetaData(net.rothsmith.dao.core.Params)} with an
	 * invalid datasource.
	 * 
	 * @throws SQLException
	 *             possible database error
	 */
	@Test(expected = SQLException.class)
	public void testDaoMetaDataInvalidTable() throws SQLException {
		DefMetaDataQuery defMetaDataQuery = new DefMetaDataQuery(dataSource);
		testParams.setTableNames("BOGUS");
		defMetaDataQuery.fetchMetaData(testParams);
	}

	/**
	 * Test method for
	 * {@link DefMetaDataQuery#fetchMetaData(net.rothsmith.dao.core.Params)} with an
	 * invalid datasource.
	 * 
	 * @throws SQLException
	 *             possible database error
	 */
	@Test(expected = SQLException.class)
	public void testDaoMetaDataInvalidSchema() throws SQLException {
		DefMetaDataQuery defMetaDataQuery = new DefMetaDataQuery(dataSource);
		testParams.setSchemaPattern("BOGUS");
		defMetaDataQuery.fetchMetaData(testParams);
	}

}
