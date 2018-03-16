package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Xalan executor via JAXP. Nothing special must exists in the classpath
 * besides of course, a parser, jaxp and xalan.
 *
 * @ant.task ignore="true"
 */
public class Xalan2Executor extends XalanExecutor {
    void execute() throws Exception {
        TransformerFactory tfactory = TransformerFactory.newInstance();
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
