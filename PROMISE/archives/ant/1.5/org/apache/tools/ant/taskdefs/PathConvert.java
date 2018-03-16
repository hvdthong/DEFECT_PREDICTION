package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import java.util.StringTokenizer;
import java.util.Vector;
import java.io.File;

/**
 * Converts path and classpath information to a specific target OS
 * format. The resulting formatted path is placed into the specified property.
 *
 * @author Larry Streepy <a href="mailto:streepy@healthlanguage.com">
 *      streepy@healthlanguage.com</a>
 * @since Ant 1.4
 * @ant.task category="utility"
 */
public class PathConvert extends Task {

    /**
     * Path to be converted
     */
    private Path path = null;
    /**
     * Reference to path/fileset to convert
     */
    private Reference refid = null;
    /**
     * The target OS type
     */
    private String targetOS = null;
    /**
     * Set when targetOS is set to windows
     */
    private boolean targetWindows = false;
    /**
     * Set if we're running on windows
     */
    private boolean onWindows = false;
    /**
     * Set if we should create a new property even if the result is empty
     */
    private boolean setonempty = true;
    /**
     * The property to receive the conversion
     */
    /**
     * Path prefix map
     */
    private Vector prefixMap = new Vector();
    /**
     * User override on path sep char
     */
    private String pathSep = null;
    /**
     * User override on directory sep char
     */
    private String dirSep = null;

    /**
     * constructor
     */
    public PathConvert() {
        onWindows = Os.isFamily("dos");
    }


    /**
     * Helper class, holds the nested &lt;map&gt; values. Elements will look like
     * this: &lt;map from=&quot;d:&quot; to=&quot;/foo&quot;/&gt;
     *
     * When running on windows, the prefix comparison will be case
     * insensitive.
     */
    public class MapEntry {

        /** Set the &quot;from&quot; attribute of the map entry  */
        /**
         * the prefix string to search for; required.
         * Note that this value is case-insensitive when the build is
         * running on a Windows platform and case-sensitive when running on
         * a Unix platform.
         * @param from
         */
        public void setFrom(String from) {
            this.from = from;
        }

        /**
         *  The replacement text to use when from is matched; required.
         * @param to new prefix
         */
        public void setTo(String to) {
            this.to = to;
        }


        /**
         * Apply this map entry to a given path element
         *
         * @param elem Path element to process
         * @return String Updated path element after mapping
         */
        public String apply(String elem) {
            if (from == null || to == null) {
                throw new BuildException("Both 'from' and 'to' must be set "
                     + "in a map entry");
            }

            String cmpElem = onWindows ? elem.toLowerCase() : elem;
            String cmpFrom = onWindows ? from.toLowerCase() : from;


            if (cmpElem.startsWith(cmpFrom)) {
                int len = from.length();

                if (len >= elem.length()) {
                    elem = to;
                } else {
                    elem = to + elem.substring(len);
                }
            }

            return elem;
        }

        private String from = null;
        private String to = null;
    }


    /**
     * an enumeration of supported targets:
     * windows", "unix", "netware", and "os/2".
     */
    public static class TargetOs extends EnumeratedAttribute {
        public String[] getValues() {
            return new String[]{"windows", "unix", "netware", "os/2"};
        }
    }


    /** Create a nested PATH element  */
    public Path createPath() {

        if (isReference()) {
            throw noChildrenAllowed();
        }

        if (path == null) {
            path = new Path(getProject());
        }
        return path.createPath();
    }


    /**
     * Create a nested MAP element
     * @return a Map to configure
     */
    public MapEntry createMap() {

        MapEntry entry = new MapEntry();

        prefixMap.addElement(entry);
        return entry;
    }


    /**
     * Set targetos to a platform to one of
     * "windows", "unix", "netware", or "os/2"; required unless
     * unless pathsep and/or dirsep are specified.
     *
     * @deprecated use the method taking a TargetOs argument instead
     * @see #setTargetos(TargetOs)
     */
    public void setTargetos(String target) {
        TargetOs to = new TargetOs();

        to.setValue(target);
        setTargetos(to);
    }


    /**
     * Set targetos to a platform to one of
     * "windows", "unix", "netware", or "os/2"; required unless
     * unless pathsep and/or dirsep are specified.
     *
     * @since Ant 1.5
     */
    public void setTargetos(TargetOs target) {

        targetOS = target.getValue();



        targetWindows = !targetOS.equals("unix");
    }

    /**
     * Set setonempty
     *
     * If false, don't set the new property if the result is the empty string.
     * @param setonempty true or false
     *
     * @since Ant 1.5
     */
     public void setSetonempty(boolean setonempty) {
         this.setonempty = setonempty;
     }

