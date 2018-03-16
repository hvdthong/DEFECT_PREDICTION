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
public class XPATHErrorResources_fr extends XPATHErrorResources
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
      "La fonction current() n\u2019est pas admise dans la correspondance \u00e0 la forme !"},


  /** Field ER_CURRENT_TAKES_NO_ARGS          */


  {
    ER_CURRENT_TAKES_NO_ARGS,
      "La fonction current() n\u2019admet pas les arguments !"},


  /** Field ER_DOCUMENT_REPLACED          */


  {
    ER_DOCUMENT_REPLACED,
      "La mise en oeuvre de la fonction document () a \u00e9t\u00e9 remplac\u00e9e par org.apache.xalan.xslt.FuncDocument!"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */


  {
    ER_CONTEXT_HAS_NO_OWNERDOC,
      "le contexte n\u2019a pas de document propri\u00e9taire !"},


  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */


  {
    ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() a trop d\u2019arguments."},


  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */


  {
    ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() a trop d\u2019arguments."},


  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */


  {
    ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() a trop d\u2019arguments."},


  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */


  {
    ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() a trop d\u2019arguments."},


  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */


  {
    ER_NAME_HAS_TOO_MANY_ARGS, "name() a trop d\u2019arguments."},


  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */


  {
    ER_STRING_HAS_TOO_MANY_ARGS,
      "string() a trop d\u2019arguments."},


  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */


  {
    ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length() a trop d\u2019arguments."},


  /** Field ER_TRANSLATE_TAKES_3_ARGS          */


  {
    ER_TRANSLATE_TAKES_3_ARGS,
      "La fonction translate() a trois arguments !"},


  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */


  {
    ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "La fonction unparsed-entity-uri ne peut avoir qu\u2019un seul argument !"},


  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */


  {
    ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "l'axe namespace n\u2019est pas encore mis en \u0153uvre !"},


  /** Field ER_UNKNOWN_AXIS          */


  {
    ER_UNKNOWN_AXIS, "axe inconnu : {0}"},


  /** Field ER_UNKNOWN_MATCH_OPERATION          */


  {
    ER_UNKNOWN_MATCH_OPERATION, "op\u00e9ration de correspondance inconnue !"},


  /** Field ER_INCORRECT_ARG_LENGTH          */


  {
    ER_INCORRECT_ARG_LENGTH,
      "La longueur d\u2019argument du test du n\u0153ud processing-instruction() n\u2019est pas correcte !"},


  /** Field ER_CANT_CONVERT_TO_NUMBER          */


  {
    ER_CANT_CONVERT_TO_NUMBER,
      "Impossible de convertir {0} en fonction number"},


  /** Field ER_CANT_CONVERT_TO_NODELIST          */


  {
    ER_CANT_CONVERT_TO_NODELIST,
      "Impossible de convertir {0} en fonction NodeList !"},


  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */


  {
    ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "Impossible de convertir {0} en fonction NodeSetDTM !"},


  /** Field ER_CANT_CONVERT_TO_TYPE          */


  {
    ER_CANT_CONVERT_TO_TYPE,


  /** Field ER_EXPECTED_MATCH_PATTERN          */


  {
    ER_EXPECTED_MATCH_PATTERN,
      "Correspondance \u00e0 la forme attendue dans getMatchScore !"},


  /** Field ER_COULDNOT_GET_VAR_NAMED          */


  {
    ER_COULDNOT_GET_VAR_NAMED,
      "Impossible de trouver la variable nomm\u00e9e {0}"},


  /** Field ER_UNKNOWN_OPCODE          */


  {
    ER_UNKNOWN_OPCODE, "ERREUR ! Code d\u2019op\u00e9ration inconnu : {0}"},


  /** Field ER_EXTRA_ILLEGAL_TOKENS          */


  {
    ER_EXTRA_ILLEGAL_TOKENS, "Unit\u00e9s lexicales suppl\u00e9mentaires non autoris\u00e9es : {0}"},


  /** Field ER_EXPECTED_DOUBLE_QUOTE          */


  {
    ER_EXPECTED_DOUBLE_QUOTE,
      "libell\u00e9 mal pr\u00e9sent\u00e9... guillemet attendu !"},


  /** Field ER_EXPECTED_SINGLE_QUOTE          */


  {
    ER_EXPECTED_SINGLE_QUOTE,
      "libell\u00e9 mal pr\u00e9sent\u00e9... apostrophe attendue !"},


  /** Field ER_EMPTY_EXPRESSION          */


  {
    ER_EMPTY_EXPRESSION, "Expression vide !"},


  /** Field ER_EXPECTED_BUT_FOUND          */


  {
    ER_EXPECTED_BUT_FOUND, "{0} attendu(e), mais : {1} trouv\u00e9(e)"},


  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */


  {
    ER_INCORRECT_PROGRAMMER_ASSERTION,
      "Assertion de programmeur incorrecte ! - {0}"},


  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */


  {
    ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "L\u2019argument boolean(...) n\u2019est plus facultatif avec la version brouillon 19990709 XPath ."},


  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "',' trouv\u00e9 sans argument avant !"},


  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "',' trouv\u00e9 sans argument apr\u00e8s !"},


  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */


  {
    ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[predicate]' ou '.[predicate]' constitue une mauvaise syntaxe. Utilisez 'self::node()[predicate]' \u00e0 la place."},


  /** Field ER_ILLEGAL_AXIS_NAME          */


  {
    ER_ILLEGAL_AXIS_NAME, "nom d\u2019axe non autoris\u00e9 : {0}"},


  /** Field ER_UNKNOWN_NODETYPE          */


  {
    ER_UNKNOWN_NODETYPE, "Type de n\u0153ud inconnu : {0}"},


  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */


  {
    ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "Le libell\u00e9 de la forme ({0}) doit \u00eatre entre guillemets !"},


  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */


  {
    ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0} ne peut pas \u00eatre format\u00e9(e) en number\u00a0!"},


  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */


  {
    ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "Impossible de cr\u00e9er XML TransformerFactory Liaison : {0}"},


  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */


  {
    ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "Erreur ! Impossible de trouver l\u2019expression de s\u00e9lection xpath (-select)."},


  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */


  {
    ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "ERREUR ! Impossible de trouver ENDOP apr\u00e8s OP_LOCATIONPATH"},


  /** Field ER_ERROR_OCCURED          */


  {
    ER_ERROR_OCCURED, "Une erreur s\u2019est produite !"},


  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */


  {
    ER_ILLEGAL_VARIABLE_REFERENCE,
      "L''\u00e9l\u00e9ment VariableReference a \u00e9t\u00e9 fourni pour la variable hors contexte ou sans d\u00e9finition !  Nom = {0}"},


  /** Field ER_AXES_NOT_ALLOWED          */


  {
    ER_AXES_NOT_ALLOWED,
      "Seuls les axes child:: et attribute:: sont admis dans les correspondances \u00e0 la forme !  Axes erron\u00e9s = {0}"},


  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */


  {
    ER_KEY_HAS_TOO_MANY_ARGS,
      "key() dispose d\u2019un nombre incorrect d\u2019arguments."},


  /** Field ER_COUNT_TAKES_1_ARG          */


  {
    ER_COUNT_TAKES_1_ARG,
      "La fonction count ne doit avoir qu\u2019un seul argument !"},


  /** Field ER_COULDNOT_FIND_FUNCTION          */


  {
    ER_COULDNOT_FIND_FUNCTION, "Impossible de trouver la fonction : {0}"},


  /** Field ER_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "Codage non pris en charge : {0}"},


  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */


  {
    ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "Un incident s\u2019est produit au niveau de DTM dans getNextSibling... Tentative de r\u00e9cup\u00e9ration"},


  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */


  {
    ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "Erreur de programmation : Impossible d\u2019\u00e9crire dans EmptyNodeList."},


  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */


  {
    ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory n\u2019est pas pris en charge par XPathContext!"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "Le pr\u00e9fixe doit se r\u00e9soudre en nom d''espace : {0}"},


  /** Field ER_PARSE_NOT_SUPPORTED          */


  {
    ER_PARSE_NOT_SUPPORTED,
      "parse (source InputSource) non pris en charge dans XpathContext ! Impossible d\u2019ouvrir {0}"},


  /** Field ER_SAX_API_NOT_HANDLED          */


  {
    ER_SAX_API_NOT_HANDLED,
      "Les caract\u00e8res SAX API (char ch[]... ne sont pas pris en charge par DTM !"},


  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */


  {
    ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... ne sont pas pris en charge par DTM !"},


  /** Field ER_DTM_CANNOT_HANDLE_NODES          */


  {
    ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison ne peut pas prendre en charge les n\u0153uds de type {0}"},


  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */


  {
    ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper ne peut pas prendre en charge les n\u0153uds de type {0}"},


  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */


  {
    ER_XERCES_PARSE_ERROR_DETAILS,
      "Erreur DOM2Helper.parse : ID syst\u00e8me - {0} ligne - {1}"},


  /** Field ER_XERCES_PARSE_ERROR          */


  {
    ER_XERCES_PARSE_ERROR, "Erreur DOM2Helper.parse"},


  /** Field ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "Substitut UTF-16 incorrect d\u00e9tect\u00e9 : {0} ?"},


  /** Field ER_OIERROR          */


  {
    ER_OIERROR, "Erreur d\u2019E/S"},


  /** Field ER_CANNOT_CREATE_URL          */


  {
    ER_CANNOT_CREATE_URL, "Impossible de cr\u00e9er une url pour : {0}"},


  /** Field ER_XPATH_READOBJECT          */


  {
    ER_XPATH_READOBJECT, "Dans XPath.readObject : {0}"},

  
  /** Field ER_XPATH_READOBJECT         */


  {
    ER_FUNCTION_TOKEN_NOT_FOUND,
      "Unit\u00e9 lexicale function introuvable."},

  
   /**  Can not deal with XPath type:   */


  {
    ER_CANNOT_DEAL_XPATH_TYPE,
       "Impossible d\u2019op\u00e9rer avec le type : {0} XPath"},

  
   /**  This NodeSet is not mutable  */

	
  {
    ER_NODESET_NOT_MUTABLE,
       "Cet \u00e9l\u00e9ment NodeSet n\u2019est pas mutable"},

  
   /**  This NodeSetDTM is not mutable  */


  {
    ER_NODESETDTM_NOT_MUTABLE,
       "Cet \u00e9l\u00e9ment NodeSetDTM n\u2019est pas mutable"},

  
   /**  Variable not resolvable:   */


  {
    ER_VAR_NOT_RESOLVABLE,
        "Variable non r\u00e9solue : {0}"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "Gestionnaire d\u2019erreur vide"},

  
   /**  Programmer's assertion: unknown opcode  */


  {
    ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "Assertion du programmeur : code op\u00e9ration inconnu : {0}"},

  
   /**  0 or 1   */


  {
    ER_ZERO_OR_ONE,
       "0 ou 1"},
    
  
  
   /**  rtf() not supported by XRTreeFragSelectWrapper   */


  {
    ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() n\u2019est pas pris en charge par XRTreeFragSelectWrapper"},

  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */


  {
    ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() n\u2019est pas pris en charge par XRTreeFragSelectWrapper"},

  
   /**  fsb() not supported for XStringForChars   */


  {
    ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb() n\u2019est pas pris en charge pour XStringForChars"},

  
   /**  Could not find variable with the name of   */


  {
    ER_COULD_NOT_FIND_VAR,
      "Impossible de trouver la variable nomm\u00e9e {0}"},

  
   /**  XStringForChars can not take a string for an argument   */


  {
    ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars ne peut pas avoir de cha\u00eene comme argument"},

  
   /**  The FastStringBuffer argument can not be null   */


  {
    ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "L\u2019argument FastStringBuffer ne peut pas \u00eatre \u00e9gal \u00e0 null"},
    
/* MANTIS_XALAN CHANGE: BEGIN */ 
   /**  2 or 3   */


  {
    ER_TWO_OR_THREE,
       "2 ou 3"},


 /** Variable accessed before it is bound! */


  {
    ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "Variable acc\u00e9d\u00e9e avant sa limite !"},


   /** XStringForFSB can not take a string for an argument! */


  {
    ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB ne peut pas \u00eatre une cha\u00eene pour un argument!"},


   /** Error! Setting the root of a walker to null! */


  {
    ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! Erreur! Attribution d'une valeur null \u00e0 la racine d'un walker !!!"},


   /** This NodeSetDTM can not iterate to a previous node! */


  {
    ER_NODESETDTM_CANNOT_ITERATE,
       "Ce NodeSetDTM ne peut pas \u00eatre r\u00e9p\u00e9t\u00e9 \u00e0 un noeud pr\u00e9c\u00e9dent !"},


  /** This NodeSet can not iterate to a previous node! */


  {
    ER_NODESET_CANNOT_ITERATE,
       "Ce NodeSet ne peut pas \u00eatre r\u00e9p\u00e9t\u00e9 vers un noeud pr\u00e9c\u00e9dent !"},


  /** This NodeSetDTM can not do indexing or counting functions! */


  {
    ER_NODESETDTM_CANNOT_INDEX,
       "Ce NodeSetDTM ne peut pas ex\u00e9cuter de fonctions d'indexation ou de comptage !"},


  /** This NodeSet can not do indexing or counting functions! */


  {
    ER_NODESET_CANNOT_INDEX,
       "Ce NodeSet ne peut pas ex\u00e9cuter de fonctions d'indexation ou de comptage !"},


  /** Can not call setShouldCacheNodes after nextNode has been called! */


  {
    ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "Impossible d'appeler setShouldCacheNodes apr\u00e8s avoir appel\u00e9 nextNode !"},


  /** {0} only allows {1} arguments */


  {
    ER_ONLY_ALLOWS,
       "{0} n''admet que les arguments {1} arguments"},


  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */


  {
    ER_UNKNOWN_STEP,
       "Assertion du programmeur dans getNextStepPos : stepType inconnu : {0}"},



  /** Problem with RelativeLocationPath */


  {
    ER_EXPECTED_REL_LOC_PATH,



  /** Problem with LocationPath */


  {
    ER_EXPECTED_LOC_PATH,
       "Un chemin d'acc\u00e8s \u00e9tait attendu, mais l'objet suivant a \u00e9t\u00e9 rencontr\u00e9\u003a  {0}"},



  /** Problem with Step */


  {
    ER_EXPECTED_LOC_STEP,



  /** Problem with NodeTest */


  {
    ER_EXPECTED_NODE_TEST,
       "Un test de noeud correspondant \u00e0 NCName:* ou \u00e0 QName \u00e9tait attendu."},



  /** Expected step pattern */


  {
    ER_EXPECTED_STEP_PATTERN,
       "Une forme d'\u00e9tape \u00e9tait attendue, mais '/' a \u00e9t\u00e9 rencontr\u00e9."},


 
/** Expected relative path pattern */


  {
    ER_EXPECTED_REL_PATH_PATTERN,
       "Une forme de chemin d'acc\u00e8s relatif \u00e9tait attendue."},



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */


  {
    ER_CANT_CONVERT_TO_BOOLEAN,
       "Impossible de convertir {0} en valeur bool\u00e9enne."},



  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */


  {
    ER_CANT_CONVERT_TO_SINGLENODE,
       "Impossible de convertir {0} en noeud unique. Cette m\u00e9thode d'obtention s'applique aux  types ANY_UNORDERED_NODE_TYPE et FIRST_ORDERED_NODE_TYPE."},



  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */


  {
    ER_CANT_GET_SNAPSHOT_LENGTH,
       "Impossible d'obtenir une longueur d'instantan\u00e9 du type : {0}. Cette m\u00e9thode d'obtention s'applique aux types UNORDERED_NODE_SNAPSHOT_TYPE et ORDERED_NODE_SNAPSHOT_TYPE."},

  
  /** Field ER_NON_ITERATOR_TYPE                */


  {
    ER_NON_ITERATOR_TYPE,
       "Impossible d'it\u00e9rer sur le type non r\u00e9p\u00e9titeur : {0}"},



  /** Field ER_DOC_MUTATED                      */


  {
    ER_DOC_MUTATED,
       "Le document a mut\u00e9 depuis que le r\u00e9sultat a \u00e9t\u00e9 renvoy\u00e9. L'it\u00e9rateur est incorrect."},


/** Field ER_INVALID_XPATH_TYPE               */


  {
    ER_INVALID_XPATH_TYPE,
       "Argument de type XPath incorrect : {0}"},


/** Field ER_EMPTY_XPATH_RESULT                */


  {
    ER_EMPTY_XPATH_RESULT,
       "Objet de r\u00e9sultat XPath vide"},


  /** Field ER_INCOMPATIBLE_TYPES                */


  {
    ER_INCOMPATIBLE_TYPES,
       "Impossible de forcer le type renvoy\u00e9 : {0} dans le fichier : {1}"},


/** Field ER_NULL_RESOLVER                     */


  {
    ER_NULL_RESOLVER,
       "Impossible de r\u00e9soudre le pr\u00e9fixe sans d\u00e9composeur de pr\u00e9fixe."},



  /** Field ER_CANT_CONVERT_TO_STRING            */


  {
    ER_CANT_CONVERT_TO_STRING,
       "Impossible de convertir {0} en cha\u00eene ."},



  /** Field ER_NON_SNAPSHOT_TYPE                 */


  {
    ER_NON_SNAPSHOT_TYPE,
       "Impossible d'appeler snapshotItem sur le type : {0}. Cette m\u00e9thode s'applique aux types UNORDERED_NODE_SNAPSHOT_TYPE et ORDERED_NODE_SNAPSHOT_TYPE."},



/** Field ER_WRONG_DOCUMENT                    */


  {
    ER_WRONG_DOCUMENT,
       "Le noeud de contexte n'appartient pas au document associ\u00e9 \u00e0 ce XPathEvaluator."},


  /** Field ER_WRONG_NODETYPE                    */


  {
    ER_WRONG_NODETYPE ,
       "Le noeud de contexte n'est pas pris en charge."},


/** Field ER_XPATH_ERROR                       */


  {
    ER_XPATH_ERROR ,
       "Erreur inconnue dans XPath."},




  /** Field WG_LOCALE_NAME_NOT_HANDLED          */


  {
    WG_LOCALE_NAME_NOT_HANDLED,
      "Le nom de l'environnement local dans la fonction format-number n\u2019est pas encore g\u00e9r\u00e9 !"},


  /** Field WG_PROPERTY_NOT_SUPPORTED          */


  {
    WG_PROPERTY_NOT_SUPPORTED,
      "XSL Property non pris en charge : {0}"},


  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */


  {
    WG_DONT_DO_ANYTHING_WITH_NS,
      "Ne rien faire pour l\u2019instant avec le nom d''espace {0} dans la propri\u00e9t\u00e9 : {1}"},


  /** Field WG_SECURITY_EXCEPTION          */


  {
    WG_SECURITY_EXCEPTION,
      "Interception de SecurityException ors de la tentative d\u2019acc\u00e8s \u00e0 la propri\u00e9t\u00e9 syst\u00e8me XSL : {0}"},


  /** Field WG_QUO_NO_LONGER_DEFINED          */


  {
    WG_QUO_NO_LONGER_DEFINED,
      "L\u2019ancienne syntaxe: quo(...) n\u2019est plus d\u00e9finie dans XPath."},


  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */


  {
    WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath requiert un objet d\u00e9riv\u00e9 pour mettre en oeuvre nodeTest !"},


  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */


  {
    WG_FUNCTION_TOKEN_NOT_FOUND,
      "Unit\u00e9 lexicale function introuvable."},


  /** Field WG_COULDNOT_FIND_FUNCTION          */


  {
    WG_COULDNOT_FIND_FUNCTION,
      "Impossible de trouver la fonction : {0}"},


  /** Field WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "Impossible de cr\u00e9er une URL \u00e0 partir de : {0}"},


  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */


  {
    WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "L\u2019option -E n\u2019est pas pris en charge pour l\u2019analyseur syntaxique DTM"},


  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */


  {
    WG_ILLEGAL_VARIABLE_REFERENCE,
      "L''\u00e9l\u00e9ment VariableReference a \u00e9t\u00e9 fourni pour la variable hors contexte ou sans d\u00e9finition !  Nom = {0}"},


  /** Field WG_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "Codage non pris en charge : {0}"},



  { "ui_language", "fr"},
  { "help_language", "fr"},
  { "language", "fr"},
    { "BAD_CODE",
      "Param\u00e8tre pour createMessage hors limites"},
    { "FORMAT_FAILED",
      "Exception \u00e9mise pendant l\u2019appel de messageFormat"},
    { "version", ">>>>>>> Version Xalan "},
    { "version2", "<<<<<<<"},
    { "yes", "oui"},
    { "xsldone", "XSLProcessor: termin\u00e9"},
    { "xpath_option", "options xpath : "},
    { "optionIN", "   [-in inputXMLURL]"},
    { "optionSelect", "   [expression xpath -select]"},
    { "optionMatch",
      "   [correspondance \u00e0 la forme -match (pour les diagnostics de correspondance)]"},
    { "optionAnyExpr",
      "Ou une expression xpath provoquera un \u00e9chec de diagnostic"},
    { "noParsermsg1", "Echec de XSL Process."},
    { "noParsermsg2", "** Impossible de trouver l\u2019analyseur syntaxique**"},
    { "noParsermsg3", "Veuillez v\u00e9rifier votre chemin de classe."},
    { "noParsermsg4",
      "Si vous ne disposez pas de l\u2019analyseur XML d\u2019IBM pour Java, vous pouvez le t\u00e9l\u00e9charger \u00e0 l\u2019adresse suivante"},
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
  public static final String ERROR_HEADER = "Erreur : ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Attention : ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "FORME ";

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


