package org.apache.tools.ant.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * A FilterSetCollection is a collection of filtersets each of which may have
 * a different start/end token settings.
 *
 * @author     <A href="mailto:conor@apache.org">Conor MacNeill</A>
 */
public class FilterSetCollection {
    
    private Vector filterSets = new Vector();

    public FilterSetCollection() {
    }
    
    public FilterSetCollection(FilterSet filterSet) {
        addFilterSet(filterSet);
    }
    
    
    public void addFilterSet(FilterSet filterSet) {
        filterSets.addElement(filterSet);
    }
    
    /**
     * Does replacement on the given string with token matching.
     * This uses the defined begintoken and endtoken values which default to @ for both.
     *
     * @param line  The line to process the tokens in.
     * @return      The string with the tokens replaced.
     */
    public String replaceTokens(String line) {
        String replacedLine = line;
        for (Enumeration e = filterSets.elements(); e.hasMoreElements();) {
            FilterSet filterSet = (FilterSet)e.nextElement();
            replacedLine = filterSet.replaceTokens(replacedLine);
        }
        return replacedLine;
    }
    
    /**
    * Test to see if this filter set it empty.
    *
    * @return   Return true if there are filter in this set otherwise false.
    */
    public boolean hasFilters() {
        for (Enumeration e = filterSets.elements(); e.hasMoreElements();) {
            FilterSet filterSet = (FilterSet)e.nextElement();
            if (filterSet.hasFilters()) {
                return true;
            }
        }
        return false;
    }
}
 


