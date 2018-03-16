package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For PHP expressions and predicates
 *
 * @version $Revision: 679483 $
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
