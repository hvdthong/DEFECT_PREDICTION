package org.apache.camel.component.cxf;

import org.apache.camel.impl.DefaultHeaderFilterStrategy;

/**
 * @version $Revision: 680645 $
 */
public class CxfHeaderFilterStrategy extends DefaultHeaderFilterStrategy {

    public CxfHeaderFilterStrategy() {
        initialize();  
    }

    protected void initialize() {
        getOutFilter().add(CxfConstants.OPERATION_NAME);
        getOutFilter().add(CxfConstants.OPERATION_NAMESPACE);
    }

}
