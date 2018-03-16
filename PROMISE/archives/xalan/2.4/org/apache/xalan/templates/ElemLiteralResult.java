package org.apache.xalan.templates;

import org.apache.xml.dtm.DTM;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.StringTokenizer;

import org.apache.xml.utils.QName;
import org.apache.xml.utils.NameSpace;
import org.apache.xpath.XPathContext;
import org.apache.xml.utils.StringToStringTable;
import org.apache.xml.utils.NameSpace;
import org.apache.xml.utils.StringVector;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.ResultTreeHandler;

import javax.xml.transform.TransformerException;

import java.io.*;

import java.util.*;

/**
 * <meta name="usage" content="advanced"/>
 * Implement a Literal Result Element.
 */
public class ElemLiteralResult extends ElemUse
{

  /**
   * Tells if this element represents a root element
   * that is also the stylesheet element.
   * TODO: This should be a derived class.
   * @serial
   */
  private boolean isLiteralResultAsStylesheet = false;

  /**
   * Set whether this element represents a root element
   * that is also the stylesheet element.
   *
   *
   * @param b boolean flag indicating whether this element
   * represents a root element that is also the stylesheet element.
   */
  public void setIsLiteralResultAsStylesheet(boolean b)
  {
    isLiteralResultAsStylesheet = b;
  }

  /**
   * Return whether this element represents a root element
   * that is also the stylesheet element.
   *
   *
   * @return boolean flag indicating whether this element
   * represents a root element that is also the stylesheet element.
   */
  public boolean getIsLiteralResultAsStylesheet()
  {
    return isLiteralResultAsStylesheet;
  }
  
