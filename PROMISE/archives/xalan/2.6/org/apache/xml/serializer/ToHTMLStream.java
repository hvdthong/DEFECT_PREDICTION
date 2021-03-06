package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.xml.transform.Result;

import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.Trie;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Santiago Pericas-Geertsen
 * @author G. Todd Miller 
 */
public class ToHTMLStream extends ToStream 
{

    /** This flag is set while receiving events from the DTD */
    protected boolean m_inDTD = false;

    /** True if the current element is a block element.  (seems like 
     *  this needs to be a stack. -sb). */
    private boolean m_inBlockElem = false;

    /**
     * Map that tells which XML characters should have special treatment, and it
     *  provides character to entity name lookup.
     */
    protected static final CharInfo m_htmlcharInfo =
        CharInfo.getCharInfo(CharInfo.HTML_ENTITIES_RESOURCE, Method.HTML);

    /** A digital search trie for fast, case insensitive lookup of ElemDesc objects. */
    static final Trie m_elementFlags = new Trie();

    static {

        m_elementFlags.put("BASEFONT", new ElemDesc(0 | ElemDesc.EMPTY));
        m_elementFlags.put(
            "FRAME",
            new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
        m_elementFlags.put("FRAMESET", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("NOFRAMES", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put(
            "ISINDEX",
            new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
        m_elementFlags.put(
            "APPLET",
            new ElemDesc(0 | ElemDesc.WHITESPACESENSITIVE));
        m_elementFlags.put("CENTER", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("DIR", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("MENU", new ElemDesc(0 | ElemDesc.BLOCK));

        m_elementFlags.put("TT", new ElemDesc(0 | ElemDesc.FONTSTYLE));
        m_elementFlags.put("I", new ElemDesc(0 | ElemDesc.FONTSTYLE));
        m_elementFlags.put("B", new ElemDesc(0 | ElemDesc.FONTSTYLE));
        m_elementFlags.put("BIG", new ElemDesc(0 | ElemDesc.FONTSTYLE));
        m_elementFlags.put("SMALL", new ElemDesc(0 | ElemDesc.FONTSTYLE));
        m_elementFlags.put("EM", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("STRONG", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("DFN", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("CODE", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("SAMP", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("KBD", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("VAR", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("CITE", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("ABBR", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put("ACRONYM", new ElemDesc(0 | ElemDesc.PHRASE));
        m_elementFlags.put(
            "SUP",
            new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL));
        m_elementFlags.put(
            "SUB",
            new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL));
        m_elementFlags.put(
            "SPAN",
            new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL));
        m_elementFlags.put(
            "BDO",
            new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL));
        m_elementFlags.put(
            "BR",
            new ElemDesc(
                0
                    | ElemDesc.SPECIAL
                    | ElemDesc.ASPECIAL
                    | ElemDesc.EMPTY
                    | ElemDesc.BLOCK));
        m_elementFlags.put("BODY", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put(
            "ADDRESS",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put(
            "DIV",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put("A", new ElemDesc(0 | ElemDesc.SPECIAL));
        m_elementFlags.put(
            "MAP",
            new ElemDesc(
                0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL | ElemDesc.BLOCK));
        m_elementFlags.put(
            "AREA",
            new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
        m_elementFlags.put(
            "LINK",
            new ElemDesc(
                0 | ElemDesc.HEADMISC | ElemDesc.EMPTY | ElemDesc.BLOCK));
        m_elementFlags.put(
            "IMG",
            new ElemDesc(
                0
                    | ElemDesc.SPECIAL
                    | ElemDesc.ASPECIAL
                    | ElemDesc.EMPTY
                    | ElemDesc.WHITESPACESENSITIVE));
        m_elementFlags.put(
            "OBJECT",
            new ElemDesc(
                0
                    | ElemDesc.SPECIAL
                    | ElemDesc.ASPECIAL
                    | ElemDesc.HEADMISC
                    | ElemDesc.WHITESPACESENSITIVE));
        m_elementFlags.put("PARAM", new ElemDesc(0 | ElemDesc.EMPTY));
        m_elementFlags.put(
            "HR",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET
                    | ElemDesc.EMPTY));
        m_elementFlags.put(
            "P",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put(
            "H1",
            new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
        m_elementFlags.put(
            "H2",
            new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
        m_elementFlags.put(
            "H3",
            new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
        m_elementFlags.put(
            "H4",
            new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
        m_elementFlags.put(
            "H5",
            new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
        m_elementFlags.put(
            "H6",
            new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
        m_elementFlags.put(
            "PRE",
            new ElemDesc(0 | ElemDesc.PREFORMATTED | ElemDesc.BLOCK));
        m_elementFlags.put(
            "Q",
            new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL));
        m_elementFlags.put(
            "BLOCKQUOTE",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put("INS", new ElemDesc(0));
        m_elementFlags.put("DEL", new ElemDesc(0));
        m_elementFlags.put(
            "DL",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put("DT", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("DD", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put(
            "OL",
            new ElemDesc(0 | ElemDesc.LIST | ElemDesc.BLOCK));
        m_elementFlags.put(
            "UL",
            new ElemDesc(0 | ElemDesc.LIST | ElemDesc.BLOCK));
        m_elementFlags.put("LI", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("FORM", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("LABEL", new ElemDesc(0 | ElemDesc.FORMCTRL));
        m_elementFlags.put(
            "INPUT",
            new ElemDesc(
                0 | ElemDesc.FORMCTRL | ElemDesc.INLINELABEL | ElemDesc.EMPTY));
        m_elementFlags.put(
            "SELECT",
            new ElemDesc(0 | ElemDesc.FORMCTRL | ElemDesc.INLINELABEL));
        m_elementFlags.put("OPTGROUP", new ElemDesc(0));
        m_elementFlags.put("OPTION", new ElemDesc(0));
        m_elementFlags.put(
            "TEXTAREA",
            new ElemDesc(0 | ElemDesc.FORMCTRL | ElemDesc.INLINELABEL));
        m_elementFlags.put(
            "FIELDSET",
            new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM));
        m_elementFlags.put("LEGEND", new ElemDesc(0));
        m_elementFlags.put(
            "BUTTON",
            new ElemDesc(0 | ElemDesc.FORMCTRL | ElemDesc.INLINELABEL));
        m_elementFlags.put(
            "TABLE",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put("CAPTION", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("THEAD", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("TFOOT", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("TBODY", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("COLGROUP", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put(
            "COL",
            new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
        m_elementFlags.put("TR", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put("TH", new ElemDesc(0));
        m_elementFlags.put("TD", new ElemDesc(0));
        m_elementFlags.put(
            "HEAD",
            new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.HEADELEM));
        m_elementFlags.put("TITLE", new ElemDesc(0 | ElemDesc.BLOCK));
        m_elementFlags.put(
            "BASE",
            new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
        m_elementFlags.put(
            "META",
            new ElemDesc(
                0 | ElemDesc.HEADMISC | ElemDesc.EMPTY | ElemDesc.BLOCK));
        m_elementFlags.put(
            "STYLE",
            new ElemDesc(
                0 | ElemDesc.HEADMISC | ElemDesc.RAW | ElemDesc.BLOCK));
        m_elementFlags.put(
            "SCRIPT",
            new ElemDesc(
                0
                    | ElemDesc.SPECIAL
                    | ElemDesc.ASPECIAL
                    | ElemDesc.HEADMISC
                    | ElemDesc.RAW));
        m_elementFlags.put(
            "NOSCRIPT",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put("HTML", new ElemDesc(0 | ElemDesc.BLOCK));

        m_elementFlags.put("FONT", new ElemDesc(0 | ElemDesc.FONTSTYLE));

        m_elementFlags.put("S", new ElemDesc(0 | ElemDesc.FONTSTYLE));
        m_elementFlags.put("STRIKE", new ElemDesc(0 | ElemDesc.FONTSTYLE));

        m_elementFlags.put("U", new ElemDesc(0 | ElemDesc.FONTSTYLE));

        m_elementFlags.put("NOBR", new ElemDesc(0 | ElemDesc.FONTSTYLE));

        m_elementFlags.put(
            "IFRAME",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put(
            "LAYER",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));
        m_elementFlags.put(
            "ILAYER",
            new ElemDesc(
                0
                    | ElemDesc.BLOCK
                    | ElemDesc.BLOCKFORM
                    | ElemDesc.BLOCKFORMFIELDSET));

        ElemDesc elemDesc;

        elemDesc = (ElemDesc) m_elementFlags.get("AREA");

        elemDesc.setAttr("HREF", ElemDesc.ATTRURL);
        elemDesc.setAttr("NOHREF", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("BASE");

        elemDesc.setAttr("HREF", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("BLOCKQUOTE");

        elemDesc.setAttr("CITE", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("Q");

        elemDesc.setAttr("CITE", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("INS");

        elemDesc.setAttr("CITE", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("DEL");

        elemDesc.setAttr("CITE", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("A");

        elemDesc.setAttr("HREF", ElemDesc.ATTRURL);
        elemDesc.setAttr("NAME", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("LINK");
        elemDesc.setAttr("HREF", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("INPUT");

        elemDesc.setAttr("SRC", ElemDesc.ATTRURL);
        elemDesc.setAttr("USEMAP", ElemDesc.ATTRURL);
        elemDesc.setAttr("CHECKED", ElemDesc.ATTREMPTY);
        elemDesc.setAttr("DISABLED", ElemDesc.ATTREMPTY);
        elemDesc.setAttr("ISMAP", ElemDesc.ATTREMPTY);
        elemDesc.setAttr("READONLY", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("SELECT");

        elemDesc.setAttr("DISABLED", ElemDesc.ATTREMPTY);
        elemDesc.setAttr("MULTIPLE", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("OPTGROUP");

        elemDesc.setAttr("DISABLED", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("OPTION");

        elemDesc.setAttr("SELECTED", ElemDesc.ATTREMPTY);
        elemDesc.setAttr("DISABLED", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("TEXTAREA");

        elemDesc.setAttr("DISABLED", ElemDesc.ATTREMPTY);
        elemDesc.setAttr("READONLY", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("BUTTON");

        elemDesc.setAttr("DISABLED", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("SCRIPT");

        elemDesc.setAttr("SRC", ElemDesc.ATTRURL);
        elemDesc.setAttr("FOR", ElemDesc.ATTRURL);
        elemDesc.setAttr("DEFER", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("IMG");

        elemDesc.setAttr("SRC", ElemDesc.ATTRURL);
        elemDesc.setAttr("LONGDESC", ElemDesc.ATTRURL);
        elemDesc.setAttr("USEMAP", ElemDesc.ATTRURL);
        elemDesc.setAttr("ISMAP", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("OBJECT");

        elemDesc.setAttr("CLASSID", ElemDesc.ATTRURL);
        elemDesc.setAttr("CODEBASE", ElemDesc.ATTRURL);
        elemDesc.setAttr("DATA", ElemDesc.ATTRURL);
        elemDesc.setAttr("ARCHIVE", ElemDesc.ATTRURL);
        elemDesc.setAttr("USEMAP", ElemDesc.ATTRURL);
        elemDesc.setAttr("DECLARE", ElemDesc.ATTREMPTY);

        elemDesc = (ElemDesc) m_elementFlags.get("FORM");

        elemDesc.setAttr("ACTION", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("HEAD");

        elemDesc.setAttr("PROFILE", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("FRAME");

        elemDesc.setAttr("SRC", ElemDesc.ATTRURL);
        elemDesc.setAttr("LONGDESC", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("IFRAME");

        elemDesc.setAttr("SRC", ElemDesc.ATTRURL);
        elemDesc.setAttr("LONGDESC", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("LAYER");

        elemDesc.setAttr("SRC", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("ILAYER");

        elemDesc.setAttr("SRC", ElemDesc.ATTRURL);

        elemDesc = (ElemDesc) m_elementFlags.get("DIV");

        elemDesc.setAttr("SRC", ElemDesc.ATTRURL);
    }

    /**
     * Dummy element for elements not found.
     */
    static private final ElemDesc m_dummy = new ElemDesc(0 | ElemDesc.BLOCK);

    /** True if URLs should be specially escaped with the %xx form. */
    private boolean m_specialEscapeURLs = true;

    /** True if the META tag should be omitted. */
    private boolean m_omitMetaTag = false;

    /**
     * Tells if the formatter should use special URL escaping.
     *
     * @param bool True if URLs should be specially escaped with the %xx form.
     */
    public void setSpecialEscapeURLs(boolean bool)
    {
        m_specialEscapeURLs = bool;
    }

    /**
     * Tells if the formatter should omit the META tag.
     *
     * @param bool True if the META tag should be omitted.
     */
    public void setOmitMetaTag(boolean bool)
    {
        m_omitMetaTag = bool;
    }

    /**
     * Specifies an output format for this serializer. It the
     * serializer has already been associated with an output format,
     * it will switch to the new format. This method should not be
     * called while the serializer is in the process of serializing
     * a document.
     *
     * @param format The output format to use
     */
    public void setOutputFormat(Properties format)
    {
 
        m_specialEscapeURLs =
            OutputPropertyUtils.getBooleanProperty(
                OutputPropertiesFactory.S_USE_URL_ESCAPING,
                format);

        m_omitMetaTag =
            OutputPropertyUtils.getBooleanProperty(
                OutputPropertiesFactory.S_OMIT_META_TAG,
                format);

        super.setOutputFormat(format);
    }

    /**
     * Tells if the formatter should use special URL escaping.
     *
     * @return True if URLs should be specially escaped with the %xx form.
     */
    private final boolean getSpecialEscapeURLs()
    {
        return m_specialEscapeURLs;
    }

    /**
     * Tells if the formatter should omit the META tag.
     *
     * @return True if the META tag should be omitted.
     */
    private final boolean getOmitMetaTag()
    {
        return m_omitMetaTag;
    }

    /**
     * Get a description of the given element.
     *
     * @param name non-null name of element, case insensitive.
     *
     * @return non-null reference to ElemDesc, which may be m_dummy if no 
     *         element description matches the given name.
     */
    public static final ElemDesc getElemDesc(String name)
    {
        /* this method used to return m_dummy  when name was null
         * but now it doesn't check and and requires non-null name.
         */
        Object obj = m_elementFlags.get(name);
        if (null != obj)
            return (ElemDesc)obj;
        return m_dummy;
    }

    /**
     * Default constructor.
     */
    public ToHTMLStream()
    {

        super();
        m_charInfo = m_htmlcharInfo;
        m_prefixMap = new NamespaceMappings();

    }

    /** The name of the current element. */

    /**
     * Receive notification of the beginning of a document.
     *
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     *
     * @throws org.xml.sax.SAXException
     */
    protected void startDocumentInternal() throws org.xml.sax.SAXException
    {
        super.startDocumentInternal();

        m_needToCallStartDocument = false; 
        m_needToOutputDocTypeDecl = true;
        m_startNewLine = false;
        setOmitXMLDeclaration(true);

        if (true == m_needToOutputDocTypeDecl)
        {
            String doctypeSystem = getDoctypeSystem();
            String doctypePublic = getDoctypePublic();
            if ((null != doctypeSystem) || (null != doctypePublic))
            {
                final java.io.Writer writer = m_writer;
                try
                {
                writer.write("<!DOCTYPE HTML");

                if (null != doctypePublic)
                {
                    writer.write(" PUBLIC \"");
                    writer.write(doctypePublic);
                    writer.write('"');
                }

                if (null != doctypeSystem)
                {
                    if (null == doctypePublic)
                        writer.write(" SYSTEM \"");
                    else
                        writer.write('"');

                    writer.write(doctypeSystem);
                    writer.write('"');
                }

                writer.write('>');
                outputLineSep();
                }
                catch(IOException e)
                {
                    throw new SAXException(e);
                }
            }
        }

        m_needToOutputDocTypeDecl = false;
    }

    /**
     * Receive notification of the end of a document. 
     *
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     *
     * @throws org.xml.sax.SAXException
     */
    public final void endDocument() throws org.xml.sax.SAXException
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
        if (m_tracer != null)
            super.fireEndDoc();
    }

    /**
     *  Receive notification of the beginning of an element.
     *
     *
     *  @param namespaceURI
     *  @param localName
     *  @param name The element type name.
     *  @param atts The attributes attached to the element, if any.
     *  @throws org.xml.sax.SAXException Any SAX exception, possibly
     *             wrapping another exception.
     *  @see #endElement
     *  @see org.xml.sax.AttributeList
     */
    public void startElement(
        String namespaceURI,
        String localName,
        String name,
        Attributes atts)
        throws org.xml.sax.SAXException
    {

        ElemContext elemContext = m_elemContext;

        if (elemContext.m_startTagOpen)
        {
            closeStartTag();
            elemContext.m_startTagOpen = false;
        }
        else if (m_cdataTagOpen)
        {
            closeCDATA();
            m_cdataTagOpen = false;
        }
        else if (m_needToCallStartDocument)
        {
            startDocumentInternal();
            m_needToCallStartDocument = false;
        }


        if (null != namespaceURI && namespaceURI.length() > 0)
        {
            super.startElement(namespaceURI, localName, name, atts);

            return;
        }
        
        try
        {
            ElemDesc elemDesc = getElemDesc(name);
            int elemFlags = elemDesc.getFlags();

            if (m_doIndent)
            {

                boolean isBlockElement = (elemFlags & ElemDesc.BLOCK) != 0;
                if (m_ispreserve)
                    m_ispreserve = false;
                else if (
                    (null != elemContext.m_elementName)
                    && (!m_inBlockElem
                        || isBlockElement) /* && !isWhiteSpaceSensitive */
                    )
                {
                    m_startNewLine = true;

                    indent();

                }
                m_inBlockElem = !isBlockElement;
            }

            if (atts != null)
                addAttributes(atts);            

            m_isprevtext = false;
            final java.io.Writer writer = m_writer;
            writer.write('<');
            writer.write(name);



            if (m_tracer != null)
                firePseudoAttributes();
            
            if ((elemFlags & ElemDesc.EMPTY) != 0)  
            {
                m_elemContext = elemContext.push();
                /* XSLTC sometimes calls namespaceAfterStartElement()
                 * so we need to remember the name
                 */
                m_elemContext.m_elementName = name;
                m_elemContext.m_elementDesc = elemDesc;
                return;                
            } 
            else
            {
                elemContext = elemContext.push(namespaceURI,localName,name);
                m_elemContext = elemContext;
                elemContext.m_elementDesc = elemDesc;
                elemContext.m_isRaw = (elemFlags & ElemDesc.RAW) != 0;
            }
            

            if ((elemFlags & ElemDesc.HEADELEM) != 0)
            {
                closeStartTag();
                elemContext.m_startTagOpen = false;
                if (!m_omitMetaTag)
                {
                    if (m_doIndent)
                        indent();
                    writer.write(
                        "<META http-equiv=\"Content-Type\" content=\"text/html; charset=");
                    String encoding = getEncoding();
                    String encode = Encodings.getMimeEncoding(encoding);
                    writer.write(encode);
                    writer.write("\">");
                }
            }
        }
        catch (IOException e)
        {
            throw new SAXException(e);
        }
    }

    /**
     *  Receive notification of the end of an element.
     *
     *
     *  @param namespaceURI
     *  @param localName
     *  @param name The element type name
     *  @throws org.xml.sax.SAXException Any SAX exception, possibly
     *             wrapping another exception.
     */
    public final void endElement(
        final String namespaceURI,
        final String localName,
        final String name)
        throws org.xml.sax.SAXException
    {
        if (m_cdataTagOpen)
            closeCDATA();

        if (null != namespaceURI && namespaceURI.length() > 0)
        {
            super.endElement(namespaceURI, localName, name);

            return;
        }

        try
        {

            ElemContext elemContext = m_elemContext;
            final ElemDesc elemDesc = elemContext.m_elementDesc;
            final int elemFlags = elemDesc.getFlags();
            final boolean elemEmpty = (elemFlags & ElemDesc.EMPTY) != 0;

            if (m_doIndent)
            {
                final boolean isBlockElement = (elemFlags&ElemDesc.BLOCK) != 0;
                boolean shouldIndent = false;

                if (m_ispreserve)
                {
                    m_ispreserve = false;
                }
                else if (m_doIndent && (!m_inBlockElem || isBlockElement))
                {
                    m_startNewLine = true;
                    shouldIndent = true;
                }
                if (!elemContext.m_startTagOpen && shouldIndent)
                    indent(elemContext.m_currentElemDepth - 1);
                m_inBlockElem = !isBlockElement;
            }

            final java.io.Writer writer = m_writer;
            if (!elemContext.m_startTagOpen)
            {
                writer.write("</");
                writer.write(name);
                writer.write('>');
            }
            else
            {
                
                if (m_tracer != null)
                    super.fireStartElem(name);

                int nAttrs = m_attributes.getLength();
                if (nAttrs > 0)
                {
                    processAttributes(m_writer, nAttrs);
                    m_attributes.clear();
                }
                if (!elemEmpty)
                {

                    writer.write("></");
                    writer.write(name);
                    writer.write('>');
                }
                else
                {
                    writer.write('>');
                }
            }
            
            if ((elemFlags & ElemDesc.WHITESPACESENSITIVE) != 0)
                m_ispreserve = true;
            m_isprevtext = false;

            if (m_tracer != null)
                super.fireEndElem(name);            
                           
            if (elemEmpty)
            {
                m_elemContext = elemContext.m_prev;
                return;
            }

            if (!elemContext.m_startTagOpen)
            {
                if (m_doIndent && !m_preserves.isEmpty())
                    m_preserves.pop();
            }
            m_elemContext = elemContext.m_prev;
        }
        catch (IOException e)
        {
            throw new SAXException(e);
        }
    }

    /**
     * Process an attribute.
     * @param   writer The writer to write the processed output to.
     * @param   name   The name of the attribute.
     * @param   value   The value of the attribute.
     * @param   elemDesc The description of the HTML element 
     *           that has this attribute.
     *
     * @throws org.xml.sax.SAXException
     */
    protected void processAttribute(
        java.io.Writer writer,
        String name,
        String value,
        ElemDesc elemDesc)
        throws IOException
    {
        writer.write(' ');

        if (   ((value.length() == 0) || value.equalsIgnoreCase(name))
            && elemDesc != null 
            && elemDesc.isAttrFlagSet(name, ElemDesc.ATTREMPTY))
        {
            writer.write(name);
        }
        else
        {
            writer.write(name);
            writer.write("=\"");
            if (   elemDesc != null
                && elemDesc.isAttrFlagSet(name, ElemDesc.ATTRURL))
                writeAttrURI(writer, value, m_specialEscapeURLs);
            else
                writeAttrString(writer, value, this.getEncoding());
            writer.write('"');

        }
    }

    /**
     * Tell if a character is an ASCII digit.
     */
    private boolean isASCIIDigit(char c)
    {
        return (c >= '0' && c <= '9');
    }

    /**
     * Make an integer into an HH hex value.
     * Does no checking on the size of the input, since this 
     * is only meant to be used locally by writeAttrURI.
     * 
     * @param i must be a value less than 255.
     * 
     * @return should be a two character string.
     */
    private static String makeHHString(int i)
    {
        String s = Integer.toHexString(i).toUpperCase();
        if (s.length() == 1)
        {
            s = "0" + s;
        }
        return s;
    }

    /**
    * Dmitri Ilyin: Makes sure if the String is HH encoded sign.
    * @param str must be 2 characters long
    *
    * @return true or false
    */
    private boolean isHHSign(String str)
    {
        boolean sign = true;
        try
        {
            char r = (char) Integer.parseInt(str, 16);
        }
        catch (NumberFormatException e)
        {
            sign = false;
        }
        return sign;
    }

    /**
     * Write the specified <var>string</var> after substituting non ASCII characters,
     * with <CODE>%HH</CODE>, where HH is the hex of the byte value.
     *
     * @param   string      String to convert to XML format.
     * @param doURLEscaping True if we should try to encode as 
     *
     * @throws org.xml.sax.SAXException if a bad surrogate pair is detected.
     */
    public void writeAttrURI(
        final java.io.Writer writer, String string, boolean doURLEscaping)
        throws IOException
    {

        final int end = string.length();
        if (end > m_attrBuff.length)
        {
           m_attrBuff = new char[end*2 + 1];               
        }
        string.getChars(0,end, m_attrBuff, 0); 
        final char[] chars = m_attrBuff;

        int cleanStart = 0;
        int cleanLength = 0;
        
        
        char ch = 0;
        for (int i = 0; i < end; i++)
        {
            ch = chars[i];

            if ((ch < 32) || (ch > 126))
            {
                if (cleanLength > 0)
                {
                    writer.write(chars, cleanStart, cleanLength);
                    cleanLength = 0;
                }
                if (doURLEscaping)
                {

                    if (ch <= 0x7F)
                    {
                        writer.write('%');
                        writer.write(makeHHString(ch));
                    }
                    else if (ch <= 0x7FF)
                    {
                        int high = (ch >> 6) | 0xC0;
                        int low = (ch & 0x3F) | 0x80;
                        writer.write('%');
                        writer.write(makeHHString(high));
                        writer.write('%');
                        writer.write(makeHHString(low));
                    }
                    {

                        int highSurrogate = ((int) ch) & 0x03FF;

                        int wwww = ((highSurrogate & 0x03C0) >> 6);
                        int uuuuu = wwww + 1;

                        int zzzz = (highSurrogate & 0x003C) >> 2;

                        int yyyyyy = ((highSurrogate & 0x0003) << 4) & 0x30;

                        ch = chars[++i];

                        int lowSurrogate = ((int) ch) & 0x03FF;

                        yyyyyy = yyyyyy | ((lowSurrogate & 0x03C0) >> 6);

                        int xxxxxx = (lowSurrogate & 0x003F);

                        int byte2 =
                            0x80 | (((uuuuu & 0x03) << 4) & 0x30) | zzzz;
                        int byte3 = 0x80 | yyyyyy;
                        int byte4 = 0x80 | xxxxxx;

                        writer.write('%');
                        writer.write(makeHHString(byte1));
                        writer.write('%');
                        writer.write(makeHHString(byte2));
                        writer.write('%');
                        writer.write(makeHHString(byte3));
                        writer.write('%');
                        writer.write(makeHHString(byte4));
                    }
                    else
                    {
                        int middle = ((ch & 0x0FC0) >> 6) | 0x80;
                        int low = (ch & 0x3F) | 0x80;
                        writer.write('%');
                        writer.write(makeHHString(high));
                        writer.write('%');
                        writer.write(makeHHString(middle));
                        writer.write('%');
                        writer.write(makeHHString(low));
                    }

                }
                else if (escapingNotNeeded(ch))
                {
                    writer.write(ch);
                }
                else
                {
                    writer.write("&#");
                    writer.write(Integer.toString(ch));
                    writer.write(';');
                }
                cleanStart = i + 1;
            }
            else if (ch == '"')
            {




                if (cleanLength > 0)
                {
                    writer.write(chars, cleanStart, cleanLength);
                    cleanLength = 0;
                }   
                
                
                if (doURLEscaping)
                    writer.write("%22");
                else

                cleanStart = i + 1;    
            }
            else
            {
                cleanLength++;
            }
        }
        
        if (cleanLength > 1)
        {
            if (cleanStart == 0)
                writer.write(string);
            else
                writer.write(chars, cleanStart, cleanLength);
        }
        else if (cleanLength == 1)
        {
            writer.write(ch);
        }
    }

    /**
     * Writes the specified <var>string</var> after substituting <VAR>specials</VAR>,
     * and UTF-16 surrogates for character references <CODE>&amp;#xnn</CODE>.
     *
     * @param   string      String to convert to XML format.
     * @param   encoding    CURRENTLY NOT IMPLEMENTED.
     *
     * @throws org.xml.sax.SAXException
     */
    public void writeAttrString(
        final java.io.Writer writer, String string, String encoding)
        throws IOException
    {
        final int end = string.length();
        if (end > m_attrBuff.length)
        {
            m_attrBuff = new char[end * 2 + 1];
        }
        string.getChars(0, end, m_attrBuff, 0);
        final char[] chars = m_attrBuff;

        

        int cleanStart = 0;
        int cleanLength = 0;

        char ch = 0;
        for (int i = 0; i < end; i++)
        {
            ch = chars[i];

            if (escapingNotNeeded(ch) && (!m_charInfo.isSpecialAttrChar(ch)))
            {
                cleanLength++;
            }
            else if ('<' == ch || '>' == ch)
            {
            }
            else if (
                ('&' == ch) && ((i + 1) < end) && ('{' == chars[i + 1]))
            {
            }
            else
            {
                if (cleanLength > 0)
                {
                    writer.write(chars,cleanStart,cleanLength);
                    cleanLength = 0;
                }
                int pos = accumDefaultEntity(writer, ch, i, chars, end, false, false);

                if (i != pos)
                {
                    i = pos - 1;
                }
                else
                {
                    if (isUTF16Surrogate(ch))
                    {
 
                            writeUTF16Surrogate(ch, chars, i, end);
                    }


                    /*
                    else if ((ch < m_maxCharacter) && (m_maxCharacter == 0xFFFF)
                    && (ch != 160))
                    {
                    }
                    else
                    */
                    String entityName = m_charInfo.getEntityNameForChar(ch);
                    if (null != entityName)
                    {
                        writer.write('&');
                        writer.write(entityName);
                        writer.write(';');
                    }
                    else if (escapingNotNeeded(ch))
                    {
                    }
                    else
                    {
                        writer.write("&#");
                        writer.write(Integer.toString(ch));
                        writer.write(';');
                    }
                }
                cleanStart = i + 1;
            }
        
        if (cleanLength > 1)
        {
            if (cleanStart == 0)
                writer.write(string);
            else
                writer.write(chars, cleanStart, cleanLength);
        }
        else if (cleanLength == 1)
        {
            writer.write(ch);
        }
    }



    /**
     * Receive notification of character data.
     *
     * <p>The Parser will call this method to report each chunk of
     * character data.  SAX parsers may return all contiguous character
     * data in a single chunk, or they may split it into several
     * chunks; however, all of the characters in any single event
     * must come from the same external entity, so that the Locator
     * provides useful information.</p>
     *
     * <p>The application must not attempt to read from the array
     * outside of the specified range.</p>
     *
     * <p>Note that some parsers will report whitespace using the
     * ignorableWhitespace() method rather than this one (validating
     * parsers must do so).</p>
     *
     * @param chars The characters from the XML document.
     * @param start The start position in the array.
     * @param length The number of characters to read from the array.
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #ignorableWhitespace
     * @see org.xml.sax.Locator
     *
     * @throws org.xml.sax.SAXException
     */
    public final void characters(char chars[], int start, int length)
        throws org.xml.sax.SAXException
    {

        if (m_elemContext.m_isRaw)
        {
            try
            {
                if (m_elemContext.m_startTagOpen)
                {
                    closeStartTag();
                    m_elemContext.m_startTagOpen = false;
                }
                m_ispreserve = true;
                

                writeNormalizedChars(chars, start, length, false, m_lineSepUse);

                
                if (m_tracer != null)
                    super.fireCharEvent(chars, start, length);
                
                return;
            }
            catch (IOException ioe)
            {
                throw new org.xml.sax.SAXException(
                    XMLMessages.createXMLMessage(
                        XMLErrorResources.ER_OIERROR,
                        null),
                    ioe);
            }
        }
        else
        {
            super.characters(chars, start, length);
        }
    }

    /**
     *  Receive notification of cdata.
     *
     *  <p>The Parser will call this method to report each chunk of
     *  character data.  SAX parsers may return all contiguous character
     *  data in a single chunk, or they may split it into several
     *  chunks; however, all of the characters in any single event
     *  must come from the same external entity, so that the Locator
     *  provides useful information.</p>
     *
     *  <p>The application must not attempt to read from the array
     *  outside of the specified range.</p>
     *
     *  <p>Note that some parsers will report whitespace using the
     *  ignorableWhitespace() method rather than this one (validating
     *  parsers must do so).</p>
     *
     *  @param ch The characters from the XML document.
     *  @param start The start position in the array.
     *  @param length The number of characters to read from the array.
     *  @throws org.xml.sax.SAXException Any SAX exception, possibly
     *             wrapping another exception.
     *  @see #ignorableWhitespace
     *  @see org.xml.sax.Locator
     *
     * @throws org.xml.sax.SAXException
     */
    public final void cdata(char ch[], int start, int length)
        throws org.xml.sax.SAXException
    {

        if ((null != m_elemContext.m_elementName)
            && (m_elemContext.m_elementName.equalsIgnoreCase("SCRIPT")
                || m_elemContext.m_elementName.equalsIgnoreCase("STYLE")))
        {
            try
            {
                if (m_elemContext.m_startTagOpen)
                {
                    closeStartTag();
                    m_elemContext.m_startTagOpen = false;
                }

                m_ispreserve = true;

                if (shouldIndent())
                    indent();

                writeNormalizedChars(ch, start, length, true, m_lineSepUse);
            }
            catch (IOException ioe)
            {
                throw new org.xml.sax.SAXException(
                    XMLMessages.createXMLMessage(
                        XMLErrorResources.ER_OIERROR,
                        null),
                    ioe);
            }
        }
        else
        {
            super.cdata(ch, start, length);
        }
    }

    /**
     *  Receive notification of a processing instruction.
     *
     *  @param target The processing instruction target.
     *  @param data The processing instruction data, or null if
     *         none was supplied.
     *  @throws org.xml.sax.SAXException Any SAX exception, possibly
     *             wrapping another exception.
     *
     * @throws org.xml.sax.SAXException
     */
    public void processingInstruction(String target, String data)
        throws org.xml.sax.SAXException
    {

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
            if (m_elemContext.m_startTagOpen)
            {
                closeStartTag();
                m_elemContext.m_startTagOpen = false;
            }
            else if (m_needToCallStartDocument)
                startDocumentInternal();

            if (shouldIndent())
                indent();

            final java.io.Writer writer = m_writer;
            writer.write("<?");
            writer.write(target);

            if (data.length() > 0 && !Character.isSpaceChar(data.charAt(0)))
                writer.write(' '); 


            if (m_elemContext.m_currentElemDepth <= 0)
                outputLineSep();

            m_startNewLine = true;
            }
            catch(IOException e)
            {
                throw new SAXException(e);
            }
        }
               
        if (m_tracer != null)
            super.fireEscapingEvent(target, data);
     }

    /**
     * Receive notivication of a entityReference.
     *
     * @param name non-null reference to entity name string.
     *
     * @throws org.xml.sax.SAXException
     */
    public final void entityReference(String name)
        throws org.xml.sax.SAXException
    {
        try
        {

        final java.io.Writer writer = m_writer;
        writer.write('&');
        writer.write(name);
        writer.write(';');
        
        } catch(IOException e)
        {
            throw new SAXException(e);
        }
    }
    /**
     * @see org.apache.xml.serializer.ExtendedContentHandler#endElement(String)
     */
    public final void endElement(String elemName) throws SAXException
    {
        endElement(null, null, elemName);
    }

    /**
     * Process the attributes, which means to write out the currently
     * collected attributes to the writer. The attributes are not
     * cleared by this method
     * 
     * @param writer the writer to write processed attributes to.
     * @param nAttrs the number of attributes in m_attributes 
     * to be processed
     *
     * @throws org.xml.sax.SAXException
     */
    public void processAttributes(java.io.Writer writer, int nAttrs)
        throws IOException,SAXException
    {
            /* 
             * process the collected attributes
             */
            for (int i = 0; i < nAttrs; i++)
            {
                processAttribute(
                    writer,
                    m_attributes.getQName(i),
                    m_attributes.getValue(i),
                    m_elemContext.m_elementDesc);
            }
    }

    /**
     * For the enclosing elements starting tag write out out any attributes
     * followed by ">"
     *
     *@throws org.xml.sax.SAXException
     */
    protected void closeStartTag() throws SAXException
    {
            try
            {

            if (m_tracer != null)
                super.fireStartElem(m_elemContext.m_elementName);  
            
            int nAttrs = m_attributes.getLength();   
            if (nAttrs>0)
            {
                processAttributes(m_writer, nAttrs);
                m_attributes.clear();
            }

            m_writer.write('>');

            /* whether Xalan or XSLTC, we have the prefix mappings now, so
             * lets determine if the current element is specified in the cdata-
             * section-elements list.
             */
            if (m_cdataSectionElements != null) 
                m_elemContext.m_isCdataSection = isCdataSection();
            if (m_doIndent)
            {
                m_isprevtext = false;
                m_preserves.push(m_ispreserve);
            }
            
            }
            catch(IOException e)
            {
                throw new SAXException(e);
            }
    }
    /**
     * Initialize the serializer with the specified output stream and output
     * format. Must be called before calling any of the serialize methods.
     *
     * @param output The output stream to use
     * @param format The output format
     * @throws UnsupportedEncodingException The encoding specified   in the
     * output format is not supported
     */
    protected synchronized void init(OutputStream output, Properties format)
        throws UnsupportedEncodingException
    {
        if (null == format)
        {
            format = OutputPropertiesFactory.getDefaultMethodProperties(Method.HTML);
         }
        super.init(output,format, false);
    }
    
        /**
         * Specifies an output stream to which the document should be
         * serialized. This method should not be called while the
         * serializer is in the process of serializing a document.
         * <p>
         * The encoding specified in the output properties is used, or
         * if no encoding was specified, the default for the selected
         * output method.
         *
         * @param output The output stream
         */
        public void setOutputStream(OutputStream output)
        {

            try
            {
                Properties format;
                if (null == m_format)
                    format = OutputPropertiesFactory.getDefaultMethodProperties(Method.HTML);
                else
                    format = m_format;
                init(output, format, true);
            }
            catch (UnsupportedEncodingException uee)
            {

            }
        }    
        /**
         * This method is used when a prefix/uri namespace mapping
         * is indicated after the element was started with a
         * startElement() and before and endElement().
         * startPrefixMapping(prefix,uri) would be used before the
         * startElement() call.
         * @param uri the URI of the namespace
         * @param prefix the prefix associated with the given URI.
         *
         * @see org.apache.xml.serializer.ExtendedContentHandler#namespaceAfterStartElement(String, String)
         */
        public void namespaceAfterStartElement(String prefix, String uri)
            throws SAXException
        {
            if (m_elemContext.m_elementURI == null)
            {
                String prefix1 = getPrefixPart(m_elemContext.m_elementName);
                if (prefix1 == null && EMPTYSTRING.equals(prefix))
                {
                    m_elemContext.m_elementURI = uri;
                }
            }            
            startPrefixMapping(prefix,uri,false);
        }

    public void startDTD(String name, String publicId, String systemId)
        throws SAXException
    {
        m_inDTD = true;
        super.startDTD(name, publicId, systemId);
    }

    /**
     * Report the end of DTD declarations.
     * @throws org.xml.sax.SAXException The application may raise an exception.
     * @see #startDTD
     */
    public void endDTD() throws org.xml.sax.SAXException
    {
        m_inDTD = false;
        /* for ToHTMLStream the DOCTYPE is entirely output in the
         * startDocumentInternal() method, so don't do anything here
         */
    }
    /**
     * This method does nothing.
     */
    public void attributeDecl(
        String eName,
        String aName,
        String type,
        String valueDefault,
        String value)
        throws SAXException
    {
    }

    /**
     * This method does nothing.
     */
    public void elementDecl(String name, String model) throws SAXException
    {
    }
    /**
     * This method does nothing.
     */
    public void internalEntityDecl(String name, String value)
        throws SAXException
    {
    }
    /**
     * This method does nothing.
     */
    public void externalEntityDecl(
        String name,
        String publicId,
        String systemId)
        throws SAXException
    {
    }

    /**
     * This method is used to add an attribute to the currently open element. 
     * The caller has guaranted that this attribute is unique, which means that it
     * not been seen before and will not be seen again.
     * 
     * @param name the qualified name of the attribute
     * @param value the value of the attribute which can contain only
     * ASCII printable characters characters in the range 32 to 127 inclusive.
     * @param flags the bit values of this integer give optimization information.
     */
    public void addUniqueAttribute(String name, String value, int flags)
        throws SAXException
    {
        try
        {
            final java.io.Writer writer = m_writer;
            if ((flags & NO_BAD_CHARS) > 0 && m_htmlcharInfo.onlyQuotAmpLtGt)
            {
                writer.write(' ');
                writer.write(name);
                writer.write("=\"");
                writer.write(value);
                writer.write('"');
            }
            else if (
                (flags & HTML_ATTREMPTY) > 0
                    && (value.length() == 0 || value.equalsIgnoreCase(name)))
            {
                writer.write(' ');
                writer.write(name);
            }
            else
            {
                writer.write(' ');
                writer.write(name);
                writer.write("=\"");
                if ((flags & HTML_ATTRURL) > 0)
                {
                    writeAttrURI(writer, value, m_specialEscapeURLs);
                }
                else
                {
                    writeAttrString(writer, value, this.getEncoding());
                }
                writer.write('"');
            }
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    public void comment(char ch[], int start, int length)
            throws SAXException
    {
        if (m_inDTD)
            return;
        super.comment(ch, start, length);
    }
    
    public boolean reset()
    {
        boolean ret = super.reset();
        if (!ret)
            return false;
        initToHTMLStream();
        return true;        
    }
    
    private void initToHTMLStream()
    {
        m_inBlockElem = false;
        m_inDTD = false;
        m_omitMetaTag = false;
        m_specialEscapeURLs = true;     
    }
}
