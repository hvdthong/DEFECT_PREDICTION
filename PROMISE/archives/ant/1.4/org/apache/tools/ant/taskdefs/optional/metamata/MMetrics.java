package org.apache.tools.ant.taskdefs.optional.metamata;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;



import java.io.*;
import java.util.*;

/**
 * Calculates global complexity and quality metrics on Java source code.
 *
 * You will not be able to use this task with the evaluation version since
 * as of Metamata 2.0, Metrics does not support command line :-(
 *
 * For more information, visit the website at
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class MMetrics extends AbstractMetamataTask {
/*
    The command line options as of Metamata 2.0 are as follows:

Usage
    mmetrics <option>... <path>...

Parameters
    path              File or directory to measure.

Options
    -arguments   -A   <file>      Includes command line arguments from file.
    -classpath   -cp  <path>      Sets class path (also source path unless one
                                  explicitly set). Overrides METAPATH/CLASSPATH.
    -compilation-units            Measure compilation units.
    -files                        Measure compilation units.
    -format      -f   <format>    Sets output format, default output file type.
    -help        -h               Prints help and exits.
    -indent      -i   <string>    Sets string used to indent labels one level.
    -methods                      Measure methods, types, and compilation units.
    -output      -o   <file>      Sets output file name.
    -quiet       -q               Suppresses copyright message.
    -sourcepath       <path>      Sets source path. Overrides SOURCEPATH.
    -types                        Measure types and compilation units.
    -verbose     -v               Prints all messages.
    -version     -V               Prints version and exits.

Format Options
    comma csv                     Format output as comma-separated text.
    html htm                      Format output as an HTML table.
    tab tab-separated tsv         Format output as tab-separated text.
    text txt                      Format output as space-aligned text.
*/

    /** the granularity mode. Should be one of 'files', 'methods' and 'types'. */
    protected String granularity = null;

    /** the XML output file */
    protected File outFile = null;
    
    /** the location of the temporary txt report */
    protected File tmpFile = createTmpFile();

    protected Path path = null;


    /** default constructor */
    public MMetrics() {
        super("com.metamata.sc.MMetrics");
    }

    /**
     * set the granularity of the audit. Should be one of 'files', 'methods'
     * or 'types'.
     * @param granularity   the audit reporting mode.
     */
    public void setGranularity(String granularity){
        this.granularity = granularity;
    }

    /**
     * Set the output XML file
     * @param file the xml file to write the XML report to.
     */
    public void setTofile(File file){
        this.outFile = file;
    }

    /**
     * Set a new path (directory) to measure metrics from.
     * @return the path instance to use.
     */
    public Path createPath(){
        if (path == null) {
            path = new Path(project);
        }
        return path;

    }



    protected void checkOptions() throws BuildException {
        super.checkOptions();

        if ( !"files".equals(granularity) && !"methods".equals(granularity)
           && !"types".equals(granularity) ){
            throw new BuildException("Metrics reporting granularity is invalid. Must be one of 'files', 'methods', 'types'");
        }
        if (outFile == null){
            throw new BuildException("Output XML file must be set via 'tofile' attribute.");
        }
        if (path == null && fileSets.size() == 0){
            throw new BuildException("Must set either paths (path element) or files (fileset element)");
        }
        if (path != null && fileSets.size() > 0){
            throw new BuildException("Cannot set paths (path element) and files (fileset element) at the same time");
        }
    }

    protected void execute0(ExecuteStreamHandler handler) throws BuildException {
        super.execute0(handler);
        transformFile();
    }
    
    /**
     * transform the generated file via the handler
     * This function can either be called if the result is written to the output
     * file via -output or we could use the handler directly on stdout if not.
     * @see #createStreamHandler()
     */
    protected void transformFile() throws BuildException {
        FileInputStream tmpStream = null;
        try {
            tmpStream = new FileInputStream( tmpFile );
        } catch (IOException e){
            throw new BuildException("Error reading temporary file: " + tmpFile, e);
        }
        FileOutputStream xmlStream = null;
        try {
            xmlStream = new FileOutputStream(outFile);
            ExecuteStreamHandler xmlHandler = new MMetricsStreamHandler(this, xmlStream);
            xmlHandler.setProcessOutputStream(tmpStream);
            xmlHandler.start();
            xmlHandler.stop();
        } catch (IOException e){
            throw new BuildException("Error creating output file: " + outFile, e);
        } finally {
            if (xmlStream != null){
                try {
                    xmlStream.close();
                } catch (IOException ignored){}
            }
            if (tmpStream != null){
                try {
                    tmpStream.close();
                } catch (IOException ignored){}
            }
        }
    }
    

    /** cleanup the temporary txt report */
    protected void cleanUp() throws BuildException {
        try {
            super.cleanUp();
        } finally {
            if (tmpFile != null){
                tmpFile.delete();
                tmpFile = null;
            }
        }
    }

    /**
     * if the report is transform via a temporary txt file we should use a
     * a normal logger here, otherwise we could use the metrics handler
     * directly to capture and transform the output on stdout to XML.
     */
    protected ExecuteStreamHandler createStreamHandler(){
        return new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_INFO);
    }


    protected Vector getOptions(){
        Vector options = new Vector(512);
        if (sourcePath != null){
            classPath = sourcePath;
        }
                
        if (classPath != null){
            options.addElement("-classpath");
            options.addElement(classPath);
        }
        options.addElement( "-output" );
        options.addElement( tmpFile.toString() );
        
        options.addElement( "-" + granularity);
        
        options.addElement( "-format");
        
        options.addElement( "tab");
        
        options.addElement( "-i");
        options.addElement( "/");
        
        String[] dirs = path.list();
        for (int i = 0; i < dirs.length; i++){
            options.addElement( dirs[i] );
        }
        addAllVector(options, includedFiles.keys());
        return options;
    }

}
