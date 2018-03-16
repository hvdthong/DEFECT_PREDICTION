package org.apache.xml.res;


/**
 * Set up error messages.
 * We build a two dimensional array of message keys and
 * message strings. In order to add a new message here,
 * you need to first add a String constant. And you need
 * to enter key, value pair as part of the contents
 * array. You also need to update MAX_CODE for error strings
 * and MAX_WARNING for warnings ( Needed for only information
 * purpose )
 */
public class XMLErrorResources_de extends XMLErrorResources
{

  /** Maximum error messages, this is needed to keep track of the number of messages.    */
  public static final int MAX_CODE = 61;

  /** Maximum warnings, this is needed to keep track of the number of warnings.          */
  public static final int MAX_WARNING = 0;

  /** Maximum misc strings.   */
  public static final int MAX_OTHERS = 4;

  /** Maximum total warnings and error messages.          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;



  /** The lookup table for error messages.   */
  public static final Object[][] contents = {

  /** Error message ID that has a null message, but takes in a single object.    */
    {"ER0000" , "{0}" },

  /** ER_FUNCTION_NOT_SUPPORTED          */

  {
    ER_FUNCTION_NOT_SUPPORTED, "Funktion nicht unterst\u00fctzt!"},
 
  /** Can't overwrite cause         */

  {
    ER_CANNOT_OVERWRITE_CAUSE,
			"Ursache f\u00fcr nicht m\u00f6gliches \u00dcberschreiben"},
  
   /**  No default implementation found */

  {
    ER_NO_DEFAULT_IMPL,
         "Keine Standardimplementierung gefunden"},
  
   /**  ChunkedIntArray({0}) not currently supported */

  {
    ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
       "ChunkedIntArray({0}) zurzeit nicht unterst\u00fctzt"},
  
   /**  Offset bigger than slot */

  {
    ER_OFFSET_BIGGER_THAN_SLOT,
       "Offset gr\u00f6\u00dfer als Slot"},
  
   /**  Coroutine not available, id= */

  {
    ER_COROUTINE_NOT_AVAIL,
       "Coroutine nicht verf\u00fcgbar, ID={0}"},
  
   /**  CoroutineManager recieved co_exit() request */

  {
    ER_COROUTINE_CO_EXIT,
       "CoroutineManager empfing Anforderung co_exit()"},
  
   /**  co_joinCoroutineSet() failed */

  {
    ER_COJOINROUTINESET_FAILED,
       "co_joinCoroutineSet() fehlgeschlagen"},
  
   /**  Coroutine parameter error () */

  {
    ER_COROUTINE_PARAM,
       "Parameterfehler in Coroutine ({0})"},
  
   /**  UNEXPECTED: Parser doTerminate answers  */

  {
    ER_PARSER_DOTERMINATE_ANSWERS,
       "\nUNEXPECTED: Parser doTerminate antwortet {0}"},
  
   /**  parse may not be called while parsing */

  {
    ER_NO_PARSE_CALL_WHILE_PARSING,
       "parse darf w\u00e4hrend des Parsens nicht aufgerufen werden"},
  
   /**  Error: typed iterator for axis  {0} not implemented  */

  {
    ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "Fehler: Typisierter Iterator f\u00fcr Achse {0} nicht implementiert"},
  
   /**  Error: iterator for axis {0} not implemented  */

  {
    ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "Fehler: Iterator f\u00fcr Achse {0} nicht implementiert"},
  
   /**  Iterator clone not supported  */

  {
    ER_ITERATOR_CLONE_NOT_SUPPORTED,
       "Iterator-Klone nicht unterst\u00fctzt"},
  
   /**  Unknown axis traversal type  */

  {
    ER_UNKNOWN_AXIS_TYPE,
       "Unbekannter Achsen-Traversaltyp: {0}"},
  
   /**  Axis traverser not supported  */

  {
    ER_AXIS_NOT_SUPPORTED,
       "Achsen-Traverser nicht unterst\u00fctzt: {0}"},
  
   /**  No more DTM IDs are available  */

  {
    ER_NO_DTMIDS_AVAIL,
       "Keine weiteren DTM-IDs verf\u00fcgbar"},
  
   /**  Not supported  */

  {
    ER_NOT_SUPPORTED,
       "Nicht unterst\u00fctzt: {0}"},
  
   /**  node must be non-null for getDTMHandleFromNode  */

  {
    ER_NODE_NON_NULL,
       "Knoten darf f\u00fcr getDTMHandleFromNode nicht Null sein"},
  
   /**  Could not resolve the node to a handle  */

  {
    ER_COULD_NOT_RESOLVE_NODE,
       "Der Knoten zu einem Handle konnte nicht aufgel\u00f6st werden"},
  
   /**  startParse may not be called while parsing */

  {
    ER_STARTPARSE_WHILE_PARSING,
       "startParse darf beim Parsen nicht aufgerufen werden"},
  
   /**  startParse needs a non-null SAXParser  */

  {
    ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse ben\u00f6tigt einen SAXParser, der nicht Null ist"},
  
   /**  could not initialize parser with */

  {
    ER_COULD_NOT_INIT_PARSER,
       "Parser konnte nicht initialisiert werden"},

   /**  exception creating new instance for pool  */

  {
    ER_EXCEPTION_CREATING_POOL,
       "Ausnahme, die neue Instanz f\u00fcr Pool erstellt"},
  
   /**  Path contains invalid escape sequence  */

  {
    ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "Pfad enth\u00e4lt ung\u00fcltige Escape-Sequenz"},
  
   /**  Scheme is required!  */

  {
    ER_SCHEME_REQUIRED,
       "Schema ist erforderlich!"},
  
   /**  No scheme found in URI  */

  {
    ER_NO_SCHEME_IN_URI,
       "Kein Schema gefunden in URI: {0}"},
  
   /**  No scheme found in URI  */

  {
    ER_NO_SCHEME_INURI,
       "Kein Schema gefunden in URI"},
  
   /**  Path contains invalid character:   */

  {
    ER_PATH_INVALID_CHAR,
       "Pfad enth\u00e4lt ung\u00fcltiges Zeichen: {0}"},
  
   /**  Cannot set scheme from null string  */

  {
    ER_SCHEME_FROM_NULL_STRING,
       "Schema kann ausgehend von Null-Zeichenkette nicht gesetzt werden"},
  
   /**  The scheme is not conformant. */

  {
    ER_SCHEME_NOT_CONFORMANT,
       "Schema ist nicht konform."},
  
   /**  Host is not a well formed address  */

  {
    ER_HOST_ADDRESS_NOT_WELLFORMED,
       "Hostadresse nicht korrekt gebildet"},
  
   /**  Port cannot be set when host is null  */

  {
    ER_PORT_WHEN_HOST_NULL,
       "Port kann nicht gesetzt werden, wenn Host Null ist"},
  
   /**  Invalid port number  */

  {
    ER_INVALID_PORT,
       "Ung\u00fcltige Port-Nummer"},
  
   /**  Fragment can only be set for a generic URI  */

  {
    ER_FRAG_FOR_GENERIC_URI,
       "Fragment kann nur f\u00fcr einen generischen URI gesetzt werden"},
  
   /**  Fragment cannot be set when path is null  */

  {
    ER_FRAG_WHEN_PATH_NULL,
       "Fragment kann nicht gesetzt werden, wenn der Pfad Null ist"},
  
   /**  Fragment contains invalid character  */

  {
    ER_FRAG_INVALID_CHAR,
       "Fragment enth\u00e4lt ung\u00fcltiges Zeichen"},
  
 
  
   /** Parser is already in use  */

  {
    ER_PARSER_IN_USE,
        "Parser wird bereits verwendet"},
  
   /** Parser is already in use  */

  {
    ER_CANNOT_CHANGE_WHILE_PARSING,
        "{0} {1} kann beim Parsen nicht ge\u00e4ndert werden"},
  
   /** Self-causation not permitted  */

  {
    ER_SELF_CAUSATION_NOT_PERMITTED,
        "Selbst-Kausalit\u00e4t nicht erlaubt"},
  
   /** Userinfo may not be specified if host is not specified   */

  {
    ER_NO_USERINFO_IF_NO_HOST,
        "Userinfo kann nicht angegeben werden, wenn Host nicht angegeben ist"},
  
   /** Port may not be specified if host is not specified   */

  {
    ER_NO_PORT_IF_NO_HOST,
        "Port kann nicht angegeben werden, wenn Host nicht angegeben ist"},
  
   /** Query string cannot be specified in path and query string   */

  {
    ER_NO_QUERY_STRING_IN_PATH,
        "Abfragezeichenkette kann nicht sowohl im Pfad als auch in der Abfragezeichenkette angegeben werden"},
  
   /** Fragment cannot be specified in both the path and fragment   */

  {
    ER_NO_FRAGMENT_STRING_IN_PATH,
        "Fragment kann nicht sowohl im Pfad als auch im Fragment angegeben werden"},
  
   /** Cannot initialize URI with empty parameters   */

  {
    ER_CANNOT_INIT_URI_EMPTY_PARMS,
        "URI kann nicht mit leeren Parametern initialisiert werden"},

  /**  Method not yet supported    */

  {
    ER_METHOD_NOT_SUPPORTED,
        "Methode noch nicht unterst\u00fctzt "},
 
  /** IncrementalSAXSource_Filter not currently restartable   */

  {
    ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
     "IncrementalSAXSource_Filter kann zurzeit nicht neu gestartet werden"},
  
  /** IncrementalSAXSource_Filter not currently restartable   */

  {
    ER_XMLRDR_NOT_BEFORE_STARTPARSE,
     "XMLReader nicht vor startParse-Anforderung"},
  
  {
    ER_AXIS_TRAVERSER_NOT_SUPPORTED,
     "Achsen-Traverser nicht unterst\u00fctzt: {0}"},

  {
    ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
     "ListingErrorHandler wurde mit Null-PrintWriter erstellt!"},

  {
    ER_SYSTEMID_UNKNOWN,
     "Unbekannte SystemId"},

  {
    ER_LOCATION_UNKNOWN,
     "Fehler befindet sich an unbekannter Stelle"},

  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "Pr\u00e4fix muss sich in Namensraum aufl\u00f6sen lassen: {0}"},

  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */

  {
    ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "createDocument() in XpathContext nicht unterst\u00fctzt!"},
   

  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "Attribut-Tochterknoten hat kein Eigent\u00fcmer-Dokument!"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "Attribut-Tochterknoten hat kein Element Eigent\u00fcmer-Dokument!"},


  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  {
    ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "Warnung: Vor einem Dokumentelement kann kein Text ausgegeben werden! Wird ignoriert..."},


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  {
    ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "Ein DOM kann nur einen Stamm haben!"},

  
   /**  Argument 'localName' is null  */


  {
    ER_ARG_LOCALNAME_NULL,
       "Argument 'localName' ist Null"},


  /** localname in QNAME should be a valid NCName */


  {
    ER_ARG_LOCALNAME_INVALID,
       "Der lokale Name in QNAME muss einem g\u00fcltigen 'NCName' entsprechen."},


  /** prefix in QNAME should be a valid NCName */


  {
    ER_ARG_PREFIX_INVALID,
       "Das Pr\u00e4fix in QNAME muss einem g\u00fcltigen 'NCName' entsprechen."},
       
  { "BAD_CODE", "Parameter f\u00fcr createMessage au\u00dferhalb der Grenzwerte"},
  { "FORMAT_FAILED", "Ausnahme bei messageFormat-Aufruf"},
  { "line", "Zeile #"},
  { "column", "Spalte #"}
  
  };

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
