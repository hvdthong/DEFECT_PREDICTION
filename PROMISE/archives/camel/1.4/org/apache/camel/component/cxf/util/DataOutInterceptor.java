package org.apache.camel.component.cxf.util;

import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.staxutils.StaxUtils;

public class DataOutInterceptor extends AbstractOutDatabindingInterceptor {
    private static final Logger LOG = LogUtils.getL7dLogger(DataOutInterceptor.class);

    public DataOutInterceptor() {
        super(Phase.MARSHAL);
    }

    public void handleMessage(Message message) throws Fault {
        XMLStreamWriter xmlWriter = getXMLStreamWriter(message);
        try {
            Source source = message.getContent(Source.class);
            if (source != null) {
                XMLStreamReader xmlReader = StaxUtils.createXMLStreamReader(source);
                StaxUtils.copy(xmlReader, xmlWriter);
            }
        } catch (XMLStreamException e) {
            throw new Fault(new org.apache.cxf.common.i18n.Message("XMLSTREAM_EXCEPTION",
                                                                   LOG, e),
                            e);
        }

    }

}
