/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.mule.cassandradb.util;

import com.mulesoft.mule.cassandradb.utils.CassandraConfig;
import com.mulesoft.mule.cassandradb.utils.Constants;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;
import org.mule.tools.devkit.ctf.exceptions.ConfigurationLoadingFailedException;

import java.util.Properties;

public class PropertiesLoaderUtil {

    public static CassandraConfig resolveCassandraConnectionProps() throws ConfigurationLoadingFailedException {
        Properties cassandraConnProps;
        cassandraConnProps = ConfigurationUtils.getAutomationCredentialsProperties();
        return new CassandraConfig(cassandraConnProps.getProperty(Constants.CASS_HOST), Integer.valueOf(cassandraConnProps.getProperty(Constants.CASS_PORT)),
                cassandraConnProps.getProperty(Constants.KEYSPACE_NAME));
    }
}
