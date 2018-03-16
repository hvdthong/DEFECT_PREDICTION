package org.apache.tools.ant.taskdefs.optional.dotnet;


import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;

/**
 *  This is a helper class to spawn net commands out. In its initial form it
 *  contains no .net specifics, just contains all the command line/exe
 *  construction stuff. However, it may be handy in future to have a means of
 *  setting the path to point to the dotnet bin directory; in which case the
 *  shared code should go in here.
 *
 *@version    0.5
 */

public class NetCommand {

    /**
     *  owner project
     */
    protected Task owner;

    /**
     *  executable
     */
    protected Execute executable;

    /**
     *  what is the command line
     */
    protected Commandline commandLine;

    /**
     *  title of the command
     */
    protected String title;

    /**
     *  actual program to invoke
     */
    protected String program;

    /**
     *  trace flag
     */
    protected boolean traceCommandLine = false;

    /**
     *  flag to control action on execution trouble
     */
    protected boolean failOnError;

    /**
     * the directory to execute the command in. When null, the current
     * directory is used.
     */
    private File directory;

    /**
     * flag to set to to use @file based command cache
     */
    private boolean useResponseFile=false;

    /**
     * name of a temp file; may be null
     */
    private File temporaryCommandFile;

    /**
     * internal threshold for auto-switch
     */
    private int automaticResponseFileThreshold = 64;

    /**
     *  constructor
     *
     *@param  title        (for logging/errors)
     *@param  owner        owner task
     *@param  program      app we are to run
     */

    public NetCommand(Task owner, String title, String program) {
        this.owner = owner;
        this.title = title;
        this.program = program;
        commandLine = new Commandline();
        commandLine.setExecutable(program);
        prepareExecutor();
    }


    /**
     *  turn tracing on or off
     *
     *@param  b  trace flag
     */
    public void setTraceCommandLine(boolean b) {
        traceCommandLine = b;
    }


    /**
     *  set fail on error flag
     *
     *@param  b  fail flag -set to true to cause an exception to be raised if
     *      the return value != 0
     */
    public void setFailOnError(boolean b) {
        failOnError = b;
    }


    /**
     *  query fail on error flag
     *
     *@return    The failFailOnError value
     */
    public boolean getFailFailOnError() {
        return failOnError;
    }


    /**
     * set the directory to run from, if the default is inadequate
     * @param directory
     */
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     *  verbose text log
     *
     *@param  msg  string to add to log if verbose is defined for the build
     */
    protected void logVerbose(String msg) {
        owner.getProject().log(msg, Project.MSG_VERBOSE);
    }


    /**
     *  error text log
     *
     *@param  msg  message to display as an error
     */
    protected void logError(String msg) {
        owner.getProject().log(msg, Project.MSG_ERR);
    }


    /**
     *  add an argument to a command line; do nothing if the arg is null or
     *  empty string
     *
     *@param  argument  The feature to be added to the Argument attribute
     */
    public void addArgument(String argument) {
        if (argument != null && argument.length() != 0) {
            commandLine.createArgument().setValue(argument);
        }
    }

    /**
     *  add an argument to a command line; do nothing if the arg is null or
     *  empty string
     *
     *@param  argument  The feature to be added to the Argument attribute
     */
    public void addArguments(String[] arguments) {
        if (arguments != null && arguments.length != 0) {
            for (int i = 0; i < arguments.length; i++) {
                addArgument(arguments[i]);
            }
        }
    }

    /**
     *  concatenate two strings together and add them as a single argument,
     *  but only if argument2 is non-null and non-zero length
     *
     *@param  argument1  The first argument
     *@param  argument2  The second argument
     */   public void addArgument(String argument1, String argument2) {
        if (argument2 != null && argument2.length() != 0) {
            commandLine.createArgument().setValue(argument1 + argument2);
        }
    }

    /**
     * getter
     * @return response file state
     */
    public boolean isUseResponseFile() {
        return useResponseFile;
    }

    /**
     * set this to true to always use the response file
     * @param useResponseFile
     */
    public void setUseResponseFile(boolean useResponseFile) {
        this.useResponseFile = useResponseFile;
    }

    /**
     * getter for threshold
     * @return 0 for disabled, or a threshold for enabling response files
     */
    public int getAutomaticResponseFileThreshold() {
        return automaticResponseFileThreshold;
    }

