package org.apache.xerces.validators.schema;

import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.validators.datatype.*;
import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.validators.common.XMLAttributeDecl;
import org.apache.xerces.validators.common.GrammarResolver;
import org.apache.xerces.validators.common.Grammar;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * @author:  Sandy Gao, IBM
 * @version 1.0
 */

public class GeneralAttrCheck {

    public static int ELE_CONTEXT_GLOBAL    = 0;
    public static int ELE_CONTEXT_LOCAL     = 1;

    public static int ATT_REQUIRED          = 0;
    public static int ATT_OPT_DFLT          = 1;
    public static int ATT_OPT_NODFLT        = 2;

    protected static String PRE_GLOBAL      = "G_";
    protected static String PRE_LOC_NAME    = "LN_";
    protected static String PRE_LOC_REF     = "LR_";

    protected static Hashtable fEleAttrsMap = new Hashtable();

    protected static DatatypeValidator[] fExtraDVs = null;

    protected static int dtCount             = 0;

    protected static final int DT_ANYURI           = dtCount++;
    protected static final int DT_BOOLEAN          = dtCount++;
    protected static final int DT_ID               = dtCount++;
    protected static final int DT_NONNEGINT        = dtCount++;
    protected static final int DT_QNAME            = dtCount++;
    protected static final int DT_STRING           = dtCount++;
    protected static final int DT_TOKEN            = dtCount++;
    protected static final int DT_NCNAME           = dtCount++;
    protected static final int DT_XPATH            = dtCount++;
    protected static final int DT_XPATH1           = dtCount++;

    protected static final int DT_BLOCK            = -1;
    protected static final int DT_BLOCK1           = DT_BLOCK-1;
    protected static final int DT_FINAL            = DT_BLOCK1-1;
    protected static final int DT_FINAL1           = DT_FINAL-1;
    protected static final int DT_FORM             = DT_FINAL1-1;
    protected static final int DT_MAXOCCURS        = DT_FORM-1;
    protected static final int DT_MAXOCCURS1       = DT_MAXOCCURS-1;
    protected static final int DT_MEMBERTYPES      = DT_MAXOCCURS1-1;
    protected static final int DT_MINOCCURS1       = DT_MEMBERTYPES-1;
    protected static final int DT_NAMESPACE        = DT_MINOCCURS1-1;
    protected static final int DT_PROCESSCONTENTS  = DT_NAMESPACE-1;
    protected static final int DT_PUBLIC           = DT_PROCESSCONTENTS-1;
    protected static final int DT_USE              = DT_PUBLIC-1;
    protected static final int DT_WHITESPACE       = DT_USE-1;

