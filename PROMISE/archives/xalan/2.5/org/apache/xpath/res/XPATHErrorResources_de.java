package org.apache.xpath.res;

/**
 * <meta name="usage" content="advanced"/>
 * Set up error messages.
 * We build a two dimensional array of message keys and
 * message strings. In order to add a new message here,
 * you need to first add a Static string constant for the
 * Key and update the contents array with Key, Value pair
  * Also you need to  update the count of messages(MAX_CODE)or
 * the count of warnings(MAX_WARNING) [ Information purpose only]
 */
public class XPATHErrorResources_de extends XPATHErrorResources
{


  /** Field MAX_CODE          */

  /** Field MAX_WARNING          */

  /** Field MAX_OTHERS          */
  public static final int MAX_OTHERS = 20;

  /** Field MAX_MESSAGES          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;


  public static final Object[][] contents = {

  /** Field ERROR0000          */


  {
    "ERROR0000", "{0}"},


  /** Field ER_CURRENT_NOT_ALLOWED_IN_MATCH          */


  {
    ER_CURRENT_NOT_ALLOWED_IN_MATCH,
      "Funktion current() in einem Muster nicht zul\u00e4ssig!"},


  /** Field ER_CURRENT_TAKES_NO_ARGS          */


  {
    ER_CURRENT_TAKES_NO_ARGS,
      "Funktion current() hat keine Argumente!"},


  /** Field ER_DOCUMENT_REPLACED          */


