/*
 * (c) 2014 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.osjava.sj.loader.convert.Converter;
import org.osjava.sj.loader.convert.DataSourceConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author drothauser
 *
 */
// CHECKSTYLE:OFF
public class SimpleJndiTest {

	/**
	 * SLF4J Logger for SimpleJndiTest.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(SimpleJndiTest.class);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBasic() throws NamingException, SQLException {
		InitialContext ctx = new InitialContext();
		String jndiName = "java:/comp/env/jdbc/TestDS";
		DataSource dataSource = (DataSource) ctx.lookup(jndiName);
		try (Connection conn = dataSource.getConnection()) {

			System.out.println(conn.getMetaData().getDatabaseProductName());

		}
	}

	@Test
	public void testDataSourceConverter() throws NamingException, SQLException {

		Properties props = new Properties();

		props.put("driver", "org.apache.derby.jdbc.EmbeddedDriver");
		props.put("url", "jdbc:derby:memory:testdb;create=true");
		props.put("user", "test");
		props.put("password", "test");

		Converter converter = new DataSourceConverter();
		DataSource dataSource = (DataSource) converter.convert(props, null);

		try (Connection conn = dataSource.getConnection()) {

			LOGGER.info(conn.getMetaData().getDatabaseProductName());

		}

		// InitialContext ctxt = new InitialContext();
		// Object lookup = ctxt.lookup("ds.TestDS");
		// dataSource = (DataSource) ctxt.lookup("ds.TestDS");
	}
}
