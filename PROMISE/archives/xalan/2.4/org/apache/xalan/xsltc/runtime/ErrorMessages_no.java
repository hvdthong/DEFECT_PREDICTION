package org.apache.xalan.xsltc.runtime;

import java.util.Vector;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class ErrorMessages_no extends ErrorMessages {

    private static final String errorMessages[] = {
	"Intern programfeil i ''{0}''",
	"Programfeil under utf\u00f8ing av <xsl:copy>.",
	"Ugyldig konvertering av ''{0}'' fra ''{1}''.",
	"Ekstern funksjon ''{0}'' er ikke st\u00f8ttet av XSLTC.",
	"Ugyldig argument i EQUALITY uttrykk.",
	"Ugyldig argument ''{0}'' i kall til ''{1}''",
	"Fors\u00f8k p\u00e5 \u00e5 formattere nummer ''{0}'' med ''{1}''.",
	"Kan ikke klone iterator ''{0}''.",
	"Iterator for axis ''{0}'' er ikke st\u00e8ttet.",
	"Iterator for typet axis ''{0}'' er ikke st\u00e8ttet.",
	"Attributt ''{0}'' utenfor element.",
	"Navnedeklarasjon ''{0}''=''{1}'' utenfor element.",
	"Prefix ''{0}'' er ikke deklartert.",
	"Fors\u00f8k p\u00e5 \u00e5 instansiere DOMAdapter med feil type DOM."
    };

    public Object handleGetObject(String key) {
	if (key == null) return null;
	if (key.equals(BasisLibrary.ERROR_MESSAGES_KEY)) return errorMessages;
	return(null);
    }

}
