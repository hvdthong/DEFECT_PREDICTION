package org.apache.xerces.validators.common;

import java.util.Vector;

import org.apache.xerces.utils.QName;
import org.apache.xerces.validators.datatype.DatatypeValidator;

/**
 * @version $Id: XMLElementDecl.java 317077 2001-04-26 17:45:50Z lmartin $
 */
public class XMLElementDecl {


    public static final int TYPE_EMPTY = 0;
    public static final int TYPE_ANY = 1;
    public static final int TYPE_CHILDREN = 4;
    public static final int TYPE_SIMPLE = 5;



    public final QName name = new QName();

    public int type;


    public boolean list;

    public DatatypeValidator datatypeValidator;


    public int contentSpecIndex;

    public int enclosingScope;


    public final Vector unique = new Vector();

    public final Vector key = new Vector();

    public final Vector keyRef = new Vector();


    public XMLElementDecl() {
        clear();
    }

    public XMLElementDecl(XMLElementDecl elementDecl) {
        setValues(elementDecl);
    }


    public void clear() {
        name.clear();
        type = - 1;
        list = false;
        datatypeValidator = null;
        contentSpecIndex = -1;
        enclosingScope = -1;
        unique.removeAllElements();
        key.removeAllElements();
        keyRef.removeAllElements();
    }

    public void setValues(XMLElementDecl elementDecl) {
        name.setValues(elementDecl.name);
        type = elementDecl.type;
        list = elementDecl.list;
        datatypeValidator = elementDecl.datatypeValidator;
        contentSpecIndex = elementDecl.contentSpecIndex;
        enclosingScope = elementDecl.enclosingScope;
    }


    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object object) {
        return super.equals(object);
    }

