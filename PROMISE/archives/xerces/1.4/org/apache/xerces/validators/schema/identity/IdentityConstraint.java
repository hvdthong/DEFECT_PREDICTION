package org.apache.xerces.validators.schema.identity;

/**
 * Base class of Schema identity constraint.
 *
 * @author Andy Clark, IBM
 * @version $Id: IdentityConstraint.java 317151 2001-05-17 20:58:59Z neilg $
 */
public abstract class IdentityConstraint {


    /** Type: unique. */
    public static final short UNIQUE = 0;

    /** Type: key. */
    public static final short KEY = 1;

    /** Type: key reference. */
    public static final short KEYREF = 2;


    /** Identity constraint name. */
    protected String fIdentityConstraintName;

    /** Element name. */
    protected String fElementName;

    /** Selector. */
    protected Selector fSelector;

    /** Field count. */
    protected int fFieldCount;

    /** Fields. */
    protected Field[] fFields;


    /** Default constructor. */
    protected IdentityConstraint(String identityConstraintName,
                                 String elementName) {
        fIdentityConstraintName = identityConstraintName;
        fElementName = elementName;


    /** Returns the identity constraint type. */
    public abstract short getType();

    /** Returns the identity constraint name. */
    public String getIdentityConstraintName() {
        return fIdentityConstraintName;

    /** Returns the element name. */
    public String getElementName() {
        return fElementName;

    /** Sets the selector. */
    public void setSelector(Selector selector) {
        fSelector = selector;

    /** Returns the selector. */
    public Selector getSelector() {
        return fSelector;

    /** Adds a field. */
    public void addField(Field field) {
        try {
            fFields[fFieldCount] = null;
        }
        catch (NullPointerException e) {
            fFields = new Field[4];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            Field[] newfields = new Field[fFields.length * 2];
            System.arraycopy(fFields, 0, newfields, 0, fFields.length);
            fFields = newfields;
        }
        fFields[fFieldCount++] = field;

    /** Returns the field count. */
    public int getFieldCount() {
        return fFieldCount;

    /** Returns the field at the specified index. */
    public Field getFieldAt(int index) {
        return fFields[index];


    /** Returns a string representation of this object. */
    public String toString() {
        String s = super.toString();
        int index1 = s.lastIndexOf('$');
        if (index1 != -1) {
            return s.substring(index1 + 1);
        }
        int index2 = s.lastIndexOf('.');
        if (index2 != -1) {
            return s.substring(index2 + 1);
        }
        return s;

    public boolean equals(IdentityConstraint id) {
        boolean areEqual = fIdentityConstraintName.equals(id.fIdentityConstraintName);
        if(!areEqual) return false;
        areEqual = fSelector.toString().equals(id.fSelector.toString());
        if(!areEqual) return false;
        areEqual = (fFieldCount == id.fFieldCount);
        if(!areEqual) return false;
        for(int i=0; i<fFieldCount; i++) 
            if(!fFields[i].toString().equals(id.fFields[i].toString())) return false;
        return true;

