package org.apache.camel.component.cxf.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.camel.Converter;
import org.apache.camel.Endpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.MessageContentsList;


/**
 * for CXF related types' converting .
 *
 * @version $Revision: 695245 $
 */
@Converter
public final class CxfConverter {
    private static final Log LOG = LogFactory.getLog(CxfConverter.class);

    private CxfConverter() {
    }

    @Converter
    public static Object[] toArray(final MessageContentsList list) throws Exception {
        if (list == null) {
            throw new IllegalArgumentException("The MessageChannel is null");
        }
        return list.toArray();
    }

    @Converter
    public static MessageContentsList toMessageContentsList(final Object[] array) {
        if (array != null) {
            return new MessageContentsList(array);
        } else {
            return new MessageContentsList();
        }
    }

    @Converter
    public static String soapMessageToString(final SOAPMessage soapMessage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            soapMessage.writeTo(baos);
        } catch (Exception e) {
            LOG.error("Get the exception when converting the SOAPMessage into String, the exception is " + e);
        }
        return baos.toString();
    }





}
