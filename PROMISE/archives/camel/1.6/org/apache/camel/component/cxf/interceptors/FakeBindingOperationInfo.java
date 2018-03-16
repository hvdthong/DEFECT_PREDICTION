package org.apache.camel.component.cxf.interceptors;

import java.util.Collection;
import java.util.Collections;

import org.apache.cxf.service.model.BindingFaultInfo;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.service.model.ServiceInfo;

/**
 * This class is used to provide the BindingOperationInfo for
 * the FaultOutInterceptor which serves for the RawMessage DataFormat
 *
 */
public class FakeBindingOperationInfo extends BindingOperationInfo {

    public FakeBindingOperationInfo() {
        super();
    }

    public boolean isUnwrapped() {
        return false;
    }



}
