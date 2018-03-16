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
public class XMLErrorResources_es extends XMLErrorResources
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
    ER_FUNCTION_NOT_SUPPORTED, "Funci\u00f3n no admitida"},

  /** Can't overwrite cause         */

  {
    ER_CANNOT_OVERWRITE_CAUSE,
			"No se puede sobrescribir la causa"},

   /**  No default implementation found */

  {
    ER_NO_DEFAULT_IMPL,
         "No se ha encontrado ninguna implementaci\u00f3n predeterminada "},
  
   /**  ChunkedIntArray({0}) not currently supported */

  {
    ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
       "ChunkedIntArray({0}) no se utiliza actualmente"},
  
   /**  Offset bigger than slot */

  {
    ER_OFFSET_BIGGER_THAN_SLOT,
       "La desviaci\u00f3n es mayor que el intervalo"},
  
   /**  Coroutine not available, id= */

  {
    ER_COROUTINE_NOT_AVAIL,
       "Corrutina no disponible, id={0}"},
  
   /**  CoroutineManager recieved co_exit() request */

  {
    ER_COROUTINE_CO_EXIT,
       "CoroutineManager ha recibido una solicitud co_exit()"},
  
   /**  co_joinCoroutineSet() failed */

  {
    ER_COJOINROUTINESET_FAILED,
       "Fallo co_joinCoroutineSet()"},
  
   /**  Coroutine parameter error () */

  {
    ER_COROUTINE_PARAM,
       "Error de par\u00e1metro de corrutina({0})"},
  
   /**  UNEXPECTED: Parser doTerminate answers  */

  {
    ER_PARSER_DOTERMINATE_ANSWERS,
       "\nUNEXPECTED: el analizador sint\u00e1ctico doTerminate responde {0}"},
  
   /**  parse may not be called while parsing */

  {
    ER_NO_PARSE_CALL_WHILE_PARSING,
       "no puede invocarse el analizador sint\u00e1ctico con un an\u00e1lisis sint\u00e1ctico en curso"},
  
   /**  Error: typed iterator for axis  {0} not implemented  */

  {
    ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "Error: El iterador introducido para el eje  {0} no est\u00e1 implementado"},
  
   /**  Error: iterator for axis {0} not implemented  */

  {
    ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "Error: el iterador para el eje {0} no est\u00e1 implementado "},
  
   /**  Iterator clone not supported  */

  {
    ER_ITERATOR_CLONE_NOT_SUPPORTED,
       "no se admite clon del iterador"},
  
   /**  Unknown axis traversal type  */

  {
    ER_UNKNOWN_AXIS_TYPE,
       "El tipo de eje transversal es desconocido: {0}"},
  
   /**  Axis traverser not supported  */

  {
    ER_AXIS_NOT_SUPPORTED,
       "No se admite traverser de eje: {0}"},
  
   /**  No more DTM IDs are available  */

  {
    ER_NO_DTMIDS_AVAIL,
       "No hay m\u00e1s Id de DTM disponibles"},
  
   /**  Not supported  */

  {
    ER_NOT_SUPPORTED,
       "No se admite: {0}"},
  
   /**  node must be non-null for getDTMHandleFromNode  */

  {
    ER_NODE_NON_NULL,
       "El nodo no puede ser nulo para getDTMHandleFromNode"},
  
   /**  Could not resolve the node to a handle  */

  {
    ER_COULD_NOT_RESOLVE_NODE,
       "No se ha podido convertir el nodo en un manejador"},
  
   /**  startParse may not be called while parsing */

  {
    ER_STARTPARSE_WHILE_PARSING,
       "no se puede invocar startParse con un an\u00e1lisis sint\u00e1ctico en curso"},
  
   /**  startParse needs a non-null SAXParser  */

  {
    ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse no admite SAXParser nulo"},
  
   /**  could not initialize parser with */

  {
    ER_COULD_NOT_INIT_PARSER,
       "No se ha podido inicializar el analizador sint\u00e1ctico con"},
  
   /**  exception creating new instance for pool  */

  {
    ER_EXCEPTION_CREATING_POOL,
       "se ha producido una excepci\u00f3n al crear una nueva instancia para pool"},
  
   /**  Path contains invalid escape sequence  */

  {
    ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "El trayecto contiene una secuencia de escape no v\u00e1lida"},
  
   /**  Scheme is required.  */

  {
    ER_SCHEME_REQUIRED,
       "Se necesita un esquema."},
  
   /**  No scheme found in URI  */

  {
    ER_NO_SCHEME_IN_URI,
       "No se ha encontrado ning\u00fan esquema en el URI: {0}"},
  
   /**  No scheme found in URI  */

  {
    ER_NO_SCHEME_INURI,
       "No se ha encontrado ning\u00fan esquema en el URI"},
  
   /**  Path contains invalid character:   */

  {
    ER_PATH_INVALID_CHAR,
       "El trayecto contiene un car\u00e1cter no v\u00e1lido: {0}"},
  
   /**  Cannot set scheme from null string  */

  {
    ER_SCHEME_FROM_NULL_STRING,
       "No se puede establecer un esquema a partir de una cadena nula"},
  
   /**  The scheme is not conformant. */

  {
    ER_SCHEME_NOT_CONFORMANT,
       "El esquema no es aceptable."},
  
   /**  Host is not a well formed address  */

  {
    ER_HOST_ADDRESS_NOT_WELLFORMED,
       "El sistema central no es una direcci\u00f3n bien construida"},
  
   /**  Port cannot be set when host is null  */

  {
    ER_PORT_WHEN_HOST_NULL,
       "No puede establecerse el puerto cuando el sistema central es nulo"},
  
   /**  Invalid port number  */

  {
    ER_INVALID_PORT,
       "N\u00famero de puerto no v\u00e1lido"},
  
   /**  Fragment can only be set for a generic URI  */

  {
    ER_FRAG_FOR_GENERIC_URI,
       "S\u00f3lo puede establecerse el fragmento para un URI gen\u00e9rico"},
  
   /**  Fragment cannot be set when path is null  */

  {
    ER_FRAG_WHEN_PATH_NULL,
       "No puede establecerse el fragmento cuando el trayecto es nulo"},
  
   /**  Fragment contains invalid character  */

  {
    ER_FRAG_INVALID_CHAR,
       "El fragmento contiene un car\u00e1cter no v\u00e1lido"}, 
  
   /** Parser is already in use  */

  {
    ER_PARSER_IN_USE,
        "El analizador sint\u00e1ctico est\u00e1 en uso"},
  
   /** Parser is already in use  */

  {
    ER_CANNOT_CHANGE_WHILE_PARSING,
        "No se puede cambiar {0} {1} mientras el an\u00e1lisis sint\u00e1ctico est\u00e1 en curso"},
  
   /** Self-causation not permitted  */

  {
    ER_SELF_CAUSATION_NOT_PERMITTED,
        "No se permite la autocausalidad"},
  
   /** Userinfo may not be specified if host is not specified   */

  {
    ER_NO_USERINFO_IF_NO_HOST,
        "La informaci\u00f3n de usuario no puede especificarse si no se especifica el sistema central"},
  
   /** Port may not be specified if host is not specified   */

  {
    ER_NO_PORT_IF_NO_HOST,
        "El puerto no puede especificarse si no est\u00e1 especificado el sistema central"},
  
   /** Query string cannot be specified in path and query string   */

  {
    ER_NO_QUERY_STRING_IN_PATH,
        "La cadena de consulta no puede especificarse a la vez en el trayecto y en la cadena de consulta"},
  
   /** Fragment cannot be specified in both the path and fragment   */

  {
    ER_NO_FRAGMENT_STRING_IN_PATH,
        "El fragmento no puede especificarse a la vez en el trayecto y en el fragmento"},
  
   /** Cannot initialize URI with empty parameters   */

  {
    ER_CANNOT_INIT_URI_EMPTY_PARMS,
        "No se puede inicializar el URI con par\u00e1metros vac\u00edos"},
  
  /**  Method not yet supported    */

  {
    ER_METHOD_NOT_SUPPORTED,
        "M\u00e9todo todav\u00eda no utilizado"},

  /** IncrementalSAXSource_Filter not currently restartable   */

  {
    ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
     "IncrementalSAXSource_Filter no puede reiniciarse actualmente"},
  
  /** IncrementalSAXSource_Filter not currently restartable   */

  {
    ER_XMLRDR_NOT_BEFORE_STARTPARSE,
     "XMLReader no antes de una solicitud startParse"},

  {
    ER_AXIS_TRAVERSER_NOT_SUPPORTED,
     "No se admite el eje transversal: {0}"},

  {
    ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
     "ListingErrorHandler creado con PrintWriter nulo"},

  {
    ER_SYSTEMID_UNKNOWN,
     "Id de sistema desconocido"},

  {
    ER_LOCATION_UNKNOWN,
     "Ubicaci\u00f3n del error desconocida"},

  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "El prefijo debe convertirse en un espacio de nombre: {0}"},

  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  {
    ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "createDocument no admitido en XPathContext."},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "El atributo hijo no tiene documento propietario!"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "El atributo hijo no tiene elemento de documento propietario!"},

  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  {
    ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "Advertencia: no puede enviar el texto antes del elemento de documento. Se ignora..."},


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  {
    ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "No puede haber m\u00e1s de una ra\u00edz en un DOM."},

  
   /**  Argument 'localName' is null  */


  {
    ER_ARG_LOCALNAME_NULL,
       "El argumentoArgument 'localName' es nulo."},


  /** localname in QNAME should be a valid NCName */


  {
    ER_ARG_LOCALNAME_INVALID,
       "El nombre local especificado en QNAME debe ser un nombre NCName v\u00e1lido"},
  

  /** prefix in QNAME should be a valid NCName */


  {
    ER_ARG_PREFIX_INVALID,
       "El prefijo especificado en QNAME debe ser un nombre NCName v\u00e1lido"},

  { "BAD_CODE",
      "El par\u00e1metro para crear el mensaje estaba fuera de los l\u00edmites"},
  { "FORMAT_FAILED",
      "Excepci\u00f3n generada durante la llamada messageFormat"},
  { "line", "L\u00ednea #"},
  { "column", "Columna #"}
  
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
