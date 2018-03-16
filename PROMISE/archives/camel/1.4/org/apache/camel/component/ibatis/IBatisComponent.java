package org.apache.camel.component.ibatis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * for performing SQL operations using an XML mapping file to abstract away the SQL
 *
 * @version $Revision: 647667 $
 */
public class IBatisComponent extends DefaultComponent {
    public static final String DEFAULT_CONFIG_URI = "SqlMapConfig.xml";
    private static final transient Log LOG = LogFactory.getLog(IBatisComponent.class);


    private SqlMapClient sqlMapClient;
    private Resource sqlMapResource;

    public IBatisComponent() {
    }

    public IBatisComponent(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    public SqlMapClient getSqlMapClient() throws IOException {
        if (sqlMapClient == null) {
            sqlMapClient = createSqlMapClient();
        }
        return sqlMapClient;
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    public Resource getSqlMapResource() {
        if (sqlMapResource == null) {
            sqlMapResource = new ClassPathResource(DEFAULT_CONFIG_URI);
            LOG.debug("Defaulting to use the iBatis configuration from: " + sqlMapResource);
        }
        return sqlMapResource;
    }

    public void setSqlMapResource(Resource sqlMapResource) {
        this.sqlMapResource = sqlMapResource;
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new IBatisEndpoint(uri, this, remaining);
    }

    protected SqlMapClient createSqlMapClient() throws IOException {
        InputStream in = getSqlMapResource().getInputStream();
        return SqlMapClientBuilder.buildSqlMapClient(in);
    }
}
