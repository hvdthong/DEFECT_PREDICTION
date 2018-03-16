package org.apache.camel.component.jpa;

import javax.persistence.EntityManager;

/**
 * A strategy for deleting entity beans which have been processed; either by a real delete or by an update of some
 * application specific properties so that the entity bean will not be found in future polling queries.
 *
 * @version $Revision: 525537 $
 */
public interface DeleteHandler<T> {
    /**
     * Deletes the entity bean after it has been processed either by actually
     * deleting the object or updating it in a way so that future queries do not return this object again.
     *
     * @param entityManager
     * @param entityBean    the entity bean that has been processed and should be deleted
     */
    void deleteObject(EntityManager entityManager, Object entityBean);
}
