package org.apache.tools.ant.taskdefs.optional;

import org.apache.tools.ant.taskdefs.XSLTLiaison;

import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Concrete liaison for Xalan 1.x API.
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 */
public class XalanLiaison implements XSLTLiaison {

    protected XSLTProcessor processor;
    protected File stylesheet;

    public XalanLiaison() throws Exception {
      processor = XSLTProcessorFactory.getProcessor();
    }

    public void setStylesheet(File stylesheet) throws Exception {
        this.stylesheet = stylesheet;
    }

    public void transform(File infile, File outfile) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileInputStream xslStream = null;
        try {
            xslStream = new FileInputStream(stylesheet);
            fis = new FileInputStream(infile);
            fos = new FileOutputStream(outfile);
            XSLTInputSource xslSheet = new XSLTInputSource(xslStream);
            xslSheet.setSystemId(stylesheet.getAbsolutePath());
            XSLTInputSource src = new XSLTInputSource(fis);
            src.setSystemId(infile.getAbsolutePath());
            XSLTResultTarget res = new XSLTResultTarget(fos);
            processor.process(src, xslSheet, res);
        } finally {
            try {
                if (xslStream != null){
                    xslStream.close();
                }
            } catch (IOException ignored){}
            try {
                if (fis != null){
                    fis.close();
                }
            } catch (IOException ignored){}
            try {
                if (fos != null){
                    fos.close();
                }
            } catch (IOException ignored){}
        }
    }

    public void addParam(String name, String value){
        processor.setStylesheetParam(name, value);
    }
