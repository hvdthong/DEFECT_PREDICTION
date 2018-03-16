package org.apache.tools.ant.taskdefs.optional;

import java.io.*;
import java.net.URL;

import org.apache.tools.ant.taskdefs.XSLTLiaison;
import org.xml.sax.InputSource;

import com.kvisco.xsl.*;

/**
 * Concrete liaison for XSLP
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
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
      xslSheet = xslReader.read( fileName.getAbsolutePath() );
    }

    public void transform(File infile, File outfile) throws Exception {
        FileOutputStream fos = new FileOutputStream(outfile);
        OutputStreamWriter out = new OutputStreamWriter(fos,"UTF8");
        processor.process(infile.getAbsolutePath(), xslSheet, out);
    }

    public void addParam(String name, String expression){
      processor.setProperty(name, expression);
    }

