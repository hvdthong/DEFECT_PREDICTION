package org.apache.tools.ant.taskdefs.optional;

import com.kvisco.xsl.XSLProcessor;
import com.kvisco.xsl.XSLReader;
import com.kvisco.xsl.XSLStylesheet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import org.apache.tools.ant.taskdefs.XSLTLiaison;

/**
 * Concrete liaison for XSLP
 *
 * @since Ant 1.1
 */
public class XslpLiaison implements XSLTLiaison {

    protected XSLProcessor processor;
    protected XSLStylesheet xslSheet;

    public XslpLiaison() {
        processor = new XSLProcessor();
        processor.getProperty("dummy-to-init-properties-map");
    }

    public void setStylesheet(File fileName) throws Exception {
        XSLReader xslReader = new XSLReader();
        xslSheet = xslReader.read(fileName.getAbsolutePath());
    }

    public void transform(File infile, File outfile) throws Exception {
        FileOutputStream fos = new FileOutputStream(outfile);
        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF8");
        processor.process(infile.getAbsolutePath(), xslSheet, out);
    }

    public void addParam(String name, String expression) {
        processor.setProperty(name, expression);
    }

