package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For EL expressions and predicates
 *
 * @version $Revision: 679483 $
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
