package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For expresions and predicates using the
 *
 * @version $Revision: 630591 $
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
