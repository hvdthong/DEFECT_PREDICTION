package org.apache.camel.component.bean;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * Information used by Camel to perform method invocation.
 *
 * @version $Revision: 660275 $
 */
public interface MethodInvocation {

    Method getMethod();

    Object[] getArguments();

    Object proceed() throws Throwable;

    Object getThis();

    AccessibleObject getStaticPart();
}
