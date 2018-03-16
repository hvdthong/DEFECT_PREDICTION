package org.apache.tools.ant.taskdefs.cvslib;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.TimeZone;

/**
 * Class used to generate an XML changelog.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 */
class ChangeLogWriter {
    /** output format for dates writtn to xml file */
    private static final SimpleDateFormat c_outputDate 
        = new SimpleDateFormat("yyyy-MM-dd");
    /** output format for times writtn to xml file */
    private static final SimpleDateFormat c_outputTime 
        = new SimpleDateFormat("HH:mm");

    static {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        c_outputDate.setTimeZone(utc);
        c_outputTime.setTimeZone(utc);
    }

    /**
     * Print out the specifed entrys.
     *
     * @param output writer to which to send output.
     * @param entries the entries to be written.
     */
    public void printChangeLog(final PrintWriter output,
                               final CVSEntry[] entries) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        output.println("<changelog>");
        for (int i = 0; i < entries.length; i++) {
            final CVSEntry entry = entries[i];

            printEntry(output, entry);
        }
        output.println("</changelog>");
        output.flush();
        output.close();
    }


    /**
     * Print out an individual entry in changelog.
     *
     * @param entry the entry to print
     * @param output writer to which to send output.
     */
    private void printEntry(final PrintWriter output, final CVSEntry entry) {
        output.println("\t<entry>");
        output.println("\t\t<date>" + c_outputDate.format(entry.getDate()) 
            + "</date>");
        output.println("\t\t<time>" + c_outputTime.format(entry.getDate()) 
            + "</time>");
        output.println("\t\t<author><![CDATA[" + entry.getAuthor() 
            + "]]></author>");

        final Enumeration enumeration = entry.getFiles().elements();

        while (enumeration.hasMoreElements()) {
            final RCSFile file = (RCSFile) enumeration.nextElement();

            output.println("\t\t<file>");
            output.println("\t\t\t<name>" + file.getName() + "</name>");
            output.println("\t\t\t<revision>" + file.getRevision() 
                + "</revision>");

            final String previousRevision = file.getPreviousRevision();

            if (previousRevision != null) {
                output.println("\t\t\t<prevrevision>" + previousRevision 
                    + "</prevrevision>");
            }

            output.println("\t\t</file>");
        }
        output.println("\t\t<msg><![CDATA[" + entry.getComment() + "]]></msg>");
        output.println("\t</entry>");
    }
}

