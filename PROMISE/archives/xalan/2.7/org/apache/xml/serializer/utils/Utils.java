package org.apache.xml.serializer.utils;

import java.util.Hashtable;

/**
 * This class contains utilities used by the serializer.
 * 
 * This class is not a public API, it is only public because it is
 * used by org.apache.xml.serializer.
 * 
 * @xsl.usage internal
 */
public final class Utils
{
    /**
     * A singleton Messages object is used to load the 
     * given resource bundle just once, it is
     * used by multiple transformations as long as the JVM stays up.
     */
    public static final org.apache.xml.serializer.utils.Messages messages= 
        new org.apache.xml.serializer.utils.Messages(
            "org.apache.xml.serializer.utils.SerializerMessages");
}
