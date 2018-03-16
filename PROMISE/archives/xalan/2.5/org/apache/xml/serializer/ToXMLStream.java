 package org.apache.xml.serializer;

import java.io.IOException;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;
import org.xml.sax.SAXException;

/**
 * @author minchau
 *
 */

public class ToXMLStream extends ToStream
{

    /**
     * remembers if we need to write out "]]>" to close the CDATA
     */
    private boolean m_cdataTagOpen = false;


    /**
     * Map that tells which XML characters should have special treatment, and it
     *  provides character to entity name lookup.
     */
    protected static CharInfo m_xmlcharInfo =
        CharInfo.getCharInfo(CharInfo.XML_ENTITIES_RESOURCE);

    /**
     * Default constructor.
     */
    public ToXMLStream()
    {
        m_charInfo = m_xmlcharInfo;

        initCDATA();
        m_prefixMap = new NamespaceMappings();

    }

    /**
     * Copy properties from another SerializerToXML.
     *
     * @param xmlListener non-null reference to a SerializerToXML object.
     */
    public void CopyFrom(ToXMLStream xmlListener)
    {

        m_writer = xmlListener.m_writer;


        String encoding = xmlListener.getEncoding();
        setEncoding(encoding);

        setOmitXMLDeclaration(xmlListener.getOmitXMLDeclaration());

        m_startTagOpen = xmlListener.m_startTagOpen;

        m_ispreserve = xmlListener.m_ispreserve;
        m_preserves = xmlListener.m_preserves;
        m_isprevtext = xmlListener.m_isprevtext;
        m_doIndent = xmlListener.m_doIndent;
        m_currentElemDepth = xmlListener.m_currentElemDepth;
        setIndentAmount(xmlListener.getIndentAmount());
        m_startNewLine = xmlListener.m_startNewLine;
        m_needToOutputDocTypeDecl = xmlListener.m_needToOutputDocTypeDecl;
        setDoctypeSystem(xmlListener.getDoctypeSystem());
        setDoctypePublic(xmlListener.getDoctypePublic());        
        setStandalone(xmlListener.getStandalone());
        setMediaType(xmlListener.getMediaType());
        m_maxCharacter = xmlListener.m_maxCharacter;
        m_spaceBeforeClose = xmlListener.m_spaceBeforeClose;
        m_cdataStartCalled = xmlListener.m_cdataStartCalled;

    }

    /**
     * Receive notification of the beginning of a document.
     *
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     *
     * @throws org.xml.sax.SAXException
     */
    public void startDocumentInternal() throws org.xml.sax.SAXException
    {

        if (m_needToCallStartDocument)
        { 
            super.startDocumentInternal();
            m_needToCallStartDocument = false;

            if (m_inEntityRef)
                return;

            m_needToOutputDocTypeDecl = true;
            m_startNewLine = false;

            if (getOmitXMLDeclaration() == false)
            {
                String encoding = Encodings.getMimeEncoding(getEncoding());
                String version = getVersion();
                if (version == null)
                    version = "1.0";
                String standalone;

                if (m_standaloneWasSpecified)
                {
                    standalone = " standalone=\"" + getStandalone() + "\"";
                }
                else
                {
                    standalone = "";
                }

                try
                {
                    m_writer.write("<?xml version=\"");
                    m_writer.write(version);
                    m_writer.write("\" encoding=\"");
                    m_writer.write(encoding);
                    m_writer.write('\"');
                    m_writer.write(standalone);
                    m_writer.write("?>");
                    m_writer.write(m_lineSep, 0, m_lineSepLen);
                } 
                catch(IOException e)
                {
                    throw new SAXException(e);
                }

            }
        }
    }

    /**
     * Receive notification of the end of a document.
     *
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     *
     * @throws org.xml.sax.SAXException
     */
    public void endDocument() throws org.xml.sax.SAXException
    {
        flushPending();
        if (m_doIndent && !m_isprevtext)
        {
            try
            {
            outputLineSep();
            }
            catch(IOException e)
            {
                throw new SAXException(e);
            }
        }

        flushWriter();
        super.fireEndDoc();
    }

    /**
     * Starts a whitespace preserving section. All characters printed
     * within a preserving section are printed without indentation and
     * without consolidating multiple spaces. This is equivalent to
     * the <tt>xml:space=&quot;preserve&quot;</tt> attribute. Only XML
     * and HTML serializers need to support this method.
     * <p>
     * The contents of the whitespace preserving section will be delivered
     * through the regular <tt>characters</tt> event.
     *
     * @throws org.xml.sax.SAXException
     */
    public void startPreserving() throws org.xml.sax.SAXException
    {

        m_preserves.push(true);

        m_ispreserve = true;
    }

    /**
     * Ends a whitespace preserving section.
     *
     * @see #startPreserving
     *
     * @throws org.xml.sax.SAXException
     */
    public void endPreserving() throws org.xml.sax.SAXException
    {

        m_ispreserve = m_preserves.isEmpty() ? false : m_preserves.pop();
    }

