package org.apache.camel.guice;

import java.util.Set;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

import org.apache.camel.Routes;
import org.guiceyfruit.Injectors;

/**
 * A Guice Module which injects the CamelContext with all available implementations
 * of {@link Routes} which are bound to Guice with an optional {@link Matcher} to filter out the classes required.
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
 * @version $Revision: 706111 $
 */
public class CamelModuleWithMatchingRoutes extends CamelModule {
    private final Matcher<Class> matcher;

    public CamelModuleWithMatchingRoutes() {
        this(Matchers.subclassesOf(Routes.class));
    }

    public CamelModuleWithMatchingRoutes(Matcher<Class> matcher) {
        this.matcher = matcher;
    }

    @Provides
    Set<Routes> routes(Injector injector) {
        return Injectors.getInstancesOf(injector, matcher);
    }
}
