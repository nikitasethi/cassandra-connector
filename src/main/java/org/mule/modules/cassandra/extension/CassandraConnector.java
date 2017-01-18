/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the
 * Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.cassandra.extension;

import org.mule.modules.cassandra.extension.connection.provider.CassandraConnectionProvider;
import org.mule.modules.cassandra.extension.operation.CassandraConnectorOperations;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;



@Extension(name = "Cassandra DataStax Connector", description = "")
@ConnectionProviders({
        CassandraConnectionProvider.class
})
@Operations(CassandraConnectorOperations.class)

@Alias(value = "cassandradb")
public class CassandraConnector {

}
