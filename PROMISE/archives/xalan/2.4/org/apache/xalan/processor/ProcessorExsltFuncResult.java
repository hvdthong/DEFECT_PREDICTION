package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemExsltFunction;
import org.apache.xalan.templates.ElemExsltFuncResult;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemFallback;
import org.apache.xalan.res.XSLTErrorResources;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.NoSuchMethodException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;

import java.util.Vector;

/**
 * <meta name="usage" content="internal"/>
 * This class processes parse events for an exslt func:result element.
 */
public class ProcessorExsltFuncResult extends ProcessorTemplateElem
{
  
  /**
   * Verify that the func:result element does not appear within a variable,
   * parameter, or another func:result, and that it belongs to a func:function 
   * element.
   */
  public void startElement(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws SAXException
  {
    String msg = "";

    super.startElement(handler, uri, localName, rawName, attributes);
    ElemTemplateElement ancestor = handler.getElemTemplateElement().getParentElem();
    while (ancestor != null && !(ancestor instanceof ElemExsltFunction))
    {
      if (ancestor instanceof ElemVariable 
          || ancestor instanceof ElemParam
          || ancestor instanceof ElemExsltFuncResult)
      {
        msg = "func:result cannot appear within a variable, parameter, or another func:result.";
        handler.error(msg, new SAXException(msg));
      }
      ancestor = ancestor.getParentElem();
    }
    if (ancestor == null)
    {
      msg = "func:result must appear in a func:function element";
      handler.error(msg, new SAXException(msg));
    }
  }
}
