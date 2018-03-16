package org.apache.camel.component.cxf;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.HeaderFilterStrategyAware;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.camel.util.CamelContextHelper;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.camel.util.URISupport;


/**
 *
 * @version $Revision: 682597 $
 */
public class CxfSoapComponent extends DefaultComponent implements HeaderFilterStrategyAware {

    private HeaderFilterStrategy headerFilterStrategy;

    public CxfSoapComponent() {
        setHeaderFilterStrategy(new CxfHeaderFilterStrategy());
    }
    
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        Map soapProps = IntrospectionSupport.extractProperties(parameters, "soap.");
        if (parameters.size() > 0) {
            remaining += "?" + URISupport.createQueryString(parameters);
        }
        Endpoint endpoint = CamelContextHelper.getMandatoryEndpoint(getCamelContext(), remaining);
        CxfSoapEndpoint soapEndpoint = new CxfSoapEndpoint(endpoint, getHeaderFilterStrategy());
        setProperties(soapEndpoint, soapProps);
        soapEndpoint.init();
        return soapEndpoint;
    }

    @Override
    protected boolean useIntrospectionOnEndpoint() {
        return false;
    }

    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    public void setHeaderFilterStrategy(HeaderFilterStrategy strategy) {
        headerFilterStrategy = strategy;
        
    }

}
