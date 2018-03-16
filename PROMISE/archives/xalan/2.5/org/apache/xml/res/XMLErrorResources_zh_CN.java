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
public class XMLErrorResources_zh_CN extends XMLErrorResources
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
    ER_FUNCTION_NOT_SUPPORTED, "\u51fd\u6570\u4e0d\u53d7\u652f\u6301\uff01 "},

  
  /** Can't overwrite cause         */


  {
    ER_CANNOT_OVERWRITE_CAUSE,
			"\u65e0\u6cd5\u8986\u5199\u4e8b\u7531 "},

  
   /**  No default implementation found */


  {
    ER_NO_DEFAULT_IMPL,
         "\u672a\u627e\u5230\u7f3a\u7701\u6267\u884c  "},

  
   /**  ChunkedIntArray({0}) not currently supported */


  {
    ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
       "\u76ee\u524d\u4e0d\u652f\u6301  ChunkedIntArray({0}) "},

  
   /**  Offset bigger than slot */


  {
    ER_OFFSET_BIGGER_THAN_SLOT,
       "\u504f\u79fb\u6bd4\u69fd\u7565\u5927 "},

  
   /**  Coroutine not available, id= */


  {
    ER_COROUTINE_NOT_AVAIL,
       "\u534f\u540c\u7a0b\u5e8f\u4e0d\u53ef\u7528\uff0c id={0}"},

  
   /**  CoroutineManager recieved co_exit() request */


  {
    ER_COROUTINE_CO_EXIT,
       "CoroutineManager \u63a5\u6536\u5230  co_exit() \u8bf7\u6c42 "},

  
   /**  co_joinCoroutineSet() failed */


  {
    ER_COJOINROUTINESET_FAILED,
       "co_joinCoroutineSet() \u5931\u8d25 "},

  
   /**  Coroutine parameter error () */


  {
    ER_COROUTINE_PARAM,
       "\u534f\u540c\u7a0b\u5e8f\u53c2\u6570\u9519\u8bef  ({0})"},

  
   /**  UNEXPECTED: Parser doTerminate answers  */


  {
    ER_PARSER_DOTERMINATE_ANSWERS,
       "\nUNEXPECTED: \u5206\u6790\u5668  doTerminate \u56de\u7b54  {0}"},

  
   /**  parse may not be called while parsing */


  {
    ER_NO_PARSE_CALL_WHILE_PARSING,
       "\u5f53\u5206\u6790\u65f6\u53ef\u80fd\u4e0d\u4f1a\u8c03\u7528\u5206\u6790\u51fd\u6570 "},

  
   /**  Error: typed iterator for axis  {0} not implemented  */


  {
    ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "\u9519\u8bef\uff1a\u5728  axis {0} \u4e2d\u8f93\u5165\u7684\u8fed\u4ee3\u7a0b\u5e8f\u65e0\u6cd5\u6267\u884c "},

  
   /**  Error: iterator for axis {0} not implemented  */


  {
    ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "\u9519\u8bef\uff1a\u5728  axis {0} \u4e2d\u7684\u8fed\u4ee3\u7a0b\u5e8f\u65e0\u6cd5\u6267\u884c "},

  
   /**  Iterator clone not supported  */


  {
    ER_ITERATOR_CLONE_NOT_SUPPORTED,
       "\u4e0d\u652f\u6301\u590d\u5236\u8fed\u4ee3\u7a0b\u5e8f "},

  
   /**  Unknown axis traversal type  */


  {
    ER_UNKNOWN_AXIS_TYPE,
       "\u672a\u77e5\u7684  axis \u904d\u5386\u7c7b\u578b\uff1a {0}"},

  
   /**  Axis traverser not supported  */


  {
    ER_AXIS_NOT_SUPPORTED,
       "\u4e0d\u652f\u6301  axis \u904d\u5386\u5668\uff1a {0}"},

  
   /**  No more DTM IDs are available  */


  {
    ER_NO_DTMIDS_AVAIL,
       "\u65e0\u6cd5\u4f7f\u7528\u591a\u4e2a  DTM ID"},

  
   /**  Not supported  */


  {
    ER_NOT_SUPPORTED,
       "\u4e0d\u652f\u6301\uff1a {0}"},

  
   /**  node must be non-null for getDTMHandleFromNode  */


  {
    ER_NODE_NON_NULL,
       "\u8282\u70b9\u5728  getDTMHandleFromNode \u4e2d\u5fc5\u987b\u975e\u7a7a "},

  
   /**  Could not resolve the node to a handle  */


  {
    ER_COULD_NOT_RESOLVE_NODE,
       "\u65e0\u6cd5\u5c06\u8282\u70b9\u89e3\u6790\u4e3a\u53e5\u67c4 "},

  
   /**  startParse may not be called while parsing */


  {
    ER_STARTPARSE_WHILE_PARSING,
       "\u5728\u5206\u6790\u65f6\u53ef\u80fd\u4f1a\u8c03\u7528  startParse "},

  
   /**  startParse needs a non-null SAXParser  */


  {
    ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse \u9700\u8981\u4e00\u4e2a\u975e\u7a7a\u7684  SAXParser"},

  
   /**  could not initialize parser with */


  {
    ER_COULD_NOT_INIT_PARSER,
       "\u65e0\u6cd5\u521d\u59cb\u5316\u5206\u6790\u5668 "},

  
   /**  exception creating new instance for pool  */


  {
    ER_EXCEPTION_CREATING_POOL,
       "\u521b\u5efa\u6c60\u7684\u65b0\u5b9e\u4f8b\u65f6\u51fa\u73b0\u5f02\u5e38 "},

  
   /**  Path contains invalid escape sequence  */


  {
    ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "\u8def\u5f84\u5305\u542b\u65e0\u6548\u7684\u6362\u7801\u5e8f\u5217 "},

  
   /**  Scheme is required!  */


  {
    ER_SCHEME_REQUIRED,
       "\u9700\u8981\u914d\u7f6e\uff01 "},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_IN_URI,
       "\u5728  URI \u4e2d\u672a\u627e\u5230\u914d\u7f6e\uff1a {0}"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_INURI,
       "\u5728  URI \u4e2d\u672a\u627e\u5230\u914d\u7f6e "},

  
   /**  Path contains invalid character:   */


  {
    ER_PATH_INVALID_CHAR,
       "\u8def\u5f84\u5305\u542b\u65e0\u6548\u7684\u5b57\u7b26\uff1a {0}"},

  
   /**  Cannot set scheme from null string  */


  {
    ER_SCHEME_FROM_NULL_STRING,
       "\u65e0\u6cd5\u5728\u7a7a\u7684\u5b57\u7b26\u4e32\u4e2d\u8bbe\u7f6e\u914d\u7f6e "},

  
   /**  The scheme is not conformant. */


  {
    ER_SCHEME_NOT_CONFORMANT,
       "\u914d\u7f6e\u4e0d\u4e00\u81f4\u3002 "},

  
   /**  Host is not a well formed address  */


  {
    ER_HOST_ADDRESS_NOT_WELLFORMED,
       "\u4e3b\u673a\u5730\u5740\u7684\u683c\u5f0f\u4e0d\u6b63\u786e "},

  
   /**  Port cannot be set when host is null  */


  {
    ER_PORT_WHEN_HOST_NULL,
       "\u5f53\u4e3b\u673a\u4e3a\u7a7a\u65f6\u65e0\u6cd5\u8bbe\u7f6e\u7aef\u53e3 "},

  
   /**  Invalid port number  */


  {
    ER_INVALID_PORT,
       "\u65e0\u6548\u7684\u7aef\u53e3\u53f7 "},

  
   /**  Fragment can only be set for a generic URI  */


  {
    ER_FRAG_FOR_GENERIC_URI,
       "\u4ec5\u5728\u4e00\u822c\u7684  URI \u4e2d\u8bbe\u7f6e\u6bb5 "},

  
   /**  Fragment cannot be set when path is null  */


  {
    ER_FRAG_WHEN_PATH_NULL,
       "\u5f53\u8def\u5f84\u4e3a\u7a7a\u65f6\u65e0\u6cd5\u8bbe\u7f6e\u6bb5 "},

  
   /**  Fragment contains invalid character  */


  {
    ER_FRAG_INVALID_CHAR,
       "\u6bb5\u4e2d\u5305\u542b\u65e0\u6548\u5b57\u7b26 "},

  
 
  
   /** Parser is already in use  */


  {
    ER_PARSER_IN_USE,
        "\u5206\u6790\u5668\u6b63\u5728\u4f7f\u7528 "},

  
   /** Parser is already in use  */


  {
    ER_CANNOT_CHANGE_WHILE_PARSING,
        "\u5728\u5206\u6790\u65f6\u65e0\u6cd5\u6539\u53d8  {0} {1}"},

  
   /** Self-causation not permitted  */


  {
    ER_SELF_CAUSATION_NOT_PERMITTED,
        "\u4e0d\u5141\u8bb8\u81ea\u8eab\u5f15\u8d77\u7ed3\u679c "},

  
   /** Userinfo may not be specified if host is not specified   */


  {
    ER_NO_USERINFO_IF_NO_HOST,
        "\u5982\u679c\u672a\u6307\u5b9a\u4e3b\u673a\uff0c\u53ef\u80fd\u4e0d\u4f1a\u6307\u5b9a\u7528\u6237\u4fe1\u606f "},

  
   /** Port may not be specified if host is not specified   */


  {
    ER_NO_PORT_IF_NO_HOST,
        "\u5982\u679c\u672a\u6307\u5b9a\u4e3b\u673a\uff0c\u53ef\u80fd\u4e0d\u4f1a\u6307\u5b9a\u7aef\u53e3 "},

  
   /** Query string cannot be specified in path and query string   */


  {
    ER_NO_QUERY_STRING_IN_PATH,
        "\u5728\u8def\u5f84\u548c\u67e5\u8be2\u5b57\u7b26\u4e32\u4e2d\uff0c\u65e0\u6cd5\u6307\u5b9a\u67e5\u8be2\u5b57\u7b26\u4e32  "},

  
   /** Fragment cannot be specified in both the path and fragment   */


  {
    ER_NO_FRAGMENT_STRING_IN_PATH,
        "\u5728\u8def\u5f84\u548c\u6bb5\u4e2d\u65e0\u6cd5\u6307\u5b9a\u6bb5  "},

  
   /** Cannot initialize URI with empty parameters   */


  {
    ER_CANNOT_INIT_URI_EMPTY_PARMS,
        "\u65e0\u6cd5\u4f7f\u7528\u7a7a\u7684\u53c2\u6570\u521d\u59cb\u5316  URI"},

  
  /**  Method not yet supported    */


  {
    ER_METHOD_NOT_SUPPORTED,
        "\u65b9\u6cd5\u4ecd\u4e0d\u53d7\u652f\u6301  "},


  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
     "\u5f53\u524d\u65e0\u6cd5\u91cd\u65b0\u542f\u52a8  IncrementalSAXSource_Filter"},

  
  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_XMLRDR_NOT_BEFORE_STARTPARSE,
     "XMLReader \u672a\u5728  startParse \u8bf7\u6c42\u4e4b\u524d\u51fa\u73b0 "},



  {
    ER_AXIS_TRAVERSER_NOT_SUPPORTED,
     "\u8f74\u904d\u5386\u7a0b\u5e8f\u4e0d\u53d7\u652f\u6301\uff1a {0}"},



  {
    ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
     "\u7528\u7a7a  PrintWriter \u6765\u521b\u5efa  ListingErrorHandler\uff01 "},



  {
    ER_SYSTEMID_UNKNOWN,
     "\u672a\u77e5\u7684  SystemId"},



  {
    ER_LOCATION_UNKNOWN,
     "\u672a\u77e5\u7684\u9519\u8bef\u4f4d\u7f6e "},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "\u5fc5\u987b\u89e3\u51b3\u540d\u79f0\u7a7a\u95f4\u7684\u524d\u7f00\uff1a{0}"},


  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  {
    ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "XPathContext \u4e2d\u4e0d\u652f\u6301 createDocument()\uff01"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "\u5b50\u5c5e\u6027\u6ca1\u6709\u5c5e\u4e3b\u6587\u6863\uff01"},


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "\u5b50\u5c5e\u6027\u6ca1\u6709\u5c5e\u4e3b\u6587\u6863\u5143\u7d20\uff01"},


  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  {
    ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "\u8b66\u544a\uff1a\u4e0d\u80fd\u8f93\u51fa\u6587\u6863\u5143\u7d20\u4e4b\u524d\u7684\u6587\u5b57\uff01\u5ffd\u7565..."},


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  {
    ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "DOM \u4e0a\u4e0d\u80fd\u6709\u591a\u4e2a\u6839\uff01"},

  
   /**  Argument 'localName' is null  */


  {
    ER_ARG_LOCALNAME_NULL,
       "\u53d8\u91cf 'localName' \u4e3a\u7a7a"},



  /** localname in QNAME should be a valid NCName */


  {
    ER_ARG_LOCALNAME_INVALID,
       "QNAME \u4e2d\u7684 Localname \u5e94\u8be5\u662f\u4e00\u4e2a\u6709\u6548\u7684 NCName"},

  

  /** prefix in QNAME should be a valid NCName */


  {
    ER_ARG_PREFIX_INVALID,
       "QNAME \u4e2d\u7684\u524d\u7f00\u5e94\u8be5\u662f\u4e00\u4e2a\u6709\u6548\u7684 NCName"},

  { "BAD_CODE",
      "createMessage \u53c2\u6570\u8d85\u8fc7\u8303\u56f4 "},
  { "FORMAT_FAILED",
      "\u8c03\u7528  messageFormat \u65f6\u51fa\u73b0\u610f\u5916\u60c5\u51b5   "},
  { "line", "\u884c  #"},
  { "column", "\u5217  #"}
  
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
