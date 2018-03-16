package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For Groovy expresions and predicates
 *
 * @version $Revision: 630591 $
 */
@XmlRootElement(name = "groovy")
public class GroovyExpression extends ExpressionType {
    public GroovyExpression() {
    }

    public GroovyExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "groovy";
    }
}
