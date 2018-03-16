package org.apache.xerces.utils;

/** 
 * QName structure useful for gathering the parts of a qualified name.
 *
 * @author Andy Clark
 * @version $Id: QName.java 316852 2001-02-01 09:58:44Z andyc $
 */
public class QName {


    /** 
     * Compile to true to help find places where a URI is being set
     * to the value -1 when it should be StringPool.EMPTY_STRING (0).
     */
    private static final boolean FIND_URI_IS_MINUS_ONE = false;


    /** Prefix. */
    public int prefix;

    /** Local part of qname. */
    public int localpart;

    /** Fully concatenated name. */
    public int rawname;

    /** URI bound to prefix. */
    public int uri;


    /** Default constructor. */
    public QName() {
        clear();
    }

    /** Constructs a specified qname. */
    public QName(int prefix, int localpart, int rawname) {
        setValues(prefix, localpart, rawname, StringPool.EMPTY_STRING);
    }

    /** Constructs a specified qname. */
    public QName(int prefix, int localpart, int rawname, int uri) {
        setValues(prefix, localpart, rawname, uri);
    }

    /** Copy constructor. */
    public QName(QName qname) {
        setValues(qname);
    }


    /** Sets the values of the qualified name. */
    public void setValues(QName qname) {
        if (FIND_URI_IS_MINUS_ONE) {
            if (qname.uri == -1) {
                try { 
                    throw new Exception("uri value is -1 instead of StringPool.EMPTY_STRING (0)"); 
                }
                catch (Exception e) { 
                    e.printStackTrace(System.err); 
                }
            }
        }
        prefix = qname.prefix;
        localpart = qname.localpart;
        rawname = qname.rawname;
        uri = qname.uri;
    }

    /** Sets the values of the qualified name. */
    public void setValues(int prefix, int localpart, int rawname) {
        setValues(prefix, localpart, rawname, StringPool.EMPTY_STRING);
    }

    /** Sets the values of the qualified name. */
    public void setValues(int prefix, int localpart, int rawname, int uri) {
        if (FIND_URI_IS_MINUS_ONE) {
            if (uri == -1) {
                try { 
                    throw new Exception("uri value is -1 instead of StringPool.EMPTY_STRING (0)"); 
                }
                catch (Exception e) { 
                    e.printStackTrace(System.err); 
                }
            }
        }
        this.prefix = prefix;
        this.localpart = localpart;
        this.rawname = rawname;
        this.uri = uri;
    }

    /** Clears all of the values. */
    public void clear() {
        prefix = -1;
        localpart = -1;
        rawname = -1;
        uri = StringPool.EMPTY_STRING;
    }


    /** Returns true if the two objects are equal. */
    public boolean equals(Object object) {
        if (object != null && object instanceof QName) {
            QName qname = (QName)object;
            if (uri == StringPool.EMPTY_STRING) {
                return rawname == qname.rawname;
            }
            return localpart == qname.localpart &&
                   uri == qname.uri;
        }
        return false;
    }

    /** Returns a hash code value. */
    public int hashCode() {
        return (localpart << 16) | uri;
    }

    /** Returns a string representation of this object. */
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("prefix: ");
        str.append(prefix);
        str.append(", ");
        str.append("localpart: ");
        str.append(localpart);
        str.append(", ");
        str.append("rawname: ");
        str.append(rawname);
        str.append(", ");
        str.append("uri: ");
        str.append(uri);
        return str.toString();
    }

    /** Returns a string representation of this object. */
    public String toString(StringPool stringPool) {
        StringBuffer str = new StringBuffer();
        str.append("prefix: ");
        str.append(String.valueOf(stringPool.toString(prefix)));
        str.append(", ");
        str.append("localpart: ");
        str.append(String.valueOf(stringPool.toString(localpart)));
        str.append(", ");
        str.append("rawname: ");
        str.append(String.valueOf(stringPool.toString(rawname)));
        str.append(", ");
        str.append("uri: ");
        str.append(String.valueOf(stringPool.toString(uri)));
        return str.toString();
    }

