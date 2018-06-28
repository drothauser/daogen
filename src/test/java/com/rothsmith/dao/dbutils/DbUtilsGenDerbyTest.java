/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.dbutils;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rothsmith.dao.runner.DaoGenRunner;

/**
 * Tests for {@link DaoGenRunner} that generates DbUtil DAOs using Derby.
 * 
 * @author drothauser
 */
public class DbUtilsGenDerbyTest
        extends BaseDbUtilsTest {

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
	 * Test the {@link DaoGenRunner}.
	 * 
	 * @throws Exception
	 *             possible exception
	 */
	@Test
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public final void testDaoGenRunner() throws Exception {

		String targetGenDir = FilenameUtils.normalize(
		    System.getProperty("user.dir") + "/../daogen-test-dbutils-derby");

		String tables = "STATE,PARTY,PRESIDENT";

		String srcOutputDir =
		    FilenameUtils.normalize(targetGenDir + "/src/main/java");
		String dtoPackageName = "com.fcci.presidents";
		String testOutputDir =
		    FilenameUtils.normalize(targetGenDir + "/src/test/java");

		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream(DERBY_TEST_PROPS));

		List<String> argList = new ArrayList<String>();
		argList.add("--jdbcurl");
		argList.add(props.getProperty("database.url"));
		argList.add("--dbuser");
		argList.add(props.getProperty("database.userName"));
		argList.add("--dbpassword");
		argList.add(props.getProperty("database.password"));
		argList.add("--driverclass");
		argList.add(props.getProperty("database.driverClassName"));
		argList.add("--jndiname");
		argList.add("java:/comp/env/jdbc/TestDS");

		argList.add("--types");
		argList.add("TABLE,VIEW,SYNONYM");
		argList.add("--names");
		argList.add(tables);
		argList.add("--outputdir");
		argList.add(srcOutputDir);

		// argList.add("--dtotpl");
		// argList.add("/dto.vm");
		// argList.add("--deftpl");
		// argList.add("/daodefs.vm");

		argList.add("--dtopkg");
		argList.add(dtoPackageName);
		argList.add("--deffile");
		argList.add(FilenameUtils
		    .normalize(targetGenDir + "/src/main/resources/daodefs.xml"));
		argList.add("--testoutputdir");
		argList.add(testOutputDir);
		argList.add("--testpkg");
		argList.add(dtoPackageName);
		argList.add("--schema");
		argList.add("TEST");
		argList.add("--junittpl");
		argList.add("dbutils/junit.vm");
		argList.add("--propsfile");
		argList.add("classpath:unitils.properties");
		argList.add("--dbupropstpl");
		argList.add("dbutils/propsfile.vm");
		argList.add("--dbupropsdir");
		argList
		    .add(FilenameUtils.normalize(targetGenDir + "/src/main/resources"));

		argList.add("--sqlstmts");
		argList.add("SELECT * FROM PRESIDENT;SELECT * FROM PRESIDENTS_VIEW");

		String[] args = argList.toArray(new String[argList.size()]);

		FileUtils.deleteDirectory(new File(srcOutputDir));
		FileUtils.deleteDirectory(new File(testOutputDir));

		DaoGenRunner.main(args);

		String[] tablesArray = StringUtils.split(tables, ",");
		String path = srcOutputDir.matches("([a-z,A-Z]:)?[/\\\\]+.*") ? ""
		    : System.getProperty("user.dir");

		for (int i = 0; i < tablesArray.length; i++) {
			String table = tablesArray[i];
			String dtoFilename = StringUtils.replacePattern(
			    StringUtils.replaceChars(table, " $_", ""), "[%]", "*")
			    + "Dto.java";
			IOFileFilter fileFilter =
			    new WildcardFileFilter(dtoFilename, IOCase.INSENSITIVE);
			File dtoPath =
			    new File(FilenameUtils.normalize(path + "/" + srcOutputDir + "/"
			        + StringUtils.replace(dtoPackageName, ".", "/") + "/"));
			Collection<File> listFiles =
			    FileUtils.listFiles(dtoPath, fileFilter, null);
			assertFalse("Expected " + dtoFilename + " to exist in " + dtoPath,
			    listFiles.isEmpty());
		}

	}
}
