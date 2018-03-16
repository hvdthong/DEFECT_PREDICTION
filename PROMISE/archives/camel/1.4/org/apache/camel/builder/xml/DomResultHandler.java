package org.apache.camel.builder.xml;

import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;

import org.apache.camel.Message;

/**
 * Uses DOM to handle results of the transformation
 *
 * @version $Revision: 630591 $
 */
public class DomResultHandler implements ResultHandler {
    private DOMResult result = new DOMResult();

    public Result getResult() {
        return result;
    }

    public void setBody(Message in) {
        in.setBody(result.getNode());
    }
}
