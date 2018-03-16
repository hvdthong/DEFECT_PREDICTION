package org.apache.tools.ant.taskdefs.optional.metamata;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.util.DateUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A handy metrics handler. Most of this code was done only with the
 * screenshots on the documentation since the evaluation version as
 * of this writing does not allow to save metrics or to run it via
 * command line.
 * <p>
 * This class can be used to transform a text file or to process the
 * output stream directly.
 *
 */
public class MMetricsStreamHandler implements ExecuteStreamHandler {

    /** CLASS construct, it should be named something like 'MyClass' */
    private static final String CLASS = "class";

    /** package construct, it should be look like 'com.mycompany.something' */
    private static final String PACKAGE = "package";

    /** FILE construct, it should look like something 'MyClass.java' or 'MyClass.class' */
    private static final String FILE = "file";

    /** METHOD construct, it should looke like something 'doSomething(...)' or 'doSomething()' */
    private static final String METHOD = "method";

    private static final String[] ATTRIBUTES = {
        "name", "vg", "loc", "dit", "noa", "nrm", "nlm", "wmc",
        "rfc", "dac", "fanout", "cbo", "lcom", "nocl"};

    /** reader for stdout */
    private InputStream metricsOutput;

    /**
     * this is where the XML output will go, should mostly be a file
     * the caller is responsible for flushing and closing this stream
     */
    private OutputStream xmlOutputStream;

    /** metrics handler */
    private TransformerHandler metricsHandler;

    /** the task */
    private Task task;

    /**
     * the stack where are stored the metrics element so that they we can
     * know if we have to close an element or not.
     */
    private Stack stack = new Stack();

    /** initialize this handler */
    MMetricsStreamHandler(Task task, OutputStream xmlOut) {
        this.task = task;
        this.xmlOutputStream = xmlOut;
    }

    /** Ignore. */
    public void setProcessInputStream(OutputStream p1) throws IOException {
    }

    /** Ignore. */
    public void setProcessErrorStream(InputStream p1) throws IOException {
    }

    /** Set the inputstream */
    public void setProcessOutputStream(InputStream is) throws IOException {
        metricsOutput = is;
    }

