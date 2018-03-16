package org.apache.camel.language.groovy;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Set;

import groovy.lang.Binding;
import groovy.lang.Script;
import org.apache.camel.Exchange;
import org.apache.camel.impl.ExpressionSupport;
import org.apache.camel.util.ExchangeHelper;

/**
 * @version $Revision: 640731 $
 */
public class GroovyExpression extends ExpressionSupport<Exchange> {
    private Class<Script> scriptType;
    private String text;

    public GroovyExpression(Class<Script> scriptType, String text) {
        this.scriptType = scriptType;
        this.text = text;
    }

    @Override
    public String toString() {
        return "groovy: " + text;
    }

    protected String assertionFailureMessage(Exchange exchange) {
        return "groovy: " + text;
    }

    public Object evaluate(Exchange exchange) {
        Script script = ExchangeHelper.newInstance(exchange, scriptType);
        configure(exchange, script);
        return script.run();
    }

    private void configure(Exchange exchange, Script script) {
        final Binding binding = script.getBinding();
        ExchangeHelper.populateVariableMap(exchange, new AbstractMap<String, Object>() {
            @Override
            public Object put(String key, Object value) {
                binding.setProperty(key, value);
                return null;
            }

            public Set entrySet() {
                return Collections.EMPTY_SET;
            }
        });
    }
}
