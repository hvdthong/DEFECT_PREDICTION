package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For OGNL expressions and predicates
 *
 * @version $Revision: 679483 $
 */
@XmlRootElement(name = "ognl")
public class OgnlExpression extends ExpressionType {
    public OgnlExpression() {
    }

    public OgnlExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "ognl";
    }
}
