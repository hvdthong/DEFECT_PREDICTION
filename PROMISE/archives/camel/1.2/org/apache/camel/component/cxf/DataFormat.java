package org.apache.camel.component.cxf;

/**
 * The data format the user expoects to see at the camel cxf components.  It can be
 * configured as a property (DataFormat) in the Camel cxf endpoint.
 */
public enum DataFormat {

    /**
     * PAYLOAD is the message payload of the message after message configured in
     * the CXF endpoint is applied.  Streaming and non-streaming are both
     * supported.
     */
    PAYLOAD,

    /**
     * MESSAGE is the raw message that is received from the transport layer.
     * Streaming and non-streaming are both supported.
     */
    MESSAGE,

    /**
     * POJOs (Plain old Java objects) are the Java parameters to the method
     * it is invoking on the target server.  The "serviceClass" property
     * must be included in the endpoint.  Streaming is not available for this
     * data format.
     */
    POJO
}
