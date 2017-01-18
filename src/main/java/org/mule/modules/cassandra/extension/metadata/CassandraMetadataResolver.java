/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the
 * Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */


package org.mule.modules.cassandra.extension.metadata;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.JavaTypeLoader;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class CassandraMetadataResolver implements OutputTypeResolver<MetadataKey> {

    @Override
    public String getCategoryName() {
        return "CassandraMetadataResolver";
    }

    @Override
    public MetadataType getOutputType(MetadataContext context, MetadataKey key) throws MetadataResolvingException, ConnectionException {
        BaseTypeBuilder typeBuilder = new BaseTypeBuilder(JavaTypeLoader.JAVA);
        return typeBuilder.anyType().build();
    }
}
