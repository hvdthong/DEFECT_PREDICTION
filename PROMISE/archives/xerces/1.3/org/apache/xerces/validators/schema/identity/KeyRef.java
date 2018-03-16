package org.apache.xerces.validators.schema.identity;

/**
 * Schema key reference identity constraint.
 *
 * @author Andy Clark, IBM
 * @version $Id: KeyRef.java 316817 2001-01-25 07:19:01Z andyc $
 */
public class KeyRef 
    extends IdentityConstraint {


    /** Key name. */
    protected String fKeyName;


    /** Constructs a keyref with the specified name. */
    public KeyRef(String elementName, String keyName) {
        super(elementName);
        fKeyName = keyName;


    /** Returns the identity constraint type. */
    public short getType() {
        return KEYREF;

    /** Returns the name. */
    public String getName() {
        return fKeyName;

