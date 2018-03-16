package org.apache.camel.component.bean;

import java.lang.annotation.Annotation;

import org.apache.camel.Expression;

/**
 * @version $Revision: $
 */
public class ParameterInfo {
    private final int index;
    private final Class type;
    private final Annotation[] annotations;
    private final Expression expression;

    public ParameterInfo(int index, Class type, Annotation[] annotations, Expression expression) {
        this.index = index;
        this.type = type;
        this.annotations = annotations;
        this.expression = expression;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public Expression getExpression() {
        return expression;
    }

    public int getIndex() {
        return index;
    }

    public Class getType() {
        return type;
    }
}
