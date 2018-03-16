package org.apache.xerces.utils;

/** 
 * QName structure useful for gathering the parts of a qualified name.
 *
 * @author Andy Clark
 * @version $Id: QName.java 315647 2000-05-17 18:33:29Z jeffreyr $
 */
public class QName {


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
        setValues(prefix, localpart, rawname, -1);
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
        prefix = qname.prefix;
        localpart = qname.localpart;
        rawname = qname.rawname;
        uri = qname.uri;
    }

    /** Sets the values of the qualified name. */
    public void setValues(int prefix, int localpart, int rawname) {
        setValues(prefix, localpart, rawname, -1);
    }

    /** Sets the values of the qualified name. */
    public void setValues(int prefix, int localpart, int rawname, int uri) {
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
        uri = -1;
    }


    /** Returns true if the two objects are equal. */
    public boolean equals(Object object) {
        if (object != null && object instanceof QName) {
            QName qname = (QName)object;
            return prefix == qname.prefix &&
                   localpart == qname.localpart &&
                   rawname == qname.rawname &&
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

