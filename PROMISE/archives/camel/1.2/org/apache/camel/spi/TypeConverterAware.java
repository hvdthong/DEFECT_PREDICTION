package org.apache.camel.spi;

import org.apache.camel.TypeConverter;

/**
 * An interface for an object which is interested in being injected with the root {@link TypeConverter}
 * such as for implementing a fallback type converter
 *
 * @see DefaultTypeConverter#addFallbackConverter(TypeConverter) 
 * @version $Revision: 1.1 $
 */
public interface TypeConverterAware {
    void setTypeConverter(TypeConverter parentTypeConverter);
}
