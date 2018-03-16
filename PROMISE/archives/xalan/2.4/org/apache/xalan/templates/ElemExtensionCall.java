package org.apache.xalan.templates;

import java.io.*;

import java.util.*;


import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.StringTokenizer;

import org.apache.xml.utils.QName;
import org.apache.xml.utils.NameSpace;
import org.apache.xml.utils.StringToStringTable;
import org.apache.xpath.XPathContext;
import org.apache.xml.utils.MutableAttrListImpl;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.XPathContext;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.extensions.ExtensionHandler;
import org.apache.xalan.extensions.ExtensionsTable;
import org.apache.xalan.transformer.TransformerImpl;

import javax.xml.transform.TransformerException;

import org.apache.xml.dtm.DTM;

/**
 * <meta name="usage" content="advanced"/>
 * Implement an extension element.
 */
public class ElemExtensionCall extends ElemLiteralResult
{

  /** The Namespace URI for this extension call element.
   *  @serial          */
  String m_extns;

  /** Language used by extension.
   *  @serial          */
  String m_lang;

  /** URL pointing to extension.
   *  @serial          */
  String m_srcURL;

  /** Source for script.
   *  @serial          */
  String m_scriptSrc;

  /** Declaration for Extension element. 
   *  @serial          */
  ElemExtensionDecl m_decl = null;

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   *@return The token ID for this element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_EXTENSIONCALL;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */


  /**
   * This function is called after everything else has been
   * recomposed, and allows the template to set remaining
   * values that may be based on some other property that
   * depends on recomposition.
   */
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
    m_extns = this.getNamespace();   
    m_decl = getElemExtensionDecl(sroot, m_extns);
    if (m_decl == null)
      sroot.getExtensionNamespacesManager().registerExtension(m_extns);
  }
 
  /**
   * Return the ElemExtensionDecl for this extension element 
   *
   *
   * @param stylesheet Stylesheet root associated with this extension element
   * @param namespace Namespace associated with this extension element
   *
   * @return the ElemExtensionDecl for this extension element. 
   */
  private ElemExtensionDecl getElemExtensionDecl(StylesheetRoot stylesheet,
          String namespace)
  {

    ElemExtensionDecl decl = null;
    int n = stylesheet.getGlobalImportCount();

    for (int i = 0; i < n; i++)
    {
      Stylesheet imported = stylesheet.getGlobalImport(i);

      for (ElemTemplateElement child = imported.getFirstChildElem();
              child != null; child = child.getNextSiblingElem())
      {
        if (Constants.ELEMNAME_EXTENSIONDECL == child.getXSLToken())
        {
          decl = (ElemExtensionDecl) child;

          String prefix = decl.getPrefix();
          String declNamespace = child.getNamespaceForPrefix(prefix);

          if (namespace.equals(declNamespace))
          {
            return decl;
          }
        }
      }
    }

    return decl;
  }
  
  /**
   * Execute the fallbacks when an extension is not available.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void executeFallbacks(
          TransformerImpl transformer)
            throws TransformerException
  {
    for (ElemTemplateElement child = m_firstChild; child != null;
             child = child.m_nextSibling)
    {
      if (child.getXSLToken() == Constants.ELEMNAME_FALLBACK)
      {
        try
        {
          transformer.pushElemTemplateElement(child);
          ((ElemFallback) child).executeFallback(transformer);
        }
        finally
        {
          transformer.popElemTemplateElement();
        }
      }
    }

  }


  /**
   * Execute an extension.
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
      transformer.getResultTreeHandler().flushPending();

      ExtensionsTable etable = transformer.getExtensionsTable();
      ExtensionHandler nsh = etable.get(m_extns);

      if (null == nsh)
      {
        executeFallbacks(transformer);
        return;
      }

      try
      {
        nsh.processElement(this.getLocalName(), this, transformer,
                           getStylesheet(), this);
      }
      catch (Exception e)
      {

        String msg = e.getMessage();
        
        TransformerException te;
        if(e instanceof TransformerException)
        {
          te = (TransformerException)e;
        }
        else
        {
          if(null != msg)
            te = new TransformerException(e);
          else
        }
        if(null == te.getLocator())
          te.setLocator(this);

        if (null != msg)
        {
          if (msg.indexOf("fatal") >= 0)
          {
            transformer.getErrorListener().fatalError(te);
          }
          else if(e instanceof RuntimeException)      
          else
            transformer.getErrorListener().warning(te);

        }
        else      

        executeFallbacks(
          transformer);
      }
    }
    catch(org.xml.sax.SAXException se)
    {
      transformer.getErrorListener().fatalError(new TransformerException(se));
    }
  }

  /**
   * Return the raw value of the attribute.
   *
   * @param rawName Raw name of the attribute to get
   *
   * @return the raw value of the attribute or null if not found
   */
  public String getAttribute(String rawName)
  {

    AVT avt = getLiteralResultAttribute(rawName);

    if ((null != avt) && avt.getRawName().equals(rawName))
    {
      return avt.getSimpleString();
    }

    return null;
  }

  /**
   * Return the value of the attribute interpreted as an Attribute
   * Value Template (in other words, you can use curly expressions
   *
   * @param rawName Raw name of the attribute to get
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @return the value of the attribute
   *
   * @throws TransformerException
   */
  public String getAttribute(
          String rawName, org.w3c.dom.Node sourceNode, TransformerImpl transformer)
            throws TransformerException
  {

    AVT avt = getLiteralResultAttribute(rawName);

    if ((null != avt) && avt.getRawName().equals(rawName))
    {
      XPathContext xctxt = transformer.getXPathContext();

      return avt.evaluate(xctxt, 
            xctxt.getDTMHandleFromNode(sourceNode), 
            this);
    }

    return null;
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
  	return visitor.visitExtensionElement(this);
  }

  
}
