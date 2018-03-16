package org.apache.camel.language.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.apache.camel.spi.Language;

/**
 * @version $Revision: 640731 $
 */
public class GroovyLanguage implements Language  {

    public static GroovyExpression groovy(String expression) {
        return new GroovyLanguage().createExpression(expression);
    }

    public GroovyExpression createPredicate(String expression) {
        return createExpression(expression);
    }

    public GroovyExpression createExpression(String expression) {
        Class<Script> scriptType = parseExpression(expression);
        return new GroovyExpression(scriptType, expression);
    }

    protected Class<Script> parseExpression(String expression) {
        return new GroovyClassLoader().parseClass(expression);
    }
}
