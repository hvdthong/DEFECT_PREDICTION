package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For EL expresions and predicates
 *
 * @version $Revision: 630591 $
 */
@XmlRootElement(name = "el")
public class ELExpression extends ExpressionType {
    public ELExpression() {
    }

    public ELExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "el";
    }
}
