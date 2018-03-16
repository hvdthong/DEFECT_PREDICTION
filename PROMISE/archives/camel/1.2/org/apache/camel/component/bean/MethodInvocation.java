package org.apache.camel.component.bean;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * @version $Revision: $
 */
public interface MethodInvocation {
    Method getMethod();

    Object[] getArguments();

    Object proceed() throws Throwable;

    Object getThis();

    AccessibleObject getStaticPart();
}
