package org.apache.xpath.functions;

import org.apache.xpath.res.XPATHErrorResources;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;

import java.util.Vector;

import org.apache.xpath.XPathContext;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XString;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the Count() function.
 */
public class FuncCount extends FunctionOneArg
{

  /**
   * Execute the function.  The function must return
   * a valid object.
   * @param xctxt The current execution context.
   * @return A valid XObject.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException
  {


	DTMIterator nl = m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
	int i = nl.getLength();	
	nl.detach();

    return new XNumber((double) i);
  }
}
