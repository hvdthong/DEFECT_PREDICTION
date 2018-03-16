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
public class XMLErrorResources_ja extends XMLErrorResources
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
    ER_FUNCTION_NOT_SUPPORTED, "Function \u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093\u3002"},

  
  /** Can't overwrite cause         */


  {
    ER_CANNOT_OVERWRITE_CAUSE,
			"cause \u3092\u4e0a\u66f8\u304d\u3067\u304d\u307e\u305b\u3093"},

  
   /**  No default implementation found */


  {
    ER_NO_DEFAULT_IMPL,
         "\u30c7\u30d5\u30a9\u30eb\u30c8\u5b9f\u88c5\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093"},

  
   /**  ChunkedIntArray({0}) not currently supported */


  {
    ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
       "ChunkedIntArray({0}) \u306f\u73fe\u5728\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /**  Offset bigger than slot */


  {
    ER_OFFSET_BIGGER_THAN_SLOT,
       "\u30b9\u30ed\u30c3\u30c8\u3088\u308a\u3082\u5927\u304d\u3044\u30aa\u30d5\u30bb\u30c3\u30c8"},

  
   /**  Coroutine not available, id= */


  {
    ER_COROUTINE_NOT_AVAIL,
       "\u30b3\u30eb\u30fc\u30c1\u30f3\u306f\u7121\u52b9\u3067\u3059\u3002id={0}"},

  
   /**  CoroutineManager recieved co_exit() request */


  {
    ER_COROUTINE_CO_EXIT,
       "CoroutineManager \u306f co_exit() \u8981\u6c42\u3092\u53d7\u3051\u53d6\u308a\u307e\u3057\u305f"},

  
   /**  co_joinCoroutineSet() failed */


  {
    ER_COJOINROUTINESET_FAILED,
       "co_joinCoroutineSet() \u306f\u5931\u6557\u3057\u307e\u3057\u305f"},

  
   /**  Coroutine parameter error () */


  {
    ER_COROUTINE_PARAM,
       "\u30b3\u30eb\u30fc\u30c1\u30f3\u30d1\u30e9\u30e1\u30fc\u30bf\u30a8\u30e9\u30fc ({0})"},

  
   /**  UNEXPECTED: Parser doTerminate answers  */


  {
    ER_PARSER_DOTERMINATE_ANSWERS,
       "\nUNEXPECTED: \u30d1\u30fc\u30b5 doTerminate \u306e\u7b54\u3048 {0}"},

  
   /**  parse may not be called while parsing */


  {
    ER_NO_PARSE_CALL_WHILE_PARSING,
       "\u69cb\u6587\u89e3\u6790\u4e2d\u306b parse \u3092\u547c\u3073\u51fa\u3059\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093"},

  
   /**  Error: typed iterator for axis  {0} not implemented  */


  {
    ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "\u30a8\u30e9\u30fc: \u5165\u529b\u3055\u308c\u305f\u8ef8\u306e\u53cd\u5fa9\u5b50 {0} \u306f\u5b9f\u88c5\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /**  Error: iterator for axis {0} not implemented  */


  {
    ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "\u30a8\u30e9\u30fc: \u8ef8\u306e\u53cd\u5fa9\u5b50 {0} \u306f\u5b9f\u88c5\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /**  Iterator clone not supported  */


  {
    ER_ITERATOR_CLONE_NOT_SUPPORTED,
       "\u53cd\u5fa9\u5b50\u30af\u30ed\u30fc\u30f3\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /**  Unknown axis traversal type  */


  {
    ER_UNKNOWN_AXIS_TYPE,
       "\u672a\u77e5\u306e\u8ef8\u30c8\u30e9\u30d0\u30fc\u30b5\u30eb\u30bf\u30a4\u30d7: {0}"},

  
   /**  Axis traverser not supported  */


  {
    ER_AXIS_NOT_SUPPORTED,
       "\u8ef8\u30c8\u30e9\u30d0\u30fc\u30b5\u30eb\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093: {0}"},

  
   /**  No more DTM IDs are available  */


  {
    ER_NO_DTMIDS_AVAIL,
       "\u3053\u308c\u4ee5\u4e0a\u306e DTM ID \u306f\u7121\u52b9\u3067\u3059"},

  
   /**  Not supported  */


  {
    ER_NOT_SUPPORTED,
       "\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093: {0}"},

  
   /**  node must be non-null for getDTMHandleFromNode  */


  {
    ER_NODE_NON_NULL,
       "getDTMHandleFromNode \u306e\u30ce\u30fc\u30c9\u306f null \u4ee5\u5916\u3067\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093"},

  
   /**  Could not resolve the node to a handle  */


  {
    ER_COULD_NOT_RESOLVE_NODE,
       "\u30ce\u30fc\u30c9\u3092\u30cf\u30f3\u30c9\u30eb\u306b\u5909\u3048\u308b\u3053\u3068\u304c\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f"},

  
   /**  startParse may not be called while parsing */


  {
    ER_STARTPARSE_WHILE_PARSING,
       "\u69cb\u6587\u89e3\u6790\u4e2d\u306b startParse \u3092\u547c\u3073\u51fa\u3059\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093"},

  
   /**  startParse needs a non-null SAXParser  */


  {
    ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse \u306f null \u3067\u306a\u3044 SAXParser \u3092\u5fc5\u8981\u3068\u3057\u307e\u3059"},

  
   /**  could not initialize parser with */


  {
    ER_COULD_NOT_INIT_PARSER,
       "\u30d1\u30fc\u30b5\u3092\u521d\u671f\u5316\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f"},

  
   /**  exception creating new instance for pool  */


  {
    ER_EXCEPTION_CREATING_POOL,
       "\u4f8b\u5916\u306b\u3088\u308a\u30d7\u30fc\u30eb\u306b\u65b0\u3057\u3044\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3092\u4f5c\u6210\u3057\u3066\u3044\u307e\u3059"},

  
   /**  Path contains invalid escape sequence  */


  {
    ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "\u30d1\u30b9\u306b\u7121\u52b9\u306a\u30a8\u30b9\u30b1\u30fc\u30d7\u30b7\u30fc\u30b1\u30f3\u30b9\u304c\u542b\u307e\u308c\u3066\u3044\u307e\u3059"},

  
   /**  Scheme is required!  */


  {
    ER_SCHEME_REQUIRED,
       "\u30b9\u30ad\u30fc\u30de\u304c\u5fc5\u8981\u3067\u3059\u3002"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_IN_URI,
       "URI \u306b\u30b9\u30ad\u30fc\u30de\u304c\u3042\u308a\u307e\u305b\u3093: {0}"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_INURI,
       "URI \u306b\u30b9\u30ad\u30fc\u30de\u304c\u3042\u308a\u307e\u305b\u3093"},

  
   /**  Path contains invalid character:   */


  {
    ER_PATH_INVALID_CHAR,
       "\u30d1\u30b9\u306b\u7121\u52b9\u306a\u6587\u5b57\u5217\u304c\u542b\u307e\u308c\u3066\u3044\u307e\u3059: {0}"},

  
   /**  Cannot set scheme from null string  */


  {
    ER_SCHEME_FROM_NULL_STRING,
       "null \u6587\u5b57\u5217\u304b\u3089\u30b9\u30ad\u30fc\u30de\u3092\u8a2d\u5b9a\u3067\u304d\u307e\u305b\u3093"},

  
   /**  The scheme is not conformant. */


  {
    ER_SCHEME_NOT_CONFORMANT,
       "\u30b9\u30ad\u30fc\u30de\u304c\u4e00\u81f4\u3057\u307e\u305b\u3093\u3002"},

  
   /**  Host is not a well formed address  */


  {
    ER_HOST_ADDRESS_NOT_WELLFORMED,
       "\u30db\u30b9\u30c8\u304c\u6b63\u3057\u3044\u5f62\u5f0f\u306e\u30a2\u30c9\u30ec\u30b9\u3067\u306f\u3042\u308a\u307e\u305b\u3093"},

  
   /**  Port cannot be set when host is null  */


  {
    ER_PORT_WHEN_HOST_NULL,
       "\u30db\u30b9\u30c8\u304c null \u306e\u3068\u304d\u3001\u30dd\u30fc\u30c8\u3092\u8a2d\u5b9a\u3067\u304d\u307e\u305b\u3093"},

  
   /**  Invalid port number  */


  {
    ER_INVALID_PORT,
       "\u7121\u52b9\u306a\u30dd\u30fc\u30c8\u756a\u53f7"},

  
   /**  Fragment can only be set for a generic URI  */


  {
    ER_FRAG_FOR_GENERIC_URI,
       "\u6c4e\u7528 URI \u306b\u5bfe\u3057\u3066\u306e\u307f\u30d5\u30e9\u30b0\u30e1\u30f3\u30c8\u3092\u8a2d\u5b9a\u3067\u304d\u307e\u3059"},

  
   /**  Fragment cannot be set when path is null  */


  {
    ER_FRAG_WHEN_PATH_NULL,
       "\u30d1\u30b9\u304c null \u306e\u3068\u304d\u3001\u30d5\u30e9\u30b0\u30e1\u30f3\u30c8\u3092\u8a2d\u5b9a\u3067\u304d\u307e\u305b\u3093"},

  
   /**  Fragment contains invalid character  */


  {
    ER_FRAG_INVALID_CHAR,
       "\u30d5\u30e9\u30b0\u30e1\u30f3\u30c8\u306b\u7121\u52b9\u306a\u6587\u5b57\u5217\u304c\u542b\u307e\u308c\u3066\u3044\u307e\u3059"},

  
 
  
   /** Parser is already in use  */


  {
    ER_PARSER_IN_USE,
        "\u30d1\u30fc\u30b5\u306f\u3059\u3067\u306b\u4f7f\u308f\u308c\u3066\u3044\u307e\u3059"},

  
   /** Parser is already in use  */


  {
    ER_CANNOT_CHANGE_WHILE_PARSING,
        "\u69cb\u6587\u89e3\u6790\u4e2d\u3001{0} {1} \u3092\u5909\u66f4\u3067\u304d\u307e\u305b\u3093"},

  
   /** Self-causation not permitted  */


  {
    ER_SELF_CAUSATION_NOT_PERMITTED,
        "\u81ea\u8eab\u304c\u539f\u56e0\u3068\u306a\u3063\u3066\u306f\u306a\u308a\u307e\u305b\u3093"},

  
   /** Userinfo may not be specified if host is not specified   */


  {
    ER_NO_USERINFO_IF_NO_HOST,
        "\u30db\u30b9\u30c8\u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u306a\u3044\u3068\u304d\u3001Userinfo \u3092\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093"},

  
   /** Port may not be specified if host is not specified   */


  {
    ER_NO_PORT_IF_NO_HOST,
        "\u30db\u30b9\u30c8\u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u306a\u3044\u3068\u304d\u3001Port \u3092\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093"},

  
   /** Query string cannot be specified in path and query string   */


  {
    ER_NO_QUERY_STRING_IN_PATH,
        "\u30d1\u30b9\u304a\u3088\u3073\u7167\u4f1a\u6587\u5b57\u5217\u3067 Query \u6587\u5b57\u5217\u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093"},

  
   /** Fragment cannot be specified in both the path and fragment   */


  {
    ER_NO_FRAGMENT_STRING_IN_PATH,
        "\u30d1\u30b9\u304a\u3088\u3073\u30d5\u30e9\u30b0\u30e1\u30f3\u30c8\u306e\u4e21\u65b9\u3067\u3001Fragment \u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093"},

  
   /** Cannot initialize URI with empty parameters   */


  {
    ER_CANNOT_INIT_URI_EMPTY_PARMS,
        "\u7a7a\u306e\u30d1\u30e9\u30e1\u30fc\u30bf\u3092\u4f7f\u3063\u3066 URI \u3092\u521d\u671f\u5316\u3067\u304d\u307e\u305b\u3093"},
  
  /**  Method not yet supported    */


  {
    ER_METHOD_NOT_SUPPORTED,
        "\u30e1\u30bd\u30c3\u30c9\u306f\u307e\u3060\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},


  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
     "IncrementalSAXSource_Filter \u306f\u73fe\u5728\u518d\u8d77\u52d5\u3067\u304d\u307e\u305b\u3093"},

  
  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_XMLRDR_NOT_BEFORE_STARTPARSE,
     "XMLReader \u306f startParse \u8981\u6c42\u3088\u308a\u524d\u306b\u914d\u7f6e\u3067\u304d\u307e\u305b\u3093"},



  {
    ER_AXIS_TRAVERSER_NOT_SUPPORTED,
     "\u25bc\u8ef8\u30c8\u30e9\u30d0\u30fc\u30b5\u30eb\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093: {0}"},



   {
    ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
     "\u25bcListingErrorHandler \u306e\u4f5c\u6210\u6642\u306b null PrintWriter \u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f!"},



  {
    ER_SYSTEMID_UNKNOWN,
     "\u25bc\u30b7\u30b9\u30c6\u30e0 ID \u304c\u4e0d\u660e\u3067\u3059"},



  {
    ER_LOCATION_UNKNOWN,
     "\u25bc\u30a8\u30e9\u30fc\u306e\u5834\u6240\u304c\u4e0d\u660e\u3067\u3059"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "\u63a5\u982d\u8f9e\u306f\u540d\u524d\u7a7a\u9593\u306b\u5909\u3048\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059: {0}"},


  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  {
    ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "createDocument() \u306f XPathContext \u3067\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "\u5c5e\u6027 child \u306f\u6240\u6709\u8005\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u3092\u4fdd\u6301\u3057\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "\u5c5e\u6027 child \u306f\u6240\u6709\u8005\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u8981\u7d20\u3092\u4fdd\u6301\u3057\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  {
    ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "\u8b66\u544a: \u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u8981\u7d20\u3088\u308a\u524d\u306b\u30c6\u30ad\u30b9\u30c8\u3092\u51fa\u529b\u3067\u304d\u307e\u305b\u3093\u3002\u7121\u8996\u3057\u307e\u3059..."},


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  {
    ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "DOM \u306b\u306f\u8907\u6570\u306e\u30eb\u30fc\u30c8\u3092\u4fdd\u6301\u3067\u304d\u307e\u305b\u3093\u3002"},

  
   /**  Argument 'localName' is null  */


  {
    ER_ARG_LOCALNAME_NULL,
       "\u5f15\u6570 'localName' \u304c null \u3067\u3059"},



  /** localname in QNAME should be a valid NCName */


  {
    ER_ARG_LOCALNAME_INVALID,
       "\u25bcQNAME \u5185\u306e\u30ed\u30fc\u30ab\u30eb\u540d\u306f\u6709\u52b9\u306a NCName \u3067\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093"},

  

  /** prefix in QNAME should be a valid NCName */


  {
    ER_ARG_PREFIX_INVALID,
       "\u25bcQNAME \u5185\u306e\u63a5\u982d\u8f9e\u306f\u6709\u52b9\u306a NCName \u3067\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093"},

  { "BAD_CODE",
      "createMessage \u306e\u30d1\u30e9\u30e1\u30fc\u30bf\u304c\u7bc4\u56f2\u5916\u3067\u3057\u305f"},
  { "FORMAT_FAILED",
      "messageFormat \u547c\u3073\u51fa\u3057\u3067\u4f8b\u5916\u304c\u30b9\u30ed\u30fc\u3055\u308c\u307e\u3057\u305f"},
  { "line", "\u884c\u756a\u53f7"},
  { "column", "\u5217\u756a\u53f7"}
  
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
