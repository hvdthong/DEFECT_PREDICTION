package org.apache.xalan.xsltc.runtime;

import java.util.Vector;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class ErrorMessages extends ResourceBundle {

    private static final String errorMessages[] = {
	"Run-time internal error in ''{0}''",
	"Run-time error when executing <xsl:copy>.",
	"Invalid conversion from ''{0}'' to ''{1}''.",
	"External function ''{0}'' not supported by XSLTC.",
	"Unknown argument type in equality expression.",
	"Invalid argument type ''{0}'' in call to ''{1}''",
	"Attempting to format number ''{0}'' using pattern ''{1}''.",
	"Cannot clone iterator ''{0}''.",
	"Iterator for axis ''{0}'' not supported.",
	"Iterator for typed axis ''{0}'' not supported.",
	"Attribute ''{0}'' outside of element.",
	"Namespace declaration ''{0}''=''{1}'' outside of element.",
	"Namespace for prefix ''{0}'' has not been declared.",
	"DOMAdapter created using wrong type of source DOM."
    };

    private static Vector _keys;

    static {
	_keys = new Vector();
	_keys.addElement(BasisLibrary.ERROR_MESSAGES_KEY);
    }

    public Enumeration getKeys() {
	return _keys.elements();
    }

    public Object handleGetObject(String key) {
	if (key == null) return null;
	if (key.equals(BasisLibrary.ERROR_MESSAGES_KEY)) return errorMessages;
	return(null);
    }

}
