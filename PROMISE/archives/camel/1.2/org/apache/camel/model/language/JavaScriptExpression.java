package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For JavaScript expresions and predicates
 *
 * @version $Revision: 1.1 $
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
