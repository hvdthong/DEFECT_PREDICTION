package org.apache.poi.util;

import org.apache.commons.logging.Log;

import java.util.*;

/**
 * A logger class that strives to make it as easy as possible for
 * developers to write log calls, while simultaneously making those
 * calls as cheap as possible by performing lazy evaluation of the log
 * message.
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 * @author Glen Stampoultzis (glens at apache.org)
 * @author Nicola Ken Barozzi (nicolaken at apache.org)
 */
public class SystemOutLogger extends POILogger
{
    private String cat;

    public void initialize(final String cat)
    {
       this.cat=cat;
    }
    
    /**
     * Log a message
     *
     * @param level One of DEBUG, INFO, WARN, ERROR, FATAL
     * @param obj1 The object to log.
     */

    public void log(final int level, final Object obj1)
    {
        if (check(level))
            System.out.println("["+cat+"] "+obj1);
    }

    /**
     * Check if a logger is enabled to log at the specified level
     *
     * @param level One of DEBUG, INFO, WARN, ERROR, FATAL
     * @see #DEBUG
     * @see #INFO
     * @see #WARN
     * @see #ERROR
     * @see #FATAL
     */
    public boolean check(final int level)
    {
        int currentLevel = Integer.parseInt(System.getProperty("poi.log.level", WARN + ""));
        if (level >= currentLevel)
            return true;
        else
            return false;
    }

 

