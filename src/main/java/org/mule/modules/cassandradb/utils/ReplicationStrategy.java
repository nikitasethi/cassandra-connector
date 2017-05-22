/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cassandradb.utils;

import org.apache.commons.lang3.StringUtils;
import org.mule.modules.cassandradb.metadata.CreateKeyspaceInput;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cassandra supported replica placement strategies
 */
public enum ReplicationStrategy {

    /**
     * SimpleStrategy merely places the first replica at the node whose
     * token is closest to the key (as determined by the Partitioner), and
     * additional replicas on subsequent nodes along the ring in increasing
     * Token order.
     * <p/>
     * Supports a single strategy option 'replication_factor' that
     * specifies the replication factor for the cluster.
     */
    SIMPLE("SimpleStrategy"),
    /**
     * With NetworkTopologyStrategy, for each datacenter, you can specify
     * how many replicas you want on a per-keyspace basis. Replicas are
     * placed on different racks within each DC, if possible.
     * <p/>
     * Supports strategy options which specify the replication factor for
     * each datacenter. The replication factor for the entire cluster is the
     * sum of all per datacenter values. Note that the datacenter names
     * must match those used in conf/cassandra-topology.properties.
     */
    NETWORK_TOPOLOGY("NetworkTopologyStrategy");

    private final String strategy;

    ReplicationStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return this.strategy;
    }

    public static ReplicationStrategy fromString(String inputToMatch) {
        if (StringUtils.isNotBlank(inputToMatch)) {
            for (ReplicationStrategy enumItem : ReplicationStrategy.values()) {
                if (enumItem.getStrategy().equalsIgnoreCase(inputToMatch)) {
                    return enumItem;
                }
            }
        }
        return null;
    }

    //based on http://docs.datastax.com/en/cql/3.1/cql/cql_reference/create_keyspace_r.html rules
    public static Map<String, Object> buildReplicationStrategy(CreateKeyspaceInput input) {
        LinkedHashMap<String, Object> replicationStrategy = new LinkedHashMap<String, Object>();

        //set the strategy class only if exists in the input && a valid one is provided
        if (input.getReplicationStrategyClass() != null) {
            ReplicationStrategy strategy = ReplicationStrategy.fromString(input.getReplicationStrategyClass());
            //setting 'replication_factor' || 'first_data_center' || 'next data center' is pointless if no class is provided
            if (strategy != null) {
                replicationStrategy.put(Constants.CLASS, input.getReplicationStrategyClass());
                //'replication_factor' required if class is SimpleStrategy; otherwise, not used
                if (strategy == ReplicationStrategy.SIMPLE) {
                    replicationStrategy.put(Constants.REPLICATION_FACTOR, input.getReplicationFactor());
                }
                if (input.getFirstDataCenter() != null) {
                    if (StringUtils.isNotBlank(input.getFirstDataCenter().getName())) {
                        replicationStrategy.put(input.getFirstDataCenter().getName(), input.getFirstDataCenter().getValue());
                    }
                }
                if (input.getNextDataCenter() != null) {
                    if (StringUtils.isNotBlank(input.getNextDataCenter().getName())) {
                        replicationStrategy.put(input.getNextDataCenter().getName(), input.getNextDataCenter().getValue());
                    }
                }
            }
        } else {
            return buildDefaultReplicationStrategy();
        }
        return replicationStrategy;
    }

    public static LinkedHashMap<String, Object> buildDefaultReplicationStrategy() {
        LinkedHashMap<String, Object> replicationStrategyMap = new LinkedHashMap<String, Object>();
        replicationStrategyMap.put(Constants.CLASS, ReplicationStrategy.SIMPLE.getStrategy());
        replicationStrategyMap.put(Constants.REPLICATION_FACTOR, 3);
        return replicationStrategyMap;
    }
}
