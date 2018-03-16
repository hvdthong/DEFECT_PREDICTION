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

public class XSLTErrorResources_fr extends XSLTErrorResources
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
      "Erreur : L\u2019expression ne peut pas contenir '{'"},

  /** ER_ILLEGAL_ATTRIBUTE          */

  {
    ER_ILLEGAL_ATTRIBUTE, "{0} dispose d\u2019un attribut non autoris\u00e9 : {1}"},

  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */

  {
    ER_NULL_SOURCENODE_APPLYIMPORTS,
      "sourceNode est vide dans xsl:apply-imports!"},

  /** ER_CANNOT_ADD          */

  {
    ER_CANNOT_ADD, "Impossible d\u2019ajouter {0} \u00e0 {1}"},

  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */

  {
    ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "sourceNode est vide dans handleApplyTemplatesInstruction!"},

  /** ER_NO_NAME_ATTRIB          */

  {
    ER_NO_NAME_ATTRIB, "{0} doit disposer d\u2019un attribut name."},

  /** ER_TEMPLATE_NOT_FOUND          */

  {
    ER_TEMPLATE_NOT_FOUND, "Impossible de trouver le mod\u00e8le : {0}"},

  /** ER_CANT_RESOLVE_NAME_AVT          */

  {
    ER_CANT_RESOLVE_NAME_AVT,
      "Impossible de r\u00e9soudre le nom AVT dans xsl:call-template."},

  /** ER_REQUIRES_ATTRIB          */

  {
    ER_REQUIRES_ATTRIB, "{0} requiert l\u2019attribut : {1}"},

  /** ER_MUST_HAVE_TEST_ATTRIB          */

  {
    ER_MUST_HAVE_TEST_ATTRIB,
      "{0} doit disposer de l\u2019attribut ''test''."},

  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */

  {
    ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "Valeur incorrecte pour l\u2019attribut level : {0}"},

  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */

  {
    ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "Le nom de processing-instruction ne peut pas \u00eatre 'xml'"},

  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */

  {
    ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "Le nom de processing-instruction doit \u00eatre un nom sans deux points correct : {0}"},

  /** ER_NEED_MATCH_ATTRIB          */

  {
    ER_NEED_MATCH_ATTRIB,
      "{0} doit disposer d\u2019un attribut conforme s\u2019il a un mode."},

  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */

  {
    ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} requiert soit un nom soit un attribut conforme."},

  /** ER_CANT_RESOLVE_NSPREFIX          */

  {
    ER_CANT_RESOLVE_NSPREFIX,
      "Impossible de r\u00e9soudre le pr\u00e9fixe d'espace de noms : {0}"},

  /** ER_ILLEGAL_VALUE          */

  {
    ER_ILLEGAL_VALUE, "xml:space dispose d\u2019une valeur non autoris\u00e9e : {0}"},

  /** ER_NO_OWNERDOC          */

  {
    ER_NO_OWNERDOC,
      "Le n\u0153ud enfant ne dispose pas d\u2019un document propri\u00e9taire !"},

  /** ER_ELEMTEMPLATEELEM_ERR          */

  {
    ER_ELEMTEMPLATEELEM_ERR, "Erreur ElemTemplateElement : {0}"},

  /** ER_NULL_CHILD          */

  {
    ER_NULL_CHILD, "Tentative d\u2019ajout d\u2019un enfant vide !"},

  /** ER_NEED_SELECT_ATTRIB          */

  {
    ER_NEED_SELECT_ATTRIB, "{0} requiert un attribut select."},

  /** ER_NEED_TEST_ATTRIB          */

  {
    ER_NEED_TEST_ATTRIB,
      "xsl:when doit disposer d\u2019un attribut 'test'."},

  /** ER_NEED_NAME_ATTRIB          */

  {
    ER_NEED_NAME_ATTRIB,
      "xsl:with-param doit disposer d\u2019un attribut 'name'."},

  /** ER_NO_CONTEXT_OWNERDOC          */

  {
    ER_NO_CONTEXT_OWNERDOC,
      "Le contexte ne dispose pas d\u2019un document propri\u00e9taire !"},

  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */

  {
    ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "Impossible de cr\u00e9er XML TransformerFactory Liaison : {0}"},

  /** ER_PROCESS_NOT_SUCCESSFUL          */

  {
    ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan: \u00e9chec du traitement."},

  /** ER_NOT_SUCCESSFUL          */

  {
    ER_NOT_SUCCESSFUL, "Xalan: \u00e9chec."},

  /** ER_ENCODING_NOT_SUPPORTED          */

  {
    ER_ENCODING_NOT_SUPPORTED, "Encodage non pris en charge : {0}"},

  /** ER_COULD_NOT_CREATE_TRACELISTENER          */

  {
    ER_COULD_NOT_CREATE_TRACELISTENER,
      "Impossible de cr\u00e9er TraceListener : {0}"},

  /** ER_KEY_REQUIRES_NAME_ATTRIB          */

  {
    ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key requiert un attribut 'name' !"},

  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */

  {
    ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key requiert un attribut 'match' !"},

  /** ER_KEY_REQUIRES_USE_ATTRIB          */

  {
    ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key requiert un attribut 'use' !"},

  /** ER_REQUIRES_ELEMENTS_ATTRIB          */

  {
    ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} requiert un attribut ''elements'' !"},

  /** ER_MISSING_PREFIX_ATTRIB          */

  {
    ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) {0} attribut ''prefix'' manquant"},

  /** ER_BAD_STYLESHEET_URL          */

  {
    ER_BAD_STYLESHEET_URL, "L\u2019URL de la feuille de style n\u2019est pas correct : {0}"},

  /** ER_FILE_NOT_FOUND          */

  {
    ER_FILE_NOT_FOUND, "Le fichier de feuille de style est introuvable : {0}"},

  /** ER_IOEXCEPTION          */

  {
    ER_IOEXCEPTION,
      "Exception d\u2019E/S avec le fichier de feuille de style : {0}"},

  /** ER_NO_HREF_ATTRIB          */

  {
    ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) Impossible de trouver l\u2019attribut href pour {0}"},

  /** ER_STYLESHEET_INCLUDES_ITSELF          */

  {
    ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} est directement ou indirectement inclus dans lui-m\u00eame !"},

  /** ER_PROCESSINCLUDE_ERROR          */

  {
    ER_PROCESSINCLUDE_ERROR,
      "Erreur StylesheetHandler.processInclude, {0}"},

  /** ER_MISSING_LANG_ATTRIB          */

  {
    ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) {0} attribut ''lang'' manquant"},

  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */

  {
    ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) \u00e9l\u00e9ment {0} mal plac\u00e9 ?? El\u00e9ment ''component'' de container manquant"},

  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */

  {
    ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "Sortie possible uniquement vers Element, DocumentFragment, Document ou PrintWriter."},

  /** ER_PROCESS_ERROR          */

  {
    ER_PROCESS_ERROR, "Erreur StylesheetRoot.process"},

  /** ER_UNIMPLNODE_ERROR          */

  {
    ER_UNIMPLNODE_ERROR, "Erreur UnImplNode : {0}"},

  /** ER_NO_SELECT_EXPRESSION          */

  {
    ER_NO_SELECT_EXPRESSION,
      "Erreur ! Impossible de trouver l\u2019expression de s\u00e9lection xpath (-select)."},

  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */

  {
    ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "Impossible de mettre en s\u00e9rie un processeur XSL !"},

  /** ER_NO_INPUT_STYLESHEET          */

  {
    ER_NO_INPUT_STYLESHEET,
      "Entr\u00e9e de la feuille de style non sp\u00e9cifi\u00e9e !"},

  /** ER_FAILED_PROCESS_STYLESHEET          */

  {
    ER_FAILED_PROCESS_STYLESHEET,
      "Echec de traitement de la feuille de style !"},

  /** ER_COULDNT_PARSE_DOC          */

  {
    ER_COULDNT_PARSE_DOC, "Impossible d\u2019analyser le document {0} !"},

  /** ER_COULDNT_FIND_FRAGMENT          */

  {
    ER_COULDNT_FIND_FRAGMENT, "Impossible de trouver le fragment\u00a0: {0}"},

  /** ER_NODE_NOT_ELEMENT          */

  {
    ER_NODE_NOT_ELEMENT,
      "Le n\u0153ud identifi\u00e9 par l\u2019identificateur de fragments n\u2019est pas un \u00e9lement : {0}"},

  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */

  {
    ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each doit disposer d\u2019un attribut match ou name"},

  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */

  {
    ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "templates doit disposer d\u2019un attribut match ou name"},

  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */

  {
    ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "Impossible de cloner un fragment de document !"},

  /** ER_CANT_CREATE_ITEM          */

  {
    ER_CANT_CREATE_ITEM,
      "Impossible de cr\u00e9er un objet dans l\u2019arbre de r\u00e9sultats : {0}"},

  /** ER_XMLSPACE_ILLEGAL_VALUE          */

  {
    ER_XMLSPACE_ILLEGAL_VALUE,
      "xml:space dispose d\u2019une valeur non autoris\u00e9e dans la source XML : {0}"},

  /** ER_NO_XSLKEY_DECLARATION          */

  {
    ER_NO_XSLKEY_DECLARATION,
      "Il n\u2019existe pas de d\u00e9claration xsl:key pour for {0} !"},

  /** ER_CANT_CREATE_URL          */

  {
    ER_CANT_CREATE_URL, "Erreur! Impossible de cr\u00e9er une url pour\u00a0: {0}"},

  /** ER_XSLFUNCTIONS_UNSUPPORTED          */

  {
    ER_XSLFUNCTIONS_UNSUPPORTED, "xsl:functions n\u2019est pas pris en charge"},

  /** ER_PROCESSOR_ERROR          */

  {
    ER_PROCESSOR_ERROR, "Erreur XSLT TransformerFactory"},

  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */

  {
    ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} non autoris\u00e9 dans une feuille de style !"},

  /** ER_RESULTNS_NOT_SUPPORTED          */

  {
    ER_RESULTNS_NOT_SUPPORTED,
      "result-ns n\u2019est plus pris en charge ! Utilisez xsl:output \u00e0 la place."},

  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */

  {
    ER_DEFAULTSPACE_NOT_SUPPORTED,
      "default-space n\u2019est plus pris en charge ! Utilisez xsl:strip-space ou xsl:preserve-space \u00e0 la place."},

  /** ER_INDENTRESULT_NOT_SUPPORTED          */

  {
    ER_INDENTRESULT_NOT_SUPPORTED,
      "indent-result n\u2019est plus pris en charge ! Utilisez xsl:output \u00e0 la place."},

  /** ER_ILLEGAL_ATTRIB          */

  {
    ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} dispose d\u2019un attribut non autoris\u00e9 : {1}"},

  /** ER_UNKNOWN_XSL_ELEM          */

  {
    ER_UNKNOWN_XSL_ELEM, "El\u00e9ment XSL inconnu : {0}"},

  /** ER_BAD_XSLSORT_USE          */

  {
    ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort ne peut \u00eatre utilis\u00e9 qu\u2019avec xsl:apply-templates ou xsl:for-each."},

  /** ER_MISPLACED_XSLWHEN          */

  {
    ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) xsl:when mal plac\u00e9 !"},

  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */

  {
    ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:choose n\u2019est pas parent de xsl:when !"},

  /** ER_MISPLACED_XSLOTHERWISE          */

  {
    ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) xsl:otherwise mal plac\u00e9 !"},

  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */

  {
    ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:choose n\u2019est pas parent de xsl:otherwise !"},

  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */

  {
    ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} n\u2019est pas admis dans un mod\u00e8le !"},

  /** ER_UNKNOWN_EXT_NS_PREFIX          */

  {
    ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) {0} pr\u00e9fixe de l\u2019espace de noms de l\u2019extension {1} inconnu"},

  /** ER_IMPORTS_AS_FIRST_ELEM          */

  {
    ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) Les importations ne peuvent intervenir qu\u2019en tant que premiers \u00e9l\u00e9ments de la feuille de style !"},

  /** ER_IMPORTING_ITSELF          */

  {
    ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} est en train de s\u2019importer directement ou indirectement !"},

  /** ER_XMLSPACE_ILLEGAL_VAL          */

  {
    ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) " + "xml:space dispose d\u2019une valeur non autoris\u00e9e : {0}"},

  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */

  {
    ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "Echec de processStylesheet !"},

  /** ER_SAX_EXCEPTION          */

  {
    ER_SAX_EXCEPTION, "Exception SAX"},

  /** ER_XSLT_ERROR          */

  {
    ER_XSLT_ERROR, "Erreur XSLT"},

  /** ER_CURRENCY_SIGN_ILLEGAL          */

  {
    ER_CURRENCY_SIGN_ILLEGAL,
      "Le symbole d\u2019une devise n\u2019est pas admise dans une cha\u00eene conforme au mod\u00e8le"},

  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */

  {
    ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "La fonction Document n\u2019est pas prise en charge dans la feuille de style DOM !"},

  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */

  {
    ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "Impossible de r\u00e9soudre le pr\u00e9fixe d\u2019un r\u00e9solveur sans pr\u00e9fixe !"},

  /** ER_REDIRECT_COULDNT_GET_FILENAME          */

  {
    ER_REDIRECT_COULDNT_GET_FILENAME,
      "Redirect extension : impossible de r\u00e9cup\u00e9rer le nom de fichier \u2013 l\u2019attribut file ou select doit retourner une cha\u00eene valide."},

  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */

  {
    ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "Impossible de cr\u00e9er FormatterListener dans Redirect extension !"},

  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */

  {
    ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "Le pr\u00e9fixe dans exclude-result-prefixes n\u2019est pas valide : {0}"},

  /** ER_MISSING_NS_URI          */

  {
    ER_MISSING_NS_URI,
      "URI d\u2019espace de noms manquant pour le pr\u00e9fixe sp\u00e9cifi\u00e9"},

  /** ER_MISSING_ARG_FOR_OPTION          */

  {
    ER_MISSING_ARG_FOR_OPTION,
      "Argument manquant pour l\u2019option : {0}"},

  /** ER_INVALID_OPTION          */

  {
    ER_INVALID_OPTION, "Option incorrecte : {0}"},

  /** ER_MALFORMED_FORMAT_STRING          */

  {
    ER_MALFORMED_FORMAT_STRING, "Cha\u00eene de format mal form\u00e9e : {0}"},

  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

  {
    ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet requiert un attribut 'version' !"},

  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */

  {
    ER_ILLEGAL_ATTRIBUTE_VALUE,
      "L\u2019attribut : {0} dispose d\u2019une valeur non autoris\u00e9e : {1}"},

  /** ER_CHOOSE_REQUIRES_WHEN          */

  {
    ER_CHOOSE_REQUIRES_WHEN, "xsl:choose requiert un xsl:when"},

  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */

  {
    ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports n\u2019est pas admis dans un xsl:for-each"},

  /** ER_CANT_USE_DTM_FOR_OUTPUT          */

  {
    ER_CANT_USE_DTM_FOR_OUTPUT,
      "Impossible d'utiliser DTMLiaison pour un noeud de sortie DOM ... Utilisez org.apache.xpath.DOM2Helper \u00e0 la place !"},

  /** ER_CANT_USE_DTM_FOR_INPUT          */

  {
    ER_CANT_USE_DTM_FOR_INPUT,
      "Impossible d'utiliser DTMLiaison pour un noeud d'entr\u00e9e DOM... Utilisez org.apache.xpath.DOM2Helper \u00e0 la place!"},

  /** ER_CALL_TO_EXT_FAILED          */

  {
    ER_CALL_TO_EXT_FAILED,
      "Echec de l\u2019appel de l\u2019\u00e9l\u00e9ment d\u2019extension : {0}"},

  /** ER_PREFIX_MUST_RESOLVE          */

  {
    ER_PREFIX_MUST_RESOLVE,
      "Le pr\u00e9fixe doit se r\u00e9soudre en espace de nom : {0}"},

  /** ER_INVALID_UTF16_SURROGATE          */

  {
    ER_INVALID_UTF16_SURROGATE,
      "Substitut UTF-16 incorrect d\u00e9tect\u00e9 : {0} ?"},

  /** ER_XSLATTRSET_USED_ITSELF          */

  {
    ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} s\u2019est utilis\u00e9 lui-m\u00eame, ce qui va entra\u00eener une boucle sans fin."},

  /** ER_CANNOT_MIX_XERCESDOM          */

  {
    ER_CANNOT_MIX_XERCESDOM,
      "Impossible de m\u00e9langer des entr\u00e9es non Xerces-DOM avec des sorties Xerces-DOM !"},

  /** ER_TOO_MANY_LISTENERS          */

  {
    ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},

  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */

  {
    ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "Dans ElemTemplateElement.readObject : {0}"},

  /** ER_DUPLICATE_NAMED_TEMPLATE          */

  {
    ER_DUPLICATE_NAMED_TEMPLATE,
      "Plusieurs mod\u00e8les trouv\u00e9s nomm\u00e9s : {0}"},

  /** ER_INVALID_KEY_CALL          */

  {
    ER_INVALID_KEY_CALL,
      "Appel de fonction incorrect : les appels de recursive key() ne sont pas autoris\u00e9s"},
  
  /** Variable is referencing itself          */

  {
    ER_REFERENCING_ITSELF,
      "La variable {0} est en train de se r\u00e9f\u00e9rencer directement ou indirectement !"},
  
  /** Illegal DOMSource input          */

  {
    ER_ILLEGAL_DOMSOURCE_INPUT,
      "Le n\u0153ud d\u2019entr\u00e9e ne peut pas \u00eatre vide au niveau d\u2019une source DOM pour newTemplates !"},
	
	/** Class not found for option         */

  {
    ER_CLASS_NOT_FOUND_FOR_OPTION,
			"Fichier de classe introuvable pour l\u2019option {0}"},
	
	/** Required Element not found         */

  {
    ER_REQUIRED_ELEM_NOT_FOUND,
			"El\u00e9ment requis introuvable : {0}"},
  
  /** InputStream cannot be null         */

  {
    ER_INPUT_CANNOT_BE_NULL,
			"InputStream ne peut pas \u00eatre vide"},
  
  /** URI cannot be null         */

  {
    ER_URI_CANNOT_BE_NULL,
			"URI ne peut pas \u00eatre vide"},
  
  /** File cannot be null         */

  {
    ER_FILE_CANNOT_BE_NULL,
			"File ne peut pas \u00eatre vide"},
  
   /** InputSource cannot be null         */

  {
    ER_SOURCE_CANNOT_BE_NULL,
			"InputSource ne peut pas \u00eatre vide"},
  
  /** Could not initialize BSF Manager        */

  {
    ER_CANNOT_INIT_BSFMGR,
			"Impossible d\u2019initialiser BSF Manager"},
  
  /** Could not compile extension       */

  {
    ER_CANNOT_CMPL_EXTENSN,
			"Impossible de compiler l\u2019extension"},

  /** Could not create extension       */

  {
    ER_CANNOT_CREATE_EXTENSN,
      "Impossible de cr\u00e9er l\u2019extension : {0} \u00e0 cause de : {1}"},
  
  /** Instance method call to method {0} requires an Object instance as first argument       */

  {
    ER_INSTANCE_MTHD_CALL_REQUIRES,
      "L\u2019appel de la m\u00e9thode d\u2019instance \u00e0 la m\u00e9thode {0} requiert une instance Object comme premier argument"},
  
  /** Invalid element name specified       */

  {
    ER_INVALID_ELEMENT_NAME,
      "Nom d\u2019\u00e9l\u00e9ment sp\u00e9cifi\u00e9 incorrect {0}"},
  
   /** Element name method must be static      */

  {
    ER_ELEMENT_NAME_METHOD_STATIC,
      "La m\u00e9thode de nom d\u2019\u00e9l\u00e9ment doit \u00eatre statique {0}"},
  
   /** Extension function {0} : {1} is unknown      */

  {
    ER_EXTENSION_FUNC_UNKNOWN,
             "Fonction d\u2019extension {0} : {1} inconnue"},
  
   /** More than one best match for constructor for       */

  {
    ER_MORE_MATCH_CONSTRUCTOR,
             "Plusieurs occurrences exactes pour le constructeur pour {0}"},
  
   /** More than one best match for method      */

  {
    ER_MORE_MATCH_METHOD,
             "Plusieurs occurrences exactes pour la m\u00e9thode {0}"},
  
   /** More than one best match for element method      */

  {
    ER_MORE_MATCH_ELEMENT,
             " Plusieurs occurrences exactes pour la m\u00e9thode d\u2019\u00e9l\u00e9ments {0}"},
  
   /** Invalid context passed to evaluate       */

  {
    ER_INVALID_CONTEXT_PASSED,
             "Contexte incorrect pour l\u2019\u00e9valuation {0}"},
  
   /** Pool already exists       */

  {
    ER_POOL_EXISTS,
             "Pool existe d\u00e9j\u00e0"},
  
   /** No driver Name specified      */

  {
    ER_NO_DRIVER_NAME,
             "Aucun nom de pilote sp\u00e9cifi\u00e9"},
  
   /** No URL specified     */

  {
    ER_NO_URL,
             "Aucune URL sp\u00e9cifi\u00e9e"},
  
   /** Pool size is less than one    */

  {
    ER_POOL_SIZE_LESSTHAN_ONE,
             "La taille du Pool est inf\u00e9rieure \u00e0 un !"},
  
   /** Invalid driver name specified    */

  {
    ER_INVALID_DRIVER,
             "Le nom de pilote sp\u00e9cifi\u00e9 n\u2019est pas correct !"},
  
   /** Did not find the stylesheet root    */

  {
    ER_NO_STYLESHEETROOT,
             "Impossible de trouver la feuille de style racine !"},
  
   /** Illegal value for xml:space     */

  {
    ER_ILLEGAL_XMLSPACE_VALUE,
         "Valeur non autoris\u00e9e pour xml:space"},
  
   /** processFromNode failed     */

  {
    ER_PROCESSFROMNODE_FAILED,
         "Echec de processFromNode"},
  
   /** The resource [] could not load:     */

  {
    ER_RESOURCE_COULD_NOT_LOAD,
        "La ressource [ {0} ] n\u2019a pas pu \u00eatre charg\u00e9e : {1} \n {2} \t {3}"},
   
  
   /** Buffer size <=0     */

  {
    ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "Taille du tampon <=0"},
  
   /** Unknown error when calling extension    */

  {
    ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "Erreur inconnue lors de l\u2019appel de l\u2019extension"},
  
   /** Prefix {0} does not have a corresponding namespace declaration    */

  {
    ER_NO_NAMESPACE_DECL,
        "Le pr\u00e9fixe {0} de dispose pas d\u2019une d\u00e9claration d\u2019espaces de noms correspondante"},
  
   /** Element content not allowed for lang=javaclass   */

  {
    ER_ELEM_CONTENT_NOT_ALLOWED,
        "Contenu d\u2019\u00e9l\u00e9ment non autoris\u00e9 pour lang=javaclass {0}"},
  
   /** Stylesheet directed termination   */

  {
    ER_STYLESHEET_DIRECTED_TERMINATION,
        "Ach\u00e8vement dirig\u00e9 de la feuille de style"},
  
   /** 1 or 2   */

  {
    ER_ONE_OR_TWO,
        "1 ou 2"},
  
   /** 2 or 3   */

  {
    ER_TWO_OR_THREE,
        "2 ou 3"},

   /** Could not load {0} (check CLASSPATH), now using just the defaults   */

  {
    ER_COULD_NOT_LOAD_RESOURCE,
        "Impossible de charger {0} (v\u00e9rifier le CHEMIN DE CLASSE). Utilisation des mod\u00e8les par d\u00e9faut"},
  
   /** Cannot initialize default templates   */

  {
    ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "Impossible d\u2019initialiser les mod\u00e8les par d\u00e9faut"},
  
   /** Result should not be null   */

  {
    ER_RESULT_NULL,
        "Le r\u00e9sultat ne peut pas \u00eatre vide"},
    
   /** Result could not be set   */

  {
    ER_RESULT_COULD_NOT_BE_SET,
        "Le r\u00e9sultat ne peut pas \u00eatre d\u00e9fini"},
  
   /** No output specified   */

  {
    ER_NO_OUTPUT_SPECIFIED,
        "Aucune sortie sp\u00e9cifi\u00e9e"},
  
   /** Can't transform to a Result of type   */

  {
    ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "Transformation impossible en un r\u00e9sultat de type {0}"},
  
   /** Can't transform to a Source of type   */

  {
    ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "Transformation impossible d\u2019une source de type {0}"},
  
   /** Null content handler  */

  {
    ER_NULL_CONTENT_HANDLER,
        "Gestionnaire de contenu vide"},
  
   /** Null error handler  */

  {
    ER_NULL_ERROR_HANDLER,
        "Gestionnaire d\u2019erreurs vide"},
  
   /** parse can not be called if the ContentHandler has not been set */

  {
    ER_CANNOT_CALL_PARSE,
        "L\u2019analyse ne peut \u00eatre appel\u00e9e si le gestionnaire de contenu n\u2019a pas \u00e9t\u00e9 d\u00e9fini"},
  
   /**  No parent for filter */

  {
    ER_NO_PARENT_FOR_FILTER,
        "Aucun parent pour le filtre"},
  
  
   /**  No stylesheet found in: {0}, media */

  {
    ER_NO_STYLESHEET_IN_MEDIA,
         "Aucune feuille de style trouv\u00e9e dans: {0}, media= {1}"},
  
   /**  No xml-stylesheet PI found in */

  {
    ER_NO_STYLESHEET_PI,
         "Aucun xml-stylesheet PI trouv\u00e9 dans : {0}"},
  
   /**  Not supported  */

  {
    ER_NOT_SUPPORTED,
       "Non pris en charge : {0}"},
  
   /**  Value for property {0} should be a Boolean instance  */

  {
    ER_PROPERTY_VALUE_BOOLEAN,
       "La valeur pour la propri\u00e9t\u00e9 {0} doit \u00eatre une instance bool\u00e9enne"},
  
   /* This key/message changed ,NEED ER_COULD_NOT_FIND_EXTERN_SCRIPT: Pending,Ramesh */

   /** src attribute not yet supported for  */

  {
    "ER_SRC_ATTRIB_NOT_SUPPORTED",
       "L\u2019attribut src n\u2019est pas encore pris en charge pour {0}"},
  
  /** The resource [] could not be found     */

  {
    ER_RESOURCE_COULD_NOT_FIND,
        "Impossible de trouver la ressource [ {0} ].\n {1}"},
  
   /** output property not recognized:  */

  {
    ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "La propri\u00e9t\u00e9 de sortie n\u2019a pas \u00e9t\u00e9 reconnue : {0}"},
  
   /** Failed creating ElemLiteralResult instance   */

  {
    ER_FAILED_CREATING_ELEMLITRSLT,
        "Echec de cr\u00e9ation de l\u2019instance ElemLiteralResult"},

  
   /** Priority value does not contain a parsable number   */

  {
    ER_VALUE_SHOULD_BE_NUMBER,
        "La valeur de {0} doit contenir un nombre analysable"}, 
  
   /**  Value for {0} should equal 'yes' or 'no'   */

  {
    ER_VALUE_SHOULD_EQUAL,
        "La valeur pour {0} doit \u00eatre \u00e9quivalente \u00e0 oui ou non"},
 
   /**  Failed calling {0} method   */

  {
    ER_FAILED_CALLING_METHOD,
        "Echec d\u2019appel de la m\u00e9thode {0}"},
  
   /** Failed creating ElemLiteralResult instance   */

  {
    ER_FAILED_CREATING_ELEMTMPL,
        "Echec de cr\u00e9ation de l\u2019instance ElemTemplateElement"},
  
   /**  Characters are not allowed at this point in the document   */

  {
    ER_CHARS_NOT_ALLOWED,
        "Les caract\u00e8res ne sont pas admis \u00e0 ce niveau du document"},
  
  /**  attribute is not allowed on the element   */

  {
    ER_ATTR_NOT_ALLOWED,
        "L\u2019attribut \"{0}\" n\u2019est pas admis dans l\u2019\u00e9l\u00e9ment {1} !"},
 
  /**  Bad value    */

  {
    ER_BAD_VALUE,
     "{0} valeur incorrecte {1}"},
  
  /**  attribute value not found   */

  {
    ER_ATTRIB_VALUE_NOT_FOUND,
     "Valeur de l\u2019attribut {0} introuvable"},
  
  /**  attribute value not recognized    */

  {
    ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "Valeur de l\u2019attribut {0} non reconnue"},

  /** Attempting to generate a namespace prefix with a null URI   */

  {
    ER_NULL_URI_NAMESPACE,
     "Tentative de g\u00e9n\u00e9ration d'un pr\u00e9fixe d'expace de nom avec un URI nul"},


   /** Attempting to generate a namespace prefix with a null URI   */

  {
    ER_NUMBER_TOO_BIG,
     "Tentative de formatage d'un nombre sup\u00e9rieur \u00e0 l'entier le plus long"},



  {
    ER_CANNOT_FIND_SAX1_DRIVER,
     "Classe de pilotes SAX1 {0} introuvable"},


  {
    ER_SAX1_DRIVER_NOT_LOADED,
     "La classe de pilotes SAX1 {0} a \u00e9t\u00e9 trouv\u00e9e mais n''a pas \u00e9t\u00e9 charg\u00e9e"},


  {
    ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "La classe de pilotes SAX1 {0} a \u00e9t\u00e9 charg\u00e9e mais n''a pas \u00e9t\u00e9 instanci\u00e9e"},



  {
    ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "La classe de pilotes SAX1 {0} n''impl\u00e9mente pas org.xml.sax.Parser"},


  {
    ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "Les propri\u00e9t\u00e9s du syst\u00e8me org.xml.sax.parser n'ont pas \u00e9t\u00e9 sp\u00e9cifi\u00e9es"},


  {
    ER_PARSER_ARG_CANNOT_BE_NULL,
     "L'argument de l'analyseur ne doit pas \u00eatre nul"},



  {
    ER_FEATURE,
     "Fonction : a {0}"},



  {
    ER_PROPERTY,
     "Propri\u00e9t\u00e9 : a {0}"},


  {
    ER_NULL_ENTITY_RESOLVER,
     "Convertisseur d'entit\u00e9 nul"},


  {
    ER_NULL_DTD_HANDLER,
     "Gestionnaire de DTD nul"},

  {
    ER_NO_DRIVER_NAME_SPECIFIED,
     "Aucun nom de pilote sp\u00e9cifi\u00e9 !"},


  {
    ER_NO_URL_SPECIFIED,
     "Aucun URL sp\u00e9cifi\u00e9 !"},


  {
    ER_POOLSIZE_LESS_THAN_ONE,
     "La taille du pool est inf\u00e9rieure \u00e0 1 !"},


  {
    ER_INVALID_DRIVER_NAME,
     "Nom de pilote sp\u00e9cifi\u00e9 incorrect !"},



  {
    ER_ERRORLISTENER,
     "ErrorListener"},


  {
    ER_ASSERT_NO_TEMPLATE_PARENT,
     "Erreur du programmeur ! expr n'a pas de parent ElemTemplateElement !"},


  {
    ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "Assertion du programmeur dans RundundentExprEliminator : {0}"},

  {
    ER_NOT_ALLOWED_IN_POSITION,
     "{0} n'est pas admis \u00e0 cet endroit de la feuille de style !"},

  {
    ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "Le texte sans espace blanc n'est pas admis \u00e0 cet endroit de la feuille de style !"},

  {
    INVALID_TCHAR,
     "Valeur incorrecte : {1} est utilis\u00e9 pour l'attribut CHAR : {0}.  Un attribut de type CHAR doit \u00eatre compos\u00e9 d'un seul caract\u00e8re !"},


  {
    INVALID_QNAME,
     "Valeur incorrecte :a {1} utilis\u00e9 pour l''attribut QNAME :a {0}"},


  {
    INVALID_ENUM,
     "Valeur incorrecte :a {1} utilis\u00e9 pour l''attribut ENUM :a {0}.  Les valeurs correctes sont :a {2}."},


  {
    INVALID_NMTOKEN,
     "Valeur incorrecte :a {1} utilis\u00e9 pour l''attribut NMTOKEN :a {0} "},


  {
    INVALID_NCNAME,
     "Valeur incorrecte :a {1} utilis\u00e9 pour l''attribut NCNAME :a {0} "},



  {
    INVALID_BOOLEAN,
     "Valeur incorrecte :a {1} utilis\u00e9 pour l''attribut boolean :a {0} "},


  {
    INVALID_NUMBER,
     "Valeur incorrecte :a {1} utilis\u00e9 pour l''attribut number :a {0} "},




  {
    ER_ARG_LITERAL,
     "L''argument de {0} de la structure de comparaison doit \u00eatre un litt\u00e9ral."},


  {
    ER_DUPLICATE_GLOBAL_VAR,
     "Duplication de la d\u00e9claration de variable globale."},



  {
    ER_DUPLICATE_VAR,
     "Duplication de la d\u00e9claration de variable."},


  {
    ER_TEMPLATE_NAME_MATCH,
     "xsl:template doit avoir un attribut name ou match (ou les deux)"},


  {
    ER_INVALID_PREFIX,
     "Le pr\u00e9fixe de exclude-result-prefixes est incorrect :a {0}"},


  {
    ER_NO_ATTRIB_SET,
     "L''ensemble d'attributs {0} n'existe pas"},



  /** WG_FOUND_CURLYBRACE          */

  {
    WG_FOUND_CURLYBRACE,
      "'}' trouv\u00e9 mais aucun mod\u00e8le d\u2019attribut ouvert !"},

  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */

  {
    WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "Attention : l\u2019attribut count ne correspond pas \u00e0 un anc\u00eatre dans xsl:number! Target = {0}"},

  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */

  {
    WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "Ancienne syntaxe : le nom de l\u2019attribut 'expr' \u00e9t\u00e9 chang\u00e9 par 'select'."},

  /** WG_NO_LOCALE_IN_FORMATNUMBER          */

  {
    WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan ne g\u00e8re pas encore la partie locale du nom dans la fonction format-number."},

  /** WG_LOCALE_NOT_FOUND          */

  {
    WG_LOCALE_NOT_FOUND,
      "Attention : Impossible de trouver la partie locale du nom pour xml:lang={0}"},

  /** WG_CANNOT_MAKE_URL_FROM          */

  {
    WG_CANNOT_MAKE_URL_FROM,
      "Impossible de cr\u00e9er une URL \u00e0 partir de : {0}"},

  /** WG_CANNOT_LOAD_REQUESTED_DOC          */

  {
    WG_CANNOT_LOAD_REQUESTED_DOC,
      "Impossible de charger le document demand\u00e9 : {0}"},

  /** WG_CANNOT_FIND_COLLATOR          */

  {
    WG_CANNOT_FIND_COLLATOR,
      "Impossible de trouver Collator pour <sort xml:lang={0}"},

  /** WG_FUNCTIONS_SHOULD_USE_URL          */

  {
    WG_FUNCTIONS_SHOULD_USE_URL,
      "Ancienne syntaxe : les fonctions doivent utiliser une url de {0}"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */

  {
    WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "Encodage non pris en charge : {0}, en utilisant UTF-8"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */

  {
    WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      " Encodage non pris en charge: {0}, en utilisant Java {1}"},

  /** WG_SPECIFICITY_CONFLICTS          */

  {
    WG_SPECIFICITY_CONFLICTS,
      "Conflits de sp\u00e9cificit\u00e9 d\u00e9tect\u00e9s : {0}, le dernier trouv\u00e9 dans la feuille de style sera utilis\u00e9."},

  /** WG_PARSING_AND_PREPARING          */

  {
    WG_PARSING_AND_PREPARING,
      "========= Analyse et pr\u00e9paration {0} =========="},

  /** WG_ATTR_TEMPLATE          */

  {
    WG_ATTR_TEMPLATE, "Mod\u00e8le d\u2019attribut, {0}"},

  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */

  {
    WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "Conflit de correspondance entre xsl:strip-space et xsl:preserve-space"},

  /** WG_ATTRIB_NOT_HANDLED          */

  {
    WG_ATTRIB_NOT_HANDLED,
      "Xalan ne g\u00e8re pas encore l\u2019attribut {0} !"},

  /** WG_NO_DECIMALFORMAT_DECLARATION          */

  {
    WG_NO_DECIMALFORMAT_DECLARATION,
      "Aucune d\u00e9claration trouv\u00e9e pour le format d\u00e9cimal : {0}"},

  /** WG_OLD_XSLT_NS          */

  {
    WG_OLD_XSLT_NS, "Espace de noms XSLT manquant ou incorrect. "},

  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */

  {
    WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "Seule une d\u00e9claration xsl:decimal-format par d\u00e9faut est autoris\u00e9e."},

  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */

  {
    WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "Les noms xsl:decimal-format doivent \u00eatre uniques. Le nom \"{0}\" appara\u00eet en double."},

  /** WG_ILLEGAL_ATTRIBUTE          */

  {
    WG_ILLEGAL_ATTRIBUTE,
      "{0} dispose d\u2019un attribut non autoris\u00e9 : {1}"},

  /** WG_COULD_NOT_RESOLVE_PREFIX          */

  {
    WG_COULD_NOT_RESOLVE_PREFIX,
      "Impossible de r\u00e9soudre de pr\u00e9fixe d\u2019espace de noms : {0}. Le n\u0153ud sera ignor\u00e9."},

  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

  {
    WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet requiert un attribut 'version' !"},

  /** WG_ILLEGAL_ATTRIBUTE_NAME          */

  {
    WG_ILLEGAL_ATTRIBUTE_NAME,
      "Nom d\u2019attribut non autoris\u00e9 : {0}"},

  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */

  {
    WG_ILLEGAL_ATTRIBUTE_VALUE,
      "La valeur utilis\u00e9e pour l\u2019attribut {0} n\u2019est pas autoris\u00e9e : {1}"},

  /** WG_EMPTY_SECOND_ARG          */

  {
    WG_EMPTY_SECOND_ARG,
      "L\u2019ensemble de n\u0153uds r\u00e9sultant d\u2019un deuxi\u00e8me argument de la fonction document est vide. Le premier argument sera utilis\u00e9."},




  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */
  {
     WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "La valeur de l'attribut 'name' de xsl:processing-instruction ne doit pas \u00eatre 'xml'"},


  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */
  {
     WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "La valeur de l''attribut ''name'' de xsl:processing-instruction doit \u00eatre un NCName valide :a {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */
  {
    WG_ILLEGAL_ATTRIBUTE_POSITION,
      "Impossible d''ajouter l''attribut {0}apr\u00e8s les noeuds enfants ou avant qu'un \u00e9l\u00e9ment ne soit produit.  L'attribut est ignor\u00e9."},


    { "ui_language", "fr"},
    { "help_language", "fr"},
    { "language", "fr"},
    { "BAD_CODE",
      "Param\u00e8tre pour createMessage hors limites"},
    { "FORMAT_FAILED",
      "Exception \u00e9mise pendant l\u2019appel de messageFormat "},
    {"version",
      ">>>>>>> Version Xalan "},
    { "version2",  "<<<<<<<"},
    {"yes",  "oui"},
    { "line", "N\u00b0 de ligne"},
    {"column", "N\u00b0 de colonne"},
    {"xsldone", "XSLProcessor: termin\u00e9"},
    {"xslProc_option", "Options de classe de traitement de la ligne de commande Xalan-J :"},
    {"optionIN",  "    -IN inputXMLURL"},
    {  "optionXSL",  "   [-XSL XSLTransformationURL]"},
    { "optionOUT",  "   [-OUT outputFileName]"},
    { "optionLXCIN", "   [-LXCIN compiledStylesheetFileNameIn]"},
    { "optionLXCOUT",
      "   [-LXCOUT compiledStylesheetFileNameOutOut]"},
    { "optionPARSER",
      "   [-PARSER nom de classe qualifi\u00e9 pour la liaison de l\u2019analyseur]"},
    { "optionE",
      "   [-E (Ne pas d\u00e9velopper les r\u00e9f\u00e9rences d\u2019entit\u00e9s)]"},
    { "optionV",  "   [-E (Ne pas d\u00e9velopper les r\u00e9f\u00e9rences d\u2019entit\u00e9s)]"},
    { "optionQC",
      "   [-QC (Avertissements pour les conflits silencieux de formes)]"},
    {"optionQ",
      "   [-Q  (Mode silencieux)]"},
    { "optionLF",
      "   [-LF (Utilisation des sauts de ligne uniquement en sortie {CR/LF par d\u00e9faut})]"},
    { "optionCR",
      "   [-CR (Utilisation des retours chariot uniquement en sortie {CR/LF par d\u00e9faut})]"},
    {  "optionESCAPE",
      "   [-ESCAPE (Caract\u00e8res \u00e0 remplacer {<>&\"\'\\r\\n par d\u00e9faut}]"},
    {  "optionINDENT",
      "   [-INDENT (Contr\u00f4le le nombre d\u2019espaces pour le retrait {0 par d\u00e9faut})]"},
    {  "optionTT",
      "   [-TT (Trace des mod\u00e8les lors de leur appel.)]"},
    { "optionTG",
      "   [-TG (Trace de chaque cr\u00e9ation d\u2019\u00e9v\u00e9nement.)]"},
    { "optionTS",
      "   [-TS (Trace de chaque s\u00e9lection d\u2019\u00e9v\u00e9nement.)]"},
    { "optionTTC",
      "   [-TTC (Trace de chaque mod\u00e8le enfant lorsqu\u2019ils sont trait\u00e9s.)]"},
    { "optionTCLASS",
      "   [-TCLASS (Classe TraceListener pour les extensions de trace.)]"},
    { "optionVALIDATE",
      "   [-VALIDATE (D\u00e9termine si la validation intervient. La validation est d\u00e9sactiv\u00e9e par d\u00e9faut.)]"},
    { "optionEDUMP",
      "   [-EDUMP {optional filename} (Permet d\u2019acc\u00e9der \u00e0 l\u2019emplacement de l\u2019erreur.)]"},
    { "optionXML",
      "   [-XML (Utilisation d\u2019un formateur XML et ajout d\u2019en-t\u00eate XML.)]"},
    {  "optionTEXT",
      "   [-TEXT (Utilisation d\u2019un formateur de texte simple.)]"},
    { "optionHTML", "   [-HTML (Utilisation d\u2019un formateur HTML.)]"},
    { "optionPARAM",
      "   [-PARAM name expression (D\u00e9finition d\u2019un param\u00e8tre de feuille de style)]"},
    {  "noParsermsg1",
      "Echec de XSL Process."},
    {  "noParsermsg2",
      "** Impossible de trouver l\u2019analyseur **"},
    { "noParsermsg3",
      "Veuillez v\u00e9rifier votre chemin de classe."},
    {  "noParsermsg4",
      " Si vous ne disposez pas de l\u2019analyseur XML d\u2019IBM pour Java, vous pouvez le t\u00e9l\u00e9charger \u00e0 l\u2019adresse suivante "},
    {  "noParsermsg5",
    { "optionURIRESOLVER",
      "   [-URIRESOLVER nom de classe complet (URIResolver \u00e0 utiliser pour r\u00e9soudre les URI)]"},
    { "optionENTITYRESOLVER",
      "   [-ENTITYRESOLVER nom de classe complet (EntityResolver \u00e0 utiliser pour r\u00e9soudre les entit\u00e9s)]"},
    { "optionCONTENTHANDLER",
      "   [-CONTENTHANDLER nom de classe complet (ContentHandler \u00e0 utiliser pour mettre en s\u00e9rie les sorties)]"},
    { "optionLINENUMBERS",
      "   [-L Utilisation des nombres de lignes pour le document source]"},



    { "optionMEDIA",
      " [-MEDIA mediaType (utilisation de l'attribut media pour rechercher la feuille de style associ\u00e9e \u00e0 un document.)]"},
    { "optionFLAVOR",
    { "optionDIAG",
      " [-DIAG (Impression du nombre global de millisecondes de la transformation.)]"},
    {  "optionINCREMENTAL",
    { "optionNOOPTIMIMIZE",
    { "optionRL",
     " [-RL recursionlimit (assertion d'une limite num\u00e9rique sur la profondeur de r\u00e9cursion de la feuille de style.)]"},
    { "optionXO",
     " [-XO [transletName] (affectation du nom au translet g\u00e9n\u00e9r\u00e9)]"},
    { "optionXD",
     " [-XD destinationDirectory (sp\u00e9cification d'un r\u00e9pertoire de destination pour le translet)]"},
    { "optionXJ",
     " [-XJ jarfile (regroupe les classes de translet dans un fichier jar nomm\u00e9 <jarfile>)]"},
    { "optionXP",
     " [-XP package (sp\u00e9cifie un pr\u00e9fixe de nom de groupe pour toutes les classes de translet g\u00e9n\u00e9r\u00e9es)]"}


  };


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "BAD_CODE";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** General error string.   */
  public static final String ERROR_STRING = "#error";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "Erreur : ";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "Attention : ";

  /** String to specify the XSLT module.  */
  public static final String XSL_HEADER = "XSLT ";

  /** String to specify the XML parser module.  */
  public static final String XML_HEADER = "XML ";

  /** I don't think this is used any more.
   * @deprecated  */
  public static final String QUERY_HEADER = "FORME";

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

