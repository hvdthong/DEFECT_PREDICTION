package org.apache.camel.builder;

import org.apache.camel.RuntimeCamelException;

/**
 * @version $Revision: 520306 $
 */
public class UndefinedDestinationException extends RuntimeCamelException {
	private static final long serialVersionUID = -5980888207885995222L;

	public UndefinedDestinationException() {
        super("No destination defined for this routing rule");
    }
}
