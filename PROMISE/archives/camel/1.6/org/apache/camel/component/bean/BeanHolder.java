package org.apache.camel.component.bean;

import org.apache.camel.Processor;

/**
 * Object holder for a bean.
 *
 * @version $Revision: 660275 $
 */
public interface BeanHolder {

    Object getBean() throws Exception;

    Processor getProcessor();

    BeanInfo getBeanInfo();
}
