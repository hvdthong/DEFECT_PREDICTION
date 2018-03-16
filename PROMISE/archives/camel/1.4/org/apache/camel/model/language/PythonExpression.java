package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For Python expresions and predicates
 *
 * @version $Revision: 630591 $
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
