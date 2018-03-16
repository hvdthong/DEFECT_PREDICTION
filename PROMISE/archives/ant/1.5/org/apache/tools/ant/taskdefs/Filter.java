package org.apache.tools.ant.taskdefs;

import java.io.File;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Sets a token filter that is used by the file copy tasks
 * to do token substitution. Sets mutiple tokens by
 * reading these from a file.
 *
 * @author Stefano Mazzocchi 
 *         <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author Gero Vermaas <a href="mailto:gero@xs4all.nl">gero@xs4all.nl</a>
 * @author <A href="gholam@xtra.co.nz">Michael McCallum</A>
 *
 * @since Ant 1.1
 *
 * @ant.task category="filesystem"
 */
public class Filter extends Task {

    private String token;
    private String value;
    private File filtersFile;

    /**
     * The token string without @ delimiters.
     * @param token token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * The string that should replace the token during filtered copies.
     * @param value token replace value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * The file from which the filters must be read.
     * This file must be a formatted as a property file.
     *
     * @param filtersFile filter file
     */
    public void setFiltersfile(File filtersFile) {
        this.filtersFile = filtersFile;
    }

    public void execute() throws BuildException {
        boolean isFiltersFromFile = 
            filtersFile != null && token == null && value == null;
        boolean isSingleFilter = 
            filtersFile == null && token != null && value != null;
        
        if (!isFiltersFromFile && !isSingleFilter) {
            throw new BuildException("both token and value parameters, or "
                                     + "only a filtersFile parameter is "
                                     + "required", location);
        }
        
        if (isSingleFilter) {
            project.getGlobalFilterSet().addFilter(token, value);
        }
        
        if (isFiltersFromFile) {
            readFilters();
        }
    }
    
    protected void readFilters() throws BuildException {
        log("Reading filters from " + filtersFile, Project.MSG_VERBOSE);
        project.getGlobalFilterSet().readFiltersFromFile(filtersFile);
    }
}
