package org.apache.tools.ant.taskdefs.optional.dotnet;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import java.io.File;

/**
 * Converts a WSDL file or URL resource into a .NET language.
 *
 * See "Creating an XML Web Service Proxy", "wsdl.exe" docs in
 * the framework SDK documentation
 * @author      Steve Loughran steve_l@iseran.com
 * @version     0.5
 * @ant.task    name="wsdltodotnet" category="dotnet"
 * @since       Ant 1.5
 */

public class WsdlToDotnet extends Task  { 
    
    /**
     * name of output file (required)
     */ 
    private File destFile = null;
    
    /**
     * url to retrieve
     */ 
    private String url = null;
    
    /**
     * name of source file
     */ 
    private File srcFile = null;
    
    /**
     * language; defaults to C#
     */ 
    private String language = "CS";
    
    /**
     * flag set to true to generate server side skeleton
     */ 
    private boolean server = false;
    
    /**
     * namespace
     */ 
    private String namespace = null;
    
    /**
     *  flag to control action on execution trouble
     */
    private boolean failOnError = true;

    /**
     *  any extra command options?
     */
    protected String extraOptions = null;
    
    /**
     * Name of the file to generate. Required
     * @param destFile filename
     */
    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }

    /**
     * Sets the URL to fetch. Fetching is by wsdl.exe; Ant proxy settings
     * are ignored; either url or srcFile is required.
     * @param url url to save
     */

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The local WSDL file to parse; either url or srcFile is required.
     * @param srcFile name of WSDL file
     */
    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    /**
     * set the language; one of "CS", "JS", or "VB"
     * optional, default is CS for C# source
     * @param language language to generate
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * flag to enable server side code generation;
     * optional, default=false
     * @param server server-side flag
     */

    public void setServer(boolean server) {
        this.server = server;
    }

    /**
     * namespace to place  the source in.
     * optional; default ""
     * @param namespace new namespace
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Should failure halt the build? optional, default=true
     * @param failOnError new failure option
     */
    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    /**
     *  Any extra WSDL.EXE options which aren't explicitly
     *  supported by the ant wrapper task; optional
     *
     *@param  extraOptions  The new ExtraOptions value
     */
    public void setExtraOptions(String extraOptions) {
        this.extraOptions = extraOptions;
    }
    
    /**
     * validation code
     * @throws  BuildException  if validation failed
     */ 
    protected void validate() 
            throws BuildException {
        if (destFile == null) {
            throw new BuildException("destination file must be specified");
        }
        if (destFile.isDirectory()) {
            throw new BuildException(
                "destination file is a directory");
        }        
        if (url != null && srcFile != null) {
            throw new BuildException(
                    "you can not specify both a source file and a URL");
        }
        if (url == null && srcFile == null) {
            throw new BuildException(
                    "you must specify either a source file or a URL");
        }
        if (srcFile != null) {
            if (!srcFile.exists()) {
                throw new BuildException(
                    "source file does not exist");
            }
            if (srcFile.isDirectory()) {
                throw new BuildException(
                    "source file is a directory");
            }
        }

    }

    /**
     *  do the work by building the command line and then calling it
     *
     *@throws  BuildException  if validation or execution failed
     */
    public void execute()
             throws BuildException {
        validate();
        NetCommand command = new NetCommand(this, "WSDL", "wsdl");
        command.setFailOnError(failOnError);
        command.setTraceCommandLine(true);
        command.addArgument("/nologo");
        command.addArgument("/out:" + destFile);
        command.addArgument("/language:", language);
        if (server) {
            command.addArgument("/server");
        }
        command.addArgument("/namespace:", namespace);
        command.addArgument(extraOptions);

        boolean rebuild = true;
        if(srcFile!=null) {
            command.addArgument(srcFile.toString());
            if (srcFile.exists() && destFile.exists() &&
                srcFile.lastModified() <= destFile.lastModified()) {
                rebuild = false;
            }
        } else {
            rebuild=true;
            command.addArgument(url);
        }
        if (rebuild) {
            command.runCommand();
        }
    }
}

