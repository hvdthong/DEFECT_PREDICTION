package org.apache.camel.spring.spi;

import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.context.ApplicationContext;

/**
 * Dependeny Injected Bean injector. 
 *
 * @version $Revision: 673477 $
 * @deprecated not used. Will be removed in Camel 2.0.
 */
public class BeanInjector extends BeanConfigurerSupport {

    public BeanInjector(ApplicationContext applicationContext) throws Exception {
        setBeanFactory(applicationContext);
        afterPropertiesSet();
    }

    public void inject(Object bean) {
        configureBean(bean);
    }
}
