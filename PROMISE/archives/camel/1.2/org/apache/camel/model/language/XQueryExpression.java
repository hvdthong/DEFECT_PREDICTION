package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For XQuery expresions and predicates
 *
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "xquery")
public class XQueryExpression extends ExpressionType {
    public XQueryExpression() {
    }

    public XQueryExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "xquery";
    }
}
