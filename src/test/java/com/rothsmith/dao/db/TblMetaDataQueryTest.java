/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.TblMetaData;
import com.rothsmith.dao.db.TblMetaDataQuery;
import com.rothsmith.dao.spring.BaseSpringTest;

/**
 * Tests for retrieving table meta data.
 * 
 * @author drothauser
 *
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class TblMetaDataQueryTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for TblMetaDataQueryTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(TblMetaDataQueryTest.class);

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
	 * {@link TblMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} .
	 * 
	 * @throws SQLException
	 *             possible SQL problem
	 */
	@Test
	public void testFetchMetaData() throws SQLException {

		String namePattern = "PRESIDENT, PRESIDENTS_VIEW, PARTY, STATE";
		String types = "TABLE,VIEW";
		String schema = "TEST";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource).withCatalog(null)
		    .withSchemaPattern(schema).withTableNames(namePattern)
		    .withTableTypes(types).build();

		TblMetaDataQuery metaDataQuery = new TblMetaDataQuery();
		List<TblMetaData> metaData = metaDataQuery.fetchMetaData(params);

		String[] typesArray =
		    StringUtils.split(StringUtils.replace(types, " ", ""), ",");

		String[] namesArray = StringUtils
		    .split(StringUtils.replaceChars(namePattern, " \"'", ""), ",");

		metaData.forEach(md -> {

			// CHECKSTYLE:OFF CS doesn't like lambda indentation
			LOGGER.info(String.format("%nTable name: %s, Type:%s%n%s",
		        md.getTableName(), md.getTableType(), md.toString()));

			assertTrue(ArrayUtils.contains(typesArray, md.getTableType())
		        && ArrayUtils.contains(namesArray, md.getTableName()));

		}); // CHECKSTYLE:ON

	}

	/**
	 * Test of getTblMetaData method, of class SchemaMetaData.
	 * 
	 * @throws SQLException
	 *             possible SQL error
	 */
	@Test
	public final void testGetTablesWithWildcard() throws SQLException {

		String catalog = null;
		String schemaPattern = "TEST";
		String tableNamePattern = "PRESIDENT%";
		String types = "TABLE,VIEW";

		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource)
		    .withCatalog(catalog).withSchemaPattern(schemaPattern)
		    .withTableNames(tableNamePattern).withTableTypes(types).build();

		TblMetaDataQuery metaDataQuery = new TblMetaDataQuery();
		List<TblMetaData> metaData = metaDataQuery.fetchMetaData(params);

		assertEquals(2, metaData.size());

		metaData.forEach(md -> {
			// CHECKSTYLE:OFF CS doesn't like lambda indentation
			LOGGER.info(String.format("%nTable name: %s, Type:%s%n%s",
		        md.getTableName(), md.getTableType(), md.toString()));
		}); // CHECKSTYLE:ON

	}

	/**
	 * Test method for
	 * {@link TblMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} using a
	 * non-existent table.
	 * 
	 * @throws SQLException
	 *             expected SQLException because one of the table names is bad
	 */
	@Test(expected = SQLException.class)
	public void testFetchMetaDataBadTable() throws SQLException {

		String namePattern = "XPERSON, PERSON_VIEW";
		String types = "TABLE,VIEW";
		String schema = "TEST";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource).withCatalog(null)
		    .withSchemaPattern(schema).withTableNames(namePattern)
		    .withTableTypes(types).build();

		TblMetaDataQuery metaDataQuery = new TblMetaDataQuery();

		metaDataQuery.fetchMetaData(params);

	}

	/**
	 * Test method for
	 * {@link TblMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} with a bad
	 * schema name.
	 * 
	 * @throws SQLException
	 *             expected SQLException because one of the table names is bad
	 */
	@Test(expected = SQLException.class)
	public void testFetchMetaDataBadSchema() throws SQLException {

		String namePattern = "PERSON, PERSON_VIEW";
		String types = "TABLE,VIEW";
		String schema = "BOGUS";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource).withCatalog(null)
		    .withSchemaPattern(schema).withTableNames(namePattern)
		    .withTableTypes(types).build();

		TblMetaDataQuery metaDataQuery = new TblMetaDataQuery();

		metaDataQuery.fetchMetaData(params);

	}

	/**
	 * Test method for
	 * {@link TblMetaDataQuery#fetchMetaData(DbMetaDataQueryParams)} using a bad
	 * DB object type.
	 * 
	 * @throws SQLException
	 *             expected SQLException because one of the table names is bad
	 */
	@Test(expected = SQLException.class)
	public void testFetchMetaDataBadType() throws SQLException {

		String namePattern = "COMMENT";
		String types = "BOGUS";
		String schema = "TEST";
		DbMetaDataQueryParams params = DbMetaDataQueryParamsBuilder
		    .metaDataQueryParams().withDataSource(dataSource).withCatalog(null)
		    .withSchemaPattern(schema).withTableNames(namePattern)
		    .withTableTypes(types).build();

		TblMetaDataQuery metaDataQuery = new TblMetaDataQuery();

		metaDataQuery.fetchMetaData(params);

	}

}
