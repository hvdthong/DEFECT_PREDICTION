package org.apache.xerces.validators.common;

import org.apache.xerces.framework.XMLAttrList;
import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.framework.XMLDocumentHandler;
import org.apache.xerces.framework.XMLDocumentScanner;
import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.readers.DefaultEntityHandler;
import org.apache.xerces.readers.XMLEntityHandler;
import org.apache.xerces.utils.ChunkyCharArray;
import org.apache.xerces.utils.Hash2intTable;
import org.apache.xerces.utils.IntStack;
import org.apache.xerces.utils.NamespacesScope;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.utils.XMLCharacterProperties;
import org.apache.xerces.utils.XMLMessages;
import org.apache.xerces.utils.ImplementationMessages;

import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.xerces.validators.dtd.DTDGrammar;

import org.apache.xerces.validators.schema.GeneralAttrCheck;
import org.apache.xerces.validators.schema.SubstitutionGroupComparator;
import org.apache.xerces.validators.schema.SchemaGrammar;
import org.apache.xerces.validators.schema.SchemaMessageProvider;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.validators.schema.TraverseSchema;
import org.apache.xerces.validators.schema.identity.Field;
import org.apache.xerces.validators.schema.identity.FieldActivator;
import org.apache.xerces.validators.schema.identity.IdentityConstraint;
import org.apache.xerces.validators.schema.identity.IDValue;
import org.apache.xerces.validators.schema.identity.Key;
import org.apache.xerces.validators.schema.identity.KeyRef;
import org.apache.xerces.validators.schema.identity.Selector;
import org.apache.xerces.validators.schema.identity.Unique;
import org.apache.xerces.validators.schema.identity.ValueStore;
import org.apache.xerces.validators.schema.identity.XPathMatcher;

import org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl;
import org.apache.xerces.validators.datatype.DatatypeValidator;
import org.apache.xerces.validators.datatype.InvalidDatatypeValueException;
import org.apache.xerces.validators.datatype.StateMessageDatatype;
import org.apache.xerces.validators.datatype.IDREFDatatypeValidator;
import org.apache.xerces.validators.datatype.IDDatatypeValidator;
import org.apache.xerces.validators.datatype.ENTITYDatatypeValidator;
import org.apache.xerces.validators.datatype.NOTATIONDatatypeValidator;
import org.apache.xerces.validators.datatype.UnionDatatypeValidator;
import org.apache.xerces.validators.datatype.AnySimpleType;

/**
 * This class is the super all-in-one validator used by the parser.
 *
 * @version $Id: XMLValidator.java 317921 2001-11-15 18:56:03Z  $
 */
