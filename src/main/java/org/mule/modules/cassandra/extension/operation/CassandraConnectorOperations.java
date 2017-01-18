/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the
 * Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */


package org.mule.modules.cassandra.extension.operation;


import com.datastax.driver.core.Metadata;
import org.mule.modules.cassandra.extension.connection.provider.CassandraConnection;
import org.mule.modules.cassandra.extension.exception.CassandraException;
import org.mule.runtime.extension.api.annotation.param.Connection;

public class CassandraConnectorOperations {

    public String describeClusterName(@Connection CassandraConnection cassandraConnection) throws CassandraException {
        Metadata metadata = cassandraConnection.getCluster().getMetadata();
        return metadata.getClusterName();
    }

    public String describePartitioner(@Connection CassandraConnection cassandraConnection) throws CassandraException {
        Metadata metadata = cassandraConnection.getCluster().getMetadata();
        return metadata.getPartitioner();
    }


}
