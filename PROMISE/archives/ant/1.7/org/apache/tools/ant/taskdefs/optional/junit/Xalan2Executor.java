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
 * This class is not used by the framework any more.
 * We plan to remove it in Ant 1.8
 * @deprecated since Ant 1.7
 *
 *
 * @ant.task ignore="true"
 */
public class Xalan2Executor extends XalanExecutor {

    private static final String APAC = "org.apache.xalan.";
    private static final String SPAC = "com.sun.org.apache.xalan.";

    private TransformerFactory tfactory = TransformerFactory.newInstance();

    /** {@inheritDoc}. */
    protected String getImplementation() throws BuildException {
        return tfactory.getClass().getName();
    }

    /** {@inheritDoc}. */
    protected String getProcVersion(String classNameImpl)
        throws BuildException {
        try {
            if (classNameImpl.equals(APAC + "processor.TransformerFactoryImpl")
                ||
                classNameImpl.equals(APAC + "xslt.XSLTProcessorFactory")) {
                return getXalanVersion(APAC + "processor.XSLProcessorVersion");
            }
            if (classNameImpl.equals(APAC
                                     + "xsltc.trax.TransformerFactoryImpl")) {
                return getXSLTCVersion(APAC + "xsltc.ProcessorVersion");
            }
            if (classNameImpl
                .equals(SPAC + "internal.xsltc.trax.TransformerFactoryImpl")) {
                return getXSLTCVersion(SPAC
                                       + "internal.xsltc.ProcessorVersion");
            }
            throw new BuildException("Could not find a valid processor version"
                                     + " implementation from "
                                     + classNameImpl);
        } catch (ClassNotFoundException e) {
            throw new BuildException("Could not find processor version "
                                     + "implementation", e);
        }
    }

    /** {@inheritDoc}. */
    void execute() throws Exception {
        String systemId = caller.getStylesheetSystemId();
        Source xslSrc = new StreamSource(systemId);
        Transformer tformer = tfactory.newTransformer(xslSrc);
        Source xmlSrc = new DOMSource(caller.document);
        OutputStream os = getOutputStream();
        try {
            tformer.setParameter("output.dir", caller.toDir.getAbsolutePath());
            Result result = new StreamResult(os);
            tformer.transform(xmlSrc, result);
        } finally {
            os.close();
        }
    }
}
