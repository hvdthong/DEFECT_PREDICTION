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
public class XMLErrorResources_zh_TW extends XMLErrorResources
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
    ER_FUNCTION_NOT_SUPPORTED, "\u4e0d\u652f\u63f4\u51fd\u5f0f\uff01"},

  
  /** Can't overwrite cause         */


  {
    ER_CANNOT_OVERWRITE_CAUSE,
			"\u7121\u6cd5\u6539\u5beb\u539f\u56e0"},

  
   /**  No default implementation found */


  {
    ER_NO_DEFAULT_IMPL,
         "\u627e\u4e0d\u5230\u9810\u8a2d\u5efa\u7f6e"},

  
   /**  ChunkedIntArray({0}) not currently supported */


  {
    ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
       "\u76ee\u524d\u4e0d\u652f\u63f4 ChunkedIntArray({0})"},

  
   /**  Offset bigger than slot */


  {
    ER_OFFSET_BIGGER_THAN_SLOT,
       "\u504f\u79fb\u5927\u65bc\u4ecb\u9762\u69fd"},

  
   /**  Coroutine not available, id= */


  {
    ER_COROUTINE_NOT_AVAIL,
       "\u6c92\u6709 Coroutine \u53ef\u7528\uff0cid={0}"},

  
   /**  CoroutineManager recieved co_exit() request */


  {
    ER_COROUTINE_CO_EXIT,
       "CoroutineManager \u6536\u5230 co_exit() \u8981\u6c42"},

  
   /**  co_joinCoroutineSet() failed */


  {
    ER_COJOINROUTINESET_FAILED,
       "co_joinCoroutineSet() \u5931\u6548"},

  
   /**  Coroutine parameter error () */


  {
    ER_COROUTINE_PARAM,
       "Coroutine \u53c3\u6578\u932f\u8aa4 ({0})"},

  
   /**  UNEXPECTED: Parser doTerminate answers  */


  {
    ER_PARSER_DOTERMINATE_ANSWERS,
       "\nUNEXPECTED: \u5256\u6790\u5668 doTerminate \u56de\u7b54 {0}"},

  
   /**  parse may not be called while parsing */


  {
    ER_NO_PARSE_CALL_WHILE_PARSING,
       "\u5728\u9032\u884c\u5256\u6790\u6642\u672a\u80fd\u547c\u53eb\u5256\u6790"},

  
   /**  Error: typed iterator for axis  {0} not implemented  */


  {
    ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "\u932f\u8aa4\uff1a\u5c0d\u8ef8 {0} \u8f38\u5165\u7684\u91cd\u8986\u5668\u6c92\u6709\u57f7\u884c"},

  
   /**  Error: iterator for axis {0} not implemented  */


  {
    ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "\u932f\u8aa4\uff1a\u8ef8 {0} \u7684\u91cd\u8986\u5668\u6c92\u6709\u57f7\u884c "},

  
   /**  Iterator clone not supported  */


  {
    ER_ITERATOR_CLONE_NOT_SUPPORTED,
       "\u4e0d\u652f\u63f4\u91cd\u8986\u5668\u8907\u88fd"},

  
   /**  Unknown axis traversal type  */


  {
    ER_UNKNOWN_AXIS_TYPE,
       "\u672a\u77e5\u8ef8\u904d\u6b77\u985e\u578b\uff1a{0}"},

  
   /**  Axis traverser not supported  */


  {
    ER_AXIS_NOT_SUPPORTED,
       "\u4e0d\u652f\u63f4\u8ef8\u904d\u8a2a\u5668\uff1a{0}"},

  
   /**  No more DTM IDs are available  */


  {
    ER_NO_DTMIDS_AVAIL,
       "\u6c92\u6709\u53ef\u7528\u7684 DTM ID"},

  
   /**  Not supported  */


  {
    ER_NOT_SUPPORTED,
       "\u4e0d\u652f\u63f4\uff1a{0}"},

  
   /**  node must be non-null for getDTMHandleFromNode  */


  {
    ER_NODE_NON_NULL,
       "\u5c0d getDTMHandleFromNode \u800c\u8a00\uff0c\u7bc0\u9ede\u5fc5\u9808\u70ba\u975e\u7a7a\u503c"},

  
   /**  Could not resolve the node to a handle  */


  {
    ER_COULD_NOT_RESOLVE_NODE,
       "\u7121\u6cd5\u89e3\u8b6f\u7bc0\u9ede\u70ba\u63a7\u9ede"},

  
   /**  startParse may not be called while parsing */


  {
    ER_STARTPARSE_WHILE_PARSING,
       "\u5728\u9032\u884c\u5256\u6790\u6642\u672a\u547c\u53eb startParse"},

  
   /**  startParse needs a non-null SAXParser  */


  {
    ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse \u9700\u8981\u975e\u7a7a\u503c\u7684 SAXParser"},

  
   /**  could not initialize parser with */


  {
    ER_COULD_NOT_INIT_PARSER,
       "\u7121\u6cd5\u8d77\u59cb\u8a2d\u5b9a\u5256\u6790\u5668\uff0c\u4ee5"},

  
   /**  exception creating new instance for pool  */


  {
    ER_EXCEPTION_CREATING_POOL,
       "\u5efa\u7acb\u5132\u5b58\u6c60\u7684\u65b0\u6848\u4f8b\u6642\u767c\u751f\u7570\u5e38"},

  
   /**  Path contains invalid escape sequence  */


  {
    ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "\u8def\u5f91\u5305\u542b\u7121\u6548\u9038\u51fa\u5e8f\u5217"},

  
   /**  Scheme is required!  */


  {
    ER_SCHEME_REQUIRED,
       "\u7db1\u8981\u662f\u5fc5\u9700\u7684\uff01"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_IN_URI,
       "\u5728 URI \u627e\u4e0d\u5230\u7db1\u8981\uff1a{0}"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_INURI,
       "\u5728 URI \u627e\u4e0d\u5230\u7db1\u8981"},

  
   /**  Path contains invalid character:   */


  {
    ER_PATH_INVALID_CHAR,
       "\u8def\u5f91\u5305\u542b\u7121\u6548\u7684\u5b57\u5143\uff1a{0}"},

  
   /**  Cannot set scheme from null string  */


  {
    ER_SCHEME_FROM_NULL_STRING,
       "\u7121\u6cd5\u5f9e\u7a7a\u5b57\u4e32\u8a2d\u5b9a\u7db1\u8981"},

  
   /**  The scheme is not conformant. */


  {
    ER_SCHEME_NOT_CONFORMANT,
       "\u7db1\u8981\u4e0d\u4e00\u81f4\u3002"},

  
   /**  Host is not a well formed address  */


  {
    ER_HOST_ADDRESS_NOT_WELLFORMED,
       "\u4e3b\u6a5f\u6c92\u6709\u5b8c\u6574\u7684\u4f4d\u5740"},

  
   /**  Port cannot be set when host is null  */


  {
    ER_PORT_WHEN_HOST_NULL,
       "\u4e3b\u6a5f\u70ba\u7a7a\u503c\u6642\uff0c\u7121\u6cd5\u8a2d\u5b9a\u901a\u8a0a\u57e0"},

  
   /**  Invalid port number  */


  {
    ER_INVALID_PORT,
       "\u7121\u6548\u7684\u901a\u8a0a\u57e0\u7de8\u865f"},

  
   /**  Fragment can only be set for a generic URI  */


  {
    ER_FRAG_FOR_GENERIC_URI,
       "\u53ea\u80fd\u5c0d\u540c\u5c6c\u7684 URI \u8a2d\u5b9a\u7247\u6bb5"},

  
   /**  Fragment cannot be set when path is null  */


  {
    ER_FRAG_WHEN_PATH_NULL,
       "\u8def\u5f91\u70ba\u7a7a\u503c\u6642\uff0c\u7121\u6cd5\u8a2d\u5b9a\u7247\u6bb5"},

  
   /**  Fragment contains invalid character  */


  {
    ER_FRAG_INVALID_CHAR,
       "\u7247\u6bb5\u5305\u542b\u7121\u6548\u5b57\u5143"},

  
 
  
   /** Parser is already in use  */


  {
    ER_PARSER_IN_USE,
        "\u5256\u6790\u5668\u5df2\u5728\u4f7f\u7528\u4e2d"},

  
   /** Parser is already in use  */


  {
    ER_CANNOT_CHANGE_WHILE_PARSING,
        "\u5256\u6790\u6642\u7121\u6cd5\u8b8a\u66f4 {0} {1}"},

  
   /** Self-causation not permitted  */


  {
    ER_SELF_CAUSATION_NOT_PERMITTED,
        "\u4e0d\u5141\u8a31\u81ea\u884c\u5f15\u8d77"},

  
   /** Userinfo may not be specified if host is not specified   */


  {
    ER_NO_USERINFO_IF_NO_HOST,
        "\u5982\u679c\u6c92\u6709\u6307\u5b9a\u4e3b\u6a5f\uff0c\u4e0d\u53ef\u6307\u5b9a Userinfo"},

  
   /** Port may not be specified if host is not specified   */


  {
    ER_NO_PORT_IF_NO_HOST,
        "\u5982\u679c\u6c92\u6709\u6307\u5b9a\u4e3b\u6a5f\uff0c\u4e0d\u53ef\u6307\u5b9a\u901a\u8a0a\u57e0"},

  
   /** Query string cannot be specified in path and query string   */


  {
    ER_NO_QUERY_STRING_IN_PATH,
        "\u5728\u8def\u5f91\u53ca\u67e5\u8a62\u5b57\u4e32\u4e2d\u4e0d\u53ef\u6307\u5b9a\u67e5\u8a62\u5b57\u4e32"},

  
   /** Fragment cannot be specified in both the path and fragment   */


  {
    ER_NO_FRAGMENT_STRING_IN_PATH,
        "\u7121\u6cd5\u5728\u8def\u5f91\u548c\u7247\u6bb5\u4e2d\u6307\u5b9a\u7247\u6bb5"},

  
   /** Cannot initialize URI with empty parameters   */


  {
    ER_CANNOT_INIT_URI_EMPTY_PARMS,
        "\u7121\u6cd5\u8d77\u59cb\u8a2d\u5b9a\u7a7a\u767d\u53c3\u6578\u7684 URI"},

  
  /**  Method not yet supported    */


  {
    ER_METHOD_NOT_SUPPORTED,
        "\u4e0d\u652f\u63f4\u65b9\u6cd5 "},


  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
     "IncrementalSAXSource_Filter \u76ee\u524d\u7121\u6cd5\u91cd\u65b0\u555f\u52d5"},

  
  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_XMLRDR_NOT_BEFORE_STARTPARSE,
     "XMLReader \u4e0d\u5728 startParse \u8981\u6c42\u4e4b\u524d"},
  

  
    {
      ER_AXIS_TRAVERSER_NOT_SUPPORTED,
       "\u4e0d\u652f\u63f4\u8ef8\u904d\u6b77\u5668\uff1a{0}"},
  

  
    {
      ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
       "\u5efa\u7acb\u7684 ListingErrorHandler \u5177\u6709\u7a7a PrintWriter\uff01"},
  

  
    {
      ER_SYSTEMID_UNKNOWN,
       "SystemId \u672a\u77e5"},
  

  
    {
      ER_LOCATION_UNKNOWN,
       "\u672a\u77e5\u7684\u932f\u8aa4\u4f4d\u7f6e"},

  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "\u524d\u7f6e\u5fc5\u9808\u89e3\u8b6f\u70ba\u540d\u7a31\u7a7a\u9593\uff1a{0}"},


  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  {
    ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "createDocument() \u5728 XPathContext \u4e2d\u4e0d\u53d7\u652f\u63f4\uff01"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "\u5c6c\u6027\u5b50\u9805\u6c92\u6709\u64c1\u6709\u8005\u6587\u4ef6\uff01"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "\u5c6c\u6027\u5b50\u9805\u6c92\u6709\u64c1\u6709\u8005\u6587\u4ef6\u5143\u7d20\uff01"},


  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  {
    ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "\u8b66\u544a\uff1a\u7121\u6cd5\u8f38\u51fa\u6587\u4ef6\u5143\u7d20\u4e4b\u524d\u7684\u6587\u5b57\uff01\u5ffd\u7565..."},


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  {
    ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "\u4e00\u500b DOM \u53ea\u80fd\u6709\u4e00\u500b\u6839\uff01"},

  
   /**  Argument 'localName' is null  */


  {
    ER_ARG_LOCALNAME_NULL,
       "\u5f15\u6578 'localName' \u70ba\u7a7a\u503c"},



  /** localname in QNAME should be a valid NCName */
 

  {
    ER_ARG_LOCALNAME_INVALID,
       "QNAME \u4e2d\u7684 Localname \u61c9\u70ba\u6709\u6548\u7684 NCName"},

  
 
  /** prefix in QNAME should be a valid NCName */
 

  {
    ER_ARG_PREFIX_INVALID,
       "QNAME \u4e2d\u7684\u524d\u7f6e\u61c9\u70ba\u6709\u6548\u7684 NCName"},

  { "BAD_CODE",
      "createMessage \u7684\u53c3\u6578\u8d85\u51fa\u754c\u9650"},
  { "FORMAT_FAILED",
      "\u5728 messageFormat \u547c\u53eb\u671f\u9593\u4e1f\u51fa\u7570\u5e38"},
  { "line", "\u884c #"},
  { "column", "\u76f4\u6b04 #"}
  
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
