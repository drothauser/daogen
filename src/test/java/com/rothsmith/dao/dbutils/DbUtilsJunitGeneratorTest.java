/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.dbutils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.IdxMetaData;
import com.rothsmith.dao.db.IdxMetaDataQuery;
import com.rothsmith.dao.db.PKMetaData;
import com.rothsmith.dao.db.PKMetaDataQuery;
import com.rothsmith.dao.db.TblColMetaDataQuery;
import com.rothsmith.dao.db.TblMetaData;
import com.rothsmith.dao.db.TblMetaDataQuery;
import com.rothsmith.dao.dbutils.DbUtilsArtifactGenerator;
import com.rothsmith.dao.dbutils.DbUtilsJunitGenerator;
import com.rothsmith.dao.dbutils.PropsGenerator;
import com.rothsmith.dao.def.DefGenerator;
import com.rothsmith.dao.def.DefMetaData;
import com.rothsmith.dao.dto.DtoGenerator;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.Params;
import net.rothsmith.dao.core.ParamsBuilder;
import net.rothsmith.dao.core.TextUtils;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Test for generating the DbUtils Junit classes.
 * 
 * @author drothauser
 */
@RunWith(Parameterized.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class DbUtilsJunitGeneratorTest
        extends BaseDbUtilsTest {

	/**
	 * SLF4J Logger for DbUtilsJunitGeneratorTest.
	 */
	private static final Logger LOGGER = LoggerFactory
	    .getLogger(DbUtilsJunitGeneratorTest.class);

	/**
	 * {@link VelocityGenerator} for generating DAO artifacts.
	 */
	private final VelocityGenerator velocityGenerator = new VelocityGenerator();

	/**
	 * {@link TblMetaDataQuery} utility for getting table metadata.
	 */
	private final TblMetaDataQuery tblMetaDataQuery = new TblMetaDataQuery();

	/**
	 * {@link TblColMetaDataQuery} utility for getting table metadata.
	 */
	private final TblColMetaDataQuery tblColMetaDataQuery =
	    new TblColMetaDataQuery();

	/**
	 * {@link PKMetaDataQuery} utility for getting a table primary key metadata.
	 */
	private final PKMetaDataQuery pKMetaDataQuery = new PKMetaDataQuery();

	/**
	 * {@link IdxMetaDataQuery} utility for getting table index metadata.
	 */
	private final IdxMetaDataQuery idxMetaDataQuery = new IdxMetaDataQuery();

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
	 * @return a {@link Collection} of elements containing an input and expected
	 *         outcome stored in an array.
	 */
	@Parameterized.Parameters
	public static Collection<Object[]> createParams() {

		Object[][] testCases =
		    new Object[][] {
		        { ParamsBuilder
		            .newInstance()
		            .withTableTypes("TABLE")
		            .withTableNames("PARTY")
		            .withDbUtilsPropsTemplate("dbutils/propsfile.vm")
		            .withDbUtilsPropsDir(
		                FilenameUtils.normalize(targetGenDir
		                    + "src/main/resources"))
		            .withOutputDir("target/gen/src/main/java")
		            .withDtoPackageName("com.fcci.presidents.party")
		            .withDefFileTemplate("/daodefs.vm")
		            .withDefFile("target/gen/daodefs.xml")
		            .withDtoTemplate("/dto.vm")

		            .withJndiName("java:/comp/env/jdbc/TestDS")
		            .withTestOutputDir("target/gen/src/test/java")
		            .withTestPackageName("com.fcci.presidents.party")
		            .withSchema("TEST").withJunitTemplate("dbutils/junit.vm")
		            .build(), },
		        { ParamsBuilder
		            .newInstance()
		            .withTableTypes("TABLE")
		            .withTableNames("STATE")
		            .withDbUtilsPropsTemplate("dbutils/propsfile.vm")
		            .withDbUtilsPropsDir(
		                FilenameUtils.normalize(targetGenDir
		                    + "src/main/resources"))
		            .withOutputDir("target/gen/src/main/java")
		            .withDtoPackageName("com.fcci.presidents.state")
		            .withDefFileTemplate("/daodefs.vm")
		            .withDefFile("target/gen/daodefs.xml")
		            .withDtoTemplate("/dto.vm")
		            .withTestOutputDir("target/gen/src/test/java")
		            .withTestPackageName("com.fcci.presidents.state")
		            .withSchema("TEST").withJunitTemplate("dbutils/junit.vm")
		            .build(), },
		        { ParamsBuilder
		            .newInstance()
		            .withTableTypes("TABLE")
		            .withTableNames("PRESIDENT")
		            .withDbUtilsPropsTemplate("dbutils/propsfile.vm")
		            .withDbUtilsPropsDir(
		                FilenameUtils.normalize(targetGenDir
		                    + "src/main/resources"))
		            .withOutputDir("target/gen/src/main/java")
		            .withDtoPackageName("com.fcci.presidents.president")
		            .withDefFileTemplate("/daodefs.vm")
		            .withDefFile("target/gen/daodefs.xml")
		            .withDtoTemplate("/dto.vm")
		            .withTestOutputDir("target/gen/src/test/java")
		            .withTestPackageName("com.fcci.presidents.president")
		            .withSchema("TEST").withJunitTemplate("dbutils/junit.vm")
		            .build(), }, };

		return Arrays.asList(testCases);

	}

	/**
	 * The {@link Params} to test.
	 */
	private final Params params;

	/**
	 * Constructor to setup the test.
	 * 
	 * @param params
	 *            The {@link Params} for the test.
	 */
	public DbUtilsJunitGeneratorTest(Params params) {
		this.params = params;
	}

	/**
	 * Test of createDaoConfig method, of class DefGenerator.
	 * 
	 * @throws Exception
	 *             possible exception
	 */
	@Test
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public void testGenerateJunits() throws Exception {

		String tableNames = params.getTableNames();
		String[] tableNamesArray = StringUtils.split(tableNames, ",");

		String schemaPattern = params.getSchemaPattern();
		DbMetaDataQueryParams dbMetaDataQueryParams =
		    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
		        .withDataSource(dataSource).withSchemaPattern(schemaPattern)
		        .withTableTypes(params.getTableTypes()).build();

		List<TblMetaData> tblMetaDataList = new ArrayList<TblMetaData>();
		for (int i = 0; i < tableNamesArray.length; i++) {
			dbMetaDataQueryParams.setTableNames(tableNamesArray[i]);
			List<TblMetaData> metaData =
			    tblMetaDataQuery.fetchMetaData(dbMetaDataQueryParams);
			CollectionUtils.addAll(tblMetaDataList, metaData.iterator());
		}

		// Build list of DAO metadata objects from table metadata list:
		List<DefMetaData> daoMetaDataList = new ArrayList<DefMetaData>();

		String testOutputDir = params.getTestOutputDir();
		String outputDir = params.getOutputDir();
		String dtoPackageName = params.getDtoPackageName();
		String testPackageName = params.getTestPackageName();

		for (TblMetaData tblMetaData : tblMetaDataList) {

			DefMetaData defMetaData =
			    buildDaoMetaData(schemaPattern, outputDir, dtoPackageName,
			        params.getTestOutputDir(), params.getTestPackageName(),
			        tblMetaData);

			daoMetaDataList.add(defMetaData);

		}

		ArtifactGenerator dbUtilsGenerator = createDbUtilsGenerator();
		dbUtilsGenerator.generate();

		for (TblMetaData tblMetaData : tblMetaDataList) {

			String tableName = tblMetaData.getTableName();

			String dtoFilename =
			    buildDtoFilename(outputDir, dtoPackageName, tableName);

			String daoTestFilename =
			    buildDaoTestFilename(testOutputDir, testPackageName, tableName);

			assertTrue(String.format("Expected both %s and %s to exist.",
			    dtoFilename, daoTestFilename), new File(dtoFilename).exists()
			    && new File(daoTestFilename).exists());
		}

	}

	/**
	 * This method builds the fully qualified name of the DTO Java file.
	 * 
	 * @param outputDir
	 *            base source directory of the DTO package
	 * @param packageName
	 *            Java package
	 * @param tableName
	 *            table name
	 * @return the fully qualified name of the DTO java file
	 */
	private String buildDtoFilename(String outputDir, String packageName,
	    String tableName) {

		String path = System.getProperty("user.dir");

		String dtoTableName =
		    StringUtils.capitalize(TextUtils.convertToCamelCase(tableName));
		String dtoFilename =
		    path
		        + IOUtils.DIR_SEPARATOR
		        + outputDir
		        + IOUtils.DIR_SEPARATOR
		        + StringUtils.replaceChars(packageName, '.',
		            IOUtils.DIR_SEPARATOR) + IOUtils.DIR_SEPARATOR
		        + dtoTableName + "Dto.java";

		return FilenameUtils.normalize(dtoFilename);
	}

	/**
	 * This method builds the fully qualified name of the DTO Java file.
	 * 
	 * @param testOutputDir
	 *            base source directory of the DTO package
	 * @param packageName
	 *            Java package
	 * @param tableName
	 *            table name
	 * @return the fully qualified name of the DTO java file
	 */
	private String buildDaoTestFilename(String testOutputDir,
	    String packageName, String tableName) {

		String path = System.getProperty("user.dir");

		String dtoTableName =
		    StringUtils.capitalize(TextUtils.convertToCamelCase(tableName));
		String dtoFilename =
		    path
		        + IOUtils.DIR_SEPARATOR
		        + testOutputDir
		        + IOUtils.DIR_SEPARATOR
		        + StringUtils.replaceChars(packageName, '.',
		            IOUtils.DIR_SEPARATOR) + IOUtils.DIR_SEPARATOR
		        + dtoTableName + "DaoTest.java";

		return FilenameUtils.normalize(dtoFilename);
	}

	/**
	 * This method a {@link DefMetaData} object from information in the
	 * {@link TblMetaData} parameter.
	 * 
	 * @param schema
	 *            A filter to narrow the object search list to a particular
	 *            schema.
	 * @param outputDir
	 *            output directory of the generated DAO
	 * @param packageName
	 *            Java package
	 * @param testOutputDir
	 *            output directory for test packages (e.g. &quot;test&quot;).
	 * @param testPackageName
	 *            test package name
	 * @param tblMetaData
	 *            {@link TblMetaData} object which contains the metadata of a
	 *            particular table
	 * 
	 * @return a {@link DefMetaData} object
	 * @throws SQLException
	 *             possible SQL error
	 */
	@SuppressWarnings("PMD.UseObjectForClearerAPI")
	private DefMetaData buildDaoMetaData(String schema, String outputDir,
	    String packageName, String testOutputDir, String testPackageName,
	    TblMetaData tblMetaData) throws SQLException {

		DefMetaData defMetaData = new DefMetaData();

		String username = System.getProperty("user.name");

		String tableName = tblMetaData.getTableName();

		defMetaData.setPackageName(packageName);
		defMetaData.setClassName(TextUtils.convertToCamelCase(tableName));
		defMetaData.setUser(username);

		defMetaData.setOutputDir(outputDir);
		defMetaData.setTblMetaData(tblMetaData);

		DbMetaDataQueryParams dmdqParams =
		    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
		        .withDataSource(dataSource).withSchemaPattern(schema)
		        .withTableNames(tableName).build();

		Map<Integer, PKMetaData> pKMetaData =
		    pKMetaDataQuery.fetchMetaData(dmdqParams);
		defMetaData.setPkMap(pKMetaData);

		Map<String, List<IdxMetaData>> idxMetaData =
		    idxMetaDataQuery.fetchMetaData(dmdqParams);
		defMetaData.setIndexMap(idxMetaData);

		defMetaData.setColumnMap(tblColMetaDataQuery.fetchMetaData(dmdqParams));

		defMetaData.setTestOutputDir(testOutputDir);
		defMetaData.setTestPackageName(testPackageName);

		return defMetaData;
	}

	/**
	 * Return an instance of {@link DbUtilsArtifactGenerator} for testing.
	 * 
	 * @return {@link DbUtilsArtifactGenerator}
	 * @throws GeneratorException
	 *             possible error
	 */
	private ArtifactGenerator createDbUtilsGenerator()
	        throws GeneratorException {
		String defFileTemplate = params.getDefFileTemplate();
		String defFile = params.getDefFile();
		LOGGER.info(String.format("Testing DefGenerator constructed with:"
		    + "%nDefinitions File Template: %s"
		    + "%nGenerated Definitions File: %s", defFileTemplate, defFile));
		DefGenerator defGenerator = new DefGenerator(velocityGenerator);
		Document defFileDoc = defGenerator.generate(dataSource, params);

		List<ArtifactGenerator> generatorList =
		    new ArrayList<ArtifactGenerator>();

		generatorList.add(new DtoGenerator(dataSource, defFileDoc,
		    velocityGenerator));

		generatorList.add(new PropsGenerator(defFileDoc, velocityGenerator));

		generatorList.add(new DbUtilsJunitGenerator(dataSource, defFileDoc,
		    velocityGenerator));

		return new DbUtilsArtifactGenerator(generatorList);
	}

}
