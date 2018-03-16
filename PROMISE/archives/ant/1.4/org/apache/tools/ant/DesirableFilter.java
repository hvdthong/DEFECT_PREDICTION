package org.apache.tools.ant;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Filters filenames to determine whether or not the file is desirable.
 *
 * @author Jason Hunter [jhunter@servlets.com]
 * @author james@x180.com
 */
public class DesirableFilter implements FilenameFilter {

    /**
     * Test the given filename to determine whether or not it's desirable.
     * This helps tasks filter temp files and files used by CVS.
     */

    public boolean accept(File dir, String name) {
        
        if (name.endsWith("~")) {
            return false;
        }

        if (name.startsWith("#") && name.endsWith("#")) {
            return false;
        }

        if (name.startsWith("%") && name.endsWith("%")) {
            return false;
        }

        /* CVS stuff -- hopefully there won't be a case with
         * an all cap file/dir named "CVS" that somebody wants
         * to keep around...
         */
        
        if (name.equals("CVS")) {
            return false;
        }
        
        /* If we are going to ignore CVS might as well ignore 
         * this one as well...
         */
        if (name.equals(".cvsignore")){
            return false;
        }

        if (name.startsWith(".#")) {
            return false;
        }

        if (name.equals("SCCS")) {
            return false;
        }
    
        if (name.equals("vssver.scc")) {
            return false;
        }

        return true;
    }
}







