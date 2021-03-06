/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cassandradb.automation.functional;

import org.mule.modules.cassandradb.automation.util.TestsConstants;
import org.mule.modules.cassandradb.metadata.AlterColumnInput;
import org.mule.modules.cassandradb.metadata.ColumnType;
import org.jetbrains.annotations.NotNull;
import org.mule.modules.cassandradb.metadata.ColumnInput;
import org.mule.modules.cassandradb.metadata.CreateTableInput;

import java.util.*;

public class TestDataBuilder {

    public static List<String> cassandraCategoryMetadataTestKeys = new LinkedList<String>();

    static {
        cassandraCategoryMetadataTestKeys.add(TestsConstants.TABLE_NAME_2);
    }

    private TestDataBuilder() {

    }

    @NotNull
    public static Map<String, Object> getInvalidEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("invalid_column", "someValue");
        return entity;
    }

    public static Map<String, Object> getValidEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(TestsConstants.DUMMY_PARTITION_KEY, "value1");
        entity.put(TestsConstants.VALID_COLUMN_2, "someValue" + System.currentTimeMillis());
        return entity;
    }

    public static Map<String, Object> getValidEntityForUpdate() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(TestsConstants.VALID_COLUMN_2, "someValue" + System.currentTimeMillis());
        return entity;
    }

    public static Map<String, Object> getValidWhereClauseWithEq() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(TestsConstants.DUMMY_PARTITION_KEY, "value1");
        return entity;
    }

    public static Map<String, Object> getValidWhereClauseWithIN() {
        Map<String, Object> entity = new HashMap<String, Object>();
        List list = new ArrayList();
        list.add("value1");
        list.add("value2");
        entity.put(TestsConstants.DUMMY_PARTITION_KEY, list);
        return entity;
    }

    /* WHERE clause restrictions for the UPDATE are
    *   - the single-column EQ on any partition key or clustering columns
        - the single-column IN restriction on the last partition key column
    * */
    public static Map<String, Object> getInvalidWhereClause() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(TestsConstants.VALID_COLUMN_1, "someValue");
        return entity;
    }

    public static List<Object> getValidParmList() {
        List<Object> parameters = new LinkedList<Object>();
        parameters.add("value1");
        parameters.add("value2");
        return parameters;
    }

    public static List<String> getValidColumnsListForDelete() {
        List<String> parameters = new LinkedList<String>();
        parameters.add(TestsConstants.VALID_COLUMN_1);
        parameters.add(TestsConstants.VALID_COLUMN_2);
        return parameters;
    }

    public static List<String> getInvalidEntityForDelete() {
        List<String> entity = new ArrayList<String>();
        entity.add("invalid_column");
        return entity;
    }

    public static Map<String, Object> getValidEntityWithList() {
        Map<String, Object> entity = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        list.add("firstValue");
        list.add("secondValue");
        entity.put(TestsConstants.DUMMY_PARTITION_KEY, "value1");
        entity.put(TestsConstants.VALID_LIST_COLUMN, list);
        entity.put(TestsConstants.VALID_COLUMN_1, 1);
        return entity;
    }

    public static List<String> getValidListItem() {
        List<String> entity = new ArrayList<String>();
        entity.add(TestsConstants.VALID_LIST_COLUMN + "[0]");
        return entity;
    }

    public static Map<String, Object> getValidEntityWithMap() {
        Map<String, Object> entity = new HashMap<String, Object>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("1", "firstValue");
        item.put("2", "secondValue");
        entity.put(TestsConstants.DUMMY_PARTITION_KEY, "value1");
        entity.put(TestsConstants.VALID_MAP_COLUMN, item);
        entity.put(TestsConstants.VALID_COLUMN_1, 1);
        return entity;
    }

    public static List<String> getValidMapItem() {
        List<String> entity = new ArrayList<String>();
        entity.add(TestsConstants.VALID_MAP_COLUMN + "['firstValue']");
        return entity;
    }

    public static Map<String, Object> getValidEntityWithSet() {
        Map<String, Object> entity = new HashMap<String, Object>();
        Set<String> item = new HashSet<String>();
        item.add("firstValue");
        item.add("secondValue");
        entity.put(TestsConstants.DUMMY_PARTITION_KEY, "value1");
        entity.put(TestsConstants.VALID_SET_COLUMN, item);
        entity.put(TestsConstants.VALID_COLUMN_1, 1);
        return entity;
    }

    public static List<String> getValidSet() {
        List<String> entity = new ArrayList<String>();
        entity.add(TestsConstants.VALID_SET_COLUMN);
        return entity;
    }

    public static Map<String, Object> getPayloadColumnsAndFilters(Object entity, Map<String, Object> whereClause){
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put(TestsConstants.COLUMNS, entity);
        payload.put(TestsConstants.WHERE, whereClause);
        return payload;
    }

    public static List<ColumnInput> getPrimaryKey(){
        List<ColumnInput> columns = new ArrayList<ColumnInput>();

        ColumnInput column = new ColumnInput();
        column.setPrimaryKey(true);
        column.setName(TestsConstants.DUMMY_PARTITION_KEY);
        column.setType(ColumnType.TEXT);
        columns.add(column);

        return columns;
    }

    public static List<ColumnInput> getCompositePrimaryKey(){
        List<ColumnInput> columns = new ArrayList<ColumnInput>();

        ColumnInput column = new ColumnInput();
        column.setPrimaryKey(true);
        column.setName(TestsConstants.DUMMY_PARTITION_KEY);
        column.setType(ColumnType.TEXT);

        ColumnInput column2 = new ColumnInput();
        column2.setPrimaryKey(true);
        column2.setName(TestsConstants.VALID_COLUMN_1);
        column2.setType(ColumnType.INT);

        columns.add(column);
        columns.add(column2);

        return columns;
    }

    public static List<ColumnInput> getColumns(){
        List<ColumnInput> columns = new ArrayList<ColumnInput>();

        ColumnInput column1 = new ColumnInput();
        column1.setPrimaryKey(true);
        column1.setName(TestsConstants.DUMMY_PARTITION_KEY);
        column1.setType(ColumnType.TEXT);

        ColumnInput column2 = new ColumnInput();
        column2.setName(TestsConstants.VALID_COLUMN_1);
        column2.setType(ColumnType.INT);

        ColumnInput column3 = new ColumnInput();
        column3.setName(TestsConstants.VALID_COLUMN_2);
        column3.setType(ColumnType.TEXT);

        columns.add(column1);
        columns.add(column2);
        columns.add(column3);

        return columns;
    }

    public static CreateTableInput getBasicCreateTableInput(List<ColumnInput> columns, String keyspaceName, String tableName){
        CreateTableInput input = new CreateTableInput();

        input.setColumns(columns);
        input.setKeyspaceName(keyspaceName);
        input.setTableName(tableName);

        return input;
    }

    public static AlterColumnInput getAlterColumnInput(String columnName, ColumnType type){
        AlterColumnInput result = new AlterColumnInput();
        result.setColumn(columnName);
        result.setType(type);
        return result;
    }

    public static Map<String, String> getRenameColumnInput(String oldName, String newName){
        Map<String, String> result = new HashMap<String, String>();
        result.put(oldName, newName);
        return result;
    }
}
