package org.apache.xml.serializer;

import org.xml.sax.SAXException;

/**
 * This interface has extensions to the standard SAX LexicalHandler interface.
 * This interface is intended to be used by a serializer.
 */
public interface ExtendedLexicalHandler extends org.xml.sax.ext.LexicalHandler
{
    /**
     * This method is used to notify of a comment
     * @param comment the comment, but unlike the SAX comment() method this
     * method takes a String rather than a character array.
     * @throws SAXException
     */
    public void comment(String comment) throws SAXException;
}
