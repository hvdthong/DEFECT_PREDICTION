package org.apache.camel.component.cxf.util;

import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.AbstractInDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.staxutils.DepthXMLStreamReader;
import org.apache.cxf.staxutils.StaxUtils;

public class DataInInterceptor extends AbstractInDatabindingInterceptor {
    private static final Logger LOG = LogUtils.getL7dLogger(DataInInterceptor.class);

    public DataInInterceptor() {
        super(Phase.UNMARSHAL);
    }

    public void handleMessage(Message message) throws Fault {
        DepthXMLStreamReader xmlReader = getXMLStreamReader(message);
        try {
            Document doc = StaxUtils.read(xmlReader);
            message.setContent(Source.class, new DOMSource(doc));
        } catch (XMLStreamException e) {
            throw new Fault(new org.apache.cxf.common.i18n.Message("XMLSTREAM_EXCEPTION",
                                                                   LOG),
                            e);
        }
    }

}
