/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.spring;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.core.Params;
import com.rothsmith.dao.core.ParamsBuilder;
import com.rothsmith.dao.dbutils.BaseDbUtilsTest;

/**
 * Class that provides @BeforeClass and @Before methods for creating database
 * objects for testing.
 * 
 * @author drothauser
 *
 */
@SuppressWarnings("PMD.UseUtilityClass")
public class BaseSpringTest {

	/**
	 * SLF4J Logger for BaseDbUtilsTest.
	 */
	private static final Logger LOGGER = LoggerFactory
	    .getLogger(BaseSpringTest.class);

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

	/**
	 * Properties file containing database connection parameters and SQL script
	 * files.
	 */
	protected static final String DERBY_TEST_PROPS = "/derby/daogen.properties";

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
	protected static void setUpDatabase(String propsFile) throws IOException,
	        SQLException {

		final Properties props = new Properties();
		props.load(BaseDbUtilsTest.class.getClass().getResourceAsStream(
		    propsFile));

		dataSource = new BasicDataSource();
		dataSource.setUrl(props.getProperty("database.url"));
		dataSource.setUsername(props.getProperty("database.userName"));
		dataSource.setPassword(props.getProperty("database.password"));
		dataSource.setDriverClassName(props
		    .getProperty("database.driverClassName"));

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
						String msg =
						    String
						        .format(
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
	 * Build test a {@link Params} instance for testing.
	 * 
	 * @throws IOException
	 *             possible error deleting the target directory for generated
	 *             artifacts
	 */
	@Before
	public void buildParams() throws IOException {

		FileUtils.deleteDirectory(new File(targetGenDir));

		this.testParams =
		    ParamsBuilder
		        .newInstance()
		        .withTableTypes("TABLE, VIEW")
		        .withTableNames("PARTY,STATE,PRESIDENT")
		        .withOutputDir(
		            FilenameUtils.normalize(targetGenDir + "src/main/java"))
		        .withDtoPackageName("com.fcci.genericdao")
		        .withDefFileTemplate("/daodefs.vm")
		        .withDefFile(
		            FilenameUtils.normalize(targetGenDir
		                + "src/main/resources/daodefs.xml"))
		        .withTestOutputDir(
		            FilenameUtils.normalize(targetGenDir + "src/test/java"))
		        .withTestPackageName("com.fcci.genericdao")
		        .withSchema("TEST")
		        .withJunitTemplate("spring/junit.vm")
		        .withPropsFile("classpath:derby/unitils.properties")
		        .withDtoTemplate("/dto.vm")
		        .withSpringDaoCtxTemplate("spring/dao.vm")
		        .withSpringAppCtxTemplate("spring/applicationContext.vm")
		        .withSpringDaoCtxFile(
		            FilenameUtils.normalize(targetGenDir
		                + "src/main/resources/dao.xml"))
		        .withSpringAppCtxFile(
		            FilenameUtils.normalize(targetGenDir + "src/main/"
		                + "resources/applicationContext.xml")).build();

	}

	/**
	 * Class intended to be used as a base class only (not to be instantiated
	 * directly) therefore this protected constructor is provided prevent direct
	 * instantiation. protected constructor to thwart instantiation.
	 */
	protected BaseSpringTest() {
		// protected constructor.
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

		for (String sqlFileName : StringUtils.split(
		    StringUtils.deleteWhitespace(sqlFileNames), ',')) {

			URL sqlFileURL =
			    Thread.currentThread().getContextClassLoader()
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
