package org.apache.camel.component.dataset;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.CamelContextHelper;

/**
 *
 * @version $Revision: 659842 $
 */
public class DataSetComponent extends DefaultComponent<Exchange> {

    @Override
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        DataSet dataSet = CamelContextHelper.mandatoryLookup(getCamelContext(), remaining, DataSet.class);
        return new DataSetEndpoint(uri, this, dataSet);
    }
}
