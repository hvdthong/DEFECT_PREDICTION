package org.apache.poi.util;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.*;

import org.apache.commons.logging.*;

/**
 * Provides logging without clients having to mess with
 * configuration/initialization.
 *
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Marc Johnson (mjohnson at apache dot org)
 * @author Nicola Ken Barozzi (nicolaken at apache.org)
 */

public class POILogFactory
{
    private static LogFactory   _creator = LogFactory.getFactory();

    private static Map          _loggers = new HashMap();;


    /**
     * construct a POILogFactory.
     */

    private POILogFactory()
    {
    }

    /**
     * Get a logger, based on a class name
     *
     * @param theclass the class whose name defines the log
     *
     * @return a POILogger for the specified class
     */

    public static POILogger getLogger(final Class theclass)
    {
        return getLogger(theclass.getName());
    }
    
    /**
     * Get a logger, based on a String
     *
     * @param cat the String that defines the log
     *
     * @return a POILogger for the specified class
     */

    public static POILogger getLogger(final String cat)
    {
        POILogger logger = null;

        if (_loggers.containsKey(cat))
        {
            logger = ( POILogger ) _loggers.get(cat);
        }
        else
        {
            logger = new POILogger(_creator.getInstance(cat));
            _loggers.put(cat, logger);
        }
        return logger;
    }
        
