/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.rothsmith.dao.core.Params;

/**
 * Class that provides @BeforeClass and @Before methods for creating database
 * objects for testing.
 * 
 * @author drothauser
 *
 */
@SuppressWarnings("PMD.UseUtilityClass")
public class DerbyScript {

	/**
	 * SLF4J Logger for BaseDbUtilsTest.
	 */
	private static final Logger LOGGER = LoggerFactory
	    .getLogger(DerbyScript.class);

	/**
	 * JDBC data source.
	 */
	// CHECKSTYLE:OFF
	protected static BasicDataSource dataSource;

	/**
	 * {@link Params} for testing.
	 */
	protected Params testParams;

	/**
	 * The directory that Java is launched from.
	 */
	protected static String targetGenDir = System.getProperty("user.dir")
	    + "/target/gen/";

	// CHECKSTYLE:ON

	/**
	 * Create database objects for testing.
	 * 
	 * @param args
	 *            Program arguments
	 * 
	 * @throws IOException
	 *             possible problem loading the properties file
	 * @throws SQLException
	 *             possible SQL error
	 */
	@BeforeClass
	public static void main(String[] args) throws IOException, SQLException {

		final Properties props = new Properties();
		props.load(DerbyScript.class.getClass().getResourceAsStream(
		    "/derby/unitils.properties"));

		dataSource = new BasicDataSource();
		dataSource.setUrl(props.getProperty("database.url"));
		dataSource.setUsername(props.getProperty("database.userName"));
		dataSource.setPassword(props.getProperty("database.password"));
		dataSource.setDriverClassName(props.getProperty("database.driverClassName"));

		try (Connection conn = dataSource.getConnection()) {

			String ddl =
			    StringUtils.trimWhitespace(IOUtils.toString(DerbyScript.class
			        .getResource("/derby/presidents_schema_new.ddl")));

			String[] sqlStmts = ddl.split("(?<!\\-{2}.{0,100});");
			for (String sql : sqlStmts) {
				try (Statement stmt = conn.createStatement()) {
					LOGGER.info(String.format("%nExecuting %s", sql));
					stmt.executeUpdate(sql);
					LOGGER.info("\nDone!");
				} catch (SQLException e) {
					LOGGER
					    .error("Initialization error running SQL statements: "
					        + e.getMessage());
				}
			}
		}

	}

	/**
	 * Class intended to be used as a base class only (not to be instantiated
	 * directly) therefore this protected constructor is provided prevent direct
	 * instantiation. protected constructor to thwart instantiation.
	 */
	protected DerbyScript() {
		// protected constructor.
	}

}
