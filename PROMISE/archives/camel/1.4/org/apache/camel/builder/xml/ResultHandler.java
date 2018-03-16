package org.apache.camel.builder.xml;

import javax.xml.transform.Result;

import org.apache.camel.Message;

/**
 * A strategy for handling XSLT results
 *
 * @version $Revision: 660275 $
 */
public interface ResultHandler {

    Result getResult();

    void setBody(Message in);
}
