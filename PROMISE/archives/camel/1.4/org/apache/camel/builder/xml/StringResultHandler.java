package org.apache.camel.builder.xml;

import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Message;

/**
 * Processes the XSLT result as a String
 *
 * @version $Revision: 630591 $
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
