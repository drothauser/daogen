/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.spring;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rothsmith.dao.spring.AppCfgMetaDataQuery;

/**
 * @author drothauser
 *
 */
public class AppCfgMetaDataQueryTest
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
	 * {@link AppCfgMetaDataQuery#fetchMetaData(org.jdom.Element)}.
	 */
	@Test
	public void testFetchMetaData() {
		// TODO Test me!
		assertTrue("Not yet implemented", true);
	}

}
