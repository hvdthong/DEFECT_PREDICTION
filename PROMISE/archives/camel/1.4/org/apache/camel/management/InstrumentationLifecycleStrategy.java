package org.apache.camel.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.Service;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.model.ExceptionType;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.RouteType;
import org.apache.camel.spi.InstrumentationAgent;
import org.apache.camel.spi.LifecycleStrategy;
import org.apache.camel.spi.RouteContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JMX agent that registeres Camel lifecycle events in JMX.
 *
 * @version $Revision: 676133 $
 *
 */
public class InstrumentationLifecycleStrategy implements LifecycleStrategy {
    private static final transient Log LOG = LogFactory.getLog(InstrumentationProcessor.class);

    private InstrumentationAgent agent;
    private CamelNamingStrategy namingStrategy;
    private boolean initialized;

    private Map<Endpoint, InstrumentationProcessor> interceptorMap =
        new HashMap<Endpoint, InstrumentationProcessor>();

    public InstrumentationLifecycleStrategy() {
        this(new DefaultInstrumentationAgent());
    }

    public InstrumentationLifecycleStrategy(InstrumentationAgent agent) {
        this.agent = agent;
    }
    /**
     * Constructor for camel context that has been started.
     *
     * @param agent
     * @param context
     */
    public InstrumentationLifecycleStrategy(InstrumentationAgent agent,
            CamelContext context) {
        this.agent = agent;
        onContextStart(context);
    }

    public void onContextStart(CamelContext context) {
        if (context instanceof DefaultCamelContext) {
            try {
                initialized = true;
                DefaultCamelContext dc = (DefaultCamelContext)context;
                dc.addService(agent);
                namingStrategy = new CamelNamingStrategy(agent.getMBeanObjectDomainName());
                ManagedService ms = new ManagedService(dc);
                agent.register(ms, getNamingStrategy().getObjectName(dc));
            } catch (Exception e) {
                LOG.warn("Could not register CamelContext MBean", e);
            }
        }
    }

    public void onEndpointAdd(Endpoint<? extends Exchange> endpoint) {

        if (!initialized) {
            return;
        }

        try {
            ManagedEndpoint me = new ManagedEndpoint(endpoint);
            agent.register(me, getNamingStrategy().getObjectName(me));
        } catch (JMException e) {
            LOG.warn("Could not register Endpoint MBean", e);
        }
    }

    public void onRoutesAdd(Collection<Route> routes) {

        if (!initialized) {
            return;
        }

        for (Route route : routes) {
            try {
                ManagedRoute mr = new ManagedRoute(route);
                InstrumentationProcessor interceptor = interceptorMap.get(route.getEndpoint());
                if (interceptor == null) {
                    LOG.warn("Instrumentation processor not found for route endpoint "
                             + route.getEndpoint());
                } else {
                    interceptor.setCounter(mr);
                }
                agent.register(mr, getNamingStrategy().getObjectName(mr));
            } catch (JMException e) {
                LOG.warn("Could not register Route MBean", e);
            }
        }
    }

    public void onServiceAdd(CamelContext context, Service service) {

        if (!initialized) {
            return;
        }
        if (service instanceof ServiceSupport) {
            try {
                ManagedService ms = new ManagedService((ServiceSupport)service);
                agent.register(ms, getNamingStrategy().getObjectName(context, ms));
            } catch (JMException e) {
                LOG.warn("Could not register Service MBean", e);
            }
        }
    }

    public void onRouteContextCreate(RouteContext routeContext) {

        if (!initialized) {
            return;
        }

        Map<ProcessorType, PerformanceCounter> counterMap =
            new HashMap<ProcessorType, PerformanceCounter>();

        RouteType route = routeContext.getRoute();
        
        for (ProcessorType processor : route.getOutputs()) {
            ObjectName name = null;
            try {
                name = getNamingStrategy().getObjectName(routeContext, processor);

                PerformanceCounter pc = new PerformanceCounter();
                agent.register(pc, name);

                counterMap.put(processor, pc);
            } catch (MalformedObjectNameException e) {
                LOG.warn("Could not create MBean name: " + name, e);
            } catch (JMException e) {
                LOG.warn("Could not register PerformanceCounter MBean: " + name, e);
            }
        }
        
        routeContext.addInterceptStrategy(new InstrumentationInterceptStrategy(counterMap));

        routeContext.setErrorHandlerWrappingStrategy(
                new InstrumentationErrorHandlerWrappingStrategy(counterMap));


        RouteType routeType = routeContext.getRoute();
        if (routeType.getInputs() != null && !routeType.getInputs().isEmpty()) {
            if (routeType.getInputs().size() > 1) {
                LOG.warn("Add InstrumentationProcessor to first input only.");
            }

            Endpoint endpoint  = routeType.getInputs().get(0).getEndpoint();

            List<ProcessorType<?>> exceptionHandlers = new ArrayList<ProcessorType<?>>();
            List<ProcessorType<?>> outputs = new ArrayList<ProcessorType<?>>();

            for (ProcessorType output : routeType.getOutputs()) {
                if (output instanceof ExceptionType) {
                    exceptionHandlers.add(output);
                } else {
                    outputs.add(output);
                }
            }

            routeType.clearOutput();

            routeType.getOutputs().addAll(exceptionHandlers);

            InstrumentationProcessor processor = new InstrumentationProcessor();
            routeType.intercept(processor);

            for (ProcessorType<?> processorType : outputs) {
                routeType.addOutput(processorType);
            }

            interceptorMap.put(endpoint, processor);
        }

    }

    public CamelNamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(CamelNamingStrategy strategy) {
        this.namingStrategy = strategy;
    }

    public void setAgent(InstrumentationAgent agent) {
        this.agent = agent;
    }
}
