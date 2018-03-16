package org.apache.tools.ant.taskdefs.optional;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.Mapper;

/**
 *
 * @author dIon Gillard <a href="mailto:dion@multitask.com.au">dion@multitask.com.au</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version 1.2
 */
public class RenameExtensions extends MatchingTask {

    private String fromExtension = "";
    private String toExtension = "";
    private boolean replace = false;
    private File srcDir;

    private Mapper.MapperType globType;


    /** Creates new RenameExtensions */
    public RenameExtensions() {
        super();
        globType = new Mapper.MapperType();
        globType.setValue("glob");
    }

    /** store fromExtension **/
    public void setFromExtension(String from) {
        fromExtension = from;
    }

    /** store toExtension **/
    public void setToExtension(String to) {
        toExtension = to;
    }

    /**
     * store replace attribute - this determines whether the target file
     * should be overwritten if present
     */
    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    /**
     * Set the source dir to find the files to be renamed.
     */
    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    /**
     * Executes the task, i.e. does the actual compiler call
     */
    public void execute() throws BuildException {

        if (fromExtension == null || toExtension == null || srcDir == null) {
            throw new BuildException( "srcDir, fromExtension and toExtension " +
                                      "attributes must be set!" );
        }

        log("DEPRECATED - The renameext task is deprecated.  Use move instead.",
            Project.MSG_WARN);
        log("Replace this with:", Project.MSG_INFO);
        log("<move todir=\""+srcDir+"\" overwrite=\""+replace+"\">", 
            Project.MSG_INFO);
        log("  <fileset dir=\""+srcDir+"\" />", Project.MSG_INFO);
        log("  <mapper type=\"glob\"", Project.MSG_INFO);
        log("          from=\"*"+fromExtension+"\"", Project.MSG_INFO);
        log("          to=\"*"+toExtension+"\" />", Project.MSG_INFO);
        log("</move>", Project.MSG_INFO);
        log("using the same patterns on <fileset> as you\'ve used here", 
            Project.MSG_INFO);

        Move move = (Move)project.createTask("move");
        move.setOwningTarget(target);
        move.setTaskName(getTaskName());
        move.setLocation(getLocation());
        move.setTodir(srcDir);
        move.setOverwrite(replace);

        fileset.setDir(srcDir);
        move.addFileset(fileset);

        Mapper me = move.createMapper();
        me.setType(globType);
        me.setFrom("*"+fromExtension);
        me.setTo("*"+toExtension);

        move.execute();
    }

}
