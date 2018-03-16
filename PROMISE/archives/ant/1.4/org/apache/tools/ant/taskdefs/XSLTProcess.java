package org.apache.tools.ant.taskdefs;


import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;


/**
 * A Task to process via XSLT a set of XML documents. This is
 * useful for building views of XML based documentation.
 * arguments:
 * <ul>
 * <li>basedir
 * <li>destdir
 * <li>style
 * <li>includes
 * <li>excludes
 * </ul>
 * Of these arguments, the <b>sourcedir</b> and <b>destdir</b> are required.
 * <p>
 * This task will recursively scan the sourcedir and destdir
 * looking for XML documents to process via XSLT. Any other files,
 * such as images, or html files in the source directory will be
 * copied into the destination directory.
 *
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @author <a href="mailto:russgold@acm.org">Russell Gold</a>
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 269609 $ $Date: 2001-08-22 04:25:22 +0800 (周三, 2001-08-22) $
 */
public class XSLTProcess extends MatchingTask {

    private File destDir = null;

    private File baseDir = null;

    private String xslFile = null;

    private String targetExtension = ".html";
    private Vector params = new Vector();

    private File inFile = null;

    private File outFile = null;

    private String processor;
    private Path classpath = null;
    private XSLTLiaison liaison;
    private boolean stylesheetLoaded = false;

    private boolean force = false;

    private FileUtils fileUtils;

