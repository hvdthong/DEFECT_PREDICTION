package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.templates.ElemElement;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemExsltFunction;
import org.apache.xalan.templates.ElemExsltFuncResult;
import org.apache.xalan.templates.ElemFallback;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemValueOf;
import org.apache.xalan.templates.Constants;
import org.apache.xpath.XPath;
import org.apache.xalan.templates.StylesheetRoot;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.xalan.res.XSLTErrorResources;


/**
 * <meta name="usage" content="internal"/>
 * This class processes parse events for an exslt func:function element.
 */
public class ProcessorExsltFunction extends ProcessorTemplateElem
{
  /**
   * Start an ElemExsltFunction. Verify that it is top level and that it has a name attribute with a
   * namespace.
   */
  public void startElement(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws SAXException
  {
    String msg = "";
    if (!(handler.getElemTemplateElement() instanceof StylesheetRoot))
    {
      msg = "func:function element must be top level.";
      handler.error(msg, new SAXException(msg));
    }
    super.startElement(handler, uri, localName, rawName, attributes);
       
    String val = attributes.getValue("name");
    int indexOfColon = val.indexOf(":");
    if (indexOfColon > 0)
    {
      String prefix = val.substring(0, indexOfColon);
      String localVal = val.substring(indexOfColon + 1);
      String ns = handler.getNamespaceSupport().getURI(prefix);
    }
    else
    {
      msg = "func:function name must have namespace";
      handler.error(msg, new SAXException(msg));
    }
  }
  
  /**
   * Must include; super doesn't suffice!
   */
  protected void appendAndPush(
          StylesheetHandler handler, ElemTemplateElement elem)
            throws SAXException
  {
    super.appendAndPush(handler, elem);
    elem.setDOMBackPointer(handler.getOriginatingNode());
    handler.getStylesheet().setTemplate((ElemTemplate) elem);
  }
    
  /**
   * End an ElemExsltFunction, and verify its validity.
   */
  public void endElement(
          StylesheetHandler handler, String uri, String localName, String rawName)
            throws SAXException
  {
   ElemTemplateElement function = handler.getElemTemplateElement();
   SourceLocator locator = handler.getLocator();
   super.endElement(handler, uri, localName, rawName);   
  }
  
  /**
   * Non-recursive traversal of FunctionElement tree based on TreeWalker to verify that
   * there are no literal result elements except within a func:result element and that
   * the func:result element does not contain any following siblings except xsl:fallback.
   */
  public void validate(ElemTemplateElement elem, StylesheetHandler handler)
    throws SAXException
  {
    String msg = "";
    while (elem != null)
    { 
      if (elem instanceof ElemExsltFuncResult 
          && elem.getNextSiblingElem() != null 
          && !(elem.getNextSiblingElem() instanceof ElemFallback))
      {
        msg = "func:result has an illegal following sibling (only xsl:fallback allowed)";
        handler.error(msg, new SAXException(msg));
      }
      if (elem instanceof ElemValueOf ||
          (elem instanceof ElemLiteralResult || elem instanceof ElemElement)
          && !(ancestorIsOk(elem)))
      {
        msg ="misplaced literal result in a func:function container.";
        handler.error(msg, new SAXException(msg));
      }
      ElemTemplateElement nextElem = elem.getFirstChildElem();
      while (nextElem == null)
      {
        nextElem = elem.getNextSiblingElem();
        if (nextElem == null)
          elem = elem.getParentElem();
        if (elem == null || elem instanceof ElemExsltFunction)
      }  
      elem = nextElem;
    }
  }
  
  /**
   * Verify that a literal result belongs to a result element, a variable, 
   * or a parameter.
   */
  
  boolean ancestorIsOk(ElemTemplateElement child)
  {
    while (child.getParentElem() != null && !(child.getParentElem() instanceof ElemExsltFunction))
    {
      ElemTemplateElement parent = child.getParentElem();
      if (parent instanceof ElemExsltFuncResult 
          || parent instanceof ElemVariable
          || parent instanceof ElemParam)
        return true;
      child = parent;      
    }
    return false;
  }
  
}
