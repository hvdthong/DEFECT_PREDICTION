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
        try {
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

        } catch (Exception e) {
            throw new BuildException(e, location);
        }
    }

    protected void loadFile (File file) throws BuildException {
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
                log("Unable to find " + file.getAbsolutePath(), 
                    Project.MSG_VERBOSE);
            }
        } catch(Exception ex) {
            throw new BuildException(ex.getMessage(), ex, location);
        }
    }

    protected void loadResource( String name ) {
        Properties props = new Properties();
        log("Resource Loading " + name, Project.MSG_VERBOSE);
        try {
            ClassLoader cL = null; 
            InputStream is = null;

            if (classpath != null) { 
                cL = new AntClassLoader(project, classpath, false); 
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void loadEnvironment( String prefix ) {
        Properties props = new Properties();
        if (!prefix.endsWith(".")) prefix += ".";
        log("Loading Environment " + prefix, Project.MSG_VERBOSE);
        try {
            Vector osEnv = Execute.getProcEnvironment();
            for (Enumeration e = osEnv.elements(); e.hasMoreElements(); ) {
                String entry = (String)e.nextElement();
                int pos = entry.indexOf('=');
                props.put(prefix + entry.substring(0, pos), 
                          entry.substring(pos + 1));
            }
            addProperties(props);
        } catch (Exception ex) {
            throw new BuildException(ex, location);
        }
    }

    protected void addProperties(Properties props) {
        resolveAllProperties(props);
        Enumeration e = props.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = (String) props.getProperty(name);
            String v = ProjectHelper.replaceProperties(project, value, project.getProperties());
            addProperty(name, value);
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

    private void resolveAllProperties(Hashtable props) {
        Hashtable unresolvableProperties = new Hashtable();
        for (Enumeration e = props.keys(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            String value = (String) props.get(name);

            boolean resolved = false;
            while (!resolved) { 
                Vector propsInValue = new Vector();
    
                resolved = true;
                boolean unresolvable = false;
                if (extractProperties(value, propsInValue)) {
                    for (int i=0; i < propsInValue.size(); i++) {
                        String elem = (String) propsInValue.elementAt(i);
                        if (elem.equals(name) || unresolvableProperties.containsKey(elem)) {
                            unresolvable = true;
                            break;
                        }
                        
                        if (project.getProperties().containsKey(elem) ||
                            props.containsKey(elem)) {
                            resolved = false;
                        }
                    }
                }
    
                if (unresolvable) {
                    unresolvableProperties.put(name, value);
                    resolved = true;
                }
    
                if (!resolved) {
                    value = ProjectHelper.replaceProperties(project, value,
                                                               project.getProperties());
                    value = ProjectHelper.replaceProperties(project, value, props);
                    props.put(name, value);
                }    
            }
        }
    }

    private boolean extractProperties(String source, Vector properties) {
        int i=0;
        int prev=0;
        int pos;

        while( (pos=source.indexOf( "$", prev )) >= 0 ) {
            if( pos == (source.length() - 1)) {
                prev = pos + 1;
            } else if (source.charAt( pos + 1 ) != '{' ) {
                prev=pos+2;
            } else {
                int endName=source.indexOf( '}', pos );
                String n=source.substring( pos+2, endName );
                properties.addElement(n);
                prev=endName+1;
            }
        }
        
        return (properties.size() > 0);
    }
    
}
