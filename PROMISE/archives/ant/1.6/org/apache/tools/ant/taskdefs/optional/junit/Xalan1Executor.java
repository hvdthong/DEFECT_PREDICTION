package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.OutputStream;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.tools.ant.BuildException;
import org.xml.sax.SAXException;

/**
 * Xalan 1 executor. It will need a lot of things in the classpath:
 * xerces for the serialization, xalan and bsf for the extension.
 * @todo do everything via reflection to avoid compile problems ?
 *
 * @ant.task ignore="true"
 */
public class Xalan1Executor extends XalanExecutor {

    private static final String xsltP = "org.apache.xalan.xslt.XSLTProcessor";

    XSLTProcessor processor = null;
    public Xalan1Executor() {
        try {
            processor = XSLTProcessorFactory.getProcessor();
        } catch (SAXException e) {
        }
    }
    protected String getImplementation() {
        return processor.getClass().getName();
    }

    protected String getProcVersion(String classNameImpl) 
        throws BuildException {
        try {
            if (classNameImpl.equals(xsltP)){
                return getXalanVersion(xsltP + "Version");
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
        processor.setStylesheetParam("output.dir", "'" 
                                     + caller.toDir.getAbsolutePath() + "'");
        XSLTInputSource xml_src = new XSLTInputSource(caller.document);
        String system_id = caller.getStylesheetSystemId();
        XSLTInputSource xsl_src = new XSLTInputSource(system_id);
        OutputStream os = getOutputStream();
        try {
            XSLTResultTarget target = new XSLTResultTarget(os);
            processor.process(xml_src, xsl_src, target);
        } finally {
            os.close();
        }
    }
}
