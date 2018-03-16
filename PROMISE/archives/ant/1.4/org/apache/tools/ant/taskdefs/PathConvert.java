package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.*;

import java.util.*;
import java.io.File;

/**
 * This task converts path and classpath information to a specific target OS format.
 * The resulting formatted path is placed into a specified property.
 * <p>
 * LIMITATION: Currently this implementation groups all machines into one of two
 * types: Unix or Windows.  Unix is defined as NOT windows.
 *
 * @author Larry Streepy <a href="mailto:streepy@healthlanguage.com">streepy@healthlanguage.com</a>
 */
public class PathConvert extends Task {

    /**
     * Helper class, holds the nested <map> values.  Elements will look like this:
     * &lt;map from="d:" to="/foo"/>
     * <p>
     * When running on windows, the prefix comparison will be case insensitive.
     */
    public class MapEntry {

        /**
         * Set the "from" attribute of the map entry
         */
        public void setFrom( String from ) {
            this.from = from;
        }

        /**
         * Set the "to" attribute of the map entry
         */
        public void setTo( String to ) {
            this.to = to;
        }

        /**
         * Apply this map entry to a given path element
         * @param elem Path element to process
         * @return String Updated path element after mapping
         */
        public String apply( String elem ) {
            if( from == null || to == null ) {
                throw new BuildException( "Both 'from' and 'to' must be set in a map entry" );
            }

            String cmpElem = onWindows ? elem.toLowerCase() : elem;
            String cmpFrom = onWindows ? from.toLowerCase() : from;


            if( cmpElem.startsWith( cmpFrom ) ) {
                int len = from.length();

                if( len >= elem.length() ) {
                    elem = to;
                } else {
                    elem = to + elem.substring( len );
                }
            }

            return elem;
        }

        private String from = null;
        private String to = null;
    }

    /**
     * Create a nested PATH element
     */
    public Path createPath() {

        if( isReference() )
            throw noChildrenAllowed();

        if( path == null ) {
            path = new Path(getProject());
        }
        return path.createPath();
    }

    /**
     * Create a nested MAP element
     */
    public MapEntry createMap() {

        MapEntry entry = new MapEntry();
        prefixMap.addElement( entry );
        return entry;
    }

    /**
     * Set the value of the targetos attribute
     */
    public void setTargetos( String target ) {

        targetOS = target.toLowerCase();

        if( ! targetOS.equals( "windows" ) && ! target.equals( "unix" ) ) {
            throw new BuildException( "targetos must be one of 'unix' or 'windows'" );
        }


        targetWindows = targetOS.equals("windows");
    }

    /**
     * Set the value of the proprty attribute - this is the property into which our
     * converted path will be placed.
     */
    public void setProperty( String p ) {
        property = p;
    }

    /**
     * Adds a reference to a PATH or FILESET defined elsewhere.
     */
    public void setRefid(Reference r) {
        if( path != null )
            throw noChildrenAllowed();

        refid = r;
    }

    /**
     * Override the default path separator string for the target os
     */
    public void setPathSep( String sep ) {
        pathSep = sep;
    }

    /**
     * Override the default directory separator string for the target os
     */
    public void setDirSep( String sep ) {
        dirSep = sep;
    }

    /**
     * Has the refid attribute of this element been set?
     */
    public boolean isReference() {
        return refid != null;
    }

    /**
     * Do the execution.
     */
    public void execute() throws BuildException {

        if( isReference() ) {
            path = new Path(getProject()).createPath();

            Object obj = refid.getReferencedObject(getProject());

            if( obj instanceof Path ) {
                path.setRefid(refid);
            } else if( obj instanceof FileSet ) {
                FileSet fs = (FileSet)obj;
                path.addFileset( fs );
            } else {
                throw new BuildException( "'refid' does not refer to a path or fileset" );
            }
        }



        String osname = System.getProperty("os.name").toLowerCase();
        onWindows = ( osname.indexOf("windows") >= 0 );

        char fromDirSep = onWindows ? '\\' : '/';
        char toDirSep   = dirSep.charAt(0);

        StringBuffer rslt = new StringBuffer( 100 );

        String[] elems = path.list();

        for( int i=0; i < elems.length; i++ ) {
            String elem = elems[i];



            elem = elem.replace( fromDirSep, toDirSep );

            if( i != 0 ) rslt.append( pathSep );
            rslt.append( elem );
        }

        String value = rslt.toString();

        log( "Set property " + property + " = " + value, Project.MSG_VERBOSE );

        getProject().setProperty( property, value );
    }

    /**
     * Apply the configured map to a path element.  The map is used to convert
     * between Windows drive letters and Unix paths.  If no map is configured,
     * then the input string is returned unchanged.
     *
     * @param elem The path element to apply the map to
     * @return String Updated element
     */
    private String mapElement( String elem ) {

        int size = prefixMap.size();

        if( size != 0 ) {


            for( int i=0; i < size; i++ ) {
                MapEntry entry = (MapEntry)prefixMap.elementAt(i);
                String newElem = entry.apply( elem );


                if( newElem != elem ) {
                    elem = newElem;
                }
            }
        }

        return elem;
    }

    /**
     * Validate that all our parameters have been properly initialized.
     * @throws BuildException if something is not setup properly
     */
    private void validateSetup() throws BuildException {

        if( path == null )
            throw new BuildException( "You must specify a path to convert" );

        if( property == null )
            throw new BuildException( "You must specify a property" );


        if( targetOS == null && pathSep == null && dirSep == null )
            throw new BuildException( "You must specify at least one of targetOS, dirSep, or pathSep" );

        String dsep = File.separator;
        String psep = File.pathSeparator;

        if( targetOS != null ) {
            psep = targetWindows ? ";" : ":";
            dsep = targetWindows ? "\\" : "/";
        }

            psep = pathSep;
        }

            dsep = dirSep;
        }

        pathSep = psep;
        dirSep = dsep;
    }

    /**
     * Creates an exception that indicates that this XML element must
     * not have child elements if the refid attribute is set.  
     */
    private BuildException noChildrenAllowed() {
        return new BuildException("You must not specify nested PATH elements when using refid");
    }


}
