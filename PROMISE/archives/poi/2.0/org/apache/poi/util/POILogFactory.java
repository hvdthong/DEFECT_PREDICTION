package org.apache.poi.util;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.*;

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
            try{
              String loggerClassName = System.getProperty("org.apache.poi.util.POILogger");
              Class loggerClass = Class.forName(loggerClassName);
              logger = ( POILogger ) loggerClass.newInstance();
            }
            catch(Exception e){
            
              logger = new NullLogger();
            }
            
            logger.initialize(cat);
            
            _loggers.put(cat, logger);
        }
        return logger;
    }
        