    /**
     * set threshold for automatically using response files -use 0 for off
     * @param automaticResponseFileThreshold
     */
    public void setAutomaticResponseFileThreshold(int automaticResponseFileThreshold) {
        this.automaticResponseFileThreshold = automaticResponseFileThreshold;
    }

    /**
     *  set up the command sequence..
     */
    protected void prepareExecutor() {
        if (owner == null) {
            throw new RuntimeException("no owner");
        }
        if (owner.getProject() == null) {
            throw new RuntimeException("Owner has no project");
        }
        File dir = owner.getProject().getBaseDir();
        if (directory != null) {
            dir = directory;
        }

        ExecuteStreamHandler handler = new LogStreamHandler(owner,
                Project.MSG_INFO, Project.MSG_WARN);
        executable = new Execute(handler, null);
        executable.setAntRun(owner.getProject());
        executable.setWorkingDirectory(dir);
    }


    /**
     *  Run the command using the given Execute instance.
     *
     *@exception  BuildException  if something goes wrong and the
     *      failOnError flag is true
     */
    public void runCommand()
             throws BuildException {
        int err = -1;
        try {
            if (traceCommandLine) {
                owner.log(commandLine.describeCommand());
            } else {
                logVerbose(commandLine.describeCommand());
            }
            setExecutableCommandLine();
            err = executable.execute();
            if (Execute.isFailure(err)) {
                if (failOnError) {
                    throw new BuildException(title + " returned: " + err, owner.getLocation());
                } else {
                    owner.log(title + "  Result: " + err, Project.MSG_ERR);
                }
            }
        } catch (IOException e) {
            throw new BuildException(title + " failed: " + e, e, owner.getLocation());
        } finally {
            if (temporaryCommandFile != null) {
                temporaryCommandFile.delete();
            }
        }
    }

    /**
     * set the executable command line
     */
    private void setExecutableCommandLine() {

        String[] commands = commandLine.getCommandline();
        if (automaticResponseFileThreshold>0 &&
                commands.length > automaticResponseFileThreshold) {
            useResponseFile = true;
        }
        if (!useResponseFile || commands.length <= 1) {
            executable.setCommandline(commands);
        } else {
            FileOutputStream fos = null;
            FileUtils fileUtils = FileUtils.newFileUtils();

            temporaryCommandFile = fileUtils.createTempFile("cmd", ".txt", null);
            owner.log("Using response file"+temporaryCommandFile,Project.MSG_VERBOSE);

            try {
                fos = new FileOutputStream(temporaryCommandFile);
                PrintWriter out = new PrintWriter(new BufferedOutputStream(fos));
                for (int i = 1; i < commands.length; ++i) {
                    out.println(commands[i]);
                }
                out.flush();
                out.close();
            } catch (IOException ex) {
                throw new BuildException("saving command stream to " + temporaryCommandFile, ex);
            }

            String newCommandLine[] = new String[2];
            newCommandLine[0] = commands[0];
            newCommandLine[1] = "@" + temporaryCommandFile.getAbsolutePath();
            executable.setCommandline(newCommandLine);
        }
    }


    /**
     * scan through one fileset for files to include
     * @param scanner
     * @param filesToBuild
     * @param outputTimestamp timestamp to compare against
     * @return #of files out of date
     * @todo: should FAT granularity be included here?
     */
    public int scanOneFileset(DirectoryScanner scanner, Hashtable filesToBuild,
                                        long outputTimestamp) {
        int filesOutOfDate = 0;
        String[] dependencies = scanner.getIncludedFiles();
        File base = scanner.getBasedir();
        for (int i = 0; i < dependencies.length; i++) {
            File targetFile = new File(base, dependencies[i]);
            if (filesToBuild.get(targetFile) == null) {
                filesToBuild.put(targetFile, targetFile);
                if (targetFile.lastModified() > outputTimestamp) {
                    filesOutOfDate++;
                    owner.log(targetFile.toString() + " is out of date",
                            Project.MSG_VERBOSE);
                } else {
                    owner.log(targetFile.toString(),
                            Project.MSG_VERBOSE);
                }
            }
        }
        return filesOutOfDate;
    }
}

