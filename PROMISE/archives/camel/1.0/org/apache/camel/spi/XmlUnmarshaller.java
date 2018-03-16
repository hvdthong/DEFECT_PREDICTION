package org.apache.camel.spi;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/**
 * Unmarshallers that unmarshall to XML should extend this base class.
 * 
 * @version $Revision: 520124 $
 */
public abstract class XmlUnmarshaller implements Unmarshaller {
	
	/**
     * Unmarshals the given stream into an object.
     */
    public Object unmarshal(InputStream stream) throws IOException {
    	return unmarshal(new StreamSource(stream));
    }

	/**
     * Unmarshals the given stream into an object.
     */
    abstract public Object unmarshal(Source stream) throws IOException;
    
}
