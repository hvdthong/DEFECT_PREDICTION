package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.DOMElementWriter;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Prints XML output of the test to a specified Writer.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:erik@hatcher.net">Erik Hatcher</a>
 *
 * @see FormatterElement 
 */

public class XMLJUnitResultFormatter implements JUnitResultFormatter, XMLConstants {

    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch(Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    /**
     * The XML document.
     */
    private Document doc;
    /**
     * The wrapper for the whole testsuite.
     */
    private Element rootElement;
    /**
     * Element for the current test.
     */
    private Element currentTest;
    /**
     * Timing helper.
     */
    private long lastTestStart = 0;
    /**
     * Where to write the log to.
     */
    private OutputStream out;

    public XMLJUnitResultFormatter() {}

    public void setOutput(OutputStream out) {
        this.out = out;
    }

    public void setSystemOutput(String out) {
        formatOutput(SYSTEM_OUT, out);
    }

    public void setSystemError(String out) {
        formatOutput(SYSTEM_ERR, out);
    }

    /**
     * The whole testsuite started.
     */
    public void startTestSuite(JUnitTest suite) {
        doc = getDocumentBuilder().newDocument();
        rootElement = doc.createElement(TESTSUITE);
        rootElement.setAttribute(ATTR_NAME, suite.getName());

        Element propsElement = doc.createElement(PROPERTIES);
        rootElement.appendChild(propsElement);
        Properties props = suite.getProperties();
        if (props != null) {
            Enumeration e = props.propertyNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                Element propElement = doc.createElement(PROPERTY);
                propElement.setAttribute(ATTR_NAME, name);
                propElement.setAttribute(ATTR_VALUE, props.getProperty(name));
                propsElement.appendChild(propElement);
            }
        }
    }

    /**
     * The whole testsuite ended.
     */
    public void endTestSuite(JUnitTest suite) throws BuildException {
        rootElement.setAttribute(ATTR_TESTS, ""+suite.runCount());
        rootElement.setAttribute(ATTR_FAILURES, ""+suite.failureCount());
        rootElement.setAttribute(ATTR_ERRORS, ""+suite.errorCount());
        rootElement.setAttribute(ATTR_TIME, ""+(suite.getRunTime()/1000.0));
        if (out != null) {
            Writer wri = null;
            try {
                wri = new OutputStreamWriter(out, "UTF8");
                wri.write("<?xml version=\"1.0\"?>\n");
                (new DOMElementWriter()).write(rootElement, wri, 0, "  ");
                wri.flush();
            } catch(IOException exc) {
                throw new BuildException("Unable to write log file", exc);
            } finally {
                if (out != System.out && out != System.err) {
                    if (wri != null) {
                        try {
                            wri.close();
                        } catch (IOException e) {}
                    }
                }
            }
        }
    }

    /**
     * Interface TestListener.
     *
     * <p>A new Test is started.
     */
    public void startTest(Test t) {
        lastTestStart = System.currentTimeMillis();
        currentTest = doc.createElement(TESTCASE);
        currentTest.setAttribute(ATTR_NAME, ((TestCase) t).name());
        rootElement.appendChild(currentTest);
    }

    /**
     * Interface TestListener.
     *
     * <p>A Test is finished.
     */
    public void endTest(Test test) {
        currentTest.setAttribute(ATTR_TIME,
                                 ""+((System.currentTimeMillis()-lastTestStart)
                                           / 1000.0));
    }

    /**
     * Interface TestListener for JUnit &lt;= 3.4.
     *
     * <p>A Test failed.
     */
    public void addFailure(Test test, Throwable t) {
        formatError(FAILURE, test, t);
    }

    /**
     * Interface TestListener for JUnit &gt; 3.4.
     *
     * <p>A Test failed.
     */
    public void addFailure(Test test, AssertionFailedError t) {
        addFailure(test, (Throwable) t);
    }

    /**
     * Interface TestListener.
     *
     * <p>An error occured while running the test.
     */
    public void addError(Test test, Throwable t) {
        formatError(ERROR, test, t);
    }

    private void formatError(String type, Test test, Throwable t) {
        if (test != null) {
            endTest(test);
        }

        Element nested = doc.createElement(type);
        if (test != null) {
            currentTest.appendChild(nested);
        } else {
            rootElement.appendChild(nested);
        }

        String message = t.getMessage();
        if (message != null && message.length() > 0) {
            nested.setAttribute(ATTR_MESSAGE, t.getMessage());
        }
        nested.setAttribute(ATTR_TYPE, t.getClass().getName());

        StringWriter swr = new StringWriter();
        t.printStackTrace(new PrintWriter(swr, true));
        Text trace = doc.createTextNode(swr.toString());
        nested.appendChild(trace);
    }

    private void formatOutput(String type, String output) {
        Element nested = doc.createElement(type);
        rootElement.appendChild(nested);
        Text content = doc.createTextNode(output);
        nested.appendChild(content);
    }

