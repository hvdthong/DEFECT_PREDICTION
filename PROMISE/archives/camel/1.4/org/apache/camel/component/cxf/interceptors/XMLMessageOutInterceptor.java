package org.apache.camel.component.cxf.interceptors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.cxf.bindings.xformat.XMLBindingMessageFormat;
import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.XMLMessage;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.MessagePartInfo;

public class XMLMessageOutInterceptor extends AbstractMessageOutInterceptor<XMLMessage> {
    private static final Logger LOG = LogUtils.getL7dLogger(XMLMessageOutInterceptor.class);
   

    public XMLMessageOutInterceptor() {
        super(Phase.PREPARE_SEND);        
        addAfter(DOMOutInterceptor.class.getName());
    }

    protected Logger getLogger() {
        return LOG;
    }

    @SuppressWarnings("unchecked")
    public void handleMessage(XMLMessage message) throws Fault {
        Exchange exchange = message.getExchange();        
        BindingMessageInfo bmi = exchange.get(BindingMessageInfo.class);

        List<Element> payload = message.get(List.class);
        if (bmi == null && payload.size() > 1) {
            throw new Fault(new org.apache.cxf.common.i18n.Message(
                            "NO_XML_ROOT_NODE", LOG));            
        }
        
        if (bmi != null) {
            Element header = message.get(Element.class);
            if (header != null) {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.info("DOMOutInterceptor Copy Message Part related Headers to Payload.");
                }
                moveHeaderPartToPayload(bmi, header, payload);
            }
    
            XMLBindingMessageFormat msgFormat = 
                bmi.getExtensor(XMLBindingMessageFormat.class);
            QName rootName = msgFormat != null ? msgFormat.getRootNode() : null;
            
            if (rootName == null) {
                if (payload.size() > 1) {
                    throw new Fault(new org.apache.cxf.common.i18n.Message(
                                    "NO_XML_ROOT_NODE", LOG));
                }
            } else {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.info("DOMOutInterceptor Create xmlformat RootNode element");
                }                
                Element el = createElement(rootName, payload);
                payload = new ArrayList<Element>();
                payload.add(el);
            }

            message.put(List.class, payload);
            message.remove(Element.class);
        }
    }
    
    private void moveHeaderPartToPayload(BindingMessageInfo bmi,
                                         Element header,
                                         List<Element> payload) {
        Collection<MessagePartInfo> bodyParts = bmi.getMessageParts();
        NodeList nodes = header.getChildNodes();

        for (int idx = 0; idx < nodes.getLength(); idx++) {
            Node node = nodes.item(idx);
            int index = 0;
            for (MessagePartInfo mpi : bodyParts) {
                QName name = mpi.getConcreteName();
                if (name.getLocalPart().equals(node.getLocalName())
                    && name.getNamespaceURI().equals(node.getNamespaceURI())) {
                    payload.add(index, (Element)node);
                    break;
                } 
                ++index;
            }
        }
    }
}
