package org.apache.camel.component.cxf.interceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.wsdl.Definition;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.SoapVersion;
import org.apache.cxf.binding.soap.model.SoapBindingInfo;
import org.apache.cxf.binding.soap.model.SoapHeaderInfo;
import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.wsdl11.WSDLServiceBuilder;

public class SoapMessageOutInterceptor extends AbstractMessageOutInterceptor<SoapMessage> {
    private static final Logger LOG = LogUtils.getL7dLogger(SoapMessageInInterceptor.class);


    public SoapMessageOutInterceptor() {
        super(Phase.PREPARE_SEND);
        addAfter(DOMOutInterceptor.class.getName());
    }

    protected Logger getLogger() {
        return LOG;
    }

    @SuppressWarnings("unchecked")
    public void handleMessage(SoapMessage message) throws Fault {        

        List<Element> payload = message.get(List.class);
        Exchange exchange = message.getExchange();
        BindingMessageInfo bmi = exchange.get(BindingMessageInfo.class);

        if (LOG.isLoggable(Level.INFO)) {
            LOG.info("SoapMessageOutInterceptor binding operation style processing.");
        }
        SoapBindingInfo soapBinding = (SoapBindingInfo)exchange.get(BindingInfo.class);
        String style = soapBinding.getStyle(bmi.getBindingOperation().getOperationInfo());
        if ("rpc".equals(style)) {
            OperationInfo oi = bmi.getBindingOperation().getOperationInfo();
            Endpoint ep = exchange.get(Endpoint.class);
            Definition def =
                ep.getService().getServiceInfos().get(0).getProperty(WSDLServiceBuilder.WSDL_DEFINITION,
                                                             Definition.class);
            String prefix = def.getPrefix(oi.getName().getNamespaceURI());

            if ("".equals(prefix)) {
                prefix = "tns";
            }
            QName opName = null;
            boolean isClient = isRequestor(message);
            if (isClient) {
                opName = new QName(oi.getName().getNamespaceURI(),
                                   oi.getName().getLocalPart(),
                                   prefix);
            } else {
                opName = new QName(oi.getName().getNamespaceURI(),
                                   oi.getName().getLocalPart() + "Response",
                                   prefix);
            }
            Element opEl = createElement(opName, payload);
            payload = new ArrayList<Element>();
            payload.add(opEl);
        }

        message.put(List.class, payload);
    }
}
