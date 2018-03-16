package org.apache.camel.component.ibatis;

import java.io.IOException;

import org.apache.camel.PollingConsumer;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultPollingEndpoint;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * for performing SQL operations using an XML mapping file to abstract away the SQL
 *
 * @version $Revision: 1.1 $
 */
public class IBatisEndpoint extends DefaultPollingEndpoint {
    private final String entityName;

    public IBatisEndpoint(String endpointUri, IBatisComponent component, String entityName) {
        super(endpointUri, component);
        this.entityName = entityName;
    }

    @Override
    public IBatisComponent getComponent() {
        return (IBatisComponent) super.getComponent();
    }

    public boolean isSingleton() {
        return true;
    }

    public Producer createProducer() throws Exception {
        return new IBatisProducer(this); 
    }

    @Override
    public PollingConsumer createPollingConsumer() throws Exception {
        return new IBatisPollingConsumer(this);
    }

    /**
     * Returns the iBatis SQL client
     */
    public SqlMapClient getSqlClient() throws IOException {
        return getComponent().getSqlMapClient();
    }

    public String getEntityName() {
        return entityName;
    }
}
