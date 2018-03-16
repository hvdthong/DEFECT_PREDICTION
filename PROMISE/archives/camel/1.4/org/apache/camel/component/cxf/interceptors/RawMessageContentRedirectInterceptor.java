package org.apache.camel.component.cxf.interceptors;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class RawMessageContentRedirectInterceptor extends AbstractPhaseInterceptor<Message> {
    public RawMessageContentRedirectInterceptor() {
        super(Phase.WRITE);
    }

    public void handleMessage(Message message) throws Fault {
        Exception ex = message.getContent(Exception.class);
        if (ex != null) {
            if (ex instanceof Fault) {
                throw (Fault)ex;
            } else {
                throw new Fault(ex);
            }
        }

        InputStream is = message.getContent(InputStream.class);
        OutputStream os = message.getContent(OutputStream.class);

        try {
            IOUtils.copy(is, os);
            is.close();
            os.flush();
        } catch (Exception e) {
            throw new Fault(e);
        }
    }
}
