package org.apache.xerces.validators.schema.identity;

/**
 * Schema unique identity constraint.
 *
 * @author Andy Clark, IBM
 * @version $Id: Unique.java 316921 2001-02-20 06:12:29Z andyc $
 */
public class Unique 
    extends IdentityConstraint {


    /** Constructs a unique identity constraint. */
    public Unique(String identityConstraintName, String elementName) {
        super(identityConstraintName, elementName);


    /** Returns the identity constraint type. */
    public short getType() {
        return UNIQUE;

