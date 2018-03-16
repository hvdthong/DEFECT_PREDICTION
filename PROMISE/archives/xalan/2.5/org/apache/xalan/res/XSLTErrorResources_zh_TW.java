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

public class XSLTErrorResources_zh_TW extends XSLTErrorResources
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
      "\u932f\u8aa4\uff1a\u5728\u8868\u793a\u5f0f\u5167\u4e0d\u80fd\u6709 '{'"},


  /** ER_ILLEGAL_ATTRIBUTE          */


  {
    ER_ILLEGAL_ATTRIBUTE, "{0} \u542b\u6709\u4e0d\u6b63\u78ba\u5c6c\u6027\uff1a{1}"},


  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */


  {
    ER_NULL_SOURCENODE_APPLYIMPORTS,
      "\u5728 xsl:apply-imports \u4e2d\u7684 sourceNode \u70ba\u7a7a\u503c\uff01"},


  /** ER_CANNOT_ADD          */


  {
    ER_CANNOT_ADD, "\u7121\u6cd5\u5c07 {0} \u65b0\u589e\u81f3 {1}"},


  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */


  {
    ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "\u5728 handleApplyTemplatesInstruction \u4e2d\u7684 sourceNode \u70ba\u7a7a\u503c\uff01"},


  /** ER_NO_NAME_ATTRIB          */


  {
    ER_NO_NAME_ATTRIB, "{0} \u5fc5\u9808\u6709 name \u5c6c\u6027\u3002 "},


  /** ER_TEMPLATE_NOT_FOUND          */


  {
    ER_TEMPLATE_NOT_FOUND, "\u627e\u4e0d\u5230\u540d\u7a31\u70ba {0} \u7684\u7bc4\u672c"},


  /** ER_CANT_RESOLVE_NAME_AVT          */


  {
    ER_CANT_RESOLVE_NAME_AVT,
      "\u7121\u6cd5\u89e3\u8b6f xsl:call-template \u4e2d\u7684\u540d\u7a31 AVT\u3002"},


  /** ER_REQUIRES_ATTRIB          */


  {
    ER_REQUIRES_ATTRIB, "{0} \u9700\u8981\u5c6c\u6027\uff1a{1}"},


  /** ER_MUST_HAVE_TEST_ATTRIB          */


  {
    ER_MUST_HAVE_TEST_ATTRIB,
      "{0} \u5fc5\u9808\u6709 ''test'' \u5c6c\u6027\u3002"},


  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */


  {
    ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "level \u5c6c\u6027 {0} \u4e0a\u7684\u503c\u932f\u8aa4"},


  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */


  {
    ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "processing-instruction \u540d\u7a31\u4e0d\u5f97\u70ba 'xml'"},


  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */


  {
    ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "processing-instruction \u540d\u7a31\u5fc5\u9808\u662f\u6709\u6548\u7684 NCName\uff1a{0}"},


  /** ER_NEED_MATCH_ATTRIB          */


  {
    ER_NEED_MATCH_ATTRIB,
      "\u5982\u679c {0} \u6709\u6a21\u5f0f\u7684\u8a71\uff0c\u5247\u5b83\u5fc5\u9808\u6709 match \u5c6c\u6027\u3002"},


  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */


  {
    ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} \u9700\u8981 name \u6216 match \u5c6c\u6027\u3002"},


  /** ER_CANT_RESOLVE_NSPREFIX          */


  {
    ER_CANT_RESOLVE_NSPREFIX,
      "\u7121\u6cd5\u89e3\u8b6f\u540d\u7a31\u7a7a\u9593\u524d\u7f6e\uff1a{0}"},


  /** ER_ILLEGAL_VALUE          */


  {
    ER_ILLEGAL_VALUE, "xml:space \u542b\u6709\u4e0d\u5408\u898f\u5247\u7684\u503c\uff1a{0}"},


  /** ER_NO_OWNERDOC          */


  {
    ER_NO_OWNERDOC,
      "\u5b50\u9805\u7bc0\u9ede\u6c92\u6709\u64c1\u6709\u8005\u6587\u4ef6\uff01"},


  /** ER_ELEMTEMPLATEELEM_ERR          */


  {
    ER_ELEMTEMPLATEELEM_ERR, "ElemTemplateElement \u932f\u8aa4\uff1a{0}"},


  /** ER_NULL_CHILD          */


  {
    ER_NULL_CHILD, "\u5617\u8a66\u65b0\u589e\u7a7a\u7684\u5b50\u9805\uff01"},


  /** ER_NEED_SELECT_ATTRIB          */


  {
    ER_NEED_SELECT_ATTRIB, "{0} \u9700\u8981 select \u5c6c\u6027\u3002"},


  /** ER_NEED_TEST_ATTRIB          */


  {
    ER_NEED_TEST_ATTRIB,
      "xsl:when \u5fc5\u9808\u6709 'test' \u5c6c\u6027\u3002"},


  /** ER_NEED_NAME_ATTRIB          */


  {
    ER_NEED_NAME_ATTRIB,
      "xsl:with-param \u5fc5\u9808\u6709 'name' \u5c6c\u6027\u3002"},


  /** ER_NO_CONTEXT_OWNERDOC          */


  {
    ER_NO_CONTEXT_OWNERDOC,
      "\u4e0a\u4e0b\u6587\u4e0d\u542b\u64c1\u6709\u8005\u6587\u4ef6\uff01"},


  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */


  {
    ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "\u7121\u6cd5\u5efa\u7acb XML TransformerFactory Liaison\uff1a{0}"},


  /** ER_PROCESS_NOT_SUCCESSFUL          */


  {
    ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan: \u8655\u7406\u4e0d\u6210\u529f\u3002"},


  /** ER_NOT_SUCCESSFUL          */


  {
    ER_NOT_SUCCESSFUL, "Xalan: \u4e0d\u6210\u529f\u3002"},


  /** ER_ENCODING_NOT_SUPPORTED          */


  {
    ER_ENCODING_NOT_SUPPORTED, "\u4e0d\u652f\u63f4\u7de8\u78bc\uff1a{0}"},


  /** ER_COULD_NOT_CREATE_TRACELISTENER          */


  {
    ER_COULD_NOT_CREATE_TRACELISTENER,
      "\u7121\u6cd5\u5efa\u7acb TraceListener\uff1a{0}"},


  /** ER_KEY_REQUIRES_NAME_ATTRIB          */


  {
    ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key \u9700\u8981 'name' \u5c6c\u6027\uff01"},


  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


  {
    ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key \u9700\u8981 'match' \u5c6c\u6027\uff01"},


  /** ER_KEY_REQUIRES_USE_ATTRIB          */


  {
    ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key \u9700\u8981 'use' \u5c6c\u6027\uff01"},


  /** ER_REQUIRES_ELEMENTS_ATTRIB          */


  {
    ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} \u9700\u8981 ''elements'' \u5c6c\u6027\uff01"},


  /** ER_MISSING_PREFIX_ATTRIB          */


  {
    ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) {0} \u5c6c\u6027 ''prefix'' \u907a\u6f0f"},


  /** ER_BAD_STYLESHEET_URL          */


  {
    ER_BAD_STYLESHEET_URL, "\u6a23\u5f0f\u8868 URL \u932f\u8aa4\uff1a{0}"},


  /** ER_FILE_NOT_FOUND          */


  {
    ER_FILE_NOT_FOUND, "\u627e\u4e0d\u5230\u6a23\u5f0f\u8868\u6a94\u6848\uff1a{0}"},


  /** ER_IOEXCEPTION          */


  {
    ER_IOEXCEPTION,
      "\u6a23\u5f0f\u8868\u6a94\u6848 {0} \u6709\u8f38\u5165/\u8f38\u51fa (I/O) \u7570\u5e38"},


  /** ER_NO_HREF_ATTRIB          */


  {
    ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) \u627e\u4e0d\u5230 {0} \u7684 href \u5c6c\u6027"},


  /** ER_STYLESHEET_INCLUDES_ITSELF          */


  {
    ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} \u76f4\u63a5\u6216\u9593\u63a5\u5305\u542b\u672c\u8eab\uff01"},


  /** ER_PROCESSINCLUDE_ERROR          */


  {
    ER_PROCESSINCLUDE_ERROR,
      "StylesheetHandler.processInclude \u932f\u8aa4\uff1a{0}"},


  /** ER_MISSING_LANG_ATTRIB          */


  {
    ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) {0} \u5c6c\u6027 ''lang'' \u907a\u6f0f"},


  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */


  {
    ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) \u8aa4\u7f6e {0} \u5143\u7d20\uff1f\uff1f \u907a\u6f0f container \u5143\u7d20 ''component''"},


  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */


  {
    ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "\u53ea\u80fd\u8f38\u51fa\u81f3 Element\u3001DocumentFragment\u3001Document \u6216 PrintWriter\u3002"},


  /** ER_PROCESS_ERROR          */


  {
    ER_PROCESS_ERROR, "StylesheetRoot.process \u932f\u8aa4"},


  /** ER_UNIMPLNODE_ERROR          */


  {
    ER_UNIMPLNODE_ERROR, "UnImplNode \u932f\u8aa4\uff1a{0}"},


  /** ER_NO_SELECT_EXPRESSION          */


  {
    ER_NO_SELECT_EXPRESSION,
      "\u932f\u8aa4\uff01\u672a\u627e\u5230 xpath select \u8868\u793a\u5f0f (-select)\u3002"},


  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */


  {
    ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "\u7121\u6cd5\u4e32\u5217\u5316 XSLProcessor\uff01"},


  /** ER_NO_INPUT_STYLESHEET          */


  {
    ER_NO_INPUT_STYLESHEET,
      "\u672a\u6307\u5b9a\u6a23\u5f0f\u8868\u8f38\u5165\uff01"},


  /** ER_FAILED_PROCESS_STYLESHEET          */


  {
    ER_FAILED_PROCESS_STYLESHEET,
      "\u7121\u6cd5\u8655\u7406\u6a23\u5f0f\u8868\uff01"},


  /** ER_COULDNT_PARSE_DOC          */


  {
    ER_COULDNT_PARSE_DOC, "\u7121\u6cd5\u5256\u6790 {0} \u6587\u4ef6\uff01"},


  /** ER_COULDNT_FIND_FRAGMENT          */


  {
    ER_COULDNT_FIND_FRAGMENT, "\u627e\u4e0d\u5230\u7247\u6bb5\uff1a{0}"},


  /** ER_NODE_NOT_ELEMENT          */


  {
    ER_NODE_NOT_ELEMENT,
      "\u7247\u6bb5\u8b58\u5225\u78bc\u6240\u6307\u7684\u7bc0\u9ede\u4e0d\u662f\u5143\u7d20\uff1a{0}"},


  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */


  {
    ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each \u5fc5\u9808\u6709 match \u6216 name \u5c6c\u6027"},


  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */


  {
    ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "templates \u5fc5\u9808\u6709 match \u6216 name \u5c6c\u6027"},


  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */


  {
    ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "\u6587\u4ef6\u7247\u6bb5\u6c92\u6709\u8907\u672c\uff01"},


  /** ER_CANT_CREATE_ITEM          */


  {
    ER_CANT_CREATE_ITEM,
      "\u7121\u6cd5\u5728\u7d50\u679c\u6a39 {0} \u5efa\u7acb\u9805\u76ee"},


  /** ER_XMLSPACE_ILLEGAL_VALUE          */


  {
    ER_XMLSPACE_ILLEGAL_VALUE,
      "\u4f86\u6e90 XML \u4e2d\u7684 xml:space \u542b\u6709\u4e0d\u5408\u898f\u5247\u7684\u503c\uff1a{0}"},


  /** ER_NO_XSLKEY_DECLARATION          */


  {
    ER_NO_XSLKEY_DECLARATION,
      "{0} \u6c92\u6709 xsl:key \u5ba3\u544a\uff01"},


  /** ER_CANT_CREATE_URL          */


  {
    ER_CANT_CREATE_URL, "\u932f\u8aa4\uff01\u7121\u6cd5\u5efa\u7acb URL \u7d66\uff1a{0}"},


  /** ER_XSLFUNCTIONS_UNSUPPORTED          */


  {
    ER_XSLFUNCTIONS_UNSUPPORTED, "\u4e0d\u652f\u63f4 xsl:functions"},


  /** ER_PROCESSOR_ERROR          */


  {
    ER_PROCESSOR_ERROR, "XSLT TransformerFactory \u932f\u8aa4"},


  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */


  {
    ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} \u4e0d\u5141\u8a31\u5728\u6a23\u5f0f\u8868\u5167\uff01"},


  /** ER_RESULTNS_NOT_SUPPORTED          */


  {
    ER_RESULTNS_NOT_SUPPORTED,
      "\u4e0d\u518d\u652f\u63f4 result-ns\uff01\u8acb\u4f7f\u7528 xsl:output \u4f86\u4ee3\u66ff\u3002"},


  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */


  {
    ER_DEFAULTSPACE_NOT_SUPPORTED,
      "\u4e0d\u518d\u652f\u63f4 default-space\uff01\u8acb\u4f7f\u7528 xsl:strip-space \u6216 xsl:preserve-space \u4f86\u4ee3\u66ff\u3002"},


  /** ER_INDENTRESULT_NOT_SUPPORTED          */


  {
    ER_INDENTRESULT_NOT_SUPPORTED,
      "\u4e0d\u518d\u652f\u63f4 indent-result\uff01\u8acb\u4f7f\u7528 xsl:output \u4f86\u4ee3\u66ff\u3002"},


  /** ER_ILLEGAL_ATTRIB          */


  {
    ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} \u542b\u6709\u4e0d\u5408\u898f\u5247\u7684\u5c6c\u6027\uff1a{1}"},


  /** ER_UNKNOWN_XSL_ELEM          */


  {
    ER_UNKNOWN_XSL_ELEM, "XSL \u5143\u7d20\uff1a{0}"},


  /** ER_BAD_XSLSORT_USE          */


  {
    ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort \u53ea\u80fd\u8207 xsl:apply-templates \u6216 xsl:for-each \u4e00\u8d77\u4f7f\u7528\u3002"},


  /** ER_MISPLACED_XSLWHEN          */


  {
    ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) \u8aa4\u7f6e xsl:when\uff01"},


  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */


  {
    ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:when \u7684\u4e0a\u4ee3\u4e0d\u662f xsl:choose\uff01"},


  /** ER_MISPLACED_XSLOTHERWISE          */


  {
    ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) \u8aa4\u7f6e xsl:otherwise\uff01"},


  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */


  {
    ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:otherwise \u7684\u4e0a\u4ee3\u4e0d\u662f xsl:choose\uff01"},


  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */


  {
    ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} \u4e0d\u5141\u8a31\u5728\u7bc4\u672c\u5167\uff01"},


  /** ER_UNKNOWN_EXT_NS_PREFIX          */


  {
    ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) {0} \u5ef6\u4f38\u7a0b\u5f0f\u540d\u7a31\u7a7a\u9593\u524d\u7f6e {1} \u672a\u77e5"},


  /** ER_IMPORTS_AS_FIRST_ELEM          */


  {
    ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) Imports \u53ea\u80fd\u51fa\u73fe\u65bc\u6a23\u5f0f\u8868\u4e2d\u4f5c\u70ba\u7b2c\u4e00\u500b\u5143\u7d20\uff01"},


  /** ER_IMPORTING_ITSELF          */


  {
    ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} \u76f4\u63a5\u6216\u9593\u63a5\u532f\u5165\u672c\u8eab\uff01"},


  /** ER_XMLSPACE_ILLEGAL_VAL          */


  {
    ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) " + "xml:space \u542b\u6709\u4e0d\u5408\u898f\u5247\u7684\u503c\uff1a{0}"},


  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */


  {
    ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "processStylesheet \u4e0d\u6210\u529f\uff01"},


  /** ER_SAX_EXCEPTION          */


  {
    ER_SAX_EXCEPTION, "SAX \u7570\u5e38"},


  /** ER_XSLT_ERROR          */


  {
    ER_XSLT_ERROR, "XSLT \u932f\u8aa4"},


  /** ER_CURRENCY_SIGN_ILLEGAL          */


  {
    ER_CURRENCY_SIGN_ILLEGAL,
      "\u8ca8\u5e63\u7b26\u865f\u4e0d\u5141\u8a31\u5728\u683c\u5f0f\u578b\u6a23\u5b57\u4e32\u4e2d"},


  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */


  {
    ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "\u6a23\u5f0f\u8868 DOM \u4e0d\u652f\u63f4\u6587\u4ef6\u51fd\u5f0f\uff01"},


  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */


  {
    ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "\u7121\u6cd5\u89e3\u8b6f\u975e\u524d\u7f6e\u89e3\u6790\u5668\u7684\u524d\u7f6e\uff01"},


  /** ER_REDIRECT_COULDNT_GET_FILENAME          */


  {
    ER_REDIRECT_COULDNT_GET_FILENAME,
      "\u91cd\u65b0\u5c0e\u5411\u5ef6\u4f38\u7a0b\u5f0f\uff1a\u7121\u6cd5\u53d6\u5f97\u6a94\u6848\u540d\u7a31 - file \u6216 select \u5c6c\u6027\u5fc5\u9808\u50b3\u56de\u6709\u6548\u5b57\u4e32\u3002"},


  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */


  {
    ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "\u7121\u6cd5\u5728\u91cd\u65b0\u5c0e\u5411\u5ef6\u4f38\u7a0b\u5f0f\u4e2d\u5efa\u7acb FormatterListener\uff01"},


  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */


  {
    ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "exclude-result-prefixes \u4e2d\u7684\u524d\u7f6e\u7121\u6548\uff1a{0}"},


  /** ER_MISSING_NS_URI          */


  {
    ER_MISSING_NS_URI,
      "\u907a\u6f0f\u6307\u5b9a\u7684\u524d\u7f6e\u7684\u540d\u7a31\u7a7a\u9593 URI"},


  /** ER_MISSING_ARG_FOR_OPTION          */


  {
    ER_MISSING_ARG_FOR_OPTION,
      "\u907a\u6f0f\u9078\u9805\uff1a{0} \u7684\u5f15\u6578"},


  /** ER_INVALID_OPTION          */


  {
    ER_INVALID_OPTION, "\u7121\u6548\u7684\u9078\u9805\uff1a{0}"},


  /** ER_MALFORMED_FORMAT_STRING          */


  {
    ER_MALFORMED_FORMAT_STRING, "\u8b8a\u5f62\u7684\u683c\u5f0f\u5b57\u4e32\uff1a{0}"},


  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */


  {
    ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet \u9700\u8981 'version' \u5c6c\u6027\uff01"},


  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */


  {
    ER_ILLEGAL_ATTRIBUTE_VALUE,
      "\u5c6c\u6027\uff1a{0} \u542b\u6709\u4e0d\u6b63\u78ba\u503c\uff1a{1}"},


  /** ER_CHOOSE_REQUIRES_WHEN          */


  {
    ER_CHOOSE_REQUIRES_WHEN, "xsl:choose \u9700\u8981 xsl:when"},


  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */


  {
    ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports \u4e0d\u5141\u8a31\u5728 xsl:for-each \u4e2d"},


  /** ER_CANT_USE_DTM_FOR_OUTPUT          */


  {
    ER_CANT_USE_DTM_FOR_OUTPUT,
      "\u7121\u6cd5\u5c0d\u8f38\u51fa DOM \u7bc0\u9ede\u4f7f\u7528 DTMLiaison... \u6539\u70ba\u50b3\u9001 org.apache.xpath.DOM2Helper\uff01"},


  /** ER_CANT_USE_DTM_FOR_INPUT          */


  {
    ER_CANT_USE_DTM_FOR_INPUT,
      "\u7121\u6cd5\u5c0d\u8f38\u5165 DOM \u7bc0\u9ede\u4f7f\u7528 DTMLiaison... \u6539\u70ba\u50b3\u9001 org.apache.xpath.DOM2Helper\uff01"},


  /** ER_CALL_TO_EXT_FAILED          */


  {
    ER_CALL_TO_EXT_FAILED,
      "\u547c\u53eb\u5ef6\u4f38\u7a0b\u5f0f\u5143\u7d20\u5931\u6557\uff1a{0}"},


  /** ER_PREFIX_MUST_RESOLVE          */


  {
    ER_PREFIX_MUST_RESOLVE,
      "\u524d\u7f6e\u5fc5\u9808\u89e3\u8b6f\u70ba\u540d\u7a31\u7a7a\u9593\uff1a{0}"},


  /** ER_INVALID_UTF16_SURROGATE          */


  {
    ER_INVALID_UTF16_SURROGATE,
      "\u5075\u6e2c\u5230\u7121\u6548\u7684 UTF-16 \u4ee3\u7528\u54c1\uff1a{0} ?"},


  /** ER_XSLATTRSET_USED_ITSELF          */


  {
    ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} \u4f7f\u7528\u672c\u8eab\uff0c\u9019\u5c07\u9020\u6210\u7121\u7aae\u8ff4\u5708\u3002"},


  /** ER_CANNOT_MIX_XERCESDOM          */


  {
    ER_CANNOT_MIX_XERCESDOM,
      "\u7121\u6cd5\u6df7\u5408\u975e Xerces-DOM \u8f38\u5165\u8207 Xerces-DOM \u8f38\u51fa\uff01"},


  /** ER_TOO_MANY_LISTENERS          */


  {
    ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},


  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */


  {
    ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "\u5728 ElemTemplateElement.readObject\uff1a{0}"},


  /** ER_DUPLICATE_NAMED_TEMPLATE          */


  {
    ER_DUPLICATE_NAMED_TEMPLATE,
      "\u627e\u5230\u4e00\u500b\u4ee5\u4e0a\u53eb\u4f5c {0} \u7684\u7bc4\u672c"},


  /** ER_INVALID_KEY_CALL          */


  {
    ER_INVALID_KEY_CALL,
      "\u7121\u6548\u7684\u51fd\u5f0f\u547c\u53eb\uff1arecursive key() \u547c\u53eb\u4e0d\u88ab\u5141\u8a31"},

  
  /** Variable is referencing itself          */


  {
    ER_REFERENCING_ITSELF,
      "\u8b8a\u6578 {0} \u76f4\u63a5\u6216\u9593\u63a5\u53c3\u7167\u672c\u8eab\uff01"},

  
  /** Illegal DOMSource input          */


  {
    ER_ILLEGAL_DOMSOURCE_INPUT,
      "\u5c0d newTemplates \u7684 DOMSource \u800c\u8a00\uff0c\u8f38\u5165\u7bc0\u9ede\u4e0d\u5f97\u70ba\u7a7a\u503c\uff01"},

	
	/** Class not found for option         */


  {
    ER_CLASS_NOT_FOUND_FOR_OPTION,
			"\u627e\u4e0d\u5230\u9078\u9805 {0} \u7684\u985e\u5225\u6a94\u6848"},

	
	/** Required Element not found         */


  {
    ER_REQUIRED_ELEM_NOT_FOUND,
			"\u627e\u4e0d\u5230\u5fc5\u9700\u7684\u5143\u7d20\uff1a{0}"},

  
  /** InputStream cannot be null         */


  {
    ER_INPUT_CANNOT_BE_NULL,
			"InputStream \u4e0d\u5f97\u70ba\u7a7a\u503c"},

  
  /** URI cannot be null         */


  {
    ER_URI_CANNOT_BE_NULL,
			"URI \u4e0d\u5f97\u70ba\u7a7a\u503c"},

  
  /** File cannot be null         */


  {
    ER_FILE_CANNOT_BE_NULL,
			"\u6a94\u6848\u4e0d\u53ef\u70ba\u7a7a\u503c"},

  
   /** InputSource cannot be null         */


  {
    ER_SOURCE_CANNOT_BE_NULL,
			"InputSource \u4e0d\u53ef\u70ba\u7a7a\u503c"},

  
  /** Could not initialize BSF Manager        */


  {
    ER_CANNOT_INIT_BSFMGR,
			"\u7121\u6cd5\u8d77\u59cb\u8a2d\u5b9a BSF Manager"},

  
  /** Could not compile extension       */


  {
    ER_CANNOT_CMPL_EXTENSN,
			"\u7121\u6cd5\u7de8\u8b6f\u5ef6\u4f38\u7a0b\u5f0f"},

  
  /** Could not create extension       */


  {
    ER_CANNOT_CREATE_EXTENSN,
      "\u7121\u6cd5\u5efa\u7acb\u5ef6\u4f38\u7a0b\u5f0f {0}\uff0c\u56e0\u70ba\uff1a{1}"},

  
  /** Instance method call to method {0} requires an Object instance as first argument       */


  {
    ER_INSTANCE_MTHD_CALL_REQUIRES,
      "\u65b9\u6cd5 {0} \u7684\u5be6\u4f8b\u65b9\u6cd5\u547c\u53eb\u9700\u8981\u4e00\u500b\u7269\u4ef6\u5be6\u4f8b\u4f5c\u70ba\u7b2c\u4e00\u500b\u5f15\u6578"},

  
  /** Invalid element name specified       */


  {
    ER_INVALID_ELEMENT_NAME,
      "\u6307\u5b9a\u7684\u5143\u7d20\u540d\u7a31\u7121\u6548 {0}"},

  
   /** Element name method must be static      */


  {
    ER_ELEMENT_NAME_METHOD_STATIC,
      "\u5143\u7d20\u540d\u7a31\u65b9\u6cd5\u5fc5\u9808\u70ba\u975c\u614b {0}"},

  
   /** Extension function {0} : {1} is unknown      */


  {
    ER_EXTENSION_FUNC_UNKNOWN,
             "\u5ef6\u4f38\u7a0b\u5f0f\u51fd\u5f0f {0} : {1} \u672a\u77e5"},

  
   /** More than one best match for constructor for       */


  {
    ER_MORE_MATCH_CONSTRUCTOR,
             "{0} \u7684\u6700\u7b26\u5408\u5efa\u69cb\u5143\u4e0d\u6b62\u4e00\u500b"},

  
   /** More than one best match for method      */


  {
    ER_MORE_MATCH_METHOD,
             "\u6700\u7b26\u5408\u65b9\u6cd5 {0} \u7684\u4e0d\u6b62\u4e00\u500b"},

  
   /** More than one best match for element method      */


  {
    ER_MORE_MATCH_ELEMENT,
             "\u6700\u7b26\u5408\u5143\u7d20\u65b9\u6cd5 {0} \u7684\u4e0d\u6b62\u4e00\u500b"},

  
   /** Invalid context passed to evaluate       */


  {
    ER_INVALID_CONTEXT_PASSED,
             "\u50b3\u9001\u4f86\u8a55\u4f30 {0} \u7684\u4e0a\u4e0b\u6587\u7121\u6548"},

  
   /** Pool already exists       */


  {
    ER_POOL_EXISTS,
             "\u5132\u5b58\u6c60\u5df2\u5b58\u5728"},

  
   /** No driver Name specified      */


  {
    ER_NO_DRIVER_NAME,
             "\u672a\u6307\u5b9a\u9a45\u52d5\u7a0b\u5f0f\u540d\u7a31"},

  
   /** No URL specified     */


  {
    ER_NO_URL,
             "\u672a\u6307\u5b9a URL"},

  
   /** Pool size is less than one    */


  {
    ER_POOL_SIZE_LESSTHAN_ONE,
             "\u5132\u5b58\u6c60\u5927\u5c0f\u5c0f\u65bc 1\uff01"},

  
   /** Invalid driver name specified    */


  {
    ER_INVALID_DRIVER,
             "\u6307\u5b9a\u7684\u9a45\u52d5\u7a0b\u5f0f\u540d\u7a31\u7121\u6548\uff01"},

  
   /** Did not find the stylesheet root    */


  {
    ER_NO_STYLESHEETROOT,
             "\u627e\u4e0d\u5230 stylesheet \u6839\uff01"},

  
   /** Illegal value for xml:space     */


  {
    ER_ILLEGAL_XMLSPACE_VALUE,
         "xml:space \u7684\u503c\u4e0d\u6b63\u78ba"},

  
   /** processFromNode failed     */


  {
    ER_PROCESSFROMNODE_FAILED,
         "processFromNode \u5931\u6548"},

  
   /** The resource [] could not load:     */


  {
    ER_RESOURCE_COULD_NOT_LOAD,
        "\u7121\u6cd5\u8f09\u5165\u8cc7\u6e90 [ {0} ]\uff1a{1} \n {2} \t {3}"},

   
  
   /** Buffer size <=0     */


  {
    ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "\u7de9\u885d\u5340\u5927\u5c0f <=0"},

  
   /** Unknown error when calling extension    */


  {
    ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "\u547c\u53eb\u5ef6\u4f38\u7a0b\u5f0f\u6642\u767c\u751f\u672a\u77e5\u932f\u8aa4"},

  
   /** Prefix {0} does not have a corresponding namespace declaration    */


  {
    ER_NO_NAMESPACE_DECL,
        "\u524d\u7f6e {0} \u6c92\u6709\u5c0d\u61c9\u7684\u540d\u7a31\u7a7a\u9593\u5ba3\u544a"},

  
   /** Element content not allowed for lang=javaclass   */


  {
    ER_ELEM_CONTENT_NOT_ALLOWED,
        "lang=javaclass {0} \u4e0d\u5141\u8a31\u5143\u7d20\u5167\u5bb9"},
     
  
   /** Stylesheet directed termination   */


  {
    ER_STYLESHEET_DIRECTED_TERMINATION,
        "Stylesheet \u5f15\u5c0e\u7d42\u6b62"},

  
   /** 1 or 2   */


  {
    ER_ONE_OR_TWO,
        "1 \u6216 2"},

  
   /** 2 or 3   */


  {
    ER_TWO_OR_THREE,
        "2 \u6216 3"},

  
   /** Could not load {0} (check CLASSPATH), now using just the defaults   */


  {
    ER_COULD_NOT_LOAD_RESOURCE,
        "\u7121\u6cd5\u8f09\u5165 {0}\uff08\u6aa2\u67e5 CLASSPATH\uff09\uff0c\u73fe\u5728\u53ea\u4f7f\u7528\u9810\u8a2d\u503c"},

  
   /** Cannot initialize default templates   */


  {
    ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "\u7121\u6cd5\u8d77\u59cb\u8a2d\u5b9a\u9810\u8a2d\u7bc4\u672c"},

  
   /** Result should not be null   */


  {
    ER_RESULT_NULL,
        "\u7d50\u679c\u4e0d\u61c9\u8a72\u70ba\u7a7a\u503c"},

    
   /** Result could not be set   */


  {
    ER_RESULT_COULD_NOT_BE_SET,
        "\u7121\u6cd5\u8a2d\u5b9a\u7d50\u679c"},

  
   /** No output specified   */


  {
    ER_NO_OUTPUT_SPECIFIED,
        "\u672a\u6307\u5b9a\u8f38\u51fa"},

  
   /** Can't transform to a Result of type   */


  {
    ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "\u7121\u6cd5\u8f49\u63db\u6210\u985e\u578b {0} \u7684\u7d50\u679c"},

  
   /** Can't transform to a Source of type   */


  {
    ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "\u7121\u6cd5\u8f49\u63db\u985e\u578b {0} \u7684\u4f86\u6e90"},

  
   /** Null content handler  */


  {
    ER_NULL_CONTENT_HANDLER,
        "\u7a7a\u7684\u5167\u5bb9\u8655\u7406\u5668"},

  
   /** Null error handler  */


  {
    ER_NULL_ERROR_HANDLER,
        "\u7a7a\u7684\u932f\u8aa4\u8655\u7406\u5668"},

  
   /** parse can not be called if the ContentHandler has not been set */


  {
    ER_CANNOT_CALL_PARSE,
        "\u5982\u679c\u672a\u8a2d\u5b9a ContentHandler \u5247\u7121\u6cd5\u547c\u53eb\u5256\u6790"},

  
   /**  No parent for filter */


  {
    ER_NO_PARENT_FOR_FILTER,
        "\u904e\u6ffe\u5668\u6c92\u6709\u4e0a\u4ee3"},

  
  
   /**  No stylesheet found in: {0}, media */


  {
    ER_NO_STYLESHEET_IN_MEDIA,
         "\u5728 {0} media= {1} \u627e\u4e0d\u5230\u6a23\u5f0f\u8868"},

  
   /**  No xml-stylesheet PI found in */


  {
    ER_NO_STYLESHEET_PI,
         "\u5728 {0} \u4e2d\u6c92\u6709\u767c\u73fe XML \u6a23\u5f0f\u8868 PI"},

  
   /**  Not supported  */


  {
    ER_NOT_SUPPORTED,
       "\u4e0d\u652f\u63f4\uff1a{0}"},

  
   /**  Value for property {0} should be a Boolean instance  */


  {
    ER_PROPERTY_VALUE_BOOLEAN,
       "\u5167\u5bb9 {0} \u7684\u503c\u61c9\u8a72\u662f\u4e00\u500b\u5e03\u6797\u6848\u4f8b"},

  
   /** src attribute not yet supported for  */


  {
    ER_COULD_NOT_FIND_EXTERN_SCRIPT,
       "\u7121\u6cd5\u65bc {0} \u8655\u53d6\u5f97\u5916\u90e8\u6307\u4ee4\u96c6"},

  
  /** The resource [] could not be found     */


  {
    ER_RESOURCE_COULD_NOT_FIND,
        "\u627e\u4e0d\u5230\u8cc7\u6e90 [ {0} ]\u3002\n {1}"},

  
   /** output property not recognized:  */


  {
    ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "\u672a\u80fd\u8fa8\u8b58\u8f38\u51fa\u5167\u5bb9\uff1a{0}"},

  
   /** Failed creating ElemLiteralResult instance   */


  {
    ER_FAILED_CREATING_ELEMLITRSLT,
        "\u5efa\u7acb ElemLiteralResult \u6848\u4f8b\u5931\u6557"},
  
  

  
   /** Priority value does not contain a parsable number   */


  {
    ER_VALUE_SHOULD_BE_NUMBER,
        "{0} \u7684\u503c\u61c9\u5305\u542b\u53ef\u5256\u6790\u7684\u6578\u5b57"},

  
   /**  Value for {0} should equal 'yes' or 'no'   */


  {
    ER_VALUE_SHOULD_EQUAL,
        " {0} \u7684\u503c\u61c9\u7b49\u65bc yes \u6216 no"},

 
   /**  Failed calling {0} method   */


  {
    ER_FAILED_CALLING_METHOD,
        " \u547c\u53eb {0} \u65b9\u6cd5\u5931\u6557"},

  
   /** Failed creating ElemLiteralResult instance   */


  {
    ER_FAILED_CREATING_ELEMTMPL,
        "\u5efa\u7acb ElemTemplateElement \u6848\u4f8b\u5931\u6557"},

  
   /**  Characters are not allowed at this point in the document   */


  {
    ER_CHARS_NOT_ALLOWED,
        "\u6587\u4ef6\u7684\u9019\u500b\u5730\u65b9\u4e0d\u5141\u8a31\u5b57\u5143"},

  
  /**  attribute is not allowed on the element   */


  {
    ER_ATTR_NOT_ALLOWED,
        "{1} \u5143\u7d20\u4e0a\u4e0d\u5141\u8a31\u6709 \"{0}\" \u5c6c\u6027\uff01"},

 
  /**  Bad value    */


  {
    ER_BAD_VALUE,
     "{0} \u932f\u8aa4\u503c {1} "},

  
  /**  attribute value not found   */


  {
    ER_ATTRIB_VALUE_NOT_FOUND,
     "\u627e\u4e0d\u5230 {0} \u5c6c\u6027\u503c "},

  
  /**  attribute value not recognized    */


  {
    ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "\u4e0d\u80fd\u8fa8\u8b58 {0} \u5c6c\u6027\u503c "},

  
  /** Attempting to generate a namespace prefix with a null URI   */


  {
    ER_NULL_URI_NAMESPACE,
     "\u6b63\u5728\u5617\u8a66\u4f7f\u7528\u7a7a URI \u7522\u751f\u540d\u7a31\u7a7a\u9593\u524d\u7f6e"},
   
   
  
  /** Attempting to generate a namespace prefix with a null URI   */


  {
    ER_NUMBER_TOO_BIG,
     "\u8a66\u5716\u683c\u5f0f\u5316\u6bd4\u6700\u5927\u7684 Long \u6574\u6578\u9084\u8981\u5927\u7684\u6578\u5b57"},

  
  
  

  {
  ER_CANNOT_FIND_SAX1_DRIVER,
   "\u7121\u6cd5\u627e\u5230 SAX1 \u9a45\u52d5\u7a0b\u5f0f\u985e\u5225 {0}"},




  {
  ER_SAX1_DRIVER_NOT_LOADED,
   "\u5df2\u627e\u5230 SAX1 \u9a45\u52d5\u7a0b\u5f0f\u985e\u5225 {0}\uff0c\u4f46\u662f\u7121\u6cd5\u8f09\u5165"},




  {
    ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "\u5df2\u8f09\u5165 SAX1 \u9a45\u52d5\u7a0b\u5f0f\u985e\u5225 {0}\uff0c\u4f46\u662f\u7121\u6cd5\u5c07\u5176\u5be6\u4f8b\u5316"},




  
    {
     ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
      "SAX1 \u9a45\u52d5\u7a0b\u5f0f\u985e\u5225 {0} \u4e0d\u57f7\u884c org.xml.sax.Parser"},
  


  
    {
      ER_PARSER_PROPERTY_NOT_SPECIFIED,
       "\u672a\u6307\u5b9a\u7cfb\u7d71\u5c6c\u6027 org.xml.sax.parser"},
  


  
    {
      ER_PARSER_ARG_CANNOT_BE_NULL,
       "\u5256\u6790\u5668\u5f15\u6578\u4e0d\u80fd\u70ba\u7a7a"},
  



  
    {
      ER_FEATURE,
       "\u529f\u80fd\uff1aa {0}"},
  



  
    {
      ER_PROPERTY,
       "\u5c6c\u6027\uff1aa {0}"},
  
 

  
    {
      ER_NULL_ENTITY_RESOLVER,
      "\u7a7a\u5be6\u9ad4\u89e3\u6790\u5668"},
  


  
    {
      ER_NULL_DTD_HANDLER,
       "\u7a7a DTD \u8655\u7406\u7a0b\u5f0f"},
  

  
    {
      ER_NO_DRIVER_NAME_SPECIFIED,
       "\u672a\u6307\u5b9a\u9a45\u52d5\u7a0b\u5f0f\u540d\u7a31\uff01"},
  


  
    {
      ER_NO_URL_SPECIFIED,
       "\u672a\u6307\u5b9a URL\uff01"},
  


  
    {
      ER_POOLSIZE_LESS_THAN_ONE,
       "\u5132\u5b58\u5340\u5c0f\u65bc 1\uff01"},
  


  
    {
      ER_INVALID_DRIVER_NAME,
       "\u6307\u5b9a\u7684\u9a45\u52d5\u7a0b\u5f0f\u540d\u7a31\u7121\u6548\uff01"},
  



  
    {
      ER_ERRORLISTENER,
       "ErrorListener"},
  


  
    {
      ER_ASSERT_NO_TEMPLATE_PARENT,
       "\u7a0b\u5f0f\u8a2d\u8a08\u5e2b\u7684\u932f\u8aa4\uff01expr \u6c92\u6709 ElemTemplateElement \u7236\uff01"},
  


  
    {
      ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
       "\u7a0b\u5f0f\u8a2d\u8a08\u5e2b\u5728 RundundentExprEliminator \u4e2d\u7684\u5224\u65b7\uff1a{0}"},
  

  
    {
      ER_NOT_ALLOWED_IN_POSITION,
       "\u5728\u6a23\u5f0f\u8868\u4e2d\uff0c\u6b64\u4f4d\u7f6e\u4e0d\u5141\u8a31\u51fa\u73fe {0}\uff01"},
  

  
    {
      ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
       "\u5728\u6a23\u5f0f\u8868\u4e2d\uff0c\u6b64\u4f4d\u7f6e\u53ea\u80fd\u662f\u7a7a\u767d\uff01"},
  

  
    {
      INVALID_TCHAR,
       "\u4e0d\u6b63\u78ba\u7684\u503c\uff1a{1} \u88ab\u7528\u65bc CHAR \u5c6c\u6027\uff1a{0}\u3002  CHAR \u985e\u578b\u7684\u5c6c\u6027\u53ea\u80fd\u662f 1 \u500b\u5b57\u5143\uff01"},
  



  {
    INVALID_QNAME,
     "\u4e0d\u6b63\u78ba\u7684\u503c\uff1aa {1} \u88ab\u7528\u65bc QNAME \u5c6c\u6027\uff1aa {0}"},



 
   {
     INVALID_ENUM,
      "\u4e0d\u6b63\u78ba\u7684\u503c\uff1aa {1} \u88ab\u7528\u65bc ENUM \u5c6c\u6027\uff1aa {0}\u3002  \u6709\u6548\u503c\u70ba\uff1aa {2}\u3002"},
 


 
   {
     INVALID_NMTOKEN,
      "\u4e0d\u6b63\u78ba\u7684\u503c\uff1aa {1} \u88ab\u7528\u65bc NMTOKEN \u5c6c\u6027\uff1aa {0}"},
 



  {
    INVALID_NCNAME,
     "\u4e0d\u6b63\u78ba\u7684\u503c\uff1aa {1} \u88ab\u7528\u65bc NCNAME \u5c6c\u6027\uff1aa {0}"},


 


  {
    INVALID_BOOLEAN,
     "\u4e0d\u6b63\u78ba\u7684\u503c\uff1aa {1} \u88ab\u7528\u65bc boolean \u5c6c\u6027\uff1aa {0}"},




  {
    INVALID_NUMBER,
     "\u4e0d\u6b63\u78ba\u7684\u503c\uff1aa {1} \u88ab\u7528\u65bc number \u5c6c\u6027\uff1aa {0}"},





 
   {
     ER_ARG_LITERAL,
      "\u5728\u76f8\u7b26\u578b\u6a23\u4e2d {0} \u7684\u5f15\u6578\u5fc5\u9808\u662f\u6587\u5b57\u3002"},
 


 
   {
     ER_DUPLICATE_GLOBAL_VAR,
      "\u91cd\u8907\u7684\u5168\u57df\u8b8a\u6578\u5ba3\u544a\u3002"},
 



 
   {
     ER_DUPLICATE_VAR,
      "\u91cd\u8907\u7684\u8b8a\u6578\u5ba3\u544a\u3002"},
 



  {
    ER_TEMPLATE_NAME_MATCH,
     "xsl:template \u5fc5\u9808\u6709\u4e00\u500b\u540d\u7a31\u6216\u76f8\u7b26\u5c6c\u6027 (\u6216\u5169\u8005\u5747\u6709)"},



 
   {
     ER_INVALID_PREFIX,
      "exclude-result-prefixes \u4e2d\u7684\u524d\u7f6e\u7121\u6548\uff1aa {0}"},
 


   
     {
       ER_NO_ATTRIB_SET,
        "\u540d\u70ba {0} \u7684 attribute-set \u4e0d\u5b58\u5728"},
   


  /** WG_FOUND_CURLYBRACE          */


  {
    WG_FOUND_CURLYBRACE,
      "\u627e\u5230 '}' \u4f46\u6c92\u6709\u958b\u555f\u7684\u5c6c\u6027\u7bc4\u672c\uff01"},


  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */


  {
    WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "\u8b66\u544a\uff1acount \u5c6c\u6027\u4e0d\u7b26\u5408 xsl:number \u4e2d\u7684\u7956\u5148\u7bc0\u9ede\uff01\u76ee\u6a19 = {0}"},


  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */


  {
    WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "\u820a\u8a9e\u6cd5\uff1a'expr' \u5c6c\u6027\u7684\u540d\u7a31\u5df2\u8b8a\u66f4\u70ba 'select'\u3002"},


  /** WG_NO_LOCALE_IN_FORMATNUMBER          */


  {
    WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan \u5c1a\u672a\u8655\u7406 format-number \u51fd\u5f0f\u4e2d\u7684\u8a9e\u8a00\u74b0\u5883\u540d\u7a31\u3002"},


  /** WG_LOCALE_NOT_FOUND          */


  {
    WG_LOCALE_NOT_FOUND,
      "\u8b66\u544a\uff1a\u627e\u4e0d\u5230 xml:lang={0} \u7684\u8a9e\u8a00\u74b0\u5883"},


  /** WG_CANNOT_MAKE_URL_FROM          */


  {
    WG_CANNOT_MAKE_URL_FROM,
      "\u7121\u6cd5\u5f9e\uff1a {0} \u7522\u751f URL"},


  /** WG_CANNOT_LOAD_REQUESTED_DOC          */


  {
    WG_CANNOT_LOAD_REQUESTED_DOC,
      "\u7121\u6cd5\u8f09\u5165\u6240\u8981\u6c42\u7684\u6587\u4ef6\uff1a{0}"},


  /** WG_CANNOT_FIND_COLLATOR          */


  {
    WG_CANNOT_FIND_COLLATOR,
      "\u627e\u4e0d\u5230 <sort xml:lang={0} \u7684\u7406\u5e8f\u5668"},


  /** WG_FUNCTIONS_SHOULD_USE_URL          */


  {
    WG_FUNCTIONS_SHOULD_USE_URL,
      "\u820a\u8a9e\u6cd5\uff1a\u51fd\u5f0f\u6307\u4ee4\u61c9\u4f7f\u7528 URL {0}"},


  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */


  {
    WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "\u4e0d\u652f\u63f4\u7de8\u78bc\uff1a{0}\uff0c\u4f7f\u7528 UTF-8"},


  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */


  {
    WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "\u4e0d\u652f\u63f4\u7de8\u78bc\uff1a{0}\uff0c\u4f7f\u7528 Java {1}"},


  /** WG_SPECIFICITY_CONFLICTS          */


  {
    WG_SPECIFICITY_CONFLICTS,
      "\u627e\u5230\u5177\u9ad4\u885d\u7a81\uff1a{0} \u5c07\u4f7f\u7528\u5728\u6a23\u5f0f\u8868\u4e2d\u627e\u5230\u7684\u6700\u5f8c\u4e00\u500b\u3002"},


  /** WG_PARSING_AND_PREPARING          */


  {
    WG_PARSING_AND_PREPARING,
      "========= \u5256\u6790\u53ca\u6e96\u5099 {0} =========="},


  /** WG_ATTR_TEMPLATE          */


  {
    WG_ATTR_TEMPLATE, "Attr \u7bc4\u672c\uff0c{0}"},


  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */


  {
    WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "xsl:strip-space \u548c xsl:preserve-space \u4e4b\u9593\u767c\u751f\u7b26\u5408\u885d\u7a81"},


  /** WG_ATTRIB_NOT_HANDLED          */


  {
    WG_ATTRIB_NOT_HANDLED,
      "Xalan \u5c1a\u672a\u8655\u7406 {0} \u5c6c\u6027\uff01"},


  /** WG_NO_DECIMALFORMAT_DECLARATION          */


  {
    WG_NO_DECIMALFORMAT_DECLARATION,
      "\u627e\u4e0d\u5230\u5341\u9032\u4f4d\u683c\u5f0f\u7684\u5ba3\u544a\uff1a{0}"},


  /** WG_OLD_XSLT_NS          */


  {
    WG_OLD_XSLT_NS, "XSLT \u540d\u7a31\u7a7a\u9593\u907a\u6f0f\u6216\u4e0d\u6b63\u78ba\u3002"},


  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */


  {
    WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "\u50c5\u5141\u8a31\u4e00\u500b\u9810\u8a2d xsl:decimal-format \u5ba3\u544a\u3002"},


  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */


  {
    WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "xsl:decimal-format \u540d\u7a31\u5fc5\u9808\u662f\u552f\u4e00\u7684\u3002 \"{0}\" \u540d\u7a31\u91cd\u8907\u3002"},


  /** WG_ILLEGAL_ATTRIBUTE          */


  {
    WG_ILLEGAL_ATTRIBUTE,
      "{0} \u542b\u6709\u4e0d\u5408\u898f\u5247\u7684\u5c6c\u6027\uff1a{1}"},


  /** WG_COULD_NOT_RESOLVE_PREFIX          */


  {
    WG_COULD_NOT_RESOLVE_PREFIX,
      "\u7121\u6cd5\u89e3\u8b6f\u540d\u7a31\u7a7a\u9593\u524d\u7f6e\uff1a{0}\u3002\u7bc0\u9ede\u88ab\u5ffd\u7565\u3002"},


  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */


  {
    WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet \u9700\u8981 'version' \u5c6c\u6027\uff01"},


  /** WG_ILLEGAL_ATTRIBUTE_NAME          */


  {
    WG_ILLEGAL_ATTRIBUTE_NAME,
      "\u4e0d\u5408\u898f\u5247\u7684\u5c6c\u6027\u540d\u7a31\uff1a{0}"},


  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */


  {
    WG_ILLEGAL_ATTRIBUTE_VALUE,
      "\u5c6c\u6027 {0} \u4f7f\u7528\u4e86\u4e0d\u5408\u898f\u5247\u7684\u503c\uff1a{1}"},


  /** WG_EMPTY_SECOND_ARG          */


  {
    WG_EMPTY_SECOND_ARG,
      "\u5f9e\u6587\u4ef6\u51fd\u5f0f\u7b2c\u4e8c\u500b\u5f15\u6578\u7522\u751f\u7684\u7bc0\u9ede\u96c6\u662f\u7a7a\u503c\u3002\u5c07\u4f7f\u7528\u7b2c\u4e00\u500b\u5f15\u6578\u3002"},



 

  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */

  {
     WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
     "xsl:processing-instruction \u540d\u7a31\u4e4b 'name' \u5c6c\u6027\u7684\u503c\u4e0d\u80fd\u662f 'xml'"},

 
 
  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */

  {
     WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
     "xsl:processing-instruction \u4e4b ''name'' \u5c6c\u6027\u7684\u503c\u5fc5\u9808\u662f\u4e00\u500b\u6709\u6548\u7684 NCName\uff1aa {0}"},



  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */

  {
    WG_ILLEGAL_ATTRIBUTE_POSITION,
     "\u7121\u6cd5\u5728\u7522\u751f\u5b50\u7bc0\u9ede\u4e4b\u5f8c\u6216\u7522\u751f\u5143\u7d20\u4e4b\u524d\u52a0\u5165\u5c6c\u6027 {0}\u3002  \u5c6c\u6027\u5c07\u88ab\u5ffd\u7565\u3002"},


 
  { "ui_language", "zh_TW"},
  { "help_language", "zh_TW"},
  { "language", "zh_TW"},
    { "BAD_CODE",
      "createMessage \u7684\u53c3\u6578\u8d85\u51fa\u754c\u9650"},
    { "FORMAT_FAILED",
      "\u5728 messageFormat \u547c\u53eb\u671f\u9593\u4e1f\u51fa\u7570\u5e38"},
    { "version", ">>>>>>> Xalan \u7248\u672c "},
    {  "version2", "<<<<<<<"},
    { "yes", "\u662f"},
    { "line", "\u884c #"},
    { "column", "\u76f4\u6b04 #"},
    { "xsldone", "XSLProcessor: done"},
    { "xslProc_option",
    "Xalan-J \u6307\u4ee4\u884c Process \u985e\u5225\u9078\u9805\uff1a"},
    { "optionIN", "    -IN inputXMLURL"},
    { "optionXSL", "   [-XSL XSLTransformationURL]"},
    { "optionOUT", "   [-OUT outputFileName]"},
    { "optionLXCIN", "   [-LXCIN compiledStylesheetFileNameIn]"},
    { "optionLXCOUT",
      "   [-LXCOUT compiledStylesheetFileNameOutOut]"},
    { "optionPARSER",
      "   [-PARSER \u5256\u6790\u5668\u95dc\u806f\u5225\u540d\u7684\u5b8c\u6574\u540d\u7a31]"},
    { "optionE",
    "   [-E (\u4e0d\u5c55\u958b\u5be6\u9ad4\u53c3\u7167)]"},
    { "optionV",
    "   [-E (\u4e0d\u5c55\u958b\u5be6\u9ad4\u53c3\u7167)]"},
    { "optionQC",
      "   [-QC (\u7121\u8072\u578b\u6a23\u885d\u7a81\u8b66\u544a)]"},
    { "optionQ",
    "   [-Q  (\u7121\u8072\u6a21\u5f0f)]"},
    { "optionLF",
      "   [-LF (\u53ea\u5728\u8f38\u51fa\u4e0a\u4f7f\u7528\u63db\u884c {\u9810\u8a2d\u662f CR/LF})]"},
    { "optionCR",
      "   [-CR (\u53ea\u5728\u8f38\u51fa\u4e0a\u4f7f\u7528\u63db\u884c\u9375 {\u9810\u8a2d\u662f CR/LF})]"},
    { "optionESCAPE",
      "   [-ESCAPE (\u8981\u9038\u51fa\u7684\u5b57\u5143 {\u9810\u8a2d\u662f <>&\"\'\\r\\n})]"},
    { "optionINDENT",
      "   [-INDENT (\u63a7\u5236\u8981\u5167\u7e2e\u7684\u7a7a\u683c\u6578 {\u9810\u8a2d\u662f 0})]"},
    { "optionTT",
      "   [-TT (\u547c\u53eb\u6642\u8ffd\u8e64\u7bc4\u672c\u3002)]"},
    { "optionTG",
      "   [-TG (\u8ffd\u8e64\u6bcf\u4e00\u500b\u7522\u751f\u4e8b\u4ef6\u3002)]"},
    { "optionTS",
    "   [-TS (\u8ffd\u8e64\u6bcf\u4e00\u500b\u9078\u53d6\u4e8b\u4ef6\u3002)]"},
    { "optionTTC",
      "   [-TTC (\u8ffd\u8e64\u8655\u7406\u4e2d\u7684\u7bc4\u672c\u5b50\u9805\u3002)]"},
    {"optionTCLASS",
      "   [-TCLASS (\u8ffd\u8e64\u5ef6\u4f38\u7a0b\u5f0f\u7684 TraceListener \u985e\u5225\u3002)]"},
    { "optionVALIDATE",
      "   [-VALIDATE (\u8a2d\u5b9a\u662f\u5426\u767c\u751f\u9a57\u8b49\u3002\u4f9d\u9810\u8a2d\u9a57\u8b49\u662f\u95dc\u9589\u7684\u3002)]"},
    { "optionEDUMP",
      "   [-EDUMP {\u53ef\u9078\u7528\u7684\u6a94\u6848\u540d\u7a31} (\u767c\u751f\u932f\u8aa4\u6642\u57f7\u884c stackdump\u3002)]"},
    { "optionXML",
      "   [-XML (\u4f7f\u7528 XML \u683c\u5f0f\u88fd\u4f5c\u5668\u53ca\u65b0\u589e XML \u8868\u982d\u3002)]"},
    { "optionTEXT",
      "   [-TEXT (\u4f7f\u7528\u7c21\u5f0f\u6587\u5b57\u683c\u5f0f\u5316\u7a0b\u5f0f\u3002)]"},
    { "optionHTML",
    "   [-HTML (\u4f7f\u7528 HTML \u683c\u5f0f\u88fd\u4f5c\u5668\u3002)]"},
    { "optionPARAM",
      "   [-PARAM \u540d\u7a31\u8868\u793a\u5f0f (\u8a2d\u5b9a\u6a23\u5f0f\u8868\u53c3\u6578)]"},
    { "noParsermsg1",
    "XSL \u8655\u7406\u4e0d\u6210\u529f\u3002"},
    { "noParsermsg2",
    "** \u627e\u4e0d\u5230\u5256\u6790\u5668 **"},
    { "noParsermsg3",
    "\u8acb\u6aa2\u67e5\u985e\u5225\u8def\u5f91\u3002"},
    { "noParsermsg4",
      "\u5982\u679c\u60a8\u6c92\u6709 IBM \u7684 XML Parser for Java\uff0c\u53ef\u4e0b\u8f09\u81ea "},
    { "noParsermsg5",
    { "optionURIRESOLVER",
    "   [-URIRESOLVER \u5b8c\u6574\u7684\u985e\u5225\u540d\u7a31 (URIResolver \u7528\u4f86\u89e3\u8b6f URI)]"},
    { "optionENTITYRESOLVER",
    "   [-ENTITYRESOLVER \u5b8c\u6574\u7684\u985e\u5225\u540d\u7a31 (EntityResolver \u7528\u4f86\u89e3\u8b6f\u5be6\u9ad4)]"},
    { "optionCONTENTHANDLER",
    "   [-CONTENTHANDLER \u5b8c\u6574\u7684\u985e\u5225\u540d\u7a31 (ContentHandler \u7528\u4f86\u4e32\u5217\u5316\u8f38\u51fa)]"},
    { "optionLINENUMBERS",
    "   [-L \u4f7f\u7528\u539f\u59cb\u6587\u4ef6\u7684\u884c\u865f]"},
    


    { "optionMEDIA",
    " [-MEDIA mediaType (\u4f7f\u7528\u5a92\u9ad4\u5c6c\u6027\u5c0b\u627e\u8207\u6587\u4ef6\u95dc\u806f\u7684\u6a23\u5f0f\u8868\u3002)]"},
    { "optionFLAVOR",
    { "optionDIAG",
    " [-DIAG (\u5217\u5370\u8f49\u63db\u4f5c\u696d\u82b1\u8cbb\u7684\u7e3d\u6beb\u79d2\u6578\u3002)]"},
    { "optionINCREMENTAL",
    { "optionNOOPTIMIMIZE",
    { "optionRL",
    " [-RL \u5faa\u74b0\u9650\u5236 (\u5047\u8a2d\u5728\u6a23\u5f0f\u8868\u5faa\u74b0\u6df1\u5ea6\u4e0a\u6578\u5b57\u9650\u5236\u3002)]"},
    { "optionXO",
    " [-XO [transletName] (\u6307\u5b9a\u7522\u751f\u7684 translet \u540d\u7a31)]"},
    { "optionXD",
    " [-XD destinationDirectory (\u6307\u5b9a translet \u7684\u76ee\u6a19\u76ee\u9304)]"},
    { "optionXJ",
    " [-XJ jarfile (\u5c07 translet \u985e\u5225\u5c01\u88dd\u6210\u540d\u70ba <jarfile> \u7684 jar \u6a94)]"},
    { "optionXP",
    " [-XP package (\u6307\u5b9a\u6240\u6709 translet \u985e\u5225\u7684\u5c01\u88dd\u540d\u7a31\u524d\u7f6e)]"}

		
  };


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "BAD_CODE";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** General error string.   */
  public static final String ERROR_STRING = "#error";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "\u932f\u8aa4\uff1a";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "\u8b66\u544a\uff1a";

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
