/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the
 * Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */


package org.mule.modules.cassandra.extension.connection.provider;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.mule.modules.cassandra.extension.connection.param.ConnectionGroup;

public class CassandraConnection {

    private ConnectionGroup connectionGroup;

    private Cluster cluster;

    private Session session;

    public CassandraConnection(ConnectionGroup connectionGroup, Cluster cluster, Session session) {
        this.connectionGroup = connectionGroup;
        this.cluster = cluster;
        this.session = session;
    }

    public ConnectionGroup getConnectionGroup() {
        return connectionGroup;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public Session getSession() {
        return session;
    }

    public boolean isConnected() {
        return cluster != null && session != null && !cluster.isClosed() && !session.isClosed();
    }
}
