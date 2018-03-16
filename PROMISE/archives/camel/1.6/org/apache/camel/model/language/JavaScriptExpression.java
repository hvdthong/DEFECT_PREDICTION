package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For JavaScript expressions and predicates
 *
 * @version $Revision: 679483 $
 */
@XmlRootElement(name = "javaScript")
public class JavaScriptExpression extends ExpressionType {
    public JavaScriptExpression() {
    }

    public JavaScriptExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "js";
    }
}
