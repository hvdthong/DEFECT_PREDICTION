package org.apache.camel.guice;

import com.google.inject.jsr250.Jsr250Module;
import com.google.inject.matcher.Matchers;
import org.apache.camel.CamelContext;
import org.apache.camel.Consume;
import org.apache.camel.MessageDriven;
import org.apache.camel.Routes;
import org.apache.camel.guice.impl.ConsumerInjection;
import org.apache.camel.guice.impl.EndpointInjector;
import org.apache.camel.guice.impl.ProduceInjector;

/**
 * A base Guice module for creating a {@link CamelContext} leaving it up to the users module
 * to bind a Set<Routes> for the routing rules.
 * <p>
 * To bind the routes you should create a provider method annotated with @Provides and returning Set<Routes> such as
 * <code><pre>
 * public class MyModule extends CamelModule {
 *   &#64;Provides
 *   Set&lt;Routes&gt; routes(Injector injector) { ... }
 * }
 * </pre></code>
 * If you wish to bind all of the bound {@link Routes} implementations available - maybe with some filter applied - then
 * please use the {@link org.apache.camel.guice.CamelModuleWithMatchingRoutes}.
 * <p>
 * Otherwise if you wish to list all of the classes of the {@link Routes} implementations then use the
 * {@link org.apache.camel.guice.CamelModuleWithRouteTypes} module instead.
 *
 * @version $Revision: 707305 $
 */
public class CamelModule extends Jsr250Module {

    protected void configure() {
        super.configure();
        
        bind(CamelContext.class).to(GuiceCamelContext.class).asEagerSingleton();

        bind(EndpointInjector.class);
        bind(ProduceInjector.class);

        ConsumerInjection consumerInjection = new ConsumerInjection();
        requestInjection(consumerInjection);


        bindConstructorInterceptor(Matchers.methodAnnotatedWith(MessageDriven.class), consumerInjection);
        bindConstructorInterceptor(Matchers.methodAnnotatedWith(Consume.class), consumerInjection);
    }

}
