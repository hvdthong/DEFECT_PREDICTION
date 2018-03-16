package org.apache.camel.processor.exceptionpolicy;

import org.apache.camel.model.WhenType;

/**
 * Exception policy key is a compound key for storing:
 * <b>exception class</b> + <b>when</b> => <b>exception type</b>.
 * <p/>
 * This is used by Camel to store the onException types configued that has or has not predicates attached (when).
 */
public final class ExceptionPolicyKey {

    private final Class exceptionClass;
    private final WhenType when;

    public ExceptionPolicyKey(Class exceptionClass, WhenType when) {
        this.exceptionClass = exceptionClass;
        this.when = when;
    }

    public Class getExceptionClass() {
        return exceptionClass;
    }

    public WhenType getWhen() {
        return when;
    }

    public static ExceptionPolicyKey newInstance(Class exceptionClass) {
        return new ExceptionPolicyKey(exceptionClass, null);
    }

    public static ExceptionPolicyKey newInstance(Class exceptionClass, WhenType when) {
        return new ExceptionPolicyKey(exceptionClass, when);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExceptionPolicyKey that = (ExceptionPolicyKey) o;

        if (!exceptionClass.equals(that.exceptionClass)) {
            return false;
        }
        if (when != null ? !when.equals(that.when) : that.when != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = exceptionClass.hashCode();
        result = 31 * result + (when != null ? when.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExceptionPolicyKey[" + exceptionClass + (when != null ? " " + when : "") + "]";
    }
}
