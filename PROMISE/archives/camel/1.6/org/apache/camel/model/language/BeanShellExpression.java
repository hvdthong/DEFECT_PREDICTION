package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For BeanShell expressions and predicates
 *
 * @deprecated use {@link org.apache.camel.model.language.LanguageExpression} will be removed in Camel 2.0
 * @version $Revision: 711235 $
 */
@XmlRootElement(name = "beanshell")
public class BeanShellExpression extends ExpressionType {
    public BeanShellExpression() {
    }

    public BeanShellExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "beanshell";
    }
}
