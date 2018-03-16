package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For Groovy expresions and predicates
 *
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "simple")
public class SimpleExpression extends ExpressionType {
    public SimpleExpression() {
    }

    public SimpleExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "simple";
    }
}
