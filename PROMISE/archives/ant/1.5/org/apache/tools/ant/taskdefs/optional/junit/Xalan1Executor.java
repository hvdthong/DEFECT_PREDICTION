package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.OutputStream;

import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;

/**
 * Xalan 1 executor. It will need a lot of things in the classpath:
 * xerces for the serialization, xalan and bsf for the extension.
 * @todo do everything via reflection to avoid compile problems ?
 *
 * @ant.task ignore="true"
 */
public class Xalan1Executor extends XalanExecutor {
    void execute() throws Exception {
        XSLTProcessor processor = XSLTProcessorFactory.getProcessor();
        processor.setStylesheetParam("output.dir", "'" + caller.toDir.getAbsolutePath() + "'");
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
