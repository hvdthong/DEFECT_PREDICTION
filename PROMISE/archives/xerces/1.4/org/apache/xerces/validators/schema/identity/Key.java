package org.apache.xerces.validators.schema.identity;

/**
 * Schema key identity constraint.
 *
 * @author Andy Clark, IBM
 * @version $Id: Key.java 316921 2001-02-20 06:12:29Z andyc $
 */
public class Key 
    extends IdentityConstraint {


    /** Constructs a key with the specified name. */
    public Key(String identityConstraintName, String elementName) {
        super(identityConstraintName, elementName);


    /** Returns the identity constraint type. */
    public short getType() {
        return KEY;

