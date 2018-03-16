package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
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

    private File dir = null;
    private String antFile = null;
    private String target = null;
    private String output = null;

    Vector properties=new Vector();
    Project p1;

    public void init() {
        p1 = new Project();
        p1.setJavaVersionProperty();
        p1.addTaskDefinition("property", 
                             (Class)project.getTaskDefinitions().get("property"));
    }

    private void reinit() {
        init();
        for (int i=0; i<properties.size(); i++) {
            Property p = (Property) properties.elementAt(i);
            Property newP = (Property) p1.createTask("property");
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
            p1.addBuildListener((BuildListener)listeners.elementAt(i));
        }

        if (output != null) {
            try {
                PrintStream out = new PrintStream(new FileOutputStream(output));
                DefaultLogger logger = new DefaultLogger();
                logger.setMessageOutputLevel(Project.MSG_INFO);
                logger.setOutputPrintStream(out);
                logger.setErrorPrintStream(out);
                p1.addBuildListener(logger);
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
            p1.addTaskDefinition(taskName, taskClass);
        }

        Hashtable typedefs = project.getDataTypeDefinitions();
        Enumeration e = typedefs.keys();
        while (e.hasMoreElements()) {
            String typeName = (String) e.nextElement();
            Class typeClass = (Class) typedefs.get(typeName);
            p1.addDataTypeDefinition(typeName, typeClass);
        }

        Hashtable prop1 = project.getProperties();
        e = prop1.keys();
        while (e.hasMoreElements()) {
            String arg = (String) e.nextElement();
            String value = (String) prop1.get(arg);
            p1.setProperty(arg, value);
        }
    }

    /**
     * Do the execution.
     */
    public void execute() throws BuildException {
        try {
            if (p1 == null) {
                reinit();
            }
        
            if(dir == null) 
                dir = project.getBaseDir();

            initializeProject();

            p1.setBaseDir(dir);
            p1.setUserProperty("basedir" , dir.getAbsolutePath());
            
            Enumeration e = properties.elements();
            while (e.hasMoreElements()) {
                Property p=(Property) e.nextElement();
                p.execute();
            }
            
            if (antFile == null) 
                antFile = "build.xml";

            File file = new File(antFile);
            if (!file.isAbsolute()) {
                antFile = (new File(dir, antFile)).getAbsolutePath();
                file = (new File(antFile)) ;
                if( ! file.isFile() ) {
                  throw new BuildException("Build file " + file + " not found.");
                }
            }

            p1.setUserProperty( "ant.file" , antFile );
            ProjectHelper.configureProject(p1, new File(antFile));
            
            if (target == null) {
                target = p1.getDefaultTarget();
            }

            if (p1.getBaseDir().equals(project.getBaseDir()) &&
                p1.getProperty("ant.file").equals(project.getProperty("ant.file")) &&
                target.equals(this.getOwningTarget().getName())) { 

                throw new BuildException("ant task calling its own parent target");
            }

            p1.executeTarget(target);
        } finally {
            p1 = null;
        }
    }

    public void setDir(File d) {
        this.dir = d;
    }

    public void setAntfile(String s) {
        this.antFile = s;
    }

    public void setTarget(String s) {
        this.target = s;
    }

    public void setOutput(String s) {
        this.output = s;
    }

    public Property createProperty() {
        if (p1 == null) {
            reinit();
        }

        Property p=(Property)p1.createTask("property");
        p.setUserProperty(true);
        properties.addElement( p );
        return p;
    }
}