  {
    ER_DOCUMENT_REPLACED,
      "Die Implementierung der Funktion document() wurde durch org.apache.xalan.xslt.FuncDocument ersetzt!"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */


  {
    ER_CONTEXT_HAS_NO_OWNERDOC,
      "Kontext hat kein Eigent\u00fcmer-Dokument!"},


  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */


  {
    ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() hat zu viele Argumente."},


  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */


  {
    ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() hat zu viele Argumente."},


  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */


  {
    ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() hat zu viele Argumente."},


  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */


  {
    ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() hat zu viele Argumente."},


  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */


  {
    ER_NAME_HAS_TOO_MANY_ARGS, "name() hat zu viele Argumente."},


  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */


  {
    ER_STRING_HAS_TOO_MANY_ARGS,
      "string() hat zu viele Argumente."},


  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */


  {
    ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length() hat zu viele Argumente."},


  /** Field ER_TRANSLATE_TAKES_3_ARGS          */


  {
    ER_TRANSLATE_TAKES_3_ARGS,
      "Funktion translate() hat drei Argumente!"},


  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */


  {
    ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "Funktion unparsed-entity-uri hat ein Argument!"},


  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */


  {
    ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "Namensraum-Achse noch nicht implementiert!"},


  /** Field ER_UNKNOWN_AXIS          */


  {
    ER_UNKNOWN_AXIS, "Unbekannte Achse: {0}"},


  /** Field ER_UNKNOWN_MATCH_OPERATION          */


  {
    ER_UNKNOWN_MATCH_OPERATION, "Unbekannte Match-Operation!"},


  /** Field ER_INCORRECT_ARG_LENGTH          */


  {
    ER_INCORRECT_ARG_LENGTH,
      "Argumentl\u00e4nge beim Test des Knotens processing-instruction() ist nicht korrekt!"},


  /** Field ER_CANT_CONVERT_TO_NUMBER          */


  {
    ER_CANT_CONVERT_TO_NUMBER,
      "{0} kann nicht in Zahl konvertiert werden"},


  /** Field ER_CANT_CONVERT_TO_NODELIST          */


  {
    ER_CANT_CONVERT_TO_NODELIST,
      "{0} kann nicht in NodeList konvertiert werden!"},


  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */


  {
    ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "{0} kann nicht in NodeSetDTM konvertiert werden!"},


  /** Field ER_CANT_CONVERT_TO_TYPE          */


  {
    ER_CANT_CONVERT_TO_TYPE,


  /** Field ER_EXPECTED_MATCH_PATTERN          */


  {
    ER_EXPECTED_MATCH_PATTERN,
      "Muster in getMatchScore erwartet!"},


  /** Field ER_COULDNOT_GET_VAR_NAMED          */


  {
    ER_COULDNOT_GET_VAR_NAMED,
      "Variable mit Namen {0} konnte nicht abgerufen werden"},


  /** Field ER_UNKNOWN_OPCODE          */


  {
    ER_UNKNOWN_OPCODE, "FEHLER! Unbekannter Opcode: {0}"},


  /** Field ER_EXTRA_ILLEGAL_TOKENS          */


  {
    ER_EXTRA_ILLEGAL_TOKENS, "Zus\u00e4tzliche unzul\u00e4ssige Token: {0}"},


  /** Field ER_EXPECTED_DOUBLE_QUOTE          */


  {
    ER_EXPECTED_DOUBLE_QUOTE,
      "Literalwert mit falschen Anf\u00fchrungszeichen... doppelte Anf\u00fchrungszeichen erwartet!"},


  /** Field ER_EXPECTED_SINGLE_QUOTE          */


  {
    ER_EXPECTED_SINGLE_QUOTE,
      "Literalwert mit falschen Anf\u00fchrungszeichen... einfache Anf\u00fchrungszeichen erwartet!"},


  /** Field ER_EMPTY_EXPRESSION          */


  {
    ER_EMPTY_EXPRESSION, "Leerer Ausdruck!"},


  /** Field ER_EXPECTED_BUT_FOUND          */


  {
    ER_EXPECTED_BUT_FOUND, "{0} erwartet, gefunden: {1}"},


  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */


  {
    ER_INCORRECT_PROGRAMMER_ASSERTION,
      "Programmierannahme ist falsch! - {0}"},


  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */


  {
    ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "Boolesches Argument (...) ist bei Xpath-Draft 19990709 nicht mehr optional."},


  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "',' gefunden, aber davor kein Argument!"},


  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "',' gefunden, aber danach kein Argument!"},


  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */


  {
    ER_PREDICATE_ILLEGAL_SYNTAX,
      "Syntax '..[predicate]' oder '.[predicate]' nicht zul\u00e4ssig. Verwenden Sie statt dessen 'self::node()[Pr\u00e4dikat]'."},


  /** Field ER_ILLEGAL_AXIS_NAME          */


  {
    ER_ILLEGAL_AXIS_NAME, "Unzul\u00e4ssiger Achsenname: {0}"},


  /** Field ER_UNKNOWN_NODETYPE          */


  {
    ER_UNKNOWN_NODETYPE, "Unbekannter nodetype: {0}"},


  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */


  {
    ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "Musterliteralwert ({0}) muss in Anf\u00fchrungszeichen eingeschlossen werden!"},


  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */


  {
    ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0} konnte nicht als Zahl formatiert werden!"},


  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */


  {
    ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "XML TransformerFactory Liaison konnte nicht erstellt werden: {0}"},


  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */


  {
    ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "Fehler! 'select'-Ausdruck bei xpath nicht gefunden (-select)."},


  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */


  {
    ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "FEHLER! ENDOP nach OP_LOCATIONPATH nicht gefunden"},


  /** Field ER_ERROR_OCCURED          */


  {
    ER_ERROR_OCCURED, "Fehler!"},


  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */


  {
    ER_ILLEGAL_VARIABLE_REFERENCE,
      "VariableReference f\u00fcr eine Variable au\u00dferhalb des Kontexts oder ohne Definition angegeben!  Name = {0}"},


  /** Field ER_AXES_NOT_ALLOWED          */


  {
    ER_AXES_NOT_ALLOWED,
      "Nur die Achsen child:: und attribute:: sind in Mustern zul\u00e4ssig! Regel verletzende Achse = {0}"},


  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */


  {
    ER_KEY_HAS_TOO_MANY_ARGS,
      "key() mit falscher Anzahl von Argumenten."},


  /** Field ER_COUNT_TAKES_1_ARG          */


  {
    ER_COUNT_TAKES_1_ARG,
      "Funktion count hat ein Argument!"},


  /** Field ER_COULDNOT_FIND_FUNCTION          */


  {
    ER_COULDNOT_FIND_FUNCTION, "Funktion nicht gefunden: {0}"},


  /** Field ER_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "Codierung nicht unterst\u00fctzt: {0}"},


  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */


  {
    ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "Problem bei DTM in getNextSibling... Wiederherstellung wird versucht"},


  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */


  {
    ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "Programmierfehler: In EmptyNodeList kann nicht geschrieben werden."},


  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */


  {
    ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory wird von XpathContext nicht unterst\u00fctzt!"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "Pr\u00e4fix muss sich in Namensraum aufl\u00f6sen lassen: {0}"},


  /** Field ER_PARSE_NOT_SUPPORTED          */


  {
    ER_PARSE_NOT_SUPPORTED,
      "Parsen von (InputSource Quelle) in XpathContext nicht unterst\u00fctzt! {0} kann nicht ge\u00f6ffnet werden"},



  /** Field ER_SAX_API_NOT_HANDLED          */


  {
    ER_SAX_API_NOT_HANDLED,
      "SAX API-Zeichen (char ch[]... werden vom DTM nicht bearbeitet!"},


  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */


  {
    ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... wird vom DTM nicht bearbeitet!"},


  /** Field ER_DTM_CANNOT_HANDLE_NODES          */


  {
    ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison kann Knoten vom Typ {0} nicht bearbeiten"},


  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */


  {
    ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper kann Knoten vom Typ {0} nicht bearbeiten"},


  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */


  {
    ER_XERCES_PARSE_ERROR_DETAILS,
      "Fehler bei DOM2Helper.parse: SystemID - {0} Zeile - {1}"},


  /** Field ER_XERCES_PARSE_ERROR          */


  {
    ER_XERCES_PARSE_ERROR, "Fehler bei DOM2Helper.parse"},



  /** Field ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "Ung\u00fcltiges UTF-16-Surrogat erkannt: {0} ?"},


  /** Field ER_OIERROR          */


  {
    ER_OIERROR, "IO-Fehler"},


  /** Field ER_CANNOT_CREATE_URL          */


  {
    ER_CANNOT_CREATE_URL, "URL kann nicht erstellt werden f\u00fcr: {0}"},


  /** Field ER_XPATH_READOBJECT          */



  {
    ER_XPATH_READOBJECT, "In XPath.readObject: {0}"},

  
  /** Field ER_XPATH_READOBJECT         */


  {
    ER_FUNCTION_TOKEN_NOT_FOUND,
      "Funktions-Token nicht gefunden."},

  
   /**  Can not deal with XPath type:   */


  {
    ER_CANNOT_DEAL_XPATH_TYPE,
       "Bearbeitung nicht m\u00f6glich von XPath vom Typ: {0}"},

  
   /**  This NodeSet is not mutable  */


  {
    ER_NODESET_NOT_MUTABLE,
       "Dieses NodeSet ist nicht mutierbar"},

  
   /**  This NodeSetDTM is not mutable  */


  {
    ER_NODESETDTM_NOT_MUTABLE,
       "Dieses NodeSetDTM ist nicht mutierbar"},

  
   /**  Variable not resolvable:   */


  {
    ER_VAR_NOT_RESOLVABLE,
        "Variable nicht aufl\u00f6sbar: {0}"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "Ung\u00fcltiger Error-Handler"},

  
   /**  Programmer's assertion: unknown opcode  */


  {
    ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "Programmierannahme: Unbekannter Opcode: {0}"},

  
   /**  0 or 1   */


  {
    ER_ZERO_OR_ONE,
       "0 oder 1"},

  
  
   /**  rtf() not supported by XRTreeFragSelectWrapper   */


  {
    ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() nicht unterst\u00fctzt von XRTreeFragSelectWrapper"},

  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */


  {
    ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() nicht unterst\u00fctzt von XRTreeFragSelectWrapper"},

  
   /**  fsb() not supported for XStringForChars   */


  {
    ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb() nicht unterst\u00fctzt von XStringForChars"},

  
   /**  Could not find variable with the name of   */


  {
    ER_COULD_NOT_FIND_VAR,
      "Variable mit Namen {0} kann nicht gefunden werden"},

  
   /**  XStringForChars can not take a string for an argument   */


  {
    ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars kann keine Zeichenkette als Argument haben"},

  
   /**  The FastStringBuffer argument can not be null   */


  {
    ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "Das FastStringBuffer-Argument darf nicht Null sein"},
    
 /* MANTIS_XALAN CHANGE: BEGIN */ 
   /**  2 or 3   */


  {
    ER_TWO_OR_THREE,
       "2 oder 3"},


   /** Variable accessed before it is bound! */


  {
    ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "Zugriff auf Variable vor deren Bindung!"},


   /** XStringForFSB can not take a string for an argument! */


  {
    ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB darf keine Zeichenkette als Argument \u00fcbergeben werden!"},


   /** Error! Setting the root of a walker to null! */


  {
    ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! Fehler! Ausgangspunkt f\u00fcr Walker wird auf Null gesetzt!!!"},


   /** This NodeSetDTM can not iterate to a previous node! */


  {
    ER_NODESETDTM_CANNOT_ITERATE,
       "Dieses NodeSetDTM-Element kann nicht zu einem vorausgegangenen Knoten iterieren!"},


  /** This NodeSet can not iterate to a previous node! */


  {
    ER_NODESET_CANNOT_ITERATE,
       "Dieses NodeSet-Element kann nicht zu einem vorausgegangenen Knoten iterieren!"},


  /** This NodeSetDTM can not do indexing or counting functions! */


  {
    ER_NODESETDTM_CANNOT_INDEX,
       "Dieses NodeSetDTM-Element kann keine Indizierung oder Z\u00e4hlung durchf\u00fchren!"},


  /** This NodeSet can not do indexing or counting functions! */


  {
    ER_NODESET_CANNOT_INDEX,
       "Dieses NodeSet-Element kann keine Indizierung oder Z\u00e4hlung durchf\u00fchren!"},


  /** Can not call setShouldCacheNodes after nextNode has been called! */


  {
    ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "setShouldCacheNodes kann nicht nach dem Aufruf von nextNode aufgerufen werden!"},


  /** {0} only allows {1} arguments */


  {
    ER_ONLY_ALLOWS,
       "F\u00fcr {0} sind nur {1} Argumente zul\u00e4ssig."},


  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */


  {
    ER_UNKNOWN_STEP,
       "Programmierannahme in getNextStepPos: unbekannter stepType: {0}"},



  /** Problem with RelativeLocationPath */


  {
    ER_EXPECTED_REL_LOC_PATH,



  /** Problem with LocationPath */


  {
    ER_EXPECTED_LOC_PATH,
       "Es wurde ein Pfad zum Speicherort erwartet. Vorgefunden wurde jedoch der folgende Token\u003a  {0}"},



  /** Problem with Step */


  {
    ER_EXPECTED_LOC_STEP,



  /** Problem with NodeTest */


  {
    ER_EXPECTED_NODE_TEST,
       "Es wurde ein Knotentest erwartet, der entweder NCName:* oder QName entspricht."},



  /** Expected step pattern */


  {
    ER_EXPECTED_STEP_PATTERN,
       "Es wurde ein Schrittmuster erwartet. Vorgefunden wurde jedoch das Zeichen '/'."},


 
  /** Expected relative path pattern */


  {
    ER_EXPECTED_REL_PATH_PATTERN,
       "Es wurde ein relatives Pfadmuster erwartet."},



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */


  {
    ER_CANT_CONVERT_TO_BOOLEAN,
       "{0} kann nicht in Datentyp ''boolean'' konvertiert werden."},



  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */


  {
    ER_CANT_CONVERT_TO_SINGLENODE,
       "{0} kann nicht in einen einzelnen Knoten konvertiert werden. Diese Abruffunktion ist f\u00fcr die Typen ANY_UNORDERED_NODE_TYPE und FIRST_ORDERED_NODE_TYPE bestimmt."},



  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */


  {
    ER_CANT_GET_SNAPSHOT_LENGTH,
       "Die Snapshot-L\u00e4nge f\u00fcr diesen Typ kann nicht ermittelt werden: {0}. Diese Abruffunktion ist f\u00fcr die Typen UNORDERED_NODE_SNAPSHOT_TYPE und ORDERED_NODE_SNAPSHOT_TYPE bestimmt."},


  /** Field ER_NON_ITERATOR_TYPE                */


  {
    ER_NON_ITERATOR_TYPE,
       "Es kann keine Iteration \u00fcber einen nicht-iterativen Typ durchgef\u00fchrt werden: {0}"},



  /** Field ER_DOC_MUTATED                      */


  {
    ER_DOC_MUTATED,
       "Das Dokument wurde ge\u00e4ndert, nachdem das Ergebnis zur\u00fcckgegeben wurde. Der Iterator ist ung\u00fcltig."},


  /** Field ER_INVALID_XPATH_TYPE               */


  {
    ER_INVALID_XPATH_TYPE,
       "Ung\u00fcltiges XPath-Typargument: {0}"},


  /** Field ER_EMPTY_XPATH_RESULT                */


  {
    ER_EMPTY_XPATH_RESULT,
       "Leeres XPath-Ergebnisobjekt"},


  /** Field ER_INCOMPATIBLE_TYPES                */


  {
    ER_INCOMPATIBLE_TYPES,
       "Der zur\u00fcckgegebene Typ {0} kann nicht in den angegebenen Typ umgewandelt werden: {1}"},


  /** Field ER_NULL_RESOLVER                     */


  {
    ER_NULL_RESOLVER,
       "Pr\u00e4fix kann nicht aufgel\u00f6st werden, wenn Pr\u00e4fix-Resolver Null ist."},



  /** Field ER_CANT_CONVERT_TO_STRING            */


  {
    ER_CANT_CONVERT_TO_STRING,
       "{0} kann nicht in eine Zeichenkette konvertiert werden."},



  /** Field ER_NON_SNAPSHOT_TYPE                 */


  {
    ER_NON_SNAPSHOT_TYPE,
       "snapshotItem kann nicht f\u00fcr diesen Typ aufgerufen werden: {0}. Diese Methode ist f\u00fcr die Typen UNORDERED_NODE_SNAPSHOT_TYPE und ORDERED_NODE_SNAPSHOT_TYPE bestimmt."},



  /** Field ER_WRONG_DOCUMENT                    */


  {
    ER_WRONG_DOCUMENT,
       "Der Kontextknoten geh\u00f6rt nicht zu dem Dokument, das an dieses XPathEvaluator-Element gebunden ist."},


  /** Field ER_WRONG_NODETYPE                    */


  {
    ER_WRONG_NODETYPE ,
       "Der Kontextknotentyp wird nicht unterst\u00fctzt."},


  /** Field ER_XPATH_ERROR                       */


  {
    ER_XPATH_ERROR ,
       "Unbekannter Fehler in XPath."},




  /** Field WG_LOCALE_NAME_NOT_HANDLED          */


  {
    WG_LOCALE_NAME_NOT_HANDLED,
      "Name von locale in der Funktion format-number noch nicht bearbeitet!"},


  /** Field WG_PROPERTY_NOT_SUPPORTED          */


  {
    WG_PROPERTY_NOT_SUPPORTED,
      "XSL Property nicht unterst\u00fctzt: {0}"},


  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */


  {
    WG_DONT_DO_ANYTHING_WITH_NS,
      "Zurzeit nichts tun mit Namensraum {0} in Eigenschaft: {1}"},


  /** Field WG_SECURITY_EXCEPTION          */


  {
    WG_SECURITY_EXCEPTION,
      "SecurityException beim Versuch des Zugriffs auf XSL-Systemeigenschaft: {0}"},


  /** Field WG_QUO_NO_LONGER_DEFINED          */


  {
    WG_QUO_NO_LONGER_DEFINED,
      "Alte Syntax: quo(...) ist in XPath nicht mehr definiert."},


  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */


  {
    WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath ben\u00f6tigt ein abgeleitetes Objekt zum Implementieren von nodeTest!"},


  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */


  {
    WG_FUNCTION_TOKEN_NOT_FOUND,
      "Funktions-Token nicht gefunden."},


  /** Field WG_COULDNOT_FIND_FUNCTION          */


  {
    WG_COULDNOT_FIND_FUNCTION,
      "Funktion nicht gefunden: {0}"},


  /** Field WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "URL kann nicht erstellt werden aus: {0}"},


  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */


  {
    WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "Option -E f\u00fcr DTM-Parser nicht unterst\u00fctzt"},


  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */


  {
    WG_ILLEGAL_VARIABLE_REFERENCE,
      "VariableReference f\u00fcr Variable au\u00dferhalb des Kontexts oder ohne Definition angegeben!  Name = {0}"},


  /** Field WG_UNSUPPORTED_ENCODING          */


  {
    WG_UNSUPPORTED_ENCODING, "Nicht unterst\u00fctzte Codierung: {0}"},


  { "ui_language", "de"},
  { "help_language", "de"},
  { "language", "de"},
    { "BAD_CODE",
      "Parameter f\u00fcr createMessage au\u00dferhalb der Grenzwerte"},
    { "FORMAT_FAILED",
      "Ausnahme bei Aufruf von messageFormat"},
    { "version", ">>>>>>> Xalan Version "},
    { "version2", "<<<<<<<"},
    { "yes", "ja"},
    { "xsldone", "XSLProcessor: fertig"},
    { "xpath_option", "xpath-Optionen: "},
    { "optionIN", "   [-in inputXMLURL]"},
    { "optionSelect", "   [-select xpath-Ausdruck]"},
    { "optionMatch", "   [-match Muster (f\u00fcr Trefferdiagnose)]"},
    { "optionAnyExpr",
      "Oder irgendein xpath-Ausdruck f\u00fchrt einen Diagnose-Dump aus"},
    { "noParsermsg1", "XSL Process fehlgeschlagen."},
    { "noParsermsg2", "** Parser nicht gefunden**"},
    { "noParsermsg3", "Pr\u00fcfen Sie Ihren classpath."},
    { "noParsermsg4",
      "Wenn Sie IBMs XML Parser for Java nicht haben, k\u00f6nnen Sie ihn von folgender Adresse herunterladen"},
  };


  /** Field BAD_CODE          */
  public static final String BAD_CODE = "BAD_CODE";

  /** Field FORMAT_FAILED          */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** Field ERROR_RESOURCES          */
  public static final String ERROR_RESOURCES =
    "org.apache.xpath.res.XPATHErrorResources";

  /** Field ERROR_STRING          */

  /** Field ERROR_HEADER          */
  public static final String ERROR_HEADER = "Fehler: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Warnung: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "MUSTER ";

  /**
   * Get the association list.
   *
   * @return The association list.
   */
  public Object[][] getContents()
  {
    return contents;
  }
}

