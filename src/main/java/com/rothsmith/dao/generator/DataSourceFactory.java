/*
 * (c) 2015 Rothsmith, LLC All Rights Reserved.
 */
package com.rothsmith.dao.generator;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rothsmith.dao.core.GeneratorException;
import com.rothsmith.dao.core.Params;
import com.rothsmith.utils.database.JDBCServiceLocator;

/**
 * Factory for creating a {@link DataSource} object with the given parameters.
 * 
 * @author drothauser
 *
 */
public final class DataSourceFactory {

	/**
	 * SLF4J Logger for DataSourceFactory.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(DataSourceFactory.class);

	/**
	 * Private constructor to thwart instantiation.
	 */
	private DataSourceFactory() {
		// Private constructor.
	}

	/**
	 * Method to create a {@link DataSource} object from the database parameters
	 * in {@link Params}.
	 * 
	 * @param params
	 *            The DAO generation parameters
	 * @return an instance of {@link DataSource}
	 * @throws GeneratorException
	 *             thrown if error create JNDI datasource.
	 */
	public static DataSource create(Params params) throws GeneratorException {

		DataSource dataSource = null;

		String jdbcURL = params.getJdbcURL();
		if (StringUtils.isEmpty(jdbcURL)) {
			String jndiName = params.getJndiName();
			LOGGER.info("Using JNDI datasource: " + jndiName);
			try {
				dataSource =
				    JDBCServiceLocator.getInstance().getDataSource(jndiName);
			} catch (NamingException e) {
				String msg = String.format(
				    "*** Exception caught while "
				        + "resolving JNDI JDBC dataource - %s : %s",
				    jndiName, e);
				LOGGER.error(msg, e);
				throw new GeneratorException(msg, e);
			}
		} else {

			BasicDataSource bds = new BasicDataSource();
			bds.setUrl(jdbcURL);
			bds.setUsername(params.getDbUsername());
			bds.setPassword(params.getDbPassword());
			bds.setDriverClassName(params.getJdbcDriverClassName());
			dataSource = bds;
			LOGGER.info(String.format(
			    "Generating DAOs with the following datasource parameters:%n%s",
			    ToStringBuilder.reflectionToString(bds,
			        ToStringStyle.MULTI_LINE_STYLE)));
		}

		return dataSource;
	}

}
