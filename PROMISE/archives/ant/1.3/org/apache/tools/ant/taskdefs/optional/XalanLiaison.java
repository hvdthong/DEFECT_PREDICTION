package org.apache.tools.ant.taskdefs.optional;

import org.apache.tools.ant.taskdefs.XSLTLiaison;

import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;

/**
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @version $Revision: 268392 $ $Date: 2001-01-03 22:18:52 +0800 (周三, 2001-01-03) $
 */
public class XalanLiaison implements XSLTLiaison {

    protected final static String FILEURL = "file:";

    XSLTProcessor processor;
    XSLTInputSource xslSheet;

    public XalanLiaison() throws Exception {
      processor = XSLTProcessorFactory.getProcessor();
    }

    public void setStylesheet(String fileName) throws Exception {
        xslSheet = new XSLTInputSource (normalize(fileName));
    };

    public void transform(String infile, String outfile) throws Exception {
        processor.process(new XSLTInputSource(normalize(infile)), xslSheet,
                        new XSLTResultTarget(outfile));
    }

    protected String normalize(String fileName) {
        if(fileName != null && !fileName.startsWith(FILEURL)) {
            return FILEURL + fileName;
        }
        return fileName;
    }
    
    public void addParam(String name, String value){
        processor.setStylesheetParam(name, value);
    }
