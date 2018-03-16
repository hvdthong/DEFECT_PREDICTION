package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For expressions and predicates using a constant
 *
 */
@XmlRootElement(name = "constant")
public class ConstantExpression extends ExpressionType {
    public ConstantExpression() {
    }

    public ConstantExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "constant";
    }
}
