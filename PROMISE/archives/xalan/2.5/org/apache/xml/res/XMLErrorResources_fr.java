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
public class XMLErrorResources_fr extends XMLErrorResources
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
    ER_FUNCTION_NOT_SUPPORTED, "Fonction non prise en charge !"},
  
  /** Can't overwrite cause         */

  {
    ER_CANNOT_OVERWRITE_CAUSE,
			"Impossible d\u2019\u00e9craser la cause"},
  
   /**  No default implementation found */

  {
    ER_NO_DEFAULT_IMPL,
         "Aucune mise en \u0153uvre par d\u00e9faut trouv\u00e9e"},
  
   /**  ChunkedIntArray({0}) not currently supported */

  {
    ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
       "ChunkedIntArray({0}) non pris en charge pour le moment"},
  
   /**  Offset bigger than slot */

  {
    ER_OFFSET_BIGGER_THAN_SLOT,
       "Impression plus importante que l\u2019emplacement"},
  
   /**  Coroutine not available, id= */

  {
    ER_COROUTINE_NOT_AVAIL,
       "Coroutine indisponible, id={0}"},
  
   /**  CoroutineManager recieved co_exit() request */

  {
    ER_COROUTINE_CO_EXIT,
       "CoroutineManager a re\u00e7u une requ\u00eate co_exit()"},
  
   /**  co_joinCoroutineSet() failed */

  {
    ER_COJOINROUTINESET_FAILED,
       "Echec de co_joinCoroutineSet()"},
  
   /**  Coroutine parameter error () */

  {
    ER_COROUTINE_PARAM,
       "Erreur de param\u00e8tre Coroutine ({0})"},
  
   /**  UNEXPECTED: Parser doTerminate answers  */

  {
    ER_PARSER_DOTERMINATE_ANSWERS,
       "\nUNEXPECTED: R\u00e9ponses de Parser doTerminate {0}"},
  
   /**  parse may not be called while parsing */

  {
    ER_NO_PARSE_CALL_WHILE_PARSING,
       "parse ne peut pas \u00eatre appel\u00e9 pendant l\u2019op\u00e9ration d\u2019analyse"},
  
   /**  Error: typed iterator for axis  {0} not implemented  */

  {
    ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "Erreur : l\u2019it\u00e9rateur saisi pour l\u2019axe {0} n\u2019est pas mis en oeuvre"},
  
   /**  Error: iterator for axis {0} not implemented  */

  {
    ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "Erreur : l\u2019it\u00e9rateur pour l\u2019axe {0} n\u2019est pas mis en oeuvre "},
  
   /**  Iterator clone not supported  */

  {
    ER_ITERATOR_CLONE_NOT_SUPPORTED,
       "Clone d\u2019it\u00e9rateur non pris en charge"},
  
   /**  Unknown axis traversal type  */

  {
    ER_UNKNOWN_AXIS_TYPE,
       "Type d\u2019axe transversal inconnu : {0}"},
  
   /**  Axis traverser not supported  */

  {
    ER_AXIS_NOT_SUPPORTED,
       "Axe transversal non pris en charge : {0}"},
  
   /**  No more DTM IDs are available  */

  {
    ER_NO_DTMIDS_AVAIL,
       "Aucun ID DTM disponible"},
  
   /**  Not supported  */

  {
    ER_NOT_SUPPORTED,
       "Non pris en charge : {0}"},
  
   /**  node must be non-null for getDTMHandleFromNode  */

  {
    ER_NODE_NON_NULL,
       "Le n\u0153ud ne doit pas \u00eatre vide pour getDTMHandleFromNode"},
  
   /**  Could not resolve the node to a handle  */

  {
    ER_COULD_NOT_RESOLVE_NODE,
       "Impossible de r\u00e9soudre le noeud en descripteur"},
  
   /**  startParse may not be called while parsing */

  {
    ER_STARTPARSE_WHILE_PARSING,
       "startParse ne peut pas \u00eatre appel\u00e9 pendant l\u2019analyse"},
  
   /**  startParse needs a non-null SAXParser  */

  {
    ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse requiert un SAXParser non vide"},
  
   /**  could not initialize parser with */

  {
    ER_COULD_NOT_INIT_PARSER,
       "Impossible d\u2019initialiser l\u2019analyseur avec"},
  
   /**  exception creating new instance for pool  */

  {
    ER_EXCEPTION_CREATING_POOL,
       "Exception\u00a0de cr\u00e9ation d\u2019une nouvelle instance pour le pool"},
  
   /**  Path contains invalid escape sequence  */

  {
    ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "Le chemin contient une s\u00e9quence de remplacement incorrecte"},
  
   /**  Scheme is required!  */

  {
    ER_SCHEME_REQUIRED,
       "Le mod\u00e8le est requis !"},
  
   /**  No scheme found in URI  */

  {
    ER_NO_SCHEME_IN_URI,
       "Aucun mod\u00e8le trouv\u00e9 dans l\u2019URI : {0}"},
  
   /**  No scheme found in URI  */

  {
    ER_NO_SCHEME_INURI,
       "Aucun mod\u00e8le trouv\u00e9 dans l\u2019URI"},
  
   /**  Path contains invalid character:   */

  {
    ER_PATH_INVALID_CHAR,
       "Le chemin contient des caract\u00e8res incorrects : {0}"},
  
   /**  Cannot set scheme from null string  */

  {
    ER_SCHEME_FROM_NULL_STRING,
       "Impossible de d\u00e9finir le mod\u00e8le \u00e0 partir d\u2019une cha\u00eene vide"},
  
   /**  The scheme is not conformant. */

  {
    ER_SCHEME_NOT_CONFORMANT,
       "Le mod\u00e8le n\u2019est pas conforme."},
  
   /**  Host is not a well formed address  */

  {
    ER_HOST_ADDRESS_NOT_WELLFORMED,
       "L\u2019h\u00f4te n\u2019est pas constitu\u00e9 d\u2019une adresse bien form\u00e9e"},
  
   /**  Port cannot be set when host is null  */

  {
    ER_PORT_WHEN_HOST_NULL,
       "Le port ne peut pas \u00eatre d\u00e9fini lorsque l\u2019h\u00f4te est vide"},
  
   /**  Invalid port number  */

  {
    ER_INVALID_PORT,
       "Num\u00e9ro de port incorrect"},
  
   /**  Fragment can only be set for a generic URI  */

  {
    ER_FRAG_FOR_GENERIC_URI,
       "Le fragment ne peut \u00eatre d\u00e9fini que pour un URI g\u00e9n\u00e9rique"},
  
   /**  Fragment cannot be set when path is null  */

  {
    ER_FRAG_WHEN_PATH_NULL,
       "Le fragment ne peut pas \u00eatre d\u00e9fini lorsque le chemin est vide"},
  
   /**  Fragment contains invalid character  */

  {
    ER_FRAG_INVALID_CHAR,
       "Le fragment contient des caract\u00e8res incorrects"},
  
 
  
   /** Parser is already in use  */

  {
    ER_PARSER_IN_USE,
        "L\u2019analyseur est d\u00e9j\u00e0 en cours d\u2019utilisation"},
  
   /** Parser is already in use  */

  {
    ER_CANNOT_CHANGE_WHILE_PARSING,
        "Impossible de modifier {0} {1} pendant la phase d\u2019analyse"},
  
   /** Self-causation not permitted  */

  {
    ER_SELF_CAUSATION_NOT_PERMITTED,
        "Lien de causalit\u00e9 vers soi impossible"},
  
   /** Userinfo may not be specified if host is not specified   */

  {
    ER_NO_USERINFO_IF_NO_HOST,
        "Les informations sur l'utilisateur ne peuvent pas \u00eatre sp\u00e9cifi\u00e9es si l\u2019h\u00f4te n\u2019est pas sp\u00e9cifi\u00e9"},
  
   /** Port may not be specified if host is not specified   */

  {
    ER_NO_PORT_IF_NO_HOST,
        "Le port ne peut pas \u00eatre sp\u00e9cifi\u00e9 si l\u2019h\u00f4te n\u2019est pas sp\u00e9cifi\u00e9"},
  
   /** Query string cannot be specified in path and query string   */

  {
    ER_NO_QUERY_STRING_IN_PATH,
        "La cha\u00eene de requ\u00eate ne peut pas \u00eatre sp\u00e9cifi\u00e9e dans le chemin et dans la cha\u00eene de requ\u00eate"},
  
   /** Fragment cannot be specified in both the path and fragment   */

  {
    ER_NO_FRAGMENT_STRING_IN_PATH,
        "Le fragment ne peut pas \u00eatre sp\u00e9cifi\u00e9 dans le chemin et dans le fragment"},
  
   /** Cannot initialize URI with empty parameters   */

  {
    ER_CANNOT_INIT_URI_EMPTY_PARMS,
        "Impossible d\u2019initialiser l\u2019URI avec des param\u00e8tres vides"},
  
  /**  Method not yet supported    */

  {
    ER_METHOD_NOT_SUPPORTED,
        "M\u00e9thode non prise en charge pour le moment"},

  /** IncrementalSAXSource_Filter not currently restartable   */

  {
    ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
     "IncrementalSAXSource_Filter ne peut pas \u00eatre relanc\u00e9 pour le moment"},
  
  /** IncrementalSAXSource_Filter not currently restartable   */

  {
    ER_XMLRDR_NOT_BEFORE_STARTPARSE,
     "XMLReader pas avant la requ\u00eate startParse"},

  {
    ER_AXIS_TRAVERSER_NOT_SUPPORTED,
     "La coupure d'axe n'est pas prise en charge : {0}"},

  {
    ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
     "ListingErrorHandler a \u00e9t\u00e9 cr\u00e9\u00e9 avec PrintWriter nul !"},

  {
    ER_SYSTEMID_UNKNOWN,
     "SystemId inconnu"},

  {
    ER_LOCATION_UNKNOWN,
     "Emplacement de l'erreur inconnu"},

  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "Le pr\u00e9fixe doit se r\u00e9soudre en nom d''espace : {0}"},

  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  {
    ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "createDocument() non pris en charge dans XpathContext !"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "L\u2019attribut child n\u2019a pas de document propri\u00e9taire !"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "L\u2019attribute child n\u2019a pas d\u2019\u00e9l\u00e9ment de document propri\u00e9taire !"},

  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  {
    ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "Attention : impossible de sortir le texte avant l\u2019\u00e9l\u00e9ment document !  Op\u00e9ration ignor\u00e9e ..."},


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  {
    ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "Impossible d\u2019avoir plus d\u2019une racine sur un DOM !"},

  
   /**  Argument 'localName' is null  */


  {
    ER_ARG_LOCALNAME_NULL,
       "L\u2019argument 'localName' est \u00e9gal \u00e0 null"},


  /** localname in QNAME should be a valid NCName */


  {
    ER_ARG_LOCALNAME_INVALID,
       "Le nom local de QNAME doit \u00eatre un NCName admis"},

  

  /** prefix in QNAME should be a valid NCName */


  {
    ER_ARG_PREFIX_INVALID,
       "Le pr\u00e9fixe de QNAME doit \u00eatre un NCName admis"},

  { "BAD_CODE",
      "Param\u00e8tre pour createMessage hors limites"},
  { "FORMAT_FAILED",
      "Exception \u00e9mise pendant l\u2019appel de messageFormat "},
  { "line", "N\u00b0 de ligne"},
  { "column", "N\u00b0 de colonne"}
  
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
