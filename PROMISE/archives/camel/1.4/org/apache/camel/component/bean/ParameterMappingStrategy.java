package org.apache.camel.component.bean;

import org.apache.camel.Expression;

/**
 * A strategy for creating a default parameter expression for a given type
 *
 * @version $Revision: 630591 $
 */
public interface ParameterMappingStrategy {

    Expression getDefaultParameterTypeExpression(Class parameterType);
}
