package org.apache.camel.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.camel.TypeConverter;
import org.apache.camel.impl.converter.AnnotationTypeConverterLoader;
import org.apache.camel.impl.converter.TypeConverterRegistry;
import org.apache.camel.util.ObjectHelper;

/**
 * Type converter loader that is capable of reporting the loaded type converters.
 * <p/>
 * Used by the camel-maven-plugin.
 */
public class ReportingTypeConverterLoader extends AnnotationTypeConverterLoader {

    private static final Comparator<TypeMapping> COMPARE_LAST_LOADED_FIRST = new Comparator<TypeMapping>() {
        public int compare(TypeMapping t1, TypeMapping t2) {
            if (ObjectHelper.equal(t1.fromType, t2.fromType)) {
                return ObjectHelper.equal(t1.toType, t2.toType) ? t1.index - t2.index : ObjectHelper
                    .compare(getTypeName(t1.toType), getTypeName(t2.toType));
            }
            return ObjectHelper.compare(getTypeName(t1.fromType), getTypeName(t2.fromType));
        }

    };
    private List<TypeMapping> typeMappings = new ArrayList<TypeMapping>();

    public TypeMapping[] getTypeConversions() {
        Collections.sort(typeMappings, COMPARE_LAST_LOADED_FIRST);
        return typeMappings.toArray(new TypeMapping[typeMappings.size()]);
    }

    protected void registerTypeConverter(TypeConverterRegistry registry, Method method, Class toType,
                                         Class fromType, TypeConverter typeConverter) {

        TypeMapping mapping = new TypeMapping(toType, fromType, typeConverter.getClass(), method);
        typeMappings.add(mapping);
    }

    private static String getTypeName(Class type) {
        return type != null ? type.getName() : null;
    }

    /**
     * Represents a mapping from one type (which can be null) to another
     *
     * Used by the camel-maven-plugin.
     */
    public static class TypeMapping {
        private static int counter;
        private Class toType;
        private Class fromType;
        private Class converterType;
        private Method method;
        private int index;

        public TypeMapping(Class toType, Class fromType, Class converterType, Method method) {
            this.toType = toType;
            this.fromType = fromType;
            this.converterType = converterType;
            this.method = method;
            this.index = counter++;
        }

        public Class getFromType() {
            return fromType;
        }

        public Class getToType() {
            return toType;
        }

        public Class getConverterType() {
            return converterType;
        }

        public Method getMethod() {
            return method;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof TypeMapping) {
                TypeMapping that = (TypeMapping)object;
                return this.index == that.index;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int answer = toType.hashCode();
            if (fromType != null) {
                answer *= 37 + fromType.hashCode();
            }
            return answer;
        }

        @Override
        public String toString() {
            return "[" + fromType.getSimpleName() + "=>" + toType.getSimpleName() + "]";
        }
    }

}
