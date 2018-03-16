package org.apache.xalan.processor;

import org.apache.xalan.templates.KeyDeclaration;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;

import java.util.Vector;

/**
 * TransformerFactory for xsl:key markup.
 * <pre>
 * <!ELEMENT xsl:key EMPTY>
 * <!ATTLIST xsl:key
 *   name %qname; #REQUIRED
 *   match %pattern; #REQUIRED
 *   use %expr; #REQUIRED
 * >
 * </pre>
 */
class ProcessorKey extends XSLTElementProcessor
{

  /**
   * Receive notification of the start of an xsl:key element.
   *
   * @param handler The calling StylesheetHandler/TemplatesBuilder.
   * @param uri The Namespace URI, or the empty string if the
   *        element has no Namespace URI or if Namespace
   *        processing is not being performed.
   * @param localName The local name (without prefix), or the
   *        empty string if Namespace processing is not being
   *        performed.
   * @param rawName The raw XML 1.0 name (with prefix), or the
   *        empty string if raw names are not available.
   * @param attributes The attributes attached to the element.  If
   *        there are no attributes, it shall be an empty
   *        Attributes object.
   */
  public void startElement(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws org.xml.sax.SAXException
  {

    KeyDeclaration kd = new KeyDeclaration(handler.getStylesheet(), handler.nextUid());

    kd.setDOMBackPointer(handler.getOriginatingNode());
    kd.setLocaterInfo(handler.getLocator());
    setPropertiesFromAttributes(handler, rawName, attributes, kd);
    handler.getStylesheet().setKey(kd);
  }

  /**
   * Set the properties of an object from the given attribute list.
   * @param handler The stylesheet's Content handler, needed for
   *                error reporting.
   * @param rawName The raw name of the owner element, needed for
   *                error reporting.
   * @param attributes The list of attributes.
   * @param target The target element where the properties will be set.
   */
  void setPropertiesFromAttributes(
          StylesheetHandler handler, String rawName, Attributes attributes, 
          org.apache.xalan.templates.ElemTemplateElement target)
            throws org.xml.sax.SAXException
  {

    XSLTElementDef def = getElemDef();

    Vector processedDefs = new Vector();
    int nAttrs = attributes.getLength();

    for (int i = 0; i < nAttrs; i++)
    {
      String attrUri = attributes.getURI(i);
      String attrLocalName = attributes.getLocalName(i);
      XSLTAttributeDef attrDef = def.getAttributeDef(attrUri, attrLocalName);

      if (null == attrDef)
      {

        handler.error(attributes.getQName(i)
                      + "attribute is not allowed on the " + rawName
                      + " element!", null);
      }
      else
      {
        String valueString = attributes.getValue(i);

        if (valueString.indexOf(org.apache.xpath.compiler.Keywords.FUNC_KEY_STRING
                                + "(") >= 0)
          handler.error(
            XSLMessages.createMessage(
            XSLTErrorResources.ER_INVALID_KEY_CALL, null), null);

        processedDefs.addElement(attrDef);
        attrDef.setAttrValue(handler, attrUri, attrLocalName,
                             attributes.getQName(i), attributes.getValue(i),
                             target);
      }
    }

    XSLTAttributeDef[] attrDefs = def.getAttributes();
    int nAttrDefs = attrDefs.length;

    for (int i = 0; i < nAttrDefs; i++)
    {
      XSLTAttributeDef attrDef = attrDefs[i];
      String defVal = attrDef.getDefault();

      if (null != defVal)
      {
        if (!processedDefs.contains(attrDef))
        {
          attrDef.setDefAttrValue(handler, target);
        }
      }

      if (attrDef.getRequired())
      {
        if (!processedDefs.contains(attrDef))
          handler.error(
            XSLMessages.createMessage(
              XSLTErrorResources.ER_REQUIRES_ATTRIB, new Object[]{ rawName,
                                                                   attrDef.getName() }), null);
      }
    }
  }
}
