package org.apache.camel.model;

import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.impl.RouteContext;

import javax.xml.bind.annotation.XmlType;

/**
 * @version $Revision: 1.1 $
 */
@XmlType(name = "interceptorType")
public abstract class InterceptorType {
    public abstract DelegateProcessor createInterceptor(RouteContext routeContext) throws Exception;
}
