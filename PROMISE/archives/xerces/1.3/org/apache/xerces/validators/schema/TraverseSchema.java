package org.apache.xerces.validators.schema;

import  org.apache.xerces.framework.XMLErrorReporter;
import  org.apache.xerces.validators.common.Grammar;
import  org.apache.xerces.validators.common.GrammarResolver;
import  org.apache.xerces.validators.common.GrammarResolverImpl;
import  org.apache.xerces.validators.common.XMLElementDecl;
import  org.apache.xerces.validators.common.XMLAttributeDecl;
import  org.apache.xerces.validators.schema.SchemaSymbols;
import  org.apache.xerces.validators.schema.XUtil;
import  org.apache.xerces.validators.schema.identity.Field;
import  org.apache.xerces.validators.schema.identity.IdentityConstraint;
import  org.apache.xerces.validators.schema.identity.Key;
import  org.apache.xerces.validators.schema.identity.KeyRef;
import  org.apache.xerces.validators.schema.identity.Selector;
import  org.apache.xerces.validators.schema.identity.Unique;
import  org.apache.xerces.validators.schema.identity.XPathException;
import  org.apache.xerces.validators.datatype.DatatypeValidator;
import  org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl;
import  org.apache.xerces.validators.datatype.InvalidDatatypeValueException;
import  org.apache.xerces.utils.StringPool;
import  org.w3c.dom.Element;

import java.io.IOException;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

import  org.w3c.dom.*;

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
import  org.w3c.dom.Document;
import  org.apache.xml.serialize.OutputFormat;
import  org.apache.xml.serialize.XMLSerializer;
import  org.apache.xerces.validators.schema.SchemaSymbols;

/**
 * Instances of this class get delegated to Traverse the Schema and
 * to populate the Grammar internal representation by
 * instances of Grammar objects.
 * Traverse a Schema Grammar:
 * 
 * @author Eric Ye, IBM
 * @author Jeffrey Rodriguez, IBM
 * @author Andy Clark, IBM
 *  
 * @see org.apache.xerces.validators.common.Grammar
 *
 * @version $Id: TraverseSchema.java 316848 2001-01-31 19:49:12Z lmartin $
 */
