package org.apache.xpath.res;

/**
 * Set up error messages.
 * We build a two dimensional array of message keys and
 * message strings. In order to add a new message here,
 * you need to first add a Static string constant for the
 * Key and update the contents array with Key, Value pair
  * Also you need to  update the count of messages(MAX_CODE)or
 * the count of warnings(MAX_WARNING) [ Information purpose only]
 * @xsl.usage advanced
 */
public class XPATHErrorResources_sv extends XPATHErrorResources
{


  /** Field MAX_CODE          */

  /** Field MAX_WARNING          */

  /** Field MAX_OTHERS          */
  public static final int MAX_OTHERS = 20;

  /** Field MAX_MESSAGES          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;


  /**
   * Get the association list.
   *
   * @return The association list.
   */
  public Object[][] getContents()
  {
    return new Object[][]{

  /** Field ERROR0000          */


  {
    "ERROR0000", "{0}"},


  /** Field ER_CURRENT_NOT_ALLOWED_IN_MATCH          */


  {
    ER_CURRENT_NOT_ALLOWED_IN_MATCH,
      "Funktionen current() \u00e4r inte till\u00e5ten i ett matchningsm\u00f6nster!"},


  /** Field ER_CURRENT_TAKES_NO_ARGS          */


  {
    ER_CURRENT_TAKES_NO_ARGS,
      "Funktionen current() tar inte emot argument!"},


  /** Field ER_DOCUMENT_REPLACED          */


  {
    ER_DOCUMENT_REPLACED,
      "Implementeringen av funktionen document() har ersatts av org.apache.xalan.xslt.FuncDocument!"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */


  {
    ER_CONTEXT_HAS_NO_OWNERDOC,
      "Kontext saknar \u00e4gardokument!"},


  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */


  {
    ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() har f\u00f6r m\u00e5nga argument."},


  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */


  {
    ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() har f\u00f6r m\u00e5nga argument."},


  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */


  {
    ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() har f\u00f6r m\u00e5nga argument."},


  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */


  {
    ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() har f\u00f6r m\u00e5nga argument."},


  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */


  {
    ER_NAME_HAS_TOO_MANY_ARGS, "name() har f\u00f6r m\u00e5nga argument."},


  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */


  {
    ER_STRING_HAS_TOO_MANY_ARGS,
      "string() har f\u00f6r m\u00e5nga argument."},


  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */


  {
    ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string.length() har f\u00f6r m\u00e5nga argument."},


  /** Field ER_TRANSLATE_TAKES_3_ARGS          */


  {
    ER_TRANSLATE_TAKES_3_ARGS,
      "Funktionen translate() tar emot tre argument!"},


  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */


  {
    ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "Funktionen unparsed-entity-uri borde ta emot ett argument!"},


  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */


  {
    ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "Namespace-axel inte implementerad \u00e4n!"},


  /** Field ER_UNKNOWN_AXIS          */


  {
    ER_UNKNOWN_AXIS, "ok\u00e4nd axel: {0}"},


  /** Field ER_UNKNOWN_MATCH_OPERATION          */


  {
    ER_UNKNOWN_MATCH_OPERATION, "ok\u00e4nd matchningshandling!"},


  /** Field ER_INCORRECT_ARG_LENGTH          */


  {
    ER_INCORRECT_ARG_LENGTH,
      "Nodtests argumentl\u00e4ngd i processing-instruction() \u00e4r inte korrekt!"},


  /** Field ER_CANT_CONVERT_TO_NUMBER          */


  {
    ER_CANT_CONVERT_TO_NUMBER,
      "Kan inte konvertera {0} till ett nummer"},


  /** Field ER_CANT_CONVERT_TO_NODELIST          */


  {
    ER_CANT_CONVERT_TO_NODELIST,
      "Kan inte konvertera {0} till en NodeList!"},


  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */


  {
    ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "Kan inte konvertera {0} till en NodeSetDTM!"},


  /** Field ER_CANT_CONVERT_TO_TYPE          */


  {
    ER_CANT_CONVERT_TO_TYPE,


  /** Field ER_EXPECTED_MATCH_PATTERN          */


  {
    ER_EXPECTED_MATCH_PATTERN,
      "Matchningsm\u00f6nster i getMatchScore f\u00f6rv\u00e4ntat!"},


  /** Field ER_COULDNOT_GET_VAR_NAMED          */


  {
    ER_COULDNOT_GET_VAR_NAMED,
      "Kunde inte h\u00e4mta variabeln {0}"},


  /** Field ER_UNKNOWN_OPCODE          */


  {
    ER_UNKNOWN_OPCODE, "FEL! Ok\u00e4nd op-kod: {0}"},


  /** Field ER_EXTRA_ILLEGAL_TOKENS          */


  {
    ER_EXTRA_ILLEGAL_TOKENS, "Ytterligare otill\u00e5tna tecken: {0}"},


  /** Field ER_EXPECTED_DOUBLE_QUOTE          */


  {
    ER_EXPECTED_DOUBLE_QUOTE,
      "Litteral omges av fel sorts citationstecken... dubbla citationstecken f\u00f6rv\u00e4ntade!"},


  /** Field ER_EXPECTED_SINGLE_QUOTE          */


  {
    ER_EXPECTED_SINGLE_QUOTE,
      "Litteral omges av fel sorts citationstecken... enkla citationstecken f\u00f6rv\u00e4ntade!"},


  /** Field ER_EMPTY_EXPRESSION          */


  {
    ER_EMPTY_EXPRESSION, "Tomt uttryck!"},


  /** Field ER_EXPECTED_BUT_FOUND          */


  {
    ER_EXPECTED_BUT_FOUND, "{0} f\u00f6rv\u00e4ntat, men hittade: {1}"},


  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */


  {
    ER_INCORRECT_PROGRAMMER_ASSERTION,
      "Programmerares f\u00f6rs\u00e4kran \u00e4r inte korrekt! - {0}"},


  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */


  {
    ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "boolean(...)-argument \u00e4r inte l\u00e4ngre valfri med 19990709 XPath-utkast."},


  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "Hittade ',' men inget f\u00f6reg\u00e5ende argument!"},


  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "Hittade ',' men inget efterf\u00f6ljande argument!"},


  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */


  {
    ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[predikat]' or '.[predikat]' \u00e4r otill\u00e5ten syntax.  Anv\u00e4nd 'self::node()[predikat]' ist\u00e4llet."},


  /** Field ER_ILLEGAL_AXIS_NAME          */


  {
    ER_ILLEGAL_AXIS_NAME, "otill\u00e5tet axel-namn: {0}"},


  /** Field ER_UNKNOWN_NODETYPE          */


  {
    ER_UNKNOWN_NODETYPE, "ok\u00e4nd nodtyp: {0}"},


  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */


  {
    ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "M\u00f6nsterlitteral {0} m\u00e5ste s\u00e4ttas inom citationstecken!"},


  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */


  {
    ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0} kunde inte formateras till ett nummer"},


  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */


  {
    ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "Kunde inte skapa XML TransformerFactory Liaison: {0}"},


  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */


  {
    ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "Fel! Hittade inte xpath select-uttryck (-select)."},


  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */


  {
    ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "FEL! Hittade inte ENDOP efter OP_LOCATIONPATH"},


  /** Field ER_ERROR_OCCURED          */


  {
    ER_ERROR_OCCURED, "Fel intr\u00e4ffade!"},


  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */


  {
    ER_ILLEGAL_VARIABLE_REFERENCE,
      "VariableReference angiven f\u00f6r variabel som \u00e4r utanf\u00f6r sammanhanget eller som saknar definition!  Namn = {0}"},


  /** Field ER_AXES_NOT_ALLOWED          */


  {
    ER_AXES_NOT_ALLOWED,
      "Enbart barn::- och attribut::- axlar \u00e4r till\u00e5tna i matchningsm\u00f6nster!  Regelvidriga axlar = {0}"},


  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */


  {
    ER_KEY_HAS_TOO_MANY_ARGS,
      "key() har ett felaktigt antal argument."},


  /** Field ER_COUNT_TAKES_1_ARG          */


  {
    ER_COUNT_TAKES_1_ARG,
      "Funktionen count borde ta emot ett argument!"},


  /** Field ER_COULDNOT_FIND_FUNCTION          */


  {
    ER_COULDNOT_FIND_FUNCTION, "Hittade inte funktionen: {0}"},


  /** Field ER_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "Ej underst\u00f6dd kodning: {0}"},


  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */


  {
    ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "Problem intr\u00e4ffade i DTM i getNextSibling... f\u00f6rs\u00f6ker \u00e5terh\u00e4mta"},


  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */


  {
    ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "Programmerarfel: EmptyNodeList kan inte skrivas till."},


  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */


  {
    ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory underst\u00f6ds inte av XPathContext!"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "Prefix must resolve to a namespace: {0}"},


  /** Field ER_PARSE_NOT_SUPPORTED          */


  {
    ER_PARSE_NOT_SUPPORTED,
      "parse (InputSource source) underst\u00f6ds inte av XPathContext! Kan inte \u00f6ppna {0}"},


  /** Field ER_SAX_API_NOT_HANDLED          */


  {
    ER_SAX_API_NOT_HANDLED,
      "SAX API-tecken(char ch[]... hanteras inte av DTM!"},


  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */


  {
    ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... hanteras inte av DTM!"},


  /** Field ER_DTM_CANNOT_HANDLE_NODES          */


  {
    ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison kan inte hantera noder av typen {0}"},


  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */


  {
    ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper kan inte hantera noder av typen {0}"},


  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */


  {
    ER_XERCES_PARSE_ERROR_DETAILS,
      "DOM2Helper.parse-fel: SystemID - {0} rad - {1}"},


  /** Field ER_XERCES_PARSE_ERROR          */


  {
    ER_XERCES_PARSE_ERROR, "DOM2Helper.parse-fel"},


  /** Field ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "Ogiltigt UTF-16-surrogat uppt\u00e4ckt: {0} ?"},


  /** Field ER_OIERROR          */


  {
    ER_OIERROR, "IO-fel"},


  /** Field ER_CANNOT_CREATE_URL          */


  {
    ER_CANNOT_CREATE_URL, "Kan inte skapa url f\u00f6r: {0}"},


  /** Field ER_XPATH_READOBJECT          */


  {
    ER_XPATH_READOBJECT, "I XPath.readObject: {0}"},

  
  /** Field ER_XPATH_READOBJECT         */


  {
    ER_FUNCTION_TOKEN_NOT_FOUND,
      "funktionstecken saknas."},

  
   /**  Can not deal with XPath type:   */


  {
    ER_CANNOT_DEAL_XPATH_TYPE,
       "Kan inte hantera XPath-typ: {0}"},

  
   /**  This NodeSet is not mutable  */


  {
    ER_NODESET_NOT_MUTABLE,
       "NodeSet \u00e4r of\u00f6r\u00e4nderlig"},

  
   /**  This NodeSetDTM is not mutable  */


  {
    ER_NODESETDTM_NOT_MUTABLE,
       "NodeSetDTM \u00e4r of\u00f6r\u00e4nderlig"},

  
   /**  Variable not resolvable:   */


  {
    ER_VAR_NOT_RESOLVABLE,
        "Variabel ej l\u00f6sbar: {0}"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "Null error handler"},

  
   /**  Programmer's assertion: unknown opcode  */


  {
    ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "Programmerares f\u00f6rs\u00e4kran: ok\u00e4nd op-kod: {0}"},

  
   /**  0 or 1   */


  {
    ER_ZERO_OR_ONE,
       "0 eller 1"},

  
  
   /**  rtf() not supported by XRTreeFragSelectWrapper   */


  {
    ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() underst\u00f6ds inte av XRTreeFragSelectWrapper!"},

  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */


  {
    ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() underst\u00f6ds inte av XRTreeFragSelectWrapper!"},

  
   /**  fsb() not supported for XStringForChars   */


  {
    ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb() underst\u00f6ds inte av XRStringForChars!"},

  
   /**  Could not find variable with the name of   */


  {
    ER_COULD_NOT_FIND_VAR,
      "Hittade inte variabeln med namn {0}"},

  
   /**  XStringForChars can not take a string for an argument   */


  {
    ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars kan inte ta en str\u00e4ng som argument"},

  
   /**  The FastStringBuffer argument can not be null   */


  {
    ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "FastStringBuffer-argumentet f\u00e5r inte vara null"},
    
/* MANTIS_XALAN CHANGE: BEGIN */ 
   /**  2 or 3   */


  {
    ER_TWO_OR_THREE,
       "2 eller 3"},


   /** Variable accessed before it is bound! */


  {
    ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "Variabeln anv\u00e4ndes innan den bands!"},


   /** XStringForFSB can not take a string for an argument! */


  {
    ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB kan inte ha en str\u00e4ng som argument!"},


   /** Error! Setting the root of a walker to null! */


  {
    ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! Fel! Anger roten f\u00f6r en \"walker\" till null!!!"},


   /** This NodeSetDTM can not iterate to a previous node! */


  {
    ER_NODESETDTM_CANNOT_ITERATE,
       "Detta NodeSetDTM kan inte iterera till en tidigare nod!"},


  /** This NodeSet can not iterate to a previous node! */


  {
    ER_NODESET_CANNOT_ITERATE,
       "Detta NodeSet kan inte iterera till en tidigare nod!"},


  /** This NodeSetDTM can not do indexing or counting functions! */


  {
    ER_NODESETDTM_CANNOT_INDEX,
       "Detta NodeSetDTM har inte funktioner f\u00f6r indexering och r\u00e4kning!"},


  /** This NodeSet can not do indexing or counting functions! */


  {
    ER_NODESET_CANNOT_INDEX,
       "Detta NodeSet har inte funktioner f\u00f6r indexering och r\u00e4kning!"},


  /** Can not call setShouldCacheNodes after nextNode has been called! */


  {
    ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "Det g\u00e5r inte att anropa setShouldCacheNodes efter att nextNode har anropats!"},


  /** {0} only allows {1} arguments */


  {
    ER_ONLY_ALLOWS,
       "{0} till\u00e5ter bara {1} argument"},


  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */


  {
    ER_UNKNOWN_STEP,
       "Programmerarkontroll i getNextStepPos: ok\u00e4nt steg Typ: {0}"},



  /** Problem with RelativeLocationPath */


  {
    ER_EXPECTED_REL_LOC_PATH,



  /** Problem with LocationPath */


  {
    ER_EXPECTED_LOC_PATH,
       "En plats f\u00f6rv\u00e4ntades, men f\u00f6ljande token p\u00e5tr\u00e4ffades\u003a  {0}"},



  /** Problem with Step */


  {
    ER_EXPECTED_LOC_STEP,



  /** Problem with NodeTest */


  {
    ER_EXPECTED_NODE_TEST,
       "Ett nodtest som matchar antingen NCName:* eller QName f\u00f6rv\u00e4ntades."},



  /** Expected step pattern */


  {
    ER_EXPECTED_STEP_PATTERN,
       "Ett stegm\u00f6nster f\u00f6rv\u00e4ntades, men '/' p\u00e5tr\u00e4ffades."},


 
  /** Expected relative path pattern */


  {
    ER_EXPECTED_REL_PATH_PATTERN,
       "Ett m\u00f6nster f\u00f6r relativ s\u00f6kv\u00e4g f\u00f6rv\u00e4ntades."},



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */


  {
    ER_CANT_CONVERT_TO_BOOLEAN,
       "Det g\u00e5r inte att konvertera {0} till ett Booleskt v\u00e4rde."},



  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */


  {
    ER_CANT_CONVERT_TO_SINGLENODE,
       "Det g\u00e5r inte att konvertera {0} till en enda nod. G\u00e4ller typerna ANY_UNORDERED_NODE_TYPE och FIRST_ORDERED_NODE_TYPE."},



  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */


  {
    ER_CANT_GET_SNAPSHOT_LENGTH,
       "Det g\u00e5r inte att erh\u00e5lla l\u00e4ngd f\u00f6r \u00f6gonblicksbild p\u00e5 typ: {0}. G\u00e4ller typerna UNORDERED_NODE_SNAPSHOT_TYPE och ORDERED_NODE_SNAPSHOT_TYPE."},


  /** Field ER_NON_ITERATOR_TYPE                */


  {
    ER_NON_ITERATOR_TYPE,
       "Det g\u00e5r inte att iterera \u00f6ver den icke itererbara typen: {0}"},



  /** Field ER_DOC_MUTATED                      */


  {
    ER_DOC_MUTATED,
       "Dokumentet har \u00e4ndrats sedan resultatet genererades. Iterering ogiltig."},


  /** Field ER_INVALID_XPATH_TYPE               */


  {
    ER_INVALID_XPATH_TYPE,
       "Ogiltigt XPath-typargument: {0}"},


  /** Field ER_EMPTY_XPATH_RESULT                */


  {
    ER_EMPTY_XPATH_RESULT,
       "Tomt XPath-resultatobjekt"},


  /** Field ER_INCOMPATIBLE_TYPES                */


  {
    ER_INCOMPATIBLE_TYPES,
       "Den genererade typen: {0} kan inte bearbetas i den angivna typen: {1}"},


  /** Field ER_NULL_RESOLVER                     */


  {
    ER_NULL_RESOLVER,
       "Det g\u00e5r inte att l\u00f6sa prefixet utan prefixl\u00f6sare."},



  /** Field ER_CANT_CONVERT_TO_STRING            */


  {
    ER_CANT_CONVERT_TO_STRING,
       "Det g\u00e5r inte att konvertera {0} till en str\u00e4ng."},



  /** Field ER_NON_SNAPSHOT_TYPE                 */


  {
    ER_NON_SNAPSHOT_TYPE,
       "Det g\u00e5r inte att anropa snapshotItem p\u00e5 typ: {0}. Metoden g\u00e4ller typerna UNORDERED_NODE_SNAPSHOT_TYPE och ORDERED_NODE_SNAPSHOT_TYPE."},



  /** Field ER_WRONG_DOCUMENT                    */


  {
    ER_WRONG_DOCUMENT,
       "Kontextnoden tillh\u00f6r inte dokumentet som \u00e4r bundet till denna XPathEvaluator."},


  /** Field ER_WRONG_NODETYPE                    */


  {
    ER_WRONG_NODETYPE ,
       "Kontextnoden kan inte hanteras."},


  /** Field ER_XPATH_ERROR                       */


  {
    ER_XPATH_ERROR ,
       "Ok\u00e4nt fel i XPath."},




  /** Field WG_LOCALE_NAME_NOT_HANDLED          */


  {
    WG_LOCALE_NAME_NOT_HANDLED,
      "locale-namnet i format-number-funktionen \u00e4nnu inte hanterat!"},


  /** Field WG_PROPERTY_NOT_SUPPORTED          */


  {
    WG_PROPERTY_NOT_SUPPORTED,
      "XSL-Egenskap underst\u00f6ds inte: {0}"},


  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */


  {
    WG_DONT_DO_ANYTHING_WITH_NS,
      "G\u00f6r f\u00f6r n\u00e4rvarande inte n\u00e5gonting med namespace {0} i egenskap: {1}"},


  /** Field WG_SECURITY_EXCEPTION          */


  {
    WG_SECURITY_EXCEPTION,
      "SecurityException vid f\u00f6rs\u00f6k att f\u00e5 tillg\u00e5ng till XSL-systemegenskap: {0}"},


  /** Field WG_QUO_NO_LONGER_DEFINED          */


  {
    WG_QUO_NO_LONGER_DEFINED,
      "Gammal syntax: quo(...) \u00e4r inte l\u00e4ngre definierad i XPath."},


  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */


  {
    WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath beh\u00f6ver ett deriverat objekt f\u00f6r att implementera nodeTest!"},


  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */


  {
    WG_FUNCTION_TOKEN_NOT_FOUND,
      "funktionstecken saknas."},


  /** Field WG_COULDNOT_FIND_FUNCTION          */


  {
    WG_COULDNOT_FIND_FUNCTION,
      "Hittade inte funktion: {0}"},


  /** Field WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "Kan inte skapa URL fr\u00e5n: {0}"},


  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */


  {
    WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "Alternativet -E underst\u00f6ds inte f\u00f6r DTM-tolk"},


  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */


  {
    WG_ILLEGAL_VARIABLE_REFERENCE,
      "VariableReference angiven f\u00f6r variabel som \u00e4r utanf\u00f6r sammanhanget eller som saknar definition!  Namn = {0}"},


  /** Field WG_UNSUPPORTED_ENCODING          */


  {
    WG_UNSUPPORTED_ENCODING, "Ej underst\u00f6dd kodning: {0}"},



  { "ui_language", "sv"},
  { "help_language", "sv"},
  { "language", "sv"},
    { "BAD_CODE",
      "Parameter till createMessage ligger utanf\u00f6r till\u00e5tet intervall"},
    { "FORMAT_FAILED",
      "Undantag utl\u00f6st vid messageFormat-anrop"},
    { "version", ">>>>>>> Xalan Version"},
    { "version2", "<<<<<<<"},
    { "yes",  "ja"},
    { "xsldone", "XSLProcessor f\u00e4rdig"},
    { "xpath_option", "xpath-alternativ"},
    { "optionIN", "    [-in inputXMLURL]"},
    { "optionSelect", "[-select xpath-uttryck]"},
    { "optionMatch",
      "   [-match matchningsm\u00f6nster (f\u00f6r matchningsdiagnostik)]"},
    { "optionAnyExpr",
      "Eller bara ett xpath-uttryck kommer att g\u00f6ra en diagnostik-dump"},
    { "noParsermsg1", "XSL-Process misslyckades."},
    { "noParsermsg2", "** Hittade inte tolk **"},
    { "noParsermsg3", "V\u00e4nligen kontrollera din classpath"},
    { "noParsermsg4",
      "Om du inte har IBMs XML-Tolk f\u00f6r Java, kan du ladda ner den fr\u00e5n"},
    { "noParsermsg5",
  };
  }


  /** Field BAD_CODE          */
  public static final String BAD_CODE = "D\u00c5LIG_KOD";

  /** Field FORMAT_FAILED          */
  public static final String FORMAT_FAILDE = "FORMATTERING_MISSLYCKADES";

  /** Field ERROR_RESOURCES          */
  public static final String ERROR_RESOURCES =
    "org.apache.xpath.res.XPATHErrorResources";

  /** Field ERROR_STRING          */

  /** Field ERROR_HEADER          */
  public static final String ERROR_HEADER = "Fel: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Varning: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "M\u00d6NSTER ";

}


