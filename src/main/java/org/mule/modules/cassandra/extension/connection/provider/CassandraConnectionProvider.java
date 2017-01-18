/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the
 * Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.cassandra.extension.connection.provider;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.jetbrains.annotations.NotNull;
import org.mule.modules.cassandra.extension.connection.param.ConnectionGroup;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionExceptionCode;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CassandraConnectionProvider implements ConnectionProvider<CassandraConnection> {


    private static final Logger logger = LoggerFactory.getLogger(CassandraConnectionProvider.class);

    @ParameterGroup("Connection")
    private ConnectionGroup connectionGroup;

    public void setConnectionGroup(ConnectionGroup connectionGroup) {
        this.connectionGroup = connectionGroup;
    }


    @Override
    public CassandraConnection connect() throws ConnectionException {
        try {
            Cluster cluster = getBuilder().addContactPoint(connectionGroup.getHost()).withPort(connectionGroup.getPort()).build();
            Metadata metadata = cluster.getMetadata();
            Session session = cluster.connect();
            logger.info("Succesfully connected to cluster:" + metadata.getClusterName());
            return new CassandraConnection(this.connectionGroup, cluster, session);
        } catch (Exception ex) {
            throw new ConnectionException(String.format("Failed to connect to: %s:%s", connectionGroup.getHost(), connectionGroup.getPort()));
        }
    }

    @NotNull
    protected Cluster.Builder getBuilder() {
        return Cluster.builder();
    }

    @Override
    public void disconnect(CassandraConnection cassandraConnection) {
        try {
            if (cassandraConnection != null) {
                cassandraConnection.getSession().close();
                cassandraConnection.getCluster().close();
                logger.info("Session disconnected.");
            }
        } catch (Exception ex) {
            logger.warn("Failed to close the connection.", ex);
        }
    }

    @Override
    public ConnectionValidationResult validate(CassandraConnection cassandraConnection) {
        if (cassandraConnection != null && cassandraConnection.isConnected()) {
            return ConnectionValidationResult.success();
        } else {
            return ConnectionValidationResult.failure("Client was disconnected!", ConnectionExceptionCode.DISCONNECTED, null);
        }
    }
}
