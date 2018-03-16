The reference CD to listen to while editing this file is
Underworld Everything, Everything
variable naming policy from Fowler's refactoring book.
 */


package org.apache.tools.ant.taskdefs.optional.dotnet;


import java.io.*;
import java.text.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;


/**
This is a helper class to spawn net commands out. 
In its initial form it contains no .net specifics, just contains
all the command line/exe construction stuff. However, it may be handy in future
to have a means of setting the path to point to the dotnet bin directory; in which
case the shared code should go in here.
@author Steve Loughran steve_l@iseran.com
@created 2000-11-01
@version 0.3
 */

public class NetCommand {
    
    /** constructor
    @param owning task
    @param title (for logging/errors)
    @param executable. Leave off the '.exe. for future portability
    */
    
    public NetCommand(Task owner, String title, String program) {
        _owner=owner;
        _title=title;
        _program=program;
        _commandLine=new Commandline();
        _commandLine.setExecutable(_program);
        prepareExecutor();
        }
    
    /** owner project
     */
    protected Task _owner;
    
    /** executabe
     */
    protected Execute _exe;
    
    /** what is the command line
     */
    protected Commandline _commandLine;
    
    /** title of the command
     */
    protected String _title;
    
    /** actual program to invoke
     */
    protected String _program;
    
    /** trace flag
     */
    protected boolean _traceCommandLine=false;
    
    /**
     * turn tracing on or off
     * @param b trace flag
     */
    public void setTraceCommandLine(boolean b){
        _traceCommandLine=b;
    }
    
    /** flag to control action on execution trouble
    */
    protected boolean _failOnError;
    
    /**
     * set fail on error flag
     * @param b fail flag -set to true to cause an exception to be raised if
     * the return value != 0
     */
    public void setFailOnError(boolean b){
        _failOnError=b;
    }
    
    /** query fail on error flag
    */
    public boolean getFailFailOnError() {
        return _failOnError;
    }
    
    /** 
     * verbose text log
     * @param msg string to add to log iff verbose is defined for the build
     */
    protected void logVerbose(String msg){
        _owner.getProject().log(msg,Project.MSG_VERBOSE);
        }
    
    
    /**
     * error text log
     * @param  msg  message to display as an error
     */
    protected void logError(String msg) {
        _owner.getProject().log(msg,Project.MSG_ERR);
    }
    
    /**
     * add an argument to a command line; do nothing if the arg is null or empty string
     * @param  argument  The feature to be added to the Argument attribute 
     */
    public void addArgument(String argument){
        if(argument!=null && argument.length()!=0) {
            _commandLine.createArgument().setValue(argument);
        }
    }
    
    /**
     * set up the command sequence..
     */
    protected void prepareExecutor() {
        File dir = _owner.getProject().getBaseDir();
        ExecuteStreamHandler handler=new LogStreamHandler(_owner,
                   Project.MSG_INFO, Project.MSG_WARN);
        _exe = new Execute(handler, null);
        _exe.setAntRun(_owner.getProject());
        _exe.setWorkingDirectory(dir);
    }
    
    /**
     * Run the command using the given Execute instance.
     * @throws an exception of something goes wrong and the failOnError flag is true
     */
    public void runCommand()
        throws BuildException {
        try {
            if(_traceCommandLine) {
                _owner.log(_commandLine.toString());
            }
            else {
                logVerbose(_commandLine.toString());
            }
            _exe.setCommandline(_commandLine.getCommandline());
            err = _exe.execute();
            if (err != 0) {
                if (_failOnError) {
                    throw new BuildException(_title+" returned: "+err, _owner.getLocation());
                } else {
                    _owner.log(_title+"  Result: " + err, Project.MSG_ERR);
                }
            }
        } catch (IOException e) {
            throw new BuildException(_title+" failed: " + e, e, _owner.getLocation());
        }
    }
