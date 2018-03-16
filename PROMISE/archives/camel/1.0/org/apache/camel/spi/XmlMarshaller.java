package org.apache.camel.spi;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * Marshallers that marshall to XML should extend this base class.
 *
 * @version $Revision: 520124 $
 */
public abstract class XmlMarshaller implements Marshaller{
	
	/**
     * Marshals the object to the given Stream.
     */
	public void marshal(Object object, OutputStream result) throws IOException {
		marshal(object, new StreamResult(result));
	}

	abstract public void marshal(Object object, Result result);
}
