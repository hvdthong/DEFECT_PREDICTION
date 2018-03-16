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
import  org.apache.xerces.validators.schema.identity.XPath;
import  org.apache.xerces.validators.schema.identity.XPathException;
import  org.apache.xerces.validators.datatype.DatatypeValidator;
import  org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl;
import  org.apache.xerces.validators.datatype.IDDatatypeValidator;
import  org.apache.xerces.validators.datatype.NOTATIONDatatypeValidator;
import  org.apache.xerces.validators.datatype.StringDatatypeValidator;
import  org.apache.xerces.validators.datatype.ListDatatypeValidator;
import  org.apache.xerces.validators.datatype.UnionDatatypeValidator;
import  org.apache.xerces.validators.datatype.InvalidDatatypeValueException;
import  org.apache.xerces.validators.datatype.AnySimpleType;
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
/** Don't check the following code in because it creates a dependency on
    the serializer, preventing to package the parser without the serializer.
import  org.apache.xml.serialize.OutputFormat;
import  org.apache.xml.serialize.XMLSerializer;
**/
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
 * @version $Id: TraverseSchema.java 317921 2001-11-15 18:56:03Z  $
 */
public class TraverseSchema implements
                            NamespacesScope.NamespacesHandler{

    private static final int TOP_LEVEL_SCOPE = -1;

    /** Identity constraint keywords. */
    private static final String[][] IDENTITY_CONSTRAINTS = {
        { SchemaSymbols.URI_SCHEMAFORSCHEMA, SchemaSymbols.ELT_UNIQUE },
        { SchemaSymbols.URI_SCHEMAFORSCHEMA, SchemaSymbols.ELT_KEY },
        { SchemaSymbols.URI_SCHEMAFORSCHEMA, SchemaSymbols.ELT_KEYREF },
    };
    private static final String redefIdentifier = "_fn3dktizrknc9pi";
    private String fUnparsedExternalSchemas = null;
    private Hashtable fExternalSchemas = null;
    private String fExternalNoNamespaceSchema = null;
    private static final int NOT_ALL_CONTEXT    = 0;
    private static final int PROCESSING_ALL_EL  = 1;
    private static final int GROUP_REF_WITH_ALL = 2;
    private static final int CHILD_OF_GROUP     = 4;
    private static final int PROCESSING_ALL_GP  = 8;

    private static final boolean DEBUGGING = false;

    /** Compile to true to debug identity constraints. */
    private static final boolean DEBUG_IDENTITY_CONSTRAINTS = false;
    private static final boolean DEBUG_NEW_GROUP = true;

    /**
     * Compile to true to debug datatype validator lookup for
     * identity constraint support.
     */
    private static final boolean DEBUG_IC_DATATYPES = false;


    private boolean fFullConstraintChecking = false;

    private XMLErrorReporter    fErrorReporter = null;
    private StringPool          fStringPool    = null;

    private GrammarResolver fGrammarResolver = null;
    private SchemaGrammar fSchemaGrammar = null;

    private Element fSchemaRootElement;
    private SchemaInfo fSchemaInfoListRoot = null;
    private SchemaInfo fCurrentSchemaInfo = null;
    private boolean fRedefineSucceeded;

    private DatatypeValidatorFactoryImpl fDatatypeRegistry = null;

    private Hashtable fComplexTypeRegistry = new Hashtable();
    private Hashtable fAttributeDeclRegistry = new Hashtable();

    private Hashtable fGroupNameRegistry = new Hashtable();
    private Hashtable fRestrictedRedefinedGroupRegistry = new Hashtable();

    private Hashtable fSimpleTypeFinalRegistry = new Hashtable();

    private Hashtable fNotationRegistry = new Hashtable();

    private Vector fIncludeLocations = new Vector();
    private Vector fImportLocations = new Vector();
    private Hashtable fRedefineLocations = new Hashtable();
    private Vector fTraversedRedefineElements = new Vector();
    private Hashtable fRedefineAttributeGroupMap = null;

    private Hashtable fFacetData = new Hashtable(10);
    private Stack fSimpleTypeNameStack = new Stack();
    private String fListName = "";


    private int fAnonTypeCount =0;
    private int fScopeCount=0;
    private int fCurrentScope=TOP_LEVEL_SCOPE;
    private int fSimpleTypeAnonCount = 0;
    private Stack fCurrentTypeNameStack = new Stack();
    private Stack fBaseTypeNameStack = new Stack();
    private Stack fCurrentGroupNameStack = new Stack();
    private Vector fElementRecurseComplex = new Vector();
    private Vector fTopLevelElementsRefdFromGroup = new Vector();
    private Stack fCurrentAttrGroupNameStack = new Stack();

    private Vector fSubstitutionGroupRecursionRegistry = new Vector();
    private boolean fElementDefaultQualified = false;
    private boolean fAttributeDefaultQualified = false;
    private int fBlockDefault = 0;
    private int fFinalDefault = 0;

    private int fTargetNSURI;
    private String fTargetNSURIString = "";
    private NamespacesScope fNamespacesScope = null;
    private String fCurrentSchemaURL = "";
    private Stack fSchemaURLStack = new Stack();

    private XMLAttributeDecl fTempAttributeDecl = new XMLAttributeDecl();
    private XMLAttributeDecl fTemp2AttributeDecl = new XMLAttributeDecl();
    private XMLElementDecl fTempElementDecl = new XMLElementDecl();
    private XMLElementDecl fTempElementDecl2 = new XMLElementDecl();
    private XMLContentSpec tempContentSpec1 = new XMLContentSpec();
    private XMLContentSpec tempContentSpec2 = new XMLContentSpec();

    private EntityResolver  fEntityResolver = null;
    private SubstitutionGroupComparator fSComp = null;

    private Hashtable fIdentityConstraints = new Hashtable();
    private Hashtable fIdentityConstraintNames = new Hashtable();

    private GeneralAttrCheck fGeneralAttrCheck = null;
    private int fXsiURI;

    public class ComplexTypeInfo {
        public String typeName;

        public DatatypeValidator baseDataTypeValidator;
        public ComplexTypeInfo baseComplexTypeInfo;

        public int derivedBy = 0;
        public int blockSet = 0;
        public int finalSet = 0;

        public int miscFlags=0;

        public int scopeDefined = -1;

        public int contentType;
        public int contentSpecHandle = -1;
        public int templateElementIndex = -1;
        public int attlistHead = -1;
        public DatatypeValidator datatypeValidator;

        public boolean isAbstractType() {
           return ((miscFlags & CT_IS_ABSTRACT)!=0);
        }
        public boolean containsAttrTypeID () {
           return ((miscFlags & CT_CONTAINS_ATTR_TYPE_ID)!=0);
        }
        public boolean declSeen () {
           return ((miscFlags & CT_DECL_SEEN)!=0);
        }

        public void setIsAbstractType() {
           miscFlags |= CT_IS_ABSTRACT;
        }
        public void setContainsAttrTypeID() {
           miscFlags |= CT_CONTAINS_ATTR_TYPE_ID;
        }
        public void setDeclSeen() {
           miscFlags |= CT_DECL_SEEN;
        }

    }
    private static final int CT_IS_ABSTRACT=1;
    private static final int CT_CONTAINS_ATTR_TYPE_ID=2;

    private class ComplexTypeRecoverableError extends Exception {
        ComplexTypeRecoverableError() {super();}
        ComplexTypeRecoverableError(String s) {super(s);}
    }

    private class ParticleRecoverableError extends Exception {
        ParticleRecoverableError(String s) {super(s);}
    }

    private class GroupInfo {
        int contentSpecIndex = -1;
        int scope = -1;
    }

    private class ElementInfo {
        int elementIndex;
        String typeName;

        private ElementInfo(int i, String name) {
           elementIndex = i;
           typeName = name;
        }
    }


    private TraverseSchema( ) {
    }

    public void setFullConstraintCheckingEnabled() {
        fFullConstraintChecking = true;
    }

    public void setGrammarResolver(GrammarResolver grammarResolver){
        fGrammarResolver = grammarResolver;
    }
    public void startNamespaceDeclScope(int prefix, int uri){
    }
    public void endNamespaceDeclScope(int prefix){
    }

    public boolean particleEmptiable(int contentSpecIndex) {

       if (!fFullConstraintChecking) {
           return true;
       }
       if (minEffectiveTotalRange(contentSpecIndex)==0)
         return true;
       else
         return false;
    }

    public int minEffectiveTotalRange(int contentSpecIndex) {

       fSchemaGrammar.getContentSpec(contentSpecIndex, tempContentSpec1);
       int type = tempContentSpec1.type;
       if (type == XMLContentSpec.CONTENTSPECNODE_SEQ ||
           type == XMLContentSpec.CONTENTSPECNODE_ALL) {
          return minEffectiveTotalRangeSeq(contentSpecIndex);
       }
       else if (type == XMLContentSpec.CONTENTSPECNODE_CHOICE) {
          return minEffectiveTotalRangeChoice(contentSpecIndex);
       }
       else {
          return(fSchemaGrammar.getContentSpecMinOccurs(contentSpecIndex));
       }

    }

    private int minEffectiveTotalRangeSeq(int csIndex) {

       fSchemaGrammar.getContentSpec(csIndex, tempContentSpec1);
       int type = tempContentSpec1.type;
       int left = tempContentSpec1.value;
       int right = tempContentSpec1.otherValue;
       int min = fSchemaGrammar.getContentSpecMinOccurs(csIndex);

       int result;
       if (right == -2)
          result = min * minEffectiveTotalRange(left);
       else
          result = min * (minEffectiveTotalRange(left) + minEffectiveTotalRange(right));
       return result;

    }
    private int minEffectiveTotalRangeChoice(int csIndex) {

       fSchemaGrammar.getContentSpec(csIndex, tempContentSpec1);
       int type = tempContentSpec1.type;
       int left = tempContentSpec1.value;
       int right = tempContentSpec1.otherValue;
       int min = fSchemaGrammar.getContentSpecMinOccurs(csIndex);

       int result;
       if (right == -2)
          result = min * minEffectiveTotalRange(left);
       else {
          int minLeft = minEffectiveTotalRange(left);
          int minRight = minEffectiveTotalRange(right);
          result = min * ((minLeft < minRight)?minLeft:minRight);
       }
       return result;
    }

    public int maxEffectiveTotalRange(int contentSpecIndex) {

       fSchemaGrammar.getContentSpec(contentSpecIndex, tempContentSpec1);
       int type = tempContentSpec1.type;
       if (type == XMLContentSpec.CONTENTSPECNODE_SEQ ||
           type == XMLContentSpec.CONTENTSPECNODE_ALL) {
          return maxEffectiveTotalRangeSeq(contentSpecIndex);
       }
       else if (type == XMLContentSpec.CONTENTSPECNODE_CHOICE) {
          return maxEffectiveTotalRangeChoice(contentSpecIndex);
       }
       else {
          return(fSchemaGrammar.getContentSpecMaxOccurs(contentSpecIndex));
       }

    }

    private int maxEffectiveTotalRangeSeq(int csIndex) {

       fSchemaGrammar.getContentSpec(csIndex, tempContentSpec1);
       int type = tempContentSpec1.type;
       int left = tempContentSpec1.value;
       int right = tempContentSpec1.otherValue;
       int max = fSchemaGrammar.getContentSpecMaxOccurs(csIndex);

       if (max == SchemaSymbols.OCCURRENCE_UNBOUNDED)
         return SchemaSymbols.OCCURRENCE_UNBOUNDED;

       int maxLeft = maxEffectiveTotalRange(left);
       if (right == -2) {
         if (maxLeft == SchemaSymbols.OCCURRENCE_UNBOUNDED)
           return SchemaSymbols.OCCURRENCE_UNBOUNDED;
         else
           return max * maxLeft;
       }
       else {
         int maxRight = maxEffectiveTotalRange(right);
         if (maxLeft == SchemaSymbols.OCCURRENCE_UNBOUNDED ||
            maxRight == SchemaSymbols.OCCURRENCE_UNBOUNDED)
           return SchemaSymbols.OCCURRENCE_UNBOUNDED;
         else
           return max * (maxLeft + maxRight);
       }
    }

    private int maxEffectiveTotalRangeChoice(int csIndex) {

       fSchemaGrammar.getContentSpec(csIndex, tempContentSpec1);
       int type = tempContentSpec1.type;
       int left = tempContentSpec1.value;
       int right = tempContentSpec1.otherValue;
       int max = fSchemaGrammar.getContentSpecMaxOccurs(csIndex);

       if (max == SchemaSymbols.OCCURRENCE_UNBOUNDED)
         return SchemaSymbols.OCCURRENCE_UNBOUNDED;

       int maxLeft = maxEffectiveTotalRange(left);
       if (right == -2) {
         if (maxLeft == SchemaSymbols.OCCURRENCE_UNBOUNDED)
           return SchemaSymbols.OCCURRENCE_UNBOUNDED;
         else
           return max * maxLeft;
       }
       else {
         int maxRight = maxEffectiveTotalRange(right);
         if (maxLeft == SchemaSymbols.OCCURRENCE_UNBOUNDED ||
            maxRight == SchemaSymbols.OCCURRENCE_UNBOUNDED)
           return SchemaSymbols.OCCURRENCE_UNBOUNDED;
         else
           return max * ((maxLeft > maxRight)?maxLeft:maxRight);
       }
    }


    private String resolvePrefixToURI (String prefix) throws Exception  {
        String uriStr = fStringPool.toString(fNamespacesScope.getNamespaceForPrefix(fStringPool.addSymbol(prefix)));
        if (uriStr.length() == 0 && prefix.length() > 0) {
            reportGenericSchemaError("prefix : [" + prefix +"] cannot be resolved to a URI");
            return "";
        }

        return uriStr;
    }

    public  TraverseSchema(Element root, StringPool stringPool,
                           SchemaGrammar schemaGrammar,
                           GrammarResolver grammarResolver,
                           XMLErrorReporter errorReporter,
                           String schemaURL,
                           EntityResolver entityResolver,
                           boolean fullChecking,
                           GeneralAttrCheck generalAttrCheck,
                           String externalSchemaLocations, String noNamespaceSchemaLocation
                           ) throws Exception {
        fErrorReporter = errorReporter;
        fCurrentSchemaURL = schemaURL;
        fFullConstraintChecking = fullChecking;
        fEntityResolver = entityResolver;
        fGeneralAttrCheck = generalAttrCheck;
        fUnparsedExternalSchemas = externalSchemaLocations;
        if(externalSchemaLocations != null) {
            StringTokenizer tokenizer = new StringTokenizer(externalSchemaLocations, " \n\t\r", false);
            int tokenTotal = tokenizer.countTokens();
            if (tokenTotal % 2 == 0 ) {
                fExternalSchemas = new Hashtable();
                String uri = null;
                String location = null;
                while (tokenizer.hasMoreTokens()) {
                    uri = tokenizer.nextToken();
                    location = tokenizer.nextToken();
                    fExternalSchemas.put(location, uri);
                }
            }
        }
        fExternalNoNamespaceSchema = noNamespaceSchemaLocation;
        doTraverseSchema(root, stringPool, schemaGrammar, grammarResolver);
    }

    public  TraverseSchema(Element root, StringPool stringPool,
                           SchemaGrammar schemaGrammar,
                           GrammarResolver grammarResolver,
                           XMLErrorReporter errorReporter,
                           String schemaURL,
                           boolean fullChecking,
                           GeneralAttrCheck generalAttrCheck
                           ) throws Exception {
        fErrorReporter = errorReporter;
        fCurrentSchemaURL = schemaURL;
        fFullConstraintChecking = fullChecking;
        fGeneralAttrCheck = generalAttrCheck;
        doTraverseSchema(root, stringPool, schemaGrammar, grammarResolver);
    }

    public  TraverseSchema(Element root, StringPool stringPool,
                           SchemaGrammar schemaGrammar,
                           GrammarResolver grammarResolver,
                           boolean fullChecking,
                           GeneralAttrCheck generalAttrCheck
                           ) throws Exception {
        fFullConstraintChecking = fullChecking;
        fGeneralAttrCheck = generalAttrCheck;
        doTraverseSchema(root, stringPool, schemaGrammar, grammarResolver);
    }

    public  void doTraverseSchema(Element root, StringPool stringPool,
                           SchemaGrammar schemaGrammar,
                           GrammarResolver grammarResolver) throws Exception {


        fSchemaRootElement = root;
        fStringPool = stringPool;
        fSchemaGrammar = schemaGrammar;
        fNamespacesScope = new NamespacesScope(this);

        if (fFullConstraintChecking) {
          fSchemaGrammar.setDeferContentSpecExpansion();
          fSchemaGrammar.setCheckUniqueParticleAttribution();
        }

        fGrammarResolver = grammarResolver;
        fDatatypeRegistry = (DatatypeValidatorFactoryImpl) fGrammarResolver.getDatatypeRegistry();

        fDatatypeRegistry.expandRegistryToFullSchemaSet();

        fXsiURI = fStringPool.addSymbol(SchemaSymbols.URI_XSI);

        if (root == null) {
            return;
        }

        int scope = GeneralAttrCheck.ELE_CONTEXT_GLOBAL;
        Hashtable attrValues = generalCheck(root, scope);

        fTargetNSURIString = getTargetNamespaceString(root);
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

            if (fSchemaGrammar.getAttributeDeclRegistry() == null ) {
                fSchemaGrammar.setAttributeDeclRegistry(fAttributeDeclRegistry);
            }
            else {
                fAttributeDeclRegistry = fSchemaGrammar.getAttributeDeclRegistry();
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
                fNamespacesScope.setNamespaceForPrefix( StringPool.EMPTY_STRING,
                                                        fStringPool.addSymbol(attValue) );
                seenXMLNS = true;
            }

        }
        if (!seenXMLNS && fTargetNSURIString.length() == 0 ) {
            fNamespacesScope.setNamespaceForPrefix( StringPool.EMPTY_STRING,
                                                    StringPool.EMPTY_STRING);
        }

        fElementDefaultQualified =
            root.getAttribute(SchemaSymbols.ATT_ELEMENTFORMDEFAULT).equals(SchemaSymbols.ATTVAL_QUALIFIED);
        fAttributeDefaultQualified =
            root.getAttribute(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT).equals(SchemaSymbols.ATTVAL_QUALIFIED);
        Attr blockAttr = root.getAttributeNode(SchemaSymbols.ATT_BLOCKDEFAULT);
        if (blockAttr == null)
            fBlockDefault = 0;
        else
            fBlockDefault =
                parseBlockSet(blockAttr.getValue());
        Attr finalAttr = root.getAttributeNode(SchemaSymbols.ATT_FINALDEFAULT);
        if (finalAttr == null)
            fFinalDefault = 0;
        else
            fFinalDefault =
                parseFinalSet(finalAttr.getValue());

        if (fTargetNSURI == StringPool.EMPTY_STRING) {
        }




        extractTopLevel3Components(root);

        Element child = XUtil.getFirstChildElement(root);
        for (; child != null;
            child = XUtil.getNextSiblingElement(child)) {

            String name = child.getLocalName();
            if (name.equals(SchemaSymbols.ELT_ANNOTATION) ) {
                traverseAnnotationDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_INCLUDE)) {
                fNamespacesScope.increaseDepth();
                traverseInclude(child);
                fNamespacesScope.decreaseDepth();
            } else if (name.equals(SchemaSymbols.ELT_IMPORT)) {
                traverseImport(child);
            } else if (name.equals(SchemaSymbols.ELT_REDEFINE)) {
                traverseRedefine(child);
            } else
                break;
        }

        for (; child != null;
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
                traverseAttributeGroupDecl(child, null, null);
            } else if (name.equals( SchemaSymbols.ELT_ATTRIBUTE ) ) {
                traverseAttributeDecl( child, null, false );
            } else if (name.equals(SchemaSymbols.ELT_GROUP)) {
                traverseGroupDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_NOTATION)) {
            } else {
                reportGenericSchemaError("error in content of <schema> element information item");
            }

        Enumeration elementIndexes = fIdentityConstraints.keys();
        while (elementIndexes.hasMoreElements()) {
            Integer elementIndexObj = (Integer)elementIndexes.nextElement();
            if (DEBUG_IC_DATATYPES) {
                System.out.println("<ICD>: traversing identity constraints for element: "+elementIndexObj);
            }
            Vector identityConstraints = (Vector)fIdentityConstraints.get(elementIndexObj);
            if (identityConstraints != null) {
                int elementIndex = elementIndexObj.intValue();
                traverseIdentityNameConstraintsFor(elementIndex, identityConstraints);
            }
        }
        elementIndexes = fIdentityConstraints.keys();
        while (elementIndexes.hasMoreElements()) {
            Integer elementIndexObj = (Integer)elementIndexes.nextElement();
            if (DEBUG_IC_DATATYPES) {
                System.out.println("<ICD>: traversing identity constraints for element: "+elementIndexObj);
            }
            Vector identityConstraints = (Vector)fIdentityConstraints.get(elementIndexObj);
            if (identityConstraints != null) {
                int elementIndex = elementIndexObj.intValue();
                traverseIdentityRefConstraintsFor(elementIndex, identityConstraints);
            }
        }


        if (fFullConstraintChecking) {

            for (int j = 0; j < fTopLevelElementsRefdFromGroup.size(); j+=2) {
               QName eltName = (QName)fTopLevelElementsRefdFromGroup.elementAt(j);
               int groupScope = ((Integer)fTopLevelElementsRefdFromGroup.elementAt(j+1)).intValue();
               checkConsistentElements(eltName, groupScope);
            }

            int count = fComplexTypeRegistry.size();
            Enumeration enum = fComplexTypeRegistry.elements();

            ComplexTypeInfo typeInfo,baseTypeInfo;
            while (enum.hasMoreElements ()) {
               typeInfo = (TraverseSchema.ComplexTypeInfo)enum.nextElement();
               baseTypeInfo = typeInfo.baseComplexTypeInfo;

               if (typeInfo.derivedBy == SchemaSymbols.RESTRICTION &&
                   baseTypeInfo!=null &&
                   typeInfo.contentSpecHandle>-1) {
                 try {
                    checkParticleDerivationOK(typeInfo.contentSpecHandle,
                         typeInfo.scopeDefined, baseTypeInfo.contentSpecHandle,
                         baseTypeInfo.scopeDefined,baseTypeInfo);
                 }
                 catch (ParticleRecoverableError e) {
                    String message = e.getMessage();
                    reportGenericSchemaError("ComplexType '" + typeInfo.typeName + "': " + message);
                 }
               }
            }

        }



    private void extractTopLevel3Components(Element root) throws Exception {

        for (Element child = XUtil.getFirstChildElement(root); child != null;
            child = XUtil.getNextSiblingElement(child)) {

            String name = child.getLocalName();
            String compName = child.getAttribute(SchemaSymbols.ATT_NAME);
            if (name.equals(SchemaSymbols.ELT_ELEMENT)) {
                if (fSchemaGrammar.topLevelElemDecls.get(compName) != null) {
                   reportGenericSchemaError("sch-props-correct: Duplicate declaration for an element " +
                                             compName);
                }
                else {
                    fSchemaGrammar.topLevelElemDecls.put(compName, child);
                }
            }
            else if (name.equals(SchemaSymbols.ELT_SIMPLETYPE) ||
                     name.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                if (fSchemaGrammar.topLevelTypeDecls.get(compName) != null) {
                   reportGenericSchemaError("sch-props-correct: Duplicate declaration for a type " +
                                             compName);
                }
                else {
                    fSchemaGrammar.topLevelTypeDecls.put(compName, child);
                }
            }
            else if (name.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                if (fSchemaGrammar.topLevelAttrGrpDecls.get(compName) != null) {
                   reportGenericSchemaError("sch-props-correct: Duplicate declaration for an attribute group " +
                                             compName);
                }
                else {
                fSchemaGrammar.topLevelAttrGrpDecls.put(compName, child);
                }
            } else if (name.equals( SchemaSymbols.ELT_ATTRIBUTE ) ) {
                if (fSchemaGrammar.topLevelAttrGrpDecls.get(compName) != null) {
                   reportGenericSchemaError("sch-props-correct: Duplicate declaration for an attribute " +
                                             compName);
                }
                else {
                    fSchemaGrammar.topLevelAttrGrpDecls.put(compName, child);
                }
            } else if ( name.equals(SchemaSymbols.ELT_GROUP) ) {
                if (fSchemaGrammar.topLevelGroupDecls.get(compName) != null){
                   reportGenericSchemaError("sch-props-correct: Duplicate declaration for a group " +
                                             compName);
                }
                else {
                fSchemaGrammar.topLevelGroupDecls.put(compName, child);
                }
            } else if ( name.equals(SchemaSymbols.ELT_NOTATION) ) {
                if (fSchemaGrammar.topLevelNotationDecls.get(compName) != null) {
                   reportGenericSchemaError("sch-props-correct: Duplicate declaration for a notation " +
                                             compName);
                }
                else {
                fSchemaGrammar.topLevelNotationDecls.put(compName, child);
            }
            }
    }

    private void checkConsistentElements(QName eltName, int scope) throws Exception {


       fTempElementDecl.clear();
       int topLevelElementNdx = fSchemaGrammar.getElementDeclIndex(eltName, TOP_LEVEL_SCOPE);
       if (topLevelElementNdx < 0)
           return;

       fSchemaGrammar.getElementDecl(topLevelElementNdx, fTempElementDecl);
       DatatypeValidator edv = fTempElementDecl.datatypeValidator;
       ComplexTypeInfo eTypeInfo = fSchemaGrammar.getElementComplexTypeInfo(topLevelElementNdx);
       int existingEltNdx = fSchemaGrammar.getElementDeclIndex(eltName.uri,
                                              eltName.localpart,scope);
       if (existingEltNdx > -1) {
          if (!checkDuplicateElementTypes(existingEltNdx,eTypeInfo,edv))

              reportGenericSchemaError("duplicate element decl in the same scope with different types : " +
                                        fStringPool.toString(eltName.localpart));
       }

       Vector substitutableNames = fSchemaGrammar.getElementDeclAllSubstitutionGroupQNames(topLevelElementNdx, fGrammarResolver, fStringPool);

       for (int i = 0; i < substitutableNames.size(); i++) {
          SchemaGrammar.OneSubGroup subGroup = (SchemaGrammar.OneSubGroup)substitutableNames.elementAt(i);
          QName substName = subGroup.name;
          int substEltNdx = subGroup.eleIndex;

          int localEltNdx = fSchemaGrammar.getElementDeclIndex(substName, scope);
          if (localEltNdx > -1) {
             fSchemaGrammar.getElementDecl(localEltNdx, fTempElementDecl);
             edv = fTempElementDecl.datatypeValidator;
             eTypeInfo = fSchemaGrammar.getElementComplexTypeInfo(localEltNdx);
             if (!checkDuplicateElementTypes(substEltNdx,eTypeInfo,edv))
                 reportGenericSchemaError("duplicate element decl in the same scope with different types : " +
                  fStringPool.toString(substName.localpart));
          }
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
        int scope = GeneralAttrCheck.ELE_CONTEXT_GLOBAL;
        Hashtable attrValues = generalCheck(includeDecl, scope);
        checkContent(includeDecl, XUtil.getFirstChildElement(includeDecl), true);

        Attr locationAttr = includeDecl.getAttributeNode(SchemaSymbols.ATT_SCHEMALOCATION);
        if (locationAttr == null) {
            reportGenericSchemaError("a schemaLocation attribute must be specified on an <include> element");
            return;
        }
        String location = locationAttr.getValue();
        InputSource source = null;

        if ( (fEntityResolver != null) &&
             !(fEntityResolver.getClass().getName().equals("org.apache.xerces.validators.common.XMLValidator$Resolver")) ) {
            source = fEntityResolver.resolveEntity("", location);
        }

        if (source == null) {
            location = expandSystemId(location, fCurrentSchemaURL);
            source = new InputSource(location);
        }
        String pubId = "";
        String sysId = "";
        if (source.getPublicId () != null)
            pubId = source.getPublicId ();
        if (source.getSystemId () != null)
            sysId = source.getSystemId ();

        if(pubId.length() != 0 || sysId.length() != 0)
            location = pubId+sysId;

        if (fIncludeLocations.contains((Object)location)) {
            return;
        }
        fIncludeLocations.addElement((Object)location);

        DOMParser parser = new IgnoreWhitespaceParser();
        parser.setEntityResolver( (fEntityResolver != null)? (fEntityResolver):(new Resolver()) );
        parser.setErrorHandler(  new ErrorHandler()
            {
                public void error(SAXParseException ex) throws SAXException {
                    StringBuffer str = new StringBuffer();
                    String systemId_ = ex.getSystemId();
                    if (systemId_ != null) {
                        int index = systemId_.lastIndexOf('/');
                        if (index != -1)
                            systemId_ = systemId_.substring(index + 1);
                        str.append(systemId_);
                    }
                    str.append(':').append(ex.getLineNumber()).append(':').append(ex.getColumnNumber());
                    String message = ex.getMessage();
                    if(message.toLowerCase().trim().endsWith("not found.")) {
                        System.err.println("[Warning] "+
                               str.toString()+": "+ message);
                        System.err.println("[Error] "+
                               str.toString()+":"+message);
                        throw ex;
                    }
                }
            });

        try {
        }catch(  org.xml.sax.SAXNotRecognizedException e ) {
            e.printStackTrace();
        }catch( org.xml.sax.SAXNotSupportedException e ) {
            e.printStackTrace();
        }

        try {
            parser.parse( source );
        }catch( IOException e ) {
        }catch( SAXException e ) {
        }

        Element root = null;
        if (document != null) {
            root = document.getDocumentElement();
        }

        if (root != null) {
            String targetNSURI = getTargetNamespaceString(root);
            if (targetNSURI.length() > 0 && !targetNSURI.equals(fTargetNSURIString) ) {
                reportGenericSchemaError("included schema '"+location+"' has a different targetNameSpace '"
                                         +targetNSURI+"'");
            }
            else {
                if (fSchemaInfoListRoot == null) {
                    fSchemaInfoListRoot = new SchemaInfo(fElementDefaultQualified, fAttributeDefaultQualified,
                        fBlockDefault, fFinalDefault,
                        fCurrentSchemaURL, fSchemaRootElement,
                        fNamespacesScope, null, null);
                    fCurrentSchemaInfo = fSchemaInfoListRoot;
                }
                fSchemaRootElement = root;
                fSchemaURLStack.push(fCurrentSchemaURL);
                fCurrentSchemaURL = location;
                traverseIncludedSchemaHeader(root);
                if((fTargetNSURIString.length() != 0) && (root.getAttributeNode("xmlns") == null)) {
                    fNamespacesScope.setNamespaceForPrefix(StringPool.EMPTY_STRING, fTargetNSURI);
                }
                fCurrentSchemaInfo = new SchemaInfo(fElementDefaultQualified, fAttributeDefaultQualified,
                        fBlockDefault, fFinalDefault,
                        fCurrentSchemaURL, fSchemaRootElement,
                        fNamespacesScope, fCurrentSchemaInfo.getNext(), fCurrentSchemaInfo);
                (fCurrentSchemaInfo.getPrev()).setNext(fCurrentSchemaInfo);
                traverseIncludedSchema(root);
                fCurrentSchemaInfo = fCurrentSchemaInfo.getPrev();
                fCurrentSchemaInfo.restore();
                fCurrentSchemaURL = (String)fSchemaURLStack.pop();
            }

        }

    }

    private void traverseIncludedSchemaHeader(Element root) throws Exception {
        int scope = GeneralAttrCheck.ELE_CONTEXT_GLOBAL;
        Hashtable attrValues = generalCheck(root, scope);

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
                fNamespacesScope.setNamespaceForPrefix( StringPool.EMPTY_STRING,
                                                        fStringPool.addSymbol(attValue) );
                seenXMLNS = true;
            }

        }
        if (!seenXMLNS && fTargetNSURIString.length() == 0 ) {
            fNamespacesScope.setNamespaceForPrefix( StringPool.EMPTY_STRING,
                                                    StringPool.EMPTY_STRING);
        }

        fElementDefaultQualified =
            root.getAttribute(SchemaSymbols.ATT_ELEMENTFORMDEFAULT).equals(SchemaSymbols.ATTVAL_QUALIFIED);
        fAttributeDefaultQualified =
            root.getAttribute(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT).equals(SchemaSymbols.ATTVAL_QUALIFIED);
        Attr blockAttr = root.getAttributeNode(SchemaSymbols.ATT_BLOCKDEFAULT);
        if (blockAttr == null)
            fBlockDefault = 0;
        else
            fBlockDefault =
                parseBlockSet(blockAttr.getValue());
        Attr finalAttr = root.getAttributeNode(SchemaSymbols.ATT_FINALDEFAULT);
        if (finalAttr == null)
            fFinalDefault = 0;
        else
            fFinalDefault =
                parseFinalSet(finalAttr.getValue());

        if (fTargetNSURI == StringPool.EMPTY_STRING) {
            fElementDefaultQualified = true;
        }


    private void traverseIncludedSchema(Element root) throws Exception {
        int scope = GeneralAttrCheck.ELE_CONTEXT_GLOBAL;
        Hashtable attrValues = generalCheck(root, scope);

        extractTopLevel3Components(root);

        Element child = XUtil.getFirstChildElement(root);
        for (; child != null;
            child = XUtil.getNextSiblingElement(child)) {

            String name = child.getLocalName();

            if (name.equals(SchemaSymbols.ELT_ANNOTATION) ) {
                traverseAnnotationDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_INCLUDE)) {
                fNamespacesScope.increaseDepth();
                traverseInclude(child);
                fNamespacesScope.decreaseDepth();
            } else if (name.equals(SchemaSymbols.ELT_IMPORT)) {
                traverseImport(child);
            } else if (name.equals(SchemaSymbols.ELT_REDEFINE)) {
                traverseRedefine(child);
            } else
                break;
        }

        for (; child != null;
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
                if(fRedefineAttributeGroupMap != null) {
                    String dName = child.getAttribute(SchemaSymbols.ATT_NAME);
                    String bName = (String)fRedefineAttributeGroupMap.get(dName);
                    if(bName != null) {
                        child.setAttribute(SchemaSymbols.ATT_NAME, bName);
                        fSchemaGrammar.topLevelAttrGrpDecls.remove(dName);
                        ComplexTypeInfo typeInfo = new ComplexTypeInfo();
                        int templateElementNameIndex = fStringPool.addSymbol("$"+bName);
                        int typeNameIndex = fStringPool.addSymbol("%"+bName);
                        typeInfo.scopeDefined = -2;
                        typeInfo.contentSpecHandle = -1;
                        typeInfo.contentType = XMLElementDecl.TYPE_SIMPLE;
                        typeInfo.datatypeValidator = null;
                        typeInfo.templateElementIndex = fSchemaGrammar.addElementDecl(
                            new QName(-1, templateElementNameIndex,typeNameIndex,fTargetNSURI),
                            (fTargetNSURI==StringPool.EMPTY_STRING) ? StringPool.EMPTY_STRING : -2, typeInfo.scopeDefined,
                            typeInfo.contentType,
                            typeInfo.contentSpecHandle, -1, typeInfo.datatypeValidator);

                        Vector anyAttDecls = new Vector();
                        traverseAttributeGroupDecl(child, typeInfo, anyAttDecls);
                        typeInfo.attlistHead = fSchemaGrammar.getFirstAttributeDeclIndex(
                                typeInfo.templateElementIndex);
                        fRedefineAttributeGroupMap.put(dName, new Object []{typeInfo, fSchemaGrammar, anyAttDecls});
                        continue;
                    }
                }
                traverseAttributeGroupDecl(child, null, null);
            } else if (name.equals( SchemaSymbols.ELT_ATTRIBUTE ) ) {
                traverseAttributeDecl( child, null , false);
            } else if (name.equals(SchemaSymbols.ELT_GROUP)) {
                String dName = child.getAttribute(SchemaSymbols.ATT_NAME);
                if(fGroupNameRegistry.get(fTargetNSURIString + ","+dName) == null) {
                    traverseGroupDecl(child);
                    continue;
                }
                try {
                    GroupInfo gi = (GroupInfo)fGroupNameRegistry.get(fTargetNSURIString + ","+dName);
                    continue;
                } catch (ClassCastException c) {
                    String s = (String)fGroupNameRegistry.get(fTargetNSURIString + ","+dName);
                };
                String bName = (String)fGroupNameRegistry.get(fTargetNSURIString +"," + dName);
                if(bName != null) {
                    child.setAttribute(SchemaSymbols.ATT_NAME, bName);
                }
                traverseGroupDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_NOTATION)) {
                traverseNotationDecl(child);
            } else {
                reportGenericSchemaError("error in content of included <schema> element information item");
            }

    }

    private void openRedefinedSchema(Element redefineDecl, SchemaInfo store) throws Exception {
        Attr locationAttr = redefineDecl.getAttributeNode(SchemaSymbols.ATT_SCHEMALOCATION);
        if (locationAttr == null) {
            fRedefineSucceeded = false;
            reportGenericSchemaError("a schemaLocation attribute must be specified on a <redefine> element");
            return;
        }
        String location = locationAttr.getValue();

        InputSource source = null;
        if (fEntityResolver != null) {
            source = fEntityResolver.resolveEntity("", location);
        }

        if (source == null) {
            location = expandSystemId(location, fCurrentSchemaURL);
            source = new InputSource(location);
        }
        String pubId = "";
        String sysId = "";
        if (source.getPublicId () != null)
            pubId = source.getPublicId ();
        if (source.getSystemId () != null)
            sysId = source.getSystemId ();

        if(pubId.length() != 0 || sysId.length() != 0)
            location += pubId+sysId;

        if(source.getSystemId().equals(fCurrentSchemaURL)) {
            reportGenericSchemaError("src-redefine.2:  a schema cannot redefine itself");
            fRedefineSucceeded = false;
            return;
        }
        if (fRedefineLocations.get((Object)location) != null) {
            fCurrentSchemaInfo = (SchemaInfo)(fRedefineLocations.get((Object)location));
            fCurrentSchemaInfo.restore();
            return;
        }

        DOMParser parser = new IgnoreWhitespaceParser();
        parser.setEntityResolver( (fEntityResolver != null)? (fEntityResolver):(new Resolver()) );
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

            fRedefineSucceeded = false;
            return;
        }


        String redefinedTargetNSURIString = getTargetNamespaceString(root);
        if (redefinedTargetNSURIString.length() > 0 && !redefinedTargetNSURIString.equals(fTargetNSURIString) ) {
            fRedefineSucceeded = false;
            reportGenericSchemaError("redefined schema '"+location+"' has a different targetNameSpace '"
                                     +redefinedTargetNSURIString+"' from the original schema");
        }
        else {
            fSchemaRootElement = root;
            fCurrentSchemaURL = location;
            fNamespacesScope = new NamespacesScope(this);
            if((redefinedTargetNSURIString.length() == 0) && (root.getAttributeNode("xmlns") == null)) {
                fNamespacesScope.setNamespaceForPrefix(StringPool.EMPTY_STRING, fTargetNSURI);
            } else {
            }
            traverseIncludedSchemaHeader(root);
            store.setNext(new SchemaInfo(fElementDefaultQualified, fAttributeDefaultQualified,
                    fBlockDefault, fFinalDefault,
                    fCurrentSchemaURL, fSchemaRootElement,
                    fNamespacesScope, null, store));
            (store.getNext()).setPrev(store);
            fCurrentSchemaInfo = store.getNext();
            fRedefineLocations.put((Object)location, store.getNext());

    /****
     * <redefine
       *        schemaLocation = uriReference
       *        {any attributes with non-schema namespace . . .}>
       *        Content: (annotation | (
     *            attributeGroup | complexType | group | simpleType))*
     *    </redefine>
     */
    private void traverseRedefine(Element redefineDecl) throws Exception {
        int scope = GeneralAttrCheck.ELE_CONTEXT_GLOBAL;
        Hashtable attrValues = generalCheck(redefineDecl, scope);

        fRedefineAttributeGroupMap = new Hashtable();
        NamespacesScope saveNSScope = (NamespacesScope)fNamespacesScope.clone();

        if (fSchemaInfoListRoot == null) {
            fSchemaInfoListRoot = new SchemaInfo(fElementDefaultQualified, fAttributeDefaultQualified,
                    fBlockDefault, fFinalDefault,
                    fCurrentSchemaURL, fSchemaRootElement,
                    fNamespacesScope, null, null);
            openRedefinedSchema(redefineDecl, fSchemaInfoListRoot);
            if(!fRedefineSucceeded)
                return;
            fCurrentSchemaInfo = fSchemaInfoListRoot.getNext();
            fNamespacesScope = (NamespacesScope)saveNSScope.clone();
            renameRedefinedComponents(redefineDecl,fSchemaInfoListRoot.getNext().getRoot(), fSchemaInfoListRoot.getNext());
        } else {
            SchemaInfo curr = fSchemaInfoListRoot;
            for(; curr.getNext() != null; curr = curr.getNext());
            fCurrentSchemaInfo = curr;
            fCurrentSchemaInfo.restore();
            openRedefinedSchema(redefineDecl, fCurrentSchemaInfo);
            if(!fRedefineSucceeded)
                return;
            fNamespacesScope = (NamespacesScope)saveNSScope.clone();
            renameRedefinedComponents(redefineDecl,fCurrentSchemaInfo.getRoot(), fCurrentSchemaInfo);
        }

        fCurrentSchemaInfo.restore();
        traverseIncludedSchema(fSchemaRootElement);
        fNamespacesScope = (NamespacesScope)saveNSScope.clone();
        for (Element child = XUtil.getFirstChildElement(redefineDecl); child != null;
                child = XUtil.getNextSiblingElement(child)) {
            String name = child.getLocalName();

            if (name.equals(SchemaSymbols.ELT_ANNOTATION) ) {
                traverseAnnotationDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_SIMPLETYPE )) {
                traverseSimpleTypeDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_COMPLEXTYPE )) {
                traverseComplexTypeDecl(child);
            } else if (name.equals(SchemaSymbols.ELT_GROUP)) {
                String dName = child.getAttribute(SchemaSymbols.ATT_NAME);
                if(fGroupNameRegistry.get(fTargetNSURIString +","+ dName) == null ||
                        ((fRestrictedRedefinedGroupRegistry.get(fTargetNSURIString+","+dName) != null) &&
                    traverseGroupDecl(child);
                    continue;
                }
                traverseGroupDecl(child);
                GroupInfo bGIObj = null;
                try {
                    bGIObj = (GroupInfo)fGroupNameRegistry.get(fTargetNSURIString +","+ dName+redefIdentifier);
                } catch(ClassCastException c) {
                    reportGenericSchemaError("src-redefine.6.2:  a <group> within a <redefine> must either have a ref to a <group> with the same name or must restrict such an <group>");
                    continue;
                }
                    int bCSIndex = bGIObj.contentSpecIndex;
                    GroupInfo dGIObj;
                    try {
                        dGIObj = (GroupInfo)fGroupNameRegistry.get(fTargetNSURIString+","+dName);
                    } catch (ClassCastException c) {
                        continue;
                    }
                        continue;
                    int dCSIndex = dGIObj.contentSpecIndex;
                    try {
                      checkParticleDerivationOK(dCSIndex, -1, bCSIndex, -1, null);
                    }
                    catch (ParticleRecoverableError e) {
                      reportGenericSchemaError(e.getMessage());
                    }
                } else
                    reportGenericSchemaError("src-redefine.6.2:  a <group> within a <redefine> must either have a ref to a <group> with the same name or must restrict such an <group>");
            } else if (name.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                if(fRedefineAttributeGroupMap != null) {
                    String dName = child.getAttribute(SchemaSymbols.ATT_NAME);
                    Object [] bAttGrpStore = null;
                    try {
                        bAttGrpStore = (Object [])fRedefineAttributeGroupMap.get(dName);
                    } catch(ClassCastException c) {
                        reportGenericSchemaError("src-redefine.7.2:  an <attributeGroup> within a <redefine> must either have a ref to an <attributeGroup> with the same name or must restrict such an <attributeGroup>");
                        continue;
                    }
                        ComplexTypeInfo bTypeInfo = (ComplexTypeInfo)bAttGrpStore[0];
                        SchemaGrammar bSchemaGrammar = (SchemaGrammar)bAttGrpStore[1];
                        Vector bAnyAttDecls = (Vector)bAttGrpStore[2];
                        XMLAttributeDecl bAnyAttDecl =
                            (bAnyAttDecls.size()>0 )? (XMLAttributeDecl)bAnyAttDecls.elementAt(0):null;
                        ComplexTypeInfo dTypeInfo = new ComplexTypeInfo();
                        int templateElementNameIndex = fStringPool.addSymbol("$"+dName);
                        int dTypeNameIndex = fStringPool.addSymbol("%"+dName);
                        dTypeInfo.scopeDefined = -2;
                        dTypeInfo.contentSpecHandle = -1;
                        dTypeInfo.contentType = XMLElementDecl.TYPE_SIMPLE;
                        dTypeInfo.datatypeValidator = null;
                        dTypeInfo.templateElementIndex = fSchemaGrammar.addElementDecl(
                            new QName(-1, templateElementNameIndex,dTypeNameIndex,fTargetNSURI),
                            (fTargetNSURI==StringPool.EMPTY_STRING) ? StringPool.EMPTY_STRING : -2, dTypeInfo.scopeDefined,
                            dTypeInfo.contentType,
                            dTypeInfo.contentSpecHandle, -1, dTypeInfo.datatypeValidator);

                        Vector dAnyAttDecls = new Vector();
                        XMLAttributeDecl dAnyAttDecl =
                            (dAnyAttDecls.size()>0 )? (XMLAttributeDecl)dAnyAttDecls.elementAt(0):null;
                        traverseAttributeGroupDecl(child, dTypeInfo, dAnyAttDecls);
                        dTypeInfo.attlistHead = fSchemaGrammar.getFirstAttributeDeclIndex(
                                dTypeInfo.templateElementIndex);
                        try {
                            checkAttributesDerivationOKRestriction(dTypeInfo.attlistHead,fSchemaGrammar,
                                dAnyAttDecl,bTypeInfo.attlistHead,bSchemaGrammar,bAnyAttDecl);
                        }
                        catch (ComplexTypeRecoverableError e) {
                            String message = e.getMessage();
                            reportGenericSchemaError("src-redefine.7.2:  redefinition failed because of " + message);
                        }
                        continue;
                    }
                }
                traverseAttributeGroupDecl(child, null, null);

        fCurrentSchemaInfo = fCurrentSchemaInfo.getPrev();
        fCurrentSchemaInfo.restore();

    private void renameRedefinedComponents(Element redefineDecl, Element schemaToRedefine, SchemaInfo currSchemaInfo) throws Exception {
        for (Element child = XUtil.getFirstChildElement(redefineDecl);
                child != null;
                child = XUtil.getNextSiblingElement(child)) {
            String name = child.getLocalName();
            if (name.equals(SchemaSymbols.ELT_ANNOTATION) )
                continue;
            else if (name.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                String typeName = child.getAttribute( SchemaSymbols.ATT_NAME );
                if(fTraversedRedefineElements.contains(typeName))
                    continue;
                if(validateRedefineNameChange(SchemaSymbols.ELT_SIMPLETYPE, typeName, typeName+redefIdentifier, child)) {
                    fixRedefinedSchema(SchemaSymbols.ELT_SIMPLETYPE,
                        typeName, typeName+redefIdentifier,
                        schemaToRedefine, currSchemaInfo);
                }
            } else if (name.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                String typeName = child.getAttribute( SchemaSymbols.ATT_NAME );
                if(fTraversedRedefineElements.contains(typeName))
                    continue;
                if(validateRedefineNameChange(SchemaSymbols.ELT_COMPLEXTYPE, typeName, typeName+redefIdentifier, child)) {
                    fixRedefinedSchema(SchemaSymbols.ELT_COMPLEXTYPE,
                        typeName, typeName+redefIdentifier,
                        schemaToRedefine, currSchemaInfo);
                }
            } else if (name.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                String baseName = child.getAttribute( SchemaSymbols.ATT_NAME );
                if(fTraversedRedefineElements.contains(baseName))
                    continue;
                if(validateRedefineNameChange(SchemaSymbols.ELT_ATTRIBUTEGROUP, baseName, baseName+redefIdentifier, child)) {
                    fixRedefinedSchema(SchemaSymbols.ELT_ATTRIBUTEGROUP,
                        baseName, baseName+redefIdentifier,
                        schemaToRedefine, currSchemaInfo);
                }
            } else if (name.equals(SchemaSymbols.ELT_GROUP)) {
                String baseName = child.getAttribute( SchemaSymbols.ATT_NAME );
                if(fTraversedRedefineElements.contains(baseName))
                    continue;
                if(validateRedefineNameChange(SchemaSymbols.ELT_GROUP, baseName, baseName+redefIdentifier, child)) {
                    fixRedefinedSchema(SchemaSymbols.ELT_GROUP,
                        baseName, baseName+redefIdentifier,
                        schemaToRedefine, currSchemaInfo);
                }
            } else {
                fRedefineSucceeded = false;
                reportGenericSchemaError("invalid top-level content for <redefine>");
                return;
            }

    private int changeRedefineGroup(QName originalName, String elementSought, String newName, Element curr) throws Exception {
        int result = 0;
        for (Element child = XUtil.getFirstChildElement(curr);
                child != null; child = XUtil.getNextSiblingElement(child)) {
            String name = child.getLocalName();
            if (!name.equals(elementSought))
                result += changeRedefineGroup(originalName, elementSought, newName, child);
            else {
                String ref = child.getAttribute( SchemaSymbols.ATT_REF );
                if (ref.length() != 0) {
                    String prefix = "";
                    String localpart = ref;
                    int colonptr = ref.indexOf(":");
                    if ( colonptr > 0) {
                        prefix = ref.substring(0,colonptr);
                        localpart = ref.substring(colonptr+1);
                    }
                    String uriStr = resolvePrefixToURI(prefix);
                    if(originalName.equals(new QName(-1, fStringPool.addSymbol(localpart), fStringPool.addSymbol(localpart), fStringPool.addSymbol(uriStr)))) {
                        if(prefix.length() == 0)
                            child.setAttribute(SchemaSymbols.ATT_REF, newName);
                        else
                            child.setAttribute(SchemaSymbols.ATT_REF, prefix + ":" + newName);
                        result++;
                        if(elementSought.equals(SchemaSymbols.ELT_GROUP)) {
                            String minOccurs = child.getAttribute( SchemaSymbols.ATT_MINOCCURS );
                            String maxOccurs = child.getAttribute( SchemaSymbols.ATT_MAXOCCURS );
                            if(!((maxOccurs.length() == 0 || maxOccurs.equals("1"))
                                    && (minOccurs.length() == 0 || minOccurs.equals("1")))) {
                                reportGenericSchemaError("src-redefine.6.1.2:  the group " + ref + " which contains a reference to a group being redefined must have minOccurs = maxOccurs = 1");
                            }
                        }
                    }
            }
        }
        return result;

    private void fixRedefinedSchema(String eltLocalname, String oldName, String newName, Element schemaToRedefine,
                SchemaInfo currSchema) throws Exception {

        boolean foundIt = false;
        for (Element child = XUtil.getFirstChildElement(schemaToRedefine);
                child != null;
                child = XUtil.getNextSiblingElement(child)) {
            String name = child.getLocalName();
                for (Element redefChild = XUtil.getFirstChildElement(child);
                        redefChild != null;
                        redefChild = XUtil.getNextSiblingElement(redefChild)) {
                    String redefName = redefChild.getLocalName();
                    if (redefName.equals(eltLocalname) ) {
                        String infoItemName = redefChild.getAttribute( SchemaSymbols.ATT_NAME );
                        if(!infoItemName.equals(oldName))
                            continue;
                            foundIt = true;
                            openRedefinedSchema(child, currSchema);
                            if(!fRedefineSucceeded)
                                return;
                            NamespacesScope saveNSS = (NamespacesScope)fNamespacesScope.clone();
                            currSchema.restore();
                            if (validateRedefineNameChange(eltLocalname, oldName, newName+redefIdentifier, redefChild) &&
                                    (currSchema.getNext() != null)) {
                                currSchema.getNext().restore();
                                fixRedefinedSchema(eltLocalname, oldName, newName+redefIdentifier, fSchemaRootElement, currSchema.getNext());
                            }
                            fNamespacesScope = saveNSS;
                            redefChild.setAttribute( SchemaSymbols.ATT_NAME, newName );
                            fTraversedRedefineElements.addElement(newName);
                            currSchema.restore();
                            fCurrentSchemaInfo = currSchema;
                            break;
                        }
                    }
                if (foundIt) break;
            }
            else if (name.equals(eltLocalname) ) {
                String infoItemName = child.getAttribute( SchemaSymbols.ATT_NAME );
                if(!infoItemName.equals(oldName))
                    continue;
                    foundIt = true;
                    child.setAttribute( SchemaSymbols.ATT_NAME, newName );
                    break;
                }
            }
        if(!foundIt) {
            fRedefineSucceeded = false;
            reportGenericSchemaError("could not find a declaration in the schema to be redefined corresponding to " + oldName);
        }

    private boolean validateRedefineNameChange(String eltLocalname, String oldName, String newName, Element child) throws Exception {
        if (eltLocalname.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            QName processedTypeName = new QName(-1, fStringPool.addSymbol(oldName), fStringPool.addSymbol(oldName), fTargetNSURI);
            Element grandKid = XUtil.getFirstChildElement(child);
            if (grandKid == null) {
                fRedefineSucceeded = false;
                reportGenericSchemaError("a simpleType child of a <redefine> must have a restriction element as a child");
            } else {
                String grandKidName = grandKid.getLocalName();
                if(grandKidName.equals(SchemaSymbols.ELT_ANNOTATION)) {
                    grandKid = XUtil.getNextSiblingElement(grandKid);
                    grandKidName = grandKid.getLocalName();
                }
                if (grandKid == null) {
                    fRedefineSucceeded = false;
                    reportGenericSchemaError("a simpleType child of a <redefine> must have a restriction element as a child");
                } else if(!grandKidName.equals(SchemaSymbols.ELT_RESTRICTION)) {
                    fRedefineSucceeded = false;
                    reportGenericSchemaError("a simpleType child of a <redefine> must have a restriction element as a child");
                } else {
                    String derivedBase = grandKid.getAttribute( SchemaSymbols.ATT_BASE );
                    QName processedDerivedBase = parseBase(derivedBase);
                    if(!processedTypeName.equals(processedDerivedBase)) {
                        fRedefineSucceeded = false;
                        reportGenericSchemaError("the base attribute of the restriction child of a simpleType child of a redefine must have the same value as the simpleType's type attribute");
                    } else {
                        String prefix = "";
                        int colonptr = derivedBase.indexOf(":");
                        if ( colonptr > 0)
                            prefix = derivedBase.substring(0,colonptr) + ":";
                        grandKid.setAttribute( SchemaSymbols.ATT_BASE, prefix + newName );
                        return true;
                    }
                }
            }
        } else if (eltLocalname.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
            QName processedTypeName = new QName(-1, fStringPool.addSymbol(oldName), fStringPool.addSymbol(oldName), fTargetNSURI);
            Element grandKid = XUtil.getFirstChildElement(child);
            if (grandKid == null) {
                fRedefineSucceeded = false;
                reportGenericSchemaError("a complexType child of a <redefine> must have a restriction or extension element as a grandchild");
            } else {
                if(grandKid.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION)) {
                    grandKid = XUtil.getNextSiblingElement(grandKid);
                }
                if (grandKid == null) {
                    fRedefineSucceeded = false;
                    reportGenericSchemaError("a complexType child of a <redefine> must have a restriction or extension element as a grandchild");
                } else {
                    Element greatGrandKid = XUtil.getFirstChildElement(grandKid);
                    if (greatGrandKid == null) {
                        fRedefineSucceeded = false;
                        reportGenericSchemaError("a complexType child of a <redefine> must have a restriction or extension element as a grandchild");
                    } else {
                        String greatGrandKidName = greatGrandKid.getLocalName();
                        if(greatGrandKidName.equals(SchemaSymbols.ELT_ANNOTATION)) {
                            greatGrandKid = XUtil.getNextSiblingElement(greatGrandKid);
                            greatGrandKidName = greatGrandKid.getLocalName();
                        }
                        if (greatGrandKid == null) {
                            fRedefineSucceeded = false;
                            reportGenericSchemaError("a complexType child of a <redefine> must have a restriction or extension element as a grandchild");
                        } else if(!greatGrandKidName.equals(SchemaSymbols.ELT_RESTRICTION) &&
                                !greatGrandKidName.equals(SchemaSymbols.ELT_EXTENSION)) {
                            fRedefineSucceeded = false;
                            reportGenericSchemaError("a complexType child of a <redefine> must have a restriction or extension element as a grandchild");
                        } else {
                            String derivedBase = greatGrandKid.getAttribute( SchemaSymbols.ATT_BASE );
                            QName processedDerivedBase = parseBase(derivedBase);
                            if(!processedTypeName.equals(processedDerivedBase)) {
                                fRedefineSucceeded = false;
                                reportGenericSchemaError("the base attribute of the restriction or extension grandchild of a complexType child of a redefine must have the same value as the complexType's type attribute");
                            } else {
                                String prefix = "";
                                int colonptr = derivedBase.indexOf(":");
                                if ( colonptr > 0)
                                    prefix = derivedBase.substring(0,colonptr) + ":";
                                greatGrandKid.setAttribute( SchemaSymbols.ATT_BASE, prefix + newName );
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (eltLocalname.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
            QName processedBaseName = new QName(-1, fStringPool.addSymbol(oldName), fStringPool.addSymbol(oldName), fTargetNSURI);
            int attGroupRefsCount = changeRedefineGroup(processedBaseName, eltLocalname, newName, child);
            if(attGroupRefsCount > 1) {
                fRedefineSucceeded = false;
                reportGenericSchemaError("if an attributeGroup child of a <redefine> element contains an attributeGroup ref'ing itself, it must have exactly 1; this one has " + attGroupRefsCount);
            } else if (attGroupRefsCount == 1) {
                return true;
            }  else
                fRedefineAttributeGroupMap.put(oldName, newName);
        } else if (eltLocalname.equals(SchemaSymbols.ELT_GROUP)) {
            QName processedBaseName = new QName(-1, fStringPool.addSymbol(oldName), fStringPool.addSymbol(oldName), fTargetNSURI);
            int groupRefsCount = changeRedefineGroup(processedBaseName, eltLocalname, newName, child);
            String restrictedName = newName.substring(0, newName.length()-redefIdentifier.length());
            if(!fRedefineSucceeded) {
                fRestrictedRedefinedGroupRegistry.put(fTargetNSURIString+","+restrictedName, new Boolean(false));
            }
            if(groupRefsCount > 1) {
                fRedefineSucceeded = false;
                fRestrictedRedefinedGroupRegistry.put(fTargetNSURIString+","+restrictedName, new Boolean(false));
                reportGenericSchemaError("if a group child of a <redefine> element contains a group ref'ing itself, it must have exactly 1; this one has " + groupRefsCount);
            } else if (groupRefsCount == 1) {
                fRestrictedRedefinedGroupRegistry.put(fTargetNSURIString+","+restrictedName, new Boolean(false));
                return true;
            }  else {
                fGroupNameRegistry.put(fTargetNSURIString + "," + oldName, newName);
                fRestrictedRedefinedGroupRegistry.put(fTargetNSURIString+","+restrictedName, new Boolean(true));
            }
        } else {
            fRedefineSucceeded = false;
            reportGenericSchemaError("internal Xerces error; please submit a bug with schema as testcase");
        }
        return false;

    private void traverseImport(Element importDecl)  throws Exception {
        int scope = GeneralAttrCheck.ELE_CONTEXT_GLOBAL;
        Hashtable attrValues = generalCheck(importDecl, scope);
        checkContent(importDecl, XUtil.getFirstChildElement(importDecl), true);

         String namespaceString = importDecl.getAttribute(SchemaSymbols.ATT_NAMESPACE);
         SchemaGrammar importedGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(namespaceString);

         if ((importedGrammar == null) || namespaceString.trim().equals(fTargetNSURIString)) {
             importedGrammar = new SchemaGrammar();
         } else {
             return;
         }

         Element root = null;
         if(namespaceString.length() == 0) {
            if(fTargetNSURI == StringPool.EMPTY_STRING) {
                reportGenericSchemaError("src-import.1.2:  if the namespace attribute on an <import> element is not present, the <import>ing schema must have a targetNamespace");
                if(fExternalNoNamespaceSchema != null) {
                    root = openImportedSchema(fExternalNoNamespaceSchema);
                }
            }
        } else {
            if(fTargetNSURIString.equals(namespaceString.trim())) {
                reportGenericSchemaError("src-import.1.1:  the namespace attribute of an <import> element must not be the same as the targetNamespace of the <import>ing schema");
                if(fExternalSchemas != null) {
                    Enumeration externalNamespaces = fExternalSchemas.keys();
                    while(externalNamespaces.hasMoreElements()) {
                        String namespace = (String)externalNamespaces.nextElement();
                        if(namespace.trim().equals(namespaceString.trim())) {
                            root = openImportedSchema((String)fExternalSchemas.get(namespace));
                            break;
                        }
                    }
                }
            }
        }
        String location = "";
            Attr locationAttr = importDecl.getAttributeNode(SchemaSymbols.ATT_SCHEMALOCATION);
            if(locationAttr != null) {
                location = locationAttr.getValue();
                root = openImportedSchema(location);
            }
         }
         if (root != null) {
             String targetNSURI = getTargetNamespaceString(root);
             if (!targetNSURI.equals(namespaceString) ) {
                 reportGenericSchemaError("imported schema '"+location+"' has a different targetNameSpace '"
                                          +targetNSURI+"' from what is declared '"+namespaceString+"'.");
             }
             else {
                 location = fCurrentSchemaURL;
                 TraverseSchema impSchema = new TraverseSchema(root, fStringPool, importedGrammar, fGrammarResolver, fErrorReporter, location, fEntityResolver, fFullConstraintChecking, fGeneralAttrCheck, fUnparsedExternalSchemas, fExternalNoNamespaceSchema);
                 fCurrentSchemaURL = (String)fSchemaURLStack.pop();
                 Enumeration ics = impSchema.fIdentityConstraints.keys();
                 while(ics.hasMoreElements()) {
                    Object icsKey = ics.nextElement();
                    fIdentityConstraints.put(icsKey, impSchema.fIdentityConstraints.get(icsKey));
                }
                 Enumeration icNames = impSchema.fIdentityConstraintNames.keys();
                 while(icNames.hasMoreElements()) {
                    String icsNameKey = (String)icNames.nextElement();
                    fIdentityConstraintNames.put(icsNameKey, impSchema.fIdentityConstraintNames.get(icsNameKey));
                }
            }
         }
    }

    private Element openImportedSchema(String location) throws Exception {
        InputSource source = null;

        if ( (fEntityResolver != null) &&
             !(fEntityResolver.getClass().getName().equals("org.apache.xerces.validators.common.XMLValidator$Resolver")) ) {
            source = fEntityResolver.resolveEntity("", location);
        }

        if (source == null) {
            location = expandSystemId(location, fCurrentSchemaURL);
            source = new InputSource(location);
        }
        String pubId = "";
        String sysId = "";
        if (source.getPublicId () != null)
            pubId = source.getPublicId ();
        if (source.getSystemId () != null)
            sysId = source.getSystemId ();

        if(pubId.length() != 0 || sysId.length() != 0)
            location = pubId+sysId;

         if (fImportLocations.contains((Object)location)) {
             return null;
         }
        if(source.getSystemId().equals(fCurrentSchemaURL)) {
            return null;
        }

         DOMParser parser = new IgnoreWhitespaceParser();
         parser.setEntityResolver( (fEntityResolver != null)? (fEntityResolver):(new Resolver()) );
         parser.setErrorHandler( new ErrorHandler  ()
            {
                public void error(SAXParseException ex) throws SAXException {
                    StringBuffer str = new StringBuffer();
                    String systemId_ = ex.getSystemId();
                    if (systemId_ != null) {
                        int index = systemId_.lastIndexOf('/');
                        if (index != -1)
                            systemId_ = systemId_.substring(index + 1);
                        str.append(systemId_);
                    }
                    str.append(':').append(ex.getLineNumber()).append(':').append(ex.getColumnNumber());
                    String message = ex.getMessage();
                    if(message.toLowerCase().trim().endsWith("not found.")) {
                        System.err.println("[Warning] "+
                               str.toString()+": "+ message);
                        System.err.println("[Error] "+
                               str.toString()+":"+message);
                        throw ex;
                    }
                }
            });

         try {
         }catch(  org.xml.sax.SAXNotRecognizedException e ) {
             e.printStackTrace();
         }catch( org.xml.sax.SAXNotSupportedException e ) {
             e.printStackTrace();
         }

         try {
             parser.parse( source );
         }catch( IOException e ) {
         }catch( SAXException e ) {
             e.printStackTrace();
         }

         Element root = null;
         if (document != null) {
             root = document.getDocumentElement();
         }
         if(root != null) {
            fImportLocations.addElement((Object)location);
        }
        fSchemaURLStack.push(fCurrentSchemaURL);
        fCurrentSchemaURL = location;
        return root;

    private String getTargetNamespaceString( Element root) throws Exception {
        String targetNSURI = "";
        Attr targetNSAttr = root.getAttributeNode(SchemaSymbols.ATT_TARGETNAMESPACE);
        if(targetNSAttr != null) {
            targetNSURI=targetNSAttr.getValue();
            if(targetNSURI.length() == 0) {
                reportGenericSchemaError("sch-prop-correct.1:  \"\" is not a legal value for the targetNamespace attribute; the attribute must either be absent or contain a nonempty value");
            }
        }
        return targetNSURI;

    /**
    * <annotation>(<appinfo> | <documentation>)*</annotation>
    *
    * @param annotationDecl:  the DOM node corresponding to the <annotation> info item
    */
    private void traverseAnnotationDecl(Element annotationDecl) throws Exception {

        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(annotationDecl, scope);

        for(Element child = XUtil.getFirstChildElement(annotationDecl); child != null;
                 child = XUtil.getNextSiblingElement(child)) {
            String name = child.getLocalName();
            if(!((name.equals(SchemaSymbols.ELT_APPINFO)) ||
                    (name.equals(SchemaSymbols.ELT_DOCUMENTATION)))) {
                reportGenericSchemaError("an <annotation> can only contain <appinfo> and <documentation> elements");
            }

            attrValues = generalCheck(child, scope);
        }
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


   private DatatypeValidator findDTValidator (Element elm, String baseTypeStr, int baseRefContext )  throws Exception{
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
        if (uri.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) &&
            localpart.equals("anySimpleType") &&
            baseRefContext == SchemaSymbols.RESTRICTION) {
            reportSchemaError(SchemaMessageProvider.UnknownBaseDatatype,
                              new Object [] { elm.getAttribute( SchemaSymbols.ATT_BASE ),
                                              elm.getAttribute(SchemaSymbols.ATT_NAME)});
            return null;
        }
        baseValidator = getDatatypeValidator(uri, localpart);
        if (baseValidator == null) {
            Element baseTypeNode = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
            if (baseTypeNode != null) {
                traverseSimpleTypeDecl( baseTypeNode );

                baseValidator = getDatatypeValidator(uri, localpart);
            }
        }
        Integer finalValue;
        if ( baseValidator == null ) {
            reportSchemaError(SchemaMessageProvider.UnknownBaseDatatype,
                              new Object [] { elm.getAttribute( SchemaSymbols.ATT_BASE ),
                                  elm.getAttribute(SchemaSymbols.ATT_NAME)});
        } else {
            finalValue =
                    ((Integer)fSimpleTypeFinalRegistry.get(uri + "," +localpart));
            if((finalValue != null) &&
                    ((finalValue.intValue() & baseRefContext) != 0)) {
                reportGenericSchemaError("the base type " + baseTypeStr + " does not allow itself to be used as the base for a restriction and/or as a type in a list and/or union");
                return baseValidator;
            }
        }
       return baseValidator;
    }

    private void checkEnumerationRequiredNotation(String name, String type) throws Exception{
        String localpart = type;
        int colonptr = type.indexOf(":");
        if ( colonptr > 0) {
            localpart = type.substring(colonptr+1);
        }
        if (localpart.equals("NOTATION")) {
          reportGenericSchemaError("[enumeration-required-notation] It is an error for NOTATION to be used "+
                                "directly in a schema in element/attribute '"+name+"'");
        }

    }

    private int resetSimpleTypeNameStack(int returnValue){
       if (!fSimpleTypeNameStack.empty()) {
           fSimpleTypeNameStack.pop();
       }
       return returnValue;
    }

    private void reportCosListOfAtomic () throws Exception{
       reportGenericSchemaError("cos-list-of-atomic: The itemType must have a {variety} of atomic or union (in which case all the {member type definitions} must be atomic)");
       fListName="";
    }

    private boolean isListDatatype (DatatypeValidator validator){
       if (validator instanceof UnionDatatypeValidator) {
              Vector temp = ((UnionDatatypeValidator)validator).getBaseValidators();
              for (int i=0;i<temp.size();i++) {
                   if (temp.elementAt(i) instanceof ListDatatypeValidator) {
                       return true;
                   }
                   if (temp.elementAt(i) instanceof UnionDatatypeValidator) {
                       if (isListDatatype((DatatypeValidator)temp.elementAt(i))) {
                           return true;
                       }
                   }
              }
       }
       return false;
    }



   /**
     * Traverse SimpleType declaration:
     * <simpleType
     *         final = #all | list of (restriction, union or list)
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

        int scope = isTopLevel(simpleTypeDecl)?
                    GeneralAttrCheck.ELE_CONTEXT_GLOBAL:
                    GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(simpleTypeDecl, scope);

        String nameProperty          =  simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME );
        String qualifiedName = nameProperty;


            qualifiedName = fTargetNSURIString+","+"#S#"+(fSimpleTypeAnonCount++);
            fStringPool.addSymbol(qualifiedName);
        }
        else {
                qualifiedName = fTargetNSURIString+","+qualifiedName;
            fStringPool.addSymbol( nameProperty );
        }

        if (fDatatypeRegistry.getDatatypeValidator(qualifiedName)!=null) {
            return resetSimpleTypeNameStack(fStringPool.addSymbol(qualifiedName));
        }
        else {
           if (fSimpleTypeNameStack.search(qualifiedName) != -1 ){
               reportGenericSchemaError("cos-no-circular-unions: no circular definitions are allowed for an element '"+ nameProperty+"'");
               return resetSimpleTypeNameStack(-1);
           }
        }


        Attr finalAttr = simpleTypeDecl.getAttributeNode(SchemaSymbols.ATT_FINAL);
        int finalProperty = 0;
        if(finalAttr != null)
            finalProperty = parseFinalSet(finalAttr.getValue());
        else
            finalProperty = parseFinalSet(null);

        if(finalProperty != 0)
            fSimpleTypeFinalRegistry.put(qualifiedName, new Integer(finalProperty));


        fSimpleTypeNameStack.push(qualifiedName);


        Element content = XUtil.getFirstChildElement(simpleTypeDecl);
        content = checkContent(simpleTypeDecl, content, false);
        if (content == null) {
            return resetSimpleTypeNameStack(-1);
        }

        scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable contentAttrs = generalCheck(content, scope);

        String varietyProperty = content.getLocalName();
        String baseTypeQNameProperty = null;
        Vector dTValidators = null;
        int size = 0;
        StringTokenizer unionMembers = null;
        boolean list = false;
        boolean union = false;
        boolean restriction = false;

           baseTypeQNameProperty =  content.getAttribute( SchemaSymbols.ATT_ITEMTYPE );
           list = true;
                    reportCosListOfAtomic();
                    return resetSimpleTypeNameStack(-1);
           }
           else {
                fListName = qualifiedName;
           }
        }
            baseTypeQNameProperty =  content.getAttribute( SchemaSymbols.ATT_BASE );
            restriction= true;
        }
            union = true;
            baseTypeQNameProperty = content.getAttribute( SchemaSymbols.ATT_MEMBERTYPES);
            if (baseTypeQNameProperty.length() != 0) {
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
        if(XUtil.getNextSiblingElement(content) != null) {
            reportGenericSchemaError("error in content of simpleType");
        }

        int typeNameIndex;
        DatatypeValidator baseValidator = null;

        if ( baseTypeQNameProperty.length() == 0 ) {

            content = XUtil.getFirstChildElement(content);

            content = checkContent(simpleTypeDecl, content, false);
            if (content == null) {
                return resetSimpleTypeNameStack(-1);
            }
            if (content.getLocalName().equals( SchemaSymbols.ELT_SIMPLETYPE )) {
              typeNameIndex = traverseSimpleTypeDecl(content);
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
                      return resetSimpleTypeNameStack(-1);
              }
            }
            else {
                 reportSchemaError(SchemaMessageProvider.ListUnionRestrictionError,
                        new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
                 return resetSimpleTypeNameStack(-1);
            }
        else {
            numOfTypes = 1;
            if (union) {
                numOfTypes= size;
            }
            int baseRefContext = (restriction? SchemaSymbols.RESTRICTION:0);
            baseRefContext = baseRefContext | (union? SchemaSymbols.UNION:0);
            baseRefContext = baseRefContext | (list ? SchemaSymbols.LIST:0);
                if (union) {
                    baseTypeQNameProperty = unionMembers.nextToken();
                }
                baseValidator = findDTValidator ( simpleTypeDecl, baseTypeQNameProperty, baseRefContext);
                if ( baseValidator == null) {
                    return resetSimpleTypeNameStack(-1);
                }
                if (fListName.length() != 0 ) {
                    if (baseValidator instanceof ListDatatypeValidator) {
                        reportCosListOfAtomic();
                        return resetSimpleTypeNameStack(-1);
                    }
                    if (isListDatatype(baseValidator)) {
                        reportCosListOfAtomic();
                        return resetSimpleTypeNameStack(-1);

                    }

                }
                if (union) {
                }
            }


        if (baseTypeQNameProperty.length() == 0) {
            content = XUtil.getNextSiblingElement( content );
        }
        else {
            content = XUtil.getFirstChildElement(content);
        }

        if (union) {
            int index=size;
            if (baseTypeQNameProperty.length() != 0 ) {
                content = checkContent(simpleTypeDecl, content, true);
            }
            while (content!=null) {
                typeNameIndex = traverseSimpleTypeDecl(content);
                if (typeNameIndex!=-1) {
                    baseValidator=fDatatypeRegistry.getDatatypeValidator(fStringPool.toString(typeNameIndex));
                    if (baseValidator != null) {
                        if (fListName.length() != 0 && baseValidator instanceof ListDatatypeValidator) {
                            reportCosListOfAtomic();
                            return resetSimpleTypeNameStack(-1);
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


        if (fListName.length() != 0) {
            if (fListName.equals(qualifiedName)) {
                fListName = "";
            }
        }

        int numFacets=0;
        fFacetData.clear();
        if (restriction && content != null) {
            int numEnumerationLiterals = 0;
            Vector enumData  = new Vector();
            content = checkContent(simpleTypeDecl, content , true);
            StringBuffer pattern = null;
            String facet;
            while (content != null) {
                if (content.getNodeType() == Node.ELEMENT_NODE) {
                        contentAttrs = generalCheck(content, scope);
                        numFacets++;
                        facet =content.getLocalName();
                        if (facet.equals(SchemaSymbols.ELT_ENUMERATION)) {
                            numEnumerationLiterals++;
                            String enumVal = content.getAttribute(SchemaSymbols.ATT_VALUE);
                            String localName;
                            if (baseValidator instanceof NOTATIONDatatypeValidator) {
                                String prefix = "";
                                String localpart = enumVal;
                                int colonptr = enumVal.indexOf(":");
                                if ( colonptr > 0) {
                                        prefix = enumVal.substring(0,colonptr);
                                        localpart = enumVal.substring(colonptr+1);
                                }
                                String uriStr = (prefix.length() != 0)?resolvePrefixToURI(prefix):fTargetNSURIString;
                                nameProperty=uriStr + ":" + localpart;
                                localName = (String)fNotationRegistry.get(nameProperty);
                                if(localName == null){
                                       localName = traverseNotationFromAnotherSchema( localpart, uriStr);
                                       if (localName == null) {
                                            reportGenericSchemaError("Notation '" + localpart +
                                                                    "' not found in the grammar "+ uriStr);

                                       }
                                }
                                enumVal=nameProperty;
                            }
                            enumData.addElement(enumVal);
                            checkContent(simpleTypeDecl, XUtil.getFirstChildElement( content ), true);
                        }
                        else if (facet.equals(SchemaSymbols.ELT_ANNOTATION) || facet.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                                  reportSchemaError(SchemaMessageProvider.ListUnionRestrictionError,
                                  new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
                        }
                        else if (facet.equals(SchemaSymbols.ELT_PATTERN)) {
                            if (pattern == null) {
                                pattern = new StringBuffer (content.getAttribute( SchemaSymbols.ATT_VALUE ));
                            }
                            else {
                                pattern.append("|");
                                pattern.append(content.getAttribute( SchemaSymbols.ATT_VALUE ));
                                checkContent(simpleTypeDecl, XUtil.getFirstChildElement( content ), true);
                            }
                        }
                        else {
                            if ( fFacetData.containsKey(facet) )
                                reportSchemaError(SchemaMessageProvider.DatatypeError,
                                                  new Object [] {"The facet '" + facet + "' is defined more than once."} );
                             fFacetData.put(facet,content.getAttribute( SchemaSymbols.ATT_VALUE ));

                             if (content.getAttribute( SchemaSymbols.ATT_FIXED).equals(SchemaSymbols.ATTVAL_TRUE) ||
                                 content.getAttribute( SchemaSymbols.ATT_FIXED).equals(SchemaSymbols.ATTVAL_TRUE_1)){
                                  if ( facet.equals(SchemaSymbols.ELT_MINLENGTH) ) {
                                      flags |= DatatypeValidator.FACET_MINLENGTH;
                                  }
                                  else if (facet.equals(SchemaSymbols.ELT_MAXLENGTH)) {
                                      flags |= DatatypeValidator.FACET_MAXLENGTH;
                                  }
                                  else if (facet.equals(SchemaSymbols.ELT_MAXEXCLUSIVE)) {
                                      flags |= DatatypeValidator.FACET_MAXEXCLUSIVE;
                                  }
                                  else if (facet.equals(SchemaSymbols.ELT_MAXINCLUSIVE)) {
                                      flags |= DatatypeValidator.FACET_MAXINCLUSIVE;
                                  }
                                  else if (facet.equals(SchemaSymbols.ELT_MINEXCLUSIVE)) {
                                      flags |= DatatypeValidator.FACET_MINEXCLUSIVE;
                                  }
                                  else if (facet.equals(SchemaSymbols.ELT_MININCLUSIVE)) {
                                      flags |= DatatypeValidator.FACET_MININCLUSIVE;
                                  }
                                  else if (facet.equals(SchemaSymbols.ELT_TOTALDIGITS)) {
                                      flags |= DatatypeValidator.FACET_TOTALDIGITS;
                                  }
                                  else if (facet.equals(SchemaSymbols.ELT_FRACTIONDIGITS)) {
                                      flags |= DatatypeValidator.FACET_FRACTIONDIGITS;
                                  }
                                  else if (facet.equals(SchemaSymbols.ELT_WHITESPACE) &&
                                           baseValidator instanceof StringDatatypeValidator) {
                                      flags |= DatatypeValidator.FACET_WHITESPACE;
                                  }
                              }
                             checkContent(simpleTypeDecl, XUtil.getFirstChildElement( content ), true);
                        }
                }
                    content = XUtil.getNextSiblingElement(content);
            }
            if (numEnumerationLiterals > 0) {
                  fFacetData.put(SchemaSymbols.ELT_ENUMERATION, enumData);
            }
            if (pattern !=null) {
                fFacetData.put(SchemaSymbols.ELT_PATTERN, pattern.toString());
            }
            if (flags != 0) {
                fFacetData.put(DatatypeValidator.FACET_FIXED, new Short(flags));
            }
        }


        else if (list && content!=null) {
            if (baseTypeQNameProperty.length() != 0) {
                content = checkContent(simpleTypeDecl, content, true);
                if (content!=null) {
                    reportSchemaError(SchemaMessageProvider.ListUnionRestrictionError,
                                      new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
            }
            }
            else {
                reportSchemaError(SchemaMessageProvider.ListUnionRestrictionError,
                        new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
            }
        }
        else if (union && content!=null) {
             if (baseTypeQNameProperty.length() != 0) {
                content = checkContent(simpleTypeDecl, content, true);
                if (content!=null) {
                    reportSchemaError(SchemaMessageProvider.ListUnionRestrictionError,
                                            new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
                }
            }
            else {
                reportSchemaError(SchemaMessageProvider.ListUnionRestrictionError,
                        new Object [] { simpleTypeDecl.getAttribute( SchemaSymbols.ATT_NAME )});
            }
        }

        try {
           DatatypeValidator newValidator =
                 fDatatypeRegistry.getDatatypeValidator( qualifiedName );

               if (list) {
                    fDatatypeRegistry.createDatatypeValidator( qualifiedName, baseValidator,
                                                               fFacetData,true);
               }
               else if (restriction) {
                   fDatatypeRegistry.createDatatypeValidator( qualifiedName, baseValidator,
                                                               fFacetData,false);
               }
                   fDatatypeRegistry.createDatatypeValidator( qualifiedName, dTValidators);
               }

           }

           } catch (Exception e) {
               reportSchemaError(SchemaMessageProvider.DatatypeError,new Object [] { e.getMessage() });
           }
        return resetSimpleTypeNameStack(fStringPool.addSymbol(qualifiedName));
     }


    /*
    * <any
    *   id = ID
    *   maxOccurs = string
    *   minOccurs = nonNegativeInteger
    *   namespace = (##any | ##other) | List of (anyURI | (##targetNamespace | ##local))
    *   processContents = lax | skip | strict>
    *   Content: (annotation?)
    * </any>
    */
    private int traverseAny(Element child) throws Exception {
        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(child, scope);

        Element annotation = checkContent( child, XUtil.getFirstChildElement(child), true );
        if(annotation != null ) {
            reportGenericSchemaError("<any> elements can contain at most one <annotation> element in their children");
        }
        int anyIndex = -1;
        String namespace = child.getAttribute(SchemaSymbols.ATT_NAMESPACE).trim();
        String processContents = child.getAttribute("processContents").trim();

        int processContentsAny = XMLContentSpec.CONTENTSPECNODE_ANY;
        int processContentsAnyOther = XMLContentSpec.CONTENTSPECNODE_ANY_OTHER;
        int processContentsAnyLocal = XMLContentSpec.CONTENTSPECNODE_ANY_NS;

        if (processContents.length() > 0 && !processContents.equals("strict")) {
            if (processContents.equals("lax")) {
                processContentsAny = XMLContentSpec.CONTENTSPECNODE_ANY_LAX;
                processContentsAnyOther = XMLContentSpec.CONTENTSPECNODE_ANY_OTHER_LAX;
                processContentsAnyLocal = XMLContentSpec.CONTENTSPECNODE_ANY_NS_LAX;
            }
            else if (processContents.equals("skip")) {
                processContentsAny = XMLContentSpec.CONTENTSPECNODE_ANY_SKIP;
                processContentsAnyOther = XMLContentSpec.CONTENTSPECNODE_ANY_OTHER_SKIP;
                processContentsAnyLocal = XMLContentSpec.CONTENTSPECNODE_ANY_NS_SKIP;
            }
        }

        if (namespace.length() == 0 || namespace.equals("##any")) {
            anyIndex = fSchemaGrammar.addContentSpecNode(processContentsAny, -1, StringPool.EMPTY_STRING, false);
        }
        else if (namespace.equals("##other")) {
            String uri = fTargetNSURIString;
            int uriIndex = fStringPool.addSymbol(uri);
            anyIndex = fSchemaGrammar.addContentSpecNode(processContentsAnyOther, -1, uriIndex, false);
        }
        else if (namespace.length() > 0) {
            int uriIndex, leafIndex, choiceIndex;

            StringTokenizer tokenizer = new StringTokenizer(namespace);
                String token = tokenizer.nextToken();
            if (token.equals(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL)) {
                uriIndex = StringPool.EMPTY_STRING;
            } else {
                if (token.equals("##targetNamespace"))
                    token = fTargetNSURIString;
                uriIndex = fStringPool.addSymbol(token);
            }
            choiceIndex = fSchemaGrammar.addContentSpecNode(processContentsAnyLocal, -1, uriIndex, false);

            int[] uriList = new int[8];
            uriList[0] = uriIndex;
            int uriCount = 1;

            while (tokenizer.hasMoreElements()) {
                token = tokenizer.nextToken();
                if (token.equals(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL)) {
                    uriIndex = StringPool.EMPTY_STRING;
                } else {
                    if (token.equals("##targetNamespace"))
                        token = fTargetNSURIString;
                    uriIndex = fStringPool.addSymbol(token);
                }
                for (int i = 0; i < uriCount; i++) {
                    if (uriList[i] == uriIndex)
                        continue;
                }
                if (uriList.length == uriCount) {
                    int[] newList = new int[uriCount*2];
                    System.arraycopy(uriList,0,newList,0,uriCount);
                    uriList = newList;
                }
                uriList[uriCount++] = uriIndex;

                leafIndex = fSchemaGrammar.addContentSpecNode(processContentsAnyLocal, -1, uriIndex, false);
                choiceIndex = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_CHOICE, choiceIndex, leafIndex, false);
            }
            anyIndex = choiceIndex;
        }
        else {
            reportGenericSchemaError("Empty namespace attribute for any element");
        }

        return anyIndex;
    }

    public DatatypeValidator getDatatypeValidator(String uri, String localpart) {

        DatatypeValidator dv = null;

        if (uri.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
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
        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(anyAttributeDecl, scope);

        Element annotation = checkContent( anyAttributeDecl, XUtil.getFirstChildElement(anyAttributeDecl), true );
        if(annotation != null ) {
            reportGenericSchemaError("<anyAttribute> elements can contain at most one <annotation> element in their children");
        }
        XMLAttributeDecl anyAttDecl = new XMLAttributeDecl();
        String processContents = anyAttributeDecl.getAttribute(SchemaSymbols.ATT_PROCESSCONTENTS).trim();
        String namespace = anyAttributeDecl.getAttribute(SchemaSymbols.ATT_NAMESPACE).trim();
        String curTargetUri = fTargetNSURIString;

        if ( namespace.length() == 0 || namespace.equals(SchemaSymbols.ATTVAL_TWOPOUNDANY) ) {
            anyAttDecl.type = XMLAttributeDecl.TYPE_ANY_ANY;
        }
        else if (namespace.equals(SchemaSymbols.ATTVAL_TWOPOUNDOTHER)) {
            anyAttDecl.type = XMLAttributeDecl.TYPE_ANY_OTHER;
            anyAttDecl.name.uri = fStringPool.addSymbol(curTargetUri);
        }
        else if (namespace.length() > 0){
            anyAttDecl.type = XMLAttributeDecl.TYPE_ANY_LIST;

            StringTokenizer tokenizer = new StringTokenizer(namespace);
            int aStringList = fStringPool.startStringList();
            Vector tokens = new Vector();
            int tokenStr;
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                if (token.equals(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL)) {
                    tokenStr = StringPool.EMPTY_STRING;
                } else {
                    if (token.equals(SchemaSymbols.ATTVAL_TWOPOUNDTARGETNS))
                    token = curTargetUri;
                    tokenStr = fStringPool.addSymbol(token);
                }
                if (!fStringPool.addStringToList(aStringList, tokenStr)){
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

        if (processContents.equals(SchemaSymbols.ATTVAL_SKIP)){
            anyAttDecl.defaultType |= XMLAttributeDecl.PROCESSCONTENTS_SKIP;
        }
        else if (processContents.equals(SchemaSymbols.ATTVAL_LAX)) {
            anyAttDecl.defaultType |= XMLAttributeDecl.PROCESSCONTENTS_LAX;
        }
        else {
            anyAttDecl.defaultType |= XMLAttributeDecl.PROCESSCONTENTS_STRICT;
        }

        return anyAttDecl;
    }

    private XMLAttributeDecl AWildCardIntersection(XMLAttributeDecl oneAny, XMLAttributeDecl anotherAny) {
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

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_OTHER &&
            anotherAny.type == XMLAttributeDecl.TYPE_ANY_LIST ||
            oneAny.type == XMLAttributeDecl.TYPE_ANY_LIST &&
            anotherAny.type == XMLAttributeDecl.TYPE_ANY_OTHER) {
            XMLAttributeDecl anyList, anyOther;
            if (oneAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
                anyList = oneAny;
                anyOther = anotherAny;
            } else {
                anyList = anotherAny;
                anyOther = oneAny;
                }

            int[] uriList = fStringPool.stringListAsIntArray(anyList.enumeration);
            if (elementInSet(anyOther.name.uri, uriList)) {
                    int newList = fStringPool.startStringList();
                for (int i=0; i< uriList.length; i++) {
                    if (uriList[i] != anyOther.name.uri ) {
                        fStringPool.addStringToList(newList, uriList[i]);
                        }
                    }
                    fStringPool.finishStringList(newList);
                anyList.enumeration = newList;
                }

            return anyList;
        }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_LIST &&
            anotherAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
            int[] result = intersect2sets(fStringPool.stringListAsIntArray(oneAny.enumeration),
                                          fStringPool.stringListAsIntArray(anotherAny.enumeration));
            int newList = fStringPool.startStringList();
            for (int i=0; i<result.length; i++) {
                fStringPool.addStringToList(newList, result[i]);
            }
            fStringPool.finishStringList(newList);
            oneAny.enumeration = newList;
                return oneAny;
            }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_OTHER &&
            anotherAny.type == XMLAttributeDecl.TYPE_ANY_OTHER) {
            if (oneAny.name.uri == anotherAny.name.uri) {
                return oneAny;
            } else {
                oneAny.type = -1;
                return oneAny;
            }
        }

                    return oneAny;
                }

    private XMLAttributeDecl AWildCardUnion(XMLAttributeDecl oneAny, XMLAttributeDecl anotherAny) {
        if (oneAny.type == -1) {
                    return oneAny;
                }
        if (anotherAny.type == -1) {
            return anotherAny;
            }


        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_ANY) {
                return oneAny;
            }
        if (anotherAny.type == XMLAttributeDecl.TYPE_ANY_ANY) {
            return anotherAny;
        }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_LIST &&
            anotherAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
            int[] result = union2sets(fStringPool.stringListAsIntArray(oneAny.enumeration),
                                               fStringPool.stringListAsIntArray(anotherAny.enumeration));
                int newList = fStringPool.startStringList();
                for (int i=0; i<result.length; i++) {
                    fStringPool.addStringToList(newList, result[i]);
                }
                fStringPool.finishStringList(newList);
                oneAny.enumeration = newList;
                return oneAny;
            }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_OTHER &&
            anotherAny.type == XMLAttributeDecl.TYPE_ANY_OTHER) {
            if (oneAny.name.uri == anotherAny.name.uri) {
                return oneAny;
            } else {
                oneAny.type = XMLAttributeDecl.TYPE_ANY_ANY;
                return oneAny;
            }
        }

        if (oneAny.type == XMLAttributeDecl.TYPE_ANY_OTHER &&
            anotherAny.type == XMLAttributeDecl.TYPE_ANY_LIST ||
            oneAny.type == XMLAttributeDecl.TYPE_ANY_LIST &&
            anotherAny.type == XMLAttributeDecl.TYPE_ANY_OTHER) {
            XMLAttributeDecl anyList, anyOther;
            if (oneAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
                anyList = oneAny;
                anyOther = anotherAny;
            } else {
                anyList = anotherAny;
                anyOther = oneAny;
        }
            if (elementInSet(anyOther.name.uri,
                             fStringPool.stringListAsIntArray(anyList.enumeration))) {
                anyOther.type = XMLAttributeDecl.TYPE_ANY_ANY;
            }

            return anyOther;
        }

        return oneAny;
    }

    private boolean AWildCardSubset(XMLAttributeDecl subAny, XMLAttributeDecl superAny) {
        if (subAny.type == -1 || superAny.type == -1)
            return false;

        if (superAny.type == XMLAttributeDecl.TYPE_ANY_ANY)
            return true;

        if (subAny.type == XMLAttributeDecl.TYPE_ANY_OTHER) {
            if (superAny.type == XMLAttributeDecl.TYPE_ANY_OTHER &&
                subAny.name.uri == superAny.name.uri) {
                return true;
            }
        }

        if (subAny.type == XMLAttributeDecl.TYPE_ANY_LIST) {
            if (superAny.type == XMLAttributeDecl.TYPE_ANY_LIST &&
                subset2sets(fStringPool.stringListAsIntArray(subAny.enumeration),
                            fStringPool.stringListAsIntArray(superAny.enumeration))) {
                return true;
            }

            if (superAny.type == XMLAttributeDecl.TYPE_ANY_OTHER &&
                !elementInSet(superAny.name.uri, fStringPool.stringListAsIntArray(superAny.enumeration))) {
                return true;
            }
        }

        return false;
    }

    private boolean AWildCardAllowsNameSpace(XMLAttributeDecl wildcard, String uri) {
        if (wildcard.type == -1)
            return false;

        if (wildcard.type == XMLAttributeDecl.TYPE_ANY_ANY)
            return true;

        int uriStr = fStringPool.addString(uri);

        if (wildcard.type == XMLAttributeDecl.TYPE_ANY_OTHER) {
            if (uriStr != wildcard.name.uri && uriStr != StringPool.EMPTY_STRING)
                return true;
        }

        if (wildcard.type == XMLAttributeDecl.TYPE_ANY_LIST) {
            if (elementInSet(uriStr, fStringPool.stringListAsIntArray(wildcard.enumeration)))
                return true;
        }

        return false;
    }

    private boolean isAWildCard(XMLAttributeDecl a) {
        if (a.type == XMLAttributeDecl.TYPE_ANY_ANY
            ||a.type == XMLAttributeDecl.TYPE_ANY_LIST
            ||a.type == XMLAttributeDecl.TYPE_ANY_OTHER )
            return true;
        else
            return false;
    }

    int[] intersect2sets(int[] one, int[] theOther){
        int[] result = new int[(one.length>theOther.length?one.length:theOther.length)];

        int count = 0;
        for (int i=0; i<one.length; i++) {
            if (elementInSet(one[i], theOther))
                    result[count++] = one[i];
                }

        int[] result2 = new int[count];
        System.arraycopy(result, 0, result2, 0, count);

        return result2;
    }

    int[] union2sets(int[] one, int[] theOther){
        int[] result1 = new int[one.length];

        int count = 0;
        for (int i=0; i<one.length; i++) {
            if (!elementInSet(one[i], theOther))
                result1[count++] = one[i];
        }

        int[] result2 = new int[count+theOther.length];
        System.arraycopy(result1, 0, result2, 0, count);
        System.arraycopy(theOther, 0, result2, count, theOther.length);

        return result2;
    }

    boolean subset2sets(int[] subSet, int[] superSet){
        for (int i=0; i<subSet.length; i++) {
            if (!elementInSet(subSet[i], superSet))
                return false;
        }

        return true;
    }

    boolean elementInSet(int ele, int[] set){
        boolean found = false;
        for (int i=0; i<set.length && !found; i++) {
            if (ele==set[i])
                found = true;
        }

        return found;
    }

    private int traverseComplexTypeDecl( Element complexTypeDecl ) throws Exception {
        return traverseComplexTypeDecl (complexTypeDecl, false);
    }

    /**
     * Traverse ComplexType Declaration - Rec Implementation.
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
     * @param forwardRef
     * @return
     */

    private int traverseComplexTypeDecl( Element complexTypeDecl, boolean forwardRef)
            throws Exception {

        int scope = isTopLevel(complexTypeDecl)?
                    GeneralAttrCheck.ELE_CONTEXT_GLOBAL:
                    GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(complexTypeDecl, scope);

        String isAbstract = complexTypeDecl.getAttribute( SchemaSymbols.ATT_ABSTRACT );
        String blockSet = null;
        Attr blockAttr = complexTypeDecl.getAttributeNode( SchemaSymbols.ATT_BLOCK );
        if (blockAttr != null)
            blockSet = blockAttr.getValue();
        String finalSet = null;
        Attr finalAttr = complexTypeDecl.getAttributeNode( SchemaSymbols.ATT_FINAL );
        if (finalAttr != null)
            finalSet = finalAttr.getValue();
        String typeId = complexTypeDecl.getAttribute( SchemaSymbols.ATTVAL_ID );
        String typeName = complexTypeDecl.getAttribute(SchemaSymbols.ATT_NAME);
        String mixed = complexTypeDecl.getAttribute(SchemaSymbols.ATT_MIXED);
        boolean isMixed = mixed.equals(SchemaSymbols.ATTVAL_TRUE) || mixed.equals(SchemaSymbols.ATTVAL_TRUE_1);
        boolean isNamedType = false;
        Stack savedGroupNameStack = null;

            typeName = genAnonTypeName(complexTypeDecl);
        }

        if ( DEBUGGING )
            System.out.println("traversing complex Type : " + typeName);

        int typeNameIndex = fStringPool.addSymbol(typeName);


        if (isTopLevel(complexTypeDecl)) {

            String fullName = fTargetNSURIString+","+typeName;
            ComplexTypeInfo temp = (ComplexTypeInfo) fComplexTypeRegistry.get(fullName);
            if (temp != null ) {
                if (!forwardRef)  {
                   if (temp.declSeen())
                      reportGenericSchemaError("sch-props-correct: Duplicate declaration for complexType " +
                                                typeName);
                   else
                      temp.setDeclSeen();

                }
                return fStringPool.addSymbol(fullName);
            }
            else {
              if (getDatatypeValidator(fTargetNSURIString,typeName)!=null)
                reportGenericSchemaError("sch-props-correct: Duplicate type declaration - type is " +
                                                typeName);

            }
        }

        fCurrentTypeNameStack.push(typeName);

        int scopeDefined = fScopeCount++;
        int previousScope = fCurrentScope;
        fCurrentScope = scopeDefined;

        if (!fCurrentGroupNameStack.isEmpty()) {
          savedGroupNameStack = fCurrentGroupNameStack;
          fCurrentGroupNameStack = new Stack();
        }

        Element child = null;
        ComplexTypeInfo typeInfo = new ComplexTypeInfo();
        typeInfo.scopeDefined = scopeDefined;

        try {

          child = checkContent(complexTypeDecl,XUtil.getFirstChildElement(complexTypeDecl),
                               true);

          if (child==null) {
              processComplexContent(typeNameIndex, child, typeInfo, null, isMixed);
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
                  traverseComplexContentDecl(typeNameIndex, child, typeInfo, isMixed);
                  if (XUtil.getNextSiblingElement(child) != null)
                     throw new ComplexTypeRecoverableError(
                      "Invalid child following the complexContent child in the complexType");
              }
              else {
                  processComplexContent(typeNameIndex, child, typeInfo, null, isMixed);

              }
          }
          typeInfo.blockSet = parseBlockSet(blockSet);
          if( (blockSet != null ) && blockSet.length() != 0 &&
                (!blockSet.equals(SchemaSymbols.ATTVAL_POUNDALL) &&
                (((typeInfo.blockSet & SchemaSymbols.RESTRICTION) == 0) &&
                ((typeInfo.blockSet & SchemaSymbols.EXTENSION) == 0))))
            throw new ComplexTypeRecoverableError("The values of the 'block' attribute of a complexType must be either #all or a list of 'restriction' and 'extension'; " + blockSet + " was found");

          typeInfo.finalSet = parseFinalSet(finalSet);
          if( (finalSet != null ) && finalSet.length() != 0 &&
                (!finalSet.equals(SchemaSymbols.ATTVAL_POUNDALL) &&
                (((typeInfo.finalSet & SchemaSymbols.RESTRICTION) == 0) &&
                ((typeInfo.finalSet & SchemaSymbols.EXTENSION) == 0))))
            throw new ComplexTypeRecoverableError("The values of the 'final' attribute of a complexType must be either #all or a list of 'restriction' and 'extension'; " + finalSet + " was found");

        }
        catch (ComplexTypeRecoverableError e) {
           String message = e.getMessage();
           handleComplexTypeError(message,typeNameIndex,typeInfo);
        }


        if (isAbstract.equals(SchemaSymbols.ATTVAL_TRUE) ||
            isAbstract.equals(SchemaSymbols.ATTVAL_TRUE_1))
          typeInfo.setIsAbstractType();
        if (!forwardRef)
          typeInfo.setDeclSeen();
        typeName = fTargetNSURIString + "," + typeName;
        typeInfo.typeName = new String(typeName);

        if ( DEBUGGING )
            System.out.println(">>>add complex Type to Registry: " + typeName +
                               " baseDTValidator=" + typeInfo.baseDataTypeValidator +
                               " baseCTInfo=" + typeInfo.baseComplexTypeInfo +
                               " derivedBy=" + typeInfo.derivedBy +
                             " contentType=" + typeInfo.contentType +
                               " contentSpecHandle=" + typeInfo.contentSpecHandle +
                               " datatypeValidator=" + typeInfo.datatypeValidator +
                               " scopeDefined=" + typeInfo.scopeDefined);

        fComplexTypeRegistry.put(typeName,typeInfo);

        fCurrentScope = previousScope;
        if (savedGroupNameStack != null)
          fCurrentGroupNameStack = savedGroupNameStack;
        fCurrentTypeNameStack.pop();
        checkRecursingComplexType();

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
     *                    | maxInclusive | totalDigits | fractionDigits | length | minLength
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


        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(simpleContentDecl, scope);

        String typeName = fStringPool.toString(typeNameIndex);

        String simpleContentTypeId = simpleContentDecl.getAttribute(SchemaSymbols.ATTVAL_ID);

        typeInfo.contentType = XMLElementDecl.TYPE_SIMPLE;
        typeInfo.contentSpecHandle = -1;

        Element simpleContent = checkContent(simpleContentDecl,
                                     XUtil.getFirstChildElement(simpleContentDecl),false);

        if (simpleContent==null) {
          throw new ComplexTypeRecoverableError();
        }

        attrValues = generalCheck(simpleContent, scope);

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
        Integer finalValue =
                ((Integer)fSimpleTypeFinalRegistry.get(fStringPool.toString(baseQName.uri) + "," +fStringPool.toString(baseQName.localpart)));
        if(finalValue != null &&
                (finalValue.intValue() == typeInfo.derivedBy))
            throw new ComplexTypeRecoverableError(
                  "The simpleType " + base + " that " + typeName + " uses has a value of \"final\" which does not permit extension");

        String baseTypeURI = fStringPool.toString(baseQName.uri);
        String baseLocalName = fStringPool.toString(baseQName.localpart);
        if (baseTypeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) &&
            baseLocalName.equals("anyType")) {
             throw new ComplexTypeRecoverableError(
             "The type '"+ base +"' specified as the " +
             "base in the simpleContent element must be a complex type with simple content");
        }

        processBaseTypeInfo(baseQName,typeInfo);

        if (typeInfo.baseComplexTypeInfo != null)  {
             if (typeInfo.baseComplexTypeInfo.contentType != XMLElementDecl.TYPE_SIMPLE) {
                 throw new ComplexTypeRecoverableError(
                 "The type '"+ base +"' specified as the " +
                 "base in the simpleContent element must not have complexContent");
             }
        }

        Element attrNode = null;
        if (typeInfo.derivedBy==SchemaSymbols.RESTRICTION) {
            if (typeInfo.baseDataTypeValidator != null) {
                throw new ComplexTypeRecoverableError(
                 "ct-props-correct.2: The type '" + base +"' is a simple type.  It cannot be used in a "+
                 "derivation by RESTRICTION for a complexType");
            }
            else {
                typeInfo.baseDataTypeValidator = typeInfo.baseComplexTypeInfo.datatypeValidator;
            }

            if((typeInfo.baseComplexTypeInfo.finalSet & SchemaSymbols.RESTRICTION) != 0) {
               throw new ComplexTypeRecoverableError("Derivation by restriction is forbidden by either the base type " + base + " or the schema");
            }


            if (content != null &&
                content.getLocalName().equals(SchemaSymbols.ELT_SIMPLETYPE )) {
                int simpleTypeNameIndex = traverseSimpleTypeDecl(content);
                if (simpleTypeNameIndex!=-1) {
                    DatatypeValidator dv=fDatatypeRegistry.getDatatypeValidator(
                       fStringPool.toString(simpleTypeNameIndex));

                  if (!checkSimpleTypeDerivationOK(dv,typeInfo.baseDataTypeValidator)) {
                    throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.1.1:  The content type is not a valid restriction of the content type of the base");
                  }
                  typeInfo.baseDataTypeValidator = dv;
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

            scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
            Hashtable contentAttrs;

            for (child = content;
                 child != null && (child.getLocalName().equals(SchemaSymbols.ELT_MINEXCLUSIVE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MININCLUSIVE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MAXEXCLUSIVE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MAXINCLUSIVE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_TOTALDIGITS) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_FRACTIONDIGITS) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_LENGTH) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MINLENGTH) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_MAXLENGTH) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_ENUMERATION) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_PATTERN) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_WHITESPACE) ||
                           child.getLocalName().equals(SchemaSymbols.ELT_ANNOTATION));
                 child = XUtil.getNextSiblingElement(child))
            {
                if ( child.getNodeType() == Node.ELEMENT_NODE ) {
                    Element facetElt = (Element) child;
                    contentAttrs = generalCheck(facetElt, scope);
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
              try{
                typeInfo.datatypeValidator = fDatatypeRegistry.createDatatypeValidator(
                                      typeName,
                                      typeInfo.baseDataTypeValidator, facetData, false);
              } catch (Exception e) {
                throw new ComplexTypeRecoverableError(e.getMessage());
              }

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
            if (typeInfo.baseComplexTypeInfo != null) {
               typeInfo.baseDataTypeValidator = typeInfo.baseComplexTypeInfo.datatypeValidator;
              if((typeInfo.baseComplexTypeInfo.finalSet &
                   SchemaSymbols.EXTENSION) != 0) {
                 throw new ComplexTypeRecoverableError("Derivation by extension is forbidden by either the base type " + base + " or the schema");
              }
            }

            typeInfo.datatypeValidator = typeInfo.baseDataTypeValidator;

            if (content != null)  {
               if (!isAttrOrAttrGroup(content)) {
                   throw new ComplexTypeRecoverableError(
                             "Only annotations and attributes are allowed in the " +
                             "content of an EXTENSION element for a complexType with simpleContent");
               }
               else {
                   attrNode = content;
               }
            }

        }

        int templateElementNameIndex = fStringPool.addSymbol("$"+typeName);

        typeInfo.templateElementIndex = fSchemaGrammar.addElementDecl(
             new QName(-1, templateElementNameIndex,typeNameIndex,fTargetNSURI),
             (fTargetNSURI==StringPool.EMPTY_STRING) ? StringPool.EMPTY_STRING : fCurrentScope, typeInfo.scopeDefined,
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

        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(complexContentDecl, scope);

        String typeName = fStringPool.toString(typeNameIndex);

        String typeId = complexContentDecl.getAttribute(SchemaSymbols.ATTVAL_ID);
        String mixed = complexContentDecl.getAttribute(SchemaSymbols.ATT_MIXED);

        boolean isMixed = mixedOnComplexTypeDecl;
        if (mixed.equals(SchemaSymbols.ATTVAL_TRUE) ||
            mixed.equals(SchemaSymbols.ATTVAL_TRUE_1))
           isMixed = true;
        else if (mixed.equals(SchemaSymbols.ATTVAL_FALSE) ||
                 mixed.equals(SchemaSymbols.ATTVAL_FALSE_0))
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
            (fTargetNSURI==StringPool.EMPTY_STRING) ? StringPool.EMPTY_STRING : fCurrentScope, typeInfo.scopeDefined,
            typeInfo.contentType,
            typeInfo.contentSpecHandle, -1, typeInfo.datatypeValidator);
        return;
    }

    /**
     * Generate a name for an anonymous type
     *
     * @param Element
     * @return String
     */
    private String genAnonTypeName(Element complexTypeDecl) throws Exception {

        String typeName;
        Element node=complexTypeDecl;
        typeName="#AnonType_";
        while (!isTopLevel(node))   {
          node = (Element)node.getParentNode();
          typeName = typeName+node.getAttribute(SchemaSymbols.ATT_NAME);
        }

        return typeName;
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
                        if (fBaseTypeNameStack.search((Object)fullBaseName) > - 1) {
                            throw new ComplexTypeRecoverableError(
                                 "ct-props-correct.3:  Recursive type definition");
                        }
                        fBaseTypeNameStack.push(fullBaseName);
                        baseTypeSymbol = traverseComplexTypeDecl( baseTypeNode, true );
                    fBaseTypeNameStack.pop();
                        baseComplexTypeInfo = (ComplexTypeInfo)
                          fComplexTypeRegistry.get(fStringPool.toString(baseTypeSymbol));
                    }
                    else {
                        baseTypeNode = getTopLevelComponentByName(
                                   SchemaSymbols.ELT_SIMPLETYPE, localpart);
                        if (baseTypeNode != null) {
                            baseTypeSymbol = traverseSimpleTypeDecl( baseTypeNode );
                            baseDTValidator = getDatatypeValidator(typeURI, localpart);
                        }
                    }
                }
            }

        if (baseComplexTypeInfo == null && baseDTValidator == null)  {
            throw new ComplexTypeRecoverableError(
             "src-ct.2: Cannot find type definition for '" + base +"'");
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
       String typeName = fStringPool.toString(typeNameIndex);

       if (complexContentChild != null) {


          String childName = complexContentChild.getLocalName();

          if (childName.equals(SchemaSymbols.ELT_GROUP)) {
               GroupInfo grpInfo = traverseGroupDecl(complexContentChild);
               int groupIndex = (grpInfo != null) ? grpInfo.contentSpecIndex:-2;

               index = handleOccurrences(groupIndex,
                                complexContentChild,
                                hasAllContent(groupIndex) ? GROUP_REF_WITH_ALL :
                                                            NOT_ALL_CONTEXT);
               attrNode = XUtil.getNextSiblingElement(complexContentChild);
           }
           else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
               index = handleOccurrences(traverseSequence(complexContentChild),
                                         complexContentChild);
               attrNode = XUtil.getNextSiblingElement(complexContentChild);
           }
           else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
               index = handleOccurrences(traverseChoice(complexContentChild),
                                          complexContentChild);
               attrNode = XUtil.getNextSiblingElement(complexContentChild);
           }
           else if (childName.equals(SchemaSymbols.ELT_ALL)) {
               index = handleOccurrences(traverseAll(complexContentChild),
                                         complexContentChild,
                                         PROCESSING_ALL_GP);
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

       typeInfo.contentSpecHandle = index;

       if (typeInfo.baseComplexTypeInfo != null) {
           int baseContentSpecHandle = typeInfo.baseComplexTypeInfo.contentSpecHandle;

           if (typeInfo.derivedBy == SchemaSymbols.RESTRICTION) {
              if((typeInfo.baseComplexTypeInfo.finalSet & SchemaSymbols.RESTRICTION) != 0)
                    throw new ComplexTypeRecoverableError("Derivation by restriction is forbidden by either the base type " + fStringPool.toString(baseName.localpart) + " or the schema");

              if (typeInfo.contentSpecHandle==-2) {
                 if (!(typeInfo.baseComplexTypeInfo.contentType==XMLElementDecl.TYPE_EMPTY ||
                     particleEmptiable(baseContentSpecHandle))) {
                    throw new ComplexTypeRecoverableError("derivation-ok-restrictoin.5.2 Content type of complexType is EMPTY but base is not EMPTY or does not have a particle which is emptiable");
                 }
              }


           }
           else {
               if((typeInfo.baseComplexTypeInfo.finalSet & SchemaSymbols.EXTENSION) != 0)
                    throw new ComplexTypeRecoverableError("cos-ct-extends.1.1: Derivation by extension is forbidden by either the base type " + fStringPool.toString(baseName.localpart) + " or the schema");

               if (typeInfo.baseComplexTypeInfo.contentType != XMLElementDecl.TYPE_EMPTY) {
                  if (((typeInfo.baseComplexTypeInfo.contentType == XMLElementDecl.TYPE_CHILDREN) &&
                       isMixed) ||
                      ((typeInfo.baseComplexTypeInfo.contentType == XMLElementDecl.TYPE_MIXED_COMPLEX) &&
                      !isMixed)) {
                    throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.2.2.2.1: The content type of the base type " +
                    fStringPool.toString(baseName.localpart) + " and derived type " +
                    typeName + " must both be mixed or element-only");
                  }

               }

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
                   if (typeInfo.contentSpecHandle > -1 &&
                       (hasAllContent(typeInfo.contentSpecHandle) ||
                        hasAllContent(baseContentSpecHandle))) {
                       throw new ComplexTypeRecoverableError("cos-all-limited.1.2:  An \"all\" model group that is part of a complex type definition must constitute the entire {content type} of the definition.");
                   }

                   typeInfo.contentSpecHandle =
                   fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                     baseContentSpecHandle,
                                                     typeInfo.contentSpecHandle,
                                                     false);
               }

           }
       }
       else {
           typeInfo.derivedBy = SchemaSymbols.RESTRICTION;
       }

       if (isMixed) {


           if (typeInfo.contentSpecHandle == -2) {

             int emptyIndex = fSchemaGrammar.addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_LEAF,
                                                                -1,
                                                                fStringPool.EMPTY_STRING,
                                                                false);
             if (fSchemaGrammar.getDeferContentSpecExpansion()) {
                 fSchemaGrammar.setContentSpecMinOccurs(emptyIndex, 0);
                 fSchemaGrammar.setContentSpecMaxOccurs(emptyIndex, 1);
                 typeInfo.contentSpecHandle = emptyIndex;
             } else {
                 typeInfo.contentSpecHandle = fSchemaGrammar.expandContentModel(emptyIndex,0,1);
             }

             typeInfo.contentType = XMLElementDecl.TYPE_MIXED_SIMPLE;
           }

           else
             typeInfo.contentType = XMLElementDecl.TYPE_MIXED_COMPLEX;
       }
       else if (typeInfo.contentSpecHandle == -2)
           typeInfo.contentType = XMLElementDecl.TYPE_EMPTY;
       else
           typeInfo.contentType = XMLElementDecl.TYPE_CHILDREN;


       int templateElementNameIndex = fStringPool.addSymbol("$"+typeName);

       typeInfo.templateElementIndex = fSchemaGrammar.addElementDecl(
            new QName(-1, templateElementNameIndex,typeNameIndex,fTargetNSURI),
            (fTargetNSURI==StringPool.EMPTY_STRING) ? StringPool.EMPTY_STRING : fCurrentScope, typeInfo.scopeDefined,
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
            else {
                throw new ComplexTypeRecoverableError( "Invalid child among the children of the complexType definition");
            }
        }

        if (attWildcard != null) {
            XMLAttributeDecl fromGroup = null;
            final int count = anyAttDecls.size();
            if ( count > 0) {
                fromGroup = (XMLAttributeDecl) anyAttDecls.elementAt(0);
                for (int i=1; i<count; i++) {
                    fromGroup = AWildCardIntersection(
                                fromGroup,(XMLAttributeDecl)anyAttDecls.elementAt(i));
                }
            }
            if (fromGroup != null) {
                int saveProcessContents = attWildcard.defaultType;
                attWildcard = AWildCardIntersection(attWildcard, fromGroup);
                attWildcard.defaultType = saveProcessContents;
            }
        }
        else {
            if (anyAttDecls.size()>0) {
                attWildcard = (XMLAttributeDecl)anyAttDecls.elementAt(0);
            }
        }
        XMLAttributeDecl baseAttWildcard = null;
        ComplexTypeInfo baseTypeInfo = typeInfo.baseComplexTypeInfo;

        SchemaGrammar aGrammar=null;
        if (baseTypeInfo != null && baseTypeInfo.attlistHead > -1 ) {
            int attDefIndex = baseTypeInfo.attlistHead;
            aGrammar = fSchemaGrammar;
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
                    ||fTempAttributeDecl.type == XMLAttributeDecl.TYPE_ANY_OTHER ) {
                    if (attWildcard == null) {
                        baseAttWildcard = fTempAttributeDecl;
                    }
                    attDefIndex = aGrammar.getNextAttributeDeclIndex(attDefIndex);
                    continue;
                }

                int temp = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, fTempAttributeDecl.name);
                if ( temp > -1) {
                  if (typeInfo.derivedBy==SchemaSymbols.EXTENSION) {
                    throw new ComplexTypeRecoverableError("Attribute " + fStringPool.toString(fTempAttributeDecl.name.localpart) + " that appeared in the base should not appear in a derivation by extension");


                  }
                  else {
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
                reportGenericSchemaError("The intensional intersection for {attribute wildcard}s must be expressible");
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

        if ((typeInfo.derivedBy==SchemaSymbols.RESTRICTION) &&
            (typeInfo.attlistHead>-1 && baseTypeInfo != null)) {
           checkAttributesDerivationOKRestriction(typeInfo.attlistHead,fSchemaGrammar,
               attWildcard,baseTypeInfo.attlistHead,aGrammar,baseAttWildcard);
        }



    private void checkAttributesDerivationOKRestriction(int dAttListHead, SchemaGrammar dGrammar, XMLAttributeDecl dAttWildCard, int bAttListHead, SchemaGrammar bGrammar, XMLAttributeDecl bAttWildCard) throws ComplexTypeRecoverableError {

       int attDefIndex = dAttListHead;

       if (bAttListHead < 0) {
          throw new ComplexTypeRecoverableError("derivation-ok-restriction.2:  Base type definition does not have any attributes");
       }

       while ( attDefIndex > -1 ) {
          fTempAttributeDecl.clear();
          dGrammar.getAttributeDecl(attDefIndex, fTempAttributeDecl);
          if (isAWildCard(fTempAttributeDecl)) {
             attDefIndex = dGrammar.getNextAttributeDeclIndex(attDefIndex);
             continue;
          }
          int bAttDefIndex = bGrammar.findAttributeDecl(bAttListHead, fTempAttributeDecl.name);
          if (bAttDefIndex > -1) {
             fTemp2AttributeDecl.clear();
             bGrammar.getAttributeDecl(bAttDefIndex, fTemp2AttributeDecl);

             if ((fTemp2AttributeDecl.defaultType &
                   XMLAttributeDecl.DEFAULT_TYPE_REQUIRED) > 0 &&
                (fTempAttributeDecl.defaultType &
                   XMLAttributeDecl.DEFAULT_TYPE_REQUIRED) <= 0) {
               throw new ComplexTypeRecoverableError("derivation-ok-restriction.2.1.1:  Attribute '" + fStringPool.toString(fTempAttributeDecl.name.localpart) + "' in derivation has an inconsistent REQUIRED setting to that of attribute in base");
             }

             if (!(checkSimpleTypeDerivationOK(
                  fTempAttributeDecl.datatypeValidator,
                  fTemp2AttributeDecl.datatypeValidator))) {
               throw new ComplexTypeRecoverableError("derivation-ok-restriction.2.1.2:  Type of attribute '" + fStringPool.toString(fTempAttributeDecl.name.localpart) + "' in derivation must be a restriction of type of attribute in base");
             }

             if ((fTemp2AttributeDecl.defaultType &
                   XMLAttributeDecl.DEFAULT_TYPE_FIXED) > 0) {

               if (!((fTempAttributeDecl.defaultType &
                   XMLAttributeDecl.DEFAULT_TYPE_FIXED) > 0) ||
                   !fTempAttributeDecl.defaultValue.equals(fTemp2AttributeDecl.defaultValue)) {

                  throw new ComplexTypeRecoverableError("derivation-ok-restriction.2.1.3:  Attribute '" + fStringPool.toString(fTempAttributeDecl.name.localpart) + "' is either not fixed, or is not fixed with the same value as the attribute in the base");
               }
             }

          }
         else {
             if ((bAttWildCard==null) ||
              !AWildCardAllowsNameSpace(bAttWildCard, dGrammar.getTargetNamespaceURI())) {
                throw new ComplexTypeRecoverableError("derivation-ok-restriction.2.2:  Attribute '" + fStringPool.toString(fTempAttributeDecl.name.localpart) + "' has a target namespace which is not valid with respect to a base type definition's wildcard or, the base does not contain a wildcard");

             }
         }
         attDefIndex = dGrammar.getNextAttributeDeclIndex(attDefIndex);

       }

       if (dAttWildCard!=null) {
          if (bAttWildCard==null) {
            throw new ComplexTypeRecoverableError("derivation-ok-restriction.4.1: An attribute wildcard is present in the derived type, but not the base");
          }

          if (!AWildCardSubset(dAttWildCard,bAttWildCard)) {
            throw new ComplexTypeRecoverableError("derivation-ok-restriction.4.2: The attribute wildcard in the derived type is not a valid subset of that in the base");

          }
       }
    }

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

               int count= fElementRecurseComplex.size();

               for (int i = 0; i<count; i++) {

                 ElementInfo eobj = (ElementInfo)fElementRecurseComplex.elementAt(i);
                 int elementIndex = eobj.elementIndex;
                 String typeName = eobj.typeName;


                 ComplexTypeInfo typeInfo =
                     (ComplexTypeInfo) fComplexTypeRegistry.get(fTargetNSURIString+","+typeName);
                 if (typeInfo==null) {
                    throw new Exception ( "Internal Error in void checkRecursingComplexType(). " );
                 }
                 else {

                   fSchemaGrammar.getElementDecl(elementIndex, fTempElementDecl);
                   fTempElementDecl.type = typeInfo.contentType;
                   fTempElementDecl.contentSpecIndex = typeInfo.contentSpecHandle;
                   fTempElementDecl.datatypeValidator = typeInfo.datatypeValidator;
                   fSchemaGrammar.setElementDecl(elementIndex, fTempElementDecl);

                   fSchemaGrammar.setFirstAttributeDeclIndex(elementIndex,
                        typeInfo.attlistHead);
                   fSchemaGrammar.setElementComplexTypeInfo(elementIndex,typeInfo);
                   fSchemaGrammar.setElementDefinedScope(elementIndex,typeInfo.scopeDefined);
                 }
               }
               fElementRecurseComplex.removeAllElements();
            }
        }
    }


    private void checkParticleDerivationOK(int derivedContentSpecIndex, int derivedScope, int baseContentSpecIndex, int baseScope, ComplexTypeInfo bInfo) throws Exception {


       if (!fFullConstraintChecking)
           return;

       int csIndex1 = derivedContentSpecIndex;
       fSchemaGrammar.getContentSpec(csIndex1, tempContentSpec1);
       int csIndex2 = baseContentSpecIndex;
       fSchemaGrammar.getContentSpec(csIndex2, tempContentSpec2);

       Vector tempVector1 = new Vector();
       Vector tempVector2 = new Vector();

       if (tempContentSpec1.type == XMLContentSpec.CONTENTSPECNODE_SEQ ||
           tempContentSpec1.type == XMLContentSpec.CONTENTSPECNODE_CHOICE ||
           tempContentSpec1.type == XMLContentSpec.CONTENTSPECNODE_ALL) {
         csIndex1 = checkForPointlessOccurrences(csIndex1,tempVector1);
       }
       if (tempContentSpec2.type == XMLContentSpec.CONTENTSPECNODE_SEQ ||
           tempContentSpec2.type == XMLContentSpec.CONTENTSPECNODE_CHOICE ||
           tempContentSpec2.type == XMLContentSpec.CONTENTSPECNODE_ALL) {
         csIndex2 = checkForPointlessOccurrences(csIndex2,tempVector2);
       }

       fSchemaGrammar.getContentSpec(csIndex1, tempContentSpec1);
       fSchemaGrammar.getContentSpec(csIndex2, tempContentSpec2);

       switch (tempContentSpec1.type & 0x0f) {
         case XMLContentSpec.CONTENTSPECNODE_LEAF:
         {
            switch (tempContentSpec2.type & 0x0f) {

              case XMLContentSpec.CONTENTSPECNODE_LEAF:
              {
                 checkNameAndTypeOK(csIndex1, derivedScope, csIndex2, baseScope, bInfo);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_ANY:
              case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER:
              case XMLContentSpec.CONTENTSPECNODE_ANY_NS:
              {
                 checkNSCompat(csIndex1, derivedScope, csIndex2);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_CHOICE:
              case XMLContentSpec.CONTENTSPECNODE_SEQ:
              case XMLContentSpec.CONTENTSPECNODE_ALL:
              {
                 checkRecurseAsIfGroup(csIndex1, derivedScope, csIndex2, tempVector2, baseScope, bInfo);
                 return;
              }

              default:
              {
            throw new ParticleRecoverableError("internal Xerces error");
              }
            }
         }

         case XMLContentSpec.CONTENTSPECNODE_ANY:
         case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER:
         case XMLContentSpec.CONTENTSPECNODE_ANY_NS:
         {
            switch (tempContentSpec2.type & 0x0f) {

              case XMLContentSpec.CONTENTSPECNODE_ANY:
              case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER:
              case XMLContentSpec.CONTENTSPECNODE_ANY_NS:
              {
                 checkNSSubset(csIndex1, csIndex2);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_CHOICE:
              case XMLContentSpec.CONTENTSPECNODE_SEQ:
              case XMLContentSpec.CONTENTSPECNODE_ALL:
              case XMLContentSpec.CONTENTSPECNODE_LEAF:
              {
                 throw new ParticleRecoverableError("cos-particle-restrict: Forbidden restriction: Any: Choice,Seq,All,Elt");
              }

              default:
              {
            throw new ParticleRecoverableError("internal Xerces error");
              }
            }
         }

         case XMLContentSpec.CONTENTSPECNODE_ALL:
         {
            switch (tempContentSpec2.type & 0x0f) {

              case XMLContentSpec.CONTENTSPECNODE_ANY:
              case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER:
              case XMLContentSpec.CONTENTSPECNODE_ANY_NS:
              {
                 checkNSRecurseCheckCardinality(csIndex1, tempVector1, derivedScope, csIndex2);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_ALL:
              {
                 checkRecurse(csIndex1, tempVector1, derivedScope, csIndex2, tempVector2, baseScope, bInfo);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_CHOICE:
              case XMLContentSpec.CONTENTSPECNODE_SEQ:
              case XMLContentSpec.CONTENTSPECNODE_LEAF:
              {
                 throw new ParticleRecoverableError("cos-particle-restrict: Forbidden restriction: All:Choice,Seq,Elt");
              }

              default:
              {
            throw new ParticleRecoverableError("internal Xerces error");
              }
            }
         }

         case XMLContentSpec.CONTENTSPECNODE_CHOICE:
         {
            switch (tempContentSpec2.type & 0x0f) {

              case XMLContentSpec.CONTENTSPECNODE_ANY:
              case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER:
              case XMLContentSpec.CONTENTSPECNODE_ANY_NS:
              {
                 checkNSRecurseCheckCardinality(csIndex1, tempVector1, derivedScope, csIndex2);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_CHOICE:
              {
                 checkRecurseLax(csIndex1, tempVector1, derivedScope, csIndex2, tempVector2, baseScope, bInfo);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_ALL:
              case XMLContentSpec.CONTENTSPECNODE_SEQ:
              case XMLContentSpec.CONTENTSPECNODE_LEAF:
              {
                 throw new ParticleRecoverableError("cos-particle-restrict: Forbidden restriction: Choice:All,Seq,Leaf");
              }

              default:
              {
            throw new ParticleRecoverableError("internal Xerces error");
              }
            }
         }


         case XMLContentSpec.CONTENTSPECNODE_SEQ:
         {
            switch (tempContentSpec2.type & 0x0f) {

              case XMLContentSpec.CONTENTSPECNODE_ANY:
              case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER:
              case XMLContentSpec.CONTENTSPECNODE_ANY_NS:
              {
                 checkNSRecurseCheckCardinality(csIndex1, tempVector1, derivedScope, csIndex2);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_ALL:
              {
                 checkRecurseUnordered(csIndex1, tempVector1, derivedScope, csIndex2, tempVector2, baseScope, bInfo);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_SEQ:
              {
                 checkRecurse(csIndex1, tempVector1, derivedScope, csIndex2, tempVector2, baseScope, bInfo);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_CHOICE:
              {
                 checkMapAndSum(csIndex1, tempVector1, derivedScope, csIndex2, tempVector2, baseScope, bInfo);
                 return;
              }

              case XMLContentSpec.CONTENTSPECNODE_LEAF:
              {
                throw new ParticleRecoverableError("cos-particle-restrict: Forbidden restriction: Seq:Elt");
              }

              default:
              {
            throw new ParticleRecoverableError("internal Xerces error");
              }
            }
         }

       }

    }

    private int checkForPointlessOccurrences(int csIndex, Vector tempVector)  {


       fSchemaGrammar.getContentSpec(csIndex, tempContentSpec1);

       if (tempContentSpec1.otherValue == -2) {
         gatherChildren(tempContentSpec1.type,tempContentSpec1.value,tempVector);
         if (tempVector.size() == 1) {
           Integer returnVal = (Integer)(tempVector.elementAt(0));
           return returnVal.intValue();
         }
         return csIndex;
       }

       int type = tempContentSpec1.type;
       int value = tempContentSpec1.value;
       int otherValue = tempContentSpec1.otherValue;

       gatherChildren(type,value, tempVector);
       gatherChildren(type,otherValue, tempVector);


       return csIndex;
    }


    private void gatherChildren(int parentType, int csIndex, Vector tempVector) {

       fSchemaGrammar.getContentSpec(csIndex, tempContentSpec1);
       int min = fSchemaGrammar.getContentSpecMinOccurs(csIndex);
       int max = fSchemaGrammar.getContentSpecMaxOccurs(csIndex);
       int left = tempContentSpec1.value;
       int right = tempContentSpec1.otherValue;
       int type = tempContentSpec1.type;

       if (type == XMLContentSpec.CONTENTSPECNODE_LEAF ||
           (type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY ||
           (type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY_NS  ||
           (type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER ) {
          tempVector.addElement(new Integer(csIndex));
       }
       else if (! (min==1 && max==1)) {
          tempVector.addElement(new Integer(csIndex));
       }
       else if (right == -2) {
          gatherChildren(type,left,tempVector);
       }
       else if (parentType == type) {
          gatherChildren(type,left,tempVector);
          gatherChildren(type,right,tempVector);
       }
       else {
          tempVector.addElement(new Integer(csIndex));
       }

    }


    private void checkNameAndTypeOK(int csIndex1, int derivedScope, int csIndex2, int baseScope, ComplexTypeInfo bInfo) throws Exception {

      fSchemaGrammar.getContentSpec(csIndex1, tempContentSpec1);
      fSchemaGrammar.getContentSpec(csIndex2, tempContentSpec2);


      int localpart1 = tempContentSpec1.value;
      int uri1 = tempContentSpec1.otherValue;
      int localpart2 = tempContentSpec2.value;
      int uri2 = tempContentSpec2.otherValue;

      int min1 = fSchemaGrammar.getContentSpecMinOccurs(csIndex1);
      int max1 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex1);
      int min2 = fSchemaGrammar.getContentSpecMinOccurs(csIndex2);
      int max2 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex2);

      if (!(localpart1==localpart2 && uri1==uri2)) {

        if (fSComp == null)
           fSComp = new SubstitutionGroupComparator(fGrammarResolver,fStringPool,fErrorReporter);
        if (!checkSubstitutionGroups(localpart1,uri1,localpart2,uri2))
          throw new ParticleRecoverableError("rcase-nameAndTypeOK.1:  Element name/uri in restriction does not match that of corresponding base element");
      }

      if (!checkOccurrenceRange(min1,max1,min2,max2)) {
        throw new ParticleRecoverableError("rcase-nameAndTypeOK.3:  Element occurrence range not a restriction of base element's range: element is " +  fStringPool.toString(localpart1));
      }

      SchemaGrammar aGrammar = fSchemaGrammar;

      String schemaURI = fStringPool.toString(uri1);
      if ( !schemaURI.equals(fTargetNSURIString)
            && schemaURI.length() != 0 )
         aGrammar= (SchemaGrammar) fGrammarResolver.getGrammar(schemaURI);

      int eltndx1 =  findElement(derivedScope, uri1, localpart1, aGrammar, null);
      if (eltndx1 < 0)
         return;

      int eltndx2 = findElement(baseScope, uri2, localpart2, aGrammar, bInfo);
      if (eltndx2 < 0)
         return;

      int miscFlags1 = ((SchemaGrammar) aGrammar).getElementDeclMiscFlags(eltndx1);
      int miscFlags2 = ((SchemaGrammar) aGrammar).getElementDeclMiscFlags(eltndx2);
      boolean element1IsNillable = (miscFlags1 & SchemaSymbols.NILLABLE) !=0;
      boolean element2IsNillable = (miscFlags2 & SchemaSymbols.NILLABLE) !=0;
      boolean element2IsFixed = (miscFlags2 & SchemaSymbols.FIXED) !=0;
      boolean element1IsFixed = (miscFlags1 & SchemaSymbols.FIXED) !=0;
      String element1Value = aGrammar.getElementDefaultValue(eltndx1);
      String element2Value = aGrammar.getElementDefaultValue(eltndx2);

      if (! (element2IsNillable || !element1IsNillable)) {
        throw new ParticleRecoverableError("rcase-nameAndTypeOK.2:  Element " +fStringPool.toString(localpart1) + " is nillable in the restriction but not the base");
      }

      if (! (element2Value == null || !element2IsFixed ||
             (element1IsFixed && element1Value.equals(element2Value)))) {
        throw new ParticleRecoverableError("rcase-nameAndTypeOK.4:  Element " +fStringPool.toString(localpart1) + " is either not fixed, or is not fixed with the same value as in the base");
      }

      int blockSet1 = ((SchemaGrammar) aGrammar).getElementDeclBlockSet(eltndx1);
      int blockSet2 = ((SchemaGrammar) aGrammar).getElementDeclBlockSet(eltndx2);
      if (((blockSet1 & blockSet2)!=blockSet2) ||
            (blockSet1==0 && blockSet2!=0))
        throw new ParticleRecoverableError("rcase-nameAndTypeOK.6:  Element " +fStringPool.toString(localpart1) + "'s disallowed subsitutions are not a superset of those of the base element's");


      aGrammar.getElementDecl(eltndx1, fTempElementDecl);
      aGrammar.getElementDecl(eltndx2, fTempElementDecl2);

      checkIDConstraintRestriction(fTempElementDecl, fTempElementDecl2, aGrammar, localpart1, localpart2);

      checkTypesOK(fTempElementDecl,fTempElementDecl2,eltndx1,eltndx2,aGrammar,fStringPool.toString(localpart1));

    }

    private void checkTypesOK(XMLElementDecl derived, XMLElementDecl base, int dndx, int bndx, SchemaGrammar aGrammar, String elementName) throws Exception {

      ComplexTypeInfo tempType=((SchemaGrammar)aGrammar).getElementComplexTypeInfo(dndx);
      ComplexTypeInfo bType=((SchemaGrammar)aGrammar).getElementComplexTypeInfo(bndx);
      if (derived.type == XMLElementDecl.TYPE_SIMPLE ) {

        if (base.type != XMLElementDecl.TYPE_SIMPLE &&
            base.type != XMLElementDecl.TYPE_ANY)
            throw new ParticleRecoverableError("rcase-nameAndTypeOK.6:  Derived element " + elementName + " has a type that does not derive from that of the base");

        if (tempType == null) {
         if (!(checkSimpleTypeDerivationOK(derived.datatypeValidator,
               base.datatypeValidator)) &&
             !(bType == null && base.datatypeValidator == null))
              throw new ParticleRecoverableError("rcase-nameAndTypeOK.6:  Derived element " + elementName + " has a type that does not derive from that of the base");
         return;
        }
      }

      for(; tempType != null; tempType = tempType.baseComplexTypeInfo) {
        if (bType != null && tempType.typeName.equals(bType.typeName))
          break;
        if (tempType.derivedBy != SchemaSymbols.RESTRICTION) {
          throw new ParticleRecoverableError("rcase-nameAndTypeOK.6:  Derived element " + elementName + " has a type that does not derive from that of the base");
        }
      }

      if(tempType == null && !(bType == null && base.datatypeValidator == null)) {
        throw new ParticleRecoverableError("rcase-nameAndTypeOK.6:  Derived element " + elementName + " has a type that does not derive from that of the base");
      }
    }

    private void checkIDConstraintRestriction(XMLElementDecl derivedElemDecl, XMLElementDecl baseElemDecl,
                SchemaGrammar grammar, int derivedElemName, int baseElemName) throws Exception {
        Vector derivedUnique = derivedElemDecl.unique;
        Vector baseUnique = baseElemDecl.unique;
        if(derivedUnique.size() > baseUnique.size()) {
            throw new ParticleRecoverableError("rcase-nameAndTypeOK.5:  derived element " +
                    fStringPool.toString(derivedElemName) +
                    " has fewer <unique> Identity Constraints than the base element"+
                    fStringPool.toString(baseElemName));
        } else {
            boolean found = true;
            for(int i=0; i<derivedUnique.size() && found; i++) {
                Unique id = (Unique)derivedUnique.elementAt(i);
                found = false;
                for(int j=0; j<baseUnique.size(); j++) {
                    if(id.equals((Unique)baseUnique.elementAt(j))) {
                        found = true;
                        break;
                    }
                }
            }
            if(!found) {
                throw new ParticleRecoverableError("rcase-nameAndTypeOK.5:  derived element " +
                    fStringPool.toString(derivedElemName) +
                    " has a <unique> Identity Constraint that does not appear on the base element"+
                    fStringPool.toString(baseElemName));
            }
        }

        Vector derivedKey = derivedElemDecl.key;
        Vector baseKey = baseElemDecl.key;
        if(derivedKey.size() > baseKey.size()) {
            throw new ParticleRecoverableError("rcase-nameAndTypeOK.5:  derived element " +
                    fStringPool.toString(derivedElemName) +
                    " has fewer <key> Identity Constraints than the base element"+
                    fStringPool.toString(baseElemName));
        } else {
            boolean found = true;
            for(int i=0; i<derivedKey.size() && found; i++) {
                Key id = (Key)derivedKey.elementAt(i);
                found = false;
                for(int j=0; j<baseKey.size(); j++) {
                    if(id.equals((Key)baseKey.elementAt(j))) {
                        found = true;
                        break;
                    }
                }
            }
            if(!found) {
                throw new ParticleRecoverableError("rcase-nameAndTypeOK.5:  derived element " +
                    fStringPool.toString(derivedElemName) +
                    " has a <key> Identity Constraint that does not appear on the base element"+
                    fStringPool.toString(baseElemName));
            }
        }

        Vector derivedKeyRef = derivedElemDecl.keyRef;
        Vector baseKeyRef = baseElemDecl.keyRef;
        if(derivedKeyRef.size() > baseKeyRef.size()) {
            throw new ParticleRecoverableError("rcase-nameAndTypeOK.5:  derived element " +
                    fStringPool.toString(derivedElemName) +
                    " has fewer <keyref> Identity Constraints than the base element"+
                    fStringPool.toString(baseElemName));
        } else {
            boolean found = true;
            for(int i=0; i<derivedKeyRef.size() && found; i++) {
                KeyRef id = (KeyRef)derivedKeyRef.elementAt(i);
                found = false;
                for(int j=0; j<baseKeyRef.size(); j++) {
                    if(id.equals((KeyRef)baseKeyRef.elementAt(j))) {
                        found = true;
                        break;
                    }
                }
            }
            if(!found) {
                throw new ParticleRecoverableError("rcase-nameAndTypeOK.5:  derived element " +
                    fStringPool.toString(derivedElemName) +
                    " has a <keyref> Identity Constraint that does not appear on the base element"+
                    fStringPool.toString(baseElemName));
            }
        }

    private boolean checkSubstitutionGroups(int local1, int uri1, int local2, int uri2)
throws Exception {

       QName name1 = new QName(-1,local1,local1,uri1);
       QName name2 = new QName(-1,local2,local2,uri2);

       if (fSComp.isEquivalentTo(name1,name2) ||
           fSComp.isEquivalentTo(name2,name1))
         return true;
       else
         return false;

    }
    private boolean checkOccurrenceRange(int min1, int max1, int min2, int max2) {

      if ((min1 >= min2) &&
          ((max2==SchemaSymbols.OCCURRENCE_UNBOUNDED) || (max1!=SchemaSymbols.OCCURRENCE_UNBOUNDED && max1<=max2)))
        return true;
      else
        return false;
    }

    private int findElement(int scope, int uriIndex, int nameIndex, SchemaGrammar gr, ComplexTypeInfo bInfo) {

      int elementDeclIndex = gr.getElementDeclIndex(uriIndex,nameIndex,scope);

      if (elementDeclIndex == -1) {
         elementDeclIndex = gr.getElementDeclIndex(nameIndex, -1);

         if (elementDeclIndex == -1 && bInfo != null) {
            ComplexTypeInfo baseInfo = bInfo;
            while (baseInfo != null) {
                elementDeclIndex = gr.getElementDeclIndex(nameIndex,baseInfo.scopeDefined);
                if (elementDeclIndex > -1)
                   break;
                baseInfo = baseInfo.baseComplexTypeInfo;
            }
         }

      }
      return elementDeclIndex;
    }

    private void checkNSCompat(int csIndex1, int derivedScope, int csIndex2) throws Exception {

      int min1 = fSchemaGrammar.getContentSpecMinOccurs(csIndex1);
      int max1 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex1);
      int min2 = fSchemaGrammar.getContentSpecMinOccurs(csIndex2);
      int max2 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex2);

      if (!checkOccurrenceRange(min1,max1,min2,max2)) {
        throw new ParticleRecoverableError("rcase-NSCompat.2:  Element occurrence range not a restriction of base any element's range");
      }


      fSchemaGrammar.getContentSpec(csIndex1, tempContentSpec1);
      int uri = tempContentSpec1.otherValue;

      if (!wildcardEltAllowsNamespace(csIndex2, uri))
        throw new ParticleRecoverableError("rcase-NSCompat.1:  Element's namespace not allowed by wildcard in base");

    }

    private boolean wildcardEltAllowsNamespace(int wildcardNode, int uriIndex) {

      fSchemaGrammar.getContentSpec(wildcardNode, tempContentSpec1);
      if ((tempContentSpec1.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY)
        return true;

      if ((tempContentSpec1.type & 0x0f)==XMLContentSpec.CONTENTSPECNODE_ANY_NS) {
        if (uriIndex == tempContentSpec1.otherValue)
           return true;
      }
        if (uriIndex != tempContentSpec1.otherValue && uriIndex != StringPool.EMPTY_STRING)
            return true;
      }

      return false;
    }


    private void checkNSSubset(int csIndex1, int csIndex2) throws Exception {
      int min1 = fSchemaGrammar.getContentSpecMinOccurs(csIndex1);
      int max1 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex1);
      int min2 = fSchemaGrammar.getContentSpecMinOccurs(csIndex2);
      int max2 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex2);

      if (!checkOccurrenceRange(min1,max1,min2,max2)) {
        throw new ParticleRecoverableError("rcase-NSSubset.2:  Wildcard's occurrence range not a restriction of base wildcard's range");
      }

      if (!wildcardEltSubset(csIndex1, csIndex2))
         throw new ParticleRecoverableError("rcase-NSSubset.1:  Wildcard is not a subset of corresponding wildcard in base");


    }

    private boolean wildcardEltSubset(int wildcardNode, int wildcardBaseNode) {

      fSchemaGrammar.getContentSpec(wildcardNode, tempContentSpec1);
      fSchemaGrammar.getContentSpec(wildcardBaseNode, tempContentSpec2);

      if ((tempContentSpec2.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY)
        return true;

      if ((tempContentSpec1.type & 0x0f)==XMLContentSpec.CONTENTSPECNODE_ANY_OTHER) {
        if ((tempContentSpec2.type & 0x0f)==XMLContentSpec.CONTENTSPECNODE_ANY_OTHER &&
            tempContentSpec1.otherValue == tempContentSpec2.otherValue)
           return true;
      }

      if ((tempContentSpec1.type & 0x0f)==XMLContentSpec.CONTENTSPECNODE_ANY_NS) {
        if ((tempContentSpec2.type & 0x0f)==XMLContentSpec.CONTENTSPECNODE_ANY_NS &&
            tempContentSpec1.otherValue == tempContentSpec2.otherValue)
           return true;

        if ((tempContentSpec2.type & 0x0f)==XMLContentSpec.CONTENTSPECNODE_ANY_OTHER &&
            tempContentSpec1.otherValue != tempContentSpec2.otherValue)
           return true;

      }

      return false;
    }

    private void checkRecurseAsIfGroup(int csIndex1, int derivedScope, int csIndex2, Vector tempVector2, int baseScope, ComplexTypeInfo bInfo) throws Exception {

      fSchemaGrammar.getContentSpec(csIndex2, tempContentSpec2);

      int indexOfGrp=fSchemaGrammar.addContentSpecNode(tempContentSpec2.type,
                             csIndex1,-2, false);
      Vector tmpVector = new Vector();
      tmpVector.addElement(new Integer(csIndex1));


      if (tempContentSpec2.type == XMLContentSpec.CONTENTSPECNODE_ALL ||
          tempContentSpec2.type == XMLContentSpec.CONTENTSPECNODE_SEQ)
         checkRecurse(indexOfGrp, tmpVector, derivedScope, csIndex2,
                      tempVector2, baseScope, bInfo);
      else
         checkRecurseLax(indexOfGrp, tmpVector, derivedScope, csIndex2,
                         tempVector2, baseScope, bInfo);

      tmpVector = null;
    }

    private void checkNSRecurseCheckCardinality(int csIndex1, Vector tempVector1, int derivedScope, int csIndex2) throws Exception {

      int min1 = minEffectiveTotalRange(csIndex1);
      int max1 = maxEffectiveTotalRange(csIndex1);

      int min2 = fSchemaGrammar.getContentSpecMinOccurs(csIndex2);
      int max2 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex2);

      if (!checkOccurrenceRange(min1,max1,min2,max2)) {
        throw new ParticleRecoverableError("rcase-NSSubset.2:  Wildcard's occurrence range not a restriction of base wildcard's range");
      }

      if (!wildcardEltSubset(csIndex1, csIndex2))
         throw new ParticleRecoverableError("rcase-NSSubset.1:  Wildcard is not a subset of corresponding wildcard in base");
      int count = tempVector1.size();
      for (int i = 0; i < count; i++) {
         Integer particle1 = (Integer)tempVector1.elementAt(i);
         checkParticleDerivationOK(particle1.intValue(),derivedScope,csIndex2,-1,null);
      }

    }

    private void checkRecurse(int csIndex1, Vector tempVector1, int derivedScope, int csIndex2, Vector tempVector2, int baseScope, ComplexTypeInfo bInfo) throws Exception {

      int min1 = fSchemaGrammar.getContentSpecMinOccurs(csIndex1);
      int max1 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex1);
      int min2 = fSchemaGrammar.getContentSpecMinOccurs(csIndex2);
      int max2 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex2);

      if (!checkOccurrenceRange(min1,max1,min2,max2)) {
        throw new ParticleRecoverableError("rcase-Recurse.1:  Occurrence range of group is not a valid restriction of occurence range of base group");
      }

      int count1= tempVector1.size();
      int count2= tempVector2.size();

      int current = 0;
      label: for (int i = 0; i<count1; i++) {

        Integer particle1 = (Integer)tempVector1.elementAt(i);
        for (int j = current; j<count2; j++) {
           Integer particle2 = (Integer)tempVector2.elementAt(j);
           current +=1;
           try {
             checkParticleDerivationOK(particle1.intValue(),derivedScope,
                particle2.intValue(), baseScope, bInfo);
             continue label;
           }
           catch (ParticleRecoverableError e) {
             if (!particleEmptiable(particle2.intValue()))
                throw new ParticleRecoverableError("rcase-Recurse.2:  There is not a complete functional mapping between the particles");
           }
        }
        throw new ParticleRecoverableError("rcase-Recurse.2:  There is not a complete functional mapping between the particles");
      }

      for (int j=current; j < count2; j++) {
        Integer particle2 = (Integer)tempVector2.elementAt(j);
        if (!particleEmptiable(particle2.intValue())) {
          throw new ParticleRecoverableError("rcase-Recurse.2:  There is not a complete functional mapping between the particles");
        }
      }

    }

    private void checkRecurseUnordered(int csIndex1, Vector tempVector1, int derivedScope, int csIndex2, Vector tempVector2, int baseScope, ComplexTypeInfo bInfo) throws Exception {
      int min1 = fSchemaGrammar.getContentSpecMinOccurs(csIndex1);
      int max1 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex1);
      int min2 = fSchemaGrammar.getContentSpecMinOccurs(csIndex2);
      int max2 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex2);

      if (!checkOccurrenceRange(min1,max1,min2,max2)) {
        throw new ParticleRecoverableError("rcase-RecurseUnordered.1:  Occurrence range of group is not a valid restriction of occurence range of base group");
      }

      int count1= tempVector1.size();
      int count2 = tempVector2.size();

      boolean foundIt[] = new boolean[count2];

      label: for (int i = 0; i<count1; i++) {

        Integer particle1 = (Integer)tempVector1.elementAt(i);
        for (int j = 0; j<count2; j++) {
           Integer particle2 = (Integer)tempVector2.elementAt(j);
           try {
             checkParticleDerivationOK(particle1.intValue(),derivedScope,
                particle2.intValue(), baseScope, bInfo);
             if (foundIt[j])
            throw new ParticleRecoverableError("rcase-RecurseUnordered.2:  There is not a complete functional mapping between the particles");
             else
                foundIt[j]=true;

             continue label;
           }
           catch (ParticleRecoverableError e) {
           }
        }
    throw new ParticleRecoverableError("rcase-RecurseUnordered.2:  There is not a complete functional mapping between the particles");
      }

    }

    private void checkRecurseLax(int csIndex1, Vector tempVector1, int derivedScope, int csIndex2, Vector tempVector2, int baseScope, ComplexTypeInfo bInfo) throws Exception {

      int min1 = fSchemaGrammar.getContentSpecMinOccurs(csIndex1);
      int max1 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex1);
      int min2 = fSchemaGrammar.getContentSpecMinOccurs(csIndex2);
      int max2 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex2);

      if (!checkOccurrenceRange(min1,max1,min2,max2)) {
        throw new ParticleRecoverableError("rcase-RecurseLax.1:  Occurrence range of group is not a valid restriction of occurence range of base group");
      }

      int count1= tempVector1.size();
      int count2 = tempVector2.size();

      int current = 0;
      label: for (int i = 0; i<count1; i++) {

        Integer particle1 = (Integer)tempVector1.elementAt(i);
        for (int j = current; j<count2; j++) {
           Integer particle2 = (Integer)tempVector2.elementAt(j);
           current +=1;
           try {
             checkParticleDerivationOK(particle1.intValue(),derivedScope,
                particle2.intValue(), baseScope, bInfo);
             continue label;
           }
           catch (ParticleRecoverableError e) {
           }
        }
    throw new ParticleRecoverableError("rcase-Recurse.2:  There is not a complete functional mapping between the particles");

      }

    }

    private void checkMapAndSum(int csIndex1, Vector tempVector1, int derivedScope, int csIndex2, Vector tempVector2, int baseScope, ComplexTypeInfo bInfo) throws Exception {




      int count1 = tempVector1.size();
      int count2 = tempVector2.size();
      int min1 = fSchemaGrammar.getContentSpecMinOccurs(csIndex1) * count1;
      int max1 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex1);
      if (max1!=SchemaSymbols.OCCURRENCE_UNBOUNDED)
        max1 = max1 * count1;
      int min2 = fSchemaGrammar.getContentSpecMinOccurs(csIndex2);
      int max2 = fSchemaGrammar.getContentSpecMaxOccurs(csIndex2);

      if (!checkOccurrenceRange(min1,max1,min2,max2)) {
        throw new ParticleRecoverableError("rcase-MapAndSum.2:  Occurrence range of group is not a valid restriction of occurence range of base group");
      }

      label: for (int i = 0; i<count1; i++) {

        Integer particle1 = (Integer)tempVector1.elementAt(i);
        for (int j = 0; j<count2; j++) {
           Integer particle2 = (Integer)tempVector2.elementAt(j);
           try {
             checkParticleDerivationOK(particle1.intValue(),derivedScope,
                particle2.intValue(), baseScope, bInfo);

             continue label;
           }
           catch (ParticleRecoverableError e) {
           }
        }
    throw new ParticleRecoverableError("rcase-MapAndSum.1:  There is not a complete functional mapping between the particles");
      }
    }

    private int importContentSpec(SchemaGrammar aGrammar, int contentSpecHead ) throws Exception {
        XMLContentSpec ctsp = new XMLContentSpec();
        aGrammar.getContentSpec(contentSpecHead, ctsp);
        int left = -1;
        int right = -1;
        if ( ctsp.type == ctsp.CONTENTSPECNODE_LEAF
             || (ctsp.type & 0x0f) == ctsp.CONTENTSPECNODE_ANY
             || (ctsp.type & 0x0f) == ctsp.CONTENTSPECNODE_ANY_NS
             || (ctsp.type & 0x0f) == ctsp.CONTENTSPECNODE_ANY_OTHER ) {
            left = fSchemaGrammar.addContentSpecNode(ctsp.type, ctsp.value, ctsp.otherValue, false);
        }
        else if (ctsp.type == -1)
            return -2;
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
            left = fSchemaGrammar.addContentSpecNode(ctsp.type, left, right, false);
        }

        if (fFullConstraintChecking) {
            fSchemaGrammar.setContentSpecMaxOccurs(left, aGrammar.getContentSpecMaxOccurs(contentSpecHead));
            fSchemaGrammar.setContentSpecMinOccurs(left, aGrammar.getContentSpecMinOccurs(contentSpecHead));
        }
        return left;
    }

    private int handleOccurrences(int index,
                                  Element particle) throws Exception {
        return handleOccurrences(index, particle, NOT_ALL_CONTEXT);
    }

    private int handleOccurrences(int index, Element particle,
                                  int allContextFlags) throws Exception {

        if (index < 0)
          return index;

        String minOccurs =
                    particle.getAttribute(SchemaSymbols.ATT_MINOCCURS).trim();
        String maxOccurs =
                    particle.getAttribute(SchemaSymbols.ATT_MAXOCCURS).trim();
        boolean processingAllEl = ((allContextFlags & PROCESSING_ALL_EL) != 0);
        boolean processingAllGP = ((allContextFlags & PROCESSING_ALL_GP) != 0);
        boolean groupRefWithAll = ((allContextFlags & GROUP_REF_WITH_ALL) != 0);
        boolean isGroupChild    = ((allContextFlags & CHILD_OF_GROUP) != 0);

        if (isGroupChild &&
            (minOccurs.length() != 0 || maxOccurs.length() != 0)) {
            Element group = (Element)particle.getParentNode();
            Object[] args = new Object[]{group.getAttribute(SchemaSymbols.ATT_NAME),
                                         particle.getNodeName()};
            reportSchemaError(SchemaMessageProvider.MinMaxOnGroupChild, args);
            minOccurs = (maxOccurs = "1");
        }

        if(minOccurs.equals("0") && maxOccurs.equals("0")){
            return -2;
        }

        int min=1, max=1;

        if (minOccurs.length() == 0) {
            minOccurs = "1";
        }
        if (maxOccurs.length() == 0) {
            maxOccurs = "1";
        }

        if (processingAllEl || groupRefWithAll || processingAllGP) {
            if ((processingAllGP||groupRefWithAll||!minOccurs.equals("0")) &&
                 !minOccurs.equals("1")) {
                int minMsg;

                if (processingAllEl) {
                    minMsg = SchemaMessageProvider.BadMinMaxForAllElem;
                }
                else if (processingAllGP) {
                    minMsg = SchemaMessageProvider.BadMinMaxForAllGp;
                }
                else {
                    minMsg = SchemaMessageProvider.BadMinMaxForGroupWithAll;
                }

                reportSchemaError(minMsg, new Object [] { "minOccurs",
                                                          minOccurs });
                minOccurs = "1";
            }

            if (!maxOccurs.equals("1")) {
                int maxMsg;

                if (processingAllEl) {
                    maxMsg = SchemaMessageProvider.BadMinMaxForAllElem;
                }
                else if (processingAllGP) {
                    maxMsg = SchemaMessageProvider.BadMinMaxForAllGp;
                }
                else {
                    maxMsg = SchemaMessageProvider.BadMinMaxForGroupWithAll;
                }

                reportSchemaError(maxMsg, new Object [] { "maxOccurs",
                                                          maxOccurs });
                maxOccurs = "1";
            }
        }

        try {
            min = Integer.parseInt(minOccurs);
        }
        catch (Exception e){
            reportSchemaError(SchemaMessageProvider.GenericError,
                              new Object [] { "illegal value for minOccurs or maxOccurs : '" +e.getMessage()+ "' "});
        }

        if (maxOccurs.equals("unbounded")) {
           max = SchemaSymbols.OCCURRENCE_UNBOUNDED;
        }
        else {
           try {
               max = Integer.parseInt(maxOccurs);
           }
           catch (Exception e){
               reportSchemaError(SchemaMessageProvider.GenericError,
                                new Object [] { "illegal value for minOccurs or maxOccurs : '" +e.getMessage()+ "' "});
           }

           if (min > max) {
               reportGenericSchemaError("p-props-correct:2.1 Value of minOccurs '" + minOccurs + "' must not be greater than value of maxOccurs '" + maxOccurs +"'");
           }

           if (max < 1) {
               reportGenericSchemaError("p-props-correct:2.2 Value of maxOccurs " + maxOccurs + " is invalid.  It must be greater than or equal to 1");
           }
        }

        if (fSchemaGrammar.getDeferContentSpecExpansion()) {
          fSchemaGrammar.setContentSpecMinOccurs(index,min);
          fSchemaGrammar.setContentSpecMaxOccurs(index,max);
          return index;
        }
        else {
          return fSchemaGrammar.expandContentModel(index,min,max);
        }

    }


    /**
     * Traverses Schema attribute declaration.
     *
     *       <attribute
     *         default = string
     *         fixed = string
     *         form = (qualified | unqualified)
     *         id = ID
     *         name = NCName
     *         ref = QName
     *         type = QName
     *         use = (optional | prohibited | required) : optional
     *         {any attributes with non-schema namespace ...}>
     *         Content: (annotation? , simpleType?)
     *       <attribute/>
     *
     * @param attributeDecl: the declaration of the attribute under consideration
     * @param typeInfo: Contains the index of the element to which the attribute declaration is attached.
     * @param referredTo:  true iff traverseAttributeDecl was called because
     *		of encountering a ``ref''property (used
     *		to suppress error-reporting).
     * @return 0 if the attribute schema is validated successfully, otherwise -1
     * @exception Exception
     */
    private int traverseAttributeDecl( Element attrDecl, ComplexTypeInfo typeInfo, boolean referredTo ) throws Exception {

        int scope = isTopLevel(attrDecl)?
                    GeneralAttrCheck.ELE_CONTEXT_GLOBAL:
                    GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(attrDecl, scope);

        String defaultStr   = attrDecl.getAttribute(SchemaSymbols.ATT_DEFAULT);
        String fixedStr     = attrDecl.getAttribute(SchemaSymbols.ATT_FIXED);
        String attNameStr    = attrDecl.getAttribute(SchemaSymbols.ATT_NAME);
        String refStr       = attrDecl.getAttribute(SchemaSymbols.ATT_REF);
        String datatypeStr  = attrDecl.getAttribute(SchemaSymbols.ATT_TYPE);
        String useStr       = attrDecl.getAttribute(SchemaSymbols.ATT_USE);
        Element simpleTypeChild = findAttributeSimpleType(attrDecl);

        Attr defaultAtt   = attrDecl.getAttributeNode(SchemaSymbols.ATT_DEFAULT);
        Attr fixedAtt     = attrDecl.getAttributeNode(SchemaSymbols.ATT_FIXED);
        Attr formAtt      = attrDecl.getAttributeNode(SchemaSymbols.ATT_FORM);
        Attr attNameAtt    = attrDecl.getAttributeNode(SchemaSymbols.ATT_NAME);
        Attr refAtt       = attrDecl.getAttributeNode(SchemaSymbols.ATT_REF);
        Attr datatypeAtt  = attrDecl.getAttributeNode(SchemaSymbols.ATT_TYPE);
        Attr useAtt       = attrDecl.getAttributeNode(SchemaSymbols.ATT_USE);

        checkEnumerationRequiredNotation(attNameStr, datatypeStr);


        int attType;
        boolean attIsList    = false;
        int dataTypeSymbol   = -1;
        String localpart = null;

        DatatypeValidator dv;
        boolean dvIsDerivedFromID = false;

        int attValueAndUseType = 0;

        boolean isAttrTopLevel = isTopLevel(attrDecl);
        boolean isOptional = false;
        boolean isProhibited = false;
        boolean isRequired = false;

        StringBuffer errorContext = new StringBuffer(30);
        errorContext.append(" -- ");
        if(typeInfo == null) {
            errorContext.append("(global attribute) ");
        }
        else if(typeInfo.typeName == null) {
            errorContext.append("(local attribute) ");
        }
        else {
            errorContext.append("(attribute) ").append(typeInfo.typeName).append("/");
        }
        errorContext.append(attNameStr).append(' ').append(refStr);

        if(useStr.length() == 0 || useStr.equals(SchemaSymbols.ATTVAL_OPTIONAL)) {
            attValueAndUseType |= XMLAttributeDecl.USE_TYPE_OPTIONAL;
            isOptional = true;
        }
        else if(useStr.equals(SchemaSymbols.ATTVAL_PROHIBITED)) {
            attValueAndUseType |= XMLAttributeDecl.USE_TYPE_PROHIBITED;
            isProhibited = true;
        }
        else if(useStr.equals(SchemaSymbols.ATTVAL_REQUIRED)) {
            attValueAndUseType |= XMLAttributeDecl.USE_TYPE_REQUIRED;
            isRequired = true;
        }
        else {
            reportGenericSchemaError("An attribute cannot declare \"" +
                SchemaSymbols.ATT_USE + "\" as \"" + useStr + "\"" + errorContext);
        }

        if(defaultAtt != null && fixedAtt != null) {
            reportGenericSchemaError("src-attribute.1: \"" + SchemaSymbols.ATT_DEFAULT +
                "\" and \"" + SchemaSymbols.ATT_FIXED +
                "\" cannot be both present" + errorContext);
        }
        else if(defaultAtt != null && !isOptional) {
            reportGenericSchemaError("src-attribute.2: If both \"" + SchemaSymbols.ATT_DEFAULT +
                "\" and \"" + SchemaSymbols.ATT_USE + "\" " +
                "are present for an attribute declaration, \"" +
                SchemaSymbols.ATT_USE + "\" can only be \"" +
                SchemaSymbols.ATTVAL_OPTIONAL + "\", not \"" + useStr + "\"." + errorContext);
        }

        if(!isAttrTopLevel) {
            if((refAtt == null) == (attNameAtt == null)) {
                reportGenericSchemaError("src-attribute.3.1: When the attribute's parent is not <schema> , one of \"" +
                    SchemaSymbols.ATT_REF + "\" and \""  + SchemaSymbols.ATT_NAME +
                    "\" should be declared, but not both."+ errorContext);
                return -1;
            }
            else if((refAtt != null) && (simpleTypeChild != null || formAtt != null || datatypeAtt != null)) {
                reportGenericSchemaError("src-attribute.3.2: When the attribute's parent is not <schema> and \"" +
                    SchemaSymbols.ATT_REF + "\" is present, " +
                    "all of <" + SchemaSymbols.ELT_SIMPLETYPE + ">, " +
                    SchemaSymbols.ATT_FORM + " and "  + SchemaSymbols.ATT_TYPE +
                    " must be absent."+ errorContext);
            }
        }

        if(datatypeAtt != null && simpleTypeChild != null) {
            reportGenericSchemaError("src-attribute.4: \"" + SchemaSymbols.ATT_TYPE + "\" and <" +
                SchemaSymbols.ELT_SIMPLETYPE + "> cannot both be present"+ errorContext);
        }


        if (isAttrTopLevel) {
            attName  = fStringPool.addSymbol(attNameStr);
            if(fTargetNSURIString.length() == 0) {
                uriIndex = StringPool.EMPTY_STRING;
            }
            else {
                uriIndex = fTargetNSURI;
            }

            attQName = new QName();
            attQName.setValues(-1,attName,attName,uriIndex);

        }
        else if(refAtt == null) {
            attName  = fStringPool.addSymbol(attNameStr);
            if((formStr.length() > 0 && formStr.equals(SchemaSymbols.ATTVAL_QUALIFIED)) ||
                (formStr.length() == 0 && fAttributeDefaultQualified)) {
                uriIndex = fTargetNSURI;
            }
            else {
                uriIndex = StringPool.EMPTY_STRING;
            }
            attQName = new QName();
            attQName.setValues(-1,attName,attName,uriIndex);
        }
        else {
            String prefix;
            int colonptr = refStr.indexOf(":");
            if ( colonptr > 0) {
                prefix = refStr.substring(0,colonptr);
                localpart = refStr.substring(colonptr+1);
            }
            else {
                prefix = "";
                localpart = refStr;
            }

            String uriStr = resolvePrefixToURI(prefix);

            if (!uriStr.equals(fTargetNSURIString)) {
                addAttributeDeclFromAnotherSchema(localpart, uriStr, typeInfo, defaultAtt != null, fixedAtt != null, fixedStr, attValueAndUseType);
                return 0;
            }

            Element referredAttribute = getTopLevelComponentByName(SchemaSymbols.ELT_ATTRIBUTE,localpart);
            if (referredAttribute != null) {
                    traverseAttributeDecl(referredAttribute, typeInfo, true);

                Attr referFixedAttr = referredAttribute.getAttributeNode(SchemaSymbols.ATT_FIXED);
                String referFixed = referFixedAttr == null ? null : referFixedAttr.getValue();
                if (referFixed != null && (defaultAtt != null || fixedAtt != null && !referFixed.equals(fixedStr))) {
                    reportGenericSchemaError("au-props-correct.2: If the {attribute declaration} has a fixed {value constraint}, then if the attribute use itself has a {value constraint}, it must also be fixed and its value must match that of the {attribute declaration}'s {value constraint}" + errorContext);
                }

                if((typeInfo != null) && (!isOptional || fixedStr.length() > 0)) {
                    int referredAttName = fStringPool.addSymbol(referredAttribute.getAttribute(SchemaSymbols.ATT_NAME));
                    uriIndex = StringPool.EMPTY_STRING;
                    if ( fTargetNSURIString.length() > 0) {
                        uriIndex = fTargetNSURI;
                    }
                    QName referredAttQName = new QName(-1,referredAttName,referredAttName,uriIndex);

                    int tempIndex = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, referredAttQName);
                    XMLAttributeDecl referredAttrDecl = new XMLAttributeDecl();
                    fSchemaGrammar.getAttributeDecl(tempIndex, referredAttrDecl);

                    boolean updated = false;

                    int useDigits =   XMLAttributeDecl.USE_TYPE_OPTIONAL |
                                      XMLAttributeDecl.USE_TYPE_PROHIBITED |
                                      XMLAttributeDecl.USE_TYPE_REQUIRED;

                    int valueDigits = XMLAttributeDecl.VALUE_CONSTRAINT_DEFAULT |
                                      XMLAttributeDecl.VALUE_CONSTRAINT_FIXED;

                    if(!isOptional &&
                       (referredAttrDecl.defaultType & useDigits) !=
                       (attValueAndUseType & useDigits))
                    {
                        if(referredAttrDecl.defaultType != XMLAttributeDecl.USE_TYPE_PROHIBITED) {
                                referredAttrDecl.defaultType |= useDigits;
                            referredAttrDecl.defaultType |= (attValueAndUseType & useDigits);
                            updated = true;
                        }
                    }

                    if(fixedStr.length() > 0) {
                        if((referredAttrDecl.defaultType & XMLAttributeDecl.VALUE_CONSTRAINT_FIXED) == 0) {
                            referredAttrDecl.defaultType |= valueDigits;
                            referredAttrDecl.defaultType |= XMLAttributeDecl.VALUE_CONSTRAINT_FIXED;
                            referredAttrDecl.defaultValue = fixedStr;
                            updated = true;
                        }
                    }

                    if(updated) {
                        fSchemaGrammar.setAttributeDecl(typeInfo.templateElementIndex, tempIndex, referredAttrDecl);
                    }
                }
            }
            else if (fAttributeDeclRegistry.get(localpart) != null) {
                addAttributeDeclFromAnotherSchema(localpart, uriStr, typeInfo, defaultAtt != null, fixedAtt != null, fixedStr, attValueAndUseType);
            }
            else {
                reportGenericSchemaError ( "Couldn't find top level attribute " + refStr + errorContext);
            }
            return 0;
            }

        if (uriIndex == fXsiURI) {
            reportGenericSchemaError("no-xsi: The {target namespace} of an attribute declaration must not match " + SchemaSymbols.URI_XSI + errorContext);
        }

        if (simpleTypeChild != null) {
            attType        = XMLAttributeDecl.TYPE_SIMPLE;
            dataTypeSymbol = traverseSimpleTypeDecl(simpleTypeChild);
            localpart = fStringPool.toString(dataTypeSymbol);
            dv = fDatatypeRegistry.getDatatypeValidator(localpart);
        }
        else if (datatypeStr.length() != 0) {
            dataTypeSymbol = fStringPool.addSymbol(datatypeStr);
            String prefix;
            int  colonptr = datatypeStr.indexOf(":");
            if ( colonptr > 0) {
                prefix = datatypeStr.substring(0,colonptr);
                localpart = datatypeStr.substring(colonptr+1);
            }
            else {
                prefix = "";
                localpart = datatypeStr;
            }
            String typeURI = resolvePrefixToURI(prefix);

            if ( typeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
                dv = getDatatypeValidator(SchemaSymbols.URI_SCHEMAFORSCHEMA, localpart);

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
                    if(dv == null && !referredTo) {
                        reportGenericSchemaError("attribute " + attNameStr + " has a type (" + datatypeStr + ") which is not recognized as one of the predefined schema datatypes");
                    }
                }
                attType = XMLAttributeDecl.TYPE_SIMPLE;

                dv = getDatatypeValidator(typeURI, localpart);
                if (dv == null && typeURI.equals(fTargetNSURIString) ) {
                    Element topleveltype = getTopLevelComponentByName(SchemaSymbols.ELT_SIMPLETYPE, localpart);
                    if (topleveltype != null) {
                        traverseSimpleTypeDecl( topleveltype );
                        dv = getDatatypeValidator(typeURI, localpart);
                    }else if (!referredTo) {
                        reportGenericSchemaError("simpleType not found : " + "("+typeURI+":"+ localpart+")"+ errorContext);
                    }
                } if(dv == null && !referredTo) {
                    reportGenericSchemaError("attribute " + attNameStr + " has an unrecognized type " + datatypeStr);
                }
            }
        } else {
            attType        = XMLAttributeDecl.TYPE_SIMPLE;
            localpart      = "string";
            dataTypeSymbol = fStringPool.addSymbol(localpart);
            dv = fDatatypeRegistry.getDatatypeValidator(localpart);

        if(defaultStr.length() > 0) {
            attValueAndUseType |= XMLAttributeDecl.VALUE_CONSTRAINT_DEFAULT;
            attValueConstraint = fStringPool.addString(defaultStr);
            }
        else if(fixedStr.length() > 0) {
            attValueAndUseType |= XMLAttributeDecl.VALUE_CONSTRAINT_FIXED;
            attValueConstraint = fStringPool.addString(fixedStr);
        }

        if (attType == XMLAttributeDecl.TYPE_SIMPLE && attValueConstraint != -1) {
            try {
                if (dv != null) {
                    if(defaultStr.length() > 0) {
                        dv.validate(defaultStr, null);
                    }
                    else {
                        dv.validate(fixedStr, null);
                    }
                }
                else if (!referredTo)
                    reportSchemaError(SchemaMessageProvider.NoValidatorFor,
                            new Object [] { datatypeStr });
            } catch (InvalidDatatypeValueException idve) {
                if (!referredTo)
                    reportSchemaError(SchemaMessageProvider.IncorrectDefaultType,
            }
        }

        dvIsDerivedFromID =
          ((dv != null) && dv instanceof IDDatatypeValidator);
        if (dvIsDerivedFromID && attValueConstraint != -1)
        {
            reportGenericSchemaError("a-props-correct.3: If type definition is or is derived from ID ," +
                "there must not be a value constraint" + errorContext);
        }

        if (attNameStr.equals("xmlns")) {
            reportGenericSchemaError("no-xmlns: The {name} of an attribute declaration must not match 'xmlns'" + errorContext);
        }

        if (isAttrTopLevel) {
            fTempAttributeDecl.datatypeValidator = dv;
            fTempAttributeDecl.name.setValues(attQName);
            fTempAttributeDecl.type = attType;
            fTempAttributeDecl.defaultType = attValueAndUseType;
            fTempAttributeDecl.list = attIsList;
            if (attValueConstraint != -1 ) {
                fTempAttributeDecl.defaultValue = fStringPool.toString(attValueConstraint);
            } else {
                fTempAttributeDecl.defaultValue = null;
            }
            fAttributeDeclRegistry.put(attNameStr, new XMLAttributeDecl(fTempAttributeDecl));
        }

        if (typeInfo != null) {

            int temp = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, attQName);
            if (temp > -1) {
              reportGenericSchemaError("ct-props-correct.4:  Duplicate attribute " +
              fStringPool.toString(attQName.rawname) + " in type definition");
            }

            if (dvIsDerivedFromID) {
               if (typeInfo.containsAttrTypeID())  {
                 reportGenericSchemaError("ct-props-correct.5: More than one attribute derived from type ID cannot appear in the same complex type definition.");
               }
               typeInfo.setContainsAttrTypeID();
            }
            fSchemaGrammar.addAttDef( typeInfo.templateElementIndex,
                                      attQName, attType,
                                      dataTypeSymbol, attValueAndUseType,
                                      fStringPool.toString( attValueConstraint), dv, attIsList);
        }

        return 0;

    private int addAttributeDeclFromAnotherSchema( String name, String uriStr, ComplexTypeInfo typeInfo) throws Exception {
        Grammar grammar = fGrammarResolver.getGrammar(uriStr);
        if (uriStr == null || ! (grammar instanceof SchemaGrammar) ) {
            reportGenericSchemaError( "no attribute named \"" + name
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }
        SchemaGrammar aGrammar = (SchemaGrammar)grammar;

        Hashtable attrRegistry = aGrammar.getAttributeDeclRegistry();
        if (attrRegistry == null) {
            reportGenericSchemaError( "no attribute named \"" + name
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }

        XMLAttributeDecl referredAttrDecl = (XMLAttributeDecl) attrRegistry.get(name);

        if (referredAttrDecl == null) {
            reportGenericSchemaError( "no attribute named \"" + name
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }


        if (typeInfo!= null) {

            int temp = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, referredAttrDecl.name);
            if (temp > -1) {
              reportGenericSchemaError("ct-props-correct.4:  Duplicate attribute " +
              fStringPool.toString(referredAttrDecl.name.rawname) + " in type definition");
            }

            if (referredAttrDecl.datatypeValidator != null &&
                referredAttrDecl.datatypeValidator instanceof IDDatatypeValidator) {
               if (typeInfo.containsAttrTypeID())  {
                 reportGenericSchemaError("ct-props-correct.5: More than one attribute derived from type ID cannot appear in the same complex type definition");
               }
               typeInfo.setContainsAttrTypeID();
            }

            fSchemaGrammar.addAttDef( typeInfo.templateElementIndex,
                                      referredAttrDecl.name, referredAttrDecl.type,
                                      -1, referredAttrDecl.defaultType,
                                      referredAttrDecl.defaultValue,
                                      referredAttrDecl.datatypeValidator,
                                      referredAttrDecl.list);
        }


        return 0;
    }

    private int addAttributeDeclFromAnotherSchema( String name, String uriStr, ComplexTypeInfo typeInfo, boolean hasDefault, boolean hasFixed, String fixedValue, int attValueAndUseType) throws Exception {
        Grammar grammar = fGrammarResolver.getGrammar(uriStr);
        if (uriStr == null || ! (grammar instanceof SchemaGrammar) ) {
            reportGenericSchemaError( "no attribute named \"" + name
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }
        SchemaGrammar aGrammar = (SchemaGrammar)grammar;

        Hashtable attrRegistry = aGrammar.getAttributeDeclRegistry();
        if (attrRegistry == null) {
            reportGenericSchemaError( "no attribute named \"" + name
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }

        XMLAttributeDecl referredAttrDecl = (XMLAttributeDecl) attrRegistry.get(name);

        if (referredAttrDecl == null) {
            reportGenericSchemaError( "no attribute named \"" + name
                                      + "\" was defined in schema : " + uriStr);
            return -1;
        }

        referredAttrDecl = new XMLAttributeDecl(referredAttrDecl);

        if (typeInfo!= null) {

            int temp = fSchemaGrammar.getAttributeDeclIndex(typeInfo.templateElementIndex, referredAttrDecl.name);
            if (temp > -1) {
              reportGenericSchemaError("ct-props-correct.4:  Duplicate attribute " +
              fStringPool.toString(referredAttrDecl.name.rawname) + " in type definition");
            }

            if (referredAttrDecl.datatypeValidator != null &&
                referredAttrDecl.datatypeValidator instanceof IDDatatypeValidator) {
               if (typeInfo.containsAttrTypeID())  {
                 reportGenericSchemaError("ct-props-correct.5: More than one attribute derived from type ID cannot appear in the same complex type definition");
               }
               typeInfo.setContainsAttrTypeID();
            }

            /**
             * Copied (and modified) the following code from traverseAttributeDecl
             * because we also need to override "use" and "fixed" of the attribute
             * from another namespace. - sandyg
             */
            boolean isReferFixed = (referredAttrDecl.defaultType & XMLAttributeDecl.DEFAULT_TYPE_FIXED) != 0;
            String referFixed = isReferFixed ? null : referredAttrDecl.defaultValue;
            if (referFixed != null && (hasDefault || hasFixed && !referFixed.equals(fixedValue))) {
                reportGenericSchemaError("au-props-correct.2: If the {attribute declaration} has a fixed {value constraint}, then if the attribute use itself has a {value constraint}, it must also be fixed and its value must match that of the {attribute declaration}'s {value constraint}");
            }

            if((attValueAndUseType & XMLAttributeDecl.USE_TYPE_OPTIONAL) == 0 ||
               fixedValue.length() > 0) {

                int useDigits =   XMLAttributeDecl.USE_TYPE_OPTIONAL |
                                  XMLAttributeDecl.USE_TYPE_PROHIBITED |
                                  XMLAttributeDecl.USE_TYPE_REQUIRED;

                int valueDigits = XMLAttributeDecl.VALUE_CONSTRAINT_DEFAULT |
                                  XMLAttributeDecl.VALUE_CONSTRAINT_FIXED;

                if((attValueAndUseType & XMLAttributeDecl.USE_TYPE_OPTIONAL) == 0 &&
                   (referredAttrDecl.defaultType & useDigits) !=
                   (attValueAndUseType & useDigits))
                {
                    if(referredAttrDecl.defaultType != XMLAttributeDecl.USE_TYPE_PROHIBITED) {
                            referredAttrDecl.defaultType |= useDigits;
                        referredAttrDecl.defaultType |= (attValueAndUseType & useDigits);
                    }
                }

                if(fixedValue.length() > 0) {
                    if((referredAttrDecl.defaultType & XMLAttributeDecl.VALUE_CONSTRAINT_FIXED) == 0) {
                        referredAttrDecl.defaultType |= valueDigits;
                        referredAttrDecl.defaultType |= XMLAttributeDecl.VALUE_CONSTRAINT_FIXED;
                        referredAttrDecl.defaultValue = fixedValue;
                    }
                }
            }

            fSchemaGrammar.addAttDef( typeInfo.templateElementIndex,
                                      referredAttrDecl.name, referredAttrDecl.type,
                                      -1, referredAttrDecl.defaultType,
                                      referredAttrDecl.defaultValue,
                                      referredAttrDecl.datatypeValidator,
                                      referredAttrDecl.list);
        }


        return 0;
    }

    /*
    *
    * <attributeGroup
    *   id = ID
    *   name = NCName
    *   ref = QName>
    *   Content: (annotation?, (attribute|attributeGroup)*, anyAttribute?)
    * </>
    *
    */
    private int traverseAttributeGroupDecl( Element attrGrpDecl, ComplexTypeInfo typeInfo, Vector anyAttDecls ) throws Exception {
        int scope = isTopLevel(attrGrpDecl)?
                    GeneralAttrCheck.ELE_CONTEXT_GLOBAL:
                    GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(attrGrpDecl, scope);

        String attGrpNameStr = attrGrpDecl.getAttribute(SchemaSymbols.ATT_NAME);
        int attGrpName = fStringPool.addSymbol(attGrpNameStr);

        String ref = attrGrpDecl.getAttribute(SchemaSymbols.ATT_REF);
        Element child = checkContent( attrGrpDecl, XUtil.getFirstChildElement(attrGrpDecl), true );

        if (ref.length() != 0) {
            if(isTopLevel(attrGrpDecl))
                reportGenericSchemaError ( "An attributeGroup with \"ref\" present must not have <schema> or <redefine> as its parent");
            if(attGrpNameStr.length() != 0)
                reportGenericSchemaError ( "attributeGroup " + attGrpNameStr + " cannot refer to another attributeGroup, but it refers to " + ref);
            if (child != null)
                reportGenericSchemaError ( "An attributeGroup with \"ref\" present must be empty");

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
            } else {
                Element parent = (Element)attrGrpDecl.getParentNode();
                if (fCurrentAttrGroupNameStack.search(localpart) > - 1) {
                    if (!((Element)parent.getParentNode()).getLocalName().equals(SchemaSymbols.ELT_REDEFINE)) {
                        reportGenericSchemaError("src-attribute_group.3: Circular attribute group reference is disallowed outside <redefine> -- "+ref);
                    }
                    return -1;
                }
            }

            if(typeInfo != null) {
                Element referredAttrGrp = getTopLevelComponentByName(SchemaSymbols.ELT_ATTRIBUTEGROUP,localpart);
                if (referredAttrGrp != null) {
                    traverseAttributeGroupDecl(referredAttrGrp, typeInfo, anyAttDecls);
                }
                else {
                    reportGenericSchemaError ( "Couldn't find top level attributeGroup " + ref);
                }
                return -1;
            }
        } else if (attGrpNameStr.length() == 0)
                reportGenericSchemaError ( "an attributeGroup must have a name or a ref attribute present");

        fCurrentAttrGroupNameStack.push(attGrpNameStr);

        for (;
             child != null ; child = XUtil.getNextSiblingElement(child)) {

            if ( child.getLocalName().equals(SchemaSymbols.ELT_ATTRIBUTE) ){
                traverseAttributeDecl(child, typeInfo, false);
            }
            else if ( child.getLocalName().equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ) {
                NamespacesScope currScope = (NamespacesScope)fNamespacesScope.clone();
                    traverseAttributeGroupDecl(child, typeInfo,anyAttDecls);
                fNamespacesScope = currScope;
            }
            else
                break;
        }

        fCurrentAttrGroupNameStack.pop();

        if (child != null) {
            if ( child.getLocalName().equals(SchemaSymbols.ELT_ANYATTRIBUTE) ) {
                if (anyAttDecls != null) {
                     anyAttDecls.addElement(traverseAnyAttribute(child));
                }
                if (XUtil.getNextSiblingElement(child) != null)
                    reportGenericSchemaError ( "src-attribute_group.0: The content of an attributeGroup declaration must match (annotation?, ((attribute | attributeGroup)*, anyAttribute?))");
                return -1;
            }
            else
                reportGenericSchemaError ( "src-attribute_group.0: The content of an attributeGroup declaration must match (annotation?, ((attribute | attributeGroup)*, anyAttribute?))");
        }
        return -1;

    private int traverseAttributeGroupDeclFromAnotherSchema( String attGrpName , String uriStr,
                                                             ComplexTypeInfo typeInfo,
                                                             Vector anyAttDecls ) throws Exception {

        Grammar grammar = fGrammarResolver.getGrammar(uriStr);
        if (uriStr == null || grammar == null || ! (grammar instanceof SchemaGrammar) ) {
            reportGenericSchemaError("!!Schema not found in #traverseAttributeGroupDeclFromAnotherSchema, schema uri : " + uriStr);
            return -1;
        }
        SchemaGrammar aGrammar = (SchemaGrammar)grammar;

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


        Element child = checkContent(attGrpDecl, XUtil.getFirstChildElement(attGrpDecl), true);
        for (;
             child != null ; child = XUtil.getNextSiblingElement(child)) {

            if ( child.getLocalName().equals(SchemaSymbols.ELT_ATTRIBUTE) ){
                String childAttName = child.getAttribute(SchemaSymbols.ATT_NAME);
                if ( childAttName.length() > 0 ) {
                    Hashtable attDeclRegistry = aGrammar.getAttributeDeclRegistry();
                    if ((attDeclRegistry != null) &&
                            (attDeclRegistry.get((Object)childAttName) != null) ){
                        addAttributeDeclFromAnotherSchema(childAttName, uriStr, typeInfo);
                        fNamespacesScope = saveNSMapping;
                        fTargetNSURI = saveTargetNSUri;
                        return -1;
                    } else {
                        traverseAttributeDecl(child, typeInfo, false);
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
            else {
                reportGenericSchemaError("Invalid content for attributeGroup");
            }
        }

        fNamespacesScope = saveNSMapping;
        fTargetNSURI = saveTargetNSUri;
        if(child != null) {
            reportGenericSchemaError("Invalid content for attributeGroup");
        }
        return -1;

    private Element findAttributeSimpleType(Element attrDecl) throws Exception {
        Element child = checkContent(attrDecl, XUtil.getFirstChildElement(attrDecl), true);
        if (child == null)
            return null;
        if (!child.getLocalName().equals(SchemaSymbols.ELT_SIMPLETYPE) ||
            XUtil.getNextSiblingElement(child) != null)
            reportGenericSchemaError("src-attribute.0: the content must match (annotation?, (simpleType?)) -- attribute declaration '"+
                                     attrDecl.getAttribute(SchemaSymbols.ATT_NAME)+"'");
        if (child.getLocalName().equals(SchemaSymbols.ELT_SIMPLETYPE))
            return child;
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
     *         nillable = boolean
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

        int scope = isTopLevel(elementDecl)?
                    GeneralAttrCheck.ELE_CONTEXT_GLOBAL:
                    GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(elementDecl, scope);

        int contentSpecType      = -1;
        int contentSpecNodeIndex = -1;
        int typeNameIndex = -1;
        DatatypeValidator dv = null;

        String abstractStr = elementDecl.getAttribute(SchemaSymbols.ATT_ABSTRACT);
        String blockStr = elementDecl.getAttribute(SchemaSymbols.ATT_BLOCK);
        String defaultStr = elementDecl.getAttribute(SchemaSymbols.ATT_DEFAULT);
        String finalStr = elementDecl.getAttribute(SchemaSymbols.ATT_FINAL);
        String fixedStr = elementDecl.getAttribute(SchemaSymbols.ATT_FIXED);
        String formStr = elementDecl.getAttribute(SchemaSymbols.ATT_FORM);
        String maxOccursStr = elementDecl.getAttribute(SchemaSymbols.ATT_MAXOCCURS);
        String minOccursStr = elementDecl.getAttribute(SchemaSymbols.ATT_MINOCCURS);
        String nameStr = elementDecl.getAttribute(SchemaSymbols.ATT_NAME);
        String nillableStr = elementDecl.getAttribute(SchemaSymbols.ATT_NILLABLE);
        String refStr = elementDecl.getAttribute(SchemaSymbols.ATT_REF);
        String substitutionGroupStr = elementDecl.getAttribute(SchemaSymbols.ATT_SUBSTITUTIONGROUP);
        String typeStr = elementDecl.getAttribute(SchemaSymbols.ATT_TYPE);

        checkEnumerationRequiredNotation(nameStr, typeStr);

        if ( DEBUGGING )
            System.out.println("traversing element decl : " + nameStr );

        Attr abstractAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_ABSTRACT);
        Attr blockAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_BLOCK);
        Attr defaultAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_DEFAULT);
        Attr finalAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_FINAL);
        Attr fixedAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_FIXED);
        Attr formAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_FORM);
        Attr maxOccursAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_MAXOCCURS);
        Attr minOccursAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_MINOCCURS);
        Attr nameAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_NAME);
        Attr nillableAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_NILLABLE);
        Attr refAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_REF);
        Attr substitutionGroupAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_SUBSTITUTIONGROUP);
        Attr typeAtt = elementDecl.getAttributeNode(SchemaSymbols.ATT_TYPE);

        if(defaultAtt != null && fixedAtt != null)
            reportGenericSchemaError("src-element.1: an element cannot have both \"fixed\" and \"default\" present at the same time");

        String fromAnotherSchema = null;

        if (isTopLevel(elementDecl)) {
            if(nameAtt == null)
                reportGenericSchemaError("globally-declared element must have a name");
            else if (refAtt != null)
                reportGenericSchemaError("globally-declared element " + nameStr + " cannot have a ref attribute");

            int nameIndex = fStringPool.addSymbol(nameStr);
            int eltKey = fSchemaGrammar.getElementDeclIndex(fTargetNSURI, nameIndex,TOP_LEVEL_SCOPE);
            if (eltKey > -1 ) {
                return new QName(-1,nameIndex,nameIndex,fTargetNSURI);
            }
        }

        if (blockAtt == null)
            blockStr = null;
        int blockSet = parseBlockSet(blockStr);
        if( (blockStr != null) && blockStr.length() != 0 &&
                (!blockStr.equals(SchemaSymbols.ATTVAL_POUNDALL) &&
                (((blockSet & SchemaSymbols.RESTRICTION) == 0) &&
                (((blockSet & SchemaSymbols.EXTENSION) == 0) &&
                ((blockSet & SchemaSymbols.SUBSTITUTION) == 0)))))
            reportGenericSchemaError("The values of the 'block' attribute of an element must be either #all or a list of 'substitution', 'restriction' and 'extension'; " + blockStr + " was found");
        if (finalAtt == null)
            finalStr = null;
        int finalSet = parseFinalSet(finalStr);
        if( (finalStr != null) && finalStr.length() != 0 &&
                (!finalStr.equals(SchemaSymbols.ATTVAL_POUNDALL) &&
                (((finalSet & SchemaSymbols.RESTRICTION) == 0) &&
                ((finalSet & SchemaSymbols.EXTENSION) == 0))))
            reportGenericSchemaError("The values of the 'final' attribute of an element must be either #all or a list of 'restriction' and 'extension'; " + finalStr + " was found");
        boolean isNillable = nillableStr.equals(SchemaSymbols.ATTVAL_TRUE) || nillableStr.equals(SchemaSymbols.ATTVAL_TRUE_1);
        boolean isAbstract = abstractStr.equals(SchemaSymbols.ATTVAL_TRUE) || abstractStr.equals(SchemaSymbols.ATTVAL_TRUE_1);
        int elementMiscFlags = 0;
        if (isNillable) {
            elementMiscFlags += SchemaSymbols.NILLABLE;
        }
        if (isAbstract) {
            elementMiscFlags += SchemaSymbols.ABSTRACT;
        }
        if(fixedAtt != null)
            elementMiscFlags += SchemaSymbols.FIXED;

        if (refAtt != null) {
            if (abstractAtt != null || blockAtt != null || defaultAtt != null ||
                finalAtt != null || fixedAtt != null || formAtt != null ||
                nillableAtt != null || substitutionGroupAtt != null || typeAtt != null)
            if (nameAtt != null)
                reportGenericSchemaError("src-element.2.1: element " + nameStr + " cannot also have a ref attribute");

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
            String localpart = refStr;
            int colonptr = refStr.indexOf(":");
            if ( colonptr > 0) {
                prefix = refStr.substring(0,colonptr);
                localpart = refStr.substring(colonptr+1);
            }
            int localpartIndex = fStringPool.addSymbol(localpart);
            String uriString = resolvePrefixToURI(prefix);
            QName eltName = new QName(prefix != null ? fStringPool.addSymbol(prefix) : -1,
                                      localpartIndex,
                                      fStringPool.addSymbol(refStr),
                                      uriString != null ? fStringPool.addSymbol(uriString) : StringPool.EMPTY_STRING);

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


            if (fCurrentScope != TOP_LEVEL_SCOPE) {
               if (fFullConstraintChecking) {
                 fTopLevelElementsRefdFromGroup.addElement(eltName);
                 fTopLevelElementsRefdFromGroup.addElement(new Integer(fCurrentScope));
               }

            }

            return eltName;
        } else if (nameAtt == null)
            reportGenericSchemaError("src-element.2.1: a local element must have a name or a ref attribute present");


        Element substitutionGroupElementDecl = null;
        int substitutionGroupElementDeclIndex = -1;
        boolean noErrorSoFar = true;


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
                    reportGenericSchemaError("anonymous complexType in element '" + nameStr +"' has a name attribute");
                }

                else {
                    String anonTypeName = genAnonTypeName(child);
                    if (fCurrentTypeNameStack.search((Object)anonTypeName) > - 1) {

                        int uriInd = StringPool.EMPTY_STRING;
                        if ( formStr.equals(SchemaSymbols.ATTVAL_QUALIFIED)||
                             fElementDefaultQualified) {
                             uriInd = fTargetNSURI;
                        }
                        int nameIndex = fStringPool.addSymbol(nameStr);
                        QName tempQName = new QName(-1, nameIndex, nameIndex, uriInd);
                        int eltIndex = fSchemaGrammar.addElementDecl(tempQName,
                              fCurrentScope, fCurrentScope, -1, -1, -1, null);
                        fElementRecurseComplex.addElement(new ElementInfo(eltIndex,anonTypeName));
                        return tempQName;

                    }
                    else {
                        typeNameIndex = traverseComplexTypeDecl(child);
                        if (typeNameIndex != -1 ) {
                            typeInfo = (ComplexTypeInfo)
                                fComplexTypeRegistry.get(fStringPool.toString(typeNameIndex));
                        }
                        else {
                            noErrorSoFar = false;
                            reportGenericSchemaError("traverse complexType error in element '" + nameStr +"'");
                        }
                    }
                }

                haveAnonType = true;
                child = XUtil.getNextSiblingElement(child);
            }
            else if (childName.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                if (child.getAttribute(SchemaSymbols.ATT_NAME).length() > 0) {
                    noErrorSoFar = false;
                    reportGenericSchemaError("anonymous simpleType in element '" + nameStr +"' has a name attribute");
                }
                else
                    typeNameIndex = traverseSimpleTypeDecl(child);
                if (typeNameIndex != -1) {
                    dv = fDatatypeRegistry.getDatatypeValidator(fStringPool.toString(typeNameIndex));
                }
                else {
                    noErrorSoFar = false;
                    reportGenericSchemaError("traverse simpleType error in element '" + nameStr +"'");
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
                reportGenericSchemaError("src-element.0: the content of an element information item must match (annotation?, (simpleType | complexType)?, (unique | key | keyref)*)");
            }
        }

        if (haveAnonType && (typeAtt != null)) {
            noErrorSoFar = false;
            reportGenericSchemaError( "src-element.3: Element '"+ nameStr +
                                      "' have both a type attribute and a annoymous type child" );
        }
        else if (typeAtt != null) {
            String prefix = "";
            String localpart = typeStr;
            int colonptr = typeStr.indexOf(":");
            if ( colonptr > 0) {
                prefix = typeStr.substring(0,colonptr);
                localpart = typeStr.substring(colonptr+1);
            }
            String typeURI = resolvePrefixToURI(prefix);

            if (!(typeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) &&
                localpart.equals("anyType"))) {
                if ( !typeURI.equals(fTargetNSURIString)
                     && !typeURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
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
                                    int uriInd = StringPool.EMPTY_STRING;
                                    if ( formStr.equals(SchemaSymbols.ATTVAL_QUALIFIED)||
                                         fElementDefaultQualified) {
                                        uriInd = fTargetNSURI;
                                    }
                                    int nameIndex = fStringPool.addSymbol(nameStr);
                                    QName tempQName = new QName(-1, nameIndex, nameIndex, uriInd);
                                    int eltIndex = fSchemaGrammar.addElementDecl(tempQName,
                                            fCurrentScope, fCurrentScope, -1, -1, -1, null);
                                    fElementRecurseComplex.addElement(new ElementInfo(eltIndex,localpart));
                                    return tempQName;
                                }
                                else {
                                    Stack savedbaseNameStack = null;
                                    if (!fBaseTypeNameStack.isEmpty()) {
                                      savedbaseNameStack = fBaseTypeNameStack;
                                      fBaseTypeNameStack = new Stack();
                                    }
                                    typeNameIndex = traverseComplexTypeDecl( topleveltype, true );
                                    if (savedbaseNameStack != null)
                                        fBaseTypeNameStack  = savedbaseNameStack;
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
        }
        String substitutionGroupUri = null;
        String substitutionGroupLocalpart = null;
        String substitutionGroupFullName = null;
        ComplexTypeInfo substitutionGroupEltTypeInfo = null;
        DatatypeValidator substitutionGroupEltDV = null;
        SchemaGrammar subGrammar = fSchemaGrammar;
        boolean ignoreSub = false;

        if ( substitutionGroupStr.length() > 0 ) {
            if(refAtt != null)
                reportGenericSchemaError("a local element cannot have a substitutionGroup");
            substitutionGroupUri =  resolvePrefixToURI(getPrefix(substitutionGroupStr));
            substitutionGroupLocalpart = getLocalPart(substitutionGroupStr);
            substitutionGroupFullName = substitutionGroupUri+","+substitutionGroupLocalpart;

            if ( !substitutionGroupUri.equals(fTargetNSURIString) ) {
                Grammar grammar = fGrammarResolver.getGrammar(substitutionGroupUri);
                if (grammar != null && grammar instanceof SchemaGrammar) {
                    subGrammar = (SchemaGrammar) grammar;
                    substitutionGroupElementDeclIndex = subGrammar.getElementDeclIndex(fStringPool.addSymbol(substitutionGroupUri),
                                                              fStringPool.addSymbol(substitutionGroupLocalpart),
                                                              TOP_LEVEL_SCOPE);
                    if (substitutionGroupElementDeclIndex<=-1) {
                        noErrorSoFar = false;
                        reportGenericSchemaError("couldn't find substitutionGroup " + substitutionGroupLocalpart + " referenced by element " + nameStr
                                         + " in the SchemaGrammar "+substitutionGroupUri);

                    } else {
                        substitutionGroupEltTypeInfo = getElementDeclTypeInfoFromNS(substitutionGroupUri, substitutionGroupLocalpart);
                        if (substitutionGroupEltTypeInfo == null) {
                            substitutionGroupEltDV = getElementDeclTypeValidatorFromNS(substitutionGroupUri, substitutionGroupLocalpart);
                            /*if (substitutionGroupEltDV == null) {
                                noErrorSoFar = false;
                                reportGenericSchemaError("Could not find type for element '" +substitutionGroupLocalpart
                                                 + "' in schema '" + substitutionGroupUri+"'");
                            }*/
                        }
                    }
                } else {
                    noErrorSoFar = false;
                    reportGenericSchemaError("couldn't find a schema grammar with target namespace '" + substitutionGroupUri + "' for element '" + substitutionGroupStr + "'");
                }
            }
            else {
                substitutionGroupElementDecl = getTopLevelComponentByName(SchemaSymbols.ELT_ELEMENT, substitutionGroupLocalpart);
                if (substitutionGroupElementDecl == null) {
                    substitutionGroupElementDeclIndex =
                        fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(substitutionGroupStr),TOP_LEVEL_SCOPE);
                    if ( substitutionGroupElementDeclIndex == -1) {
                        noErrorSoFar = false;
                        reportGenericSchemaError("unable to locate substitutionGroup affiliation element "
                                                  +substitutionGroupStr
                                                  +" in element declaration "
                                                  +nameStr);
                    }
                }
                else {
                    substitutionGroupElementDeclIndex =
                        fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(substitutionGroupStr),TOP_LEVEL_SCOPE);

                    if ( substitutionGroupElementDeclIndex == -1) {
                        if(fSubstitutionGroupRecursionRegistry.contains(fTargetNSURIString+","+substitutionGroupElementDecl.getAttribute(SchemaSymbols.ATT_NAME))) {
                            ignoreSub = true;
                        } else {
                            fSubstitutionGroupRecursionRegistry.addElement(fTargetNSURIString+","+substitutionGroupElementDecl.getAttribute(SchemaSymbols.ATT_NAME));
                            traverseElementDecl(substitutionGroupElementDecl);
                            substitutionGroupElementDeclIndex =
                                fSchemaGrammar.getElementDeclIndex(fTargetNSURI, getLocalPartIndex(substitutionGroupStr),TOP_LEVEL_SCOPE);
                            fSubstitutionGroupRecursionRegistry.removeElement((Object)fTargetNSURIString+","+substitutionGroupElementDecl.getAttribute(SchemaSymbols.ATT_NAME));
                        }
                    }
                }

                if (!ignoreSub && substitutionGroupElementDeclIndex != -1) {
                    substitutionGroupEltTypeInfo = fSchemaGrammar.getElementComplexTypeInfo( substitutionGroupElementDeclIndex );
                    if (substitutionGroupEltTypeInfo == null) {
                        fSchemaGrammar.getElementDecl(substitutionGroupElementDeclIndex, fTempElementDecl);
                        substitutionGroupEltDV = fTempElementDecl.datatypeValidator;
                        /*if (substitutionGroupEltDV == null) {
                            noErrorSoFar = false;
                            reportGenericSchemaError("Could not find type for element '" +substitutionGroupLocalpart
                                                     + "' in schema '" + substitutionGroupUri+"'");
                        }*/
                    }
                }
            }
            if (substitutionGroupElementDeclIndex <= -1)
                ignoreSub = true;
            if(!ignoreSub)
                checkSubstitutionGroupOK(elementDecl, substitutionGroupElementDecl, noErrorSoFar, substitutionGroupElementDeclIndex, subGrammar, typeInfo, substitutionGroupEltTypeInfo, dv, substitutionGroupEltDV);
        }

        if ( noErrorSoFar && typeInfo == null && dv == null ) {
            typeInfo = substitutionGroupEltTypeInfo;
            dv = substitutionGroupEltDV;
        }

        boolean isAnyType = false;
        if (typeInfo == null && dv==null) {
            if (noErrorSoFar) {
                isAnyType = true;
                contentSpecType = XMLElementDecl.TYPE_ANY;
            }
            else {
                noErrorSoFar = false;
                reportGenericSchemaError ("untyped element : " + nameStr );
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

        if(fixedAtt != null) defaultStr = fixedStr;
        if(defaultStr.length() != 0) {
            if(typeInfo != null &&
                    (typeInfo.contentType != XMLElementDecl.TYPE_MIXED_SIMPLE &&
                     typeInfo.contentType != XMLElementDecl.TYPE_MIXED_COMPLEX &&
                    typeInfo.contentType != XMLElementDecl.TYPE_SIMPLE)) {
                reportGenericSchemaError ("e-props-correct.2.1: element " + nameStr + " has a fixed or default value and must have a mixed or simple content model");
            }
            if(typeInfo != null &&
               (typeInfo.contentType == XMLElementDecl.TYPE_MIXED_SIMPLE ||
                typeInfo.contentType == XMLElementDecl.TYPE_MIXED_COMPLEX)) {
                if (!particleEmptiable(typeInfo.contentSpecHandle))
                    reportGenericSchemaError ("e-props-correct.2.2.2: for element " + nameStr + ", the {content type} is mixed, then the {content type}'s particle must be emptiable");
            }

            try {
                if(dv != null) {
                    dv.validate(defaultStr, null);
                }
            } catch (InvalidDatatypeValueException ide) {
                reportGenericSchemaError ("e-props-correct.2: invalid fixed or default value '" + defaultStr + "' in element " + nameStr);
            }
        }

        if (defaultStr.length() != 0 &&
            dv != null && dv instanceof IDDatatypeValidator) {
            reportGenericSchemaError ("e-props-correct.4: If the {type definition} or {type definition}'s {content type} is or is derived from ID then there must not be a {value constraint} -- element " + nameStr);
        }


        int elementNameIndex     = fStringPool.addSymbol(nameStr);
        int localpartIndex = elementNameIndex;
        int uriIndex = StringPool.EMPTY_STRING;
        int enclosingScope = fCurrentScope;

        if ( isTopLevel(elementDecl)) {
            uriIndex = fTargetNSURI;
            enclosingScope = TOP_LEVEL_SCOPE;
        }
        else if ( !formStr.equals(SchemaSymbols.ATTVAL_UNQUALIFIED) &&
                    (( formStr.equals(SchemaSymbols.ATTVAL_QUALIFIED)||
                        fElementDefaultQualified ))) {

            uriIndex = fTargetNSURI;
        }

        QName eltQName = new QName(-1,localpartIndex,elementNameIndex,uriIndex);

        int existingEltNdx = fSchemaGrammar.getElementDeclIndex(eltQName.uri,
                                                 eltQName.localpart,enclosingScope);

        if (existingEltNdx > -1) {
          if (!checkDuplicateElementTypes(existingEltNdx,typeInfo,dv)) {
             noErrorSoFar = false;

             reportGenericSchemaError("duplicate element decl in the same scope with different types : " +
                                      fStringPool.toString(localpartIndex));

          }
        }


        int attrListHead = -1 ;

        if (typeInfo != null) {
            attrListHead = typeInfo.attlistHead;
        }
        int elementIndex = fSchemaGrammar.addElementDecl(eltQName, enclosingScope, scopeDefined,
                                                         contentSpecType, contentSpecNodeIndex,
                                                         attrListHead, dv);
        if (isAnyType) {
            fSchemaGrammar.addAttDef( elementIndex,
                                      new QName(),
                                      XMLAttributeDecl.TYPE_ANY_ANY,
                                      -1,
                                      XMLAttributeDecl.PROCESSCONTENTS_STRICT,
                                      null,
                                      null,
                                      false);
        }

        if ( DEBUGGING ) {
            /***/
            System.out.println("########elementIndex:"+elementIndex+" ("+fStringPool.toString(eltQName.uri)+","
                               + fStringPool.toString(eltQName.localpart) + ")"+
                               " eltType:"+typeStr+" contentSpecType:"+contentSpecType+
                               " SpecNodeIndex:"+ contentSpecNodeIndex +" enclosingScope: " +enclosingScope +
                               " scopeDefined: " +scopeDefined+"\n");
             /***/
        }

        fSchemaGrammar.setElementComplexTypeInfo(elementIndex, typeInfo);

        fSchemaGrammar.setElementFromAnotherSchemaURI(elementIndex, fromAnotherSchema);

        fSchemaGrammar.setElementDeclBlockSet(elementIndex, blockSet);
        fSchemaGrammar.setElementDeclFinalSet(elementIndex, finalSet);
        fSchemaGrammar.setElementDeclMiscFlags(elementIndex, elementMiscFlags);
        fSchemaGrammar.setElementDefault(elementIndex, defaultStr);

        fSchemaGrammar.setElementDeclSubstitutionGroupAffFullName(elementIndex, substitutionGroupFullName);

        if ( substitutionGroupStr.length() > 0 && !ignoreSub) {
            subGrammar.addElementDeclOneSubstitutionGroupQName(substitutionGroupElementDeclIndex, eltQName, fSchemaGrammar, elementIndex);
        }


        Element ic = XUtil.getFirstChildElementNS(elementDecl, IDENTITY_CONSTRAINTS);
        if (ic != null) {
            Integer elementIndexObj = new Integer(elementIndex);
            Vector identityConstraints = (Vector)fIdentityConstraints.get(elementIndexObj);
            if (identityConstraints == null) {
                identityConstraints = new Vector();
                fIdentityConstraints.put(elementIndexObj, identityConstraints);
            }
            while (ic != null) {
                if (DEBUG_IC_DATATYPES) {
                    System.out.println("<ICD>: adding ic for later traversal: "+ic);
                }
                identityConstraints.addElement(ic);
                ic = XUtil.getNextSiblingElementNS(ic, IDENTITY_CONSTRAINTS);
            }
        }

        return eltQName;


    private boolean checkDuplicateElementTypes(int eltNdx, ComplexTypeInfo typeInfo,
                                       DatatypeValidator dv) {


        fSchemaGrammar.getElementDecl(eltNdx, fTempElementDecl);
        DatatypeValidator edv = fTempElementDecl.datatypeValidator;
        ComplexTypeInfo eTypeInfo = fSchemaGrammar.getElementComplexTypeInfo(eltNdx);
        if ( ((eTypeInfo != null)&&(eTypeInfo!=typeInfo))
             || ((edv != null)&&(edv != dv)) )
            return false;
        else
            return true;

    }

    private void traverseIdentityNameConstraintsFor(int elementIndex,
                                                Vector identityConstraints)
        throws Exception {

        int size = identityConstraints != null ? identityConstraints.size() : 0;
        if (size > 0) {
            XMLElementDecl edecl = new XMLElementDecl();
            fSchemaGrammar.getElementDecl(elementIndex, edecl);
            for (int i = 0; i < size; i++) {
                Element ic = (Element)identityConstraints.elementAt(i);
                String icName = ic.getLocalName();
                if ( icName.equals(SchemaSymbols.ELT_KEY) ) {
                    traverseKey(ic, edecl);
                }
                else if ( icName.equals(SchemaSymbols.ELT_UNIQUE) ) {
                    traverseUnique(ic, edecl);
                }
                fSchemaGrammar.setElementDecl(elementIndex, edecl);




    private void traverseIdentityRefConstraintsFor(int elementIndex,
                                                Vector identityConstraints)
        throws Exception {

        int size = identityConstraints != null ? identityConstraints.size() : 0;
        if (size > 0) {
            XMLElementDecl edecl = new XMLElementDecl();
            fSchemaGrammar.getElementDecl(elementIndex, edecl);
            for (int i = 0; i < size; i++) {
                Element ic = (Element)identityConstraints.elementAt(i);
                String icName = ic.getLocalName();
                if ( icName.equals(SchemaSymbols.ELT_KEYREF) ) {
                    traverseKeyRef(ic, edecl);
                }
                fSchemaGrammar.setElementDecl(elementIndex, edecl);




    private void traverseUnique(Element uElem, XMLElementDecl eDecl)
        throws Exception {

        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(uElem, scope);

        String uName = uElem.getAttribute(SchemaSymbols.ATT_NAME);
        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: traverseUnique(\""+uElem.getNodeName()+"\") ["+uName+']');
        }
        String eName = getElementNameFor(uElem);
        Unique unique = new Unique(uName, eName);
        if(fIdentityConstraintNames.get(fTargetNSURIString+","+uName) != null) {
            reportGenericSchemaError("More than one identity constraint named " + uName);
        }
        fIdentityConstraintNames.put(fTargetNSURIString+","+uName, unique);

        traverseIdentityConstraint(unique, uElem);

        eDecl.unique.addElement(unique);


    private void traverseKey(Element kElem, XMLElementDecl eDecl)
        throws Exception {

        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(kElem, scope);

        String kName = kElem.getAttribute(SchemaSymbols.ATT_NAME);
        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: traverseKey(\""+kElem.getNodeName()+"\") ["+kName+']');
        }
        String eName = getElementNameFor(kElem);
        Key key = new Key(kName, eName);
        if(fIdentityConstraintNames.get(fTargetNSURIString+","+kName) != null) {
            reportGenericSchemaError("More than one identity constraint named " + kName);
        }
        fIdentityConstraintNames.put(fTargetNSURIString+","+kName, key);

        traverseIdentityConstraint(key, kElem);

        eDecl.key.addElement(key);


    private void traverseKeyRef(Element krElem, XMLElementDecl eDecl)
        throws Exception {

        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(krElem, scope);

        String krName = krElem.getAttribute(SchemaSymbols.ATT_NAME);
        String kName = krElem.getAttribute(SchemaSymbols.ATT_REFER);
        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: traverseKeyRef(\""+krElem.getNodeName()+"\") ["+krName+','+kName+']');
        }

        if(fIdentityConstraintNames.get(fTargetNSURIString+","+krName) != null) {
            reportGenericSchemaError("More than one identity constraint named " + krName);
        }

        String prefix = "";
        String localpart = kName;
        int colonptr = kName.indexOf(":");
        if ( colonptr > 0) {
            prefix = kName.substring(0,colonptr);
            localpart = kName.substring(colonptr+1);
        }
        String uriStr = resolvePrefixToURI(prefix);
        IdentityConstraint kId = (IdentityConstraint)fIdentityConstraintNames.get(uriStr+","+localpart);
        if (kId== null) {
            reportSchemaError(SchemaMessageProvider.KeyRefReferNotFound,
                              new Object[]{krName,kName});
            return;
        }

        String eName = getElementNameFor(krElem);
        KeyRef keyRef = new KeyRef(krName, kId, eName);

        traverseIdentityConstraint(keyRef, krElem);
        if(keyRef.getFieldCount() != kId.getFieldCount()){
                 reportSchemaError(SchemaMessageProvider.CardinalityNotEqual, new Object[]{krName,kName});
                 return ;
        }

        eDecl.keyRef.addElement(keyRef);
        fIdentityConstraintNames.put(fTargetNSURIString+","+krName, keyRef);


    private void traverseIdentityConstraint(IdentityConstraint ic,
                                            Element icElem) throws Exception {

        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(icElem, scope);

        Element sElem = XUtil.getFirstChildElement(icElem);
        if(sElem == null) {
            reportGenericSchemaError("The content of an identity constraint must match (annotation?, selector, field+)");
            return;
        }
        sElem = checkContent( icElem, sElem, false);
        attrValues = generalCheck(sElem, scope);

        if(!sElem.getLocalName().equals(SchemaSymbols.ELT_SELECTOR)) {
            reportGenericSchemaError("The content of an identity constraint must match (annotation?, selector, field+)");
        }
        checkContent(icElem, XUtil.getFirstChildElement(sElem), true);

        String sText = sElem.getAttribute(SchemaSymbols.ATT_XPATH);
        sText = sText.trim();
        Selector.XPath sXpath = null;
        try {
            sXpath = new Selector.XPath(sText, fStringPool,
                                        fNamespacesScope);
            Selector selector = new Selector(sXpath, ic);
            if (DEBUG_IDENTITY_CONSTRAINTS) {
                System.out.println("<IC>:   selector: "+selector);
            }
            ic.setSelector(selector);
        }
        catch (XPathException e) {
            reportGenericSchemaError(e.getMessage());
            return;
        }

        Element fElem = XUtil.getNextSiblingElement(sElem);
        if(fElem == null) {
            reportGenericSchemaError("The content of an identity constraint must match (annotation?, selector, field+)");
        }
        while (fElem != null) {
            attrValues = generalCheck(fElem, scope);

            if(!fElem.getLocalName().equals(SchemaSymbols.ELT_FIELD))
                reportGenericSchemaError("The content of an identity constraint must match (annotation?, selector, field+)");
            checkContent(icElem, XUtil.getFirstChildElement(fElem), true);
            String fText = fElem.getAttribute(SchemaSymbols.ATT_XPATH);
            fText = fText.trim();
            try {
                Field.XPath fXpath = new Field.XPath(fText, fStringPool,
                                                     fNamespacesScope);
                Field field = new Field(fXpath, ic);
                if (DEBUG_IDENTITY_CONSTRAINTS) {
                    System.out.println("<IC>:   field:    "+field);
                }
                ic.addField(field);
            }
            catch (XPathException e) {
                reportGenericSchemaError(e.getMessage());
                return;
            }
            fElem = XUtil.getNextSiblingElement(fElem);
        }


    /* This code is no longer used because datatypes can't be found statically for ID constraints.
    private DatatypeValidator getDatatypeValidatorFor(Element element,
                                                      Selector.XPath sxpath,
                                                      Field.XPath fxpath)
        throws Exception {

        String ename = element.getAttribute("name");
        if (DEBUG_IC_DATATYPES) {
            System.out.println("<ICD>: XMLValidator#getDatatypeValidatorFor("+
                               ename+','+sxpath+','+fxpath+')');
        }
        int localpart = fStringPool.addSymbol(ename);
        String targetNamespace = fSchemaRootElement.getAttribute("targetNamespace");
        int uri = fStringPool.addSymbol(targetNamespace);
        int edeclIndex = fSchemaGrammar.getElementDeclIndex(uri, localpart,
                                                            Grammar.TOP_LEVEL_SCOPE);

        XPath.LocationPath spath = sxpath.getLocationPath();
        XPath.Step[] ssteps = spath.steps;
        for (int i = 0; i < ssteps.length; i++) {
            XPath.Step step = ssteps[i];
            XPath.Axis axis = step.axis;
            XPath.NodeTest nodeTest = step.nodeTest;
            switch (axis.type) {
                case XPath.Axis.ATTRIBUTE: {
                    reportGenericSchemaError("not allowed to select attribute");
                    return null;
                }
                case XPath.Axis.CHILD: {
                    int index = fSchemaGrammar.getElementDeclIndex(nodeTest.name, edeclIndex);
                    if (index == -1) {
                        index = fSchemaGrammar.getElementDeclIndex(nodeTest.name, Grammar.TOP_LEVEL_SCOPE);
                    }
                    if (index == -1) {
                        reportGenericSchemaError("no such element \""+fStringPool.toString(nodeTest.name.rawname)+'"');
                        return null;
                    }
                    edeclIndex = index;
                    break;
                }
                case XPath.Axis.SELF: {
                    break;
                }
                default: {
                    reportGenericSchemaError("invalid selector axis");
                    return null;
                }
            }
        }

        XPath.LocationPath fpath = fxpath.getLocationPath();
        XPath.Step[] fsteps = fpath.steps;
        for (int i = 0; i < fsteps.length; i++) {
            XPath.Step step = fsteps[i];
            XPath.Axis axis = step.axis;
            XPath.NodeTest nodeTest = step.nodeTest;
            switch (axis.type) {
                case XPath.Axis.ATTRIBUTE: {
                    if (i != fsteps.length - 1) {
                        reportGenericSchemaError("attribute must be last step");
                        return null;
                    }
                    int adeclIndex = fSchemaGrammar.getAttributeDeclIndex(edeclIndex, nodeTest.name);
                    if (adeclIndex == -1) {
                        reportGenericSchemaError("no such attribute \""+fStringPool.toString(nodeTest.name.rawname)+'"');
                    }
                    XMLAttributeDecl adecl = new XMLAttributeDecl();
                    fSchemaGrammar.getAttributeDecl(adeclIndex, adecl);
                    DatatypeValidator validator = adecl.datatypeValidator;
                    return validator;
                }
                case XPath.Axis.CHILD: {
                    int index = fSchemaGrammar.getElementDeclIndex(nodeTest.name, edeclIndex);
                    if (index == -1) {
                        index = fSchemaGrammar.getElementDeclIndex(nodeTest.name, Grammar.TOP_LEVEL_SCOPE);
                    }
                    if (index == -1) {
                        reportGenericSchemaError("no such element \""+fStringPool.toString(nodeTest.name.rawname)+'"');
                        return null;
                    }
                    edeclIndex = index;
                    if (i < fsteps.length - 1) {
                        break;
                    }
                }
                case XPath.Axis.SELF: {
                    if (i == fsteps.length - 1) {
                        XMLElementDecl edecl = new XMLElementDecl();
                        fSchemaGrammar.getElementDecl(edeclIndex, edecl);
                        if (edecl.type != XMLElementDecl.TYPE_SIMPLE) {
                            reportGenericSchemaError("selected element is not of simple type");
                            return null;
                        }
                        DatatypeValidator validator = edecl.datatypeValidator;
                        if (validator == null) validator = new StringDatatypeValidator();
                        return validator;
                    }
                    break;
                }
                default: {
                    reportGenericSchemaError("invalid selector axis");
                    return null;
                }
            }
        }

        reportGenericSchemaError("No datatype validator for field "+fxpath+
                                 " of element "+ename);
        return null;


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

    private void checkSubstitutionGroupOK(Element elementDecl, Element substitutionGroupElementDecl,
            boolean noErrorSoFar, int substitutionGroupElementDeclIndex, SchemaGrammar substitutionGroupGrammar, ComplexTypeInfo typeInfo,
            ComplexTypeInfo substitutionGroupEltTypeInfo, DatatypeValidator dv,
            DatatypeValidator substitutionGroupEltDV)  throws Exception {

        int finalSet = substitutionGroupGrammar.getElementDeclFinalSet(substitutionGroupElementDeclIndex);
        if ((finalSet&SchemaSymbols.RESTRICTION) != 0 &&
            (finalSet&SchemaSymbols.EXTENSION) != 0) {
            reportGenericSchemaError("element " + elementDecl.getAttribute(SchemaSymbols.ATT_NAME)
                + " cannot be part of the substitution group headed by "
                + substitutionGroupElementDecl.getAttribute(SchemaSymbols.ATT_NAME));
        }


        if (typeInfo != null) {
            if (substitutionGroupEltTypeInfo == typeInfo)
                return;
            int derivationMethod = typeInfo.derivedBy;
            if(typeInfo.baseComplexTypeInfo == null) {
                    if (!checkSimpleTypeDerivationOK(typeInfo.baseDataTypeValidator,
                                                     substitutionGroupEltDV) &&
                        !(substitutionGroupEltTypeInfo == null &&
                          substitutionGroupEltDV == null)) {
                        reportGenericSchemaError("Element " + elementDecl.getAttribute(SchemaSymbols.ATT_NAME) + " has a type which does not derive from the type of the element at the head of the substitution group");
                        noErrorSoFar = false;
                        if((derivationMethod & finalSet) != 0) {
                            noErrorSoFar = false;
                            reportGenericSchemaError("element " + elementDecl.getAttribute(SchemaSymbols.ATT_NAME)
                                + " cannot be part of the substitution group headed by "
                                + substitutionGroupElementDecl.getAttribute(SchemaSymbols.ATT_NAME));
                        }
                    }
                } else if (!(substitutionGroupEltTypeInfo == null &&
                             substitutionGroupEltDV == null)) {
                    reportGenericSchemaError("Element " + elementDecl.getAttribute(SchemaSymbols.ATT_NAME) + " which is part of a substitution must have a type which derives from the type of the element at the head of the substitution group");
                    noErrorSoFar = false;
                }
            } else {
                ComplexTypeInfo subTypeInfo = typeInfo;
                for (; subTypeInfo != null && substitutionGroupEltTypeInfo != null && subTypeInfo != substitutionGroupEltTypeInfo; subTypeInfo = subTypeInfo.baseComplexTypeInfo);
                if (subTypeInfo == null ||
                    (substitutionGroupEltTypeInfo == null &&
                    reportGenericSchemaError("Element " + elementDecl.getAttribute(SchemaSymbols.ATT_NAME) + " has a type which does not derive from the type of the element at the head of the substitution group");
                    noErrorSoFar = false;
                    if((derivationMethod & finalSet) != 0) {
                        noErrorSoFar = false;
                        reportGenericSchemaError("element " + elementDecl.getAttribute(SchemaSymbols.ATT_NAME)
                            + " cannot be part of the substitution group headed by "
                            + substitutionGroupElementDecl.getAttribute(SchemaSymbols.ATT_NAME));
                    }
                }
            }
            if (dv == substitutionGroupEltDV)
                return;
            if (!(checkSimpleTypeDerivationOK(dv,substitutionGroupEltDV)) &&
                !(substitutionGroupEltTypeInfo == null &&
                  substitutionGroupEltDV == null)) {
               reportGenericSchemaError("Element " + elementDecl.getAttribute(SchemaSymbols.ATT_NAME) + " has a type which does not derive from the type of the element at the head of the substitution group");
               noErrorSoFar = false;
            }
                if((SchemaSymbols.RESTRICTION & finalSet) != 0) {
                    noErrorSoFar = false;
                    reportGenericSchemaError("element " + elementDecl.getAttribute(SchemaSymbols.ATT_NAME)
                        + " cannot be part of the substitution group headed by "
                        + substitutionGroupElementDecl.getAttribute(SchemaSymbols.ATT_NAME));
                }
            }
        }
    }

    private boolean checkSimpleTypeDerivationOK(DatatypeValidator d, DatatypeValidator b) {
       if (b instanceof AnySimpleType)
          return true;

       DatatypeValidator dTemp = d;
       for(; dTemp != null; dTemp = dTemp.getBaseValidator()) {
           if(dTemp == b) break;
       }
       if (dTemp == null) {
           if(b instanceof UnionDatatypeValidator) {
               Vector subUnionMemberDV = ((UnionDatatypeValidator)b).getBaseValidators();
               int subUnionSize = subUnionMemberDV.size();
               boolean found = false;
               for (int i=0; i<subUnionSize && !found; i++) {
                   DatatypeValidator dTempSub = (DatatypeValidator)subUnionMemberDV.elementAt(i);
                   if (dTempSub instanceof AnySimpleType)
                      return true;

                   DatatypeValidator dTempOrig = d;
                   for(; dTempOrig != null; dTempOrig = dTempOrig.getBaseValidator()) {
                       if(dTempSub == dTempOrig) {
                           found = true;
                           break;
                       }
                   }
               }
               if(!found) {
                  return false;
               }
           } else {
               return false;
           }
       }

    return true;
    }

    private Element getTopLevelComponentByName(String componentCategory, String name) throws Exception {
        Element child = null;
        SchemaInfo curr = fSchemaInfoListRoot;
        for (; curr != null || curr == fSchemaInfoListRoot; curr = curr.getNext()) {
            if (curr != null) curr.restore();
            if ( componentCategory.equals(SchemaSymbols.ELT_GROUP) ) {
                child = (Element) fSchemaGrammar.topLevelGroupDecls.get(name);
            }
            else if ( componentCategory.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP ) && fSchemaInfoListRoot == null ) {
                child = (Element) fSchemaGrammar.topLevelAttrGrpDecls.get(name);
            }
            else if ( componentCategory.equals(SchemaSymbols.ELT_ATTRIBUTE ) ) {
                child = (Element) fSchemaGrammar.topLevelAttrDecls.get(name);
            }

            if (child != null ) {
                break;
            }

            child = XUtil.getFirstChildElement(fSchemaRootElement);

            if (child == null) {
                continue;
            }

            while (child != null ){
                if ( child.getLocalName().equals(componentCategory)) {
                    if (child.getAttribute(SchemaSymbols.ATT_NAME).equals(name)) {
                        break;
                    }
                } else if (fRedefineSucceeded && child.getLocalName().equals(SchemaSymbols.ELT_REDEFINE)) {
                    Element gChild = XUtil.getFirstChildElement(child);
                    while (gChild != null ){
                        if (gChild.getLocalName().equals(componentCategory)) {
                            if (gChild.getAttribute(SchemaSymbols.ATT_NAME).equals(name)) {
                                break;
                            }
                        }
                        gChild = XUtil.getNextSiblingElement(gChild);
                    }
                    if (gChild != null) {
                        child = gChild;
                        break;
                    }
                }
                child = XUtil.getNextSiblingElement(child);
            }
            if (child != null || fSchemaInfoListRoot == null) break;
        }
        if(curr != null)
            curr.restore();
        else
            if (fSchemaInfoListRoot != null)
                fSchemaInfoListRoot.restore();
        return child;
    }

    private boolean isTopLevel(Element component) {
        String parentName = component.getParentNode().getLocalName();
        return (parentName.endsWith(SchemaSymbols.ELT_SCHEMA))
            || (parentName.endsWith(SchemaSymbols.ELT_REDEFINE)) ;
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
     * Traverses notation declaration
     * and saves it in a registry.
     * Notations are stored in registry with the following
     * key: "uri:localname"
     *
     * @param notation child <notation>
     * @return  local name of notation
     * @exception Exception
     */
    private String traverseNotationDecl( Element notation ) throws Exception {
        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(notation, scope);

        String name = notation.getAttribute(SchemaSymbols.ATT_NAME);
        String qualifiedName =name;
        if (fTargetNSURIString.length () != 0) {
            qualifiedName = fTargetNSURIString+":"+name;
        }
        if (fNotationRegistry.get(qualifiedName)!=null) {
            return name;
        }
        String publicId = notation.getAttribute(SchemaSymbols.ATT_PUBLIC);
        String systemId = notation.getAttribute(SchemaSymbols.ATT_SYSTEM);
        if (publicId.length() == 0 && systemId.length() == 0) {
            reportGenericSchemaError("<notation> declaration is invalid");
        }
        if (name.length() == 0) {
            reportGenericSchemaError("<notation> declaration does not have a name");

        }

        fNotationRegistry.put(qualifiedName, name);

        checkContent( notation, XUtil.getFirstChildElement(notation), true );

        return name;
    }

    /**
     * This methods will traverse notation from current schema,
     * as well as from included or imported schemas
     *
     * @param notationName
     *               localName of notation
     * @param uriStr uriStr for schema grammar
     * @return  return local name for Notation (if found), otherwise
     *         return empty string;
     * @exception Exception
     */
    private String traverseNotationFromAnotherSchema( String notationName , String uriStr ) throws Exception {

        Grammar grammar = fGrammarResolver.getGrammar(uriStr);
        if (uriStr == null || grammar==null ||! (grammar instanceof SchemaGrammar) ) {
            reportGenericSchemaError("!!Schema not found in #traverseNotationDeclFromAnotherSchema, "+
                                     "schema uri: " + uriStr
                                     +", groupName: " + notationName);
            return "";
        }
        SchemaGrammar aGrammar = (SchemaGrammar)grammar;

        String savedNSURIString = fTargetNSURIString;
        fTargetNSURIString = fStringPool.toString(fStringPool.addSymbol(aGrammar.getTargetNamespaceURI()));
        if (DEBUGGING) {
            System.out.println("[traverseFromAnotherSchema]: " + fTargetNSURIString);
        }

        String qualifiedName = fTargetNSURIString + ":" + notationName;
        String localName = (String)fNotationRegistry.get(qualifiedName);

            return localName;

        Element notationDecl = (Element) aGrammar.topLevelNotationDecls.get((Object)notationName);
        if (notationDecl == null) {
            reportGenericSchemaError( "no notation named \"" + notationName
                                      + "\" was defined in schema : " + uriStr);
            return "";
        }

        localName = traverseNotationDecl(notationDecl);
        fTargetNSURIString = savedNSURIString;
        return localName;



    /**
     * Traverse Group Declaration.
     *
     * <group
     *         id = ID
     *         maxOccurs = string
     *         minOccurs = nonNegativeInteger
     *         name = NCName
     *         ref = QName>
     *   Content: (annotation? , (all | choice | sequence)?)
     * <group/>
     *
     * @param elementDecl
     * @return
     * @exception Exception
     */
    private GroupInfo traverseGroupDecl( Element groupDecl ) throws Exception {

        int scope = isTopLevel(groupDecl)?
                    GeneralAttrCheck.ELE_CONTEXT_GLOBAL:
                    GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(groupDecl, scope);

        String groupName = groupDecl.getAttribute(SchemaSymbols.ATT_NAME);
        String ref = groupDecl.getAttribute(SchemaSymbols.ATT_REF);
        GroupInfo gInfo = null;
    Element child = checkContent( groupDecl, XUtil.getFirstChildElement(groupDecl), true );

        if (ref.length() != 0) {
            if (isTopLevel(groupDecl))
                reportGenericSchemaError ( "A group with \"ref\" present must not have <schema> or <redefine> as its parent");
            if (groupName.length() != 0)
                reportGenericSchemaError ( "group " + groupName + " cannot refer to another group, but it refers to " + ref);

            if (XUtil.getFirstChildElement(groupDecl)!=null)
                reportGenericSchemaError ( "A group with \"ref\" present must not have children");
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
                gInfo = traverseGroupDeclFromAnotherSchema(localpart, uriStr);
                if (gInfo != null) {
                  if (DEBUG_NEW_GROUP)
                    findAndCreateElements(gInfo.contentSpecIndex,gInfo.scope);
                }
                return gInfo;
            }

            try {
              gInfo = (GroupInfo) fGroupNameRegistry.get(uriStr + "," + localpart);
              if (gInfo != null) {
                if (DEBUG_NEW_GROUP)
                  findAndCreateElements(gInfo.contentSpecIndex,gInfo.scope);
        return gInfo;
              }

            }
            catch (ClassCastException c) {
            }


            if (fCurrentGroupNameStack.search((Object)localpart) > - 1) {
                reportGenericSchemaError("mg-props-correct: Circular definition for group " + localpart);
                return null;
            }

            int contentSpecIndex = -1;
            Element referredGroup = getTopLevelComponentByName(SchemaSymbols.ELT_GROUP,localpart);
            if (referredGroup == null) {
                reportGenericSchemaError("Group " + localpart + " not found in the Schema");
            }
            else {
                gInfo = traverseGroupDecl(referredGroup);
            }

            if (gInfo != null) {
               if (DEBUG_NEW_GROUP)
                 findAndCreateElements(gInfo.contentSpecIndex,gInfo.scope);
            }
            return gInfo;

        } else if (groupName.length() == 0)
            reportGenericSchemaError("a <group> must have a name or a ref present");

    String qualifiedGroupName = fTargetNSURIString + "," + groupName;
        try {
          gInfo = (GroupInfo) fGroupNameRegistry.get(qualifiedGroupName);
          if (gInfo != null) {
            if (DEBUG_NEW_GROUP)
              findAndCreateElements(gInfo.contentSpecIndex,gInfo.scope);
        return gInfo;
          }

        }
        catch (ClassCastException c) {
        }


        fCurrentGroupNameStack.push(groupName);

        int savedScope = fCurrentScope;
        if (DEBUG_NEW_GROUP)
           fCurrentScope = fScopeCount++;
        else
           fCurrentScope = -1;

        int index = -2;

        boolean illegalChild = false;
        String childName =
            (child != null) ? child.getLocalName() : "";

        if (childName.equals(SchemaSymbols.ELT_ALL)) {
            index = traverseAll(child);
        }
        else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
            index = traverseChoice(child);
        }
        else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
            index = traverseSequence(child);
        }
        else if (childName.length() != 0 || (child != null && XUtil.getNextSiblingElement(child) != null)) {
            illegalChild = true;
            reportSchemaError(SchemaMessageProvider.GroupContentRestricted,
                              new Object [] { "group", childName });
        }

        if (child == null) {
            reportGenericSchemaError("Named group must contain an 'all', 'choice' or 'sequence' child");
        }
        else if (XUtil.getNextSiblingElement(child) != null) {
            illegalChild = true;
            reportSchemaError(SchemaMessageProvider.GroupContentRestricted,
                              new Object [] { "group", childName });
        }
        if ( ! illegalChild && child != null) {
            index = handleOccurrences(index, child, CHILD_OF_GROUP);
        }

    gInfo = new GroupInfo();
        gInfo.contentSpecIndex = index;
        gInfo.scope = fCurrentScope;
        fCurrentScope = savedScope;
        fCurrentGroupNameStack.pop();
    fGroupNameRegistry.put(qualifiedGroupName, gInfo);
        return gInfo;
    }

    private void findAndCreateElements(int csIndex, int scope) {

        if (csIndex<0 || fCurrentScope==TOP_LEVEL_SCOPE) {
           return;
        }

        fSchemaGrammar.getContentSpec( csIndex, tempContentSpec1);

        int type = tempContentSpec1.type;
        int left = tempContentSpec1.value;
        int right = tempContentSpec1.otherValue;

        if (type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
          int eltNdx = fSchemaGrammar.getElementDeclIndex(right, left, scope);
          if (eltNdx <0)
            return;

          ComplexTypeInfo typeInfo = fSchemaGrammar.getElementComplexTypeInfo(eltNdx);
          int scopeDefined = typeInfo != null ? typeInfo.scopeDefined : fCurrentScope;

          int newIdx = fSchemaGrammar.cloneElementDecl(eltNdx, fCurrentScope, scopeDefined);

          int count = fElementRecurseComplex.size();
          for (int i = 0; i < count; i++) {
             ElementInfo eobj = (ElementInfo)fElementRecurseComplex.elementAt(i);
             if (eobj.elementIndex == eltNdx) {
                fElementRecurseComplex.addElement(new ElementInfo(newIdx,eobj.typeName));
                break;
             }
          }
        }
        else if (type == XMLContentSpec.CONTENTSPECNODE_CHOICE ||
                 type == XMLContentSpec.CONTENTSPECNODE_ALL ||
                 type == XMLContentSpec.CONTENTSPECNODE_SEQ) {

          findAndCreateElements(left,scope);

          if (right != -2)
             findAndCreateElements(right,scope);
        }

    else if (type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE
          || type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE
          || type == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE) {

          findAndCreateElements(left,scope);
    }
        return;

    }

    private GroupInfo traverseGroupDeclFromAnotherSchema( String groupName , String uriStr ) throws Exception {

        GroupInfo gInfo = null;
        Grammar grammar = fGrammarResolver.getGrammar(uriStr);
        if (uriStr == null || grammar==null ||! (grammar instanceof SchemaGrammar) ) {
            reportGenericSchemaError("!!Schema not found in #traverseGroupDeclFromAnotherSchema, "+
                                     "schema uri: " + uriStr
                                     +", groupName: " + groupName);
            return null;
        }
        SchemaGrammar aGrammar = (SchemaGrammar)grammar;

        Element groupDecl = (Element) aGrammar.topLevelGroupDecls.get((Object)groupName);
        if (groupDecl == null) {
            reportGenericSchemaError( "no group named \"" + groupName
                                      + "\" was defined in schema : " + uriStr);
            return null;
        }

        NamespacesScope saveNSMapping = fNamespacesScope;
        int saveTargetNSUri = fTargetNSURI;
        fTargetNSURI = fStringPool.addSymbol(aGrammar.getTargetNamespaceURI());
        fNamespacesScope = aGrammar.getNamespacesScope();

    Element child = checkContent( groupDecl, XUtil.getFirstChildElement(groupDecl), true );

    String qualifiedGroupName = fTargetNSURIString + "," + groupName;
        try {
        gInfo = (GroupInfo) fGroupNameRegistry.get(qualifiedGroupName);
            if (gInfo != null)
              return gInfo;
        } catch (ClassCastException c) {
        }


        int index = -2;
        int savedScope = fCurrentScope;
        if (DEBUG_NEW_GROUP)
          fCurrentScope = fScopeCount++;

        boolean illegalChild = false;
    String childName = (child != null) ? child.getLocalName() : "";
        if (childName.equals(SchemaSymbols.ELT_ALL)) {
            index = traverseAll(child);
        }
        else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
            index = traverseChoice(child);
        }
        else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
            index = traverseSequence(child);
        }
        else if (childName.length() != 0 || (child != null && XUtil.getNextSiblingElement(child) != null)) {
            illegalChild = true;
            reportSchemaError(SchemaMessageProvider.GroupContentRestricted,
                              new Object [] { "group", childName });
        }
        if ( ! illegalChild && child != null) {
            index = handleOccurrences( index, child);
        }

    gInfo = new GroupInfo();
        gInfo.contentSpecIndex = index;
        gInfo.scope = fCurrentScope;
        fCurrentScope = savedScope;
    fGroupNameRegistry.put(qualifiedGroupName, gInfo);
    fNamespacesScope = saveNSMapping;
    fTargetNSURI = saveTargetNSUri;
        return gInfo;



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
        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(sequenceDecl, scope);

        Element child = checkContent(sequenceDecl, XUtil.getFirstChildElement(sequenceDecl), true);

        int csnType = XMLContentSpec.CONTENTSPECNODE_SEQ;

        int left = -2;
        int right = -2;
        boolean hadContent = false;

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
            else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                GroupInfo grpInfo = traverseGroupDecl(child);
                index = (grpInfo != null) ? grpInfo.contentSpecIndex:-2;

                if (hasAllContent(index)) {
                    reportSchemaError(SchemaMessageProvider.AllContentLimited,
                                      new Object [] { "sequence" });
                    continue;
                }

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
                reportSchemaError(
                             SchemaMessageProvider.SeqChoiceContentRestricted,
                             new Object [] { "sequence", childName });
                continue;
            }

            if (index != -2)
                hadContent = true;

            if (seeParticle) {
                index = handleOccurrences( index, child);
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

        if (hadContent) {
            if (right != -2 || fSchemaGrammar.getDeferContentSpecExpansion())
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right,
                                                         false);
        }

        return left;
    }

    /**
    *
    * Traverse the Choice declaration
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
        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(choiceDecl, scope);

        Element child = checkContent(choiceDecl, XUtil.getFirstChildElement(choiceDecl), true);

        int csnType = XMLContentSpec.CONTENTSPECNODE_CHOICE;

        int left = -2;
        int right = -2;
        boolean hadContent = false;

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
            else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                GroupInfo grpInfo = traverseGroupDecl(child);
                index = (grpInfo != null) ? grpInfo.contentSpecIndex:-2;

                if (hasAllContent(index)) {
                    reportSchemaError(SchemaMessageProvider.AllContentLimited,
                                      new Object [] { "choice" });
                    continue;
                }

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
                reportSchemaError(
                              SchemaMessageProvider.SeqChoiceContentRestricted,
                              new Object [] { "choice", childName });
                continue;
            }

            if (index != -2)
                hadContent = true;

            if (seeParticle) {
                index = handleOccurrences( index, child);
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


        if (hadContent) {
            if (right != -2 || fSchemaGrammar.getDeferContentSpecExpansion())
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right,
                                                         false);
        }
        return left;
    }


   /**
    *
    * Traverse the "All" declaration
    *
    * <all
    *   id = ID
    *   maxOccurs = 1 : 1
    *   minOccurs = (0 | 1) : 1>
    *   Content: (annotation? , element*)
    * </all>
    **/

    int traverseAll(Element allDecl) throws Exception {
        int scope = GeneralAttrCheck.ELE_CONTEXT_LOCAL;
        Hashtable attrValues = generalCheck(allDecl, scope);

        Element child = checkContent(allDecl,
                                     XUtil.getFirstChildElement(allDecl), true);

        int csnType = XMLContentSpec.CONTENTSPECNODE_ALL;

        int left = -2;
        int right = -2;
        boolean hadContent = false;

        for (;
             child != null;
             child = XUtil.getNextSiblingElement(child)) {
            int index = -2;

            String childName = child.getLocalName();

            if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
                QName eltQName = traverseElementDecl(child);
                index = fSchemaGrammar.addContentSpecNode(
                                           XMLContentSpec.CONTENTSPECNODE_LEAF,
                                           eltQName.localpart,
                                           eltQName.uri,
                                           false);

                index = handleOccurrences(index, child, PROCESSING_ALL_EL);
            }
            else {
                reportSchemaError(SchemaMessageProvider.AllContentRestricted,
                                  new Object [] { childName });
                continue;
            }

            hadContent = true;

            if (left == -2) {
                left = index;
            } else if (right == -2) {
                right = index;
            } else {
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right,
                                                         false);
                right = index;
            }
        }

        if (hadContent) {
            if (right != -2 || fSchemaGrammar.getDeferContentSpecExpansion())
                left = fSchemaGrammar.addContentSpecNode(csnType, left, right,
                                                         false);
        }

        return left;
    }

    private boolean hasAllContent(int contentSpecIndex) {
        if (contentSpecIndex > -1) {
            XMLContentSpec content = new XMLContentSpec();
            fSchemaGrammar.getContentSpec(contentSpecIndex, content);

            if (content.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE) {
                fSchemaGrammar.getContentSpec(content.value, content);
            }

            return (content.type == XMLContentSpec.CONTENTSPECNODE_ALL);
        }

        return false;
    }

    private Hashtable generalCheck(Element element, int scope) throws Exception{
        String uri = element.getNamespaceURI();
        if (uri == null || !uri.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
            reportGenericSchemaError("The namespce name for '"+element.getLocalName()+"' must be "+SchemaSymbols.URI_SCHEMAFORSCHEMA);
        }
        return fGeneralAttrCheck.checkAttributes (element, scope);
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
                    return SchemaSymbols.ENUMERATION+SchemaSymbols.RESTRICTION+SchemaSymbols.LIST;
            } else {
                    int enumerate = 0;
                    int restrict = 0;
                    int list = 0;

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

                    return enumerate+list;
            }
    }


    private int parseDerivationSet (String finalString)  throws Exception
    {
            if ( finalString.equals (SchemaSymbols.ATTVAL_POUNDALL) ) {
                    return SchemaSymbols.EXTENSION+SchemaSymbols.RESTRICTION;
            } else {
                    int extend = 0;
                    int restrict = 0;

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

                    return extend+restrict;
            }
    }

    private int parseBlockSet (String blockString)  throws Exception
    {
            if( blockString == null)
                return fBlockDefault;
            else if ( blockString.equals (SchemaSymbols.ATTVAL_POUNDALL) ) {
                    return SchemaSymbols.SUBSTITUTION+SchemaSymbols.EXTENSION+SchemaSymbols.RESTRICTION;
            } else {
                    int extend = 0;
                    int restrict = 0;
                    int substitute = 0;

                    StringTokenizer t = new StringTokenizer (blockString, " ");
                    while (t.hasMoreTokens()) {
                            String token = t.nextToken ();

                            if ( token.equals (SchemaSymbols.ATTVAL_SUBSTITUTION) ) {
                                    if ( substitute == 0 ) {
                                            substitute = SchemaSymbols.SUBSTITUTION;
                                    } else {
                                            reportGenericSchemaError ( "The value 'substitution' already in the list" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_EXTENSION) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EXTENSION;
                                    } else {
                                            reportGenericSchemaError ( "The value 'extension' is already in the list" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_RESTRICTION) ) {
                                    if ( restrict == 0 ) {
                                            restrict = SchemaSymbols.RESTRICTION;
                                    } else {
                                            reportGenericSchemaError ( "The value 'restriction' is already in the list" );
                                    }
                            } else {
                                    reportGenericSchemaError ( "Invalid block value (" + blockString + ")" );
                            }
                    }

                    int defaultVal = extend+restrict+substitute;
                    return (defaultVal == 0 ? fBlockDefault : defaultVal);
            }
    }

    private int parseFinalSet (String finalString)  throws Exception
    {
            if( finalString == null) {
                return fFinalDefault;
            }
            else if ( finalString.equals (SchemaSymbols.ATTVAL_POUNDALL) ) {
                    return SchemaSymbols.EXTENSION+SchemaSymbols.LIST+SchemaSymbols.RESTRICTION+SchemaSymbols.UNION;
            } else {
                    int extend = 0;
                    int restrict = 0;
                    int list = 0;
                    int union = 0;

                    StringTokenizer t = new StringTokenizer (finalString, " ");
                    while (t.hasMoreTokens()) {
                            String token = t.nextToken ();

                            if ( token.equals (SchemaSymbols.ELT_UNION) ) {
                                    if ( union == 0 ) {
                                            union = SchemaSymbols.UNION;
                                    } else {
                                            reportGenericSchemaError ( "The value 'union' is already in the list" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_EXTENSION) ) {
                                    if ( extend == 0 ) {
                                            extend = SchemaSymbols.EXTENSION;
                                    } else {
                                            reportGenericSchemaError ( "The value 'extension' is already in the list" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ELT_LIST) ) {
                                    if ( list == 0 ) {
                                            list = SchemaSymbols.LIST;
                                    } else {
                                            reportGenericSchemaError ( "The value 'list' is already in the list" );
                                    }
                            } else if ( token.equals (SchemaSymbols.ATTVAL_RESTRICTION) ) {
                                    if ( restrict == 0 ) {
                                            restrict = SchemaSymbols.RESTRICTION;
                                    } else {
                                            reportGenericSchemaError ( "The value 'restriction' is already in the list" );
                                    }
                            } else {
                                    reportGenericSchemaError ( "Invalid final value (" + finalString + ")" );
                            }
                    }

                    int defaultVal = extend+restrict+list+union;
                    return (defaultVal == 0 ? fFinalDefault : defaultVal);
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

    /** Don't check the following code in because it creates a dependency on
        the serializer, preventing to package the parser without the serializer
    public static void main(String args[] ) {

        if( args.length != 1 ) {
            System.out.println( "Error: Usage java TraverseSchema yourFile.xsd" );
            System.exit(0);
        }

        DOMParser parser = new IgnoreWhitespaceParser();
        parser.setEntityResolver( (fEntityResolver != null)? (fEntityResolver):(new Resolver()) );
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
    **/

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
        public void error(SAXParseException ex) throws SAXException {
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

    public class SchemaInfo {
        private Element saveRoot;
        private SchemaInfo nextRoot;
        private SchemaInfo prevRoot;
        private String savedSchemaURL = fCurrentSchemaURL;
        private boolean saveElementDefaultQualified = fElementDefaultQualified;
        private boolean saveAttributeDefaultQualified = fAttributeDefaultQualified;
        private int saveBlockDefault = fBlockDefault;
        private int saveFinalDefault = fFinalDefault;
        private NamespacesScope saveNamespacesScope = fNamespacesScope;

        public SchemaInfo ( boolean saveElementDefaultQualified, boolean saveAttributeDefaultQualified,
                int saveBlockDefault, int saveFinalDefault,
                String savedSchemaURL, Element saveRoot,
                NamespacesScope saveNamespacesScope, SchemaInfo nextRoot, SchemaInfo prevRoot) {
            this.saveElementDefaultQualified = saveElementDefaultQualified;
            this.saveAttributeDefaultQualified = saveAttributeDefaultQualified;
            this.saveBlockDefault = saveBlockDefault;
            this.saveFinalDefault = saveFinalDefault;
            this.savedSchemaURL = savedSchemaURL;
            this.saveRoot  = saveRoot ;
            if(saveNamespacesScope != null)
                this.saveNamespacesScope  = (NamespacesScope)saveNamespacesScope.clone();
            this.nextRoot = nextRoot;
            this.prevRoot = prevRoot;
        }
        public void setNext (SchemaInfo next) {
            nextRoot = next;
        }
        public SchemaInfo getNext () {
            return nextRoot;
        }
        public void setPrev (SchemaInfo prev) {
            prevRoot = prev;
        }
        public String getCurrentSchemaURL() { return savedSchemaURL; }
        public SchemaInfo getPrev () {
            return prevRoot;
        }
        public Element getRoot() { return saveRoot; }
        public void restore() {
            fCurrentSchemaURL = savedSchemaURL;
            fElementDefaultQualified = saveElementDefaultQualified;
            fAttributeDefaultQualified = saveAttributeDefaultQualified;
            fBlockDefault = saveBlockDefault;
            fFinalDefault = saveFinalDefault;
            fNamespacesScope = (NamespacesScope)saveNamespacesScope.clone();
            fSchemaRootElement = saveRoot;
        }

