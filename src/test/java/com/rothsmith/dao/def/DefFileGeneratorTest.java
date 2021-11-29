/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.def;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.jdom2.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.def.DefGenerator;
import com.rothsmith.dao.spring.BaseSpringTest;

import net.rothsmith.dao.core.VelocityGenerator;

/**
 * Tests for generating the definitions file.
 * 
 * @author drothauser
 *
 */
public class DefFileGeneratorTest
        extends BaseSpringTest {

	/**
	 * SLF4J Logger for DefFileGeneratorTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(DefFileGeneratorTest.class);

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
	 * Test method for {@link DefGenerator#generate(List)}.
	 * 
	 * @throws Exception
	 *             possible error
	 */
	@Test
	public void testGenerate() throws Exception {

		String defFileTemplate = testParams.getDefFileTemplate();
		String defFile = testParams.getDefFile();
		LOGGER.info(String.format(
		    "Testing DefGenerator constructed with:"
		        + "%nDefinitions File Template: %s"
		        + "%nGenerated Definitions File: %s",
		    defFileTemplate, defFile));
		DefGenerator defGenerator = new DefGenerator(new VelocityGenerator());

		Document defFileDoc = defGenerator.generate(dataSource, testParams);

		assertTrue(new File(testParams.getDefFile()).exists());

		assertEquals("Apache Derby",
		    defFileDoc.getRootElement().getChildTextTrim("vendor"));
	}

}
