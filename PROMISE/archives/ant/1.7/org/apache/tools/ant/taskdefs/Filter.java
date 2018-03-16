package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Sets a token filter that is used by the file copy tasks
 * to do token substitution. Sets multiple tokens by
 * reading these from a file.
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

    /**
     * Execute the task.
     * @throws BuildException on error
     */
    public void execute() throws BuildException {
        boolean isFiltersFromFile =
            filtersFile != null && token == null && value == null;
        boolean isSingleFilter =
            filtersFile == null && token != null && value != null;

        if (!isFiltersFromFile && !isSingleFilter) {
            throw new BuildException("both token and value parameters, or "
                                     + "only a filtersFile parameter is "
                                     + "required", getLocation());
        }

        if (isSingleFilter) {
            getProject().getGlobalFilterSet().addFilter(token, value);
        }

        if (isFiltersFromFile) {
            readFilters();
        }
    }

    /**
     * Read the filters.
     * @throws BuildException on error
     */
    protected void readFilters() throws BuildException {
        log("Reading filters from " + filtersFile, Project.MSG_VERBOSE);
        getProject().getGlobalFilterSet().readFiltersFromFile(filtersFile);
    }
}
