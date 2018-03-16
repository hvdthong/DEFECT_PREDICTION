package org.apache.camel.component.bean;

import java.lang.annotation.Annotation;

import org.apache.camel.Expression;

/**
 * Parameter information to be used for method invocation.
 *
 * @version $Revision: 659771 $
 */
public class ParameterInfo {
    private final int index;
    private final Class type;
    private final Annotation[] annotations;
    private Expression expression;

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

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
