package org.apache.camel.builder.xml;

import org.apache.camel.Message;

import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;

/**
 * Uses DOM to handle results of the transformation
 * 
 * @version $Revision: 1.1 $
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
