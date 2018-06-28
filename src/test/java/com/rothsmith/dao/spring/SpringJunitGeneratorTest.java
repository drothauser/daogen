/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.spring;

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

import com.rothsmith.dao.core.ArtifactGenerator;
import com.rothsmith.dao.core.GeneratorException;
import com.rothsmith.dao.core.Params;
import com.rothsmith.dao.core.ParamsBuilder;
import com.rothsmith.dao.core.TextUtils;
import com.rothsmith.dao.core.VelocityGenerator;
import com.rothsmith.dao.db.DbMetaDataQueryParams;
import com.rothsmith.dao.db.DbMetaDataQueryParamsBuilder;
import com.rothsmith.dao.db.IdxMetaData;
import com.rothsmith.dao.db.IdxMetaDataQuery;
import com.rothsmith.dao.db.PKMetaData;
import com.rothsmith.dao.db.PKMetaDataQuery;
import com.rothsmith.dao.db.TblColMetaDataQuery;
import com.rothsmith.dao.db.TblMetaData;
import com.rothsmith.dao.db.TblMetaDataQuery;
import com.rothsmith.dao.def.DefGenerator;
import com.rothsmith.dao.def.DefMetaData;
import com.rothsmith.dao.dto.DtoGenerator;
import com.rothsmith.dao.spring.AppCfgArtifactGenerator;
import com.rothsmith.dao.spring.DaoCfgArtifactGenerator;
import com.rothsmith.dao.spring.SpringArtifactGenerator;
import com.rothsmith.dao.spring.SpringJunitGenerator;

/**
 * Test for generating the Spring Junit test classes.
 * 
 * @author drothauser
 */
@RunWith(Parameterized.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class SpringJunitGeneratorTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for DbUtilsJunitGeneratorTest.
	 */
	private static final Logger LOGGER = LoggerFactory
	    .getLogger(SpringJunitGeneratorTest.class);

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
		        { ParamsBuilder.newInstance().withTableTypes("TABLE")
		            .withTableNames("PARTY")
		            .withOutputDir("target/gen/src/main/java")
		            .withDtoPackageName("com.fcci.party")
		            .withDefFileTemplate("/daodefs.vm")
		            .withDefFile("target/gen/daodefs.xml")
		            .withDtoTemplate("/dto.vm")
		            .withSpringDaoCtxFile("daobeans.xml")
		            .withSpringAppCtxFile("applicationConfig.xml")
		            .withSpringDaoCtxTemplate("spring/dao.vm")
		            .withSpringAppCtxTemplate("spring/applicationContext.vm")
		            .withTestOutputDir("target/gen/src/test/java")
		            .withTestPackageName("com.fcci.party").withSchema("TEST")
		            .withJunitTemplate("spring/junit.vm").build(), },
		        { ParamsBuilder.newInstance().withTableTypes("TABLE")
		            .withTableNames("STATE")
		            .withOutputDir("target/gen/src/main/java")
		            .withDtoPackageName("com.fcci.state")
		            .withDefFileTemplate("/daodefs.vm")
		            .withDefFile("target/gen/daodefs.xml")
		            .withDtoTemplate("/dto.vm")
		            .withSpringDaoCtxFile("daobeans.xml")
		            .withSpringAppCtxFile("applicationConfig.xml")
		            .withSpringDaoCtxTemplate("spring/dao.vm")
		            .withSpringAppCtxTemplate("spring/applicationContext.vm")
		            .withTestOutputDir("target/gen/src/test/java")
		            .withTestPackageName("com.fcci.state").withSchema("TEST")
		            .withJunitTemplate("spring/junit.vm").build(), },
		        { ParamsBuilder.newInstance().withTableTypes("TABLE")
		            .withTableNames("PRESIDENT")
		            .withOutputDir("target/gen/src/main/java")
		            .withDtoPackageName("com.fcci.president")
		            .withDefFileTemplate("/daodefs.vm")
		            .withDefFile("target/gen/daodefs.xml")
		            .withDtoTemplate("/dto.vm")
		            .withSpringDaoCtxFile("daobeans.xml")
		            .withSpringAppCtxFile("applicationConfig.xml")
		            .withSpringDaoCtxTemplate("spring/dao.vm")
		            .withSpringAppCtxTemplate("spring/applicationContext.vm")
		            .withTestOutputDir("target/gen/src/test/java")
		            .withTestPackageName("com.fcci.president")
		            .withSchema("TEST").withJunitTemplate("spring/junit.vm")
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
	public SpringJunitGeneratorTest(Params params) {
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

		ArtifactGenerator springDaoGenerator = createSpringDaoGenerator();
		springDaoGenerator.generate();

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

		DbMetaDataQueryParams params =
		    DbMetaDataQueryParamsBuilder.metaDataQueryParams()
		        .withDataSource(dataSource).withSchemaPattern(schema)
		        .withTableNames(tableName).build();

		Map<Integer, PKMetaData> pKMetaData =
		    pKMetaDataQuery.fetchMetaData(params);
		defMetaData.setPkMap(pKMetaData);

		Map<String, List<IdxMetaData>> idxMetaData =
		    idxMetaDataQuery.fetchMetaData(params);
		defMetaData.setIndexMap(idxMetaData);

		defMetaData.setColumnMap(tblColMetaDataQuery.fetchMetaData(params));

		defMetaData.setTestOutputDir(testOutputDir);
		defMetaData.setTestPackageName(testPackageName);

		return defMetaData;
	}

	/**
	 * Return an instance of {@link SpringArtifactGenerator} for testing.
	 * 
	 * @return {@link SpringArtifactGenerator}
	 * @throws GeneratorException
	 *             possible error
	 */
	private ArtifactGenerator createSpringDaoGenerator()
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
		// CHECKSTYLE:OFF Magic number 4 ok here.
		generatorList = new ArrayList<ArtifactGenerator>(4);
		// CHECKSTYLE:ON
		generatorList.add(new DtoGenerator(dataSource, defFileDoc,
		    velocityGenerator));
		generatorList.add(new DaoCfgArtifactGenerator(defFileDoc,
		    velocityGenerator));
		generatorList.add(new AppCfgArtifactGenerator(defFileDoc,
		    velocityGenerator));
		generatorList.add(new SpringJunitGenerator(dataSource, defFileDoc,
		    velocityGenerator));
		return new SpringArtifactGenerator(generatorList);
	}
}
