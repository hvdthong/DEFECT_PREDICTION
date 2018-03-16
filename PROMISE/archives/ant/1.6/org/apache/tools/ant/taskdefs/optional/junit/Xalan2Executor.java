package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.OutputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.tools.ant.BuildException;

/**
 * Xalan executor via JAXP. Nothing special must exists in the classpath
 * besides of course, a parser, jaxp and xalan.
 *
 * @ant.task ignore="true"
 */
public class Xalan2Executor extends XalanExecutor {

    private static final String aPack = "org.apache.xalan.";
    private static final String sPack = "com.sun.org.apache.xalan.";

    private TransformerFactory tfactory = TransformerFactory.newInstance();

    protected String getImplementation() throws BuildException {
        return tfactory.getClass().getName();
    }

    protected String getProcVersion(String classNameImpl) 
        throws BuildException {
        try {
            if (classNameImpl.equals(aPack + "processor.TransformerFactoryImpl") 
                ||
                classNameImpl.equals(aPack + "xslt.XSLTProcessorFactory")) {
                return getXalanVersion(aPack + "processor.XSLProcessorVersion");
            }
            if (classNameImpl.equals(aPack 
                                     + "xsltc.trax.TransformerFactoryImpl")){
                return getXSLTCVersion(aPack +"xsltc.ProcessorVersion");
            }
            if (classNameImpl
                .equals(sPack + "internal.xsltc.trax.TransformerFactoryImpl")){
                return getXSLTCVersion(sPack 
                                       + "internal.xsltc.ProcessorVersion");
            }
            throw new BuildException("Could not find a valid processor version"
                                     + " implementation from " 
                                     + classNameImpl);
        } catch (ClassNotFoundException e){
            throw new BuildException("Could not find processor version "
                                     + "implementation", e);
        }
    }

    void execute() throws Exception {
        String system_id = caller.getStylesheetSystemId();
        Source xsl_src = new StreamSource(system_id);
        Transformer tformer = tfactory.newTransformer(xsl_src);
        Source xml_src = new DOMSource(caller.document);
        OutputStream os = getOutputStream();
        try {
            tformer.setParameter("output.dir", caller.toDir.getAbsolutePath());
            Result result = new StreamResult(os);
            tformer.transform(xml_src, result);
        } finally {
            os.close();
        }
    }
}
