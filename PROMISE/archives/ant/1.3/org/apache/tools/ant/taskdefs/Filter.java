package org.apache.tools.ant.taskdefs;

import java.util.Enumeration;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

import org.apache.tools.ant.*;

/**
 * This task sets a token filter that is used by the file copy methods
 * of the project to do token substitution, or sets mutiple tokens by
 * reading these from a file.
 *
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author Gero Vermaas <a href="mailto:gero@xs4all.nl">gero@xs4all.nl</a>
 */
public class Filter extends Task {

    private String token;
    private String value;
    private File filtersFile;
    
    public void setToken(String token) {
        this.token = token;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setFiltersfile(File filtersFile) {
        this.filtersFile = filtersFile;
    }

    public void execute() throws BuildException {
        boolean isFiltersFromFile = filtersFile != null && token == null && value == null;
        boolean isSingleFilter = filtersFile == null && token != null && value != null;
        
        if (!isFiltersFromFile && !isSingleFilter) {
            throw new BuildException("both token and value parameters, or only a filtersFile parameter is required", location);
        }
        
        if (isSingleFilter) {
            project.addFilter(token, value);
        }
        
        if (isFiltersFromFile) {
            readFilters();
        }
    }
    
    protected void readFilters() throws BuildException {
        log("Reading filters from " + filtersFile, Project.MSG_VERBOSE);
        FileInputStream in = null;
        try {
            Properties props = new Properties();
            in = new FileInputStream(filtersFile);
            props.load(in);

            Project proj = getProject();

            Enumeration enum = props.propertyNames();
            while (enum.hasMoreElements()) {
                String strPropName = (String)enum.nextElement();
                String strValue = props.getProperty(strPropName);
                proj.addFilter(strPropName, strValue);
            }
        } catch (Exception e) {
            throw new BuildException("Could not read filters from file: " + filtersFile);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (java.io.IOException ioex) {}
            }
        }
    }
}
