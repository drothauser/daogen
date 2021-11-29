/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.dbutils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.dbutils.DbUtilsArtifactGenerator;
import com.rothsmith.dao.dbutils.DbUtilsJunitGenerator;
import com.rothsmith.dao.dbutils.PropsGenerator;
import com.rothsmith.dao.dbutils.UnitilsGenerator;
import com.rothsmith.dao.def.DefGenerator;
import com.rothsmith.dao.dto.DtoGenerator;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.Params;
import net.rothsmith.dao.core.ParamsBuilder;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Tests for {@link DbUtilsArtifactGenerator}.
 * 
 * @author drothauser
 */
public class DbUtilsArtifactGeneratorTest
        extends BaseDbUtilsTest {

	/**
	 * SLF4J Logger for DaoGeneratorTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(DbUtilsArtifactGeneratorTest.class);

	/**
	 * {@link ParamBuilder} for testing.
	 */
	private ParamsBuilder paramBuilder;

	/**
	 * {@link VelocityGenerator} for generating DAO artifacts.
	 */
	private final VelocityGenerator velocityGenerator = new VelocityGenerator();

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

		FileUtils.deleteDirectory(new File(targetGenDir));

		setUpDatabase(DERBY_TEST_PROPS);

	}

	/**
	 * Initializes base test parameters.
	 * 
	 * @throws java.lang.Exception
	 *             possible exception
	 */
	@Before
	public final void setUp() throws Exception {
		paramBuilder = initParams();
	}

	/**
	 * Test the {@link DbUtilsArtifactGenerator}.
	 * 
	 * @throws Exception
	 *             possible exception
	 */
	@Test
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	public final void testGoodTables() throws Exception {

		paramBuilder.withTableTypes("TABLE, VIEW");
		Params testParams =
		    paramBuilder.withTableNames("PARTY,STATE,PRESIDENT").build();

		LOGGER.info(String.format("Testing with the following parameters:%n%s",
		    testParams));

		ArtifactGenerator artifactGenerator =
		    createDbUtilsGenerator(testParams);

		artifactGenerator.generate();

		String[] tablesArray = StringUtils
		    .split(StringUtils.replace("PARTY,STATE,PRESIDENT", " ", ""), ",");

		for (int i = 0; i < tablesArray.length; i++) {
			String tableCamelcase = StringUtils.replace(
			    WordUtils.capitalizeFully(tablesArray[i], '_'), "_", "");
			String dtoJavaFile =
			    testParams.getOutputDir() + IOUtils.DIR_SEPARATOR
			        + StringUtils.replace(testParams.getDtoPackageName(), ".",
			            Character.toString(IOUtils.DIR_SEPARATOR))
			    + IOUtils.DIR_SEPARATOR + StringUtils.capitalize(tableCamelcase)
			    + "Dto.java";

			assertTrue("Expected " + dtoJavaFile + " to exist",
			    FileUtils.getFile(dtoJavaFile).exists());
		}

	}

	/**
	 * Test the {@link DbUtilsArtifactGenerator} with no specified table names.
	 * 
	 * @throws Exception
	 *             Expected {@link Exception} due to null table
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testNoTables() throws Exception {

		Params testParams = paramBuilder.build();

		ArtifactGenerator dbUtilsDaoGenerator =
		    createDbUtilsGenerator(testParams);

		dbUtilsDaoGenerator.generate();

	}

	/**
	 * Test the {@link DbUtilsArtifactGenerator} with no specified table names.
	 * 
	 * @throws Exception
	 *             Expected {@link Exception} due to null table
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testNoTableTypes() throws Exception {

		Params testParams =
		    paramBuilder.withTableNames("PARTY,STATE,PRESIDENT").build();

		ArtifactGenerator dbUtilsDaoGenerator =
		    createDbUtilsGenerator(testParams);

		dbUtilsDaoGenerator.generate();

	}

	/**
	 * Test the {@link DbUtilsArtifactGenerator} with no specified table names.
	 * 
	 * @throws Exception
	 *             Expected {@link Exception} due to null table
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testIllegalTableTypes() throws Exception {

		Params testParams = paramBuilder.withTableNames("PARTY,STATE,PRESIDENT")
		    .withTableTypes("TABLE,BOGUS").build();

		ArtifactGenerator dbUtilsDaoGenerator =
		    createDbUtilsGenerator(testParams);

		dbUtilsDaoGenerator.generate();

	}

	/**
	 * Test the {@link DbUtilsArtifactGenerator} with an empty DaoBeanFilename.
	 * 
	 * @throws GeneratorException
	 *             possible generator exception
	 * @throws IOException
	 *             possible I/O error reading the definitions file
	 * @throws JDOMException
	 *             possible I/O parsing the definitions file
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testNullProps()
	        throws GeneratorException, JDOMException, IOException {

		Params testParams = paramBuilder.withDbUtilsPropsDir(null).build();

		ArtifactGenerator artifactGenerator =
		    createDbUtilsGenerator(testParams);

		artifactGenerator.generate();

	}

	/**
	 * Return an instance of {@link DbUtilsArtifactGenerator} for testing.
	 * 
	 * @param testParams
	 *            {@link Params} object to create the {@link ArtifactGenerator}
	 *            with.
	 * 
	 * @return {@link DbUtilsArtifactGenerator}
	 * @throws GeneratorException
	 *             possible error
	 */
	private ArtifactGenerator createDbUtilsGenerator(Params testParams)
	        throws GeneratorException {
		String defFileTemplate = testParams.getDefFileTemplate();
		String defFile = testParams.getDefFile();
		LOGGER.info(String.format(
		    "Testing DefGenerator constructed with:"
		        + "%nDefinitions File Template: %s"
		        + "%nGenerated Definitions File: %s",
		    defFileTemplate, defFile));
		DefGenerator defGenerator = new DefGenerator(velocityGenerator);
		Document defFileDoc = defGenerator.generate(dataSource, testParams);

		List<ArtifactGenerator> generatorList =
		    new ArrayList<ArtifactGenerator>();

		generatorList
		    .add(new DtoGenerator(dataSource, defFileDoc, velocityGenerator));
		generatorList.add(new PropsGenerator(defFileDoc, velocityGenerator));

		generatorList.add(new DbUtilsJunitGenerator(dataSource, defFileDoc,
		    velocityGenerator));
		generatorList.add(new UnitilsGenerator(defFileDoc, velocityGenerator));

		return new DbUtilsArtifactGenerator(generatorList);
	}
}
