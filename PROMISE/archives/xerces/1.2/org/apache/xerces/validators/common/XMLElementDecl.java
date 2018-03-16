package org.apache.xerces.validators.common;

import org.apache.xerces.utils.QName;
import org.apache.xerces.validators.datatype.DatatypeValidator;

/**
 * @version $Id: XMLElementDecl.java 315647 2000-05-17 18:33:29Z jeffreyr $
 */
public class XMLElementDecl {


    public static final int TYPE_EMPTY = 0;
    public static final int TYPE_ANY = 1;
    public static final int TYPE_MIXED = 2;
    public static final int TYPE_CHILDREN = 3;
    public static final int TYPE_SIMPLE = 4;



    public final QName name = new QName();

    public int type;


    public boolean list;

    public DatatypeValidator datatypeValidator;


    public int contentSpecIndex;

    public int enclosingScope;


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

