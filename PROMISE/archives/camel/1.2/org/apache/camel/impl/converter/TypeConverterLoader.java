package org.apache.camel.impl.converter;

/**
 * @version $Revision: 563607 $
 */
public interface TypeConverterLoader {
    /**
     * A pluggable strategy to load type converters into a registry from some kind of mechanism
     *
     * @param registry the registry to load the type converters into
     * @throws Exception if the type converters could not be loaded
     */
    void load(TypeConverterRegistry registry) throws Exception;
}
