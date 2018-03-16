package org.apache.xerces.validators.schema.identity;

/**
 * Schema key reference identity constraint.
 *
 * @author Andy Clark, IBM
 * @version $Id: KeyRef.java 317095 2001-05-08 19:55:46Z neilg $
 */
public class KeyRef 
    extends IdentityConstraint {


    /** The key (or unique) being referred to. */
    protected IdentityConstraint fKey;


    /** Constructs a keyref with the specified name. */
    public KeyRef(String identityConstraintName, IdentityConstraint key,
                  String elementName) {
        super(identityConstraintName, elementName);
        fKey = key;


    /** Returns the identity constraint type. */
    public short getType() {
        return KEYREF;

    /** Returns the key being referred to.  */
    public IdentityConstraint getKey() {
        return fKey;