public class TraverseSchema implements 
                            NamespacesScope.NamespacesHandler{

    
    private static final int TOP_LEVEL_SCOPE = -1;

    /** Identity constraint keywords. */
    private static final String[] IDENTITY_CONSTRAINTS = {
        SchemaSymbols.ELT_UNIQUE, 
        SchemaSymbols.ELT_KEY, SchemaSymbols.ELT_KEYREF
    };

    private static boolean DEBUGGING = false;

    /** Compile to true to debug identity constraints. */
    private static boolean DEBUG_IDENTITY_CONSTRAINTS = false;
    
	private static boolean DEBUG_UNION = false;
	private static boolean CR_IMPL = true;


    private XMLErrorReporter    fErrorReporter = null;
    private StringPool          fStringPool    = null;

    private GrammarResolver fGrammarResolver = null;
    private SchemaGrammar fSchemaGrammar = null;

    private Element fSchemaRootElement;

    private DatatypeValidatorFactoryImpl fDatatypeRegistry = null;

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

    private EntityResolver  fEntityResolver = null;
    
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

    private class ComplexTypeRecoverableError extends Exception {
        ComplexTypeRecoverableError() {super();}
        ComplexTypeRecoverableError(String s) {super(s);}
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
                           String schemaURL,
                   EntityResolver entityResolver
                           ) throws Exception {
        fErrorReporter = errorReporter;
        fCurrentSchemaURL = schemaURL;
    fEntityResolver = entityResolver;
        doTraverseSchema(root, stringPool, schemaGrammar, grammarResolver);
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
        fDatatypeRegistry = (DatatypeValidatorFactoryImpl) fGrammarResolver.getDatatypeRegistry();

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
            if (fSchemaGrammar.getComplexTypeRegistry() == null ) {
                fSchemaGrammar.setComplexTypeRegistry(fComplexTypeRegistry);
            }
            else {
                fComplexTypeRegistry = fSchemaGrammar.getComplexTypeRegistry();
            }

            if (fSchemaGrammar.getAttirubteDeclRegistry() == null ) {
                fSchemaGrammar.setAttributeDeclRegistry(fAttributeDeclRegistry);
            }
            else {
                fAttributeDeclRegistry = fSchemaGrammar.getAttirubteDeclRegistry();
            }

            if (fSchemaGrammar.getNamespacesScope() == null ) {
                fSchemaGrammar.setNamespacesScope(fNamespacesScope);
            }
            else {
                fNamespacesScope = fSchemaGrammar.getNamespacesScope();
            }

            fSchemaGrammar.setDatatypeRegistry(fDatatypeRegistry);
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

            String name = child.getLocalName();
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
                traverseAttributeDecl( child, null, false );
            } else if (name.equals(SchemaSymbols.ELT_GROUP)) {
                traverseGroupDecl(child);
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

            String name = child.getLocalName();
            String compName = child.getAttribute(SchemaSymbols.ATT_NAME);
            if (name.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                fSchemaGrammar.topLevelAttrGrpDecls.put(compName, child);
            } else if (name.equals( SchemaSymbols.ELT_ATTRIBUTE ) ) {
                fSchemaGrammar.topLevelAttrDecls.put(compName, child);
            } else if ( name.equals(SchemaSymbols.ELT_GROUP) ) {
                fSchemaGrammar.topLevelGroupDecls.put(compName, child);
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
        
        InputSource source = null;
        if (fEntityResolver != null) {
            source = fEntityResolver.resolveEntity("", location);
        }
        if (source == null) {
            location = expandSystemId(location, fCurrentSchemaURL);
            source = new InputSource(location);
        }
        else {
            if (source.getPublicId () != null)
                location = source.getPublicId ();

            location += (',' + source.getSystemId ());
        }

        if (fIncludeLocations.contains((Object)location)) {
            return;
        }
        fIncludeLocations.addElement((Object)location);

        DOMParser parser = new IgnoreWhitespaceParser();
        parser.setEntityResolver( new Resolver() );
        parser.setErrorHandler(  new ErrorHandler() );

        try {
        }catch(  org.xml.sax.SAXNotRecognizedException e ) {
            e.printStackTrace();
        }catch( org.xml.sax.SAXNotSupportedException e ) {
            e.printStackTrace();
        }

        try {
            parser.parse( source );
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

            String name = child.getLocalName();

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
                traverseAttributeDecl( child, null , false);
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
        InputSource source = null;
        if (fEntityResolver != null) {
            source = fEntityResolver.resolveEntity("", location);
        }
        if (source == null) {
            location = expandSystemId(location, fCurrentSchemaURL);
            source = new InputSource(location);
        }
         else {
             if (source.getPublicId () != null)
                 location = source.getPublicId ();

             location += (',' + source.getSystemId ());
         }

         if (fImportLocations.contains((Object)location)) {
             return;
         }
         fImportLocations.addElement((Object)location);

         String namespaceString = importDecl.getAttribute(SchemaSymbols.ATT_NAMESPACE);
         SchemaGrammar importedGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(namespaceString);

         if (importedGrammar == null) {
             importedGrammar = new SchemaGrammar();
         }

         DOMParser parser = new IgnoreWhitespaceParser();
         parser.setEntityResolver( new Resolver() );
         parser.setErrorHandler(  new ErrorHandler() );

         try {
         }catch(  org.xml.sax.SAXNotRecognizedException e ) {
             e.printStackTrace();
         }catch( org.xml.sax.SAXNotSupportedException e ) {
             e.printStackTrace();
         }

         try {
             parser.parse( source );
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
                 new TraverseSchema(root, fStringPool, importedGrammar, fGrammarResolver, fErrorReporter, location, fEntityResolver);
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

   private Element checkContent( Element elm, Element content, boolean isEmpty ) throws Exception {
       if ( content == null) {
           if (!isEmpty) {
               reportSchemaError(SchemaMessageProvider.ContentError,
                                 new Object [] { elm.getAttribute( SchemaSymbols.ATT_NAME )});
           }
           return null;
       }
       if (content.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION)) {
		   traverseAnnotationDecl( content );   
		   content = XUtil.getNextSiblingElement(content);
			   if (!isEmpty) {
				   reportSchemaError(SchemaMessageProvider.ContentError,
									 new Object [] { elm.getAttribute( SchemaSymbols.ATT_NAME )});
			   }
			   return null;
		   }
		   if (content.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION)) {
			   reportSchemaError(SchemaMessageProvider.AnnotationError,
								 new Object [] { elm.getAttribute( SchemaSymbols.ATT_NAME )});
			   return null;
		   }
	   }
       return content;  
   }

   
   private DatatypeValidator findDTValidator (Element elm, String baseTypeStr )  throws Exception{
            int baseType      = fStringPool.addSymbol( baseTypeStr );
            String prefix = "";
            DatatypeValidator baseValidator = null;
            String localpart = baseTypeStr;
            int colonptr = baseTypeStr.indexOf(":");
            if ( colonptr > 0) {
                prefix = baseTypeStr.substring(0,colonptr);
                localpart = baseTypeStr.substring(colonptr+1);
            }
            String uri = resolvePrefixToURI(prefix);
            baseValidator = getDatatypeValidator(uri, localpart);
            if (baseValidator == null) {
                Element baseTypeNode = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
                if (baseTypeNode != null) {
                    traverseSimpleTypeDecl( baseTypeNode ); 
                    
                    baseValidator = getDatatypeValidator(uri, localpart);
                }
            }
            if ( baseValidator == null ) {
                reportSchemaError(SchemaMessageProvider.UnknownBaseDatatype,
                                  new Object [] { elm.getAttribute( SchemaSymbols.ATT_BASE ),
                                      elm.getAttribute(SchemaSymbols.ATT_NAME)});
            }
       return baseValidator;
    }

   /**
     * Traverse SimpleType declaration:
     * <simpleType
     *         id = ID 
     *         name = NCName>
     *         Content: (annotation? , ((list | restriction | union)))
     *       </simpleType>
     * traverse <list>|<restriction>|<union>
     * 
     * @param simpleTypeDecl
     * @return 
     */
    private int traverseSimpleTypeDecl( Element simpleTypeDecl ) throws Exception {
        
        if (DEBUG_UNION) {
            System.out.println("----------->CR traverseSimpleType()");
        }

        String nameProperty          =  simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME );
        boolean list = false;
        boolean union = false;
        boolean restriction = false;
        int     newSimpleTypeName    = -1;
            newSimpleTypeName = fStringPool.addSymbol(
                "#S#"+fSimpleTypeAnonCount++ );   
        }
        else 
            newSimpleTypeName       = fStringPool.addSymbol( nameProperty );

        Element content = XUtil.getFirstChildElement(simpleTypeDecl);
        content = checkContent(simpleTypeDecl, content, false);
        if (content == null) {
            return (-1);
        }
        String varietyProperty = content.getLocalName();
        String baseTypeQNameProperty = null;
        Vector dTValidators = null;
        int size = 0;  
        StringTokenizer unionMembers = null;
        
        if (DEBUG_UNION) {
            System.out.println("[varietyProperty]:"+   varietyProperty );
        }

           baseTypeQNameProperty =  content.getAttribute( SchemaSymbols.ATT_ITEMTYPE );
           list = true;
        }
            baseTypeQNameProperty =  content.getAttribute( SchemaSymbols.ATT_BASE );
            restriction= true;
        }
            union = true;
            baseTypeQNameProperty = content.getAttribute( SchemaSymbols.ATT_MEMBERTYPES);
            if (baseTypeQNameProperty != "" ) {
                unionMembers = new StringTokenizer( baseTypeQNameProperty );
                size = unionMembers.countTokens();
            }
            else {
            }
            dTValidators = new Vector (size, 2); 
        }
        else {
             reportSchemaError(SchemaMessageProvider.FeatureUnsupported,
                       new Object [] { varietyProperty });
                       return -1;
        }

        int typeNameIndex;
        DatatypeValidator baseValidator = null;
        
        if (DEBUG_UNION) {
            System.out.println("[nameProperty]= " +nameProperty);
            System.out.println("[base]= " +baseTypeQNameProperty+";");
            System.out.println("[size]= " +size);
            if (unionMembers!=null) {
                System.out.println("[unionMembers]= " +unionMembers.toString());
            }
        }
            content = XUtil.getFirstChildElement(content);
            content = checkContent(simpleTypeDecl, content, false);
            if (content == null) {
                return (-1);
            }
              typeNameIndex = traverseSimpleTypeDecl(content); 
              if (DEBUG_UNION) { 
				  System.out.println("[After traverseSimpleTypeDecl]: " +fStringPool.toString(typeNameIndex));
				  System.out.println("[traverseSimpleTypeDecl]: " +  nameProperty);  
				}
              if (typeNameIndex!=-1) {
                  baseValidator=fDatatypeRegistry.getDatatypeValidator(fStringPool.toString(typeNameIndex));
                  if (baseValidator !=null && union) {  
                      dTValidators.addElement((DatatypeValidator)baseValidator);
                  }
              }
              if ( typeNameIndex == -1 || baseValidator == null) {
                  reportSchemaError(SchemaMessageProvider.UnknownBaseDatatype,
                                        new Object [] { content.getAttribute( SchemaSymbols.ATT_BASE ),
                                            content.getAttribute(SchemaSymbols.ATT_NAME) });
                      return -1;
              }
            }
            numOfTypes = 1;
            if (union) {
                numOfTypes= size;
            }
                if (union) {
                    baseTypeQNameProperty = unionMembers.nextToken();
                }
                baseValidator = findDTValidator ( simpleTypeDecl, baseTypeQNameProperty);
                if ( baseValidator == null) {
                    return (-1);
                }
                if (union) {
                }
                if ( list && (baseValidator instanceof UnionDatatypeValidator)) {
                    reportSchemaError(SchemaMessageProvider.UnknownBaseDatatype,
                                      new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_BASE ),
                                          simpleTypeDecl.getAttribute(SchemaSymbols.ATT_NAME)});
                    return -1;
                }
            }
        
            content = XUtil.getNextSiblingElement( content );
        }
            content = XUtil.getFirstChildElement(content);
        }
        
        if (union) {
            int index=size; 
            while (content!=null) {
                if (DEBUG_UNION) {
                    System.out.println("[start Union types traversal] + " + content.getNodeName());
                    System.out.println(index+"-Getting all other simpletypes");
                    System.out.println("content: " + content.getNodeName());
                }
                typeNameIndex = traverseSimpleTypeDecl(content);   
                if (typeNameIndex!=-1) {
                    baseValidator=fDatatypeRegistry.getDatatypeValidator(fStringPool.toString(typeNameIndex));
                    if (baseValidator != null) {
                        if (DEBUG_UNION) {
                            System.out.println("validator to add: " + baseValidator.toString());
                        }
                        dTValidators.addElement((DatatypeValidator)baseValidator);
                    }
                }
				
                if ( baseValidator == null || typeNameIndex == -1) {
                     reportSchemaError(SchemaMessageProvider.UnknownBaseDatatype,
                                      new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_BASE ),
                                          simpleTypeDecl.getAttribute(SchemaSymbols.ATT_NAME)});
                    return (-1);
                }
				content   = XUtil.getNextSiblingElement( content );
            }
        
        
        Hashtable facetData =null; 
        int numFacets=0;

        if (restriction && content != null) {
        
            int numEnumerationLiterals = 0;
            facetData        = new Hashtable();
            Vector enumData  = new Vector();
            content = checkContent(simpleTypeDecl, content , true);
            while (content != null) { 
                if (content.getNodeType() == Node.ELEMENT_NODE) {
                        numFacets++;
                        if (content.getLocalName().equals(SchemaSymbols.ELT_ENUMERATION)) {
                            numEnumerationLiterals++;
                            String enumVal = content.getAttribute(SchemaSymbols.ATT_VALUE);
                            enumData.addElement(enumVal);
                            checkContent(simpleTypeDecl, XUtil.getFirstChildElement( content ), true);
                        }
                        else if (content.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION)) {
                                  reportSchemaError(SchemaMessageProvider.ContentError,
                                                    new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
                        }
                        else {
                             facetData.put(content.getLocalName(),content.getAttribute( SchemaSymbols.ATT_VALUE ));
                             checkContent(simpleTypeDecl, XUtil.getFirstChildElement( content ), true);
                        }
                }
                    content = XUtil.getNextSiblingElement(content);
            }
            if (numEnumerationLiterals > 0) {
                  facetData.put(SchemaSymbols.ELT_ENUMERATION, enumData);
            }
        }

        
            if (baseTypeQNameProperty != "") {
                content = checkContent(simpleTypeDecl, content, true);
            }
            else {
                reportSchemaError(SchemaMessageProvider.ListUnionRestrictionError,
                        new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
            }
        }
             if (baseTypeQNameProperty != "") {
                content = checkContent(simpleTypeDecl, content, true);
            }
            else {
                reportSchemaError(SchemaMessageProvider.ListUnionRestrictionError,
                        new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
            }
        }

        String nameOfType = fStringPool.toString( newSimpleTypeName);
        if (fTargetNSURIString.length () != 0) {
            nameOfType = fTargetNSURIString+","+nameOfType;
        }
        try { 
           DatatypeValidator newValidator =
                 fDatatypeRegistry.getDatatypeValidator( nameOfType );

               if (list) {
                    fDatatypeRegistry.createDatatypeValidator( nameOfType, baseValidator, 
                                                               facetData,true);
               }
               else if (restriction) {
                   fDatatypeRegistry.createDatatypeValidator( nameOfType, baseValidator,
                                                               facetData,false);
               }
                   fDatatypeRegistry.createDatatypeValidator( nameOfType, dTValidators);
               }
                           
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
     * Traverse ComplexType Declaration - CR Implementation.
     *  
     *       <complexType 
     *         abstract = boolean 
     *         block = #all or (possibly empty) subset of {extension, restriction} 
     *         final = #all or (possibly empty) subset of {extension, restriction} 
     *         id = ID 
     *         mixed = boolean : false
     *         name = NCName>
     *         Content: (annotation? , (simpleContent | complexContent | 
     *                    ( (group | all | choice | sequence)? , 
     *                    ( (attribute | attributeGroup)* , anyAttribute?))))  
     *       </complexType>
     * @param complexTypeDecl
     * @return 
     */
    
    private int traverseComplexTypeDecl( Element complexTypeDecl ) throws Exception { 
        
        String isAbstract = complexTypeDecl.getAttribute( SchemaSymbols.ATT_ABSTRACT );
        String blockSet = complexTypeDecl.getAttribute( SchemaSymbols.ATT_BLOCK );
        String finalSet = complexTypeDecl.getAttribute( SchemaSymbols.ATT_FINAL );
        String typeId = complexTypeDecl.getAttribute( SchemaSymbols.ATTVAL_ID );
        String typeName = complexTypeDecl.getAttribute(SchemaSymbols.ATT_NAME); 
        String mixed = complexTypeDecl.getAttribute(SchemaSymbols.ATT_MIXED);
        boolean isNamedType = false;

        if ( DEBUGGING )
            System.out.println("traversing complex Type : " + typeName); 

            typeName = "#"+fAnonTypeCount++;
        }
        else {
            fCurrentTypeNameStack.push(typeName);
            isNamedType = true;
        }

        int typeNameIndex = fStringPool.addSymbol(typeName);

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
        ComplexTypeInfo typeInfo = new ComplexTypeInfo();

        try {

          child = checkContent(complexTypeDecl,XUtil.getFirstChildElement(complexTypeDecl),
                               true);

          if (child==null) {
              processComplexContent(typeNameIndex, child, typeInfo, null, false);
          }
          else {
              String childName = child.getLocalName();
              int index = -2;

              if (childName.equals(SchemaSymbols.ELT_SIMPLECONTENT)) {
                  traverseSimpleContentDecl(typeNameIndex, child, typeInfo);
                  if (XUtil.getNextSiblingElement(child) != null) 
                     throw new ComplexTypeRecoverableError(
                      "Invalid child following the simpleContent child in the complexType");
              }
              else if (childName.equals(SchemaSymbols.ELT_COMPLEXCONTENT)) {
                  traverseComplexContentDecl(typeNameIndex, child, typeInfo,   
                        mixed.equals(SchemaSymbols.ATTVAL_TRUE) ? true:false);
                  if (XUtil.getNextSiblingElement(child) != null) 
                     throw new ComplexTypeRecoverableError(
                      "Invalid child following the complexContent child in the complexType");
              }
              else {
                  processComplexContent(typeNameIndex, child, typeInfo, null, 
                        mixed.equals(SchemaSymbols.ATTVAL_TRUE) ? true:false);
       
              }
          }
        }
        catch (ComplexTypeRecoverableError e) {
           String message = e.getMessage();
           handleComplexTypeError(message,typeNameIndex,typeInfo);
        }
                
            
        typeInfo.scopeDefined = scopeDefined; 
        typeInfo.blockSet = parseBlockSet(blockSet); 
        typeInfo.finalSet = parseFinalSet(finalSet); 
        typeInfo.isAbstract = isAbstract.equals(SchemaSymbols.ATTVAL_TRUE) ? true:false ;
        if (!typeName.startsWith("#")) {
            typeName = fTargetNSURIString + "," + typeName;
        }
        typeInfo.typeName = new String(typeName);

        if ( DEBUGGING )
            System.out.println(">>>add complex Type to Registry: " + typeName +
                               " baseDTValidator=" + typeInfo.baseDataTypeValidator +
                               " baseCTInfo=" + typeInfo.baseComplexTypeInfo + 
                               " derivedBy=" + typeInfo.derivedBy + 
                             " contentType=" + typeInfo.contentType +
                               " contentSpecHandle=" + typeInfo.contentSpecHandle + 
                               " datatypeValidator=" + typeInfo.datatypeValidator);

        fComplexTypeRegistry.put(typeName,typeInfo);

        fCurrentScope = previousScope;
        if (isNamedType) {
            fCurrentTypeNameStack.pop();
            checkRecursingComplexType();
        }

        fSchemaGrammar.setElementComplexTypeInfo(typeInfo.templateElementIndex, typeInfo);

        typeNameIndex = fStringPool.addSymbol(typeName);
        return typeNameIndex;


      
    /**
     * Traverse SimpleContent Declaration                          
     *  
     *       <simpleContent 
     *         id = ID 
     *         {any attributes with non-schema namespace...}>
     *
     *         Content: (annotation? , (restriction | extension)) 
     *       </simpleContent>
     *
     *       <restriction
     *         base = QNAME
     *         id = ID           
     *         {any attributes with non-schema namespace...}>
     *
     *         Content: (annotation?,(simpleType?, (minExclusive|minInclusive|maxExclusive
     *                    | maxInclusive | precision | scale | length | minLength 
     *                    | maxLength | encoding | period | duration | enumeration 
     *                    | pattern | whiteSpace)*) ? ,  
     *                    ((attribute | attributeGroup)* , anyAttribute?))
     *       </restriction>
     *
     *       <extension
     *         base = QNAME
     *         id = ID           
     *         {any attributes with non-schema namespace...}>
     *         Content: (annotation? , ((attribute | attributeGroup)* , anyAttribute?))
     *       </extension>
     *
     * @param typeNameIndex
     * @param simpleContentTypeDecl
     * @param typeInfo                   
     * @return 
     */
    
    private void traverseSimpleContentDecl(int typeNameIndex,
               Element simpleContentDecl, ComplexTypeInfo typeInfo) 
               throws Exception {

        
        String typeName = fStringPool.toString(typeNameIndex);

        String simpleContentTypeId = simpleContentDecl.getAttribute(SchemaSymbols.ATTVAL_ID);

        typeInfo.contentType = XMLElementDecl.TYPE_SIMPLE;
        typeInfo.contentSpecHandle = -1;

        Element simpleContent = checkContent(simpleContentDecl,
                                     XUtil.getFirstChildElement(simpleContentDecl),false);
        
        if (simpleContent==null) {
          throw new ComplexTypeRecoverableError();
        }

        String simpleContentName = simpleContent.getLocalName();
        if (simpleContentName.equals(SchemaSymbols.ELT_RESTRICTION))
          typeInfo.derivedBy = SchemaSymbols.RESTRICTION;
        else if (simpleContentName.equals(SchemaSymbols.ELT_EXTENSION))
          typeInfo.derivedBy = SchemaSymbols.EXTENSION;
        else {
          
          throw new ComplexTypeRecoverableError(
                     "The content of the simpleContent element is invalid.  The " +
                     "content must be RESTRICTION or EXTENSION");
        }

        String base = simpleContent.getAttribute(SchemaSymbols.ATT_BASE);
        String typeId = simpleContent.getAttribute(SchemaSymbols.ATTVAL_ID);


        Element content = checkContent(simpleContent,
                              XUtil.getFirstChildElement(simpleContent),true); 

        if (base.length() == 0)  {
          throw new ComplexTypeRecoverableError(
                  "The BASE attribute must be specified for the " +
                  "RESTRICTION or EXTENSION element"); 
        }

        QName baseQName = parseBase(base);
        processBaseTypeInfo(baseQName,typeInfo);
       
        if (typeInfo.baseComplexTypeInfo != null)  {
             if (typeInfo.baseComplexTypeInfo.contentSpecHandle > -1) {
                 throw new ComplexTypeRecoverableError(
                 "The type '"+ base +"' specified as the " + 
                 "base in the simpleContent element must not have complexContent");
             }
        }
           
        Element attrNode = null;
        if (typeInfo.derivedBy==SchemaSymbols.RESTRICTION) {
            if (typeInfo.baseDataTypeValidator != null) {
                throw new ComplexTypeRecoverableError(
                 "The type '" + base +"' is a simple type.  It cannot be used in a "+
                 "derivation by RESTRICTION for a complexType");
            }
            else {
               typeInfo.baseDataTypeValidator = typeInfo.baseComplexTypeInfo.datatypeValidator;
            }

            if (content.getLocalName().equals(SchemaSymbols.ELT_SIMPLETYPE )) { 
                int simpleTypeNameIndex = traverseSimpleTypeDecl(content); 
                if (simpleTypeNameIndex!=-1) {
                    typeInfo.baseDataTypeValidator=fDatatypeRegistry.getDatatypeValidator(
                       fStringPool.toString(simpleTypeNameIndex));
                    content = XUtil.getNextSiblingElement(content);
                }
                else {
                    throw new ComplexTypeRecoverableError();
                }
            }

 
            int numEnumerationLiterals = 0;
            int numFacets = 0;
            Hashtable facetData        = new Hashtable();
            Vector enumData            = new Vector();
            Element child;

            for (child = content;
                 child != null && (child.getLocalName().equals(SchemaSymbols.ELT_MINEXCLUSIVE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MININCLUSIVE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MAXEXCLUSIVE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MAXINCLUSIVE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_PRECISION) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_SCALE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_LENGTH) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MINLENGTH) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MAXLENGTH) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_ENCODING) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_PERIOD) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_DURATION) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_ENUMERATION) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_PATTERN) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION));
                 child = XUtil.getNextSiblingElement(child)) 
            {
                if ( child.getNodeType() == Node.ELEMENT_NODE ) {
                    Element facetElt = (Element) child;
                    numFacets++;
                    if (facetElt.getLocalName().equals(SchemaSymbols.ELT_ENUMERATION)) {
                        numEnumerationLiterals++;
                        enumData.addElement(facetElt.getAttribute(SchemaSymbols.ATT_VALUE));
                        Element enumContent =  XUtil.getFirstChildElement( facetElt );
                        if( enumContent != null && 
                            enumContent.getLocalName().equals
                                  ( SchemaSymbols.ELT_ANNOTATION )){
                            traverseAnnotationDecl( child );   
                        }
                    } 
                    else {
                        facetData.put(facetElt.getLocalName(),
                              facetElt.getAttribute( SchemaSymbols.ATT_VALUE ));
                    }
                }

            if (numEnumerationLiterals > 0) {
                facetData.put(SchemaSymbols.ELT_ENUMERATION, enumData);
            }

            if (numFacets > 0) {
                typeInfo.datatypeValidator = fDatatypeRegistry.createDatatypeValidator(
                                      typeName,
                                      typeInfo.baseDataTypeValidator, facetData, false);
            }
            else
                typeInfo.datatypeValidator = 
                             typeInfo.baseDataTypeValidator;

            if (child != null) {
               if (!isAttrOrAttrGroup(child)) {
                  throw new ComplexTypeRecoverableError(
                     "Invalid child in the RESTRICTION element of simpleContent");
               }
               else
                  attrNode = child;
            }
            

        else {
            if (typeInfo.baseComplexTypeInfo != null)
               typeInfo.baseDataTypeValidator = typeInfo.baseComplexTypeInfo.datatypeValidator;

            typeInfo.datatypeValidator = typeInfo.baseDataTypeValidator;

            if (content != null)  {
               if (!isAttrOrAttrGroup(content)) {
                   throw new ComplexTypeRecoverableError(
                             "Only annotations and attributes are allowed in the " +
                             "content of an EXTENSION element for a complexType"); 
               }
               else {
                   attrNode = content;
               }
            }
              
        }

        int templateElementNameIndex = fStringPool.addSymbol("$"+typeName);

        typeInfo.templateElementIndex = fSchemaGrammar.addElementDecl(
             new QName(-1, templateElementNameIndex,typeNameIndex,fTargetNSURI),
             (fTargetNSURI==-1) ? -1 : fCurrentScope, typeInfo.scopeDefined,
             typeInfo.contentType, 
             typeInfo.contentSpecHandle, -1, typeInfo.datatypeValidator);
        typeInfo.attlistHead = fSchemaGrammar.getFirstAttributeDeclIndex(
                                typeInfo.templateElementIndex);

        processAttributes(attrNode,baseQName,typeInfo);

        if (XUtil.getNextSiblingElement(simpleContent) != null) 
            throw new ComplexTypeRecoverableError(
               "Invalid child following the RESTRICTION or EXTENSION element in the " +
               "complex type definition");


    /**
     * Traverse complexContent Declaration                          
     *  
     *       <complexContent 
     *         id = ID 
     *         mixed = boolean 
     *         {any attributes with non-schema namespace...}>
     *
     *         Content: (annotation? , (restriction | extension)) 
     *       </complexContent>
     *
     *       <restriction
     *         base = QNAME
     *         id = ID           
     *         {any attributes with non-schema namespace...}>
     *
     *         Content: (annotation? , (group | all | choice | sequence)?, 
     *                  ((attribute | attributeGroup)* , anyAttribute?))
     *       </restriction>
     *
     *       <extension
     *         base = QNAME
     *         id = ID           
     *         {any attributes with non-schema namespace...}>
     *         Content: (annotation? , (group | all | choice | sequence)?, 
     *                  ((attribute | attributeGroup)* , anyAttribute?))
     *       </extension>
     *
     * @param typeNameIndex
     * @param simpleContentTypeDecl
     * @param typeInfo                   
     * @param mixedOnComplexTypeDecl                   
     * @return 
     */
    
    private void traverseComplexContentDecl(int typeNameIndex,  
               Element complexContentDecl, ComplexTypeInfo typeInfo, 
               boolean mixedOnComplexTypeDecl) throws Exception { 

        String typeName = fStringPool.toString(typeNameIndex);

        String typeId = complexContentDecl.getAttribute(SchemaSymbols.ATTVAL_ID);
        String mixed = complexContentDecl.getAttribute(SchemaSymbols.ATT_MIXED);
        
        boolean isMixed = mixedOnComplexTypeDecl;
        if (mixed.equals(SchemaSymbols.ATTVAL_TRUE))
           isMixed = true;
        else if (mixed.equals(SchemaSymbols.ATTVAL_FALSE))
           isMixed = false;
        
        typeInfo.datatypeValidator = null;
        typeInfo.baseDataTypeValidator = null;

        Element complexContent = checkContent(complexContentDecl,
                                 XUtil.getFirstChildElement(complexContentDecl),false);
        
        if (complexContent==null) {
           throw new ComplexTypeRecoverableError();
        }

        String complexContentName = complexContent.getLocalName();
        if (complexContentName.equals(SchemaSymbols.ELT_RESTRICTION))
          typeInfo.derivedBy = SchemaSymbols.RESTRICTION;
        else if (complexContentName.equals(SchemaSymbols.ELT_EXTENSION))
          typeInfo.derivedBy = SchemaSymbols.EXTENSION;
        else {
           throw new ComplexTypeRecoverableError(
                     "The content of the complexContent element is invalid. " +
                     "The content must be RESTRICTION or EXTENSION");
        }

        String base = complexContent.getAttribute(SchemaSymbols.ATT_BASE);
        String complexContentTypeId=complexContent.getAttribute(SchemaSymbols.ATTVAL_ID);


        Element content = checkContent(complexContent,
                              XUtil.getFirstChildElement(complexContent),true); 

        if (base.length() == 0)  {
           throw new ComplexTypeRecoverableError(
                  "The BASE attribute must be specified for the " +
                  "RESTRICTION or EXTENSION element"); 
        }

        QName baseQName = parseBase(base);

        String baseTypeURI = fStringPool.toString(baseQName.uri);
        String baseLocalName = fStringPool.toString(baseQName.localpart);
        if (!(baseTypeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) &&
            baseLocalName.equals("anyType"))) {

            processBaseTypeInfo(baseQName,typeInfo);
       
            if (typeInfo.baseComplexTypeInfo == null)  {
                 throw new ComplexTypeRecoverableError(
                   "The base type specified in the complexContent element must be a complexType");
            }
        }

        processComplexContent(typeNameIndex,content,typeInfo,baseQName,isMixed);

        if (XUtil.getNextSiblingElement(complexContent) != null) 
            throw new ComplexTypeRecoverableError(
               "Invalid child following the RESTRICTION or EXTENSION element in the " +
               "complex type definition");



    /**
     * Handle complexType error                                            
     *  
     * @param message 
     * @param typeNameIndex 
     * @param typeInfo 
     * @return 
     */
    private void handleComplexTypeError(String message, int typeNameIndex,  
                                        ComplexTypeInfo typeInfo) throws Exception {

        String typeName = fStringPool.toString(typeNameIndex);
        if (message != null) {
          if (typeName.startsWith("#")) 
            reportGenericSchemaError("Anonymous complexType: " + message);
          else
            reportGenericSchemaError("ComplexType '" + typeName + "': " + message);
        }

        typeInfo.contentSpecHandle = -1;
        typeInfo.derivedBy = 0;
        typeInfo.datatypeValidator = null;
        typeInfo.attlistHead = -1;

        int templateElementNameIndex = fStringPool.addSymbol("$"+typeName);
        typeInfo.templateElementIndex = fSchemaGrammar.addElementDecl(
            new QName(-1, templateElementNameIndex,typeNameIndex,fTargetNSURI),
            (fTargetNSURI==-1) ? -1 : fCurrentScope, typeInfo.scopeDefined,
            typeInfo.contentType, 
            typeInfo.contentSpecHandle, -1, typeInfo.datatypeValidator);
        return;
    }

    /**
     * Parse base string                                            
     *  
     * @param base
     * @return QName
     */
    private QName parseBase(String base) throws Exception {

        String prefix = "";
        String localpart = base;
        int colonptr = base.indexOf(":");
        if ( colonptr > 0) {
            prefix = base.substring(0,colonptr);
            localpart = base.substring(colonptr+1);
        }

        int nameIndex = fStringPool.addSymbol(base);
        int prefixIndex = fStringPool.addSymbol(prefix);
        int localpartIndex = fStringPool.addSymbol(localpart);
        int URIindex = fStringPool.addSymbol(resolvePrefixToURI(prefix));
        return new QName(prefixIndex,localpartIndex,nameIndex,URIindex);
    }
    
    /**
     * Check if base is from another schema                         
     *  
     * @param baseName
     * @return boolean
     */
    private boolean baseFromAnotherSchema(QName baseName) throws Exception {

        String typeURI = fStringPool.toString(baseName.uri);
        if ( ! typeURI.equals(fTargetNSURIString) 
             && ! typeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) 
             && typeURI.length() != 0 ) 
          return true;
        else
          return false;
                                        
    }
    
    /**
     * Process "base" information for a complexType
     *  
     * @param baseTypeInfo        
     * @param baseName
     * @param typeInfo
     * @return 
     */
    
    private void processBaseTypeInfo(QName baseName, ComplexTypeInfo typeInfo) throws Exception {

        ComplexTypeInfo baseComplexTypeInfo = null;
        DatatypeValidator baseDTValidator = null;
        
        String typeURI = fStringPool.toString(baseName.uri);
        String localpart = fStringPool.toString(baseName.localpart);
        String base = fStringPool.toString(baseName.rawname);


        if (baseFromAnotherSchema(baseName)) {
            baseComplexTypeInfo = getTypeInfoFromNS(typeURI, localpart);
            if (baseComplexTypeInfo == null) {
                baseDTValidator = getTypeValidatorFromNS(typeURI, localpart);
                if (baseDTValidator == null) {
                    throw new ComplexTypeRecoverableError(
                       "Could not find base type " +localpart 
                       + " in schema " + typeURI);
                }
            }
        }

        else {
            String fullBaseName = typeURI+","+localpart;

            baseComplexTypeInfo= (ComplexTypeInfo) fComplexTypeRegistry.get(fullBaseName);

            if (baseComplexTypeInfo == null) {
                baseDTValidator = getDatatypeValidator(typeURI, localpart);

                if (baseDTValidator == null) {
                    int baseTypeSymbol;
                    Element baseTypeNode = getTopLevelComponentByName(
                                   SchemaSymbols.ELT_COMPLEXTYPE,localpart);
                    if (baseTypeNode != null) {
                        baseTypeSymbol = traverseComplexTypeDecl( baseTypeNode );
                        baseComplexTypeInfo = (ComplexTypeInfo)
                        fComplexTypeRegistry.get(fStringPool.toString(baseTypeSymbol)); 
                    }
                    else {
                        baseTypeNode = getTopLevelComponentByName(
                                   SchemaSymbols.ELT_SIMPLETYPE, localpart);
                        if (baseTypeNode != null) {
                            baseTypeSymbol = traverseSimpleTypeDecl( baseTypeNode );
                            baseDTValidator = getDatatypeValidator(typeURI, localpart);
                            if (baseDTValidator == null)  {
                            }
                        }
                        else {
                            throw new ComplexTypeRecoverableError(
                                 "Base type could not be found : " + base);
                        }
                    }
                }
            }

        typeInfo.baseComplexTypeInfo = baseComplexTypeInfo;
        typeInfo.baseDataTypeValidator = baseDTValidator;



    /**
     * Process content which is complex                             
     *  
     *     (group | all | choice | sequence) ? , 
     *     ((attribute | attributeGroup)* , anyAttribute?))
     *
     * @param typeNameIndex
     * @param complexContentChild
     * @param typeInfo                   
     * @return 
     */
    
    private void processComplexContent(int typeNameIndex,
               Element complexContentChild, ComplexTypeInfo typeInfo, QName baseName,
               boolean isMixed) throws Exception {

       Element attrNode = null;
       int index=-2;

       if (complexContentChild != null) {


          String childName = complexContentChild.getLocalName();

          if (childName.equals(SchemaSymbols.ELT_GROUP)) {
               index = expandContentModel(traverseGroupDecl(complexContentChild), 
                                          complexContentChild);
               attrNode = XUtil.getNextSiblingElement(complexContentChild);
           }
           else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
               index = expandContentModel(traverseSequence(complexContentChild), 
                                         complexContentChild);
               attrNode = XUtil.getNextSiblingElement(complexContentChild);
           }
           else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
               index = expandContentModel(traverseChoice(complexContentChild), 
                                          complexContentChild);
               attrNode = XUtil.getNextSiblingElement(complexContentChild);
           }
           else if (childName.equals(SchemaSymbols.ELT_ALL)) {
               index = expandContentModel(traverseAll(complexContentChild), 
                                      complexContentChild);
               attrNode = XUtil.getNextSiblingElement(complexContentChild);
           }
           else if (isAttrOrAttrGroup(complexContentChild)) {
               typeInfo.contentType = XMLElementDecl.TYPE_ANY;
               attrNode = complexContentChild;
           }
           else {
               throw new ComplexTypeRecoverableError(
                "Invalid child '"+ childName +"' in the complex type");               
           }
       }
     
       if (isMixed) {


            int pcdataNode = fSchemaGrammar.addContentSpecNode(
                             XMLContentSpec.CONTENTSPECNODE_LEAF,
                             -1, false);
            if (index != -2) 
                index = fSchemaGrammar.addContentSpecNode(
                    XMLContentSpec.CONTENTSPECNODE_CHOICE,pcdataNode,index,false);
            else
                index = pcdataNode;

       }

       typeInfo.contentSpecHandle = index;

       if (typeInfo.baseComplexTypeInfo != null) {
           int baseContentSpecHandle = typeInfo.baseComplexTypeInfo.contentSpecHandle;

           if (typeInfo.derivedBy == SchemaSymbols.RESTRICTION) {
           }
           else {
               if (baseFromAnotherSchema(baseName)) {
                   String baseSchemaURI = fStringPool.toString(baseName.uri);
                   SchemaGrammar aGrammar= (SchemaGrammar) fGrammarResolver.getGrammar(
                                    baseSchemaURI);
                   baseContentSpecHandle = importContentSpec(aGrammar, baseContentSpecHandle);
               }
               if (typeInfo.contentSpecHandle == -2) {
                   typeInfo.contentSpecHandle = baseContentSpecHandle;
               }
               else if (baseContentSpecHandle > -1) {
                   typeInfo.contentSpecHandle = 
                   fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ, 
                                                     baseContentSpecHandle,
                                                     typeInfo.contentSpecHandle,
                                                     false);
               }
           }
       }
       else {
           typeInfo.derivedBy = 0;
       }

       if (isMixed) 
           typeInfo.contentType = XMLElementDecl.TYPE_MIXED;
       else if (typeInfo.contentSpecHandle == -2)
           typeInfo.contentType = XMLElementDecl.TYPE_EMPTY;
       else
           typeInfo.contentType = XMLElementDecl.TYPE_CHILDREN;


       String typeName = fStringPool.toString(typeNameIndex);
       int templateElementNameIndex = fStringPool.addSymbol("$"+typeName);

       typeInfo.templateElementIndex = fSchemaGrammar.addElementDecl(
            new QName(-1, templateElementNameIndex,typeNameIndex,fTargetNSURI),
            (fTargetNSURI==-1) ? -1 : fCurrentScope, typeInfo.scopeDefined,
            typeInfo.contentType, 
            typeInfo.contentSpecHandle, -1, typeInfo.datatypeValidator);
       typeInfo.attlistHead = fSchemaGrammar.getFirstAttributeDeclIndex(
                               typeInfo.templateElementIndex);

       if (attrNode !=null) {
           if (!isAttrOrAttrGroup(attrNode)) {
              throw new ComplexTypeRecoverableError(
                  "Invalid child "+ attrNode.getLocalName() + " in the complexType or complexContent");
           }
           else
              processAttributes(attrNode,baseName,typeInfo);
       }
       else if (typeInfo.baseComplexTypeInfo != null)
           processAttributes(null,baseName,typeInfo);



    /**
     * Process attributes of a complex type                              
     *  
     * @param attrNode
     * @param typeInfo                   
     * @return 
     */
    
    private void processAttributes(Element attrNode, QName baseName,            
               ComplexTypeInfo typeInfo) throws Exception {


        XMLAttributeDecl attWildcard = null;
        Vector anyAttDecls = new Vector();

        Element child;
        for (child = attrNode;
             child != null;
             child = XUtil.getNextSiblingElement(child)) {

            String childName = child.getLocalName();

            if (childName.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                traverseAttributeDecl(child, typeInfo, false);
            } 
            else if ( childName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ) { 
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
                    fromGroup = mergeTwoAnyAttribute(
                                fromGroup,(XMLAttributeDecl)anyAttDecls.elementAt(i));
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
        ComplexTypeInfo baseTypeInfo = typeInfo.baseComplexTypeInfo;

        if (baseTypeInfo != null && baseTypeInfo.attlistHead > -1 ) {
            int attDefIndex = baseTypeInfo.attlistHead;
            SchemaGrammar aGrammar = fSchemaGrammar;
            String baseTypeSchemaURI = baseFromAnotherSchema(baseName)? 
                           fStringPool.toString(baseName.uri):null;
            if (baseTypeSchemaURI != null) {
                aGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(baseTypeSchemaURI);
            }
            if (aGrammar == null) {
            }
            else
            while ( attDefIndex > -1 ) {
                fTempAttributeDecl.clear();
                aGrammar.getAttributeDecl(attDefIndex, fTempAttributeDecl);
                if (fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_ANY 
                    ||fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_LIST
                    ||fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_LOCAL 
                    ||fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_OTHER ) {
                    if (attWildcard == null) {
                        baseAttWildcard = fTempAttributeDecl;
                    }
                    attDefIndex = aGrammar.getNextAttributeDeclIndex(attDefIndex);
                    continue;
                }
                
                int temp = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, fTempAttributeDecl.name);
                if ( temp > -1) {
                    if (typeInfo.derivedBy==SchemaSymbols.RESTRICTION) {
                        attDefIndex = fSchemaGrammar.getNextAttributeDeclIndex(attDefIndex);
                        continue;
                    }
                }

               
                fSchemaGrammar.addAttDef( typeInfo.templateElementIndex, 
                                          fTempAttributeDecl.name, fTempAttributeDecl.type, 
                                          fTempAttributeDecl.enumeration, fTempAttributeDecl.defaultType, 
                                          fTempAttributeDecl.defaultValue, 
                                          fTempAttributeDecl.datatypeValidator,
                                          fTempAttributeDecl.list);
                attDefIndex = aGrammar.getNextAttributeDeclIndex(attDefIndex);
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

        typeInfo.attlistHead = fSchemaGrammar.getFirstAttributeDeclIndex
                                              (typeInfo.templateElementIndex);

    private boolean isAttrOrAttrGroup(Element e) 
    {
        String elementName = e.getLocalName();

        if (elementName.equals(SchemaSymbols.ELT_ATTRIBUTE) ||
            elementName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ||
            elementName.equals(SchemaSymbols.ELT_ANYATTRIBUTE))
          return true;
        else
          return false;
    }

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

    private int importContentSpec(SchemaGrammar aGrammar, int contentSpecHead ) throws Exception {
        XMLContentSpec ctsp = new XMLContentSpec();
        aGrammar.getContentSpec(contentSpecHead, ctsp);
        int left = -1;
        int right = -1;
        if ( ctsp.type == ctsp.CONTENTSPECNODE_LEAF 
             || (ctsp.type & 0x0f) == ctsp.CONTENTSPECNODE_ANY
             || (ctsp.type & 0x0f) == ctsp.CONTENTSPECNODE_ANY_LOCAL
             || (ctsp.type & 0x0f) == ctsp.CONTENTSPECNODE_ANY_OTHER ) {
            return fSchemaGrammar.addContentSpecNode(ctsp.type, ctsp.value, ctsp.otherValue, false);
        }
        else {
            if ( ctsp.value == -1 ) {
                left = -1;
            }
            else {
                left = importContentSpec(aGrammar, ctsp.value);
            }
            
            if ( ctsp.otherValue == -1 ) {
                right = -1;
            }
            else {
                right = importContentSpec(aGrammar, ctsp.otherValue);
            }
            return fSchemaGrammar.addContentSpecNode(ctsp.type, left, right, false);
        
        }
    }
    
    private int expandContentModel ( int index, Element particle) throws Exception {
        
        String minOccurs = particle.getAttribute(SchemaSymbols.ATT_MINOCCURS).trim();
        String maxOccurs = particle.getAttribute(SchemaSymbols.ATT_MAXOCCURS).trim();    

        int min=1, max=1;

        if(minOccurs.equals("0") && maxOccurs.equals("0")){
            return -2;
        }

        if (minOccurs.equals("")) {
            minOccurs = "1";
        }
        if (maxOccurs.equals("")) {
                maxOccurs = "1";
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
     * @param attributeDecl: the declaration of the attribute under 
	 * 		consideration
	 * @param typeInfo: Contains the index of the element to which 
	 * 		the attribute declaration is attached.
	 * @param referredTo:  true iff traverseAttributeDecl was called because 
	 *		of encountering a ``ref''property (used
	 *		to suppress error-reporting).
     * @return 
     * @exception Exception
     */
    private int traverseAttributeDecl( Element attrDecl, ComplexTypeInfo typeInfo, boolean referredTo ) throws Exception {
        String attNameStr    = attrDecl.getAttribute(SchemaSymbols.ATT_NAME);
		boolean isAttrTopLevel = isTopLevel(attrDecl);
		
        DatatypeValidator dv = null;
        int attType          = -1;
        boolean attIsList    = false;
        int dataTypeSymbol   = -1;

        String ref       = attrDecl.getAttribute(SchemaSymbols.ATT_REF); 
        String datatype  = attrDecl.getAttribute(SchemaSymbols.ATT_TYPE);
		if(!ref.equals("")) {
			if(isAttrTopLevel)
   	    	    reportGenericSchemaError ( "An attribute with \"ref\" present must not have <schema> as its parent");
			if(!attNameStr.equals(""))
   	    	    reportGenericSchemaError ( "Attribute " + attNameStr + " cannot refer to another attribute, but it refers to " + ref);
			if(!datatype.equals(""))
       	        reportGenericSchemaError ( "Attribute with reference " + ref + " cannot also contain a type");
        	if(!attrDecl.getAttribute(SchemaSymbols.ATT_FORM).equals(""))
       	        reportGenericSchemaError ( "Attribute with reference " + ref + " cannot also contain a \"form\" property");
        	if(!attrDecl.getAttribute(SchemaSymbols.ATT_VALUE).equals(""))
       	        reportGenericSchemaError ( "Attribute with reference " + ref + " cannot also contain a value");
		}
		Element simpleTypeChild = findAttributeSimpleType(attrDecl);

        String localpart = null;

        String  use      = attrDecl.getAttribute(SchemaSymbols.ATT_USE);
        boolean prohibited = use.equals(SchemaSymbols.ATTVAL_PROHIBITED);
        boolean required = use.equals(SchemaSymbols.ATTVAL_REQUIRED);

		if (!ref.equals("")) {
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
                traverseAttributeDecl(referredAttribute, typeInfo, true);
				int referredAttName = fStringPool.addSymbol(referredAttribute.getAttribute(SchemaSymbols.ATT_NAME));
        		int uriIndex = -1;
        		if ( fTargetNSURIString.length() > 0) 
            		uriIndex = fTargetNSURI; 
        		QName referredAttQName = new QName(-1,referredAttName,referredAttName,uriIndex);
				if (prohibited) {
	                int tempIndex = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, referredAttQName);
					XMLAttributeDecl referredAttrDecl = new XMLAttributeDecl();
					fSchemaGrammar.getAttributeDecl(tempIndex, referredAttrDecl);
					referredAttrDecl.defaultType = XMLAttributeDecl.DEFAULT_TYPE_PROHIBITED;
					fSchemaGrammar.setAttributeDecl(typeInfo.templateElementIndex, tempIndex, referredAttrDecl);
				}
				else if (required) {
   	             	int tempIndex = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, referredAttQName);
					XMLAttributeDecl referredAttrDecl = new XMLAttributeDecl();
					fSchemaGrammar.getAttributeDecl(tempIndex, referredAttrDecl);
					if(referredAttrDecl.defaultType == XMLAttributeDecl.DEFAULT_TYPE_FIXED) 
						referredAttrDecl.defaultType = XMLAttributeDecl.DEFAULT_TYPE_REQUIRED_AND_FIXED;
					else
						referredAttrDecl.defaultType = XMLAttributeDecl.DEFAULT_TYPE_REQUIRED;
					fSchemaGrammar.setAttributeDecl(typeInfo.templateElementIndex, tempIndex, referredAttrDecl);
				}
            }
            else {

                if (fAttributeDeclRegistry.get(localpart) != null) {
                    addAttributeDeclFromAnotherSchema(localpart, uriStr, typeInfo);
                }
                else 
                    reportGenericSchemaError ( "Couldn't find top level attribute " + ref);
            }
            return -1;
        }

        if (datatype.equals("")) {
            if (simpleTypeChild != null) { 
                attType        = XMLAttributeDecl.TYPE_SIMPLE;
                dataTypeSymbol = traverseSimpleTypeDecl(simpleTypeChild);
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
			if(simpleTypeChild != null && !referredTo)
            	reportGenericSchemaError("Attribute declarations may not contain both a type and a simpleType declaration");

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
                        }else if (!referredTo) {
                            reportGenericSchemaError("simpleType not found : " + "("+typeURI+":"+localpart+")");
                        }
                    }
                }


                dv = getDatatypeValidator(typeURI, localpart);
                if (dv == null && typeURI.equals(fTargetNSURIString) ) {
                    Element topleveltype = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
                    if (topleveltype != null) {
                        traverseSimpleTypeDecl( topleveltype );
                        dv = getDatatypeValidator(typeURI, localpart);
                    }else if (!referredTo) {
                        reportGenericSchemaError("simpleType not found : " + "("+typeURI+":"+ localpart+")");
                    }
                }

                attType = XMLAttributeDecl.TYPE_SIMPLE;
            }

        }


        int attDefaultType = -1;
        int attDefaultValue = -1;

        if (dv == null && !referredTo) {
            reportGenericSchemaError("could not resolve the type or get a null validator for datatype : " 
                                     + fStringPool.toString(dataTypeSymbol));
        }

        String fixed = attrDecl.getAttribute(SchemaSymbols.ATT_VALUE);
		if (isAttrTopLevel) {
            if (!fixed.equals("")) {
				if((required || prohibited
						|| use.equals(SchemaSymbols.ATTVAL_OPTIONAL)) && !referredTo) 
            		reportGenericSchemaError("Globally-declared attributes containing values must have \"use\" set to \"FIXED\" or \"DEFAULT\", not " +
						use);
				else if (use.equals("") && !referredTo) 
					reportGenericSchemaError("Globally-declared attributes containing values MUST have \"use\" present and set to \"FIXED\" or \"DEFAULT\"");
            	else if (use.equals(SchemaSymbols.ATTVAL_FIXED))	{
                   	attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_FIXED;
                   	attDefaultValue = fStringPool.addString(fixed);
				} 
				else {
                   	attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_DEFAULT;
                   	attDefaultValue = fStringPool.addString(fixed);
				}
			}
            	if (!use.equals("") && !referredTo)	
					reportGenericSchemaError("Globally-declared attributes containing no value may not have \"use\" present");
            	else 
               		attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_IMPLIED;
			}
		} 
            if (!fixed.equals("")) {
				if(required || prohibited
						|| use.equals(SchemaSymbols.ATTVAL_OPTIONAL)) 
            		reportGenericSchemaError("Locally-declared attributes containing values must have \"use\" set to \"FIXED\" or \"DEFAULT\", not " +
						use);
				else if (use.equals("")) 
					reportGenericSchemaError("Locally-declared attributes containing values MUST have \"use\" present and set to \"FIXED\" or \"DEFAULT\"");
            	else if (use.equals(SchemaSymbols.ATTVAL_FIXED))	{
                   	attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_FIXED;
                   	attDefaultValue = fStringPool.addString(fixed);
				} 
				else {
                   	attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_DEFAULT;
                   	attDefaultValue = fStringPool.addString(fixed);
				}
			}
				if(required) 
                   	attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_REQUIRED;
				else if (prohibited)  
                   	attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_PROHIBITED;
            	else 
               		attDefaultType = XMLAttributeDecl.DEFAULT_TYPE_IMPLIED;
			}
		}

        if (attType == XMLAttributeDecl.TYPE_SIMPLE && attDefaultValue != -1) {
            try { 
                if (dv != null) 
                    dv.validate(fStringPool.toString(attDefaultValue), null);
                else if (!referredTo)
                    reportSchemaError(SchemaMessageProvider.NoValidatorFor,
                            new Object [] { datatype });
            } catch (InvalidDatatypeValueException idve) {
				if (!referredTo) 
                	reportSchemaError(SchemaMessageProvider.IncorrectDefaultType,
                        new Object [] { attrDecl.getAttribute(SchemaSymbols.ATT_NAME), idve.getMessage() });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Internal error in attribute datatype validation");
            }
        }

        int uriIndex = -1;
        if ( fTargetNSURIString.length() > 0) {
                if ( isAttrTopLevel) {
                        uriIndex = fTargetNSURI;
                }
                else if ( !isQName.equals(SchemaSymbols.ATTVAL_UNQUALIFIED)){
                        if ( isQName.equals(SchemaSymbols.ATTVAL_QUALIFIED)||
                            fAttributeDefaultQualified ) {
                                uriIndex = fTargetNSURI;
                        }
                }
        }

        QName attQName = new QName(-1,attName,attName,uriIndex);
        if ( DEBUGGING )
            System.out.println(" the dataType Validator for " + fStringPool.toString(attName) + " is " + dv);

        if (isAttrTopLevel) {
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
       
            if ( child.getLocalName().equals(SchemaSymbols.ELT_ATTRIBUTE) ){
                traverseAttributeDecl(child, typeInfo, false);
            }
            else if ( child.getLocalName().equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ) {
                traverseAttributeGroupDecl(child, typeInfo,anyAttDecls);
            }
            else if ( child.getLocalName().equals(SchemaSymbols.ELT_ANYATTRIBUTE) ) {
                anyAttDecls.addElement(traverseAnyAttribute(child));
                break;
            }
            else if (child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION) ) {
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

            if ( child.getLocalName().equals(SchemaSymbols.ELT_ATTRIBUTE) ){
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
                    traverseAttributeDecl(child, typeInfo, false);
            }
            else if ( child.getLocalName().equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ) {
                traverseAttributeGroupDecl(child, typeInfo, anyAttDecls);
            }
            else if ( child.getLocalName().equals(SchemaSymbols.ELT_ANYATTRIBUTE) ) {
                anyAttDecls.addElement(traverseAnyAttribute(child));
                break;
            }
            else if (child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION) ) {
            }
        }

        fNamespacesScope = saveNSMapping;
        fTargetNSURI = saveTargetNSUri;
        return -1;
    
	private Element findAttributeSimpleType(Element attrDecl) throws Exception {
		Element child = XUtil.getFirstChildElement(attrDecl);
	    	if (child == null)
	   		return null;
		if (child.getLocalName().equals(SchemaSymbols.ELT_SIMPLETYPE))
			return child;
			if (child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION)) {
   	 			traverseAnnotationDecl(child);
				child = XUtil.getNextSiblingElement(child);
			}
	    	if (child == null)
	   		return null;
		if (child.getLocalName().equals(SchemaSymbols.ELT_SIMPLETYPE) &&
				XUtil.getNextSiblingElement(child) == null) 
			return child;
		reportGenericSchemaError ( "An attribute declaration must contain at most one annotation preceding at most one simpleType");
		return null;

    /**
     * Traverse element declaration:
     *  <element
     *         abstract = boolean
     *         block = #all or (possibly empty) subset of {substitutionGroup, extension, restriction}
     *         default = string
     *         substitutionGroup = QName
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
		if(!(dflt.equals("") || fixed.equals(""))) 
			reportGenericSchemaError("an element cannot have both \"fixed\" and \"default\" present at the same time");
        String substitutionGroup = elementDecl.getAttribute(SchemaSymbols.ATT_SUBSTITUTIONGROUP);
        String isQName = elementDecl.getAttribute(SchemaSymbols.ATT_FORM);

        String fromAnotherSchema = null;

        if (isTopLevel(elementDecl)) {
			if(name.equals(""))
                reportGenericSchemaError("globally-declared element must have a name");
			else if (!ref.equals(""))
                reportGenericSchemaError("globally-declared element " + name + " cannot have a ref attribute");
        
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

        if (!ref.equals("")) {
        	if (!type.equals("") || (elementMiscFlags > 0) 
					|| (finalSet > 0) || (blockSet > 0)
					|| !dflt.equals("") || !fixed.equals(""))
            	reportSchemaError(SchemaMessageProvider.BadAttWithRef, null);
			if (!name.equals(""))
                reportGenericSchemaError("element " + name + " cannot also have a ref attribute");

            Element child = XUtil.getFirstChildElement(elementDecl);
        	if(child != null && child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION)) {
            	if (XUtil.getNextSiblingElement(child) != null) 
                	reportSchemaError(SchemaMessageProvider.NoContentForRef, null);
				else
					traverseAnnotationDecl(child);
			}
			else if (child != null) 
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
                
        Element substitutionGroupElementDecl = null;
        int substitutionGroupElementDeclIndex = -1;
        boolean noErrorSoFar = true;
        String substitutionGroupUri = null;
        String substitutionGroupLocalpart = null;
        String substitutionGroupFullName = null;
        ComplexTypeInfo substitutionGroupEltTypeInfo = null;
        DatatypeValidator substitutionGroupEltDV = null;

        if ( substitutionGroup.length() > 0 ) {
            substitutionGroupUri =  resolvePrefixToURI(getPrefix(substitutionGroup));
            substitutionGroupLocalpart = getLocalPart(substitutionGroup);
            substitutionGroupFullName = substitutionGroupUri+","+substitutionGroupLocalpart;
           
            if ( !substitutionGroupUri.equals(fTargetNSURIString) ) {  
                substitutionGroupEltTypeInfo = getElementDeclTypeInfoFromNS(substitutionGroupUri, substitutionGroupLocalpart);
                if (substitutionGroupEltTypeInfo == null) {
                    substitutionGroupEltDV = getElementDeclTypeValidatorFromNS(substitutionGroupUri, substitutionGroupLocalpart);
                    if (substitutionGroupEltDV == null) {
                        noErrorSoFar = false;
                        reportGenericSchemaError("Could not find type for element '" +substitutionGroupLocalpart 
                                                 + "' in schema '" + substitutionGroupUri+"'");
                    }
                }
            }
            else {
                substitutionGroupElementDecl = getTopLevelComponentByName(SchemaSymbols.ELT_ELEMENT, substitutionGroupLocalpart);
                if (substitutionGroupElementDecl == null) {
                    substitutionGroupElementDeclIndex = 
                        fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(substitutionGroup),TOP_LEVEL_SCOPE);
                    if ( substitutionGroupElementDeclIndex == -1) {
                        noErrorSoFar = false;
                        reportGenericSchemaError("substitutionGroup affiliation element "
                                                  +substitutionGroup
                                                  +" in element declaration " 
                                                  +name);  
                    }
                }
                else {
                    substitutionGroupElementDeclIndex = 
                        fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(substitutionGroup),TOP_LEVEL_SCOPE);

                    if ( substitutionGroupElementDeclIndex == -1) {
                        traverseElementDecl(substitutionGroupElementDecl);
                        substitutionGroupElementDeclIndex = 
                            fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(substitutionGroup),TOP_LEVEL_SCOPE);
                    }
                }

                if (substitutionGroupElementDeclIndex != -1) {
                    substitutionGroupEltTypeInfo = fSchemaGrammar.getElementComplexTypeInfo( substitutionGroupElementDeclIndex );
                    if (substitutionGroupEltTypeInfo == null) {
                        fSchemaGrammar.getElementDecl(substitutionGroupElementDeclIndex, fTempElementDecl);
                        substitutionGroupEltDV = fTempElementDecl.datatypeValidator;
                        if (substitutionGroupEltDV == null) {
                            noErrorSoFar = false;
                            reportGenericSchemaError("Could not find type for element '" +substitutionGroupLocalpart 
                                                     + "' in schema '" + substitutionGroupUri+"'");
                        }
                    }
                }
            }
 
        }
        


        ComplexTypeInfo typeInfo = null;

        Element child = XUtil.getFirstChildElement(elementDecl);
        
        if(child != null && child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION)) {
			traverseAnnotationDecl(child);
            child = XUtil.getNextSiblingElement(child);
		}
        if(child != null && child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION)) 
            reportGenericSchemaError("element declarations can contain at most one annotation Element Information Item");
        
        boolean haveAnonType = false;

        if (child != null) {
            
            String childName = child.getLocalName();
            
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
            	child = XUtil.getNextSiblingElement(child);
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
            	child = XUtil.getNextSiblingElement(child);
                contentSpecType = XMLElementDecl.TYPE_ANY;
                contentSpecNodeIndex = -1;
            } 
			if (child != null) 
            	childName = child.getLocalName();
			while ((child != null) && ((childName.equals(SchemaSymbols.ELT_KEY))
					|| (childName.equals(SchemaSymbols.ELT_KEYREF)) 
					|| (childName.equals(SchemaSymbols.ELT_UNIQUE)))) {
            	child = XUtil.getNextSiblingElement(child);
                if (child != null) {
                    childName = child.getLocalName();
                }
			}
			if (child != null) {
            	noErrorSoFar = false;
                reportGenericSchemaError("only annotation, simpleType, complexType, key, keyref and unique Element Information Items are allowed in element declarations");
			}
        } 

        if (haveAnonType && (type.length()>0)) {
            noErrorSoFar = false;
            reportGenericSchemaError( "Element '"+ name +
                                      "' have both a type attribute and a annoymous type child" );
        }
        else if (!type.equals("")) { 
            if (substitutionGroupElementDecl != null) {
                checkSubstitutionGroupOK(elementDecl, substitutionGroupElementDecl); 
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
            if (substitutionGroupElementDecl != null ) {
                checkSubstitutionGroupOK(elementDecl, substitutionGroupElementDecl); 
            }

        }
        else {
            if ( typeInfo == null && dv == null ) typeInfo = substitutionGroupEltTypeInfo;
            if ( typeInfo == null && dv == null ) dv = substitutionGroupEltDV;
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
            if (typeInfo == null) {
            }
        }


        int elementNameIndex     = fStringPool.addSymbol(name);
        int localpartIndex = elementNameIndex;
        int uriIndex = -1;
        int enclosingScope = fCurrentScope;

        if ( isTopLevel(elementDecl)) {
            uriIndex = fTargetNSURI;
            enclosingScope = TOP_LEVEL_SCOPE;
        }
        else if ( !isQName.equals(SchemaSymbols.ATTVAL_UNQUALIFIED)){
                if ( isQName.equals(SchemaSymbols.ATTVAL_QUALIFIED)||
                   fElementDefaultQualified ) {
                        uriIndex = fTargetNSURI;
                }
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

        fSchemaGrammar.setElementComplexTypeInfo(elementIndex, typeInfo); 

        fSchemaGrammar.setElementFromAnotherSchemaURI(elementIndex, fromAnotherSchema);

        fSchemaGrammar.setElementDeclBlockSet(elementIndex, blockSet);
        fSchemaGrammar.setElementDeclFinalSet(elementIndex, finalSet);
        fSchemaGrammar.setElementDeclMiscFlags(elementIndex, elementMiscFlags);

        fSchemaGrammar.setElementDeclSubstitutionGroupElementFullName(elementIndex, substitutionGroupFullName);


        Element ic = XUtil.getFirstChildElement(elementDecl, IDENTITY_CONSTRAINTS);
        Vector idConstraints = null;
        
        if (ic != null) {
            XMLElementDecl edecl = new XMLElementDecl();
            fSchemaGrammar.getElementDecl(elementIndex, edecl);
            while (ic != null){
                String icName = ic.getLocalName();
                if ( icName.equals(SchemaSymbols.ELT_KEY) ) { 
                    traverseKey(ic, edecl);
                }
                else if ( icName.equals(SchemaSymbols.ELT_KEYREF) ) {
                    traverseKeyRef(ic, edecl);
                }
                else if ( icName.equals(SchemaSymbols.ELT_UNIQUE) ) {
                    traverseUnique(ic, edecl);
                }
                else {
                    throw new RuntimeException("identity constraint must be one of "+
                                               "\""+SchemaSymbols.ELT_UNIQUE+"\", "+
                                               "\""+SchemaSymbols.ELT_KEY+"\", or "+
                                               "\""+SchemaSymbols.ELT_KEYREF+'"');
                }
                fSchemaGrammar.setElementDecl(elementIndex, edecl);
                ic = XUtil.getNextSiblingElement(ic, IDENTITY_CONSTRAINTS);
            }
        }
        
        return eltQName;


    private void traverseUnique(Element uelem, XMLElementDecl edecl) 
        throws Exception {

        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: traverseUnique(\""+uelem.getNodeName()+"\")");
        }
        String ename = getElementNameFor(uelem);
        Unique unique = new Unique(ename);

        traverseIdentityConstraint(unique, uelem);

        edecl.unique.addElement(unique);


    private void traverseKey(Element kelem, XMLElementDecl edecl)
        throws Exception {

        String kname = kelem.getAttribute(SchemaSymbols.ATT_NAME);
        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: traverseKey(\""+kelem.getNodeName()+"\") ["+kname+']');
        }
        String ename = getElementNameFor(kelem);
        Key key = new Key(ename, kname);

        traverseIdentityConstraint(key, kelem);

        edecl.key.addElement(key);


    private void traverseKeyRef(Element krelem, XMLElementDecl edecl) 
        throws Exception {

        String krname = krelem.getAttribute(SchemaSymbols.ATT_NAME);
        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: traverseKeyRef(\""+krelem.getNodeName()+"\") ["+krname+']');
        }
        String ename = getElementNameFor(krelem);
        KeyRef keyRef = new KeyRef(ename, krname);

        traverseIdentityConstraint(keyRef, krelem);

        edecl.keyRef.addElement(keyRef);


    private void traverseIdentityConstraint(IdentityConstraint ic, 
                                            Element icelem) throws Exception {
        
        Element selem = XUtil.getFirstChildElement(icelem, SchemaSymbols.ELT_SELECTOR);
        String stext = CR_IMPL
                     ? selem.getAttribute(SchemaSymbols.ATT_XPATH) 
                     : XUtil.getChildText(selem);
        stext = stext.trim();
        try {
            Selector.XPath sxpath = new Selector.XPath(stext, fStringPool, fNamespacesScope);
            Selector selector = new Selector(sxpath, ic);
            if (DEBUG_IDENTITY_CONSTRAINTS) {
                System.out.println("<IC>:   selector: "+selector);
            }
            ic.setSelector(selector);
        }
        catch (XPathException e) {
            throw new SAXException(e.getMessage());
        }

        Element felem = XUtil.getNextSiblingElement(selem, SchemaSymbols.ELT_FIELD);
        while (felem != null) {
            String ftext = CR_IMPL
                         ? felem.getAttribute(SchemaSymbols.ATT_XPATH) 
                         : XUtil.getChildText(felem);
            ftext = ftext.trim();
            try {
                Field.XPath fxpath = new Field.XPath(ftext, fStringPool, fNamespacesScope);
                Field field = new Field(fxpath, null, ic);
                if (DEBUG_IDENTITY_CONSTRAINTS) {
                    System.out.println("<IC>:   field:    "+field);
                }
                ic.addField(field);
            }
            catch (XPathException e) {
                throw new SAXException(e.getMessage());
            }
            felem = XUtil.getNextSiblingElement(felem, SchemaSymbols.ELT_FIELD);
        }


    private String getElementNameFor(Element icnode) {
        Element enode = (Element)icnode.getParentNode();
        String ename = enode.getAttribute("name");
        if (ename.length() == 0) {
            ename = enode.getAttribute("ref");
        }
        return ename;

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
    
    private void checkSubstitutionGroupOK(Element elementDecl, Element substitutionGroupElementDecl){
    }
    
    private Element getTopLevelComponentByName(String componentCategory, String name) throws Exception {
        Element child = null;

        if ( componentCategory.equals(SchemaSymbols.ELT_GROUP) ) {
            child = (Element) fSchemaGrammar.topLevelGroupDecls.get(name);
        }
        else if ( componentCategory.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP ) ) {
            child = (Element) fSchemaGrammar.topLevelAttrGrpDecls.get(name);
        }
        else if ( componentCategory.equals(SchemaSymbols.ELT_ATTRIBUTE ) ) {
            child = (Element) fSchemaGrammar.topLevelAttrDecls.get(name);
        }

        if (child != null ) {
            return child;
        }
            
        child = XUtil.getFirstChildElement(fSchemaRootElement);

        if (child == null) {
            return null;
        }

        while (child != null ){
            if ( child.getLocalName().equals(componentCategory)) {
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
	/* why not make this more efficient by simply returning the conditional's value?  NG
        if (component.getParentNode().getLocalName().endsWith(SchemaSymbols.ELT_SCHEMA) ) {
            return true;
        }
        return false; 
	*****/
        return (component.getParentNode().getLocalName().endsWith(SchemaSymbols.ELT_SCHEMA) ); 
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
        while (child != null && child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION))
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

            String childName = child.getLocalName();
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
                if (index == -1) 
                    continue;
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
        while (child != null && child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION))
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
            String childName = child.getLocalName();
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
                if (index == -1) 
                    continue;                
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
        while (child != null && child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION))
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
            String childName = child.getLocalName();
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
                if (index == -1) 
                    continue;
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
        while (child != null && child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION))
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
            String childName = child.getLocalName();
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
                if (index == -1) 
                    continue;
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

        while (child != null && child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION))
            child = XUtil.getNextSiblingElement(child);

        int allChildren[] = null;
        int allChildCount = 0;

        int left = -2;

        for (;
             child != null;
             child = XUtil.getNextSiblingElement(child)) {

            int index = -2;
            boolean seeParticle = false;

            String childName = child.getLocalName();

            if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
                QName eltQName = traverseElementDecl(child);
                index = fSchemaGrammar.addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                       eltQName.localpart,
                                                       eltQName.uri, 
                                                       false);
                seeParticle = true;

            } 
            else if (CR_IMPL) {
                reportGenericSchemaError("Content of all group is restricted to elements only.  '" +  
               
                childName + "' was seen and is being ignored");
                break;
                
            }
            else {
               
                if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                    index = traverseGroupDecl(child);
                    if (index == -1) 
                        continue;
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

        if (allChildCount==0) 
          return left;

        try {
           left = allCalcWrapper(allChildren, allChildCount);
        } catch (java.lang.OutOfMemoryError e) {
            reportGenericSchemaError("The size of the <all>"
                + " declaration in your schema is too large for this parser"
                + " and elements using it will not validate correctly.");
        }
        return left;
    }
    
    private int allCalcWrapper (int[] initialArray, int size)
            throws Exception {
        int permSize = size/2;
        int[] targetArray = new int[size];
        System.arraycopy(initialArray, 0, targetArray, 0, size);
        
        if(targetArray.length == 1) {
            return targetArray[0];
        } else if (targetArray.length < 1) {
            return -2;
        } else if (permSize > targetArray.length) {
            reportGenericSchemaError("The size of the permutations " 
                + permSize + 
                " cannot be greater than the length of the array to be permuted; error in processing of <all>!");
            return -2;
        } else if (targetArray.length <= 3) {
            return allCombo(targetArray);
        } else {
            return allCalc (targetArray, 0, permSize, 0, new
                int[targetArray.length-permSize], -2);
        }

    private int allCombo(int[] targetArray) 
            throws Exception {
        if(targetArray.length == 2) {
            int left, right;  
            int[] lA = {targetArray[0], targetArray[1]};
            int[] rA = {targetArray[1], targetArray[0]};
            left = createSeq(lA);
            right = createSeq(rA);
            return fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE, left, right, false);
        } else if (targetArray.length == 3) {
            int tempChoice;
            int[] a1 = {targetArray[0], targetArray[1], targetArray[2]}; 
            int[] a2 = {targetArray[0], targetArray[2], targetArray[1]};
            int[] a3 = {targetArray[1], targetArray[0], targetArray[2]};
            int[] a4 = {targetArray[1], targetArray[2], targetArray[0]};
            int[] a5 = {targetArray[2], targetArray[1], targetArray[0]};
            int[] a6 = {targetArray[2], targetArray[0], targetArray[1]};
            int s1 = createSeq(a1);
            int s2 = createSeq(a2);
            int s3 = createSeq(a3);
            int s4 = createSeq(a4);
            int s5 = createSeq(a5);
            int s6 = createSeq(a6);
            tempChoice = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE,
                    s1, s2, false);
            tempChoice = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE,
                    tempChoice, s3, false);
            tempChoice = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE,
                    tempChoice, s4, false);
            tempChoice = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE,
                    tempChoice, s5, false);
            return fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE,
                    tempChoice, s6, false);
        } else {
            return -2;
        }
    
    private int allCalc(int[] targetArray, int targetPosition, int 
            permSize, int progressIndicator, int[] 
            complementArray, int choiceHead) 
                    throws Exception {
            int[] newTargetArray = new int[permSize+targetPosition];
            for (int i=targetPosition; i<targetArray.length; i++){
                arrayProducer(targetArray, i,
                    newTargetArray, complementArray,
                    progressIndicator);
                int c1 = allCalcWrapper(newTargetArray, newTargetArray.length);
                int c2 = allCalcWrapper(complementArray, complementArray.length);
                allSeq = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                    c1, c2, false);
                if (choiceHead != -2) { 
                    choiceHead =
                        fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE,
                        choiceHead, allSeq, false);
                } else {
                    choiceHead = allSeq;
                } 
            }
            return choiceHead;
            for (int i=targetPosition; i<targetArray.length; i++){
                int[] newTargetArray = new
                    int[targetArray.length-1];
                arrayProducer(targetArray, i, 
                    newTargetArray, complementArray,
                    progressIndicator);
                choiceHead = allCalc(newTargetArray, targetPosition, permSize,
                    progressIndicator+1, complementArray, choiceHead);
                targetPosition++;
                permSize--;
            }
            return choiceHead;

    private void arrayProducer(int [] targetArray, int tPos, 
            int[] newTargetArray, int[] complementArray, 
            int cPos) {
        complementArray[cPos] = targetArray[tPos];
        if (tPos > 0) 
            System.arraycopy(targetArray, 0, newTargetArray, 0, tPos);
        if (tPos < targetArray.length-1) 
            System.arraycopy(targetArray, tPos+1, newTargetArray, tPos, targetArray.length-tPos-1);

        
        
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

    }     


    private int parseInt (String intString) throws Exception
    {
            if ( intString.equals("*") ) {
                    return SchemaSymbols.INFINITY;
            } else {
                    return Integer.parseInt (intString);
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
                            } else if ( token.equals (SchemaSymbols.ELT_LIST) ) {
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
                    return SchemaSymbols.SUBSTITUTIONGROUP+SchemaSymbols.EXTENSION+SchemaSymbols.LIST+SchemaSymbols.RESTRICTION+SchemaSymbols.REPRODUCTION;
            } else {
                    int extend = 0;
                    int restrict = 0;
                    int reproduce = 0;

                    StringTokenizer t = new StringTokenizer (finalString, " ");
                    while (t.hasMoreTokens()) {
                            String token = t.nextToken ();

                            if ( token.equals (SchemaSymbols.ATTVAL_SUBSTITUTIONGROUP) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.SUBSTITUTIONGROUP;
                                    } else {
                                            reportGenericSchemaError ( "'substitutionGroup' already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_EXTENSION) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EXTENSION;
                                    } else {
                                            reportGenericSchemaError ( "extension already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ELT_LIST) ) {
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
                    return SchemaSymbols.SUBSTITUTIONGROUP+SchemaSymbols.EXTENSION+SchemaSymbols.LIST+SchemaSymbols.RESTRICTION+SchemaSymbols.REPRODUCTION;
            } else {
                    int extend = 0;
                    int restrict = 0;
                    int reproduce = 0;

                    StringTokenizer t = new StringTokenizer (finalString, " ");
                    while (t.hasMoreTokens()) {
                            String token = t.nextToken ();

                            if ( token.equals (SchemaSymbols.ATTVAL_SUBSTITUTIONGROUP) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.SUBSTITUTIONGROUP;
                                    } else {
                                            reportGenericSchemaError ( "'substitutionGroup' already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_EXTENSION) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EXTENSION;
                                    } else {
                                            reportGenericSchemaError ( "extension already in set" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ELT_LIST) ) {
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

        DOMParser parser = new IgnoreWhitespaceParser();
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

    static class IgnoreWhitespaceParser
        extends DOMParser {
        public void ignorableWhitespace(char ch[], int start, int length) {}
        public void ignorableWhitespace(int dataIdx) {}

