package org.apache.synapse.util.xpath;

/**
 * All the constants that has been used for the SynapseXPath
 */
public final class SynapseXPathConstants {

    /** Get-Property XPath extension function name */
    public static final String GET_PROPERTY_FUNCTION = "get-property";

    /** Body relative XPath variale name for the SOAPBody */
    public static final String SOAP_BODY_VARIABLE = "body";

    /** Header relative XPath variable name for the SOAPHeader */
    public static final String SOAP_HEADER_VARIABLE = "header";

    /** Variable prefix for accessing the MessageContext properties through XPath variables */
    public static final String MESSAGE_CONTEXT_VARIABLE_PREFIX = "ctx";

    /** Variable prefix for accessing the axis2 MessageContext properties through XPath variables */
    public static final String AXIS2_CONTEXT_VARIABLE_PREFIX = "axis2";

    /** Variable prefix for accessing transport headers of the message through XPath variables */
    public static final String TRANSPORT_VARIABLE_PREFIX = "trp";
}