    public void start() throws IOException {
        TransformerFactory factory = TransformerFactory.newInstance();
        if (!factory.getFeature(SAXTransformerFactory.FEATURE)) {
            throw new IllegalStateException("Invalid Transformer factory feature");
        }
        try {
            metricsHandler = ((SAXTransformerFactory) factory).newTransformerHandler();
            metricsHandler.setResult(new StreamResult(new OutputStreamWriter(xmlOutputStream, "UTF-8")));
            Transformer transformer = metricsHandler.getTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            final Date now = new Date();
            metricsHandler.startDocument();
            AttributesImpl attr = new AttributesImpl();
            attr.addAttribute("", "company", "company", "CDATA", "metamata");
            attr.addAttribute("", "snapshot_created", "snapshot_created", "CDATA",
                    DateUtils.format(now, DateUtils.ISO8601_DATETIME_PATTERN));
            attr.addAttribute("", "program_start", "program_start", "CDATA",
                    DateUtils.format(new Date(), DateUtils.ISO8601_DATETIME_PATTERN));
            metricsHandler.startElement("", "metrics", "metrics", attr);

            parseOutput();

        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    /**
     * Pretty dangerous business here.
     */
    public void stop() {
        try {
            while (stack.size() > 0) {
                ElementEntry elem = (ElementEntry) stack.pop();
                metricsHandler.endElement("", elem.getType(), elem.getType());
            }
            metricsHandler.endElement("", "metrics", "metrics");
            metricsHandler.endDocument();
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    /** read each line and process it */
    protected void parseOutput() throws IOException, SAXException {
        BufferedReader br = new BufferedReader(new InputStreamReader(metricsOutput));
        String line = null;
        while ((line = br.readLine()) != null) {
            processLine(line);
        }
    }

    /**
     * Process a metrics line. If the metrics is invalid and that this is not
     * the header line, it is display as info.
     * @param line the line to process, it is normally a line full of metrics.
     */
    protected void processLine(String line) throws SAXException {
        if (line.startsWith("Construct\tV(G)\tLOC\tDIT\tNOA\tNRM\tNLM\tWMC\tRFC\tDAC\tFANOUT\tCBO\tLCOM\tNOCL")) {
            return;
        }
        try {
            MetricsElement elem = MetricsElement.parse(line);
            startElement(elem);
        } catch (ParseException e) {
            task.log(line, Project.MSG_INFO);
        }
    }

    /**
     * Start a new construct. Elements are popped until we are on the same
     * parent node, then the element type is guessed and pushed on the
     * stack.
     * @param elem the element to process.
     * @throws SAXException thrown if there is a problem when sending SAX events.
     */
    protected void startElement(MetricsElement elem) throws SAXException {
        int indent = elem.getIndent();
        if (stack.size() > 0) {
            ElementEntry previous = (ElementEntry) stack.peek();
            try {
                while (indent <= previous.getIndent() && stack.size() > 0) {
                    stack.pop();
                    metricsHandler.endElement("", previous.getType(), previous.getType());
                    previous = (ElementEntry) stack.peek();
                }
            } catch (EmptyStackException ignored) {
            }
        }

        String type = getConstructType(elem);
        Attributes attrs = createAttributes(elem);
        metricsHandler.startElement("", type, type, attrs);

        stack.push(new ElementEntry(type, indent));
    }

    /**
     * return the construct type of the element. We can hardly recognize the
     * type of a metrics element, so we are kind of forced to do some black
     * magic based on the name and indentation to recognize the type.
     * @param elem  the metrics element to guess for its type.
     * @return the type of the metrics element, either PACKAGE, FILE, CLASS or
     * METHOD.
     */
    protected String getConstructType(MetricsElement elem) {
        if (elem.isCompilationUnit()) {
            return FILE;
        }

        if (elem.isMethod()) {
            return METHOD;
        }

        if (stack.size() == 0) {
            return PACKAGE;
        }

        final ElementEntry previous = (ElementEntry) stack.peek();
        final String prevType = previous.getType();
        final int prevIndent = previous.getIndent();
        final int indent = elem.getIndent();
        if (prevType.equals(FILE) && indent > prevIndent) {
            return CLASS;
        }

        if (prevType.equals(CLASS) && indent >= prevIndent) {
            return CLASS;
        }

        return PACKAGE;
    }


    /**
     * Create all attributes of a MetricsElement skipping those who have an
     * empty string
     */
    protected Attributes createAttributes(MetricsElement elem) {
        AttributesImpl impl = new AttributesImpl();
        int i = 0;
        String name = ATTRIBUTES[i++];
        impl.addAttribute("", name, name, "CDATA", elem.getName());
        Enumeration metrics = elem.getMetrics();
        for (; metrics.hasMoreElements(); i++) {
            String value = (String) metrics.nextElement();
            if (value.length() > 0) {
                name = ATTRIBUTES[i];
                impl.addAttribute("", name, name, "CDATA", value);
            }
        }
        return impl;
    }

    /**
     * helper class to keep track of elements via its type and indent
     * that's all we need to guess a type.
     */
    private static final class ElementEntry {
        private String type;
        private int indent;

        ElementEntry(String type, int indent) {
            this.type = type;
            this.indent = indent;
        }

        public String getType() {
            return type;
        }

        public int getIndent() {
            return indent;
        }
    }
}

class MetricsElement {

    private static final NumberFormat METAMATA_NF;

    private static final NumberFormat NEUTRAL_NF;

    static {
        METAMATA_NF = NumberFormat.getInstance();
        METAMATA_NF.setMaximumFractionDigits(1);
        NEUTRAL_NF = NumberFormat.getInstance();
        if (NEUTRAL_NF instanceof DecimalFormat) {
            ((DecimalFormat) NEUTRAL_NF).applyPattern("###0.###;-###0.###");
        }
        NEUTRAL_NF.setMaximumFractionDigits(1);
    }

    private int indent;

    private String construct;

    private Vector metrics;

    MetricsElement(int indent, String construct, Vector metrics) {
        this.indent = indent;
        this.construct = construct;
        this.metrics = metrics;
    }

    public int getIndent() {
        return indent;
    }

    public String getName() {
        return construct;
    }

    public Enumeration getMetrics() {
        return metrics.elements();
    }

    public boolean isCompilationUnit() {
        return (construct.endsWith(".java") || construct.endsWith(".class"));
    }

    public boolean isMethod() {
        return (construct.endsWith("(...)") || construct.endsWith("()"));
    }

    public static MetricsElement parse(String line) throws ParseException {
        final Vector metrics = new Vector();
        int pos;

        while ((pos = line.indexOf('\t')) != -1) {
            String token = line.substring(0, pos);
            /*if (metrics.size() != 0 || token.length() != 0) {
            }*/
            metrics.addElement(token);
            line = line.substring(pos + 1);
        }
        metrics.addElement(line);

        if (metrics.size() != 14) {
            throw new ParseException("Could not parse the following line as "
                + "a metrics: -->" + line + "<--", -1);
        }

        String name = (String) metrics.elementAt(0);
        metrics.removeElementAt(0);
        int indent = 0;
        pos = name.lastIndexOf('/');
        if (pos != -1) {
            name = name.substring(pos + 1);
        }
        return new MetricsElement(indent, name, metrics);
    }
}

