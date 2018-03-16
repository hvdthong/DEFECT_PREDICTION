package org.apache.tools.ant.taskdefs.optional;

import com.ibm.bsf.*;
import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;

/**
 * Execute a script
 *
 * @author Sam Ruby <a href="mailto:rubys@us.ibm.com">rubys@us.ibm.com</a>
 */
public class Script extends Task {
    private String language;
    private String script = "";
    private Hashtable beans = new Hashtable();
    
    /**
     * Add a list of named objects to the list to be exported to the script
     */
    private void addBeans(Hashtable dictionary) {
        for (Enumeration e=dictionary.keys(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();

            boolean isValid = key.length()>0 &&
                Character.isJavaIdentifierStart(key.charAt(0));

            for (int i=1; isValid && i<key.length(); i++)
                isValid = Character.isJavaIdentifierPart(key.charAt(i));

            if (isValid) beans.put(key, dictionary.get(key));
        }
    }

    /**
     * Do the work.
     *
     * @exception BuildException if someting goes wrong with the build
     */
    public void execute() throws BuildException {
        try {
            addBeans(project.getProperties());
            addBeans(project.getUserProperties());
            addBeans(project.getTargets());
            addBeans(project.getReferences());

            BSFManager manager = new BSFManager ();

            for (Enumeration e = beans.keys() ; e.hasMoreElements() ;) {
                String key = (String)e.nextElement();
                Object value = beans.get(key);
                manager.declareBean(key, value, value.getClass());
            }

            manager.exec(language, "<ANT>", 0, 0, script);
        } catch (BSFException be) {
            Throwable t = be;
            Throwable te = be.getTargetException();
            if (te != null) {
                if  (te instanceof BuildException) {
                    throw (BuildException) te;
                } else {
                    t = te;
                }
            }
            throw new BuildException(t);
        }
    }

    /**
     * Defines the language (required).
     *
     * @param msg Sets the value for the script variable.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Load the script from an external file 
     *
     * @param msg Sets the value for the script variable.
     */
    public void setSrc(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) 
            throw new BuildException("file " + fileName + " not found.");

        int count = (int)file.length();
        byte data[] = new byte[count];

        try {
            FileInputStream inStream = new FileInputStream(file);
            inStream.read(data);
            inStream.close();
        } catch (IOException e) {
            throw new BuildException(e);
        }
        
        script += new String(data);
    }

    /**
     * Defines the script.
     *
     * @param msg Sets the value for the script variable.
     */
    public void addText(String text) {
        this.script += text;
    }
}
