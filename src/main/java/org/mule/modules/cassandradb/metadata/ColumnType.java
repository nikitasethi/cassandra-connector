/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cassandradb.metadata;

import com.datastax.driver.core.DataType;

public enum ColumnType {

    ASCII,
    BIGINT,
    BLOB,
    BOOLEAN,
    COUNTER,
    DECIMAL,
    DOUBLE,
    FLOAT,
    INET,
    INT,
    TEXT,
    TIMESTAMP,
    UUID,
    VARCHAR,
    VARINT,
    TIMEUUID,
    SMALLINT,
    TINYINT,
    DATE,
    TIME;

    public static DataType resolveDataType(ColumnType type){
        switch (type){
            case ASCII: return DataType.ascii();
            case BIGINT: return DataType.bigint();
            case BLOB: return DataType.blob();
            case BOOLEAN: return DataType.cboolean();
            case COUNTER: return DataType.counter();
            case DECIMAL: return DataType.decimal();
            case DOUBLE: return DataType.cdouble();
            case FLOAT: return DataType.cfloat();
            case INET: return DataType.inet();
            case TINYINT: return DataType.tinyint();
            case SMALLINT: return DataType.smallint();
            case INT: return DataType.cint();
            case TEXT: return DataType.text();
            case TIMESTAMP: return DataType.timestamp();
            case DATE: return DataType.date();
            case TIME: return DataType.time();
            case UUID: return DataType.uuid();
            case VARCHAR: return DataType.varchar();
            case VARINT: return DataType.varint();
            case TIMEUUID: return DataType.timeuuid();
            default: return DataType.text();
        }
    }
}
