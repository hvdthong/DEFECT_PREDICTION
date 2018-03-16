package org.apache.camel.component.rmi;

import java.rmi.registry.Registry;

import org.apache.camel.CamelContext;
import org.apache.camel.component.bean.ParameterMappingStrategy;
import org.apache.camel.component.bean.RegistryBean;

/**
 * @version $Revision: 641680 $
 */
public class RmiRegistryBean extends RegistryBean {

    private final Registry registry;

    public RmiRegistryBean(CamelContext context, String name, Registry registry) {
        super(context, name);
        this.registry = registry;
    }

    public RmiRegistryBean(CamelContext context, String name, ParameterMappingStrategy parameterMappingStrategy, Registry registry) {
        super(context, name, parameterMappingStrategy);
        this.registry = registry;
    }

    @Override
    protected Object lookupBean() throws Exception {
        return registry.lookup(getName());
    }
}
