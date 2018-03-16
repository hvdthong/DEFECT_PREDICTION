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
public class XPATHErrorResources_ja extends XPATHErrorResources
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
      "current() \u95a2\u6570\u306f\u4e00\u81f4\u30d1\u30bf\u30fc\u30f3\u3067\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093\u3002"},


  /** Field ER_CURRENT_TAKES_NO_ARGS          */


  {
    ER_CURRENT_TAKES_NO_ARGS,
      "current() \u95a2\u6570\u306f\u5f15\u6570\u3092\u53d7\u3051\u5165\u308c\u307e\u305b\u3093\u3002"},


  /** Field ER_DOCUMENT_REPLACED          */


  {
    ER_DOCUMENT_REPLACED,
      "document() \u95a2\u6570\u5b9f\u88c5\u306f org.apache.xalan.xslt.FuncDocument \u306b\u7f6e\u304d\u63db\u3048\u3089\u308c\u307e\u3057\u305f\u3002"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */


  {
    ER_CONTEXT_HAS_NO_OWNERDOC,
      "\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u306f\u6240\u6709\u8005\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u3092\u4fdd\u6301\u3057\u307e\u305b\u3093\u3002"},


  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */


  {
    ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() \u306e\u5f15\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},


  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */


  {
    ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() \u306e\u5f15\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},


  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */


  {
    ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() \u306e\u5f15\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},


  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */


  {
    ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() \u306e\u5f15\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},


  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */


  {
    ER_NAME_HAS_TOO_MANY_ARGS, "name() \u306e\u5f15\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},


  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */


  {
    ER_STRING_HAS_TOO_MANY_ARGS,
      "string() \u306e\u5f15\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},


  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */


  {
    ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length() \u306e\u5f15\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},


  /** Field ER_TRANSLATE_TAKES_3_ARGS          */


  {
    ER_TRANSLATE_TAKES_3_ARGS,
      "translate() \u95a2\u6570\u306b 3 \u3064\u306e\u5f15\u6570\u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059\u3002"},


  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */


  {
    ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "unparsed-entity-uri \u95a2\u6570\u306f\u5f15\u6570\u3092 1 \u3064\u3060\u3051\u4f7f\u7528\u3067\u304d\u307e\u3059\u3002"},


  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */


  {
    ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "\u540d\u524d\u7a7a\u9593\u8ef8\u306f\u307e\u3060\u5b9f\u88c5\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field ER_UNKNOWN_AXIS          */


  {
    ER_UNKNOWN_AXIS, "\u672a\u77e5\u306e\u8ef8: {0}"},


  /** Field ER_UNKNOWN_MATCH_OPERATION          */


  {
    ER_UNKNOWN_MATCH_OPERATION, "\u672a\u77e5\u306e\u7167\u5408\u30aa\u30da\u30ec\u30fc\u30b7\u30e7\u30f3\u3067\u3059\u3002"},


  /** Field ER_INCORRECT_ARG_LENGTH          */


  {
    ER_INCORRECT_ARG_LENGTH,
      "processing-instruction() \u30ce\u30fc\u30c9\u30c6\u30b9\u30c8\u306e\u5f15\u6570\u306e\u9577\u3055\u304c\u4e0d\u6b63\u3067\u3059\u3002"},


  /** Field ER_CANT_CONVERT_TO_NUMBER          */


  {
    ER_CANT_CONVERT_TO_NUMBER,
      "{0} \u3092\u6570\u5b57\u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093"},


  /** Field ER_CANT_CONVERT_TO_NODELIST          */


  {
    ER_CANT_CONVERT_TO_NODELIST,
      "{0} \u3092 NodeList \u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */


  {
    ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "{0} \u3092 NodeSetDTM \u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** Field ER_CANT_CONVERT_TO_TYPE          */


  {
    ER_CANT_CONVERT_TO_TYPE,


  /** Field ER_EXPECTED_MATCH_PATTERN          */


  {
    ER_EXPECTED_MATCH_PATTERN,
      "getMatchScore \u306b\u4e88\u671f\u3055\u308c\u308b\u4e00\u81f4\u30d1\u30bf\u30fc\u30f3\u3067\u3059\u3002"},


  /** Field ER_COULDNOT_GET_VAR_NAMED          */


  {
    ER_COULDNOT_GET_VAR_NAMED,
      "{0} \u3068\u3044\u3046\u540d\u524d\u306e\u5909\u6570\u3092\u53d6\u5f97\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f"},


  /** Field ER_UNKNOWN_OPCODE          */


  {
    ER_UNKNOWN_OPCODE, "\u30a8\u30e9\u30fc\u3002\u672a\u77e5\u306e\u30aa\u30d7\u30b7\u30e7\u30f3\u30b3\u30fc\u30c9: {0}"},


  /** Field ER_EXTRA_ILLEGAL_TOKENS          */


  {
    ER_EXTRA_ILLEGAL_TOKENS, "\u4f59\u5206\u306a\u4e0d\u6b63\u30c8\u30fc\u30af\u30f3: {0}"},


  /** Field ER_EXPECTED_DOUBLE_QUOTE          */


  {
    ER_EXPECTED_DOUBLE_QUOTE,
      "\u30ea\u30c6\u30e9\u30eb\u306e\u5f15\u7528\u7b26\u304c\u8aa4\u308a\u3067\u3059... \u4e8c\u91cd\u5f15\u7528\u7b26\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** Field ER_EXPECTED_SINGLE_QUOTE          */


  {
    ER_EXPECTED_SINGLE_QUOTE,
      "\u30ea\u30c6\u30e9\u30eb\u306e\u5f15\u7528\u7b26\u304c\u8aa4\u308a\u3067\u3059... \u5358\u4e00\u5f15\u7528\u7b26\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** Field ER_EMPTY_EXPRESSION          */


  {
    ER_EMPTY_EXPRESSION, "\u5f0f\u304c\u7a7a\u3067\u3059\u3002"},


  /** Field ER_EXPECTED_BUT_FOUND          */


  {
    ER_EXPECTED_BUT_FOUND, "{0} \u304c\u4e88\u671f\u3055\u308c\u3066\u3044\u307e\u3057\u305f\u304c\u3001{1} \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u3002"},


  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */


  {
    ER_INCORRECT_PROGRAMMER_ASSERTION,
      "\u30d7\u30ed\u30b0\u30e9\u30de\u306e\u8868\u660e\u304c\u4e0d\u6b63\u3067\u3059\u3002 - {0}"},


  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */


  {
    ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "19990709 XPath \u30c9\u30e9\u30d5\u30c8\u306b\u3064\u3044\u3066\u3001boolean(...) \u5f15\u6570\u306f\u3082\u3046\u4efb\u610f\u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3002"},


  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "',' \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u304c\u3001\u305d\u306e\u524d\u306b\u5f15\u6570\u304c\u3042\u308a\u307e\u305b\u3093\u3002"},


  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */


  {
    ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "',' \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u304c\u3001\u305d\u308c\u306b\u7d9a\u304f\u5f15\u6570\u304c\u3042\u308a\u307e\u305b\u3093\u3002"},


  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */


  {
    ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[predicate]' \u307e\u305f\u306f '.[predicate]' \u306f\u4e0d\u5f53\u306a\u69cb\u6587\u3067\u3059\u3002\u4ee3\u308f\u308a\u306b 'self::node()[predicate]' \u3092\u4f7f\u7528\u3057\u3066\u304f\u3060\u3055\u3044\u3002"},


  /** Field ER_ILLEGAL_AXIS_NAME          */


  {
    ER_ILLEGAL_AXIS_NAME, "\u4e0d\u5f53\u306a\u8ef8\u540d: {0}"},


  /** Field ER_UNKNOWN_NODETYPE          */


  {
    ER_UNKNOWN_NODETYPE, "\u672a\u77e5\u306e\u30ce\u30fc\u30c9\u30bf\u30a4\u30d7: {0}"},


  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */


  {
    ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "\u30ea\u30c6\u30e9\u30eb ({0}) \u30d1\u30bf\u30fc\u30f3\u306f\u3001\u5f15\u7528\u7b26\u3067\u56f2\u3080\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002"},


  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */


  {
    ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0} \u306f\u6570\u5b57\u306b\u66f8\u5f0f\u8a2d\u5b9a\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */


  {
    ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "XML TransformerFactory Liaison \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f: {0}"},


  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */


  {
    ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "\u30a8\u30e9\u30fc\u3002xpath \u9078\u629e\u5f0f (-select) \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */


  {
    ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "\u30a8\u30e9\u30fc\u3002OP_LOCATIONPATH \u306e\u5f8c\u306b ENDOP \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},


  /** Field ER_ERROR_OCCURED          */


  {
    ER_ERROR_OCCURED, "\u30a8\u30e9\u30fc\u304c\u767a\u751f\u3057\u307e\u3057\u305f\u3002"},


  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */


  {
    ER_ILLEGAL_VARIABLE_REFERENCE,
      "\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u306e\u5916\u3067\u3001\u307e\u305f\u306f\u5b9a\u7fa9\u306a\u3057\u3067 VariableReference \u304c\u5909\u6570\u306b\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002Name = {0}"},


  /** Field ER_AXES_NOT_ALLOWED          */


  {
    ER_AXES_NOT_ALLOWED,
      "\u4e00\u81f4\u30d1\u30bf\u30fc\u30f3\u306b\u306f\u3001child:: \u304a\u3088\u3073 attribute:: \u8ef8\u3060\u3051\u304c\u8a31\u53ef\u3055\u308c\u307e\u3059\u3002\u8a31\u53ef\u3055\u308c\u306a\u3044\u8ef8 = {0}"},


  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */


  {
    ER_KEY_HAS_TOO_MANY_ARGS,
      "key() \u306e\u5f15\u6570\u306e\u6570\u304c\u4e0d\u6b63\u3067\u3059\u3002"},


  /** Field ER_COUNT_TAKES_1_ARG          */


  {
    ER_COUNT_TAKES_1_ARG,
      "count \u95a2\u6570\u306b\u4f7f\u7528\u3067\u304d\u308b\u5f15\u6570\u306f 1 \u3064\u3067\u3059\u3002"},


  /** Field ER_COULDNOT_FIND_FUNCTION          */


  {
    ER_COULDNOT_FIND_FUNCTION, "\u95a2\u6570 {0} \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},


  /** Field ER_UNSUPPORTED_ENCODING          */


  {
    ER_UNSUPPORTED_ENCODING, "\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u306a\u3044\u30a8\u30f3\u30b3\u30fc\u30c7\u30a3\u30f3\u30b0: {0}"},


  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */


  {
    ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "getNextSibling \u306e DTM \u306b\u554f\u984c\u304c\u767a\u751f\u3057\u307e\u3057\u305f... \u5fa9\u5143\u3057\u3066\u3044\u307e\u3059"},


  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */


  {
    ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "\u30d7\u30ed\u30b0\u30e9\u30de\u30a8\u30e9\u30fc: EmptyNodeList \u306b\u66f8\u304d\u8fbc\u307f\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */


  {
    ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory \u306f XPathContext \u3067\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "\u63a5\u982d\u8f9e\u306f\u540d\u524d\u7a7a\u9593\u306b\u5909\u3048\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059: {0}"},


  /** Field ER_PARSE_NOT_SUPPORTED          */


  {
    ER_PARSE_NOT_SUPPORTED,
      "\u69cb\u6587\u89e3\u6790 (InputSource \u30bd\u30fc\u30b9) \u306f XPathContext \u3067\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002{0} \u3092\u30aa\u30fc\u30d7\u30f3\u3067\u304d\u307e\u305b\u3093"},


  /** Field ER_SAX_API_NOT_HANDLED          */


  {
    ER_SAX_API_NOT_HANDLED,
      "SAX API characters(char ch[]... \u306f DTM \u3067\u51e6\u7406\u3055\u308c\u307e\u305b\u3093\u3002"},


  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */


  {
    ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... \u306f DTM \u3067\u51e6\u7406\u3055\u308c\u307e\u305b\u3093\u3002"},


  /** Field ER_DTM_CANNOT_HANDLE_NODES          */


  {
    ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison \u306f\u30bf\u30a4\u30d7 {0} \u306e\u30ce\u30fc\u30c9\u3092\u51e6\u7406\u3067\u304d\u307e\u305b\u3093"},


  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */


  {
    ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper \u306f\u30bf\u30a4\u30d7 {0} \u306e\u30ce\u30fc\u30c9\u3092\u51e6\u7406\u3067\u304d\u307e\u305b\u3093"},


  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */


  {
    ER_XERCES_PARSE_ERROR_DETAILS,
      "DOM2Helper.parse \u30a8\u30e9\u30fc: SystemID - {0} \u884c\u756a\u53f7 - {1}"},


  /** Field ER_XERCES_PARSE_ERROR          */


  {
    ER_XERCES_PARSE_ERROR, "DOM2Helper.parse \u30a8\u30e9\u30fc"},


  /** Field ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "\u7121\u52b9\u306a UTF-16 \u4ee3\u7406\u304c\u691c\u51fa\u3055\u308c\u307e\u3057\u305f: {0} ?"},


  /** Field ER_OIERROR          */


  {
    ER_OIERROR, "\u5165\u51fa\u529b\u30a8\u30e9\u30fc"},


  /** Field ER_CANNOT_CREATE_URL          */


  {
    ER_CANNOT_CREATE_URL, "{0} \u306e URL \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093"},


  /** Field ER_XPATH_READOBJECT          */


  {
    ER_XPATH_READOBJECT, "XPath.readObject \u306b\u3042\u308a\u307e\u3059: {0}"},

  
  /** Field ER_XPATH_READOBJECT         */


  {
    ER_FUNCTION_TOKEN_NOT_FOUND,
      "\u95a2\u6570\u30c8\u30fc\u30af\u30f3\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002"},

  
   /**  Can not deal with XPath type:   */


  {
    ER_CANNOT_DEAL_XPATH_TYPE,
       "XPath \u30bf\u30a4\u30d7 {0} \u306f\u51e6\u7406\u3067\u304d\u307e\u305b\u3093"},

  
   /**  This NodeSet is not mutable  */


  {
    ER_NODESET_NOT_MUTABLE,
       "\u3053\u306e NodeSet \u306f\u53ef\u5909\u3067\u306f\u3042\u308a\u307e\u305b\u3093"},

  
   /**  This NodeSetDTM is not mutable  */


  {
    ER_NODESETDTM_NOT_MUTABLE,
       "\u3053\u306e NodeSetDTM \u306f\u53ef\u5909\u3067\u306f\u3042\u308a\u307e\u305b\u3093"},

  
   /**  Variable not resolvable:   */


  {
    ER_VAR_NOT_RESOLVABLE,
        "\u89e3\u6c7a\u3067\u304d\u306a\u3044\u5909\u6570: {0}"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "null \u30a8\u30e9\u30fc\u30cf\u30f3\u30c9\u30e9"},

  
   /**  Programmer's assertion: unknown opcode  */


  {
    ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "\u30d7\u30ed\u30b0\u30e9\u30de\u306e\u8868\u660e: \u672a\u77e5\u306e\u30aa\u30d7\u30b7\u30e7\u30f3\u30b3\u30fc\u30c9: {0}"},

  
   /**  0 or 1   */


  {
    ER_ZERO_OR_ONE,
       "0 \u307e\u305f\u306f 1"},

  
  
  
   /**  rtf() not supported by XRTreeFragSelectWrapper   */


  {
    ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() \u306f XRTreeFragSelectWrapper \u3067\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */


  {
    ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() \u306f XRTreeFragSelectWrapper \u3067\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /**  fsb() not supported for XStringForChars   */


  {
    ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "XStringForChars \u306b fsb() \u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /**  Could not find variable with the name of   */


  {
    ER_COULD_NOT_FIND_VAR,
      "{0} \u3068\u3044\u3046\u540d\u524d\u306e\u5909\u6570\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},

  
   /**  XStringForChars can not take a string for an argument   */


  {
    ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars \u306f\u5f15\u6570\u306b\u6587\u5b57\u5217\u3092\u4f7f\u7528\u3067\u304d\u307e\u305b\u3093"},

  
   /**  The FastStringBuffer argument can not be null   */


  {
    ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "FastStringBuffer \u5f15\u6570\u306f null \u306b\u3067\u304d\u307e\u305b\u3093"},
    
  
  /* MANTIS_XALAN CHANGE: BEGIN */ 
   /**  2 or 3   */


  {
    ER_TWO_OR_THREE,
       "\u25bc2 \u307e\u305f\u306f 3"},


   /** Variable accessed before it is bound! */


  {
    ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "\u25bc\u30d0\u30a4\u30f3\u30c9\u524d\u306e\u5909\u6570\u306b\u30a2\u30af\u30bb\u30b9\u3057\u307e\u3057\u305f!"},


   /** XStringForFSB can not take a string for an argument! */


  {
    ER_FSB_CANNOT_TAKE_STRING,
       "\u25bcXStringForFSB \u306e\u5f15\u6570\u306b\u306f\u6587\u5b57\u5217\u3092\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093!"},


   /** Error! Setting the root of a walker to null! */


  {
    ER_SETTING_WALKER_ROOT_TO_NULL,
       "\u25bc\n !!!! \u30a8\u30e9\u30fc! walker \u306e\u30eb\u30fc\u30c8\u306e\u8a2d\u5b9a\u3092 null \u306b\u3057\u3066\u304f\u3060\u3055\u3044!!!"},


   /** This NodeSetDTM can not iterate to a previous node! */


  {
    ER_NODESETDTM_CANNOT_ITERATE,
       "\u25bc\u3053\u306e NodeSetDTM \u306f\u524d\u306e\u30ce\u30fc\u30c9\u306b\u5bfe\u3057\u3066\u7e70\u308a\u8fd4\u3057\u51e6\u7406\u3092\u5b9f\u884c\u3067\u304d\u307e\u305b\u3093!"},


  /** This NodeSet can not iterate to a previous node! */


  {
    ER_NODESET_CANNOT_ITERATE,
       "\u25bc\u3053\u306e NodeSet \u306f\u524d\u306e\u30ce\u30fc\u30c9\u306b\u5bfe\u3057\u3066\u7e70\u308a\u8fd4\u3057\u51e6\u7406\u3092\u5b9f\u884c\u3067\u304d\u307e\u305b\u3093!"},


  /** This NodeSetDTM can not do indexing or counting functions! */


  {
    ER_NODESETDTM_CANNOT_INDEX,
       "\u25bc\u3053\u306e NodeSetDTM \u306f\u30a4\u30f3\u30c7\u30c3\u30af\u30b9\u51e6\u7406\u307e\u305f\u306f\u30ab\u30a6\u30f3\u30c8\u51e6\u7406\u3092\u5b9f\u884c\u3067\u304d\u307e\u305b\u3093!"},


  /** This NodeSet can not do indexing or counting functions! */


  {
    ER_NODESET_CANNOT_INDEX,
       "\u25bc\u3053\u306e NodeSet \u306f\u30a4\u30f3\u30c7\u30c3\u30af\u30b9\u51e6\u7406\u307e\u305f\u306f\u30ab\u30a6\u30f3\u30c8\u51e6\u7406\u3092\u5b9f\u884c\u3067\u304d\u307e\u305b\u3093!"},


  /** Can not call setShouldCacheNodes after nextNode has been called! */


  {
    ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "\u25bcnextNode \u3092\u547c\u3073\u51fa\u3057\u305f\u5f8c\u3067 setShouldCacheNodes \u3092\u547c\u3073\u51fa\u3059\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093!"},


  /** {0} only allows {1} arguments */


  {
    ER_ONLY_ALLOWS,
       "\u25bc{0} \u3067\u8a31\u3055\u308c\u308b\u5f15\u6570\u306f {1} \u3060\u3051\u3067\u3059"},


  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */


  {
    ER_UNKNOWN_STEP,
       "\u25bcgetNextStepPos \u306b\u304a\u3051\u308b\u30d7\u30ed\u30b0\u30e9\u30de\u306e\u8868\u660e: \u672a\u77e5\u306e stepType: {0}"},



  /** Problem with RelativeLocationPath */


  {
    ER_EXPECTED_REL_LOC_PATH,



  /** Problem with LocationPath */


  {
    ER_EXPECTED_LOC_PATH,
       "\u25bc\u30ed\u30b1\u30fc\u30b7\u30e7\u30f3\u30d1\u30b9\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002\u4ee3\u308f\u308a\u306b\u6b21\u306e\u30c8\u30fc\u30af\u30f3\u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u003a  {0}"},



  /** Problem with Step */


  {
    ER_EXPECTED_LOC_STEP,



  /** Problem with NodeTest */


  {
    ER_EXPECTED_NODE_TEST,
       "\u25bcNCName:* \u307e\u305f\u306f QName \u306e\u5f62\u5f0f\u306b\u4e00\u81f4\u3059\u308b\u30ce\u30fc\u30c9\u30c6\u30b9\u30c8\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002"},



  /** Expected step pattern */


  {
    ER_EXPECTED_STEP_PATTERN,
       "\u25bc\u30b9\u30c6\u30c3\u30d7\u30d1\u30bf\u30fc\u30f3\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002\u4ee3\u308f\u308a\u306b '/' \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u3002"},


 
  /** Expected relative path pattern */


  {
    ER_EXPECTED_REL_PATH_PATTERN,
       "\u25bc\u76f8\u5bfe\u30d1\u30b9\u30d1\u30bf\u30fc\u30f3\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002"},



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */


  {
    ER_CANT_CONVERT_TO_BOOLEAN,
       "\u25bc{0} \u3092 boolean \u5024\u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093\u3002"},



  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */


  {
    ER_CANT_CONVERT_TO_SINGLENODE,
       "\u25bc{0} \u3092\u5358\u4e00\u306e\u30ce\u30fc\u30c9\u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093\u3002\u3053\u306e\u53d6\u5f97\u30e1\u30bd\u30c3\u30c9\u3092\u9069\u7528\u3067\u304d\u308b\u578b\u306f\u3001ANY_UNORDERED_NODE_TYPE \u3068 FIRST_ORDERED_NODE_TYPE \u3067\u3059\u3002"},



  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */


  {
    ER_CANT_GET_SNAPSHOT_LENGTH,
       "\u25bc\u578b {0} \u306e\u30b9\u30ca\u30c3\u30d7\u30b7\u30e7\u30c3\u30c8\u9577\u3092\u53d6\u5f97\u3067\u304d\u307e\u305b\u3093\u3002\u3053\u306e\u53d6\u5f97\u30e1\u30bd\u30c3\u30c9\u3092\u9069\u7528\u3067\u304d\u308b\u578b\u306f\u3001UNORDERED_NODE_SNAPSHOT_TYPE \u3068 ORDERED_NODE_SNAPSHOT_TYPE \u3067\u3059\u3002"},


  /** Field ER_NON_ITERATOR_TYPE                */


  {
    ER_NON_ITERATOR_TYPE,
       "\u25bc\u975e iterator \u578b {0} \u306b\u5bfe\u3057\u3066\u7e70\u308a\u8fd4\u3057\u51e6\u7406\u3092\u5b9f\u884c\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093"},



  /** Field ER_DOC_MUTATED                      */


  {
    ER_DOC_MUTATED,
       "\u25bc\u7d50\u679c\u304c\u8fd4\u3055\u308c\u305f\u5f8c\u306b\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u304c\u5909\u66f4\u3055\u308c\u305f\u305f\u3081\u3001\u73fe\u5728\u306e\u53cd\u5fa9\u5b50\u306f\u7121\u52b9\u306b\u306a\u308a\u307e\u3057\u305f\u3002"},


  /** Field ER_INVALID_XPATH_TYPE               */


  {
    ER_INVALID_XPATH_TYPE,
       "\u25bc\u7121\u52b9\u306a XPath \u578b\u306e\u5f15\u6570: {0}"},


  /** Field ER_EMPTY_XPATH_RESULT                */


  {
    ER_EMPTY_XPATH_RESULT,
       "\u25bc\u7a7a\u306e XPath \u7d50\u679c\u30aa\u30d6\u30b8\u30a7\u30af\u30c8"},


  /** Field ER_INCOMPATIBLE_TYPES                */


  {
    ER_INCOMPATIBLE_TYPES,
       "\u25bc\u623b\u308a\u5024\u306e\u578b {0} \u306f\u6307\u5b9a\u3055\u308c\u305f\u578b {1} \u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093"},


  /** Field ER_NULL_RESOLVER                     */


  {
    ER_NULL_RESOLVER,
       "\u25bc\u63a5\u982d\u8f9e\u30ea\u30be\u30eb\u30d0\u304c null \u3067\u3042\u3063\u305f\u305f\u3081\u3001\u63a5\u982d\u8f9e\u306e\u89e3\u6c7a\u306b\u5931\u6557\u3057\u307e\u3057\u305f\u3002"},



  /** Field ER_CANT_CONVERT_TO_STRING            */


  {
    ER_CANT_CONVERT_TO_STRING,
       "\u25bc{0} \u3092\u6587\u5b57\u5217\u5024\u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093\u3002"},



  /** Field ER_NON_SNAPSHOT_TYPE                 */


  {
    ER_NON_SNAPSHOT_TYPE,
       "\u25bc\u578b {0} \u306e snapshotItem \u3092\u547c\u3073\u51fa\u305b\u307e\u305b\u3093\u3002\u3053\u306e\u30e1\u30bd\u30c3\u30c9\u3092\u547c\u3073\u51fa\u305b\u308b\u578b\u306f\u3001UNORDERED_NODE_SNAPSHOT_TYPE \u3068 ORDERED_NODE_SNAPSHOT_TYPE \u3067\u3059\u3002"},



  /** Field ER_WRONG_DOCUMENT                    */


  {
    ER_WRONG_DOCUMENT,
       "\u25bc\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u30ce\u30fc\u30c9\u304c\u3001\u3053\u306e XPathEvaluator \u306b\u30d0\u30a4\u30f3\u30c9\u3055\u308c\u305f\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u306b\u6240\u5c5e\u3057\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field ER_WRONG_NODETYPE                    */


  {
    ER_WRONG_NODETYPE ,
       "\u25bc\u3053\u306e\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u30ce\u30fc\u30c9\u578b\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field ER_XPATH_ERROR                       */


  {
    ER_XPATH_ERROR ,
       "\u25bcXPath \u5185\u306b\u304a\u3051\u308b\u672a\u77e5\u306e\u30a8\u30e9\u30fc"},

 



  /** Field WG_LOCALE_NAME_NOT_HANDLED          */


  {
    WG_LOCALE_NAME_NOT_HANDLED,
      "format-number \u95a2\u6570\u3067\u30ed\u30b1\u30fc\u30eb\u540d\u306f\u307e\u3060\u51e6\u7406\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field WG_PROPERTY_NOT_SUPPORTED          */


  {
    WG_PROPERTY_NOT_SUPPORTED,
      "XSL \u30d7\u30ed\u30d1\u30c6\u30a3\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093: {0}"},


  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */


  {
    WG_DONT_DO_ANYTHING_WITH_NS,
      "\u540d\u524d\u7a7a\u9593 {0} \u306e\u30d7\u30ed\u30d1\u30c6\u30a3 {1} \u306b\u306f\u73fe\u5728\u4f55\u3082\u884c\u306a\u3063\u3066\u306f\u306a\u308a\u307e\u305b\u3093"},


  /** Field WG_SECURITY_EXCEPTION          */


  {
    WG_SECURITY_EXCEPTION,
      "XSL \u30b7\u30b9\u30c6\u30e0\u30d7\u30ed\u30d1\u30c6\u30a3\u306b\u30a2\u30af\u30bb\u30b9\u3057\u3088\u3046\u3068\u3057\u305f\u3068\u304d\u306b SecurityException \u304c\u767a\u751f\u3057\u307e\u3057\u305f: {0}"},


  /** Field WG_QUO_NO_LONGER_DEFINED          */


  {
    WG_QUO_NO_LONGER_DEFINED,
      "\u53e4\u3044\u69cb\u6587: quo(...) \u306f XPath \u3067\u306f\u3082\u3046\u5b9a\u7fa9\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */


  {
    WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath \u306f nodeTest \u3092\u5b9f\u88c5\u3059\u308b\u305f\u3081\u306b\u62bd\u51fa\u3055\u308c\u305f\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */


  {
    WG_FUNCTION_TOKEN_NOT_FOUND,
      "\u95a2\u6570\u30c8\u30fc\u30af\u30f3\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002"},


  /** Field WG_COULDNOT_FIND_FUNCTION          */


  {
    WG_COULDNOT_FIND_FUNCTION,
      "\u95a2\u6570 {0} \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** Field WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "\u3053\u3053\u304b\u3089 URL \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093: {0}"},


  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */


  {
    WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "DTM \u30d1\u30fc\u30b5\u3067 -E \u30aa\u30d7\u30b7\u30e7\u30f3\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},


  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */


  {
    WG_ILLEGAL_VARIABLE_REFERENCE,
      "\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u306e\u5916\u3067\u3001\u307e\u305f\u306f\u5b9a\u7fa9\u306a\u3057\u3067\u3001\u5909\u6570\u306b VariableReference \u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002Name = {0}"},


  /** Field WG_UNSUPPORTED_ENCODING          */


  {
    WG_UNSUPPORTED_ENCODING, "\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u306a\u3044\u30a8\u30f3\u30b3\u30fc\u30c7\u30a3\u30f3\u30b0: {0}"},


  { "ui_language", "ja"},
  { "help_language", "ja"},
  { "language", "ja"},
    { "BAD_CODE",
      "createMessage \u306e\u30d1\u30e9\u30e1\u30fc\u30bf\u304c\u7bc4\u56f2\u5916\u3067\u3057\u305f"},
    { "FORMAT_FAILED",
      "messageFormat \u547c\u3073\u51fa\u3057\u4e2d\u306b\u4f8b\u5916\u304c\u30b9\u30ed\u30fc\u3055\u308c\u307e\u3057\u305f"},
    { "version", ">>>>>>> Xalan \u30d0\u30fc\u30b8\u30e7\u30f3 "},
    { "version2", "<<<<<<<"},
    { "yes", "\u306f\u3044"},
    { "xsldone", "XSLProcessor: \u7d42\u4e86"},
    { "xpath_option", "xpath \u30aa\u30d7\u30b7\u30e7\u30f3: "},
    { "optionIN", "   [-in inputXMLURL]"},
    { "optionSelect", "   [-select xpath \u5f0f]"},
    { "optionMatch",
      "   [-match \u4e00\u81f4\u30d1\u30bf\u30fc\u30f3 (\u7167\u5408\u8a3a\u65ad\u7528)]"},
    { "optionAnyExpr",
      "\u3082\u3057\u304f\u306f\u3001\u305f\u3060 xpath \u5f0f\u304c\u8a3a\u65ad\u7528\u30c0\u30f3\u30d7\u3092\u884c\u3046\u306e\u307f"},
    { "noParsermsg1",
    "XSL \u30d7\u30ed\u30bb\u30b9\u306f\u6210\u529f\u3057\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},
    { "noParsermsg2",
    "** \u30d1\u30fc\u30b5\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f **"},
    { "noParsermsg3",
    "\u30af\u30e9\u30b9\u30d1\u30b9\u3092\u30c1\u30a7\u30c3\u30af\u3057\u3066\u304f\u3060\u3055\u3044\u3002"},
    { "noParsermsg4",
      "Java \u7528\u306b IBM \u306e XML \u30d1\u30fc\u30b5\u3092\u5099\u3048\u3066\u3044\u306a\u3044\u5834\u5408\u306f\u3001\u3053\u308c\u3092\u4ee5\u4e0b\u304b\u3089\u30c0\u30a6\u30f3\u30ed\u30fc\u30c9\u3067\u304d\u307e\u3059\u3002"},
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
  public static final String ERROR_HEADER = "Error: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Warning: ";

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
