package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ScriptRunner;

/**
 * Executes a script.
 *
 * @ant.task name="script"
 */
public class Script extends Task {

    private String language;
    private File   src;
    private String text;

    /**
     * Do the work.
     *
     * @exception BuildException if something goes wrong with the build
     */
    public void execute() throws BuildException {
        ScriptRunner runner = new ScriptRunner();
        if (language != null) {
            runner.setLanguage(language);
        }
        if (src != null) {
            runner.setSrc(src);
        }
        if (text != null) {
            runner.addText(text);
        }

        runner.addBeans(getProject().getProperties());
        runner.addBeans(getProject().getUserProperties());
        runner.addBeans(getProject().getTargets());
        runner.addBeans(getProject().getReferences());

        runner.addBean("project", getProject());
        runner.addBean("self", this);

        runner.executeScript("ANT");
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
     * Load the script from an external file ; optional.
     *
     * @param fileName the name of the file containing the script source.
     */
    public void setSrc(String fileName) {
        this.src = new File(fileName);
    }

    /**
     * Set the script text.
     *
     * @param text a component of the script text to be added.
     */
    public void addText(String text) {
        this.text = text;
    }
}
