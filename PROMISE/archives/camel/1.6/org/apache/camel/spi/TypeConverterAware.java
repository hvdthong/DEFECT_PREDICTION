package org.apache.camel.spi;

import org.apache.camel.TypeConverter;

/**
 * An interface for an object which is interested in being injected with the root {@link TypeConverter}
 * such as for implementing a fallback type converter
 *
 * @see org.apache.camel.impl.converter.DefaultTypeConverter#addFallbackConverter(TypeConverter)
 *         DefaultTypeConverter.addFallbackConverter
 * @version $Revision: 688279 $
 */
public interface TypeConverterAware {

    /**
     * Injects the root type converter.
     *
     * @param parentTypeConverter the root type converter
     */
    void setTypeConverter(TypeConverter parentTypeConverter);
}