  /**
   * This function is called after everything else has been
   * recomposed, and allows the template to set remaining
   * values that may be based on some other property that
   * depends on recomposition.
   */
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    java.util.Vector vnames = cstate.getVariableNames();
    if (null != m_avts)
    {
      int nAttrs = m_avts.size();

      for (int i = (nAttrs - 1); i >= 0; i--)
      {
        AVT avt = (AVT) m_avts.elementAt(i);
        avt.fixupVariables(vnames, cstate.getGlobalsSize());
      } 
    }   
  }
  
  /**
   * The created element node will have the attribute nodes
   * that were present on the element node in the stylesheet tree,
   * other than attributes with names in the XSLT namespace.
   * @serial
   */
  private Vector m_avts = null;

  /** List of attributes with the XSLT namespace.
   *  @serial */
  private Vector m_xslAttr = null;

  /**
   * Set a literal result attribute (AVTs only).
   *
   * @param avt literal result attribute to add (AVT only)
   */
  public void addLiteralResultAttribute(AVT avt)
  {

    if (null == m_avts)
      m_avts = new Vector();

    m_avts.addElement(avt);
  }

  /**
   * Set a literal result attribute (used for xsl attributes).
   *
   * @param att literal result attribute to add
   */
  public void addLiteralResultAttribute(String att)
  {

    if (null == m_xslAttr)
      m_xslAttr = new Vector();

    m_xslAttr.addElement(att);
  }
  
  /**
   * Set the "xml:space" attribute.
   * A text node is preserved if an ancestor element of the text node
   * has an xml:space attribute with a value of preserve, and
   * no closer ancestor element has xml:space with a value of default.
   *
   * @param v  Enumerated value, either Constants.ATTRVAL_PRESERVE 
   * or Constants.ATTRVAL_STRIP.
   */
  public void setXmlSpace(AVT avt)
  {
    addLiteralResultAttribute(avt);
    String val = avt.getSimpleString();
    if(val.equals("default"))
    {
      super.setXmlSpace(Constants.ATTRVAL_STRIP);
    }
    else if(val.equals("preserve"))
    {
      super.setXmlSpace(Constants.ATTRVAL_PRESERVE);
    }
  }


  /**
   * Get a literal result attribute by name.
   *
   * @param name Name of literal result attribute to get
   *
   * @return literal result attribute (AVT)
   */
  public AVT getLiteralResultAttribute(String name)
  {

    if (null != m_avts)
    {
      int nAttrs = m_avts.size();

      for (int i = (nAttrs - 1); i >= 0; i--)
      {
        AVT avt = (AVT) m_avts.elementAt(i);

        if (avt.getRawName().equals(name))
        {
          return avt;
        }
    }

    return null;
  }

  /**
   * Get whether or not the passed URL is flagged by
   * the "extension-element-prefixes" or "exclude-result-prefixes"
   * properties.
   *
   * @param prefix non-null reference to prefix that might be excluded.(not currently used)
   * @param uri reference to namespace that prefix maps to
   *
   * @return true if the prefix should normally be excluded.
   */
  public boolean containsExcludeResultPrefix(String prefix, String uri)
  {
    if (uri == null ||
				(null == m_excludeResultPrefixes &&
				 null == m_ExtensionElementURIs)
				)
      return super.containsExcludeResultPrefix(prefix, uri);

    if (prefix.length() == 0)
      prefix = Constants.ATTRVAL_DEFAULT_PREFIX;

		if(m_excludeResultPrefixes!=null)
			for (int i =0; i< m_excludeResultPrefixes.size(); i++)
			{
				if (uri.equals(getNamespaceForPrefix(m_excludeResultPrefixes.elementAt(i))))
					return true;
			}    
		
    if(m_ExtensionElementURIs!=null && m_ExtensionElementURIs.contains(uri))
       return true;

		return super.containsExcludeResultPrefix(prefix, uri);
  }

  /**
   * Augment resolvePrefixTables, resolving the namespace aliases once
   * the superclass has resolved the tables.
   *
   * @throws TransformerException
   */
  public void resolvePrefixTables() throws TransformerException
  {

    super.resolvePrefixTables();

    StylesheetRoot stylesheet = getStylesheetRoot();

    if ((null != m_namespace) && (m_namespace.length() > 0))
    {
      NamespaceAlias nsa = stylesheet.getNamespaceAliasComposed(m_namespace);

      if (null != nsa)
      {
        m_namespace = nsa.getResultNamespace();


        if ((null != resultPrefix) && (resultPrefix.length() > 0))
          m_rawName = resultPrefix + ":" + m_localName;
        else
          m_rawName = m_localName;
      }
    }

    if (null != m_avts)
    {
      int n = m_avts.size();

      for (int i = 0; i < n; i++)
      {
        AVT avt = (AVT) m_avts.elementAt(i);

        String ns = avt.getURI();

        if ((null != ns) && (ns.length() > 0))
        {
          NamespaceAlias nsa =

          if (null != nsa)
          {
            String namespace = nsa.getResultNamespace();

            String rawName = avt.getName();

            if ((null != resultPrefix) && (resultPrefix.length() > 0))
              rawName = resultPrefix + ":" + rawName;

            avt.setURI(namespace);
            avt.setRawName(rawName);
          }
        }
      }
    }
  }

  /**
   * Return whether we need to check namespace prefixes
   * against the exclude result prefixes or extensions lists.
   * Note that this will create a new prefix table if one
   * has not been created already.
   *
   * NEEDSDOC ($objectName$) @return
   */
  boolean needToCheckExclude()
  {
    if (null == m_excludeResultPrefixes && null == m_prefixTable
				)
      return false;
    else
    {

      if (null == m_prefixTable)
        m_prefixTable = new Vector();

      return true;
    }
  }

  /**
   * The namespace of the element to be created.
   * @serial
   */
  private String m_namespace;

  /**
   * Set the namespace URI of the result element to be created.
   * Note that after resolvePrefixTables has been called, this will
   * return the aliased result namespace, not the original stylesheet
   * namespace.
   *
   * @param ns The Namespace URI, or the empty string if the
   *        element has no Namespace URI.
   */
  public void setNamespace(String ns)
  {
      ns = "";
    m_namespace = ns;
  }

  /**
   * Get the original namespace of the Literal Result Element.
   * 
   * %REVIEW% Why isn't this overriding the getNamespaceURI method
   * rather than introducing a new one?
   *
   * @return The Namespace URI, or the empty string if the
   *        element has no Namespace URI.
   */
  public String getNamespace()
  {
    return m_namespace;
  }

  /**
   * The local name of the element to be created.
   * @serial
   */
  private String m_localName;

  /**
   * Set the local name of the LRE.
   *
   * @param localName The local name (without prefix) of the result element
   *                  to be created.
   */
  public void setLocalName(String localName)
  {
    m_localName = localName;
  }

  /**
   * Get the local name of the Literal Result Element.
   * Note that after resolvePrefixTables has been called, this will
   * return the aliased name prefix, not the original stylesheet
   * namespace prefix.
   *
   * @return The local name (without prefix) of the result element
   *                  to be created.
   */
  public String getLocalName()
  {
    return m_localName;
  }

  /**
   * The raw name of the element to be created.
   * @serial
   */
  private String m_rawName;

  /**
   * Set the raw name of the LRE.
   *
   * @param rawName The qualified name (with prefix), or the
   *        empty string if qualified names are not available.
   */
  public void setRawName(String rawName)
  {
    m_rawName = rawName;
  }

  /**
   * Get the raw name of the Literal Result Element.
   *
   * @return  The qualified name (with prefix), or the
   *        empty string if qualified names are not available.
   */
  public String getRawName()
  {
    return m_rawName;
  }
	
 /**
   * Get the prefix part of the raw name of the Literal Result Element.
   *
   * @return The prefix, or the empty string if noprefix was provided.
   */
  public String getPrefix()
  {
		int len=m_rawName.length()-m_localName.length()-1;
    return (len>0)
			? m_rawName.substring(0,len)
			: "";
  }


  /**
   * The "extension-element-prefixes" property, actually contains URIs.
   * @serial
   */
  private StringVector m_ExtensionElementURIs;

  /**
   * Set the "extension-element-prefixes" property.
   *
   * @param v Vector of URIs (not prefixes) to set as the "extension-element-prefixes" property
   */
  public void setExtensionElementPrefixes(StringVector v)
  {
    m_ExtensionElementURIs = v;
  }

  /**
   * Get an "extension-element-prefix" property.
   *
   * @param i Index of URI ("extension-element-prefix" property) to get
   *
   * @return URI at given index ("extension-element-prefix" property)
   *
   * @throws ArrayIndexOutOfBoundsException
   */
  public String getExtensionElementPrefix(int i)
          throws ArrayIndexOutOfBoundsException
  {

    if (null == m_ExtensionElementURIs)
      throw new ArrayIndexOutOfBoundsException();

    return m_ExtensionElementURIs.elementAt(i);
  }

  /**
   * Get the number of "extension-element-prefixes" Strings.
   *
   * @return the number of "extension-element-prefixes" Strings
   */
  public int getExtensionElementPrefixCount()
  {
    return (null != m_ExtensionElementURIs)
           ? m_ExtensionElementURIs.size() : 0;
  }

  /**
   * Find out if the given "extension-element-prefix" property is defined.
   *
   * @param uri The URI to find
   *
   * @return True if the given URI is found
   */
  public boolean containsExtensionElementURI(String uri)
  {

    if (null == m_ExtensionElementURIs)
      return false;

    return m_ExtensionElementURIs.contains(uri);
  }

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_LITERALRESULT;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {

    return m_rawName;
  }

  /**
   * The XSLT version as specified by this element.
   * @serial
   */
  private String m_version;

  /**
   * Set the "version" property.
   *
   * @param v Version property value to set
   */
  public void setVersion(String v)
  {
    m_version = v;
  }
  
  /**
   * Get the "version" property.
   *
   * @return Version property value
   */
  public String getVersion()
  {
    return m_version;
  }

  /**
   * The "exclude-result-prefixes" property.
   * @serial
   */
  private StringVector m_excludeResultPrefixes;

  /**
   * Set the "exclude-result-prefixes" property.
   * The designation of a namespace as an excluded namespace is
   * effective within the subtree of the stylesheet rooted at
   * the element bearing the exclude-result-prefixes or
   * xsl:exclude-result-prefixes attribute; a subtree rooted
   * at an xsl:stylesheet element does not include any stylesheets
   * imported or included by children of that xsl:stylesheet element.
   *
   * @param v vector of prefixes that are resolvable to strings.
   */
  public void setExcludeResultPrefixes(StringVector v)
  {
    m_excludeResultPrefixes = v;
  }

  /**
   * Tell if the result namespace decl should be excluded.  Should be called before
   * namespace aliasing (I think).
   *
   * @param prefix Prefix of namespace to check
   * @param uri URI of namespace to check
   *
   * @return True if the given namespace should be excluded
   *
   * @throws TransformerException
   */
  private boolean excludeResultNSDecl(String prefix, String uri)
          throws TransformerException
  {

    if (null != m_excludeResultPrefixes)
    {
      return containsExcludeResultPrefix(prefix, uri);
    }

    return false;
  }
  
  /**
   * Copy a Literal Result Element into the Result tree, copy the
   * non-excluded namespace attributes, copy the attributes not
   * of the XSLT namespace, and execute the children of the LRE.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {

    try
    {
      ResultTreeHandler rhandler = transformer.getResultTreeHandler();
			
			rhandler.startPrefixMapping(getPrefix(),getNamespace());

      executeNSDecls(transformer);
      rhandler.startElement(getNamespace(), getLocalName(), getRawName(), null);

      try
      {

        super.execute(transformer);

        if (null != m_avts)
        {
          int nAttrs = m_avts.size();

          for (int i = (nAttrs - 1); i >= 0; i--)
          {
            AVT avt = (AVT) m_avts.elementAt(i);
            XPathContext xctxt = transformer.getXPathContext();
            int sourceNode = xctxt.getCurrentNode();
            String stringedValue = avt.evaluate(xctxt, sourceNode, this);

            if (null != stringedValue)
            {

							
              rhandler.addAttribute(avt.getURI(), avt.getName(),
                                    avt.getRawName(), "CDATA", stringedValue);
            }
        }

        transformer.executeChildTemplates(this, true);
      }
      finally
      {
        rhandler.endElement(getNamespace(), getLocalName(), getRawName());
        unexecuteNSDecls(transformer);
				
				rhandler.endPrefixMapping(getPrefix());
      }
    }
    catch (org.xml.sax.SAXException se)
    {
      throw new TransformerException(se);
    }
  }

  /**
   * Compiling templates requires that we be able to list the AVTs
   * ADDED 9/5/2000 to support compilation experiment
   *
   * @return an Enumeration of the literal result attributes associated
   * with this element.
   */
  public Enumeration enumerateLiteralResultAttributes()
  {
    return (null == m_avts) ? null : m_avts.elements();
  }
  
    /**
     * Accept a visitor and call the appropriate method 
     * for this class.
     * 
     * @param visitor The visitor whose appropriate method will be called.
     * @return true if the children of the object should be visited.
     */
    protected boolean accept(XSLTVisitor visitor)
    {
      return visitor.visitLiteralResultElement(this);
    }

    /**
     * Call the children visitors.
     * @param visitor The visitor whose appropriate method will be called.
     */
    protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
    {
      if (callAttrs && null != m_avts)
      {
        int nAttrs = m_avts.size();

        for (int i = (nAttrs - 1); i >= 0; i--)
        {
          AVT avt = (AVT) m_avts.elementAt(i);
          avt.callVisitors(visitor);
        }
      }
      super.callChildVisitors(visitor, callAttrs);
    }

}
