package org.apache.camel.spring.spi;

import static org.apache.camel.util.ObjectHelper.notNull;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.spi.ComponentResolver;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * An implementation of {@link ComponentResolver} which tries to find a Camel {@link Component}
 * in the Spring {@link ApplicationContext} first; if its not there it defaults to the auto-discovery mechanism.
 *
 * @version $Revision: 530555 $
 */
public class SpringComponentResolver implements ComponentResolver {
    private final ApplicationContext applicationContext;
    private final ComponentResolver nextResolver;

    public SpringComponentResolver(ApplicationContext applicationContext, ComponentResolver nextResolver) {
        notNull(applicationContext, "applicationContext");
        this.applicationContext = applicationContext;
        this.nextResolver = nextResolver;
    }

    public Component resolveComponent(String name, CamelContext context) throws Exception {
        Object bean = null;
        try {
            bean = applicationContext.getBean(name);
        }
        catch (NoSuchBeanDefinitionException e) {
        }
        if (bean != null) {
            if (bean instanceof Component) {
                return (Component) bean;
            }
            else {
                throw new IllegalArgumentException("Bean with name: " + name + " in spring context is not a Component: " + bean);
            }
        }
        if (nextResolver == null) {
            return null;
        }
        return nextResolver.resolveComponent(name, context);
    }
}
