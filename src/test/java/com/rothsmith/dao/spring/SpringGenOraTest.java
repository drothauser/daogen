/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.spring;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.runner.DaoGenRunner;

/**
 * {@link DaoGenRunner} Testing with Oracle.
 * 
 * @author drothauser
 */
public class SpringGenOraTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for SpringGenOraTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(SpringGenOraTest.class);

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
		    System.getProperty("user.dir") + "/../daogen-test-spring-ora");

		// String tables = "USERSGENERAL";
		String tables = "APPLICATION,USERSGENERAL,GROUPS";

		// String tables =
		// "DR$MLCRTSXPF_CTX_IDX1$K," + ",FCCI_KY_TAX_CODE,"
		// + "FCCI_KY_TAX_CODE_DESCR," + "QUERYJOBS";

		// String tables = "QUERYJOBS";

		// String tables = "%";

		String srcOutputDir =
		    FilenameUtils.normalize(targetGenDir + "/src/main/java");
		String dtoPackageName = "com.fcci.genericdao.ora";
		String testOutputDir =
		    FilenameUtils.normalize(targetGenDir + "/src/test/java");

		String sql0 =
		    "SELECT AO.APPLICATION_OBJECT_NAME,OT.OBJECT_TYPE,G.GROUPDESC,"
		        + "P.PRIVILEGE_DESC,PT.PRIVILEGE_TYPE FROM GROUP_OBJECTS GO,"
		        + "APPLICATION_OBJECTS AO, GROUP_OBJECT_PRIVILEGES GOP,"
		        + "PRIVILEGE P, PRIVILEGE_TYPE PT, OBJECT_TYPE OT,"
		        + "APPLICATION A,GROUPS G,shareduser.groupusers grpusers "
		        + "WHERE GO.APPLICATION_OBJECTID = AO.APPLICATION_OBJECTID "
		        + "AND GOP.GROUP_OBJECTID = GO.GROUP_OBJECTID AND "
		        + "GOP.PRIVILEGEID = P.PRIVILEGEID AND P.PRIVILEGE_TYPEID = "
		        + "PT.PRIVILEGE_TYPEID AND AO.OBJECT_TYPEID = OT.OBJECT_TYPEID "
		        + "AND AO.APPLICATIONID = A.APPLICATIONID AND GO.GROUPID = "
		        + "G.GROUPID and grpusers.GROUPID=G.GROUPID and "
		        + "grpusers.USERIDCODE=8599 order by application_object_name";

		String sql1 =
		    "select a.useridcode, c.USERID, d.FIRSTNAME, d.LASTNAME, a.GROUPID, b.GROUPDESC"
		        + " from GROUPUSERS a join groups b on a.GROUPID = b.GROUPID"
		        + " join users c on a.USERIDCODE = c.USERIDCODE"
		        + " join usersgeneral d on d.USERIDCODE = c.USERIDCODE"
		        + " where a.USERIDCODE = 1";

		String[] args = buildArgs(targetGenDir, tables, sql0 + ";" + sql1,
		    srcOutputDir, dtoPackageName, testOutputDir);

		FileUtils.deleteDirectory(new File(srcOutputDir));
		FileUtils.deleteDirectory(new File(testOutputDir));

		LOGGER.info(String.format("Testing with arguments:%n%s",
		    StringUtils.join(args, "\n")));

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

	/**
	 * Build arguments for the DAO generator test.
	 * 
	 * @param targetGenDir
	 *            target directory for generated artifacts
	 * @param tables
	 *            DB tables
	 * @param sql
	 *            loose SQL statement(s)
	 * @param srcOutputDir
	 *            target directory for generated source code
	 * @param dtoPackageName
	 *            DTO package name
	 * @param testOutputDir
	 *            target directory for JUnit test classes
	 * @return array of arguments
	 * @throws IOException
	 *             possible I/O error reading the properties file
	 */
	@SuppressWarnings("PMD.UseObjectForClearerAPI")
	private String[] buildArgs(String targetGenDir, String tables, String sql,
	    String srcOutputDir, String dtoPackageName, String testOutputDir)
	            throws IOException {
		Properties props = new Properties();

		String propsFile = "oracle/daogen-ora.properties";
		props.load(Thread.currentThread().getContextClassLoader()
		    .getResourceAsStream(propsFile));

		// TODO set defaults in Params object to reduce number of parameters.
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
		argList.add("SHAREDUSER");
		argList.add("--junittpl");
		argList.add("spring/junit.vm");
		argList.add("--propsfile");
		argList.add("daogen-ora.properties");
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
		argList.add(sql);

		return argList.toArray(new String[argList.size()]);

	}
}
