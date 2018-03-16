package org.apache.xerces.validators.schema.identity;

import org.xml.sax.SAXException;

/**
 * Interface for a field activator. The field activator is responsible
 * for activating fields within a specific scope; the caller merely
 * requests the fields to be activated.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: FieldActivator.java 316817 2001-01-25 07:19:01Z andyc $
 */
public interface FieldActivator {
    

    /**
     * Start the value scope for the specified identity constraint. This 
     * method is called when the selector matches in order to initialize 
     * the value store.
     *
     * @param identityConstraint The identity constraint.
     */
    public void startValueScopeFor(IdentityConstraint identityConstraint)
        throws Exception;

    /** 
     * Request to activate the specified field. This method returns the
     * matcher for the field.
     *
     * @param field The field to activate.
     */
    public XPathMatcher activateField(Field field) throws Exception;

    /**
     * Ends the value scope for the specified identity constraint.
     *
     * @param identityConstraint The identity constraint.
     */
    public void endValueScopeFor(IdentityConstraint identityConstraint)
        throws Exception;

