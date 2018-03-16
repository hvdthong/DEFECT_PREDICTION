package org.apache.tools.ant.taskdefs;


import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.*;


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
 * @version $Revision: 268430 $ $Date: 2001-01-11 17:12:44 +0800 (周四, 2001-01-11) $
 */
public class XSLTProcess extends MatchingTask {

    private File destDir = null;

    private File baseDir = null;

    private File xslFile = null;

    private String targetExtension = ".html";
    private Vector params = new Vector();
 
    private File inFile = null;
 
    private File outFile = null;

    private XSLTLiaison liaison;

    /**
     * Creates a new XSLTProcess Task.
     **/
    public XSLTProcess() {

    /**
     * Executes the task.
     */

    public void execute() throws BuildException {
        DirectoryScanner scanner;
        String[]         list;
        String[]         dirs;

        if (baseDir == null) {
            baseDir = project.resolveFile(".");
        }
        
        if (destDir == null ) {
            String msg = "destdir attributes must be set!";
            throw new BuildException(msg);
        }
        scanner = getDirectoryScanner(baseDir);
        log("Transforming into "+destDir, Project.MSG_INFO);

        if (liaison == null) {
            try {
                setProcessor("trax");
            } catch (Throwable e1) {
                try {
                    setProcessor("xslp");
                } catch (Throwable e2) {
                    try {
                        setProcessor("xalan");
                    } catch (Throwable e3) {
                        throw new BuildException(e1);
                    }
                }
            }
        }

        log("Using "+liaison.getClass().toString(), Project.MSG_VERBOSE);

        long styleSheetLastModified = 0;
        if (xslFile != null) {
            try {
                File file = new File( baseDir, xslFile.toString() );
                styleSheetLastModified = file.lastModified();
                log( "Loading stylesheet " + file, Project.MSG_INFO);
                liaison.setStylesheet( file.toString() );
                for(Enumeration e = params.elements();e.hasMoreElements();) {
                    Param p = (Param)e.nextElement();
                    liaison.addParam( p.getName(), p.getExpression() );
                }
            } catch (Exception ex) {
                log("Failed to read stylesheet " + xslFile, Project.MSG_INFO);
                throw new BuildException(ex);
            }
        }

        if (inFile != null && outFile != null) {
            process(inFile, outFile, styleSheetLastModified);
            return;
        }

        list = scanner.getIncludedFiles();
        for (int i = 0;i < list.length; ++i) {
            process( baseDir, list[i], destDir, styleSheetLastModified );
        }
        
        dirs = scanner.getIncludedDirectories();
        for (int j = 0;j < dirs.length;++j){
            list=new File(baseDir,dirs[j]).list();
            for (int i = 0;i < list.length;++i)
                process( baseDir, list[i], destDir, styleSheetLastModified );
        }

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
     * Sets the file to use for styling relative to the base directory.
     */
    public void setStyle(String xslFile) {
        this.xslFile = new File(xslFile);
    }

    /**
     * Sets the file to use for styling relative to the base directory.
     */
    public void setProcessor(String processor) throws Exception {

        if (processor.equals("trax")) {
            final Class clazz = 
                Class.forName("org.apache.tools.ant.taskdefs.optional.TraXLiaison");
            liaison = (XSLTLiaison)clazz.newInstance();
        } else if (processor.equals("xslp")) {
            final Class clazz = 
                Class.forName("org.apache.tools.ant.taskdefs.optional.XslpLiaison");
            liaison = (XSLTLiaison) clazz.newInstance();
        } else if (processor.equals("xalan")) {
            final Class clazz = 
                Class.forName("org.apache.tools.ant.taskdefs.optional.XalanLiaison");
            liaison = (XSLTLiaison)clazz.newInstance();
        } else {
            liaison = (XSLTLiaison) Class.forName(processor).newInstance();
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
                         long styleSheetLastModified)
        throws BuildException {

        String fileExt=targetExtension;
        File   outFile=null;
        File   inFile=null;

        try {
            inFile = new File(baseDir,xmlFile);
            int dotPos = xmlFile.lastIndexOf('.');
            if(dotPos>0){
                outFile = new File(destDir,xmlFile.substring(0,xmlFile.lastIndexOf('.'))+fileExt);
            }else{
                outFile = new File(destDir,xmlFile+fileExt);
            }
            if (inFile.lastModified() > outFile.lastModified() ||
                styleSheetLastModified > outFile.lastModified()) {
                ensureDirectoryFor( outFile );
                log("Transforming into "+destDir);

                liaison.transform(inFile.toString(), outFile.toString());
            }
        }
        catch (Exception ex) {
            log("Failed to process " + inFile, Project.MSG_INFO);
            if (outFile != null) {
                outFile.delete();
            }
        
            throw new BuildException(ex);
        }


    private void process(File inFile, File outFile, long styleSheetLastModified) throws BuildException {
        try{
            log("In file "+inFile+" time: " + inFile.lastModified() , Project.MSG_DEBUG);
            log("Out file "+outFile+" time: " + outFile.lastModified() , Project.MSG_DEBUG);
            log("Style file "+xslFile+" time: " + styleSheetLastModified , Project.MSG_DEBUG);
            if (inFile.lastModified() > outFile.lastModified() ||
                styleSheetLastModified > outFile.lastModified()) {
                ensureDirectoryFor( outFile );
                log("Processing " + inFile + " to " + outFile, Project.MSG_INFO);
                liaison.transform(inFile.toString(), outFile.toString());
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
