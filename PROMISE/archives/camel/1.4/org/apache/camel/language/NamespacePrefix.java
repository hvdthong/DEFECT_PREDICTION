package org.apache.camel.language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Creates a namespace prefix for an XPath
 *
 * @version $Revision: 640438 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE })
public @interface NamespacePrefix {
    String prefix();
    String uri();
}
