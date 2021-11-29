/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.spring;

import static org.junit.Assert.assertTrue;

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
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.def.DefGenerator;
import com.rothsmith.dao.dto.DtoGenerator;
import com.rothsmith.dao.spring.AppCfgArtifactGenerator;
import com.rothsmith.dao.spring.DaoCfgArtifactGenerator;
import com.rothsmith.dao.spring.SpringArtifactGenerator;
import com.rothsmith.dao.spring.SpringJunitGenerator;

import net.rothsmith.dao.core.ArtifactGenerator;
import net.rothsmith.dao.core.GeneratorException;
import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Tests for {@link SpringArtifactGenerator}.
 * 
 * @author drothauser
 */
public class SpringArtifactGeneratorTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for DaoGeneratorTest.
	 */
	private static final Logger LOGGER = LoggerFactory
	    .getLogger(SpringArtifactGeneratorTest.class);

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

		setUpDatabase(DERBY_TEST_PROPS);

	}

	/**
	 * Test the {@link SpringArtifactGenerator}.
	 * 
	 * @throws Exception
	 *             possible exception
	 */
	@Test
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	public final void testSpringArtifactGenerator() throws Exception {

		LOGGER.info(String.format("Testing with the following parameters:%n%s",
		    testParams));

		ArtifactGenerator artifactGenerator = createSpringArtifactGenerator();

		artifactGenerator.generate();

		String[] tablesArray =
		    StringUtils.split(
		        StringUtils.replace("PARTY,STATE,PRESIDENT", " ", ""), ",");

		for (int i = 0; i < tablesArray.length; i++) {
			String tableCamelcase =
			    StringUtils.replace(
			        WordUtils.capitalizeFully(tablesArray[i], '_'), "_", "");
			String dtoJavaFile =
			    testParams.getOutputDir()
			        + IOUtils.DIR_SEPARATOR
			        + StringUtils.replace("com.fcci.genericdao", ".",
			            Character.toString(IOUtils.DIR_SEPARATOR))
			        + IOUtils.DIR_SEPARATOR
			        + StringUtils.capitalize(tableCamelcase) + "Dto.java";

			assertTrue("Expected " + dtoJavaFile + " to exist", FileUtils
			    .getFile(dtoJavaFile).exists());
		}

	}

	/**
	 * Test the {@link SpringArtifactGenerator} with no specified table names.
	 * 
	 * @throws Exception
	 *             possible exception
	 */
	@Test(expected = Exception.class)
	public final void testSpringArtifactGeneratorNoTables() throws Exception {

		testParams.setTableNames(null);

		ArtifactGenerator springDaoGenerator = createSpringArtifactGenerator();

		springDaoGenerator.generate();

	}

	/**
	 * Test the {@link SpringArtifactGenerator} with an empty DaoBeanFilename.
	 * 
	 * @throws GeneratorException
	 *             possible generator exception
	 * @throws IOException
	 *             possible I/O error reading the definitions file
	 * @throws JDOMException
	 *             possible I/O parsing the definitions file
	 */
	@Test(expected = GeneratorException.class)
	public final void testSpringArtifactGeneratorNullDaoCtx()
	        throws GeneratorException, JDOMException, IOException {

		testParams.setSpringDaoCtxFile(null);

		ArtifactGenerator springDaoGenerator = createSpringArtifactGenerator();

		springDaoGenerator.generate();

	}

	/**
	 * Return an instance of {@link SpringArtifactGenerator} for testing.
	 * 
	 * @return {@link SpringArtifactGenerator}
	 * @throws GeneratorException
	 *             possible error
	 */
	private ArtifactGenerator createSpringArtifactGenerator()
	        throws GeneratorException {
		String defFileTemplate = testParams.getDefFileTemplate();
		String defFile = testParams.getDefFile();
		LOGGER.info(String.format("Testing DefGenerator constructed with:"
		    + "%nDefinitions File Template: %s"
		    + "%nGenerated Definitions File: %s", defFileTemplate, defFile));
		DefGenerator defGenerator = new DefGenerator(velocityGenerator);
		Document defFileDoc = defGenerator.generate(dataSource, testParams);

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
