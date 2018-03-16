package org.apache.camel.spi;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @version $Revision: 520124 $
 */
public interface Marshaller {
	
	/**
     * Marshals the object to the given Stream.
     */
	void marshal(Object graph, OutputStream stream) throws IOException;
}
