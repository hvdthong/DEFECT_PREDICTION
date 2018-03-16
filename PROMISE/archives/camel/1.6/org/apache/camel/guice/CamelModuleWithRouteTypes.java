package org.apache.camel.guice;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.apache.camel.Routes;



/**
 * A Guice Module which injects the CamelContext with the specified {@link Routes} types - which are then injected by Guice.
 * <p>
 * If you wish to bind all of the bound {@link Routes} implementations available - maybe with some filter applied - then
 * please use the {@link org.apache.camel.guice.CamelModuleWithMatchingRoutes}.
 * <p>
 * Or if you would like to specify exactly which {@link Routes} to bind then use the {@link CamelModule} and create a provider
 * method annotated with @Provides and returning Set<Routes> such as
 * <code><pre>
 * public class MyModule extends CamelModule {
 *   &#64;Provides
 *   Set&lt;Routes&gt; routes(Injector injector) { ... }
 * }
 * </pre></code>
 * 
 *
 * @version $Revision: 706111 $
 */
public class CamelModuleWithRouteTypes extends CamelModule {
    private Set<Class<? extends Routes>> routes;

    public CamelModuleWithRouteTypes(Class<? extends Routes>... routes) {
        this(Sets.newHashSet(routes));
    }

    public CamelModuleWithRouteTypes(Set<Class<? extends Routes>> routes) {
        this.routes = routes;
    }

    @Provides
    Set<Routes> routes(Injector injector) {
        Set<Routes> answer = Sets.newHashSet();
        for (Class<? extends Routes> type : routes) {
            Routes route = injector.getInstance(type);
            answer.add(route);
        }
        return answer;
    }
}
