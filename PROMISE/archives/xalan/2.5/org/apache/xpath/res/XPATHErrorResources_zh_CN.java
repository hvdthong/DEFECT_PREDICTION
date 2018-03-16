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
public class XPATHErrorResources_zh_CN extends XPATHErrorResources
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
      "\u5339\u914d\u6a21\u5f0f\u4e2d\u4e0d\u5141\u8bb8\u4f7f\u7528 current() \u51fd\u6570\uff01"},


  /** Field ER_CURRENT_TAKES_NO_ARGS          */


  {
    ER_CURRENT_TAKES_NO_ARGS,
      "current() \u51fd\u6570\u4e0d\u63a5\u53d7\u53d8\u91cf\uff01"},


  /** Field ER_DOCUMENT_REPLACED          */


  {
    ER_DOCUMENT_REPLACED,
      "document() \u51fd\u6570\u5de5\u5177\u5df2\u88ab org.apache.xalan.xslt.FuncDocument \u66ff\u4ee3\uff01"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */


  {
    ER_CONTEXT_HAS_NO_OWNERDOC,
      "\u4e0a\u4e0b\u6587\u6ca1\u6709\u5c5e\u4e3b\u6587\u6863\uff01"},


  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */


  {
    ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() \u7684\u53d8\u91cf\u592a\u591a\u3002"},


  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */


  {
    ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() \u7684\u53d8\u91cf\u592a\u591a\u3002"},


  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */


  {
    ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() \u7684\u53d8\u91cf\u592a\u591a\u3002"},


  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */


  {
    ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() \u7684\u53d8\u91cf\u592a\u591a\u3002"},


  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */


  {
    ER_NAME_HAS_TOO_MANY_ARGS, "name() \u7684\u53d8\u91cf\u592a\u591a\u3002"},


  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */


  {
    ER_STRING_HAS_TOO_MANY_ARGS,
      "string() \u7684\u53d8\u91cf\u592a\u591a\u3002"},


  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */


  {
    ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length() \u7684\u53d8\u91cf\u592a\u591a\u3002"},


  /** Field ER_TRANSLATE_TAKES_3_ARGS          */


  {
    ER_TRANSLATE_TAKES_3_ARGS,
      "translate() \u51fd\u6570\u4f7f\u7528\u4e09\u4e2a\u53d8\u91cf\uff01"},


  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */


  {
    ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "unparsed-entity-uri \u51fd\u6570\u5e94\u4f7f\u7528\u4e00\u4e2a\u53d8\u91cf\uff01"},


  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */


  {
    ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "\u4ecd\u672a\u5b9e\u73b0\u540d\u79f0\u7a7a\u95f4\u8f74\uff01"},


  /** Field ER_UNKNOWN_AXIS          */


  {
    ER_UNKNOWN_AXIS, "\u672a\u77e5\u8f74\uff1a{0}"},


  /** Field ER_UNKNOWN_MATCH_OPERATION          */


  {
    ER_UNKNOWN_MATCH_OPERATION, "\u5339\u914d\u64cd\u4f5c\u672a\u77e5\uff01"},


  /** Field ER_INCORRECT_ARG_LENGTH          */


  {
    ER_INCORRECT_ARG_LENGTH,
      "processing-instruction() \u8282\u70b9\u7684\u53d8\u91cf\u957f\u5ea6\u6d4b\u8bd5\u4e0d\u6b63\u786e\uff01"},


  /** Field ER_CANT_CONVERT_TO_NUMBER          */


  {
    ER_CANT_CONVERT_TO_NUMBER,
      "\u4e0d\u80fd\u5c06 {0} \u8f6c\u6362\u4e3a\u4e00\u4e2a\u6570\u5b57"},


  /** Field ER_CANT_CONVERT_TO_NODELIST          */


  {
    ER_CANT_CONVERT_TO_NODELIST,
      "\u4e0d\u80fd\u5c06 {0} \u8f6c\u6362\u4e3a\u4e00\u4e2a NodeList\uff01"},


  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */


  {
    ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "\u4e0d\u80fd\u5c06 {0} \u8f6c\u6362\u4e3a\u4e00\u4e2a NodeSetDTM\uff01"},


  /** Field ER_CANT_CONVERT_TO_TYPE          */


  {
    ER_CANT_CONVERT_TO_TYPE,


  /** Field ER_EXPECTED_MATCH_PATTERN          */


  {
    ER_EXPECTED_MATCH_PATTERN,
      "getMatchScore \u4e2d\u9884\u671f\u7684\u5339\u914d\u6a21\u5f0f\uff01"},


  /** Field ER_COULDNOT_GET_VAR_NAMED          */


  {
    ER_COULDNOT_GET_VAR_NAMED,
      "\u65e0\u6cd5\u83b7\u5f97\u547d\u540d\u7684\u53d8\u91cf {0}"},


  /** Field ER_UNKNOWN_OPCODE          */


  {
    ER_UNKNOWN_OPCODE, "\u9519\u8bef\uff01op \u4ee3\u7801\u672a\u77e5\uff1a{0}"},


  /** Field ER_EXTRA_ILLEGAL_TOKENS          */


  {
    ER_EXTRA_ILLEGAL_TOKENS, "\u989d\u5916\u7684\u975e\u6cd5\u6807\u8bb0\uff1a{0}"},


  /** Field ER_EXPECTED_DOUBLE_QUOTE          */


  {
    ER_EXPECTED_DOUBLE_QUOTE,
      "\u9519\u8bef\u5f15\u7528\u6587\u5b57... \u9884\u671f\u7684\u53cc\u5f15\u7528\uff01"},


  /** Field ER_EXPECTED_SINGLE_QUOTE          */


  {
    ER_EXPECTED_SINGLE_QUOTE,
      "\u9519\u8bef\u5f15\u7528\u6587\u5b57... \u9884\u671f\u7684\u5355\u5f15\u7528\uff01"},


  /** Field ER_EMPTY_EXPRESSION          */


  {
    ER_EMPTY_EXPRESSION, "\u7a7a\u8868\u8fbe\u5f0f\uff01"},


  /** Field ER_EXPECTED_BUT_FOUND          */


  {
    ER_EXPECTED_BUT_FOUND, "\u9884\u671f\u7684 {0}\uff0c\u4f46\u53d1\u73b0\uff1a{1}"},


  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */


  {
    ER_INCORRECT_PROGRAMMER_ASSERTION,
      "\u7a0b\u5e8f\u5458\u65ad\u8a00\u662f\u9519\u8bef\u7684\uff01- {0}"},


  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */


  {
    ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "19990709 XPath \u8349\u7a3f\u4e0d\u518d\u53ef\u9009\u7528 boolean(...) \u53d8\u91cf\u3002"},


  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "\u5df2\u627e\u5230 ','\uff0c\u4f46\u672a\u627e\u5230\u524d\u8ff0\u53d8\u91cf\uff01"},


  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "\u5df2\u627e\u5230 ','\uff0c\u4f46\u672a\u627e\u5230\u4ee5\u4e0b\u53d8\u91cf\uff01"},


  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */


  {
    ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[predicate]' \u6216 '.[predicate]' \u662f\u975e\u6cd5\u8bed\u6cd5\u3002\u8bf7\u6539\u7528 'self::node()[predicate]'\u3002"},


  /** Field ER_ILLEGAL_AXIS_NAME          */


  {
    ER_ILLEGAL_AXIS_NAME, "\u975e\u6cd5\u7684\u8f74\u540d\u79f0\uff1a{0}"},


  /** Field ER_UNKNOWN_NODETYPE          */


  {
    ER_UNKNOWN_NODETYPE, "\u672a\u77e5\u7684\u8282\u70b9\u7c7b\u578b\uff1a{0}"},


  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */


  {
    ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "\u9700\u8981\u5f15\u7528\u6a21\u5f0f\u6587\u5b57 ({0})\uff01"},


  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */


  {
    ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "\u4e0d\u80fd\u5c06 {0} \u683c\u5f0f\u5316\u4e3a\u4e00\u4e2a\u6570\u5b57\uff01"},


  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */


  {
    ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "\u4e0d\u80fd\u521b\u5efa XML TransformerFactory Liaison\uff1a{0}"},


  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */


  {
    ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "\u9519\u8bef\uff01\u672a\u627e\u5230 xpath select \u8868\u8fbe\u5f0f (-select)\u3002"},


  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */


  {
    ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "\u9519\u8bef\uff01\u672a\u5728 OP_LOCATIONPATH \u540e\u627e\u5230 ENDOP"},


  /** Field ER_ERROR_OCCURED          */


  {
    ER_ERROR_OCCURED, "\u51fa\u9519\uff01"},


  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */


  {
    ER_ILLEGAL_VARIABLE_REFERENCE,
      "\u4e3a\u53d8\u91cf\u7ed9\u51fa\u7684 VariableReference \u8d85\u51fa\u4e86\u4e0a\u4e0b\u6587\u8303\u56f4\u6216\u6ca1\u6709\u5b9a\u4e49\uff01\u540d\u79f0 = {0}"},


  /** Field ER_AXES_NOT_ALLOWED          */


  {
    ER_AXES_NOT_ALLOWED,
      "\u5339\u914d\u6a21\u5f0f\u4e2d\u4ec5\u5141\u8bb8\u4f7f\u7528 child:: \u548c attribute:: \u8f74\uff01\u8fdd\u4f8b\u8f74 = {0}"},


  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */


  {
    ER_KEY_HAS_TOO_MANY_ARGS,
      "key() \u6709\u8bb8\u591a\u4e0d\u6b63\u786e\u7684\u53d8\u91cf\u3002"},


  /** Field ER_COUNT_TAKES_1_ARG          */


  {
    ER_COUNT_TAKES_1_ARG,
      "count \u51fd\u6570\u5e94\u4f7f\u7528\u4e00\u4e2a\u53d8\u91cf\uff01"},


  /** Field ER_COULDNOT_FIND_FUNCTION          */


  {
    ER_COULDNOT_FIND_FUNCTION, "\u672a\u627e\u5230\u51fd\u6570\uff1a{0}"},


  /** Field ER_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "\u4e0d\u53d7\u652f\u6301\u7684\u7f16\u7801\uff1a{0}"},


  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */


  {
    ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "getNextSibling \u4e2d\u7684 DTM \u51fa\u73b0\u95ee\u9898 ... \u6b63\u5728\u5c1d\u8bd5\u6062\u590d"},


  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */


  {
    ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "\u7a0b\u5e8f\u5458\u51fa\u9519\uff1a\u4e0d\u80fd\u5c06 EmptyNodeList \u5199\u5165\u3002"},


  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */


  {
    ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory \u4e0d\u53d7 XPathContext \u652f\u6301\uff01"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "\u5fc5\u987b\u89e3\u51b3\u540d\u79f0\u7a7a\u95f4\u7684\u524d\u7f00\uff1a{0}"},


  /** Field ER_PARSE_NOT_SUPPORTED          */


  {
    ER_PARSE_NOT_SUPPORTED,
      "XPathContext \u4e2d\u4e0d\u652f\u6301\uff08\u8bed\u6cd5\uff09\u5206\u6790\u5668\uff08InputSource \u6e90\uff09\uff01\u65e0\u6cd5\u6253\u5f00 {0}"},


  /** Field ER_SAX_API_NOT_HANDLED          */


  {
    ER_SAX_API_NOT_HANDLED,
      "SAX API characters(char ch[]... \u4e0d\u7531 DTM \u5904\u7406\uff01"},


  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */


  {
    ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... \u4e0d\u7531 DTM \u5904\u7406\uff01"},


  /** Field ER_DTM_CANNOT_HANDLE_NODES          */


  {
    ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison \u65e0\u6cd5\u5904\u7406\u8282\u70b9\u7c7b\u578b {0}"},


  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */


  {
    ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper \u65e0\u6cd5\u5904\u7406\u8282\u70b9\u7c7b\u578b {0}"},


  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */


  {
    ER_XERCES_PARSE_ERROR_DETAILS,
      "DOM2Helper.parse \u9519\u8bef\uff1aSystemID - {0} line - {1}"},


  /** Field ER_XERCES_PARSE_ERROR          */


  {
    ER_XERCES_PARSE_ERROR, "DOM2Helper.parse \u9519\u8bef"},


  /** Field ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "\u68c0\u6d4b\u5230\u65e0\u6548\u7684 UTF-16 surrogate\uff1a{0} ?"},


  /** Field ER_OIERROR          */


  {
    ER_OIERROR, "IO \u9519\u8bef"},


  /** Field ER_CANNOT_CREATE_URL          */


  {
    ER_CANNOT_CREATE_URL, "\u65e0\u6cd5\u521b\u5efa url \u4e3a\uff1a{0}"},


  /** Field ER_XPATH_READOBJECT          */


  {
    ER_XPATH_READOBJECT, "\u5728 XPath.readObject \u4e2d\uff1a{0}"},

  
  /** Field ER_XPATH_READOBJECT         */


  {
    ER_FUNCTION_TOKEN_NOT_FOUND,
      "\u672a\u627e\u5230\u51fd\u6570\u6807\u8bb0\u3002"},

  
   /**  Can not deal with XPath type:   */


  {
    ER_CANNOT_DEAL_XPATH_TYPE,
       "\u65e0\u6cd5\u5904\u7406 XPath \u7c7b\u578b\uff1a{0}"},

  
   /**  This NodeSet is not mutable  */


  {
    ER_NODESET_NOT_MUTABLE,
       "\u6b64 NodeSet \u4e0d\u6613\u53d8"},

  
   /**  This NodeSetDTM is not mutable  */


  {
    ER_NODESETDTM_NOT_MUTABLE,
       "\u6b64 NodeSetDTM \u4e0d\u6613\u53d8"},

  
   /**  Variable not resolvable:   */


  {
    ER_VAR_NOT_RESOLVABLE,
        "\u53d8\u91cf\u4e0d\u53ef\u5206\u89e3\uff1a{0}"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "\u9519\u8bef\u5904\u7406\u5668\u65e0\u6548"},

  
   /**  Programmer's assertion: unknown opcode  */


  {
    ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "\u7a0b\u5e8f\u5458\u65ad\u8a00\uff1aopcode \u672a\u77e5\uff1a{0}"},

  
   /**  0 or 1   */


  {
    ER_ZERO_OR_ONE,
       "0 \u6216 1"},

  
   /**  rtf() not supported by XRTreeFragSelectWrapper   */


  {
    ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() \u4e0d\u53d7 XRTreeFragSelectWrapper \u652f\u6301"},

  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */


  {
    ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() \u4e0d\u53d7 XRTreeFragSelectWrapper \u652f\u6301"},

  
   /**  fsb() not supported for XStringForChars   */


  {
    ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb() \u4e0d\u652f\u6301 XStringForChars"},

  
   /**  Could not find variable with the name of   */


  {
    ER_COULD_NOT_FIND_VAR,
      "\u65e0\u6cd5\u627e\u5230\u540d\u79f0\u4e3a {0} \u7684\u53d8\u91cf"},

  
   /**  XStringForChars can not take a string for an argument   */


  {
    ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars \u65e0\u6cd5\u4f7f\u7528\u53d8\u91cf\u7684\u4e00\u4e2a\u5b57\u7b26\u4e32"},

  
   /**  The FastStringBuffer argument can not be null   */


  {
    ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "FastStringBuffer \u53d8\u91cf\u4e0d\u80fd\u4e3a\u7a7a"},
    
   /**  2 or 3   */
  

  {
    ER_TWO_OR_THREE,
       "2 \u6216 3"},

  
   /** Variable accessed before it is bound! */
  

  {
    ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "\u7ed1\u5b9a\u4e4b\u524d\u8bbf\u95ee\u4e86\u53d8\u91cf\uff01"},


   /** XStringForFSB can not take a string for an argument! */


  {
    ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB \u65e0\u6cd5\u63a5\u53d7\u5b57\u7b26\u4e32\u7c7b\u578b\u7684\u53d8\u91cf\uff01"},


   /** Error! Setting the root of a walker to null! */


  {
    ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! \u9519\u8bef\uff01\u904d\u5386\u7a0b\u5e8f\u7684\u6839\u8bbe\u7f6e\u4e3a\u7a7a!!!"},


   /** This NodeSetDTM can not iterate to a previous node! */


  {
    ER_NODESETDTM_CANNOT_ITERATE,
       "\u6b64 NodeSetDTM \u65e0\u6cd5\u8fed\u4ee3\u5230\u524d\u4e00\u4e2a\u8282\u70b9\uff01"},


  /** This NodeSet can not iterate to a previous node! */


  {
    ER_NODESET_CANNOT_ITERATE,
       "\u6b64 NodeSet \u65e0\u6cd5\u8fed\u4ee3\u5230\u524d\u4e00\u4e2a\u8282\u70b9\uff01"},


  /** This NodeSetDTM can not do indexing or counting functions! */


  {
    ER_NODESETDTM_CANNOT_INDEX,
       "\u6b64 NodeSetDTM \u65e0\u6cd5\u4e3a\u51fd\u6570\u5efa\u7acb\u7d22\u5f15\u6216\u8ba1\u7b97\u4e2a\u6570\uff01"},


  /** This NodeSet can not do indexing or counting functions! */


  {
    ER_NODESET_CANNOT_INDEX,
       "\u6b64 NodeSet \u65e0\u6cd5\u4e3a\u51fd\u6570\u5efa\u7acb\u7d22\u5f15\u6216\u8ba1\u7b97\u4e2a\u6570\uff01"},


  /** Can not call setShouldCacheNodes after nextNode has been called! */


  {
    ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "\u8c03\u7528 nextNode \u540e\u65e0\u6cd5\u8c03\u7528 setShouldCacheNodes\uff01"},


  /** {0} only allows {1} arguments */


  {
    ER_ONLY_ALLOWS,
       "{0} \u4ec5\u5141\u8bb8 {1} \u53d8\u91cf"},


  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */


  {
    ER_UNKNOWN_STEP,
       "getNextStepPos \u4e2d\u7684\u7a0b\u5e8f\u5458\u58f0\u660e\uff1a\u672a\u77e5\u7684 stepType\uff1a{0}"},



  /** Problem with RelativeLocationPath */


  {
    ER_EXPECTED_REL_LOC_PATH,



  /** Problem with LocationPath */


  {
    ER_EXPECTED_LOC_PATH,
       "\u9700\u8981\u4f4d\u7f6e\u8def\u5f84\uff0c\u4f46\u9047\u5230\u4ee5\u4e0b\u6807\u8bb0\u003a  {0}"},



  /** Problem with Step */


  {
    ER_EXPECTED_LOC_STEP,



  /** Problem with NodeTest */


  {
    ER_EXPECTED_NODE_TEST,
       "\u9700\u8981\u4e0e NCName:* \u6216 QName \u5339\u914d\u7684\u8282\u70b9\u6d4b\u8bd5\u3002"},



  /** Expected step pattern */


  {
    ER_EXPECTED_STEP_PATTERN,
       "\u9700\u8981\u6b65\u9aa4\u6a21\u5f0f\uff0c\u4f46\u9047\u5230\u201c/\u201d\u3002"},


 
  /** Expected relative path pattern */


  {
    ER_EXPECTED_REL_PATH_PATTERN,
       "\u9700\u8981\u76f8\u5bf9\u8def\u5f84\u6a21\u5f0f\u3002"},



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */


  {
    ER_CANT_CONVERT_TO_BOOLEAN,
       "\u65e0\u6cd5\u5c06 {0} \u8f6c\u6362\u6210\u5e03\u5c14\u503c\u3002"},



  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */


  {
    ER_CANT_CONVERT_TO_SINGLENODE,
       "\u65e0\u6cd5\u5c06 {0} \u8f6c\u6362\u6210\u5355\u8282\u70b9\u3002\u6b64\u83b7\u53d6\u7a0b\u5e8f\u9002\u7528\u4e8e ANY_UNORDERED_NODE_TYPE \u548c FIRST_ORDERED_NODE_TYPE \u7c7b\u578b\u3002"},



  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */


  {
    ER_CANT_GET_SNAPSHOT_LENGTH,
       "\u65e0\u6cd5\u83b7\u53d6\u7c7b\u578b {0} \u4e0a\u7684\u5feb\u7167\u957f\u5ea6\u3002\u6b64\u83b7\u53d6\u7a0b\u5e8f\u9002\u7528\u4e8e UNORDERED_NODE_SNAPSHOT_TYPE \u548c ORDERED_NODE_SNAPSHOT_TYPE \u7c7b\u578b\u3002"},


  /** Field ER_NON_ITERATOR_TYPE                */


  {
    ER_NON_ITERATOR_TYPE,
       "\u65e0\u6cd5\u8fed\u4ee3\u975e\u53e0\u4ee3\u5668\u7c7b\u578b\uff1a{0}"},



  /** Field ER_DOC_MUTATED                      */


  {
    ER_DOC_MUTATED,
       "\u7531\u4e8e\u7ed3\u679c\u7684\u8fd4\u56de\u4f7f\u6587\u6863\u53d1\u751f\u6539\u53d8\u3002\u53e0\u4ee3\u5668\u65e0\u6548\u3002"},


  /** Field ER_INVALID_XPATH_TYPE               */


  {
    ER_INVALID_XPATH_TYPE,
       "\u65e0\u6548 XPath \u7c7b\u578b\u53d8\u91cf\uff1a{0}"},


  /** Field ER_EMPTY_XPATH_RESULT                */


  {
    ER_EMPTY_XPATH_RESULT,
       "\u7a7a\u7684 XPath \u7ed3\u679c\u5bf9\u8c61"},


  /** Field ER_INCOMPATIBLE_TYPES                */


  {
    ER_INCOMPATIBLE_TYPES,
       "\u8fd4\u56de\u7c7b\u578b\uff1a{0} \u65e0\u6cd5\u5f3a\u5236\u8f6c\u6362\u6210\u6307\u5b9a\u7684\u7c7b\u578b\uff1a{1}"},


  /** Field ER_NULL_RESOLVER                     */


  {
    ER_NULL_RESOLVER,
       "\u65e0\u6cd5\u7528\u7a7a\u7684\u524d\u7f00\u89e3\u51b3\u5668\u6765\u89e3\u51b3\u524d\u7f00\u3002"},



  /** Field ER_CANT_CONVERT_TO_STRING            */


  {
    ER_CANT_CONVERT_TO_STRING,
       "\u65e0\u6cd5\u5c06 {0} \u8f6c\u6362\u6210\u5b57\u7b26\u4e32\u3002"},



  /** Field ER_NON_SNAPSHOT_TYPE                 */


  {
    ER_NON_SNAPSHOT_TYPE,
       "\u65e0\u6cd5\u5bf9\u7c7b\u578b {0} \u8c03\u7528 snapshotItem\u3002\u6b64\u65b9\u6cd5\u9002\u7528\u4e8e UNORDERED_NODE_SNAPSHOT_TYPE \u548c ORDERED_NODE_SNAPSHOT_TYPE \u7c7b\u578b\u3002"},



  /** Field ER_WRONG_DOCUMENT                    */


  {
    ER_WRONG_DOCUMENT,
       "\u4e0a\u4e0b\u6587\u8282\u70b9\u4e0d\u5c5e\u4e8e\u7ed1\u5b9a\u5230\u6b64 XPathEvaluator \u7684\u6587\u6863\u3002"},


  /** Field ER_WRONG_NODETYPE                    */


  {
    ER_WRONG_NODETYPE ,
       "\u4e0a\u4e0b\u6587\u8282\u70b9\u7c7b\u578b\u4e0d\u53d7\u652f\u6301\u3002"},


  /** Field ER_XPATH_ERROR                       */


  {
    ER_XPATH_ERROR ,
       "XPath \u4e2d\u51fa\u73b0\u672a\u77e5\u9519\u8bef\u3002"},



  



  /** Field WG_LOCALE_NAME_NOT_HANDLED          */


  {
    WG_LOCALE_NAME_NOT_HANDLED,
      "\u4ecd\u672a\u5904\u7406 format-number \u51fd\u6570\u4e2d\u7684\u8bed\u8a00\u73af\u5883\u540d\u79f0\uff01"},


  /** Field WG_PROPERTY_NOT_SUPPORTED          */


  {
    WG_PROPERTY_NOT_SUPPORTED,
      "\u4e0d\u53d7\u652f\u6301\u7684 XSL Property\uff1a{0}"},


  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */


  {
    WG_DONT_DO_ANYTHING_WITH_NS,
      "\u5f53\u524d\u4e0d\u8981\u5bf9\u5c5e\u6027\u4e2d\u7684\u540d\u79f0\u7a7a\u95f4 {0} \u6267\u884c\u4efb\u4f55\u64cd\u4f5c\uff1a{1}"},


  /** Field WG_SECURITY_EXCEPTION          */


  {
    WG_SECURITY_EXCEPTION,
      "\u5c1d\u8bd5\u8bbf\u95ee XSL \u7cfb\u7edf\u5c5e\u6027\u65f6\uff0cSecurityException\uff1a{0}"},


  /** Field WG_QUO_NO_LONGER_DEFINED          */


  {
    WG_QUO_NO_LONGER_DEFINED,
      "\u65e7\u8bed\u6cd5\uff1aXPath \u4e2d\u4e0d\u518d\u5b9a\u4e49 quo(...)\u3002"},


  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */


  {
    WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath \u9700\u8981\u7531\u4e00\u4e2a\u6d3e\u751f\u5bf9\u8c61\u6765\u6267\u884c nodeTest\uff01"},


  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */


  {
    WG_FUNCTION_TOKEN_NOT_FOUND,
      "\u672a\u627e\u5230\u51fd\u6570\u6807\u8bb0\u3002"},


  /** Field WG_COULDNOT_FIND_FUNCTION          */


  {
    WG_COULDNOT_FIND_FUNCTION,
      "\u65e0\u6cd5\u627e\u5230\u51fd\u6570\uff1a{0}"},


  /** Field WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "\u65e0\u6cd5\u5f97\u5230 URL \u81ea\uff1a{0}"},


  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */


  {
    WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "-E \u9009\u9879\u4e0d\u652f\u6301 DTM\uff08\u8bed\u6cd5\uff09\u5206\u6790\u5668"},


  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */


  {
    WG_ILLEGAL_VARIABLE_REFERENCE,
      "\u4e3a\u53d8\u91cf\u7ed9\u51fa\u7684 VariableReference \u8d85\u51fa\u4e86\u4e0a\u4e0b\u6587\u8303\u56f4\u6216\u6ca1\u6709\u5b9a\u4e49\uff01\u540d\u79f0 = {0}"},


  /** Field WG_UNSUPPORTED_ENCODING          */


  {
    WG_UNSUPPORTED_ENCODING, "\u4e0d\u53d7\u652f\u6301\u7684\u7f16\u7801\uff1a{0}"},


  { "ui_language", "zh"},
  { "help_language", "zh"},
  { "language", "zh"},
    { "BAD_CODE",
      "createMessage \u53c2\u6570\u8d85\u51fa\u4e86\u8303\u56f4"},
    { "FORMAT_FAILED",
      "messageFormat \u8c03\u7528\u671f\u95f4\u63d0\u51fa\u4e86\u4f8b\u5916"},
    { "version", ">>>>>>> Xalan \u7248\u672c"},
    { "version2", "<<<<<<<"},
    { "yes", "\u662f"},
    { "xsldone", "XSLProcessor\uff1a\u5df2\u5b8c\u6210"},
    { "xpath_option", "xpath \u9009\u9879\uff1a"},
    { "optionIN", "   [-in inputXMLURL]"},
    { "optionSelect", "   [-select xpath expression]"},
    { "optionMatch",
      "   [-match match pattern (for match diagnostics)]"},
    { "optionAnyExpr",
      "\u6216\u8005\u53ea\u6709 xpath \u8868\u8fbe\u5f0f\u5c06\u6267\u884c\u8bca\u65ad\u8f6c\u50a8"},
    { "noParsermsg1", "XSL Process \u672a\u6210\u529f\u3002"},
    { "noParsermsg2",
    "** \u65e0\u6cd5\u627e\u5230\uff08\u8bed\u6cd5\uff09\u5206\u6790\u5668 **"},
    { "noParsermsg3",
    "\u8bf7\u68c0\u67e5\u60a8\u7684\u7c7b\u8def\u5f84\u3002"},
    { "noParsermsg4",
      "\u5982\u679c\u60a8\u6ca1\u6709 IBM \u7684 XML Parser for Java\uff0c\u5219\u53ef\u4ee5\u4e0b\u8f7d\u5176\u81ea"},
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
  public static final String ERROR_HEADER = "\u9519\u8bef\uff1a";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "\u8b66\u544a\uff1a";

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
}

