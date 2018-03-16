package org.apache.camel.component.jpa;

import org.apache.camel.Service;
import org.springframework.orm.jpa.JpaCallback;

/**
 * @version $Revision: 630591 $
 */
public interface TransactionStrategy extends Service {
    Object execute(JpaCallback callback);
}
