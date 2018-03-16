package org.apache.camel.builder.xml;

import org.apache.camel.Message;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;

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
