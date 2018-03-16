package org.apache.camel.component.jpa;

/**
 * @version $Revision: 630591 $
 */
public interface Callback<R, P> {
    R callback(P parameter);
}
