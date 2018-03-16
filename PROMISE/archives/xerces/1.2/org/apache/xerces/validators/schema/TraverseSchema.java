package org.apache.xerces.validators.schema;

import  org.apache.xerces.framework.XMLErrorReporter;
import  org.apache.xerces.validators.common.Grammar;
import  org.apache.xerces.validators.common.GrammarResolver;
import  org.apache.xerces.validators.common.GrammarResolverImpl;
import  org.apache.xerces.validators.common.XMLElementDecl;
import  org.apache.xerces.validators.common.XMLAttributeDecl;
import  org.apache.xerces.validators.schema.SchemaSymbols;
import  org.apache.xerces.validators.schema.XUtil;
import  org.apache.xerces.validators.datatype.DatatypeValidator;
import  org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl;
import  org.apache.xerces.validators.datatype.InvalidDatatypeValueException;
import  org.apache.xerces.utils.StringPool;
import  org.w3c.dom.Element;

import  org.w3c.dom.*;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

import  org.apache.xerces.parsers.DOMParser;
import  org.apache.xerces.validators.common.XMLValidator;
import  org.apache.xerces.validators.datatype.DatatypeValidator.*;
import  org.apache.xerces.validators.datatype.InvalidDatatypeValueException;
import  org.apache.xerces.framework.XMLContentSpec;
import  org.apache.xerces.utils.QName;
import  org.apache.xerces.utils.NamespacesScope;
import  org.apache.xerces.parsers.SAXParser;
import  org.apache.xerces.framework.XMLParser;
import  org.apache.xerces.framework.XMLDocumentScanner;

import  org.xml.sax.InputSource;
import  org.xml.sax.SAXParseException;
import  org.xml.sax.EntityResolver;
import  org.xml.sax.ErrorHandler;
import  org.xml.sax.SAXException;
import  java.io.IOException;
import  org.w3c.dom.Document;
import  org.apache.xml.serialize.OutputFormat;
import  org.apache.xml.serialize.XMLSerializer;
import  org.apache.xerces.validators.schema.SchemaSymbols;




/**
 * Instances of this class get delegated to Traverse the Schema and
 * to populate the Grammar internal representation by
 * instances of Grammar objects.
 * Traverse a Schema Grammar:
     * As of April 07, 2000 the following is the
     * XML Representation of Schemas and Schema components,
     * Chapter 4 of W3C Working Draft.
     * <schema 
     *   attributeFormDefault = qualified | unqualified 
     *   blockDefault = #all or (possibly empty) subset of {equivClass, extension, restriction} 
     *   elementFormDefault = qualified | unqualified 
     *   finalDefault = #all or (possibly empty) subset of {extension, restriction} 
     *   id = ID 
     *   targetNamespace = uriReference 
     *   version = string>
     *   Content: ((include | import | annotation)* , ((simpleType | complexType | element | group | attribute | attributeGroup | notation) , annotation*)+)
     * </schema>
     * 
     * 
     * <attribute 
     *   form = qualified | unqualified 
     *   id = ID 
     *   name = NCName 
     *   ref = QName 
     *   type = QName 
     *   use = default | fixed | optional | prohibited | required 
     *   value = string>
     *   Content: (annotation? , simpleType?)
     * </>
     * 
     * <element 
     *   abstract = boolean 
     *   block = #all or (possibly empty) subset of {equivClass, extension, restriction} 
     *   default = string 
     *   equivClass = QName 
     *   final = #all or (possibly empty) subset of {extension, restriction} 
     *   fixed = string 
     *   form = qualified | unqualified 
     *   id = ID 
     *   maxOccurs = string 
     *   minOccurs = nonNegativeInteger 
     *   name = NCName 
     *   nullable = boolean 
     *   ref = QName 
     *   type = QName>
     *   Content: (annotation? , (simpleType | complexType)? , (unique | key | keyref)*)
     * </>
     * 
     * 
     * <complexType 
     *   abstract = boolean 
     *   base = QName 
     *   block = #all or (possibly empty) subset of {extension, restriction} 
     *   content = elementOnly | empty | mixed | textOnly 
     *   derivedBy = extension | restriction 
     *   final = #all or (possibly empty) subset of {extension, restriction} 
     *   id = ID 
     *   name = NCName>
     *   Content: (annotation? , (((minExclusive | minInclusive | maxExclusive | maxInclusive | precision | scale | length | minLength | maxLength | encoding | period | duration | enumeration | pattern)* | (element | group | all | choice | sequence | any)*) , ((attribute | attributeGroup)* , anyAttribute?)))
     * </>
     * 
     * 
     * <attributeGroup 
     *   id = ID 
     *   name = NCName
     *   ref = QName>
     *   Content: (annotation?, (attribute|attributeGroup), anyAttribute?)
     * </>
     * 
     * <anyAttribute 
     *   id = ID 
     *   namespace = ##any | ##other | ##local | list of {uri, ##targetNamespace}>
     *   Content: (annotation?)
     * </anyAttribute>
     * 
     * <group 
     *   id = ID 
     *   maxOccurs = string 
     *   minOccurs = nonNegativeInteger 
     *   name = NCName 
     *   ref = QName>
     *   Content: (annotation? , (element | group | all | choice | sequence | any)*)
     * </>
     * 
     * <all 
     *   id = ID 
     *   maxOccurs = string 
     *   minOccurs = nonNegativeInteger>
     *   Content: (annotation? , (element | group | choice | sequence | any)*)
     * </all>
     * 
     * <choice 
     *   id = ID 
     *   maxOccurs = string 
     *   minOccurs = nonNegativeInteger>
     *   Content: (annotation? , (element | group | choice | sequence | any)*)
     * </choice>
     * 
     * <sequence 
     *   id = ID 
     *   maxOccurs = string 
     *   minOccurs = nonNegativeInteger>
     *   Content: (annotation? , (element | group | choice | sequence | any)*)
     * </sequence>
     * 
     * 
     * <any 
     *   id = ID 
     *   maxOccurs = string 
     *   minOccurs = nonNegativeInteger 
     *   namespace = ##any | ##other | ##local | list of {uri, ##targetNamespace} 
     *   processContents = lax | skip | strict>
     *   Content: (annotation?)
     * </any>
     * 
     * <unique 
     *   id = ID 
     *   name = NCName>
     *   Content: (annotation? , (selector , field+))
     * </unique>
     * 
     * <key 
     *   id = ID 
     *   name = NCName>
     *   Content: (annotation? , (selector , field+))
     * </key>
     * 
     * <keyref 
     *   id = ID 
     *   name = NCName 
     *   refer = QName>
     *   Content: (annotation? , (selector , field+))
     * </keyref>
     * 
     * <selector>
     *   Content: XPathExprApprox : An XPath expression 
     * </selector>
     * 
     * <field>
     *   Content: XPathExprApprox : An XPath expression 
     * </field>
     * 
     * 
     * <notation 
     *   id = ID 
     *   name = NCName 
     *   public = A public identifier, per ISO 8879 
     *   system = uriReference>
     *   Content: (annotation?)
     * </notation>
     * 
     * <annotation>
     *   Content: (appinfo | documentation)*
     * </annotation>
     * 
     * <include 
     *   id = ID 
     *   schemaLocation = uriReference>
     *   Content: (annotation?)
     * </include>
     * 
     * <import 
     *   id = ID 
     *   namespace = uriReference 
     *   schemaLocation = uriReference>
     *   Content: (annotation?)
     * </import>
     * 
     * <simpleType
     *   abstract = boolean 
     *   base = QName 
     *   derivedBy = | list | restriction  : restriction
     *   id = ID 
     *   name = NCName>
     *   Content: ( annotation? , ( minExclusive | minInclusive | maxExclusive | maxInclusive | precision | scale | length | minLength | maxLength | encoding | period | duration | enumeration | pattern )* )
     * </simpleType>
     * 
     * <length
     *   id = ID 
     *   value = nonNegativeInteger>
     *   Content: ( annotation? )
     * </length>
     * 
     * <minLength
     *   id = ID 
     *   value = nonNegativeInteger>
     *   Content: ( annotation? )
     * </minLength>
     * 
     * <maxLength
     *   id = ID 
     *   value = nonNegativeInteger>
     *   Content: ( annotation? )
     * </maxLength>
     * 
     * 
     * <pattern
     *   id = ID 
     *   value = string>
     *   Content: ( annotation? )
     * </pattern>
     * 
     * 
     * <enumeration
     *   id = ID 
     *   value = string>
     *   Content: ( annotation? )
     * </enumeration>
     * 
     * <maxInclusive
     *   id = ID 
     *   value = string>
     *   Content: ( annotation? )
     * </maxInclusive>
     * 
     * <maxExclusive
     *   id = ID 
     *   value = string>
     *   Content: ( annotation? )
     * </maxExclusive>
     * 
     * <minInclusive
     *   id = ID 
     *   value = string>
     *   Content: ( annotation? )
     * </minInclusive>
     * 
     * 
     * <minExclusive
     *   id = ID 
     *   value = string>
     *   Content: ( annotation? )
     * </minExclusive>
     * 
     * <precision
     *   id = ID 
     *   value = nonNegativeInteger>
     *   Content: ( annotation? )
     * </precision>
     * 
     * <scale
     *   id = ID 
     *   value = nonNegativeInteger>
     *   Content: ( annotation? )
     * </scale>
     * 
     * <encoding
     *   id = ID 
     *   value = | hex | base64 >
     *   Content: ( annotation? )
     * </encoding>
     * 
     * 
     * <duration
     *   id = ID 
     *   value = timeDuration>
     *   Content: ( annotation? )
     * </duration>
     * 
     * <period
     *   id = ID 
     *   value = timeDuration>
     *   Content: ( annotation? )
     * </period>
     * 
 * 
 * @author Eric Ye, Jeffrey Rodriguez, Andy Clark
 *  
 * @see                  org.apache.xerces.validators.common.Grammar
 *
 * @version $Id: TraverseSchema.java 316029 2000-08-28 21:07:36Z ericye $
 */

