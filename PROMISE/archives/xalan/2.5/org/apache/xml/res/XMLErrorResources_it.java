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
public class XMLErrorResources_it extends XMLErrorResources
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
   ER_FUNCTION_NOT_SUPPORTED, "Funzione non supportata"},
 
 /** Can't overwrite cause         */


 {
   ER_CANNOT_OVERWRITE_CAUSE,
			"Impossibile sovrascrivere la causa."},
 
  /**  No default implementation found */


 {
   ER_NO_DEFAULT_IMPL,
        "Non \u00e8 stata trovata alcuna implementazione predefinita "},

 
  /**  ChunkedIntArray({0}) not currently supported */


 {
   ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
      "ChunkedIntArray({0}) non \u00e8 correntemente supportato."},

 
  /**  Offset bigger than slot */


 {
   ER_OFFSET_BIGGER_THAN_SLOT,
      "L'offset \u00e8 maggiore dello slot."},

 
  /**  Coroutine not available, id= */


 {
   ER_COROUTINE_NOT_AVAIL,
      "Coroutine non disponibile, id={0}"},

 
  /**  CoroutineManager recieved co_exit() request */


 {
   ER_COROUTINE_CO_EXIT,
      "CoroutineManager ha ricevuto una richiesta co_exit()."},

 
  /**  co_joinCoroutineSet() failed */


 {
   ER_COJOINROUTINESET_FAILED,
      "co_joinCoroutineSet() non riuscito."},

 
  /**  Coroutine parameter error () */


 {
   ER_COROUTINE_PARAM,
      "Errore del parametro di coroutine ({0})."},

 
  /**  UNEXPECTED: Parser doTerminate answers  */


 {
   ER_PARSER_DOTERMINATE_ANSWERS,
      "\nUNEXPECTED: Risposte doTerminate del parser {0}"},

 
  /**  parse may not be called while parsing */


 {
   ER_NO_PARSE_CALL_WHILE_PARSING,
      "Impossibile chiamare l'analisi mentre \u00e8 in esecuzione."},

 
  /**  Error: typed iterator for axis  {0} not implemented  */


 {
   ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
      "Errore: tipo di iteratore per l'asse {0} non implementato."},

 
  /**  Error: iterator for axis {0} not implemented  */


 {
   ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
      "Errore: l'iteratore per l'asse {0} non \u00e8 implementato. "},

 
  /**  Iterator clone not supported  */


 {
   ER_ITERATOR_CLONE_NOT_SUPPORTED,
      "Il duplicato dell'iteratore non \u00e8 supportato."},

 
  /**  Unknown axis traversal type  */


 {
   ER_UNKNOWN_AXIS_TYPE,
      "Tipo di asse trasversale sconosciuto : {0}."},

 
  /**  Axis traverser not supported  */


 {
   ER_AXIS_NOT_SUPPORTED,
      "Attraversatore dell'asse non supportato: {0}"},

 
  /**  No more DTM IDs are available  */


 {
   ER_NO_DTMIDS_AVAIL,
      "Non sono pi\u00f9 disponibili ID DTM."},

 
  /**  Not supported  */


 {
   ER_NOT_SUPPORTED,
      "Non supportato: {0}"},

 
  /**  node must be non-null for getDTMHandleFromNode  */


 {
   ER_NODE_NON_NULL,
      "Il nodo deve essere non nullo per getDTMHandleFromNode."},

 
  /**  Could not resolve the node to a handle  */


 {
   ER_COULD_NOT_RESOLVE_NODE,
      "Impossibile risolvere il nodo a un handle"},

 
  /**  startParse may not be called while parsing */


 {
   ER_STARTPARSE_WHILE_PARSING,
      "Impossibile chiamare startParse durante l'analisi."},

 
  /**  startParse needs a non-null SAXParser  */


 {
   ER_STARTPARSE_NEEDS_SAXPARSER,
      "startParse richiede un SAXParser non nullo."},

 
  /**  could not initialize parser with */


 {
   ER_COULD_NOT_INIT_PARSER,
      "Impossibile inizializzare il parser con"},
 
  /**  exception creating new instance for pool  */


 {
   ER_EXCEPTION_CREATING_POOL,
      "l'eccezione crea una nuova istanza del pool"},

 
  /**  Path contains invalid escape sequence  */


 {
   ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
      "Il percorso contiene una sequenza di escape non valida."},

 
  /**  Scheme is required!  */


 {
   ER_SCHEME_REQUIRED,
      "Lo schema \u00e8 necessario."},

 
  /**  No scheme found in URI  */


 {
   ER_NO_SCHEME_IN_URI,
      "Nessuno schema trovato nell'URI: {0}"},

 
  /**  No scheme found in URI  */


 {
   ER_NO_SCHEME_INURI,
      "Nessuno schema trovato nell'URI"},

 
  /**  Path contains invalid character:   */


 {
   ER_PATH_INVALID_CHAR,
      "Il percorso contiene un carattere non valido: {0}"},

 
  /**  Cannot set scheme from null string  */


 {
   ER_SCHEME_FROM_NULL_STRING,
      "Impossibile impostare lo schema da una stringa nulla."},

 
  /**  The scheme is not conformant. */


 {
   ER_SCHEME_NOT_CONFORMANT,
      "Lo schema non \u00e8 conforme."},

 
  /**  Host is not a well formed address  */


 {
   ER_HOST_ADDRESS_NOT_WELLFORMED,
      "L'host non \u00e8 un indirizzo corretto."},

 
  /**  Port cannot be set when host is null  */


 {
   ER_PORT_WHEN_HOST_NULL,
      "Impossibile impostare la porta quando l'host \u00e8 nullo."},

 
  /**  Invalid port number  */


 {
   ER_INVALID_PORT,
      "Numero di porta non valido"},

 
  /**  Fragment can only be set for a generic URI  */


 {
   ER_FRAG_FOR_GENERIC_URI,
      "\u00c8 possibile impostare il frammento solo per un URI generico."},

 
  /**  Fragment cannot be set when path is null  */


 {
   ER_FRAG_WHEN_PATH_NULL,
      "Impossibile impostare il frammento quando il percorso \u00e8 nullo."},

 
  /**  Fragment contains invalid character  */


 {
   ER_FRAG_INVALID_CHAR,
      "Il frammento contiene un carattere non valido."},

 

 
  /** Parser is already in use  */


 {
   ER_PARSER_IN_USE,
       "Il parser \u00e8 gi\u00e0 in uso."},

 
  /** Parser is already in use  */


 {
   ER_CANNOT_CHANGE_WHILE_PARSING,
       "Impossibile cambiare {0} {1} durante l'analisi."},

 
  /** Self-causation not permitted  */


 {
   ER_SELF_CAUSATION_NOT_PERMITTED,
       "Non \u00e8 consentito essere causa ed effetto contemporaneamente."},

 
  /** Userinfo may not be specified if host is not specified   */


 {
   ER_NO_USERINFO_IF_NO_HOST,
       "Impossibile specificare Userinfo se non \u00e8 specificato l'host."},

 
  /** Port may not be specified if host is not specified   */


 {
   ER_NO_PORT_IF_NO_HOST,
       "Impossibile specificare la porta se non \u00e8 specificato l'host."},

 
  /** Query string cannot be specified in path and query string   */


 {
   ER_NO_QUERY_STRING_IN_PATH,
       "La stringa di query non pu\u00f2 essere specificata nella stringa di percorso e di query."},

 
  /** Fragment cannot be specified in both the path and fragment   */


 {
   ER_NO_FRAGMENT_STRING_IN_PATH,
       "Il frammento non pu\u00f2 essere specificato sia nel percorso sia nel frammento."},

 
  /** Cannot initialize URI with empty parameters   */


 {
   ER_CANNOT_INIT_URI_EMPTY_PARMS,
       "Impossibile inizializzare l'URI con parametri vuoti."},

 
 /**  Method not yet supported    */


 {
   ER_METHOD_NOT_SUPPORTED,
       "Metodo non ancora supportato. "},


 /** IncrementalSAXSource_Filter not currently restartable   */


 {
   ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
    "IncrementalSAXSource_Filter non correntemente riavviabile."},

 
 /** IncrementalSAXSource_Filter not currently restartable   */


 {
   ER_XMLRDR_NOT_BEFORE_STARTPARSE,
    "XMLReader non prima della richiesta startParse."},
  
 
 
   {
     ER_AXIS_TRAVERSER_NOT_SUPPORTED,
      "Secante asse non supportata: {0}"},
  
 
 
   {
     ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
      "ListingErrorHandler creato con PrintWriter nullo."},
  
 
 
   {
     ER_SYSTEMID_UNKNOWN,
      "ID sistema sconosciuto"},
  
 
 
   {
     ER_LOCATION_UNKNOWN,
      "Ubicazione errore sconosciuta"},


 /** Field ER_PREFIX_MUST_RESOLVE          */


 {
   ER_PREFIX_MUST_RESOLVE,
     "Il prefisso deve risolvere in namespace: {0}"},


 /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


 {
   ER_CREATEDOCUMENT_NOT_SUPPORTED,
     "createDocument() non supportato in XPathContext."},


 /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


 {
   ER_CHILD_HAS_NO_OWNER_DOCUMENT,
     "L'elemento secondario dell'attributo non ha un documento di propriet\u00e0."},


 /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


 {
   ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
     "L'elemento secondario dell'attributo non ha un elemento del documento di propriet\u00e0."},


 /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


 {
   ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
     "Avvertenza: impossibile generare un output di testo prima dell'elemento documento. Richiesta ignorata..."},


 /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


 {
   ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
     "Impossibile avere pi\u00f9 di una root su un DOM."},

 
  /**  Argument 'localName' is null  */


 {
   ER_ARG_LOCALNAME_NULL,
      "L'argomento 'localName' \u00e8 nullo"},
  

 
   /** localname in QNAME should be a valid NCName */
 
 
   {
     ER_ARG_LOCALNAME_INVALID,
        "Localname in QNAME deve essere un NCName valido"},
  
   
 
   /** prefix in QNAME should be a valid NCName */
 
 
   {
     ER_ARG_PREFIX_INVALID,
        "Prefisso in QNAME deve essere un NCName valido"},

   { "BAD_CODE",
     "I parametri di createMessage sono esterni ai limiti"},
   { "FORMAT_FAILED",
     "Eccezione generata durante la chiamata di messageFormat"},
   { "line", "Linea #"},
   { "column", "Colonna #"}
   
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