public final class XMLValidator
    implements DefaultEntityHandler.EventHandler,
               XMLEntityHandler.CharDataHandler,
               XMLDocumentScanner.EventHandler,
               NamespacesScope.NamespacesHandler,
    {



   private static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
   private static final boolean DEBUG_PRINT_ATTRIBUTES = false;
   private static final boolean DEBUG_PRINT_CONTENT = false;
   private static final boolean DEBUG_SCHEMA_VALIDATION = false;
   private static final boolean DEBUG_ELEMENT_CHILDREN = false;

   /** Compile to true to debug identity constraints. */
   protected static final boolean DEBUG_IDENTITY_CONSTRAINTS = false;

   /** Compile to true to debug value stores. */
   protected static final boolean DEBUG_VALUE_STORES = false;


   private static final int CHUNK_SIZE = (1 << CHUNK_SHIFT);
   private static final int CHUNK_MASK = CHUNK_SIZE - 1;

   private Hashtable fIdDefs = new Hashtable();
   private Hashtable fIdREFDefs = new Hashtable();

   private  StateMessageDatatype fValidateIDRef = new StateMessageDatatype() {
      private Hashtable fIdDefs;
      public Object getDatatypeObject(){
         return(Object) fIdDefs;
      }
      public int getDatatypeState(){
         return IDREFDatatypeValidator.IDREF_VALIDATE;
      }
      public void setDatatypeObject( Object data ){
         fIdDefs = (Hashtable) data;
      }
   };

   private  StateMessageDatatype fCheckIDRef = new StateMessageDatatype() {
      private Object[] fLists;
      public Object getDatatypeObject(){
         return(Object) fLists;
      }
      public int getDatatypeState(){
         return IDREFDatatypeValidator.IDREF_CHECKID;
      }
      public void setDatatypeObject( Object data ){
        fLists = (Object[]) data;
      }
   };

   private  StateMessageDatatype fValidateEntity = new StateMessageDatatype() {
      private Object fData;
      public Object getDatatypeObject(){
         return fData;
      }
      public int getDatatypeState(){
         return ENTITYDatatypeValidator.ENTITY_VALIDATE;
      }
      public void setDatatypeObject( Object data ){
         fData = data;
      }
   };




   private AttributeValidator fAttValidatorNOTATION = new AttValidatorNOTATION();
   private AttributeValidator fAttValidatorENUMERATION = new AttValidatorENUMERATION();
   private AttributeValidator fAttValidatorDATATYPE = null;


   StringPool fStringPool = null;
   boolean fValidating = false;
   boolean fInElementContent = false;
   int fStandaloneReader = -1;



   private boolean fValidationEnabled = false;
   private boolean fDynamicValidation = false;
   private boolean fSchemaValidation = true;
   private boolean fSchemaValidationFullChecking = false;
   private boolean fValidationEnabledByDynamic = false;
   private boolean fDynamicDisabledByValidation = false;
   private boolean fWarningOnDuplicateAttDef = false;
   private boolean fWarningOnUndeclaredElements = false;
   private boolean fNormalizeAttributeValues = true;
   private boolean fLoadDTDGrammar = true;
   private boolean fNormalizeContents = false;

   
   
  private Hashtable fLocationUriPairs = new Hashtable(10);



   private String fExternalSchemas = null;
   private String fExternalNoNamespaceSchema = null;
   private DOMParser fSchemaGrammarParser = null;
   private int fDeclaration[];
   private XMLErrorReporter fErrorReporter = null;
   private DefaultEntityHandler fEntityHandler = null;
   private QName fCurrentElement = new QName();

   private ContentLeafNameTypeVector[] fContentLeafStack = new ContentLeafNameTypeVector[8];

   private XMLContentModel[] fContentModelStack = new XMLContentModel[8];
   private int[] fContentModelStateStack = new int[8];
   private int[] fContentModelEleCount = new int[8];

   private int[] fValidationFlagStack = new int[8];

   private int[] fScopeStack = new int[8];
   private int[] fGrammarNameSpaceIndexStack = new int[8];

   private int[] fElementEntityStack = new int[8];
   private int[] fElementIndexStack = new int[8];
   private int[] fContentSpecTypeStack = new int[8];

   private static final int sizeQNameParts      = 8;
   private QName[] fElementQNamePartsStack      = new QName[sizeQNameParts];

   private QName[] fElementChildren = new QName[32];
   private int fElementChildrenLength = 0;
   private int[] fElementChildrenOffsetStack = new int[32];
   private int fElementDepth = -1;

   private boolean fNamespacesEnabled = false;
   private NamespacesScope fNamespacesScope = null;
   private int fNamespacesPrefix = -1;
   private QName fRootElement = new QName();
   private int fAttrListHandle = -1;
   private int fCurrentElementEntity = -1;
   private int fCurrentElementIndex = -1;
   private int fCurrentContentSpecType = -1;
   private boolean fSeenDoctypeDecl = false;

   private final int TOP_LEVEL_SCOPE = -1;
   private int fCurrentScope = TOP_LEVEL_SCOPE;
   private int fCurrentSchemaURI = StringPool.EMPTY_STRING;
   private int fEmptyURI = StringPool.EMPTY_STRING;
   private int fXsiPrefix = - 1;
   private int fXsiURI = -2;
   private int fXsiTypeAttValue = -1;
   private DatatypeValidator fXsiTypeValidator = null;

   private boolean fNil = false;

   private Grammar fGrammar = null;
   private int fGrammarNameSpaceIndex = StringPool.EMPTY_STRING;
   private GrammarResolver fGrammarResolver = null;



   private boolean fScanningDTD = false;
   private XMLDocumentScanner fDocumentScanner = null;
   private boolean fCalledStartDocument = false;
   private XMLDocumentHandler fDocumentHandler = null;
   private XMLDocumentHandler.DTDHandler fDTDHandler = null;
   private boolean fSeenRootElement = false;
   private XMLAttrList fAttrList = null;
   private int fXMLLang = -1;
   private LocatorImpl fAttrNameLocator = null;
   private boolean fCheckedForSchema = false;
   private boolean fDeclsAreExternal = false;
   private StringPool.CharArrayRange fCurrentElementCharArrayRange = null;
   private char[] fCharRefData = null;
   private boolean fSendCharDataAsCharArray = false;
   private boolean fBufferDatatype = false;
   private StringBuffer fDatatypeBuffer = new StringBuffer();

   private QName fTempQName = new QName();
   private XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
   private XMLAttributeDecl fTempAttributeDecl = new XMLAttributeDecl();
   private XMLElementDecl fTempElementDecl = new XMLElementDecl();

   private boolean fGrammarIsDTDGrammar = false;
   private boolean fGrammarIsSchemaGrammar = false;

   private boolean fNeedValidationOff = false;

    private static final boolean DEBUG_NORMALIZATION = false;




   private int fEMPTYSymbol = -1;
   private int fANYSymbol = -1;
   private int fMIXEDSymbol = -1;
   private int fCHILDRENSymbol = -1;
   private int fCDATASymbol = -1;
   private int fIDSymbol = -1;
   private int fIDREFSymbol = -1;
   private int fIDREFSSymbol = -1;
   private int fENTITYSymbol = -1;
   private int fENTITIESSymbol = -1;
   private int fNMTOKENSymbol = -1;
   private int fNMTOKENSSymbol = -1;
   private int fNOTATIONSymbol = -1;
   private int fENUMERATIONSymbol = -1;
   private int fREQUIREDSymbol = -1;
   private int fFIXEDSymbol = -1;
   private int fDATATYPESymbol = -1;
   private int fEpsilonIndex = -1;



   private DatatypeValidatorFactoryImpl fDataTypeReg = null;
   private DatatypeValidator            fValID   = null;
   private DatatypeValidator            fValIDRef    = null;
   private DatatypeValidator            fValIDRefs   = null;
   private DatatypeValidator            fValENTITY   = null;
   private DatatypeValidator            fValENTITIES = null;
   private DatatypeValidator            fValNMTOKEN  = null;
   private DatatypeValidator            fValNMTOKENS = null;
   private DatatypeValidator            fValNOTATION = null;


    /**
     * Stack of active XPath matchers for identity constraints. All
     * active XPath matchers are notified of startElement, characters
     * and endElement callbacks in order to perform their matches.
     * <p>
     * For each element with identity constraints, the selector of
     * each identity constraint is activated. When the selector matches
     * its XPath, then all the fields of the identity constraint are
     * activated.
     * <p>
     * <strong>Note:</strong> Once the activation scope is left, the
     * XPath matchers are automatically removed from the stack of
     * active matchers and no longer receive callbacks.
     */
    protected XPathMatcherStack fMatcherStack = new XPathMatcherStack();

    /** Cache of value stores for identity constraint fields. */
    protected ValueStoreCache fValueStoreCache = new ValueStoreCache();

    protected SubstitutionGroupComparator fSGComparator = null;

    protected Hashtable UPACheckedGrammarURIs = new Hashtable();
    protected static Object fgNullObject = new Object();


   /** Constructs an XML validator. */
   public XMLValidator(StringPool stringPool,
                       XMLErrorReporter errorReporter,
                       DefaultEntityHandler entityHandler,
                       XMLDocumentScanner documentScanner) {

      fStringPool = stringPool;
      fErrorReporter = errorReporter;
      fEntityHandler = entityHandler;
      fDocumentScanner = documentScanner;

      fValidateEntity.setDatatypeObject(new Object[]{entityHandler, stringPool});
      fValidateIDRef.setDatatypeObject(fIdREFDefs);
      fCheckIDRef.setDatatypeObject(new Object[]{fIdDefs, fIdREFDefs});

      fEmptyURI = fStringPool.addSymbol("");
      fXsiURI = fStringPool.addSymbol(SchemaSymbols.URI_XSI);
      fAttrList = new XMLAttrList(fStringPool);
      entityHandler.setEventHandler(this);
      entityHandler.setCharDataHandler(this);
      fDocumentScanner.setEventHandler(this);

      for (int i = 0; i < sizeQNameParts; i++) {
         fElementQNamePartsStack[i] = new QName();
      }
      init();


   public void setGrammarResolver(GrammarResolver grammarResolver){
      fGrammarResolver = grammarResolver;
    }



   /** Set char data processing preference and handlers. */
   public void initHandlers(boolean sendCharDataAsCharArray,
                            XMLDocumentHandler docHandler,
                            XMLDocumentHandler.DTDHandler dtdHandler) {

      fSendCharDataAsCharArray = sendCharDataAsCharArray;
      fEntityHandler.setSendCharDataAsCharArray(fSendCharDataAsCharArray);
      fDocumentHandler = docHandler;
      fDTDHandler = dtdHandler;


   /** Reset or copy. */
   public void resetOrCopy(StringPool stringPool) throws Exception {
      fAttrList = new XMLAttrList(stringPool);
      resetCommon(stringPool);
   }

   /** Reset. */
   public void reset(StringPool stringPool) throws Exception {
      fAttrList.reset(stringPool);
      resetCommon(stringPool);
   }



   /**
    * Turning on validation/dynamic turns on validation if it is off, and
    * this is remembered.  Turning off validation DISABLES validation/dynamic
    * if it is on.  Turning off validation/dynamic DOES NOT turn off
    * validation if it was explicitly turned on, only if it was turned on
    * BECAUSE OF the call to turn validation/dynamic on.  Turning on
    * validation will REENABLE and turn validation/dynamic back on if it
    * was disabled by a call that turned off validation while
    * validation/dynamic was enabled.
    */
   public void setValidationEnabled(boolean flag) throws Exception {
      fValidationEnabled = flag;
      fValidationEnabledByDynamic = false;
      if (fValidationEnabled) {
         if (fDynamicDisabledByValidation) {
            fDynamicValidation = true;
            fDynamicDisabledByValidation = false;
         }
      } else if (fDynamicValidation) {
         fDynamicValidation = false;
         fDynamicDisabledByValidation = true;
      }
      fValidating = fValidationEnabled;
      if (fValidating) {
        initDataTypeValidators();
      }
    }

   /** Returns true if validation is enabled. */
   public boolean getValidationEnabled() {
      return fValidationEnabled;
   }

   /** Sets whether Schema support is on/off. */
   public void setSchemaValidationEnabled(boolean flag) {
      fSchemaValidation = flag;
   }

   /** Returns true if Schema support is on. */
   public boolean getSchemaValidationEnabled() {
      return fSchemaValidation;
   }

   /** Sets whether full Schema error checking is on/off */
   public void setSchemaFullCheckingEnabled(boolean flag) {
      fSchemaValidationFullChecking = flag;
   }

   public void setExternalSchemas(Object value){
       fExternalSchemas = (String)value;
   }
   public void setExternalNoNamespaceSchema(Object value){
       fExternalNoNamespaceSchema = (String)value;
   }

   public String getExternalSchemas(){
       return fExternalSchemas;
   }
   public String getExternalNoNamespaceSchema(){
       return fExternalNoNamespaceSchema;
   }

   /** Returns true if full Schema checking is on. */
   public boolean getSchemaFullCheckingEnabled() {
      return fSchemaValidationFullChecking;
   }

   /** Sets whether validation is dynamic. */
   public void setDynamicValidationEnabled(boolean flag) throws Exception {
      fDynamicValidation = flag;
      fDynamicDisabledByValidation = false;
      if (!fDynamicValidation) {
         if (fValidationEnabledByDynamic) {
            fValidationEnabled = false;
            fValidationEnabledByDynamic = false;
         }
      } else if (!fValidationEnabled) {
         fValidationEnabled = true;
         fValidationEnabledByDynamic = true;
      }
      fValidating = fValidationEnabled;
      if (fValidating) {
        initDataTypeValidators();
      }
    }

   /** Returns true if validation is dynamic. */
   public boolean getDynamicValidationEnabled() {
      return fDynamicValidation;
   }

   /** Sets fNormalizeAttributeValues **/
   public void setNormalizeAttributeValues(boolean normalize){
      fNormalizeAttributeValues = normalize;
   }

   /** Sets fLoadDTDGrammar when validation is off **/
   public void setLoadDTDGrammar(boolean loadDG){
      if (fValidating) {
         fLoadDTDGrammar = true;
      } else {
         fLoadDTDGrammar = loadDG;
      }
   }

   /** Returns fLoadDTDGrammar **/
   public boolean getLoadDTDGrammar() {
      return fLoadDTDGrammar;
   }

   /** Sets whether namespaces are enabled. */
   public void setNamespacesEnabled(boolean flag) {
      fNamespacesEnabled = flag;
   }

   /** Returns true if namespaces are enabled. */
   public boolean getNamespacesEnabled() {
      return fNamespacesEnabled;
   }

   /** Sets whether duplicate attribute definitions signal a warning. */
   public void setWarningOnDuplicateAttDef(boolean flag) {
      fWarningOnDuplicateAttDef = flag;
   }

   /** Returns true if duplicate attribute definitions signal a warning. */
   public boolean getWarningOnDuplicateAttDef() {
      return fWarningOnDuplicateAttDef;
   }

   /** Sets whether undeclared elements signal a warning. */
   public void setWarningOnUndeclaredElements(boolean flag) {
      fWarningOnUndeclaredElements = flag;
   }

   /** Returns true if undeclared elements signal a warning. */
   public boolean getWarningOnUndeclaredElements() {
      return fWarningOnUndeclaredElements;
   }

   /** Sets fNormalizeContents **/
   public void setNormalizeContents(boolean normalize){
      fNormalizeContents = normalize;
   }
   public boolean getNormalizeConents() {
      return fNormalizeContents;
   }


   /**
    * Start the value scope for the specified identity constraint. This
    * method is called when the selector matches in order to initialize
    * the value store.
    *
    * @param identityConstraint The identity constraint.
    */
   public void startValueScopeFor(IdentityConstraint identityConstraint)
        throws Exception {

        for(int i=0; i<identityConstraint.getFieldCount(); i++) {
            Field field = identityConstraint.getFieldAt(i);
            ValueStoreBase valueStore = fValueStoreCache.getValueStoreFor(field);
            valueStore.startValueScope();
        }


    /**
     * Request to activate the specified field. This method returns the
     * matcher for the field.
     *
     * @param field The field to activate.
     */
    public XPathMatcher activateField(Field field) throws Exception {
        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: activateField(\""+field+"\")");
        }
        ValueStore valueStore = fValueStoreCache.getValueStoreFor(field);
        field.setMayMatch(true);
        XPathMatcher matcher = field.createMatcher(valueStore);
        fMatcherStack.addMatcher(matcher);
        matcher.startDocumentFragment(fStringPool);
        return matcher;

    /**
     * Ends the value scope for the specified identity constraint.
     *
     * @param identityConstraint The identity constraint.
     */
    public void endValueScopeFor(IdentityConstraint identityConstraint)
        throws Exception {

        ValueStoreBase valueStore = fValueStoreCache.getValueStoreFor(identityConstraint);
        valueStore.endValueScope();



   /** Start entity reference. */
   public void startEntityReference(int entityName, int entityType, int entityContext) throws Exception {
      fDocumentHandler.startEntityReference(entityName, entityType, entityContext);
   }

   /** End entity reference. */
   public void endEntityReference(int entityName, int entityType, int entityContext) throws Exception {
      fDocumentHandler.endEntityReference(entityName, entityType, entityContext);
   }

   /** Send end of input notification. */
   public void sendEndOfInputNotifications(int entityName, boolean moreToFollow) throws Exception {
      fDocumentScanner.endOfInput(entityName, moreToFollow);
      /***
      if (fScanningDTD) {
          fDTDImporter.sendEndOfInputNotifications(entityName, moreToFollow);
      }
      /***/
   }

   /** Send reader change notifications. */
   public void sendReaderChangeNotifications(XMLEntityHandler.EntityReader reader, int readerId) throws Exception {
      fDocumentScanner.readerChange(reader, readerId);
      /***
      if (fScanningDTD) {
          fDTDImporter.sendReaderChangeNotifications(reader, readerId);
      }
      /***/
   }

   /** External entity standalone check. */
   public boolean externalEntityStandaloneCheck() {
      return(fStandaloneReader != -1 && fValidating);
   }

   /** Return true if validating. */
   public boolean getValidating() {
      return fValidating;
   }


   private String normalizeValue (String unNormalizedValue){
       fUnnormalizedStr.setLength(0);
       fUnnormalizedStr.append(unNormalizedValue);
       normalizeWhitespace(fUnnormalizedStr, (fWhiteSpace == DatatypeValidator.COLLAPSE));
       return fNormalizedStr.toString();                                                          
   }

    /**
     * Normalize whitespace in an XMLString according to the rules of attribute
     * value normalization - converting all whitespace characters to space
     * characters.
     * In addition for attributes of type other than CDATA: trim leading and
     * trailing spaces and collapse spaces (0x20 only).
     *
     * @param value The string to normalize.
     * @returns 0 if no triming is done or if there is neither leading nor
     *            trailing whitespace,
     *          1 if there is only leading whitespace,
     *          2 if there is only trailing whitespace,
     *          3 if there is both leading and trailing whitespace.
     */

    private int normalizeWhitespace( StringBuffer chars, boolean collapse) {
        int length = fUnnormalizedStr.length();
        fNormalizedStr.setLength(0);
        boolean skipSpace = collapse;
        boolean sawNonWS = false;
        int leading = 0;
        int trailing = 0;
        int c;
        for (int i = 0; i < length; i++) {
            c = chars.charAt(i);
            if (c == 0x20 || c == 0x0D || c == 0x0A || c == 0x09) {
                if (!skipSpace) {
                    fNormalizedStr.append(' ');
                    skipSpace = collapse;
                }
                if (!sawNonWS) {
                    leading = 1;
                }
            }
            else {
                fNormalizedStr.append((char)c);
                skipSpace = false;
                sawNonWS = true;
            }
        }
        if (skipSpace) {
            c = fNormalizedStr.length();
            if ( c != 0) {
                fNormalizedStr.setLength (--c);
                trailing = 2;
            }
            else if (leading != 0 && !sawNonWS) {
                trailing = 2;
            }
        }
        return collapse ? leading + trailing : 0;
    }

   /** Process characters. Schema Normalization*/
   public void processCharacters(char[] chars, int offset, int length) throws Exception {
       if (DEBUG_NORMALIZATION) {
           System.out.println("==>processCharacters(char[] chars, int offset, int length");
       }
       if (fValidating) {
           if (fInElementContent || fCurrentContentSpecType == XMLElementDecl.TYPE_EMPTY) {
               if (DEBUG_NORMALIZATION) {
                   System.out.println("==>charDataInContent()");
               }
            charDataInContent();
           }
        if (fBufferDatatype) {
            if (fFirstChunk && fGrammar!=null) {
                fGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
                fCurrentDV = fTempElementDecl.datatypeValidator;
                if ( fXsiTypeValidator != null ) {
                   fCurrentDV = fXsiTypeValidator;
                   fXsiTypeValidator = null;
                }
                if (fCurrentDV !=null) {
                    fWhiteSpace = fCurrentDV.getWSFacet();
                }
            }
            if (DEBUG_NORMALIZATION) {
                System.out.println("Start schema datatype normalization <whiteSpace value=" +fWhiteSpace+">");
            }
                fDatatypeBuffer.append(chars, offset, length);
            }
            else {
                fUnnormalizedStr.setLength(0);
                fUnnormalizedStr.append(chars, offset, length);
                int spaces = normalizeWhitespace(fUnnormalizedStr, (fWhiteSpace==DatatypeValidator.COLLAPSE));
                int nLength = fNormalizedStr.length();
                if (nLength > 0) {
                    if (!fFirstChunk && (fWhiteSpace==DatatypeValidator.COLLAPSE) && fTrailing) {
                         fNormalizedStr.insert(0, ' ');
                         nLength++;
                    }
                    if ((length-offset)!=nLength) {
                        char[] newChars = new char[nLength];
                        fNormalizedStr.getChars(0, nLength , newChars, 0);
                        chars = newChars;
                        offset = 0;
                        length = nLength;
                    }
                    else {
                       fNormalizedStr.getChars(0, nLength , chars, 0);
                    }
                    fDatatypeBuffer.append(chars, offset, length);

                    int count = fMatcherStack.getMatcherCount();
                    for (int i = 0; i < count; i++) {
                        XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
                        if (DEBUG_IDENTITY_CONSTRAINTS) {
                            String text = new String(chars, offset, length);
                            System.out.println("<IC>: "+matcher.toString()+"#characters("+text+")");
                        }
                        matcher.characters(chars, offset, length);
                    }

                    fDocumentHandler.characters(chars, offset, length);
                }
                fTrailing = (spaces > 1)?true:false;
                fFirstChunk = false;
                return;
            }
        }
    }

       fFirstChunk = false;

       int count = fMatcherStack.getMatcherCount();
       for (int i = 0; i < count; i++) {
           XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
           if (DEBUG_IDENTITY_CONSTRAINTS) {
               String text = new String(chars, offset, length);
               System.out.println("<IC>: "+matcher.toString()+"#characters("+text+")");
           }
           matcher.characters(chars, offset, length);
       }

       fDocumentHandler.characters(chars, offset, length);
   }

   /** Process characters. */
   public void processCharacters(int data) throws Exception {
      if (fValidating) {
        if (fInElementContent || fCurrentContentSpecType == XMLElementDecl.TYPE_EMPTY) {
            charDataInContent();
        }
        if (fBufferDatatype) {
            fGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
            fCurrentDV = fTempElementDecl.datatypeValidator;
            if ( fXsiTypeValidator != null ) {
               fCurrentDV = fXsiTypeValidator;
               fXsiTypeValidator = null;
            }
            if (fCurrentDV !=null) {
                fWhiteSpace = fCurrentDV.getWSFacet();
            }
                fDatatypeBuffer.append(fStringPool.toString(data));
            }
            else {
                String str =  fStringPool.toString(data);
                int length = str.length();
                fUnnormalizedStr.setLength(0);
                fUnnormalizedStr.append(str);
                int spaces = normalizeWhitespace(fUnnormalizedStr, (fWhiteSpace == DatatypeValidator.COLLAPSE));
                if (fWhiteSpace != DatatypeValidator.PRESERVE) {
                    fStringPool.releaseString(data);
                    data = fStringPool.addString(fNormalizedStr.toString());
                }
                fDatatypeBuffer.append(fNormalizedStr.toString());
            }
        }
      }

      int count = fMatcherStack.getMatcherCount();
      if (count > 0) {
          String text = fStringPool.toString(data);
          char[] chars = new char[text.length()];
          int offset = 0;
          int length = chars.length;
          text.getChars(offset, length, chars, offset);
          for (int i = 0; i < count; i++) {
              XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
              if (DEBUG_IDENTITY_CONSTRAINTS) {
                  System.out.println("<IC>: "+matcher.toString()+"#characters("+text+")");
              }
              matcher.characters(chars, offset, length);
          }
      }

      fDocumentHandler.characters(data);
   }

   /** Process whitespace. */
   public void processWhitespace(char[] chars, int offset, int length)
   throws Exception {

      if (fInElementContent) {
         if (fStandaloneReader != -1 && fValidating && getElementDeclIsExternal(fCurrentElementIndex)) {
            reportRecoverableXMLError(XMLMessages.MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE,
                                      XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION);
         }
         fDocumentHandler.ignorableWhitespace(chars, offset, length);
      } else {
         if (fCurrentContentSpecType == XMLElementDecl.TYPE_EMPTY) {
            charDataInContent();
         }

         int count = fMatcherStack.getMatcherCount();
         for (int i = 0; i < count; i++) {
             XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
             if (DEBUG_IDENTITY_CONSTRAINTS) {
                 String text = new String(chars, offset, length);
                 System.out.println("<IC>: "+matcher.toString()+"#characters("+text+")");
             }
             matcher.characters(chars, offset, length);
         }
         if (fGrammar != null && fValidating) {

             fGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
             fCurrentDV = fTempElementDecl.datatypeValidator;
             if ( fXsiTypeValidator != null ) {
                fCurrentDV = fXsiTypeValidator;
                fXsiTypeValidator = null;
             }
             if (fCurrentDV !=null) {
                fWhiteSpace = fCurrentDV.getWSFacet();
             }


                fDatatypeBuffer.append(chars, offset, length);
            }

         }

         fDocumentHandler.characters(chars, offset, length);
      }


   /** Process whitespace. */
   public void processWhitespace(int data) throws Exception {

      if (fInElementContent) {
         if (fStandaloneReader != -1 && fValidating && getElementDeclIsExternal(fCurrentElementIndex)) {
            reportRecoverableXMLError(XMLMessages.MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE,
                                      XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION);
         }
         fDocumentHandler.ignorableWhitespace(data);
      } else {
         if (fCurrentContentSpecType == XMLElementDecl.TYPE_EMPTY) {
            charDataInContent();
         }

         int count = fMatcherStack.getMatcherCount();
         if (count > 0) {
             String text = fStringPool.toString(data);
             char[] chars = new char[text.length()];
             int offset = 0;
             int length = chars.length;
             text.getChars(length, length, chars, offset);
             for (int i = 0; i < count; i++) {
                 XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
                 if (DEBUG_IDENTITY_CONSTRAINTS) {
                     System.out.println("<IC>: "+matcher.toString()+"#characters("+text+")");
                 }
                 matcher.characters(chars, offset, length);
             }
         }

         if (fGrammar != null && fValidating) {
            fGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
            fCurrentDV = fTempElementDecl.datatypeValidator;
            if ( fXsiTypeValidator != null ) {
               fCurrentDV = fXsiTypeValidator;
               fXsiTypeValidator = null;
            }
            if (fCurrentDV !=null) {
                fWhiteSpace = fCurrentDV.getWSFacet();
            }
                fDatatypeBuffer.append(fStringPool.toString(data));
            }

         }
         fDocumentHandler.characters(data);
      }



   /** Scans element type. */
   public void scanElementType(XMLEntityHandler.EntityReader entityReader,
                               char fastchar, QName element) throws Exception {

      if (!fNamespacesEnabled) {
         element.clear();
         element.localpart = entityReader.scanName(fastchar);
         element.rawname = element.localpart;
      } else {
         entityReader.scanQName(fastchar, element);
         if (entityReader.lookingAtChar(':', false)) {
            fErrorReporter.reportError(fErrorReporter.getLocator(),
                                       XMLMessages.XML_DOMAIN,
                                       XMLMessages.MSG_TWO_COLONS_IN_QNAME,
                                       XMLMessages.P5_INVALID_CHARACTER,
                                       null,
                                       XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
            entityReader.skipPastNmtoken(' ');
         }
      }


   /** Scans expected element type. */
   public boolean scanExpectedElementType(XMLEntityHandler.EntityReader entityReader,
                                          char fastchar, QName element)
   throws Exception {

      if (fCurrentElementCharArrayRange == null) {
         fCurrentElementCharArrayRange = fStringPool.createCharArrayRange();
      }
      fStringPool.getCharArrayRange(fCurrentElement.rawname, fCurrentElementCharArrayRange);
      return entityReader.scanExpectedName(fastchar, fCurrentElementCharArrayRange);


   /** Scans attribute name. */
   public void scanAttributeName(XMLEntityHandler.EntityReader entityReader,
                                 QName element, QName attribute)
   throws Exception {

      if (!fSeenRootElement) {
         fSeenRootElement = true;
         rootElementSpecified(element);
         fStringPool.resetShuffleCount();
      }

      if (!fNamespacesEnabled) {
         attribute.clear();
         attribute.localpart = entityReader.scanName('=');
         attribute.rawname = attribute.localpart;
      } else {
         entityReader.scanQName('=', attribute);
         if (entityReader.lookingAtChar(':', false)) {
            fErrorReporter.reportError(fErrorReporter.getLocator(),
                                       XMLMessages.XML_DOMAIN,
                                       XMLMessages.MSG_TWO_COLONS_IN_QNAME,
                                       XMLMessages.P5_INVALID_CHARACTER,
                                       null,
                                       XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
            entityReader.skipPastNmtoken(' ');
         }
      }


   /** Call start document. */
   public void callStartDocument() throws Exception {

      if (!fCalledStartDocument) {
         fDocumentHandler.startDocument();
         fCalledStartDocument = true;
         if (fValidating) {
             fValueStoreCache.startDocument();
         }
      }
   }

   /** Call end document. */
   public void callEndDocument() throws Exception {

      if (fCalledStartDocument) {
          if (fValidating) {
                if (DEBUG_IDENTITY_CONSTRAINTS) {
                    System.out.println("<IC>: ValueStoreCache#endDocument()");
                }
              fValueStoreCache.endDocument();
          }
         fDocumentHandler.endDocument();
      }
   }

   /** Call XML declaration. */
   public void callXMLDecl(int version, int encoding, int standalone) throws Exception {
      fDocumentHandler.xmlDecl(version, encoding, standalone);
   }
   public void callStandaloneIsYes() throws Exception {
      fStandaloneReader = fEntityHandler.getReaderId() ;

   }



   /** Call text declaration. */
   public void callTextDecl(int version, int encoding) throws Exception {
      fDocumentHandler.textDecl(version, encoding);
   }

   /**
    * Signal the scanning of an element name in a start element tag.
    *
    * @param element Element name scanned.
    */
   public void element(QName element) throws Exception {
      fAttrListHandle = -1;
   }
   /**
    * Signal the scanning of an attribute associated to the previous
    * start element tag.
    *
    * @param element Element name scanned.
    * @param attrName Attribute name scanned.
    * @param attrValue The string pool index of the attribute value.
    */
   public boolean attribute(QName element, QName attrName, int attrValue) throws Exception {
      if (fAttrListHandle == -1) {
         fAttrListHandle = fAttrList.startAttrList();
      }

      return fAttrList.addAttr(attrName, attrValue, fCDATASymbol, true, true) == -1;
   }

   /** Call start element. */
   public void callStartElement(QName element) throws Exception {

      if ( DEBUG_SCHEMA_VALIDATION )
         System.out.println("\n=======StartElement : " + fStringPool.toString(element.localpart));



      if (!fSeenRootElement) {
         rootElementSpecified(element);
         fStringPool.resetShuffleCount();
      }

      if (fGrammar != null && fGrammarIsDTDGrammar) {
         fAttrListHandle = addDTDDefaultAttributes(element, fAttrList, fAttrListHandle, fValidating, fStandaloneReader != -1);
      }

      fCheckedForSchema = true;
      if (fNamespacesEnabled) {
         bindNamespacesToElementAndAttributes(element, fAttrList);
      }

      if (fDynamicValidation && fGrammar==null) {
         fValidating = false;
      }

      if (!fSeenRootElement) {
          fSeenRootElement = true;
      }

      validateElementAndAttributes(element, fAttrList);
      if (fAttrListHandle != -1) {
         int dupAttrs[];
         if ((dupAttrs = fAttrList.endAttrList()) != null) {
            Object[] args = {fStringPool.toString(element.rawname), null};
            for (int i = 0; i < dupAttrs.length; i++) {
                args[1] = fStringPool.toString(dupAttrs[i]);
                fErrorReporter.reportError(fErrorReporter.getLocator(),
                                           XMLMessages.XMLNS_DOMAIN,
                                           XMLMessages.MSG_ATTRIBUTE_NOT_UNIQUE,
                                           XMLMessages.WFC_UNIQUE_ATT_SPEC,
                                           args,
                                           XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
            }
         }
      }

      if (fValidating && fGrammar != null && fGrammarIsSchemaGrammar) {
          if (DEBUG_IDENTITY_CONSTRAINTS) {
              System.out.println("<IC>: pushing context - element: "+fStringPool.toString(element.rawname));
          }
          fValueStoreCache.startElement();
          fMatcherStack.pushContext();
          int eindex = fGrammar.getElementDeclIndex(element, -1);
          if (eindex != -1) {
              fGrammar.getElementDecl(eindex, fTempElementDecl);
              fValueStoreCache.initValueStoresFor(fTempElementDecl);
              int uCount = fTempElementDecl.unique.size();
              for (int i = 0; i < uCount; i++) {
                  activateSelectorFor((IdentityConstraint)fTempElementDecl.unique.elementAt(i));
              }
              int kCount = fTempElementDecl.key.size();
              for (int i = 0; i < kCount; i++) {
                  activateSelectorFor((IdentityConstraint)fTempElementDecl.key.elementAt(i));
              }
              int krCount = fTempElementDecl.keyRef.size();
              for (int i = 0; i < krCount; i++) {
                  activateSelectorFor((IdentityConstraint)fTempElementDecl.keyRef.elementAt(i));
              }
          }

          int count = fMatcherStack.getMatcherCount();
          for (int i = 0; i < count; i++) {
              XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
              if (DEBUG_IDENTITY_CONSTRAINTS) {
                  System.out.println("<IC>: "+matcher.toString()+"#startElement("+fStringPool.toString(element.rawname)+")");
              }
              matcher.startElement(element, fAttrList, fAttrListHandle, fCurrentElementIndex, (SchemaGrammar)fGrammar);
          }
      }

      fDocumentHandler.startElement(element, fAttrList, fAttrListHandle);
      fElementDepth++;
      fAttrListHandle = -1;

      if (fValidating) {
         if (fElementChildrenOffsetStack.length <= fElementDepth) {
            int newarray[] = new int[fElementChildrenOffsetStack.length * 2];
            System.arraycopy(fElementChildrenOffsetStack, 0, newarray, 0, fElementChildrenOffsetStack.length);
            fElementChildrenOffsetStack = newarray;
         }
         fElementChildrenOffsetStack[fElementDepth] = fElementChildrenLength;

         if (fElementChildren.length <= fElementChildrenLength) {
            QName[] newarray = new QName[fElementChildrenLength * 2];
            System.arraycopy(fElementChildren, 0, newarray, 0, fElementChildren.length);
            fElementChildren = newarray;
         }
         QName qname = fElementChildren[fElementChildrenLength];
         if (qname == null) {
            for (int i = fElementChildrenLength; i < fElementChildren.length; i++) {
               fElementChildren[i] = new QName();
            }
            qname = fElementChildren[fElementChildrenLength];
         }
         qname.setValues(element);
         fElementChildrenLength++;

         if (DEBUG_ELEMENT_CHILDREN) {
            printChildren();
            printStack();
         }

      }

      ensureStackCapacity(fElementDepth);
      fCurrentElement.setValues(element);
      fCurrentElementEntity = fEntityHandler.getReaderId();

      fElementQNamePartsStack[fElementDepth].setValues(fCurrentElement);

      fElementEntityStack[fElementDepth] = fCurrentElementEntity;
      fElementIndexStack[fElementDepth] = fCurrentElementIndex;
      fContentSpecTypeStack[fElementDepth] = fCurrentContentSpecType;

      if (fNeedValidationOff) {
         fValidating = false;
         fNeedValidationOff = false;
      }

      if (fValidating && fGrammarIsSchemaGrammar) {
         pushContentLeafStack();
      } else {
        fContentModelStateStack[fElementDepth] = -2;
      }

      fValidationFlagStack[fElementDepth] = fValidating ? 0 : -1;

      fScopeStack[fElementDepth] = fCurrentScope;
      fGrammarNameSpaceIndexStack[fElementDepth] = fGrammarNameSpaceIndex;


    private void activateSelectorFor(IdentityConstraint ic) throws Exception {
        Selector selector = ic.getSelector();
        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: XMLValidator#activateSelectorFor("+selector+')');
        }
        FieldActivator activator = this;
        if(selector == null)
            return;
        XPathMatcher matcher = selector.createMatcher(activator);
        fMatcherStack.addMatcher(matcher);
        if (DEBUG_IDENTITY_CONSTRAINTS) {
            System.out.println("<IC>: "+matcher+"#startDocumentFragment()");
        }
        matcher.startDocumentFragment(fStringPool);
    }

   private void pushContentLeafStack() throws Exception {
      int contentType = getContentSpecType(fCurrentElementIndex);
      if ( contentType == XMLElementDecl.TYPE_CHILDREN ||
           contentType == XMLElementDecl.TYPE_MIXED_COMPLEX) {
         XMLContentModel cm = getElementContentModel(fCurrentElementIndex);
         ContentLeafNameTypeVector cv = cm.getContentLeafNameTypeVector();
         if (cm != null) {
            fContentLeafStack[fElementDepth] = cv;
            fContentModelStack[fElementDepth] = cm;
            if (cm instanceof DFAContentModel && cv != null)
                fContentModelStateStack[fElementDepth] = 0;
            else
                fContentModelStateStack[fElementDepth] = -2;
            fContentModelEleCount[fElementDepth] = 0;
         }
      } else {
        fContentModelStateStack[fElementDepth] = -2;
      }
   }

   private void ensureStackCapacity ( int newElementDepth) {

      if (newElementDepth == fElementQNamePartsStack.length ) {
         int[] newStack = new int[newElementDepth * 2];
         System.arraycopy(fScopeStack, 0, newStack, 0, newElementDepth);
         fScopeStack = newStack;

         newStack = new int[newElementDepth * 2];
         System.arraycopy(fGrammarNameSpaceIndexStack, 0, newStack, 0, newElementDepth);
         fGrammarNameSpaceIndexStack = newStack;

         QName[] newStackOfQueue = new QName[newElementDepth * 2];
         System.arraycopy(this.fElementQNamePartsStack, 0, newStackOfQueue, 0, newElementDepth );
         fElementQNamePartsStack      = newStackOfQueue;

         QName qname = fElementQNamePartsStack[newElementDepth];
         if (qname == null) {
            for (int i = newElementDepth; i < fElementQNamePartsStack.length; i++) {
               fElementQNamePartsStack[i] = new QName();
            }
         }

         newStack = new int[newElementDepth * 2];
         System.arraycopy(fElementEntityStack, 0, newStack, 0, newElementDepth);
         fElementEntityStack = newStack;

         newStack = new int[newElementDepth * 2];
         System.arraycopy(fElementIndexStack, 0, newStack, 0, newElementDepth);
         fElementIndexStack = newStack;

         newStack = new int[newElementDepth * 2];
         System.arraycopy(fContentSpecTypeStack, 0, newStack, 0, newElementDepth);
         fContentSpecTypeStack = newStack;

         newStack = new int[newElementDepth * 2];
         System.arraycopy(fValidationFlagStack, 0, newStack, 0, newElementDepth);
         fValidationFlagStack = newStack;

         ContentLeafNameTypeVector[] newStackV = new ContentLeafNameTypeVector[newElementDepth * 2];
         System.arraycopy(fContentLeafStack, 0, newStackV, 0, newElementDepth);
         fContentLeafStack = newStackV;

         XMLContentModel[] newStackCM = new XMLContentModel[newElementDepth * 2];
         System.arraycopy(fContentModelStack, 0, newStackCM, 0, newElementDepth);
         fContentModelStack = newStackCM;
         newStack = new int[newElementDepth * 2];
         System.arraycopy(fContentModelStateStack, 0, newStack, 0, newElementDepth);
         fContentModelStateStack = newStack;
         newStack = new int[newElementDepth * 2];
         System.arraycopy(fContentModelEleCount, 0, newStack, 0, newElementDepth);
         fContentModelEleCount = newStack;
      }
   }

   /** Call end element. */
   public void callEndElement(int readerId) throws Exception {
      if ( DEBUG_SCHEMA_VALIDATION )
         System.out.println("=======EndElement : " + fStringPool.toString(fCurrentElement.localpart)+"\n");

      int prefixIndex = fCurrentElement.prefix;
      int elementType = fCurrentElement.rawname;

      if (fCurrentElementEntity != readerId) {
         fErrorReporter.reportError(fErrorReporter.getLocator(),
                                    XMLMessages.XML_DOMAIN,
                                    XMLMessages.MSG_ELEMENT_ENTITY_MISMATCH,
                                    XMLMessages.P78_NOT_WELLFORMED,
                                    new Object[] { fStringPool.toString(elementType)},
                                    XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
      }

      fElementDepth--;
      if (fValidating) {
         int elementIndex = fCurrentElementIndex;
         if (elementIndex != -1 && fCurrentContentSpecType != -1) {
            QName children[] = fElementChildren;
            int childrenOffset = fElementChildrenOffsetStack[fElementDepth + 1] + 1;
            int childrenLength = fElementChildrenLength - childrenOffset;
            if (DEBUG_ELEMENT_CHILDREN) {
               System.out.println("endElement("+fStringPool.toString(fCurrentElement.rawname)+')');
               System.out.println("fCurrentContentSpecType : " + fCurrentContentSpecType );
               System.out.print("offset: ");
               System.out.print(childrenOffset);
               System.out.print(", length: ");
               System.out.print(childrenLength);
               System.out.println();
               printChildren();
               printStack();
            }
            int result = checkContent(elementIndex,
                                      children, childrenOffset, childrenLength);
            fCurrentDV = null;

            if ( DEBUG_SCHEMA_VALIDATION )
               System.out.println("!!!!!!!!In XMLValidator, the return value from checkContent : " + result);

            if (result != -1) {
               int majorCode = result != childrenLength ? XMLMessages.MSG_CONTENT_INVALID : XMLMessages.MSG_CONTENT_INCOMPLETE;
               fGrammar.getElementDecl(elementIndex, fTempElementDecl);
               if (fTempElementDecl.type == XMLElementDecl.TYPE_EMPTY) {
                  reportRecoverableXMLError(majorCode,
                                            0,
                                            fStringPool.toString(elementType),
                                            "EMPTY");
               } else
                  reportRecoverableXMLError(majorCode,
                                            0,
                                            fStringPool.toString(elementType),
                                            XMLContentSpec.toString(fGrammar, fStringPool, fTempElementDecl.contentSpecIndex));
            }
         }
         fElementChildrenLength = fElementChildrenOffsetStack[fElementDepth + 1] + 1;

         if(fGrammarIsSchemaGrammar) {
            int oldCount = fMatcherStack.getMatcherCount();
            for (int i = oldCount - 1; i >= 0; i--) {
                XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
                if (DEBUG_IDENTITY_CONSTRAINTS) {
                    System.out.println("<IC>: "+matcher+"#endElement("+fStringPool.toString(fCurrentElement.rawname)+")");
                }
                matcher.endElement(fCurrentElement, fCurrentElementIndex, (SchemaGrammar)fGrammar);
            }
            if (DEBUG_IDENTITY_CONSTRAINTS) {
                System.out.println("<IC>: popping context - element: "+fStringPool.toString(fCurrentElement.rawname));
            }
            if (fMatcherStack.size() > 0) {
                fMatcherStack.popContext();
            }
            int newCount = fMatcherStack.getMatcherCount();
            for (int i = oldCount - 1; i >= newCount; i--) {
                XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
                IdentityConstraint id;
                if((id = matcher.getIDConstraint()) != null  && id.getType() != IdentityConstraint.KEYREF) {
                    if (DEBUG_IDENTITY_CONSTRAINTS) {
                        System.out.println("<IC>: "+matcher+"#endDocumentFragment()");
                    }
                    matcher.endDocumentFragment();
                    fValueStoreCache.transplant(id);
                } else if (id == null)
                    matcher.endDocumentFragment();
            }
            for (int i = oldCount - 1; i >= newCount; i--) {
                XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
                IdentityConstraint id;
                if((id = matcher.getIDConstraint()) != null && id.getType() == IdentityConstraint.KEYREF) {
                    if (DEBUG_IDENTITY_CONSTRAINTS) {
                        System.out.println("<IC>: "+matcher+"#endDocumentFragment()");
                    }
                    ValueStoreBase values = fValueStoreCache.getValueStoreFor(id);
                        values.endDocumentFragment();
                    matcher.endDocumentFragment();
                }
            }
            fValueStoreCache.endElement();
        }
      }
      fDocumentHandler.endElement(fCurrentElement);
      if (fNamespacesEnabled) {
         fNamespacesScope.decreaseDepth();
      }

      if (fElementDepth < -1) {
         throw new RuntimeException("FWK008 Element stack underflow");
      }
      if (fElementDepth < 0) {
         fCurrentElement.clear();
         fCurrentElementEntity = -1;
         fCurrentElementIndex = -1;
         fCurrentContentSpecType = -1;
         fInElementContent = false;
         if (fValidating ) {
            try {
                this.fIdDefs.clear();
                this.fIdREFDefs.clear();
            } catch ( InvalidDatatypeValueException ex ) {
               reportRecoverableXMLError( ex.getMajorCode(), ex.getMinorCode(),
                                          ex.getMessage() );
            }
         }
         return;
      }


      fCurrentElement.prefix = -1;


         fCurrentElement.localpart = fElementQNamePartsStack[fElementDepth].localpart;
         fCurrentElement.localpart = fElementQNamePartsStack[fElementDepth].rawname;
      }
      fCurrentElement.rawname      = fElementQNamePartsStack[fElementDepth].rawname;
      fCurrentElement.uri          = fElementQNamePartsStack[fElementDepth].uri;
      fCurrentElement.prefix       = fElementQNamePartsStack[fElementDepth].prefix;


      fCurrentElementEntity = fElementEntityStack[fElementDepth];
      fCurrentElementIndex = fElementIndexStack[fElementDepth];
      fCurrentContentSpecType = fContentSpecTypeStack[fElementDepth];

      fValidating = fValidationFlagStack[fElementDepth] == 0 ? true : false;

      fCurrentScope = fScopeStack[fElementDepth];

      if ( DEBUG_SCHEMA_VALIDATION ) {

        System.out.println("+++++ currentElement : " + fStringPool.toString(elementType)+
                   "\n fCurrentElementIndex : " + fCurrentElementIndex +
                   "\n fCurrentScope : " + fCurrentScope +
                   "\n fCurrentContentSpecType : " + fCurrentContentSpecType +
                   "\n++++++++++++++++++++++++++++++++++++++++++++++++" );
      }

      if ( fGrammarNameSpaceIndex != fGrammarNameSpaceIndexStack[fElementDepth] ) {

         fGrammarNameSpaceIndex = fGrammarNameSpaceIndexStack[fElementDepth];
         if ( fValidating && fGrammarIsSchemaGrammar )
             if (fGrammarNameSpaceIndex < StringPool.EMPTY_STRING) {
                 fGrammar = null;
                 fGrammarIsSchemaGrammar = false;
                 fGrammarIsDTDGrammar = false;
                } else if (!switchGrammar(fGrammarNameSpaceIndex)) {
                     reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR, XMLMessages.SCHEMA_GENERIC_ERROR,
                                               "Grammar with uri: " + fStringPool.toString(fGrammarNameSpaceIndex)
                                               + " , can not be found; possible mismatch between instance document's namespace and that of schema");
             }
      }

      if (fValidating) {
         fBufferDatatype = false;
      }
      fInElementContent = (fCurrentContentSpecType == XMLElementDecl.TYPE_CHILDREN);


   /** Call start CDATA section. */
   public void callStartCDATA() throws Exception {
      if (fValidating && fInElementContent) {
         charDataInContent();
      }
      fDocumentHandler.startCDATA();
   }

   /** Call end CDATA section. */
   public void callEndCDATA() throws Exception {
      fDocumentHandler.endCDATA();
   }

   /** Call characters. */
   public void callCharacters(int ch) throws Exception {

      if (fCharRefData == null) {
         fCharRefData = new char[2];
      }
      int count = (ch < 0x10000) ? 1 : 2;
      if (count == 1) {
         fCharRefData[0] = (char)ch;
      } else {
         fCharRefData[0] = (char)(((ch-0x00010000)>>10)+0xd800);
         fCharRefData[1] = (char)(((ch-0x00010000)&0x3ff)+0xdc00);
      }
      if (fValidating && (fInElementContent || fCurrentContentSpecType == XMLElementDecl.TYPE_EMPTY)) {
         charDataInContent();
      }
      if (fValidating) {
         if (fBufferDatatype) {
            fDatatypeBuffer.append(fCharRefData,0,1);
         }
      }

      int matcherCount = fMatcherStack.getMatcherCount();
      for (int i = 0; i < matcherCount; i++) {
          XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
          if (DEBUG_IDENTITY_CONSTRAINTS) {
              String text = new String(fCharRefData, 0, count);
              System.out.println("<IC>: "+matcher.toString()+"#characters("+text+")");
          }
          matcher.characters(fCharRefData, 0, count);
      }

      if (fSendCharDataAsCharArray) {
         fDocumentHandler.characters(fCharRefData, 0, count);
      } else {
         int index = fStringPool.addString(new String(fCharRefData, 0, count));
         fDocumentHandler.characters(index);
      }

   /** Call processing instruction. */
   public void callProcessingInstruction(int target, int data) throws Exception {
      fDocumentHandler.processingInstruction(target, data);
   }

   /** Call comment. */
   public void callComment(int comment) throws Exception {
      fDocumentHandler.comment(comment);
   }


   /** Start a new namespace declaration scope. */
   public void startNamespaceDeclScope(int prefix, int uri) throws Exception {
      fDocumentHandler.startNamespaceDeclScope(prefix, uri);
   }

   /** End a namespace declaration scope. */
   public void endNamespaceDeclScope(int prefix) throws Exception {
      fDocumentHandler.endNamespaceDeclScope(prefix);
   }




   /** Sets the root element. */
   public void setRootElementType(QName rootElement) {
      fRootElement.setValues(rootElement);
   }

   /**
    * Returns true if the element declaration is external.
    * <p>
    * <strong>Note:</strong> This method is primarilly useful for
    * DTDs with internal and external subsets.
    */
   private boolean getElementDeclIsExternal(int elementIndex) {
      /*if (elementIndex < 0 || elementIndex >= fElementCount) {
          return false;
      }
      int chunk = elementIndex >> CHUNK_SHIFT;
      int index = elementIndex & CHUNK_MASK;
      return (fElementDeclIsExternal[chunk][index] != 0);
      */

      if (fGrammarIsDTDGrammar ) {
         return((DTDGrammar) fGrammar).getElementDeclIsExternal(elementIndex);
      }
      return false;
   }

   /** Returns the content spec type for an element index. */
   public int getContentSpecType(int elementIndex) {

      int contentSpecType = -1;
      if ( elementIndex > -1) {
         if ( fGrammar.getElementDecl(elementIndex,fTempElementDecl) ) {
            contentSpecType = fTempElementDecl.type;
         }
      }
      return contentSpecType;
   }

   /** Returns the content spec handle for an element index. */
   public int getContentSpecHandle(int elementIndex) {
      int contentSpecHandle = -1;
      if ( elementIndex > -1) {
         if ( fGrammar.getElementDecl(elementIndex,fTempElementDecl) ) {
            contentSpecHandle = fTempElementDecl.contentSpecIndex;
         }
      }
      return contentSpecHandle;
   }



   /** Report a recoverable schema error. */
   private void reportSchemaError(int code, Object[] args) throws Exception {
       fErrorReporter.reportError(fErrorReporter.getLocator(),
                                  SchemaMessageProvider.SCHEMA_DOMAIN,
                                  code,
                                  SchemaMessageProvider.MSG_NONE,
                                  args,
                                  XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);

   /** Report a recoverable xml error. */
   protected void reportRecoverableXMLError(int majorCode, int minorCode)
   throws Exception {

      fErrorReporter.reportError(fErrorReporter.getLocator(),
                                 XMLMessages.XML_DOMAIN,
                                 majorCode,
                                 minorCode,
                                 null,
                                 XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);


   /** Report a recoverable xml error. */
   protected void reportRecoverableXMLError(int majorCode, int minorCode,
                                            int stringIndex1)
   throws Exception {

      Object[] args = { fStringPool.toString(stringIndex1)};
      fErrorReporter.reportError(fErrorReporter.getLocator(),
                                 XMLMessages.XML_DOMAIN,
                                 majorCode,
                                 minorCode,
                                 args,
                                 XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);


   /** Report a recoverable xml error. */
   protected void reportRecoverableXMLError(int majorCode, int minorCode,
                                            String string1) throws Exception {

      Object[] args = { string1};
      fErrorReporter.reportError(fErrorReporter.getLocator(),
                                 XMLMessages.XML_DOMAIN,
                                 majorCode,
                                 minorCode,
                                 args,
                                 XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);


   /** Report a recoverable xml error. */
   protected void reportRecoverableXMLError(int majorCode, int minorCode,
                                            int stringIndex1, int stringIndex2)
   throws Exception {

      Object[] args = { fStringPool.toString(stringIndex1), fStringPool.toString(stringIndex2)};
      fErrorReporter.reportError(fErrorReporter.getLocator(),
                                 XMLMessages.XML_DOMAIN,
                                 majorCode,
                                 minorCode,
                                 args,
                                 XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);


   /** Report a recoverable xml error. */
   protected void reportRecoverableXMLError(int majorCode, int minorCode,
                                            String string1, String string2)
   throws Exception {

      Object[] args = { string1, string2};
      fErrorReporter.reportError(fErrorReporter.getLocator(),
                                 XMLMessages.XML_DOMAIN,
                                 majorCode,
                                 minorCode,
                                 args,
                                 XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);


   /** Report a recoverable xml error. */
   protected void reportRecoverableXMLError(int majorCode, int minorCode,
                                            String string1, String string2,
                                            String string3) throws Exception {

      Object[] args = { string1, string2, string3};
      fErrorReporter.reportError(fErrorReporter.getLocator(),
                                 XMLMessages.XML_DOMAIN,
                                 majorCode,
                                 minorCode,
                                 args,
                                 XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);



   /**
    * Returns information about which elements can be placed at a particular point
    * in the passed element's content model.
    * <p>
    * Note that the incoming content model to test must be valid at least up to
    * the insertion point. If not, then -1 will be returned and the info object
    * will not have been filled in.
    * <p>
    * If, on return, the info.isValidEOC flag is set, then the 'insert after'
    * elemement is a valid end of content, i.e. nothing needs to be inserted
    * after it to make the parent element's content model valid.
    *
    * @param elementIndex The index within the <code>ElementDeclPool</code> of the
    *                     element which is being querying.
    * @param fullyValid Only return elements that can be inserted and still
    *                   maintain the validity of subsequent elements past the
    *                   insertion point (if any).  If the insertion point is at
    *                   the end, and this is true, then only elements that can
    *                   be legal final states will be returned.
    * @param info An object that contains the required input data for the method,
    *             and which will contain the output information if successful.
    *
    * @return The value -1 if fully valid, else the 0 based index of the child
    *         that first failed before the insertion point. If the value
    *         returned is equal to the number of children, then the specified
    *         children are valid but additional content is required to reach a
    *         valid ending state.
    *
    * @exception Exception Thrown on error.
    *
    * @see InsertableElementsInfo
    */
   protected int whatCanGoHere(int elementIndex, boolean fullyValid,
                               InsertableElementsInfo info) throws Exception {

      if (info.insertAt > info.childCount || info.curChildren == null ||
          info.childCount < 1 || info.childCount > info.curChildren.length) {
         fErrorReporter.reportError(fErrorReporter.getLocator(),
                                    ImplementationMessages.XERCES_IMPLEMENTATION_DOMAIN,
                                    ImplementationMessages.VAL_WCGHI,
                                    0,
                                    null,
                                    XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
      }

      int retVal = 0;
      try {
         final XMLContentModel cmElem = getElementContentModel(elementIndex);

         retVal = cmElem.whatCanGoHere(fullyValid, info);
      } catch (CMException excToCatch) {
         int majorCode = excToCatch.getErrorCode();
         fErrorReporter.reportError(fErrorReporter.getLocator(),
                                    ImplementationMessages.XERCES_IMPLEMENTATION_DOMAIN,
                                    majorCode,
                                    0,
                                    null,
                                    XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
         throw excToCatch;
      }
      return retVal;



   /** Protected for use by AttributeValidator classes. */
   protected boolean getAttDefIsExternal(QName element, QName attribute) {
      int attDefIndex = getAttDef(element, attribute);
      if (fGrammarIsDTDGrammar ) {
         return((DTDGrammar) fGrammar).getAttributeDeclIsExternal(attDefIndex);
      }
      return false;
   }





   /** Returns true if using a standalone reader. */
   private boolean usingStandaloneReader() {
      return fStandaloneReader == -1 || fEntityHandler.getReaderId() == fStandaloneReader;
   }

   /** Returns a locator implementation. */
   private LocatorImpl getLocatorImpl(LocatorImpl fillin) {

      Locator here = fErrorReporter.getLocator();
      if (fillin == null)
         return new LocatorImpl(here);
      fillin.setPublicId(here.getPublicId());
      fillin.setSystemId(here.getSystemId());
      fillin.setLineNumber(here.getLineNumber());
      fillin.setColumnNumber(here.getColumnNumber());
      return fillin;




   /** Reset pool. */
   private void poolReset() {
            this.fIdDefs.clear();
            this.fIdREFDefs.clear();
        }

   /** Reset common. */
   private void resetCommon(StringPool stringPool) throws Exception {

      fStringPool = stringPool;
      fValidateEntity.setDatatypeObject(new Object[]{fEntityHandler, stringPool});
      fValidating = fValidationEnabled;
      fValidationEnabledByDynamic = false;
      fDynamicDisabledByValidation = false;
      fNormalizeContents = false;
      poolReset();
      fCalledStartDocument = false;
      fStandaloneReader = -1;
      fElementChildrenLength = 0;
      fElementDepth = -1;
      fSeenRootElement = false;
      fSeenDoctypeDecl = false;
      fNamespacesScope = null;
      fNamespacesPrefix = -1;
      fRootElement.clear();
      fAttrListHandle = -1;
      fCheckedForSchema = false;

      fCurrentScope = TOP_LEVEL_SCOPE;
      fCurrentSchemaURI = StringPool.EMPTY_STRING;
      fEmptyURI = StringPool.EMPTY_STRING;
      fXsiPrefix = - 1;
      fXsiTypeValidator = null;

      fNil = false;

      fGrammar = null;
      fGrammarNameSpaceIndex = StringPool.EMPTY_STRING;

      fSGComparator = null;
      fGrammarIsDTDGrammar = false;
      fGrammarIsSchemaGrammar = false;

      fCurrentDV = null;
      fFirstChunk = true;
      fTrailing = false;
      fWhiteSpace = DatatypeValidator.COLLAPSE;

      fMatcherStack.clear();

      UPACheckedGrammarURIs.clear();
      init();


   /** Initialize. */
   private void init() {

      fEmptyURI = fStringPool.addSymbol("");
      fXsiURI = fStringPool.addSymbol(SchemaSymbols.URI_XSI);

      fEMPTYSymbol = fStringPool.addSymbol("EMPTY");
      fANYSymbol = fStringPool.addSymbol("ANY");
      fMIXEDSymbol = fStringPool.addSymbol("MIXED");
      fCHILDRENSymbol = fStringPool.addSymbol("CHILDREN");

      fCDATASymbol = fStringPool.addSymbol("CDATA");
      fIDSymbol = fStringPool.addSymbol("ID");
      fIDREFSymbol = fStringPool.addSymbol("IDREF");
      fIDREFSSymbol = fStringPool.addSymbol("IDREFS");
      fENTITYSymbol = fStringPool.addSymbol("ENTITY");
      fENTITIESSymbol = fStringPool.addSymbol("ENTITIES");
      fNMTOKENSymbol = fStringPool.addSymbol("NMTOKEN");
      fNMTOKENSSymbol = fStringPool.addSymbol("NMTOKENS");
      fNOTATIONSymbol = fStringPool.addSymbol("NOTATION");
      fENUMERATIONSymbol = fStringPool.addSymbol("ENUMERATION");
      fREQUIREDSymbol = fStringPool.addSymbol("#REQUIRED");
      fFIXEDSymbol = fStringPool.addSymbol("#FIXED");
      fDATATYPESymbol = fStringPool.addSymbol("<<datatype>>");
      fEpsilonIndex = fStringPool.addSymbol("<<CMNODE_EPSILON>>");
      fXMLLang = fStringPool.addSymbol("xml:lang");


    /**
    * This method should only be invoked when validation
    * is turn on.
    * fDataTypeReg object of type DatatypeValidatorFactoryImpl
    * needs to be initialized.
    * In the XMLValidator the table will be by default
    * first initialized to 9 validators used by DTD
    * validation.
    * These Validators are known.
    * Later on if we ever find a Schema and need to do
    * Schema validation then we will expand this
    * registry table of fDataTypeReg.
    */
   private void initDataTypeValidators() {

       if ( fGrammarResolver != null ) {
           fDataTypeReg = (DatatypeValidatorFactoryImpl) fGrammarResolver.getDatatypeRegistry();
           fDataTypeReg.initializeDTDRegistry();
        }
       if ( fDataTypeReg != null ) {
            fValID       = fDataTypeReg.getDatatypeValidator("ID" );
            fValIDRef    = fDataTypeReg.getDatatypeValidator("IDREF" );
            fValIDRefs   = fDataTypeReg.getDatatypeValidator("IDREFS" );
            fValENTITY   = fDataTypeReg.getDatatypeValidator("ENTITY" );
            fValENTITIES = fDataTypeReg.getDatatypeValidator("ENTITIES" );
            fValNMTOKEN  = fDataTypeReg.getDatatypeValidator("NMTOKEN");
            fValNMTOKENS = fDataTypeReg.getDatatypeValidator("NMTOKENS");
            fValNOTATION = fDataTypeReg.getDatatypeValidator("NOTATION" );
       }
   }



   /** addDefaultAttributes. */
   private int addDefaultAttributes(int elementIndex, XMLAttrList attrList, int attrIndex, boolean validationEnabled, boolean standalone) throws Exception {


      fGrammar.getElementDecl(elementIndex,fTempElementDecl);

      int elementNameIndex = fTempElementDecl.name.localpart;
      int attlistIndex = fGrammar.getFirstAttributeDeclIndex(elementIndex);
      int firstCheck = attrIndex;
      int lastCheck = -1;
      while (attlistIndex != -1) {
         fGrammar.getAttributeDecl(attlistIndex, fTempAttDecl);


         int attPrefix = fTempAttDecl.name.prefix;
         int attName = fTempAttDecl.name.localpart;
         int attType = attributeTypeName(fTempAttDecl);
         int attDefType =fTempAttDecl.defaultType;
         int attValue = -1 ;
         if (fTempAttDecl.defaultValue != null ) {
            attValue = fStringPool.addSymbol(fTempAttDecl.defaultValue);
         }

         boolean specified = false;
         boolean required = (attDefType & XMLAttributeDecl.DEFAULT_TYPE_REQUIRED) > 0;
         boolean prohibited = (attDefType & XMLAttributeDecl.DEFAULT_TYPE_PROHIBITED) > 0;
         boolean fixed = (attDefType & XMLAttributeDecl.DEFAULT_TYPE_FIXED) > 0;

         if (firstCheck != -1) {
            boolean cdata = attType == fCDATASymbol;
            if (!cdata || required || prohibited || attValue != -1) {
               int i = attrList.getFirstAttr(firstCheck);
               while (i != -1 && (lastCheck == -1 || i <= lastCheck)) {

                  if ( (fGrammarIsDTDGrammar && (attrList.getAttrName(i) == fTempAttDecl.name.rawname)) ||
                       (  fStringPool.equalNames(attrList.getAttrLocalpart(i), attName)
                          && fStringPool.equalNames(attrList.getAttrURI(i), fTempAttDecl.name.uri) ) ) {

                    if (prohibited && validationEnabled) {
                        Object[] args = { fStringPool.toString(elementNameIndex),
                            fStringPool.toString(attName)};
                        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                SchemaMessageProvider.SCHEMA_DOMAIN,
                                SchemaMessageProvider.ProhibitedAttributePresent,
                                SchemaMessageProvider.MSG_NONE,
                                args,
                                XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                    }
                     specified = true;
                     break;
                  }
                  i = attrList.getNextAttr(i);
               }
            }
         }

         if (!specified) {
            if (required) {
               if (validationEnabled) {
                  Object[] args = { fStringPool.toString(elementNameIndex),
                     fStringPool.toString(attName)};
                  fErrorReporter.reportError(fErrorReporter.getLocator(),
                                             XMLMessages.XML_DOMAIN,
                                             XMLMessages.MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED,
                                             XMLMessages.VC_REQUIRED_ATTRIBUTE,
                                             args,
                                             XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
               }
            } else if (attValue != -1) {
               if (validationEnabled && standalone ) {
                  if ( fGrammarIsDTDGrammar
                       && ((DTDGrammar) fGrammar).getAttributeDeclIsExternal(attlistIndex) ) {

                     Object[] args = { fStringPool.toString(elementNameIndex),
                        fStringPool.toString(attName)};
                     fErrorReporter.reportError(fErrorReporter.getLocator(),
                                                XMLMessages.XML_DOMAIN,
                                                XMLMessages.MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED,
                                                XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                                args,
                                                XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                  }
               }
               if (validationEnabled) {
                   validateUsingDV (fTempAttDecl.datatypeValidator,
                                    fStringPool.toString(attValue), true);
               }
               if (attrIndex == -1) {
                  attrIndex = attrList.startAttrList();
               }
               fTempQName.setValues(attPrefix, attName, attName, fTempAttDecl.name.uri);
               int newAttr = attrList.addAttr(fTempQName,
                                              attValue, attType,
                                              false, false);
               if (lastCheck == -1) {
                  lastCheck = newAttr;
               }
            }
         }
         attlistIndex = fGrammar.getNextAttributeDeclIndex(attlistIndex);
      }
      return attrIndex;


   /** addDTDDefaultAttributes. */
   private int addDTDDefaultAttributes(QName element, XMLAttrList attrList, int attrIndex, boolean validationEnabled, boolean standalone) throws Exception {



      int elementIndex = fGrammar.getElementDeclIndex(element, -1);

      if (elementIndex == -1) {
         return attrIndex;
      }

      fGrammar.getElementDecl(elementIndex,fTempElementDecl);


      int elementNameIndex = fTempElementDecl.name.rawname;
      int attlistIndex = fGrammar.getFirstAttributeDeclIndex(elementIndex);
      int firstCheck = attrIndex;
      int lastCheck = -1;
      while (attlistIndex != -1) {

         fGrammar.getAttributeDecl(attlistIndex, fTempAttDecl);

         /***
         if (fTempAttDecl != null) {
             XMLElementDecl element = new XMLElementDecl();
             fGrammar.getElementDecl(elementIndex, element);
             System.out.println("element: "+fStringPool.toString(element.name.localpart));
             System.out.println("attlistIndex " + attlistIndex + "\n"+
                 "attName : '"+fStringPool.toString(fTempAttDecl.name.localpart) + "'\n"
                                + "attType : "+fTempAttDecl.type + "\n"
                                + "attDefaultType : "+fTempAttDecl.defaultType + "\n"
                                + "attDefaultValue : '"+fTempAttDecl.defaultValue + "'\n"
                                + attrList.getLength() +"\n"
                                );
         }
         /***/

         int attPrefix = fTempAttDecl.name.prefix;
         int attName = fTempAttDecl.name.rawname;
         int attLocalpart = fTempAttDecl.name.localpart;
         int attType = attributeTypeName(fTempAttDecl);
         int attDefType =fTempAttDecl.defaultType;
         int attValue = -1 ;
         if (fTempAttDecl.defaultValue != null ) {
            attValue = fStringPool.addSymbol(fTempAttDecl.defaultValue);
         }
         boolean specified = false;
         boolean required = (attDefType & XMLAttributeDecl.DEFAULT_TYPE_REQUIRED)>0;


         /****
         if (fValidating && fGrammar != null && fGrammarIsDTDGrammar && attValue != -1) {
             normalizeAttValue(null, fTempAttDecl.name,
                               attValue,attType,fTempAttDecl.list,
                               fTempAttDecl.enumeration);
         }
         /****/

         if (firstCheck != -1) {
            boolean cdata = attType == fCDATASymbol;
            if (!cdata || required || attValue != -1) {
               int i = attrList.getFirstAttr(firstCheck);
               while (i != -1 && (lastCheck == -1 || i <= lastCheck)) {

                  if ( attrList.getAttrName(i) == fTempAttDecl.name.rawname ) {

                     if (validationEnabled && (attDefType & XMLAttributeDecl.DEFAULT_TYPE_FIXED) > 0) {
                        int alistValue = attrList.getAttValue(i);
                        if (alistValue != attValue &&
                            !fStringPool.toString(alistValue).equals(fStringPool.toString(attValue))) {
                           Object[] args = { fStringPool.toString(elementNameIndex),
                              fStringPool.toString(attName),
                              fStringPool.toString(alistValue),
                              fStringPool.toString(attValue)};
                           fErrorReporter.reportError(fErrorReporter.getLocator(),
                                                      XMLMessages.XML_DOMAIN,
                                                      XMLMessages.MSG_FIXED_ATTVALUE_INVALID,
                                                      XMLMessages.VC_FIXED_ATTRIBUTE_DEFAULT,
                                                      args,
                                                      XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                        }
                     }
                     specified = true;
                     break;
                  }
                  i = attrList.getNextAttr(i);
               }
            }
         }

         if (!specified) {
            if (required) {
               if (validationEnabled) {
                  Object[] args = { fStringPool.toString(elementNameIndex),
                     fStringPool.toString(attName)};
                  fErrorReporter.reportError(fErrorReporter.getLocator(),
                                             XMLMessages.XML_DOMAIN,
                                             XMLMessages.MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED,
                                             XMLMessages.VC_REQUIRED_ATTRIBUTE,
                                             args,
                                             XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
               }
            } else if (attValue != -1) {
               if (validationEnabled && standalone ){
                  if ( fGrammarIsDTDGrammar
                       && ((DTDGrammar) fGrammar).getAttributeDeclIsExternal(attlistIndex) ) {

                     Object[] args = { fStringPool.toString(elementNameIndex),
                        fStringPool.toString(attName)};
                     fErrorReporter.reportError(fErrorReporter.getLocator(),
                                                XMLMessages.XML_DOMAIN,
                                                XMLMessages.MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED,
                                                XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                                args,
                                                XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                  }
               }
               if (validationEnabled) {
                    validateUsingDV (fTempAttDecl.datatypeValidator,
                                     fStringPool.toString(attValue), true);
               }
               if (attrIndex == -1) {
                  attrIndex = attrList.startAttrList();
               }

               fTempQName.setValues(attPrefix, attLocalpart, attName, fTempAttDecl.name.uri);
               int newAttr = attrList.addAttr(fTempQName,
                                              attValue, attType,
                                              false, false);
               if (lastCheck == -1) {
                  lastCheck = newAttr;
               }
            }
         }
         attlistIndex = fGrammar.getNextAttributeDeclIndex(attlistIndex);
      }
      return attrIndex;



   /** Queries the content model for the specified element index. */
   private XMLContentModel getElementContentModel(int elementIndex) throws Exception {
      XMLContentModel contentModel = null;
      if ( elementIndex > -1) {
         if ( fGrammar.getElementDecl(elementIndex,fTempElementDecl) ) {
             if (fSGComparator == null) {
                 fSGComparator = new SubstitutionGroupComparator(fGrammarResolver, fStringPool, fErrorReporter);
             }
            contentModel = fGrammar.getElementContentModel(elementIndex, fSGComparator);
         }
      }
      return contentModel;
   }




   /** Returns an attribute definition for an element type. */
   private int getAttDef(QName element, QName attribute) {
      if (fGrammar != null) {
         int scope = fCurrentScope;
         if (element.uri > -1) {
            scope = TOP_LEVEL_SCOPE;
         }
         int elementIndex = fGrammar.getElementDeclIndex(element,scope);
         if (elementIndex == -1) {
            return -1;
         }
         int attDefIndex = fGrammar.getFirstAttributeDeclIndex(elementIndex);
         while (attDefIndex != -1) {
            fGrammar.getAttributeDecl(attDefIndex, fTempAttributeDecl);
            if (fTempAttributeDecl.name.localpart == attribute.localpart &&
                fTempAttributeDecl.name.uri == attribute.uri ) {
               return attDefIndex;
            }
            attDefIndex = fGrammar.getNextAttributeDeclIndex(attDefIndex);
         }
      }
      return -1;


   /** Returns an attribute definition for an element type. */
   private int getAttDefByElementIndex(int elementIndex, QName attribute) {
      if (fGrammar != null && elementIndex > -1) {
         if (elementIndex == -1) {
            return -1;
         }
         int attDefIndex = fGrammar.getFirstAttributeDeclIndex(elementIndex);
         while (attDefIndex != -1) {
            fGrammar.getAttributeDecl(attDefIndex, fTempAttDecl);

            if (fGrammarIsDTDGrammar) {
               if (fTempAttDecl.name.rawname == attribute.rawname )
                  return attDefIndex;
            } else
               if (fTempAttDecl.name.localpart == attribute.localpart &&
                   fTempAttDecl.name.uri == attribute.uri ) {
               return attDefIndex;
            }

            if (fGrammarIsSchemaGrammar) {
               if (fTempAttDecl.type == XMLAttributeDecl.TYPE_ANY_ANY) {
                  return attDefIndex;
               } else if (fTempAttDecl.type == XMLAttributeDecl.TYPE_ANY_OTHER) {
                  if (attribute.uri != fTempAttDecl.name.uri) {
                     return attDefIndex;
                  }
               } else if (fTempAttDecl.type == XMLAttributeDecl.TYPE_ANY_LIST) {
                  if (fStringPool.stringInList(fTempAttDecl.enumeration, attribute.uri)) {
                     return attDefIndex;
                  }
               }
            }

            attDefIndex = fGrammar.getNextAttributeDeclIndex(attDefIndex);
         }
      }
      return -1;



   /** Root element specified. */
   private void rootElementSpecified(QName rootElement) throws Exception {

      if ( fLoadDTDGrammar )
         if (fGrammar == null) {

            fGrammar = fGrammarResolver.getGrammar("");

            if (fGrammar != null) {
               if (fGrammar instanceof DTDGrammar) {
                  fGrammarIsDTDGrammar = true;
                  fGrammarIsSchemaGrammar = false;
               } else if ( fGrammar instanceof SchemaGrammar ) {
                  fGrammarIsSchemaGrammar = true;
                  fGrammarIsDTDGrammar = false;
               }

               fGrammarNameSpaceIndex = fEmptyURI;
            }
         }

      if (fValidating) {
         if ( fGrammarIsDTDGrammar &&
              ((DTDGrammar) fGrammar).getRootElementQName(fRootElement) ) {

            String root1 = fStringPool.toString(fRootElement.rawname);
            String root2 = fStringPool.toString(rootElement.rawname);
            if (!root1.equals(root2)) {
               reportRecoverableXMLError(XMLMessages.MSG_ROOT_ELEMENT_TYPE,
                                         XMLMessages.VC_ROOT_ELEMENT_TYPE,
                                         fRootElement.rawname,
                                         rootElement.rawname);
            }
         }
      }

      if (fNamespacesEnabled) {
         if (fNamespacesScope == null) {
            fNamespacesScope = new NamespacesScope(this);
            fNamespacesPrefix = fStringPool.addSymbol("xmlns");
            fNamespacesScope.setNamespaceForPrefix(fNamespacesPrefix, -1);
            int xmlSymbol = fStringPool.addSymbol("xml");
            fNamespacesScope.setNamespaceForPrefix(xmlSymbol, xmlNamespace);
         }
      }


   /** Switchs to correct validating symbol tables when Schema changes.*/

   private boolean switchGrammar(int newGrammarNameSpaceIndex) throws Exception {
      Grammar tempGrammar = fGrammarResolver.getGrammar(fStringPool.toString(newGrammarNameSpaceIndex));
      if (tempGrammar == null) {
         tempGrammar = fGrammarResolver.getGrammar("");
      }
      if (tempGrammar == null) {
         return false;
      } else {
         fGrammar = tempGrammar;
         if (fGrammar instanceof DTDGrammar) {
            fGrammarIsDTDGrammar = true;
            fGrammarIsSchemaGrammar = false;
         } else if ( fGrammar instanceof SchemaGrammar ) {
            fGrammarIsSchemaGrammar = true;
            fGrammarIsDTDGrammar = false;
         }

         return true;
      }
   }

   /** Binds namespaces to the element and attributes. */
   private void bindNamespacesToElementAndAttributes(QName element,
                                                     XMLAttrList attrList)
   throws Exception {

      fNamespacesScope.increaseDepth();


      if (fAttrListHandle != -1 || !fSeenRootElement) {
         int index = attrList.getFirstAttr(fAttrListHandle);
         while (index != -1) {
            int attName = attrList.getAttrName(index);
            int attPrefix = attrList.getAttrPrefix(index);
            if (fStringPool.equalNames(attName, fXMLLang)) {
            } else if (fStringPool.equalNames(attName, fNamespacesPrefix)) {
               int uri = fStringPool.addSymbol(attrList.getAttValue(index));
               fNamespacesScope.setNamespaceForPrefix(StringPool.EMPTY_STRING, uri);
            } else {
               if (attPrefix == fNamespacesPrefix) {
                  int nsPrefix = attrList.getAttrLocalpart(index);
                  int uri = fStringPool.addSymbol(attrList.getAttValue(index));
                  if(uri == StringPool.EMPTY_STRING) {
 		 		      Object[] args = { fStringPool.toString(nsPrefix) };
 		 		      fErrorReporter.reportError(fErrorReporter.getLocator(),
 		 		 		 		 		 		 XMLMessages.XMLNS_DOMAIN,
 		 		 		 		 		 		 XMLMessages.MSG_NAMESPACE_NAME_EMPTY,
 		 		 		 		 		 		 XMLMessages.NC_NAMESPACE_NAME_EMPTY,
 		 		 		 		 		 		 args, XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
 		 		   }
                  fNamespacesScope.setNamespaceForPrefix(nsPrefix, uri);

                  if (fValidating && fSchemaValidation) {
                     boolean seeXsi = false;
                     String attrValue = fStringPool.toString(attrList.getAttValue(index));

                     if (attrValue.equals(SchemaSymbols.URI_XSI)) {
                        fXsiPrefix = nsPrefix;
                        seeXsi = true;
                     }
                  }
               }
            }
            index = attrList.getNextAttr(index);
         }

         String location = null;
         String uri = null;

         if (fValidating && fSchemaValidation) {

             fLocationUriPairs.clear();
             if (!fSeenRootElement) {
                 if (fExternalSchemas != null && fExternalSchemas.length()!=0) {

                     parseSchemaLocation(fExternalSchemas);
                 }
                 if (fExternalNoNamespaceSchema!=null && fExternalNoNamespaceSchema.length() !=0 ) {

                     fLocationUriPairs.put(fExternalNoNamespaceSchema, "");

                     if (fNamespacesScope != null) {
                      fNamespacesScope.setNamespaceForPrefix( fStringPool.addSymbol(""),
                                                              fStringPool.addSymbol(""));
                   }
                 }
                 parseSchemas();
                 fLocationUriPairs.clear();
             }

            fXsiTypeAttValue = -1;
            fNil = false;
            index = attrList.getFirstAttr(fAttrListHandle);
            int attName;
            int attPrefix;
            while (index != -1) {

               attName = attrList.getAttrName(index);
               attPrefix = attrList.getAttrPrefix(index);

               if (fStringPool.equalNames(attName, fNamespacesPrefix)) {
               } else {
                  if ( DEBUG_SCHEMA_VALIDATION ) {
                     System.out.println("deal with XSI");
                     System.out.println("before find XSI: "+fStringPool.toString(attPrefix)
                                        +","+fStringPool.toString(fXsiPrefix) );
                  }
                  if ( fXsiPrefix != -1 && attPrefix == fXsiPrefix ) {

                     if (DEBUG_SCHEMA_VALIDATION) {
                        System.out.println("find XSI: "+fStringPool.toString(attPrefix)
                                           +","+fStringPool.toString(attName) );
                     }

                     int localpart = attrList.getAttrLocalpart(index);
                     if (localpart == fStringPool.addSymbol(SchemaSymbols.XSI_SCHEMALOCACTION)) {
                        parseSchemaLocation(fStringPool.toString(attrList.getAttValue(index)));
                     } else if (localpart == fStringPool.addSymbol(SchemaSymbols.XSI_NONAMESPACESCHEMALOCACTION)) {
                        fLocationUriPairs.put(fStringPool.toString(attrList.getAttValue(index)), "");

                        /***/
                        if (fNamespacesScope != null) {
                           fNamespacesScope.setNamespaceForPrefix( fStringPool.addSymbol(""),
                                                                   fStringPool.addSymbol(""));
                        }
                        /***/
                        }
                        else if ( localpart == fStringPool.addSymbol(SchemaSymbols.XSI_TYPE) ) {
                            fXsiTypeAttValue = attrList.getAttValue(index);
                        }
                        else if ( localpart == fStringPool.addSymbol(SchemaSymbols.ATT_NIL) ) {
                            fNil = (fStringPool.toString(attrList.getAttValue(index)).equals("true")) ? true: false;

                        }
                  }
               }
               index = attrList.getNextAttr(index);
            }
            parseSchemas ();
         }

      }

      int prefix = element.prefix != -1 ? element.prefix : 0;
      int uri    = fNamespacesScope.getNamespaceForPrefix(prefix);
      if (element.prefix != -1 || uri != StringPool.EMPTY_STRING) {
         element.uri = uri;
         if (element.uri == StringPool.EMPTY_STRING) {
            Object[] args = { fStringPool.toString(element.prefix)};
            fErrorReporter.reportError(fErrorReporter.getLocator(),
                                       XMLMessages.XMLNS_DOMAIN,
                                       XMLMessages.MSG_PREFIX_DECLARED,
                                       XMLMessages.NC_PREFIX_DECLARED,
                                       args,
                                       XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
         }
      }


      if (fAttrListHandle != -1) {
         int index = attrList.getFirstAttr(fAttrListHandle);
         while (index != -1) {
            int attName = attrList.getAttrName(index);
            if (!fStringPool.equalNames(attName, fNamespacesPrefix)) {
               int attPrefix = attrList.getAttrPrefix(index);
               if (attPrefix != fNamespacesPrefix) {
                  if (attPrefix != -1 ) {
                     int attrUri = fNamespacesScope.getNamespaceForPrefix(attPrefix);
                     if (attrUri == -1) {
                        Object[] args = { fStringPool.toString(attPrefix)};
                        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                                   XMLMessages.XMLNS_DOMAIN,
                                                   XMLMessages.MSG_PREFIX_DECLARED,
                                                   XMLMessages.NC_PREFIX_DECLARED,
                                                   args,
                                                   XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                     }
                     attrList.setAttrURI(index, attrUri);
                  }
               }
            }
            index = attrList.getNextAttr(index);
         }
      }


   private void parseSchemas () throws Exception{

       Enumeration locations = fLocationUriPairs.keys();
       String location = null;
       String uri = null;
       while (locations.hasMoreElements()) {
           location = (String) locations.nextElement();
           uri = (String) fLocationUriPairs.get(location);
           resolveSchemaGrammar( location, uri);
       }
   }

   private void parseSchemaLocation(String schemaLocationStr) throws Exception{
         StringTokenizer tokenizer = new StringTokenizer(schemaLocationStr, " \n\t\r", false);
         int tokenTotal = tokenizer.countTokens();
         if (tokenTotal % 2 != 0 ) {
             fErrorReporter.reportError(fErrorReporter.getLocator(),
                     SchemaMessageProvider.SCHEMA_DOMAIN,
                     SchemaMessageProvider.SchemaLocation,
                     SchemaMessageProvider.MSG_NONE,
                     new Object[]{schemaLocationStr},
                     XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);

         } else {
             String uri = null;
             String location = null;
             while (tokenizer.hasMoreTokens()) {
               uri = tokenizer.nextToken();
               location = tokenizer.nextToken();
               fLocationUriPairs.put(location, uri);
            }
         }



   private void resolveSchemaGrammar( String loc, String uri) throws Exception {
       Grammar grammar = null;
       if (uri!=null) {
           if (fGrammarIsDTDGrammar && uri.length()==0) {
               fErrorReporter.reportError(fErrorReporter.getLocator(),
                                          XMLMessages.XML_DOMAIN,
                                          XMLMessages.MSG_DTD_SCHEMA_ERROR,
                                          XMLMessages.MSG_DTD_SCHEMA_ERROR,
                                          null,
                                          fErrorReporter.ERRORTYPE_WARNING);
           }  else {
                grammar = fGrammarResolver.getGrammar(uri);
           }
       }

       if (grammar == null) {

          if (fSchemaGrammarParser == null) {
              fSchemaGrammarParser = new DOMParser();
              fSchemaGrammarParser.setEntityResolver( new Resolver(fEntityHandler) );
              fSchemaGrammarParser.setErrorHandler(  new ErrorHandler() );

              try {
              } catch (  org.xml.sax.SAXNotRecognizedException e ) {
                e.printStackTrace();
              } catch ( org.xml.sax.SAXNotSupportedException e ) {
                e.printStackTrace();
              }
          }
         InputSource source = null;
         EntityResolver currentER = fSchemaGrammarParser.getEntityResolver();
         if (currentER != null) {
            source = currentER.resolveEntity("", loc);
         }
         if (source == null) {
            loc = fEntityHandler.expandSystemId(loc);
            source = new InputSource(loc);
         }

         try {
            fSchemaGrammarParser.parse( source );
         } catch ( IOException e ) {
            e.printStackTrace();
         } catch ( SAXException e ) {
            reportRecoverableXMLError( XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                       XMLMessages.SCHEMA_GENERIC_ERROR, e.getMessage() );
         }


         TraverseSchema tst = null;
         if (DEBUG_SCHEMA_VALIDATION) {
            System.out.println("I am geting the Schema Document");
         }

         Element root   = null;
         if (document != null) {
         }
         if (root == null) {
            reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR, XMLMessages.SCHEMA_GENERIC_ERROR, "Can't get back Schema document's root element :" + loc);
         } else {
            if (uri!=null && !uri.equals(root.getAttribute(SchemaSymbols.ATT_TARGETNAMESPACE)) ) {
               reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR, XMLMessages.SCHEMA_GENERIC_ERROR, "Schema in " + loc + " has a different target namespace " +
                                         "from the one specified in the instance document :" + uri);
            }

            grammar = new SchemaGrammar();
            grammar.setGrammarDocument(document);

            fGrammarIsSchemaGrammar = true;
            fGrammarIsDTDGrammar = false;

            GeneralAttrCheck generalAttrCheck = new GeneralAttrCheck(fErrorReporter, fDataTypeReg);
            tst = new TraverseSchema( root, fStringPool, (SchemaGrammar)grammar, fGrammarResolver, fErrorReporter, source.getSystemId(), currentER, getSchemaFullCheckingEnabled(), generalAttrCheck, fExternalSchemas, fExternalNoNamespaceSchema);
            generalAttrCheck.checkNonSchemaAttributes(fGrammarResolver);


            String targetNS =   root.getAttribute("targetNamespace");
            fGrammarNameSpaceIndex = fStringPool.addSymbol(targetNS);
            fGrammarResolver.putGrammar(targetNS, grammar);
            fGrammar = (SchemaGrammar)grammar;

            if (fSchemaValidationFullChecking) {
               try {
                 Enumeration grammarURIs = fGrammarResolver.nameSpaceKeys();
                 String grammarURI;
                 Grammar gGrammar;
                 SchemaGrammar sGrammar;
                 while (grammarURIs.hasMoreElements()) {
                     grammarURI = (String)grammarURIs.nextElement();
                     if (UPACheckedGrammarURIs.get(grammarURI) != null)
                        continue;
                     UPACheckedGrammarURIs.put(grammarURI, fgNullObject);
                     gGrammar = fGrammarResolver.getGrammar(grammarURI);
                     if (!(gGrammar instanceof SchemaGrammar))
                        continue;
                     sGrammar = (SchemaGrammar)gGrammar;

                     Hashtable complexTypeRegistry = sGrammar.getComplexTypeRegistry();
                     int count = complexTypeRegistry.size();
                     Enumeration enum = complexTypeRegistry.elements();

                     TraverseSchema.ComplexTypeInfo typeInfo;
                     if (fSGComparator == null) {
                         fSGComparator = new SubstitutionGroupComparator(fGrammarResolver, fStringPool, fErrorReporter);
                     }
                     while (enum.hasMoreElements ()) {
                         typeInfo = (TraverseSchema.ComplexTypeInfo)enum.nextElement();
                         sGrammar.getContentModel(typeInfo.contentSpecHandle,
                                                  typeInfo.contentType,
                                                  fSGComparator);
                     }
                 }
               } catch (CMException excToCatch) {
                  int majorCode = excToCatch.getErrorCode();
                  fErrorReporter.reportError(fErrorReporter.getLocator(),
                                             ImplementationMessages.XERCES_IMPLEMENTATION_DOMAIN,
                                             majorCode,
                                             0,
                                             null,
                                             XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
               }
            }
         }
      }

   }


   /*
    * for <notation> must resolve values "b:myNotation"
    *
    * @param value  string value of element or attribute
    * @return
    * @exception Exception
    */
   private String bindNotationURI (String value)  throws Exception{
       int colonP = value.indexOf(":");
       String prefix = "";
       String localpart = value;
       if (colonP > -1) {
           prefix = value.substring(0,colonP);
           localpart = value.substring(colonP+1);
       }

       String uri = "";
       int uriIndex = StringPool.EMPTY_STRING;
       if (fNamespacesScope != null) {
           uriIndex = fNamespacesScope.getNamespaceForPrefix(fStringPool.addSymbol(prefix));
           if (uriIndex > 0) {
               return fStringPool.toString(uriIndex)+":"+localpart;
           } else if (fGrammarNameSpaceIndex!=-1){
               return  fStringPool.toString( fGrammarNameSpaceIndex)+":"+localpart;
           }


       }
       return value;
   }

   static class Resolver implements EntityResolver {


      private static final String SYSTEM[] = {
      };
      private static final String PATH[] = {
         "structures.dtd",
         "datatypes.dtd",
         "versionInfo.ent",
      };


      private DefaultEntityHandler fEntityHandler;


      public Resolver(DefaultEntityHandler handler) {
         fEntityHandler = handler;
      }


      public InputSource resolveEntity(String publicId, String systemId)
      throws IOException, SAXException {

         for (int i = 0; i < SYSTEM.length; i++) {
            if (systemId.equals(SYSTEM[i])) {
               InputSource source = new InputSource(getClass().getResourceAsStream(PATH[i]));
               source.setPublicId(publicId);
               source.setSystemId(systemId);
               return source;
            }
         }

         EntityResolver resolver = fEntityHandler.getEntityResolver();
         if (resolver != null) {
            InputSource source = resolver.resolveEntity(publicId, systemId);
            if (source != null) {
               return source;
            }
         }

         return new InputSource(fEntityHandler.expandSystemId(systemId));



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
      public void fatalError(SAXParseException ex)  {
         System.err.println("[Fatal Error] "+
                            getLocationString(ex)+": "+
                            ex.getMessage());
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

   private int attributeTypeName(XMLAttributeDecl attrDecl) {
      switch (attrDecl.type) {
      case XMLAttributeDecl.TYPE_ENTITY: {
            return attrDecl.list ? fENTITIESSymbol : fENTITYSymbol;
         }
      case XMLAttributeDecl.TYPE_ENUMERATION: {
            String enumeration = fStringPool.stringListAsString(attrDecl.enumeration);
            return fStringPool.addSymbol(enumeration);
         }
      case XMLAttributeDecl.TYPE_ID: {
            return fIDSymbol;
         }
      case XMLAttributeDecl.TYPE_IDREF: {
            return attrDecl.list ? fIDREFSSymbol : fIDREFSymbol;
         }
      case XMLAttributeDecl.TYPE_NMTOKEN: {
            return attrDecl.list ? fNMTOKENSSymbol : fNMTOKENSymbol;
         }
      case XMLAttributeDecl.TYPE_NOTATION: {
            return fNOTATIONSymbol;
         }
      }
      return fCDATASymbol;
   }

   /** Validates element and attributes. */
   private void validateElementAndAttributes(QName element,
                                             XMLAttrList attrList)
   throws Exception {

      if ((fGrammarIsSchemaGrammar && fElementDepth >= 0 && fValidationFlagStack[fElementDepth] != 0 )||
          (fGrammar == null && !fValidating && !fNamespacesEnabled) ) {
         fCurrentElementIndex = -1;
         fCurrentContentSpecType = -1;
         fInElementContent = false;
         if (fAttrListHandle != -1) {
            int dupAttrs[];
            if ((dupAttrs = fAttrList.endAttrList()) != null) {
               Object[] args = {fStringPool.toString(element.rawname), null};
               for (int i = 0; i < dupAttrs.length; i++) {
                   args[1] = fStringPool.toString(dupAttrs[i]);
                   fErrorReporter.reportError(fErrorReporter.getLocator(),
                                              XMLMessages.XMLNS_DOMAIN,
                                              XMLMessages.MSG_ATTRIBUTE_NOT_UNIQUE,
                                              XMLMessages.WFC_UNIQUE_ATT_SPEC,
                                              args,
                                              XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
               }
            }
         }
         return;
      }

      int elementIndex = -1;
      int contentSpecType = -1;

      boolean skipThisOne = false;
      boolean laxThisOne = false;

      if ( fGrammarIsSchemaGrammar && fElementDepth > -1 && fContentLeafStack[fElementDepth] != null ) {
         ContentLeafNameTypeVector cv = fContentLeafStack[fElementDepth];

         if (fContentModelStateStack[fElementDepth] >= 0) {
            int pos = ((DFAContentModel)fContentModelStack[fElementDepth]).
                      oneTransition(element, fContentModelStateStack, fElementDepth);
            if (pos >= 0) {
                fContentModelEleCount[fElementDepth]++;
                switch (cv.leafTypes[pos]) {
                case XMLContentSpec.CONTENTSPECNODE_ANY_SKIP:
                case XMLContentSpec.CONTENTSPECNODE_ANY_NS_SKIP:
                case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER_SKIP:
                      skipThisOne = true;
                      break;
                case XMLContentSpec.CONTENTSPECNODE_ANY_LAX:
                case XMLContentSpec.CONTENTSPECNODE_ANY_NS_LAX:
                case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER_LAX:
                      laxThisOne = true;
                      break;
                }
            }
         }
/*
         QName[] fElemMap = cv.leafNames;
         for (int i=0; i<cv.leafCount; i++) {
            int type = cv.leafTypes[i]  ;

            if (type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
               if (fElemMap[i].uri==element.uri
                   && fElemMap[i].localpart == element.localpart)
                  break;
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY) {
                  break;
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_NS) {
               if (element.uri == fElemMap[i].uri) {
                  break;
               }
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER) {
               if (fElemMap[i].uri != element.uri) {
                  break;
               }
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_SKIP) {
                  skipThisOne = true;
                  break;
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_NS_SKIP) {
               if (element.uri == fElemMap[i].uri) {
                  skipThisOne = true;
                  break;
               }
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER_SKIP) {
               if (fElemMap[i].uri != element.uri) {
                  skipThisOne = true;
                  break;
               }
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_LAX) {
                  laxThisOne = true;
                  break;
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_NS_LAX) {
               if (element.uri == fElemMap[i].uri) {
                  laxThisOne = true;
                  break;
               }
            } else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER_LAX) {
               if (fElemMap[i].uri != element.uri) {
                  laxThisOne = true;
                  break;
               }
            }

         }
*/
      }

      if (skipThisOne) {
         fNeedValidationOff = true;
      } else {
         TraverseSchema.ComplexTypeInfo baseTypeInfo = null;
        if (fGrammarIsSchemaGrammar && fCurrentElementIndex != -1)
            baseTypeInfo = ((SchemaGrammar)fGrammar).getElementComplexTypeInfo(fCurrentElementIndex);


         if ( fNamespacesEnabled && fValidating &&
              element.uri != fGrammarNameSpaceIndex &&
              element.uri != StringPool.EMPTY_STRING) {
            fGrammarNameSpaceIndex = element.uri;

            boolean success = switchGrammar(fGrammarNameSpaceIndex);

            if (!success && !laxThisOne) {
               reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR, XMLMessages.SCHEMA_GENERIC_ERROR,
                                         "Grammar with uri: " + fStringPool.toString(fGrammarNameSpaceIndex)
                                         + " or the instance document's namespace may not match the targetNamespace of the schema");
            }
         }

         if ( fGrammar != null ) {
            if (DEBUG_SCHEMA_VALIDATION) {
               System.out.println("*******Lookup element: uri: " + fStringPool.toString(element.uri)+
                                  " localpart: '" + fStringPool.toString(element.localpart)
                                  +"' and scope : " + fCurrentScope+"\n");
            }

            elementIndex = fGrammar.getElementDeclIndex(element,fCurrentScope);

            if (elementIndex == -1 ) {
               elementIndex = fGrammar.getElementDeclIndex(element, TOP_LEVEL_SCOPE);
            }

            if (elementIndex == -1) {
               if (fGrammarIsSchemaGrammar && fCurrentElementIndex != -1) {
                  int aGrammarNSIndex = fGrammarNameSpaceIndex;
                  while (baseTypeInfo != null) {
                    String baseTName = baseTypeInfo.typeName;
                    if (!baseTName.startsWith("#")) {
                       int comma = baseTName.indexOf(',');
                       aGrammarNSIndex = fStringPool.addSymbol(baseTName.substring(0,comma).trim());
                       if (aGrammarNSIndex != fGrammarNameSpaceIndex) {
                          if ( !switchGrammar(aGrammarNSIndex) ) {
                          }
                          fGrammarNameSpaceIndex = aGrammarNSIndex;
                       }
                    }
                     elementIndex = fGrammar.getElementDeclIndex(element, baseTypeInfo.scopeDefined);
                     if (elementIndex > -1 ) {
                        break;
                     }
                     baseTypeInfo = baseTypeInfo.baseComplexTypeInfo;
                  }
                  if(elementIndex == -1 && element.uri == StringPool.EMPTY_STRING) {
                    boolean success = switchGrammar(element.uri);
                    if(success) {
                        fGrammarNameSpaceIndex = element.uri;
                        elementIndex = fGrammar.getElementDeclIndex(element.localpart, TOP_LEVEL_SCOPE);
                    }
                  }
               }
               /****/
               if ( element.uri == StringPool.EMPTY_STRING && elementIndex == -1
               && fNamespacesScope != null ) {
               elementIndex = fGrammar.getElementDeclIndex(element.localpart, TOP_LEVEL_SCOPE);
               element.uri = StringPool.EMPTY_STRING;
               }
               /****/

               /****/
               if (elementIndex == -1) {
                  if (laxThisOne) {
                     fNeedValidationOff = true;
                  } else
                     if (DEBUG_SCHEMA_VALIDATION)
                     System.out.println("!!! can not find elementDecl in the grammar, " +
                                        " the element localpart: " + element.localpart +
                                        "["+fStringPool.toString(element.localpart) +"]" +
                                        " the element uri: " + element.uri +
                                        "["+fStringPool.toString(element.uri) +"]" +
                                        " and the current enclosing scope: " + fCurrentScope );
               }
               /****/
            }

            if (DEBUG_SCHEMA_VALIDATION) {
               fGrammar.getElementDecl(elementIndex, fTempElementDecl);
               System.out.println("elementIndex: " + elementIndex+" \n and itsName : '"
                                  + fStringPool.toString(fTempElementDecl.name.localpart)
                                  +"' \n its ContentType:" + fTempElementDecl.type
                                  +"\n its ContentSpecIndex : " + fTempElementDecl.contentSpecIndex +"\n"+
                                  " and the current enclosing scope: " + fCurrentScope);
            }
         }

         final int oldElementIndex = elementIndex;

         contentSpecType =  getContentSpecType(elementIndex);
         int elementNameLocalPart = StringPool.NULL_STRING;
         DatatypeValidator elementDatatypeValidator = null;

         if (elementIndex != -1) {
             fGrammar.getElementDecl(elementIndex, fTempElementDecl);
             elementNameLocalPart = fTempElementDecl.name.localpart;
             elementDatatypeValidator = fTempElementDecl.datatypeValidator;
         }

         if (fGrammarIsSchemaGrammar) {

            if (fXsiTypeAttValue > -1) {
               String xsiType = fStringPool.toString(fXsiTypeAttValue);
               int colonP = xsiType.indexOf(":");
               String prefix = "";
               String localpart = xsiType;
               if (colonP > -1) {
                  prefix = xsiType.substring(0,colonP);
                  localpart = xsiType.substring(colonP+1);
               }

               String uri = "";
               int uriIndex = StringPool.EMPTY_STRING;
               if (fNamespacesScope != null) {
                  uriIndex = fNamespacesScope.getNamespaceForPrefix(fStringPool.addSymbol(prefix));
                  if (uriIndex > 0) {
                     uri = fStringPool.toString(uriIndex);
                     if (uriIndex != fGrammarNameSpaceIndex) {
                        fGrammarNameSpaceIndex = fCurrentSchemaURI = uriIndex;
                        boolean success = switchGrammar(fCurrentSchemaURI);
                        if (!success && !fNeedValidationOff) {
                           if (!uri.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA))
                               reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                                         XMLMessages.SCHEMA_GENERIC_ERROR,
                                                         "Grammar with uri: "
                                                         + fStringPool.toString(fCurrentSchemaURI)
                                                         + " , can not be found");
                        }
                     }
                  }
               }


               Hashtable complexRegistry = ((SchemaGrammar)fGrammar).getComplexTypeRegistry();
               DatatypeValidatorFactoryImpl dataTypeReg = ((SchemaGrammar)fGrammar).getDatatypeRegistry();
               if (complexRegistry==null || dataTypeReg == null) {
                  reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                            XMLMessages.SCHEMA_GENERIC_ERROR,
                                            fErrorReporter.getLocator().getSystemId()
                                            +" line"+fErrorReporter.getLocator().getLineNumber()
                                            +", canot resolve xsi:type = " + xsiType+"  ---2");
               } else {
                  TraverseSchema.ComplexTypeInfo typeInfo =
                  (TraverseSchema.ComplexTypeInfo) complexRegistry.get(uri+","+localpart);

                  if (typeInfo==null) {
                     if (uri.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) ) {
                        fXsiTypeValidator = dataTypeReg.getDatatypeValidator(localpart);
                     } else
                        fXsiTypeValidator = dataTypeReg.getDatatypeValidator(uri+","+localpart);
                     if ( fXsiTypeValidator == null )
                        reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                                  XMLMessages.SCHEMA_GENERIC_ERROR,
                                                  "unresolved type : "+uri+","+localpart
                                                  +" found  in xsi:type handling");
                     else if (elementIndex != -1) {
                        DatatypeValidator ancestorValidator = elementDatatypeValidator;
                        DatatypeValidator tempVal = fXsiTypeValidator;
                        for(; tempVal != null; tempVal = tempVal.getBaseValidator())
                            if(tempVal == ancestorValidator) break;
                        if(tempVal == null) {
                            if(ancestorValidator instanceof UnionDatatypeValidator) {
                                Vector subUnionMemberDV = ((UnionDatatypeValidator)ancestorValidator).getBaseValidators();
                                int subUnionSize = subUnionMemberDV.size();
                                boolean found = false;
                                for (int i=0; i<subUnionSize && !found; i++) {
                                    DatatypeValidator dTempSub = (DatatypeValidator)subUnionMemberDV.elementAt(i);
                                    DatatypeValidator dTemp = fXsiTypeValidator;
                                    for(; dTemp != null; dTemp = dTemp.getBaseValidator()) {
                                        if(dTempSub == dTemp) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        if (dTempSub instanceof AnySimpleType) {
                                            found = true;
                                        }
                                    }
                                }
                                if(!found) {
                                    reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                        XMLMessages.SCHEMA_GENERIC_ERROR,
                                        "Type : "+uri+","+localpart
                                        +" does not derive from the type of element " + fStringPool.toString(elementNameLocalPart));
                                }
                            } else
                            if ((ancestorValidator == null &&
                                 ((SchemaGrammar)fGrammar).getElementComplexTypeInfo(elementIndex) == null) ||
                                (ancestorValidator instanceof AnySimpleType)) {
                            } else {
                                reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                    XMLMessages.SCHEMA_GENERIC_ERROR,
                                    "Type : "+uri+","+localpart
                                    +" does not derive from the type of element " + fStringPool.toString(elementNameLocalPart));
                            }
                        } else {
                            if (tempVal != null && fXsiTypeValidator != null &&
                                    (fGrammar.getFirstAttributeDeclIndex(elementIndex) != -1)) {
                                reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                    XMLMessages.SCHEMA_GENERIC_ERROR,
                                    "Type : "+uri+","+localpart
                                    +" does not derive from the type of element " + fStringPool.toString(elementNameLocalPart));
                            }
                            if((((SchemaGrammar)fGrammar).getElementDeclBlockSet(elementIndex) & SchemaSymbols.RESTRICTION) != 0) {
                                reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                    XMLMessages.SCHEMA_GENERIC_ERROR,
                                    "Element " + fStringPool.toString(elementNameLocalPart)
                                    + "does not permit substitution by a type such as "+uri+","+localpart);
                            }
                        }
                     }
                  } else {

                     if (typeInfo.isAbstractType()) {
                        reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                               XMLMessages.SCHEMA_GENERIC_ERROR,
                               "Abstract type " + xsiType + " should not be used in xsi:type");
                     }
                     if (elementIndex != -1) {

                         TraverseSchema.ComplexTypeInfo tempType = typeInfo;
                         TraverseSchema.ComplexTypeInfo destType = ((SchemaGrammar)fGrammar).getElementComplexTypeInfo(elementIndex);
                         for(; tempType != null && destType != null; tempType = tempType.baseComplexTypeInfo) {
                            if(tempType.typeName.equals(destType.typeName))
                                break;
                         }
                         if(tempType == null) {
                            reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                   XMLMessages.SCHEMA_GENERIC_ERROR,
                                    "Type : "+uri+","+localpart
                                    +" does not derive from the type " + destType.typeName);
                         } else if (destType == null && elementDatatypeValidator != null) {
                            DatatypeValidator ancestorValidator = elementDatatypeValidator;
                            DatatypeValidator tempVal = fXsiTypeValidator;
                            for(; tempVal != null; tempVal = tempVal.getBaseValidator())
                                if(tempVal == ancestorValidator) break;
                            if (tempVal == null) {
                                if (ancestorValidator instanceof AnySimpleType) {
                                    tempVal = fXsiTypeValidator;
                                }
                            }
                            if(tempVal == null) {
                                if(ancestorValidator instanceof UnionDatatypeValidator) {
                                    Vector subUnionMemberDV = ((UnionDatatypeValidator)ancestorValidator).getBaseValidators();
                                    int subUnionSize = subUnionMemberDV.size();
                                    boolean found = false;
                                    for (int i=0; i<subUnionSize && !found; i++) {
                                        DatatypeValidator dTempSub = (DatatypeValidator)subUnionMemberDV.elementAt(i);
                                        DatatypeValidator dTemp = fXsiTypeValidator;
                                        for(; dTemp != null; dTemp = dTemp.getBaseValidator()) {
                                            if(dTempSub == dTemp) {
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (!found) {
                                            if (dTempSub instanceof AnySimpleType) {
                                                found = true;
                                            }
                                        }
                                    }
                                    if(!found) {
                                        reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                            XMLMessages.SCHEMA_GENERIC_ERROR,
                                            "Type : "+uri+","+localpart
                                            +" does not derive from the type of element " + fStringPool.toString(elementNameLocalPart));
                                    }
                                } else {
                                    reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                        XMLMessages.SCHEMA_GENERIC_ERROR,
                                        "Type : "+uri+","+localpart
                                        +" does not derive from the type of element " + fStringPool.toString(elementNameLocalPart));
                                }
                            }
                            int derivationMethod = typeInfo.derivedBy;
                            if((((SchemaGrammar)fGrammar).getElementDeclBlockSet(elementIndex) & derivationMethod) != 0) {
                                reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                   XMLMessages.SCHEMA_GENERIC_ERROR,
                                    "Element " + fStringPool.toString(elementNameLocalPart) +
                                    " does not permit xsi:type substitution in the manner required by type "+uri+","+localpart);
                            } else if (typeInfo.baseComplexTypeInfo != null &&
                                       (typeInfo.baseComplexTypeInfo.blockSet & derivationMethod) != 0) {
                                reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                   XMLMessages.SCHEMA_GENERIC_ERROR,
                                    "Type " + typeInfo.baseComplexTypeInfo.typeName + " does not permit other types, such as "
                                    +uri+","+localpart + " to be substituted for itself using xsi:type");
                            }
                         }
                     }
                     elementIndex = typeInfo.templateElementIndex;
                  }
               }

               fXsiTypeAttValue = -1;
            }

            else if (elementIndex != -1) {
               TraverseSchema.ComplexTypeInfo typeInfo =
                 ((SchemaGrammar) fGrammar).getElementComplexTypeInfo(elementIndex);

               if (typeInfo != null &&
                   typeInfo.isAbstractType()) {
                  reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                   XMLMessages.SCHEMA_GENERIC_ERROR,
                   "Element " + fStringPool.toString(element.rawname) + " is declared with a type that is abstract.  Use xsi:type to specify a non-abstract type");
               }
            }

            if (elementIndex != -1) {
                int miscFlags = ((SchemaGrammar) fGrammar).getElementDeclMiscFlags(elementIndex);
                if ((miscFlags & SchemaSymbols.ABSTRACT) != 0) {
                  reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                    XMLMessages.SCHEMA_GENERIC_ERROR,
                    "A member of abstract element " + fStringPool.toString(element.rawname) + "'s substitution group must be specified");
                }
                if (fNil && (miscFlags & SchemaSymbols.NILLABLE) == 0 ) {
                    fNil = false;
                    reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                    XMLMessages.SCHEMA_GENERIC_ERROR,
                    "xsi:nil must not be specified for the element "+ fStringPool.toString(element.rawname)+
                                              " with {nillable} equals 'false'");
                }
                fCurrentScope = ((SchemaGrammar) fGrammar).getElementDefinedScope(elementIndex);

                String anotherSchemaURI = ((SchemaGrammar)fGrammar).getElementFromAnotherSchemaURI(elementIndex);
                if (anotherSchemaURI != null) {
                   if (contentSpecType != -1) {
                      TraverseSchema.ComplexTypeInfo typeInfo = ((SchemaGrammar) fGrammar).getElementComplexTypeInfo(elementIndex);
                      if (typeInfo != null) {
                         elementIndex = typeInfo.templateElementIndex;
                      }

                      fGrammarNameSpaceIndex = fCurrentSchemaURI = fStringPool.addSymbol(anotherSchemaURI);
                      boolean success = switchGrammar(fCurrentSchemaURI);
                      if (!success && !fNeedValidationOff) {
                         reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                                   XMLMessages.SCHEMA_GENERIC_ERROR,
                                                   "Grammar with uri: "
                                                   + fStringPool.toString(fCurrentSchemaURI)
                                                   + " , can not be found");
                      }
                    }
                }

             }
         }
         if (elementIndex != oldElementIndex)
             contentSpecType =  getContentSpecType(elementIndex);

         if (contentSpecType == -1 && fValidating && !fNeedValidationOff ) {
            reportRecoverableXMLError(XMLMessages.MSG_ELEMENT_NOT_DECLARED,
                                      XMLMessages.VC_ELEMENT_VALID,
                                      element.rawname);
         }
         if (fGrammar != null && fGrammarIsSchemaGrammar && elementIndex != -1) {
            fAttrListHandle = addDefaultAttributes(elementIndex, attrList, fAttrListHandle, fValidating, fStandaloneReader != -1);
         }
         if (fAttrListHandle != -1) {
            int dupAttrs[];
            if ((dupAttrs = fAttrList.endAttrList()) != null) {
               Object[] args = {fStringPool.toString(element.rawname), null};
               for (int i = 0; i < dupAttrs.length; i++) {
                   args[1] = fStringPool.toString(dupAttrs[i]);
                   fErrorReporter.reportError(fErrorReporter.getLocator(),
                                              XMLMessages.XMLNS_DOMAIN,
                                              XMLMessages.MSG_ATTRIBUTE_NOT_UNIQUE,
                                              XMLMessages.WFC_UNIQUE_ATT_SPEC,
                                              args,
                                              XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
               }
            }
         }

         if (DEBUG_PRINT_ATTRIBUTES) {
            String elementStr = fStringPool.toString(element.rawname);
            System.out.print("startElement: <" + elementStr);
            if (fAttrListHandle != -1) {
               int index = attrList.getFirstAttr(fAttrListHandle);
               while (index != -1) {
                  System.out.print(" " + fStringPool.toString(attrList.getAttrName(index)) + "=\"" +
                                   fStringPool.toString(attrList.getAttValue(index)) + "\"");
                  index = attrList.getNextAttr(index);
               }
            }
            System.out.println(">");
         }
         if (fAttrListHandle != -1 && !fNeedValidationOff ) {
            int index = fAttrList.getFirstAttr(fAttrListHandle);
            while (index != -1) {
               int attrNameIndex = attrList.getAttrName(index);

               int _xmlns = fStringPool.addSymbol("xmlns");

               if (attrNameIndex != _xmlns && attrList.getAttrPrefix(index) != _xmlns)
                  if (fGrammar != null) {
                     fTempQName.setValues(attrList.getAttrPrefix(index),
                                          attrList.getAttrLocalpart(index),
                                          attrList.getAttrName(index),
                                          attrList.getAttrURI(index) );
                     int attDefIndex = getAttDefByElementIndex(elementIndex, fTempQName);

                     if (fTempQName.uri != fXsiURI)
                        if (attDefIndex == -1 ) {
                           if (fValidating) {
                              Object[] args = { fStringPool.toString(element.rawname),
                                 fStringPool.toString(attrList.getAttrName(index))};

                              /*****/
                              fAttrNameLocator = getLocatorImpl(fAttrNameLocator);

                              fErrorReporter.reportError(fAttrNameLocator,
                                                         XMLMessages.XML_DOMAIN,
                                                         XMLMessages.MSG_ATTRIBUTE_NOT_DECLARED,
                                                         XMLMessages.VC_ATTRIBUTE_VALUE_TYPE,
                                                         args,
                                                         XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                              /******/
                           }
                        } else {

                           fGrammar.getAttributeDecl(attDefIndex, fTempAttDecl);

                           int attributeType = attributeTypeName(fTempAttDecl);
                           attrList.setAttType(index, attributeType);


                            if (fGrammarIsDTDGrammar) {
                                  int normalizedValue = validateDTDattribute(element, attrList.getAttValue(index), fTempAttDecl);
                                  attrList.setAttValue(index, normalizedValue);

                            }
                            if (fValidating) {
                              if (fGrammarIsDTDGrammar) {
                              }
                              else if ( fGrammarIsSchemaGrammar &&
                                        (fTempAttDecl.type == XMLAttributeDecl.TYPE_ANY_ANY
                                         ||fTempAttDecl.type == XMLAttributeDecl.TYPE_ANY_LIST
                                         ||fTempAttDecl.type == XMLAttributeDecl.TYPE_ANY_OTHER) ) {

                                 if ((fTempAttDecl.defaultType & XMLAttributeDecl.PROCESSCONTENTS_SKIP) > 0) {
                                 } else if ( (fTempAttDecl.defaultType & XMLAttributeDecl.PROCESSCONTENTS_STRICT) > 0
                                             || (fTempAttDecl.defaultType & XMLAttributeDecl.PROCESSCONTENTS_LAX) > 0) {

                                    boolean reportError = false;
                                    boolean processContentStrict =
                                    (fTempAttDecl.defaultType & XMLAttributeDecl.PROCESSCONTENTS_STRICT) > 0;

                                    {
                                       Grammar aGrammar =
                                       fGrammarResolver.getGrammar(fStringPool.toString(fTempQName.uri));

                                       if (aGrammar == null || !(aGrammar instanceof SchemaGrammar) ) {
                                          if (processContentStrict) {
                                             reportError = true;
                                          }
                                       } else {
                                          SchemaGrammar sGrammar = (SchemaGrammar) aGrammar;
                                          Hashtable attRegistry = sGrammar.getAttributeDeclRegistry();
                                          if (attRegistry == null) {
                                             if (processContentStrict) {
                                                reportError = true;
                                             }
                                          } else {
                                             XMLAttributeDecl attDecl = (XMLAttributeDecl) attRegistry.get(fStringPool.toString(fTempQName.localpart));
                                             if (attDecl == null) {
                                                if (processContentStrict) {
                                                   reportError = true;
                                                }
                                             } else {
                                                DatatypeValidator attDV = attDecl.datatypeValidator;
                                                if (attDV == null) {
                                                   if (processContentStrict) {
                                                      reportError = true;
                                                   }
                                                } else {
                                                   try {
                                                      String  value = fStringPool.toString(attrList.getAttValue(index));
                                                      fWhiteSpace = attDV.getWSFacet();
                                                      if (fWhiteSpace != DatatypeValidator.PRESERVE) { 
                                                            value = normalizeValue(value);
                                                      }
                                                       validateUsingDV(attDV, value, false);
                                                       if (fNormalizeContents) {
                                                            int normalizedValue = fStringPool.addString(value);
                                                            attrList.setAttValue(index,normalizedValue);
                                                       }
                                                   } catch (InvalidDatatypeValueException idve) {
                                                      fErrorReporter.reportError(fErrorReporter.getLocator(),
                                                                                 SchemaMessageProvider.SCHEMA_DOMAIN,
                                                                                 SchemaMessageProvider.DatatypeError,
                                                                                 SchemaMessageProvider.MSG_NONE,
                                                                                 new Object [] { idve.getMessage()},
                                                                                 XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                    if (reportError) {
                                       Object[] args = { fStringPool.toString(element.rawname),
                                          "ANY---"+fStringPool.toString(attrList.getAttrName(index))};

                                       fAttrNameLocator = getLocatorImpl(fAttrNameLocator);

                                       fErrorReporter.reportError(fAttrNameLocator,
                                                                  XMLMessages.XML_DOMAIN,
                                                                  XMLMessages.MSG_ATTRIBUTE_NOT_DECLARED,
                                                                  XMLMessages.VC_ATTRIBUTE_VALUE_TYPE,
                                                                  args,
                                                                  XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);

                                    }
                                 }
                              } else if (fTempAttDecl.datatypeValidator == null) {
                                 Object[] args = { fStringPool.toString(element.rawname),
                                    fStringPool.toString(attrList.getAttrName(index))};

                                 System.out.println("[Error] Datatypevalidator for attribute " + fStringPool.toString(attrList.getAttrName(index))
                                                    + " not found in element type " + fStringPool.toString(element.rawname));
                                 /****/
                                 fAttrNameLocator = getLocatorImpl(fAttrNameLocator);

                                 fErrorReporter.reportError(fAttrNameLocator,
                                                            XMLMessages.XML_DOMAIN,
                                                            XMLMessages.MSG_ATTRIBUTE_NOT_DECLARED,
                                                            XMLMessages.VC_ATTRIBUTE_VALUE_TYPE,
                                                            args,
                                                            XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                                 /****/
                              } else {
                                 try {
                                    String  value = fStringPool.toString(attrList.getAttValue(index));
                                    DatatypeValidator tempDV = fTempAttDecl.datatypeValidator;
                                    fWhiteSpace = tempDV.getWSFacet();
                                    if (fWhiteSpace != DatatypeValidator.PRESERVE) { 
                                        value = normalizeValue(value);
                                    }
                                    if ((fTempAttDecl.defaultType & XMLAttributeDecl.DEFAULT_TYPE_FIXED) > 0 &&
                                        tempDV.compare(value, fTempAttDecl.defaultValue) != 0) {
                                        Object[] args = { fStringPool.toString(element.rawname),
                                                          fStringPool.toString(attrList.getAttrName(index)),
                                                          value,
                                                          fTempAttDecl.defaultValue};
                                        fErrorReporter.reportError( fErrorReporter.getLocator(),
                                                                    XMLMessages.XML_DOMAIN,
                                                                    XMLMessages.MSG_FIXED_ATTVALUE_INVALID,
                                                                    XMLMessages.VC_FIXED_ATTRIBUTE_DEFAULT,
                                                                    args,
                                                                    XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                                    }
                                    validateUsingDV(tempDV, value, false);
                                    if (fNormalizeContents) {
                                        int normalizedValue = fStringPool.addString(value);
                                        attrList.setAttValue(index,normalizedValue );
                                    }
                                 } catch (InvalidDatatypeValueException idve) {
                                    fErrorReporter.reportError(fErrorReporter.getLocator(),
                                                               SchemaMessageProvider.SCHEMA_DOMAIN,
                                                               SchemaMessageProvider.DatatypeError,
                                                               SchemaMessageProvider.MSG_NONE,
                                                               new Object [] { idve.getMessage()},
                                                               XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                                 }
                              }



               index = fAttrList.getNextAttr(index);
            }
         }
      }
      if (fAttrListHandle != -1) {
         int index = attrList.getFirstAttr(fAttrListHandle);
         while (index != -1) {
            int attName = attrList.getAttrName(index);
            if (!fStringPool.equalNames(attName, fNamespacesPrefix)) {
               int attPrefix = attrList.getAttrPrefix(index);
               if (attPrefix != fNamespacesPrefix) {
                  if (attPrefix != -1) {
                     int uri = fNamespacesScope.getNamespaceForPrefix(attPrefix);
                     if (uri == StringPool.EMPTY_STRING) {
                        Object[] args = { fStringPool.toString(attPrefix)};
                        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                                   XMLMessages.XMLNS_DOMAIN,
                                                   XMLMessages.MSG_PREFIX_DECLARED,
                                                   XMLMessages.NC_PREFIX_DECLARED,
                                                   args,
                                                   XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                     }
                     attrList.setAttrURI(index, uri);
                  }
               }
            }
            index = attrList.getNextAttr(index);
         }
      }

      fCurrentElementIndex = elementIndex;
      fCurrentContentSpecType = contentSpecType;

      if (fValidating && contentSpecType == XMLElementDecl.TYPE_SIMPLE) {
         fBufferDatatype = true;
         fDatatypeBuffer.setLength(0);
      }

      fInElementContent = (contentSpecType == XMLElementDecl.TYPE_CHILDREN);



   /**
    * Validate attributes in DTD fashion.
    * Validation is separated from attribute value normalization (which is required
    * for non-validating parsers)
    * @return normalized attribute value
    */
   private int validateDTDattribute(QName element, int attValue,
                                     XMLAttributeDecl attributeDecl) throws Exception{
      AttributeValidator av = null;
      switch (attributeDecl.type) {
      case XMLAttributeDecl.TYPE_ENTITY:
         {
            String  unTrimValue      = fStringPool.toString(attValue);
            String  value            = unTrimValue.trim();
            if ( fValidationEnabled ) {
                if ( value != unTrimValue ) {
                    if ( invalidStandaloneAttDef(element, attributeDecl.name) ) {
                        reportRecoverableXMLError(XMLMessages.MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE,
                                                  XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                                  fStringPool.toString(attributeDecl.name.rawname), unTrimValue, value);
                    }
                }
                try {
                    if ( isAlistAttribute ) {
                        fValENTITIES.validate( value, fValidateEntity );
                    }
                    else {
                        fValENTITY.validate( value, fValidateEntity );
                    }
                }
                catch ( InvalidDatatypeValueException ex ) {
                    if ( ex.getMajorCode() != 1 && ex.getMinorCode() != -1 ) {
                        reportRecoverableXMLError(ex.getMajorCode(),
                                                  ex.getMinorCode(),
                                                  fStringPool.toString( attributeDecl.name.rawname), value );
                    }
                    else {
                        reportRecoverableXMLError(XMLMessages.MSG_ENTITY_INVALID,
                                                  XMLMessages.VC_ENTITY_NAME,
                                                  fStringPool.toString( attributeDecl.name.rawname), value );
                    }
                }
            }
            if (fNormalizeAttributeValues) {
                if (attributeDecl.list) {
                    attValue = normalizeListAttribute(value, attValue, unTrimValue);
                } else {
                    if (value != unTrimValue) {
                        attValue = fStringPool.addSymbol(value);
                    }
                }
            }
         }
         break;
      case XMLAttributeDecl.TYPE_ENUMERATION:
         av = fAttValidatorENUMERATION;
         break;
      case XMLAttributeDecl.TYPE_ID:
         {
            String  unTrimValue = fStringPool.toString(attValue);
            String  value       = unTrimValue.trim();
            if ( fValidationEnabled ) {
                if ( value != unTrimValue ) {
                    if ( invalidStandaloneAttDef(element, attributeDecl.name) ) {
                        reportRecoverableXMLError(XMLMessages.MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE,
                                                  XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                                  fStringPool.toString(attributeDecl.name.rawname), unTrimValue, value);
                    }
                }

                try {
                    fValID.validate( value, fIdDefs );
                }
                catch ( InvalidDatatypeValueException ex ) {
                    int major = ex.getMajorCode(), minor = ex.getMinorCode();
                    if ( major == -1 ) {
                        major = XMLMessages.MSG_ID_INVALID;
                        minor = XMLMessages.VC_ID;
                    }
                    reportRecoverableXMLError(major, minor,
                                              fStringPool.toString( attributeDecl.name.rawname),
                                              value );
                }
            }

            if (fNormalizeAttributeValues && value != unTrimValue) {
                attValue = fStringPool.addSymbol(value);
            }
         }
         break;
      case XMLAttributeDecl.TYPE_IDREF:
         {
            String  unTrimValue = fStringPool.toString(attValue);
            String  value       = unTrimValue.trim();
            if ( fValidationEnabled ) {
                if ( value != unTrimValue ) {
                    if ( invalidStandaloneAttDef(element, attributeDecl.name) ) {
                        reportRecoverableXMLError(XMLMessages.MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE,
                                                  XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                                  fStringPool.toString(attributeDecl.name.rawname), unTrimValue, value);
                    }
                }

                if ( attributeDecl.list && value.length() == 0 ) {
                    reportRecoverableXMLError(XMLMessages.MSG_IDREFS_INVALID, XMLMessages.VC_IDREF,
                                              fStringPool.toString(attributeDecl.name.rawname) ) ;
                }

                try {
                    if ( isAlistAttribute ) {
                        fValIDRefs.validate( value, this.fValidateIDRef );
                    }
                    else {
                        fValIDRef.validate( value, this.fValidateIDRef );
                    }
                }
                catch ( InvalidDatatypeValueException ex ) {
                    if ( ex.getMajorCode() != 1 && ex.getMinorCode() != -1 ) {
                        reportRecoverableXMLError(ex.getMajorCode(),
                                                  ex.getMinorCode(),
                                                  fStringPool.toString( attributeDecl.name.rawname), value );
                    }
                    else {
                        reportRecoverableXMLError(XMLMessages.MSG_IDREFS_INVALID,
                                                  XMLMessages.VC_IDREF,
                                                  fStringPool.toString( attributeDecl.name.rawname), value );
                    }
                }
            }
            if (fNormalizeAttributeValues) {
                if (attributeDecl.list) {
                    attValue = normalizeListAttribute(value, attValue, unTrimValue);
                } else {
                    if (value != unTrimValue) {
                        attValue = fStringPool.addSymbol(value);
                    }
                }
            }
         }
         break;
      case XMLAttributeDecl.TYPE_NOTATION:
         {
            /* WIP
            String  unTrimValue = fStringPool.toString(attValue);
         String  value       = unTrimValue.trim();
         if (fValidationEnabled) {
             if (value != unTrimValue) {
                 if (invalidStandaloneAttDef(element, attributeDecl.name)) {
                     reportRecoverableXMLError(XMLMessages.MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE,
                                               XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                               fStringPool.toString(attributeDecl.name.rawname), unTrimValue, value);
                 }
             }
         }
      }
        */
            av = fAttValidatorNOTATION;
         }
         break;
      case XMLAttributeDecl.TYPE_NMTOKEN:
         {
            String  unTrimValue = fStringPool.toString(attValue);
            String  value       = unTrimValue.trim();
            if ( fValidationEnabled ) {
                if ( value != unTrimValue ) {
                    if ( invalidStandaloneAttDef(element, attributeDecl.name) ) {
                        reportRecoverableXMLError(XMLMessages.MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE,
                                                  XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                                  fStringPool.toString(attributeDecl.name.rawname), unTrimValue, value);
                    }
                }
                if ( attributeDecl.list && value.length() == 0 ) {
                    reportRecoverableXMLError(XMLMessages.MSG_NMTOKENS_INVALID, XMLMessages.VC_NAME_TOKEN,
                                              fStringPool.toString(attributeDecl.name.rawname) ) ;
                }

                try {
                    if ( isAlistAttribute ) {
                        fValNMTOKENS.validate( value, null );
                    }
                    else {
                        fValNMTOKEN.validate( value, null );
                    }
                }
                catch ( InvalidDatatypeValueException ex ) {
                    reportRecoverableXMLError(XMLMessages.MSG_NMTOKEN_INVALID,
                                              XMLMessages.VC_NAME_TOKEN,
                }
            }
            if (fNormalizeAttributeValues) {
                if (attributeDecl.list) {
                    attValue = normalizeListAttribute(value, attValue, unTrimValue);
                } else {
                    if (value != unTrimValue) {
                        attValue = fStringPool.addSymbol(value);
                    }
                }
            }
         }
         break;
      }
      if ( av != null ) {
          int newValue = av.normalize(element, attributeDecl.name, attValue,
                                  attributeDecl.type, attributeDecl.enumeration);
          if (fNormalizeAttributeValues)
              attValue = newValue;
      }
      return attValue;
   }

   /**
    * @param value This is already trimmed.
    */
   private int normalizeListAttribute(String value, int origIndex, String unTrimValue) {

       fNormalizedStr.setLength(0);
       int length = value.length();
       boolean skipSpace = true;
       char c= 0;

       for (int i = 0; i < length; i++) {
            c = value.charAt(i);
            if (c == 0x20) {
                if (!skipSpace) {
                    fNormalizedStr.append(' ');
                    skipSpace = true;
                }
            }
            else {
                fNormalizedStr.append((char)c);
                skipSpace = false;
            }
       }
       if (fNormalizedStr.length() == unTrimValue.length())
           return origIndex;
       return fStringPool.addSymbol(fNormalizedStr.toString());
   }

   /** Character data in content. */
   private void charDataInContent() {

      if (DEBUG_ELEMENT_CHILDREN) {
         System.out.println("charDataInContent()");
      }
      if (fElementChildren.length <= fElementChildrenLength) {
         QName[] newarray = new QName[fElementChildren.length * 2];
         System.arraycopy(fElementChildren, 0, newarray, 0, fElementChildren.length);
         fElementChildren = newarray;
      }
      QName qname = fElementChildren[fElementChildrenLength];
      if (qname == null) {
         for (int i = fElementChildrenLength; i < fElementChildren.length; i++) {
            fElementChildren[i] = new QName();
         }
         qname = fElementChildren[fElementChildrenLength];
      }
      qname.clear();
      fElementChildrenLength++;


   /**
    * Check that the content of an element is valid.
    * <p>
    * This is the method of primary concern to the validator. This method is called
    * upon the scanner reaching the end tag of an element. At that time, the
    * element's children must be structurally validated, so it calls this method.
    * The index of the element being checked (in the decl pool), is provided as
    * well as an array of element name indexes of the children. The validator must
    * confirm that this element can have these children in this order.
    * <p>
    * This can also be called to do 'what if' testing of content models just to see
    * if they would be valid.
    * <p>
    * Note that the element index is an index into the element decl pool, whereas
    * the children indexes are name indexes, i.e. into the string pool.
    * <p>
    * A value of -1 in the children array indicates a PCDATA node. All other
    * indexes will be positive and represent child elements. The count can be
    * zero, since some elements have the EMPTY content model and that must be
    * confirmed.
    *
    * @param elementIndex The index within the <code>ElementDeclPool</code> of this
    *                     element.
    * @param childCount The number of entries in the <code>children</code> array.
    * @param children The children of this element.  Each integer is an index within
    *                 the <code>StringPool</code> of the child element name.  An index
    *                 of -1 is used to indicate an occurrence of non-whitespace character
    *                 data.
    *
    * @return The value -1 if fully valid, else the 0 based index of the child
    *         that first failed. If the value returned is equal to the number
    *         of children, then additional content is required to reach a valid
    *         ending state.
    *
    * @exception Exception Thrown on error.
    */
   private int checkContent(int elementIndex,
                            QName[] children,
                            int childOffset,
                            int childCount) throws Exception {

      final int elementType = fCurrentElement.rawname;

      if (DEBUG_PRINT_CONTENT) {
         String strTmp = fStringPool.toString(elementType);
         System.out.println("Name: "+strTmp+", "+
                            "Count: "+childCount+", "+
         for (int index = childOffset; index < (childOffset+childCount)  && index < 10; index++) {
            if (index == 0) {
               System.out.print("  (");
            }
            String childName = (children[index].localpart == -1) ? "#PCDATA" : fStringPool.toString(children[index].localpart);
            if (index + 1 == childCount) {
               System.out.println(childName + ")");
            } else if (index + 1 == 10) {
               System.out.println(childName + ",...)");
            } else {
               System.out.print(childName + ",");
            }
         }
      }

      final int contentType = fCurrentContentSpecType;

      if (contentType == XMLElementDecl.TYPE_EMPTY) {
         if (childCount != 0) {
            return 0;
         }
      } else if (contentType == XMLElementDecl.TYPE_ANY) {
      } else if (contentType == XMLElementDecl.TYPE_MIXED_SIMPLE ||
                 contentType == XMLElementDecl.TYPE_MIXED_COMPLEX ||
                 contentType == XMLElementDecl.TYPE_CHILDREN) {

          if (childCount == 0 && fNil) {
              fNil = false;
              return -1;
          }

         XMLContentModel cmElem = null;
         try {
            cmElem = getElementContentModel(elementIndex);
            int curState = fContentModelStateStack[fElementDepth+1];
            if (curState != -2) {
                if (curState == -1 ||
                    !((DFAContentModel)cmElem).isFinalState(curState)) {
                    return fContentModelEleCount[fElementDepth+1];
                } else {
                    return -1;
                }
            }
            int result = cmElem.validateContent(children, childOffset, childCount);
            if (result != -1 && fGrammarIsSchemaGrammar) {
               result = cmElem.validateContentSpecial(children, childOffset, childCount);
            }
            return result;
         } catch (CMException excToCatch) {
            int majorCode = excToCatch.getErrorCode();
            fErrorReporter.reportError(fErrorReporter.getLocator(),
                                       ImplementationMessages.XERCES_IMPLEMENTATION_DOMAIN,
                                       majorCode,
                                       0,
                                       null,
                                       XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
         }
      } else if (contentType == -1) {
         reportRecoverableXMLError(XMLMessages.MSG_ELEMENT_NOT_DECLARED,
                                   XMLMessages.VC_ELEMENT_VALID,
                                   elementType);
      } else if (contentType == XMLElementDecl.TYPE_SIMPLE ) {

         XMLContentModel cmElem = null;
         if (childCount > 0) {
            fErrorReporter.reportError(fErrorReporter.getLocator(),
                                       SchemaMessageProvider.SCHEMA_DOMAIN,
                                       SchemaMessageProvider.DatatypeError,
                                       SchemaMessageProvider.MSG_NONE,
                                       new Object [] { "In element '"+fStringPool.toString(elementType)+"' : "+
                                          "Can not have element children within a simple type content"},
                                       XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
         } else {
            try {

                    fGrammar.getElementDecl(elementIndex, fTempElementDecl);
                    fCurrentDV = fTempElementDecl.datatypeValidator;
                }

               if ( fXsiTypeValidator != null ) {
                  fCurrentDV = fXsiTypeValidator;
                  fXsiTypeValidator = null;
               }
               if (fCurrentDV == null) {
                  System.out.println("Internal Error: this element have a simpletype "+
                                     "but no datatypevalidator was found, element "+fTempElementDecl.name
                                     +",locapart: "+fStringPool.toString(fTempElementDecl.name.localpart));
               } else {
                   String value = fDatatypeBuffer.toString();
                   if (!fNormalizeContents && fWhiteSpace != DatatypeValidator.PRESERVE) {
                       fUnnormalizedStr.setLength(0);
                       fUnnormalizedStr.append(value);
                       normalizeWhitespace(fUnnormalizedStr, (fWhiteSpace==DatatypeValidator.COLLAPSE));
                       value = fNormalizedStr.toString();
                   }
                   String currentElementDefault = ((SchemaGrammar)fGrammar).getElementDefaultValue(fCurrentElementIndex);
                   int hasFixed =  (((SchemaGrammar)fGrammar).getElementDeclMiscFlags(fCurrentElementIndex) & SchemaSymbols.FIXED);
                   if (fNil) {
                       if (value.length() != 0) {
                         reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                                                 XMLMessages.SCHEMA_GENERIC_ERROR,
                                                 "An element <" +fStringPool.toString(elementType)+"> with attribute xsi:nil=\"true\" must be empty");
                       }
                       if (hasFixed !=0) {
                           reportRecoverableXMLError(XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                             XMLMessages.SCHEMA_GENERIC_ERROR,
                             "An element <" +fStringPool.toString(elementType)+"> with attribute xsi:nil=\"true\" must not have fixed value constraint");
                       }
                       fNil = false;
                       return -1;
                   }
                    if( currentElementDefault == null || currentElementDefault.length() == 0) {
                            validateUsingDV(fCurrentDV, value, false);
                    }
                    else {
                        if (hasFixed !=0) {
                                fDocumentHandler.characters(currentElementDefault.toCharArray(), 0, currentElementDefault.length());
                                validateUsingDV(fCurrentDV, currentElementDefault, true);
                            }
                                if ( fCurrentDV.compare(value, currentElementDefault) != 0 ) {
                                    fErrorReporter.reportError(fErrorReporter.getLocator(),
                                                               SchemaMessageProvider.SCHEMA_DOMAIN,
                                                               SchemaMessageProvider.FixedDiffersFromActual,
                                                               0, null,
                                                               XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                                }
                                validateUsingDV(fCurrentDV, value, true);
                            }
                        }
                        else {
                                fDocumentHandler.characters(currentElementDefault.toCharArray(), 0, currentElementDefault.length());
                                validateUsingDV(fCurrentDV, currentElementDefault, true);
                            }
                            else {
                                validateUsingDV(fCurrentDV, value, false);
                            }
                        }
                    }
               }
            } catch (InvalidDatatypeValueException idve) {
               fErrorReporter.reportError(fErrorReporter.getLocator(),
                                          SchemaMessageProvider.SCHEMA_DOMAIN,
                                          SchemaMessageProvider.DatatypeError,
                                          SchemaMessageProvider.MSG_NONE,
                                          new Object [] { "In element '"+fStringPool.toString(elementType)+"' : "+idve.getMessage()},
                                          XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
            }

            fCurrentDV = null;
            fFirstChunk= true;
            fTrailing=false;
         }
      } else {
         fErrorReporter.reportError(fErrorReporter.getLocator(),
                                    ImplementationMessages.XERCES_IMPLEMENTATION_DOMAIN,
                                    ImplementationMessages.VAL_CST,
                                    0,
                                    null,
                                    XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
      }

      return -1;



   /**
    * Checks that all declared elements refer to declared elements
    * in their content models. This method calls out to the error
    * handler to indicate warnings.
    */
   /*private void checkDeclaredElements() throws Exception {

               if (DEBUG) print("(???) XMLValidator.checkDeclaredElements\n");

       for (int i = 0; i < fElementCount; i++) {
           int type = fGrammar.getContentSpecType(i);
           if (type == XMLElementDecl.TYPE_MIXED_SIMPLE ||
               type == XMLElementDecl.TYPE_MIXED_COMPLEX ||
               type == XMLElementDecl.TYPE_CHILDREN) {
               int chunk = i >> CHUNK_SHIFT;
               int index = i &  CHUNK_MASK;
               int contentSpecIndex = fContentSpec[chunk][index];
               checkDeclaredElements(i, contentSpecIndex);
           }
       }
   }
   */

   private void printChildren() {
      if (DEBUG_ELEMENT_CHILDREN) {
         System.out.print('[');
         for (int i = 0; i < fElementChildrenLength; i++) {
            System.out.print(' ');
            QName qname = fElementChildren[i];
            if (qname != null) {
               System.out.print(fStringPool.toString(qname.rawname));
            } else {
               System.out.print("null");
            }
            if (i < fElementChildrenLength - 1) {
               System.out.print(", ");
            }
            System.out.flush();
         }
         System.out.print(" ]");
         System.out.println();
      }
   }

   private void printStack() {
      if (DEBUG_ELEMENT_CHILDREN) {
         System.out.print('{');
         for (int i = 0; i <= fElementDepth; i++) {
            System.out.print(' ');
            System.out.print(fElementChildrenOffsetStack[i]);
            if (i < fElementDepth) {
               System.out.print(", ");
            }
            System.out.flush();
         }
         System.out.print(" }");
         System.out.println();
      }
   }



   /**
    * AttributeValidator.
    */
   public interface AttributeValidator {


      /** Normalize. */
      public int normalize(QName element, QName attribute,
                           int attValue, int attType, int enumHandle)
      throws Exception;



   /** Returns true if invalid standalone attribute definition. */
   boolean invalidStandaloneAttDef(QName element, QName attribute) {
      if (fStandaloneReader == -1) {
         return false;
      }
      if (element.rawname == -1) {
         return false;
      }
      return getAttDefIsExternal(element, attribute);
   }

   void validateUsingDV (DatatypeValidator dv, String content, boolean onlyVal3Types)
       throws Exception, InvalidDatatypeValueException {
       if (dv instanceof IDDatatypeValidator) {
           dv.validate( content, fIdDefs );
       } else if (dv instanceof IDREFDatatypeValidator) {
           dv.validate( content, fValidateIDRef );
       } else if (dv instanceof ENTITYDatatypeValidator) {
           dv.validate( content, fValidateEntity);
       } else if (!onlyVal3Types) {
           if (dv instanceof NOTATIONDatatypeValidator && content != null) {
               content = bindNotationURI(content);
           }
           dv.validate( content, null);
       }
   }



   /**
    * AttValidatorNOTATION.
    */
   final class AttValidatorNOTATION
   implements AttributeValidator {



      /** Normalize. */
      public int normalize(QName element, QName attribute,
                           int attValueHandle, int attType,
                           int enumHandle) throws Exception {
         String attValue = fStringPool.toString(attValueHandle);
         String newAttValue = attValue.trim();
         if (fValidating) {
            if (newAttValue != attValue) {
               if (invalidStandaloneAttDef(element, attribute)) {
                  reportRecoverableXMLError(XMLMessages.MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE,
                                            XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                            fStringPool.toString(attribute.rawname), attValue, newAttValue);
               }
               attValueHandle = fStringPool.addSymbol(newAttValue);
            } else {
               attValueHandle = fStringPool.addSymbol(attValueHandle);
            }
            if (!fStringPool.stringInList(enumHandle, attValueHandle)) {
               reportRecoverableXMLError(XMLMessages.MSG_ATTRIBUTE_VALUE_NOT_IN_LIST,
                                         XMLMessages.VC_NOTATION_ATTRIBUTES,
                                         fStringPool.toString(attribute.rawname),
                                         newAttValue, fStringPool.stringListAsString(enumHandle));
            }
         } else if (newAttValue != attValue) {
            attValueHandle = fStringPool.addSymbol(newAttValue);
         }
         return attValueHandle;



      /** Returns true if invalid standalone attribute definition. */
      boolean invalidStandaloneAttDef(QName element, QName attribute) {
         if (fStandaloneReader == -1) {
            return false;
         }
         if (element.rawname == -1) {
            return false;
         }
         return getAttDefIsExternal(element, attribute);
      }


   /**
    * AttValidatorENUMERATION.
    */
   final class AttValidatorENUMERATION
   implements AttributeValidator {


      /** Normalize. */
      public int normalize(QName element, QName attribute,
                           int attValueHandle, int attType,
                           int enumHandle) throws Exception {
         String attValue = fStringPool.toString(attValueHandle);
         String newAttValue = attValue.trim();
         if (fValidating) {
            if (newAttValue != attValue) {
               if (invalidStandaloneAttDef(element, attribute)) {
                  reportRecoverableXMLError(XMLMessages.MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE,
                                            XMLMessages.VC_STANDALONE_DOCUMENT_DECLARATION,
                                            fStringPool.toString(attribute.rawname), attValue, newAttValue);
               }
               attValueHandle = fStringPool.addSymbol(newAttValue);
            } else {
               attValueHandle = fStringPool.addSymbol(attValueHandle);
            }
            if (!fStringPool.stringInList(enumHandle, attValueHandle)) {
               reportRecoverableXMLError(XMLMessages.MSG_ATTRIBUTE_VALUE_NOT_IN_LIST,
                                         XMLMessages.VC_ENUMERATION,
                                         fStringPool.toString(attribute.rawname),
                                         newAttValue, fStringPool.stringListAsString(enumHandle));
            }
         } else if (newAttValue != attValue) {
            attValueHandle = fStringPool.addSymbol(newAttValue);
         }
         return attValueHandle;



      /** Returns true if invalid standalone attribute definition. */
      boolean invalidStandaloneAttDef(QName element, QName attribute) {
         if (fStandaloneReader == -1) {
            return false;
         }
         if (element.rawname == -1) {
            return false;
         }
         return getAttDefIsExternal(element, attribute);
      }



    /**
     * Stack of XPath matchers for identity constraints.
     *
     * @author Andy Clark, IBM
     */
    protected static class XPathMatcherStack {


        /** Active matchers. */
        protected XPathMatcher[] fMatchers = new XPathMatcher[4];

        /** Count of active matchers. */
        protected int fMatchersCount;

        /** Offset stack for contexts. */
        protected IntStack fContextStack = new IntStack();


        public XPathMatcherStack() {


        /** Resets the XPath matcher stack. */
        public void clear() {
            for (int i = 0; i < fMatchersCount; i++) {
                fMatchers[i] = null;
            }
            fMatchersCount = 0;
            fContextStack.clear();

        /** Returns the size of the stack. */
        public int size() {
            return fContextStack.size();

        /** Returns the count of XPath matchers. */
        public int getMatcherCount() {
            return fMatchersCount;

        /** Adds a matcher. */
        public void addMatcher(XPathMatcher matcher) {
            ensureMatcherCapacity();
            fMatchers[fMatchersCount++] = matcher;

        /** Returns the XPath matcher at the specified index. */
        public XPathMatcher getMatcherAt(int index) {
            return fMatchers[index];

        /** Pushes a new context onto the stack. */
        public void pushContext() {
            fContextStack.push(fMatchersCount);

        /** Pops a context off of the stack. */
        public void popContext() {
            fMatchersCount = fContextStack.pop();


        /** Ensures the size of the matchers array. */
        private void ensureMatcherCapacity() {
            if (fMatchersCount == fMatchers.length) {
                XPathMatcher[] array = new XPathMatcher[fMatchers.length * 2];
                System.arraycopy(fMatchers, 0, array, 0, fMatchers.length);
                fMatchers = array;
            }



    /**
     * Value store implementation base class. There are specific subclasses
     * for handling unique, key, and keyref.
     *
     * @author Andy Clark, IBM
     */
    protected abstract class ValueStoreBase
        implements ValueStore {


        /** Not a value (Unicode: #FFFF). */
        protected IDValue NOT_AN_IDVALUE = new IDValue("\uFFFF", null);


        /** Identity constraint. */
        protected IdentityConstraint fIdentityConstraint;

        /** Current data values. */
        protected final OrderedHashtable fValues = new OrderedHashtable();

        /** Current data value count. */
        protected int fValuesCount;

        /** Data value tuples. */
        protected final Vector fValueTuples = new Vector();


        /** Constructs a value store for the specified identity constraint. */
        protected ValueStoreBase(IdentityConstraint identityConstraint) {
            fIdentityConstraint = identityConstraint;


        public void destroy() {
            fValuesCount = 0;
            fValues.clear();
            fValueTuples.removeAllElements();

        public void append(ValueStoreBase newVal) {
            for (int i = 0; i < newVal.fValueTuples.size(); i++) {
                OrderedHashtable o = (OrderedHashtable)newVal.fValueTuples.elementAt(i);
                if (!contains(o))
                    fValueTuples.addElement(o);
            }

        /** Start scope for value store. */
        public void startValueScope() throws Exception {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#startValueScope()");
            }
            fValuesCount = 0;
            int count = fIdentityConstraint.getFieldCount();
            for (int i = 0; i < count; i++) {
                fValues.put(fIdentityConstraint.getFieldAt(i), NOT_AN_IDVALUE);
            }

        /** Ends scope for value store. */
        public void endValueScope() throws Exception {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#endValueScope()");
            }

            if (fValuesCount == 0) {
                if(fIdentityConstraint.getType() == IdentityConstraint.KEY) {
                    int code = SchemaMessageProvider.AbsentKeyValue;
                    String eName = fIdentityConstraint.getElementName();
                    reportSchemaError(code, new Object[]{eName});
                }
                return;
            }

            if (fValuesCount != fIdentityConstraint.getFieldCount()) {
                switch (fIdentityConstraint.getType()) {
                    case IdentityConstraint.UNIQUE: {
                        int code = SchemaMessageProvider.UniqueNotEnoughValues;
                        String ename = fIdentityConstraint.getElementName();
                        reportSchemaError(code, new Object[]{ename});
                        break;
                    }
                    case IdentityConstraint.KEY: {
                        int code = SchemaMessageProvider.KeyNotEnoughValues;
                        Key key = (Key)fIdentityConstraint;
                        String ename = fIdentityConstraint.getElementName();
                        String kname = key.getIdentityConstraintName();
                        reportSchemaError(code, new Object[]{ename,kname});
                        break;
                    }
                    case IdentityConstraint.KEYREF: {
                        int code = SchemaMessageProvider.KeyRefNotEnoughValues;
                        KeyRef keyref = (KeyRef)fIdentityConstraint;
                        String ename = fIdentityConstraint.getElementName();
                        String kname = (keyref.getKey()).getIdentityConstraintName();
                        reportSchemaError(code, new Object[]{ename,kname});
                        break;
                    }
                }
                return;
            }



        public void endDocumentFragment() throws Exception {

        /**
         * Signals the end of the document. This is where the specific
         * instances of value stores can verify the integrity of the
         * identity constraints.
         */
        public void endDocument() throws Exception {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#endDocument()");
            }


        /* reports an error if an element is matched
         * has nillable true and is matched by a key.
         */

        public void reportNilError(IdentityConstraint id) throws Exception {
            if(id.getType() == IdentityConstraint.KEY) {
                int code = SchemaMessageProvider.KeyMatchesNillable;
                reportSchemaError(code, new Object[]{id.getElementName()});
            }

        /**
         * Adds the specified value to the value store.
         *
         * @param value The value to add.
         * @param field The field associated to the value. This reference
         *              is used to ensure that each field only adds a value
         *              once within a selection scope.
         */
        public void addValue(Field field, IDValue value) throws Exception {
            if(!field.mayMatch()) {
                int code = SchemaMessageProvider.FieldMultipleMatch;
                reportSchemaError(code, new Object[]{field.toString()});
            }
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#addValue("+
                                   "field="+field+','+
                                   "value="+value+
                                   ")");
            }

            int index = fValues.indexOf(field);
            if (index == -1) {
                int code = SchemaMessageProvider.UnknownField;
                reportSchemaError(code, new Object[]{field.toString()});
                return;
            }

            IDValue storedValue = fValues.valueAt(index);
            if (storedValue.isDuplicateOf(NOT_AN_IDVALUE)) {
                fValuesCount++;
            }
            fValues.put(field, value);

            if (fValuesCount == fValues.size()) {
                if (contains(fValues)) {
                    duplicateValue(fValues);
                }

                OrderedHashtable values = (OrderedHashtable)fValues.clone();
                fValueTuples.addElement(values);
            }


        /**
         * Returns true if this value store contains the specified
         * values tuple.
         */
        public boolean contains(OrderedHashtable tuple) {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+this.toString()+"#contains("+toString(tuple)+")");
            }

            int tcount = tuple.size();

            int count = fValueTuples.size();
            LOOP: for (int i = 0; i < count; i++) {
                OrderedHashtable vtuple = (OrderedHashtable)fValueTuples.elementAt(i);
                for (int j = 0; j < tcount; j++) {
                    IDValue value1 = vtuple.valueAt(j);
                    IDValue value2 = tuple.valueAt(j);
                    if(!(value1.isDuplicateOf(value2))) {
                        continue LOOP;
                    }
                }

                return true;
            }

            return false;



        /**
         * Called when a duplicate value is added. Subclasses should override
         * this method to perform error checking.
         *
         * @param tuple The duplicate value tuple.
         */
        protected void duplicateValue(OrderedHashtable tuple)
            throws Exception {

        /** Returns a string of the specified values. */
        protected String toString(OrderedHashtable tuple) {

            int size = tuple.size();
            if (size == 0) {
                return "";
            }

            StringBuffer str = new StringBuffer();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    str.append(',');
                }
                str.append(tuple.valueAt(i));
            }
            return str.toString();



        /** Returns a string representation of this object. */
        public String toString() {
            String s = super.toString();
            int index1 = s.lastIndexOf('$');
            if (index1 != -1) {
                s = s.substring(index1 + 1);
            }
            int index2 = s.lastIndexOf('.');
            if (index2 != -1) {
                s = s.substring(index2 + 1);
            }
            return s + '[' + fIdentityConstraint + ']';


    /**
     * Unique value store.
     *
     * @author Andy Clark, IBM
     */
    protected class UniqueValueStore
        extends ValueStoreBase {


        /** Constructs a unique value store. */
        public UniqueValueStore(Unique unique) {
            super(unique);


        /**
         * Called when a duplicate value is added.
         *
         * @param tuple The duplicate value tuple.
         */
        protected void duplicateValue(OrderedHashtable tuple)
            throws Exception {
            int code = SchemaMessageProvider.DuplicateUnique;
            String value = toString(tuple);
            String ename = fIdentityConstraint.getElementName();
            reportSchemaError(code, new Object[]{value,ename});


    /**
     * Key value store.
     *
     * @author Andy Clark, IBM
     */
    protected class KeyValueStore
        extends ValueStoreBase {



        /** Constructs a key value store. */
        public KeyValueStore(Key key) {
            super(key);


        /**
         * Called when a duplicate value is added.
         *
         * @param tuple The duplicate value tuple.
         */
        protected void duplicateValue(OrderedHashtable tuple)
            throws Exception {
            int code = SchemaMessageProvider.DuplicateKey;
            String value = toString(tuple);
            String ename = fIdentityConstraint.getElementName();
            reportSchemaError(code, new Object[]{value,ename});


    /**
     * Key reference value store.
     *
     * @author Andy Clark, IBM
     */
    protected class KeyRefValueStore
        extends ValueStoreBase {


        /** Key value store. */
        protected ValueStoreBase fKeyValueStore;


        /** Constructs a key value store. */
        public KeyRefValueStore(KeyRef keyRef, KeyValueStore keyValueStore) {
            super(keyRef);
            fKeyValueStore = keyValueStore;


        public void endDocumentFragment () throws Exception {

            super.endDocumentFragment ();

            fKeyValueStore = (ValueStoreBase)fValueStoreCache.fGlobalIDConstraintMap.get(((KeyRef)fIdentityConstraint).getKey());

            if(fKeyValueStore == null) {
                int code = SchemaMessageProvider.KeyRefOutOfScope;
                String value = fIdentityConstraint.toString();
                reportSchemaError(code, new Object[]{value});
                return;
            }

            int count = fValueTuples.size();
            for (int i = 0; i < count; i++) {
                OrderedHashtable values = (OrderedHashtable)fValueTuples.elementAt(i);
                if (!fKeyValueStore.contains(values)) {
                    int code = SchemaMessageProvider.KeyNotFound;
                    String value = toString(values);
                    String element = fIdentityConstraint.getElementName();
                    reportSchemaError(code, new Object[]{value,element});
                }
            }


        /** End document. */
        public void endDocument() throws Exception {
            super.endDocument();




    /**
     * Value store cache. This class is used to store the values for
     * identity constraints.
     *
     * @author Andy Clark, IBM
     */
    protected class ValueStoreCache {



        /** stores all global Values stores. */
        protected final Vector fValueStores = new Vector();

        /** Values stores associated to specific identity constraints. */
        protected final Hashtable fIdentityConstraint2ValueStoreMap = new Hashtable();

        protected final Stack fGlobalMapStack = new Stack();
        protected final Hashtable fGlobalIDConstraintMap = new Hashtable();


        /** Default constructor. */
        public ValueStoreCache() {


        /** Resets the identity constraint cache. */
        public void startDocument() throws Exception {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#startDocument()");
            }
            fValueStores.removeAllElements();
            fIdentityConstraint2ValueStoreMap.clear();
            fGlobalIDConstraintMap.clear();
            fGlobalMapStack.removeAllElements();

        public void startElement() {
            fGlobalMapStack.push(fGlobalIDConstraintMap.clone());
            fGlobalIDConstraintMap.clear();

        public void endElement() {
            Hashtable oldMap = (Hashtable)fGlobalMapStack.pop();
            Enumeration keys = oldMap.keys();
            while(keys.hasMoreElements()) {
                IdentityConstraint id = (IdentityConstraint)keys.nextElement();
                ValueStoreBase oldVal = (ValueStoreBase)oldMap.get(id);
                if(oldVal != null) {
                    ValueStoreBase currVal = (ValueStoreBase)fGlobalIDConstraintMap.get(id);
                    if (currVal == null)
                        fGlobalIDConstraintMap.put(id, oldVal);
                    else {
                        currVal.append(oldVal);
                        fGlobalIDConstraintMap.put(id, currVal);
                    }
                }
            }

        /**
         * Initializes the value stores for the specified element
         * declaration.
         */
        public void initValueStoresFor(XMLElementDecl eDecl)
            throws Exception {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#initValueStoresFor("+
                                   fStringPool.toString(eDecl.name.rawname)+
                                   ")");
            }

            Vector uVector = eDecl.unique;
            int uCount = uVector.size();
            for (int i = 0; i < uCount; i++) {
                Unique unique = (Unique)uVector.elementAt(i);
                UniqueValueStore valueStore = (UniqueValueStore)fIdentityConstraint2ValueStoreMap.get(unique);
                if (valueStore != null) {
                    continue;
                }
                valueStore = new UniqueValueStore(unique);
                fValueStores.addElement(valueStore);
                if (DEBUG_VALUE_STORES) {
                    System.out.println("<VS>: "+unique+" -> "+valueStore);
                }
                fIdentityConstraint2ValueStoreMap.put(unique, valueStore);
            }

            Vector kVector = eDecl.key;
            int kCount = kVector.size();
            for (int i = 0; i < kCount; i++) {
                Key key = (Key)kVector.elementAt(i);
                KeyValueStore valueStore = (KeyValueStore)fIdentityConstraint2ValueStoreMap.get(key);
                if (valueStore != null) {
                    continue;
                }
                valueStore = new KeyValueStore(key);
                fValueStores.addElement(valueStore);
                if (DEBUG_VALUE_STORES) {
                    System.out.println("<VS>: "+key+" -> "+valueStore);
                }
                fIdentityConstraint2ValueStoreMap.put(key, valueStore);
            }

            Vector krVector = eDecl.keyRef;
            int krCount = krVector.size();
            for (int i = 0; i < krCount; i++) {
                KeyRef keyRef = (KeyRef)krVector.elementAt(i);
                KeyRefValueStore keyRefValueStore = new KeyRefValueStore(keyRef, null);
                fValueStores.addElement(keyRefValueStore);
                if (DEBUG_VALUE_STORES) {
                    System.out.println("<VS>: "+keyRef+" -> "+keyRefValueStore);
                }
                fIdentityConstraint2ValueStoreMap.put(keyRef, keyRefValueStore);
            }


        /** Returns the value store associated to the specified field. */
        public ValueStoreBase getValueStoreFor(Field field) {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#getValueStoreFor("+field+")");
            }
            IdentityConstraint identityConstraint = field.getIdentityConstraint();
            return (ValueStoreBase)fIdentityConstraint2ValueStoreMap.get(identityConstraint);

        /** Returns the value store associated to the specified IdentityConstraint. */
        public ValueStoreBase getValueStoreFor(IdentityConstraint id) {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#getValueStoreFor("+id+")");
            }
            return (ValueStoreBase)fIdentityConstraint2ValueStoreMap.get(id);

        /** Returns the global value store associated to the specified IdentityConstraint. */
        public ValueStoreBase getGlobalValueStoreFor(IdentityConstraint id) {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#getGlobalValueStoreFor("+id+")");
            }
            return (ValueStoreBase)fGlobalIDConstraintMap.get(id);
        public void transplant(IdentityConstraint id) throws Exception {
            if (id.getType() == IdentityConstraint.KEYREF ) return;
            ValueStoreBase newVals = (ValueStoreBase)fIdentityConstraint2ValueStoreMap.get(id);
            fIdentityConstraint2ValueStoreMap.remove(id);
            ValueStoreBase currVals = (ValueStoreBase)fGlobalIDConstraintMap.get(id);
            if (currVals != null) {
                currVals.append(newVals);
                fGlobalIDConstraintMap.put(id, currVals);
            } else
                fGlobalIDConstraintMap.put(id, newVals);


        /** Check identity constraints. */
        public void endDocument() throws Exception {
            if (DEBUG_VALUE_STORES) {
                System.out.println("<VS>: "+toString()+"#endDocument()");
            }

            int count = fValueStores.size();
            for (int i = 0; i < count; i++) {
                ValueStoreBase valueStore = (ValueStoreBase)fValueStores.elementAt(i);
                valueStore.endDocument();
            }



        /** Returns a string representation of this object. */
        public String toString() {
            String s = super.toString();
            int index1 = s.lastIndexOf('$');
            if (index1 != -1) {
                return s.substring(index1 + 1);
            }
            int index2 = s.lastIndexOf('.');
            if (index2 != -1) {
                return s.substring(index2 + 1);
            }
            return s;



    /**
     * Ordered hashtable. This class acts as a hashtable with
     * <code>put()</code> and <code>get()</code> operations but also
     * allows values to be queried via the order that they were
     * added to the hashtable.
     * <p>
     * <strong>Note:</strong> This class does not perform any error
     * checking.
     * <p>
     * <strong>Note:</strong> This class is <em>not</em> efficient but
     * is assumed to be used for a very small set of values.
     *
     * @author Andy Clark, IBM
     */
    static final class OrderedHashtable
        implements Cloneable {


        /** Size. */
        private int fSize;

        /** Hashtable entries. */
        private Entry[] fEntries = null;


        /** Returns the number of entries in the hashtable. */
        public int size() {
            return fSize;

        /** Puts an entry into the hashtable. */
        public void put(Field key, IDValue value) {
            int index = indexOf(key);
            if (index == -1) {
                ensureCapacity(fSize);
                index = fSize++;
                fEntries[index].key = key;
            }
            fEntries[index].value = value;

        /** Returns the value associated to the specified key. */
        public IDValue get(Field key) {
            return fEntries[indexOf(key)].value;

        /** Returns the index of the entry with the specified key. */
        public int indexOf(Field key) {
            for (int i = 0; i < fSize; i++) {
                if (fEntries[i].key == key) {
                    return i;
                }
            }
            return -1;

        /** Returns the key at the specified index. */
        public Field keyAt(int index) {
            return fEntries[index].key;

        /** Returns the value at the specified index. */
        public IDValue valueAt(int index) {
            return fEntries[index].value;

        /** Removes all of the entries from the hashtable. */
        public void clear() {
            fSize = 0;


        /** Ensures the capacity of the entries array. */
        private void ensureCapacity(int size) {

            int osize = -1;
            int nsize = -1;

            if (fEntries == null) {
                osize = 0;
                nsize = 2;
                fEntries = new Entry[nsize];
            }

            else if (fEntries.length <= size) {
                osize = fEntries.length;
                nsize = 2 * osize;
                Entry[] array = new Entry[nsize];
                System.arraycopy(fEntries, 0, array, 0, osize);
                fEntries = array;
            }

            for (int i = osize; i < nsize; i++) {
                fEntries[i] = new Entry();
            }



        /** Clones this object. */
        public Object clone() {

            OrderedHashtable hashtable = new OrderedHashtable();
            for (int i = 0; i < fSize; i++) {
                hashtable.put(fEntries[i].key, fEntries[i].value);
            }
            return hashtable;



        /** Returns a string representation of this object. */
        public String toString() {
            if (fSize == 0) {
                return "[]";
            }
            StringBuffer str = new StringBuffer();
            str.append('[');
            for (int i = 0; i < fSize; i++) {
                if (i > 0) {
                    str.append(',');
                }
                str.append('{');
                str.append(fEntries[i].key);
                str.append(',');
                str.append(fEntries[i].value);
                str.append('}');
            }
            str.append(']');
            return str.toString();


        /**
         * Hashtable entry.
         */
        public static final class Entry {


            /** Key. */
            public Field key;

            /** Value. */
            public IDValue value;