    /**
     * The property into which the converted path will be placed.
     */
    public void setProperty(String p) {
        property = p;
    }


    /**
     * Adds a reference to a Path, FileSet, DirSet, or FileList defined
     * elsewhere.
     */
    public void setRefid(Reference r) {
        if (path != null) {
            throw noChildrenAllowed();
        }

        refid = r;
    }


    /**
     * Set the default path separator string;
     * defaults to current JVM
     * {@link java.io.File#pathSeparator File.pathSeparator}
     * @param sep path separator string
     */
    public void setPathSep(String sep) {
        pathSep = sep;
    }


    /**
     * Set the default directory separator string;
     * defaults to current JVM {@link java.io.File#separator File.separator}
     * @param sep directory separator string
     */
    public void setDirSep(String sep) {
        dirSep = sep;
    }


    /**
     * Has the refid attribute of this element been set?
     * @return true if refid is valid
     */
    public boolean isReference() {
        return refid != null;
    }


    /** Do the execution.
     * @throws BuildException if something is invalid
     */
    public void execute() throws BuildException {
        Path savedPath = path;

        try {
            if (isReference()) {
                path = new Path(getProject()).createPath();

                Object obj = refid.getReferencedObject(getProject());

                if (obj instanceof Path) {
                    path.setRefid(refid);
                } else if (obj instanceof FileSet) {
                    FileSet fs = (FileSet) obj;

                    path.addFileset(fs);
                } else if (obj instanceof DirSet) {
                    DirSet ds = (DirSet) obj;

                    path.addDirset(ds);
                } else if (obj instanceof FileList) {
                    FileList fl = (FileList) obj;

                    path.addFilelist(fl);

                } else {
                    throw new BuildException("'refid' does not refer to a "
                         + "path, fileset, dirset, or "
                         + "filelist.");
                }
            }



            String fromDirSep = onWindows ? "\\" : "/";

            StringBuffer rslt = new StringBuffer(100);

            String[] elems = path.list();

            for (int i = 0; i < elems.length; i++) {
                String elem = elems[i];



                if (i != 0) {
                    rslt.append(pathSep);
                }

                StringTokenizer stDirectory =
                    new StringTokenizer(elem, fromDirSep, true);
                String token = null;

                while (stDirectory.hasMoreTokens()) {
                    token = stDirectory.nextToken();

                    if (fromDirSep.equals(token)) {
                        rslt.append(dirSep);
                    } else {
                        rslt.append(token);
                    }
                }
            }

            String value = rslt.toString();
            if(setonempty) {
                log("Set property " + property + " = " + value,
                    Project.MSG_VERBOSE);
                getProject().setNewProperty(property, value);
            } else {
                if(rslt.length() > 0) {
                    log("Set property " + property + " = " + value,
                        Project.MSG_VERBOSE);
                    getProject().setNewProperty(property, value);
                }
            }
        } finally {
            path = savedPath;
            dirSep = savedDirSep;
            pathSep = savedPathSep;
        }
    }


    /**
     * Apply the configured map to a path element. The map is used to convert
     * between Windows drive letters and Unix paths. If no map is configured,
     * then the input string is returned unchanged.
     *
     * @param elem The path element to apply the map to
     * @return String Updated element
     */
    private String mapElement(String elem) {

        int size = prefixMap.size();

        if (size != 0) {


            for (int i = 0; i < size; i++) {
                MapEntry entry = (MapEntry) prefixMap.elementAt(i);
                String newElem = entry.apply(elem);


                if (newElem != elem) {
                    elem = newElem;
                }
            }
        }

        return elem;
    }


    /**
     * Validate that all our parameters have been properly initialized.
     *
     * @throws BuildException if something is not setup properly
     */
    private void validateSetup() throws BuildException {

        if (path == null) {
            throw new BuildException("You must specify a path to convert");
        }

        if (property == null) {
            throw new BuildException("You must specify a property");
        }


        if (targetOS == null && pathSep == null && dirSep == null) {
            throw new BuildException("You must specify at least one of "
                 + "targetOS, dirSep, or pathSep");
        }

        String dsep = File.separator;
        String psep = File.pathSeparator;

        if (targetOS != null) {
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
     * Creates an exception that indicates that this XML element must not have
     * child elements if the refid attribute is set.
     */
    private BuildException noChildrenAllowed() {
        return new BuildException("You must not specify nested <path> "
             + "elements when using the refid attribute.");
    }

}

