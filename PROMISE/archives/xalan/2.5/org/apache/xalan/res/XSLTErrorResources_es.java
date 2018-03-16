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
public class XSLTErrorResources_es extends XSLTErrorResources
{

  /** Maximum error messages, this is needed to keep track of the number of messages.    */
  public static final int MAX_CODE = 201;          

  /** Maximum warnings, this is needed to keep track of the number of warnings.          */
  public static final int MAX_WARNING = 29;

  /** Maximum misc strings.   */
  public static final int MAX_OTHERS = 55;

  /** Maximum total warnings and error messages.          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;

  /** The lookup table for error messages.   */

  public static final Object[][] contents = {
  /** Error message ID that has a null message, but takes in a single object.    */

  {
    "ERROR0000", "{0}"},

  /** ER_NO_CURLYBRACE          */

  {
    ER_NO_CURLYBRACE,
      "Error: No se puede incluir '{' en una expresi\u00f3n"},

  /** ER_ILLEGAL_ATTRIBUTE          */

  {
    ER_ILLEGAL_ATTRIBUTE, "{0} tiene un atributo no permitido: {1}"},

  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */

  {
    ER_NULL_SOURCENODE_APPLYIMPORTS,
      "sourceNode es nulo en xsl:apply-imports."},

  /** ER_CANNOT_ADD          */

  {
    ER_CANNOT_ADD, "No se puede a\u00f1adir {0} a {1}"},

  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */

  {
    ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "sourceNode es nulo en handleApplyTemplatesInstruction."},

  /** ER_NO_NAME_ATTRIB          */

  {
    ER_NO_NAME_ATTRIB, "{0} debe tener un atributo de nombre."},

  /** ER_TEMPLATE_NOT_FOUND          */

  {
    ER_TEMPLATE_NOT_FOUND, "No se ha encontrado ninguna plantilla con el nombre: {0}"},

  /** ER_CANT_RESOLVE_NAME_AVT          */

  {
    ER_CANT_RESOLVE_NAME_AVT,
      "No se ha podido convertir el nombre AVT en xsl:call-template."},

  /** ER_REQUIRES_ATTRIB          */

  {
    ER_REQUIRES_ATTRIB, "{0} necesita un atributo: {1}"},

  /** ER_MUST_HAVE_TEST_ATTRIB          */

  {
    ER_MUST_HAVE_TEST_ATTRIB,
      "{0} debe tener un atributo ''test''."},

  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */

  {
    ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "Valor err\u00f3neo en un atributo de nivel: {0}"},

  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */

  {
    ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "el nombre de la instrucci\u00f3n de procesamiento no puede ser 'xml'"},

  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */

  {
    ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "el nombre de la instrucci\u00f3n de procesamiento debe ser un NCName v\u00e1lido: {0}"},

  /** ER_NEED_MATCH_ATTRIB          */

  {
    ER_NEED_MATCH_ATTRIB,
      "{0} debe tener un atributo de b\u00fasqueda si tiene un modo."},

  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */

  {
    ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} necesita un atributo de nombre o de b\u00fasqueda."},

  /** ER_CANT_RESOLVE_NSPREFIX          */

  {
    ER_CANT_RESOLVE_NSPREFIX,
      "No se puede convertir el prefijo de espacio de nombre: {0}"},

  /** ER_ILLEGAL_VALUE          */

  {
    ER_ILLEGAL_VALUE, "xml:space tiene un valor no permitido: {0}"},

  /** ER_NO_OWNERDOC          */

  {
    ER_NO_OWNERDOC,
      "El nodo hijo no tiene documento propietario."},

  /** ER_ELEMTEMPLATEELEM_ERR          */

  {
    ER_ELEMTEMPLATEELEM_ERR, "Error ElemTemplateElement: {0}"},

  /** ER_NULL_CHILD          */

  {
    ER_NULL_CHILD, "Intentando agregar un hijo nulo."},

  /** ER_NEED_SELECT_ATTRIB          */

  {
    ER_NEED_SELECT_ATTRIB, "{0} necesita un atributo de selecci\u00f3n."},

  /** ER_NEED_TEST_ATTRIB          */


  {
    ER_NEED_TEST_ATTRIB,
      "xsl:when debe tener un atributo 'test'."},

  /** ER_NEED_NAME_ATTRIB          */

  {
    ER_NEED_NAME_ATTRIB,
      "xsl:with-param debe tener un atributo 'name'."},

  /** ER_NO_CONTEXT_OWNERDOC          */

  {
    ER_NO_CONTEXT_OWNERDOC,
      "el contexto no tiene documento propietario."},

  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */

  {
    ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "No se ha podido crear un v\u00ednculo XML TransformerFactory: {0}"},

  /** ER_PROCESS_NOT_SUCCESSFUL          */

  {
    ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan: El proceso ha fallado."},

  /** ER_NOT_SUCCESSFUL          */

  {
    ER_NOT_SUCCESSFUL, "Xalan: ha fallado."},

  /** ER_ENCODING_NOT_SUPPORTED          */

  {
    ER_ENCODING_NOT_SUPPORTED, "Codificaci\u00f3n no admitida: {0}"},

  /** ER_COULD_NOT_CREATE_TRACELISTENER          */

  {
    ER_COULD_NOT_CREATE_TRACELISTENER,
      "No se ha podido crear TraceListener: {0}"},

  /** ER_KEY_REQUIRES_NAME_ATTRIB          */

  {
    ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key necesita un atributo 'name'."},

  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


  {
    ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key necesita un atributo 'match'."},

  /** ER_KEY_REQUIRES_USE_ATTRIB          */

  {
    ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key necesita un atributo 'use'."},

  /** ER_REQUIRES_ELEMENTS_ATTRIB          */

  {
    ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} necesita un atributo ''elements''."},

  /** ER_MISSING_PREFIX_ATTRIB          */

  {
    ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) {0} falta el atributo ''prefix''"},

  /** ER_BAD_STYLESHEET_URL          */

  {
    ER_BAD_STYLESHEET_URL, "El URL de la hoja de estilo es err\u00f3neo: {0}"},

  /** ER_FILE_NOT_FOUND          */

  {
    ER_FILE_NOT_FOUND, "No se ha encontrado el archivo de la hoja de estilo: {0}"},

  /** ER_IOEXCEPTION          */

  {
    ER_IOEXCEPTION,
      "Ten\u00eda una excepci\u00f3n E/S en el archivo de la hoja de estilo: {0}"},

  /** ER_NO_HREF_ATTRIB          */

  {
    ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) No se ha encontrado el atributo href para {0}"},

  /** ER_STYLESHEET_INCLUDES_ITSELF          */

  {
    ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} se incluye a s\u00ed mismo directa o indirectamente."},

  /** ER_PROCESSINCLUDE_ERROR          */

  {
    ER_PROCESSINCLUDE_ERROR,
      "Error StylesheetHandler.processInclude, {0}"},

  /** ER_MISSING_LANG_ATTRIB          */

  {
    ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) {0} falta el atributo ''lang''"},

  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */

  {
    ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) \u00bfelemento {0} mal colocado? Falta el elemento ''component'' del contenedor"},

  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */

  {
    ER_CAN_ONLY_OUTPUT_TO_ELEMENT,

      "S\u00f3lo puede enviarse a Element, DocumentFragment, Document o PrintWriter."},

  /** ER_PROCESS_ERROR          */

  {
    ER_PROCESS_ERROR, "Error StylesheetRoot.process"},

  /** ER_UNIMPLNODE_ERROR          */

  {
    ER_UNIMPLNODE_ERROR, "Error UnImplNode: {0}"},

  /** ER_NO_SELECT_EXPRESSION          */

  {
    ER_NO_SELECT_EXPRESSION,
      "Error. No se ha encontrado la expresi\u00f3n de selecci\u00f3n xpath (-seleccionar)."},

  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */

  {
    ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "No se puede serializar un XSLProcessor."},

  /** ER_NO_INPUT_STYLESHEET          */

  {
    ER_NO_INPUT_STYLESHEET,
      "No se ha especificado la entrada de la hoja de estilo."},

  /** ER_FAILED_PROCESS_STYLESHEET          */

  {
    ER_FAILED_PROCESS_STYLESHEET,
      "No se ha podido procesar la hoja de estilo."},

  /** ER_COULDNT_PARSE_DOC          */

  {
    ER_COULDNT_PARSE_DOC, "No se ha podido analizar sint\u00e1cticamente el documento {0}."},

  /** ER_COULDNT_FIND_FRAGMENT          */

  {
    ER_COULDNT_FIND_FRAGMENT, "No se ha encontrado el fragmento: {0}"},

  /** ER_NODE_NOT_ELEMENT          */

  {
    ER_NODE_NOT_ELEMENT,
      "El nodo se\u00f1alado por el identificador de fragmento no era un elemento: {0}"},

  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */

  {
    ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each debe tener un atributo de b\u00fasqueda o de nombre"},

  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */

  {
    ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "las plantillas deben tener un atributo de b\u00fasqueda o de nombre"},

  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */

  {
    ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "No existe clon de un fragmento de un documento"},

  /** ER_CANT_CREATE_ITEM          */

  {
    ER_CANT_CREATE_ITEM,
      "No se puede crear el elemento en el \u00e1rbol de resultados: {0}"},

  /** ER_XMLSPACE_ILLEGAL_VALUE          */

  {
    ER_XMLSPACE_ILLEGAL_VALUE,
      "xml:space en el XML fuente tiene un valor no permitido: {0}"},

  /** ER_NO_XSLKEY_DECLARATION          */

  {
    ER_NO_XSLKEY_DECLARATION,
      "No existe ninguna declaraci\u00f3n xsl:key para {0}."},

  /** ER_CANT_CREATE_URL          */

  {
    ER_CANT_CREATE_URL, "Error. No se puede crear el url para: {0}"},

  /** ER_XSLFUNCTIONS_UNSUPPORTED          */

  {
    ER_XSLFUNCTIONS_UNSUPPORTED, "xsl:functions no se admite"},

  /** ER_PROCESSOR_ERROR          */

  {
    ER_PROCESSOR_ERROR, "Error XSLT TransformerFactory"},

  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */

  {
    ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} no se permite en una hoja de estilo."},

  /** ER_RESULTNS_NOT_SUPPORTED          */

  {
    ER_RESULTNS_NOT_SUPPORTED,
      "result-ns ya no se utiliza.  Utilizar en su lugar xsl:output"},

  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */

  {
    ER_DEFAULTSPACE_NOT_SUPPORTED,
      "default-space ya no se utiliza.  Utilizar en su lugar xsl:strip-space o xsl:preserve-space"},

  /** ER_INDENTRESULT_NOT_SUPPORTED          */

  {
    ER_INDENTRESULT_NOT_SUPPORTED,
      "indent-result ya no se utiliza. Utilizar en su lugar xsl:output"},

  /** ER_ILLEGAL_ATTRIB          */

  {
    ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} tiene un atributo no permitido: {1}"},

  /** ER_UNKNOWN_XSL_ELEM          */

  {
    ER_UNKNOWN_XSL_ELEM, "Elemento XSL desconocido: {0}"},

  /** ER_BAD_XSLSORT_USE          */

  {
    ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort s\u00f3lo puede utilizarse con xsl:apply-templates o con xsl:for-each."},

  /** ER_MISPLACED_XSLWHEN          */

  {
    ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) xsl:when mal colocado."},

  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */

  {
    ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:when no es hijo de xsl:choose."},

  /** ER_MISPLACED_XSLOTHERWISE          */

  {
    ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) xsl:otherwise mal colocado."},

  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */

  {
    ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:otherwise no es hijo de xsl:choose."},

  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */

  {
    ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} no se permite en una plantilla."},

  /** ER_UNKNOWN_EXT_NS_PREFIX          */

  {
    ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) {0} prejijo de espacio de nombre de extensi\u00f3n {1} desconocido"},

  /** ER_IMPORTS_AS_FIRST_ELEM          */

  {
    ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) Las importaciones s\u00f3lo pueden ser los primeros elementos de la hoja de estilo."},

  /** ER_IMPORTING_ITSELF          */

  {
    ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} se importa a s\u00ed mismo directa o indirectamente."},

  /** ER_XMLSPACE_ILLEGAL_VAL          */

  {
    ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) " + "xml:space tiene un valor no permitido: {0}"},


  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */

  {
    ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "processStylesheet ha fallado."},

  /** ER_SAX_EXCEPTION          */

  {
    ER_SAX_EXCEPTION, "Excepci\u00f3n SAX"},

  /** ER_XSLT_ERROR          */

  {
    ER_XSLT_ERROR, "Error XSLT"},

  /** ER_CURRENCY_SIGN_ILLEGAL          */

  {
    ER_CURRENCY_SIGN_ILLEGAL,
      "el signo de divisa no se permite en la cadena de patrones de formato"},

  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */

  {
    ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "La hoja de estilo DOM no admite la funci\u00f3n de documento."},

  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */

  {
    ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "No se puede convertir el prefijo del convertidor de ausencia de prefijo."},

  /** ER_REDIRECT_COULDNT_GET_FILENAME          */

  {
    ER_REDIRECT_COULDNT_GET_FILENAME,
      "Reencaminar extensi\u00f3n : No se ha podido obtener el nombre del archivo - el atributo de archivo o de selecci\u00f3n debe presentar una cadena v\u00e1lida."},

  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */

  {
    ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "No se puede crear FormatterListener en extensi\u00f3n Redirect."},

  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */

  {
    ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "El prefijo de exclude-result-prefixes no es v\u00e1lido: {0}"},

  /** ER_MISSING_NS_URI          */

  {
    ER_MISSING_NS_URI,
      "Falta el URI de espacio de nombre del prefijo especificado"},

  /** ER_MISSING_ARG_FOR_OPTION          */

  {
    ER_MISSING_ARG_FOR_OPTION,
      "Falta el argumento en la opci\u00f3n: {0}"},

  /** ER_INVALID_OPTION          */

  {
    ER_INVALID_OPTION, "Opci\u00f3n no v\u00e1lida: {0}"},

  /** ER_MALFORMED_FORMAT_STRING          */

  {
    ER_MALFORMED_FORMAT_STRING, "Cadena de formato mal construida: {0}"},

  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

  {
    ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet necesita un atributo 'version'."},

  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */

  {
    ER_ILLEGAL_ATTRIBUTE_VALUE,
      "El atributo: {0} tiene un valor no permitido: {1}"},

  /** ER_CHOOSE_REQUIRES_WHEN          */

  {
    ER_CHOOSE_REQUIRES_WHEN, "xsl:choose requiere xsl:when"},

  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */

  {
    ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports no se permite en xsl:for-each"},

  /** ER_CANT_USE_DTM_FOR_OUTPUT          */

  {
    ER_CANT_USE_DTM_FOR_OUTPUT,
      "No se puede utilizar DTMLiaison con un nodo DOM de salida... utilizar en su lugar org.apache.xpath.DOM2Helper."},

  /** ER_CANT_USE_DTM_FOR_INPUT          */


  {
    ER_CANT_USE_DTM_FOR_INPUT,
      "No se puede utilizar DTMLiaison con un nodo DOM de salida... utilizar en su lugar org.apache.xpath.DOM2Helper."},

  /** ER_CALL_TO_EXT_FAILED          */

  {
    ER_CALL_TO_EXT_FAILED,
      "Ha fallado el elemento de llamada a la extensi\u00f3n: {0}"},

  /** ER_PREFIX_MUST_RESOLVE          */

  {
    ER_PREFIX_MUST_RESOLVE,
      "El prefijo debe convertir un espacio de nombre: {0}"},

  /** ER_INVALID_UTF16_SURROGATE          */

  {
    ER_INVALID_UTF16_SURROGATE,
      "Se ha detectado un sustituto de UTF-16 no v\u00e1lido: {0} ?"},

  /** ER_XSLATTRSET_USED_ITSELF          */

  {
    ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} se utiliza a s\u00ed mismo y provocar\u00e1 un bucle sin fin."},

  /** ER_CANNOT_MIX_XERCESDOM          */

  {
    ER_CANNOT_MIX_XERCESDOM,
      "No se puede mezclar una entrada no Xerces-DOM con una salida Xerces-DOM."},

  /** ER_TOO_MANY_LISTENERS          */

  {
    ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},

  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */

  {
    ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "En ElemTemplateElement.readObject: {0}"},


  /** ER_DUPLICATE_NAMED_TEMPLATE          */

  {
    ER_DUPLICATE_NAMED_TEMPLATE,
      "Se ha encontrado m\u00e1s de una plantilla con el nombre: {0}"},

  /** ER_INVALID_KEY_CALL          */

  {
    ER_INVALID_KEY_CALL,
      "Llamada a funci\u00f3n no v\u00e1lida: no se permiten las llamadas con clave recursiva()"},
  
  /** Variable is referencing itself          */

  {
    ER_REFERENCING_ITSELF,
      "La variable {0} se refiere a s\u00ed misma directa o indirectamente."},
  
  /** Illegal DOMSource input          */

  {
    ER_ILLEGAL_DOMSOURCE_INPUT,
      "El nodo de entrada no puede ser nulo para DOMSource en newTemplates."},
	
	/** Class not found for option         */

  {
    ER_CLASS_NOT_FOUND_FOR_OPTION,
			"No se ha encontrado el archivo de clase para la opci\u00f3n {0}"},
	
	/** Required Element not found         */

  {
    ER_REQUIRED_ELEM_NOT_FOUND,
			"No se ha encontrado el elemento requerido: {0}"},
  
  /** InputStream cannot be null         */

  {
    ER_INPUT_CANNOT_BE_NULL,
			"InputStream no puede ser nulo"},
  
  /** URI cannot be null         */

  {
    ER_URI_CANNOT_BE_NULL,
			"URI no puede ser nulo"},
  
  /** File cannot be null         */

  {
    ER_FILE_CANNOT_BE_NULL,
			"El archivo no puede ser nulo"},
  
   /** InputSource cannot be null         */

  {
    ER_SOURCE_CANNOT_BE_NULL,
			"InputSource no puede ser nulo"},
    
  /** Could not initialize BSF Manager        */

  {
    ER_CANNOT_INIT_BSFMGR,
			"No se ha podido inicializar el administrador de BSF"},
  
  /** Could not compile extension       */

  {
    ER_CANNOT_CMPL_EXTENSN,
			"No se ha podido compilar la extensi\u00f3n"},
  

  /** Could not create extension       */

  {
    ER_CANNOT_CREATE_EXTENSN,
      "No se ha podido crear la extensi\u00f3n: {0} debido a: {1}"},
  
  /** Instance method call to method {0} requires an Object instance as first argument       */

  {
    ER_INSTANCE_MTHD_CALL_REQUIRES,
      "El primer argumento de la llamada del m\u00e9todo de instancia al m\u00e9todo {0} necesita una instancia de objeto"},
  
  /** Invalid element name specified       */

  {
    ER_INVALID_ELEMENT_NAME,
      "Se ha especificado un nombre de elemento no v\u00e1lido {0}"},
  
   /** Element name method must be static      */

  {
    ER_ELEMENT_NAME_METHOD_STATIC,
      "El m\u00e9todo del nombre de elemento debe ser est\u00e1tico {0}"},
  
   /** Extension function {0} : {1} is unknown      */

  {
    ER_EXTENSION_FUNC_UNKNOWN,
             "La funci\u00f3n de extensi\u00f3n {0} : {1} es desconocida"},
  
   /** More than one best match for constructor for       */

  {
    ER_MORE_MATCH_CONSTRUCTOR,
             "Hay m\u00e1s de una coincidencia \u00f3ptima para el creador en {0}"},
  
   /** More than one best match for method      */

  {
    ER_MORE_MATCH_METHOD,
             "Hay m\u00e1s de una coincidencia \u00f3ptima para el m\u00e9todo {0}"},
  
   /** More than one best match for element method      */

  {
    ER_MORE_MATCH_ELEMENT,
             "Hay m\u00e1s de una coincidencia \u00f3ptima para el m\u00e9todo del elemento {0}"},
  
   /** Invalid context passed to evaluate       */

  {
    ER_INVALID_CONTEXT_PASSED,
             "El contexto no v\u00e1lido se ha pasado a evaluaci\u00f3n {0}"},
  
   /** Pool already exists       */

  {
    ER_POOL_EXISTS,
             "El pool ya existe"},
  
   /** No driver Name specified      */

  {
    ER_NO_DRIVER_NAME,
             "No se ha especificado ning\u00fan nombre para el dispositivo"},
  
   /** No URL specified     */

  {
    ER_NO_URL,
             "No se ha especificado ning\u00fan URL"},
  
   /** Pool size is less than one    */

  {
    ER_POOL_SIZE_LESSTHAN_ONE,
             "El tama\u00f1o del pool es menor que uno."},
  
   /** Invalid driver name specified    */

  {
    ER_INVALID_DRIVER,
             "Se ha especificado un nombre de dispositivo no v\u00e1lido."},
  
   /** Did not find the stylesheet root    */

  {
    ER_NO_STYLESHEETROOT,
             "No se ha encontrado la ra\u00edz de la hoja de estilo."},
  
   /** Illegal value for xml:space     */

  {
    ER_ILLEGAL_XMLSPACE_VALUE,
         "Valor no permitido para xml:space"},
  
   /** processFromNode failed     */

  {
    ER_PROCESSFROMNODE_FAILED,
         "Fallo de processFromNode"},
  
   /** The resource [] could not load:     */

  {
    ER_RESOURCE_COULD_NOT_LOAD,
        "El recurso [ {0} ] no ha podido cargar: {1} \n {2} \t {3}"},
   
  
   /** Buffer size <=0     */

  {
    ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "Tama\u00f1o del b\u00fafer <=0"},
  
   /** Unknown error when calling extension    */

  {
    ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "Error desconocido al llamar a la extensi\u00f3n"},
  
   /** Prefix {0} does not have a corresponding namespace declaration    */

  {
    ER_NO_NAMESPACE_DECL,
        "El prefijo {0} no tiene la declaraci\u00f3n de espacio de nombre correspondiente"},
  
   /** Element content not allowed for lang=javaclass   */

  {
    ER_ELEM_CONTENT_NOT_ALLOWED,
        "El contenido del elemento no est\u00e1 permitido para lang=javaclass {0}"},
  
   /** Stylesheet directed termination   */

  {
    ER_STYLESHEET_DIRECTED_TERMINATION,
        "Terminaci\u00f3n dirigida a la hoja de estilo"},
  
   /** 1 or 2   */

  {
    ER_ONE_OR_TWO,
        "1 \u00f3 2"},
  
   /** 2 or 3   */

  {
    ER_TWO_OR_THREE,
        "2 \u00f3 3"},

  
   /** Could not load {0} (check CLASSPATH), now using just the defaults   */

  {
    ER_COULD_NOT_LOAD_RESOURCE,
        "No se ha podido cargar {0} (comprobar CLASSPATH), el sistema est\u00e1 utilizando los valores predeterminados"},
  
   /** Cannot initialize default templates   */

  {
    ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "No se puede inicializar las plantillas predeterminadas"},
  
   /** Result should not be null   */

  {
    ER_RESULT_NULL,
        "El resultado no debe ser nulo"},
    
   /** Result could not be set   */

  {
    ER_RESULT_COULD_NOT_BE_SET,
        "No ha podido establecerse el resultado"},
  
   /** No output specified   */


  {
    ER_NO_OUTPUT_SPECIFIED,
        "No se ha especificado ninguna salida"},
  
   /** Can't transform to a Result of type   */

  {
    ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "No se puede transformar en un resultado del tipo {0}"},
  
   /** Can't transform to a Source of type   */

  {
    ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "No se puede transformar una fuente del tipo {0}"},
  
   /** Null content handler  */

  {
    ER_NULL_CONTENT_HANDLER,
        "Manejador de contenido nulo"},
  
   /** Null error handler  */

  {
    ER_NULL_ERROR_HANDLER,
        "Manejador de errores nulo"},
  
   /** parse can not be called if the ContentHandler has not been set */

  {
    ER_CANNOT_CALL_PARSE,
        "no puede invocarse el analizador sint\u00e1ctico si no se ha establecido el ContentHandler"},
  
   /**  No parent for filter */

  {
    ER_NO_PARENT_FOR_FILTER,
        "No existe ning\u00fan elemento padre para el filtro"},
  
  
   /**  No stylesheet found in: {0}, media */

  {
    ER_NO_STYLESHEET_IN_MEDIA,
         "No se ha encontrado ninguna hoja de estilo en: {0}, media= {1}"},
  
   /**  No xml-stylesheet PI found in */

  {
    ER_NO_STYLESHEET_PI,
         "No se ha encontrado xml-stylesheet PI en: {0}"},
  
   /**  Not supported  */

  {
    ER_NOT_SUPPORTED,
       "No se admite: {0}"},
    
   /**  Value for property {0} should be a Boolean instance  */

  {
    ER_PROPERTY_VALUE_BOOLEAN,
       "El valor de propiedad {0} debe ser una instancia booleana"},
  
  /* This key/message changed ,NEED ER_COULD_NOT_FIND_EXTERN_SCRIPT: Pending,Ra
mesh */
   /** src attribute not yet supported for  */

  {
    "ER_SRC_ATTRIB_NOT_SUPPORTED",
       "el atributo src no se permite todav\u00eda para {0}"},
  
  /** The resource [] could not be found     */

  {
    ER_RESOURCE_COULD_NOT_FIND,
        "No se ha encontrado el recurso [ {0} ].\n {1}"},
  
   /** output property not recognized:  */

  {
    ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "Propiedad de salida no reconocida: {0}"},
  
   /** Failed creating ElemLiteralResult instance   */

  {
    ER_FAILED_CREATING_ELEMLITRSLT,
        "Fallo de creaci\u00f3n de instancia ElemLiteralResult"},


   /** Priority value does not contain a parsable number   */

  {
    ER_VALUE_SHOULD_BE_NUMBER,
        "El valor de {0} debe contener un n\u00famero que se pueda analizar"},

  
   /**  Value for {0} should equal 'yes' or 'no'   */

  {
    ER_VALUE_SHOULD_EQUAL,
        " El valor de {0} debe ser igual a s\u00ed o no"},
 
   /**  Failed calling {0} method   */

  {
    ER_FAILED_CALLING_METHOD,
        " Fallo de invocaci\u00f3n del m\u00e9todo {0}"},
  
   /** Failed creating ElemLiteralResult instance   */

  {
    ER_FAILED_CREATING_ELEMTMPL,
        "Fallo de creaci\u00f3n de instancia ElemTemplateElement"},
  
   /**  Characters are not allowed at this point in the document   */

  {
    ER_CHARS_NOT_ALLOWED,
        "No se permiten caracteres en esta parte del documento"},
  
  /**  attribute is not allowed on the element   */

  {
    ER_ATTR_NOT_ALLOWED,
        "el atributo \"{0}\" no se permite en el elemento {1}."},
 
  /**  Bad value    */

  {
    ER_BAD_VALUE,
     "{0} valor err\u00f3neo {1} "},
  
  /**  attribute value not found   */

  {
    ER_ATTRIB_VALUE_NOT_FOUND,
     "no se ha encontrado el valor del atributo {0}"},
  
  /**  attribute value not recognized    */

  {
    ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "no se reconoce el valor del atributo {0}"},

  /** Attempting to generate a namespace prefix with a null URI   */

  {
    ER_NULL_URI_NAMESPACE,
     "Se ha intentado generar un prefijo de espacio de nombre con un URI nulo"},


  /** Attempting to generate a namespace prefix with a null URI   */

  {
    ER_NUMBER_TOO_BIG,
     "Se ha intentado dar formato a un n\u00famero mayor que el entero Long de mayor tama\u00f1o"},



  {
    ER_CANNOT_FIND_SAX1_DRIVER,
     "No se puede hallar la clase {0} del controlador SAX1"},


  {
    ER_SAX1_DRIVER_NOT_LOADED,
     "Se ha encontrado la clase {0} del controlador SAX1 pero no se puede cargar"},


  {
    ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "Se ha cargado la clase {0} del controlador SAX1 pero no se puede crear una instancia"},



  {
    ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "La clase {0} del controlador SAX1 no implementa org.xml.sax.Parser"},


  {
    ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "No se ha especificado la propiedad de sistema org.xml.sax.parser"},


  {
    ER_PARSER_ARG_CANNOT_BE_NULL,
     "El argumento del analizador sint\u00e1ctico no debe ser nulo"},



  {
    ER_FEATURE,
     "Caracter\u00edstica: {0}"},



  {
    ER_PROPERTY,
     "Propiedad:{0}"},


  {
    ER_NULL_ENTITY_RESOLVER,
     "Convertidor de entidad nulo"},


  {
    ER_NULL_DTD_HANDLER,
     "Manejador DTD nulo"},

  {
    ER_NO_DRIVER_NAME_SPECIFIED,
     "No se ha especificado un nombre de controlador"},


  {
    ER_NO_URL_SPECIFIED,
     "No se ha especificado una URL"},


  {
    ER_POOLSIZE_LESS_THAN_ONE,
     "\u00a1El tama\u00f1o de pool es inferior a 1!"},


  {
    ER_INVALID_DRIVER_NAME,
     "El nombre de controlador especificado no es v\u00e1lido"},



  {
    ER_ERRORLISTENER,
     "ErrorListener"},


  {
    ER_ASSERT_NO_TEMPLATE_PARENT,
     "Error de programaci\u00f3n. expr no tiene ElemTemplateElement padre!"},


  {
    ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "Confirmaci\u00f3n del programador en RundundentExprEliminator: {0}"},

  {
    ER_NOT_ALLOWED_IN_POSITION,
     "{0} no est\u00e1 permitido en esta posici\u00f3n de la hoja de estilo"},

  {
    ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "En esta posici\u00f3n de la hoja de estilo no se admite texto; solo espacios en blanco "},

  {
    INVALID_TCHAR,
     "Valor no v\u00e1lido: {1} utilizado para el atributo CHAR: {0}.  Un atributo de tipo CHAR debe tener 1 solo car\u00e1cter"},


  {
    INVALID_QNAME,
     "Valor no v\u00e1lido: {1} utilizado para atributo QNAME: {0}"},


  {
    INVALID_ENUM,
     "Valor no v\u00e1lido: {1} utilizado para atributo ENUM: {0}.  Los valores v\u00e1lidos son: {2}."},


  {
    INVALID_NMTOKEN,
     "Valor no v\u00e1lido: {1} utilizado para atributo NMTOKEN: {0}"},


  {
    INVALID_NCNAME,
     "Valor no v\u00e1lido: {1} utilizado para atributo NCNAME : {0}"},



  {
    INVALID_BOOLEAN,
     "Valor no v\u00e1lido: {1} utilizado para atributo booleano: {0}"},


  {
    INVALID_NUMBER,
     "Valor no v\u00e1lido: {1} utilizado para atributo de n\u00famero: {0}"},




  {
    ER_ARG_LITERAL,
     "El argumento de {0} en el patr\u00f3n coincidente debe ser literal."},


  {
    ER_DUPLICATE_GLOBAL_VAR,
     "Declaraci\u00f3n de variable global duplicada."},



  {
    ER_DUPLICATE_VAR,
     "Declaraci\u00f3n de variable duplicada."},


  {
    ER_TEMPLATE_NAME_MATCH,
     "xsl: template debe tener un atributo name o match (o ambos)"},


  {
    ER_INVALID_PREFIX,
     "El prefijo de exclude-result-prefixes no es v\u00e1lido: {0}"},


  {
    ER_NO_ATTRIB_SET,
     "no existe el conjunto de atributos denominado {0}"},



  /** WG_FOUND_CURLYBRACE          */

  {
    WG_FOUND_CURLYBRACE,
      "Se ha encontrado '}' pero no hay abierta ninguna plantilla de atributos."},

  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */

  {
    WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "Advertencia: el atributo de c\u00f3mputo no coincide con ning\u00fan antecesor en xsl:number. Objetivo = {0}"},

  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */

  {
    WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "Sintaxis antigua: El nombre del atributo 'expr' se ha cambiado por 'select'."},

  /** WG_NO_LOCALE_IN_FORMATNUMBER          */

  {
    WG_NO_LOCALE_IN_FORMATNUMBER,

      "Xalan no maneja todav\u00eda el nombre locale en la funci\u00f3n format-number."},

  /** WG_LOCALE_NOT_FOUND          */

  {
    WG_LOCALE_NOT_FOUND,
      "Advertencia: No se ha encontrado locale para xml:lang={0}"},

  /** WG_CANNOT_MAKE_URL_FROM          */

  {
    WG_CANNOT_MAKE_URL_FROM,
      "No se puede crear URL desde: {0}"},

  /** WG_CANNOT_LOAD_REQUESTED_DOC          */

  {
    WG_CANNOT_LOAD_REQUESTED_DOC,
      "No se puede cargar el doc solicitado: {0}"},

  /** WG_CANNOT_FIND_COLLATOR          */

  {
    WG_CANNOT_FIND_COLLATOR,
      "No se ha encontrado Collator para <sort xml:lang={0}"},

  /** WG_FUNCTIONS_SHOULD_USE_URL          */

  {
    WG_FUNCTIONS_SHOULD_USE_URL,
      "Sintaxis antigua: la instrucci\u00f3n de las funciones debe utilizar un url de {0}"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */

  {
    WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "codificaci\u00f3n no admitida: {0}, se utiliza UTF-8"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */

  {
    WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "codificaci\u00f3n no admitida: {0}, se utiliza Java {1}"},

  /** WG_SPECIFICITY_CONFLICTS          */

  {
    WG_SPECIFICITY_CONFLICTS,
      "Se han encontrado conflictos de especificidad: {0} Se utilizar\u00e1 la \u00faltima encontrada en la hoja de estilo."},

  /** WG_PARSING_AND_PREPARING          */

  {
    WG_PARSING_AND_PREPARING,
      "========= An\u00e1lisis sint\u00e1ctico y preparaci\u00f3n {0} =========="},


  /** WG_ATTR_TEMPLATE          */

  {
    WG_ATTR_TEMPLATE, "Plantilla atri, {0}"},

  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */

  {
    WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "Conflicto de coincidencia entre xsl:strip-space y xsl:preserve-space"},

  /** WG_ATTRIB_NOT_HANDLED          */

  {
    WG_ATTRIB_NOT_HANDLED,
      "Xalan no maneja todav\u00eda el atributo {0}."},

  /** WG_NO_DECIMALFORMAT_DECLARATION          */

  {
    WG_NO_DECIMALFORMAT_DECLARATION,
      "No se ha encontrado ninguna declaraci\u00f3n para el formato decimal: {0}"},

  /** WG_OLD_XSLT_NS          */

  {
    WG_OLD_XSLT_NS, "Falta el espacio de nombre XSLT o es incorrecto. "},

  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */

  {
    WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "S\u00f3lo se permite una declaraci\u00f3n xsl:decimal-format predeterminada."},

  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */

  {
    WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "los nombres xsl:decimal-format deben ser \u00fanicos. El nombre \"{0}\" est\u00e1 duplicado."},

  /** WG_ILLEGAL_ATTRIBUTE          */

  {
    WG_ILLEGAL_ATTRIBUTE,
      "{0} tiene un atributo no permitido: {1}"},

  /** WG_COULD_NOT_RESOLVE_PREFIX          */

  {
    WG_COULD_NOT_RESOLVE_PREFIX,
      "No se ha podido convertir el prefijo de espacio de nombre : {0}. El nodo se ignorar\u00e1."},

  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

  {
    WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet necesita un atributo 'version'."},

  /** WG_ILLEGAL_ATTRIBUTE_NAME          */

  {
    WG_ILLEGAL_ATTRIBUTE_NAME,
      "Nombre de atributo no permitido: {0}"},

  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */

  {
    WG_ILLEGAL_ATTRIBUTE_VALUE,
      "Se ha utilizado un valor no permitido para el atributo {0}: {1}"},

  /** WG_EMPTY_SECOND_ARG          */

  {
    WG_EMPTY_SECOND_ARG,
      "El conjunto de nodos resultante del segundo argumento de la funci\u00f3n de documento est\u00e1 vac\u00edo. Se utilizar\u00e1 el primer argumento."},




  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */
  {
     WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "El valor del atributo 'name' del nombre xsl:processing-instruction no debe ser 'xml'"},


  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */
  {
     WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "El valor del atributo ''name'' de xsl:processing-instruction debe ser un nombre NCName v\u00e1lido: {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */
  {
    WG_ILLEGAL_ATTRIBUTE_POSITION,
      "No se puede agregar el atributo {0} despu\u00e9s de nodos hijos ni antes de generar un elemento.  El atributo no ser\u00e1 considerado"},



    { "ui_language", "es"},
    { "help_language", "es"},
    { "language", "es"},
    { "BAD_CODE",
      "El par\u00e1metro para crear el mensaje estaba fuera de los l\u00edmites"},
    { "FORMAT_FAILED",
      "Excepci\u00f3n generada durante la llamada messageFormat"},
    { "version", ">>>>>>> Versi\u00f3n Xalan"},
    { "version2", "<<<<<<<"},
    { "yes", "s\u00ed"},
    { "line", "L\u00ednea #"},
    { "column", "Columna #"},
    { "xsldone", "XSLProcessor: hecho"},
    { "xslProc_option", "opciones de clase Proceso de la l\u00ednea de comandos Xalan-J:"},
    { "optionIN", "    -IN inputXMLURL"},
    { "optionXSL", "   [-XSL XSLTransformationURL]"},
    { "optionOUT", "   [-OUT outputFileName]"},
    { "optionLXCIN", "   [-LXCIN compiledStylesheetFileNameIn]"},
    { "optionLXCOUT", "   [-LXCOUT compiledStylesheetFileNameOutOut]"},
    { "optionPARSER",
      "   [nombre totalmente cualificado -PARSER de clase de v\u00ednculo de analizador sint\u00e1ctico]"},
    { "optionE",
     "   [-E (No expandir refs de entidad)]"},
    { "optionV",
     "   [-E (No expandir refs de entidad)]"},
    {"optionQC",
      "   [-QC (Advertencias silenciosas de conflictos de patrones)]"},
    {"optionQ",
     "   [-Q  (Modo silencioso)]"},
    { "optionLF",
      "   [-LF (Utilizar cambios de l\u00ednea s\u00f3lo en la salida {el valor predeterminado es CR/LF})]"},
    { "optionCR",
      "   [-CR (Utilizar retornos de carro s\u00f3lo en la salida {el valor predeterminado es CR/LF})]"},
    { "optionESCAPE",
      "   [-ESCAPE (\u00bfCu\u00e1les son los caracteres de escape? {el valor por defecto es <>&\"\'\\r\\n}]"},
    { "optionINDENT",
      "   [-INDENT (Controlar el n\u00famero de espacios de indentaci\u00f3n {el valor por defecto es 0})]"},
    { "optionTT",
      "   [-TT (Rastrear las plantillas seg\u00fan se vayan invocando.)]"},
    { "optionTG",
      "   [-TG (Rastrear cada suceso de generaci\u00f3n.)]"},
    {"optionTS",
     "   [-TS (Rastrear cada suceso de selecci\u00f3n.)]"},
    { "optionTTC",
      "   [-TTC (Rastrear las plantillas hijas seg\u00fan se vayan procesando.)]"},
    { "optionTCLASS",
      "   [-TCLASS (Clase TraceListener para las extensiones de rastreo.)]"},
    { "optionVALIDATE",
      "   [-VALIDATE (Establecer si se realiza la validaci\u00f3n.  El valor predeterminado de la validaci\u00f3n es off.)]"},
    {"optionEDUMP",
      "   [-EDUMP {nombre de archivo opcional} (Hacer volcado de pila en caso de error.)]"},
    { "optionXML",
      "   [-XML (Utilizar el formateador XML y agregar la cabecera de XML.)]"},
    { "optionTEXT",
      "   [-TEXT (Utilizar el formateador de texto sencillo.)]"},
    { "optionHTML",
     "   [-HTML (Utilizar el formateador HTML.)]"},
    {"optionPARAM",
      "   [expresi\u00f3n de nombre -PARAM (Establecer un par\u00e1metro de hoja de estilo)]"},
    { "noParsermsg1",
     "Ha fallado el proceso XSL."},
    { "noParsermsg2",
     "** No se ha encontrado el analizador sint\u00e1ctico **"},
    { "noParsermsg3",
     "Comprobar classpath."},
    { "noParsermsg4",
      "Si no tiene el analizador sint\u00e1ctico XML para Java de IBM puede cargarlo de "},
    { "noParsermsg5",
    { "optionURIRESOLVER",
     "   [nombre de clase completo -URIRESOLVER (Utilizar URIResolver para convertir los URIs)]"},
    { "optionENTITYRESOLVER",
     "   [nombre de clase completo -ENTITYRESOLVER (Utilizar EntityResolver para convertir las entidades)]"},
    {"optionCONTENTHANDLER",
     "   [nombre de clase completo -CONTENTHANDLER (Utilizar ContentHandler para serializar la salida)]"},
    {"optionLINENUMBERS",
     "   [-L utilizar n\u00fameros de l\u00edneas para el documento fuente]"},
		


    { "optionMEDIA",
     " [-MEDIA mediaType (utilice un atributo media para buscar las hojas de estilo asociadas con un documento.)]"},
    { "optionFLAVOR",
    { "optionDIAG",
     " [-DIAG (la impresi\u00f3n tard\u00f3 milisegundos.)]"},
    {"optionINCREMENTAL",
    { "optionNOOPTIMIMIZE",
    { "optionRL",
     " [-RL recursionlimit (afirmar l\u00edmite num\u00e9rico en la profundidad de recursividad de la hoja de estilo.)]"},
    { "optionXO",
     " [-XO [transletName] (asignar nombre al translet generado)]"},
    {"optionXD",
     " [-XD destinationDirectory (especifica un directorio de destino para translet)]"},
    { "optionXJ",
     " [-XJ jarfile (empaqueta clases translet en un archivo JAR denominado <archivoJAR>)]"},
    { "optionXP",
     " [-XP package (especifica un prefijo de nombre de paquete para todas las clases translet generadas)]"}


  };


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "BAD_CODE";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** General error string.   */
  public static final String ERROR_STRING = "#error";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "Error: ";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "Advertencia: ";

  /** String to specify the XSLT module.  */
  public static final String XSL_HEADER = "XSLT ";

  /** String to specify the XML parser module.  */
  public static final String XML_HEADER = "XML ";

  /** I don't think this is used any more.
   * @deprecated  */
  public static final String QUERY_HEADER = "PATR\u00d3N ";

  /**
   * Get the lookup table. 
   *
   * @return The int to message lookup table.
   */
  public Object[][] getContents()
  {
    return contents;
  }
}