    /**
     * Creates a new XSLTProcess Task.
     **/
    public XSLTProcess() {
        fileUtils = FileUtils.newFileUtils();

    /**
     * Executes the task.
     */

    public void execute() throws BuildException {
        DirectoryScanner scanner;
        String[]         list;
        String[]         dirs;

        if (xslFile == null) {
            throw new BuildException("no stylesheet specified", location);
        }

        if (baseDir == null) {
            baseDir = project.resolveFile(".");
        }

        liaison = getLiaison();
        log("Using "+liaison.getClass().toString(), Project.MSG_VERBOSE);

        File stylesheet = project.resolveFile(xslFile);
        if (!stylesheet.exists()) {
            stylesheet = fileUtils.resolveFile(baseDir, xslFile);
            /*
             * shouldn't throw out deprecation warnings before we know,
             * the wrong version has been used.
             */
            if (stylesheet.exists()) {
                log("DEPRECATED - the style attribute should be relative to the project\'s");
                log("             basedir, not the tasks\'s basedir.");
            }
        }

        if (inFile != null && outFile != null) {
            process(inFile, outFile, stylesheet);
            return;
        }

        /*
         * if we get here, in and out have not been specified, we are
         * in batch processing mode.
         */

        if (destDir == null ) {
            String msg = "destdir attributes must be set!";
            throw new BuildException(msg);
        }
        scanner = getDirectoryScanner(baseDir);
        log("Transforming into "+destDir, Project.MSG_INFO);

        list = scanner.getIncludedFiles();
        for (int i = 0;i < list.length; ++i) {
            process( baseDir, list[i], destDir, stylesheet );
        }

        dirs = scanner.getIncludedDirectories();
        for (int j = 0;j < dirs.length;++j){
            list=new File(baseDir,dirs[j]).list();
            for (int i = 0;i < list.length;++i)
                process( baseDir, list[i], destDir, stylesheet );
        }

    /**
     * Set whether to check dependencies, or always generate.
     **/
    public void setForce(boolean force) {
        this.force = force;

    /**
     * Set the base directory.
     **/
    public void setBasedir(File dir) {
        baseDir = dir;

    /**
     * Set the destination directory into which the XSL result
     * files should be copied to
     * @param dirName the name of the destination directory
     **/
    public void setDestdir(File dir) {
        destDir = dir;

    /**
     * Set the desired file extension to be used for the target
     * @param name the extension to use
     **/
    public void setExtension(String name) {
        targetExtension = name;

    /**
     * Sets the file to use for styling relative to the base directory
     * of this task.
     */
    public void setStyle(String xslFile) {
        this.xslFile = xslFile;
    }

    /**
     * Set the classpath to load the Processor through (attribute).
     */
    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
    }

    /**
     * Set the classpath to load the Processor through (nested element).
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(project);
        }
        return classpath.createPath();
    }

    /**
     * Set the classpath to load the Processor through via reference
     * (attribute).
     */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }


    public void setProcessor(String processor) {
        this.processor = processor;
    }

    /**
     * Load processor here instead of in setProcessor - this will be
     * called from within execute, so we have access to the latest
     * classpath.
     */
    private void resolveProcessor(String proc) throws Exception {
        if (proc.equals("trax")) {
            final Class clazz =
                loadClass("org.apache.tools.ant.taskdefs.optional.TraXLiaison");
            liaison = (XSLTLiaison)clazz.newInstance();
        } else if (proc.equals("xslp")) {
            log("DEPRECATED - xslp processor is deprecated. Use trax or xalan instead.");
            final Class clazz =
                loadClass("org.apache.tools.ant.taskdefs.optional.XslpLiaison");
            liaison = (XSLTLiaison) clazz.newInstance();
        } else if (proc.equals("xalan")) {
            final Class clazz =
                loadClass("org.apache.tools.ant.taskdefs.optional.XalanLiaison");
            liaison = (XSLTLiaison)clazz.newInstance();
        } else {
            liaison = (XSLTLiaison) loadClass(proc).newInstance();
        }
    }

    /**
     * Load named class either via the system classloader or a given
     * custom classloader.
     */
    private Class loadClass(String classname) throws Exception {
        if (classpath == null) {
            return Class.forName(classname);
        } else {
            AntClassLoader al = new AntClassLoader(project, classpath);
            Class c = al.loadClass(classname);
            AntClassLoader.initializeClass(c);
            return c;
        }
    }

    /**
     * Sets an out file
     */
    public void setOut(File outFile){
        this.outFile = outFile;
    }

    /**
     * Sets an input xml file to be styled
     */
    public void setIn(File inFile){
        this.inFile = inFile;
    }

    /**
     * Processes the given input XML file and stores the result
     * in the given resultFile.
     **/
    private void process(File baseDir, String xmlFile, File destDir,
                         File stylesheet)
        throws BuildException {

        String fileExt=targetExtension;
        File   outFile=null;
        File   inFile=null;

        try {
            long styleSheetLastModified = stylesheet.lastModified();
            inFile = new File(baseDir,xmlFile);
            int dotPos = xmlFile.lastIndexOf('.');
            if(dotPos>0){
                outFile = new File(destDir,xmlFile.substring(0,xmlFile.lastIndexOf('.'))+fileExt);
            }else{
                outFile = new File(destDir,xmlFile+fileExt);
            }
            if (force ||
                inFile.lastModified() > outFile.lastModified() ||
                styleSheetLastModified > outFile.lastModified()) {
                ensureDirectoryFor( outFile );
                log("Transforming into "+destDir);

                configureLiaison(stylesheet);
                liaison.transform(inFile, outFile);
            }
        }
        catch (Exception ex) {
            log("Failed to process " + inFile, Project.MSG_INFO);
            if (outFile != null) {
                outFile.delete();
            }

            throw new BuildException(ex);
        }


    private void process(File inFile, File outFile, File stylesheet) throws BuildException {
        try{
            long styleSheetLastModified = stylesheet.lastModified();
            log("In file "+inFile+" time: " + inFile.lastModified() , Project.MSG_DEBUG);
            log("Out file "+outFile+" time: " + outFile.lastModified() , Project.MSG_DEBUG);
            log("Style file "+xslFile+" time: " + styleSheetLastModified , Project.MSG_DEBUG);
            if (force ||
                inFile.lastModified() > outFile.lastModified() ||
                styleSheetLastModified > outFile.lastModified()) {
                ensureDirectoryFor( outFile );
                log("Processing " + inFile + " to " + outFile, Project.MSG_INFO);
                configureLiaison(stylesheet);
                liaison.transform(inFile, outFile);
            }
        }catch (Exception ex) {
            log("Failed to process " + inFile, Project.MSG_INFO);
            if(outFile!=null)outFile.delete();
            throw new BuildException(ex);
        }
    }

    private void ensureDirectoryFor( File targetFile ) throws BuildException {
        File directory = new File( targetFile.getParent() );
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new BuildException("Unable to create directory: "
                                         + directory.getAbsolutePath() );
            }
        }
    }

    protected XSLTLiaison getLiaison() {
        if (liaison == null) {
            if (processor != null) {
                try {
                    resolveProcessor(processor);
                } catch (Exception e) {
                    throw new BuildException(e);
                }
            } else {
                try {
                    resolveProcessor("trax");
                } catch (Throwable e1) {
                    try {
                        resolveProcessor("xslp");
                    } catch (Throwable e2) {
                        try {
                            resolveProcessor("xalan");
                        } catch (Throwable e3) {
                            e3.printStackTrace();
                            e2.printStackTrace();
                            throw new BuildException(e1);
                        }
                    }
                }
            }
        }
        return liaison;
    }

    public Param createParam() {
        Param p = new Param();
        params.addElement(p);
        return p;
    }

    public class Param {
        private String name=null;
        private String expression=null;

        public void setName(String name){
            this.name = name;
        }

        public void setExpression(String expression){
            this.expression = expression;
        }

        public String getName() throws BuildException{
            if(name==null)throw new BuildException("Name attribute is missing.");
            return name;
        }

        public String getExpression() throws BuildException{
            if(expression==null)throw new BuildException("Expression attribute is missing.");
            return expression;
        }
    }

    /**
     * Loads the stylesheet and set xsl:param parameters.
     */
    protected void configureLiaison(File stylesheet) throws BuildException {
        if (stylesheetLoaded) {
            return;
        }
        stylesheetLoaded = true;

        try {
            log( "Loading stylesheet " + stylesheet, Project.MSG_INFO);
            liaison.setStylesheet( stylesheet );
            for(Enumeration e = params.elements();e.hasMoreElements();) {
                Param p = (Param)e.nextElement();
                liaison.addParam( p.getName(), p.getExpression() );
            }
        } catch (Exception ex) {
            log("Failed to read stylesheet " + stylesheet, Project.MSG_INFO);
            throw new BuildException(ex);
        }
    }

