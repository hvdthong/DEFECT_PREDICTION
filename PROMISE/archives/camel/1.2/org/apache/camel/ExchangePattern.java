package org.apache.camel;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the kind of message exchange pattern
 *
 * @version $Revision: 1.1 $
 */
public enum ExchangePattern {
    InOnly,
    RobustInOnly,
    InOut,
    InOptionalOut,
    OutOnly,
    RobustOutOnly,
    OutIn,
    OutOptionalIn;
    
    protected static final Map<String, ExchangePattern> map = new HashMap<String, ExchangePattern>();

    /**
     * Returns the WSDL URI for this message exchange pattern
     *
     * @return the WSDL URI for this message exchange pattern
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
                return true;
            default:
                return false;
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
        return map.get(wsdlUri);
    }

    static {
        for (ExchangePattern mep : values()) {
            String uri = mep.getWsdlUri();
            map.put(uri, mep);
            String name = uri.substring(uri.lastIndexOf('/'));
        }
    }
}
