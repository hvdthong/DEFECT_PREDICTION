package org.apache.camel;

/**
 * An interface which provides the processing logic as a pluggable processor
 *
 * @version $Revision: 673954 $
 */
public interface Intercept {

    Processor getInterceptorLogic();

    void setInterceptorLogic(Processor interceptorLogic);
}
