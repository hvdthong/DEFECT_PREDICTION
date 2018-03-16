package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a parameterised language expression which can support any language
 * at runtime using the language attribute.
 *
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "expression")
@XmlAccessorType(XmlAccessType.FIELD)
public class LanguageExpression extends ExpressionType {
    @XmlAttribute
    private String language;

    public LanguageExpression() {
    }

    public LanguageExpression(String language, String expression) {
        setLanguage(language);
        setExpression(expression);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
