/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the
 * Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.cassandra.extension.connection.param;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class ConnectionGroup {

    @Parameter
    @DisplayName("Host")
    @Optional(defaultValue="localhost")
    private String host = "localhost";

    @Parameter
    @DisplayName("Port")
    @Optional(defaultValue="9042")
    private Integer port = 9042;

    @Parameter
    @DisplayName("Keyspace")
    private String keyspace;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }
}
