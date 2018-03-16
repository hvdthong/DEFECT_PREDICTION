package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For PHP expresions and predicates
 *
 * @version $Revision: 630591 $
 */
@XmlRootElement(name = "php")
public class PhpExpression extends ExpressionType {
    public PhpExpression() {
    }

    public PhpExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "php";
    }
}
