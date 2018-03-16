package org.apache.camel.spi;

import java.io.IOException;
import java.io.InputStream;

/**
 * @version $Revision: 520124 $
 */
public interface Unmarshaller {
	
	/**
     * Unmarshals the given stream into an object.
     */
    Object unmarshal(InputStream stream) throws IOException;

}