public class TraverseSchema implements 
                            NamespacesScope.NamespacesHandler{

    
    private static final int TOP_LEVEL_SCOPE = -1;

    private static boolean DEBUGGING = false;



    private XMLErrorReporter    fErrorReporter = null;
    private StringPool          fStringPool    = null;

    private GrammarResolver fGrammarResolver = null;
    private SchemaGrammar fSchemaGrammar = null;

    private Element fSchemaRootElement;

    private DatatypeValidatorFactoryImpl fDatatypeRegistry =  
                          DatatypeValidatorFactoryImpl.getDatatypeRegistry();

    private Hashtable fComplexTypeRegistry = new Hashtable();
    private Hashtable fAttributeDeclRegistry = new Hashtable();

    private Vector fIncludeLocations = new Vector();
    private Vector fImportLocations = new Vector();


    private int fAnonTypeCount =0;
    private int fScopeCount=0;
    private int fCurrentScope=TOP_LEVEL_SCOPE;
    private int fSimpleTypeAnonCount = 0;
    private Stack fCurrentTypeNameStack = new Stack();
    private Hashtable fElementRecurseComplex = new Hashtable();

    private boolean fElementDefaultQualified = false;
    private boolean fAttributeDefaultQualified = false;

    private int fTargetNSURI;
    private String fTargetNSURIString = "";
    private NamespacesScope fNamespacesScope = null;
    private String fCurrentSchemaURL = "";

    private XMLAttributeDecl fTempAttributeDecl = new XMLAttributeDecl();
    private XMLElementDecl fTempElementDecl = new XMLElementDecl();

    public class ComplexTypeInfo {
        public String typeName;
        
        public DatatypeValidator baseDataTypeValidator;
        public ComplexTypeInfo baseComplexTypeInfo;

        public int derivedBy = 0;
        public int blockSet = 0;
        public int finalSet = 0;

        public boolean isAbstract = false;

        public int scopeDefined = -1;

        public int contentType;
        public int contentSpecHandle = -1;
        public int templateElementIndex = -1;
        public int attlistHead = -1;
        public DatatypeValidator datatypeValidator;
    }



    private TraverseSchema( ) {
    }


    public void setGrammarResolver(GrammarResolver grammarResolver){
        fGrammarResolver = grammarResolver;
    }
    public void startNamespaceDeclScope(int prefix, int uri){
    }
    public void endNamespaceDeclScope(int prefix){
    }

    

    private String resolvePrefixToURI (String prefix) throws Exception  {
        String uriStr = fStringPool.toString(fNamespacesScope.getNamespaceForPrefix(fStringPool.addSymbol(prefix)));
        if (uriStr == null) {
            reportGenericSchemaError("prefix : [" + prefix +"] can not be resolved to a URI");
            return "";
        }

        if ( prefix.length()==0 && uriStr.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) 
             && fTargetNSURIString.length() == 0) {
            uriStr = "";
        }

        return uriStr;
    }

    public  TraverseSchema(Element root, StringPool stringPool, 
                           SchemaGrammar schemaGrammar, 
                           GrammarResolver grammarResolver,
                           XMLErrorReporter errorReporter,
                           String schemaURL
                           ) throws Exception {
        fErrorReporter = errorReporter;
        fCurrentSchemaURL = schemaURL;
        doTraverseSchema(root, stringPool, schemaGrammar, grammarResolver);
    }

    public  TraverseSchema(Element root, StringPool stringPool, 
                           SchemaGrammar schemaGrammar, 
                           GrammarResolver grammarResolver
                           ) throws Exception {
        doTraverseSchema(root, stringPool, schemaGrammar, grammarResolver);
    }

    public  void doTraverseSchema(Element root, StringPool stringPool, 
                           SchemaGrammar schemaGrammar, 
                           GrammarResolver grammarResolver) throws Exception {

        fNamespacesScope = new NamespacesScope(this);
        
        fSchemaRootElement = root;
        fStringPool = stringPool;
        fSchemaGrammar = schemaGrammar;
        fGrammarResolver = grammarResolver;

        if (root == null) { 
            return;
        }

        String rootPrefix = root.getPrefix();
        if( rootPrefix == null || rootPrefix.length() == 0 ){
            String xmlns = root.getAttribute("xmlns");
            if( xmlns.length() == 0 )
                root.setAttribute("xmlns", SchemaSymbols.URI_SCHEMAFORSCHEMA );
        }

        fTargetNSURIString = root.getAttribute(SchemaSymbols.ATT_TARGETNAMESPACE);
        if (fTargetNSURIString==null) {
            fTargetNSURIString="";
        }
        fTargetNSURI = fStringPool.addSymbol(fTargetNSURIString);

        if (fGrammarResolver == null) {
            reportGenericSchemaError("Internal error: don't have a GrammarResolver for TraverseSchema");
        }
        else{
            fSchemaGrammar.setComplexTypeRegistry(fComplexTypeRegistry);
            fSchemaGrammar.setDatatypeRegistry(fDatatypeRegistry);
            fSchemaGrammar.setAttributeDeclRegistry(fAttributeDeclRegistry);
            fSchemaGrammar.setNamespacesScope(fNamespacesScope);
            fSchemaGrammar.setTargetNamespaceURI(fTargetNSURIString);
            fGrammarResolver.putGrammar(fTargetNSURIString, fSchemaGrammar);
        }
        


        NamedNodeMap schemaEltAttrs = root.getAttributes();
        int i = 0;
        Attr sattr = null;

        boolean seenXMLNS = false;                                        
        while ((sattr = (Attr)schemaEltAttrs.item(i++)) != null) {
            String attName = sattr.getName();
            if (attName.startsWith("xmlns:")) {
                String attValue = sattr.getValue();
                String prefix = attName.substring(attName.indexOf(":")+1);
                fNamespacesScope.setNamespaceForPrefix( fStringPool.addSymbol(prefix),
                                                        fStringPool.addSymbol(attValue) );
            }
            if (attName.equals("xmlns")) {

                String attValue = sattr.getValue();
                fNamespacesScope.setNamespaceForPrefix( fStringPool.addSymbol(""),
                                                        fStringPool.addSymbol(attValue) );
                seenXMLNS = true;
            }

        }
        if (!seenXMLNS && fTargetNSURIString.length() == 0 ) {
            fNamespacesScope.setNamespaceForPrefix( fStringPool.addSymbol(""),
                                                    fStringPool.addSymbol("") );
        }

        fElementDefaultQualified = 
            root.getAttribute(SchemaSymbols.ATT_ELEMENTFORMDEFAULT).equals(SchemaSymbols.ATTVAL_QUALIFIED);
        fAttributeDefaultQualified = 
            root.getAttribute(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT).equals(SchemaSymbols.ATTVAL_QUALIFIED);
        
        if (fTargetNSURI == StringPool.EMPTY_STRING) {
            fElementDefaultQualified = true;
        }


        fCurrentScope = -1;


        checkTopLevelDuplicateNames(root);

        extractTopLevel3Components(root);

        for (Element child = XUtil.getFirstChildElement(root); child != null;
            child = XUtil.getNextSiblingElement(child)) {

            String name = child.getNodeName();

            if (name.equals(SchemaSymbols.ELT_ANNOTATION) ) {
                traverseAnnotationDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_SIMPLETYPE )) {
                traverseSimpleTypeDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_COMPLEXTYPE )) {
                traverseComplexTypeDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_ELEMENT )) { 
                traverseElementDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
            } else if (name.equals( SchemaSymbols.ELT_ATTRIBUTE ) ) {
                traverseAttributeDecl( child, null );
            } else if (name.equals( SchemaSymbols.ELT_WILDCARD) ) {
                traverseWildcardDecl( child);
            } else if (name.equals(SchemaSymbols.ELT_GROUP) && child.getAttribute(SchemaSymbols.ATT_REF).equals("")) {
            } else if (name.equals(SchemaSymbols.ELT_NOTATION)) {
            }
            else if (name.equals(SchemaSymbols.ELT_INCLUDE)) {
                traverseInclude(child); 
            }
            else if (name.equals(SchemaSymbols.ELT_IMPORT)) {
                traverseImport(child); 
            }


    private void checkTopLevelDuplicateNames(Element root) {
    }

    private void extractTopLevel3Components(Element root){
        
        for (Element child = XUtil.getFirstChildElement(root); child != null;
            child = XUtil.getNextSiblingElement(child)) {

            String name = child.getNodeName();

            if (name.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                fSchemaGrammar.topLevelAttrGrpDecls.put(name, child);
            } else if (name.equals( SchemaSymbols.ELT_ATTRIBUTE ) ) {
                fSchemaGrammar.topLevelAttrDecls.put(name, child);
            } else if (name.equals(SchemaSymbols.ELT_GROUP) && child.getAttribute(SchemaSymbols.ATT_REF).equals("")) {
                fSchemaGrammar.topLevelGroupDecls.put(name, child);
            } 
    }

    /**
     * Expands a system id and returns the system id as a URL, if
     * it can be expanded. A return value of null means that the
     * identifier is already expanded. An exception thrown
     * indicates a failure to expand the id.
     *
     * @param systemId The systemId to be expanded.
     *
     * @return Returns the URL object representing the expanded system
     *         identifier. A null value indicates that the given
     *         system identifier is already expanded.
     *
     */
    private String expandSystemId(String systemId, String currentSystemId) throws Exception{
     String id = systemId;

     if (id == null || id.length() == 0) {
         return systemId;
     }

     try {
         URL url = new URL(id);
         if (url != null) {
             return systemId;
         }
     }
     catch (MalformedURLException e) {
     }

     id = fixURI(id);

     URL base = null;
     URL url = null;
     try {
         if (currentSystemId == null) {
             String dir;
             try {
                 dir = fixURI(System.getProperty("user.dir"));
             }
             catch (SecurityException se) {
                 dir = "";
             }
             if (!dir.endsWith("/")) {
                 dir = dir + "/";
             }
             base = new URL("file", "", dir);
         }
         else {
             base = new URL(currentSystemId);
         }

         url = new URL(base, id);
     }
     catch (Exception e) {
     }
     if (url == null) {
         return systemId;
     }
     return url.toString();
    }
    /**
     * Fixes a platform dependent filename to standard URI form.
     *
     * @param str The string to fix.
     *
     * @return Returns the fixed URI string.
     */
    private static String fixURI(String str) {

        str = str.replace(java.io.File.separatorChar, '/');

        if (str.length() >= 2) {
            char ch1 = str.charAt(1);
            if (ch1 == ':') {
                char ch0 = Character.toUpperCase(str.charAt(0));
                if (ch0 >= 'A' && ch0 <= 'Z') {
                    str = "/" + str;
                }
            }
        }

        return str;
    }


    private void traverseInclude(Element includeDecl) throws Exception {


        String location = includeDecl.getAttribute(SchemaSymbols.ATT_SCHEMALOCATION);
        location = expandSystemId(location, fCurrentSchemaURL);

        if (fIncludeLocations.contains((Object)location)) {
            return;
        }
        fIncludeLocations.addElement((Object)location);

        DOMParser parser = new DOMParser() {
            public void ignorableWhitespace(char ch[], int start, int length) {}
            public void ignorableWhitespace(int dataIdx) {}
        };
        parser.setEntityResolver( new Resolver() );
        parser.setErrorHandler(  new ErrorHandler() );

        try {
        }catch(  org.xml.sax.SAXNotRecognizedException e ) {
            e.printStackTrace();
        }catch( org.xml.sax.SAXNotSupportedException e ) {
            e.printStackTrace();
        }

        try {
            parser.parse( location);
        }catch( IOException e ) {
            e.printStackTrace();
        }catch( SAXException e ) {
        }

        Element root = null;
        if (document != null) {
            root = document.getDocumentElement();
        }

        if (root != null) {
            String targetNSURI = root.getAttribute(SchemaSymbols.ATT_TARGETNAMESPACE);
            if (targetNSURI.length() > 0 && !targetNSURI.equals(fTargetNSURIString) ) {
                reportGenericSchemaError("included schema '"+location+"' has a different targetNameSpace '"
                                         +targetNSURI+"'");
            }
            else {
                boolean saveElementDefaultQualified = fElementDefaultQualified;
                boolean saveAttributeDefaultQualified = fAttributeDefaultQualified;
                int saveScope = fCurrentScope;
                String savedSchemaURL = fCurrentSchemaURL;
                Element saveRoot = fSchemaRootElement;
                fSchemaRootElement = root;
                fCurrentSchemaURL = location;
                traverseIncludedSchema(root);
                fCurrentSchemaURL = savedSchemaURL;
                fCurrentScope = saveScope;
                fElementDefaultQualified = saveElementDefaultQualified;
                fAttributeDefaultQualified = saveAttributeDefaultQualified;
                fSchemaRootElement = saveRoot;
            }

        }

    }

    private void traverseIncludedSchema(Element root) throws Exception {
        NamedNodeMap schemaEltAttrs = root.getAttributes();
        int i = 0;
        Attr sattr = null;

        boolean seenXMLNS = false;                                        
        while ((sattr = (Attr)schemaEltAttrs.item(i++)) != null) {
            String attName = sattr.getName();
            if (attName.startsWith("xmlns:")) {
                String attValue = sattr.getValue();
                String prefix = attName.substring(attName.indexOf(":")+1);
                fNamespacesScope.setNamespaceForPrefix( fStringPool.addSymbol(prefix),
                                                        fStringPool.addSymbol(attValue) );
            }
            if (attName.equals("xmlns")) {

                String attValue = sattr.getValue();
                fNamespacesScope.setNamespaceForPrefix( fStringPool.addSymbol(""),
                                                        fStringPool.addSymbol(attValue) );
                seenXMLNS = true;
            }

        }
        if (!seenXMLNS && fTargetNSURIString.length() == 0 ) {
            fNamespacesScope.setNamespaceForPrefix( fStringPool.addSymbol(""),
                                                    fStringPool.addSymbol("") );
        }

        fElementDefaultQualified = 
            root.getAttribute(SchemaSymbols.ATT_ELEMENTFORMDEFAULT).equals(SchemaSymbols.ATTVAL_QUALIFIED);
        fAttributeDefaultQualified = 
            root.getAttribute(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT).equals(SchemaSymbols.ATTVAL_QUALIFIED);
        
        if (fTargetNSURI == StringPool.EMPTY_STRING) {
            fElementDefaultQualified = true;
        }

        fCurrentScope = -1;


        checkTopLevelDuplicateNames(root);

        extractTopLevel3Components(root);

        for (Element child = XUtil.getFirstChildElement(root); child != null;
            child = XUtil.getNextSiblingElement(child)) {

            String name = child.getNodeName();

            if (name.equals(SchemaSymbols.ELT_ANNOTATION) ) {
                traverseAnnotationDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_SIMPLETYPE )) {
                traverseSimpleTypeDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_COMPLEXTYPE )) {
                traverseComplexTypeDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_ELEMENT )) { 
                traverseElementDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
            } else if (name.equals( SchemaSymbols.ELT_ATTRIBUTE ) ) {
                traverseAttributeDecl( child, null );
            } else if (name.equals( SchemaSymbols.ELT_WILDCARD) ) {
                traverseWildcardDecl( child);
            } else if (name.equals(SchemaSymbols.ELT_GROUP) && child.getAttribute(SchemaSymbols.ATT_REF).equals("")) {
            } else if (name.equals(SchemaSymbols.ELT_NOTATION)) {
            }
            else if (name.equals(SchemaSymbols.ELT_INCLUDE)) {
                traverseInclude(child); 
            }
            else if (name.equals(SchemaSymbols.ELT_IMPORT)) {
                traverseImport(child); 
            }

    }

    private void traverseImport(Element importDecl)  throws Exception {
        String location = importDecl.getAttribute(SchemaSymbols.ATT_SCHEMALOCATION);
        location = expandSystemId(location, fCurrentSchemaURL);


        String namespaceString = importDecl.getAttribute(SchemaSymbols.ATT_NAMESPACE);
        SchemaGrammar importedGrammar = new SchemaGrammar();
        if (fGrammarResolver.getGrammar(namespaceString) != null) {
            importedGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(namespaceString);
        }

        if (fImportLocations.contains((Object)location)) {
            return;
        }
        fImportLocations.addElement((Object)location);

        DOMParser parser = new DOMParser() {
            public void ignorableWhitespace(char ch[], int start, int length) {}
            public void ignorableWhitespace(int dataIdx) {}
        };
        parser.setEntityResolver( new Resolver() );
        parser.setErrorHandler(  new ErrorHandler() );

        try {
        }catch(  org.xml.sax.SAXNotRecognizedException e ) {
            e.printStackTrace();
        }catch( org.xml.sax.SAXNotSupportedException e ) {
            e.printStackTrace();
        }

        try {
            parser.parse( location);
        }catch( IOException e ) {
            e.printStackTrace();
        }catch( SAXException e ) {
            e.printStackTrace();
        }

        Element root = null;
        if (document != null) {
            root = document.getDocumentElement();
        }

        if (root != null) {
            String targetNSURI = root.getAttribute(SchemaSymbols.ATT_TARGETNAMESPACE);
            if (!targetNSURI.equals(namespaceString) ) {
                reportGenericSchemaError("imported schema '"+location+"' has a different targetNameSpace '"
                                         +targetNSURI+"' from what is declared '"+namespaceString+"'.");
            }
            else
                new TraverseSchema(root, fStringPool, importedGrammar, fGrammarResolver, fErrorReporter, location);
        }
        else {
            reportGenericSchemaError("Could not get the doc root for imported Schema file: "+location);
        }
    }

