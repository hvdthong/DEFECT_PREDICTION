package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * For SQ: expresions and predicates
 *
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "sql")
public class SqlExpression extends ExpressionType {
    public SqlExpression() {
    }

    public SqlExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "sql";
    }
}
