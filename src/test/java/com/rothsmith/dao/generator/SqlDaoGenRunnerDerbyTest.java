/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.generator;

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

import com.rothsmith.dao.generator.DaoGenerator;
import com.rothsmith.dao.runner.DaoGenRunner;
import com.rothsmith.dao.spring.BaseSpringTest;

/**
 * Tests for {@link DaoGenerator}.
 * 
 * @author drothauser
 */
public class SqlDaoGenRunnerDerbyTest
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
	 * Test the {@link DaoGenerator}.
	 * 
	 * @throws Exception
	 *             possible exception
	 */
	@Test
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public final void testDaoGenRunner() throws Exception {

		String targetGenDir = FilenameUtils.normalize(
		    System.getProperty("user.dir") + "/../daogen-test-spring-derby");

		String tables = "STATE,PARTY,PRESIDENT";

		String srcOutputDir =
		    FilenameUtils.normalize(targetGenDir + "/src/main/java");
		String dtoPackageName = "com.fcci.genericdao.derby";
		String testOutputDir =
		    FilenameUtils.normalize(targetGenDir + "/src/test/java");

		Properties props = new Properties();
		props.load(
		    this.getClass().getResourceAsStream("/derby/unitils.properties"));

		List<String> argList = new ArrayList<String>();
		argList.add("--jdbcurl");
		argList.add(props.getProperty("database.url"));
		argList.add("--dbuser");
		argList.add(props.getProperty("database.userName"));
		argList.add("--dbpassword");
		argList.add(props.getProperty("database.password"));
		argList.add("--driverclass");
		argList.add(props.getProperty("database.driverClassName"));
		argList.add("--types");
		argList.add("TABLE");
		argList.add("--names");
		argList.add(tables);
		argList.add("--outputdir");
		argList.add(srcOutputDir);
		argList.add("--dtopkg");
		argList.add(dtoPackageName);
		argList.add("--deftpl");
		argList.add("/daodefs.vm");
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
		argList.add("spring/junit.vm");
		argList.add("--propsfile");
		argList.add("classpath:derby/unitils.properties");
		argList.add("--dtotpl");
		argList.add("/dto.vm");
		argList.add("--springdaofiletpl");
		argList.add("spring/dao.vm");
		argList.add("--springdaofile");
		argList.add(FilenameUtils
		    .normalize(targetGenDir + "/src/main/resources/dao.xml"));
		argList.add("--springappctxtpl");
		argList.add("spring/applicationContext.vm");
		argList.add("--springappctx");
		argList.add(FilenameUtils.normalize(
		    targetGenDir + "/src/main/resources/applicationContext.xml"));
		argList.add("--sqlstmts");
		argList.add("SELECT * FROM TEST.PRESIDENT");

		String[] args = argList.toArray(new String[argList.size()]);

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
