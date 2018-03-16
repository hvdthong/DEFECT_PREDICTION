package org.apache.xerces.validators.schema.identity;

/**
 * Schema key identity constraint.
 *
 * @author Andy Clark, IBM
 * @version $Id: Key.java 316817 2001-01-25 07:19:01Z andyc $
 */
public class Key 
    extends IdentityConstraint {


    /** Name. */
    protected String fKeyName;


    /** Constructs a key with the specified name. */
    public Key(String elementName, String keyName) {
        super(elementName);
        fKeyName = keyName;


    /** Returns the identity constraint type. */
    public short getType() {
        return KEY;

    /** Returns the name. */
    public String getName() {
        return fKeyName;

