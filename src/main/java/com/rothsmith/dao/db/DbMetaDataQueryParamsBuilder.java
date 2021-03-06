// CHECKSTYLE:OFF
/**
 * Source code generated by Fluent Builders ArtifactGenerator Do not modify this
 * file See generator home page at:
 * http://code.google.com/p/fluent-builders-generator-eclipse-plugin/
 */

package com.rothsmith.dao.db;

import javax.sql.DataSource;

@SuppressWarnings("PMD")
public class DbMetaDataQueryParamsBuilder
        extends
        DbMetaDataQueryParamsBuilderBase<DbMetaDataQueryParamsBuilder> {
	public static DbMetaDataQueryParamsBuilder metaDataQueryParams() {
		return new DbMetaDataQueryParamsBuilder();
	}

	public DbMetaDataQueryParamsBuilder() {
		super(new DbMetaDataQueryParams());
	}

	public DbMetaDataQueryParams build() {
		return getInstance();
	}
}

@SuppressWarnings("PMD")
class DbMetaDataQueryParamsBuilderBase<T extends DbMetaDataQueryParamsBuilderBase<T>> {
	private DbMetaDataQueryParams instance;

	protected DbMetaDataQueryParamsBuilderBase(
	    DbMetaDataQueryParams aInstance) {
		instance = aInstance;
	}

	protected DbMetaDataQueryParams getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public T withDataSource(DataSource aValue) {
		instance.setDataSource(aValue);

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withCatalog(String aValue) {
		instance.setCatalog(aValue);

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withSchemaPattern(String aValue) {
		instance.setSchemaPattern(aValue);

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withTableNames(String aValue) {
		instance.setTableNames(aValue);

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withTableTypes(String aValue) {
		instance.setTypes(aValue);

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withColumnPattern(String aValue) {
		instance.setColumnPattern(aValue);

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withSelectSql(String aValue) {
		instance.setSelectSql(aValue);

		return (T) this;
	}
}
