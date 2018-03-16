package org.apache.tools.ant.taskdefs.optional.metamata;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.regexp.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.DOMElementWriter;

import org.w3c.dom.*;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;


/**
 * This is a very bad stream handler for the MAudit task.
 * All report to stdout that does not match a specific report pattern is dumped
 * to the Ant output as warn level. The report that match the pattern is stored
 * in a map with the key being the filepath that caused the error report.
 * <p>
 * The limitation with the choosen implementation is clear:
 * <ul>
 * <li>it does not handle multiline report( message that has \n ). the part until
 * the \n will be stored and the other part (which will not match the pattern)
 * will go to Ant output in Warn level.
 * <li>it does not report error that goes to stderr.
 * </ul>
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
class MAuditStreamHandler implements ExecuteStreamHandler {

    protected MAudit task;

    /** reader for stdout */
    protected BufferedReader br;

    /** matcher that will be used to extract the info from the line */
    protected RegexpMatcher matcher;

    /**
     * this is where the XML output will go, should mostly be a file
     * the caller is responsible for flushing and closing this stream
     */
    protected OutputStream xmlOut = null;

    /**
     * the multimap. The key in the map is the filepath that caused the audit
     * error and the value is a vector of MAudit.Violation entries.
     */
    protected Hashtable auditedFiles = new Hashtable();

    MAuditStreamHandler(MAudit task, OutputStream xmlOut){
        this.task = task;
        this.xmlOut = xmlOut;
        /** the matcher should be the Oro one. I don't know about the other one */
        matcher = (new RegexpMatcherFactory()).newRegexpMatcher();
        matcher.setPattern(MAudit.AUDIT_PATTERN);
    }

    /** Ignore. */
    public void setProcessInputStream(OutputStream os) {}

    /** Ignore. */
    public void setProcessErrorStream(InputStream is) {}

    /** Set the inputstream */
    public void setProcessOutputStream(InputStream is) throws IOException {
        br = new BufferedReader(new InputStreamReader(is));
    }

    /** Invokes parseOutput. This will block until the end :-(*/
    public void start() throws IOException {
        parseOutput(br);
    }

    /**
     * Pretty dangerous business here. It serializes what was extracted from
     * the MAudit output and write it to the output.
     */
    public void stop() {
        Document doc = getDocumentBuilder().newDocument();
        Element rootElement = doc.createElement("classes");
        Enumeration keys = auditedFiles.keys();
        Hashtable filemapping = task.getFileMapping();
        rootElement.setAttribute("audited", String.valueOf(filemapping.size()));
        rootElement.setAttribute("reported", String.valueOf(auditedFiles.size()));
        int errors = 0;
        while (keys.hasMoreElements()){
            String filepath = (String)keys.nextElement();
            Vector v = (Vector)auditedFiles.get(filepath);
            String fullclassname = (String)filemapping.get(filepath);
            if (fullclassname == null) {
                task.getProject().log("Could not find class mapping for " + filepath, Project.MSG_WARN);
                continue;
            }
            int pos = fullclassname.lastIndexOf('.');
            String pkg = (pos == -1) ? "" : fullclassname.substring(0, pos);
            String clazzname = (pos == -1) ? fullclassname : fullclassname.substring(pos + 1);
            Element clazz = doc.createElement("class");
            clazz.setAttribute("package", pkg);
            clazz.setAttribute("name", clazzname);
            clazz.setAttribute("violations", String.valueOf(v.size()));
            errors += v.size();
            for (int i = 0; i < v.size(); i++){
                MAudit.Violation violation = (MAudit.Violation)v.elementAt(i);
                Element error = doc.createElement("violation");
                error.setAttribute("line", String.valueOf(violation.line));
                error.setAttribute("message", violation.error);
                clazz.appendChild(error);
            }
            rootElement.appendChild(clazz);
        }
        rootElement.setAttribute("violations", String.valueOf(errors));

        if (xmlOut != null) {
            Writer wri = null;
            try {
                wri = new OutputStreamWriter(xmlOut, "UTF-8");
                wri.write("<?xml version=\"1.0\"?>\n");
                (new DOMElementWriter()).write(rootElement, wri, 0, "  ");
                wri.flush();
            } catch(IOException exc) {
                task.log("Unable to write log file", Project.MSG_ERR);
            } finally {
                if (xmlOut != System.out && xmlOut != System.err) {
                    if (wri != null) {
                        try {
                            wri.close();
                        } catch (IOException e) {}
                    }
                }
            }
        }

    }

    protected static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch(Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    /** read each line and process it */
    protected void parseOutput(BufferedReader br) throws IOException {
        String line = null;
        while ( (line = br.readLine()) != null ){
            processLine(line);
        }
    }

    protected void processLine(String line){
        Vector matches = matcher.getGroups(line);
        if (matches != null) {
            String file = (String)matches.elementAt(1);
            int lineNum = Integer.parseInt((String)matches.elementAt(2));
            String msg = (String)matches.elementAt(3);
            addViolationEntry(file, MAudit.createViolation(lineNum, msg) );
        } else {
            task.log(line, Project.MSG_INFO);
        }
    }

    /** add a violation entry for the file */
    protected void addViolationEntry(String file, MAudit.Violation entry){
            Vector violations = (Vector)auditedFiles.get(file);
            if (violations == null){
                violations = new Vector();
                auditedFiles.put(file, violations);
            }
            violations.add( entry );
    }

}
