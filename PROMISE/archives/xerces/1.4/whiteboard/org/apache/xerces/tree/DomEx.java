package org.apache.xerces.tree;

import java.util.Locale;

import org.w3c.dom.DOMException;


/**
 * Concrete class for DOM exceptions, associating standard messages
 * with DOM error codes.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
class DomEx extends DOMException
{
    static String messageString (Locale locale, int code)
    {
	switch (code) {
	  case INDEX_SIZE_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-000");
	  case DOMSTRING_SIZE_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-001");
	  case HIERARCHY_REQUEST_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-002");
	  case WRONG_DOCUMENT_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-003");
	  case INVALID_CHARACTER_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-004");
	  case NO_DATA_ALLOWED_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-005");
	  case NO_MODIFICATION_ALLOWED_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-006");
	  case NOT_FOUND_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-007");
	  case NOT_SUPPORTED_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-008");
	  case INUSE_ATTRIBUTE_ERR:
	    return XmlDocument.catalog.getMessage (locale, "D-009");
	  default:
	    return XmlDocument.catalog.getMessage (locale, "D-010");
	}
    }

    /**
     * Creates a DOM exception which provides a standard message
     * corresponding to the given error code, using the default
     * locale for the message.
     */
    public DomEx (short code)
    {
	super (code, messageString (Locale.getDefault (), code));
    }

    /**
     * Creates a DOM exception which provides a standard message
     * corresponding to the given error code and using the specified
     * locale for the message.
     */
    public DomEx (Locale locale, short code)
    {
	super (code, messageString (locale, code));
    }
}
