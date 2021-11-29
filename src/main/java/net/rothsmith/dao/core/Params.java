/*
 * (c) 2009 Rothsmith, LLC, All Rights Reserved
 */
package net.rothsmith.dao.core;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.bval.constraints.NotEmpty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Parameter object for DAO generation.
 * 
 * @author drothauser
 * 
 */
// CHECKSTYLE:OFF Excessive method count (>35) ok in this class
@SuppressWarnings({ "PMD.TooManyFields", "PMD.ExcessivePublicCount" })
public class Params {
	// CHECKSTYLE:ON

	/**
	 * Database password.
	 */
	private String dbPassword;

	/**
	 * Database user name.
	 */
	private String dbUsername;

	/**
	 * DAO definitions XML file file generated from the {@link #defFileTemplate}
	 * .
	 */
	private String defFile;

	/**
	 * Velocity template file for generating the DAO definitions file.
	 */
	private String defFileTemplate;

	/**
	 * Java package for the generated DTO.
	 */
	private String dtoPackageName;

	/**
	 * The template used to generate DTOs.
	 */
	private String dtoTemplate;

	/**
	 * JDBC driver class name.
	 */
	private String jdbcDriverClassName;

	/**
	 * JDBC URL.
	 */
	private String jdbcURL;

	/**
	 * JNDI name to obtain a JDBC DataSource.
	 */
	private String jndiName;

	/**
	 * JUnit Velocity template.
	 */
	private String junitTemplate;

	/**
	 * Base output directory e.g. src, gen.
	 */
	private String outputDir;

	/**
	 * Properties file for DAOs.
	 */
	private String propsFile;

	/**
	 * The database schemaPattern pattern to filter objects with.
	 */
	private String schemaPattern;

	/**
	 * The main Spring bean configuration (typically "applicationContext.xml").
	 * This file will contain an import element for the dao Spring configuration
	 * file.
	 */
	private String springAppCtxFile;

	/**
	 * Name of the Velocity template used to generate the Spring application
	 * context file.
	 */
	private String springAppCtxTemplate;

	/**
	 * Name of the Spring DAO bean context file.
	 */
	private String springDaoCtxFile;

	/**
	 * Name of the Velocity template used to generate the Spring DAO bean
	 * context file.
	 */
	private String springDaoCtxTemplate;

	/**
	 * A comma separated list of SQL statements e.g. &quot;users&quot or
	 * &quot;user,groups&quot;.
	 */
	private String sqlStmts;

	/**
	 * A comma separated list of object name patterns e.g. &quot;users&quot or
	 * &quot;user,groups&quot;.
	 */
	@NotNull(message = "Table names is compulsory")
	private String tableNames;

	/**
	 * A comma separated list of data object type e.g. TABLE, VIEW.
	 */
	@NotNull(message = "Table types is compulsory")
	@NotEmpty(message = "Table types is compulsory")
	@Pattern(message = "Invalid type(s). Valid types are TABLE, VIEW, SYNONYM",
	    regexp = "(?i)^(table|view|synonym)"
	        + "(,\\s*(?!\\1)(table|view||synonym))*$")
	private String tableTypes;

	/**
	 * Output directory for test packages (e.g. &quot;test&quot;).
	 */
	private String testOutputDir;

	/**
	 * Package for test classes.
	 */
	private String testPackageName;

	/**
	 * Name of the Velocity template used to generate the DbUtils SQL properties
	 * file.
	 */
	private String dbUtilsPropsTemplate;

	/**
	 * Name of the directory to place DbUtils SQL properties files.
	 */
	private String dbUtilsPropsDir;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * @return the database password
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @return the database user name
	 */
	public String getDbUsername() {
		return dbUsername;
	}

	/**
	 * @return the name of the directory to place DbUtils SQL properties files.
	 */
	public String getDbUtilsPropsDir() {
		return dbUtilsPropsDir;
	}

	/**
	 * @return the name of the Velocity template used to generate the DbUtils
	 *         SQL properties file.
	 */
	public String getDbUtilsPropsTemplate() {
		return dbUtilsPropsTemplate;
	}

