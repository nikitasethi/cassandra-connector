/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cassandradb;

import org.apache.commons.lang3.StringUtils;
import org.mule.modules.cassandradb.configurations.BasicAuthConnectionStrategy;
import org.mule.api.annotations.*;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.MetaDataKeyParamAffectsType;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.RefOnly;
import org.mule.common.query.DsqlQuery;
import org.mule.modules.cassandradb.metadata.*;
import org.mule.modules.cassandradb.utils.CassandraDBException;
import org.mule.modules.cassandradb.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * The Apache Cassandra database is the right choice when you need scalability and high availability without compromising performance.
 * Cassandra's ColumnFamily data model offers the convenience of column indexes with the performance of log-structured updates, strong support for materialized views, and powerful built-in caching.
 *
 * @author MuleSoft, Inc.
 */
@Connector(name = "cassandradb", schemaVersion = "3.2", friendlyName = "CassandraDB", minMuleVersion = "3.6")
@RequiresEnterpriseLicense(allowEval = true)
public class CassandraDBConnector {

    private static final Logger logger = LoggerFactory.getLogger(CassandraDBConnector.class);
    
    private static final String PAYLOAD = "#[payload]";

    @Config
    private BasicAuthConnectionStrategy basicAuthConnectionStrategy;

    /**
     * Creates a new keyspace
     * @param input operation input containing the keyspace name and the replication details
     * @return true if the operation succeeded, false otherwise
     * @throws CassandraDBException if any error occurs when executing the create keyspace operation
     */
    @Processor
    public boolean createKeyspace(@RefOnly @Default(PAYLOAD) CreateKeyspaceInput input) throws CassandraDBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating keyspace " + input.toString());
        }
        try {
            return basicAuthConnectionStrategy.getCassandraClient().createKeyspace(input);
        } catch (Exception e) {
            throw new CassandraDBException(e.getMessage(), e);
        }
    }

    /**
     * Drops the entire keyspace
     * @param keyspaceName the name of the keyspace to be dropped
     * @return true if the operation succeeded, false otherwise
     * @throws CassandraDBException if any error occurs when executing the drop keyspace operation
     */
    @Processor
    public boolean dropKeyspace(String keyspaceName) throws CassandraDBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Dropping keyspace " + keyspaceName);
        }
        try {
            return basicAuthConnectionStrategy.getCassandraClient().dropKeyspace(keyspaceName);
        } catch (Exception e) {
            throw new CassandraDBException(e.getMessage(), e);
        }
    }

    /**
     * Creates a table(column family) in a specific keyspace; If no keyspace is specified the keyspace used for login will be used
     *
     * @param input operation input describing the table name, the keyspace name and the list of columns
     * @return true if the operation succeeded, false otherwise
     * @throws CassandraDBException if any error occurs when executing the create table operation
     */
    @Processor
    public boolean createTable(@RefOnly @Default(PAYLOAD) CreateTableInput input) throws CassandraDBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating table " + input.toString());
        }
        try {
            return basicAuthConnectionStrategy.getCassandraClient().createTable(input);
        } catch (Exception e) {
            throw new CassandraDBException(e.getMessage(), e);
        }
    }

    /**
     * Drops an entire table form the specified keyspace or from the keyspace used for login if none is specified as an operation parameter
     * @param tableName the name of the table to be dropped
     * @param keyspaceName (optional) the keyspace which contains the table to be dropped
     * @return true if the operation succeeded, false otherwise
     * @throws CassandraDBException if any error occurs when executing the drop table operation
     */
    @Processor
    public boolean dropTable(String tableName, @Optional String keyspaceName) throws CassandraDBException{
        if (logger.isDebugEnabled()) {
            logger.debug("Dropping table " + tableName);
        }
        try {
            return basicAuthConnectionStrategy.getCassandraClient().dropTable(tableName, keyspaceName);
        } catch (Exception e) {
            throw new CassandraDBException(e.getMessage(), e);
        }
    }

    /**
     * Executes the raw input query provided
     *
     * @param input CQLQueryInput describing the parametrized query to be executed along with the parameters
     * @return the result of the query execution
     * @throws CassandraDBException if any error occurs when executing the custom query
     */
    @Processor(friendlyName="Execute CQL Query")
    public List<Map<String, Object>> executeCQLQuery(@Placement(group = "Query") @RefOnly @Default(PAYLOAD) CQLQueryInput input) throws CassandraDBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing query " + input.toString());
        }
        try {
            return basicAuthConnectionStrategy.getCassandraClient().executeCQLQuery(input.getCqlQuery(), input.getParameters());
        } catch (Exception e) {
            throw new CassandraDBException(e.getMessage(), e);
        }
    }

    /**
     * Executes the insert entity operation
     * @param table the table name in which the entity will be inserted
     * @param keyspaceName (optional) the keyspace which contains the table to be used
     * @param entity the entity to be inserted
     * @throws CassandraDBException if any error occurs when executing the insert query
     */
    @Processor
    @MetaDataScope(CassandraMetadataCategory.class)
    public void insert(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.INPUT) String table,
                       @Optional String keyspaceName,
                       @RefOnly @Default(PAYLOAD) Map<String, Object> entity) throws CassandraDBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inserting entity " + entity + " into the " + table + " table ");
        }
        String keySpace = StringUtils.isNotBlank(keyspaceName) ? keyspaceName : basicAuthConnectionStrategy.getKeyspace();
        basicAuthConnectionStrategy.getCassandraClient().insert(keySpace,table,entity);
    }

    /**
     * Executes the update entity operation
     * @param table the table name in which the entity will be updated
     * @param keyspaceName (optional) the keyspace which contains the table to be dropped
     * @param entity the entity to be updated
     * @throws CassandraDBException if any error occurs when executing the update query
     */
    @Processor
    @MetaDataScope(CassandraWithFiltersMetadataCategory.class)
    public void update(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.INPUT) String table,
                       @Optional String keyspaceName,
                       @RefOnly @Default(PAYLOAD) Map<String, Object> entity) throws CassandraDBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Updating  entity" + entity + " into the " + table + " table ");
        }
        String keySpace = StringUtils.isNotBlank(keyspaceName) ? keyspaceName : basicAuthConnectionStrategy.getKeyspace();
        basicAuthConnectionStrategy.getCassandraClient().update(keySpace, table, (Map) entity.get(Constants.COLUMNS), (Map) entity.get(Constants.WHERE));
    }

    /**
     * Deletes values from an object specified by the where clause
     * @param table the name of the table
     * @param keyspaceName (optional) the keyspace which contains the table to be used
     * @param payload operation input: columns to be deleted and where clause for the delete operation
     * @throws CassandraDBException if any error occurs when executing the delete query
     */
    @Processor
    @MetaDataScope(CassandraWithFiltersMetadataCategory.class)
    public void deleteColumnsValue(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.INPUT) String table,
                                   @Optional String keyspaceName,
                                   @RefOnly @Default(PAYLOAD) Map<String, Object> payload) throws CassandraDBException {
        String keySpace = StringUtils.isNotBlank(keyspaceName) ? keyspaceName : basicAuthConnectionStrategy.getKeyspace();
        basicAuthConnectionStrategy.getCassandraClient().delete(keySpace, table, (List) payload.get(Constants.COLUMNS), (Map) payload.get(Constants.WHERE));
    }

    /**
     * Deletes an entire record
     * @param table the name of the table
     * @param keyspaceName (optional) the keyspace which contains the table to be used
     * @param payload operation input: where clause for the delete operation
     * @throws CassandraDBException if any error occurs when executing the delete query
     */
    @Processor
    @MetaDataScope(CassandraOnlyWithFiltersMetadataCategory.class)
    public void deleteRows(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.INPUT) String table,
                           @Optional String keyspaceName,
                           @RefOnly @Default(PAYLOAD) Map<String, Object> payload) throws CassandraDBException {
        String keySpace = StringUtils.isNotBlank(keyspaceName) ? keyspaceName : basicAuthConnectionStrategy.getKeyspace();
        basicAuthConnectionStrategy.getCassandraClient().delete(keySpace, table, null, (Map) payload.get(Constants.WHERE));
    }

    /**
     * Executes a select query
     * @param query  the query to be executed
     * @param parameters the query parameters
     * @return list of entities returned by the select query
     * @throws CassandraDBException if any error occurs when executing the select query
     */
    @Processor
    @MetaDataScope(CassandraMetadataCategory.class)
    public List<Map<String, Object>>  select(@Default(PAYLOAD) @Query final String query, @Optional List<Object> parameters) throws CassandraDBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing select query: " + query + " with the parameters: " + parameters);
        }
        return basicAuthConnectionStrategy.getCassandraClient().select(query, parameters);
    }

    /**
     * Returns all the table names from the specified keyspace
     * @param keyspaceName the name of the keyspace to be used on the operation
     * @return a list of table names
     * @throws CassandraDBException if any error occurs when executing the operation
     */
    @Processor
    public List<String> getTableNamesFromKeyspace(@Optional String keyspaceName) throws CassandraDBException {
        try {
            return basicAuthConnectionStrategy.getCassandraClient().getTableNamesFromKeyspace(keyspaceName);
        } catch (Exception e) {
            throw new CassandraDBException(e.getMessage(), e);
        }
    }

    /**
     * Changes the type of a column - check compatibility here: <a href="http://docs.datastax.com/en/cql/3.1/cql/cql_reference/cql_data_types_c.html#concept_ds_wbk_zdt_xj__cql_data_type_compatibility">CQL type compatibility</a>
     * @param table the name of the table to be used for the operation
     * @param keyspaceName (optional) the keyspace which contains the table to be used
     * @param input POJO defining the name of the column to be changed and the new DataType
     * @return true if the operation succeeded or false if not
     */
    @Processor(friendlyName="Change the type of a column")
    public boolean changeColumnType(String table, @Optional String keyspaceName, @RefOnly @Default(PAYLOAD) AlterColumnInput input){
        return basicAuthConnectionStrategy.getCassandraClient().changeColumnType(table, keyspaceName, input);
    }

    /**
     * Adds a new column
     * @param table the name of the table to be used for the operation
     * @param keyspaceName (optional) the keyspace which contains the table to be used
     * @param input POJO defining the name of the new column and its DataType
     * @return true if the operation succeeded or false if not
     */
    @Processor(friendlyName="Add new column")
    public boolean addNewColumn(String table, @Optional String keyspaceName, @RefOnly @Default(PAYLOAD) AlterColumnInput input) throws CassandraDBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding new column " + input.toString());
        }
        try {
            return basicAuthConnectionStrategy.getCassandraClient().addNewColumn(table, keyspaceName, input.getColumn(), ColumnType.resolveDataType(input.getType()));
        } catch (Exception e) {
            throw new CassandraDBException(e.getMessage(), e);
        }
    }

    /**
     * Removes a column
     * @param table the name of the table to be used for the operation
     * @param keyspaceName (optional) the keyspace which contains the table to be used
     * @param columnName the name of the column to be removed
     * @return true if the operation succeeded or false if not
     */
    @Processor(friendlyName="Remove column")
    public boolean dropColumn(String table, @Optional String keyspaceName, @RefOnly @Default(PAYLOAD) String columnName) {
        return basicAuthConnectionStrategy.getCassandraClient().dropColumn(table, keyspaceName, columnName);
    }

    /**
     * Renames a column
     * @param table the name of the table to be used for the operation
     * @param keyspaceName (optional) the keyspace which contains the table to be used
     * @param oldColumnName the name of the column to be changed
     * @param newColumnName the new value for the name of the column
     * @return true if the operation succeeded or false if not
     */
    @Processor(friendlyName="Rename column")
    public boolean renameColumn(String table, @Optional String keyspaceName, @Default(PAYLOAD) String oldColumnName, String newColumnName) {
        return basicAuthConnectionStrategy.getCassandraClient().renameColumn(table, keyspaceName, oldColumnName, newColumnName);
    }

    @QueryTranslator public String toNativeQuery(DsqlQuery query) {
        CassQueryVisitor visitor = new CassQueryVisitor();
        query.accept(visitor);
        return visitor.dsqlQuery();
    }

    public BasicAuthConnectionStrategy getBasicAuthConnectionStrategy() {
        return basicAuthConnectionStrategy;
    }

    public void setBasicAuthConnectionStrategy(BasicAuthConnectionStrategy basicAuthConnectionStrategy) {
        this.basicAuthConnectionStrategy = basicAuthConnectionStrategy;
    }
}
