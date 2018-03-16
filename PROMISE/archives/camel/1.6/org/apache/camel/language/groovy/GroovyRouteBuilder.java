package org.apache.camel.language.groovy;

import groovy.lang.GroovyShell;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

/**
 * @version $Revision: 640731 $
 */
public abstract class GroovyRouteBuilder extends RouteBuilder {
    public GroovyRouteBuilder() {
        init();
    }

    public GroovyRouteBuilder(CamelContext context) {
        super(context);
        init();
    }

    private void init() {
        ClassLoader loader = getClass().getClassLoader();
        GroovyShell shell = new GroovyShell(loader);
        shell.evaluate(loader.getResourceAsStream("org/apache/camel/language/groovy/ConfigureCamel.groovy"));

    }
}