	/**
	 * @return DAO definitions XML file file generated from the
	 *         {@link #defFileTemplate}.
	 */
	public String getDefFile() {
		return defFile;
	}

	/**
	 * @return Velocity template file for generating the DAO definitions file.
	 */
	public String getDefFileTemplate() {
		return defFileTemplate;
	}

	/**
	 * @return Java package for the generated DTO.
	 */
	public String getDtoPackageName() {
		return dtoPackageName;
	}

	/**
	 * @return The template used to generate DTOs.
	 */
	public String getDtoTemplate() {
		return dtoTemplate;
	}

	/**
	 * @return the JDBC driver class name
	 */
	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}

	/**
	 * @return the JDBC URL
	 */
	public String getJdbcURL() {
		return jdbcURL;
	}

	/**
	 * @return the JNDI name to obtain a JDBC DataSource.
	 */
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * @return the JUnit Velocity template file name
	 */
	public String getJunitTemplate() {
		return junitTemplate;
	}

	/**
	 * @return Base output directory e.g. src, gen.
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * Get the DAO properties file.
	 * 
	 * @return the propsFile
	 */
	public String getPropsFile() {
		return propsFile;
	}

	/**
	 * @return the database schemaPattern to filter objects with.
	 */
	public String getSchemaPattern() {
		return schemaPattern;
	}

	/**
	 * Get the main Spring bean configuration (typically
	 * "applicationContext.xml"). This file will contain an import element for
	 * the dao Spring configuration file.
	 * 
	 * @return the Spring bean configuration file
	 */
	public String getSpringAppCtxFile() {
		return springAppCtxFile;
	}

	/**
	 * @return the name of the Velocity template used to generate the Spring
	 *         application context file.
	 */
	public String getSpringAppCtxTemplate() {
		return springAppCtxTemplate;
	}

	/**
	 * @return Name of the Spring DAO bean context file.
	 */
	public String getSpringDaoCtxFile() {
		return springDaoCtxFile;
	}

	/**
	 * @return the name of the Velocity template used to generate the Spring DAO
	 *         bean context file.
	 */
	public String getSpringDaoCtxTemplate() {
		return springDaoCtxTemplate;
	}

	/**
	 * @return A comma separated list of SQL statements.
	 */
	public String getSqlStmts() {
		return sqlStmts;
	}

	/**
	 * @return A comma separated list of object name patterns e.g.
	 *         &quot;tblcase&quot or &quot;tblassociate,tblcustomer&quot;.
	 */
	public String getTableNames() {
		return tableNames;
	}

	/**
	 * @return A comma separated list of data object type e.g. TABLE, VIEW.
	 */
	public String getTableTypes() {
		return tableTypes;
	}

	/**
	 * @return Output directory for test packages (e.g. &quot;test&quot;).
	 */
	public String getTestOutputDir() {
		return testOutputDir;
	}

	/**
	 * @return Package for test classes.
	 */
	public String getTestPackageName() {
		return testPackageName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {

		return HashCodeBuilder.reflectionHashCode(this);

	}

	/**
	 * @param dbPassword
	 *            the database password to set
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	/**
	 * @param dbUsername
	 *            the database user name to set
	 */
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	/**
	 * @param dbUtilsPropsDir
	 *            the name of the directory to place DbUtils SQL properties
	 *            files to set
	 */
	public void setDbUtilsPropsDir(String dbUtilsPropsDir) {
		this.dbUtilsPropsDir = dbUtilsPropsDir;
	}

	/**
	 * @param dbUtilsPropsTemplate
	 *            the bame of the Velocity template used to generate the DbUtils
	 *            SQL properties file to set.
	 */
	public void setDbUtilsPropsTemplate(String dbUtilsPropsTemplate) {
		this.dbUtilsPropsTemplate = dbUtilsPropsTemplate;
	}

	/**
	 * @param daoConfigXmlFile
	 *            DAO definitions XML file file generated from the
	 *            {@link #defFileTemplate}.
	 */
	public void setDefFile(final String daoConfigXmlFile) {
		this.defFile = daoConfigXmlFile;
	}

	/**
	 * @param defFileTemplate
	 *            Velocity template file for generating the DAO definitions
	 *            file. config file.
	 */
	public void setDefFileTemplate(final String defFileTemplate) {
		this.defFileTemplate = defFileTemplate;
	}

	/**
	 * @param dtoPackageName
	 *            Java package for the generated DTO.
	 */
	public void setDtoPackageName(final String dtoPackageName) {
		this.dtoPackageName = dtoPackageName;
	}

	/**
	 * @param dtoTemplate
	 *            The template used to generate DTOs to set
	 */
	public void setDtoTemplate(String dtoTemplate) {
		this.dtoTemplate = dtoTemplate;
	}

	/**
	 * @param jdbcDriverClassName
	 *            the JDBC driver class name to set
	 */
	public void setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}

	/**
	 * @param jdbcURL
	 *            the JDBC URL to set
	 */
	public void setJdbcURL(String jdbcURL) {
		this.jdbcURL = jdbcURL;
	}

	/**
	 * @param jndiName
	 *            the JNDI name to obtain a JDBC DataSource to set
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * @param junitTemplate
	 *            the JUnit Velocity template file name
	 */
	public void setJunitTemplate(String junitTemplate) {
		this.junitTemplate = junitTemplate;
	}

	/**
	 * @param outputDir
	 *            Base output directory e.g. src, gen.
	 */
	public void setOutputDir(final String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * Set the DAO properties file.
	 * 
	 * @param propsFile
	 *            the propsFile to set
	 */
	public void setPropsFile(String propsFile) {
		this.propsFile = propsFile;
	}

	/**
	 * @param schemaPattern
	 *            the database schemaPattern to filter objects with.
	 */
	public void setSchemaPattern(final String schemaPattern) {
		this.schemaPattern = schemaPattern;
	}

	/**
	 * Set the main Spring bean configuration (typically
	 * "applicationContext.xml"). This file will contain an import element for
	 * the dao Spring configuration file.
	 * 
	 * @param springAppCtxFile
	 *            the Spring bean configuration file to set
	 */
	public void setSpringAppCtxFile(final String springAppCtxFile) {
		this.springAppCtxFile = springAppCtxFile;
	}

	/**
	 * @param springAppCtxTemplate
	 *            the name of the Velocity template used to generate the Spring
	 *            application context file to set
	 */
	public void setSpringAppCtxTemplate(String springAppCtxTemplate) {
		this.springAppCtxTemplate = springAppCtxTemplate;
	}

	/**
	 * @param springDaoCtxFile
	 *            Name of the Spring DAO bean context file.
	 */
	public void setSpringDaoCtxFile(final String springDaoCtxFile) {
		this.springDaoCtxFile = springDaoCtxFile;
	}

	/**
	 * @param springDaoCtxTemplate
	 *            the name of the Velocity template used to generate the Spring
	 *            DAO bean context file to set
	 */
	public void setSpringDaoCtxTemplate(String springDaoCtxTemplate) {
		this.springDaoCtxTemplate = springDaoCtxTemplate;
	}

	/**
	 * @param sqlStmts
	 *            the SQL statements to set
	 */
	public void setSqlStmts(final String sqlStmts) {
		this.sqlStmts = sqlStmts;
	}

	/**
	 * @param tableNames
	 *            the tableNames to set
	 */
	public void setTableNames(final String tableNames) {
		this.tableNames = tableNames;
	}

	/**
	 * @param tableTypes
	 *            A comma separated list of data object type e.g. TABLE, VIEW.
	 */
	public void setTableTypes(final String tableTypes) {
		this.tableTypes = tableTypes;
	}

	/**
	 * @param testOutputDir
	 *            Output directory for test packages (e.g. &quot;test&quot;).
	 */
	public void setTestOutputDir(final String testOutputDir) {
		this.testOutputDir = testOutputDir;
	}

	/**
	 * @param testPackageName
	 *            Package for test classes.
	 */
	public void setTestPackageName(final String testPackageName) {
		this.testPackageName = testPackageName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
		    ToStringStyle.MULTI_LINE_STYLE);
	}

}
