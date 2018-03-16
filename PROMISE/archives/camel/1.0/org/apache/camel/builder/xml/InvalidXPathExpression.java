package org.apache.camel.builder.xml;

import org.apache.camel.RuntimeExpressionException;

import javax.xml.xpath.XPathException;

/**
 * An exception thrown if am XPath expression could not be parsed or evaluated
 *
 * @version $Revision: 521180 $
 */
public class InvalidXPathExpression extends RuntimeExpressionException {
    private final String xpath;

    public InvalidXPathExpression(String xpath, XPathException e) {
        super("Invalid xpath: " + xpath + ". Reason: " + e, e);
        this.xpath = xpath;
    }

    public String getXpath() {
        return xpath;
    }
}
