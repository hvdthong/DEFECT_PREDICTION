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
public class XPATHErrorResources_zh_TW extends XPATHErrorResources
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
      "\u5728\u7b26\u5408\u578b\u6a23\u4e2d\u4e0d\u5141\u8a31\u4f7f\u7528 current() \u51fd\u5f0f\uff01"},


  /** Field ER_CURRENT_TAKES_NO_ARGS          */


  {
    ER_CURRENT_TAKES_NO_ARGS,
      "current() \u51fd\u5f0f\u4e0d\u63a5\u53d7\u5f15\u6578\uff01"},


  /** Field ER_DOCUMENT_REPLACED          */


  {
    ER_DOCUMENT_REPLACED,
      "org.apache.xalan.xslt.FuncDocument \u5df2\u53d6\u4ee3\u57f7\u884c document() \u51fd\u5f0f\uff01"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */


  {
    ER_CONTEXT_HAS_NO_OWNERDOC,
      "\u4e0a\u4e0b\u6587\u4e0d\u542b\u64c1\u6709\u8005\u6587\u4ef6\uff01"},


  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */


  {
    ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() \u6709\u592a\u591a\u5f15\u6578\u3002"},


  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */


  {
    ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() \u6709\u592a\u591a\u5f15\u6578\u3002"},


  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */


  {
    ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() \u6709\u592a\u591a\u5f15\u6578\u3002"},


  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */


  {
    ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() \u6709\u592a\u591a\u5f15\u6578\u3002"},


  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */


  {
    ER_NAME_HAS_TOO_MANY_ARGS, "name() \u6709\u592a\u591a\u5f15\u6578\u3002"},


  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */


  {
    ER_STRING_HAS_TOO_MANY_ARGS,
      "string() \u6709\u592a\u591a\u5f15\u6578\u3002"},


  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */


  {
    ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length() \u6709\u592a\u591a\u5f15\u6578\u3002"},


  /** Field ER_TRANSLATE_TAKES_3_ARGS          */


  {
    ER_TRANSLATE_TAKES_3_ARGS,
      "translate() \u51fd\u5f0f\u9700\u8981 3 \u500b\u5f15\u6578\uff01"},


  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */


  {
    ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "unparsed-entity-uri \u51fd\u5f0f\u9700\u8981 1 \u500b\u5f15\u6578\uff01"},


  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */


  {
    ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "namespace \u8ef8\u5c1a\u672a\u5efa\u7f6e\uff01"},


  /** Field ER_UNKNOWN_AXIS          */


  {
    ER_UNKNOWN_AXIS, "\u672a\u77e5\u8ef8\uff1a{0}"},


  /** Field ER_UNKNOWN_MATCH_OPERATION          */


  {
    ER_UNKNOWN_MATCH_OPERATION, "\u672a\u77e5\u7684\u7b26\u5408\u4f5c\u696d\uff01"},


  /** Field ER_INCORRECT_ARG_LENGTH          */


  {
    ER_INCORRECT_ARG_LENGTH,
      "processing-instruction() \u7bc0\u9ede\u6e2c\u8a66\u7684\u5f15\u6578\u9577\u5ea6\u4e0d\u6b63\u78ba\uff01"},


  /** Field ER_CANT_CONVERT_TO_NUMBER          */


  {
    ER_CANT_CONVERT_TO_NUMBER,
      "{0} \u7121\u6cd5\u8f49\u63db\u70ba\u6578\u5b57"},


  /** Field ER_CANT_CONVERT_TO_NODELIST          */


  {
    ER_CANT_CONVERT_TO_NODELIST,
      "{0} \u7121\u6cd5\u8f49\u63db\u70ba NodeList\uff01"},


  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */


  {
    ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "{0} \u7121\u6cd5\u8f49\u63db\u70ba NodeSetDTM\uff01"},


  /** Field ER_CANT_CONVERT_TO_TYPE          */


  {
    ER_CANT_CONVERT_TO_TYPE,


  /** Field ER_EXPECTED_MATCH_PATTERN          */


  {
    ER_EXPECTED_MATCH_PATTERN,
      "\u5728 getMatchScore \u4e2d\u9810\u671f\u7684\u7b26\u5408\u578b\u6a23\uff01"},


  /** Field ER_COULDNOT_GET_VAR_NAMED          */


  {
    ER_COULDNOT_GET_VAR_NAMED,
      "\u7121\u6cd5\u53d6\u5f97\u53eb\u4f5c {0} \u7684\u8b8a\u6578"},


  /** Field ER_UNKNOWN_OPCODE          */


  {
    ER_UNKNOWN_OPCODE, "\u932f\u8aa4\uff01\u672a\u77e5\u7684\u4f5c\u696d\u78bc\uff1a{0}"},


  /** Field ER_EXTRA_ILLEGAL_TOKENS          */


  {
    ER_EXTRA_ILLEGAL_TOKENS, "\u984d\u5916\u4e0d\u5408\u898f\u5247\u7684\u8a18\u865f\uff1a{0}"},


  /** Field ER_EXPECTED_DOUBLE_QUOTE          */


  {
    ER_EXPECTED_DOUBLE_QUOTE,
      "\u6587\u5b57\u5217\u5f15\u865f\u62ec\u932f... \u9810\u671f\u51fa\u73fe\u96d9\u5f15\u865f\uff01"},


  /** Field ER_EXPECTED_SINGLE_QUOTE          */


  {
    ER_EXPECTED_SINGLE_QUOTE,
      "\u6587\u5b57\u5217\u5f15\u865f\u62ec\u932f... \u9810\u671f\u51fa\u73fe\u55ae\u5f15\u865f\uff01"},


  /** Field ER_EMPTY_EXPRESSION          */


  {
    ER_EMPTY_EXPRESSION, "\u7a7a\u7684\u8868\u793a\u5f0f\uff01"},


  /** Field ER_EXPECTED_BUT_FOUND          */


  {
    ER_EXPECTED_BUT_FOUND, "\u9810\u671f {0}\uff0c\u537b\u627e\u5230\uff1a{1}"},


  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */


  {
    ER_INCORRECT_PROGRAMMER_ASSERTION,
      "\u7a0b\u5f0f\u8a2d\u8a08\u5e2b\u5047\u8a2d\u4e0d\u6b63\u78ba\uff01- {0}"},


  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */


  {
    ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "boolean(...) \u5f15\u6578\u5728 19990709 XPath \u521d\u7a3f\u4e2d\u4e0d\u518d\u662f\u53ef\u9078\u7528\u7684\u3002"},


  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "\u627e\u5230 ','\uff0c\u4f46\u4e4b\u524d\u6c92\u6709\u5f15\u6578\uff01"},


  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "\u627e\u5230 ','\uff0c\u4f46\u4e4b\u5f8c\u6c92\u6709\u5f15\u6578\uff01"},


  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */


  {
    ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[predicate]' \u6216 '.[predicate]' \u662f\u4e0d\u5408\u898f\u5247\u7684\u8a9e\u6cd5\u3002\u8acb\u4f7f\u7528 'self::node()[predicate]' \u4f86\u4ee3\u66ff\u3002"},


  /** Field ER_ILLEGAL_AXIS_NAME          */


  {
    ER_ILLEGAL_AXIS_NAME, "\u4e0d\u5408\u898f\u5247\u7684\u8ef8\u540d\u7a31\uff1a{0}"},


  /** Field ER_UNKNOWN_NODETYPE          */


  {
    ER_UNKNOWN_NODETYPE, "\u672a\u77e5\u7bc0\u9ede\u985e\u578b\uff1a{0}"},


  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */


  {
    ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "\u578b\u6a23\u6587\u5b57\u5217 ({0}) \u9700\u8981\u7528\u5f15\u865f\u62ec\u4f4f\uff01"},


  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */


  {
    ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0} \u7121\u6cd5\u683c\u5f0f\u5316\u70ba\u6578\u5b57\uff01"},


  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */


  {
    ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "\u7121\u6cd5\u5efa\u7acb XML TransformerFactory Liaison\uff1a{0}"},


  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */


  {
    ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "\u932f\u8aa4\uff01\u672a\u627e\u5230 xpath select \u8868\u793a\u5f0f (-select)\u3002"},


  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */


  {
    ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "\u932f\u8aa4\uff01\u5728 OP_LOCATIONPATH \u4e4b\u5f8c\u627e\u4e0d\u5230 ENDOP"},


  /** Field ER_ERROR_OCCURED          */


  {
    ER_ERROR_OCCURED, "\u767c\u751f\u932f\u8aa4\uff01"},


  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */


  {
    ER_ILLEGAL_VARIABLE_REFERENCE,
      "\u63d0\u4f9b\u7d66\u8b8a\u6578\u7684 VariableReference \u8d85\u51fa\u4e0a\u4e0b\u6587\u6216\u6c92\u6709\u5b9a\u7fa9\uff01\u540d\u7a31 = {0}"},


  /** Field ER_AXES_NOT_ALLOWED          */


  {
    ER_AXES_NOT_ALLOWED,
      "\u5728\u7b26\u5408\u578b\u6a23\u4e2d\u50c5\u5141\u8a31 child:: \u53ca attribute:: \u8ef8\uff01 \u9055\u4f8b\u8ef8 = {0}"},


  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */


  {
    ER_KEY_HAS_TOO_MANY_ARGS,
      "key() \u542b\u6709\u4e0d\u6b63\u78ba\u7684\u5f15\u6578\u6578\u76ee\u3002"},


  /** Field ER_COUNT_TAKES_1_ARG          */


  {
    ER_COUNT_TAKES_1_ARG,
      "count \u51fd\u5f0f\u53ea\u63a5\u53d7\u4e00\u500b\u5f15\u6578\uff01"},


  /** Field ER_COULDNOT_FIND_FUNCTION          */


  {
    ER_COULDNOT_FIND_FUNCTION, "\u627e\u4e0d\u5230\u51fd\u5f0f\uff1a{0}"},


  /** Field ER_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "\u672a\u652f\u63f4\u7684\u7de8\u78bc\uff1a{0}"},


  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */


  {
    ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "getNextSibling \u4e2d\u7684 DTM \u767c\u751f\u554f\u984c... \u5617\u8a66\u56de\u5fa9"},


  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */


  {
    ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "\u7a0b\u5f0f\u8a2d\u8a08\u5e2b\u932f\u8aa4\uff1a\u7121\u6cd5\u5beb\u5165 EmptyNodeList \u4e2d\u3002"},


  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */


  {
    ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory \u4e0d\u53d7 XPathContext \u652f\u63f4\uff01"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "\u524d\u7f6e\u5fc5\u9808\u89e3\u8b6f\u70ba\u540d\u7a31\u7a7a\u9593\uff1a{0}"},


  /** Field ER_PARSE_NOT_SUPPORTED          */


  {
    ER_PARSE_NOT_SUPPORTED,
      "\u5728 XPathContext \u4e2d\u4e0d\u652f\u63f4\u5256\u6790\uff08InputSource \u4f86\u6e90\uff09\uff01\u7121\u6cd5\u958b\u555f {0}"},


  /** Field ER_SAX_API_NOT_HANDLED          */


  {
    ER_SAX_API_NOT_HANDLED,
      "SAX API \u5b57\u5143(char ch[]... DTM \u672a\u8655\u7406\uff01"},


  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */


  {
    ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... DTM \u672a\u8655\u7406\uff01"},


  /** Field ER_DTM_CANNOT_HANDLE_NODES          */


  {
    ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison \u7121\u6cd5\u8655\u7406\u985e\u578b {0} \u7684\u7bc0\u9ede"},


  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */


  {
    ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper \u7121\u6cd5\u8655\u7406\u985e\u578b {0} \u7684\u7bc0\u9ede"},


  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */


  {
    ER_XERCES_PARSE_ERROR_DETAILS,
      "DOM2Helper.parse \u932f\u8aa4\uff1aSystemID - {0} \u884c - {1}"},


  /** Field ER_XERCES_PARSE_ERROR          */


  {
    ER_XERCES_PARSE_ERROR, "DOM2Helper.parse \u932f\u8aa4"},


  /** Field ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "\u5075\u6e2c\u5230\u7121\u6548\u7684 UTF-16 \u4ee3\u7528\u54c1\uff1a{0} ?"},


  /** Field ER_OIERROR          */


  {
    ER_OIERROR, "\u8f38\u5165/\u8f38\u51fa (I/O) \u932f\u8aa4"},


  /** Field ER_CANNOT_CREATE_URL          */


  {
    ER_CANNOT_CREATE_URL, "\u7121\u6cd5\u5efa\u7acb URL \u7d66\uff1a {0}"},


  /** Field ER_XPATH_READOBJECT          */


  {
    ER_XPATH_READOBJECT, "\u5728 XPath.readObject\uff1a{0}"},

  
  /** Field ER_XPATH_READOBJECT         */


  {
    ER_FUNCTION_TOKEN_NOT_FOUND,
      "\u627e\u4e0d\u5230\u51fd\u5f0f\u8a18\u865f\u3002"},

  
   /**  Can not deal with XPath type:   */


  {
    ER_CANNOT_DEAL_XPATH_TYPE,
       "\u7121\u6cd5\u8655\u7406 XPath \u985e\u578b\uff1a{0}"},

  
   /**  This NodeSet is not mutable  */


  {
    ER_NODESET_NOT_MUTABLE,
       "\u6b64\u985e NodeSet \u4e0d\u6613\u8b8a"},

  
   /**  This NodeSetDTM is not mutable  */


  {
    ER_NODESETDTM_NOT_MUTABLE,
       "\u6b64\u985e NodeSetDTM \u4e0d\u6613\u8b8a"},

  
   /**  Variable not resolvable:   */


  {
    ER_VAR_NOT_RESOLVABLE,
        "\u8b8a\u6578\u7121\u6cd5\u89e3\u8b6f\uff1a{0}"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "\u7a7a\u7684\u932f\u8aa4\u8655\u7406\u5668"},

  
   /**  Programmer's assertion: unknown opcode  */


  {
    ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "\u7a0b\u5f0f\u8a2d\u8a08\u5e2b\u7684\u5047\u8a2d\uff1a\u672a\u77e5\u4f5c\u696d\u78bc\uff1a{0}"},

  
   /**  0 or 1   */


  {
    ER_ZERO_OR_ONE,
       "0 \u6216 1"},

  
  
     /**  rtf() not supported by XRTreeFragSelectWrapper   */


  {
    ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "XRTreeFragSelectWrapper \u4e0d\u652f\u63f4 rtf()"},

  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */


  {
    ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "XRTreeFragSelectWrapper \u4e0d\u652f\u63f4 asNodeIterator()"},

  
   /**  fsb() not supported for XStringForChars   */


  {
    ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "XStringForChars \u4e0d\u652f\u63f4 fsb()"},

  
   /**  Could not find variable with the name of   */


  {
    ER_COULD_NOT_FIND_VAR,
      "\u627e\u4e0d\u5230\u540d\u7a31\u70ba {0} \u7684\u8b8a\u6578"},

  
   /**  XStringForChars can not take a string for an argument   */


  {
    ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars \u4e0d\u63a5\u53d7\u5b57\u4e32\u5f15\u6578"},

  
   /**  The FastStringBuffer argument can not be null   */


  {
    ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "FastStringBuffer \u5f15\u6578\u4e0d\u5f97\u70ba\u7a7a\u503c"},
    
    /**  2 or 3   */
  

  {
    ER_TWO_OR_THREE,
       "2 \u6216 3"},

 
   /** Variable accessed before it is bound! */
 

  {
    ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "\u9023\u7d50\u524d\u5df2\u5b58\u53d6\u8b8a\u6578\uff01"},

 
   /** XStringForFSB can not take a string for an argument! */
 

  {
    ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB \u4e0d\u6703\u5c07\u5b57\u4e32\u770b\u4f5c\u5f15\u6578\uff01"},

 
   /** Error! Setting the root of a walker to null! */
 

  {
    ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n \uff01\uff01\uff01\uff01\u932f\u8aa4\uff01\u5c07 walker \u7684\u6839\u8a2d\u5b9a\u70ba\u7a7a\uff01\uff01\uff01"},

 
   /** This NodeSetDTM can not iterate to a previous node! */
 

  {
    ER_NODESETDTM_CANNOT_ITERATE,
       "\u7121\u6cd5\u5c0d\u524d\u4e00\u500b\u7bc0\u9ede\u91cd\u8907\u6b64 NodeSetDTM\uff01"},

 
  /** This NodeSet can not iterate to a previous node! */
 

  {
    ER_NODESET_CANNOT_ITERATE,
       "\u7121\u6cd5\u5c0d\u524d\u4e00\u500b\u7bc0\u9ede\u91cd\u8907\u6b64 NodeSet\uff01"},

 
  /** This NodeSetDTM can not do indexing or counting functions! */
 

  {
    ER_NODESETDTM_CANNOT_INDEX,
       "\u6b64 NodeSetDTM \u7121\u6cd5\u5c0d\u51fd\u5f0f\u9032\u884c\u7d22\u5f15\u6216\u8a08\u6578\uff01"},

 
  /** This NodeSet can not do indexing or counting functions! */
 

  {
    ER_NODESET_CANNOT_INDEX,
       "\u6b64 NodeSet \u7121\u6cd5\u5c0d\u51fd\u5f0f\u9032\u884c\u7d22\u5f15\u6216\u8a08\u6578\uff01"},

 
  /** Can not call setShouldCacheNodes after nextNode has been called! */
 

  {
    ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "\u547c\u53eb nextNode \u4e4b\u5f8c\uff0c\u4e0d\u80fd\u547c\u53eb setShouldCacheNodes\uff01"},

 
  /** {0} only allows {1} arguments */
 

  {
    ER_ONLY_ALLOWS,
       "{0} \u50c5\u5141\u8a31\u4f7f\u7528 {1} \u5f15\u6578"},

 
  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */
 

  {
    ER_UNKNOWN_STEP,
       "\u7a0b\u5f0f\u8a2d\u8a08\u5e2b\u5728 getNextStepPos \u4e2d\u7684\u5224\u65b7\uff1a\u672a\u77e5\u7684 stepType\uff1a{0}"},

 
 
  /** Problem with RelativeLocationPath */
 

  {
    ER_EXPECTED_REL_LOC_PATH,

 
 
  /** Problem with LocationPath */
 

  {
    ER_EXPECTED_LOC_PATH,
       "\u61c9\u70ba\u4f4d\u7f6e\u8def\u5f91\uff0c\u537b\u9047\u5230\u4ee5\u4e0b\u8a18\u865f \u003a  {0}"},

 
 
  /** Problem with Step */
 

  {
    ER_EXPECTED_LOC_STEP,

 
 
  /** Problem with NodeTest */
 

  {
    ER_EXPECTED_NODE_TEST,
       "\u61c9\u70ba\u7b26\u5408 NCName:* \u6216 QName \u7684\u7bc0\u9ede\u6e2c\u8a66\u3002"},

 
 
  /** Expected step pattern */
 

  {
    ER_EXPECTED_STEP_PATTERN,
       "\u61c9\u70ba\u6b65\u9a5f\u578b\u6a23\uff0c\u537b\u9047\u5230 '/'\u3002"},

  
 
  /** Expected relative path pattern */
 

  {
    ER_EXPECTED_REL_PATH_PATTERN,
       "\u61c9\u70ba\u76f8\u5c0d\u8def\u5f91\u578b\u6a23\u3002"},

  
 
  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */
 

  {
    ER_CANT_CONVERT_TO_BOOLEAN,
       "{0} \u7121\u6cd5\u8f49\u63db\u70ba\u5e03\u6797\u503c\u3002"},

 
 
  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */
 

  {
    ER_CANT_CONVERT_TO_SINGLENODE,
       "{0} \u7121\u6cd5\u8f49\u63db\u70ba\u55ae\u4e00\u7bc0\u9ede\u3002\u6b64 getter \u9069\u7528\u65bc ANY_UNORDERED_NODE_TYPE \u548c FIRST_ORDERED_NODE_TYPE \u985e\u578b\u3002"},

 
 
  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */
 

  {
    ER_CANT_GET_SNAPSHOT_LENGTH,
       "\u7121\u6cd5\u53d6\u5f97\u985e\u578b {0} \u7684\u5feb\u7167\u9577\u5ea6\u3002\u6b64 getter \u9069\u7528\u65bc UNORDERED_NODE_SNAPSHOT_TYPE \u548c ORDERED_NODE_SNAPSHOT_TYPE \u985e\u578b\u3002"},

 
  /** Field ER_NON_ITERATOR_TYPE                */
 

  {
    ER_NON_ITERATOR_TYPE,
       "\u7121\u6cd5\u91cd\u8907\u975e\u758a\u4ee3\u5668\u985e\u578b\uff1a{0}"},

 
 
  /** Field ER_DOC_MUTATED                      */
 

  {
    ER_DOC_MUTATED,
       "\u50b3\u56de\u7d50\u679c\u5f8c\u6587\u4ef6\u767c\u751f\u8b8a\u66f4\u3002\u758a\u4ee3\u5668\u7121\u6548\u3002"},

 
  /** Field ER_INVALID_XPATH_TYPE               */
 

  {
    ER_INVALID_XPATH_TYPE,
       "\u7121\u6548\u7684 XPath \u985e\u578b\u5f15\u6578\uff1a{0}"},

 
  /** Field ER_EMPTY_XPATH_RESULT                */
 

  {
    ER_EMPTY_XPATH_RESULT,
       "\u7a7a\u7684 XPath \u7d50\u679c\u7269\u4ef6"},

 
  /** Field ER_INCOMPATIBLE_TYPES                */
 

  {
    ER_INCOMPATIBLE_TYPES,
       "\u50b3\u56de\u7684\u985e\u578b\uff1a{0} \u7121\u6cd5\u5f37\u884c\u8f49\u63db\u70ba\u6307\u5b9a\u7684\u985e\u578b\uff1a{1}"},

 
  /** Field ER_NULL_RESOLVER                     */
 

  {
    ER_NULL_RESOLVER,
       "\u7121\u6cd5\u4f7f\u7528\u7a7a\u524d\u7f6e\u89e3\u6790\u5668\u89e3\u6790\u524d\u7f6e\u3002"},

 
 
  /** Field ER_CANT_CONVERT_TO_STRING            */
 

  {
    ER_CANT_CONVERT_TO_STRING,
       "{0} \u7121\u6cd5\u8f49\u63db\u70ba\u5b57\u4e32\u3002"},

 
 
  /** Field ER_NON_SNAPSHOT_TYPE                 */
 

  {
    ER_NON_SNAPSHOT_TYPE,
       "\u7121\u6cd5\u547c\u53eb\u985e\u578b {0} \u7684 snapshotItem\u3002 \u6b64\u65b9\u6cd5\u9069\u7528\u65bc UNORDERED_NODE_SNAPSHOT_TYPE \u548c ORDERED_NODE_SNAPSHOT_TYPE \u985e\u578b\u3002"},

 
 
  /** Field ER_WRONG_DOCUMENT                    */
 
  {
    ER_WRONG_DOCUMENT,
       "\u4e0a\u4e0b\u6587\u7bc0\u9ede\u4e0d\u5c6c\u65bc\u9023\u7d50\u81f3\u6b64 XPathEvaluator \u7684\u6587\u4ef6\u3002"},

 
  /** Field ER_WRONG_NODETYPE                    */
 

  {
    ER_WRONG_NODETYPE ,
       "\u4e0d\u652f\u63f4\u4e0a\u4e0b\u6587\u7bc0\u9ede\u3002"},

 
  /** Field ER_XPATH_ERROR                       */
 

  {
    ER_XPATH_ERROR ,
       "XPath \u4e2d\u51fa\u73fe\u672a\u77e5\u932f\u8aa4\u3002"},

 


  /** Field WG_LOCALE_NAME_NOT_HANDLED          */


  {
    WG_LOCALE_NAME_NOT_HANDLED,
      "format-number \u51fd\u5f0f\u4e2d\u7684\u8a9e\u8a00\u74b0\u5883\u540d\u7a31\u5c1a\u672a\u8655\u7406\uff01"},


  /** Field WG_PROPERTY_NOT_SUPPORTED          */


  {
    WG_PROPERTY_NOT_SUPPORTED,
      "\u4e0d\u652f\u63f4 XSL \u5167\u5bb9\uff1a{0}"},


  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */


  {
    WG_DONT_DO_ANYTHING_WITH_NS,
      "\u76ee\u524d\u8acb\u52ff\u8655\u7406\u5167\u5bb9 {1} \u4e2d\u7684\u540d\u7a31\u7a7a\u9593 {0}"},


  /** Field WG_SECURITY_EXCEPTION          */


  {
    WG_SECURITY_EXCEPTION,
      "\u5617\u8a66\u5b58\u53d6 XSL \u7cfb\u7d71\u5167\u5bb9 {0} \u6642\u767c\u751f SecurityException"},


  /** Field WG_QUO_NO_LONGER_DEFINED          */


  {
    WG_QUO_NO_LONGER_DEFINED,
      "\u820a\u8a9e\u6cd5\uff1aquo(...) \u4e0d\u518d\u5b9a\u7fa9\u65bc XPath \u4e2d\u3002"},


  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */


  {
    WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath \u9700\u8981\u884d\u751f\u7269\u4ef6\u4f86\u57f7\u884c nodeTest\uff01"},


  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */


  {
    WG_FUNCTION_TOKEN_NOT_FOUND,
      "\u627e\u4e0d\u5230\u51fd\u5f0f\u8a18\u865f\u3002"},


  /** Field WG_COULDNOT_FIND_FUNCTION          */


  {
    WG_COULDNOT_FIND_FUNCTION,
      "\u627e\u4e0d\u5230\u51fd\u5f0f\uff1a{0}"},


  /** Field WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "\u7121\u6cd5\u5f9e {0} \u7522\u751f URL"},


  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */


  {
    WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "DTM \u5256\u6790\u5668\u4e0d\u652f\u63f4 -E \u9078\u9805"},


  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */


  {
    WG_ILLEGAL_VARIABLE_REFERENCE,
      "\u63d0\u4f9b\u7d66\u8b8a\u6578\u7684 VariableReference \u8d85\u51fa\u4e0a\u4e0b\u6587\u6216\u6c92\u6709\u5b9a\u7fa9\uff01\u540d\u7a31 = {0}"},


  /** Field WG_UNSUPPORTED_ENCODING          */


  {
    WG_UNSUPPORTED_ENCODING, "\u672a\u652f\u63f4\u7684\u7de8\u78bc\uff1a{0}"},



  { "ui_language", "zh_TW"},
  { "help_language", "zh_TW"},
  { "language", "zh_TW"},
    { "BAD_CODE",
      "createMessage \u7684\u53c3\u6578\u8d85\u51fa\u754c\u9650"},
    { "FORMAT_FAILED",
      "\u5728 messageFormat \u547c\u53eb\u671f\u9593\u7570\u5e38\u4e1f\u51fa"},
    { "version", ">>>>>>> Xalan \u7248\u672c"},
    { "version2", "<<<<<<<"},
    { "yes", "\u662f"},
    { "xsldone", "XSLProcessor: done"},
    { "xpath_option", "xpath \u9078\u9805\uff1a "},
    { "optionIN", "   [-in inputXMLURL]"},
    { "optionSelect", "   [-select xpath expression]"},
    { "optionMatch", 
      "   [-match match pattern (\u7528\u65bc\u7b26\u5408\u8a3a\u65b7)]"},
    { "optionAnyExpr",
      "\u6216\u53ea\u6709\u4e00\u500b xpath \u8868\u793a\u5f0f\u6703\u57f7\u884c\u8a3a\u65b7\u50be\u5370"},
    { "noParsermsg1", "XSL \u7a0b\u5e8f\u4e0d\u6210\u529f\u3002"},
    { "noParsermsg2", "** \u627e\u4e0d\u5230\u5256\u6790\u5668 **"},
    { "noParsermsg3", "\u8acb\u6aa2\u67e5\u985e\u5225\u8def\u5f91\u3002"},
    { "noParsermsg4", 
      "\u5982\u679c\u60a8\u6c92\u6709 IBM \u7684 XML Parser for Java\uff0c\u53ef\u4e0b\u8f09\u81ea "},
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
  public static final String ERROR_HEADER = "\u932f\u8aa4\uff1a";

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

