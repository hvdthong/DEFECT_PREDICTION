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
public class XPATHErrorResources_es extends XPATHErrorResources
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
      "Esta funci\u00f3n no se permite en un patr\u00f3n de b\u00fasqueda"},


  /** Field ER_CURRENT_TAKES_NO_ARGS          */


  {
    ER_CURRENT_TAKES_NO_ARGS,
      "Esta funci\u00f3n no acepta argumentos."},


  /** Field ER_DOCUMENT_REPLACED          */


  {
    ER_DOCUMENT_REPLACED,
      "la implementaci\u00f3n de la funci\u00f3n del documento() se ha sustituido por org.apache.xalan.xslt.FuncDocument.!"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */


  {
    ER_CONTEXT_HAS_NO_OWNERDOC,
      "el contexto no tiene documento propietario!"},


  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */


  {
    ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "el nombre local tiene demasiados argumentos."},


  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */


  {
    ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "el URI del espacio de nombre tiene demasiados argumentos."},


  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */


  {
    ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "el espacio est\u00e1ndar tiene demasiados argumentos."},


  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */


  {
    ER_NUMBER_HAS_TOO_MANY_ARGS,
      "el n\u00famero tiene demasiados argumentos."},


  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */


  {
    ER_NAME_HAS_TOO_MANY_ARGS, "el nombre tiene demasiados argumentos."},


  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */


  {
    ER_STRING_HAS_TOO_MANY_ARGS,
      "la cadena tiene demasiados argumentos."},


  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */


  {
    ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "la longitud de la cadena tiene demasiados argumentos."},


  /** Field ER_TRANSLATE_TAKES_3_ARGS          */


  {
    ER_TRANSLATE_TAKES_3_ARGS,
      "La funci\u00f3n de traducci\u00f3n utiliza tres argumentos!"},


  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */


  {
    ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "La funci\u00f3n unparsed-entity-uri deber\u00eda utilizar un argumento!"},


  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */


  {
    ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "no se ha implementado todav\u00eda el eje de espacio de nombre!"},


  /** Field ER_UNKNOWN_AXIS          */


  {
    ER_UNKNOWN_AXIS, "eje desconocido: {0}"},


  /** Field ER_UNKNOWN_MATCH_OPERATION          */


  {
    ER_UNKNOWN_MATCH_OPERATION, "operaci\u00f3n de b\u00fasqueda desconocida!"},


  /** Field ER_INCORRECT_ARG_LENGTH          */


  {
    ER_INCORRECT_ARG_LENGTH,
      "La longitud de los argumentos de la prueba del nodo de instrucci\u00f3n de procesamiento () es incorrecta!"},


  /** Field ER_CANT_CONVERT_TO_NUMBER          */


  {
    ER_CANT_CONVERT_TO_NUMBER,
      "No se puede convertir {0} en un n\u00famero."},


  /** Field ER_CANT_CONVERT_TO_NODELIST          */


  {
    ER_CANT_CONVERT_TO_NODELIST,
      "No se puede convertir {0} en una NodeList."},


  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */


  {
    ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "No se puede convertir {0} en un NodeSetDTM!."},


  /** Field ER_CANT_CONVERT_TO_TYPE          */


  {
    ER_CANT_CONVERT_TO_TYPE,


  /** Field ER_EXPECTED_MATCH_PATTERN          */


  {
    ER_EXPECTED_MATCH_PATTERN,
      "Patr\u00f3n de b\u00fasqueda esperado en getMatchScore!"},


  /** Field ER_COULDNOT_GET_VAR_NAMED          */


  {
    ER_COULDNOT_GET_VAR_NAMED,
      "No se ha podido obtener una variable con el nombre {0}"},


  /** Field ER_UNKNOWN_OPCODE          */


  {
    ER_UNKNOWN_OPCODE, "ERROR! C\u00f3digo de operaci\u00f3n desconocido: {0}"},


  /** Field ER_EXTRA_ILLEGAL_TOKENS          */


  {
    ER_EXTRA_ILLEGAL_TOKENS, "Tokens adicionales no permitidos: {0}"},


  /** Field ER_EXPECTED_DOUBLE_QUOTE          */


  {
    ER_EXPECTED_DOUBLE_QUOTE,
      "error de entrecomillado... debe usar comillas dobles!"},


  /** Field ER_EXPECTED_SINGLE_QUOTE          */


  {
    ER_EXPECTED_SINGLE_QUOTE,
      "error de entrecomillado... debe usar comillas sencillas!"},


  /** Field ER_EMPTY_EXPRESSION          */


  {
    ER_EMPTY_EXPRESSION, "Expresi\u00f3n vac\u00eda!"},


  /** Field ER_EXPECTED_BUT_FOUND          */


  {
    ER_EXPECTED_BUT_FOUND, "Esperados {0}, pero encontrados: {1}"},


  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */


  {
    ER_INCORRECT_PROGRAMMER_ASSERTION,
      "El aserto del programador es incorrecto! - {0}"},


  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */


  {
    ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "El argumento booleano(...) ya no es opcional con el borrador 19990709 Xpath."},


  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "Se ha encontrado ',' pero no hay ning\u00fan argumento anterior!"},


  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "Se ha encontrado ',' pero no hay ning\u00fan argumento posterior!"},


  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */


  {
    ER_PREDICATE_ILLEGAL_SYNTAX,
      "La sintaxis '..[predicate]' no es v\u00e1lida.  Sustituir por 'self::node()[predicate]'."},


  /** Field ER_ILLEGAL_AXIS_NAME          */


  {
    ER_ILLEGAL_AXIS_NAME, "nombre de eje no v\u00e1lido: {0}"},


  /** Field ER_UNKNOWN_NODETYPE          */


  {
    ER_UNKNOWN_NODETYPE, "Tipo de nodo desconocido: {0}"},


  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */


  {
    ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "La cadena literal del patr\u00f3 ({0}) requiere entrecomillado!"},


  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */


  {
    ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "No se ha podido dar formato num\u00e9rico a {0}!"},


  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */


  {
    ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "No se ha podido crear un v\u00ednculo XML TransformerFactory: {0}"},


  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */


  {
    ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "Error! No se ha encontrado la expresi\u00f3n de selecci\u00f3n de xpath (-seleccionar)."},


  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */


  {
    ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "ERROR! No se ha podido encontra ENDOP despu\u00e9s de OP_LOCATIONPATH."},


  /** Field ER_ERROR_OCCURED          */


  {
    ER_ERROR_OCCURED, "Se ha producido un error!"},


  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */


  {
    ER_ILLEGAL_VARIABLE_REFERENCE,
      "Referencia variable atribuida a una variable fuera de contexto o sin definici\u00f3n! Nombre = {0}"},


  /** Field ER_AXES_NOT_ALLOWED          */


  {
    ER_AXES_NOT_ALLOWED,
      "S\u00f3lo se permiten los ejes child:: y attribute:: en los patrones de b\u00fasqueda!  Ejes incompatibles = {0}"},


  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */


  {
    ER_KEY_HAS_TOO_MANY_ARGS,
      "El n\u00famero de argumentos de la clave es incorrecto."},


  /** Field ER_COUNT_TAKES_1_ARG          */


  {
    ER_COUNT_TAKES_1_ARG,
      "La funci\u00f3n de c\u00f3mputo deber\u00eda utilizar un argumento!"},


  /** Field ER_COULDNOT_FIND_FUNCTION          */


  {
    ER_COULDNOT_FIND_FUNCTION, "No se ha podido encontrar la funci\u00f3n: {0}"},


  /** Field ER_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "Codificaci\u00f3n no admitida: {0}"},


  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */


  {
    ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "Se ha producido un error en DTM en getNextSibling... intentando restablecer."},


  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */


  {
    ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "Error del programador: no se puede escribir en EmptyNodeList."},


  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */


  {
    ER_SETDOMFACTORY_NOT_SUPPORTED,
      "XPathContext no admite setDOMFactory!"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "El prefijo debe convertirse en un espacio de nombre: {0}"},


  /** Field ER_PARSE_NOT_SUPPORTED          */


  {
    ER_PARSE_NOT_SUPPORTED,
      "an\u00e1lisis sint\u00e1ctico (fuente InputSource source) no admitido! No se puede abri {0}"},


  /** Field ER_SAX_API_NOT_HANDLED          */


  {
    ER_SAX_API_NOT_HANDLED,
      "Caracteres SAX API no manejados por DTM!"},


  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */


  {
    ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "Espacio en blanco que puede ignorarse no procesado por DTM!"},


  /** Field ER_DTM_CANNOT_HANDLE_NODES          */


  {
    ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison no puede manejar nodos de tipo {0}"},


  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */


  {
    ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper no puede manejar nodos de tipo {0}"},


  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */


  {
    ER_XERCES_PARSE_ERROR_DETAILS,
      "Error DOM2Helper.parse: ID del sistema - {0} l\u00ednea - {1}"},


  /** Field ER_XERCES_PARSE_ERROR          */


  {
    ER_XERCES_PARSE_ERROR, "Error DOM2Helper.parse."},


  /** Field ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "Se ha detectado un sustituto UTF-16 no v\u00e1lido: {0} ?"},


  /** Field ER_OIERROR          */


  {
    ER_OIERROR, "Error de entrada/salida."},


  /** Field ER_CANNOT_CREATE_URL          */


  {
    ER_CANNOT_CREATE_URL, "No se puede crear url para: {0}"},


  /** Field ER_XPATH_READOBJECT          */


  {
    ER_XPATH_READOBJECT, "En XPath.readObject: {0}"},

  
  /** Field ER_XPATH_READOBJECT         */


  {
    ER_FUNCTION_TOKEN_NOT_FOUND,
      "No se ha encontrado el token de funci\u00f3n."},

  
   /**  Can not deal with XPath type:   */


  {
    ER_CANNOT_DEAL_XPATH_TYPE,
       "No puede manejar el tipo XPath: {0}"},

  
   /**  This NodeSet is not mutable  */


  {
    ER_NODESET_NOT_MUTABLE,
       "Este NodeSet es inmutable."},

  
   /**  This NodeSetDTM is not mutable  */


  {
    ER_NODESETDTM_NOT_MUTABLE,
       " Este NodeSetDTM es inmutable."},

  
   /**  Variable not resolvable:   */


  {
    ER_VAR_NOT_RESOLVABLE,
        "Variable no convertible: {0}"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "Manejador de errores nulo."},

  
   /**  Programmer's assertion: unknown opcode  */


  {
    ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "Aserto del programador: c\u00f3digo de operaci\u00f3n desconocido: {0}"},

  
   /**  0 or 1   */


  {
    ER_ZERO_OR_ONE,
       "0 \u00f3r 1"},

  
    
   /**  rtf() not supported by XRTreeFragSelectWrapper   */


  {
    ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() no admitido por XRTreeFragSelectWrapper"},

  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */


  {
    ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() no admitido por XRTreeFragSelectWrapper"},

  
   /**  fsb() not supported for XStringForChars   */


  {
    ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb() no admitido para XStringForChars"},

  
   /**  Could not find variable with the name of   */


  {
    ER_COULD_NOT_FIND_VAR,
      "No se ha podido encontrar variable con el nombre {0}"},

  
   /**  XStringForChars can not take a string for an argument   */


  {
    ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars no puede utilizar una cadena para un argumento"},

  
   /**  The FastStringBuffer argument can not be null   */


  {
    ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "El argumento FastStringBuffer no puede ser nulo"},
    
  /* MANTIS_XALAN CHANGE: BEGIN */ 
   /**  2 or 3   */


  {
    ER_TWO_OR_THREE,
       "2 o 3"},


   /** Variable accessed before it is bound! */


  {
    ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "Se ha accedido a la variable antes de vincularla!"},


   /** XStringForFSB can not take a string for an argument! */


  {
    ER_FSB_CANNOT_TAKE_STRING,
       "\u00a1XStringForFSB no puede utilizar una cadena como argumento!"},


   /** Error! Setting the root of a walker to null! */


  {
    ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! \u00a1Error! Se est\u00e1 estableciendo la ra\u00edz de un walker a nulo"},


   /** This NodeSetDTM can not iterate to a previous node! */


  {
    ER_NODESETDTM_CANNOT_ITERATE,
       "\u00a1Este NodeSetDTM no puede hacer iteraciones a un nodo previo!"},


  /** This NodeSet can not iterate to a previous node! */


  {
    ER_NODESET_CANNOT_ITERATE,
       "\u00a1Este NodeSet no puede hacer iteraciones a un nodo previo!"},


  /** This NodeSetDTM can not do indexing or counting functions! */


  {
    ER_NODESETDTM_CANNOT_INDEX,
       "\u00a1Este NodeSetDTM no puede generar \u00edndices o funciones de contador!"},


  /** This NodeSet can not do indexing or counting functions! */


  {
    ER_NODESET_CANNOT_INDEX,
       "\u00a1Este NodeSet no puede generar \u00edndices ni funciones de contador!"},


  /** Can not call setShouldCacheNodes after nextNode has been called! */


  {
    ER_CANNOT_CALL_SETSHOULDCACHENODE,
      "No se puede invocar setShouldCacheNodes despu\u00e9s de haber invocado nextNode"},


  /** {0} only allows {1} arguments */


  {
    ER_ONLY_ALLOWS,
       "{0} s\u00f3lo permite {1} argumentos"},


  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */


  {
    ER_UNKNOWN_STEP,
       "Confirmaci\u00f3n del programador en getNextStepPos: stepType desconocido: {0}"},



  /** Problem with RelativeLocationPath */


  {
    ER_EXPECTED_REL_LOC_PATH,



  /** Problem with LocationPath */


  {
    ER_EXPECTED_LOC_PATH,
       "Se esperaba una ruta de destino, pero se ha encontrado el siguiente token\u003a  {0}"},



  /** Problem with Step */


  {
    ER_EXPECTED_LOC_STEP,



  /** Problem with NodeTest */


  {
    ER_EXPECTED_NODE_TEST,
       "Se esperaba una prueba de nodo coincidente con NCName:* o con QName."},



  /** Expected step pattern */


  {
    ER_EXPECTED_STEP_PATTERN,
       "Se esperaba un patr\u00f3n de pasos, pero se ha encontrado '/' ."},


 
  /** Expected relative path pattern */


  {
    ER_EXPECTED_REL_PATH_PATTERN,
       "Se esperaba un patr\u00f3n de pasos relativo."},



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */


  {
    ER_CANT_CONVERT_TO_BOOLEAN,
       "No se puede convertir {0} en una instancia booleana."},



  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */


  {
    ER_CANT_CONVERT_TO_SINGLENODE,
       "No se puede convertir {0} en un nodo \u00fanico. Este m\u00e9todo getter se aplica a los tipos ANY_UNORDERED_NODE_TYPE y FIRST_ORDERED_NODE_TYPE."},



  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */


  {
    ER_CANT_GET_SNAPSHOT_LENGTH,
       "No se puede obtener la longitud de la snapshot del tipo: {0}. Este m\u00e9todo getter se aplica a los tipos UNORDERED_NODE_SNAPSHOT_TYPE y ORDERED_NODE_SNAPSHOT_TYPE."},


  /** Field ER_NON_ITERATOR_TYPE                */


  {
    ER_NON_ITERATOR_TYPE,
       "No se puede hacer iteraciones en un tipo que no permite iteraciones: {0}"},



  /** Field ER_DOC_MUTATED                      */


  {
    ER_DOC_MUTATED,
       "El documento ha cambiado desde que se envi\u00f3 el resultado. La iteraci\u00f3n no es v\u00e1lida."},


  /** Field ER_INVALID_XPATH_TYPE               */


  {
    ER_INVALID_XPATH_TYPE,
       "Argumento de tipo XPath no v\u00e1lido: {0}"},


  /** Field ER_EMPTY_XPATH_RESULT                */


  {
    ER_EMPTY_XPATH_RESULT,
       "Objeto resultado XPath vac\u00edo"},


  /** Field ER_INCOMPATIBLE_TYPES                */


  {
    ER_INCOMPATIBLE_TYPES,
       "El tipo devuelto: {0} no se puede transformar en el tipo especificado: {1}"},


  /** Field ER_NULL_RESOLVER                     */


  {
    ER_NULL_RESOLVER,
       "No se puede resolver el prefijo con un convertidor de prefijo nulo."},



  /** Field ER_CANT_CONVERT_TO_STRING            */


  {
    ER_CANT_CONVERT_TO_STRING,
       "No se puede convertir {0} en una cadena."},



  /** Field ER_NON_SNAPSHOT_TYPE                 */


  {
    ER_NON_SNAPSHOT_TYPE,
       "No se puede invocar snapshotItem en el tipo: {0}. Este m\u00e9todo se aplica a los tipos UNORDERED_NODE_SNAPSHOT_TYPE y ORDERED_NODE_SNAPSHOT_TYPE."},



  /** Field ER_WRONG_DOCUMENT                    */


  {
    ER_WRONG_DOCUMENT,
       "El nodo de contexto no pertenece al documento vinculado a este XPathEvaluator."},


  /** Field ER_WRONG_NODETYPE                    */


  {
    ER_WRONG_NODETYPE ,
       "El tipo de nodo de contexto no es compatible."},


  /** Field ER_XPATH_ERROR                       */


  {
    ER_XPATH_ERROR ,
       "Error desconocido en XPath."},




  /** Field WG_LOCALE_NAME_NOT_HANDLED          */


  {
    WG_LOCALE_NAME_NOT_HANDLED,
      "No se ha manejado todav\u00eda el nombre locale en la funci\u00f3n!"},


  /** Field WG_PROPERTY_NOT_SUPPORTED          */


  {
    WG_PROPERTY_NOT_SUPPORTED,
      "Propiedad XSL no admitida: {0}"},


  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */


  {
    WG_DONT_DO_ANYTHING_WITH_NS,
      "Actualmente el espacio de nombres {0} en propiedad debe dejarse como est\u00e1: {1}"},


  /** Field WG_SECURITY_EXCEPTION          */


  {
    WG_SECURITY_EXCEPTION,
      "SecurityException al intentar tener acceso a la propiedad del sistema XSL: {0}"},


  /** Field WG_QUO_NO_LONGER_DEFINED          */


  {
    WG_QUO_NO_LONGER_DEFINED,
      "Sintaxis antigua: quo(...) ya no viene definida enis XPath"},


  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */


  {
    WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath requiere un objeto derivado para ejecutar nodeTest!"},


  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */


  {
    WG_FUNCTION_TOKEN_NOT_FOUND,
      "No se ha encontrado el token de la funci\u00f3n"},


  /** Field WG_COULDNOT_FIND_FUNCTION          */


  {
    WG_COULDNOT_FIND_FUNCTION,
      "No se ha podido encontrar la funci\u00f3n: {0}"},


  /** Field WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "No se puede crear URL desde: {0}"},


  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */


  {
    WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "opci\u00f3n -E no admitida para analizador sint\u00e1ctico DTM"},


  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */


  {
    WG_ILLEGAL_VARIABLE_REFERENCE,
      "VariableReference atribuida a una variable fuera de contexto o sin definici\u00f3n. Nombre = {0}"},


  /** Field WG_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "Codificaci\u00f3n no admitida: {0}"},


  { "ui_language", "es"},
  { "help_language", "es"},
  { "language", "es"},
    { "BAD_CODE",
      "El par\u00e1metro para createMessage estaba fuera de los l\u00edmites"},
    { "FORMAT_FAILED", "Excepci\u00f3n generada la llamada messageFormat"},
    { "version", ">>>>>>> Versi\u00f3n Xalan "},
    { "version2", "<<<<<<<"},
    { "yes", "s\u00ed"},
    { "xsldone", "XSLProcessor: hecho"},
    { "xpath_option", "opciones xpath: "},
    { "optionIN", "   [-in inputXMLURL]"},
    { "optionSelect", "   [- seleccionar expresi\u00f3n xpath]"},
    { "optionMatch",
      "   [-match coincidir patr\u00f3n de b\u00fasqueda (para diagn\u00f3sticos de b\u00fasqueda)]"},
    { "optionAnyExpr",
      "O una expresi\u00f3n xpath realizar\u00e1 un volcado de diagn\u00f3stico"},
    { "noParsermsg1", "Ha fallado el proceso XSLl"},
    { "noParsermsg2", "** No se ha podido encontrar analizador sint\u00e1ctico **"},
    { "noParsermsg3", "Compruebe el classpath"},
    { "noParsermsg4",
      "Si no tiene el analizador sint\u00e1ctico XML para Java de IBM, puede descargarlo desde"},
    { "noParsermsg5",
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
  public static final String ERROR_HEADER = "Error: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Advertencia: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "PATR\u00d3N ";

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


