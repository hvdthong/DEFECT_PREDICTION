package org.apache.tools.ant;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.apache.tools.ant.util.DOMElementWriter;

/**
 *  Generates a "log.xml" file in the current directory with
 *  an XML description of what happened during a build.
 *
 *  @see Project#addBuildListener(BuildListener)
 */
public class XmlLogger implements BuildListener {

    private static final DocumentBuilder builder = getDocumentBuilder();

    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch(Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    private static final String BUILD_TAG = "build";
    private static final String TARGET_TAG = "target";
    private static final String TASK_TAG = "task";
    private static final String MESSAGE_TAG = "message";
    private static final String NAME_ATTR = "name";
    private static final String TIME_ATTR = "time";
    private static final String PRIORITY_ATTR = "priority";
    private static final String LOCATION_ATTR = "location";
    private static final String ERROR_ATTR = "error";

    private Document doc;
    private Element buildElement;
    private Element targetElement;
    private Element taskElement;

    private long buildStartTime;
    private long targetStartTime;
    private long taskStartTime;

    private Stack targetTimeStack = new Stack();
    private Stack targetStack = new Stack();
    private Stack taskTimeStack = new Stack();
    private Stack taskStack = new Stack();

    /**
     *  Constructs a new BuildListener that logs build events to an XML file.
     */
    public XmlLogger() {
    }

    public void buildStarted(BuildEvent event) {
        buildStartTime = System.currentTimeMillis();

        doc = builder.newDocument();
        buildElement = doc.createElement(BUILD_TAG);
    }

    public void buildFinished(BuildEvent event) {
        long totalTime = System.currentTimeMillis() - buildStartTime;
        buildElement.setAttribute(TIME_ATTR, DefaultLogger.formatTime(totalTime));

        if (event.getException() != null) {
            buildElement.setAttribute(ERROR_ATTR, event.getException().toString());
        }

        try {
            String outFilename = 
                event.getProject().getProperty("XmlLogger.file");

            if (outFilename == null) {
                outFilename = "log.xml";
            }

            Writer out = new FileWriter(outFilename);
            out.write("<?xml:stylesheet type=\"text/xsl\" href=\"log.xsl\"?>\n\n");
            (new DOMElementWriter()).write(buildElement, out, 0, "\t");
            out.flush();
            out.close();
            
        } catch(IOException exc) {
            throw new BuildException("Unable to close log file", exc);
        }
        buildElement = null;
    }

    public void targetStarted(BuildEvent event) {
        if (targetElement != null) {
            targetTimeStack.push(new Long(targetStartTime));
            targetStack.push(targetElement);
        }
        targetStartTime = System.currentTimeMillis();
        targetElement = doc.createElement(TARGET_TAG);
        targetElement.setAttribute(NAME_ATTR, event.getTarget().getName());
    }

    public void targetFinished(BuildEvent event) {
        long totalTime = System.currentTimeMillis() - targetStartTime;
        targetElement.setAttribute(TIME_ATTR, DefaultLogger.formatTime(totalTime));
        if (taskElement == null) {
            buildElement.appendChild(targetElement);
        } else {
            taskElement.appendChild(targetElement);
        }

        targetElement = null;

        if (!targetStack.isEmpty()) {
            targetStartTime = ((Long) targetTimeStack.pop()).longValue();
            targetElement = (Element) targetStack.pop();
        }
    }

    public void taskStarted(BuildEvent event) {
        if (taskElement != null) {
            taskTimeStack.push(new Long(taskStartTime));
            taskStack.push(taskElement);
        }

        taskStartTime = System.currentTimeMillis();
        taskElement = doc.createElement(TASK_TAG);

        String name = event.getTask().getClass().getName();
        int pos = name.lastIndexOf(".");
        if (pos != -1) {
            name = name.substring(pos + 1);
        }
        taskElement.setAttribute(NAME_ATTR, name);

        taskElement.setAttribute(LOCATION_ATTR, event.getTask().getLocation().toString());
    }

    public void taskFinished(BuildEvent event) {
        long totalTime = System.currentTimeMillis() - taskStartTime;
        taskElement.setAttribute(TIME_ATTR, DefaultLogger.formatTime(totalTime));
        targetElement.appendChild(taskElement);

        taskElement = null;
        if (!taskStack.isEmpty()) {
            taskStartTime = ((Long) taskTimeStack.pop()).longValue();
            taskElement = (Element) taskStack.pop();
        }
    }

    public void messageLogged(BuildEvent event) {
        Element messageElement = doc.createElement(MESSAGE_TAG);

        String name = "debug";
        switch(event.getPriority()) {
            case Project.MSG_ERR: name = "error"; break;
            case Project.MSG_WARN: name = "warn"; break;
            case Project.MSG_INFO: name = "info"; break;
            default: name = "debug"; break;
        }
        messageElement.setAttribute(PRIORITY_ATTR, name);

        Text messageText = doc.createTextNode(event.getMessage());
        messageElement.appendChild(messageText);

        if (taskElement != null) {
            taskElement.appendChild(messageElement);
        }
        else if (targetElement != null) {
            targetElement.appendChild(messageElement);
        }
        else {
            buildElement.appendChild(messageElement);
        }
    }

}
