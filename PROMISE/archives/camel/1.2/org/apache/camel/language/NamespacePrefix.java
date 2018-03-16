package org.apache.camel.language;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Creates a namespace prefix for an XPath
 * 
 * @version $Revision: 1.1 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE })
public @interface NamespacePrefix {
    public abstract String prefix();
    public abstract String uri();
}
