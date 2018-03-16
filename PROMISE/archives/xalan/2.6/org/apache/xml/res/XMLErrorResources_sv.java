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
public class XMLErrorResources_sv extends XMLErrorResources
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
    ER_FUNCTION_NOT_SUPPORTED, "Funktion inte underst\u00f6dd:"},

  
  /** Can't overwrite cause         */


  {
    ER_CANNOT_OVERWRITE_CAUSE,
			"Kan inte skriva \u00f6ver orsak"},

  
   /**  No default implementation found */


  {
    ER_NO_DEFAULT_IMPL,
         "Standardimplementering saknas i:"},

  
   /**  ChunkedIntArray({0}) not currently supported */


  {
    ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
       "ChunkedIntArray({0}) underst\u00f6ds f\u00f6r n\u00e4rvarande inte"},

  
   /**  Offset bigger than slot */


  {
    ER_OFFSET_BIGGER_THAN_SLOT,
       "Offset st\u00f6rre \u00e4n fack"},

  
   /**  Coroutine not available, id= */


  {
    ER_COROUTINE_NOT_AVAIL,
       "Sidorutin inte tillg\u00e4nglig, id={0}"},

  
   /**  CoroutineManager recieved co_exit() request */


  {
    ER_COROUTINE_CO_EXIT,
       "CoroutineManager mottog co_exit()-f\u00f6rfr\u00e5gan"},

  
   /**  co_joinCoroutineSet() failed */


  {
    ER_COJOINROUTINESET_FAILED,
       "co_joinCoroutineSet() misslyckades"},

  
   /**  Coroutine parameter error () */


  {
    ER_COROUTINE_PARAM,
       "Sidorutin fick parameterfel ({0})"},

  
   /**  UNEXPECTED: Parser doTerminate answers  */


  {
    ER_PARSER_DOTERMINATE_ANSWERS,
       "\nOV\u00c4NTAT: Parser doTerminate-svar {0}"},

  
   /**  parse may not be called while parsing */


  {
    ER_NO_PARSE_CALL_WHILE_PARSING,
       "parse f\u00e5r inte anropas medan tolkning sker"},

  
   /**  Error: typed iterator for axis  {0} not implemented  */


  {
    ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "Fel: typad upprepare f\u00f6r axel {0} inte implementerad"},

  
   /**  Error: iterator for axis {0} not implemented  */


  {
    ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "Fel: upprepare f\u00f6r axel {0} inte implementerad"},

  
   /**  Iterator clone not supported  */


  {
    ER_ITERATOR_CLONE_NOT_SUPPORTED,
       "Uppreparklon underst\u00f6ds inte"},

  
   /**  Unknown axis traversal type  */


  {
    ER_UNKNOWN_AXIS_TYPE,
       "Ok\u00e4nd axeltraverstyp: {0}"},

  
   /**  Axis traverser not supported  */


  {
    ER_AXIS_NOT_SUPPORTED,
       "Axeltravers underst\u00f6ds inte: {0}"},

  
   /**  No more DTM IDs are available  */


  {
    ER_NO_DTMIDS_AVAIL,
       "Inga fler DTM-IDs \u00e4r tillg\u00e4ngliga"},

  
   /**  Not supported  */


  {
    ER_NOT_SUPPORTED,
       "Underst\u00f6ds inte: {0}"},

  
   /**  node must be non-null for getDTMHandleFromNode  */


  {
    ER_NODE_NON_NULL,
       "Nod m\u00e5ste vara icke-null f\u00f6r getDTMHandleFromNode"},

  
   /**  Could not resolve the node to a handle  */


  {
    ER_COULD_NOT_RESOLVE_NODE,
       "Kunde inte l\u00f6sa nod till ett handtag"},

  
   /**  startParse may not be called while parsing */


  {
    ER_STARTPARSE_WHILE_PARSING,
       "startParse f\u00e5r inte anropas medan tolkning sker"},

  
   /**  startParse needs a non-null SAXParser  */


  {
    ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse beh\u00f6ver en SAXParser som \u00e4r icke-null"},

  
   /**  could not initialize parser with */


  {
    ER_COULD_NOT_INIT_PARSER,
       "kunde inte initialisera tolk med"},

  
   /**  exception creating new instance for pool  */


  {
    ER_EXCEPTION_CREATING_POOL,
       "undantag skapar ny instans f\u00f6r pool"},

  
   /**  Path contains invalid escape sequence  */


  {
    ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "V\u00e4g inneh\u00e5ller ogiltig flyktsekvens"},

  
   /**  Scheme is required!  */


  {
    ER_SCHEME_REQUIRED,
       "Schema kr\u00e4vs!"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_IN_URI,
       "Schema saknas i URI: {0}"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_INURI,
       "Schema saknas i URI"},

  
   /**  Path contains invalid character:   */


  {
    ER_PATH_INVALID_CHAR,
       "V\u00e4g inneh\u00e5ller ogiltigt tecken: {0}"},

  
   /**  Cannot set scheme from null string  */


  {
    ER_SCHEME_FROM_NULL_STRING,
       "Kan inte s\u00e4tta schema fr\u00e5n null-str\u00e4ng"},

  
   /**  The scheme is not conformant. */


  {
    ER_SCHEME_NOT_CONFORMANT,
       "Schemat \u00e4r inte likformigt."},

  
   /**  Host is not a well formed address  */


  {
    ER_HOST_ADDRESS_NOT_WELLFORMED,
       "V\u00e4rd \u00e4r inte en v\u00e4lformulerad adress"},

  
   /**  Port cannot be set when host is null  */


  {
    ER_PORT_WHEN_HOST_NULL,
       "Port kan inte s\u00e4ttas n\u00e4r v\u00e4rd \u00e4r null"},

  
   /**  Invalid port number  */


  {
    ER_INVALID_PORT,
       "Ogiltigt portnummer"},

  
   /**  Fragment can only be set for a generic URI  */


  {
    ER_FRAG_FOR_GENERIC_URI,
       "Fragment kan bara s\u00e4ttas f\u00f6r en allm\u00e4n URI"},

  
   /**  Fragment cannot be set when path is null  */


  {
    ER_FRAG_WHEN_PATH_NULL,
       "Fragment kan inte s\u00e4ttas n\u00e4r v\u00e4g \u00e4r null"},

  
   /**  Fragment contains invalid character  */


  {
    ER_FRAG_INVALID_CHAR,
       "Fragment inneh\u00e5ller ogiltigt tecken"},

  
 
  
   /** Parser is already in use  */


  {
    ER_PARSER_IN_USE,
        "Tolk anv\u00e4nds redan"},

  
   /** Parser is already in use  */


  {
    ER_CANNOT_CHANGE_WHILE_PARSING,
        "Kan inte \u00e4ndra {0} {1} medan tolkning sker"},

  
   /** Self-causation not permitted  */


  {
    ER_SELF_CAUSATION_NOT_PERMITTED,
        "Sj\u00e4lvorsakande inte till\u00e5ten"},

  
   /** Userinfo may not be specified if host is not specified   */


  {
    ER_NO_USERINFO_IF_NO_HOST,
        "Userinfo f\u00e5r inte anges om v\u00e4rden inte \u00e4r angiven"},

  
   /** Port may not be specified if host is not specified   */


  {
    ER_NO_PORT_IF_NO_HOST,
        "Port f\u00e5r inte anges om v\u00e4rden inte \u00e4r angiven"},

  
   /** Query string cannot be specified in path and query string   */


  {
    ER_NO_QUERY_STRING_IN_PATH,
        "F\u00f6rfr\u00e5gan-str\u00e4ng kan inte anges i v\u00e4g och f\u00f6rfr\u00e5gan-str\u00e4ng"},

  
   /** Fragment cannot be specified in both the path and fragment   */


  {
    ER_NO_FRAGMENT_STRING_IN_PATH,
        "Fragment kan inte anges i b\u00e5de v\u00e4gen och fragmentet"},

  
   /** Cannot initialize URI with empty parameters   */


  {
    ER_CANNOT_INIT_URI_EMPTY_PARMS,
        "Kan inte initialisera URI med tomma parametrar"},

  
  /**  Method not yet supported    */


  {
    ER_METHOD_NOT_SUPPORTED,
        "Metod \u00e4nnu inte underst\u00f6dd "},


  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
     "IncrementalSAXSource_Filter kan f\u00f6r n\u00e4rvarande inte startas om"},

  
  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_XMLRDR_NOT_BEFORE_STARTPARSE,
     "XMLReader inte innan startParse-beg\u00e4ran"},



  {
    ER_AXIS_TRAVERSER_NOT_SUPPORTED,
     "Det g\u00e5r inte att v\u00e4nda axeln: {0}"},



  {
    ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
     "ListingErrorHandler skapad med null PrintWriter!"},



  {
    ER_SYSTEMID_UNKNOWN,
     "SystemId ok\u00e4nt"},



  {
    ER_LOCATION_UNKNOWN,
     "Platsen f\u00f6r felet \u00e4r ok\u00e4nd"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "Prefix must resolve to a namespace: {0}"},


  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  {
    ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "createDocument() underst\u00f6ds inte av XPathContext!"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "Attributbarn saknar \u00e4gardokument!"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "Attributbarn saknar \u00e4gardokumentelement!"},


  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  {
    ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "Varning: kan inte skriva ut text innan dokumentelement!  Ignorerar..."},


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  {
    ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "Kan inte ha mer \u00e4n en rot p\u00e5 en DOM!"},

  
   /**  Argument 'localName' is null  */


  {
    ER_ARG_LOCALNAME_NULL,
       "Argument 'localName' \u00e4r null"},



  /** localname in QNAME should be a valid NCName */


  {
    ER_ARG_LOCALNAME_INVALID,
       "Localname i QNAME b\u00f6r vara ett giltigt NCName"},

  

  /** prefix in QNAME should be a valid NCName */


  {
    ER_ARG_PREFIX_INVALID,
       "Prefixet i QNAME b\u00f6r vara ett giltigt NCName"},

  { "BAD_CODE",
      "Parameter till createMessage ligger utanf\u00f6r till\u00e5tet intervall"},
  { "FORMAT_FAILED",
      "Undantag utl\u00f6st vid messageFormat-anrop"},
  { "line",  "Rad #"},
  { "column", "Kolumn #"}
         
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
