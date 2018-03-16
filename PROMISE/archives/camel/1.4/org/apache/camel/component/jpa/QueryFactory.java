package org.apache.camel.component.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A Strategy to create a query to search for objects in a database
 *
 * @version $Revision: 630591 $
 */
public interface QueryFactory {
    /**
     * Creates a new query to find objects to be processed
     *
     * @param entityManager
     * @return the query configured with any parameters etc
     */
    Query createQuery(EntityManager entityManager);
}
