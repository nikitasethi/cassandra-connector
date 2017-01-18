package org.mule.modules.cassandra.extension.connection.provider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.datastax.driver.core.Cluster;

import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mule.modules.cassandra.extension.connection.param.ConnectionGroup;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;

public class CassandraConnectionProviderTest {


    @InjectMocks
    CassandraConnectionProvider provider = new CassandraConnectionProvider();

    @Before
    public void beforeClass() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testConnect() throws ConnectionException {
        //given
        ConnectionGroup connectionGroup = new ConnectionGroup();
        connectionGroup.setHost("localhost");
        connectionGroup.setPort(9042);
        provider.setConnectionGroup(connectionGroup);

        Cluster.Builder builderMock = Mockito.mock(Cluster.Builder.class);
        when(builderMock.addContactPoint(anyString())).thenReturn(builderMock);
        when(builderMock.withPort(anyInt())).thenReturn(builderMock);

        CassandraConnectionProvider spyProvider = Mockito.spy(provider);
        when(spyProvider.getBuilder()).thenReturn(builderMock);

        Cluster clusterMock = Mockito.mock(Cluster.class);
        Metadata metaDataMock = Mockito.mock(Metadata.class);
        when(metaDataMock.getClusterName()).thenReturn("TestCluster");
        when(clusterMock.getMetadata()).thenReturn(metaDataMock);
        when(builderMock.build()).thenReturn(clusterMock);

        //when
        CassandraConnection connection = spyProvider.connect();

        //then
        Mockito.verify(clusterMock).connect();
        assertNotNull(connection);
    }


    @Test
    public void testDisconnect() {

        ConnectionGroup connectionGroup = new ConnectionGroup();
        connectionGroup.setHost("localhost");
        connectionGroup.setPort(9042);
        Cluster mockCluster = Mockito.mock(Cluster.class);
        Session mockSession = Mockito.mock(Session.class);
        CassandraConnection connectionMock = new CassandraConnection(connectionGroup, mockCluster, mockSession);

        provider.disconnect(connectionMock);

        Mockito.verify(mockSession).close();
        Mockito.verify(mockCluster).close();

    }


    @Test
    public void testValidate() {

        CassandraConnection connectionMock = Mockito.mock(CassandraConnection.class);

        when(connectionMock.isConnected()).thenReturn(Boolean.TRUE);
        ConnectionValidationResult validateResult = provider.validate(connectionMock);
        assertTrue(validateResult.isValid());

        when(connectionMock.isConnected()).thenReturn(Boolean.FALSE);
        validateResult = provider.validate(connectionMock);
        assertFalse(validateResult.isValid());

        validateResult = provider.validate(null);
        assertFalse(validateResult.isValid());


    }


}
