package org.apache.camel.builder.xml;

import org.apache.camel.Message;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;
import java.io.StringWriter;

/**
 * Processes the XSLT result as a String
 *
 * @version $Revision: 1.1 $
 */
public class StringResultHandler implements ResultHandler {
    StringWriter buffer = new StringWriter();
    StreamResult result = new StreamResult(buffer);

    public Result getResult() {
        return result;
    }

    public void setBody(Message in) {
        in.setBody(buffer.toString());
    }
}
