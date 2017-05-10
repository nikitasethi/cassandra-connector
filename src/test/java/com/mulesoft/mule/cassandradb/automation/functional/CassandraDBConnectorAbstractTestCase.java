/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.mule.cassandradb.automation.functional;

import com.datastax.driver.core.DataType;
import com.mulesoft.mule.cassandradb.api.CassandraClient;
import com.mulesoft.mule.cassandradb.configurations.ConnectionParameters;
import com.mulesoft.mule.cassandradb.metadata.ColumnInput;
import com.mulesoft.mule.cassandradb.metadata.CreateKeyspaceInput;
import com.mulesoft.mule.cassandradb.metadata.CreateTableInput;
import com.mulesoft.mule.cassandradb.util.ConstantsTest;
import com.mulesoft.mule.cassandradb.util.PropertiesLoaderUtil;
import com.mulesoft.mule.cassandradb.utils.CassandraConfig;
import org.mule.tools.devkit.ctf.exceptions.ConfigurationLoadingFailedException;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;
import com.mulesoft.mule.cassandradb.CassandraDBConnector;

import java.util.List;

public class CassandraDBConnectorAbstractTestCase extends AbstractTestCase<CassandraDBConnector> {

    public CassandraDBConnectorAbstractTestCase() {
        super(CassandraDBConnector.class);
    }

    public static CassandraClient configureClient(CassandraConfig cassConfig) throws Exception {

        //get instance of cass client based on the configs
        CassandraClient cassClient = CassandraClient.buildCassandraClient(new ConnectionParameters(cassConfig.getHost(), cassConfig.getPort(), null, null, null, null));
        assert cassClient != null;

        //setup db env
        CreateKeyspaceInput keyspaceInput = new CreateKeyspaceInput();
        keyspaceInput.setKeyspaceName(cassConfig.getKeyspace());

        cassClient.createKeyspace(keyspaceInput);
        cassClient.createTable(TestDataBuilder.getBasicCreateTableInput(TestDataBuilder.getPrimaryKey(), cassConfig.getKeyspace(), ConstantsTest.TABLE_NAME));
        cassClient.addNewColumn(ConstantsTest.TABLE_NAME, cassConfig.getKeyspace(), ConstantsTest.VALID_COLUMN, DataType.text());

        return cassClient;
    }

    public static CassandraConfig getClientConfig() throws ConfigurationLoadingFailedException {
        //load required properties
        CassandraConfig cassConfig = PropertiesLoaderUtil.resolveCassandraConnectionProps();
        assert cassConfig != null;

        return cassConfig;
    }

}
