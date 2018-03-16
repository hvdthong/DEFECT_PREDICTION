package org.apache.xerces.validators.schema.identity;

import org.xml.sax.SAXException;

/**
 * Interface for storing values associated to an identity constraint. 
 * Each value stored corresponds to a field declared for the identity
 * constraint. One instance of an object implementing this interface
 * is created for each identity constraint per element declaration in
 * the instance document to store the information for this identity
 * constraint.
 * <p>
 * <strong>Note:</strong> The component performing identity constraint
 * collection and validation is responsible for providing an 
 * implementation of this interface. The component is also responsible
 * for performing the necessary checks required by each type of identity
 * constraint.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: ValueStore.java 317135 2001-05-15 17:21:31Z neilg $
 */
public interface ValueStore {
    

    /** 
     * Adds the specified value to the value store.
     *
     * @param value The value to add.
     * @param field The field associated to the value. This reference
     *              is used to ensure that each field only adds a value
     *              once within a selection scope.
     */
    public void addValue(Field field, IDValue value) throws Exception;

    /* report an error if a nillable element is being keyed on.
     * the implementor must check that a <key> really is present, not a <unique> or <keyref>.
     * @param:  id:  the IdentityConstraint that has matched.
     */
    public void reportNilError(IdentityConstraint id) throws Exception;

