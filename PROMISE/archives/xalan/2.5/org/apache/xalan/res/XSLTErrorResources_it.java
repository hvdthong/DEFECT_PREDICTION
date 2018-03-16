package org.apache.xalan.res;

import java.util.Locale;

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

public class XSLTErrorResources_it extends XSLTErrorResources
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
     "Errore: Impossibile inserire '{' nell'espressione."},


 /** ER_ILLEGAL_ATTRIBUTE          */


 {
   ER_ILLEGAL_ATTRIBUTE, "{0} ha un attributo illegale: {1}."},


 /** ER_NULL_SOURCENODE_APPLYIMPORTS          */


 {
   ER_NULL_SOURCENODE_APPLYIMPORTS,
     "sourceNode nullo in xsl:apply-imports"},


 /** ER_CANNOT_ADD          */


 {
   ER_CANNOT_ADD, "Impossibile aggiungere {0} a {1}."},


 /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */


 {
   ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
     "sourceNode nullo in handleApplyTemplatesInstruction."},


 /** ER_NO_NAME_ATTRIB          */


 {
   ER_NO_NAME_ATTRIB, "{0} deve avere un attributo nome."},


 /** ER_TEMPLATE_NOT_FOUND          */


 {
   ER_TEMPLATE_NOT_FOUND, "Impossibile trovare il modello denominato: {0}."},


 /** ER_CANT_RESOLVE_NAME_AVT          */


 {
   ER_CANT_RESOLVE_NAME_AVT,
     "Impossibile risolvere il nome AVT in xsl:call-template."},


 /** ER_REQUIRES_ATTRIB          */


 {
   ER_REQUIRES_ATTRIB, "{0} richiede l'attributo: {1}."},


 /** ER_MUST_HAVE_TEST_ATTRIB          */


 {
   ER_MUST_HAVE_TEST_ATTRIB,
     "{0} deve avere un attributo ''test''."},


 /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */


 {
   ER_BAD_VAL_ON_LEVEL_ATTRIB,
     "Valore non valido su attributo livello: {0}"},


 /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */


 {
   ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
     "Il nome dell'istruzione di elaborazione non pu\u00f2 essere 'xml'."},


 /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */


 {
   ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
     "Il nome dell'istruzione di elaborazione deve essere un NCName valido: {0}."},


 /** ER_NEED_MATCH_ATTRIB          */


 {
   ER_NEED_MATCH_ATTRIB,
     "{0} deve avere un attributo corrispondenza se ha una modalit\u00e0."},


 /** ER_NEED_NAME_OR_MATCH_ATTRIB          */


 {
   ER_NEED_NAME_OR_MATCH_ATTRIB,
     "{0} richiede un attributo nome o corrispondenza."},


 /** ER_CANT_RESOLVE_NSPREFIX          */


 {
   ER_CANT_RESOLVE_NSPREFIX,
     "Impossibile risolvere il prefisso namespace: {0}."},


 /** ER_ILLEGAL_VALUE          */


 {
   ER_ILLEGAL_VALUE, "xml:space ha valore non valido: {0}."},


 /** ER_NO_OWNERDOC          */


 {
   ER_NO_OWNERDOC,
     "Il nodo secondario non ha alcun documento di propriet\u00e0."},


 /** ER_ELEMTEMPLATEELEM_ERR          */


 {
   ER_ELEMTEMPLATEELEM_ERR, "Errore ElemTemplateElement: {0}."},


 /** ER_NULL_CHILD          */


 {
   ER_NULL_CHILD, "Tentativo di aggiungere un elemento secondario nullo."},


 /** ER_NEED_SELECT_ATTRIB          */


 {
   ER_NEED_SELECT_ATTRIB, "{0} richiede un attributo selezione."},


 /** ER_NEED_TEST_ATTRIB          */


 {
   ER_NEED_TEST_ATTRIB,
     "xsl:when deve avere un attributo 'test'."},


 /** ER_NEED_NAME_ATTRIB          */


 {
   ER_NEED_NAME_ATTRIB,
     "xsl:with-param deve avere un attributo 'name'."},


 /** ER_NO_CONTEXT_OWNERDOC          */


 {
   ER_NO_CONTEXT_OWNERDOC,
     "Il contesto non ha un documento di propriet\u00e0."},


 /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */


 {
   ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
     "Impossibile creare un XML TransformerFactory Liaison: {0}"},


 /** ER_PROCESS_NOT_SUCCESSFUL          */


 {
   ER_PROCESS_NOT_SUCCESSFUL,
     "Xalan: il processo non \u00e8 riuscito."},


 /** ER_NOT_SUCCESSFUL          */


 {
   ER_NOT_SUCCESSFUL, "Xalan: non \u00e8 riuscito."},


 /** ER_ENCODING_NOT_SUPPORTED          */


 {
   ER_ENCODING_NOT_SUPPORTED, "Codifica non supportata: {0}"},


 /** ER_COULD_NOT_CREATE_TRACELISTENER          */


 {
   ER_COULD_NOT_CREATE_TRACELISTENER,
     "Impossibile creare TraceListener: {0}"},


 /** ER_KEY_REQUIRES_NAME_ATTRIB          */


 {
   ER_KEY_REQUIRES_NAME_ATTRIB,
     "xsl:key richiede un attributo 'nome'."},


 /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


 {
   ER_KEY_REQUIRES_MATCH_ATTRIB,
     "xsl:key richiede un attributo 'corrispondenza'."},


 /** ER_KEY_REQUIRES_USE_ATTRIB          */


 {
   ER_KEY_REQUIRES_USE_ATTRIB,
     "xsl:key richiede un attributo 'uso'."},


 /** ER_REQUIRES_ELEMENTS_ATTRIB          */


 {
   ER_REQUIRES_ELEMENTS_ATTRIB,
     "(StylesheetHandler) {0} richiede un attributo ''elementi''."},


 /** ER_MISSING_PREFIX_ATTRIB          */


 {
   ER_MISSING_PREFIX_ATTRIB,
     "(StylesheetHandler) {0} attributo ''prefisso'' mancante"},


 /** ER_BAD_STYLESHEET_URL          */


 {
   ER_BAD_STYLESHEET_URL, "URL del foglio di stile non valido: {0}"},


 /** ER_FILE_NOT_FOUND          */


 {
   ER_FILE_NOT_FOUND, "File del foglio di stile non trovato: {0}"},


 /** ER_IOEXCEPTION          */


 {
   ER_IOEXCEPTION,
     "Rilevata eccezione IO con il file del foglio di stile: {0}"},


 /** ER_NO_HREF_ATTRIB          */


 {
   ER_NO_HREF_ATTRIB,
     "(StylesheetHandler) Impossibile trovare l'attributo href per {0}"},


 /** ER_STYLESHEET_INCLUDES_ITSELF          */


 {
   ER_STYLESHEET_INCLUDES_ITSELF,
     "(StylesheetHandler) {0} include se stesso direttamente o indirettamente."},


 /** ER_PROCESSINCLUDE_ERROR          */


 {
   ER_PROCESSINCLUDE_ERROR,
     "Errore StylesheetHandler.processInclude, {0}"},


 /** ER_MISSING_LANG_ATTRIB          */


 {
   ER_MISSING_LANG_ATTRIB,
     "(StylesheetHandler) {0} attributo ''lang'' mancante"},


 /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */


 {
   ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
     "(StylesheetHandler) elemento {0} fuori posto? ''Componente'' dell'elemento contenitore mancante"},


 /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */


 {
   ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
     "\u00c8 possibile eseguire l'output solo in Element, DocumentFragment, Document o PrintWriter."},


 /** ER_PROCESS_ERROR          */


 {
   ER_PROCESS_ERROR, "Errore in StylesheetRoot.process"},


 /** ER_UNIMPLNODE_ERROR          */


 {
   ER_UNIMPLNODE_ERROR, "Errore in UnImplNode: {0}"},


 /** ER_NO_SELECT_EXPRESSION          */


 {
   ER_NO_SELECT_EXPRESSION,
     "Errore. L'espressione di selezione del percorso (-select) non \u00e8 stata trovata."},


 /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */


 {
   ER_CANNOT_SERIALIZE_XSLPROCESSOR,
     "Impossibile serializzare un XSLProcessor!"},


 /** ER_NO_INPUT_STYLESHEET          */


 {
   ER_NO_INPUT_STYLESHEET,
     "Input del foglio di stile non specificato."},


 /** ER_FAILED_PROCESS_STYLESHEET          */


 {
   ER_FAILED_PROCESS_STYLESHEET,
     "Elaborazione del foglio di stile non riuscita."},


 /** ER_COULDNT_PARSE_DOC          */


 {
   ER_COULDNT_PARSE_DOC, "Impossibile analizzare il documento {0}."},


 /** ER_COULDNT_FIND_FRAGMENT          */


 {
   ER_COULDNT_FIND_FRAGMENT, "Impossibile trovare il frammento: {0}"},


 /** ER_NODE_NOT_ELEMENT          */


 {
   ER_NODE_NOT_ELEMENT,
     "Il nodo a cui puntava l'identificatore del frammento non era un elemento: {0}"},


 /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */


 {
   ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
     "for-each deve avere un attributo corrispondenza o nome."},


 /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */


 {
   ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
     "I modelli devono avere un attributo corrispondenza o nome."},


 /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */


 {
   ER_NO_CLONE_OF_DOCUMENT_FRAG,
     "Nessun duplicato di frammento di un documento."},


 /** ER_CANT_CREATE_ITEM          */


 {
   ER_CANT_CREATE_ITEM,
     "Impossibile creare un elemento nell'albero del risultato: {0}"},


 /** ER_XMLSPACE_ILLEGAL_VALUE          */


 {
   ER_XMLSPACE_ILLEGAL_VALUE,
     "xml:space nell'XML sorgente ha valore non valido: {0}"},


 /** ER_NO_XSLKEY_DECLARATION          */


 {
   ER_NO_XSLKEY_DECLARATION,
     "Dichiarazione xsl:key mancante per {0}!"},


 /** ER_CANT_CREATE_URL          */


 {
   ER_CANT_CREATE_URL, "Errore. Impossibile creare URL per: {0}"},


 /** ER_XSLFUNCTIONS_UNSUPPORTED          */


 {
   ER_XSLFUNCTIONS_UNSUPPORTED, "xsl:functions non supportato"},


 /** ER_PROCESSOR_ERROR          */


 {
   ER_PROCESSOR_ERROR, "Errore XSLT TransformerFactory"},


 /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */


 {
   ER_NOT_ALLOWED_INSIDE_STYLESHEET,
     "(StylesheetHandler) {0} non consentito in un foglio di stile."},


 /** ER_RESULTNS_NOT_SUPPORTED          */


 {
   ER_RESULTNS_NOT_SUPPORTED,
     "result-ns non \u00e8 pi\u00f9 supportato. Utilizzare xsl:output."},


 /** ER_DEFAULTSPACE_NOT_SUPPORTED          */


 {
   ER_DEFAULTSPACE_NOT_SUPPORTED,
     "default-space non \u00e8 pi\u00f9 supportato. Utilizzare xsl:strip-space o xsl:preserve-space."},


 /** ER_INDENTRESULT_NOT_SUPPORTED          */


 {
   ER_INDENTRESULT_NOT_SUPPORTED,
     "indent-result non \u00e8 pi\u00f9 supportato. Utilizzare xsl:output."},


 /** ER_ILLEGAL_ATTRIB          */


 {
   ER_ILLEGAL_ATTRIB,
     "(StylesheetHandler) {0} ha un attributo non valido {1}"},


 /** ER_UNKNOWN_XSL_ELEM          */


 {
   ER_UNKNOWN_XSL_ELEM, "Elemento XSL sconosciuto: {0}"},


 /** ER_BAD_XSLSORT_USE          */


 {
   ER_BAD_XSLSORT_USE,
     "(StylesheetHandler) xsl:sort pu\u00f2 essere utilizzato solo con xsl:apply-templates o xsl:for-each."},


 /** ER_MISPLACED_XSLWHEN          */


 {
   ER_MISPLACED_XSLWHEN,
     "(StylesheetHandler) xsl:when fuori posto."},


 /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */


 {
   ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
     "(StylesheetHandler) xsl:when non dipende da xsl:choose!"},


 /** ER_MISPLACED_XSLOTHERWISE          */


 {
   ER_MISPLACED_XSLOTHERWISE,
     "(StylesheetHandler) xsl:otherwise fuori posto."},


 /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */


 {
   ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
     "(StylesheetHandler) xsl:otherwise non dipende da xsl:choose."},


 /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */


 {
   ER_NOT_ALLOWED_INSIDE_TEMPLATE,
     "(StylesheetHandler) {0} non \u00e8 consentito in un modello."},


 /** ER_UNKNOWN_EXT_NS_PREFIX          */


 {
   ER_UNKNOWN_EXT_NS_PREFIX,
     "(StylesheetHandler) {0} prefisso namespace di estensione {1} sconosciuto"},


 /** ER_IMPORTS_AS_FIRST_ELEM          */


 {
   ER_IMPORTS_AS_FIRST_ELEM,
     "(StylesheetHandler) Le importazioni sono possibili solo come primi elementi di un foglio di stile."},


 /** ER_IMPORTING_ITSELF          */


 {
   ER_IMPORTING_ITSELF,
     "(StylesheetHandler) {0} sta importando se stesso direttamente o indirettamente."},


 /** ER_XMLSPACE_ILLEGAL_VAL          */


 {
   ER_XMLSPACE_ILLEGAL_VAL,
     "(StylesheetHandler) " + "xml:space ha valore non valido: {0}"},


 /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */


 {
   ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
     "processStylesheet non \u00e8 riuscito."},


 /** ER_SAX_EXCEPTION          */


 {
   ER_SAX_EXCEPTION, "Eccezione SAX"},


 /** ER_XSLT_ERROR          */


 {
   ER_XSLT_ERROR, "Errore XSLT"},


 /** ER_CURRENCY_SIGN_ILLEGAL          */


 {
   ER_CURRENCY_SIGN_ILLEGAL,
     "Il segno di valuta non \u00e8 consentito nelle stringhe modello di formato."},


 /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */


 {
   ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
     "Funzione documento non supportata nel foglio di stile DOM!"},


 /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */


 {
   ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
     "Impossibile risolvere il prefisso del risolutore non-Prefix."},


 /** ER_REDIRECT_COULDNT_GET_FILENAME          */


 {
   ER_REDIRECT_COULDNT_GET_FILENAME,
     "Estensione di reindirizzamento: Impossibile trovare il nome file. Il file o l'attributo di selezione devono generare una stringa valida."},


 /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */


 {
   ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
     "Impossibile generare FormatterListener nell'estensione di reindirizzamento."},


 /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */


 {
   ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
     "Il prefisso in exclude-result-prefixes non \u00e8 valido: {0}"},


 /** ER_MISSING_NS_URI          */


 {
   ER_MISSING_NS_URI,
     "URI namespace mancante per il prefisso specificato."},


 /** ER_MISSING_ARG_FOR_OPTION          */


 {
   ER_MISSING_ARG_FOR_OPTION,
     "Argomento mancante per l'opzione: {0}"},


 /** ER_INVALID_OPTION          */


 {
   ER_INVALID_OPTION, "Opzione non valida: {0}"},


 /** ER_MALFORMED_FORMAT_STRING          */


 {
   ER_MALFORMED_FORMAT_STRING, "Stringa di formato non valida: {0}"},


 /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */


 {
   ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
     "xsl:stylesheet richiede un attributo 'versione'."},


 /** ER_ILLEGAL_ATTRIBUTE_VALUE          */


 {
   ER_ILLEGAL_ATTRIBUTE_VALUE,
     "Attributo: {0} ha un valore non valido: {1}"},


 /** ER_CHOOSE_REQUIRES_WHEN          */


 {
   ER_CHOOSE_REQUIRES_WHEN, "xsl:choose richiede xsl:when"},


 /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */


 {
   ER_NO_APPLY_IMPORT_IN_FOR_EACH,
     "xsl:apply-imports non consentito in xsl:for-each"},


 /** ER_CANT_USE_DTM_FOR_OUTPUT          */


 {
   ER_CANT_USE_DTM_FOR_OUTPUT,
     "Impossibile utilizzare un collegamento DTM per un nodo DOM di output. Utilizzare org.apache.xpath.DOM2Helper."},


 /** ER_CANT_USE_DTM_FOR_INPUT          */


 {
   ER_CANT_USE_DTM_FOR_INPUT,
     "Impossibile utilizzare un collegamento DTM per un nodo DOM di input. Utilizzare org.apache.xpath.DOM2Helper."},


 /** ER_CALL_TO_EXT_FAILED          */


 {
   ER_CALL_TO_EXT_FAILED,
     "Chiamata all'elemento di estensione non riuscita: {0}"},


 /** ER_PREFIX_MUST_RESOLVE          */


 {
   ER_PREFIX_MUST_RESOLVE,
     "Il prefisso deve risolvere in namespace: {0}"},


 /** ER_INVALID_UTF16_SURROGATE          */


 {
   ER_INVALID_UTF16_SURROGATE,
     "Rilevato surrogato di UTF-16 non valido: {0} ?"},


 /** ER_XSLATTRSET_USED_ITSELF          */


 {
   ER_XSLATTRSET_USED_ITSELF,
     "xsl:attribute-set {0} ha utilizzato se stesso, generando un loop infinito."},


 /** ER_CANNOT_MIX_XERCESDOM          */


 {
   ER_CANNOT_MIX_XERCESDOM,
     "Impossibile combinare un input non Xerces-DOM con un input Xerces-DOM."},


 /** ER_TOO_MANY_LISTENERS          */


 {
   ER_TOO_MANY_LISTENERS,
     "addTraceListenersToStylesheet - TooManyListenersException"},


 /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */


 {
   ER_IN_ELEMTEMPLATEELEM_READOBJECT,
     "In ElemTemplateElement.readObject: {0}"},


 /** ER_DUPLICATE_NAMED_TEMPLATE          */


 {
   ER_DUPLICATE_NAMED_TEMPLATE,
     "Trovato pi\u00f9 di un modello denominato: {0}"},


 /** ER_INVALID_KEY_CALL          */


 {
   ER_INVALID_KEY_CALL,
     "Chiamata di funzione non valida: le chiamate chiave() ricorsive non sono consentite."},

 
 /** Variable is referencing itself          */


 {
   ER_REFERENCING_ITSELF,
     "La variabile {0} fa riferimento a se stessa direttamente o indirettamente."},

 
 /** Illegal DOMSource input          */


 {
   ER_ILLEGAL_DOMSOURCE_INPUT,
     "Il nodo di input non pu\u00f2 essere nullo per DOMSource per newTemplates."},

	
	/** Class not found for option         */


 {
   ER_CLASS_NOT_FOUND_FOR_OPTION,
			"File di classe non trovato per l'opzione {0}"},

	
	/** Required Element not found         */


 {
   ER_REQUIRED_ELEM_NOT_FOUND,
			"Elemento richiesto non trovato: {0}"},

 
 /** InputStream cannot be null         */


 {
   ER_INPUT_CANNOT_BE_NULL,
			"InputStream non pu\u00f2 essere nullo."},

 
 /** URI cannot be null         */


 {
   ER_URI_CANNOT_BE_NULL,
			"L'URI non pu\u00f2 essere nullo."},

 
 /** File cannot be null         */


 {
   ER_FILE_CANNOT_BE_NULL,
			"Il file non pu\u00f2 essere nullo."},

 
  /** InputSource cannot be null         */


 {
   ER_SOURCE_CANNOT_BE_NULL,
			"InputSource non pu\u00f2 essere nullo."},

 
 /** Could not initialize BSF Manager        */


 {
   ER_CANNOT_INIT_BSFMGR,
			"Impossibile inizializzare BSF Manager."},

 
 /** Could not compile extension       */


 {
   ER_CANNOT_CMPL_EXTENSN,
			"Impossibile compilare l'estensione."},

 
 /** Could not create extension       */


 {
   ER_CANNOT_CREATE_EXTENSN,
     "Impossibile creare l'estensione: {0} a causa di: {1}"},

 
 /** Instance method call to method {0} requires an Object instance as first argument       */


 {
   ER_INSTANCE_MTHD_CALL_REQUIRES,
     "La chiamata del metodo istanza al metodo {0} richiede un'istanza oggetto come primo argomento."},

 
 /** Invalid element name specified       */


 {
   ER_INVALID_ELEMENT_NAME,
     "\u00c8 stato specificato un nome elemento non valido {0}"},

 
  /** Element name method must be static      */


 {
   ER_ELEMENT_NAME_METHOD_STATIC,
     "Il metodo del nome elemento deve essere statico {0}"},

 
  /** Extension function {0} : {1} is unknown      */


 {
   ER_EXTENSION_FUNC_UNKNOWN,
            "Funzione estensione {0} : {1} sconosciuta."},

 
  /** More than one best match for constructor for       */


 {
   ER_MORE_MATCH_CONSTRUCTOR,
            "Pi\u00f9 di una corrispondenza migliore per costruttore per {0}."},

 
  /** More than one best match for method      */


 {
   ER_MORE_MATCH_METHOD,
            "Pi\u00f9 di una corrispondenza migliore per il metodo {0}"},

 
  /** More than one best match for element method      */


 {
   ER_MORE_MATCH_ELEMENT,
            "Pi\u00f9 di una corrispondenza migliore per il metodo elemento {0}"},

 
  /** Invalid context passed to evaluate       */


 {
   ER_INVALID_CONTEXT_PASSED,
            "Contesto non valido passato da valutare {0}."},

 
  /** Pool already exists       */


 {
   ER_POOL_EXISTS,
            "Pool gi\u00e0 esistente."},

 
  /** No driver Name specified      */


 {
   ER_NO_DRIVER_NAME,
            "Non \u00e8 stato specificato alcun nome di driver."},

 
  /** No URL specified     */


 {
   ER_NO_URL,
            "Non \u00e8 stato specificato alcun URL."},

 
  /** Pool size is less than one    */


 {
   ER_POOL_SIZE_LESSTHAN_ONE,
            "Le dimensioni del pool sono minori di uno."},

 
  /** Invalid driver name specified    */


 {
   ER_INVALID_DRIVER,
            "\u00c8 stato specificato un nome di driver non valido."},

 
  /** Did not find the stylesheet root    */


 {
   ER_NO_STYLESHEETROOT,
            "Impossibile trovare la root del foglio di stile."},

 
  /** Illegal value for xml:space     */


 {
   ER_ILLEGAL_XMLSPACE_VALUE,
        "Valore non valido per xml:space."},

 
  /** processFromNode failed     */


 {
   ER_PROCESSFROMNODE_FAILED,
        "processFromNode non riuscito."},

 
  /** The resource [] could not load:     */


 {
   ER_RESOURCE_COULD_NOT_LOAD,
       "Impossibile caricare la risorsa [ {0} ]: {1} \n {2} \t {3}"},

  
 
  /** Buffer size <=0     */


 {
   ER_BUFFER_SIZE_LESSTHAN_ZERO,
       "Dimensioni del buffer <=0"},

 
  /** Unknown error when calling extension    */


 {
   ER_UNKNOWN_ERROR_CALLING_EXTENSION,
       "Errore sconosciuto nella chiamata dell'estensione."},

 
  /** Prefix {0} does not have a corresponding namespace declaration    */


 {
   ER_NO_NAMESPACE_DECL,
       "Il prefisso {0} non ha una corrispondente dichiarazione namespace."},

 
  /** Element content not allowed for lang=javaclass   */


 {
   ER_ELEM_CONTENT_NOT_ALLOWED,
       "Contenuto dell'elemento non consentito per lang=javaclass {0}."},
   
 
  /** Stylesheet directed termination   */


 {
   ER_STYLESHEET_DIRECTED_TERMINATION,
       "Conclusione richiesta dal foglio di stile."},

 
  /** 1 or 2   */


 {
   ER_ONE_OR_TWO,
       "1 o 2"},

 
  /** 2 or 3   */


 {
   ER_TWO_OR_THREE,
       "2 o 3"},

 
  /** Could not load {0} (check CLASSPATH), now using just the defaults   */


 {
   ER_COULD_NOT_LOAD_RESOURCE,
       "Impossibile caricare {0} (verificare CLASSPATH). Attualmente sono in uso i valori predefiniti."},

 
  /** Cannot initialize default templates   */


 {
   ER_CANNOT_INIT_DEFAULT_TEMPLATES,
       "Impossibile inizializzare i modelli predefiniti."},

 
  /** Result should not be null   */


 {
   ER_RESULT_NULL,
       "Il risultato non dovrebbe essere nullo."},

   
  /** Result could not be set   */


 {
   ER_RESULT_COULD_NOT_BE_SET,
       "Impossibile stabilire il risultato."},

 
  /** No output specified   */


 {
   ER_NO_OUTPUT_SPECIFIED,
       "Nessun output specificato."},

 
  /** Can't transform to a Result of type   */


 {
   ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
       "Impossibile trasformare in un risultato di tipo {0}."},

 
  /** Can't transform to a Source of type   */


 {
   ER_CANNOT_TRANSFORM_SOURCE_TYPE,
       "Impossibile trasformare un sorgente di tipo {0}."},

 
  /** Null content handler  */


 {
   ER_NULL_CONTENT_HANDLER,
       "Contenuto gestore nullo"},

 
  /** Null error handler  */


 {
   ER_NULL_ERROR_HANDLER,
       "Errore gestore nullo"},

 
  /** parse can not be called if the ContentHandler has not been set */


 {
   ER_CANNOT_CALL_PARSE,
       "Impossibile chiamare l'analisi se non \u00e8 impostato ContentHandler."},

 
  /**  No parent for filter */


 {
   ER_NO_PARENT_FOR_FILTER,
       "Nessun elemento principale per il filtro."},

 
 
  /**  No stylesheet found in: {0}, media */


 {
   ER_NO_STYLESHEET_IN_MEDIA,
        "Nessun foglio di stile trovato in: {0}, media= {1}"},

 
  /**  No xml-stylesheet PI found in */


 {
   ER_NO_STYLESHEET_PI,
        "Nessun xml-stylesheet PI trovato in : {0}"},

 
  /**  Not supported  */


 {
   ER_NOT_SUPPORTED,
      "Non supportato: {0}"},
 
  /**  Value for property {0} should be a Boolean instance  */


 {
   ER_PROPERTY_VALUE_BOOLEAN,
      "Il valore della propriet\u00e0 {0} deve essere un'istanza booleana"},

 
  /** src attribute not yet supported for  */


 {
   ER_COULD_NOT_FIND_EXTERN_SCRIPT,
      "L'attributo src non \u00e8 ancora supportato per {0}."},

 
 /** The resource [] could not be found     */


 {
   ER_RESOURCE_COULD_NOT_FIND,
       "Impossibile trovare la risorsa [ {0} ].\n {1}"},

 
  /** output property not recognized:  */


 {
   ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
       "La propriet\u00e0 dell'output non \u00e8 riconosciuta: {0}"},

 
  /** Failed creating ElemLiteralResult instance   */


 {
   ER_FAILED_CREATING_ELEMLITRSLT,
       "Creazione non riuscita dell'istanza ElemLiteralResult."},
  
 

  /** Priority value does not contain a parsable number   */


 {
   ER_VALUE_SHOULD_BE_NUMBER,
       "Il valore di {0} deve contenere un numero analizzabile."},

 
  /**  Value for {0} should equal 'yes' or 'no'   */


 {
   ER_VALUE_SHOULD_EQUAL,
       "Il valore di {0} deve essere s\u00ec o no."},


  /**  Failed calling {0} method   */


 {
   ER_FAILED_CALLING_METHOD,
       "Chiamata non riuscita del metodo {0}."},

 
  /** Failed creating ElemLiteralResult instance   */


 {
   ER_FAILED_CREATING_ELEMTMPL,
       "Creazione non riuscita dell'istanza ElemTemplateElement."},

 
  /**  Characters are not allowed at this point in the document   */


 {
   ER_CHARS_NOT_ALLOWED,
       "I caratteri non sono consentiti in questo punto del documento."},

 
 /**  attribute is not allowed on the element   */


 {
   ER_ATTR_NOT_ALLOWED,
       "\"{0}\": questo attributo non \u00e8 consentito sull'elemento {1}."},


 /**  Bad value    */


 {
   ER_BAD_VALUE,
    "{0} valore non valido {1} "},

 
 /**  attribute value not found   */


 {
   ER_ATTRIB_VALUE_NOT_FOUND,
    "{0} valore dell'attributo non trovato."},

 
 /**  attribute value not recognized    */


 {
   ER_ATTRIB_VALUE_NOT_RECOGNIZED,
    "{0} valore dell'attributo non riconosciuto "},

 
  /** Attempting to generate a namespace prefix with a null URI   */


  {
      ER_NULL_URI_NAMESPACE,
      "Tentativo di generare un prefisso di namespace con URI nullo"},
  
 
 
   /** Attempting to generate a namespace prefix with a null URI        */
 
 
   {
     ER_NUMBER_TOO_BIG,
      "Tentativo di formattare un numero maggiore dell'intero lungo pi\u00f9 grande"},
  
 
 
 
 
   {
     ER_CANNOT_FIND_SAX1_DRIVER,
      "Impossibile trovare classe driver SAX1 {0}"},
  
 
 
 
   {
     ER_SAX1_DRIVER_NOT_LOADED,
      "La classe di driver SAX1 {0} \u00e8 stata trovata ma \u00e8 impossibile caricarla"},
  
 
 
 
   {
     ER_SAX1_DRIVER_NOT_INSTANTIATED,
      "La classe di driver SAX1 {0} \u00e8 stata caricata ma non \u00e8 possibile creare istanze"},
  
 
 
 
 
   {
     ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
      "La classe di driver SAX1 {0} non implementa org.xml.sax.Parser"},
  
 
 
 
   {
     ER_PARSER_PROPERTY_NOT_SPECIFIED,
      "Propriet\u00e0 di sistema org.xml.sax.parser non specificata"},
  
 
 
 
   {
     ER_PARSER_ARG_CANNOT_BE_NULL,
      "L'argomento del Parser non deve essere nullo"},
  
 
 
 
 
   {
     ER_FEATURE,
      "Caratteristica:a {0}"},
  
 
 
 
 
   {
     ER_PROPERTY,
      "Propriet\u00e0:a {0}"},
  
 
 
 
   {
     ER_NULL_ENTITY_RESOLVER,
      "Il risolutore dell'entit\u00e0 \u00e8 nullo"},
  
 
 
 
   {
     ER_NULL_DTD_HANDLER,
      "Il gestore DTD \u00e8 nullo"},
  
 
 
   {
     ER_NO_DRIVER_NAME_SPECIFIED,
      "Nessun nome di driver specificato."},
  
 
 
 
   {
     ER_NO_URL_SPECIFIED,
      "Nessun URL specificato."},
  
 
 
 
   {
     ER_POOLSIZE_LESS_THAN_ONE,
      "La dimensione pool \u00e8 inferiore a 1."},
  
 
 
 
   {
     ER_INVALID_DRIVER_NAME,
      "Il nome specificato del driver non \u00e8 valido."},
  
 
 
 
 
   {
     ER_ERRORLISTENER,
      "ErrorListener"},
  
 
 
 
   {
     ER_ASSERT_NO_TEMPLATE_PARENT,
      "Errore del programmatore. L'espressione non presenta ElemTemplateElement superiore."},
  
 
 
 
   {
     ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
      "Affermazione del programmatore in RundundentExprEliminator: {0}"},
  
 
 
   {
     ER_NOT_ALLOWED_IN_POSITION,
      "{0} non \u00e8 consentito in questa posizione nel foglio di stile."},
  
 
 
   {
     ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
      "Il testo senza spazi non \u00e8 consentito nel foglio di stile."},
  
 
 
   {
     INVALID_TCHAR,
      "Valore non consentito: {1} utilizzato per attributo CHAR: {0}. L'attributo di tipo CHAR deve contenere 1 solo carattere."},
  
 
 
 
   {
     INVALID_QNAME,
      "Valore non consentito:a {1} utilizzato per attributo QNAME:a {0}"},
  
 

 
   {
     INVALID_ENUM,
      "Valore non consentito:a {1} utilizzato per attributo ENUM:a {0}. I valori validi sono:a {2}."},
  
 
 
 
    {
      INVALID_NMTOKEN,
       "Valore non consentito:a {1} utilizzato per attributo NMTOKEN:a {0} "},
   
  
  
  
    {
      INVALID_NCNAME,
       "Valore non consentito:a {1} utilizzato per attributo NCNAME:a {0} "},
   
  
  
  
  
    {
      INVALID_BOOLEAN,
       "Valore non consentito:a {1} utilizzato per attributo boolean:a {0} "},
   
  
  
  
    {
      INVALID_NUMBER,
       "Valore non consentito:a {1} utilizzato per attributo number:a {0} "},
   
  
  
  
  
  
    {
     ER_ARG_LITERAL,
       "L'argomento di {0} nel pattern di corrispondenza deve essere letterale."},
   
  
  
  
    {
      ER_DUPLICATE_GLOBAL_VAR,
       "Dichiarazione variabile globale duplicata."},
   
  
  
  
  
    {
      ER_DUPLICATE_VAR,
       "Dichiarazione variabile duplicata."},
   
 
  
  
    {
      ER_TEMPLATE_NAME_MATCH,
       "xsl:template deve presentare un name o attributo match (o entrambi)"},
   
 
  
  
    {
      ER_INVALID_PREFIX,
       "Il prefisso in exclude-result-prefixes non \u00e8 valido:a {0}"},
   
  
  
  
    {
      ER_NO_ATTRIB_SET,
       "la serie di attributi denominata {0} \u00e8 inesistente"},
   


 /** WG_FOUND_CURLYBRACE          */


 {
   WG_FOUND_CURLYBRACE,
     "Trovato '}' ma non vi \u00e8 alcun modello di attributi aperto."},


 /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */


 {
   WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
     "Avvertenza: l'attributo di conteggio non corrisponde a un predecessore in xsl:number! Target = {0}."},


 /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */


 {
   WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
     "Sintassi precedente: il nome dell'attributo 'expr' \u00e8 stato cambiato in 'select'."},


 /** WG_NO_LOCALE_IN_FORMATNUMBER          */


 {
   WG_NO_LOCALE_IN_FORMATNUMBER,
     "Xalan non gestisce ancora il nome locale nella funzione format-number."},


 /** WG_LOCALE_NOT_FOUND          */


 {
   WG_LOCALE_NOT_FOUND,
     "Avvertenza: impossibile trovare la versione locale per xml:lang={0}."},


 /** WG_CANNOT_MAKE_URL_FROM          */


 {
   WG_CANNOT_MAKE_URL_FROM,
     "Impossibile creare l'URL da: {0}."},


 /** WG_CANNOT_LOAD_REQUESTED_DOC          */


 {
   WG_CANNOT_LOAD_REQUESTED_DOC,
     "Impossibile caricare il documento richiesto: {0}"},


 /** WG_CANNOT_FIND_COLLATOR          */


 {
   WG_CANNOT_FIND_COLLATOR,
     "Impossibile trovare il collatore per <sort xml:lang={0}."},


 /** WG_FUNCTIONS_SHOULD_USE_URL          */


 {
   WG_FUNCTIONS_SHOULD_USE_URL,
     "Sintassi precedente: l'istruzione delle funzioni deve utilizzare l'URL {0}"},


 /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */


 {
   WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
     "codifica non supportata: {0}, utilizzando UTF-8"},


 /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */


 {
   WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
     "Codifica non supportata: {0}, utilizzando Java {1}"},


 /** WG_SPECIFICITY_CONFLICTS          */


 {
   WG_SPECIFICITY_CONFLICTS,
     "Trovati conflitti di specificit\u00e0: {0} Sar\u00e0 utilizzato l'ultimo trovato nel foglio di stile."},


 /** WG_PARSING_AND_PREPARING          */


 {
   WG_PARSING_AND_PREPARING,
     "========= Analisi e preparazione {0} =========="},


 /** WG_ATTR_TEMPLATE          */


 {
   WG_ATTR_TEMPLATE, "Modello attr., {0}"},


 /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */


 {
   WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
     "Conflitto di corrispondenza tra xsl:strip-space e xsl:preserve-space."},


 /** WG_ATTRIB_NOT_HANDLED          */


 {
   WG_ATTRIB_NOT_HANDLED,
     "Xalan non gestisce ancora l'attributo {0}."},


 /** WG_NO_DECIMALFORMAT_DECLARATION          */


 {
   WG_NO_DECIMALFORMAT_DECLARATION,
     "Non \u00e8 stata trovata alcuna dichiarazione per il formato decimale: {0}"},


 /** WG_OLD_XSLT_NS          */


 {
   WG_OLD_XSLT_NS, "XSLT Namespace mancante o non valido. "},


 /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */


 {
   WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
     "\u00c8 consentita solo una dichiarazione xsl:decimal-format predefinita."},


 /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */


 {
   WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
     "I nomi xsl:decimal-format devono essere univoci. Il nome \"{0}\" \u00e8 duplicato."},


 /** WG_ILLEGAL_ATTRIBUTE          */


 {
   WG_ILLEGAL_ATTRIBUTE,
     "{0} ha un attributo non valido: {1}"},


 /** WG_COULD_NOT_RESOLVE_PREFIX          */


 {
   WG_COULD_NOT_RESOLVE_PREFIX,
     "Impossibile risolvere il prefisso namespace: {0}. Nodo ignorato."},


 /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */


 {
   WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
     "xsl:stylesheet richiede un attributo 'versione'."},


 /** WG_ILLEGAL_ATTRIBUTE_NAME          */


 {
   WG_ILLEGAL_ATTRIBUTE_NAME,
     "Nome attributo non valido: {0}"},


 /** WG_ILLEGAL_ATTRIBUTE_VALUE          */


 {
   WG_ILLEGAL_ATTRIBUTE_VALUE,
     "Valore non valido per l'attributo {0}: {1}."},


 /** WG_EMPTY_SECOND_ARG          */


 {
   WG_EMPTY_SECOND_ARG,
     "Il nodeset risultante dal secondo argomento della funzione documento \u00e8 vuoto. Sar\u00e0 utilizzato il primo argomento."},


 
 
 
   /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */
 
   {
      WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
       "Il valore dell'attributo 'name' del nome xsl:processing-instruction name non deve essere 'xml'"},
  
 
 
   /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */
 
   {
      WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
       "Il valore dell'attributo ''name'' di xsl:processing-instruction deve essere un NCName valido:a {0}"},
  
 
 
   /** WG_ILLEGAL_ATTRIBUTE_POSITION         */
 
   {
     WG_ILLEGAL_ATTRIBUTE_POSITION,
       "Impossibile aggiungere l'attributo {0} dopo i nodi secondari o prima della produzione di un elemento.  L'attributo verr\u00e0 ignorato."},
  
 
 
     { "ui_language",  "it"},
     { "help_language",  "it"},
     { "language",  "it"},
     { "BAD_CODE",
     "I parametri di createMessage sono esterni ai limiti"},
   { "FORMAT_FAILED",
     "Eccezione generata durante la chiamata di messageFormat"},
   { "version",
    ">>>>>>> Xalan Versione "},
   { "version2", "<<<<<<<"},
   { "yes", "s\u00ec"},
   { "line", "Linea #"},
   { "column", "Colonna #"},
   { "xsldone", "XSLProcessor: done"},
   { "xslProc_option",
   "opzioni dalla riga di comando della classe Process di Xalan-J:"},
   { "optionIN", "    -IN inputXMLURL"},
   { "optionXSL", "   [-XSL XSLTransformationURL]"},
   { "optionOUT", "   [-OUT outputFileName]"},
   { "optionLXCIN",
     "   [-LXCIN compiledStylesheetFileNameIn]"},
   { "optionLXCOUT",
     "   [-LXCOUT compiledStylesheetFileNameOutOut]"},
   { "optionPARSER",
     "   [-PARSER nome di classe pienamente qualificato del collegamento parser]"},
   { "optionE", "   [-E (Non espandere i rif entit\u00e0)]"},
   { "optionV", "   [-E (Non espandere i rif entit\u00e0)]"},
   { "optionQC", "   [-QC (Avvertenze di conflitti Quiet Pattern)]"},
   { "optionQ", "   [-Q  (Modalit\u00e0 Quiet)]"},
   { "optionLF",
     "   [-LF (Usa nuove righe solo su output {valore predefinito CR/LF})]"},
   { "optionCR",
     "   [-CR (Usa ritorno a capo solo su output {valore predefinito CR/LF})]"},
   { "optionESCAPE",
     "   [-ESCAPE (Quali carattere saltare {valore predefinito <>&\"\'\\r\\n}]"},
   { "optionINDENT",
     "   [-INDENT (Controlla il numero di spazi del rientro {valore predefinito 0})]"},
  { "optionTT",
     "   [-TT (Traccia i modelli man mano che sono chiamati)]"},
   { "optionTG", "   [-TG (Traccia ogni evento di generazione)]"},
   { "optionTS", "   [-TS (Traccia ogni evento di selezione)]"},
   { "optionTTC",
     "   [-TTC (Traccia gli elementi secondari del modello man mano che sono elaborati)]"},
   { "optionTCLASS",
     "   [-TCLASS (Classe TraceListener per le estensioni di traccia)]"},
   { "optionVALIDATE",
     "   [-VALIDATE (Imposta se eseguire la validazione. Il valore predefinito \u00e8 validazione disattivata.)]"},
   { "optionEDUMP",
     "   [-EDUMP {nome file opzionale} (Esegue il dump dello stack in caso di errore)]"},
   { "optionXML",
     "   [-XML (Utilizza il formattatore XML e aggiunge l'intestazione XML)]"},
  { "optionTEXT",
     "   [-TEXT (Utilizza il formattatore di testo semplice)]"},
   { "optionHTML", "   [-HTML (Utilizza il formattatore HTML)]"},
   { "optionPARAM",
     "   [-PARAM espressione nome (Imposta un parametro di foglio di stile)]"},
   { "noParsermsg1", "Processo XSL non riuscito."},
   { "noParsermsg2", "** Impossibile trovare il parser **"},
   { "noParsermsg3", "Verificare il classpath."},
   { "noParsermsg4",
     "Se non si dispone del parser XML IBM per Java, scaricarlo da"},
   { "noParsermsg5",
   { "optionURIRESOLVER",
   "   [-URIRESOLVER nome classe completo (URIResolver da utilizzare per risolvere gli URI)]"},
   { "optionENTITYRESOLVER",
   "   [-ENTITYRESOLVER nome classe completo (EntityResolver da utilizzare per risolvere le entit\u00e0)]"},
   { "optionCONTENTHANDLER",
   "   [-CONTENTHANDLER nome classe completo (ContentHandler da utilizzare per serializzare l'output)]"},
   { "optionLINENUMBERS",
   "   [-L utilizza i numeri di linea per i documenti sorgente]"},
		
 
 
   { "optionMEDIA",
     " [-MEDIA mediaType (utilizzare l'attributo media per trovare il foglio di stile associato a un documento.)]"},
   { "optionFLAVOR",
   { "optionDIAG",
     " [-DIAG (stampa i millisecondi globali impiegati dalla trasformazione.)]"},
   { "optionINCREMENTAL",
   { "optionNOOPTIMIMIZE",
   { "optionRL",
     " [-RL recursionlimit (garantisce il limite numerico sulla profondit\u00e0 di ricorsione del foglio di stile.)]"},
   { "optionXO",
     " [-XO [transletName] (assegna il nome al translet generato)]"},
   { "optionXD",
     " [-XD destinationDirectory (specifica una directory di destinazione per il translet)]"},
   { "optionXJ",
     " [-XJ jarfile (compatta la classi del translet in un file jar denominato <filejar>)]"},
   { "optionXP",
     " [-XP package (specifica un prefisso del nome di pacchetto per tutte le classi translet generate)]"}
 
  
 };


 /** String for use when a bad error code was encountered.    */
 public static final String BAD_CODE = "BAD_CODE";

 /** String for use when formatting of the error string failed.   */
 public static final String FORMAT_FAILED = "FORMAT_FAILED";

 /** General error string.   */
 public static final String ERROR_STRING = "#error";

 /** String to prepend to error messages.  */
 public static final String ERROR_HEADER = "Errore: ";

 /** String to prepend to warning messages.    */
 public static final String WARNING_HEADER = "Avvertenza: ";

 /** String to specify the XSLT module.  */
 public static final String XSL_HEADER = "XSLT ";

 /** String to specify the XML parser module.  */
 public static final String XML_HEADER = "XML ";

 /** I don't think this is used any more.
  * @deprecated  */
 public static final String QUERY_HEADER = "PATTERN ";

 /**
  * Get the lookup table. 
  *
  * @return The int to message lookup table.
  */
 public Object[][] getContents()
 {
   return contents;
 }


  /**
   * Return the resource file suffic for the indicated locale
   * For most locales, this will be based the language code.  However
   * for Chinese, we do distinguish between Taiwan and PRC
   *
   * @param locale the locale
   * @return an String suffix which canbe appended to a resource name
   */
  private static final String getResourceSuffix(Locale locale)
  {

    String suffix = "_" + locale.getLanguage();
    String country = locale.getCountry();

    if (country.equals("TW"))
      suffix += "_" + country;

    return suffix;
  }
}

