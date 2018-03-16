package org.apache.camel.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.TypeConverter;
import org.apache.camel.impl.converter.TypeConverterRegistry;
import org.apache.camel.spi.Injector;

/**
 * Registry for reporting type converters.
 * <p/>
 * Used by the camel-maven-plugin.
 */
public class ReportingTypeConverterRegistry implements TypeConverterRegistry {
    private List<String> errors = new ArrayList<String>();

    public String[] getErrors() {
        return errors.toArray(new String[errors.size()]);
    }

    public void addTypeConverter(Class toType, Class fromType, TypeConverter typeConverter) {
        if (errors.size() == 0) {
            errors.add("Method should not be invoked.");
        }
    }

    public Injector getInjector() {
        return null;
    }

}
