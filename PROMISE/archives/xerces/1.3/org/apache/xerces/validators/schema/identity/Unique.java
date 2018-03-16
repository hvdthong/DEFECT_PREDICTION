package org.apache.xerces.validators.schema.identity;

/**
 * Schema unique identity constraint.
 *
 * @author Andy Clark, IBM
 * @version $Id: Unique.java 316817 2001-01-25 07:19:01Z andyc $
 */
public class Unique 
    extends IdentityConstraint {


    /** Constructs a unique identity constraint. */
    public Unique(String elementName) {
        super(elementName);


    /** Returns the identity constraint type. */
    public short getType() {
        return UNIQUE;

