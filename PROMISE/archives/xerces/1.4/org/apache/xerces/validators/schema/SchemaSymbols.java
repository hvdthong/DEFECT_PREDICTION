package org.apache.xerces.validators.schema;


/**
 *             Collection of symbols used to parse a Schema Grammar
 *             We have temporary references to this class from
 *             SchemaImporter but we will be moving all the
 *             SchemaImporter functionality to the Grammar classes
 *
 * @author jeffrey rodriguez
 */
public final  class SchemaSymbols {
    public static final String XSI_SCHEMALOCACTION =  "schemaLocation";
    public static final String XSI_NONAMESPACESCHEMALOCACTION =  "noNamespaceSchemaLocation";
    public static final String XSI_TYPE =  "type";

    public static final String ELT_ALL =  "all";
    public static final String ELT_ANNOTATION =  "annotation";
    public static final String ELT_APPINFO =  "appinfo";
    public static final String ELT_DOCUMENTATION =  "documentation";
    public static final String ELT_ANY =  "any";
    public static final String ELT_WILDCARD = "any";
    public static final String ELT_ANYATTRIBUTE =  "anyAttribute";
    public static final String ELT_ATTRIBUTE =  "attribute";
    public static final String ELT_ATTRIBUTEGROUP =  "attributeGroup";
    public static final String ELT_CHOICE =  "choice";
    public static final String ELT_COMPLEXTYPE =  "complexType";
    public static final String ELT_CONTENT =  "content";
    public static final String ELT_DURATION =  "duration";
    public static final String ELT_ELEMENT =  "element";
    public static final String ELT_ENUMERATION =  "enumeration";
    public static final String ELT_FIELD = "field";
    public static final String ELT_WHITESPACE =  "whiteSpace";
    public static final String ELT_GROUP =  "group";
    public static final String ELT_IMPORT =  "import";
    public static final String ELT_INCLUDE =  "include";
    public static final String ELT_REDEFINE =  "redefine";
    public static final String ELT_KEY =  "key";
    public static final String ELT_KEYREF =  "keyref";
    public static final String ELT_LENGTH =  "length";
    public static final String ELT_MAXEXCLUSIVE =  "maxExclusive";
    public static final String ELT_MAXINCLUSIVE =  "maxInclusive";
    public static final String ELT_MAXLENGTH =  "maxLength";
    public static final String ELT_MINEXCLUSIVE =  "minExclusive";
    public static final String ELT_MININCLUSIVE =  "minInclusive";
    public static final String ELT_MINLENGTH =  "minLength";
    public static final String ELT_NOTATION =  "notation";
    public static final String ELT_PATTERN =  "pattern";
    public static final String ELT_PERIOD =  "period";
    public static final String ELT_TOTALDIGITS =  "totalDigits";
    public static final String ELT_FRACTIONDIGITS =  "fractionDigits";
    public static final String ELT_SCHEMA =  "schema";
    public static final String ELT_SELECTOR =  "selector";
    public static final String ELT_SEQUENCE =  "sequence";
    public static final String ELT_SIMPLETYPE =  "simpleType";
    public static final String ELT_UNION =  "union";
    public static final String ELT_LIST =  "list";
    public static final String ELT_UNIQUE = "unique";
    public static final String ELT_COMPLEXCONTENT = "complexContent";
    public static final String ELT_SIMPLECONTENT = "simpleContent";
    public static final String ELT_RESTRICTION = "restriction";
    public static final String ELT_EXTENSION = "extension";
    public static final String ATT_ABSTRACT =  "abstract";
    public static final String ATT_ATTRIBUTEFORMDEFAULT =  "attributeFormDefault";
    public static final String ATT_BASE =  "base";
    public static final String ATT_ITEMTYPE =  "itemType";
    public static final String ATT_MEMBERTYPES =  "memberTypes";
    public static final String ATT_BLOCK =  "block";
    public static final String ATT_BLOCKDEFAULT =  "blockDefault";
    public static final String ATT_CONTENT  = "content";
    public static final String ATT_DEFAULT =  "default";
    public static final String ATT_DERIVEDBY = "derivedBy";
    public static final String ATT_ELEMENTFORMDEFAULT =  "elementFormDefault";
    public static final String ATT_SUBSTITUTIONGROUP =  "substitutionGroup";
    public static final String ATT_FINAL =  "final";
    public static final String ATT_FINALDEFAULT =  "finalDefault";
    public static final String ATT_FIXED =  "fixed";
    public static final String ATT_FORM =  "form";
    public static final String ATT_ID =  "id";
    public static final String ATT_MAXOCCURS =  "maxOccurs";
    public static final String ATT_MINOCCURS =  "minOccurs";
    public static final String ATT_NAME =  "name";
    public static final String ATT_NAMESPACE =  "namespace";
    public static final String ATT_NILLABLE =  "nillable";
    public static final String ATT_NIL =  "nil";
    public static final String ATT_PROCESSCONTENTS =  "processContents";
    public static final String ATT_REF =  "ref";
    public static final String ATT_REFER =  "refer";
    public static final String ATT_SCHEMALOCATION =  "schemaLocation";
    public static final String ATT_SOURCE =  "source";
    public static final String ATT_SYSTEM =  "system";
    public static final String ATT_PUBLIC =  "public";
    public static final String ATT_TARGETNAMESPACE =  "targetNamespace";
    public static final String ATT_TYPE =  "type";
    public static final String ATT_USE =  "use";
    public static final String ATT_VALUE = "value";
    public static final String ATT_MIXED = "mixed";
    public static final String ATT_VERSION = "version";
    public static final String ATT_XPATH = "xpath";
    public static final String ATT_PRESERVE = "preserve";
    public static final String ATT_REPLACE = "replace";
    public static final String ATT_COLLAPSE = "collapse";
    public static final String ATTVAL_TWOPOUNDANY =  "##any";
    public static final String ATTVAL_TWOPOUNDLOCAL =  "##local";
    public static final String ATTVAL_TWOPOUNDOTHER =  "##other";
    public static final String ATTVAL_TWOPOUNDTARGETNS =  "##targetNamespace";
    public static final String ATTVAL_POUNDALL =  "#all";
    public static final String ATTVAL_BOOLEAN =  "boolean";
    public static final String ATTVAL_DEFAULT =  "default";
    public static final String ATTVAL_ELEMENTONLY =  "elementOnly";
    public static final String ATTVAL_EMPTY =  "empty";
    public static final String ATTVAL_EXTENSION =  "extension";
    public static final String ATTVAL_FALSE =  "false";
    public static final String ATTVAL_FALSE_0 =  "0";
    public static final String ATTVAL_FIXED =  "fixed";
    public static final String ATTVAL_ID =  "ID";
    public static final String ATTVAL_LAX =  "lax";
    public static final String ATTVAL_MAXLENGTH =  "maxLength";
    public static final String ATTVAL_MINLENGTH =  "minLength";
    public static final String ATTVAL_MIXED =  "mixed";
    public static final String ATTVAL_NCNAME =  "NCName";
    public static final String ATTVAL_OPTIONAL =  "optional";
    public static final String ATTVAL_PROHIBITED =  "prohibited";
    public static final String ATTVAL_QNAME =  "QName";
    public static final String ATTVAL_QUALIFIED =  "qualified";
    public static final String ATTVAL_REQUIRED =  "required";
    public static final String ATTVAL_RESTRICTION =  "restriction";
    public static final String ATTVAL_SKIP =  "skip";
    public static final String ATTVAL_STRICT =  "strict";
    public static final String ATTVAL_STRING =  "string";
    public static final String ATTVAL_TEXTONLY =  "textOnly";
    public static final String ATTVAL_TIMEDURATION =  "timeDuration";
    public static final String ATTVAL_TRUE =  "true";
    public static final String ATTVAL_TRUE_1 =  "1";
    public static final String ATTVAL_UNBOUNDED =  "unbounded";
    public static final String ATTVAL_UNQUALIFIED =  "unqualified";
    public static final String ATTVAL_ANYURI =  "anyURI";
    public static final String ATTVAL_TOKEN =  "token";
    public static final String ATTVAL_SUBSTITUTIONGROUP = "substitutionGroup";
    public static final String ATTVAL_SUBSTITUTION = "substitution";


    public static final int                     EMPTY_SET = 0;
    public static final int                     EXTENSION = 1;
    public static final int                     RESTRICTION = 2;
    public static final int                     UNION = 4;
    public static final int                     LIST = 8;
    public static final int                     ENUMERATION = 16;
    public static final int                     SUBSTITUTION = 32;



    public static final int NILLABLE = 1;
    public static final int ABSTRACT = 2;
    public static final int FIXED = 4;

    public static final int OCCURRENCE_UNBOUNDED = -2;

}
