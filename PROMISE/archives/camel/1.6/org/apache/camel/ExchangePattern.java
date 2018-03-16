package org.apache.camel;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents the kind of message exchange pattern
 *
 * @version $Revision: 725598 $
 */
@XmlType
@XmlEnum(String.class)
public enum ExchangePattern {
    InOnly, RobustInOnly, InOut, InOptionalOut, OutOnly, RobustOutOnly, OutIn, OutOptionalIn;

    protected static final Map<String, ExchangePattern> MAP = new HashMap<String, ExchangePattern>();

    /**
     * Returns the WSDL URI for this message exchange pattern
     */
    public String getWsdlUri() {
        switch (this) {
        case InOnly:
        case InOptionalOut:
        case InOut:
        case OutIn:
        case OutOnly:
        case OutOptionalIn:
        case RobustInOnly:
        case RobustOutOnly:
        default:
            throw new IllegalArgumentException("Unknown message exchange pattern: " + this);
        }
    }

    /**
     * Return true if there can be an IN message
     */
    public boolean isInCapable() {
        switch (this) {
        case OutOnly:
        case RobustOutOnly:
            return false;
        default:
            return true;
        }
    }

    /**
     * Return true if there can be an OUT message
     */
    public boolean isOutCapable() {
        switch (this) {
        case InOnly:
        case RobustInOnly:
            return false;
        default:
            return true;
        }
    }

    /**
     * Return true if there can be a FAULT message
     */
    public boolean isFaultCapable() {
        switch (this) {
        case InOnly:
        case OutOnly:
            return false;
        default:
            return true;
        }
    }

    /**
     * Converts the WSDL URI into a {@link ExchangePattern} instance
     */
    public static ExchangePattern fromWsdlUri(String wsdlUri) {
        return MAP.get(wsdlUri);
    }
    
    public static ExchangePattern asEnum(String value) {
        try {
            return valueOf(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown message exchange pattern: " + value);
        }
    }

    static {
        for (ExchangePattern mep : values()) {
            String uri = mep.getWsdlUri();
            MAP.put(uri, mep);
            String name = uri.substring(uri.lastIndexOf('/') + 1);
        }
    }
}
