/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cassandradb.automation.functional.processors;

import org.mule.modules.cassandradb.metadata.CreateKeyspaceInput;
import org.mule.modules.cassandradb.metadata.DataCenter;
import org.mule.modules.cassandradb.utils.CassandraDBException;
import org.mule.modules.cassandradb.utils.ReplicationStrategy;
import org.junit.AfterClass;
import org.junit.Test;

public class CreateKeyspaceTestCases extends CassandraDBConnectorAbstractTestCases {

    private final static String KEYSPACE_NAME_1 = "keyspaceName1";
    private final static String KEYSPACE_NAME_2 = "keyspaceName2";
    private final static String KEYSPACE_NAME_3 = "keyspaceName3";
    private final static String DATA_CENTER_NAME = "datacenter1";

    @AfterClass
    public static void tearDown() {
        cassClient.dropKeyspace(KEYSPACE_NAME_1);
        cassClient.dropKeyspace(KEYSPACE_NAME_2);
        cassClient.dropKeyspace(KEYSPACE_NAME_3);
    }

    @Test
    public void testCreateKeyspaceWithDefaultReplicationStrategyWithSuccess() throws CassandraDBException {
        CreateKeyspaceInput keyspaceInput = new CreateKeyspaceInput();
        keyspaceInput.setKeyspaceName(KEYSPACE_NAME_1);

        getConnector().createKeyspace(keyspaceInput);
    }

    @Test
    public void testCreateKeyspaceWithDifferentReplicationStrategyWithSuccess() throws CassandraDBException {
        CreateKeyspaceInput keyspaceInput = new CreateKeyspaceInput();
        keyspaceInput.setKeyspaceName(KEYSPACE_NAME_2);
        keyspaceInput.setFirstDataCenter(new DataCenter(DATA_CENTER_NAME, 1));
        keyspaceInput.setReplicationStrategyClass(ReplicationStrategy.NETWORK_TOPOLOGY.getStrategy());

        getConnector().createKeyspace(keyspaceInput);
    }

    @Test(expected = CassandraDBException.class)
    public void testCreateKeyspaceWithInvalidReplicationStrategy() throws CassandraDBException {
        CreateKeyspaceInput keyspaceInput = new CreateKeyspaceInput();
        keyspaceInput.setKeyspaceName(KEYSPACE_NAME_3);
        keyspaceInput.setReplicationFactor(3);
        keyspaceInput.setReplicationStrategyClass("SomeReplicationStrategy");

        getConnector().createKeyspace(keyspaceInput);
    }

    @Test(expected = CassandraDBException.class)
    public void shouldFail_Using_MissingReplicationFactor() throws CassandraDBException {
        CreateKeyspaceInput keyspaceInput = new CreateKeyspaceInput();
        keyspaceInput.setKeyspaceName(KEYSPACE_NAME_1);
        keyspaceInput.setReplicationFactor(null);
        keyspaceInput.setReplicationStrategyClass(ReplicationStrategy.SIMPLE.getStrategy());

        getConnector().createKeyspace(keyspaceInput);
    }
}
