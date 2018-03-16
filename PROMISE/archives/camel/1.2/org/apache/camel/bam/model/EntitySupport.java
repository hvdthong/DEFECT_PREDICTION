package org.apache.camel.bam.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A base class for persistent entities
 *
 * @version $Revision: $
 */
public class EntitySupport {
    private Long id;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return getClass().getName() + "[" + id + "]";
    }
}
