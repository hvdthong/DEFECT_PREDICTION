package org.apache.xerces.dom;

import org.w3c.dom.range.*;

public class RangeExceptionImpl extends RangeException {
    public RangeExceptionImpl(short code, String message) {
        super(code,message);
    }
}
