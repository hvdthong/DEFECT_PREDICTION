package org.apache.tools.ant.taskdefs.optional.vss;

import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Performs History commands to Microsoft Visual SourceSafe.
 *
 * @ant.task name="vsshistory" category="scm"
 */
public class MSVSSHISTORY extends MSVSS {

    /**
     * Builds a command line to execute ss.
     * @return     The constructed commandline.
     */
    Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();

        if (getVsspath() == null) {
            String msg = "vsspath attribute must be set!";
            throw new BuildException(msg, getLocation());
        }

        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_HISTORY);

        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getVersionDate());
        commandLine.createArgument().setValue(getVersionLabel());
        commandLine.createArgument().setValue(getRecursive());
        commandLine.createArgument().setValue(getStyle());
        commandLine.createArgument().setValue(getLogin());
        commandLine.createArgument().setValue(getOutput());

        return commandLine;
    }

    /**
     * Retrieve history recursively. Defaults to false.
     *
     * @param recursive  The boolean value for recursive.
     */
    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    /**
     * Name of the user whose change history is generated.
     *
     * @param   user The username.
     */
    public void setUser(String user) {
        super.setInternalUser(user);
    }

    /**
     * Date representing the 'start' of the range.
     *
     * @param   fromDate    The start date.
     */
    public void setFromDate(String fromDate) {
        super.setInternalFromDate(fromDate);
    }

    /**
     * Date representing the 'end' of the range.
     *
     * @param   toDate    The end date.
     */
    public void setToDate(String toDate) {
        super.setInternalToDate(toDate);
    }

    /**
     * Label representing the 'start' of the range.
     *
     * @param   fromLabel    The start label.
     */
    public void setFromLabel(String fromLabel) {
        super.setInternalFromLabel(fromLabel);
    }

    /**
     * Label representing the 'end' of the range.
     *
     * @param   toLabel    The end label.
     */
    public void setToLabel(String toLabel) {
        super.setInternalToLabel(toLabel);
    }

    /**
     * Number of days for comparison.
     * Defaults to 2 days.
     *
     * @param   numd    The number of days.
     */
    public void setNumdays(int numd) {
        super.setInternalNumDays(numd);
    }

    /**
     * Output file name for the history.
     *
     * @param   outfile The output file name.
     */
    public void setOutput(File outfile) {
        if (outfile != null) {
            super.setInternalOutputFilename(outfile.getAbsolutePath());
        }
    }

    /**
     * Format of dates in <code>fromDate</code and <code>toDate</code>.
     * Used when calculating dates with the numdays attribute.
     * This string uses the formatting rules of <code>SimpleDateFormat</code>.
     * Defaults to <code>DateFormat.SHORT</code>.
     *
     * @param   dateFormat  The date format.
     */
    public void setDateFormat(String dateFormat) {
        super.setInternalDateFormat(new SimpleDateFormat(dateFormat));
    }

   /**
     * Output style. Valid options are:
     * <ul>
     * <li>brief:    -B Display a brief history.
     * <li>codediff: -D Display line-by-line file changes.
     * <li>nofile:   -F- Do not display individual file updates in the project history.
     * <li>default:  No option specified. Display in Source Safe's default format.
     * </ul>
     *
     * @param attr The history style:
     */
    public void setStyle(BriefCodediffNofile attr) {
        String option = attr.getValue();
        if (option.equals(STYLE_BRIEF)) {
            super.setInternalStyle(FLAG_BRIEF);
        } else if (option.equals(STYLE_CODEDIFF)) {
            super.setInternalStyle(FLAG_CODEDIFF);
        } else if (option.equals(STYLE_DEFAULT)) {
            super.setInternalStyle("");
        } else if (option.equals(STYLE_NOFILE)) {
            super.setInternalStyle(FLAG_NO_FILE);
        } else {
            throw new BuildException("Style " + attr + " unknown.", getLocation());
        }
    }

    /**
     * Extention of EnumeratedAttribute to hold the values for style.
     */
    public static class BriefCodediffNofile extends EnumeratedAttribute {
        /**
         * Gets the list of allowable values.
         * @return The values.
         */
        public String[] getValues() {
            return new String[] {STYLE_BRIEF, STYLE_CODEDIFF, STYLE_NOFILE, STYLE_DEFAULT};
        }
    }
}