/**
     * No-op - Traverse Annotation Declaration
     * 
     * @param comment
     */
    private void traverseAnnotationDecl(Element comment) {
        return ;
    }

    /**
     * Traverse SimpleType declaration:
     * <simpleType
     *         abstract = boolean 
     *         base = QName 
     *         derivedBy = | list | restriction  : restriction
     *         id = ID 
     *         name = NCName>
     *         Content: ( annotation? , ( minExclusive | minInclusive | maxExclusive | maxInclusive | precision | scale | length | minLength | maxLength | encoding | period | duration | enumeration | pattern )* )
     *       </simpleType>
     * 
     * @param simpleTypeDecl
     * @return 
     */
    private int traverseSimpleTypeDecl( Element simpleTypeDecl ) throws Exception {
        
        String varietyProperty       =  simpleTypeDecl.getAttribute( SchemaSymbols.ATT_DERIVEDBY );
        if (varietyProperty.length() == 0) {
            varietyProperty = SchemaSymbols.ATTVAL_RESTRICTION;
        }
        String nameProperty          =  simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME );
        String baseTypeQNameProperty =  simpleTypeDecl.getAttribute( SchemaSymbols.ATT_BASE );
        String abstractProperty      =  simpleTypeDecl.getAttribute( SchemaSymbols.ATT_ABSTRACT );

        int     newSimpleTypeName    = -1;


            newSimpleTypeName = fStringPool.addSymbol(
                "#S#"+fSimpleTypeAnonCount++ );   
            } else 
            newSimpleTypeName       = fStringPool.addSymbol( nameProperty );


        int               basetype;
        DatatypeValidator baseValidator = null;

        if( baseTypeQNameProperty!= null ) {
            basetype      = fStringPool.addSymbol( baseTypeQNameProperty );
            String prefix = "";
            String localpart = baseTypeQNameProperty;
            int colonptr = baseTypeQNameProperty.indexOf(":");
            if ( colonptr > 0) {
                prefix = baseTypeQNameProperty.substring(0,colonptr);
                localpart = baseTypeQNameProperty.substring(colonptr+1);
            }
            String uri = resolvePrefixToURI(prefix);

            baseValidator = getDatatypeValidator(uri, localpart);

            if (baseValidator == null) {
                Element baseTypeNode = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
                if (baseTypeNode != null) {
                    traverseSimpleTypeDecl( baseTypeNode );
                    
                    baseValidator = getDatatypeValidator(uri, localpart);
                    
                    if (baseValidator == null) {
                        reportSchemaError(SchemaMessageProvider.UnknownBaseDatatype,
                        new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_BASE ),
                                                          simpleTypeDecl.getAttribute(SchemaSymbols.ATT_NAME) });
                        return -1;
                    }

                }
                else {
                    reportSchemaError(SchemaMessageProvider.UnknownBaseDatatype,
                    new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_BASE ),
                                                      simpleTypeDecl.getAttribute(SchemaSymbols.ATT_NAME) });
                    return -1;
                }
                
            }
        }

        Element   content   = XUtil.getFirstChildElement( simpleTypeDecl );
        int       numFacets = 0; 
        Hashtable facetData = null;

        if( content != null ) {


            if( content.getNodeName().equals( SchemaSymbols.ELT_ANNOTATION ) ){
                traverseAnnotationDecl( content );   
                content                    = XUtil.getNextSiblingElement(content);
            } 



            
            int numEnumerationLiterals = 0;
            facetData        = new Hashtable();
            Vector enumData            = new Vector();

            while (content != null) {
                if (content.getNodeType() == Node.ELEMENT_NODE) {
                    Element facetElt = (Element) content;
                    numFacets++;
                    if (facetElt.getNodeName().equals(SchemaSymbols.ELT_ENUMERATION)) {
                        numEnumerationLiterals++;
                        String enumVal = facetElt.getAttribute(SchemaSymbols.ATT_VALUE);
                        enumData.addElement(enumVal);
                        Element enumContent =  XUtil.getFirstChildElement( facetElt );
                        if( enumContent != null && enumContent != null && enumContent.getNodeName().equals( SchemaSymbols.ELT_ANNOTATION ) ){
                            traverseAnnotationDecl( content );   
                         } 
                    } else {
                    facetData.put(facetElt.getNodeName(),facetElt.getAttribute( SchemaSymbols.ATT_VALUE ));
                    }
                }
                content = XUtil.getNextSiblingElement(content);
            }
            if (numEnumerationLiterals > 0) {
               facetData.put(SchemaSymbols.ELT_ENUMERATION, enumData);
            }
        }

        
        String nameOfType = fStringPool.toString( newSimpleTypeName);
        if (fTargetNSURIString.length () != 0) {
            nameOfType = fTargetNSURIString+","+nameOfType;
        }
                 

        try {

           DatatypeValidator newValidator =
                 fDatatypeRegistry.getDatatypeValidator( nameOfType );

               boolean  derivedByList = 
                    varietyProperty.equals( SchemaSymbols.ATTVAL_LIST ) ? true:false;

               fDatatypeRegistry.createDatatypeValidator( nameOfType, baseValidator,
                              facetData, derivedByList ); 
              
               }
            
           } catch (Exception e) {
               reportSchemaError(SchemaMessageProvider.DatatypeError,new Object [] { e.getMessage() });
           }
        return fStringPool.addSymbol(nameOfType);
    }

    /*
    * <any 
    *   id = ID 
    *   maxOccurs = string 
    *   minOccurs = nonNegativeInteger 
    *   namespace = ##any | ##other | ##local | list of {uri, ##targetNamespace} 
    *   processContents = lax | skip | strict>
    *   Content: (annotation?)
    * </any>
    */
    private int traverseAny(Element child) throws Exception {
        int anyIndex = -1;
        String namespace = child.getAttribute(SchemaSymbols.ATT_NAMESPACE).trim();
        String processContents = child.getAttribute("processContents").trim();

        int processContentsAny = XMLContentSpec.CONTENTSPECNODE_ANY;
        int processContentsAnyOther = XMLContentSpec.CONTENTSPECNODE_ANY_OTHER;
        int processContentsAnyLocal = XMLContentSpec.CONTENTSPECNODE_ANY_LOCAL;

        if (processContents.length() > 0 && !processContents.equals("strict")) {
            if (processContents.equals("lax")) {
                processContentsAny = XMLContentSpec.CONTENTSPECNODE_ANY_LAX;
                processContentsAnyOther = XMLContentSpec.CONTENTSPECNODE_ANY_OTHER_LAX;
                processContentsAnyLocal = XMLContentSpec.CONTENTSPECNODE_ANY_LOCAL_LAX;
            }
            else if (processContents.equals("skip")) {
                processContentsAny = XMLContentSpec.CONTENTSPECNODE_ANY_SKIP;
                processContentsAnyOther = XMLContentSpec.CONTENTSPECNODE_ANY_OTHER_SKIP;
                processContentsAnyLocal = XMLContentSpec.CONTENTSPECNODE_ANY_LOCAL_SKIP;
            }
        }

        if (namespace.length() == 0 || namespace.equals("##any")) {
            anyIndex = fSchemaGrammar.addContentSpecNode(processContentsAny, -1, -1, false);
        }
        else if (namespace.equals("##other")) {
            String uri = child.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace");
            int uriIndex = fStringPool.addSymbol(uri);
            anyIndex = fSchemaGrammar.addContentSpecNode(processContentsAnyOther, -1, uriIndex, false);
        }
        else if (namespace.equals("##local")) {
            anyIndex = fSchemaGrammar.addContentSpecNode(processContentsAnyLocal, -1, -1, false);
        }
        else if (namespace.length() > 0) {
            StringTokenizer tokenizer = new StringTokenizer(namespace);
            Vector tokens = new Vector();
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                if (token.equals("##targetNamespace")) {
                    token = child.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace");
                }
                tokens.addElement(token);
            }
            String uri = (String)tokens.elementAt(0);
            int uriIndex = fStringPool.addSymbol(uri);
            int leafIndex = fSchemaGrammar.addContentSpecNode(processContentsAny, -1, uriIndex, false);
            int valueIndex = leafIndex;
            int count = tokens.size();
            if (count > 1) {
                uri = (String)tokens.elementAt(1);
                uriIndex = fStringPool.addSymbol(uri);
                leafIndex = fSchemaGrammar.addContentSpecNode(processContentsAny, -1, uriIndex, false);
                int otherValueIndex = leafIndex;
                int choiceIndex = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE, valueIndex, otherValueIndex, false);
                for (int i = 2; i < count; i++) {
                    uri = (String)tokens.elementAt(i);
                    uriIndex = fStringPool.addSymbol(uri);
                    leafIndex = fSchemaGrammar.addContentSpecNode(processContentsAny, -1, uriIndex, false);
                    otherValueIndex = leafIndex;
                    choiceIndex = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE, choiceIndex, otherValueIndex, false);
                }
                anyIndex = choiceIndex;
            }
            else {
                anyIndex = leafIndex;
            }
        }
        else {
            reportGenericSchemaError("Empty namespace attribute for any element");
        }

        return anyIndex;
    }


    public DatatypeValidator getDatatypeValidator(String uri, String localpart) {

        DatatypeValidator dv = null;

        if (uri.length()==0 || uri.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
            dv = fDatatypeRegistry.getDatatypeValidator( localpart );
        }
        else {
            dv = fDatatypeRegistry.getDatatypeValidator( uri+","+localpart );
        }

        return dv;
    }

    /*
    * <anyAttribute 
    *   id = ID 
    *   namespace = ##any | ##other | ##local | list of {uri, ##targetNamespace}>
    *   Content: (annotation?)
    * </anyAttribute>
    */
    private XMLAttributeDecl traverseAnyAttribute(Element anyAttributeDecl) throws Exception {
        XMLAttributeDecl anyAttDecl = new XMLAttributeDecl();
        String processContents = anyAttributeDecl.getAttribute(SchemaSymbols.ATT_PROCESSCONTENTS).trim();
        String namespace = anyAttributeDecl.getAttribute(SchemaSymbols.ATT_NAMESPACE).trim();
        String curTargetUri = anyAttributeDecl.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace");

        if ( namespace.length() == 0 || namespace.equals(SchemaSymbols.ATTVAL_TWOPOUNDANY) ) {
            anyAttDecl.type = XMLAttributeDecl.TYPE_ANY_ANY;
        } 
        else if (namespace.equals(SchemaSymbols.ATTVAL_TWOPOUNDOTHER)) {
            anyAttDecl.type = XMLAttributeDecl.TYPE_ANY_OTHER;
            anyAttDecl.name.uri = fStringPool.addSymbol(curTargetUri);
        }
        else if (namespace.equals(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL)) {
            anyAttDecl.type = XMLAttributeDecl.TYPE_ANY_LOCAL;
        }
        else if (namespace.length() > 0){
            anyAttDecl.type = XMLAttributeDecl.TYPE_ANY_LIST;

            StringTokenizer tokenizer = new StringTokenizer(namespace);
            int aStringList = fStringPool.startStringList();
            Vector tokens = new Vector();
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                if (token.equals("##targetNamespace")) {
                    token = curTargetUri;
                }
                if (!fStringPool.addStringToList(aStringList, fStringPool.addSymbol(token))){
                    reportGenericSchemaError("Internal StringPool error when reading the "+
                                             "namespace attribute for anyattribute declaration");
                }
            }
            fStringPool.finishStringList(aStringList);

            anyAttDecl.enumeration = aStringList;
        }
        else {
            reportGenericSchemaError("Empty namespace attribute for anyattribute declaration");
        }

        anyAttDecl.defaultType = XMLAttributeDecl.PROCESSCONTENTS_STRICT;

        if (processContents.equals(SchemaSymbols.ATTVAL_SKIP)){
            anyAttDecl.defaultType = XMLAttributeDecl.PROCESSCONTENTS_SKIP;
        }
        else if (processContents.equals(SchemaSymbols.ATTVAL_LAX)) {
            anyAttDecl.defaultType = XMLAttributeDecl.PROCESSCONTENTS_LAX;
        }

        return anyAttDecl; 
    }

    private XMLAttributeDecl mergeTwoAnyAttribute(XMLAttributeDecl oneAny, XMLAttributeDecl anotherAny) {
        if (oneAny.type == -1) {
            return oneAny;
        }
        if (anotherAny.type == -1) {
            return anotherAny;
        }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_ANY) {
            return anotherAny;
        }

        if (anotherAny.type == XMLAttributeDecl.TYPE_ANY_ANY) {
            return oneAny;
        }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_OTHER) {
            if (anotherAny.type == XMLAttributeDecl.TYPE_ANY_OTHER) {

                if ( anotherAny.name.uri == oneAny.name.uri ) {
                    return oneAny;
                }
                else {
                    oneAny.type = -1;
                    return oneAny;
                }

            }
            else if (anotherAny.type == XMLAttributeDecl.TYPE_ANY_LOCAL) {
                return anotherAny;
            }
            else if (anotherAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
                if (!fStringPool.stringInList(anotherAny.enumeration, oneAny.name.uri) ) {
                    return anotherAny;
                }
                else {
                    int[] anotherAnyURIs = fStringPool.stringListAsIntArray(anotherAny.enumeration);
                    int newList = fStringPool.startStringList();
                    for (int i=0; i< anotherAnyURIs.length; i++) {
                        if (anotherAnyURIs[i] != oneAny.name.uri ) {
                            fStringPool.addStringToList(newList, anotherAnyURIs[i]);
                        }
                    }
                    fStringPool.finishStringList(newList);
                    anotherAny.enumeration = newList;
                    return anotherAny;
                }
            }
        }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_LOCAL) {
            if ( anotherAny.type == XMLAttributeDecl.TYPE_ANY_OTHER
                || anotherAny.type == XMLAttributeDecl.TYPE_ANY_LOCAL) {
                return oneAny;
            }
            else if (anotherAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
                oneAny.type = -1;
                return oneAny;
            }
        }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
            if ( anotherAny.type == XMLAttributeDecl.TYPE_ANY_OTHER){
                if (!fStringPool.stringInList(oneAny.enumeration, anotherAny.name.uri) ) {
                    return oneAny;
                }
                else {
                    int[] oneAnyURIs = fStringPool.stringListAsIntArray(oneAny.enumeration);
                    int newList = fStringPool.startStringList();
                    for (int i=0; i< oneAnyURIs.length; i++) {
                        if (oneAnyURIs[i] != anotherAny.name.uri ) {
                            fStringPool.addStringToList(newList, oneAnyURIs[i]);
                        }
                    }
                    fStringPool.finishStringList(newList);
                    oneAny.enumeration = newList;
                    return oneAny;
                }

            }
            else if ( anotherAny.type == XMLAttributeDecl.TYPE_ANY_LOCAL) {
                oneAny.type = -1;
                return oneAny;
            }
            else if (anotherAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
                int[] result = intersect2sets( fStringPool.stringListAsIntArray(oneAny.enumeration), 
                                               fStringPool.stringListAsIntArray(anotherAny.enumeration));
                int newList = fStringPool.startStringList();
                for (int i=0; i<result.length; i++) {
                    fStringPool.addStringToList(newList, result[i]);
                }
                fStringPool.finishStringList(newList);
                oneAny.enumeration = newList;
                return oneAny;
            }
        }

        return oneAny;
    }

    int[] intersect2sets(int[] one, int[] theOther){
        int[] result = new int[(one.length>theOther.length?one.length:theOther.length)];

        int count = 0;
        for (int i=0; i<one.length; i++) {
            for(int j=0; j<theOther.length; j++) {
                if (one[i]==theOther[j]) {
                    result[count++] = one[i];
                }
            }
        }

        int[] result2 = new int[count];
        System.arraycopy(result, 0, result2, 0, count);

        return result2;
    }

    /**
     * Traverse ComplexType Declaration.
     *  
     *       <complexType 
     *         abstract = boolean 
     *         base = QName 
     *         block = #all or (possibly empty) subset of {extension, restriction} 
     *         content = elementOnly | empty | mixed | textOnly 
     *         derivedBy = extension | restriction 
     *         final = #all or (possibly empty) subset of {extension, restriction} 
     *         id = ID 
     *         name = NCName>
     *          Content: (annotation? , (((minExclusive | minInclusive | maxExclusive
     *                    | maxInclusive | precision | scale | length | minLength 
     *                    | maxLength | encoding | period | duration | enumeration 
     *                    | pattern)* | (element | group | all | choice | sequence | any)*) , 
     *                    ((attribute | attributeGroup)* , anyAttribute?)))
     *        </complexType>
     * @param complexTypeDecl
     * @return 
     */
    
    private int traverseComplexTypeDecl( Element complexTypeDecl ) throws Exception { 
        String isAbstract = complexTypeDecl.getAttribute( SchemaSymbols.ATT_ABSTRACT );
        String base = complexTypeDecl.getAttribute(SchemaSymbols.ATT_BASE);
        String blockSet = complexTypeDecl.getAttribute( SchemaSymbols.ATT_BLOCK );
        String content = complexTypeDecl.getAttribute(SchemaSymbols.ATT_CONTENT);
        String derivedBy = complexTypeDecl.getAttribute( SchemaSymbols.ATT_DERIVEDBY );
        String finalSet = complexTypeDecl.getAttribute( SchemaSymbols.ATT_FINAL );
        String typeId = complexTypeDecl.getAttribute( SchemaSymbols.ATTVAL_ID );
        String typeName = complexTypeDecl.getAttribute(SchemaSymbols.ATT_NAME); 
        boolean isNamedType = false;

        if ( DEBUGGING )
            System.out.println("traversing complex Type : " + typeName +","+base+","+content+".");

            typeName = "#"+fAnonTypeCount++;
        }
        else {
            fCurrentTypeNameStack.push(typeName);
            isNamedType = true;
        }

        if (isTopLevel(complexTypeDecl)) {
        
            String fullName = fTargetNSURIString+","+typeName;
            ComplexTypeInfo temp = (ComplexTypeInfo) fComplexTypeRegistry.get(fullName);
            if (temp != null ) {
                return fStringPool.addSymbol(fullName);
            }
        }

        int scopeDefined = fScopeCount++;
        int previousScope = fCurrentScope;
        fCurrentScope = scopeDefined;

        Element child = null;
        int contentSpecType = -1;
        int csnType = 0;
        int left = -2;
        int right = -2;

        DatatypeValidator simpleTypeValidator = null;
        int baseTypeSymbol = -1;
        String fullBaseName = "";
        boolean baseIsSimpleSimple = false;
        boolean baseIsComplexSimple = false;
        boolean derivedByRestriction = true;
        boolean derivedByExtension = false;
        int baseContentSpecHandle = -1;
        Element baseTypeNode = null;


        if (base.length()>0) {

            if (derivedBy.length() == 0) {
                reportGenericSchemaError("derivedBy must be present when base is present in " 
                                         +SchemaSymbols.ELT_COMPLEXTYPE
                                         +" "+ typeName);
            }
            else {
                if (derivedBy.equals(SchemaSymbols.ATTVAL_EXTENSION)) {
                    derivedByRestriction = false;
                }
                
                String prefix = "";
                String localpart = base;
                int colonptr = base.indexOf(":");
                if ( colonptr > 0) {
                    prefix = base.substring(0,colonptr);
                    localpart = base.substring(colonptr+1);
                }
                int localpartIndex = fStringPool.addSymbol(localpart);
                String typeURI = resolvePrefixToURI(prefix);
                
                if ( ! typeURI.equals(fTargetNSURIString) 
                     && ! typeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) 
                     && typeURI.length() != 0 )  /*REVISIT, !!!! a hack: for schema that has no target namespace, e.g. personal-schema.xml*/{
                    baseTypeInfo = getTypeInfoFromNS(typeURI, localpart);
                    if (baseTypeInfo == null) {
                        baseTypeValidator = getTypeValidatorFromNS(typeURI, localpart);
                        if (baseTypeValidator == null) {
                            System.out.println("Could not find base type " +localpart 
                                               + " in schema " + typeURI);
                        }
                        else{
                            baseIsSimpleSimple = true;
                        }
                    }
                }
                else {
                
                    fullBaseName = typeURI+","+localpart;
                    
                    baseTypeInfo = (ComplexTypeInfo) fComplexTypeRegistry.get(fullBaseName);

                    if (baseTypeInfo == null) {
                            baseTypeValidator = getDatatypeValidator(typeURI, localpart);

                        if (baseTypeValidator == null) {
                            baseTypeNode = getTopLevelComponentByName(SchemaSymbols.ELT_COMPLEXTYPE,localpart);
                            if (baseTypeNode != null) {
                                baseTypeSymbol = traverseComplexTypeDecl( baseTypeNode );
                                baseTypeInfo = (ComplexTypeInfo)
                            }
                            else {
                                baseTypeNode = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
                                if (baseTypeNode != null) {
                                    baseTypeSymbol = traverseSimpleTypeDecl( baseTypeNode );
                                    simpleTypeValidator = baseTypeValidator = getDatatypeValidator(typeURI, localpart);
                                    if (simpleTypeValidator == null) {
                                    }

                                    baseIsSimpleSimple = true;
                                }
                                else {
                                    reportGenericSchemaError("Base type could not be found : " + base);
                                }
                            }
                        }
                        else {
                            simpleTypeValidator = baseTypeValidator;
                            baseIsSimpleSimple = true;
                        }
                    }
                }
                if (baseIsSimpleSimple && derivedByRestriction) {
                    reportGenericSchemaError("base is a simpledType, can't derive by restriction in " + typeName); 
                }

                if (baseTypeInfo != null ) {

                    if (derivedByRestriction) {
                    }
                    else {
                    }

                    if ( baseTypeInfo.contentSpecHandle > -1) {
                        if (derivedByRestriction) {
                            checkParticleDerivationOK(complexTypeDecl, baseTypeNode);
                        }
                        baseContentSpecHandle = baseTypeInfo.contentSpecHandle;
                    }
                    else if ( baseTypeInfo.datatypeValidator != null ) {
                        baseTypeValidator = baseTypeInfo.datatypeValidator;
                        baseIsComplexSimple = true;
                    }
                }

                if (baseIsComplexSimple && !derivedByRestriction ) {
                    reportGenericSchemaError("base is ComplexSimple, can't derive by extension in " + typeName);
                }



        child = null;

        if (baseIsComplexSimple) {

            contentSpecType = XMLElementDecl.TYPE_SIMPLE;
            
            int numEnumerationLiterals = 0;
            int numFacets = 0;
            Hashtable facetData        = new Hashtable();
            Vector enumData            = new Vector();


            for (child = XUtil.getFirstChildElement(complexTypeDecl);
                 child != null && (child.getNodeName().equals(SchemaSymbols.ELT_MINEXCLUSIVE) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_MININCLUSIVE) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_MAXEXCLUSIVE) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_MAXINCLUSIVE) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_PRECISION) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_SCALE) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_LENGTH) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_MINLENGTH) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_MAXLENGTH) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_ENCODING) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_PERIOD) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_DURATION) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_ENUMERATION) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_PATTERN) ||
                                   child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION));
                 child = XUtil.getNextSiblingElement(child)) 
            {
                if ( child.getNodeType() == Node.ELEMENT_NODE ) {
                    Element facetElt = (Element) child;
                    numFacets++;
                    if (facetElt.getNodeName().equals(SchemaSymbols.ELT_ENUMERATION)) {
                        numEnumerationLiterals++;
                        enumData.addElement(facetElt.getAttribute(SchemaSymbols.ATT_VALUE));
                        Element enumContent =  XUtil.getFirstChildElement( facetElt );
                        if( enumContent != null && enumContent.getNodeName().equals( SchemaSymbols.ELT_ANNOTATION ) ){
                            traverseAnnotationDecl( child );   
                        }
                    } else {
                        facetData.put(facetElt.getNodeName(),facetElt.getAttribute( SchemaSymbols.ATT_VALUE ));
                    }
                }
            }
            if (numEnumerationLiterals > 0) {
                facetData.put(SchemaSymbols.ELT_ENUMERATION, enumData);
            }

            if (numFacets > 0) {
                simpleTypeValidator = fDatatypeRegistry.createDatatypeValidator( typeName, baseTypeValidator,
                                                           facetData, false ); 
            }
            else
                simpleTypeValidator = baseTypeValidator;

            if (child != null) {
                reportGenericSchemaError("Invalid child '"+child.getNodeName()+"' in complexType : '" + typeName 
                                         + "', because it restricts another complexSimpleType");
            }
        }

        if (content.equals(SchemaSymbols.ATTVAL_TEXTONLY)) {
            if (base.length() == 0) {
                simpleTypeValidator = baseTypeValidator = getDatatypeValidator("", SchemaSymbols.ATTVAL_STRING);
            }
            else if ( baseTypeValidator == null 
                        reportSchemaError(SchemaMessageProvider.NotADatatype,
            contentSpecType = XMLElementDecl.TYPE_SIMPLE;
            /****
            left = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                 fStringPool.addSymbol(base),
                                                 -1, false);
            /****/                                                 

        } 
        else {   
            if (!baseIsComplexSimple) {
                contentSpecType = XMLElementDecl.TYPE_CHILDREN;
            }
            csnType = XMLContentSpec.CONTENTSPECNODE_SEQ;
            boolean mixedContent = false;
            boolean elementContent = true;
            boolean textContent = false;
            boolean emptyContent = false;
            left = -2;
            right = -2;
            boolean hadContent = false;

            if (content.equals(SchemaSymbols.ATTVAL_EMPTY)) {
                contentSpecType = XMLElementDecl.TYPE_EMPTY;
                emptyContent = true;
                elementContent = false;
            } else if (content.equals(SchemaSymbols.ATTVAL_MIXED) ) {
                contentSpecType = XMLElementDecl.TYPE_MIXED;
                mixedContent = true;
                elementContent = false;
                csnType = XMLContentSpec.CONTENTSPECNODE_CHOICE;
            } else if (content.equals(SchemaSymbols.ATTVAL_ELEMENTONLY) || content.equals("")) {
                elementContent = true;
            } else if (content.equals(SchemaSymbols.ATTVAL_TEXTONLY)) {
                textContent = true;
                elementContent = false;
            }

            if (mixedContent) {

                left = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                         -1, false);
                csnType = XMLContentSpec.CONTENTSPECNODE_CHOICE;
            }

            boolean seeParticle = false;
            boolean seeOtherParticle = false;                
            boolean seeAll = false;

            for (child = XUtil.getFirstChildElement(complexTypeDecl);
                 child != null;
                 child = XUtil.getNextSiblingElement(child)) {

                hadContent = true;

                seeParticle = false;

                String childName = child.getNodeName();

                if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
                    if (mixedContent || elementContent) {
                        if ( DEBUGGING )
                            System.out.println(" child element name " + child.getAttribute(SchemaSymbols.ATT_NAME));

                        QName eltQName = traverseElementDecl(child);
                        index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                                   eltQName.localpart,
                                                                   eltQName.uri, 
                                                                   false);
                        seeParticle = true;
                        seeOtherParticle = true;

                    } 
                    else {
                        reportSchemaError(SchemaMessageProvider.EltRefOnlyInMixedElemOnly, null);
                    }

                } 
                else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                    index = traverseGroupDecl(child);
                    seeParticle = true;
                    seeOtherParticle = true;
                } 
                else if (childName.equals(SchemaSymbols.ELT_ALL)) {
                    index = traverseAll(child);
                    seeParticle = true;
                    seeAll = true;
                  
                } 
                else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
                    index = traverseChoice(child);
                    seeParticle = true;
                    seeOtherParticle = true;                  
                } 
                else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                    index = traverseSequence(child);
                    seeParticle = true;
                    seeOtherParticle = true;                  
                } 
                else if (childName.equals(SchemaSymbols.ELT_ATTRIBUTE) ||
                           childName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                } 
                else if (childName.equals(SchemaSymbols.ELT_ANY)) {

                    index = traverseAny(child);
                    seeParticle = true;
                    seeOtherParticle = true;
                } 
                else if (childName.equals(SchemaSymbols.ELT_ANNOTATION)) {
                } 
                else if (childName.equals(SchemaSymbols.ELT_ANYATTRIBUTE)) {
                    break;
                } 
                    if (!baseIsComplexSimple ) 
                    if (base.equals(""))
                        reportSchemaError(SchemaMessageProvider.GenericError, 
                                          new Object [] { "unrecogized child '"+childName+"' in compelx type "+typeName });
                    else
                        reportSchemaError(SchemaMessageProvider.GenericError,
                                          new Object [] { "unrecogized child '"+childName+"' in compelx type '"+typeName+"' with base "+base  });
                }

                if (baseIsComplexSimple && seeParticle) {
                    reportGenericSchemaError("In complexType "+typeName+", base type is complextype with simpleType content, can't have any particle children at all");
                    hadContent = false;
                    left = index = -2;
                    contentSpecType = XMLElementDecl.TYPE_SIMPLE;
                    break;
                }

                if (seeParticle) {
                    index = expandContentModel(index, child);

                if (seeAll && seeOtherParticle) {
                    reportGenericSchemaError ( " 'All' group needs to be the only child in Complextype : " + typeName);
                }

                if (seeAll) {
                }

                if (left == -2) {
                    left = index;
                } else if (right == -2) {
                    right = index;
                } else {
                    left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);
                    right = index;
                }

            if ( ! ( seeOtherParticle || seeAll ) && (elementContent || mixedContent)
                 &&  (base.length() == 0 || ( base.length() > 0 && derivedByRestriction && !baseIsComplexSimple))  ) {
                contentSpecType = XMLElementDecl.TYPE_SIMPLE;
                simpleTypeValidator = getDatatypeValidator("", SchemaSymbols.ATTVAL_STRING);
                reportGenericSchemaError ( " complexType '"+typeName+"' with a elementOnly or mixed content "
                                           +"need to have at least one particle child");
            }

            if (hadContent && right != -2)
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);

            if (mixedContent && hadContent) {
                left = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE,
                                                     left, -1, false);
            }
        }

        if (!derivedByRestriction && baseContentSpecHandle > -1 ) {
            if (left == -2) {
                left = baseContentSpecHandle;
            }
            else 
                left = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ, 
                                                         baseContentSpecHandle,
                                                         left,
                                                         false);
        }

        if (content.length() == 0 && base.length() == 0 && left == -2) {
            contentSpecType = XMLElementDecl.TYPE_ANY;
        }

        if (content.length() == 0 && simpleTypeValidator == null && left == -2 ) {
            if (base.length() > 0 && baseTypeInfo != null 
                && baseTypeInfo.contentType == XMLElementDecl.TYPE_EMPTY) {
                contentSpecType = XMLElementDecl.TYPE_EMPTY;
            }
        }

        if ( DEBUGGING )
            System.out.println("!!!!!>>>>>" + typeName+", "+ baseTypeInfo + ", " 
                               + baseContentSpecHandle +", " + left +", "+scopeDefined);

        ComplexTypeInfo typeInfo = new ComplexTypeInfo();
        typeInfo.baseComplexTypeInfo = baseTypeInfo;
        typeInfo.baseDataTypeValidator = baseTypeValidator;
        int derivedByInt = -1;
        if (derivedBy.length() > 0) {
            derivedByInt = parseComplexDerivedBy(derivedBy);
        }
        typeInfo.derivedBy = derivedByInt;
        typeInfo.scopeDefined = scopeDefined; 
        typeInfo.contentSpecHandle = left;
        typeInfo.contentType = contentSpecType;
        typeInfo.datatypeValidator = simpleTypeValidator;
        typeInfo.blockSet = parseBlockSet(complexTypeDecl.getAttribute(SchemaSymbols.ATT_BLOCK));
        typeInfo.finalSet = parseFinalSet(complexTypeDecl.getAttribute(SchemaSymbols.ATT_FINAL));
        typeInfo.isAbstract = isAbstract.equals(SchemaSymbols.ATTVAL_TRUE) ? true:false ;

        int typeNameIndex = fStringPool.addSymbol(typeName);
        int templateElementNameIndex = fStringPool.addSymbol("$"+typeName);

        typeInfo.templateElementIndex = 
            fSchemaGrammar.addElementDecl(new QName(-1, templateElementNameIndex,typeNameIndex,fTargetNSURI),
                                          (fTargetNSURI==-1) ? -1 : fCurrentScope, scopeDefined,
                                            contentSpecType, left, 
                                          -1, simpleTypeValidator);
        typeInfo.attlistHead = fSchemaGrammar.getFirstAttributeDeclIndex(typeInfo.templateElementIndex);


        XMLAttributeDecl attWildcard = null;
        Vector anyAttDecls = new Vector();

        for (child = XUtil.getFirstChildElement(complexTypeDecl);
             child != null;
             child = XUtil.getNextSiblingElement(child)) {

            String childName = child.getNodeName();

            if (childName.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                if ((baseIsComplexSimple||baseIsSimpleSimple) && derivedByRestriction) {
                    reportGenericSchemaError("In complexType "+typeName+
                                             ", base type has simpleType "+
                                             "content and derivation method is"+
                                             " 'restriction', can't have any "+
                                             "attribute children at all");
                    break;
                }
                traverseAttributeDecl(child, typeInfo);
            } 
            else if ( childName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ) { 
                if ((baseIsComplexSimple||baseIsSimpleSimple) && derivedByRestriction) {
                    reportGenericSchemaError("In complexType "+typeName+", base "+
                                             "type has simpleType content and "+
                                             "derivation method is 'restriction',"+
                                             " can't have any attribute children at all");
                    break;
                }
                traverseAttributeGroupDecl(child,typeInfo,anyAttDecls);
            }
            else if ( childName.equals(SchemaSymbols.ELT_ANYATTRIBUTE) ) { 
                attWildcard = traverseAnyAttribute(child);
            }
        }

        if (attWildcard != null) {
            XMLAttributeDecl fromGroup = null;
            final int count = anyAttDecls.size();
            if ( count > 0) {
                fromGroup = (XMLAttributeDecl) anyAttDecls.elementAt(0);
                for (int i=1; i<count; i++) {
                    fromGroup = mergeTwoAnyAttribute(fromGroup,(XMLAttributeDecl)anyAttDecls.elementAt(i));
                }
            }
            if (fromGroup != null) {
                int saveProcessContents = attWildcard.defaultType;
                attWildcard = mergeTwoAnyAttribute(attWildcard, fromGroup);
                attWildcard.defaultType = saveProcessContents;
            }
        }
        else {
        }

        XMLAttributeDecl baseAttWildcard = null;
        if (baseTypeInfo != null && baseTypeInfo.attlistHead > -1 ) {
            int attDefIndex = baseTypeInfo.attlistHead;
            while ( attDefIndex > -1 ) {
                fTempAttributeDecl.clear();
                fSchemaGrammar.getAttributeDecl(attDefIndex, fTempAttributeDecl);
                if (fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_ANY 
                    ||fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_LIST
                    ||fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_LOCAL 
                    ||fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_OTHER ) {
                    if (attWildcard == null) {
                        baseAttWildcard = fTempAttributeDecl;
                    }
                    attDefIndex = fSchemaGrammar.getNextAttributeDeclIndex(attDefIndex);
                    continue;
                }
                /**/
                int temp = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, fTempAttributeDecl.name);
                if ( temp > -1) {
                    if (derivedByRestriction) {
                        attDefIndex = fSchemaGrammar.getNextAttributeDeclIndex(attDefIndex);
                        continue;
                    }
                }
                /**/
                fSchemaGrammar.addAttDef( typeInfo.templateElementIndex, 
                                          fTempAttributeDecl.name, fTempAttributeDecl.type, 
                                          fTempAttributeDecl.enumeration, fTempAttributeDecl.defaultType, 
                                          fTempAttributeDecl.defaultValue, 
                                          fTempAttributeDecl.datatypeValidator,
                                          fTempAttributeDecl.list);
                attDefIndex = fSchemaGrammar.getNextAttributeDeclIndex(attDefIndex);
            }
        }
        if (attWildcard != null) {
            if (attWildcard.type != -1) {
                fSchemaGrammar.addAttDef( typeInfo.templateElementIndex, 
                                          attWildcard.name, attWildcard.type, 
                                          attWildcard.enumeration, attWildcard.defaultType, 
                                          attWildcard.defaultValue, 
                                          attWildcard.datatypeValidator,
                                          attWildcard.list);
            }
            else {
            }
        }
        else if (baseAttWildcard != null) {
            fSchemaGrammar.addAttDef( typeInfo.templateElementIndex, 
                                      baseAttWildcard.name, baseAttWildcard.type, 
                                      baseAttWildcard.enumeration, baseAttWildcard.defaultType, 
                                      baseAttWildcard.defaultValue, 
                                      baseAttWildcard.datatypeValidator,
                                      baseAttWildcard.list);
        }

        typeInfo.attlistHead = fSchemaGrammar.getFirstAttributeDeclIndex(typeInfo.templateElementIndex);

        if (!typeName.startsWith("#")) {
            typeName = fTargetNSURIString + "," + typeName;
        }
        typeInfo.typeName = new String(typeName);
        if ( DEBUGGING )
            System.out.println("add complex Type to Registry: " + typeName +","+content+".");
        fComplexTypeRegistry.put(typeName,typeInfo);

        fCurrentScope = previousScope;
        if (isNamedType) {
            fCurrentTypeNameStack.pop();
            checkRecursingComplexType();
        }

        fSchemaGrammar.setElementComplexTypeInfo(typeInfo.templateElementIndex, typeInfo);

        typeNameIndex = fStringPool.addSymbol(typeName);
        return typeNameIndex;



    private void checkRecursingComplexType() throws Exception {
        if ( fCurrentTypeNameStack.empty() ) {
            if (! fElementRecurseComplex.isEmpty() ) {
                Enumeration e = fElementRecurseComplex.keys();
                while( e.hasMoreElements() ) {
                    QName nameThenScope = (QName) e.nextElement();
                    String typeName = (String) fElementRecurseComplex.get(nameThenScope);

                    int eltUriIndex = nameThenScope.uri;
                    int eltNameIndex = nameThenScope.localpart;
                    int enclosingScope = nameThenScope.prefix;
                    ComplexTypeInfo typeInfo = 
                        (ComplexTypeInfo) fComplexTypeRegistry.get(fTargetNSURIString+","+typeName);
                    if (typeInfo==null) {
                        throw new Exception ( "Internal Error in void checkRecursingComplexType(). " );
                    }
                    else {
                        int elementIndex = fSchemaGrammar.addElementDecl(new QName(-1, eltNameIndex, eltNameIndex, eltUriIndex), 
                                                                         enclosingScope, typeInfo.scopeDefined, 
                                                                         typeInfo.contentType, 
                                                                         typeInfo.contentSpecHandle, 
                                                                         typeInfo.attlistHead, 
                                                                         typeInfo.datatypeValidator);
                        fSchemaGrammar.setElementComplexTypeInfo(elementIndex, typeInfo);
                    }

                }
                fElementRecurseComplex.clear();
            }
        }
    }

    private void checkParticleDerivationOK(Element derivedTypeNode, Element baseTypeNode) {
    }

    private int expandContentModel ( int index, Element particle) throws Exception {
        
        String minOccurs = particle.getAttribute(SchemaSymbols.ATT_MINOCCURS);
        String maxOccurs = particle.getAttribute(SchemaSymbols.ATT_MAXOCCURS);    

        int min=1, max=1;

        if (minOccurs.equals("")) {
            minOccurs = "1";
        }
        if (maxOccurs.equals("") ){
            if ( minOccurs.equals("0")) {
                maxOccurs = "1";
            }
            else {
                maxOccurs = minOccurs;
            }
        }


        int leafIndex = index;
        if (minOccurs.equals("1")&& maxOccurs.equals("1")) {

        }
        else if (minOccurs.equals("0")&& maxOccurs.equals("1")) {
            index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE,
                                                   index,
                                                   -1,
                                                   false);
        }
        else if (minOccurs.equals("0")&& maxOccurs.equals("unbounded")) {
            index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE,
                                                   index,
                                                   -1,
                                                   false);
        }
        else if (minOccurs.equals("1")&& maxOccurs.equals("unbounded")) {
            index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE,
                                                   index,
                                                   -1,
                                                   false);
        }
        else if (maxOccurs.equals("unbounded") ) {
            try {
                min = Integer.parseInt(minOccurs);
            }
            catch (Exception e) {
                reportSchemaError(SchemaMessageProvider.GenericError,
                                  new Object [] { "illegal value for minOccurs : '" +e.getMessage()+ "' " });
            }
            if (min<2) {
            }

            index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE,
                   index,
                   -1,
                   false);

            for (int i=0; i < (min-1); i++) {
                index = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                      leafIndex,
                                                      index,
                                                      false);
            }

        }
        else {
            try {
                min = Integer.parseInt(minOccurs);
                max = Integer.parseInt(maxOccurs);
            }
            catch (Exception e){
                reportSchemaError(SchemaMessageProvider.GenericError,
                                  new Object [] { "illegal value for minOccurs or maxOccurs : '" +e.getMessage()+ "' "});
            }
            if (min==0) {
                int optional = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE,
                                                                 leafIndex,
                                                                 -1,
                                                                 false);
                index = optional;
                for (int i=0; i < (max-min-1); i++) {
                    index = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                              index,
                                                              optional,
                                                              false);
                }
            }
            else {
                for (int i=0; i<(min-1); i++) {
                    index = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                          index,
                                                          leafIndex,
                                                          false);
                }

                int optional = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE,
                                                                 leafIndex,
                                                                 -1,
                                                                 false);
                for (int i=0; i < (max-min); i++) {
                    index = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                              index,
                                                              optional,
                                                              false);
                }
            }
        }

        return index;
    }

    /**
     * Traverses Schema attribute declaration.
     *   
     *       <attribute 
     *         form = qualified | unqualified 
     *         id = ID 
     *         name = NCName 
     *         ref = QName 
     *         type = QName 
     *         use = default | fixed | optional | prohibited | required 
     *         value = string>
     *         Content: (annotation? , simpleType?)
     *       <attribute/>
     * 
     * @param attributeDecl
     * @return 
     * @exception Exception
     */
    private int traverseAttributeDecl( Element attrDecl, ComplexTypeInfo typeInfo ) throws Exception {
        String attNameStr    = attrDecl.getAttribute(SchemaSymbols.ATT_NAME);

        DatatypeValidator dv = null;
        int attType          = -1;
        boolean attIsList    = false;
        int dataTypeSymbol   = -1;

        String ref       = attrDecl.getAttribute(SchemaSymbols.ATT_REF); 
        String datatype  = attrDecl.getAttribute(SchemaSymbols.ATT_TYPE);
        String localpart = null;

        if (!ref.equals("")) {
            if (XUtil.getFirstChildElement(attrDecl) != null)
                reportSchemaError(SchemaMessageProvider.NoContentForRef, null);
            String prefix = "";
            localpart = ref;
            int colonptr = ref.indexOf(":");
            if ( colonptr > 0) {
                prefix = ref.substring(0,colonptr);
                localpart = ref.substring(colonptr+1);
            }
            String uriStr = resolvePrefixToURI(prefix);

            if (!uriStr.equals(fTargetNSURIString)) {
                addAttributeDeclFromAnotherSchema(localpart, uriStr, typeInfo);

                return -1;
            }

            Element referredAttribute = getTopLevelComponentByName(SchemaSymbols.ELT_ATTRIBUTE,localpart);
            if (referredAttribute != null) {
                traverseAttributeDecl(referredAttribute, typeInfo);
            }
            else {
                reportGenericSchemaError ( "Couldn't find top level attribute " + ref);
            }
            return -1;
        }


        if (datatype.equals("")) {
            Element child = XUtil.getFirstChildElement(attrDecl);

            while (child != null && 
                             !child.getNodeName().equals(SchemaSymbols.ELT_SIMPLETYPE))
                child = XUtil.getNextSiblingElement(child);


            if (child != null && child.getNodeName().equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                attType        = XMLAttributeDecl.TYPE_SIMPLE;
                dataTypeSymbol = traverseSimpleTypeDecl(child);
                localpart = fStringPool.toString(dataTypeSymbol);
            } 
            else {
                attType        = XMLAttributeDecl.TYPE_SIMPLE;
                localpart      = "string";
                dataTypeSymbol = fStringPool.addSymbol(localpart);
            }
            localpart = fStringPool.toString(dataTypeSymbol);

            dv = fDatatypeRegistry.getDatatypeValidator(localpart);

        } else {

            String prefix = "";
            localpart = datatype;
            dataTypeSymbol = fStringPool.addSymbol(localpart);

            int  colonptr = datatype.indexOf(":");
            if ( colonptr > 0) {
                prefix = datatype.substring(0,colonptr);
                localpart = datatype.substring(colonptr+1);
            }
            String typeURI = resolvePrefixToURI(prefix);

            if ( typeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) 
                 || typeURI.length()==0) {

                dv = getDatatypeValidator("", localpart);

                if (localpart.equals("ID")) {
                    attType = XMLAttributeDecl.TYPE_ID;
                } else if (localpart.equals("IDREF")) {
                    attType = XMLAttributeDecl.TYPE_IDREF;
                } else if (localpart.equals("IDREFS")) {
                    attType = XMLAttributeDecl.TYPE_IDREF;
                    attIsList = true;
                } else if (localpart.equals("ENTITY")) {
                    attType = XMLAttributeDecl.TYPE_ENTITY;
                } else if (localpart.equals("ENTITIES")) {
                    attType = XMLAttributeDecl.TYPE_ENTITY;
                    attIsList = true;
                } else if (localpart.equals("NMTOKEN")) {
                    attType = XMLAttributeDecl.TYPE_NMTOKEN;
                } else if (localpart.equals("NMTOKENS")) {
                    attType = XMLAttributeDecl.TYPE_NMTOKEN;
                    attIsList = true;
                } else if (localpart.equals(SchemaSymbols.ELT_NOTATION)) {
                    attType = XMLAttributeDecl.TYPE_NOTATION;
                }
                else {
                    attType = XMLAttributeDecl.TYPE_SIMPLE;
                    if (dv == null && typeURI.length() == 0) {
                        Element topleveltype = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
                        if (topleveltype != null) {
                            traverseSimpleTypeDecl( topleveltype );
                            dv = getDatatypeValidator(typeURI, localpart);
                        }else {
                            reportGenericSchemaError("simpleType not found : " + localpart);
                        }
                    }
                }
            } else {


                dv = getDatatypeValidator(typeURI, localpart);
                if (dv == null && typeURI.equals(fTargetNSURIString) ) {
                    Element topleveltype = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
                    if (topleveltype != null) {
                        traverseSimpleTypeDecl( topleveltype );
                        dv = getDatatypeValidator(typeURI, localpart);
                    }else {
                        reportGenericSchemaError("simpleType not found : " + localpart);
                    }
                }

                attType = XMLAttributeDecl.TYPE_SIMPLE;
            }

        }


        int attDefaultType = -1;
        int attDefaultValue = -1;

        String  use      = attrDecl.getAttribute(SchemaSymbols.ATT_USE);
        boolean required = use.equals(SchemaSymbols.ATTVAL_REQUIRED);


        if (dv == null) {
            reportGenericSchemaError("could not resolve the type or get a null validator for datatype : " 
                                     + fStringPool.toString(dataTypeSymbol));
        }

        if (required) {
            attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_REQUIRED;
        } else {
            if (use.equals(SchemaSymbols.ATTVAL_FIXED)) {
                String fixed = attrDecl.getAttribute(SchemaSymbols.ATT_VALUE);
                if (!fixed.equals("")) {
                    attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_FIXED;
                    attDefaultValue = fStringPool.addString(fixed);
                } 
            }
            else if (use.equals(SchemaSymbols.ATTVAL_DEFAULT)) {
                String defaultValue = attrDecl.getAttribute(SchemaSymbols.ATT_VALUE);
                if (!defaultValue.equals("")) {
                    attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_DEFAULT;
                    attDefaultValue = fStringPool.addString(defaultValue);
                } 
            }
            else if (use.equals(SchemaSymbols.ATTVAL_PROHIBITED)) {
                
                attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_IMPLIED;
            }
            else {
                attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_IMPLIED;

            if (attType == XMLAttributeDecl.TYPE_SIMPLE && attDefaultValue != -1) {
                try { 
                    if (dv != null) 
                        dv.validate(fStringPool.toString(attDefaultValue), null);
                    else
                        reportSchemaError(SchemaMessageProvider.NoValidatorFor,
                                          new Object [] { datatype });
                } catch (InvalidDatatypeValueException idve) {
                    reportSchemaError(SchemaMessageProvider.IncorrectDefaultType,
                                      new Object [] { attrDecl.getAttribute(SchemaSymbols.ATT_NAME), idve.getMessage() });
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Internal error in attribute datatype validation");
                }
            }
        }

        int uriIndex = -1;
        if ( isQName.equals(SchemaSymbols.ATTVAL_QUALIFIED)||
             fAttributeDefaultQualified || isTopLevel(attrDecl) ) {
            uriIndex = fTargetNSURI;
        }

        QName attQName = new QName(-1,attName,attName,uriIndex);
        if ( DEBUGGING )
            System.out.println(" the dataType Validator for " + fStringPool.toString(attName) + " is " + dv);

        if (isTopLevel(attrDecl)) {
            fTempAttributeDecl.datatypeValidator = dv;
            fTempAttributeDecl.name.setValues(attQName);
            fTempAttributeDecl.type = attType;
            fTempAttributeDecl.defaultType = attDefaultType;
            fTempAttributeDecl.list = attIsList;
            if (attDefaultValue != -1 ) {
                fTempAttributeDecl.defaultValue = new String(fStringPool.toString(attDefaultValue));
            }
            fAttributeDeclRegistry.put(attNameStr, new XMLAttributeDecl(fTempAttributeDecl));
        }

        if (typeInfo != null) {
            fSchemaGrammar.addAttDef( typeInfo.templateElementIndex, 
                                      attQName, attType, 
                                      dataTypeSymbol, attDefaultType, 
                                      fStringPool.toString( attDefaultValue), dv, attIsList);
        }
        return -1;

    private int addAttributeDeclFromAnotherSchema( String name, String uriStr, ComplexTypeInfo typeInfo) throws Exception {
        SchemaGrammar aGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(uriStr);
        if (uriStr == null || ! (aGrammar instanceof SchemaGrammar) ) {
            reportGenericSchemaError("!!Schema not found in #addAttributeDeclFromAnotherSchema, schema uri : " + uriStr);
            return -1;
        }

        Hashtable attrRegistry = aGrammar.getAttirubteDeclRegistry();
        if (attrRegistry == null) {
            reportGenericSchemaError("no attribute was defined in schema : " + uriStr);
            return -1;
        }

        XMLAttributeDecl tempAttrDecl = (XMLAttributeDecl) attrRegistry.get(name);

        if (tempAttrDecl == null) {
            reportGenericSchemaError( "no attribute named \"" + name 
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }


        if (typeInfo!= null) {
            fSchemaGrammar.addAttDef( typeInfo.templateElementIndex, 
                                      tempAttrDecl.name, tempAttrDecl.type,
                                      -1, tempAttrDecl.defaultType,
                                      tempAttrDecl.defaultValue, 
                                      tempAttrDecl.datatypeValidator, 
                                      tempAttrDecl.list);
        }


        return 0;
    }

    /*
    * 
    * <attributeGroup 
    *   id = ID 
    *   name = NCName
    *   ref = QName>
    *   Content: (annotation?, (attribute|attributeGroup), anyAttribute?)
    * </>
    * 
    */
    private int traverseAttributeGroupDecl( Element attrGrpDecl, ComplexTypeInfo typeInfo, Vector anyAttDecls ) throws Exception {
        int attGrpName = fStringPool.addSymbol(attrGrpDecl.getAttribute(SchemaSymbols.ATT_NAME));
        
        String ref = attrGrpDecl.getAttribute(SchemaSymbols.ATT_REF); 

        int attType = -1;
        int enumeration = -1;

        if (!ref.equals("")) {
            if (XUtil.getFirstChildElement(attrGrpDecl) != null)
                reportSchemaError(SchemaMessageProvider.NoContentForRef, null);
            String prefix = "";
            String localpart = ref;
            int colonptr = ref.indexOf(":");
            if ( colonptr > 0) {
                prefix = ref.substring(0,colonptr);
                localpart = ref.substring(colonptr+1);
            }
            String uriStr = resolvePrefixToURI(prefix);
            if (!uriStr.equals(fTargetNSURIString)) {
                
                traverseAttributeGroupDeclFromAnotherSchema(localpart, uriStr, typeInfo, anyAttDecls);

                return -1;
            }
            Element referredAttrGrp = getTopLevelComponentByName(SchemaSymbols.ELT_ATTRIBUTEGROUP,localpart);
            if (referredAttrGrp != null) {
                traverseAttributeGroupDecl(referredAttrGrp, typeInfo, anyAttDecls);
            }
            else {
                reportGenericSchemaError ( "Couldn't find top level attributegroup " + ref);
            }
            return -1;
        }

        for ( Element child = XUtil.getFirstChildElement(attrGrpDecl); 
             child != null ; child = XUtil.getNextSiblingElement(child)) {
       
            if ( child.getNodeName().equals(SchemaSymbols.ELT_ATTRIBUTE) ){
                traverseAttributeDecl(child, typeInfo);
            }
            else if ( child.getNodeName().equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ) {
                traverseAttributeGroupDecl(child, typeInfo,anyAttDecls);
            }
            else if ( child.getNodeName().equals(SchemaSymbols.ELT_ANYATTRIBUTE) ) {
                anyAttDecls.addElement(traverseAnyAttribute(child));
                break;
            }
            else if (child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION) ) {
            }
        }
        return -1;
    
    private int traverseAttributeGroupDeclFromAnotherSchema( String attGrpName , String uriStr, 
                                                             ComplexTypeInfo typeInfo,
                                                             Vector anyAttDecls ) throws Exception {
        
        SchemaGrammar aGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(uriStr);
        if (uriStr == null || aGrammar == null || ! (aGrammar instanceof SchemaGrammar) ) {
            reportGenericSchemaError("!!Schema not found in #traverseAttributeGroupDeclFromAnotherSchema, schema uri : " + uriStr);
            return -1;
        }
        Element attGrpDecl = (Element) aGrammar.topLevelAttrGrpDecls.get((Object)attGrpName);
        if (attGrpDecl == null) {
            reportGenericSchemaError( "no attribute group named \"" + attGrpName 
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }
        
        NamespacesScope saveNSMapping = fNamespacesScope;
        int saveTargetNSUri = fTargetNSURI;
        fTargetNSURI = fStringPool.addSymbol(aGrammar.getTargetNamespaceURI());
        fNamespacesScope = aGrammar.getNamespacesScope();

        int attType = -1;
        int enumeration = -1;


        for ( Element child = XUtil.getFirstChildElement(attGrpDecl); 
             child != null ; child = XUtil.getNextSiblingElement(child)) {

            if ( child.getNodeName().equals(SchemaSymbols.ELT_ATTRIBUTE) ){
                String childAttName = child.getAttribute(SchemaSymbols.ATT_NAME);
                if ( childAttName.length() > 0 ) {
                    Hashtable attDeclRegistry = aGrammar.getAttirubteDeclRegistry();
                    if (attDeclRegistry != null) {
                        if (attDeclRegistry.get((Object)childAttName) != null ){
                            addAttributeDeclFromAnotherSchema(childAttName, uriStr, typeInfo);
                            return -1;
                        }
                    }       
                }
                else 
                    traverseAttributeDecl(child, typeInfo);
            }
            else if ( child.getNodeName().equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ) {
                traverseAttributeGroupDecl(child, typeInfo, anyAttDecls);
            }
            else if ( child.getNodeName().equals(SchemaSymbols.ELT_ANYATTRIBUTE) ) {
                anyAttDecls.addElement(traverseAnyAttribute(child));
                break;
            }
            else if (child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION) ) {
            }
        }

        fNamespacesScope = saveNSMapping;
        fTargetNSURI = saveTargetNSUri;
        return -1;
    

    /**
     * Traverse element declaration:
     *  <element
     *         abstract = boolean
     *         block = #all or (possibly empty) subset of {equivClass, extension, restriction}
     *         default = string
     *         equivClass = QName
     *         final = #all or (possibly empty) subset of {extension, restriction}
     *         fixed = string
     *         form = qualified | unqualified
     *         id = ID
     *         maxOccurs = string
     *         minOccurs = nonNegativeInteger
     *         name = NCName
     *         nullable = boolean
     *         ref = QName
     *         type = QName>
     *   Content: (annotation? , (simpleType | complexType)? , (unique | key | keyref)*)
     *   </element>
     * 
     * 
     *       The following are identity-constraint definitions
     *        <unique 
     *         id = ID 
     *         name = NCName>
     *         Content: (annotation? , (selector , field+))
     *       </unique>
     *       
     *       <key 
     *         id = ID 
     *         name = NCName>
     *         Content: (annotation? , (selector , field+))
     *       </key>
     *       
     *       <keyref 
     *         id = ID 
     *         name = NCName 
     *         refer = QName>
     *         Content: (annotation? , (selector , field+))
     *       </keyref>
     *       
     *       <selector>
     *         Content: XPathExprApprox : An XPath expression 
     *       </selector>
     *       
     *       <field>
     *         Content: XPathExprApprox : An XPath expression 
     *       </field>
     *       
     * 
     * @param elementDecl
     * @return 
     * @exception Exception
     */
    private QName traverseElementDecl(Element elementDecl) throws Exception {

        int contentSpecType      = -1;
        int contentSpecNodeIndex = -1;
        int typeNameIndex = -1;
        DatatypeValidator dv = null;



        String name = elementDecl.getAttribute(SchemaSymbols.ATT_NAME);

        if ( DEBUGGING )
            System.out.println("traversing element decl : " + name );

        String ref = elementDecl.getAttribute(SchemaSymbols.ATT_REF);
        String type = elementDecl.getAttribute(SchemaSymbols.ATT_TYPE);
        String minOccurs = elementDecl.getAttribute(SchemaSymbols.ATT_MINOCCURS);
        String maxOccurs = elementDecl.getAttribute(SchemaSymbols.ATT_MAXOCCURS);
        String dflt = elementDecl.getAttribute(SchemaSymbols.ATT_DEFAULT);
        String fixed = elementDecl.getAttribute(SchemaSymbols.ATT_FIXED);
        String equivClass = elementDecl.getAttribute(SchemaSymbols.ATT_EQUIVCLASS);
        String isQName = elementDecl.getAttribute(SchemaSymbols.ATT_FORM);

        String fromAnotherSchema = null;

        if (isTopLevel(elementDecl)) {
        
            int nameIndex = fStringPool.addSymbol(name);
            int eltKey = fSchemaGrammar.getElementDeclIndex(fTargetNSURI, nameIndex,TOP_LEVEL_SCOPE);
            if (eltKey > -1 ) {
                return new QName(-1,nameIndex,nameIndex,fTargetNSURI);
            }
        }
        
        int blockSet = parseBlockSet(elementDecl.getAttribute(SchemaSymbols.ATT_BLOCK));
        int finalSet = parseFinalSet(elementDecl.getAttribute(SchemaSymbols.ATT_FINAL));
        boolean isNullable = elementDecl.getAttribute
            (SchemaSymbols.ATT_NULLABLE).equals(SchemaSymbols.ATTVAL_TRUE)? true:false;
        boolean isAbstract = elementDecl.getAttribute
            (SchemaSymbols.ATT_ABSTRACT).equals(SchemaSymbols.ATTVAL_TRUE)? true:false;
        int elementMiscFlags = 0;
        if (isNullable) {
            elementMiscFlags += SchemaSymbols.NULLABLE;
        }
        if (isAbstract) {
            elementMiscFlags += SchemaSymbols.ABSTRACT;
        }

        int attrCount = 0;
        if (!ref.equals("")) attrCount++;
        if (!type.equals("")) attrCount++;
        if (attrCount > 1)
            reportSchemaError(SchemaMessageProvider.OneOfTypeRefArchRef, null);

        if (!ref.equals("")) {
            if (XUtil.getFirstChildElement(elementDecl) != null)
                reportSchemaError(SchemaMessageProvider.NoContentForRef, null);
            String prefix = "";
            String localpart = ref;
            int colonptr = ref.indexOf(":");
            if ( colonptr > 0) {
                prefix = ref.substring(0,colonptr);
                localpart = ref.substring(colonptr+1);
            }
            int localpartIndex = fStringPool.addSymbol(localpart);
            String uriString = resolvePrefixToURI(prefix);
            QName eltName = new QName(prefix != null ? fStringPool.addSymbol(prefix) : -1,
                                      localpartIndex,
                                      fStringPool.addSymbol(ref),
                                      uriString != null ? fStringPool.addSymbol(uriString) : -1);

            if (! uriString.equals(fTargetNSURIString) ) {
                return eltName;
            }

            int elementIndex = fSchemaGrammar.getElementDeclIndex(eltName, TOP_LEVEL_SCOPE);

            if (elementIndex == -1 ) {
                Element targetElement = getTopLevelComponentByName(SchemaSymbols.ELT_ELEMENT,localpart);
                if (targetElement == null ) {
                    reportGenericSchemaError("Element " + localpart + " not found in the Schema");
                    return eltName;
                }
                else {
                }
            }
            return eltName;
        }
                
        Element equivClassElementDecl = null;
        int equivClassElementDeclIndex = -1;
        boolean noErrorSoFar = true;
        String equivClassUri = null;
        String equivClassLocalpart = null;
        String equivClassFullName = null;
        ComplexTypeInfo equivClassEltTypeInfo = null;
        DatatypeValidator equivClassEltDV = null;

        if ( equivClass.length() > 0 ) {
            equivClassUri =  resolvePrefixToURI(getPrefix(equivClass));
            equivClassLocalpart = getLocalPart(equivClass);
            equivClassFullName = equivClassUri+","+equivClassLocalpart;
           
            if ( !equivClassUri.equals(fTargetNSURIString) ) {  
                equivClassEltTypeInfo = getElementDeclTypeInfoFromNS(equivClassUri, equivClassLocalpart);
                if (equivClassEltTypeInfo == null) {
                    equivClassEltDV = getElementDeclTypeValidatorFromNS(equivClassUri, equivClassLocalpart);
                    if (equivClassEltDV == null) {
                        noErrorSoFar = false;
                        reportGenericSchemaError("Could not find type for element '" +equivClassLocalpart 
                                                 + "' in schema '" + equivClassUri+"'");
                    }
                }
            }
            else {
                equivClassElementDecl = getTopLevelComponentByName(SchemaSymbols.ELT_ELEMENT, equivClassLocalpart);
                if (equivClassElementDecl == null) {
                    equivClassElementDeclIndex = 
                        fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(equivClass),TOP_LEVEL_SCOPE);
                    if ( equivClassElementDeclIndex == -1) {
                        noErrorSoFar = false;
                        reportGenericSchemaError("Equivclass affiliation element "
                                                  +equivClass
                                                  +" in element declaration " 
                                                  +name);  
                    }
                }
                else {
                    equivClassElementDeclIndex = 
                        fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(equivClass),TOP_LEVEL_SCOPE);

                    if ( equivClassElementDeclIndex == -1) {
                        traverseElementDecl(equivClassElementDecl);
                        equivClassElementDeclIndex = 
                            fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(equivClass),TOP_LEVEL_SCOPE);
                    }
                }

                if (equivClassElementDeclIndex != -1) {
                    equivClassEltTypeInfo = fSchemaGrammar.getElementComplexTypeInfo( equivClassElementDeclIndex );
                    if (equivClassEltTypeInfo == null) {
                        fSchemaGrammar.getElementDecl(equivClassElementDeclIndex, fTempElementDecl);
                        equivClassEltDV = fTempElementDecl.datatypeValidator;
                        if (equivClassEltDV == null) {
                            noErrorSoFar = false;
                            reportGenericSchemaError("Could not find type for element '" +equivClassLocalpart 
                                                     + "' in schema '" + equivClassUri+"'");
                        }
                    }
                }
            }
 
        }
        


        ComplexTypeInfo typeInfo = null;

        Element child = XUtil.getFirstChildElement(elementDecl);
        
        while (child != null && child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION))
            child = XUtil.getNextSiblingElement(child);
        
        boolean haveAnonType = false;

        if (child != null) {
            
            String childName = child.getNodeName();
            
            if (childName.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                if (child.getAttribute(SchemaSymbols.ATT_NAME).length() > 0) {
                    noErrorSoFar = false;
                    reportGenericSchemaError("anonymous complexType in element '" + name +"' has a name attribute"); 
                }
                else 
                    typeNameIndex = traverseComplexTypeDecl(child);
                if (typeNameIndex != -1 ) {
                    typeInfo = (ComplexTypeInfo)
                        fComplexTypeRegistry.get(fStringPool.toString(typeNameIndex));
                }
                else {
                    noErrorSoFar = false;
                    reportGenericSchemaError("traverse complexType error in element '" + name +"'"); 
                }
                haveAnonType = true;
            } 
            else if (childName.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                if (child.getAttribute(SchemaSymbols.ATT_NAME).length() > 0) {
                    noErrorSoFar = false;
                    reportGenericSchemaError("anonymous simpleType in element '" + name +"' has a name attribute"); 
                }
                else 
                    typeNameIndex = traverseSimpleTypeDecl(child);
                if (typeNameIndex != -1) {
                    dv = fDatatypeRegistry.getDatatypeValidator(fStringPool.toString(typeNameIndex));
                }
                else {
                    noErrorSoFar = false;
                    reportGenericSchemaError("traverse simpleType error in element '" + name +"'"); 
                }
                contentSpecType = XMLElementDecl.TYPE_SIMPLE; 
                haveAnonType = true;
                contentSpecType = XMLElementDecl.TYPE_ANY;
                contentSpecNodeIndex = -1;
            } else {
                System.out.println("unhandled case in TraverseElementDecl");
            }
        } 

        if (haveAnonType && (type.length()>0)) {
            noErrorSoFar = false;
            reportGenericSchemaError( "Element '"+ name +
                                      "' have both a type attribute and a annoymous type child" );
        }
        else if (!type.equals("")) { 
            if (equivClassElementDecl != null) {
                checkEquivClassOK(elementDecl, equivClassElementDecl); 
            }
            String prefix = "";
            String localpart = type;
            int colonptr = type.indexOf(":");
            if ( colonptr > 0) {
                prefix = type.substring(0,colonptr);
                localpart = type.substring(colonptr+1);
            }
            String typeURI = resolvePrefixToURI(prefix);
            
            if ( !typeURI.equals(fTargetNSURIString) 
                 && !typeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)
                fromAnotherSchema = typeURI;
                typeInfo = getTypeInfoFromNS(typeURI, localpart);
                if (typeInfo == null) {
                    dv = getTypeValidatorFromNS(typeURI, localpart);
                    if (dv == null) {
                        noErrorSoFar = false;
                        reportGenericSchemaError("Could not find type " +localpart 
                                           + " in schema " + typeURI);
                    }
                }
            }
            else {
                typeInfo = (ComplexTypeInfo) fComplexTypeRegistry.get(typeURI+","+localpart);
                if (typeInfo == null) {
                    dv = getDatatypeValidator(typeURI, localpart);
                    if (dv == null )
                    if (typeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)
                        && !fTargetNSURIString.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) 
                    {
                        noErrorSoFar = false;
                        reportGenericSchemaError("type not found : " + typeURI+":"+localpart);
                    }
                    else {
                        Element topleveltype = getTopLevelComponentByName(SchemaSymbols.ELT_COMPLEXTYPE,localpart);
                        if (topleveltype != null) {
                            if (fCurrentTypeNameStack.search((Object)localpart) > - 1) {
                                int uriInd = -1;
                                if ( isQName.equals(SchemaSymbols.ATTVAL_QUALIFIED)||
                                     fElementDefaultQualified) {
                                    uriInd = fTargetNSURI;
                                }
                                int nameIndex = fStringPool.addSymbol(name);
                                QName tempQName = new QName(fCurrentScope, nameIndex, nameIndex, uriInd);
                                fElementRecurseComplex.put(tempQName, localpart);
                                return new QName(-1, nameIndex, nameIndex, uriInd);
                            }
                            else {
                                typeNameIndex = traverseComplexTypeDecl( topleveltype );
                                typeInfo = (ComplexTypeInfo)
                                    fComplexTypeRegistry.get(fStringPool.toString(typeNameIndex));
                            }
                        }
                        else {
                            topleveltype = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
                            if (topleveltype != null) {
                                typeNameIndex = traverseSimpleTypeDecl( topleveltype );
                                dv = getDatatypeValidator(typeURI, localpart);
                            }
                            else {
                                noErrorSoFar = false;
                                reportGenericSchemaError("type not found : " + typeURI+":"+localpart);
                            }

                        }

                    }
                }
            }
   
        } 
        else if (haveAnonType){
            if (equivClassElementDecl != null ) {
                checkEquivClassOK(elementDecl, equivClassElementDecl); 
            }

        }
        else {
            if ( typeInfo == null && dv == null ) typeInfo = equivClassEltTypeInfo;
            if ( typeInfo == null && dv == null ) dv = equivClassEltDV;
        }

        if (typeInfo == null && dv==null) {
            if (noErrorSoFar) {
                contentSpecType = XMLElementDecl.TYPE_ANY;
            }
            else {
                noErrorSoFar = false;
                reportGenericSchemaError ("untyped element : " + name );
            }
        }
        if (typeInfo!=null) {
            contentSpecNodeIndex = typeInfo.contentSpecHandle;
            contentSpecType = typeInfo.contentType;
            scopeDefined = typeInfo.scopeDefined;
            dv = typeInfo.datatypeValidator;
        }

        if (dv!=null) {
            contentSpecType = XMLElementDecl.TYPE_SIMPLE;
        }


        child = XUtil.getFirstChildElement(elementDecl);
        Vector idConstraints = null;
        
        while (child != null){
            String childName = child.getNodeName();
           /**** 
            if ( childName.equals(SchemaSymbols.ELT_KEY) ) { 
                traverseKey(child, idCnstrt);
            }
            else if ( childName.equals(SchemaSymbols.ELT_KEYREF) ) {
                traverseKeyRef(child, idCnstrt);
            }
            else if ( childName.equals(SchemaSymbols.ELT_UNIQUE) ) {
                traverseUnique(child, idCnstrt);
            }

            if (idCnstrt!= null) {
                if (idConstraints != null) {
                    idConstraints = new Vector();
                }
                idConstraints.addElement(idCnstrt);
            }
            /****/
            child = XUtil.getNextSiblingElement(child);
        }
        

        int elementNameIndex     = fStringPool.addSymbol(name);
        int localpartIndex = elementNameIndex;
        int uriIndex = -1;
        int enclosingScope = fCurrentScope;

        if ( isQName.equals(SchemaSymbols.ATTVAL_QUALIFIED)||
             fElementDefaultQualified ) {
            uriIndex = fTargetNSURI;
        }

        if ( isTopLevel(elementDecl)) {
            uriIndex = fTargetNSURI;
            enclosingScope = TOP_LEVEL_SCOPE;
        }


        int existSuchElementIndex = fSchemaGrammar.getElementDeclIndex(uriIndex, localpartIndex, enclosingScope);
        if ( existSuchElementIndex > -1) {
            fSchemaGrammar.getElementDecl(existSuchElementIndex, fTempElementDecl);
            DatatypeValidator edv = fTempElementDecl.datatypeValidator;
            ComplexTypeInfo eTypeInfo = fSchemaGrammar.getElementComplexTypeInfo(existSuchElementIndex);
            if ( ((eTypeInfo != null)&&(eTypeInfo!=typeInfo))
                 || ((edv != null)&&(edv != dv)) )  {
                noErrorSoFar = false;
                reportGenericSchemaError("duplicate element decl in the same scope : " + 
                                         fStringPool.toString(localpartIndex));

            }
        }

        QName eltQName = new QName(-1,localpartIndex,elementNameIndex,uriIndex);
        
        
        int attrListHead = -1 ;

        if (typeInfo != null) {
            attrListHead = typeInfo.attlistHead;
        }
        int elementIndex = fSchemaGrammar.addElementDecl(eltQName, enclosingScope, scopeDefined, 
                                                         contentSpecType, contentSpecNodeIndex, 
                                                         attrListHead, dv);
        if ( DEBUGGING ) {
            /***/
            System.out.println("########elementIndex:"+elementIndex+" ("+fStringPool.toString(eltQName.uri)+","
                               + fStringPool.toString(eltQName.localpart) + ")"+
                               " eltType:"+type+" contentSpecType:"+contentSpecType+
                               " SpecNodeIndex:"+ contentSpecNodeIndex +" enclosingScope: " +enclosingScope +
                               " scopeDefined: " +scopeDefined+"\n");
             /***/
        }

        if (typeInfo != null) {
            fSchemaGrammar.setElementComplexTypeInfo(elementIndex, typeInfo);
        }
        else {
            fSchemaGrammar.setElementComplexTypeInfo(elementIndex, typeInfo);

        }

        fSchemaGrammar.setElementFromAnotherSchemaURI(elementIndex, fromAnotherSchema);

        fSchemaGrammar.setElementDeclBlockSet(elementIndex, blockSet);
        fSchemaGrammar.setElementDeclFinalSet(elementIndex, finalSet);
        fSchemaGrammar.setElementDeclMiscFlags(elementIndex, elementMiscFlags);

        fSchemaGrammar.setElementDeclEquivClassElementFullName(elementIndex, equivClassFullName);

        return eltQName;



    int getLocalPartIndex(String fullName){
        int colonAt = fullName.indexOf(":"); 
        String localpart = fullName;
        if (  colonAt > -1 ) {
            localpart = fullName.substring(colonAt+1);
        }
        return fStringPool.addSymbol(localpart);
    }
    
    String getLocalPart(String fullName){
        int colonAt = fullName.indexOf(":"); 
        String localpart = fullName;
        if (  colonAt > -1 ) {
            localpart = fullName.substring(colonAt+1);
        }
        return localpart;
    }
    
    int getPrefixIndex(String fullName){
        int colonAt = fullName.indexOf(":"); 
        String prefix = "";
        if (  colonAt > -1 ) {
            prefix = fullName.substring(0,colonAt);
        }
        return fStringPool.addSymbol(prefix);
    }

    String getPrefix(String fullName){
        int colonAt = fullName.indexOf(":"); 
        String prefix = "";
        if (  colonAt > -1 ) {
            prefix = fullName.substring(0,colonAt);
        }
        return prefix;
    }
    
    private void checkEquivClassOK(Element elementDecl, Element equivClassElementDecl){
    }
    
    private Element getTopLevelComponentByName(String componentCategory, String name) throws Exception {
        Element child = XUtil.getFirstChildElement(fSchemaRootElement);

        if (child == null) {
            return null;
        }

        while (child != null ){
            if ( child.getNodeName().equals(componentCategory)) {
                if (child.getAttribute(SchemaSymbols.ATT_NAME).equals(name)) {
                    return child;
                }
            }
            child = XUtil.getNextSiblingElement(child);
        }

        return null;
    }

    private boolean isTopLevel(Element component) {
        /****
        if (component.getParentNode() == fSchemaRootElement ) {
            return true;
        }
        /****/
        if (component.getParentNode().getNodeName().endsWith(SchemaSymbols.ELT_SCHEMA) ) {
            return true;
        }
        return false;
    }
    
    DatatypeValidator getTypeValidatorFromNS(String newSchemaURI, String localpart) throws Exception {
        /*****
        Grammar grammar = fGrammarResolver.getGrammar(newSchemaURI);
        if (grammar != null && grammar instanceof SchemaGrammar) {
            SchemaGrammar sGrammar = (SchemaGrammar) grammar;
            DatatypeValidator dv = (DatatypeValidator) fSchemaGrammar.getDatatypeRegistry().getDatatypeValidator(localpart);
            return dv;
        }
        else {
            reportGenericSchemaError("could not resolve URI : " + newSchemaURI + " to a SchemaGrammar in getTypeValidatorFromNS");
        }
        return null;
        /*****/
        return getDatatypeValidator(newSchemaURI, localpart);
    }

    ComplexTypeInfo getTypeInfoFromNS(String newSchemaURI, String localpart) throws Exception {
        Grammar grammar = fGrammarResolver.getGrammar(newSchemaURI);
        if (grammar != null && grammar instanceof SchemaGrammar) {
            SchemaGrammar sGrammar = (SchemaGrammar) grammar;
            ComplexTypeInfo typeInfo = (ComplexTypeInfo) sGrammar.getComplexTypeRegistry().get(newSchemaURI+","+localpart);
            return typeInfo;
        }
        else {
            reportGenericSchemaError("could not resolve URI : " + newSchemaURI + " to a SchemaGrammar in getTypeInfoFromNS");
        }
        return null;
    }
    
    DatatypeValidator getElementDeclTypeValidatorFromNS(String newSchemaURI, String localpart) throws Exception {
        Grammar grammar = fGrammarResolver.getGrammar(newSchemaURI);
        if (grammar != null && grammar instanceof SchemaGrammar) {
            SchemaGrammar sGrammar = (SchemaGrammar) grammar;
            int eltIndex = sGrammar.getElementDeclIndex(fStringPool.addSymbol(newSchemaURI), 
                                                        fStringPool.addSymbol(localpart), 
                                                        TOP_LEVEL_SCOPE);

            DatatypeValidator dv = null;
            if (eltIndex>-1) {
                sGrammar.getElementDecl(eltIndex, fTempElementDecl);
                dv = fTempElementDecl.datatypeValidator;
            }
            else {
                reportGenericSchemaError("could not find global element : '" + localpart 
                                         + " in the SchemaGrammar "+newSchemaURI);
            }
            return dv;
        }
        else {
            reportGenericSchemaError("could not resolve URI : " + newSchemaURI
                                      + " to a SchemaGrammar in getELementDeclTypeValidatorFromNS");
        }
        return null;
    }

    ComplexTypeInfo getElementDeclTypeInfoFromNS(String newSchemaURI, String localpart) throws Exception {
        Grammar grammar = fGrammarResolver.getGrammar(newSchemaURI);
        if (grammar != null && grammar instanceof SchemaGrammar) {
            SchemaGrammar sGrammar = (SchemaGrammar) grammar;
            int eltIndex = sGrammar.getElementDeclIndex(fStringPool.addSymbol(newSchemaURI), 
                                                              fStringPool.addSymbol(localpart), 
                                                              TOP_LEVEL_SCOPE);
            ComplexTypeInfo typeInfo = null;
            if (eltIndex>-1) {
                 typeInfo = sGrammar.getElementComplexTypeInfo(eltIndex);
            }
            else {
                reportGenericSchemaError("could not find global element : '" + localpart 
                                         + " in the SchemaGrammar "+newSchemaURI);

            }
            return typeInfo;
        }
        else {
            reportGenericSchemaError("could not resolve URI : " + newSchemaURI 
                                     + " to a SchemaGrammar in getElementDeclTypeInfoFromNS");
        }
        return null;
    }

    /**
     * Traverse attributeGroup Declaration
     * 
     *   <attributeGroup
     *         id = ID
     *         ref = QName>
     *         Content: (annotation?)
     *      </>
     * 
     * @param elementDecl
     * @exception Exception
     */
    /*private int traverseAttributeGroupDecl( Element attributeGroupDecl ) throws Exception {
        int attributeGroupID         =  fStringPool.addSymbol(
                                                             attributeGroupDecl.getAttribute( SchemaSymbols.ATTVAL_ID ));

        int attributeGroupName      =  fStringPool.addSymbol(
                                                            attributeGroupDecl.getAttribute( SchemaSymbols.ATT_NAME ));

        return -1;
    }*/


    /**
     * Traverse Group Declaration.
     * 
     * <group 
     *         id = ID 
     *         maxOccurs = string 
     *         minOccurs = nonNegativeInteger 
     *         name = NCName 
     *         ref = QName>
     *   Content: (annotation? , (element | group | all | choice | sequence | any)*)
     * <group/>
     * 
     * @param elementDecl
     * @return 
     * @exception Exception
     */
    private int traverseGroupDecl( Element groupDecl ) throws Exception {

        String groupName = groupDecl.getAttribute(SchemaSymbols.ATT_NAME);
        String ref = groupDecl.getAttribute(SchemaSymbols.ATT_REF);

        if (!ref.equals("")) {
            if (XUtil.getFirstChildElement(groupDecl) != null)
                reportSchemaError(SchemaMessageProvider.NoContentForRef, null);
            String prefix = "";
            String localpart = ref;
            int colonptr = ref.indexOf(":");
            if ( colonptr > 0) {
                prefix = ref.substring(0,colonptr);
                localpart = ref.substring(colonptr+1);
            }
            int localpartIndex = fStringPool.addSymbol(localpart);
            
            String uriStr = resolvePrefixToURI(prefix);

            if (!uriStr.equals(fTargetNSURIString)) {
                return traverseGroupDeclFromAnotherSchema(localpart, uriStr);
            }

            int contentSpecIndex = -1;
            Element referredGroup = getTopLevelComponentByName(SchemaSymbols.ELT_GROUP,localpart);
            if (referredGroup == null) {
                reportGenericSchemaError("Group " + localpart + " not found in the Schema");
                throw new Exception("Group " + localpart + " not found in the Schema");
            }
            else {
                contentSpecIndex = traverseGroupDecl(referredGroup);
            }
            
            return contentSpecIndex;
        }

        boolean traverseElt = true; 
        if (fCurrentScope == TOP_LEVEL_SCOPE) {
            traverseElt = false;
        }

        Element child = XUtil.getFirstChildElement(groupDecl);
        while (child != null && child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION))
            child = XUtil.getNextSiblingElement(child);

        int contentSpecType = 0;
        int csnType = 0;
        int allChildren[] = null;
        int allChildCount = 0;

        csnType = XMLContentSpec.CONTENTSPECNODE_SEQ;
        contentSpecType = XMLElementDecl.TYPE_CHILDREN;
        
        int left = -2;
        int right = -2;
        boolean hadContent = false;
        boolean seeAll = false;
        boolean seeParticle = false;

        for (;
             child != null;
             child = XUtil.getNextSiblingElement(child)) {
            int index = -2;
            hadContent = true;

            boolean illegalChild = false;

            String childName = child.getNodeName();
            if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
                QName eltQName = traverseElementDecl(child);
                index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                       eltQName.localpart,
                                                       eltQName.uri, 
                                                       false);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                index = traverseGroupDecl(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_ALL)) {
                index = traverseAll(child);
                seeAll = true;
            } 
            else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
                index = traverseChoice(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                index = traverseSequence(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_ANY)) {
                index = traverseAny(child);
                seeParticle = true;
            } 
            else {
                illegalChild = true;
                reportSchemaError(SchemaMessageProvider.GroupContentRestricted,
                                  new Object [] { "group", childName });
            }
            
            if ( ! illegalChild ) {
                index = expandContentModel( index, child);
            }

            if (seeParticle && seeAll) {
                reportSchemaError( SchemaMessageProvider.GroupContentRestricted,
                                   new Object [] { "'all' needs to be 'the' only Child", childName});
            }

            if (left == -2) {
                left = index;
            } else if (right == -2) {
                right = index;
            } else {
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);
                right = index;
            }
        }
        if (hadContent && right != -2)
            left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);


        return left;
    }

    private int traverseGroupDeclFromAnotherSchema( String groupName , String uriStr ) throws Exception {
        
        SchemaGrammar aGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(uriStr);
        if (uriStr == null || aGrammar==null ||! (aGrammar instanceof SchemaGrammar) ) {
            reportGenericSchemaError("!!Schema not found in #traverseGroupDeclFromAnotherSchema, "+
                                     "schema uri: " + uriStr
                                     +", groupName: " + groupName);
            return -1;
        }


        Element groupDecl = (Element) aGrammar.topLevelGroupDecls.get((Object)groupName);
        if (groupDecl == null) {
            reportGenericSchemaError( "no group named \"" + groupName 
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }

        NamespacesScope saveNSMapping = fNamespacesScope;
        int saveTargetNSUri = fTargetNSURI;
        fTargetNSURI = fStringPool.addSymbol(aGrammar.getTargetNamespaceURI());
        fNamespacesScope = aGrammar.getNamespacesScope();

        boolean traverseElt = true; 
        if (fCurrentScope == TOP_LEVEL_SCOPE) {
            traverseElt = false;
        }

        Element child = XUtil.getFirstChildElement(groupDecl);
        while (child != null && child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION))
            child = XUtil.getNextSiblingElement(child);

        int contentSpecType = 0;
        int csnType = 0;
        int allChildren[] = null;
        int allChildCount = 0;

        csnType = XMLContentSpec.CONTENTSPECNODE_SEQ;
        contentSpecType = XMLElementDecl.TYPE_CHILDREN;
        
        int left = -2;
        int right = -2;
        boolean hadContent = false;

        for (;
             child != null;
             child = XUtil.getNextSiblingElement(child)) {
            int index = -2;
            hadContent = true;

            boolean seeParticle = false;
            String childName = child.getNodeName();
            int childNameIndex = fStringPool.addSymbol(childName);
            String formAttrVal = child.getAttribute(SchemaSymbols.ATT_FORM);
            if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
                QName eltQName = traverseElementDecl(child); 
                index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                       eltQName.localpart,
                                                       eltQName.uri, 
                                                       false);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                index = traverseGroupDecl(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_ALL)) {
                index = traverseAll(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
                index = traverseChoice(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                index = traverseSequence(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_ANY)) {
                index = traverseAny(child);
                seeParticle = true;
            } 
            else {
                reportSchemaError(SchemaMessageProvider.GroupContentRestricted,
                                  new Object [] { "group", childName });
            }

            if (seeParticle) {
                index = expandContentModel( index, child);
            }
            if (left == -2) {
                left = index;
            } else if (right == -2) {
                right = index;
            } else {
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);
                right = index;
            }
        }
        if (hadContent && right != -2)
            left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);

        fNamespacesScope = saveNSMapping;
        fTargetNSURI = saveTargetNSUri;
        return left;


    
    /**
    *
    * Traverse the Sequence declaration
    * 
    * <sequence 
    *   id = ID 
    *   maxOccurs = string 
    *   minOccurs = nonNegativeInteger>
    *   Content: (annotation? , (element | group | choice | sequence | any)*)
    * </sequence>
    * 
    **/
    int traverseSequence (Element sequenceDecl) throws Exception {
            
        Element child = XUtil.getFirstChildElement(sequenceDecl);
        while (child != null && child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION))
            child = XUtil.getNextSiblingElement(child);

        int contentSpecType = 0;
        int csnType = 0;

        csnType = XMLContentSpec.CONTENTSPECNODE_SEQ;
        contentSpecType = XMLElementDecl.TYPE_CHILDREN;

        int left = -2;
        int right = -2;
        boolean hadContent = false;

        for (;
             child != null;
             child = XUtil.getNextSiblingElement(child)) {
            int index = -2;
            hadContent = true;

            boolean seeParticle = false;
            String childName = child.getNodeName();
            if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
                QName eltQName = traverseElementDecl(child);
                index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                       eltQName.localpart,
                                                       eltQName.uri, 
                                                       false);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                index = traverseGroupDecl(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
                index = traverseChoice(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                index = traverseSequence(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_ANY)) {
                index = traverseAny(child);
                seeParticle = true;
            } 
            else {
                reportSchemaError(SchemaMessageProvider.GroupContentRestricted,
                                  new Object [] { "group", childName });
            }

            if (seeParticle) {
                index = expandContentModel( index, child);
            }
            if (left == -2) {
                left = index;
            } else if (right == -2) {
                right = index;
            } else {
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);
                right = index;
            }
        }

        if (hadContent && right != -2)
            left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);

        return left;
    }
    
    /**
    *
    * Traverse the Sequence declaration
    * 
    * <choice
    *   id = ID 
    *   maxOccurs = string 
    *   minOccurs = nonNegativeInteger>
    *   Content: (annotation? , (element | group | choice | sequence | any)*)
    * </choice>
    * 
    **/
    int traverseChoice (Element choiceDecl) throws Exception {
            
        Element child = XUtil.getFirstChildElement(choiceDecl);
        while (child != null && child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION))
            child = XUtil.getNextSiblingElement(child);

        int contentSpecType = 0;
        int csnType = 0;

        csnType = XMLContentSpec.CONTENTSPECNODE_CHOICE;
        contentSpecType = XMLElementDecl.TYPE_CHILDREN;

        int left = -2;
        int right = -2;
        boolean hadContent = false;

        for (;
             child != null;
             child = XUtil.getNextSiblingElement(child)) {
            int index = -2;
            hadContent = true;

            boolean seeParticle = false;
            String childName = child.getNodeName();
            if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
                QName eltQName = traverseElementDecl(child);
                index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                       eltQName.localpart,
                                                       eltQName.uri, 
                                                       false);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                index = traverseGroupDecl(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
                index = traverseChoice(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                index = traverseSequence(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_ANY)) {
                index = traverseAny(child);
                seeParticle = true;
            } 
            else {
                reportSchemaError(SchemaMessageProvider.GroupContentRestricted,
                                  new Object [] { "group", childName });
            }

            if (seeParticle) {
                index = expandContentModel( index, child);
            }
            if (left == -2) {
                left = index;
            } else if (right == -2) {
                right = index;
            } else {
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);
                right = index;
            }
        }

        if (hadContent && right != -2)
            left = fSchemaGrammar.addContentSpecNode(csnType, left, right, false);

        return left;
    }
    

   /**
    * 
    * Traverse the "All" declaration
    *
    * <all 
    *   id = ID 
    *   maxOccurs = string 
    *   minOccurs = nonNegativeInteger>
    *   Content: (annotation? , (element | group | choice | sequence | any)*)
    * </all>
    *   
    **/

    int traverseAll( Element allDecl) throws Exception {

        Element child = XUtil.getFirstChildElement(allDecl);

        while (child != null && child.getNodeName().equals(SchemaSymbols.ELT_ANNOTATION))
            child = XUtil.getNextSiblingElement(child);

        int allChildren[] = null;
        int allChildCount = 0;

        int left = -2;

        for (;
             child != null;
             child = XUtil.getNextSiblingElement(child)) {

            int index = -2;
            boolean seeParticle = false;

            String childName = child.getNodeName();

            if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
                QName eltQName = traverseElementDecl(child);
                index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                       eltQName.localpart,
                                                       eltQName.uri, 
                                                       false);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                index = traverseGroupDecl(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
                    index = traverseChoice(child);
                    seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                index = traverseSequence(child);
                seeParticle = true;

            } 
            else if (childName.equals(SchemaSymbols.ELT_ANY)) {
                index = traverseAny(child);
                seeParticle = true;
            } 
            else {
                reportSchemaError(SchemaMessageProvider.GroupContentRestricted,
                                  new Object [] { "group", childName });
            }

            if (seeParticle) {
                index = expandContentModel( index, child);
            }
            try {
                allChildren[allChildCount] = index;
            }
            catch (NullPointerException ne) {
                allChildren = new int[32];
                allChildren[allChildCount] = index;
            }
            catch (ArrayIndexOutOfBoundsException ae) {
                int[] newArray = new int[allChildren.length*2];
                System.arraycopy(allChildren, 0, newArray, 0, allChildren.length);
                allChildren[allChildCount] = index;
            }
            allChildCount++;
        }
        left = buildAllModel(allChildren,allChildCount);

        return left;
    }
    
    /** builds the all content model */
    private int buildAllModel(int children[], int count) throws Exception {

        if (count > 1) {

            XMLContentSpec choice = new XMLContentSpec();

            choice.type = XMLContentSpec.CONTENTSPECNODE_CHOICE;
            choice.value = -1;
            choice.otherValue = -1;

            int[] exactChildren = new int[count];
            System.arraycopy(children,0,exactChildren,0,count);
            sort(exactChildren, 0, count);
            int index = buildAllModel(exactChildren, 0, choice);

            return index;
        }

        if (count > 0) {
            return children[0];
        }

        return -1;
    }

    /** Builds the all model. */
    private int buildAllModel(int src[], int offset,
                              XMLContentSpec choice) throws Exception {

        if (src.length - offset == 2) {
            int seqIndex = createSeq(src);
            if (choice.value == -1) {
                choice.value = seqIndex;
            }
            else {
                if (choice.otherValue != -1) {
                    choice.value = fSchemaGrammar.addContentSpecNode(choice.type, choice.value, choice.otherValue, false);
                }
                choice.otherValue = seqIndex;
            }
            swap(src, offset, offset + 1);
            seqIndex = createSeq(src);
            if (choice.value == -1) {
                choice.value = seqIndex;
            }
            else {
                if (choice.otherValue != -1) {
                    choice.value = fSchemaGrammar.addContentSpecNode(choice.type, choice.value, choice.otherValue, false);
                }
                choice.otherValue = seqIndex;
            }
            return fSchemaGrammar.addContentSpecNode(choice.type, choice.value, choice.otherValue, false);
        }

        for (int i = offset; i < src.length - 1; i++) {
            choice.value = buildAllModel(src, offset + 1, choice);
            choice.otherValue = -1;
            sort(src, offset, src.length - offset);
            shift(src, offset, i + 1);
        }

        int choiceIndex = buildAllModel(src, offset + 1, choice);
        sort(src, offset, src.length - offset);

        return choiceIndex;


    /** Creates a sequence. */
    private int createSeq(int src[]) throws Exception {

        int left = src[0];
        int right = src[1];

        for (int i = 2; i < src.length; i++) {
            left = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                       left, right, false);
            right = src[i];
        }

        return fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                   left, right, false);


    /** Shifts a value into position. */
    private void shift(int src[], int pos, int offset) {

        int temp = src[offset];
        for (int i = offset; i > pos; i--) {
            src[i] = src[i - 1];
        }
        src[pos] = temp;


    /** Simple sort. */
    private void sort(int src[], final int offset, final int length) {

        for (int i = offset; i < offset + length - 1; i++) {
            int lowest = i;
            for (int j = i + 1; j < offset + length; j++) {
                if (src[j] < src[lowest]) {
                    lowest = j;
                }
            }
            if (lowest != i) {
                int temp = src[i];
                src[i] = src[lowest];
                src[lowest] = temp;
            }
        }


    /** Swaps two values. */
    private void swap(int src[], int i, int j) {

        int temp = src[i];
        src[i] = src[j];
        src[j] = temp;


    /**
     * Traverse Wildcard declaration
     * 
     * <any 
     *   id = ID 
     *   maxOccurs = string 
     *   minOccurs = nonNegativeInteger 
     *   namespace = ##any | ##other | ##local | list of {uri, ##targetNamespace} 
     *   processContents = lax | skip | strict>
     *   Content: (annotation?)
     * </any>
     * @param elementDecl
     * @return 
     * @exception Exception
     */
    private int traverseWildcardDecl( Element wildcardDecl ) throws Exception {
        int wildcardID         =  fStringPool.addSymbol(
                                                       wildcardDecl.getAttribute( SchemaSymbols.ATTVAL_ID ));

        int wildcardMaxOccurs  =  fStringPool.addSymbol(
                                                       wildcardDecl.getAttribute( SchemaSymbols.ATT_MAXOCCURS ));

        int wildcardMinOccurs  =  fStringPool.addSymbol(
                                                       wildcardDecl.getAttribute( SchemaSymbols.ATT_MINOCCURS ));

        int wildcardNamespace  =  fStringPool.addSymbol(
                                                       wildcardDecl.getAttribute( SchemaSymbols.ATT_NAMESPACE ));

        int wildcardProcessContents =  fStringPool.addSymbol(
                                                            wildcardDecl.getAttribute( SchemaSymbols.ATT_PROCESSCONTENTS ));


        int wildcardContent =  fStringPool.addSymbol(
                                                    wildcardDecl.getAttribute( SchemaSymbols.ATT_CONTENT ));


        return -1;
    }
    
    


    private int parseInt (String intString) throws Exception
    {
            if ( intString.equals("*") ) {
                    return SchemaSymbols.INFINITY;
            } else {
                    return Integer.parseInt (intString);
            }
    }

    private int parseSimpleDerivedBy (String derivedByString) throws Exception
    {
            if ( derivedByString.equals (SchemaSymbols.ATTVAL_LIST) ) {
                    return SchemaSymbols.LIST;
            } 
            else if ( derivedByString.equals (SchemaSymbols.ATTVAL_RESTRICTION) ) {
                    return SchemaSymbols.RESTRICTION;
            }  
            else {
                    reportGenericSchemaError ("SimpleType: Invalid value for 'derivedBy'");
                    return -1;
            }
    }

    private int parseComplexDerivedBy (String derivedByString)  throws Exception
    {
            if ( derivedByString.equals (SchemaSymbols.ATTVAL_EXTENSION) ) {
                    return SchemaSymbols.EXTENSION;
            } 
            else if ( derivedByString.equals (SchemaSymbols.ATTVAL_RESTRICTION) ) {
                    return SchemaSymbols.RESTRICTION;
            } 
            else {
                    reportGenericSchemaError ( "ComplexType: Invalid value for 'derivedBy'" );
                    return -1;
            }
    }

    private int parseSimpleFinal (String finalString) throws Exception
    {
            if ( finalString.equals (SchemaSymbols.ATTVAL_POUNDALL) ) {
                    return SchemaSymbols.ENUMERATION+SchemaSymbols.RESTRICTION+SchemaSymbols.LIST+SchemaSymbols.REPRODUCTION;
            } else {
                    int enumerate = 0;
                    int restrict = 0;
                    int list = 0;
                    int reproduce = 0;

                    StringTokenizer t = new StringTokenizer (finalString, " ");
                    while (t.hasMoreTokens()) {
                            String token = t.nextToken ();

                            if ( token.equals (SchemaSymbols.ATTVAL_RESTRICTION) ) {
                                    if ( restrict == 0 ) {
                                            restrict = SchemaSymbols.RESTRICTION;
                                    } else {
                                            reportGenericSchemaError ("restriction in set twice");
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_LIST) ) {
                                    if ( list == 0 ) {
                                            list = SchemaSymbols.LIST;
                                    } else {
                                            reportGenericSchemaError ("list in set twice");
                                    }
                            }
                            else {
                                reportGenericSchemaError (  "Invalid value (" + 
                                                            finalString +
                                                            ")" );
                            }
                    }

                    return enumerate+restrict+list+reproduce;
            }
    }

    private int parseComplexContent (String contentString)  throws Exception
    {
            if ( contentString.equals (SchemaSymbols.ATTVAL_EMPTY) ) {
                    return XMLElementDecl.TYPE_EMPTY;
            } else if ( contentString.equals (SchemaSymbols.ATTVAL_ELEMENTONLY) ) {
                    return XMLElementDecl.TYPE_CHILDREN;
            } else if ( contentString.equals (SchemaSymbols.ATTVAL_TEXTONLY) ) {
                    return XMLElementDecl.TYPE_SIMPLE;
            } else if ( contentString.equals (SchemaSymbols.ATTVAL_MIXED) ) {
                    return XMLElementDecl.TYPE_MIXED;
            } else {
                    reportGenericSchemaError ( "Invalid value for content" );
                    return -1;
            }
    }

    private int parseDerivationSet (String finalString)  throws Exception
    {
            if ( finalString.equals ("#all") ) {
                    return SchemaSymbols.EXTENSION+SchemaSymbols.RESTRICTION+SchemaSymbols.REPRODUCTION;
            } else {
                    int extend = 0;
                    int restrict = 0;
                    int reproduce = 0;

                    StringTokenizer t = new StringTokenizer (finalString, " ");
                    while (t.hasMoreTokens()) {
                            String token = t.nextToken ();

                            if ( token.equals (SchemaSymbols.ATTVAL_EXTENSION) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EXTENSION;
                                    } else {
                                            reportGenericSchemaError ( "extension already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_RESTRICTION) ) {
                                    if ( restrict == 0 ) {
                                            restrict = SchemaSymbols.RESTRICTION;
                                    } else {
                                            reportGenericSchemaError ( "restriction already in set" );
                                    }
                            } else {
                                    reportGenericSchemaError ( "Invalid final value (" + finalString + ")" );
                            }
                    }

                    return extend+restrict+reproduce;
            }
    }

    private int parseBlockSet (String finalString)  throws Exception
    {
            if ( finalString.equals ("#all") ) {
                    return SchemaSymbols.EQUIVCLASS+SchemaSymbols.EXTENSION+SchemaSymbols.LIST+SchemaSymbols.RESTRICTION+SchemaSymbols.REPRODUCTION;
            } else {
                    int extend = 0;
                    int restrict = 0;
                    int reproduce = 0;

                    StringTokenizer t = new StringTokenizer (finalString, " ");
                    while (t.hasMoreTokens()) {
                            String token = t.nextToken ();

                            if ( token.equals (SchemaSymbols.ATTVAL_EQUIVCLASS) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EQUIVCLASS;
                                    } else {
                                            reportGenericSchemaError ( "'equivClass' already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_EXTENSION) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EXTENSION;
                                    } else {
                                            reportGenericSchemaError ( "extension already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_LIST) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.LIST;
                                    } else {
                                            reportGenericSchemaError ( "'list' already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_RESTRICTION) ) {
                                    if ( restrict == 0 ) {
                                            restrict = SchemaSymbols.RESTRICTION;
                                    } else {
                                            reportGenericSchemaError ( "restriction already in set" );
                                    }
                            } else {
                                    reportGenericSchemaError ( "Invalid final value (" + finalString + ")" );
                            }
                    }

                    return extend+restrict+reproduce;
            }
    }

    private int parseFinalSet (String finalString)  throws Exception
    {
            if ( finalString.equals ("#all") ) {
                    return SchemaSymbols.EQUIVCLASS+SchemaSymbols.EXTENSION+SchemaSymbols.LIST+SchemaSymbols.RESTRICTION+SchemaSymbols.REPRODUCTION;
            } else {
                    int extend = 0;
                    int restrict = 0;
                    int reproduce = 0;

                    StringTokenizer t = new StringTokenizer (finalString, " ");
                    while (t.hasMoreTokens()) {
                            String token = t.nextToken ();

                            if ( token.equals (SchemaSymbols.ATTVAL_EQUIVCLASS) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EQUIVCLASS;
                                    } else {
                                            reportGenericSchemaError ( "'equivClass' already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_EXTENSION) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EXTENSION;
                                    } else {
                                            reportGenericSchemaError ( "extension already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_LIST) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.LIST;
                                    } else {
                                            reportGenericSchemaError ( "'list' already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_RESTRICTION) ) {
                                    if ( restrict == 0 ) {
                                            restrict = SchemaSymbols.RESTRICTION;
                                    } else {
                                            reportGenericSchemaError ( "restriction already in set" );
                                    }
                            } else {
                                    reportGenericSchemaError ( "Invalid final value (" + finalString + ")" );
                            }
                    }

                    return extend+restrict+reproduce;
            }
    }

    private void reportGenericSchemaError (String error) throws Exception {
        if (fErrorReporter == null) {
            System.err.println("__TraverseSchemaError__ : " + error);       
        }
        else {
            reportSchemaError (SchemaMessageProvider.GenericError, new Object[] { error });
        }        
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

    public static void main(String args[] ) {

        if( args.length != 1 ) {
            System.out.println( "Error: Usage java TraverseSchema yourFile.xsd" );
            System.exit(0);
        }

        DOMParser parser = new DOMParser() {
            public void ignorableWhitespace(char ch[], int start, int length) {}
            public void ignorableWhitespace(int dataIdx) {}
        };
        parser.setEntityResolver( new Resolver() );
        parser.setErrorHandler(  new ErrorHandler() );

        try {
        }catch(  org.xml.sax.SAXNotRecognizedException e ) {
            e.printStackTrace();
        }catch( org.xml.sax.SAXNotSupportedException e ) {
            e.printStackTrace();
        }

        try {
        parser.parse( args[0]);
        }catch( IOException e ) {
            e.printStackTrace();
        }catch( SAXException e ) {
            e.printStackTrace();
        }


        OutputFormat    format  = new OutputFormat( document );
        java.io.StringWriter outWriter = new java.io.StringWriter();
        XMLSerializer    serial = new XMLSerializer( outWriter,format);

        TraverseSchema tst = null;
        try {

            tst = new TraverseSchema( root, new StringPool(), new SchemaGrammar(), (GrammarResolver) new GrammarResolverImpl() );
            }
            catch (Exception e) {
                e.printStackTrace(System.err);
            }
            
            parser.getDocument();
    }

    static class Resolver implements EntityResolver {
        private static final String SYSTEM[] = {
        };
        private static final String PATH[] = {
            "structures.dtd",
            "datatypes.dtd",
            "versionInfo.ent",
        };

        public InputSource resolveEntity(String publicId, String systemId)
        throws IOException {

            for (int i = 0; i < SYSTEM.length; i++) {
                if (systemId.equals(SYSTEM[i])) {
                    InputSource source = new InputSource(getClass().getResourceAsStream(PATH[i]));
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    return source;
                }
            }

            return null;



    static class ErrorHandler implements org.xml.sax.ErrorHandler {

        /** Warning. */
        public void warning(SAXParseException ex) {
            System.err.println("[Warning] "+
                               getLocationString(ex)+": "+
                               ex.getMessage());
        }

        /** Error. */
        public void error(SAXParseException ex) {
            System.err.println("[Error] "+
                               getLocationString(ex)+": "+
                               ex.getMessage());
        }

        /** Fatal error. */
        public void fatalError(SAXParseException ex) throws SAXException {
            System.err.println("[Fatal Error] "+
                               getLocationString(ex)+": "+
                               ex.getMessage());
            throw ex;
        }


        /** Returns a string of the location. */
        private String getLocationString(SAXParseException ex) {
            StringBuffer str = new StringBuffer();

            String systemId_ = ex.getSystemId();
            if (systemId_ != null) {
                int index = systemId_.lastIndexOf('/');
                if (index != -1)
                    systemId_ = systemId_.substring(index + 1);
                str.append(systemId_);
            }
            str.append(':');
            str.append(ex.getLineNumber());
            str.append(':');
            str.append(ex.getColumnNumber());

            return str.toString();

    }


}





