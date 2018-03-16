package org.apache.camel.guice.impl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.spi.AnnotationProviderFactory;
import com.google.inject.spi.InjectionAnnotation;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.impl.CamelPostProcessorHelper;
import org.apache.camel.util.ObjectHelper;

/**
 * Injects values into the {@link Produce} injection point
 *
 * @version $Revision: 724293 $
 */
@InjectionAnnotation(Produce.class)
public class ProduceInjector extends CamelPostProcessorHelper implements AnnotationProviderFactory {

    @Inject
    public ProduceInjector(CamelContext camelContext) {
        super(camelContext);
    }

    public Provider createProvider(final AnnotatedElement member) {
        final Produce inject = member.getAnnotation(Produce.class);
        Objects.nonNull(inject, "@Produce is not present!");


        final Class<?> type;
        final String injectionPointName;
        final String endpointRef = inject.ref();
        final String uri = inject.uri();

        if (member instanceof Field) {
            Field field = (Field) member;
            type = field.getType();
            injectionPointName = field.getName();
        } else if (member instanceof Method) {
            Method method = (Method) member;
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1) {
                type = parameterTypes[0];
                injectionPointName = ObjectHelper.getPropertyName(method);
            } else {
                throw new UnsupportedOperationException("Only a single method parameter value supported for @Produce on " + method);
            }
        } else {
            throw new UnsupportedOperationException("Annotated element " + member + " not supported");
        }

        return new Provider() {
            public Object get() {
                return getInjectionValue(type, uri, endpointRef, injectionPointName);
            }
        };
    }
}
