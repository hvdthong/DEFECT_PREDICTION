package org.apache.xalan.serialize;

import java.util.Stack;

import java.io.Writer;
import java.io.IOException;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Properties;
import java.util.BitSet;

import org.xml.sax.*;

import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.Trie;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.res.XPATHErrorResources;
import org.apache.xml.utils.StringToIntTable;
import org.apache.xalan.templates.OutputProperties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;

/**
 * <meta name="usage" content="general"/>
 * SerializerToHTML formats SAX-style events into XML.
 */
public class SerializerToHTML extends SerializerToXML
{

  /** State stack to keep track of if the current element has output 
   *  escaping disabled. */
  protected BoolStack m_isRawStack = new BoolStack();

  /** True if the current element is a block element.  (seems like 
   *  this needs to be a stack. -sb). */
  private boolean m_inBlockElem = false;

  /**
   * Map that tells which XML characters should have special treatment, and it
   *  provides character to entity name lookup.
   */
  protected static CharInfo m_htmlcharInfo =
    new CharInfo(CharInfo.HTML_ENTITIES_RESOURCE);

  /** A digital search trie for fast, case insensitive lookup of ElemDesc objects. */
  static Trie m_elementFlags = new Trie();

  static
  {

    m_elementFlags.put("BASEFONT", new ElemDesc(0 | ElemDesc.EMPTY));
    m_elementFlags.put("FRAME",
                       new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
    m_elementFlags.put("FRAMESET", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("NOFRAMES", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("ISINDEX",
                       new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
    m_elementFlags.put("APPLET",
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
    m_elementFlags.put("SUP",
                       new ElemDesc(0 | ElemDesc.SPECIAL
                                    | ElemDesc.ASPECIAL));
    m_elementFlags.put("SUB",
                       new ElemDesc(0 | ElemDesc.SPECIAL
                                    | ElemDesc.ASPECIAL));
    m_elementFlags.put("SPAN",
                       new ElemDesc(0 | ElemDesc.SPECIAL
                                    | ElemDesc.ASPECIAL));
    m_elementFlags.put("BDO",
                       new ElemDesc(0 | ElemDesc.SPECIAL
                                    | ElemDesc.ASPECIAL));
    m_elementFlags.put("BR",
                       new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL
                                    | ElemDesc.EMPTY | ElemDesc.BLOCK));
    m_elementFlags.put("BODY", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("ADDRESS",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                    | ElemDesc.BLOCKFORMFIELDSET));
    m_elementFlags.put("DIV",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                    | ElemDesc.BLOCKFORMFIELDSET));
    m_elementFlags.put("A", new ElemDesc(0 | ElemDesc.SPECIAL));
    m_elementFlags.put("MAP",
                       new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL
                                    | ElemDesc.BLOCK));
    m_elementFlags.put("AREA",
                       new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
    m_elementFlags.put("LINK",
                       new ElemDesc(0 | ElemDesc.HEADMISC | ElemDesc.EMPTY
                                    | ElemDesc.BLOCK));
    m_elementFlags.put("IMG",
                       new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL
                                    | ElemDesc.EMPTY
                                    | ElemDesc.WHITESPACESENSITIVE));
    m_elementFlags.put("OBJECT",
                       new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL
                                    | ElemDesc.HEADMISC
                                    | ElemDesc.WHITESPACESENSITIVE));
    m_elementFlags.put("PARAM", new ElemDesc(0 | ElemDesc.EMPTY));
    m_elementFlags.put("HR",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                    | ElemDesc.BLOCKFORMFIELDSET
                                    | ElemDesc.EMPTY));
    m_elementFlags.put("P",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                    | ElemDesc.BLOCKFORMFIELDSET));
    m_elementFlags.put("H1",
                       new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
    m_elementFlags.put("H2",
                       new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
    m_elementFlags.put("H3",
                       new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
    m_elementFlags.put("H4",
                       new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
    m_elementFlags.put("H5",
                       new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
    m_elementFlags.put("H6",
                       new ElemDesc(0 | ElemDesc.HEAD | ElemDesc.BLOCK));
    m_elementFlags.put("PRE",
                       new ElemDesc(0 | ElemDesc.PREFORMATTED
                                    | ElemDesc.BLOCK));
    m_elementFlags.put("Q",
                       new ElemDesc(0 | ElemDesc.SPECIAL
                                    | ElemDesc.ASPECIAL));
    m_elementFlags.put("BLOCKQUOTE",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                    | ElemDesc.BLOCKFORMFIELDSET));
    m_elementFlags.put("INS", new ElemDesc(0));
    m_elementFlags.put("DEL", new ElemDesc(0));
    m_elementFlags.put("DL",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                    | ElemDesc.BLOCKFORMFIELDSET));
    m_elementFlags.put("DT", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("DD", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("OL",
                       new ElemDesc(0 | ElemDesc.LIST | ElemDesc.BLOCK));
    m_elementFlags.put("UL",
                       new ElemDesc(0 | ElemDesc.LIST | ElemDesc.BLOCK));
    m_elementFlags.put("LI", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("FORM", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("LABEL", new ElemDesc(0 | ElemDesc.FORMCTRL));
    m_elementFlags.put("INPUT",
                       new ElemDesc(0 | ElemDesc.FORMCTRL
                                    | ElemDesc.INLINELABEL | ElemDesc.EMPTY));
    m_elementFlags.put("SELECT",
                       new ElemDesc(0 | ElemDesc.FORMCTRL
                                    | ElemDesc.INLINELABEL));
    m_elementFlags.put("OPTGROUP", new ElemDesc(0));
    m_elementFlags.put("OPTION", new ElemDesc(0));
    m_elementFlags.put("TEXTAREA",
                       new ElemDesc(0 | ElemDesc.FORMCTRL
                                    | ElemDesc.INLINELABEL));
    m_elementFlags.put("FIELDSET",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM));
    m_elementFlags.put("LEGEND", new ElemDesc(0));
    m_elementFlags.put("BUTTON",
                       new ElemDesc(0 | ElemDesc.FORMCTRL
                                    | ElemDesc.INLINELABEL));
    m_elementFlags.put("TABLE",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                    | ElemDesc.BLOCKFORMFIELDSET));
    m_elementFlags.put("CAPTION", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("THEAD", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("TFOOT", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("TBODY", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("COLGROUP", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("COL",
                       new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
    m_elementFlags.put("TR", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("TH", new ElemDesc(0));
    m_elementFlags.put("TD", new ElemDesc(0));
    m_elementFlags.put("HEAD",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.HEADELEM));
    m_elementFlags.put("TITLE", new ElemDesc(0 | ElemDesc.BLOCK));
    m_elementFlags.put("BASE",
                       new ElemDesc(0 | ElemDesc.EMPTY | ElemDesc.BLOCK));
    m_elementFlags.put("META",
                       new ElemDesc(0 | ElemDesc.HEADMISC | ElemDesc.EMPTY
                                    | ElemDesc.BLOCK));
    m_elementFlags.put("STYLE",
                       new ElemDesc(0 | ElemDesc.HEADMISC | ElemDesc.RAW
                                    | ElemDesc.BLOCK));
    m_elementFlags.put("SCRIPT",
                       new ElemDesc(0 | ElemDesc.SPECIAL | ElemDesc.ASPECIAL
                                    | ElemDesc.HEADMISC | ElemDesc.RAW));
    m_elementFlags.put("NOSCRIPT",
                       new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                    | ElemDesc.BLOCKFORMFIELDSET));
    m_elementFlags.put("HTML", new ElemDesc(0 | ElemDesc.BLOCK));

    m_elementFlags.put("FONT", new ElemDesc(0 | ElemDesc.FONTSTYLE));

    m_elementFlags.put("S", new ElemDesc(0 | ElemDesc.FONTSTYLE));
    m_elementFlags.put("STRIKE", new ElemDesc(0 | ElemDesc.FONTSTYLE));

    m_elementFlags.put("U", new ElemDesc(0 | ElemDesc.FONTSTYLE));

    m_elementFlags.put("NOBR", new ElemDesc(0 | ElemDesc.FONTSTYLE));
    
     m_elementFlags.put("IFRAME",
                                        new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                                                     | ElemDesc.BLOCKFORMFIELDSET));
     m_elementFlags.put("LAYER",
                                        new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
                                                                     | ElemDesc.BLOCKFORMFIELDSET));
     m_elementFlags.put("ILAYER",
                                        new ElemDesc(0 | ElemDesc.BLOCK | ElemDesc.BLOCKFORM
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
  static private ElemDesc m_dummy = new ElemDesc(0 | ElemDesc.BLOCK);

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
      OutputProperties.getBooleanProperty(OutputProperties.S_USE_URL_ESCAPING,
                                          format);

    m_omitMetaTag =
      OutputProperties.getBooleanProperty(OutputProperties.S_OMIT_META_TAG,
                                          format);
                            
    super.setOutputFormat(format);
  }

  /**
   * Tells if the formatter should use special URL escaping.
   *
   * @return True if URLs should be specially escaped with the %xx form.
   */
  public boolean getSpecialEscapeURLs()
  {
    return m_specialEscapeURLs;
  }

  /**
   * Tells if the formatter should omit the META tag.
   *
   * @return True if the META tag should be omitted.
   */
  public boolean getOmitMetaTag()
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
  ElemDesc getElemDesc(String name)
  {

    if (null != name)
    {
      Object obj = m_elementFlags.get(name);

      if (null != obj)
        return (ElemDesc) obj;
    }

    return m_dummy;
  }

  /**
   * Default constructor.
   */
  public SerializerToHTML()
  {

    super();
    m_charInfo = m_htmlcharInfo;
  }

  /** The name of the current element. */
  private String m_currentElementName = null;

  /**
   * Receive notification of the beginning of a document.
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   *
   * @throws org.xml.sax.SAXException
   */
  public void startDocument() throws org.xml.sax.SAXException
  {

    m_needToOutputDocTypeDecl = true;
    m_startNewLine = false;
    m_shouldNotWriteXMLHeader = true;

    if (true == m_needToOutputDocTypeDecl)
    {
      if ((null != m_doctypeSystem) || (null != m_doctypePublic))
      {
        accum("<!DOCTYPE HTML");

        if (null != m_doctypePublic)
        {
          accum(" PUBLIC \"");
          accum(m_doctypePublic);
          accum("\"");
        }

        if (null != m_doctypeSystem)
        {
          if (null == m_doctypePublic)
            accum(" SYSTEM \"");
          else
            accum(" \"");

          accum(m_doctypeSystem);
          accum("\"");
        }

        accum(">");
        outputLineSep();
      }
    }

    m_needToOutputDocTypeDecl = false;
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
          String namespaceURI, String localName, String name, Attributes atts)
            throws org.xml.sax.SAXException
  {

    if (null != namespaceURI && namespaceURI.length() > 0)
    {
      super.startElement(namespaceURI, localName, name, atts);

      return;
    }

    boolean savedDoIndent = m_doIndent;
    boolean noLineBreak;

    writeParentTagEnd();
    pushState(
      namespaceURI, localName,
      m_cdataSectionNames, m_cdataSectionStates);

    ElemDesc elemDesc = getElemDesc(name);

    boolean isBlockElement = elemDesc.is(ElemDesc.BLOCK);
    boolean isHeadElement = elemDesc.is(ElemDesc.HEADELEM);

    if (m_ispreserve)
      m_ispreserve = false;
    else if (m_doIndent && (null != m_currentElementName)
             && (!m_inBlockElem || isBlockElement)

    /* && !isWhiteSpaceSensitive */
    )
    {
      m_startNewLine = true;

      indent(m_currentIndent);
    }

    m_inBlockElem = !isBlockElement;

    m_isRawStack.push(elemDesc.is(ElemDesc.RAW));

    m_currentElementName = name;

    this.accum('<');
    this.accum(name);

    int nAttrs = atts.getLength();

    for (int i = 0; i < nAttrs; i++)
    {
      processAttribute(atts.getQName(i), elemDesc, atts.getValue(i));
    }

    openElementForChildren();

    m_currentIndent += this.m_indentAmount;
    m_isprevtext = false;
    m_doIndent = savedDoIndent;

    if (isHeadElement)
    {
      writeParentTagEnd();

      if (!m_omitMetaTag)
      {
        if (m_doIndent)
          indent(m_currentIndent);

        accum(
          "<META http-equiv=\"Content-Type\" content=\"text/html; charset=");

        String encoding = Encodings.getMimeEncoding(m_encoding);

        accum(encoding);
        accum('"');
        accum('>');
      }
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
  public void endElement(String namespaceURI, String localName, String name)
          throws org.xml.sax.SAXException
  {
    if (null != namespaceURI && namespaceURI.length() > 0)
    {
      super.endElement(namespaceURI, localName, name);

      return;
    }

    m_currentIndent -= this.m_indentAmount;

    boolean hasChildNodes = childNodesWereAdded();

    m_isRawStack.pop();

    ElemDesc elemDesc = getElemDesc(name);

    boolean isBlockElement = elemDesc.is(ElemDesc.BLOCK);
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

    m_inBlockElem = !isBlockElement;

    if (hasChildNodes)
    {
      if (shouldIndent)
        indent(m_currentIndent);

      this.accum("</");
      this.accum(name);
      this.accum('>');

      m_currentElementName = name;
    }
    else
    {
      if (!elemDesc.is(ElemDesc.EMPTY))
      {
        this.accum('>');


        this.accum('<');
        this.accum('/');
        this.accum(name);
        this.accum('>');
      }
      else
      {
        this.accum('>');
      }
    }

    if (elemDesc.is(ElemDesc.WHITESPACESENSITIVE))
      m_ispreserve = true;

    if (hasChildNodes)
    {
      if (!m_preserves.isEmpty())
        m_preserves.pop();
    }

    m_isprevtext = false;

    m_cdataSectionStates.pop();
  }

  /**
   * Process an attribute.
   * @param   name   The name of the attribute.
   * @param elemDesc non-null reference to the owning element description.
   * @param   value   The value of the attribute.
   *
   * @throws org.xml.sax.SAXException
   */
  protected void processAttribute(
          String name, ElemDesc elemDesc, String value)
            throws org.xml.sax.SAXException
  {

    this.accum(' ');

    if (((value.length() == 0) || value.equalsIgnoreCase(name))
            && elemDesc.isAttrFlagSet(name, ElemDesc.ATTREMPTY))
    {
      this.accum(name);
    }
    else
    {
      this.accum(name);
      this.accum('=');

      this.accum('\"');
      if (elemDesc.isAttrFlagSet(name, ElemDesc.ATTRURL))
        writeAttrURI(value, m_specialEscapeURLs);
      else
        writeAttrString(value, this.m_encoding);
      this.accum('\"');

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
  private String makeHHString(int i)
  {
    String s = Integer.toHexString(i).toUpperCase();
    if(s.length() == 1)
    {
      s = "0"+s;
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
    try {
      char r = (char)Integer.parseInt(str,16);
    } catch (NumberFormatException e) {
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
  public void writeAttrURI(String string, boolean doURLEscaping)
          throws org.xml.sax.SAXException
  {

    char[] stringArray = string.toCharArray();
    int len = stringArray.length;
        
    for (int i = 0; i < len; i++)
    {
      char ch = stringArray[i];

      if ((ch < 32) || (ch > 126))
      {
        if (doURLEscaping)
        {
          
          if(ch <= 0x7F)
          {
            accum('%');
            accum(makeHHString(ch));          
          }
          else if(ch <= 0x7FF)
          {
            int high = (ch >> 6) | 0xC0;  
            accum('%');
            accum(makeHHString(high));
            accum('%');
            accum(makeHHString(low));
          }
          {
            
            int highSurrogate = ((int) ch) & 0x03FF;
            
            int wwww = ((highSurrogate & 0x03C0) >> 6);
            int uuuuu = wwww+1;  
            
            int zzzz = (highSurrogate & 0x003C) >> 2;
            
            int yyyyyy = ((highSurrogate & 0x0003) << 4) & 0x30;
            
            ch = stringArray[++i];
            
            int lowSurrogate = ((int) ch) & 0x03FF;
            
            yyyyyy = yyyyyy | ((lowSurrogate & 0x03C0) >> 6);
            
            int xxxxxx = (lowSurrogate & 0x003F);
            
            int byte2 = 0x80 | (((uuuuu & 0x03) << 4) & 0x30) | zzzz;
            int byte3 = 0x80 | yyyyyy;
            int byte4 = 0x80 | xxxxxx;
            
            accum('%');
            accum(makeHHString(byte1));
            accum('%');
            accum(makeHHString(byte2));
            accum('%');
            accum(makeHHString(byte3));
            accum('%');
            accum(makeHHString(byte4));
          }
          else 
          {
            accum('%');
            accum(makeHHString(high));
            accum('%');
            accum(makeHHString(middle));
            accum('%');
            accum(makeHHString(low));
          }

        }
        else if (canConvert(ch))
        {
          accum(ch);
        }
        else
        {
          accum("&#");
          accum(Integer.toString(ch));
          accum(';');
        }
      }
      else if('%' == ch)
      {
          
          
        
       /* if ( ((i+2) < len) && isHHSign(new String(stringArray,i+1,2)) )
        {
          accum(ch);
        }
        else
        {
          if (doURLEscaping)
          {
           accum('%');
           accum(makeHHString(ch));
          }
          else*/
            accum(ch);
               
      } 
      else if (ch == '"')
      {
        if (doURLEscaping)
          accum("%22");
        else
      }
      else
      {
        accum(ch);
      }
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
  public void writeAttrString(String string, String encoding)
          throws org.xml.sax.SAXException
  {

    final char chars[] = string.toCharArray();
    final int strLen = chars.length;

    for (int i = 0; i < strLen; i++)
    {
      char ch = chars[i];

      if (canConvert(ch) && (!m_charInfo.isSpecial(ch)))
      {
        accum(ch);
      }
      else if ('<' == ch || '>' == ch)
      {
      }
      else if (('&' == ch) && ((i + 1) < strLen) && ('{' == chars[i + 1]))
      {
      }
      else
      {
        int pos = accumDefaultEntity(ch, i, chars, strLen, false);

        if (i != pos)
        {
          i = pos - 1;
        }
        else
        {
          if (isUTF16Surrogate(ch))
          {
            try
            {
              i = writeUTF16Surrogate(ch, chars, i, strLen);
            }
            catch(IOException ioe)
            {
              throw new SAXException(ioe);
            }
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
            accum('&');
            accum(entityName);
            accum(';');
          }
          else if (canConvert(ch))
          {
          }
          else
          {
            accum("&#");
            accum(Integer.toString(ch));
            accum(';');
          }
        }
      }
    }
  }

  /**
   * Copy an entity into the accumulation buffer.
   *
   * @param s The name of the entity.
   * @param pos unused.
   *
   * @return The pos argument.
   *
   * @throws org.xml.sax.SAXException
   */
  private int copyEntityIntoBuf(String s, int pos)
          throws org.xml.sax.SAXException
  {

    int l = s.length();

    accum('&');

    for (int i = 0; i < l; i++)
    {
      accum(s.charAt(i));
    }

    accum(';');

    return pos;
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
  public void characters(char chars[], int start, int length)
          throws org.xml.sax.SAXException
  {

    if (m_isRawStack.peekOrFalse())
    {
      try
      {
        writeParentTagEnd();

        m_ispreserve = true;

        if (shouldIndent())
          indent(m_currentIndent);

        writeNormalizedChars(chars, start, length, false);

        return;
      }
      catch (IOException ioe)
      {
        throw new org.xml.sax.SAXException(
          XSLMessages.createXPATHMessage(
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
  public void cdata(char ch[], int start, int length)
          throws org.xml.sax.SAXException
  {

    if ((null != m_currentElementName)
            && (m_currentElementName.equalsIgnoreCase("SCRIPT")
                || m_currentElementName.equalsIgnoreCase("STYLE")))
    {
      try
      {
        writeParentTagEnd();

        m_ispreserve = true;

        if (shouldIndent())
          indent(m_currentIndent);

        writeNormalizedChars(ch, start, length, true);
      }
      catch (IOException ioe)
      {
        throw new org.xml.sax.SAXException(
          XSLMessages.createXPATHMessage(
      }
    }

    /*
    {
      try
      {
        writeParentTagEnd();
        m_ispreserve = true;
        if (shouldIndent())
          indent(m_currentIndent);
        this.accum(ch, start, length);
      }
      catch(IOException ioe)
      {
      }
    }
    */
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
      writeParentTagEnd();

      if (shouldIndent())
        indent(m_currentIndent);

      this.accum("<?" + target);

      if (data.length() > 0 &&!Character.isSpaceChar(data.charAt(0)))
        this.accum(" ");

      
      if (m_elemStack.isEmpty())
         outputLineSep();

      m_startNewLine = true;
    }
  }

  /**
   * Receive notivication of a entityReference.
   *
   * @param name non-null reference to entity name string.
   *
   * @throws org.xml.sax.SAXException
   */
  public void entityReference(String name) throws org.xml.sax.SAXException
  {

    this.accum("&");
    this.accum(name);
    this.accum(";");
  }
}

