package org.apache.camel.builder.xml;

import org.apache.camel.Message;

import javax.xml.transform.Result;

/**
 * A strategy for handling XSLT results
 *
 * @version $Revision: 1.1 $
 */
public interface ResultHandler {
    Result getResult();

    void setBody(Message in);
}
