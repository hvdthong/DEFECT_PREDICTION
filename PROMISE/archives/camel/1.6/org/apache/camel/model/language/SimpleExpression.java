package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For expressions and predicates using the
 *
 * @version $Revision: 679483 $
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