    /**
     * Receive notification of a processing instruction.
     *
     * @param target The processing instruction target.
     * @param data The processing instruction data, or null if
     *        none was supplied.
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     *
     * @throws org.xml.sax.SAXException
     */
    public void processingInstruction(String target, String data)
        throws org.xml.sax.SAXException
    {
        if (m_inEntityRef)
            return;
        
        flushPending();   

        if (target.equals(Result.PI_DISABLE_OUTPUT_ESCAPING))
        {
            startNonEscaping();
        }
        else if (target.equals(Result.PI_ENABLE_OUTPUT_ESCAPING))
        {
            endNonEscaping();
        }
        else
        {
            try
            {
                if (m_startTagOpen)
                    closeStartTag();

                if (shouldIndent())
                    indent();

                m_writer.write('<');
                m_writer.write('?');
                m_writer.write(target);

                if (data.length() > 0
                    && !Character.isSpaceChar(data.charAt(0)))
                    m_writer.write(' ');

                int indexOfQLT = data.indexOf("?>");

                if (indexOfQLT >= 0)
                {

                    if (indexOfQLT > 0)
                    {
                        m_writer.write(data.substring(0, indexOfQLT));
                    }


                    if ((indexOfQLT + 2) < data.length())
                    {
                        m_writer.write(data.substring(indexOfQLT + 2));
                    }
                }
                else
                {
                    m_writer.write(data);
                }

                m_writer.write('?');
                m_writer.write('>');

                if (m_currentElemDepth <= 0)
                    m_writer.write(m_lineSep, 0, m_lineSepLen);

                m_startNewLine = true;
            }
            catch(IOException e)
            {
                throw new SAXException(e);
            }
        }
        super.fireEscapingEvent(target, data);        
    }

    /**
     * Receive notivication of a entityReference.
     *
     * @param name The name of the entity.
     *
     * @throws org.xml.sax.SAXException
     */
    public void entityReference(String name) throws org.xml.sax.SAXException
    {
        if (m_startTagOpen)
        {
            closeStartTag();
            m_startTagOpen = false;
        }

        try
        {
            if (shouldIndent())
                indent();

            m_writer.write("&");
            m_writer.write(name);
            m_writer.write(";");
        }
        catch(IOException e)
        {
            throw new SAXException(e);
        }
        super.fireEntityReference(name);            
    }


    public void addAttribute(
        String uri,
        String localName,
        String rawName,
        String type,
        String value)
        throws SAXException
    {
        if (m_startTagOpen)
        {
            if (!rawName.startsWith("xmlns"))
            {
                String prefixUsed =
                    ensureAttributesNamespaceIsDeclared(
                        uri,
                        localName,
                        rawName);
                if (prefixUsed != null
                    && rawName != null
                    && !rawName.startsWith(prefixUsed))
                {
                    rawName = prefixUsed + ":" + localName;

                }
            }
            addAttributeAlways(uri, localName, rawName, type, value);
        }
        else
        {
            /*
             * The startTag is closed, yet we are adding an attribute?
             *
             * Section: 7.1.3 Creating Attributes Adding an attribute to an
             * element after a PI (for example) has been added to it is an
             * error. The attributes can be ignored. The spec doesn't explicitly
             * say this is disallowed, as it does for child elements, but it
             * makes sense to have the same treatment.
             *
             * We choose to ignore the attribute which is added too late.
             */

            String msg = XMLMessages.createXMLMessage(
                    XMLErrorResources.ER_ILLEGAL_ATTRIBUTE_POSITION,new Object[]{ localName });

            try {
                Transformer tran = super.getTransformer();
                ErrorListener errHandler = tran.getErrorListener();


                if (null != errHandler && m_sourceLocator != null)
                  errHandler.warning(new TransformerException(msg, m_sourceLocator));
                else
                  System.out.println(msg);
                }
            catch (Exception e){}             
        }
    }

    /**
     * This method escapes special characters used in attribute values
     * It is stolen from XSLTC
     */
    private String escapeString(String value)
    {
        final char[] ch = value.toCharArray();
        final int limit = ch.length;
        StringBuffer result = new StringBuffer();

        int offset = 0;
        for (int i = 0; i < limit; i++)
        {
            switch (ch[i])
            {
                case '&' :
                    result.append(ch, offset, i - offset).append(ENTITY_AMP);
                    offset = i + 1;
                    break;
                case '"' :
                    result.append(ch, offset, i - offset).append(ENTITY_QUOT);
                    offset = i + 1;
                    break;
                case '<' :
                    result.append(ch, offset, i - offset).append(ENTITY_LT);
                    offset = i + 1;
                    break;
                case '>' :
                    result.append(ch, offset, i - offset).append(ENTITY_GT);
                    offset = i + 1;
                    break;
                case '\n' :
                    result.append(ch, offset, i - offset).append(ENTITY_CRLF);
                    offset = i + 1;
                    break;
            }
        }

        if (offset < limit)
        {
            result.append(ch, offset, limit - offset);
        }
        return result.toString();
    }

    /**
     * @see org.apache.xml.serializer.ExtendedContentHandler#endElement(String)
     */
    public void endElement(String elemName) throws SAXException
    {
        endElement(null, null, elemName);
    }

    /**
     * From XSLTC
     * Related to startPrefixMapping ???
     */
    public void namespaceAfterStartElement(
        final String prefix,
        final String uri)
        throws SAXException
    {

        if (m_elementURI == null)
        {
            String prefix1 = getPrefixPart(m_elementName);
            if (prefix1 == null && EMPTYSTRING.equals(prefix))
            {
                m_elementURI = uri;
            }
        }            
        startPrefixMapping(prefix,uri,false);
        return;

    }

    /**
     * From XSLTC
     * Declare a prefix to point to a namespace URI. Inform SAX handler
     * if this is a new prefix mapping.
     */
    protected boolean pushNamespace(String prefix, String uri)
    {
        try
        {
            if (m_prefixMap.pushNamespace(prefix, uri, m_currentElemDepth))
            {
                startPrefixMapping(prefix, uri);
                return true;
            }
        }
        catch (SAXException e)
        {
        }
        return false;
    }


}
