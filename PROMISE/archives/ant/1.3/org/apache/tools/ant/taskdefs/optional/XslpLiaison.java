package org.apache.tools.ant.taskdefs.optional;

import java.io.FileWriter;

import org.apache.tools.ant.taskdefs.XSLTLiaison;

import com.kvisco.xsl.XSLProcessor;
import com.kvisco.xsl.XSLReader;
import com.kvisco.xsl.XSLStylesheet;

/**
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @version $Revision: 268392 $ $Date: 2001-01-03 22:18:52 +0800 (周三, 2001-01-03) $
 */
public class XslpLiaison implements XSLTLiaison {

    XSLProcessor processor;
    XSLStylesheet xslSheet;

    public XslpLiaison() {
      processor = new XSLProcessor();
    }

    public void setStylesheet(String fileName) throws Exception {
      XSLReader xslReader = new XSLReader();
      xslSheet = xslReader.read( fileName );
    };

    public void transform(String infile, String outfile) throws Exception {
      processor.process(infile, xslSheet, new FileWriter(outfile));
    }

    public void addParam(String name, String expression){
      processor.setProperty(name, expression);
    }

