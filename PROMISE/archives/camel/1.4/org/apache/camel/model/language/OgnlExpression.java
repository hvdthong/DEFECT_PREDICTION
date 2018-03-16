package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For OGNL expresions and predicates
 *
 * @version $Revision: 630591 $
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
