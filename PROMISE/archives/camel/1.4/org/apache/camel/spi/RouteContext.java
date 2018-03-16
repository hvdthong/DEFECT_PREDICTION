package org.apache.camel.spi;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Intercept;
import org.apache.camel.Processor;
import org.apache.camel.model.FromType;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.RouteType;

/**
 * The context used to activate new routing rules
 *
 * @version $Revision: 673335 $
 */
public interface RouteContext {

    Endpoint<? extends Exchange> getEndpoint();

    FromType getFrom();

    RouteType getRoute();

    /**
     * Gets the CamelContext
     */
    CamelContext getCamelContext();

    Processor createProcessor(ProcessorType node) throws Exception;

    /**
     * Resolves an endpoint from the URI
     */
    Endpoint<? extends Exchange> resolveEndpoint(String uri);

    /**
     * Resolves an endpoint from either a URI or a named reference
     */
    Endpoint<? extends Exchange> resolveEndpoint(String uri, String ref);

    /**
     * lookup an object by name and type
     */
    <T> T lookup(String name, Class<T> type);

    /**
     * Lets complete the route creation, creating a single event driven route
     * for the current from endpoint with any processors required
     */
    void commit();

    void addEventDrivenProcessor(Processor processor);

    void intercept(Intercept interceptor);

    Processor createProceedProcessor();

    /**
     * This method retrieves the InterceptStrategy instances this route context.
     *
     * @return InterceptStrategy
     */
    List<InterceptStrategy> getInterceptStrategies();

    /**
     * This method sets the InterceptStrategy instances on this route context.
     *
     * @param interceptStrategies
     */
    void setInterceptStrategies(List<InterceptStrategy> interceptStrategies);

    void addInterceptStrategy(InterceptStrategy interceptStrategy);

    /**
     * This method retrieves the ErrorHandlerWrappingStrategy.
     *  
     * @return ErrorHandlerWrappingStrategy
     */
    ErrorHandlerWrappingStrategy getErrorHandlerWrappingStrategy();
    
    /**
     * This method sets the ErrorHandlerWrappingStrategy.
     * 
     */
    void setErrorHandlerWrappingStrategy(ErrorHandlerWrappingStrategy strategy);

    /**
     * If this flag is true, {@link ProcessorType#addRoutes(RouteContext, java.util.Collection)
     * will not add processor to addEventDrivenProcessor to the RouteContext and it
     * will prevent from adding an EventDrivenRoute.
     * 
     */
    void setIsRouteAdded(boolean value);
    
    /**
     * @see {@link #setIsRouteAdded(boolean)}
     * 
     */
    boolean isRouteAdded();
    
    

}
