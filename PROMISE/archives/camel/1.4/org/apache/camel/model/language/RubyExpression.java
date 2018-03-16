package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For Ruby expresions and predicates
 *
 * @version $Revision: 630591 $
 */
@XmlRootElement(name = "ruby")
public class RubyExpression extends ExpressionType {
    public RubyExpression() {
    }

    public RubyExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "ruby";
    }
}
