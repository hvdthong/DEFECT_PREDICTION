package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * An expression which extracts the named header
 *
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "header")
public class HeaderExpression extends ExpressionType {
    public HeaderExpression() {
    }

    public HeaderExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "header";
    }
}
