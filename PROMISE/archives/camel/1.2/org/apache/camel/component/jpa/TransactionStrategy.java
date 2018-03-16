package org.apache.camel.component.jpa;

import org.apache.camel.Service;
import org.springframework.orm.jpa.JpaCallback;

/**
 * @version $Revision: 563665 $
 */
public interface TransactionStrategy extends Service {
    Object execute(JpaCallback callback);
}
