package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For Python expressions and predicates
 *
 * @version $Revision: 679483 $
 */
@XmlRootElement(name = "python")
public class PythonExpression extends ExpressionType {
    public PythonExpression() {
    }

    public PythonExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "python";
    }
}
