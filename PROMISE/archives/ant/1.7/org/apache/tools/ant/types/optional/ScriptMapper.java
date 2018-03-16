package org.apache.tools.ant.types.optional;

import org.apache.tools.ant.util.FileNameMapper;

import java.util.ArrayList;

/**
 * Script support at map time.
 * @since Ant1.7
 */
public class ScriptMapper extends AbstractScriptComponent implements FileNameMapper {


    private ArrayList files;
    static final String[] EMPTY_STRING_ARRAY = new String[0];


    /**
     * Sets the from part of the transformation rule.
     *
     * @param from a string.
     */
    public void setFrom(String from) {

    }

    /**
     * Sets the to part of the transformation rule.
     *
     * @param to a string.
     */
    public void setTo(String to) {

    }

    /**
     * Reset the list of files
     */
    public void clear() {
        files = new ArrayList(1);
    }

    /**
     * Add a mapped name
     * @param mapping the value to use.
     */
    public void addMappedName(String mapping) {
        files.add(mapping);
    }

    /**
     * Returns an array containing the target filename(s) for the given source
     * file.
     * <p/>
     * <p>if the given rule doesn't apply to the source file, implementation
     * must return null. SourceFileScanner will then omit the source file in
     * question.</p>
     *
     * @param sourceFileName the name of the source file relative to some given
     *                       basedirectory.
     * @return an array of strings if the rule applies to the source file, or
     *         null if it does not.
     */

    public String[] mapFileName(String sourceFileName) {
        initScriptRunner();
        getRunner().addBean("source", sourceFileName);
        clear();
        executeScript("ant_mapper");
        if (files.size() == 0) {
            return null;
        } else {
            return (String[]) files.toArray(EMPTY_STRING_ARRAY);
        }
    }
}
