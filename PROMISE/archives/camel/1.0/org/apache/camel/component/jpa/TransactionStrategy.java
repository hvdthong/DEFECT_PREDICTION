package org.apache.camel.component.jpa;

import org.apache.camel.Service;
import org.springframework.orm.jpa.JpaCallback;

/**
 * @version $Revision: 525537 $
 */
public interface TransactionStrategy extends Service {
    public Object execute(JpaCallback callback);
}
