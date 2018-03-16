package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.EnumeratedAttribute;
import java.io.File;
import java.util.*;
import java.text.*;
/**
 * Task to perform HISTORY commands to Microsoft Visual Source Safe.
 *
 * @author Balazs Fejes 2
 * @author Glenn_Twiggs@bmc.com
 */

public class MSVSSHISTORY extends MSVSS {

    private String m_FromDate = null;
    private String m_ToDate = null;
    private DateFormat m_DateFormat =
        DateFormat.getDateInstance(DateFormat.SHORT);
    
    private String m_FromLabel = null;
    private String m_ToLabel = null;
    private String m_OutputFileName = null;
    private String m_User = null;
    private int m_NumDays = Integer.MIN_VALUE;
    private String m_Style = "";
    private boolean m_Recursive = false;
    
    public static final String VALUE_FROMDATE = "~d";
    public static final String VALUE_FROMLABEL = "~L";

    public static final String FLAG_OUTPUT = "-O";
    public static final String FLAG_USER = "-U";

    /**
     * Executes the task.
     * <p>
     * Builds a command line to execute ss and then calls Exec's run method
     * to execute the command line.
     */
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        int result = 0;

        if (getVsspath() == null) {
            String msg = "vsspath attribute must be set!";
            throw new BuildException(msg, location);
        }


        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_HISTORY);

        commandLine.createArgument().setValue(getVsspath());


        getVersionDateCommand(commandLine);
        getVersionLabelCommand(commandLine);

        if (m_Recursive) {
            commandLine.createArgument().setValue(FLAG_RECURSION);
        }

        if (m_Style.length() > 0) {
            commandLine.createArgument().setValue(m_Style);
        }

        getLoginCommand(commandLine);
                
        getOutputCommand(commandLine);

        System.out.println("***: " + commandLine);
        
        result = run(commandLine);
        if ( result != 0 ) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }

    }

    /**
     * Set the Start Date for the Comparison of two versions in SourceSafe History
     */
    public void setFromDate(String fromDate) {
        if ( fromDate.equals("") || fromDate == null ) {
            m_FromDate = null;
        } else {
            m_FromDate = fromDate;
        }
    }

    /**
     * Set the Start Label 
     */
    public void setFromLabel(String fromLabel) {
        if ( fromLabel.equals("") || fromLabel == null ) {
            m_FromLabel = null;
        } else {
            m_FromLabel = fromLabel;
        }
    }

    /**
     * Set the End Label 
     */
    public void setToLabel(String toLabel) {
        if ( toLabel.equals("") || toLabel == null ) {
            m_ToLabel = null;
        } else {
            m_ToLabel = toLabel;
        }
    }

    /**
     * Set the End Date for the Comparison of two versions in SourceSafe History
     */
    public void setToDate(String toDate) {
        if ( toDate.equals("") || toDate == null ) {
            m_ToDate = null;
        } else {
            m_ToDate = toDate;
        }
    }

    /**
     * Set the number of days to go back for Comparison
     * <p>
     * The default value is 2 days. 
     */
    public void setNumdays(int numd) {
        m_NumDays = numd;
    }
    
    /**
     * Set the output file name for SourceSafe output
     */
    public void setOutput(File outfile) {
        if ( outfile == null ) {
            m_OutputFileName = null;
        } else {
            m_OutputFileName = outfile.getAbsolutePath();
        }
    }

    /**
     * Set the Start Date for the Comparison of two versions in SourceSafe History
     */
    public void setDateFormat(String dateFormat) {
        if ( !(dateFormat.equals("") || dateFormat == null) ) {
            m_DateFormat = new SimpleDateFormat(dateFormat);
        }
    }

    /**
     * Builds the version date command.
     * @param cmd the commandline the command is to be added to
     */
    private void getVersionDateCommand(Commandline cmd) throws BuildException {
        if ( m_FromDate == null && m_ToDate == null && m_NumDays == Integer.MIN_VALUE) {
            return;
        }
        
        if ( m_FromDate != null && m_ToDate != null) {
            cmd.createArgument().setValue(FLAG_VERSION_DATE + m_ToDate + VALUE_FROMDATE + m_FromDate);
        } else if (m_ToDate != null && m_NumDays != Integer.MIN_VALUE) {
            String startDate = null;
            try {
                startDate = calcDate(m_ToDate, m_NumDays);
            } catch (ParseException ex) {
                String msg = "Error parsing date: " + m_ToDate;
                throw new BuildException(msg, location);
            }
            cmd.createArgument().setValue(FLAG_VERSION_DATE + m_ToDate + VALUE_FROMDATE + startDate);
        } else if (m_FromDate != null && m_NumDays != Integer.MIN_VALUE) {
            String endDate = null;
            try {
                endDate = calcDate(m_FromDate, m_NumDays);
            } catch (ParseException ex) {
                String msg = "Error parsing date: " + m_FromDate;
                throw new BuildException(msg, location);
            }
            cmd.createArgument().setValue(FLAG_VERSION_DATE + endDate + VALUE_FROMDATE + m_FromDate);
        } else {
            if (m_FromDate != null) {
                cmd.createArgument().setValue(FLAG_VERSION + VALUE_FROMDATE + m_FromDate);
            } else {
                cmd.createArgument().setValue(FLAG_VERSION_DATE + m_ToDate);
            }
        }
    }

    /**
     * Builds the version date command.
     * @param cmd the commandline the command is to be added to
     */
    private void getVersionLabelCommand(Commandline cmd) throws BuildException {
        if ( m_FromLabel == null && m_ToLabel == null ) {
            return;
        }
        
        if ( m_FromLabel != null && m_ToLabel != null) {
            cmd.createArgument().setValue(FLAG_VERSION_LABEL + m_ToLabel + VALUE_FROMLABEL + m_FromLabel);
        } else if (m_FromLabel != null) {
            cmd.createArgument().setValue(FLAG_VERSION + VALUE_FROMLABEL + m_FromLabel);
        } else {
            cmd.createArgument().setValue(FLAG_VERSION_LABEL + m_ToLabel);
        }
    }
    
    /**
     * Builds the version date command.
     * @param cmd the commandline the command is to be added to
     */
    private void getOutputCommand(Commandline cmd) {
        if ( m_OutputFileName != null) {
            cmd.createArgument().setValue(FLAG_OUTPUT + m_OutputFileName);
        }
    }

    /**
     * Builds the User command.
     * @param cmd the commandline the command is to be added to
     */
    private void getUserCommand(Commandline cmd) {
        if ( m_User != null) {
            cmd.createArgument().setValue(FLAG_USER + m_User);
        }
    }

     /**
     * Calculates the start date for version comparison.
     * <p>
     * Calculates the date numDay days earlier than startdate.
     */
    private String calcDate(String fromDate, int numDays) throws ParseException {
        String toDate = null;
        Date currdate = new Date();
        Calendar calend= new GregorianCalendar();
        currdate = m_DateFormat.parse(fromDate); 
        calend.setTime(currdate);
        calend.add(Calendar.DATE, numDays);
        toDate = m_DateFormat.format(calend.getTime());
        return toDate;
    }

    /**
     * Set behaviour recursive or non-recursive
     */
    public void setRecursive(boolean recursive) {
        m_Recursive = recursive;
    }

    /**
     * Set the Username of the user whose changes we would like to see.
     */
    public void setUser(String user) {
        m_User = user;
    }

    /**
     * @return the 'recursive' command if the attribute was 'true', otherwise an empty string
     */
    private void getRecursiveCommand(Commandline cmd) {
        if ( !m_Recursive ) {
            return;
        } else {
            cmd.createArgument().setValue(FLAG_RECURSION);
        }
    }

    /**
     * Specify the detail of output
     *
     * @param option valid values:
     * <ul>
     * <li>brief:    -B Display a brief history. 
     * <li>codediff: -D Display line-by-line file changes. 
     * <li>nofile:   -F- Do not display individual file updates in the project history. 
     * <li>default:  No option specified. Display in Source Safe's default format.
     * </ul>
     */
    public void setStyle(BriefCodediffNofile attr) {
        String option = attr.getValue();
        if (option.equals("brief")) {
            m_Style = "-B";
        } else if (option.equals("codediff")) {
            m_Style = "-D";
        } else if (option.equals("default")) {
            m_Style = "";
        } else if (option.equals("nofile")) {
            m_Style = "-F-";
        } else {
            throw new BuildException("Style " + attr + " unknown.");
        }
    }

    public static class BriefCodediffNofile extends EnumeratedAttribute {
       public String[] getValues() {
           return new String[] {"brief", "codediff", "nofile", "default"};
       }
   }


}
