package org.apache.camel.component.cxf.interceptors;

import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.MessageSenderInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.XMLMessage;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class DOMOutInterceptor extends AbstractPhaseInterceptor<Message> {
    private static final Logger LOG = LogUtils.getL7dLogger(DOMOutInterceptor.class);


    private final XMLMessageOutInterceptor xmlInterceptor = new XMLMessageOutInterceptor();
    private final SoapMessageOutInterceptor soapInterceptor = new SoapMessageOutInterceptor();
    
    public DOMOutInterceptor() {
        super(Phase.PREPARE_SEND);
        this.addBefore(MessageSenderInterceptor.class.getName());
    }

    public boolean isRequestor(Message message) {
        return Boolean.TRUE.equals(message.get(Message.REQUESTOR_ROLE));
    }
    
    @SuppressWarnings("unchecked")
    public void handleMessage(Message message) throws Fault {
        if (message instanceof XMLMessage) {
            xmlInterceptor.handleMessage((XMLMessage)message);
        } else if (message instanceof SoapMessage) {
            soapInterceptor.handleMessage((SoapMessage)message);
        } else {
            throw new Fault(new org.apache.cxf.common.i18n.Message("NOT_SUPPORTED_MESSAGE", 
                                                                   LOG, message.getClass().getName()));
        }        
    }
}
