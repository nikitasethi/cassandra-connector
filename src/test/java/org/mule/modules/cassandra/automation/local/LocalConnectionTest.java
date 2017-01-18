/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the
 * Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cassandra.automation.local;

import static junit.framework.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.mule.modules.cassandra.extension.connection.param.ConnectionGroup;
import org.mule.modules.cassandra.extension.connection.provider.CassandraConnection;
import org.mule.modules.cassandra.extension.connection.provider.CassandraConnectionProvider;
import org.mule.modules.cassandra.extension.exception.CassandraException;
import org.mule.modules.cassandra.extension.operation.CassandraConnectorOperations;
import org.mule.runtime.api.connection.ConnectionException;

public class LocalConnectionTest {


    @Test
    @Ignore
    public void testLocalConnection() throws ConnectionException {
        CassandraConnectionProvider provider = new CassandraConnectionProvider();
        ConnectionGroup connectionGroup = new ConnectionGroup();
        connectionGroup.setHost("localhost");
        connectionGroup.setPort(9042);
        provider.setConnectionGroup(connectionGroup);
        CassandraConnection connection = provider.connect();
        assertTrue(connection.isConnected());
        provider.disconnect(connection);
    }

    @Test
    @Ignore
    public void testGetClusterName() throws ConnectionException, CassandraException {
        CassandraConnectorOperations operations = new CassandraConnectorOperations();
        CassandraConnectionProvider provider = new CassandraConnectionProvider();
        ConnectionGroup connectionGroup = new ConnectionGroup();
        connectionGroup.setHost("localhost");
        connectionGroup.setPort(9042);
        provider.setConnectionGroup(connectionGroup);
        CassandraConnection connection = provider.connect();
        String clusterName = operations.describeClusterName(connection);
        System.out.println("clusterName = " + clusterName);
        provider.disconnect(connection);

    }

}
