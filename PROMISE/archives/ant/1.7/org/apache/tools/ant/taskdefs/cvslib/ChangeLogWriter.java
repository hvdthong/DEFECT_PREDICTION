package org.apache.tools.ant.taskdefs.cvslib;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.TimeZone;

import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.DOMUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class used to generate an XML changelog.
 *
 */
public class ChangeLogWriter {
    /** output format for dates written to xml file */
    private static final SimpleDateFormat OUTPUT_DATE
        = new SimpleDateFormat("yyyy-MM-dd");
    /** output format for times written to xml file */
    private static final SimpleDateFormat OUTPUT_TIME
        = new SimpleDateFormat("HH:mm");
    /** stateless helper for writing the XML document */
    private static final DOMElementWriter DOM_WRITER = new DOMElementWriter();

    static {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        OUTPUT_DATE.setTimeZone(utc);
        OUTPUT_TIME.setTimeZone(utc);
    }

    /**
     * Print out the specified entries.
     *
     * @param output writer to which to send output.
     * @param entries the entries to be written.
     */
    public void printChangeLog(final PrintWriter output,
                               final CVSEntry[] entries) {
        try {
            output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            Document doc = DOMUtils.newDocument();
            Element root = doc.createElement("changelog");
            DOM_WRITER.openElement(root, output, 0, "\t");
            output.println();
            for (int i = 0; i < entries.length; i++) {
                final CVSEntry entry = entries[i];

                printEntry(doc, output, entry);
            }
            DOM_WRITER.closeElement(root, output, 0, "\t", true);
            output.flush();
            output.close();
        } catch (IOException e) {
            throw new org.apache.tools.ant.BuildException(e);
        }
    }


    /**
     * Print out an individual entry in changelog.
     *
     * @param doc Document used to create elements.
     * @param entry the entry to print
     * @param output writer to which to send output.
     */
    private void printEntry(Document doc, final PrintWriter output,
                            final CVSEntry entry) throws IOException {
        Element ent = doc.createElement("entry");
        DOMUtils.appendTextElement(ent, "date",
                                   OUTPUT_DATE.format(entry.getDate()));
        DOMUtils.appendTextElement(ent, "time",
                                   OUTPUT_TIME.format(entry.getDate()));
        DOMUtils.appendCDATAElement(ent, "author", entry.getAuthor());

        final Enumeration enumeration = entry.getFiles().elements();

        while (enumeration.hasMoreElements()) {
            final RCSFile file = (RCSFile) enumeration.nextElement();

            Element f = DOMUtils.createChildElement(ent, "file");
            DOMUtils.appendCDATAElement(f, "name", file.getName());
            DOMUtils.appendTextElement(f, "revision", file.getRevision());

            final String previousRevision = file.getPreviousRevision();
            if (previousRevision != null) {
                DOMUtils.appendTextElement(f, "prevrevision",
                                           previousRevision);
            }
        }
        DOMUtils.appendCDATAElement(ent, "msg", entry.getComment());
        DOM_WRITER.write(ent, output, 1, "\t");
    }
}

