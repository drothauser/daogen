/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.dbutils;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.core.ParamsBuilder;

/**
 * Class that provides @BeforeClass and @Before methods for creating database
 * objects for testing.
 * 
 * @author drothauser
 *
 */
@SuppressWarnings("PMD.UseUtilityClass")
public class BaseDbUtilsTest {

	/**
	 * SLF4J Logger for BaseDbUtilsTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(BaseDbUtilsTest.class);

	/**
	 * JDBC data source.
	 */
	// CHECKSTYLE:OFF
	protected static BasicDataSource dataSource;

	/**
	 * The directory that Java is launched from.
	 */
	protected static String targetGenDir =
	    System.getProperty("user.dir") + "/target/gen/";

	/**
	 * Properties file containing Derby database connection parameters and SQL
	 * script files.
	 */
	protected static final String DERBY_TEST_PROPS = "/derby/daogen.properties";

	/**
	 * Class intended to be used as a base class only (not to be instantiated
	 * directly) therefore this protected constructor is provided prevent direct
	 * instantiation. protected constructor to thwart instantiation.
	 */
	protected BaseDbUtilsTest() {
		// protected constructor.
	}

	// CHECKSTYLE:ON

	/**
	 * Create database objects for testing.
	 * 
	 * @param propsFile
	 *            The properties file containing connection parameters and the
	 *            SQL scripts to run.
	 * 
	 * @throws IOException
	 *             possible problem loading the properties file
	 * @throws SQLException
	 *             possible SQL error
	 */
	protected static void setUpDatabase(String propsFile)
	        throws IOException, SQLException {

		final Properties props = new Properties();
		props.load(
		    BaseDbUtilsTest.class.getClass().getResourceAsStream(propsFile));

		dataSource = new BasicDataSource();
		dataSource.setUrl(props.getProperty("database.url"));
		dataSource.setUsername(props.getProperty("database.userName"));
		dataSource.setPassword(props.getProperty("database.password"));
		dataSource
		    .setDriverClassName(props.getProperty("database.driverClassName"));

		try (Connection conn = dataSource.getConnection()) {

			String createSql = fetchSql(props, "db.create.sql");

			String populateSql = fetchSql(props, "db.populate.sql");

			String initSql = createSql + populateSql;

			if (!"".equals(initSql)) {
				String[] sqlStmts = initSql.split("(?<!\\-{2}.{0,100});");
				for (String sql : sqlStmts) {
					try (Statement stmt = conn.createStatement()) {
						LOGGER.info(String.format("%nExecuting %s", sql));
						stmt.executeUpdate(sql);
						LOGGER.info("\nDone!");
					} catch (SQLException e) {
						String msg = String.format(
						    "Initialization error running SQL statement: %s: %s",
						    sql, e);
						LOGGER.error(msg, e);
						fail("Initialization error running SQL statements: "
						    + e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Initialize a {@link ParamsBuilder} instance for testing.
	 * 
	 * @return {@link ParamsBuilder} initialized with default testing parameters
	 * 
	 * @throws IOException
	 *             possible error deleting the target directory for generated
	 *             artifacts
	 */
	protected ParamsBuilder initParams() throws IOException {

		return ParamsBuilder.newInstance()
		    // .withTableTypes("TABLE, VIEW")
		    // .withTableNames("PARTY,STATE,PRESIDENT")
		    .withOutputDir(
		        FilenameUtils.normalize(targetGenDir + "src/main/java"))
		    .withDtoPackageName("com.fcci.presidents")
		    .withDefFileTemplate("/daodefs.vm")
		    .withDefFile(FilenameUtils
		        .normalize(targetGenDir + "src/main/resources/daodefs.xml"))
		    .withTestOutputDir(
		        FilenameUtils.normalize(targetGenDir + "src/test/java"))
		    .withTestPackageName("com.fcci.presidents").withSchema("TEST")
		    .withJunitTemplate("dbutils/junit.vm").withDtoTemplate("/dto.vm")
		    .withDbUtilsPropsTemplate("dbutils/propsfile.vm")
		    .withDbUtilsPropsDir(
		        FilenameUtils.normalize(targetGenDir + "src/main/resources"))
		    .withJndiName("java:/comp/env/jdbc/TestDS");

	}

	/**
	 * Method to read the sql files specified in the given property of the
	 * properties file and concatenate their contents into a string.
	 * 
	 * @param props
	 *            {@link Properties} object that contains sql parameters
	 * @param sqlProperty
	 *            Name of the property in the property file that contains names
	 *            of SQL files.
	 * @return String containing the contents of all the SQL files
	 * @throws IOException
	 *             thrown if there is problem accessing a SQL file
	 */
	private static String fetchSql(Properties props, String sqlProperty)
	        throws IOException {

		String sqlFileNames =
		    StringUtils.defaultString(props.getProperty(sqlProperty));

		StringBuilder sb = new StringBuilder();

		for (String sqlFileName : StringUtils
		    .split(StringUtils.deleteWhitespace(sqlFileNames), ',')) {

			URL sqlFileURL = Thread.currentThread().getContextClassLoader()
			    .getResource(sqlFileName);
			if (sqlFileURL == null) {
				throw new IOException("Couldn't find: " + sqlFileName);
			}

			String sql = StringUtils.trim(IOUtils.toString(sqlFileURL));

			sb.append(sql);

		}

		return sb.toString();

	}

}
