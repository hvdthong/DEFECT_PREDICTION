package org.apache.camel.osgi;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class CamelNamespaceHandler extends org.apache.camel.spring.handler.CamelNamespaceHandler {

    public void init() {
        super.init();
        registerParser("camelContext", new CamelContextBeanDefinitionParser(CamelContextFactoryBean.class));
    }

    protected JAXBContext createJaxbContext() throws JAXBException {
        StringBuilder packages = new StringBuilder();
        for (Class cl : getJaxbPackages()) {
            if (packages.length() > 0) {
                packages.append(":");
            }
            packages.append(cl.getName().substring(0, cl.getName().lastIndexOf('.')));
        }
        return JAXBContext.newInstance(packages.toString(), getClass().getClassLoader());
    }

    protected Set<Class> getJaxbPackages() {
        Set<Class> classes = new HashSet<Class>();
        classes.add(org.apache.camel.osgi.CamelContextFactoryBean.class);
        classes.add(org.apache.camel.spring.CamelContextFactoryBean.class);
        classes.add(org.apache.camel.model.RouteType.class);
        classes.add(org.apache.camel.model.config.StreamResequencerConfig.class);
        classes.add(org.apache.camel.model.dataformat.DataFormatType.class);
        classes.add(org.apache.camel.model.language.ExpressionType.class);
        classes.add(org.apache.camel.model.loadbalancer.LoadBalancerType.class);
        return classes;
    }


}
