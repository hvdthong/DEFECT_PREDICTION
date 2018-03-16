package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @version $Revision: 679483 $
 */
@XmlRootElement(name = "jxpath")
public class JXPathExpression extends ExpressionType {
    public JXPathExpression() {
    }

    public JXPathExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "jxpath";
    }
}
