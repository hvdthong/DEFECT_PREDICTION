package org.apache.xalan.res;


/**
 * Set up error messages.
 * We build a two dimensional array of message keys and
 * message strings. In order to add a new message here,
 * you need to first add a String constant. And
 *  you need to enter key , value pair as part of contents
 * Array. You also need to update MAX_CODE for error strings
 * and MAX_WARNING for warnings ( Needed for only information
 * purpose )
 */
public class XSLTErrorResources_sv extends XSLTErrorResources
{

  /** Maximum error messages, this is needed to keep track of the number of messages.    */
  public static final int MAX_CODE = 201;          

  /** Maximum warnings, this is needed to keep track of the number of warnings.          */
  public static final int MAX_WARNING = 29;

  /** Maximum misc strings.   */
  public static final int MAX_OTHERS = 55;

  /** Maximum total warnings and error messages.          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;

  /** Get the lookup table for error messages.   
   *
   * @return The int to message lookup table.
   */
  public Object[][] getContents()
  {
    return new Object[][] {

  /** Error message ID that has a null message, but takes in a single object.    */


  {
    "ERROR0000", "{0}"},


  /** ER_NO_CURLYBRACE          */


  {
    ER_NO_CURLYBRACE,
      "Fel: Kan inte ha '{' inuti uttryck"},


  /** ER_ILLEGAL_ATTRIBUTE          */


  {
    ER_ILLEGAL_ATTRIBUTE, "{0} har ett otill\u00e5tet attribut: {1}"},


  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */


  {
    ER_NULL_SOURCENODE_APPLYIMPORTS,
      "sourceNode \u00e4r null i xsl:apply-imports!"},


  /** ER_CANNOT_ADD          */


  {
    ER_CANNOT_ADD, "Kan inte l\u00e4gga {0} till {1}"},


  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */


  {
    ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "sourceNode \u00e4r null i handleApplyTemplatesInstruction!"},


  /** ER_NO_NAME_ATTRIB          */


  {
    ER_NO_NAME_ATTRIB, "{0} m\u00e5ste ha ett namn-attribut."},


  /** ER_TEMPLATE_NOT_FOUND          */


  {
    ER_TEMPLATE_NOT_FOUND, "Hittade inte mallen med namn: {0}"},


  /** ER_CANT_RESOLVE_NAME_AVT          */


  {
    ER_CANT_RESOLVE_NAME_AVT,
      "Kunde inte l\u00f6sa namn-AVT i xsl:call-template."},


  /** ER_REQUIRES_ATTRIB          */


  {
    ER_REQUIRES_ATTRIB, "{0} kr\u00e4ver attribut: {1}"},


  /** ER_MUST_HAVE_TEST_ATTRIB          */


  {
    ER_MUST_HAVE_TEST_ATTRIB,
      "{0} m\u00e5ste ha ett ''test''-attribut."},


  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */


  {
    ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "D\u00e5ligt v\u00e4rde p\u00e5 niv\u00e5-attribut: {0}"},


  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */


  {
    ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "Namn p\u00e5 behandlande instruktion f\u00e5r inte vara 'xml'"},


  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */


  {
    ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "Namn p\u00e5 behandlande instruktion m\u00e5ste vara ett giltigt NCNamn: {0}"},


  /** ER_NEED_MATCH_ATTRIB          */


  {
    ER_NEED_MATCH_ATTRIB,
      "{0} m\u00e5ste ha ett matchningsattribut om det har ett tillst\u00e5nd."},


  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */


  {
    ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} kr\u00e4ver antingen ett namn eller ett matchningsattribut."},


  /** ER_CANT_RESOLVE_NSPREFIX          */


  {
    ER_CANT_RESOLVE_NSPREFIX,
      "Kan inte l\u00f6sa namnrymdsprefix: {0}"},


  /** ER_ILLEGAL_VALUE          */


  {
    ER_ILLEGAL_VALUE, "xml:space har ett otill\u00e5tet v\u00e4rde: {0}"},


  /** ER_NO_OWNERDOC          */


  {
    ER_NO_OWNERDOC,
      "Barnnod saknar \u00e4gardokument!"},


  /** ER_ELEMTEMPLATEELEM_ERR          */


  {
    ER_ELEMTEMPLATEELEM_ERR, "ElemTemplateElement-fel: {0}"},


  /** ER_NULL_CHILD          */


  {
    ER_NULL_CHILD, "F\u00f6rs\u00f6ker l\u00e4gga till ett null-barn!"},


  /** ER_NEED_SELECT_ATTRIB          */


  {
    ER_NEED_SELECT_ATTRIB, "{0} kr\u00e4ver ett valattribut."},


  /** ER_NEED_TEST_ATTRIB          */


  {
    ER_NEED_TEST_ATTRIB,
      "xsl:when m\u00e5ste ha ett 'test'-attribut."},


  /** ER_NEED_NAME_ATTRIB          */


  {
    ER_NEED_NAME_ATTRIB,
      "xsl:with-param m\u00e5ste ha ett 'namn'-attribut."},


  /** ER_NO_CONTEXT_OWNERDOC          */


  {
    ER_NO_CONTEXT_OWNERDOC,
      "Kontext saknar \u00e4gardokument!"},


  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */


  {
    ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "Kunde inte skapa XML TransformerFactory Liaison: {0}"},


  /** ER_PROCESS_NOT_SUCCESSFUL          */


  {
    ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan: Process misslyckades."},


  /** ER_NOT_SUCCESSFUL          */


  {
    ER_NOT_SUCCESSFUL, "Xalan: misslyckades."},


  /** ER_ENCODING_NOT_SUPPORTED          */


  {
    ER_ENCODING_NOT_SUPPORTED, "Kodning inte underst\u00f6dd: {0}"},


  /** ER_COULD_NOT_CREATE_TRACELISTENER          */


  {
    ER_COULD_NOT_CREATE_TRACELISTENER,
      "Kunde inte skapa TraceListener: {0}"},


  /** ER_KEY_REQUIRES_NAME_ATTRIB          */


  {
    ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key m\u00e5ste ha ett 'namn'-attribut."},


  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


  {
    ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key m\u00e5ste ha ett 'matcha'-attribut."},


  /** ER_KEY_REQUIRES_USE_ATTRIB          */


  {
    ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key m\u00e5ste ha ett 'anv\u00e4nd'-attribut."},


  /** ER_REQUIRES_ELEMENTS_ATTRIB          */


  {
    ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} kr\u00e4ver ett ''element''-attribut!"},


  /** ER_MISSING_PREFIX_ATTRIB          */


  {
    ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) {0} ''prefix''-attribut saknas"},


  /** ER_BAD_STYLESHEET_URL          */


  {
    ER_BAD_STYLESHEET_URL, "Stylesheet URL \u00e4r d\u00e5lig: {0}"},


  /** ER_FILE_NOT_FOUND          */


  {
    ER_FILE_NOT_FOUND, "Stylesheet-fil saknas: {0}"},


  /** ER_IOEXCEPTION          */


  {
    ER_IOEXCEPTION,
      "Fick IO-Undantag med stylesheet-fil: {0}"},


  /** ER_NO_HREF_ATTRIB          */


  {
    ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) Hittade inte href-attribute f\u00f6r {0}"},


  /** ER_STYLESHEET_INCLUDES_ITSELF          */


  {
    ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} inkluderar, direkt eller indirekt, sig sj\u00e4lv!"},


  /** ER_PROCESSINCLUDE_ERROR          */


  {
    ER_PROCESSINCLUDE_ERROR,
      "StylesheetHandler.processInclude-fel, {0}"},


  /** ER_MISSING_LANG_ATTRIB          */


  {
    ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) {0} ''lang''-attribut' saknas"},


  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */


  {
    ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) felplacerade {0} element?? Saknar beh\u00e5llarelement  ''komponent''"},


  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */


  {
    ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "Kan endast skicka utdata till ett Element, ett DocumentFragment, ett Document, eller en PrintWriter."},


  /** ER_PROCESS_ERROR          */


  {
    ER_PROCESS_ERROR, "StylesheetRoot.process-fel"},


  /** ER_UNIMPLNODE_ERROR          */


  {
    ER_UNIMPLNODE_ERROR, "UnImplNode-fel: {0}"},


  /** ER_NO_SELECT_EXPRESSION          */


  {
    ER_NO_SELECT_EXPRESSION,
      "Fel! Hittade inte xpath select-uttryck (-select)."},


  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */


  {
    ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "Kan inte serialisera en XSLProcessor!"},


  /** ER_NO_INPUT_STYLESHEET          */


  {
    ER_NO_INPUT_STYLESHEET,
      "Stylesheet-indata ej angiven!"},


  /** ER_FAILED_PROCESS_STYLESHEET          */


  {
    ER_FAILED_PROCESS_STYLESHEET,
      "Kunde inte behandla stylesheet!"},


  /** ER_COULDNT_PARSE_DOC          */


  {
    ER_COULDNT_PARSE_DOC, "Kunde inte tolka {0} dokument!"},


  /** ER_COULDNT_FIND_FRAGMENT          */


  {
    ER_COULDNT_FIND_FRAGMENT, "Hittade inte fragment: {0}"},


  /** ER_NODE_NOT_ELEMENT          */


  {
    ER_NODE_NOT_ELEMENT,
      "Nod som pekades p\u00e5 av fragment-identifierare var inte ett element: {0}"},


  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */


  {
    ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each kr\u00e4ver antingen en matchning eller ett namnattribut."},


  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */


  {
    ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "mallar kr\u00e4ver antingen en matchning eller ett namnattribut."},


  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */


  {
    ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "Ingen klon av ett dokumentfragment!"},


  /** ER_CANT_CREATE_ITEM          */


  {
    ER_CANT_CREATE_ITEM,
      "Kan inte skapa element i resultattr\u00e4d: {0}"},


  /** ER_XMLSPACE_ILLEGAL_VALUE          */


  {
    ER_XMLSPACE_ILLEGAL_VALUE,
      "xml:space i k\u00e4ll-XML har ett otill\u00e5tet v\u00e4rde: {0}"},


  /** ER_NO_XSLKEY_DECLARATION          */


  {
    ER_NO_XSLKEY_DECLARATION,
      "Det finns ingen xsl:key-deklaration f\u00f6r {0}!"},


  /** ER_CANT_CREATE_URL          */


  {
    ER_CANT_CREATE_URL, "Fel! Kan inte skapa url f\u00f6r: {0}"},


  /** ER_XSLFUNCTIONS_UNSUPPORTED          */


  {
    ER_XSLFUNCTIONS_UNSUPPORTED, "xsl:functions \u00e4r inte underst\u00f6dd"},


  /** ER_PROCESSOR_ERROR          */


  {
    ER_PROCESSOR_ERROR, "XSLT TransformerFactory-Fel"},


  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */


  {
    ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} \u00e4r inte till\u00e5ten inne i ett stylesheet!"},


  /** ER_RESULTNS_NOT_SUPPORTED          */


  {
    ER_RESULTNS_NOT_SUPPORTED,
      "result-ns inte l\u00e4ngre underst\u00f6dd!  Anv\u00e4nd xsl:output ist\u00e4llet."},


  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */


  {
    ER_DEFAULTSPACE_NOT_SUPPORTED,
      "default-space inte l\u00e4ngre underst\u00f6dd!  Anv\u00e4nd xsl:strip-space eller xsl:preserve-space ist\u00e4llet."},


  /** ER_INDENTRESULT_NOT_SUPPORTED          */


  {
    ER_INDENTRESULT_NOT_SUPPORTED,
      "indent-result inte l\u00e4ngre underst\u00f6dd!  Anv\u00e4nd xsl:output ist\u00e4llet."},


  /** ER_ILLEGAL_ATTRIB          */


  {
    ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} har ett otill\u00e5tet attribut: {1}"},


  /** ER_UNKNOWN_XSL_ELEM          */


  {
    ER_UNKNOWN_XSL_ELEM, "Ok\u00e4nt XSL-element: {0}"},


  /** ER_BAD_XSLSORT_USE          */


  {
    ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort kan endast anv\u00e4ndas med xsl:apply-templates eller xsl:for-each."},


  /** ER_MISPLACED_XSLWHEN          */


  {
    ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) felplacerade xsl:when!"},


  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */


  {
    ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:when h\u00e4rstammar inte fr\u00e5n xsl:choose!"},


  /** ER_MISPLACED_XSLOTHERWISE          */


  {
    ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) felplacerade xsl:otherwise!"},


  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */


  {
    ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:otherwise h\u00e4rstammar inte fr\u00e5n xsl:choose!"},


  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */


  {
    ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} \u00e4r inte till\u00e5ten inne i en mall!"},


  /** ER_UNKNOWN_EXT_NS_PREFIX          */


  {
    ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) {0} utbyggnadsnamnrymdsprefix {1} ok\u00e4nt"},


  /** ER_IMPORTS_AS_FIRST_ELEM          */


  {
    ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) Imports kan endast f\u00f6rekomma som de f\u00f6rsta elementen i ett stylesheet!"},


  /** ER_IMPORTING_ITSELF          */


  {
    ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} importerar, direkt eller indirekt, sig sj\u00e4lv!"},


  /** ER_XMLSPACE_ILLEGAL_VAL          */


  {
    ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) " + "xml:space har ett otill\u00e5tet v\u00e4rde: {0}"},


  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */


  {
    ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "processStylesheet misslyckades!"},


  /** ER_SAX_EXCEPTION          */


  {
    ER_SAX_EXCEPTION, "SAX-Undantag"},



  /** ER_XSLT_ERROR          */


  {
    ER_XSLT_ERROR, "XSLT-fel"},


  /** ER_CURRENCY_SIGN_ILLEGAL          */


  {
    ER_CURRENCY_SIGN_ILLEGAL,
      "valutatecken \u00e4r inte till\u00e5tet i formatm\u00f6nsterstr\u00e4ng"},


  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */


  {
    ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "Dokumentfunktion inte underst\u00f6dd i Stylesheet DOM!"},


  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */


  {
    ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "Kan inte l\u00f6sa prefix i icke-Prefixl\u00f6sare!"},


  /** ER_REDIRECT_COULDNT_GET_FILENAME          */


  {
    ER_REDIRECT_COULDNT_GET_FILENAME,
      "Redirect extension: Hittade inte filnamn - fil eller valattribut m\u00e5ste returnera vald  str\u00e4ng."},


  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */


  {
    ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "Kan inte bygga FormatterListener i Redirect extension!"},


  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */


  {
    ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "Prefix i exkludera-resultat-prefix \u00e4r inte giltig: {0}"},


  /** ER_MISSING_NS_URI          */


  {
    ER_MISSING_NS_URI,
      "Namnrymds-URI saknas f\u00f6r angivna prefix"},


  /** ER_MISSING_ARG_FOR_OPTION          */


  {
    ER_MISSING_ARG_FOR_OPTION,
      "Argument saknas f\u00f6r alternativ: {0}"},


  /** ER_INVALID_OPTION          */


  {
    ER_INVALID_OPTION, "Ogiltigt alternativ: {0}"},


  /** ER_MALFORMED_FORMAT_STRING          */


  {
    ER_MALFORMED_FORMAT_STRING, "Fel format p\u00e5 formatstr\u00e4ng: {0}"},


  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */


  {
    ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet m\u00e5ste ha ett 'version'-attribut!"},


  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */


  {
    ER_ILLEGAL_ATTRIBUTE_VALUE,
      "Attribut: {0} har ett otill\u00e5tet v\u00e4rde: {1}"},


  /** ER_CHOOSE_REQUIRES_WHEN          */


  {
    ER_CHOOSE_REQUIRES_WHEN, "xsl:choose kr\u00e4ver ett xsl:when"},


  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */


  {
    ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports inte till\u00e5tet i ett xsl:for-each"},


  /** ER_CANT_USE_DTM_FOR_OUTPUT          */


  {
    ER_CANT_USE_DTM_FOR_OUTPUT,
      "Kan inte anv\u00e4nda DTMLiaison till en DOM utdatanod... skicka en org.apache.xpath.DOM2Helper ist\u00e4llet!"},


  /** ER_CANT_USE_DTM_FOR_INPUT          */


  {
    ER_CANT_USE_DTM_FOR_INPUT,
      "Kan inte anv\u00e4nda DTMLiaison till en DOM indatanod... skicka en org.apache.xpath.DOM2Helper ist\u00e4llet!"},


  /** ER_CALL_TO_EXT_FAILED          */


  {
    ER_CALL_TO_EXT_FAILED,
      "Anrop till anslutningselement misslyckades: {0}"},


  /** ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "Prefix m\u00e5ste l\u00f6sa till en mamnrymd: {0}"},


  /** ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "Ogiltigt UTF-16-surrogat uppt\u00e4ckt: {0} ?"},


  /** ER_XSLATTRSET_USED_ITSELF          */


  {
    ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} anv\u00e4nde sig sj\u00e4lvt, vilket kommer att orsaka en  o\u00e4ndlig loop."},


  /** ER_CANNOT_MIX_XERCESDOM          */


  {
    ER_CANNOT_MIX_XERCESDOM,
      "Kan inte blanda icke-Xerces-DOM-indata med Xerces-DOM-utdata!"},


  /** ER_TOO_MANY_LISTENERS          */


  {
    ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},


  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */


  {
    ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "I ElemTemplateElement.readObject: {0}"},


  /** ER_DUPLICATE_NAMED_TEMPLATE          */


  {
    ER_DUPLICATE_NAMED_TEMPLATE,
      "Hittade mer \u00e4n en mall med namnet: {0}"},


  /** ER_INVALID_KEY_CALL          */


  {
    ER_INVALID_KEY_CALL,
      "Ogiltigt funktionsanrop: rekursiva key()-anrop \u00e4r inte till\u00e5tna"},

  
  /** Variable is referencing itself          */


  {
    ER_REFERENCING_ITSELF,
      "Variabel {0} h\u00e4nvisar, direkt eller indirekt, till sig sj\u00e4lv!"},

  
  /** Illegal DOMSource input          */


  {
    ER_ILLEGAL_DOMSOURCE_INPUT,
      "Indatanoden till en DOMSource f\u00f6r newTemplates f\u00e5r inte vara null!"},

	
	/** Class not found for option         */


  {
    ER_CLASS_NOT_FOUND_FOR_OPTION,
			"Klassfil f\u00f6r alternativ {0} saknas"},

	
	/** Required Element not found         */


  {
    ER_REQUIRED_ELEM_NOT_FOUND,
			"N\u00f6dv\u00e4ndigt element saknas: {0}"},

  
  /** InputStream cannot be null         */


  {
    ER_INPUT_CANNOT_BE_NULL,
			"InputStream f\u00e5r inte vara null"},

  
  /** URI cannot be null         */


  {
    ER_URI_CANNOT_BE_NULL,
			"URI f\u00e5r inte vara null"},

  
  /** File cannot be null         */


  {
    ER_FILE_CANNOT_BE_NULL,
			"Fil f\u00e5r inte vara null"},

  
   /** InputSource cannot be null         */


  {
    ER_SOURCE_CANNOT_BE_NULL,
			"InputSource f\u00e5r inte vara null"},

  
  /** Could not initialize BSF Manager        */


  {
    ER_CANNOT_INIT_BSFMGR,
			"Kan inte initialisera BSF Manager"},

  
  /** Could not compile extension       */


  {
    ER_CANNOT_CMPL_EXTENSN,
			"Kunde inte kompilera anslutning"},

  
  /** Could not create extension       */


  {
    ER_CANNOT_CREATE_EXTENSN,
      "Kunde inte skapa anslutning: {0} p\u00e5 grund av: {1}"},

  
  /** Instance method call to method {0} requires an Object instance as first argument       */


  {
    ER_INSTANCE_MTHD_CALL_REQUIRES,
      "Instansmetodanrop till metod {0} kr\u00e4ver en Objektinstans som f\u00f6rsta argument"},

  
  /** Invalid element name specified       */


  {
    ER_INVALID_ELEMENT_NAME,
      "Ogiltigt elementnamn angivet {0}"},

  
   /** Element name method must be static      */


  {
    ER_ELEMENT_NAME_METHOD_STATIC,
      "Elementnamnmetod m\u00e5ste vara static {0}"},

  
   /** Extension function {0} : {1} is unknown      */


  {
    ER_EXTENSION_FUNC_UNKNOWN,
             "Anslutningsfunktion {0} : {1} \u00e4r ok\u00e4nd"},

  
   /** More than one best match for constructor for       */


  {
    ER_MORE_MATCH_CONSTRUCTOR,
             "Fler \u00e4n en b\u00e4sta matchning f\u00f6r konstruktor f\u00f6r {0}"},

  
   /** More than one best match for method      */


  {
    ER_MORE_MATCH_METHOD,
             "Fler \u00e4n en b\u00e4sta matchning f\u00f6r metod {0}"},

  
   /** More than one best match for element method      */


  {
    ER_MORE_MATCH_ELEMENT,
             "Fler \u00e4n en b\u00e4sta matchning f\u00f6r elementmetod {0}"},

  
   /** Invalid context passed to evaluate       */


  {
    ER_INVALID_CONTEXT_PASSED,
             "Ogiltig kontext skickad f\u00f6r att utv\u00e4rdera {0}"},

  
   /** Pool already exists       */


  {
    ER_POOL_EXISTS,
             "Pool finns redan"},

  
   /** No driver Name specified      */


  {
    ER_NO_DRIVER_NAME,
             "Inget driver-namn angivet"},

  
   /** No URL specified     */


  {
    ER_NO_URL,
             "Ingen URL angiven"},

  
   /** Pool size is less than one    */


  {
    ER_POOL_SIZE_LESSTHAN_ONE,
             "Poolstorlek \u00e4r mindre \u00e4n ett!"},

  
   /** Invalid driver name specified    */


  {
    ER_INVALID_DRIVER,
             "Ogiltigt driver-namn angivet"},

  
   /** Did not find the stylesheet root    */


  {
    ER_NO_STYLESHEETROOT,
             "Hittade inte stylesheet-roten!"},

  
   /** Illegal value for xml:space     */


  {
    ER_ILLEGAL_XMLSPACE_VALUE,
         "Ogiltigt v\u00e4rde f\u00f6r xml:space"},

  
   /** processFromNode failed     */


  {
    ER_PROCESSFROMNODE_FAILED,
         "processFromNode misslyckades"},

  
   /** The resource [] could not load:     */


  {
    ER_RESOURCE_COULD_NOT_LOAD,
        "Resursen [ {0} ] kunde inte laddas: {1} \n {2} \t {3}"},

   
  
   /** Buffer size <=0     */


  {
    ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "Bufferstorlek <=0"},

  
   /** Unknown error when calling extension    */


  {
    ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "Ok\u00e4nt fel vid anslutningsanrop"},

  
   /** Prefix {0} does not have a corresponding namespace declaration    */


  {
    ER_NO_NAMESPACE_DECL,
        "Prefix{0} har inte en motsvarande namnrymdsdeklaration"},

  
   /** Element content not allowed for lang=javaclass   */


  {
    ER_ELEM_CONTENT_NOT_ALLOWED,
        "Elementinneh\u00e5ll \u00e4r inte till\u00e5tet f\u00f6r lang=javaclass {0}"},

  
   /** Stylesheet directed termination   */


  {
    ER_STYLESHEET_DIRECTED_TERMINATION,
        "Stylesheet-ledd avslutning"},

  
   /** 1 or 2   */


  {
    ER_ONE_OR_TWO,
        "1 eller 2"},

  
   /** 2 or 3   */


  {
    ER_TWO_OR_THREE,
        "2 eller 3"},

  
   /** Could not load {0} (check CLASSPATH), now using just the defaults   */


  {
    ER_COULD_NOT_LOAD_RESOURCE,
        "Kunde inte ladda {0} (kontrollera CLASSPATH), anv\u00e4nder nu enbart standard"},

  
   /** Cannot initialize default templates   */


  {
    ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "Kan inte initialisera standardmallar"},

  
   /** Result should not be null   */


  {
    ER_RESULT_NULL,
        "Result borde inte vara null"},

    
   /** Result could not be set   */


  {
    ER_RESULT_COULD_NOT_BE_SET,
        "Result kunde inte s\u00e4ttas"},

  
   /** No output specified   */


  {
    ER_NO_OUTPUT_SPECIFIED,
        "Ingen utdata angiven"},

  
   /** Can't transform to a Result of type   */


  {
    ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "Kan inte omvandla till en Result av typ {0}"},

  
   /** Can't transform to a Source of type   */


  {
    ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "Kan inte omvandla en Source av typ {0}"},

  
   /** Null content handler  */


  {
    ER_NULL_CONTENT_HANDLER,
        "Inneh\u00e5llshanterare med v\u00e4rde null"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "Felhanterare med v\u00e4rde null"},

  
   /** parse can not be called if the ContentHandler has not been set */


  {
    ER_CANNOT_CALL_PARSE,
        "parse kan inte anropas om ContentHandler inte har satts"},

  
   /**  No parent for filter */


  {
    ER_NO_PARENT_FOR_FILTER,
        "Ingen f\u00f6r\u00e4lder till filter"},

  
  
   /**  No stylesheet found in: {0}, media */


  {
    ER_NO_STYLESHEET_IN_MEDIA,
         "Stylesheet saknas i: {0}, media= {1}"},

  
   /**  No xml-stylesheet PI found in */


  {
    ER_NO_STYLESHEET_PI,
         "xml-stylesheet PI saknas i: {0}"},

  
   /**  Not supported  */


  {
    ER_NOT_SUPPORTED,
       "Underst\u00f6ds inte: {0}"},

  
   /**  Value for property {0} should be a Boolean instance  */


  {
    ER_PROPERTY_VALUE_BOOLEAN,
       "V\u00e4rde p\u00e5 egenskap {0} borde vara en Boolesk instans"},

  
   /* This key/message changed ,NEED ER_COULD_NOT_FIND_EXTERN_SCRIPT: Pending,Ramesh */

   /** src attribute not yet supported for  */


  {
    "ER_SRC_ATTRIB_NOT_SUPPORTED",
       "src-attributet underst\u00f6ds \u00e4nnu inte f\u00f6r {0}"},

  
  /** The resource [] could not be found     */


  {
    ER_RESOURCE_COULD_NOT_FIND,
        "Resursen [ {0} ] saknas. \n {1}"},

  
   /** output property not recognized:  */


  {
    ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "Utdata-egenskap k\u00e4nns inte igen: {0}"},

  
   /** Failed creating ElemLiteralResult instance   */


  {
    ER_FAILED_CREATING_ELEMLITRSLT,
        "Kunde inte skapa instans av ElemLiteralResult"},
    
  

   /** Priority value does not contain a parsable number   */


  {
     ER_VALUE_SHOULD_BE_NUMBER,
         "V\u00e4rdet f\u00f6r {0} b\u00f6r inneh\u00e5lla en siffra som inte kan tolkas"},

  
   /**  Value for {0} should equal 'yes' or 'no'   */


  {
    ER_VALUE_SHOULD_EQUAL,
        "V\u00e4rde p\u00e5 {0} borde motsvara ja eller nej"},

 
   /**  Failed calling {0} method   */


  {
    ER_FAILED_CALLING_METHOD,
        " Kunde inte anropa metoden {0}"},

  
   /** Failed creating ElemLiteralResult instance   */


  {
    ER_FAILED_CREATING_ELEMTMPL,
        "Kunde inte skapa instans av ElemTemplateElement"},

  
   /**  Characters are not allowed at this point in the document   */


  {
    ER_CHARS_NOT_ALLOWED,
        "Tecken \u00e4r inte till\u00e5tna i dokumentet vid den h\u00e4r tidpunkten"},

  
  /**  attribute is not allowed on the element   */


  {
    ER_ATTR_NOT_ALLOWED,
        "Attributet \"{0}\" \u00e4r inte till\u00e5ten i det {1} elementet!"},

 
  /**  Bad value    */


  {
    ER_BAD_VALUE,
     "{0} d\u00e5ligt v\u00e4rde {1} "},

  
  /**  attribute value not found   */


  {
    ER_ATTRIB_VALUE_NOT_FOUND,
     "Attributet {0} saknas "},

  
  /**  attribute value not recognized    */


  {
    ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "Attributv\u00e4rdet {0} k\u00e4nns inte igen "},

  
  /** Attempting to generate a namespace prefix with a null URI   */


  {
    ER_NULL_URI_NAMESPACE,
     "F\u00f6rs\u00f6ker generera ett namnomr\u00e5desprefix med en null-URI"},



  /** Attempting to generate a namespace prefix with a null URI   */


  {
    ER_NUMBER_TOO_BIG,
     "F\u00f6rs\u00f6ker formatera en siffra som \u00e4r st\u00f6rre \u00e4n det st\u00f6rsta l\u00e5nga heltalet"},





  {
    ER_CANNOT_FIND_SAX1_DRIVER,
     "Det g\u00e5r inte att hitta SAX1-drivrutinen klass {0}"},




  {
    ER_SAX1_DRIVER_NOT_LOADED,
     "SAX1-drivrutinen klass {0} hittades men kan inte laddas"},




  {
    ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "SAX1-drivrutinen klass {0} hittades men kan inte instansieras"},





  {
    ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "SAX1-drivrutinen klass {0} implementerar inte org.xml.sax.Parser"},




  {
    ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "Systemegenskapen org.xml.sax.parser \u00e4r inte angiven"},




  {
    ER_PARSER_ARG_CANNOT_BE_NULL,
     "Tolkningsargumentet f\u00e5r inte vara null"},





  {
    ER_FEATURE,
     "Funktion:a {0}"},





  {
    ER_PROPERTY,
     "Egenskap:a {0}"},




  {
    ER_NULL_ENTITY_RESOLVER,
     "Nullenhetsl\u00f6sare"},




  {
    ER_NULL_DTD_HANDLER,
     "Null-DTD-hanterare"},



  {
    ER_NO_DRIVER_NAME_SPECIFIED,
     "Inget drivrutinsnamn \u00e4r angett!"},




  {
    ER_NO_URL_SPECIFIED,
     "Ingen URL har angetts!"},




  {
    ER_POOLSIZE_LESS_THAN_ONE,
     "Poolstorleken \u00e4r mindre \u00e4n 1!"},




  {
    ER_INVALID_DRIVER_NAME,
     "Ett ogiltigt drivrutinsnamn har angetts!"},





  {
    ER_ERRORLISTENER,
     "ErrorListener"},




  {
    ER_ASSERT_NO_TEMPLATE_PARENT,
     "Programmerarfel! expr har inget \u00f6verordnat ElemTemplateElement!"},




  {
    ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "Programmerarkontroll i RundundentExprEliminator: {0}"},



  {
    ER_NOT_ALLOWED_IN_POSITION,
     "{0} \u00e4r inte till\u00e5ten i denna position i formatmallen!"},



  {
    ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "Text utan blanksteg \u00e4r inte till\u00e5ten i denna position i formatmallen!"},



  {
    INVALID_TCHAR,
     "Ogiltigt v\u00e4rde: {1} anv\u00e4nds f\u00f6r CHAR-attributet: {0}.  Ett attribut av CHAR-typ f\u00e5r bara ha 1 tecken!"},




  {
    INVALID_QNAME,
     "Ogiltigt v\u00e4rde:a {1} anv\u00e4nds f\u00f6r QNAME-attributet:a {0}"},




  {
    INVALID_ENUM,
     "Ogiltigt v\u00e4rde:a  {1} anv\u00e4nds f\u00f6r ENUM-attributet:a {0}.  Giltiga v\u00e4rden \u00e4r:a {2}."},




  {
    INVALID_NMTOKEN,
     "Ogiltigt v\u00e4rde:a {1} anv\u00e4nds f\u00f6r NMTOKEN-attributet:a {0} "},




  {
    INVALID_NCNAME,
     "Ogiltigt v\u00e4rde:a {1} anv\u00e4nds f\u00f6r NCNAME-attributet:a {0} "},





  {
    INVALID_BOOLEAN,
     "Ogiltigt v\u00e4rde:a {1} anv\u00e4nds som Booleskt attribut:a {0} "},




  {
    INVALID_NUMBER,
     "Ogiltigt v\u00e4rde:a {1} anv\u00e4nds som sifferattribut:a {0} "},






  {
    ER_ARG_LITERAL,
     "Argument f\u00f6r {0} i matchningsm\u00f6nstret m\u00e5ste vara literalt."},




  {
    ER_DUPLICATE_GLOBAL_VAR,
     "Dubbel deklaration av global variabel."},





  {
    ER_DUPLICATE_VAR,
     "Dubbel variabeldeklaration."},




  {
    ER_TEMPLATE_NAME_MATCH,
     "xsl: en mall m\u00e5ste ha ett namn och ett matchningsattribut (eller b\u00e5de och)"},




  {
    ER_INVALID_PREFIX,
     "Prefix i exclude-result-prefixes \u00e4r ogiltigt:a {0}"},




  {
    ER_NO_ATTRIB_SET,
     "attributserien {0} finns inte"},



  /** WG_FOUND_CURLYBRACE          */


  {
    WG_FOUND_CURLYBRACE,
      "Hittade '}' men ingen attributmall \u00e4r \u00f6ppen!"},


  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */


  {
    WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "Varning: r\u00e4knarattribut matchar inte en f\u00f6rf\u00e4der in xsl:number! Target = {0}"},


  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */


  {
    WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "Gammal syntax: Namnet p\u00e5  'expr'-attributet har \u00e4ndrats till 'select'."},


  /** WG_NO_LOCALE_IN_FORMATNUMBER          */


  {
    WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan hanterar \u00e4nnu inte locale-namnet i funktionen format-number."},


  /** WG_LOCALE_NOT_FOUND          */


  {
    WG_LOCALE_NOT_FOUND,
      "Varning: Hittade inte locale f\u00f6r xml:lang{0}"},


  /** WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "Kan inte skapa URL fr\u00e5n: {0}"},


  /** WG_CANNOT_LOAD_REQUESTED_DOC          */


  {
    WG_CANNOT_LOAD_REQUESTED_DOC,
      "Kan inte ladda beg\u00e4rd doc: {0}"},


  /** WG_CANNOT_FIND_COLLATOR          */


  {
    WG_CANNOT_FIND_COLLATOR,
      "Hittade inte Collator f\u00f6r <sort xml:lang={0}"},


  /** WG_FUNCTIONS_SHOULD_USE_URL          */


  {
    WG_FUNCTIONS_SHOULD_USE_URL,
      "Gammal syntax: Funktionsinstruktionen borde anv\u00e4nda en url av {0}"},


  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */


  {
    WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "kodning underst\u00f6ds inte: {0}, anv\u00e4nder UTF-8"},


  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */


  {
    WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "kodning underst\u00f6ds inte: {0}, anv\u00e4nder Java {1}"},


  /** WG_SPECIFICITY_CONFLICTS          */


  {
    WG_SPECIFICITY_CONFLICTS,
      "Hittade specificitetskonflikter: {0} Senast hittade i stylesheet kommer att anv\u00e4ndas."},


  /** WG_PARSING_AND_PREPARING          */


  {
    WG_PARSING_AND_PREPARING,
      "========= Tolkar och f\u00f6rbereder {0} =========="},


  /** WG_ATTR_TEMPLATE          */


  {
    WG_ATTR_TEMPLATE, "Attributmall, {0}"},


  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */


  {
    WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "Matcha konflikter mellan xsl:strip-space och xsl:preserve-space"},


  /** WG_ATTRIB_NOT_HANDLED          */


  {
    WG_ATTRIB_NOT_HANDLED,
      "Xalan hanterar \u00e4nnu inte attributet {0}!"},


  /** WG_NO_DECIMALFORMAT_DECLARATION          */


  {
    WG_NO_DECIMALFORMAT_DECLARATION,
      "Deklaration saknas f\u00f6r decimalformat: {0}"},


  /** WG_OLD_XSLT_NS          */


  {
    WG_OLD_XSLT_NS, "XSLT-Namnrymd saknas eller \u00e4r inkorrekt "},


  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */


  {
    WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "Endast en standarddeklaration av xsl:decimal-format \u00e4r till\u00e5ten."},


  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */


  {
    WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "xsl:decimal-formatnamn m\u00e5ste vara unika. Namnet \"{0}\" har blivit duplicerat."},


  /** WG_ILLEGAL_ATTRIBUTE          */


  {
    WG_ILLEGAL_ATTRIBUTE,
      "{0} har ett otill\u00e5tet attribut: {1}"},


  /** WG_COULD_NOT_RESOLVE_PREFIX          */


  {
    WG_COULD_NOT_RESOLVE_PREFIX,
      "Kan inte l\u00f6sa namnrymdsprefix: {0}. Noden kommer att ignoreras."},


  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */


  {
    WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet m\u00e5ste ha ett 'version'-attribut!"},


  /** WG_ILLEGAL_ATTRIBUTE_NAME          */


  {
    WG_ILLEGAL_ATTRIBUTE_NAME,
      "Otill\u00e5tet attributnamn: {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */


  {
    WG_ILLEGAL_ATTRIBUTE_VALUE,
      "Ogiltigt v\u00e4rde anv\u00e4nt f\u00f6r attribut {0}: {1}"},


  /** WG_EMPTY_SECOND_ARG          */


  {
    WG_EMPTY_SECOND_ARG,
      "Den resulterande nodm\u00e4ngden fr\u00e5n dokumentfunktions andra argument \u00e4r tomt. Det f\u00f6rsta argumentet kommer att anv\u00e4ndas."},





  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */

  {
     WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "V\u00e4rdet p\u00e5 attributet 'name' i xsl:processing-instruction f\u00e5r inte vara 'xml'"},



  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */

  {
     WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "V\u00e4rdet p\u00e5 attributet  ''name'' i xsl:processing-instruction m\u00e5ste vara ett giltigt NCName:a {0}"},



  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */

  {
    WG_ILLEGAL_ATTRIBUTE_POSITION,
      "Det g\u00e5r inte att l\u00e4gga till attributet {0} efter undernoder eller innan ett element produceras. Attributet ignoreras."},



  { "ui_language", "sv"},
  { "help_language", "sv"},
  { "language", "sv"},
    { "BAD_CODE",
      "Parameter till createMessage ligger utanf\u00f6r till\u00e5tet intervall"},
    { "FORMAT_FAILED",
      "Undantag utl\u00f6st vid messageFormat-anrop"},
    { "version", ">>>>>>> Xalan Version"},
    { "version2", "<<<<<<<"},
    { "yes", "ja"},
    { "line",  "Rad #"},
    { "column", "Kolumn #"},
    { "xsldone", "XSLProcessor: f\u00e4rdig"},
    { "xslProc_option", "Xalan-J kommando linje Process klass alternativ:"},
    { "optionIN", "    -IN inputXMLURL"},
    { "optionXSL", "   [-XSL XSLTransformationURL]"},
    { "optionOUT", "   [-OUT utdataFilnamn]"},
    { "optionLXCIN", "   [-LXCIN kompileratStylesheetFilnameIn]"},
    { "optionLXCOUT", "   [-LXCOUT kompileratStylesheetFilenameUt]"},
    { "optionPARSER", 
      "   [-PARSER fullt kvalificerat klassnamn eller tolkf\u00f6rbindelse]"},
    { "optionE", "   [-E (Ut\u00f6ka inte enhetsreferenser)]"},
    { "optionV", "   [-E (Ut\u00f6ka inte enhetsreferenser)]"},
    { "optionQC",
      "   [-QC (Tysta M\u00f6nsterkonfliktvarningar)]"},
    { "optionQ", "   [-Q  (Tyst Tillst\u00e5nd)]"},
    { "optionLF",
      "   [-LF (Anv\u00e4nd radframmatning enbart p\u00e5 utdata {standard \u00e4r CR/LF})]"},
    { "optionCR",
      "   [-CR (Anv\u00e4nd vagnretur enbart p\u00e5 utdata {standard \u00e4r CR/LF})]"},
    { "optionESCAPE",
      "   [-ESCAPE (Vilka tecken \u00e4r skiftningstecken {standard \u00e4r <>&\"\'\\r\\n}]"},
    { "optionINDENT",
      "   [-INDENT (Best\u00e4m antal blanksteg f\u00f6r att tabulera {standard \u00e4r 0})]"},
    { "optionTT",
      "   [-TT (Sp\u00e5ra mallarna allt eftersom de blir anropade.)]"},
    { "optionTG",
      "   [-TG (Sp\u00e5ra varje generationsh\u00e4ndelse.)]"},
    { "optionTS", "   [-TS (Sp\u00e5ra varje valh\u00e4ndelse.)]"},
    { "optionTTC",
      "   [-TTC (Sp\u00e5ra mallbarnen allt eftersom de blir behandlade.)]"},
    { "optionTCLASS",
      "   [-TCLASS (TraceListener-klass f\u00f6r sp\u00e5rningsanslutningar.)]"},
    { "optionVALIDATE",
      "   [-VALIDATE (S\u00e4tt om validering ska ske.  Standard \u00e4r att validering \u00e4r avst\u00e4ngd)]"},
    { "optionEDUMP",
      "   [-EDUMP {valfritt filnamn) (G\u00f6r stackdump vid fel.)]"},
    { "optionXML",
      "   [-XML (Anv\u00e4nd XML-formaterare och l\u00e4gg till XML-huvud.)]"},
    { "optionTEXT",
      "   [-XML (Anv\u00e4nd enkel Text-formaterare.)]"},
    { "optionHTML", "   [-HTML (Anv\u00e4nd HTML-formaterare)]"},
    { "optionPARAM",
      "   [-PARAM namn uttryck (S\u00e4tt en stylesheet-parameter)]"},
    { "noParsermsg1", "XSL-Process misslyckades."},
    { "noParsermsg2", "** Hittade inte tolk **"},
    { "noParsermsg3", "V\u00e4nligen kontrollera din classpath"},
    { "noParsermsg4",
      "Om du inte har IBMs XML-Tolk f\u00f6r Java, kan du ladda ner den fr\u00e5n"},
    { "noParsermsg5",
    {  "optionURIRESOLVER",
    "   [-URIRESOLVER fullst\u00e4ndigt klassnamn (URIResolver som ska anv\u00e4ndas f\u00f6r att l\u00f6sa URI-er)]"},
    { "optionENTITYRESOLVER",
    "   [-ENTITYRESOLVER fullst\u00e4ndigt klassnamn (EntityResolver som ska anv\u00e4ndas f\u00f6r att l\u00f6sa enheter)]"},
    {  "optionCONTENTHANDLER",
    "   [-CONTENTRESOLVER fullst\u00e4ndigt klassnamn (ContentHandler som ska anv\u00e4ndas f\u00f6r att serialisera utdata)]"},
    { "optionLINENUMBERS", "   [-L anv\u00e4nd radnummer i k\u00e4lldokument]"},



    { "optionMEDIA",
    " [-MEDIA mediaType (anv\u00e4nd medieattribut f\u00f6r att hitta en formatmall som \u00e4r associerad med ett dokument.)]"},
    { "optionFLAVOR",
    { "optionDIAG",
    " [-DIAG (Skriv ut totala transformationer, millisekunder.)]"},
    { "optionINCREMENTAL",
    { "optionNOOPTIMIMIZE",
    { "optionRL",
     " [-RL recursionlimit (kontrollera numerisk gr\u00e4ns p\u00e5 formatmallens rekursionsdjup.)]"},
    { "optionXO",
    " [-XO [transletName] (tilldela namnet till genererad translet)]"},
    { "optionXD",
    " [-XD destinationDirectory (ange m\u00e5lkatalog f\u00f6r translet)]"},
    { "optionXJ",
    " [-XJ jarfile (paketerar transletklasserna i en jar-fil med namnet <jarfile>)]"},
    { "optionXP",
    " [-XP-paket (anger ett paketnamnsprefix f\u00f6r alla genererade transletklasser)]"}

		
  };
  }


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "D\u00c5LIG_KOD";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMATERING_MISSLYCKADES";

  /** General error string.   */
  public static final String ERROR_STRING = "#fel";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "Fel: ";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "Varning: ";

  /** String to specify the XSLT module.  */
  public static final String XSL_HEADER = "XSLT ";

  /** String to specify the XML parser module.  */
  public static final String XML_HEADER = "XML ";

  /** I don't think this is used any more.
   * @deprecated  */
  public static final String QUERY_HEADER = "M\u00d6NSTER ";

}


