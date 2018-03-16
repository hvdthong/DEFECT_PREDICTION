package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.util.FileUtils;
import java.io.*;
import java.util.*;

/**
 * Call Ant in a sub-project
 *
 *  <pre>
 *    <target name="foo" depends="init">
 *    <ant antfile="build.xml" target="bar" >
 *      <property name="property1" value="aaaaa" />
 *      <property name="foo" value="baz" />
 *     </ant>
 *  </target>
 *
 * <target name="bar" depends="init">
 *    <echo message="prop is ${property1} ${foo}" />
 * </target>
 * </pre>
 *
 *
 * @author costin@dnt.ro
 */
public class Ant extends Task {

    /** the basedir where is executed the build file */
    private File dir = null;
    
    /** the build.xml file (can be absolute) in this case dir will be ignored */
    private String antFile = null;
    
    /** the target to call if any */
    private String target = null;
    
    /** the output */
    private String output = null;
    
    /** should we inherit properties from the parent ? */
    private boolean inheritAll = true;
    
    /** the properties to pass to the new project */
    private Vector properties = new Vector();
    
    /** the temporary project created to run the build file */
    private Project newProject;

    /**
     * If true, inherit all properties from parent Project
     * If false, inherit only userProperties and those defined
     * inside the ant call itself
     */
    public void setInheritAll(boolean value) {
       inheritAll = value;
    }

    public void init() {
        newProject = new Project();
        newProject.setJavaVersionProperty();
        newProject.addTaskDefinition("property", 
                             (Class)project.getTaskDefinitions().get("property"));
    }

    private void reinit() {
        init();
        for (int i=0; i<properties.size(); i++) {
            Property p = (Property) properties.elementAt(i);
            Property newP = (Property) newProject.createTask("property");
            newP.setName(p.getName());
            if (p.getValue() != null) {
                newP.setValue(p.getValue());
            }
            if (p.getFile() != null) {
                newP.setFile(p.getFile());
            } 
            if (p.getResource() != null) {
                newP.setResource(p.getResource());
            }
            properties.setElementAt(newP, i);
        }
    }

    private void initializeProject() {
        Vector listeners = project.getBuildListeners();
        for (int i = 0; i < listeners.size(); i++) {
            newProject.addBuildListener((BuildListener)listeners.elementAt(i));
        }

        if (output != null) {
            try {
                PrintStream out = new PrintStream(new FileOutputStream(output));
                DefaultLogger logger = new DefaultLogger();
                logger.setMessageOutputLevel(Project.MSG_INFO);
                logger.setOutputPrintStream(out);
                logger.setErrorPrintStream(out);
                newProject.addBuildListener(logger);
            }
            catch( IOException ex ) {
                log( "Ant: Can't set output to " + output );
            }
        }

        Hashtable taskdefs = project.getTaskDefinitions();
        Enumeration et = taskdefs.keys();
        while (et.hasMoreElements()) {
            String taskName = (String) et.nextElement();
            Class taskClass = (Class) taskdefs.get(taskName);
            newProject.addTaskDefinition(taskName, taskClass);
        }

        Hashtable typedefs = project.getDataTypeDefinitions();
        Enumeration e = typedefs.keys();
        while (e.hasMoreElements()) {
            String typeName = (String) e.nextElement();
            Class typeClass = (Class) typedefs.get(typeName);
            newProject.addDataTypeDefinition(typeName, typeClass);
        }

        Hashtable prop1;
        if (inheritAll == true) {
           prop1 = project.getProperties();
        }
        else {
           prop1 = project.getUserProperties();

           newProject.setSystemProperties();
        }
        
        e = prop1.keys();
        while (e.hasMoreElements()) {
            String arg = (String) e.nextElement();
            String value = (String) prop1.get(arg);
            if (inheritAll == true){
               newProject.setProperty(arg, value);
            } else {
               newProject.setUserProperty(arg, value);
            }
        }
    }

    protected void handleOutput(String line) {
        if (newProject != null) {
            newProject.demuxOutput(line, false);
        }
        else {
            super.handleOutput(line);
        }
    }
    
    protected void handleErrorOutput(String line) {
        if (newProject != null) {
            newProject.demuxOutput(line, true);
        }
        else {
            super.handleErrorOutput(line);
        }
    }
    
    /**
     * Do the execution.
     */
    public void execute() throws BuildException {
        try {
            if (newProject == null) {
                reinit();
            }
        
            if (dir == null) {
                dir = project.getBaseDir();
            }

            initializeProject();

            newProject.setBaseDir(dir);
            newProject.setUserProperty("basedir" , dir.getAbsolutePath());
            
            Enumeration e = properties.elements();
            while (e.hasMoreElements()) {
                Property p=(Property) e.nextElement();
                p.execute();
            }
            
            if (antFile == null){
                antFile = "build.xml";
            }

            File file = FileUtils.newFileUtils().resolveFile(dir, antFile);
            antFile = file.getAbsolutePath();

            newProject.setUserProperty( "ant.file" , antFile );
            ProjectHelper.configureProject(newProject, new File(antFile));
            
            if (target == null) {
                target = newProject.getDefaultTarget();
            }

            if (newProject.getBaseDir().equals(project.getBaseDir()) &&
                newProject.getProperty("ant.file").equals(project.getProperty("ant.file")) &&
                getOwningTarget() != null &&
                target.equals(this.getOwningTarget().getName())) { 

                throw new BuildException("ant task calling its own parent target");
            }

            newProject.executeTarget(target);
        } finally {
            newProject = null;
        }
    }

    /**
     * ...
     */
    public void setDir(File d) {
        this.dir = d;
    }

    /**
     * set the build file, it can be either absolute or relative.
     * If it is absolute, <tt>dir</tt> will be ignored, if it is
     * relative it will be resolved relative to <tt>dir</tt>.
     */
    public void setAntfile(String s) {
        this.antFile = s;
    }

    /**
     * set the target to execute. If none is defined it will
     * execute the default target of the build file
     */
    public void setTarget(String s) {
        this.target = s;
    }

    public void setOutput(String s) {
        this.output = s;
    }

    /** create a property to pass to the new project as a 'user property' */
    public Property createProperty() {
        if (newProject == null) {
            reinit();
        }
        Property p=(Property)newProject.createTask("property");
        p.setUserProperty(true);
        properties.addElement( p );
        return p;
    }
}
