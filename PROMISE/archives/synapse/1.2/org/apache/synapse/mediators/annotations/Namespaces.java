package org.apache.synapse.mediators.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation used to declare namespaces available to be used in XPATH expressions
 * 
 * To support setting multiple namespaces with arbitrary prefixes the namespace and 
 * namespace prefix are held in as a single string value seperated by a colon. For
 * and the namespace prefix "ns", and the complete Namespaces annotation for that
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
public @interface Namespaces {

    String[] value();

}
