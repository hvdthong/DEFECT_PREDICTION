package org.apache.camel.spring;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.ResolverUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

/**
 * A helper class which will find all {@link RouteBuilder} instances on the classpath
 *
 * @version $Revision: 656103 $
 */
public class RouteBuilderFinder {
    private static final transient Log LOG = LogFactory.getLog(RouteBuilderFinder.class);
    private final SpringCamelContext camelContext;
    private final String[] packages;
    private ApplicationContext applicationContext;
    private ResolverUtil resolver = new ResolverUtil();
    private BeanPostProcessor beanPostProcessor;

    public RouteBuilderFinder(SpringCamelContext camelContext, String[] packages, ClassLoader classLoader, BeanPostProcessor postProcessor) {
        this.camelContext = camelContext;
        this.applicationContext = camelContext.getApplicationContext();
        this.packages = packages;
        this.beanPostProcessor = postProcessor;

        Set set = resolver.getClassLoaders();
        set.clear();
        set.add(classLoader);
/*
        set.add(classLoader);
        set.add(applicationContext.getClassLoader());
        set.add(getClass().getClassLoader());
*/
    }


    public String[] getPackages() {
        return packages;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    /**
     * Appends all the {@link RouteBuilder} instances that can be found on the classpath
     */
    public void appendBuilders(List<RouteBuilder> list) throws IllegalAccessException, InstantiationException {
        resolver.findImplementations(RouteBuilder.class, packages);
        Set<Class> classes = resolver.getClasses();
        for (Class aClass : classes) {
            if (shouldIgnoreBean(aClass)) {
                continue;
            }
            if (isValidClass(aClass)) {
                RouteBuilder builder = instantiateBuilder(aClass);
                if (beanPostProcessor != null) {
                    beanPostProcessor.postProcessBeforeInitialization(builder, builder.toString());
                }
                list.add(builder);
            }
        }
    }

    public void destroy() throws Exception {
    }

    /**
     * Lets ignore beans that are not explicitly configured in the spring.xml
     */
    protected boolean shouldIgnoreBean(Class type) {
        Map beans = applicationContext.getBeansOfType(type, true, true);
        if (beans == null || beans.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the object is non-abstract and supports a zero argument constructor
     */
    protected boolean isValidClass(Class type) {
        if (!Modifier.isAbstract(type.getModifiers()) && !type.isInterface()) {
            return true;
        }
        return false;
    }

    protected RouteBuilder instantiateBuilder(Class type) throws IllegalAccessException, InstantiationException {
        return (RouteBuilder) camelContext.getInjector().newInstance(type);
    }
}
