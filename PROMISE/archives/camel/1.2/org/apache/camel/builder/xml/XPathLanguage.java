package org.apache.camel.builder.xml;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.Language;

import javax.xml.namespace.QName;

/**
 * @version $Revision: 1.1 $
 */
public class XPathLanguage implements Language {
    private QName resultType;

    public Predicate<Exchange> createPredicate(String expression) {
        XPathBuilder builder = XPathBuilder.xpath(expression);
        configureBuilder(builder);
        return builder;
    }

    public Expression<Exchange> createExpression(String expression) {
        XPathBuilder builder = XPathBuilder.xpath(expression);
        configureBuilder(builder);
        return builder;
    }

    public QName getResultType() {
        return resultType;
    }

    public void setResultType(QName resultType) {
        this.resultType = resultType;
    }

    protected void configureBuilder(XPathBuilder builder) {
        if (resultType != null) {
            builder.setResultType(resultType);
        }
    }
}
