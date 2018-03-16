package org.apache.camel.component.ibatis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;

import org.apache.camel.Message;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultPollingEndpoint;

/**
 * for performing SQL operations using an XML mapping file to abstract away the SQL
 *
 * @version $Revision: 655516 $
 */
public class IBatisEndpoint extends DefaultPollingEndpoint {
    private final String entityName;

    public IBatisEndpoint(String endpointUri, IBatisComponent component, String entityName) {
        super(endpointUri, component);
        this.entityName = entityName;
    }

    public IBatisEndpoint(String endpointUri, String entityName) {
        super(endpointUri);
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

    public void query(Message message) throws IOException, SQLException {
        String name = getEntityName();
        List list = getSqlClient().queryForList(name);
        message.setBody(list);
        message.setHeader("org.apache.camel.ibatis.queryName", name);

    }
}
