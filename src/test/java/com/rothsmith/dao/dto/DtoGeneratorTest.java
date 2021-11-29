/*
 * (c) 2013 Rothsmith, LLC, All Rights Reserved
 */
package com.rothsmith.dao.dto;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.def.DefGenerator;
import com.rothsmith.dao.dto.DtoGenerator;
import com.rothsmith.dao.spring.BaseSpringTest;

import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Test {@link DtoGenerator}.
 * 
 * @author drothauser
 */
public class DtoGeneratorTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for DtoGeneratorTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(DtoGeneratorTest.class);

	/**
	 * Definitions XML file as JDom {@link Document}.
	 */
	private Document defFileDoc;

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
	 * Generate DAO definitions file for testing.
	 * 
	 * @throws Exception
	 *             possible error
	 */
	@Before
	public void setUp() throws Exception {

		String defFileTemplate = testParams.getDefFileTemplate();
		String defFile = FilenameUtils.normalize(testParams.getDefFile());
		LOGGER.info(String.format(
		    "%n%nTesting DefGenerator constructed with:"
		        + "%nDefinitions File Template: %s"
		        + "%nGenerated Definitions File: %s%n",
		    defFileTemplate, defFile));
		DefGenerator defGenerator = new DefGenerator(new VelocityGenerator());
		defGenerator.generate(dataSource, testParams);

		SAXBuilder builder = new SAXBuilder();
		defFileDoc = builder.build(new File(defFile));

	}

	/**
	 * Test {@link DtoGenerator#generate()}.
	 * 
	 * @throws Exception
	 *             possible exception
	 */
	@Test
	public final void testDtoGeneration() throws Exception {

		DtoGenerator dtoGenerator =
		    new DtoGenerator(dataSource, defFileDoc, new VelocityGenerator());

		dtoGenerator.generate();

		String[] tablesArray =
		    StringUtils.split(testParams.getTableNames(), ",");
		String path =
		    testParams.getOutputDir().matches("([a-z,A-Z]:)?[/\\\\]+.*") ? ""
		        : System.getProperty("user.dir");

		for (int i = 0; i < tablesArray.length; i++) {
			String dtoJavaFile =
			    FilenameUtils.normalize(path + IOUtils.DIR_SEPARATOR
			        + testParams.getOutputDir() + IOUtils.DIR_SEPARATOR
			        + StringUtils.replace(testParams.getDtoPackageName(), ".",
			            Character.toString(IOUtils.DIR_SEPARATOR))
			    + IOUtils.DIR_SEPARATOR
			    + WordUtils.capitalizeFully(
			        StringUtils.replaceChars(tablesArray[i], "_", ""))
			    + "Dto.java");

			assertTrue("Expected " + dtoJavaFile + " to exist",
			    FileUtils.getFile(dtoJavaFile).exists());

			LOGGER
			    .info(String.format("Successfully generated %s.", dtoJavaFile));

		}
	}

}
