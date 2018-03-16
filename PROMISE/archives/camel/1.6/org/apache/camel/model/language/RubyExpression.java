package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For Ruby expressions and predicates
 *
 * @version $Revision: 679483 $
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
