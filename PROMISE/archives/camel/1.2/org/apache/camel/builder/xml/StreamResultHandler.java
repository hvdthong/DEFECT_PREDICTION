package org.apache.camel.builder.xml;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Message;

/**
 * Processes the XSLT result as a byte[]
 *
 * @version $Revision: 1.1 $
 */
public class StreamResultHandler implements ResultHandler {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    StreamResult result = new StreamResult(buffer);

    public Result getResult() {
        return result;
    }

    public void setBody(Message in) {
        in.setBody(buffer.toByteArray());
    }
}
