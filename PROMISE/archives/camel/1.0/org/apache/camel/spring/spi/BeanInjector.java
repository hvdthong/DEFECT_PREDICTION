package org.apache.camel.spring.spi;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.wiring.BeanConfigurerSupport;

/**
 * @version $Revision: 1.1 $
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
