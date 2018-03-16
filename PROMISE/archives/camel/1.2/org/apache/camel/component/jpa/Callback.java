package org.apache.camel.component.jpa;

/**
 * @version $Revision: 563665 $
 */
public interface Callback<R, P> {
    R callback(P parameter);
}
