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
public class XSLTErrorResources_ja extends XSLTErrorResources
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
      "\u30a8\u30e9\u30fc: \u5f0f\u306e\u4e2d\u3067 '{' \u3092\u6301\u3064\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093"},


  /** ER_ILLEGAL_ATTRIBUTE          */


  {
    ER_ILLEGAL_ATTRIBUTE, "{0} \u306b\u4e0d\u5f53\u306a\u5c5e\u6027\u304c\u542b\u307e\u308c\u3066\u3044\u307e\u3059: {1}"},


  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */


  {
    ER_NULL_SOURCENODE_APPLYIMPORTS,
      "xsl:apply-imports \u3067 sourceNode \u304c null \u3067\u3059"},


  /** ER_CANNOT_ADD          */


  {
    ER_CANNOT_ADD, "{0} \u3092 {1} \u306b\u8ffd\u52a0\u3067\u304d\u307e\u305b\u3093"},


  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */


  {
    ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "handleApplyTemplatesInstruction \u3067 sourceNode \u304c null \u3067\u3059\u3002"},


  /** ER_NO_NAME_ATTRIB          */


  {
    ER_NO_NAME_ATTRIB, "{0} \u306b\u306f\u540d\u524d\u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_TEMPLATE_NOT_FOUND          */


  {
    ER_TEMPLATE_NOT_FOUND, "{0} \u3068\u3044\u3046\u540d\u524d\u306e\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** ER_CANT_RESOLVE_NAME_AVT          */


  {
    ER_CANT_RESOLVE_NAME_AVT,
      "xls:call-template \u3067\u540d\u524d AVT \u3092\u89e3\u6c7a\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** ER_REQUIRES_ATTRIB          */


  {
    ER_REQUIRES_ATTRIB, "{0} \u306b\u306f\u5c5e\u6027 {1} \u304c\u5fc5\u8981\u3067\u3059:"},


  /** ER_MUST_HAVE_TEST_ATTRIB          */


  {
    ER_MUST_HAVE_TEST_ATTRIB,
      "{0} \u306b\u306f ''test'' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */


  {
    ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "\u30ec\u30d9\u30eb\u5c5e\u6027\u306b\u4e0d\u6b63\u306a\u5024\u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059: {0}"},


  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */


  {
    ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "processing-instruction \u540d\u306f 'xml' \u306b\u3067\u304d\u307e\u305b\u3093"},


  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */


  {
    ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "processing-instruction \u540d\u306f\u6709\u52b9\u306a NCName \u3067\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093: {0}"},


  /** ER_NEED_MATCH_ATTRIB          */


  {
    ER_NEED_MATCH_ATTRIB,
      "{0} \u306b\u30e2\u30fc\u30c9\u304c\u3042\u308b\u5834\u5408\u3001\u4e00\u81f4\u3059\u308b\u5c5e\u6027\u3092\u6301\u305f\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093\u3002"},


  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */


  {
    ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} \u306f name \u5c5e\u6027\u304b\u3001\u307e\u305f\u306f match \u5c5e\u6027\u3092\u5fc5\u8981\u3068\u3057\u307e\u3059\u3002"},


  /** ER_CANT_RESOLVE_NSPREFIX          */


  {
    ER_CANT_RESOLVE_NSPREFIX,
      "\u540d\u524d\u7a7a\u9593\u306e\u63a5\u982d\u8f9e {0} \u3092\u89e3\u6c7a\u3067\u304d\u307e\u305b\u3093"},


  /** ER_ILLEGAL_VALUE          */


  {
    ER_ILLEGAL_VALUE, "xml:space \u306b\u4e0d\u5f53\u306a\u5024\u304c\u3042\u308a\u307e\u3059: {0}"},


  /** ER_NO_OWNERDOC          */


  {
    ER_NO_OWNERDOC,
      "\u5b50\u30ce\u30fc\u30c9\u306f\u6240\u6709\u8005\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u3092\u4fdd\u6301\u3057\u307e\u305b\u3093\u3002"},


  /** ER_ELEMTEMPLATEELEM_ERR          */


  {
    ER_ELEMTEMPLATEELEM_ERR, "ElemTemplateElement \u30a8\u30e9\u30fc: {0}"},


  /** ER_NULL_CHILD          */


  {
    ER_NULL_CHILD, "null \u3067\u3042\u308b\u5b50\u3092\u8ffd\u52a0\u3057\u3066\u3044\u307e\u3059\u3002"},


  /** ER_NEED_SELECT_ATTRIB          */


  {
    ER_NEED_SELECT_ATTRIB, "{0} \u306f select \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_NEED_TEST_ATTRIB          */


  {
    ER_NEED_TEST_ATTRIB,
      "xsl:when \u306b\u306f 'test' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_NEED_NAME_ATTRIB          */


  {
    ER_NEED_NAME_ATTRIB,
      "xsl:with-param \u306b\u306f 'name' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_NO_CONTEXT_OWNERDOC          */


  {
    ER_NO_CONTEXT_OWNERDOC,
      "\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u306f\u6240\u6709\u8005\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u3092\u4fdd\u6301\u3057\u307e\u305b\u3093\u3002"},


  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */


  {
    ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "XML TransformerFactory Liaison {0} \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** ER_PROCESS_NOT_SUCCESSFUL          */


  {
    ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan: \u30d7\u30ed\u30bb\u30b9\u306f\u6210\u529f\u3057\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** ER_NOT_SUCCESSFUL          */


  {
    ER_NOT_SUCCESSFUL, "Xalan: \u306f\u6210\u529f\u3057\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** ER_ENCODING_NOT_SUPPORTED          */


  {
    ER_ENCODING_NOT_SUPPORTED, "\u30a8\u30f3\u30b3\u30fc\u30c7\u30a3\u30f3\u30b0\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093: {0}"},


  /** ER_COULD_NOT_CREATE_TRACELISTENER          */


  {
    ER_COULD_NOT_CREATE_TRACELISTENER,
      "TraceListener \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f: {0}"},


  /** ER_KEY_REQUIRES_NAME_ATTRIB          */


  {
    ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key \u306b\u306f 'name' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


  {
    ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key \u306b\u306f 'match' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_KEY_REQUIRES_USE_ATTRIB          */


  {
    ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key \u306b\u306f 'use' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_REQUIRES_ELEMENTS_ATTRIB          */


  {
    ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} \u306b\u306f ''elements'' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_MISSING_PREFIX_ATTRIB          */


  {
    ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) {0} \u5c5e\u6027\u306b ''prefix'' \u304c\u8db3\u308a\u307e\u305b\u3093"},


  /** ER_BAD_STYLESHEET_URL          */


  {
    ER_BAD_STYLESHEET_URL, "\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306e URL \u304c\u4e0d\u6b63\u3067\u3059: {0}"},


  /** ER_FILE_NOT_FOUND          */


  {
    ER_FILE_NOT_FOUND, "\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u30d5\u30a1\u30a4\u30eb\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f: {0}"},


  /** ER_IOEXCEPTION          */


  {
    ER_IOEXCEPTION,
      "\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u30d5\u30a1\u30a4\u30eb\u3067\u5165\u51fa\u529b\u4f8b\u5916\u304c\u767a\u751f\u3057\u307e\u3057\u305f: {0}"},


  /** ER_NO_HREF_ATTRIB          */


  {
    ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) {0} \u306e href \u5c5e\u6027\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},


  /** ER_STYLESHEET_INCLUDES_ITSELF          */


  {
    ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} \u306f\u76f4\u63a5\u7684\u307e\u305f\u306f\u9593\u63a5\u7684\u306b\u81ea\u8eab\u3092\u30a4\u30f3\u30af\u30eb\u30fc\u30c9\u3057\u3066\u3044\u307e\u3059\u3002"},


  /** ER_PROCESSINCLUDE_ERROR          */


  {
    ER_PROCESSINCLUDE_ERROR,
      "StylesheetHandler.processInclude \u30a8\u30e9\u30fc\u3001{0}"},


  /** ER_MISSING_LANG_ATTRIB          */


  {
    ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) {0} \u5c5e\u6027 ''lang'' \u304c\u8db3\u308a\u307e\u305b\u3093"},


  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */


  {
    ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) {0} \u8981\u7d20\u3092\u914d\u7f6e\u3057\u5fd8\u308c\u3066\u3044\u307e\u305b\u3093\u304b?? \u30b3\u30f3\u30c6\u30ca\u8981\u7d20 ''component'' \u304c\u8db3\u308a\u307e\u305b\u3093"},


  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */


  {
    ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "Element\u3001DocumentFragment\u3001Document\u3001\u307e\u305f\u306f PrintWriter \u306b\u3060\u3051\u51fa\u529b\u3067\u304d\u307e\u3059\u3002"},


  /** ER_PROCESS_ERROR          */


  {
    ER_PROCESS_ERROR, "StylesheetRoot.process \u30a8\u30e9\u30fc"},


  /** ER_UNIMPLNODE_ERROR          */


  {
    ER_UNIMPLNODE_ERROR, "UnImplNode \u30a8\u30e9\u30fc: {0}"},


  /** ER_NO_SELECT_EXPRESSION          */


  {
    ER_NO_SELECT_EXPRESSION,
      "\u30a8\u30e9\u30fc\u3002xpath \u306e\u9078\u629e\u5f0f (-select) \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */


  {
    ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "XSLProcessor \u3092\u76f4\u5217\u5316\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** ER_NO_INPUT_STYLESHEET          */


  {
    ER_NO_INPUT_STYLESHEET,
      "\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306e\u5165\u529b\u304c\u6307\u5b9a\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** ER_FAILED_PROCESS_STYLESHEET          */


  {
    ER_FAILED_PROCESS_STYLESHEET,
      "\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u3067\u306e\u51e6\u7406\u306b\u5931\u6557\u3057\u307e\u3057\u305f\u3002"},


  /** ER_COULDNT_PARSE_DOC          */


  {
    ER_COULDNT_PARSE_DOC, "{0} \u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u3092\u69cb\u6587\u89e3\u6790\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},


  /** ER_COULDNT_FIND_FRAGMENT          */


  {
    ER_COULDNT_FIND_FRAGMENT, "\u30d5\u30e9\u30b0\u30e1\u30f3\u30c8 {0} \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},


  /** ER_NODE_NOT_ELEMENT          */


  {
    ER_NODE_NOT_ELEMENT,
      "\u30d5\u30e9\u30b0\u30e1\u30f3\u30c8\u8b58\u5225\u5b50\u304c\u6307\u3059\u30ce\u30fc\u30c9\u304c\u8981\u7d20\u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3067\u3057\u305f: {0}"},


  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */


  {
    ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each \u306f match \u5c5e\u6027\u307e\u305f\u306f name \u5c5e\u6027\u3092\u6301\u305f\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093"},


  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */


  {
    ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u306f match \u5c5e\u6027\u307e\u305f\u306f name \u5c5e\u6027\u3092\u6301\u305f\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093"},


  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */


  {
    ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u306e\u30d5\u30e9\u30b0\u30e1\u30f3\u30c8\u306b\u30af\u30ed\u30fc\u30f3\u304c\u3042\u308a\u307e\u305b\u3093\u3002"},


  /** ER_CANT_CREATE_ITEM          */


  {
    ER_CANT_CREATE_ITEM,
      "\u7d50\u679c\u30c4\u30ea\u30fc\u306b\u9805\u76ee\u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093: {0}"},


  /** ER_XMLSPACE_ILLEGAL_VALUE          */


  {
    ER_XMLSPACE_ILLEGAL_VALUE,
      "\u30bd\u30fc\u30b9 XML \u306e xml:space \u306b\u4e0d\u5f53\u306a\u5024\u304c\u3042\u308a\u307e\u3059: {0}"},


  /** ER_NO_XSLKEY_DECLARATION          */


  {
    ER_NO_XSLKEY_DECLARATION,
      "{0} \u306b xsl:key \u5ba3\u8a00\u304c\u3042\u308a\u307e\u305b\u3093\u3002"},


  /** ER_CANT_CREATE_URL          */


  {
    ER_CANT_CREATE_URL, "\u30a8\u30e9\u30fc\u3002{0} \u306e URL \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093"},


  /** ER_XSLFUNCTIONS_UNSUPPORTED          */


  {
    ER_XSLFUNCTIONS_UNSUPPORTED, "xsl:functions \u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},


  /** ER_PROCESSOR_ERROR          */


  {
    ER_PROCESSOR_ERROR, "XSLT TransformerFactory \u30a8\u30e9\u30fc"},


  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */


  {
    ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} \u306f\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u5185\u3067\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093\u3002"},


  /** ER_RESULTNS_NOT_SUPPORTED          */


  {
    ER_RESULTNS_NOT_SUPPORTED,
      "result-ns \u306f\u3082\u3046\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002\u4ee3\u308f\u308a\u306b xsl:output \u3092\u4f7f\u7528\u3057\u3066\u304f\u3060\u3055\u3044\u3002"},


  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */


  {
    ER_DEFAULTSPACE_NOT_SUPPORTED,
      "default-space \u306f\u3082\u3046\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002\u4ee3\u308f\u308a\u306b xsl:strip-space \u307e\u305f\u306f xsl:preserve-space \u3092\u4f7f\u7528\u3057\u3066\u304f\u3060\u3055\u3044\u3002"},


  /** ER_INDENTRESULT_NOT_SUPPORTED          */


  {
    ER_INDENTRESULT_NOT_SUPPORTED,
      "indent-result \u306f\u3082\u3046\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002\u4ee3\u308f\u308a\u306b xsl:output \u3092\u4f7f\u7528\u3057\u3066\u304f\u3060\u3055\u3044\u3002"},


  /** ER_ILLEGAL_ATTRIB          */


  {
    ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} \u306b\u4e0d\u5f53\u306a\u5c5e\u6027\u304c\u3042\u308a\u307e\u3059: {1}"},


  /** ER_UNKNOWN_XSL_ELEM          */


  {
    ER_UNKNOWN_XSL_ELEM, "\u672a\u77e5\u306e XSL \u8981\u7d20: {0}"},


  /** ER_BAD_XSLSORT_USE          */


  {
    ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort \u306f xsl:apply-templates \u307e\u305f\u306f xsl:for-each \u3068\u3044\u3063\u3057\u3087\u306b\u306e\u307f\u4f7f\u7528\u3067\u304d\u307e\u3059\u3002"},


  /** ER_MISPLACED_XSLWHEN          */


  {
    ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) xsl:when \u306e\u914d\u7f6e\u304c\u8aa4\u3063\u3066\u3044\u307e\u3059\u3002"},


  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */


  {
    ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:when \u306e\u89aa\u306f xsl:choose \u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3002"},


  /** ER_MISPLACED_XSLOTHERWISE          */


  {
    ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) xsl:otherwise \u306e\u914d\u7f6e\u304c\u8aa4\u3063\u3066\u3044\u307e\u3059\u3002"},


  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */


  {
    ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:otherwise \u306e\u89aa\u306f xsl:choose \u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3002"},


  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */


  {
    ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} \u306f\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u306e\u4e2d\u306b\u7f6e\u304f\u3053\u3068\u3092\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093\u3002"},


  /** ER_UNKNOWN_EXT_NS_PREFIX          */


  {
    ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) {0} \u62e1\u5f35\u540d\u524d\u7a7a\u9593\u306e\u63a5\u982d\u8f9e {1} \u304c\u672a\u77e5\u3067\u3059"},


  /** ER_IMPORTS_AS_FIRST_ELEM          */


  {
    ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) \u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306e\u6700\u521d\u306e\u8981\u7d20\u3067\u306e\u307f\u30a4\u30f3\u30dd\u30fc\u30c8\u304c\u5b9f\u884c\u53ef\u80fd\u3067\u3059\u3002"},


  /** ER_IMPORTING_ITSELF          */


  {
    ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} \u306f\u76f4\u63a5\u7684\u307e\u305f\u306f\u9593\u63a5\u7684\u306b\u81ea\u8eab\u3092\u30a4\u30f3\u30dd\u30fc\u30c8\u3057\u3066\u3044\u307e\u3059\u3002"},


  /** ER_XMLSPACE_ILLEGAL_VAL          */


  {
    ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) " + "xml:space \u306b\u4e0d\u5f53\u306a\u5024\u304c\u3042\u308a\u307e\u3059: {0}"},


  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */


  {
    ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "processStylesheet \u306f\u6210\u529f\u3057\u307e\u305b\u3093\u3002"},


  /** ER_SAX_EXCEPTION          */


  {
    ER_SAX_EXCEPTION, "SAX \u4f8b\u5916"},


  /** ER_XSLT_ERROR          */


  {
    ER_XSLT_ERROR, "XSLT \u30a8\u30e9\u30fc"},


  /** ER_CURRENCY_SIGN_ILLEGAL          */


  {
    ER_CURRENCY_SIGN_ILLEGAL,
      "\u901a\u8ca8\u8a18\u53f7\u306f\u66f8\u5f0f\u30d1\u30bf\u30fc\u30f3\u6587\u5b57\u5217\u3067\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093\u3002"},


  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */


  {
    ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "Document \u95a2\u6570\u306f Stylesheet DOM \u3067\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093\u3002"},


  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */


  {
    ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "non-Prefix \u30ea\u30be\u30eb\u30d0\u306e\u63a5\u982d\u8f9e\u306f\u89e3\u6c7a\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** ER_REDIRECT_COULDNT_GET_FILENAME          */


  {
    ER_REDIRECT_COULDNT_GET_FILENAME,
      "Rediret \u62e1\u5f35: \u30d5\u30a1\u30a4\u30eb\u540d\u3092\u53d6\u5f97\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f - file \u5c5e\u6027\u307e\u305f\u306f select \u5c5e\u6027\u304c\u6709\u52b9\u306a\u6587\u5b57\u5217\u3092\u623b\u3059\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002 "},


  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */


  {
    ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "Redirect \u62e1\u5f35\u3067 FormatterListener \u3092\u69cb\u7bc9\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */


  {
    ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "exclude-result-prefixes \u306e\u63a5\u982d\u8f9e\u304c\u6709\u52b9\u3067\u306f\u3042\u308a\u307e\u305b\u3093: {0}"},


  /** ER_MISSING_NS_URI          */


  {
    ER_MISSING_NS_URI,
      "\u6307\u5b9a\u3055\u308c\u305f\u63a5\u982d\u8f9e\u306e\u540d\u524d\u7a7a\u9593 URI \u304c\u3042\u308a\u307e\u305b\u3093"},


  /** ER_MISSING_ARG_FOR_OPTION          */


  {
    ER_MISSING_ARG_FOR_OPTION,
      "\u30aa\u30d7\u30b7\u30e7\u30f3\u306e\u5f15\u6570\u304c\u3042\u308a\u307e\u305b\u3093: {0}"},


  /** ER_INVALID_OPTION          */


  {
    ER_INVALID_OPTION, "\u7121\u52b9\u306a\u30aa\u30d7\u30b7\u30e7\u30f3: {0}"},


  /** ER_MALFORMED_FORMAT_STRING          */


  {
    ER_MALFORMED_FORMAT_STRING, "\u5f62\u5f0f\u306e\u8aa4\u3063\u305f\u6587\u5b57\u5217: {0}"},


  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */


  {
    ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet \u306b\u306f 'version' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */


  {
    ER_ILLEGAL_ATTRIBUTE_VALUE,
      "\u5c5e\u6027: {0} \u306b\u4e0d\u5f53\u306a\u5024\u304c\u3042\u308a\u307e\u3059: {1}"},


  /** ER_CHOOSE_REQUIRES_WHEN          */


  {
    ER_CHOOSE_REQUIRES_WHEN, "xsl:choose \u306b\u306f xsl:when \u304c\u5fc5\u8981\u3067\u3059"},


  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */


  {
    ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports \u306f xsl:for-each \u3067\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093"},


  /** ER_CANT_USE_DTM_FOR_OUTPUT          */


  {
    ER_CANT_USE_DTM_FOR_OUTPUT,
      "\u51fa\u529b DOM \u30ce\u30fc\u30c9\u306b DTMLiaison \u3092\u4f7f\u7528\u3067\u304d\u307e\u305b\u3093... \u4ee3\u308f\u308a\u306b org.apache.xpath.DOM2Helper \u3092\u6e21\u3057\u307e\u3059\u3002"},


  /** ER_CANT_USE_DTM_FOR_INPUT          */


  {
    ER_CANT_USE_DTM_FOR_INPUT,
      "\u5165\u529b DOM \u30ce\u30fc\u30c9\u306b DTMLiaison \u3092\u4f7f\u7528\u3067\u304d\u307e\u305b\u3093... \u4ee3\u308f\u308a\u306b org.apache.xpath.DOM2Helper \u3092\u6e21\u3057\u307e\u3059\u3002"},


  /** ER_CALL_TO_EXT_FAILED          */


  {
    ER_CALL_TO_EXT_FAILED,
      "\u62e1\u5f35\u8981\u7d20\u306e\u547c\u3073\u51fa\u3057\u306b\u5931\u6557\u3057\u307e\u3057\u305f: {0}"},


  /** ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "\u63a5\u982d\u8f9e\u306f\u540d\u524d\u7a7a\u9593\u306b\u5909\u308f\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059: {0}"},


  /** ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "\u7121\u52b9\u306a UTF-16 \u4ee3\u7406\u304c\u691c\u51fa\u3055\u308c\u307e\u3057\u305f: {0} ?"},


  /** ER_XSLATTRSET_USED_ITSELF          */


  {
    ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} \u306f\u81ea\u8eab\u3092\u4f7f\u7528\u3057\u305f\u305f\u3081\u3001\u7121\u9650\u30eb\u30fc\u30d7\u304c\u767a\u751f\u3057\u307e\u3059\u3002"},


  /** ER_CANNOT_MIX_XERCESDOM          */


  {
    ER_CANNOT_MIX_XERCESDOM,
      "\u975e Xerces-DOM \u5165\u529b\u3068 Xerces-DOM \u51fa\u529b\u3092\u6df7\u5408\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** ER_TOO_MANY_LISTENERS          */


  {
    ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},


  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */


  {
    ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "ElemTemplateElement.readObject \u306b {0} \u304c\u3042\u308a\u307e\u3059"},


  /** ER_DUPLICATE_NAMED_TEMPLATE          */


  {
    ER_DUPLICATE_NAMED_TEMPLATE,
      "\u4ee5\u4e0b\u306b\u793a\u3059\u540d\u524d\u306e\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u304c\u8907\u6570\u898b\u3064\u304b\u308a\u307e\u3057\u305f: {0}"},


  /** ER_INVALID_KEY_CALL          */


  {
    ER_INVALID_KEY_CALL,
      "\u7121\u52b9\u306a\u95a2\u6570\u547c\u3073\u51fa\u3057: recursive key() \u547c\u3073\u51fa\u3057\u306f\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093"},

  
  /** Variable is referencing itself          */


  {
    ER_REFERENCING_ITSELF,
      "\u5909\u6570 {0} \u306f\u76f4\u63a5\u7684\u307e\u305f\u306f\u9593\u63a5\u7684\u306b\u81ea\u8eab\u3092\u53c2\u7167\u3057\u3066\u3044\u307e\u3059\u3002"},

  
  /** Illegal DOMSource input          */


  {
    ER_ILLEGAL_DOMSOURCE_INPUT,
      "newTemplates \u306e DOMSource \u306b\u5bfe\u3059\u308b\u5165\u529b\u30ce\u30fc\u30c9\u306f null \u306b\u3067\u304d\u307e\u305b\u3093\u3002 "},

	
	/** Class not found for option         */


  {
    ER_CLASS_NOT_FOUND_FOR_OPTION,
			"\u30aa\u30d7\u30b7\u30e7\u30f3 {0} \u306b\u5bfe\u3059\u308b\u30af\u30e9\u30b9\u30d5\u30a1\u30a4\u30eb\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093"},

	
	/** Required Element not found         */


  {
    ER_REQUIRED_ELEM_NOT_FOUND,
			"\u5fc5\u8981\u306a\u8981\u7d20\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093: {0}"},

  
  /** InputStream cannot be null         */


  {
    ER_INPUT_CANNOT_BE_NULL,
			"InputStream \u306f null \u306b\u3067\u304d\u307e\u305b\u3093"},

  
  /** URI cannot be null         */


  {
    ER_URI_CANNOT_BE_NULL,
			"URI \u306f null \u306b\u3067\u304d\u307e\u305b\u3093"},

  
  /** File cannot be null         */


  {
    ER_FILE_CANNOT_BE_NULL,
			"File \u306f null \u306b\u3067\u304d\u307e\u305b\u3093"},

  
   /** InputSource cannot be null         */


  {
    ER_SOURCE_CANNOT_BE_NULL,
			"InputSource \u306f null \u306b\u3067\u304d\u307e\u305b\u3093"},

  
  /** Could not initialize BSF Manager        */


  {
    ER_CANNOT_INIT_BSFMGR,
			"BSF Manager \u3092\u521d\u671f\u5316\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f"},

  
  /** Could not compile extension       */


  {
    ER_CANNOT_CMPL_EXTENSN,
			"\u62e1\u5f35\u3092\u30b3\u30f3\u30d1\u30a4\u30eb\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f"},

  
  /** Could not create extension       */


  {
    ER_CANNOT_CREATE_EXTENSN,
       "\u62e1\u5f35 {0} \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f\u3002\u539f\u56e0: {1}"},

  
  /** Instance method call to method {0} requires an Object instance as first argument       */


  {
    ER_INSTANCE_MTHD_CALL_REQUIRES,
      "\u30e1\u30bd\u30c3\u30c9 {0} \u306e\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u30e1\u30bd\u30c3\u30c9\u547c\u3073\u51fa\u3057\u306f\u3001\u6700\u521d\u306e\u5f15\u6570\u306b Objcet \u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3092\u5fc5\u8981\u3068\u3057\u307e\u3059"},

  
  /** Invalid element name specified       */


  {
    ER_INVALID_ELEMENT_NAME,
      "\u7121\u52b9\u306a\u8981\u7d20\u540d\u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f {0}"},

  
   /** Element name method must be static      */


  {
    ER_ELEMENT_NAME_METHOD_STATIC,
      "\u8981\u7d20\u540d\u30e1\u30bd\u30c3\u30c9\u306f static \u3067\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093 {0}"},

  
   /** Extension function {0} : {1} is unknown      */


  {
    ER_EXTENSION_FUNC_UNKNOWN,
             "\u62e1\u5f35\u95a2\u6570 {0} : {1} \u306f\u672a\u77e5\u3067\u3059"},

  
   /** More than one best match for constructor for       */


  {
    ER_MORE_MATCH_CONSTRUCTOR,
             "{0} \u306e\u30b3\u30f3\u30b9\u30c8\u30e9\u30af\u30bf\u306b\u6700\u9069\u306a\u4e00\u81f4\u304c\u8907\u6570\u500b\u3042\u308a\u307e\u3059 {0}"},

  
   /** More than one best match for method      */


  {
    ER_MORE_MATCH_METHOD,
             "\u30e1\u30bd\u30c3\u30c9 {0} \u306b\u6700\u9069\u306a\u4e00\u81f4\u304c\u8907\u6570\u500b\u3042\u308a\u307e\u3059"},

  
   /** More than one best match for element method      */


  {
    ER_MORE_MATCH_ELEMENT,
             "\u8981\u7d20\u30e1\u30bd\u30c3\u30c9 {0} \u306b\u6700\u9069\u306a\u4e00\u81f4\u304c\u8907\u6570\u500b\u3042\u308a\u307e\u3059"},

  
   /** Invalid context passed to evaluate       */


  {
    ER_INVALID_CONTEXT_PASSED,
             "{0} \u3092\u8a55\u4fa1\u3059\u308b\u306e\u306b\u7121\u52b9\u306a\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u304c\u6e21\u3055\u308c\u307e\u3057\u305f"},

  
   /** Pool already exists       */


  {
    ER_POOL_EXISTS,
             "Pool \u304c\u3059\u3067\u306b\u5b58\u5728\u3057\u307e\u3059"},

  
   /** No driver Name specified      */


  {
    ER_NO_DRIVER_NAME,
             "\u30c9\u30e9\u30a4\u30d0\u306e Name \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /** No URL specified     */


  {
    ER_NO_URL,
             "URL \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  
   /** Pool size is less than one    */


  {
    ER_POOL_SIZE_LESSTHAN_ONE,
             "1 \u3088\u308a\u5c0f\u3055\u3044 Pool \u306e\u30b5\u30a4\u30ba\u3067\u3059\u3002"},

  
   /** Invalid driver name specified    */


  {
    ER_INVALID_DRIVER,
             "\u7121\u52b9\u306a\u30c9\u30e9\u30a4\u30d0\u540d\u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002"},

  
   /** Did not find the stylesheet root    */


  {
    ER_NO_STYLESHEETROOT,
             "\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306e\u30eb\u30fc\u30c8\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},

  
   /** Illegal value for xml:space     */


  {
    ER_ILLEGAL_XMLSPACE_VALUE,
         "xml:space \u306b\u4e0d\u5f53\u306a\u5024\u3067\u3059"},

  
   /** processFromNode failed     */


  {
    ER_PROCESSFROMNODE_FAILED,
         "processFromNode \u304c\u5931\u6557\u3057\u307e\u3057\u305f"},

  
   /** The resource [] could not load:     */


  {
    ER_RESOURCE_COULD_NOT_LOAD,
        "\u30ea\u30bd\u30fc\u30b9 [ {0} ] \u306f\u6b21\u306e\u3082\u306e\u3092\u30ed\u30fc\u30c9\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f: {1} \n {2} \t {3}"},

   
  
   /** Buffer size <=0     */


  {
    ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "\u30d0\u30c3\u30d5\u30a1\u30b5\u30a4\u30ba <=0"},

  
   /** Unknown error when calling extension    */


  {
    ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "\u62e1\u5f35\u3092\u547c\u3073\u51fa\u3059\u3068\u304d\u306b\u672a\u77e5\u306e\u30a8\u30e9\u30fc\u304c\u767a\u751f\u3057\u307e\u3057\u305f"},

  
   /** Prefix {0} does not have a corresponding namespace declaration    */


  {
    ER_NO_NAMESPACE_DECL,
        "\u63a5\u982d\u8f9e {0} \u306b\u306f\u5bfe\u5fdc\u3059\u308b\u540d\u524d\u7a7a\u9593\u5ba3\u8a00\u304c\u3042\u308a\u307e\u305b\u3093"},

  
   /** Element content not allowed for lang=javaclass   */


  {
    ER_ELEM_CONTENT_NOT_ALLOWED,
        "\u8981\u7d20\u306e\u5185\u5bb9\u306f lang=javaclass {0} \u306b\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093"},
     
  
   /** Stylesheet directed termination   */


  {
    ER_STYLESHEET_DIRECTED_TERMINATION,
        "\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306b\u3088\u308a\u7d42\u4e86\u304c\u6307\u793a\u3055\u308c\u307e\u3057\u305f"},

  
   /** 1 or 2   */


  {
    ER_ONE_OR_TWO,
        "1 \u307e\u305f\u306f 2"},

  
   /** 2 or 3   */


  {
    ER_TWO_OR_THREE,
        "2 \u307e\u305f\u306f 3"},

  
   /** Could not load {0} (check CLASSPATH), now using just the defaults   */


  {
    ER_COULD_NOT_LOAD_RESOURCE,
        "{0} \u3092\u30ed\u30fc\u30c9\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f (CLASSPATH \u3092\u30c1\u30a7\u30c3\u30af\u3057\u3066\u304f\u3060\u3055\u3044)\u3002\u30c7\u30d5\u30a9\u30eb\u30c8\u3060\u3051\u3092\u4f7f\u7528\u3057\u307e\u3059\u3002"},

  
   /** Cannot initialize default templates   */


  {
    ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "\u30c7\u30d5\u30a9\u30eb\u30c8\u306e\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u3092\u521d\u671f\u5316\u3067\u304d\u307e\u305b\u3093"},

  
   /** Result should not be null   */


  {
    ER_RESULT_NULL,
        "Result \u306f null \u306b\u306f\u3067\u304d\u307e\u305b\u3093"},

    
   /** Result could not be set   */


  {
    ER_RESULT_COULD_NOT_BE_SET,
        "Result \u3092\u8a2d\u5b9a\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f"},

  
   /** No output specified   */


  {
    ER_NO_OUTPUT_SPECIFIED,
        "\u51fa\u529b\u3092\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f"},

  
   /** Can't transform to a Result of type   */


  {
    ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "\u30bf\u30a4\u30d7 {0} \u306e Result \u306b\u5909\u5f62\u3067\u304d\u307e\u305b\u3093"},

  
   /** Can't transform to a Source of type   */


  {
    ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "\u30bf\u30a4\u30d7 {0} \u306e Source \u306b\u5909\u5f62\u3067\u304d\u307e\u305b\u3093"},

  
   /** Null content handler  */


  {
    ER_NULL_CONTENT_HANDLER,
        "Null \u30b3\u30f3\u30c6\u30f3\u30c4\u30cf\u30f3\u30c9\u30e9"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "Null \u30a8\u30e9\u30fc\u30cf\u30f3\u30c9\u30e9"},

  
   /** parse can not be called if the ContentHandler has not been set */


  {
    ER_CANNOT_CALL_PARSE,
        "ContentHandler \u304c\u8a2d\u5b9a\u3055\u308c\u3066\u3044\u306a\u3044\u3068\u69cb\u6587\u89e3\u6790\u3092\u547c\u3073\u51fa\u3059\u3053\u3068\u304c\u3067\u304d\u307e\u305b\u3093"},

  
   /**  No parent for filter */


  {
    ER_NO_PARENT_FOR_FILTER,
        "\u30d5\u30a3\u30eb\u30bf\u51e6\u7406\u3059\u308b\u89aa\u304c\u3042\u308a\u307e\u305b\u3093"},

  
  
   /**  No stylesheet found in: {0}, media */


  {
    ER_NO_STYLESHEET_IN_MEDIA,
         "{0} \u306b\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002\u30e1\u30c7\u30a3\u30a2= {1}"},

  
   /**  No xml-stylesheet PI found in */


  {
    ER_NO_STYLESHEET_PI,
         "xml-stylesheet PI \u304c {0} \u306b\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},

  
   /**  Not supported  */


  {
    ER_NOT_SUPPORTED,
       "\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093: {0}"},

  
   /**  Value for property {0} should be a Boolean instance  */


  {
    ER_PROPERTY_VALUE_BOOLEAN,
       "\u30d7\u30ed\u30d1\u30c6\u30a3 {0} \u306e\u5024\u306f Boolean \u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3067\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093"},

  
   /** src attribute not yet supported for  */


  {
    ER_COULD_NOT_FIND_EXTERN_SCRIPT,
         "{0} \u306b\u3042\u308b\u5916\u90e8\u30b9\u30af\u30ea\u30d7\u30c8\u3092\u5165\u624b\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},

  
  /** The resource [] could not be found     */


  {
    ER_RESOURCE_COULD_NOT_FIND,
        "\u30ea\u30bd\u30fc\u30b9 [ {0} ] \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f.\n {1}"},

  
   /** output property not recognized:  */


  {
    ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "\u51fa\u529b\u30d7\u30ed\u30d1\u30c6\u30a3\u304c\u8a8d\u3081\u3089\u308c\u307e\u305b\u3093: {0}"},

  
   /** Failed creating ElemLiteralResult instance   */


  {
    ER_FAILED_CREATING_ELEMLITRSLT,
        "ElemLiteralResult \u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u306e\u4f5c\u6210\u306b\u5931\u6557\u3057\u307e\u3057\u305f"},
    
  
   /** Priority value does not contain a parsable number   */


  {
    ER_VALUE_SHOULD_BE_NUMBER,
        "\u25bc{0} \u306e\u5024\u306b\u89e3\u6790\u53ef\u80fd\u306a\u6570\u5b57\u304c\u542b\u307e\u308c\u3066\u3044\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059"},

  
   /**  Value for {0} should equal 'yes' or 'no'   */


  {
    ER_VALUE_SHOULD_EQUAL,
        " {0} \u306e\u5024\u306f yes \u307e\u305f\u306f no \u306e\u3044\u305a\u308c\u304b\u3067\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093"},

 
   /**  Failed calling {0} method   */


  {
    ER_FAILED_CALLING_METHOD,
        " {0} \u30e1\u30bd\u30c3\u30c9\u306e\u547c\u3073\u51fa\u3057\u306b\u5931\u6557\u3057\u307e\u3057\u305f"},

  
   /** Failed creating ElemLiteralResult instance   */


  {
    ER_FAILED_CREATING_ELEMTMPL,
        "ElemTemplateElement \u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u306e\u4f5c\u6210\u306b\u5931\u6557\u3057\u307e\u3057\u305f"},

  
   /**  Characters are not allowed at this point in the document   */


  {
    ER_CHARS_NOT_ALLOWED,
        "\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u306e\u3053\u306e\u90e8\u5206\u3067\u3001\u6587\u5b57\u5217\u306f\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093"},

  
  /**  attribute is not allowed on the element   */


  {
    ER_ATTR_NOT_ALLOWED,
        "\"{0}\" \u5c5e\u6027\u306f {1} \u8981\u7d20\u3067\u8a31\u53ef\u3055\u308c\u307e\u305b\u3093\u3002"},


 
  /**  Bad value    */


  {
    ER_BAD_VALUE,
     "{0} \u8aa4\u3063\u305f\u5024 {1} "},

  
  /**  attribute value not found   */


  {
    ER_ATTRIB_VALUE_NOT_FOUND,
     "{0} \u5c5e\u6027\u5024\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093"},

  
  /**  attribute value not recognized    */


  {
    ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "{0} \u5c5e\u6027\u5024\u304c\u8a8d\u3081\u3089\u308c\u307e\u305b\u3093"},

  
    /** Attempting to generate a namespace prefix with a null URI   */


  {
    ER_NULL_URI_NAMESPACE,
     "null URI \u3092\u4f7f\u3063\u3066\u540d\u524d\u7a7a\u9593\u306e\u63a5\u982d\u8f9e\u3092\u751f\u6210\u3057\u3088\u3046\u3068\u3057\u3066\u3044\u307e\u3059"},
      

  
  /** Attempting to generate a namespace prefix with a null URI   */


  {
    ER_NUMBER_TOO_BIG,
     "\u25bcLong \u578b\u6574\u6570\u306e\u6700\u5927\u5024\u3092\u8d85\u3048\u308b\u6570\u5b57\u3092\u30d5\u30a9\u30fc\u30de\u30c3\u30c8\u3057\u3088\u3046\u3068\u3057\u3066\u3044\u307e\u3059"},





  {
    ER_CANNOT_FIND_SAX1_DRIVER,
     "\u25bcSAX1 \u30c9\u30e9\u30a4\u30d0\u30af\u30e9\u30b9 {0} \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093"},




  {
    ER_SAX1_DRIVER_NOT_LOADED,
     "\u25bcSAX1 \u30c9\u30e9\u30a4\u30d0\u30af\u30e9\u30b9 {0} \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u304c\u3001\u30ed\u30fc\u30c9\u3067\u304d\u307e\u305b\u3093"},




  {
    ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "\u25bcSAX1 \u30c9\u30e9\u30a4\u30d0\u30af\u30e9\u30b9 {0} \u304c\u30ed\u30fc\u30c9\u3055\u308c\u307e\u3057\u305f\u304c\u3001\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u5316\u3067\u304d\u307e\u305b\u3093"},





  {
    ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "\u25bcSAX1 \u30c9\u30e9\u30a4\u30d0\u30af\u30e9\u30b9 {0} \u304c org.xml.sax.Parser \u3092\u5b9f\u88c5\u3057\u3066\u3044\u307e\u305b\u3093"},




  {
    ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "\u25bc\u30b7\u30b9\u30c6\u30e0\u30d7\u30ed\u30d1\u30c6\u30a3 org.xml.sax.parser \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},




  {
    ER_PARSER_ARG_CANNOT_BE_NULL,
     "\u25bc\u30d1\u30fc\u30b5\u5c5e\u6027\u306f null \u306b\u3067\u304d\u307e\u305b\u3093"},





  {
    ER_FEATURE,
     "\u25bc\u6a5f\u80fd: {0}"},





  {
    ER_PROPERTY,
     "\u25bc\u30d7\u30ed\u30d1\u30c6\u30a3: {0}"},




  {
    ER_NULL_ENTITY_RESOLVER,
     "\u25bcnull \u30a8\u30f3\u30c6\u30a3\u30c6\u30a3\u30ea\u30be\u30eb\u30d0"},




  {
    ER_NULL_DTD_HANDLER,
     "\u25bcnull DTD \u30cf\u30f3\u30c9\u30e9"},



  {
    ER_NO_DRIVER_NAME_SPECIFIED,
     "\u25bc\u30c9\u30e9\u30a4\u30d0\u540d\u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u305b\u3093!"},




  {
    ER_NO_URL_SPECIFIED,
     "\u25bcURL \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u305b\u3093!"},




  {
    ER_POOLSIZE_LESS_THAN_ONE,
     "\u25bc\u30d7\u30fc\u30eb\u30b5\u30a4\u30ba\u304c 1 \u3088\u308a\u5c0f\u3055\u3044\u5024\u306b\u306a\u3063\u3066\u3044\u307e\u3059!"},




  {
    ER_INVALID_DRIVER_NAME,
     "\u25bc\u7121\u52b9\u306a\u30c9\u30e9\u30a4\u30d0\u540d\u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059!"},





  {
    ER_ERRORLISTENER,
     "\u25bc\u30a8\u30e9\u30fc\u30ea\u30b9\u30ca\u30fc"},




  {
    ER_ASSERT_NO_TEMPLATE_PARENT,
     "\u25bc\u30d7\u30ed\u30b0\u30e9\u30de\u30a8\u30e9\u30fc! \u5f0f\u306b ElemTemplateElement \u306e\u89aa\u304c\u542b\u307e\u308c\u3066\u3044\u307e\u305b\u3093!"},




  {
    ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "\u25bcRundundentExprEliminator \u306b\u304a\u3051\u308b\u30d7\u30ed\u30b0\u30e9\u30de\u306e\u8868\u660e: {0}"},



  {
    ER_NOT_ALLOWED_IN_POSITION,
     "\u25bc\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306e\u3053\u306e\u4f4d\u7f6e\u3067\u306f\u3001{0} \u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093!"},



  {
    ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "\u25bc\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306e\u3053\u306e\u4f4d\u7f6e\u3067\u306f\u3001\u7a7a\u767d\u985e\u4ee5\u5916\u306e\u30c6\u30ad\u30b9\u30c8\u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093!"},



  {
    INVALID_TCHAR,
     "\u25bcCHAR \u578b\u306e\u5c5e\u6027 {0} \u306b\u4e0d\u6b63\u306a\u5024 {1} \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059\u3002CHAR \u578b\u306e\u5c5e\u6027\u306b\u6307\u5b9a\u3067\u304d\u308b\u306e\u306f\u3001\u5358\u4e00\u306e\u6587\u5b57\u3060\u3051\u3067\u3059!"},




  {
    INVALID_QNAME,
     "\u25bcQNAME \u578b\u306e\u5c5e\u6027 {0} \u306b\u4e0d\u6b63\u306a\u5024 {1} \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059"},




  {
    INVALID_ENUM,
     "\u25bcENUM \u578b\u306e\u5c5e\u6027 {0} \u306b\u4e0d\u6b63\u306a\u5024 {1} \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059\u3002\u6709\u52b9\u306a\u5024\u306f {2} \u3067\u3059\u3002"},




  {
    INVALID_NMTOKEN,
     "\u25bcNMTOKEN \u578b\u306e\u5c5e\u6027 {0} \u306b\u4e0d\u6b63\u306a\u5024 {1} \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059"},




  {
    INVALID_NCNAME,
     "\u25bcNCNAME \u578b\u306e\u5c5e\u6027 {0} \u306b\u4e0d\u6b63\u306a {1} \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059"},





  {
    INVALID_BOOLEAN,
     "\u25bcboolean \u578b\u306e\u5c5e\u6027 {0} \u306b\u4e0d\u6b63\u306a {1} \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059"},




  {
    INVALID_NUMBER,
     "\u25bc\u6570\u5b57\u578b\u306e\u5c5e\u6027 {0} \u306b\u4e0d\u6b63\u306a {1} \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u3059"},






  {
    ER_ARG_LITERAL,
     "\u25bc\u30de\u30c3\u30c1\u30d1\u30bf\u30fc\u30f3\u5185\u306b\u542b\u307e\u308c\u308b {0} \u306e\u5f15\u6570\u306b\u306f\u3001\u30ea\u30c6\u30e9\u30eb\u3092\u6307\u5b9a\u3059\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002"},




  {
    ER_DUPLICATE_GLOBAL_VAR,
     "\u25bc\u30b0\u30ed\u30fc\u30d0\u30eb\u5909\u6570\u306e\u5ba3\u8a00\u304c\u91cd\u8907\u3057\u3066\u3044\u307e\u3059\u3002"},





  {
    ER_DUPLICATE_VAR,
     "\u25bc\u5909\u6570\u306e\u5ba3\u8a00\u304c\u91cd\u8907\u3057\u3066\u3044\u307e\u3059\u3002"},




  {
    ER_TEMPLATE_NAME_MATCH,
     "\u25bcxsl:template \u306b\u306f\u3001name \u5c5e\u6027\u3001match \u5c5e\u6027\u306e\u3044\u305a\u308c\u304b\u307e\u305f\u306f\u4e21\u65b9\u304c\u542b\u307e\u308c\u3066\u3044\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059"},




  {
    ER_INVALID_PREFIX,
     "\u25bcexclude-result-prefixes \u306e\u63a5\u982d\u8f9e\u304c\u6709\u52b9\u3067\u306f\u3042\u308a\u307e\u305b\u3093: {0}"},




  {
    ER_NO_ATTRIB_SET,
     "\u25bc{0} \u3068\u3044\u3046\u540d\u524d\u306e attribute-set \u304c\u5b58\u5728\u3057\u307e\u305b\u3093"},



  /** WG_FOUND_CURLYBRACE          */


  {
    WG_FOUND_CURLYBRACE,
      "'}' \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u304c\u3001\u5c5e\u6027\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u304c\u958b\u304b\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */


  {
    WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "\u8b66\u544a: count \u5c5e\u6027\u304c xsl:number \u5185\u306e\u7956\u5148\u3068\u4e00\u81f4\u3057\u307e\u305b\u3093\u3002 \u30bf\u30fc\u30b2\u30c3\u30c8 = {0}"},


  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */


  {
    WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "\u53e4\u3044\u69cb\u6587: 'expr' \u5c5e\u6027\u306e\u540d\u524d\u306f 'select' \u306b\u5909\u66f4\u3055\u308c\u3066\u3044\u307e\u3059\u3002"},


  /** WG_NO_LOCALE_IN_FORMATNUMBER          */


  {
    WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan \u306f format-number \u95a2\u6570\u5185\u306e\u30ed\u30b1\u30fc\u30eb\u540d\u3092\u307e\u3060\u51e6\u7406\u3057\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** WG_LOCALE_NOT_FOUND          */


  {
    WG_LOCALE_NOT_FOUND,
      "\u8b66\u544a: xml:lang={0} \u306e\u30ed\u30b1\u30fc\u30eb\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},


  /** WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "URL \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093: {0}"},


  /** WG_CANNOT_LOAD_REQUESTED_DOC          */


  {
    WG_CANNOT_LOAD_REQUESTED_DOC,
      "\u8981\u6c42\u3055\u308c\u305f\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u3092\u30ed\u30fc\u30c9\u3067\u304d\u307e\u305b\u3093: {0}"},


  /** WG_CANNOT_FIND_COLLATOR          */


  {
    WG_CANNOT_FIND_COLLATOR,
      "<sort xml:lang={0} \u306e Collator \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},


  /** WG_FUNCTIONS_SHOULD_USE_URL          */


  {
    WG_FUNCTIONS_SHOULD_USE_URL,
      "\u53e4\u3044\u69cb\u6587: \u95a2\u6570\u306e\u6307\u4ee4\u306f {0} \u306e URL \u3092\u4f7f\u7528\u3059\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059"},


  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */


  {
    WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "\u30a8\u30f3\u30b3\u30fc\u30c7\u30a3\u30f3\u30b0\u304c\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093: {0}\u3001UTF-8 \u3092\u4f7f\u7528"},


  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */


  {
    WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "\u30a8\u30f3\u30b3\u30fc\u30c7\u30a3\u30f3\u30b0\u304c\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093: {0}\u3001Java {1} \u3092\u4f7f\u7528"},


  /** WG_SPECIFICITY_CONFLICTS          */


  {
    WG_SPECIFICITY_CONFLICTS,
      "\u7279\u5b9a\u3067\u3042\u308b\u3079\u304d\u3082\u306e\u306e\u7af6\u5408\u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f: {0} \u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u3067\u6700\u5f8c\u306b\u898b\u3064\u304b\u3063\u305f\u3082\u306e\u304c\u4f7f\u7528\u3055\u308c\u307e\u3059\u3002"},


  /** WG_PARSING_AND_PREPARING          */


  {
    WG_PARSING_AND_PREPARING,
      "========= {0} \u306e\u69cb\u6587\u89e3\u6790\u304a\u3088\u3073\u6e96\u5099  =========="},


  /** WG_ATTR_TEMPLATE          */


  {
    WG_ATTR_TEMPLATE, "\u5c5e\u6027\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u3001{0}"},


  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */


  {
    WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "xsl:strip-space \u3068 xsl:preserve-space \u306e\u9593\u3067 match \u7af6\u5408\u3057\u307e\u3059"},


  /** WG_ATTRIB_NOT_HANDLED          */


  {
    WG_ATTRIB_NOT_HANDLED,
      "Xalan \u306f\u307e\u3060 {0} \u5c5e\u6027\u3092\u51e6\u7406\u3057\u3066\u3044\u307e\u305b\u3093\u3002"},


  /** WG_NO_DECIMALFORMAT_DECLARATION          */


  {
    WG_NO_DECIMALFORMAT_DECLARATION,
      "10 \u9032\u6570\u5f62\u5f0f\u306e\u5ba3\u8a00\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093: {0}"},


  /** WG_OLD_XSLT_NS          */


  {
    WG_OLD_XSLT_NS, "XSLT \u540d\u524d\u7a7a\u9593\u304c\u306a\u3044\u3001\u307e\u305f\u306f\u4e0d\u6b63\u3067\u3059\u3002"},


  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */


  {
    WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "\u30c7\u30d5\u30a9\u30eb\u30c8\u306e xsl:decimal-format \u5ba3\u8a00\u306f 1 \u3064\u3060\u3051\u8a31\u53ef\u3055\u308c\u307e\u3059\u3002"},


  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */


  {
    WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "xsl:decimal-format \u306e\u540d\u524d\u306f\u4e00\u610f\u3067\u306a\u304f\u3066\u306f\u306a\u308a\u307e\u305b\u3093\u3002\u540d\u524d \"{0}\" \u306f\u91cd\u8907\u3057\u3066\u3044\u307e\u3059\u3002"},


  /** WG_ILLEGAL_ATTRIBUTE          */


  {
    WG_ILLEGAL_ATTRIBUTE,
      "{0} \u306b\u4e0d\u5f53\u306a\u5c5e\u6027\u304c\u3042\u308a\u307e\u3059: {1}"},


  /** WG_COULD_NOT_RESOLVE_PREFIX          */


  {
    WG_COULD_NOT_RESOLVE_PREFIX,
      "\u540d\u524d\u7a7a\u9593\u306e\u63a5\u982d\u8f9e\u3092\u89e3\u6c7a\u3067\u304d\u307e\u305b\u3093: {0}\u3002 \u30ce\u30fc\u30c9\u306f\u7121\u8996\u3055\u308c\u307e\u3059\u3002"},


  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */


  {
    WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet \u306f 'version' \u5c5e\u6027\u304c\u5fc5\u8981\u3067\u3059\u3002"},


  /** WG_ILLEGAL_ATTRIBUTE_NAME          */


  {
    WG_ILLEGAL_ATTRIBUTE_NAME,
      "\u4e0d\u5f53\u306a\u5c5e\u6027\u540d: {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */


  {
    WG_ILLEGAL_ATTRIBUTE_VALUE,
      "\u5c5e\u6027 {0} \u306b\u4e0d\u5f53\u306a\u5024\u304c\u4f7f\u7528\u3055\u308c\u3066\u3044\u307e\u3059: {1}"},


  /** WG_EMPTY_SECOND_ARG          */


  {
    WG_EMPTY_SECOND_ARG,
      "document \u95a2\u6570\u306e 2 \u756a\u76ee\u306e\u5f15\u6570\u306e\u7d50\u679c\u306e\u30ce\u30fc\u30c9\u30bb\u30c3\u30c8\u304c\u7a7a\u3067\u3059\u3002\u6700\u521d\u306e\u5f15\u6570\u304c\u4f7f\u7528\u3055\u308c\u307e\u3059\u3002"},





  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */

  {
     WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "\u25bcxsl:processing-instruction \u306e 'name' \u5c5e\u6027\u3067\u306f\u3001'xml' \u3092\u5024\u3068\u3057\u3066\u6307\u5b9a\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093"},



  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */

  {
     WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "\u25bcxsl:processing-instruction \u306e ''name'' \u5c5e\u6027\u306b\u306f\u3001\u6709\u52b9\u306a NCName \u5024 {0} \u3092\u6307\u5b9a\u3059\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059"},



  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */

  {
    WG_ILLEGAL_ATTRIBUTE_POSITION,
      "\u25bc\u5b50\u30ce\u30fc\u30c9\u306e\u751f\u6210\u5f8c\u3084\u8981\u7d20\u306e\u751f\u6210\u524d\u306b\u5c5e\u6027 {0} \u3092\u8981\u7d20\u306b\u8ffd\u52a0\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093\u3002\u305d\u306e\u5c5e\u6027\u306f\u7121\u8996\u3055\u308c\u307e\u3059\u3002"},




  { "ui_language", "ja"},
  { "help_language", "ja"},
  { "language", "ja"},
    { "BAD_CODE",
      "createMessage \u306e\u30d1\u30e9\u30e1\u30fc\u30bf\u304c\u7bc4\u56f2\u5916\u3067\u3057\u305f"},
    { "FORMAT_FAILED",
      "messageFormat \u547c\u3073\u51fa\u3057\u3067\u4f8b\u5916\u304c\u30b9\u30ed\u30fc\u3055\u308c\u307e\u3057\u305f"},
    { "version", ">>>>>>> Xalan \u30d0\u30fc\u30b8\u30e7\u30f3 "},
    { "version2", "<<<<<<<"},
    { "yes", "\u306f\u3044"},
    { "line", "\u884c\u756a\u53f7"},
    { "column", "\u5217\u756a\u53f7"},
    { "xsldone", "XSLProcessor: \u7d42\u4e86"},
    { "xslProc_option",
    "Xalan-J \u30b3\u30de\u30f3\u30c9\u884c\u30d7\u30ed\u30bb\u30b9\u306e\u30af\u30e9\u30b9\u30aa\u30d7\u30b7\u30e7\u30f3:"},
    { "optionIN", "    -IN inputXMLURL"},
    { "optionXSL", "   [-XSL XSLTransformationURL]"},
    { "optionOUT", "   [-OUT outputFileName]"},
    { "optionLXCIN",
      "   [-LXCIN compiledStylesheetFileNameIn]"},
    { "optionLXCOUT",
      "   [-LXCOUT compiledStylesheetFileNameOutOut]"},
    { "optionPARSER",
      "   [-PARSER \u306f parser liaison \u306e\u30af\u30e9\u30b9\u540d\u3092\u5b8c\u5168\u4fee\u98fe\u3059\u308b]"},
    { "optionE",
    "   [-E (\u30a8\u30f3\u30c6\u30a3\u30c6\u30a3\u53c2\u7167\u3092\u62e1\u5f35\u3057\u306a\u3044)]"},
    { "optionV",
    "   [-E (\u30a8\u30f3\u30c6\u30a3\u30c6\u30a3\u53c2\u7167\u3092\u62e1\u5f35\u3057\u306a\u3044)]"},
    { "optionQC", "   [-QC (Quiet Pattern Conflicts Warnings)]"},
    { "optionQ", "   [-Q  (\u975e\u51fa\u529b\u30e2\u30fc\u30c9)]"},
    { "optionLF",
      "   [-LF (\u51fa\u529b\u306b\u306e\u307f\u6539\u884c\u3092\u4f7f\u7528\u3059\u308b {\u30c7\u30d5\u30a9\u30eb\u30c8\u306f CR/LF})]"},
    { "optionCR",
      "   [-CR (\u51fa\u529b\u306b\u306e\u307f\u30ad\u30e3\u30ea\u30c3\u30b8\u30ea\u30bf\u30fc\u30f3\u3092\u4f7f\u7528\u3059\u308b {\u30c7\u30d5\u30a9\u30eb\u30c8\u306f CR/LF})]"},
    { "optionESCAPE",
      "   [-ESCAPE (\u30a8\u30b9\u30b1\u30fc\u30d7\u3059\u308b\u6587\u5b57\u5217 {\u30c7\u30d5\u30a9\u30eb\u30c8\u306f <>&\"\'\\r\\n}]"},
    { "optionINDENT",
      "   [-INDENT (\u30a4\u30f3\u30c7\u30f3\u30c8\u306b\u8a2d\u5b9a\u3059\u308b\u7a7a\u767d\u6587\u5b57\u6570\u3092\u5236\u5fa1\u3059\u308b {\u30c7\u30d5\u30a9\u30eb\u30c8\u306f 0})]"},
    { "optionTT",
      "   [-TT (\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u304c\u547c\u3073\u51fa\u3055\u308c\u305f\u3068\u304d\u306b\u30c8\u30ec\u30fc\u30b9\u3059\u308b)]"},
    { "optionTG",
      "   [-TG (\u5404\u751f\u6210\u30a4\u30d9\u30f3\u30c8\u3092\u30c8\u30ec\u30fc\u30b9\u3059\u308b\u3002)]"},
    { "optionTS",
    "   [-TS (\u5404\u9078\u629e\u30a4\u30d9\u30f3\u30c8\u3092\u30c8\u30ec\u30fc\u30b9\u3059\u308b\u3002)]"},
    { "optionTTC",
      "   [-TTC (\u30c6\u30f3\u30d7\u30ec\u30fc\u30c8\u306e\u5b50\u304c\u51e6\u7406\u3055\u308c\u305f\u3068\u304d\u306b\u30c8\u30ec\u30fc\u30b9\u3059\u308b\u3002)]"},
    { "optionTCLASS",
      "   [-TCLASS (\u30c8\u30ec\u30fc\u30b9\u62e1\u5f35\u7528\u306e TraceListener \u30af\u30e9\u30b9\u3002)]"},
    { "optionVALIDATE",
      "   [-VALIDATE (\u59a5\u5f53\u6027\u691c\u67fb\u3092\u6709\u52b9\u306b\u3059\u308b\u304b\u3069\u3046\u304b\u3092\u8a2d\u5b9a\u3059\u308b\u3002\u30c7\u30d5\u30a9\u30eb\u30c8\u3067\u306f\u7121\u52b9\u3002)]"},
    { "optionEDUMP",
      "   [-EDUMP {\u30aa\u30d7\u30b7\u30e7\u30f3\u306e\u30d5\u30a1\u30a4\u30eb\u540d} (\u30a8\u30e9\u30fc\u767a\u751f\u6642\u306b\u30b9\u30bf\u30c3\u30af\u30c0\u30f3\u30d7\u3092\u5b9f\u884c\u3059\u308b\u3002)]"},
    { "optionXML",
      "   [-XML (XML \u30d5\u30a9\u30fc\u30de\u30c3\u30bf\u3092\u4f7f\u7528\u3057\u3066\u3001XML \u30d8\u30c3\u30c0\u3092\u8ffd\u52a0\u3059\u308b\u3002)]"},
    { "optionTEXT",
      "   [-TEXT (\u5358\u7d14\u306a Text \u30d5\u30a9\u30fc\u30de\u30c3\u30bf\u3092\u4f7f\u7528\u3059\u308b\u3002)]"},
    { "optionHTML",
    "   [-HTML (HTML \u30d5\u30a9\u30fc\u30de\u30c3\u30bf\u3092\u4f7f\u7528\u3059\u308b\u3002)]"},
    { "optionPARAM",
      "   [-PARAM \u540d\u524d\u5f0f (\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306e\u30d1\u30e9\u30e1\u30fc\u30bf\u3092\u8a2d\u5b9a)]"},
    { "noParsermsg1",
    "XSL \u30d7\u30ed\u30bb\u30b9\u306f\u6210\u529f\u3057\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},
    { "noParsermsg2",
    "** \u30d1\u30fc\u30b5\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f **"},
    { "noParsermsg3",
    "\u30af\u30e9\u30b9\u30d1\u30b9\u3092\u30c1\u30a7\u30c3\u30af\u3057\u3066\u304f\u3060\u3055\u3044\u3002"},
    { "noParsermsg4",
      "Java \u7528\u306b IBM \u306e XML \u30d1\u30fc\u30b5\u304c\u306a\u3044\u5834\u5408\u3001\u4ee5\u4e0b\u304b\u3089\u30c0\u30a6\u30f3\u30ed\u30fc\u30c9\u3067\u304d\u307e\u3059"},
    { "noParsermsg5",
    { "optionURIRESOLVER",
    "   [-URIRESOLVER \u30d5\u30eb\u30af\u30e9\u30b9\u540d (URI \u3092\u89e3\u6c7a\u3059\u308b\u5834\u5408\u306f URIResolver \u3092\u4f7f\u7528\u3059\u308b)]"},
    { "optionENTITYRESOLVER",
    "   [-ENTITYRESOLVER \u30d5\u30eb\u30af\u30e9\u30b9\u540d (\u30a8\u30f3\u30c6\u30a3\u30c6\u30a3\u3092\u89e3\u6c7a\u3059\u308b\u5834\u5408\u306f EntityResolver \u3092\u4f7f\u7528\u3059\u308b)]"},
    { "optionCONTENTHANDLER",
    "   [-CONTENTHANDLER \u30d5\u30eb\u30af\u30e9\u30b9\u540d (\u51fa\u529b\u3092\u76f4\u5217\u5316\u3059\u308b\u5834\u5408\u306f ContentHandler \u3092\u4f7f\u7528\u3059\u308b)]"},
    { "optionLINENUMBERS",
    "   [-L \u30bd\u30fc\u30b9\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u306b\u306f\u884c\u756a\u53f7\u3092\u4f7f\u7528\u3059\u308b]"},
		


    { "optionMEDIA",
    " \u25bc[-MEDIA mediaType (media \u5c5e\u6027\u3092\u4f7f\u7528\u3057\u3066\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u306b\u95a2\u9023\u4ed8\u3051\u3089\u308c\u305f\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u3092\u691c\u7d22\u3059\u308b\u3002)]"},
    { "optionFLAVOR",
    { "optionDIAG",
    " \u25bc[-DIAG (\u30c8\u30e9\u30f3\u30b9\u30d5\u30a9\u30fc\u30e0\u306b\u304b\u304b\u3063\u305f\u5408\u8a08\u6642\u9593 (\u30df\u30ea\u79d2) \u3092\u51fa\u529b\u3059\u308b\u3002)]"},
    { "optionINCREMENTAL",
    { "optionNOOPTIMIMIZE",
    { "optionRL",
    " \u25bc[-RL recursionlimit (\u30b9\u30bf\u30a4\u30eb\u30b7\u30fc\u30c8\u306e\u56de\u5e30\u306e\u6df1\u3055\u306b\u4e0a\u9650\u5024\u3092\u8a2d\u5b9a\u3059\u308b\u3002)]"},
    { "optionXO",
    " \u25bc[-XO [transletName] (\u751f\u6210\u3055\u308c\u305f\u30c8\u30e9\u30f3\u30b9\u30ec\u30c3\u30c8\u306e\u540d\u524d\u3092\u6307\u5b9a\u3059\u308b)]"},
    { "optionXD",
    " \u25bc[-XD destinationDirectory (\u30c8\u30e9\u30f3\u30b9\u30ec\u30c3\u30c8\u306e\u51fa\u529b\u5148\u30c7\u30a3\u30ec\u30af\u30c8\u30ea\u3092\u6307\u5b9a\u3059\u308b)]"},
    { "optionXJ",
    " \u25bc[-XJ jarfile (\u30c8\u30e9\u30f3\u30b9\u30ec\u30c3\u30c8\u306e\u30af\u30e9\u30b9\u3092 <jarfile> \u306b\u6307\u5b9a\u3055\u308c\u305f\u540d\u524d\u306e JAR \u30d5\u30a1\u30a4\u30eb\u306b\u30d1\u30c3\u30b1\u30fc\u30b8\u5316\u3059\u308b)]"},
    { "optionXP",
    " \u25bc[-XP package (\u751f\u6210\u3055\u308c\u305f\u3059\u3079\u3066\u306e\u30c8\u30e9\u30f3\u30b9\u30ec\u30c3\u30c8\u30af\u30e9\u30b9\u306b\u5bfe\u3059\u308b\u30d1\u30c3\u30b1\u30fc\u30b8\u540d\u306e\u63a5\u982d\u8f9e\u3092\u6307\u5b9a\u3059\u308b)]"}



  };


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "BAD_CODE";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** General error string.   */
  public static final String ERROR_STRING = "#error";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "Error: ";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "Warning: ";

  /** String to specify the XSLT module.  */
  public static final String XSL_HEADER = "XSLT ";

  /** String to specify the XML parser module.  */
  public static final String XML_HEADER = "XML ";

  /** I don't think this is used any more.
   * @deprecated  */
  public static final String QUERY_HEADER = "PATTERN ";

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
