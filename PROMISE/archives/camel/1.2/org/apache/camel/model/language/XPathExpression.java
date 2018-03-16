package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For XPath expresions and predicates
 *
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "xpath")
public class XPathExpression extends ExpressionType {
    public XPathExpression() {
    }

    public XPathExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "xpath";
    }
}
