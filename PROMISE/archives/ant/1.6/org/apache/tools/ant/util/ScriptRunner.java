package org.apache.tools.ant.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.tools.ant.BuildException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is used to run BSF scripts
 *
 */
public class ScriptRunner {

    static {
        BSFManager.registerScriptingEngine(
            "groovy",
            "org.codehaus.groovy.bsf.GroovyEngine",
            new String[] {"groovy", "gy"});
    }

    /** Script language */
    private String language;

    /** Script content */
    private String script = "";

    /** Beans to be provided to the script */
    private Map beans = new HashMap();


    /**
     * Add a list of named objects to the list to be exported to the script
     *
     * @param dictionary a map of objects to be placed into the script context
     *        indexed by String names.
     */
    public void addBeans(Map dictionary) {
        for (Iterator i = dictionary.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            try {
                Object val = dictionary.get(key);
                addBean(key, val);
            } catch (BuildException ex) {
            }
        }
    }

    /**
     * Add a single object into the script context.
     *
     * @param key the name in the context this object is to stored under.
     * @param bean the object to be stored in the script context.
     */
    public void addBean(String key, Object bean) {
        boolean isValid = key.length() > 0
            && Character.isJavaIdentifierStart(key.charAt(0));

        for (int i = 1; isValid && i < key.length(); i++) {
            isValid = Character.isJavaIdentifierPart(key.charAt(i));
        }

        if (isValid) {
            beans.put(key, bean);
        }
    }

    /**
     * Do the work.
     *
     * @param execName the name that will be passed to BSF for this script
     *        execution.
     *
     * @exception BuildException if someting goes wrong exectuing the script.
     */
    public void executeScript(String execName) throws BuildException {
        if (language == null) {
            throw new BuildException("script language must be specified");
        }

        try {
            BSFManager manager = new BSFManager ();

            for (Iterator i = beans.keySet().iterator(); i.hasNext();) {
                String key = (String) i.next();
                Object value = beans.get(key);
                if (value != null) {
                    manager.declareBean(key, value, value.getClass());
                } else {
                    manager.undeclareBean(key);
                }
            }

            manager.exec(language, execName, 0, 0, script);
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
     * @param language the scripting language name for the script.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get the script language
     *
     * @return the script language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Load the script from an external file ; optional.
     *
     * @param file the file containing the script source.
     */
    public void setSrc(File file) {
        if (!file.exists()) {
            throw new BuildException("file " + file.getPath() + " not found.");
        }

        int count = (int) file.length();
        byte[] data = new byte[count];

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
     * Set the script text.
     *
     * @param text a component of the script text to be added.
     */
    public void addText(String text) {
        this.script += text;
    }
}
