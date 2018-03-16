package org.apache.camel.language.juel;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.el.BeanELResolver;
import javax.el.ELContext;
import javax.el.PropertyNotFoundException;

/**
 * An extension of the JUEL {@link BeanELResolver} which also supports the resolving of methods
 *
 * @version $Revision: 647893 $
 */
public class BeanAndMethodELResolver extends BeanELResolver {
    public BeanAndMethodELResolver() {
        super(false);
    }

    @Override
    public Object getValue(ELContext elContext, Object base, Object property) {
        try {
            return super.getValue(elContext, base, property);
        } catch (PropertyNotFoundException e) {
            Method method = findMethod(elContext, base, property);
            if (method != null) {
                elContext.setPropertyResolved(true);
                return method;
            } else {
                throw e;
            }
        }
    }

    protected Method findMethod(ELContext elContext, Object base, Object property) {
        if (base != null && property instanceof String) {
            Method[] methods = base.getClass().getMethods();
            List<Method> matching = new ArrayList<Method>();
            for (Method method : methods) {
                if (method.getName().equals(property) && Modifier.isPublic(method.getModifiers())) {
                    matching.add(method);
                }
            }
            int size = matching.size();
            if (size > 0) {
                if (size > 1) {
                    for (Method method : matching) {
                        Class<?>[] paramTypes = method.getParameterTypes();
                        if (paramTypes.length == 1) {
                            return method;
                        }
                    }
                }
                return matching.get(0);
            }
        }
        return null;
    }
}
