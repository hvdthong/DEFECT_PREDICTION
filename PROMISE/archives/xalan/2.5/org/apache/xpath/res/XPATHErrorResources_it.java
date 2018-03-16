package org.apache.xpath.res;

import java.util.Locale;


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
public class XPATHErrorResources_it extends XPATHErrorResources
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
     "La funzione current() non \u00e8 consentita in un criterio di corrispondenza."},


 /** Field ER_CURRENT_TAKES_NO_ARGS          */


 {
   ER_CURRENT_TAKES_NO_ARGS,
     "La funzione current() non accetta argomenti."},


 /** Field ER_DOCUMENT_REPLACED          */


 {
   ER_DOCUMENT_REPLACED,
     "L'implementazione della funzione document() \u00e8 stata sostituita da org.apache.xalan.xslt.FuncDocument."},


 /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */


 {
   ER_CONTEXT_HAS_NO_OWNERDOC,
     "Il contesto non ha un documento proprietario."},


 /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */


 {
   ER_LOCALNAME_HAS_TOO_MANY_ARGS,
     "local-name() ha troppi argomenti."},


 /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */


 {
   ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
     "namespace-uri() ha troppi argomenti."},


 /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */


 {
   ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
     "normalize-space() ha troppi argomenti."},


 /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */


 {
   ER_NUMBER_HAS_TOO_MANY_ARGS,
     "number() ha troppi argomenti."},


 /** Field ER_NAME_HAS_TOO_MANY_ARGS          */


 {
   ER_NAME_HAS_TOO_MANY_ARGS, "name() ha troppi argomenti."},


 /** Field ER_STRING_HAS_TOO_MANY_ARGS          */


 {
   ER_STRING_HAS_TOO_MANY_ARGS,
     "string() ha troppi argomenti."},


 /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */


 {
   ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
     "string-length() ha troppi argomenti."},


 /** Field ER_TRANSLATE_TAKES_3_ARGS          */


 {
   ER_TRANSLATE_TAKES_3_ARGS,
     "La funzione translate() richiede tre argomenti."},


 /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */


 {
   ER_UNPARSEDENTITYURI_TAKES_1_ARG,
     "La funzione unparsed-entity-uri richiede un argomento."},


 /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */


 {
   ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
     "Asse namespace non ancora implementato."},


 /** Field ER_UNKNOWN_AXIS          */


 {
   ER_UNKNOWN_AXIS, "Asse sconosciuto: {0}"},


 /** Field ER_UNKNOWN_MATCH_OPERATION          */


 {
   ER_UNKNOWN_MATCH_OPERATION, "Operazione di corrispondenza sconosciuta."},


 /** Field ER_INCORRECT_ARG_LENGTH          */


 {
   ER_INCORRECT_ARG_LENGTH,
     "La lunghezza argomento del test di nodo di processing-instruction() non \u00e8 corretta."},


 /** Field ER_CANT_CONVERT_TO_NUMBER          */


 {
   ER_CANT_CONVERT_TO_NUMBER,
     "Impossibile convertire {0} in un numero."},


 /** Field ER_CANT_CONVERT_TO_NODELIST          */


 {
   ER_CANT_CONVERT_TO_NODELIST,
     "Impossibile convertire {0} in NodeList."},


 /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */


 {
   ER_CANT_CONVERT_TO_MUTABLENODELIST,
     "Impossibile convertire {0} in NodeSetDTM."},


 /** Field ER_CANT_CONVERT_TO_TYPE          */


 {
   ER_CANT_CONVERT_TO_TYPE,


 /** Field ER_EXPECTED_MATCH_PATTERN          */


 {
   ER_EXPECTED_MATCH_PATTERN,
     "Previsto criterio di corrispondenza in getMatchScore."},


 /** Field ER_COULDNOT_GET_VAR_NAMED          */


 {
   ER_COULDNOT_GET_VAR_NAMED,
     "Impossibile trovare la variabile denominata {0}"},


 /** Field ER_UNKNOWN_OPCODE          */


 {
   ER_UNKNOWN_OPCODE, "ERRORE. Codice operativo sconosciuto: {0}"},


 /** Field ER_EXTRA_ILLEGAL_TOKENS          */


 {
   ER_EXTRA_ILLEGAL_TOKENS, "Altri token non validi: {0}"},


 /** Field ER_EXPECTED_DOUBLE_QUOTE          */


 {
   ER_EXPECTED_DOUBLE_QUOTE,
     "Letterale non corretto... previste le doppie virgolette."},


 /** Field ER_EXPECTED_SINGLE_QUOTE          */


 {
   ER_EXPECTED_SINGLE_QUOTE,
     "Letterale non corretto... previste le virgolette singole."},


 /** Field ER_EMPTY_EXPRESSION          */


 {
   ER_EMPTY_EXPRESSION, "Espressione vuota."},


 /** Field ER_EXPECTED_BUT_FOUND          */


 {
   ER_EXPECTED_BUT_FOUND, "Previsto {0}, trovato: {1}"},


 /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */


 {
   ER_INCORRECT_PROGRAMMER_ASSERTION,
     "L''asserzione di programmazione non \u00e8 corretta. - {0}"},


 /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */


 {
   ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
     "L'argomento booleano(...) non \u00e8 pi\u00f9 opzionale con 19990709 XPath draft."},


 /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */


 {
   ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
     "Trovato ',' ma senza argomento precedente."},


 /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */


 {
   ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
     "Trovato ',' ma senza argomento successivo."},


 /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */


 {
   ER_PREDICATE_ILLEGAL_SYNTAX,
     "'.. la sintassi [predicato]' o '.[predicato]' non \u00e8 valida. Utilizzare 'self::node()[predicato]'."},


 /** Field ER_ILLEGAL_AXIS_NAME          */


 {
   ER_ILLEGAL_AXIS_NAME, "Nome di asse non valido: {0}"},


 /** Field ER_UNKNOWN_NODETYPE          */


 {
   ER_UNKNOWN_NODETYPE, "Tipo di nodo sconosciuto: {0}"},


 /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */


 {
   ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
     "Il criterio letterale ({0}) deve essere tra virgolette."},


 /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */


 {
   ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
     "Impossibile formattare {0} in un numero."},


 /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */


 {
   ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
     "Impossibile creare il collegamento XML TransformerFactory: {0}"},


 /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */


 {
   ER_DIDNOT_FIND_XPATH_SELECT_EXP,
     "Errore. Impossibile trovare l'espressione di selezione xpath (-select)."},


 /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */


 {
   ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
     "ERRORE. Impossibile trovare ENDOP dopo OP_LOCATIONPATH"},


 /** Field ER_ERROR_OCCURED          */


 {
   ER_ERROR_OCCURED, "Errore"},


 /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */


 {
   ER_ILLEGAL_VARIABLE_REFERENCE,
     "VariableReference fornito per la variabile \u00e8 esterno al contesto o senza definizione.  Nome = {0}"},


 /** Field ER_AXES_NOT_ALLOWED          */


 {
   ER_AXES_NOT_ALLOWED,
     "Nei criteri di corrispondenza sono consentiti solo gli assi child:: e attribute::. Assi non validi = {0}"},


 /** Field ER_KEY_HAS_TOO_MANY_ARGS          */


 {
   ER_KEY_HAS_TOO_MANY_ARGS,
     "key() ha un numero di argomenti non valido."},


 /** Field ER_COUNT_TAKES_1_ARG          */


 {
   ER_COUNT_TAKES_1_ARG,
     "La funzione di conteggio deve prendere un argomento."},


 /** Field ER_COULDNOT_FIND_FUNCTION          */


 {
   ER_COULDNOT_FIND_FUNCTION, "Impossibile trovare la funzione: {0}"},


 /** Field ER_UNSUPPORTED_ENCODING          */


 {
   ER_UNSUPPORTED_ENCODING, "Codifica non supportata: {0}"},


 /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */


 {
   ER_PROBLEM_IN_DTM_NEXTSIBLING,
     "Problema in DTM in getNextSibling... Tentativo di recupero in corso."},


 /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */


 {
   ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
     "Errore di programmazione: impossibile scrivere in EmptyNodeList."},


 /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */


 {
   ER_SETDOMFACTORY_NOT_SUPPORTED,
     "setDOMFactory non \u00e8 supportato da XPathContext."},


 /** Field ER_PREFIX_MUST_RESOLVE          */


 {
   ER_PREFIX_MUST_RESOLVE,
     "Il prefisso deve risolvere in namespace: {0}"},


 /** Field ER_PARSE_NOT_SUPPORTED          */


 {
   ER_PARSE_NOT_SUPPORTED,
     "analisi (sorgente InputSource) non supportata in XPathContext. Impossibile aprire {0}"},


 /** Field ER_SAX_API_NOT_HANDLED          */


 {
   ER_SAX_API_NOT_HANDLED,
     "Caratteri SAX API (char ch[]... non gestiti da DTM."},


 /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */


 {
   ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
     "ignorableWhitespace(char ch[]... non \u00e8 gestito da DTM."},


 /** Field ER_DTM_CANNOT_HANDLE_NODES          */


 {
   ER_DTM_CANNOT_HANDLE_NODES,
     "DTMLiaison non pu\u00f2 gestire nodi di tipo {0}"},


 /** Field ER_XERCES_CANNOT_HANDLE_NODES          */


 {
   ER_XERCES_CANNOT_HANDLE_NODES,
     "DOM2Helper non pu\u00f2 gestire nodi di tipo {0}"},


 /** Field ER_XERCES_PARSE_ERROR_DETAILS          */


 {
   ER_XERCES_PARSE_ERROR_DETAILS,
     "Errore DOM2Helper.parse: SystemID - {0} linea - {1}"},


 /** Field ER_XERCES_PARSE_ERROR          */


 {
   ER_XERCES_PARSE_ERROR, "Errore DOM2Helper.parse"},


 /** Field ER_INVALID_UTF16_SURROGATE          */


 {
   ER_INVALID_UTF16_SURROGATE,
     "Rilevato surrogato di UTF-16 non valido: {0} ?"},


 /** Field ER_OIERROR          */


 {
   ER_OIERROR, "Errore IO"},


 /** Field ER_CANNOT_CREATE_URL          */


 {
   ER_CANNOT_CREATE_URL, "Impossibile creare URL per: {0}"},


 /** Field ER_XPATH_READOBJECT          */


 {
   ER_XPATH_READOBJECT, "In XPath.readObject: {0}"},

 
 /** Field ER_XPATH_READOBJECT         */


 {
   ER_FUNCTION_TOKEN_NOT_FOUND,
     "token di funzione non trovato."},

 
  /**  Can not deal with XPath type:   */


 {
   ER_CANNOT_DEAL_XPATH_TYPE,
      "Impossibile gestire il tipo XPath: {0}"},

 
  /**  This NodeSet is not mutable  */


 {
   ER_NODESET_NOT_MUTABLE,
      "Questo NodeSet non \u00e8 mutabile"},

 
  /**  This NodeSetDTM is not mutable  */


 {
   ER_NODESETDTM_NOT_MUTABLE,
      "Questo NodeSetDTM non \u00e8 mutabile"},

 
  /**  Variable not resolvable:   */


 {
   ER_VAR_NOT_RESOLVABLE,
       "Variabile non risolvibile: {0}"},

 
  /** Null error handler  */


 {
   ER_NULL_ERROR_HANDLER,
       "Errore gestore nullo"},

 
  /**  Programmer's assertion: unknown opcode  */


 {
   ER_PROG_ASSERT_UNKNOWN_OPCODE,
      "Asserzione di programmazione: codice operativo sconosciuto: {0}"},

 
  /**  0 or 1   */


 {
   ER_ZERO_OR_ONE,
      "0 o 1"},

 
 
  /**  rtf() not supported by XRTreeFragSelectWrapper   */


 {
   ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
      "rtf() non supportato da XRTreeFragSelectWrapper"},

 
  /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */


 {
   ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
      "asNodeIterator() non supportato da XRTreeFragSelectWrapper"},

 
  /**  fsb() not supported for XStringForChars   */


 {
   ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
      "fsb() non supportato per XStringForChars"},

 
  /**  Could not find variable with the name of   */


 {
   ER_COULD_NOT_FIND_VAR,
     "Impossibile trovare la variabile denominata {0}"},

 
  /**  XStringForChars can not take a string for an argument   */


 {
   ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
     "XStringForChars non pu\u00f2 prendere una stringa per un argomento"},

 
  /**  The FastStringBuffer argument can not be null   */


 {
   ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
     "L'argomento FastStringBuffer non pu\u00f2 essere nullo"},
   
    /**  2 or 3   */
 
 
   {
     ER_TWO_OR_THREE,
        "2 o 3"},
  
 
    /** Variable accessed before it is bound! */
 
 
   {
     ER_VARIABLE_ACCESSED_BEFORE_BIND,
        "Accesso alla variabile prima della delimitazione."},
  
 
    /** XStringForFSB can not take a string for an argument! */
 
 
   {
     ER_FSB_CANNOT_TAKE_STRING,
        "XStringForFSB non pu\u00f2 avere una stringa per argomento."},
  
 
    /** Error! Setting the root of a walker to null! */
 
 
   {
     ER_SETTING_WALKER_ROOT_TO_NULL,
        "\n !!!! Errore! Impostazione radice walker a zero."},
  
 
    /** This NodeSetDTM can not iterate to a previous node! */
 
 
   {
     ER_NODESETDTM_CANNOT_ITERATE,
        "Questo NodeSetDTM non \u00e8 in grado di eseguire iterazione in un nodo precedente."},
  
 
   /** This NodeSet can not iterate to a previous node! */
 
 
   {
     ER_NODESET_CANNOT_ITERATE,
        "Questo NodeSet non \u00e8 in grado di eseguire iterazione in un nodo precedente."},
  
 
   /** This NodeSetDTM can not do indexing or counting functions! */
 
 
   {
     ER_NODESETDTM_CANNOT_INDEX,
        "Questo NodeSetDTM non \u00e8 in grado di indicizzare o calcolare le funzioni."},
  
 
   /** This NodeSet can not do indexing or counting functions! */
 
 
   {
     ER_NODESET_CANNOT_INDEX,
        "Questo NodeSet non \u00e8 in grado di indicizzare o calcolare le funzioni."},
  
 
   /** Can not call setShouldCacheNodes after nextNode has been called! */
 
 
   {
     ER_CANNOT_CALL_SETSHOULDCACHENODE,
        "Impossibile richiamare setShouldCacheNodes dopo aver richiamato nextNode."},
  
 
   /** {0} only allows {1} arguments */
 
 
   {
     ER_ONLY_ALLOWS,
        "{0} consente solo argomenti {1}"},
  
 
   /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */
 
 
   {
     ER_UNKNOWN_STEP,
        "Conferma del programmatore in getNextStepPos: stepType sconosciuto: {0}"},
  
 
 
   /** Problem with RelativeLocationPath */
 
 
   {
     ER_EXPECTED_REL_LOC_PATH,
  
 
 
   /** Problem with LocationPath */
 
 
   {
     ER_EXPECTED_LOC_PATH,
        "Atteso percorso ubicazione, ma \u00e8 stato incontrato il token seguente\u003a  {0}"},
  
 
 
   /** Problem with Step */
 
 
   {
     ER_EXPECTED_LOC_STEP,
  
 
 
   /** Problem with NodeTest */
 
 
   {
     ER_EXPECTED_NODE_TEST,
        "Atteso test nodo corrispondente a NCName:* o QName."},
  
 
 
   /** Expected step pattern */
 
 
   {
     ER_EXPECTED_STEP_PATTERN,
        "Atteso pattern step, ma \u00e8 stato incontrato '/'."},
  
 
  
   /** Expected relative path pattern */
 
 
   {
     ER_EXPECTED_REL_PATH_PATTERN,
        "Atteso pattern percorso relativo."},
  
 
 
   /** Field ER_CANT_CONVERT_TO_BOOLEAN          */
 
 
   {
     ER_CANT_CONVERT_TO_BOOLEAN,
        "Impossibile convertire {0} in booleano."},
  
 
 
   /** Field ER_CANT_CONVERT_TO_SINGLENODE       */
 
 
   {
     ER_CANT_CONVERT_TO_SINGLENODE,
        "Impossibile convertire {0} in nodo singolo. Questo getter si applica ai tipi ANY_UNORDERED_NODE_TYPE e FIRST_ORDERED_NODE_TYPE."},
  
 
 
   /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */
 
 
   {
     ER_CANT_GET_SNAPSHOT_LENGTH,
        "Impossibile recuperare lunghezza snapshot in tipo: {0}. Questo getter si applica ai tipi UNORDERED_NODE_SNAPSHOT_TYPE e ORDERED_NODE_SNAPSHOT_TYPE."},
  
 
   /** Field ER_NON_ITERATOR_TYPE                */
 
 
   {
     ER_NON_ITERATOR_TYPE,
        "Impossibile eseguire iterazione su tipo non iterativo: {0}"},
  
 
 
   /** Field ER_DOC_MUTATED                      */
 
 
   {
     ER_DOC_MUTATED,
        "Documento modificato dalla restituzione del risultato. Iteratore non valido."},
  
 
   /** Field ER_INVALID_XPATH_TYPE               */
 
 
   {
     ER_INVALID_XPATH_TYPE,
        "Argomento tipo XPath non valido: {0}"},
  
 
   /** Field ER_EMPTY_XPATH_RESULT                */
 
 
   {
     ER_EMPTY_XPATH_RESULT,
        "Oggetto risultato XPath vuoto"},
  
 
   /** Field ER_INCOMPATIBLE_TYPES                */
 
 
   {
     ER_INCOMPATIBLE_TYPES,
        "Il tipo restituito: {0} non pu\u00f2 essere forzato nel tipo specificato: {1}"},
  
 
   /** Field ER_NULL_RESOLVER                     */
 
 
   {
     ER_NULL_RESOLVER,
        "Impossibile risolvere il prefisso con risolutore prefisso nullo."},
  
 
 
   /** Field ER_CANT_CONVERT_TO_STRING            */
 
 
   {
     ER_CANT_CONVERT_TO_STRING,
        "Impossibile convertire {0} in stringa."},
  
 
 
   /** Field ER_NON_SNAPSHOT_TYPE                 */
 
 
   {
     ER_NON_SNAPSHOT_TYPE,
        "Impossibile richiamare snapshotItem su tipo: {0}. Questo metodo \u00e8 valido per i tipi UNORDERED_NODE_SNAPSHOT_TYPE e ORDERED_NODE_SNAPSHOT_TYPE."},
  
 
 
   /** Field ER_WRONG_DOCUMENT                    */
 
 
   {
     ER_WRONG_DOCUMENT,
        "Il nodo contesto non appartiene al documento collegato a questo XPathEvaluator."},
  
 
   /** Field ER_WRONG_NODETYPE                    */
 
 
   {
     ER_WRONG_NODETYPE ,
        "Il tipo di nodo contesto non \u00e8 supportato."},
  
 
   /** Field ER_XPATH_ERROR                       */
 
 
   {
     ER_XPATH_ERROR ,
        "Errore sconosciuto in XPath."},
  
 
 

 /** Field WG_LOCALE_NAME_NOT_HANDLED          */


 {
   WG_LOCALE_NAME_NOT_HANDLED,
     "Il nome locale nella funzione format-number non \u00e8 ancora gestito."},


 /** Field WG_PROPERTY_NOT_SUPPORTED          */


 {
   WG_PROPERTY_NOT_SUPPORTED,
     "Propriet\u00e0 XSL non supportata: {0}"},


 /** Field WG_DONT_DO_ANYTHING_WITH_NS          */


 {
   WG_DONT_DO_ANYTHING_WITH_NS,
     "Nulla da fare correntemente con namespace {0} in propriet\u00e0: {1}"},


 /** Field WG_SECURITY_EXCEPTION          */


 {
   WG_SECURITY_EXCEPTION,
     "Generata SecurityException al tentativo di accedere alle propriet\u00e0 di sistema XSL: {0}"},


 /** Field WG_QUO_NO_LONGER_DEFINED          */


 {
   WG_QUO_NO_LONGER_DEFINED,
     "Sintassi precedente: quo(...) non \u00e8 pi\u00f9 definita in XPath."},


 /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */


 {
   WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
     "XPath deve avere un oggetto derivato per implementare nodeTest."},


 /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */


 {
   WG_FUNCTION_TOKEN_NOT_FOUND,
     "Token di funzione non trovato."},


 /** Field WG_COULDNOT_FIND_FUNCTION          */


 {
   WG_COULDNOT_FIND_FUNCTION,
     "Impossibile trovare la funzione: {0}"},


 /** Field WG_CANNOT_MAKE_URL_FROM          */


 {
   WG_CANNOT_MAKE_URL_FROM,
     "Impossibile creare un URL da: {0}"},


 /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */


 {
   WG_EXPAND_ENTITIES_NOT_SUPPORTED,
     "L'opzione -E non \u00e8 supportata per il parser DTM"},


 /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */


 {
   WG_ILLEGAL_VARIABLE_REFERENCE,
     "VariableReference fornita per la variabile \u00e8 esterna al contesto o senza definizione.  Nome = {0}"},


 /** Field WG_UNSUPPORTED_ENCODING          */


 {
   WG_UNSUPPORTED_ENCODING, "Codifica non supportata: {0}"},



 { "ui_language", "it"},
 { "help_language", "it"},
 { "language", "it"},
   { "BAD_CODE",
     "Il parametro di createMessage \u00e8 esterno ai limiti"},
   { "FORMAT_FAILED",
     "Eccezione generata durante la chiamata di messageFormat"},
   { "version", ">>>>>>> Xalan Versione "},
   { "version2", "<<<<<<<"},
   { "yes", "s\u00ec"},
   { "xsldone", "XSLProcessor: done"},
   { "xpath_option", "xpath options: "},
   { "optionIN", "   [-in inputXMLURL]"},
   { "optionSelect", "   [-select espressione xpath]"},
   { "optionMatch",
     "   [-match match pattern (per la diagnostica di corrispondenza)]"},
   { "optionAnyExpr",
     "O solo un'espressione xpath per eseguire un dump di diagnostica"},
   { "noParsermsg1", "Processo XSL non riuscito."},
   { "noParsermsg2", "** Impossibile trovare il parser **"},
   { "noParsermsg3", "Verificare il classpath."},
   { "noParsermsg4",
     "Se non si dispone del parser XML IBM per Java, scaricarlo da"},
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
 public static final String ERROR_HEADER = "Errore: ";

 /** Field WARNING_HEADER          */
 public static final String WARNING_HEADER = "Avvertenza: ";

 /** Field XSL_HEADER          */
 public static final String XSL_HEADER = "XSL ";

 /** Field XML_HEADER          */
 public static final String XML_HEADER = "XML ";

 /** Field QUERY_HEADER          */
 public static final String QUERY_HEADER = "PATTERN ";

 /**
  * Get the association list.
  *
  * @return The association list.
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

