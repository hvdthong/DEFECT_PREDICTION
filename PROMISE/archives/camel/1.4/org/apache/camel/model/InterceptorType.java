package org.apache.camel.model;

import javax.xml.bind.annotation.XmlType;

import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.spi.RouteContext;

/**
 * Base class for interceptor types.
 * 
 * @version $Revision: 660266 $
 */
@XmlType(name = "interceptorType")
public abstract class InterceptorType extends OutputType implements Block {
    public abstract DelegateProcessor createInterceptor(RouteContext routeContext) throws Exception;
}
