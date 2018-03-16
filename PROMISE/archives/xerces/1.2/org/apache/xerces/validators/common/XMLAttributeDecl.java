package org.apache.xerces.validators.common;

import org.apache.xerces.utils.QName;
import org.apache.xerces.validators.datatype.DatatypeValidator;

/**
 * @version $Id: XMLAttributeDecl.java 315947 2000-07-21 03:42:45Z ericye $
 */
public class XMLAttributeDecl {



    public static final int TYPE_CDATA = 0;
    public static final int TYPE_ENTITY = 1;
    public static final int TYPE_ENUMERATION = 2;
    public static final int TYPE_ID = 3;
    public static final int TYPE_IDREF = 4;
    public static final int TYPE_NMTOKEN = 5;
    public static final int TYPE_NOTATION = 6;


    public static final int TYPE_SIMPLE = 7;

    public static final int TYPE_ANY_ANY = 8;
    public static final int TYPE_ANY_OTHER = 9;
    public static final int TYPE_ANY_LOCAL = 10;
    public static final int TYPE_ANY_LIST = 11;


    public static final int DEFAULT_TYPE_IMPLIED = 0;
    public static final int DEFAULT_TYPE_FIXED = 1;
    public static final int DEFAULT_TYPE_REQUIRED = 2;
    public static final int DEFAULT_TYPE_DEFAULT = 3;

    public static final int PROCESSCONTENTS_STRICT = 4;
    public static final int PROCESSCONTENTS_LAX = 5;
    public static final int PROCESSCONTENTS_SKIP = 6;




    public QName name = new QName();


    public DatatypeValidator datatypeValidator;


    public int type;

    public boolean list;

    public int enumeration;

    public int defaultType;

    public String defaultValue;


    public XMLAttributeDecl() {
        clear();
    }

    public XMLAttributeDecl(XMLAttributeDecl attributeDecl) {
        setValues(attributeDecl);
    }


    public void clear() {
        name.clear();
        datatypeValidator = null;
        type = -1;
        list = false;
        enumeration = -1;
        defaultType = DEFAULT_TYPE_IMPLIED;
        defaultValue = null;
    }

    public void setValues(XMLAttributeDecl attributeDecl) {
        name.setValues(attributeDecl.name);
        datatypeValidator = attributeDecl.datatypeValidator;
        type = attributeDecl.type;
        list = attributeDecl.list;
        enumeration = attributeDecl.enumeration;
        defaultType = attributeDecl.defaultType;
        defaultValue = attributeDecl.defaultValue;
    }


    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object object) {
        return super.equals(object);
    }