    static {
        fExtraDVs = new DatatypeValidator[dtCount];

        int attCount = 0;
        int ATT_ABSTRACT_D          = attCount++;
        int ATT_ATTRIBUTE_FD_D      = attCount++;
        int ATT_BASE_R              = attCount++;
        int ATT_BASE_N              = attCount++;
        int ATT_BLOCK_N             = attCount++;
        int ATT_BLOCK1_N            = attCount++;
        int ATT_BLOCK_D_D           = attCount++;
        int ATT_DEFAULT_N           = attCount++;
        int ATT_ELEMENT_FD_D        = attCount++;
        int ATT_FINAL_N             = attCount++;
        int ATT_FINAL1_N            = attCount++;
        int ATT_FINAL_D_D           = attCount++;
        int ATT_FIXED_N             = attCount++;
        int ATT_FIXED_D             = attCount++;
        int ATT_FORM_N              = attCount++;
        int ATT_ID_N                = attCount++;
        int ATT_ITEMTYPE_N          = attCount++;
        int ATT_MAXOCCURS_D         = attCount++;
        int ATT_MAXOCCURS1_D        = attCount++;
        int ATT_MEMBER_T_N          = attCount++;
        int ATT_MINOCCURS_D         = attCount++;
        int ATT_MINOCCURS1_D        = attCount++;
        int ATT_MIXED_D             = attCount++;
        int ATT_MIXED_N             = attCount++;
        int ATT_NAME_R              = attCount++;
        int ATT_NAMESPACE_D         = attCount++;
        int ATT_NAMESPACE_N         = attCount++;
        int ATT_NILLABLE_D          = attCount++;
        int ATT_PROCESS_C_D         = attCount++;
        int ATT_PUBLIC_R            = attCount++;
        int ATT_REF_R               = attCount++;
        int ATT_REFER_R             = attCount++;
        int ATT_SCHEMA_L_R          = attCount++;
        int ATT_SCHEMA_L_N          = attCount++;
        int ATT_SOURCE_N            = attCount++;
        int ATT_SUBSTITUTION_G_N    = attCount++;
        int ATT_SYSTEM_N            = attCount++;
        int ATT_TARGET_N_N          = attCount++;
        int ATT_TYPE_N              = attCount++;
        int ATT_USE_D               = attCount++;
        int ATT_VALUE_NNI_N         = attCount++;
        int ATT_VALUE_STR_N         = attCount++;
        int ATT_VALUE_WS_N          = attCount++;
        int ATT_VERSION_N           = attCount++;
        int ATT_XPATH_R             = attCount++;
        int ATT_XPATH1_R            = attCount++;

        OneAttr[] allAttrs = new OneAttr[attCount];
        allAttrs[ATT_ABSTRACT_D]        =   new OneAttr(SchemaSymbols.ATT_ABSTRACT,
                                                        DT_BOOLEAN,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_FALSE);
        allAttrs[ATT_ATTRIBUTE_FD_D]    =   new OneAttr(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT,
                                                        DT_FORM,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_UNQUALIFIED);
        allAttrs[ATT_BASE_R]            =   new OneAttr(SchemaSymbols.ATT_BASE,
                                                        DT_QNAME,
                                                        ATT_REQUIRED,
                                                        null);
        allAttrs[ATT_BASE_N]            =   new OneAttr(SchemaSymbols.ATT_BASE,
                                                        DT_QNAME,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_BLOCK_N]           =   new OneAttr(SchemaSymbols.ATT_BLOCK,
                                                        DT_BLOCK,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_BLOCK1_N]          =   new OneAttr(SchemaSymbols.ATT_BLOCK,
                                                        DT_BLOCK1,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_BLOCK_D_D]         =   new OneAttr(SchemaSymbols.ATT_BLOCKDEFAULT,
                                                        DT_BLOCK,
                                                        ATT_OPT_DFLT,
                                                        "");
        allAttrs[ATT_DEFAULT_N]         =   new OneAttr(SchemaSymbols.ATT_DEFAULT,
                                                        DT_STRING,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_ELEMENT_FD_D]      =   new OneAttr(SchemaSymbols.ATT_ELEMENTFORMDEFAULT,
                                                        DT_FORM,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_UNQUALIFIED);
        allAttrs[ATT_FINAL_N]           =   new OneAttr(SchemaSymbols.ATT_FINAL,
                                                        DT_FINAL,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_FINAL1_N]          =   new OneAttr(SchemaSymbols.ATT_FINAL,
                                                        DT_FINAL1,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_FINAL_D_D]         =   new OneAttr(SchemaSymbols.ATT_FINALDEFAULT,
                                                        DT_FINAL,
                                                        ATT_OPT_DFLT,
                                                        "");
        allAttrs[ATT_FIXED_N]           =   new OneAttr(SchemaSymbols.ATT_FIXED,
                                                        DT_STRING,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_FIXED_D]           =   new OneAttr(SchemaSymbols.ATT_FIXED,
                                                        DT_BOOLEAN,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_FALSE);
        allAttrs[ATT_FORM_N]            =   new OneAttr(SchemaSymbols.ATT_FORM,
                                                        DT_FORM,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_ID_N]              =   new OneAttr(SchemaSymbols.ATT_ID,
                                                        DT_ID,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_ITEMTYPE_N]        =   new OneAttr(SchemaSymbols.ATT_ITEMTYPE,
                                                        DT_QNAME,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_MAXOCCURS_D]       =   new OneAttr(SchemaSymbols.ATT_MAXOCCURS,
                                                        DT_MAXOCCURS,
                                                        ATT_OPT_DFLT,
                                                        "1");
        allAttrs[ATT_MAXOCCURS1_D]      =   new OneAttr(SchemaSymbols.ATT_MAXOCCURS,
                                                        DT_MAXOCCURS1,
                                                        ATT_OPT_DFLT,
                                                        "1");
        allAttrs[ATT_MEMBER_T_N]        =   new OneAttr(SchemaSymbols.ATT_MEMBERTYPES,
                                                        DT_MEMBERTYPES,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_MINOCCURS_D]       =   new OneAttr(SchemaSymbols.ATT_MINOCCURS,
                                                        DT_NONNEGINT,
                                                        ATT_OPT_DFLT,
                                                        "1");
        allAttrs[ATT_MINOCCURS1_D]      =   new OneAttr(SchemaSymbols.ATT_MINOCCURS,
                                                        DT_MINOCCURS1,
                                                        ATT_OPT_DFLT,
                                                        "1");
        allAttrs[ATT_MIXED_D]           =   new OneAttr(SchemaSymbols.ATT_MIXED,
                                                        DT_BOOLEAN,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_FALSE);
        allAttrs[ATT_MIXED_N]           =   new OneAttr(SchemaSymbols.ATT_MIXED,
                                                        DT_BOOLEAN,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_NAME_R]            =   new OneAttr(SchemaSymbols.ATT_NAME,
                                                        DT_NCNAME,
                                                        ATT_REQUIRED,
                                                        null);
        allAttrs[ATT_NAMESPACE_D]       =   new OneAttr(SchemaSymbols.ATT_NAMESPACE,
                                                        DT_NAMESPACE,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_TWOPOUNDANY);
        allAttrs[ATT_NAMESPACE_N]       =   new OneAttr(SchemaSymbols.ATT_NAMESPACE,
                                                        DT_ANYURI,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_NILLABLE_D]        =   new OneAttr(SchemaSymbols.ATT_NILLABLE,
                                                        DT_BOOLEAN,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_FALSE);
        allAttrs[ATT_PROCESS_C_D]       =   new OneAttr(SchemaSymbols.ATT_PROCESSCONTENTS,
                                                        DT_PROCESSCONTENTS,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_STRICT);
        allAttrs[ATT_PUBLIC_R]          =   new OneAttr(SchemaSymbols.ATT_PUBLIC,
                                                        DT_PUBLIC,
                                                        ATT_REQUIRED,
                                                        null);
        allAttrs[ATT_REF_R]             =   new OneAttr(SchemaSymbols.ATT_REF,
                                                        DT_QNAME,
                                                        ATT_REQUIRED,
                                                        null);
        allAttrs[ATT_REFER_R]           =   new OneAttr(SchemaSymbols.ATT_REFER,
                                                        DT_QNAME,
                                                        ATT_REQUIRED,
                                                        null);
        allAttrs[ATT_SCHEMA_L_R]        =   new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION,
                                                        DT_ANYURI,
                                                        ATT_REQUIRED,
                                                        null);
        allAttrs[ATT_SCHEMA_L_N]        =   new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION,
                                                        DT_ANYURI,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_SOURCE_N]          =   new OneAttr(SchemaSymbols.ATT_SOURCE,
                                                        DT_ANYURI,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_SUBSTITUTION_G_N]  =   new OneAttr(SchemaSymbols.ATT_SUBSTITUTIONGROUP,
                                                        DT_QNAME,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_SYSTEM_N]          =   new OneAttr(SchemaSymbols.ATT_SYSTEM,
                                                        DT_ANYURI,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_TARGET_N_N]        =   new OneAttr(SchemaSymbols.ATT_TARGETNAMESPACE,
                                                        DT_ANYURI,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_TYPE_N]            =   new OneAttr(SchemaSymbols.ATT_TYPE,
                                                        DT_QNAME,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_USE_D]             =   new OneAttr(SchemaSymbols.ATT_USE,
                                                        DT_USE,
                                                        ATT_OPT_DFLT,
                                                        SchemaSymbols.ATTVAL_OPTIONAL);
        allAttrs[ATT_VALUE_NNI_N]       =   new OneAttr(SchemaSymbols.ATT_VALUE,
                                                        DT_NONNEGINT,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_VALUE_STR_N]       =   new OneAttr(SchemaSymbols.ATT_VALUE,
                                                        DT_STRING,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_VALUE_WS_N]        =   new OneAttr(SchemaSymbols.ATT_VALUE,
                                                        DT_WHITESPACE,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_VERSION_N]         =   new OneAttr(SchemaSymbols.ATT_VERSION,
                                                        DT_TOKEN,
                                                        ATT_OPT_NODFLT,
                                                        null);
        allAttrs[ATT_XPATH_R]           =   new OneAttr(SchemaSymbols.ATT_XPATH,
                                                        DT_XPATH,
                                                        ATT_REQUIRED,
                                                        null);
        allAttrs[ATT_XPATH1_R]          =   new OneAttr(SchemaSymbols.ATT_XPATH,
                                                        DT_XPATH1,
                                                        ATT_REQUIRED,
                                                        null);

        Hashtable attrList;
        Object[] attrArray;
        OneElement oneEle;

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[ATT_TYPE_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_ATTRIBUTE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
        attrList.put(SchemaSymbols.ATT_FORM, allAttrs[ATT_FORM_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[ATT_TYPE_N]);
        attrList.put(SchemaSymbols.ATT_USE, allAttrs[ATT_USE_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_ATTRIBUTE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_REF, allAttrs[ATT_REF_R]);
        attrList.put(SchemaSymbols.ATT_USE, allAttrs[ATT_USE_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_REF+SchemaSymbols.ELT_ATTRIBUTE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ABSTRACT, allAttrs[ATT_ABSTRACT_D]);
        attrList.put(SchemaSymbols.ATT_BLOCK, allAttrs[ATT_BLOCK_N]);
        attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
        attrList.put(SchemaSymbols.ATT_FINAL, allAttrs[ATT_FINAL_N]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        attrList.put(SchemaSymbols.ATT_NILLABLE, allAttrs[ATT_NILLABLE_D]);
        attrList.put(SchemaSymbols.ATT_SUBSTITUTIONGROUP, allAttrs[ATT_SUBSTITUTION_G_N]);
        attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[ATT_TYPE_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_ELEMENT, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_BLOCK, allAttrs[ATT_BLOCK_N]);
        attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
        attrList.put(SchemaSymbols.ATT_FORM, allAttrs[ATT_FORM_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        attrList.put(SchemaSymbols.ATT_NILLABLE, allAttrs[ATT_NILLABLE_D]);
        attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[ATT_TYPE_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_ELEMENT, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_REF, allAttrs[ATT_REF_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_REF+SchemaSymbols.ELT_ELEMENT, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ABSTRACT, allAttrs[ATT_ABSTRACT_D]);
        attrList.put(SchemaSymbols.ATT_BLOCK, allAttrs[ATT_BLOCK1_N]);
        attrList.put(SchemaSymbols.ATT_FINAL, allAttrs[ATT_FINAL_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MIXED, allAttrs[ATT_MIXED_D]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_COMPLEXTYPE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MIXED, allAttrs[ATT_MIXED_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_COMPLEXTYPE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_SIMPLECONTENT, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_BASE, allAttrs[ATT_BASE_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_RESTRICTION, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_BASE, allAttrs[ATT_BASE_R]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_EXTENSION, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_REF, allAttrs[ATT_REF_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_REF+SchemaSymbols.ELT_ATTRIBUTEGROUP, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[ATT_NAMESPACE_D]);
        attrList.put(SchemaSymbols.ATT_PROCESSCONTENTS, allAttrs[ATT_PROCESS_C_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_ANYATTRIBUTE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MIXED, allAttrs[ATT_MIXED_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_COMPLEXCONTENT, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_ATTRIBUTEGROUP, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_GROUP, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_REF, allAttrs[ATT_REF_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_REF+SchemaSymbols.ELT_GROUP, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS1_D]);
        attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS1_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_ALL, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_CHOICE, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_SEQUENCE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
        attrList.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[ATT_NAMESPACE_D]);
        attrList.put(SchemaSymbols.ATT_PROCESSCONTENTS, allAttrs[ATT_PROCESS_C_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_ANY, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_UNIQUE, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_KEY, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        attrList.put(SchemaSymbols.ATT_REFER, allAttrs[ATT_REFER_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_KEYREF, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_XPATH, allAttrs[ATT_XPATH_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_SELECTOR, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_XPATH, allAttrs[ATT_XPATH1_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_FIELD, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        attrList.put(SchemaSymbols.ATT_PUBLIC, allAttrs[ATT_PUBLIC_R]);
        attrList.put(SchemaSymbols.ATT_SYSTEM, allAttrs[ATT_SYSTEM_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_NOTATION, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_ANNOTATION, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_ANNOTATION, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_SOURCE, allAttrs[ATT_SOURCE_N]);
        oneEle = new OneElement (attrList, false);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_APPINFO, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_SOURCE, allAttrs[ATT_SOURCE_N]);
        oneEle = new OneElement (attrList, false);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_DOCUMENTATION, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_FINAL, allAttrs[ATT_FINAL1_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_SIMPLETYPE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_FINAL, allAttrs[ATT_FINAL1_N]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_SIMPLETYPE, oneEle);


        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_ITEMTYPE, allAttrs[ATT_ITEMTYPE_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_LIST, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_MEMBERTYPES, allAttrs[ATT_MEMBER_T_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_UNION, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, allAttrs[ATT_ATTRIBUTE_FD_D]);
        attrList.put(SchemaSymbols.ATT_BLOCKDEFAULT, allAttrs[ATT_BLOCK_D_D]);
        attrList.put(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, allAttrs[ATT_ELEMENT_FD_D]);
        attrList.put(SchemaSymbols.ATT_FINALDEFAULT, allAttrs[ATT_FINAL_D_D]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_TARGETNAMESPACE, allAttrs[ATT_TARGET_N_N]);
        attrList.put(SchemaSymbols.ATT_VERSION, allAttrs[ATT_VERSION_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_SCHEMA, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_SCHEMALOCATION, allAttrs[ATT_SCHEMA_L_R]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_INCLUDE, oneEle);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_REDEFINE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[ATT_NAMESPACE_N]);
        attrList.put(SchemaSymbols.ATT_SCHEMALOCATION, allAttrs[ATT_SCHEMA_L_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_GLOBAL+SchemaSymbols.ELT_IMPORT, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_NNI_N]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_LENGTH, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_MINLENGTH, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_MAXLENGTH, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_TOTALDIGITS, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_FRACTIONDIGITS, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_STR_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_PATTERN, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_STR_N]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_ENUMERATION, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_WS_N]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_WHITESPACE, oneEle);

        attrList = new Hashtable();
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
        attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_STR_N]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_D]);
        oneEle = new OneElement (attrList);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_MAXINCLUSIVE, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_MAXEXCLUSIVE, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_MININCLUSIVE, oneEle);
        fEleAttrsMap.put(PRE_LOC_NAME+SchemaSymbols.ELT_MINEXCLUSIVE, oneEle);
    }

    private Hashtable fIdDefs = new Hashtable();

    protected XMLErrorReporter fErrorReporter = null;

    protected Hashtable fProcessedElements = new Hashtable();

    protected Hashtable fNonSchemaAttrs = new Hashtable();

    private GeneralAttrCheck() {}
    public GeneralAttrCheck (XMLErrorReporter er, DatatypeValidatorFactoryImpl datatypeRegistry) {
        fErrorReporter = er;
        synchronized (getClass()) {
            if (fExtraDVs[DT_ANYURI] == null) {
                datatypeRegistry.expandRegistryToFullSchemaSet();
                fExtraDVs[DT_ANYURI] = datatypeRegistry.getDatatypeValidator("anyURI");
                fExtraDVs[DT_BOOLEAN] = datatypeRegistry.getDatatypeValidator("boolean");
                fExtraDVs[DT_ID] = datatypeRegistry.getDatatypeValidator("ID");
                fExtraDVs[DT_NONNEGINT] = datatypeRegistry.getDatatypeValidator("nonNegativeInteger");
                fExtraDVs[DT_QNAME] = datatypeRegistry.getDatatypeValidator("QName");
                fExtraDVs[DT_STRING] = datatypeRegistry.getDatatypeValidator("string");
                fExtraDVs[DT_TOKEN] = datatypeRegistry.getDatatypeValidator("token");
                fExtraDVs[DT_NCNAME] = datatypeRegistry.getDatatypeValidator("NCName");
                fExtraDVs[DT_XPATH] = fExtraDVs[DT_STRING];
                fExtraDVs[DT_XPATH] = fExtraDVs[DT_STRING];
            }
        }
    }

    public Hashtable checkAttributes(Element element, int eleContext) throws Exception {
        if (element == null)
            return null;

        Hashtable attrValues = (Hashtable)fProcessedElements.get(element);
        if (attrValues != null)
            return attrValues;

        String elName = element.getLocalName(), name;
        if (eleContext == ELE_CONTEXT_GLOBAL) {
            name = PRE_GLOBAL + elName;
        } else {
            if (element.getAttributeNode(SchemaSymbols.ATT_REF) == null)
                name = PRE_LOC_NAME + elName;
            else
                name = PRE_LOC_REF + elName;
        }

        OneElement oneEle = (OneElement)fEleAttrsMap.get(name);
        if (oneEle == null) {
            reportSchemaError (SchemaMessageProvider.Con3X3ElementAppearance,
                               new Object[] {elName});
            return null;
        }

        attrValues = new Hashtable();
        Hashtable attrList = oneEle.attrList;

        NamedNodeMap attrs = element.getAttributes();
        Attr sattr = null;
        int i = 0;
        while ((sattr = (Attr)attrs.item(i++)) != null) {
            String attrName = sattr.getName();
            String attrVal = sattr.getValue();

            if (attrName.toLowerCase().startsWith("xml")) {
                attrValues.put(attrName, new Object[] {sattr.getValue(), Boolean.FALSE});
                continue;
            }

            String attrURI = sattr.getNamespaceURI();
            if (attrURI != null && attrURI.length() != 0) {
                if (attrURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) ||
                    !oneEle.allowNonSchemaAttr) {
                    reportSchemaError (SchemaMessageProvider.Con3X3AttributeAppearance,
                                       new Object[] {elName, attrName});
                } else {
                    attrValues.put(attrName,
                                   new Object[] {attrVal, Boolean.FALSE});
                    String attrRName = attrURI + "," + sattr.getLocalName();
                    Vector values = (Vector)fNonSchemaAttrs.get(attrRName);
                    if (values == null) {
                        values = new Vector();
                        values.addElement(attrName);
                        values.addElement(elName);
                        values.addElement(attrVal);
                        fNonSchemaAttrs.put(attrRName, values);
                    } else {
                        values.addElement(elName);
                        values.addElement(attrVal);
                    }
                }
                continue;
            }

            OneAttr oneAttr = (OneAttr)attrList.get(attrName);
            if (oneAttr == null) {
                reportSchemaError (SchemaMessageProvider.Con3X3AttributeAppearance,
                                   new Object[] {elName, attrName});
                continue;
            }

            try {
                if (oneAttr.dvIndex >= 0) {
                    if (oneAttr.dvIndex != DT_STRING &&
                        oneAttr.dvIndex != DT_XPATH &&
                        oneAttr.dvIndex != DT_XPATH1) {
                        DatatypeValidator dv = fExtraDVs[oneAttr.dvIndex];
                        if (dv instanceof IDDatatypeValidator) {
                            dv.validate( attrVal, fIdDefs );
                        } else {
                            dv.validate( attrVal, null);
                        }
                    }
                    attrValues.put(attrName, new Object[] {attrVal, Boolean.FALSE});
                } else {
                    attrVal = validate(attrName, attrVal, oneAttr.dvIndex);
                    attrValues.put(attrName, new Object[] {attrVal, Boolean.FALSE});
                }
            } catch(InvalidDatatypeValueException ide) {
                reportSchemaError (SchemaMessageProvider.Con3X3AttributeInvalidValue,
                                   new Object[] {elName, attrName, ide.getLocalizedMessage()});
            }
        }

        Object[] reqAttrs = oneEle.attrArray;
        for (i = 0; i < reqAttrs.length; i++) {
            OneAttr oneAttr = (OneAttr)reqAttrs[i];

            if (element.getAttributeNode(oneAttr.name) != null)
                continue;

            if (oneAttr.optdflt == ATT_REQUIRED) {
                reportSchemaError (SchemaMessageProvider.Con3X3AttributeMustAppear,
                                   new Object[] {elName, oneAttr.name});
            }
            else if (oneAttr.optdflt == ATT_OPT_DFLT) {
                attrValues.put(oneAttr.name, new Object[] {oneAttr.dfltValue, Boolean.TRUE});
            }
        }

        fProcessedElements.put(element, attrValues);

        return attrValues;
    }

    private String validate(String attr, String value, int dvIndex) throws InvalidDatatypeValueException {
        Vector unionBase, enum;
        int choice;

        if (value == null)
            return null;

        switch (dvIndex) {
        case DT_BLOCK:
            choice = 0;
            if (value.equals (SchemaSymbols.ATTVAL_POUNDALL)) {
                choice = SchemaSymbols.SUBSTITUTION|SchemaSymbols.EXTENSION|
                         SchemaSymbols.RESTRICTION|SchemaSymbols.LIST|
                         SchemaSymbols.UNION;
            } else {
                StringTokenizer t = new StringTokenizer (value, " ");
                while (t.hasMoreTokens()) {
                    String token = t.nextToken ();

                    if (token.equals (SchemaSymbols.ATTVAL_SUBSTITUTION) ) {
                        choice |= SchemaSymbols.SUBSTITUTION;
                    } else if (token.equals (SchemaSymbols.ATTVAL_EXTENSION)) {
                        choice |= SchemaSymbols.EXTENSION;
                    } else if (token.equals (SchemaSymbols.ATTVAL_RESTRICTION)) {
                        choice |= SchemaSymbols.RESTRICTION;
                    } else if ( token.equals (SchemaSymbols.ELT_LIST) ) {
                        choice |= SchemaSymbols.LIST;
                    } else if ( token.equals (SchemaSymbols.ELT_UNION) ) {
                        choice |= SchemaSymbols.RESTRICTION;
                    } else {
                        throw new InvalidDatatypeValueException("the value '"+value+"' must match (#all | List of (substitution | extension | restriction | list | union))");
                    }
                }
            }
            break;
        case DT_BLOCK1:
        case DT_FINAL:
            choice = 0;
            if (value.equals (SchemaSymbols.ATTVAL_POUNDALL)) {
                choice = SchemaSymbols.EXTENSION|SchemaSymbols.RESTRICTION;
            } else {
                StringTokenizer t = new StringTokenizer (value, " ");
                while (t.hasMoreTokens()) {
                    String token = t.nextToken ();

                    if (token.equals (SchemaSymbols.ATTVAL_EXTENSION)) {
                        choice |= SchemaSymbols.EXTENSION;
                    } else if (token.equals (SchemaSymbols.ATTVAL_RESTRICTION)) {
                        choice |= SchemaSymbols.RESTRICTION;
                    } else {
                        throw new InvalidDatatypeValueException("the value '"+value+"' must match (#all | List of (extension | restriction))");
                    }
                }
            }
            break;
        case DT_FINAL1:
            choice = 0;
            if (value.equals (SchemaSymbols.ATTVAL_POUNDALL)) {
                choice = SchemaSymbols.RESTRICTION|SchemaSymbols.LIST|
                         SchemaSymbols.UNION;
            } else if (value.equals (SchemaSymbols.ELT_LIST)) {
                choice = SchemaSymbols.LIST;
            } else if (value.equals (SchemaSymbols.ELT_UNION)) {
                choice = SchemaSymbols.UNION;
            } else if (value.equals (SchemaSymbols.ATTVAL_RESTRICTION)) {
                choice = SchemaSymbols.RESTRICTION;
            } else {
                throw new InvalidDatatypeValueException("the value '"+value+"' must match (#all | (list | union | restriction))");
            }
            break;
        case DT_FORM:
            if (!value.equals (SchemaSymbols.ATTVAL_QUALIFIED) &&
                !value.equals (SchemaSymbols.ATTVAL_UNQUALIFIED)) {
                throw new InvalidDatatypeValueException("the value '"+value+"' must match (qualified | unqualified)");
            }
            break;
        case DT_MAXOCCURS:
            if (!value.equals("unbounded")) {
                try {
                    fExtraDVs[DT_NONNEGINT].validate(value, null);
                } catch (InvalidDatatypeValueException ide) {
                    throw new InvalidDatatypeValueException("the value '"+value+"' must match (nonNegativeInteger | unbounded)");
                }
            }
            break;
        case DT_MAXOCCURS1:
            if (!value.equals("1"))
                throw new InvalidDatatypeValueException("the value '"+value+"' must be '1'");
            break;
        case DT_MEMBERTYPES:
            try {
                StringTokenizer t = new StringTokenizer (value, " ");
                while (t.hasMoreTokens()) {
                    String token = t.nextToken ();
                    fExtraDVs[DT_QNAME].validate(token, null);
                }
            } catch (InvalidDatatypeValueException ide) {
                throw new InvalidDatatypeValueException("the value '"+value+"' must match (List of QName)");
            }
            break;
        case DT_MINOCCURS1:
            if (!value.equals("0") && !value.equals("1"))
                throw new InvalidDatatypeValueException("the value '"+value+"' must be '0' or '1'");
            break;
        case DT_NAMESPACE:
            if (!value.equals(SchemaSymbols.ATTVAL_TWOPOUNDANY) &&
                !value.equals(SchemaSymbols.ATTVAL_TWOPOUNDOTHER)) {
                StringTokenizer t = new StringTokenizer (value, " ");
                try {
                    while (t.hasMoreTokens()) {
                        String token = t.nextToken ();
                        if (!token.equals(SchemaSymbols.ATTVAL_TWOPOUNDTARGETNS) &&
                            !token.equals(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL)) {
                            fExtraDVs[DT_ANYURI].validate(token, null);
                        }
                    }
                } catch (InvalidDatatypeValueException ide) {
                    throw new InvalidDatatypeValueException("the value '"+value+"' must match ((##any | ##other) | List of (anyURI | (##targetNamespace | ##local)) )");
                }
            }
            break;
        case DT_PROCESSCONTENTS:
            if (!value.equals (SchemaSymbols.ATTVAL_SKIP) &&
                !value.equals (SchemaSymbols.ATTVAL_LAX) &&
                !value.equals (SchemaSymbols.ATTVAL_STRICT)) {
                throw new InvalidDatatypeValueException("the value '"+value+"' must match (lax | skip | strict)");
            }
            break;
        case DT_PUBLIC:
            fExtraDVs[DT_TOKEN].validate(value, null);
            break;
        case DT_USE:
            if (!value.equals (SchemaSymbols.ATTVAL_OPTIONAL) &&
                !value.equals (SchemaSymbols.ATTVAL_PROHIBITED) &&
                !value.equals (SchemaSymbols.ATTVAL_REQUIRED)) {
                throw new InvalidDatatypeValueException("the value '"+value+"' must match (optional | prohibited | required)");
            }
            break;
        case DT_WHITESPACE:
            if (!value.equals (SchemaSymbols.ATT_PRESERVE) &&
                !value.equals (SchemaSymbols.ATT_REPLACE) &&
                !value.equals (SchemaSymbols.ATT_COLLAPSE)) {
                throw new InvalidDatatypeValueException("the value '"+value+"' must match (preserve | replace | collapse)");
            }
            break;
        }

        return value;
    }

    private void reportSchemaError(int major, Object args[]) throws Exception {
        if (fErrorReporter == null) {
            System.out.println("__TraverseSchemaError__ : " + SchemaMessageProvider.fgMessageKeys[major]);
            for (int i=0; i< args.length ; i++) {
                System.out.println((String)args[i]);
            }
        }
        else {
            fErrorReporter.reportError(fErrorReporter.getLocator(),
                                       SchemaMessageProvider.SCHEMA_DOMAIN,
                                       major,
                                       SchemaMessageProvider.MSG_NONE,
                                       args,
                                       XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
        }
    }

    public void checkNonSchemaAttributes(GrammarResolver grammarResolver) throws Exception {
        Enumeration enum = fNonSchemaAttrs.keys();
        while (enum.hasMoreElements()) {
            String attrRName = (String)enum.nextElement();
            String attrURI = attrRName.substring(0,attrRName.indexOf(','));
            String attrLocal = attrRName.substring(attrRName.indexOf(',')+1);
            Grammar grammar = grammarResolver.getGrammar(attrURI);
            if (grammar == null || !(grammar instanceof SchemaGrammar))
                continue;
            SchemaGrammar sGrammar = (SchemaGrammar)grammar;
            Hashtable attrRegistry = sGrammar.getAttributeDeclRegistry();
            if (attrRegistry == null)
                continue;
            XMLAttributeDecl tempAttrDecl = (XMLAttributeDecl)attrRegistry.get(attrLocal);
            if (tempAttrDecl == null)
                continue;
            DatatypeValidator dv = tempAttrDecl.datatypeValidator;
            if (dv == null)
                continue;

            Vector values = (Vector)fNonSchemaAttrs.get(attrRName);
            String elName, attrVal;
            String attrName = (String)values.elementAt(0);
            int count = values.size();
            for (int i = 1; i < count; i += 2) {
                elName = (String)values.elementAt(i);
                attrVal = normalize((String)values.elementAt(i+1), dv.getWSFacet());
                try {
                    dv.validate(attrVal,null);
                } catch(InvalidDatatypeValueException ide) {
                    reportSchemaError (SchemaMessageProvider.Con3X3AttributeInvalidValue,
                                       new Object[] {elName, attrName, ide.getLocalizedMessage()});
                }
            }
        }
    }

    private String normalize(String content, short ws) {
        int len = content == null ? 0 : content.length();
        if (len == 0 || ws == DatatypeValidator.PRESERVE)
            return content;

        StringBuffer sb = new StringBuffer();
        if (ws == DatatypeValidator.REPLACE) {
            char ch;
            for (int i = 0; i < len; i++) {
                ch = content.charAt(i);
                if (ch != 0x9 && ch != 0xa && ch != 0xd)
                    sb.append(ch);
                else
                    sb.append((char)0x20);
            }
        } else {
            char ch;
            int i;
            boolean isLeading = true;
            for (i = 0; i < len; i++) {
                ch = content.charAt(i);
                if (ch != 0x9 && ch != 0xa && ch != 0xd && ch != 0x20) {
                    sb.append(ch);
                    isLeading = false;
                } else {
                    for (; i < len-1; i++) {
                        ch = content.charAt(i+1);
                        if (ch != 0x9 && ch != 0xa && ch != 0xd && ch != 0x20)
                            break;
                    }
                    if (i < len - 1 && !isLeading)
                        sb.append((char)0x20);
                }
            }
        }

        return sb.toString();
    }
}

class OneAttr {
    public String name;
    public int dvIndex;
    public int optdflt;
    public String dfltValue;

    public OneAttr(String name, int dvIndex, int optdflt, String dfltValue) {
        this.name = name;
        this.dvIndex = dvIndex;
        this.optdflt = optdflt;
        this.dfltValue = dfltValue;
    }
}

class OneElement {
    public Hashtable attrList;
    public Object[] attrArray;
    public boolean allowNonSchemaAttr;

    public OneElement (Hashtable attrList) {
        this(attrList, true);
    }

    public OneElement (Hashtable attrList, boolean allowNonSchemaAttr) {
        this.attrList = attrList;

        int count = attrList.size();
        this.attrArray = new Object[count];
        Enumeration enum = attrList.elements();
        for (int i = 0; i < count; i++)
            this.attrArray[i] = enum.nextElement();

        this.allowNonSchemaAttr = allowNonSchemaAttr;
    }
}
