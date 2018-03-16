package org.apache.camel.component.jpa;

/**
 * @version $Revision: 525537 $
 */
public interface Callback<R, P> {
    R callback(P parameter);
}
