package org.apache.camel.spring.spi;

import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.context.ApplicationContext;

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
