package org.apache.ivy.util;

import java.util.Iterator;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContextualSAXHandler extends DefaultHandler {

    private Stack contextStack = new Stack();
    
    private StringBuffer buffer = new StringBuffer();

    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        contextStack.push(qName);
        buffer.setLength(0);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        contextStack.pop();
        buffer.setLength(0);
    }


    protected String getContext() {
        StringBuffer buf = new StringBuffer();
        for (Iterator iter = contextStack.iterator(); iter.hasNext();) {
            String ctx = (String) iter.next();
            buf.append(ctx).append("/");
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 1);
        }
        return buf.toString();
    }
    
    protected String getText() {
        return buffer.toString();
    }

}
