package org.apache.camel.impl.converter;

import org.apache.camel.TypeConverter;
import org.apache.camel.spi.Injector;

/**
 * Registry for type converters.
 *
 * @version $Revision: 659798 $
 */
public interface TypeConverterRegistry {

    /**
     * Allows a new type converter to be registered
     *
     * @param toType        the type to convert to
     * @param fromType      the type to convert from
     * @param typeConverter the type converter to use
     */
    void addTypeConverter(Class toType, Class fromType, TypeConverter typeConverter);

    Injector getInjector();
}
