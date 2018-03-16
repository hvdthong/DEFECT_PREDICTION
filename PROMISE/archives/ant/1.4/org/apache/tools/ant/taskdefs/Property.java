package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import java.io.*;
import java.util.*;

/**
 * Will set a Project property. Used to be a hack in ProjectHelper
 * Will not override values set by the command line or parent projects.
 *
 * @author costin@dnt.ro
 * @author Sam Ruby <rubys@us.ibm.com>
 * @author Glenn McAllister <glennm@ca.ibm.com>
 */
public class Property extends Task {

    protected String name;
    protected String value;
    protected File file;
    protected String resource;
    protected Path classpath;
    protected String env;
    protected Reference ref = null;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLocation(File location) {
        setValue(location.getAbsolutePath());
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setRefid(Reference ref) {
        this.ref = ref;
    }

    public Reference getRefid() {
        return ref;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public void setEnvironment(String env) {
        this.env = env;
    }

    public String getEnvironment() {
        return env;
    }

    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }
    
    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(project);
        }
        return this.classpath.createPath();
    }
    
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setUserProperty(boolean userProperty) {
        this.userProperty = userProperty;
    }

    public String toString() {
        return value == null ? "" : value;
    }

    public void execute() throws BuildException {
        if (name != null) {
            if (value == null && ref == null) {
                throw new BuildException("You must specify value, location or refid with the name attribute",
                                         location);
            }
        } else {
            if (file == null && resource == null && env == null) {
                throw new BuildException("You must specify file, resource or environment when not using the name attribute",
                                         location);
            }
        }

        if ((name != null) && (value != null)) {
            addProperty(name, value);
        }
        
        if (file != null) loadFile(file);
        
        if (resource != null) loadResource(resource);
        
        if (env != null) loadEnvironment(env);
        
        if ((name != null) && (ref != null)) {
            Object obj = ref.getReferencedObject(getProject());
            if (obj != null) {
                addProperty(name, obj.toString());
            }
        }
    }

    protected void loadFile(File file) throws BuildException {
        Properties props = new Properties();
        log("Loading " + file.getAbsolutePath(), Project.MSG_VERBOSE);
        try {
            if (file.exists()) { 
                FileInputStream fis = new FileInputStream(file);
                try { 
                    props.load(fis);
                } finally {
                    if (fis != null) { 
                        fis.close();
                    }
                }
                addProperties(props);
            } else {
                log("Unable to find property file: " + file.getAbsolutePath(), 
                    Project.MSG_VERBOSE);
            }
        } catch(IOException ex) {
            throw new BuildException(ex, location);
        }
    }

    protected void loadResource(String name) {
        Properties props = new Properties();
        log("Resource Loading " + name, Project.MSG_VERBOSE);
        try {
            ClassLoader cL = null; 
            InputStream is = null;

            if (classpath != null) { 
                cL = new AntClassLoader(project, classpath); 
            } else { 
                cL = this.getClass().getClassLoader(); 
            } 

            if (cL == null) {
                is = ClassLoader.getSystemResourceAsStream(name);
            } else {
                is = cL.getResourceAsStream(name);
            }
            
            if (is != null) {
                props.load(is);
                addProperties(props);
            } else {
                log("Unable to find resource " + name, Project.MSG_WARN);
            }
        } catch (IOException ex) {
            throw new BuildException(ex, location);
        }
    }

    protected void loadEnvironment( String prefix ) {
        Properties props = new Properties();
        if (!prefix.endsWith(".")) prefix += ".";
        log("Loading Environment " + prefix, Project.MSG_VERBOSE);
        Vector osEnv = Execute.getProcEnvironment();
        for (Enumeration e = osEnv.elements(); e.hasMoreElements(); ) {
            String entry = (String)e.nextElement();
            int pos = entry.indexOf('=');
            if (pos == -1) {
                log("Ignoring: " + entry, Project.MSG_WARN);
            } else {
                props.put(prefix + entry.substring(0, pos), 
                entry.substring(pos + 1));
            }
        }
        addProperties(props);
    }

    protected void addProperties(Properties props) {
        resolveAllProperties(props);
        Enumeration e = props.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = (String) props.getProperty(name);

            String v = ProjectHelper.replaceProperties(project, value, project.getProperties());
            addProperty(name, v);
        }
    }

    protected void addProperty(String n, String v) {
        if( userProperty ) {
            if (project.getUserProperty(n) == null) {
                project.setUserProperty(n, v);
            } else {
                log("Override ignored for " + n, Project.MSG_VERBOSE);
            } 
        } else {
            if (project.getProperty(n) == null) {
                project.setProperty(n, v);
            } else {
                log("Override ignored for " + n, Project.MSG_VERBOSE);
            }
        }
    }

    private void resolveAllProperties(Properties props) throws BuildException {
        for (Enumeration e = props.keys(); e.hasMoreElements();) {
            String name = (String)e.nextElement();
            String value = props.getProperty(name);

            boolean resolved = false;
            while (!resolved) {
                Vector fragments = new Vector();
                Vector propertyRefs = new Vector();
                ProjectHelper.parsePropertyString(value, fragments, propertyRefs);
                
                resolved = true;
                if (propertyRefs.size() != 0) {
                    StringBuffer sb = new StringBuffer();
                    Enumeration i = fragments.elements();
                    Enumeration j = propertyRefs.elements();
                    while (i.hasMoreElements()) {
                        String fragment = (String)i.nextElement();
                        if (fragment == null) {
                            String propertyName = (String)j.nextElement();
                            if (propertyName.equals(name)) {
                                throw new BuildException("Property " + name + " was circularly defined.");
                            }
                            fragment = getProject().getProperty(propertyName);
                            if (fragment == null) {
                                if (props.containsKey(propertyName)) {
                                    fragment = props.getProperty(propertyName);
                                    resolved = false;
                                }
                                else {
                                    fragment = "${" + propertyName + "}";
                                }
                            }
                        }
                        sb.append(fragment);
                    }
                    value = sb.toString();
                    props.put(name, value);
                }
            }
        }
    }    
}
