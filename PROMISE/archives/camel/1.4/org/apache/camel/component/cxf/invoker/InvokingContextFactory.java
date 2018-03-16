package org.apache.camel.component.cxf.invoker;

import org.apache.camel.component.cxf.DataFormat;

public final class InvokingContextFactory {
    
    private InvokingContextFactory() {
    }
    
    /**
     * Static method that creates a routing context object from a given data format
     * @param dataFormat
     * @return routing context
     */
    public static InvokingContext createContext(DataFormat dataFormat) {
            
        if (dataFormat == DataFormat.MESSAGE) {
            return new RawMessageInvokingContext();
        }

        if (dataFormat == DataFormat.PAYLOAD) {
            return new PayloadInvokingContext();
        }

        return new RawMessageInvokingContext();
    }
}
