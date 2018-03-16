package org.apache.xpath.domapi;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

/**
 *
 * A new exception to add support for DOM Level 3 XPath API.
 * This class is needed to throw a org.w3c.dom.DOMException with proper error code in
 * createExpression method of XPathEvaluatorImpl (a DOM Level 3 class).
 * 
 * This class extends TransformerException because the error message includes information
 * about where the XPath problem is in the stylesheet as well as the XPath expression itself.
 * 
 * @xsl.usage internal
 */
final public class XPathStylesheetDOM3Exception extends TransformerException {
	public XPathStylesheetDOM3Exception(String msg, SourceLocator arg1)
	{
		super(msg, arg1);
	}
}
