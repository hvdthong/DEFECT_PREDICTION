package org.apache.xalan.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
public class XSLTErrorResources_es extends ListResourceBundle
{

/*
 * This file contains error and warning messages related to Xalan Error
 * Handling.
 *
 *  General notes to translators:
 *
 *  1) Xalan (or more properly, Xalan-interpretive) and XSLTC are names of
 *     components.
 *     XSLT is an acronym for "XML Stylesheet Language: Transformations".
 *     XSLTC is an acronym for XSLT Compiler.
 *
 *  2) A stylesheet is a description of how to transform an input XML document
 *     into a resultant XML document (or HTML document or text).  The
 *     stylesheet itself is described in the form of an XML document.
 *
 *  3) A template is a component of a stylesheet that is used to match a
 *     particular portion of an input document and specifies the form of the
 *     corresponding portion of the output document.
 *
 *  4) An element is a mark-up tag in an XML document; an attribute is a
 *     modifier on the tag.  For example, in <elem attr='val' attr2='val2'>
 *     "elem" is an element name, "attr" and "attr2" are attribute names with
 *     the values "val" and "val2", respectively.
 *
 *  5) A namespace declaration is a special attribute that is used to associate
 *     a prefix with a URI (the namespace).  The meanings of element names and
 *     attribute names that use that prefix are defined with respect to that
 *     namespace.
 *
 *  6) "Translet" is an invented term that describes the class file that
 *     results from compiling an XML stylesheet into a Java class.
 *
 *  7) XPath is a specification that describes a notation for identifying
 *     nodes in a tree-structured representation of an XML document.  An
 *     instance of that notation is referred to as an XPath expression.
 *
 */

  /** Maximum error messages, this is needed to keep track of the number of messages.    */
  public static final int MAX_CODE = 201;

  /** Maximum warnings, this is needed to keep track of the number of warnings.          */
  public static final int MAX_WARNING = 29;

  /** Maximum misc strings.   */
  public static final int MAX_OTHERS = 55;

  /** Maximum total warnings and error messages.          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;


  /*
   * Static variables
   */
  public static final String ER_NO_CURLYBRACE = "ER_NO_CURLYBRACE";;
  public static final String ER_ILLEGAL_ATTRIBUTE = "ER_ILLEGAL_ATTRIBUTE";
  public static final String ER_NULL_SOURCENODE_APPLYIMPORTS = "ER_NULL_SOURCENODE_APPLYIMPORTS";
  public static final String ER_CANNOT_ADD = "ER_CANNOT_ADD";
  public static final String ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES="ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES";
  public static final String ER_NO_NAME_ATTRIB = "ER_NO_NAME_ATTRIB";
  public static final String ER_TEMPLATE_NOT_FOUND = "ER_TEMPLATE_NOT_FOUND";
  public static final String ER_CANT_RESOLVE_NAME_AVT = "ER_CANT_RESOLVE_NAME_AVT";
  public static final String ER_REQUIRES_ATTRIB = "ER_REQUIRES_ATTRIB";
  public static final String ER_MUST_HAVE_TEST_ATTRIB = "ER_MUST_HAVE_TEST_ATTRIB";
  public static final String ER_BAD_VAL_ON_LEVEL_ATTRIB =
         "ER_BAD_VAL_ON_LEVEL_ATTRIB";
  public static final String ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML =
         "ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML";
  public static final String ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME =
         "ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME";
  public static final String ER_NEED_MATCH_ATTRIB = "ER_NEED_MATCH_ATTRIB";
  public static final String ER_NEED_NAME_OR_MATCH_ATTRIB =
         "ER_NEED_NAME_OR_MATCH_ATTRIB";
  public static final String ER_CANT_RESOLVE_NSPREFIX =
         "ER_CANT_RESOLVE_NSPREFIX";
  public static final String ER_ILLEGAL_VALUE = "ER_ILLEGAL_VALUE";
  public static final String ER_NO_OWNERDOC = "ER_NO_OWNERDOC";
  public static final String ER_ELEMTEMPLATEELEM_ERR ="ER_ELEMTEMPLATEELEM_ERR";
  public static final String ER_NULL_CHILD = "ER_NULL_CHILD";
  public static final String ER_NEED_SELECT_ATTRIB = "ER_NEED_SELECT_ATTRIB";
  public static final String ER_NEED_TEST_ATTRIB = "ER_NEED_TEST_ATTRIB";
  public static final String ER_NEED_NAME_ATTRIB = "ER_NEED_NAME_ATTRIB";
  public static final String ER_NO_CONTEXT_OWNERDOC = "ER_NO_CONTEXT_OWNERDOC";
  public static final String ER_COULD_NOT_CREATE_XML_PROC_LIAISON =
         "ER_COULD_NOT_CREATE_XML_PROC_LIAISON";
  public static final String ER_PROCESS_NOT_SUCCESSFUL =
         "ER_PROCESS_NOT_SUCCESSFUL";
  public static final String ER_NOT_SUCCESSFUL = "ER_NOT_SUCCESSFUL";
  public static final String ER_ENCODING_NOT_SUPPORTED =
         "ER_ENCODING_NOT_SUPPORTED";
  public static final String ER_COULD_NOT_CREATE_TRACELISTENER =
         "ER_COULD_NOT_CREATE_TRACELISTENER";
  public static final String ER_KEY_REQUIRES_NAME_ATTRIB =
         "ER_KEY_REQUIRES_NAME_ATTRIB";
  public static final String ER_KEY_REQUIRES_MATCH_ATTRIB =
         "ER_KEY_REQUIRES_MATCH_ATTRIB";
  public static final String ER_KEY_REQUIRES_USE_ATTRIB =
         "ER_KEY_REQUIRES_USE_ATTRIB";
  public static final String ER_REQUIRES_ELEMENTS_ATTRIB =
         "ER_REQUIRES_ELEMENTS_ATTRIB";
  public static final String ER_MISSING_PREFIX_ATTRIB =
         "ER_MISSING_PREFIX_ATTRIB";
  public static final String ER_BAD_STYLESHEET_URL = "ER_BAD_STYLESHEET_URL";
  public static final String ER_FILE_NOT_FOUND = "ER_FILE_NOT_FOUND";
  public static final String ER_IOEXCEPTION = "ER_IOEXCEPTION";
  public static final String ER_NO_HREF_ATTRIB = "ER_NO_HREF_ATTRIB";
  public static final String ER_STYLESHEET_INCLUDES_ITSELF =
         "ER_STYLESHEET_INCLUDES_ITSELF";
  public static final String ER_PROCESSINCLUDE_ERROR ="ER_PROCESSINCLUDE_ERROR";
  public static final String ER_MISSING_LANG_ATTRIB = "ER_MISSING_LANG_ATTRIB";
  public static final String ER_MISSING_CONTAINER_ELEMENT_COMPONENT =
         "ER_MISSING_CONTAINER_ELEMENT_COMPONENT";
  public static final String ER_CAN_ONLY_OUTPUT_TO_ELEMENT =
         "ER_CAN_ONLY_OUTPUT_TO_ELEMENT";
  public static final String ER_PROCESS_ERROR = "ER_PROCESS_ERROR";
  public static final String ER_UNIMPLNODE_ERROR = "ER_UNIMPLNODE_ERROR";
  public static final String ER_NO_SELECT_EXPRESSION ="ER_NO_SELECT_EXPRESSION";
  public static final String ER_CANNOT_SERIALIZE_XSLPROCESSOR =
         "ER_CANNOT_SERIALIZE_XSLPROCESSOR";
  public static final String ER_NO_INPUT_STYLESHEET = "ER_NO_INPUT_STYLESHEET";
  public static final String ER_FAILED_PROCESS_STYLESHEET =
         "ER_FAILED_PROCESS_STYLESHEET";
  public static final String ER_COULDNT_PARSE_DOC = "ER_COULDNT_PARSE_DOC";
  public static final String ER_COULDNT_FIND_FRAGMENT =
         "ER_COULDNT_FIND_FRAGMENT";
  public static final String ER_NODE_NOT_ELEMENT = "ER_NODE_NOT_ELEMENT";
  public static final String ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB =
         "ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB";
  public static final String ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB =
         "ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB";
  public static final String ER_NO_CLONE_OF_DOCUMENT_FRAG =
         "ER_NO_CLONE_OF_DOCUMENT_FRAG";
  public static final String ER_CANT_CREATE_ITEM = "ER_CANT_CREATE_ITEM";
  public static final String ER_XMLSPACE_ILLEGAL_VALUE =
         "ER_XMLSPACE_ILLEGAL_VALUE";
  public static final String ER_NO_XSLKEY_DECLARATION =
         "ER_NO_XSLKEY_DECLARATION";
  public static final String ER_CANT_CREATE_URL = "ER_CANT_CREATE_URL";
  public static final String ER_XSLFUNCTIONS_UNSUPPORTED =
         "ER_XSLFUNCTIONS_UNSUPPORTED";
  public static final String ER_PROCESSOR_ERROR = "ER_PROCESSOR_ERROR";
  public static final String ER_NOT_ALLOWED_INSIDE_STYLESHEET =
         "ER_NOT_ALLOWED_INSIDE_STYLESHEET";
  public static final String ER_RESULTNS_NOT_SUPPORTED =
         "ER_RESULTNS_NOT_SUPPORTED";
  public static final String ER_DEFAULTSPACE_NOT_SUPPORTED =
         "ER_DEFAULTSPACE_NOT_SUPPORTED";
  public static final String ER_INDENTRESULT_NOT_SUPPORTED =
         "ER_INDENTRESULT_NOT_SUPPORTED";
  public static final String ER_ILLEGAL_ATTRIB = "ER_ILLEGAL_ATTRIB";
  public static final String ER_UNKNOWN_XSL_ELEM = "ER_UNKNOWN_XSL_ELEM";
  public static final String ER_BAD_XSLSORT_USE = "ER_BAD_XSLSORT_USE";
  public static final String ER_MISPLACED_XSLWHEN = "ER_MISPLACED_XSLWHEN";
  public static final String ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE =
         "ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE";
  public static final String ER_MISPLACED_XSLOTHERWISE =
         "ER_MISPLACED_XSLOTHERWISE";
  public static final String ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE =
         "ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE";
  public static final String ER_NOT_ALLOWED_INSIDE_TEMPLATE =
         "ER_NOT_ALLOWED_INSIDE_TEMPLATE";
  public static final String ER_UNKNOWN_EXT_NS_PREFIX =
         "ER_UNKNOWN_EXT_NS_PREFIX";
  public static final String ER_IMPORTS_AS_FIRST_ELEM =
         "ER_IMPORTS_AS_FIRST_ELEM";
  public static final String ER_IMPORTING_ITSELF = "ER_IMPORTING_ITSELF";
  public static final String ER_XMLSPACE_ILLEGAL_VAL ="ER_XMLSPACE_ILLEGAL_VAL";
  public static final String ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL =
         "ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL";
  public static final String ER_SAX_EXCEPTION = "ER_SAX_EXCEPTION";
  public static final String ER_XSLT_ERROR = "ER_XSLT_ERROR";
  public static final String ER_CURRENCY_SIGN_ILLEGAL=
         "ER_CURRENCY_SIGN_ILLEGAL";
  public static final String ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM =
         "ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM";
  public static final String ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER =
         "ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER";
  public static final String ER_REDIRECT_COULDNT_GET_FILENAME =
         "ER_REDIRECT_COULDNT_GET_FILENAME";
  public static final String ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT =
         "ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT";
  public static final String ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX =
         "ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX";
  public static final String ER_MISSING_NS_URI = "ER_MISSING_NS_URI";
  public static final String ER_MISSING_ARG_FOR_OPTION =
         "ER_MISSING_ARG_FOR_OPTION";
  public static final String ER_INVALID_OPTION = "ER_INVALID_OPTION";
  public static final String ER_MALFORMED_FORMAT_STRING =
         "ER_MALFORMED_FORMAT_STRING";
  public static final String ER_STYLESHEET_REQUIRES_VERSION_ATTRIB =
         "ER_STYLESHEET_REQUIRES_VERSION_ATTRIB";
  public static final String ER_ILLEGAL_ATTRIBUTE_VALUE =
         "ER_ILLEGAL_ATTRIBUTE_VALUE";
  public static final String ER_CHOOSE_REQUIRES_WHEN ="ER_CHOOSE_REQUIRES_WHEN";
  public static final String ER_NO_APPLY_IMPORT_IN_FOR_EACH =
         "ER_NO_APPLY_IMPORT_IN_FOR_EACH";
  public static final String ER_CANT_USE_DTM_FOR_OUTPUT =
         "ER_CANT_USE_DTM_FOR_OUTPUT";
  public static final String ER_CANT_USE_DTM_FOR_INPUT =
         "ER_CANT_USE_DTM_FOR_INPUT";
  public static final String ER_CALL_TO_EXT_FAILED = "ER_CALL_TO_EXT_FAILED";
  public static final String ER_PREFIX_MUST_RESOLVE = "ER_PREFIX_MUST_RESOLVE";
  public static final String ER_INVALID_UTF16_SURROGATE =
         "ER_INVALID_UTF16_SURROGATE";
  public static final String ER_XSLATTRSET_USED_ITSELF =
         "ER_XSLATTRSET_USED_ITSELF";
  public static final String ER_CANNOT_MIX_XERCESDOM ="ER_CANNOT_MIX_XERCESDOM";
  public static final String ER_TOO_MANY_LISTENERS = "ER_TOO_MANY_LISTENERS";
  public static final String ER_IN_ELEMTEMPLATEELEM_READOBJECT =
         "ER_IN_ELEMTEMPLATEELEM_READOBJECT";
  public static final String ER_DUPLICATE_NAMED_TEMPLATE =
         "ER_DUPLICATE_NAMED_TEMPLATE";
  public static final String ER_INVALID_KEY_CALL = "ER_INVALID_KEY_CALL";
  public static final String ER_REFERENCING_ITSELF = "ER_REFERENCING_ITSELF";
  public static final String ER_ILLEGAL_DOMSOURCE_INPUT =
         "ER_ILLEGAL_DOMSOURCE_INPUT";
  public static final String ER_CLASS_NOT_FOUND_FOR_OPTION =
         "ER_CLASS_NOT_FOUND_FOR_OPTION";
  public static final String ER_REQUIRED_ELEM_NOT_FOUND =
         "ER_REQUIRED_ELEM_NOT_FOUND";
  public static final String ER_INPUT_CANNOT_BE_NULL ="ER_INPUT_CANNOT_BE_NULL";
  public static final String ER_URI_CANNOT_BE_NULL = "ER_URI_CANNOT_BE_NULL";
  public static final String ER_FILE_CANNOT_BE_NULL = "ER_FILE_CANNOT_BE_NULL";
  public static final String ER_SOURCE_CANNOT_BE_NULL =
         "ER_SOURCE_CANNOT_BE_NULL";
  public static final String ER_CANNOT_INIT_BSFMGR = "ER_CANNOT_INIT_BSFMGR";
  public static final String ER_CANNOT_CMPL_EXTENSN = "ER_CANNOT_CMPL_EXTENSN";
  public static final String ER_CANNOT_CREATE_EXTENSN =
         "ER_CANNOT_CREATE_EXTENSN";
  public static final String ER_INSTANCE_MTHD_CALL_REQUIRES =
         "ER_INSTANCE_MTHD_CALL_REQUIRES";
  public static final String ER_INVALID_ELEMENT_NAME ="ER_INVALID_ELEMENT_NAME";
  public static final String ER_ELEMENT_NAME_METHOD_STATIC =
         "ER_ELEMENT_NAME_METHOD_STATIC";
  public static final String ER_EXTENSION_FUNC_UNKNOWN =
         "ER_EXTENSION_FUNC_UNKNOWN";
  public static final String ER_MORE_MATCH_CONSTRUCTOR =
         "ER_MORE_MATCH_CONSTRUCTOR";
  public static final String ER_MORE_MATCH_METHOD = "ER_MORE_MATCH_METHOD";
  public static final String ER_MORE_MATCH_ELEMENT = "ER_MORE_MATCH_ELEMENT";
  public static final String ER_INVALID_CONTEXT_PASSED =
         "ER_INVALID_CONTEXT_PASSED";
  public static final String ER_POOL_EXISTS = "ER_POOL_EXISTS";
  public static final String ER_NO_DRIVER_NAME = "ER_NO_DRIVER_NAME";
  public static final String ER_NO_URL = "ER_NO_URL";
  public static final String ER_POOL_SIZE_LESSTHAN_ONE =
         "ER_POOL_SIZE_LESSTHAN_ONE";
  public static final String ER_INVALID_DRIVER = "ER_INVALID_DRIVER";
  public static final String ER_NO_STYLESHEETROOT = "ER_NO_STYLESHEETROOT";
  public static final String ER_ILLEGAL_XMLSPACE_VALUE =
         "ER_ILLEGAL_XMLSPACE_VALUE";
  public static final String ER_PROCESSFROMNODE_FAILED =
         "ER_PROCESSFROMNODE_FAILED";
  public static final String ER_RESOURCE_COULD_NOT_LOAD =
         "ER_RESOURCE_COULD_NOT_LOAD";
  public static final String ER_BUFFER_SIZE_LESSTHAN_ZERO =
         "ER_BUFFER_SIZE_LESSTHAN_ZERO";
  public static final String ER_UNKNOWN_ERROR_CALLING_EXTENSION =
         "ER_UNKNOWN_ERROR_CALLING_EXTENSION";
  public static final String ER_NO_NAMESPACE_DECL = "ER_NO_NAMESPACE_DECL";
  public static final String ER_ELEM_CONTENT_NOT_ALLOWED =
         "ER_ELEM_CONTENT_NOT_ALLOWED";
  public static final String ER_STYLESHEET_DIRECTED_TERMINATION =
         "ER_STYLESHEET_DIRECTED_TERMINATION";
  public static final String ER_ONE_OR_TWO = "ER_ONE_OR_TWO";
  public static final String ER_TWO_OR_THREE = "ER_TWO_OR_THREE";
  public static final String ER_COULD_NOT_LOAD_RESOURCE =
         "ER_COULD_NOT_LOAD_RESOURCE";
  public static final String ER_CANNOT_INIT_DEFAULT_TEMPLATES =
         "ER_CANNOT_INIT_DEFAULT_TEMPLATES";
  public static final String ER_RESULT_NULL = "ER_RESULT_NULL";
  public static final String ER_RESULT_COULD_NOT_BE_SET =
         "ER_RESULT_COULD_NOT_BE_SET";
  public static final String ER_NO_OUTPUT_SPECIFIED = "ER_NO_OUTPUT_SPECIFIED";
  public static final String ER_CANNOT_TRANSFORM_TO_RESULT_TYPE =
         "ER_CANNOT_TRANSFORM_TO_RESULT_TYPE";
  public static final String ER_CANNOT_TRANSFORM_SOURCE_TYPE =
         "ER_CANNOT_TRANSFORM_SOURCE_TYPE";
  public static final String ER_NULL_CONTENT_HANDLER ="ER_NULL_CONTENT_HANDLER";
  public static final String ER_NULL_ERROR_HANDLER = "ER_NULL_ERROR_HANDLER";
  public static final String ER_CANNOT_CALL_PARSE = "ER_CANNOT_CALL_PARSE";
  public static final String ER_NO_PARENT_FOR_FILTER ="ER_NO_PARENT_FOR_FILTER";
  public static final String ER_NO_STYLESHEET_IN_MEDIA =
         "ER_NO_STYLESHEET_IN_MEDIA";
  public static final String ER_NO_STYLESHEET_PI = "ER_NO_STYLESHEET_PI";
  public static final String ER_NOT_SUPPORTED = "ER_NOT_SUPPORTED";
  public static final String ER_PROPERTY_VALUE_BOOLEAN =
         "ER_PROPERTY_VALUE_BOOLEAN";
  public static final String ER_COULD_NOT_FIND_EXTERN_SCRIPT =
         "ER_COULD_NOT_FIND_EXTERN_SCRIPT";
  public static final String ER_RESOURCE_COULD_NOT_FIND =
         "ER_RESOURCE_COULD_NOT_FIND";
  public static final String ER_OUTPUT_PROPERTY_NOT_RECOGNIZED =
         "ER_OUTPUT_PROPERTY_NOT_RECOGNIZED";
  public static final String ER_FAILED_CREATING_ELEMLITRSLT =
         "ER_FAILED_CREATING_ELEMLITRSLT";
  public static final String ER_VALUE_SHOULD_BE_NUMBER =
         "ER_VALUE_SHOULD_BE_NUMBER";
  public static final String ER_VALUE_SHOULD_EQUAL = "ER_VALUE_SHOULD_EQUAL";
  public static final String ER_FAILED_CALLING_METHOD =
         "ER_FAILED_CALLING_METHOD";
  public static final String ER_FAILED_CREATING_ELEMTMPL =
         "ER_FAILED_CREATING_ELEMTMPL";
  public static final String ER_CHARS_NOT_ALLOWED = "ER_CHARS_NOT_ALLOWED";
  public static final String ER_ATTR_NOT_ALLOWED = "ER_ATTR_NOT_ALLOWED";
  public static final String ER_BAD_VALUE = "ER_BAD_VALUE";
  public static final String ER_ATTRIB_VALUE_NOT_FOUND =
         "ER_ATTRIB_VALUE_NOT_FOUND";
  public static final String ER_ATTRIB_VALUE_NOT_RECOGNIZED =
         "ER_ATTRIB_VALUE_NOT_RECOGNIZED";
  public static final String ER_NULL_URI_NAMESPACE = "ER_NULL_URI_NAMESPACE";
  public static final String ER_NUMBER_TOO_BIG = "ER_NUMBER_TOO_BIG";
  public static final String  ER_CANNOT_FIND_SAX1_DRIVER =
         "ER_CANNOT_FIND_SAX1_DRIVER";
  public static final String  ER_SAX1_DRIVER_NOT_LOADED =
         "ER_SAX1_DRIVER_NOT_LOADED";
  public static final String  ER_SAX1_DRIVER_NOT_INSTANTIATED =
         "ER_SAX1_DRIVER_NOT_INSTANTIATED" ;
  public static final String ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER =
         "ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER";
  public static final String  ER_PARSER_PROPERTY_NOT_SPECIFIED =
         "ER_PARSER_PROPERTY_NOT_SPECIFIED";
  public static final String  ER_PARSER_ARG_CANNOT_BE_NULL =
         "ER_PARSER_ARG_CANNOT_BE_NULL" ;
  public static final String  ER_FEATURE = "ER_FEATURE";
  public static final String ER_PROPERTY = "ER_PROPERTY" ;
  public static final String ER_NULL_ENTITY_RESOLVER ="ER_NULL_ENTITY_RESOLVER";
  public static final String  ER_NULL_DTD_HANDLER = "ER_NULL_DTD_HANDLER" ;
  public static final String ER_NO_DRIVER_NAME_SPECIFIED =
         "ER_NO_DRIVER_NAME_SPECIFIED";
  public static final String ER_NO_URL_SPECIFIED = "ER_NO_URL_SPECIFIED";
  public static final String ER_POOLSIZE_LESS_THAN_ONE =
         "ER_POOLSIZE_LESS_THAN_ONE";
  public static final String ER_INVALID_DRIVER_NAME = "ER_INVALID_DRIVER_NAME";
  public static final String ER_ERRORLISTENER = "ER_ERRORLISTENER";
  public static final String ER_ASSERT_NO_TEMPLATE_PARENT =
         "ER_ASSERT_NO_TEMPLATE_PARENT";
  public static final String ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR =
         "ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR";
  public static final String ER_NOT_ALLOWED_IN_POSITION =
         "ER_NOT_ALLOWED_IN_POSITION";
  public static final String ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION =
         "ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION";
  public static final String INVALID_TCHAR = "INVALID_TCHAR";
  public static final String INVALID_QNAME = "INVALID_QNAME";
  public static final String INVALID_ENUM = "INVALID_ENUM";
  public static final String INVALID_NMTOKEN = "INVALID_NMTOKEN";
  public static final String INVALID_NCNAME = "INVALID_NCNAME";
  public static final String INVALID_BOOLEAN = "INVALID_BOOLEAN";
  public static final String INVALID_NUMBER = "INVALID_NUMBER";
  public static final String ER_ARG_LITERAL = "ER_ARG_LITERAL";
  public static final String ER_DUPLICATE_GLOBAL_VAR ="ER_DUPLICATE_GLOBAL_VAR";
  public static final String ER_DUPLICATE_VAR = "ER_DUPLICATE_VAR";
  public static final String ER_TEMPLATE_NAME_MATCH = "ER_TEMPLATE_NAME_MATCH";
  public static final String ER_INVALID_PREFIX = "ER_INVALID_PREFIX";
  public static final String ER_NO_ATTRIB_SET = "ER_NO_ATTRIB_SET";

  public static final String WG_FOUND_CURLYBRACE = "WG_FOUND_CURLYBRACE";
  public static final String WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR =
         "WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR";
  public static final String WG_EXPR_ATTRIB_CHANGED_TO_SELECT =
         "WG_EXPR_ATTRIB_CHANGED_TO_SELECT";
  public static final String WG_NO_LOCALE_IN_FORMATNUMBER =
         "WG_NO_LOCALE_IN_FORMATNUMBER";
  public static final String WG_LOCALE_NOT_FOUND = "WG_LOCALE_NOT_FOUND";
  public static final String WG_CANNOT_MAKE_URL_FROM ="WG_CANNOT_MAKE_URL_FROM";
  public static final String WG_CANNOT_LOAD_REQUESTED_DOC =
         "WG_CANNOT_LOAD_REQUESTED_DOC";
  public static final String WG_CANNOT_FIND_COLLATOR ="WG_CANNOT_FIND_COLLATOR";
  public static final String WG_FUNCTIONS_SHOULD_USE_URL =
         "WG_FUNCTIONS_SHOULD_USE_URL";
  public static final String WG_ENCODING_NOT_SUPPORTED_USING_UTF8 =
         "WG_ENCODING_NOT_SUPPORTED_USING_UTF8";
  public static final String WG_ENCODING_NOT_SUPPORTED_USING_JAVA =
         "WG_ENCODING_NOT_SUPPORTED_USING_JAVA";
  public static final String WG_SPECIFICITY_CONFLICTS =
         "WG_SPECIFICITY_CONFLICTS";
  public static final String WG_PARSING_AND_PREPARING =
         "WG_PARSING_AND_PREPARING";
  public static final String WG_ATTR_TEMPLATE = "WG_ATTR_TEMPLATE";
  public static final String WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE = "WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESP";
  public static final String WG_ATTRIB_NOT_HANDLED = "WG_ATTRIB_NOT_HANDLED";
  public static final String WG_NO_DECIMALFORMAT_DECLARATION =
         "WG_NO_DECIMALFORMAT_DECLARATION";
  public static final String WG_OLD_XSLT_NS = "WG_OLD_XSLT_NS";
  public static final String WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED =
         "WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED";
  public static final String WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE =
         "WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE";
  public static final String WG_ILLEGAL_ATTRIBUTE = "WG_ILLEGAL_ATTRIBUTE";
  public static final String WG_COULD_NOT_RESOLVE_PREFIX =
         "WG_COULD_NOT_RESOLVE_PREFIX";
  public static final String WG_STYLESHEET_REQUIRES_VERSION_ATTRIB =
         "WG_STYLESHEET_REQUIRES_VERSION_ATTRIB";
  public static final String WG_ILLEGAL_ATTRIBUTE_NAME =
         "WG_ILLEGAL_ATTRIBUTE_NAME";
  public static final String WG_ILLEGAL_ATTRIBUTE_VALUE =
         "WG_ILLEGAL_ATTRIBUTE_VALUE";
  public static final String WG_EMPTY_SECOND_ARG = "WG_EMPTY_SECOND_ARG";
  public static final String WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML =
         "WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML";
  public static final String WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME =
         "WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME";
  public static final String WG_ILLEGAL_ATTRIBUTE_POSITION =
         "WG_ILLEGAL_ATTRIBUTE_POSITION";


  /*
   * Now fill in the message text.
   * Then fill in the message text for that message code in the
   * array. Use the new error code as the index into the array.
   */


  /** The lookup table for error messages.   */
  public static final Object[][] contents = {

  /** Error message ID that has a null message, but takes in a single object.    */
  {"ER0000" , "{0}" },


  /** ER_NO_CURLYBRACE          */


    { ER_NO_CURLYBRACE,
      "Error: No puede haber '{' dentro de la expresi\u00f3n"},

  /** ER_ILLEGAL_ATTRIBUTE          */

    { ER_ILLEGAL_ATTRIBUTE ,
     "{0} tiene un atributo no permitido: {1}"},

  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */

  {ER_NULL_SOURCENODE_APPLYIMPORTS ,
      "\u00a1sourceNode es nulo en xsl:apply-imports!"},

  /** ER_CANNOT_ADD          */

  {ER_CANNOT_ADD,
      "No se puede a\u00f1adir {0} a {1}"},


  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */


    { ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "\u00a1sourceNode es nulo en handleApplyTemplatesInstruction!"},

  /** ER_NO_NAME_ATTRIB          */


    { ER_NO_NAME_ATTRIB,
     "{0} debe tener un atributo de nombre."},

  /** ER_TEMPLATE_NOT_FOUND          */


    {ER_TEMPLATE_NOT_FOUND,
     "No se ha podido encontrar la plantilla: {0}"},

  /** ER_CANT_RESOLVE_NAME_AVT          */

    {ER_CANT_RESOLVE_NAME_AVT,
      "No se ha podido resolver AVT de nombre en xsl:call-template."},

  /** ER_REQUIRES_ATTRIB          */


    {ER_REQUIRES_ATTRIB,
     "{0} necesita un atributo: {1}"},

  /** ER_MUST_HAVE_TEST_ATTRIB          */


    { ER_MUST_HAVE_TEST_ATTRIB,
      "{0} debe tener un atributo ''test''."},

  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */


    {ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "Valor incorrecto en atributo de nivel: {0}"},

  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */


    {ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "Nombre de processing-instruction no puede ser 'xml'"},

  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */


    { ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "Nombre de processing-instruction debe ser un NCName v\u00e1lido: {0}"},

  /** ER_NEED_MATCH_ATTRIB          */


    { ER_NEED_MATCH_ATTRIB,
      "{0} debe tener un atributo de coincidencia si tiene una modalidad."},

  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */


    { ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} necesita un atributo de nombre o de coincidencia."},

  /** ER_CANT_RESOLVE_NSPREFIX          */


    {ER_CANT_RESOLVE_NSPREFIX,
      "No se puede resolver el prefijo del espacio de nombres: {0}"},

  /** ER_ILLEGAL_VALUE          */


    { ER_ILLEGAL_VALUE,
     "xml:space tiene un valor no permitido: {0}"},

  /** ER_NO_OWNERDOC          */


    { ER_NO_OWNERDOC,
      "\u00a1El nodo hijo no tiene un documento propietario!"},

  /** ER_ELEMTEMPLATEELEM_ERR          */


    { ER_ELEMTEMPLATEELEM_ERR,
     "Error de ElemTemplateElement: {0}"},

  /** ER_NULL_CHILD          */


    { ER_NULL_CHILD,
     "\u00a1Intentando a\u00f1adir un hijo nulo!"},

  /** ER_NEED_SELECT_ATTRIB          */


    { ER_NEED_SELECT_ATTRIB,
     "{0} necesita un atributo de selecci\u00f3n."},

  /** ER_NEED_TEST_ATTRIB          */


    { ER_NEED_TEST_ATTRIB ,
      "xsl:when debe tener un atributo 'test'."},

  /** ER_NEED_NAME_ATTRIB          */


    { ER_NEED_NAME_ATTRIB,
      "xsl:with-param debe tener un atributo 'name'."},

  /** ER_NO_CONTEXT_OWNERDOC          */


    { ER_NO_CONTEXT_OWNERDOC,
      "\u00a1El contexto no tiene un documento propietario!"},

  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */


    {ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "No se ha podido crear Liaison TransformerFactory XML: {0}"},

  /** ER_PROCESS_NOT_SUCCESSFUL          */


    {ER_PROCESS_NOT_SUCCESSFUL,
      "El proceso Xalan no ha sido satisfactorio."},

  /** ER_NOT_SUCCESSFUL          */


    { ER_NOT_SUCCESSFUL,
     "Xalan no ha sido satisfactorio."},

  /** ER_ENCODING_NOT_SUPPORTED          */


    { ER_ENCODING_NOT_SUPPORTED,
     "Codificaci\u00f3n no soportada: {0}"},

  /** ER_COULD_NOT_CREATE_TRACELISTENER          */


    {ER_COULD_NOT_CREATE_TRACELISTENER,
      "No se ha podido crear TraceListener: {0}"},

  /** ER_KEY_REQUIRES_NAME_ATTRIB          */


    {ER_KEY_REQUIRES_NAME_ATTRIB,
      "\u00a1xsl:key necesita un atributo 'name'!"},

  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


    { ER_KEY_REQUIRES_MATCH_ATTRIB,
      "\u00a1xsl:key necesita un atributo 'match'!"},

  /** ER_KEY_REQUIRES_USE_ATTRIB          */


    { ER_KEY_REQUIRES_USE_ATTRIB,
      "\u00a1xsl:key necesita un atributo 'use'!"},

  /** ER_REQUIRES_ELEMENTS_ATTRIB          */


    { ER_REQUIRES_ELEMENTS_ATTRIB,
      "\u00a1(StylesheetHandler) \u00a1{0} necesita un atributo ''elements''!"},

  /** ER_MISSING_PREFIX_ATTRIB          */


    { ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) Falta el atributo ''prefix'' de {0}"},

  /** ER_BAD_STYLESHEET_URL          */


    { ER_BAD_STYLESHEET_URL,
     "El URL de la hoja de estilos es incorrecto: {0}"},

  /** ER_FILE_NOT_FOUND          */


    { ER_FILE_NOT_FOUND,
     "No se ha encontrado el archivo de hoja de estilos: {0}"},

  /** ER_IOEXCEPTION          */


    { ER_IOEXCEPTION,
      "Se ha producido una excepci\u00f3n de ES con el archivo de hoja de estilos: {0}"},

  /** ER_NO_HREF_ATTRIB          */


    { ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) No se ha podido encontrar el atributo href para {0}"},

  /** ER_STYLESHEET_INCLUDES_ITSELF          */


    { ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) \u00a1Inclusi\u00f3n propia de {0} directa o indirectamente!"},

  /** ER_PROCESSINCLUDE_ERROR          */


    { ER_PROCESSINCLUDE_ERROR,
      "Error de StylesheetHandler.processInclude, {0}"},

  /** ER_MISSING_LANG_ATTRIB          */


    { ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) Falta el atributo ''lang'' de {0}"},

  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */

    { ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) Elemento {0} incorrecto. Falta el elemento de contenedor ''component''"},

  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */

    { ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "S\u00f3lo se puede dar salida hacia Element, DocumentFragment, Document o PrintWriter."},

  /** ER_PROCESS_ERROR          */

    { ER_PROCESS_ERROR,
     "Error de StylesheetRoot.process"},

  /** ER_UNIMPLNODE_ERROR          */

    { ER_UNIMPLNODE_ERROR,
     "Error de UnImplNode: {0}"},

  /** ER_NO_SELECT_EXPRESSION          */

    { ER_NO_SELECT_EXPRESSION,
      "\u00a1Error! No se ha encontrado la expresi\u00f3n de selecci\u00f3n (-select) de xpath."},

  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */

    { ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "\u00a1No se puede serializar un XSLProcessor!"},

  /** ER_NO_INPUT_STYLESHEET          */

    { ER_NO_INPUT_STYLESHEET,
      "\u00a1No se ha especificado la entrada de hoja de estilos!"},

  /** ER_FAILED_PROCESS_STYLESHEET          */

    { ER_FAILED_PROCESS_STYLESHEET,
      "\u00a1No se ha podido procesar la hoja de estilos!"},

  /** ER_COULDNT_PARSE_DOC          */

    { ER_COULDNT_PARSE_DOC,
     "\u00a1No se ha podido analizar el documento {0}!"},

  /** ER_COULDNT_FIND_FRAGMENT          */

    { ER_COULDNT_FIND_FRAGMENT,
     "No se ha podido encontrar el fragmento: {0}"},

  /** ER_NODE_NOT_ELEMENT          */

    { ER_NODE_NOT_ELEMENT,
      "El nodo se\u00f1alado por un identificador de fragmento no es un elemento: {0}"},

  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */

    { ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each debe tener un atributo de coincidencia o de nombre"},

  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */

    { ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "templates debe tener un atributo de coincidencia o de nombre"},

  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */

    { ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "\u00a1No es r\u00e9plica de un fragmento de documento!"},

  /** ER_CANT_CREATE_ITEM          */

    { ER_CANT_CREATE_ITEM,
      "No se puede crear el elemento en el \u00e1rbol de resultados: {0}"},

  /** ER_XMLSPACE_ILLEGAL_VALUE          */

    { ER_XMLSPACE_ILLEGAL_VALUE,
      "xml:space en el XML fuente tiene un valor no permitido: {0}"},

  /** ER_NO_XSLKEY_DECLARATION          */

    { ER_NO_XSLKEY_DECLARATION,
      "\u00a1No hay declaraci\u00f3n xsl:key para {0}!"},

  /** ER_CANT_CREATE_URL          */

    { ER_CANT_CREATE_URL,
     "\u00a1Error! No se puede crear url para: {0}"},

  /** ER_XSLFUNCTIONS_UNSUPPORTED          */

    { ER_XSLFUNCTIONS_UNSUPPORTED,
     "xsl:functions no est\u00e1 soportado"},

  /** ER_PROCESSOR_ERROR          */

    { ER_PROCESSOR_ERROR,
     "Error de XSLT TransformerFactory"},

  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */

    { ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) \u00a1{0} no permitido dentro de una hoja de estilos!"},

  /** ER_RESULTNS_NOT_SUPPORTED          */

    { ER_RESULTNS_NOT_SUPPORTED,
      "\u00a1Ya no se soporta result-ns! Utilice xsl:output en su lugar."},

  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */

    { ER_DEFAULTSPACE_NOT_SUPPORTED,
      "\u00a1Ya no se soporta default-space! Utilice xsl:strip-space o xsl:preserve-space en su lugar."},

  /** ER_INDENTRESULT_NOT_SUPPORTED          */

    { ER_INDENTRESULT_NOT_SUPPORTED,
      "\u00a1Ya no se soporta indent-result! Utilice xsl:output en su lugar."},

  /** ER_ILLEGAL_ATTRIB          */

    { ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} tiene un atributo no permitido: {1}"},

  /** ER_UNKNOWN_XSL_ELEM          */

    { ER_UNKNOWN_XSL_ELEM,
     "Elemento XSL desconocido: {0}"},

  /** ER_BAD_XSLSORT_USE          */

    { ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort s\u00f3lo puede utilizarse con xsl:apply-templates o xsl:for-each."},

  /** ER_MISPLACED_XSLWHEN          */

    { ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) \u00a1xsl:when equivocado!"},

  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */

    { ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) \u00a1xsl:when no emparentado por xsl:choose!"},

  /** ER_MISPLACED_XSLOTHERWISE          */

    { ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) \u00a1xsl:otherwise equivocado!"},

  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */

    { ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) \u00a1xsl:otherwise no emparentado por xsl:choose!"},

  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */

    { ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) \u00a1{0} no permitido dentro de una plantilla!"},

  /** ER_UNKNOWN_EXT_NS_PREFIX          */

    { ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) Prefijo {1} de espacio de nombres de extensi\u00f3n {0} desconocido"},

  /** ER_IMPORTS_AS_FIRST_ELEM          */

    { ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) \u00a1Las importaciones s\u00f3lo pueden aparecer como primeros elementos de la hoja de estilos!"},

  /** ER_IMPORTING_ITSELF          */

    { ER_IMPORTING_ITSELF,
      "(StylesheetHandler) \u00a1Importaci\u00f3n propia de {0} directa o indirectamente!"},

  /** ER_XMLSPACE_ILLEGAL_VAL          */

    { ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) xml:space tiene un valor no permitido: {0}"},

  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */

    { ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "\u00a1processStylesheet no satisfactorio!"},

  /** ER_SAX_EXCEPTION          */

    { ER_SAX_EXCEPTION,
     "Excepci\u00f3n SAX"},

  /** ER_FUNCTION_NOT_SUPPORTED          */


  /** ER_XSLT_ERROR          */

    { ER_XSLT_ERROR,
     "Error de XSLT"},

  /** ER_CURRENCY_SIGN_ILLEGAL          */

    { ER_CURRENCY_SIGN_ILLEGAL,
      "El signo monetario no est\u00e1 permitido en la serie del patr\u00f3n de formato"},

  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */

    { ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "\u00a1La funci\u00f3n de documento no est\u00e1 soportada en DOM de hoja de estilos!"},

  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */

    { ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "\u00a1No se puede resolver el prefijo de un resolucionador sin prefijo!"},

  /** ER_REDIRECT_COULDNT_GET_FILENAME          */

    { ER_REDIRECT_COULDNT_GET_FILENAME,
      "Extensi\u00f3n Redirect: No se ha podido obtener el nombre de archivo - el atributo de archivo o de selecci\u00f3n debe devolver una serie v\u00e1lida."},

  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */

    { ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "\u00a1No se puede crear FormatterListener en extensi\u00f3n Redirect!"},

  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */

    { ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "El prefijo en exclude-result-prefixes no es v\u00e1lido: {0}"},

  /** ER_MISSING_NS_URI          */

    { ER_MISSING_NS_URI,
      "Falta el URI del espacio de nombres para el prefijo especificado"},

  /** ER_MISSING_ARG_FOR_OPTION          */

    { ER_MISSING_ARG_FOR_OPTION,
      "Falta un argumento para la opci\u00f3n: {0}"},

  /** ER_INVALID_OPTION          */

    { ER_INVALID_OPTION,
     "Opci\u00f3n no v\u00e1lida: {0}"},

  /** ER_MALFORMED_FORMAT_STRING          */

    { ER_MALFORMED_FORMAT_STRING,
     "Serie de formato mal formada: {0}"},

  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

    { ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "\u00a1xsl:stylesheet necesita un atributo 'version'!"},

  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */

    { ER_ILLEGAL_ATTRIBUTE_VALUE,
      "Atributo: {0} tiene un valor no permitido: {1}"},

  /** ER_CHOOSE_REQUIRES_WHEN          */

    { ER_CHOOSE_REQUIRES_WHEN,
     "xsl:choose necesita un xsl:when"},

  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */

    { ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports no permitido en xsl:for-each"},

  /** ER_CANT_USE_DTM_FOR_OUTPUT          */

    { ER_CANT_USE_DTM_FOR_OUTPUT,
      "No se puede utilizar DTMLiaison para un nodo DOM de salida... \u00a1Pase org.apache.xpath.DOM2Helper en su lugar!"},

  /** ER_CANT_USE_DTM_FOR_INPUT          */

    { ER_CANT_USE_DTM_FOR_INPUT,
      "No se puede utilizar DTMLiaison para un nodo DOM de entrada... \u00a1Pase org.apache.xpath.DOM2Helper en su lugar!"},

  /** ER_CALL_TO_EXT_FAILED          */

    { ER_CALL_TO_EXT_FAILED,
      "Anomal\u00eda al llamar al elemento de extensi\u00f3n: {0}"},

  /** ER_PREFIX_MUST_RESOLVE          */

    { ER_PREFIX_MUST_RESOLVE,
      "El prefijo debe resolverse como un espacio de nombres: {0}"},

  /** ER_INVALID_UTF16_SURROGATE          */

    { ER_INVALID_UTF16_SURROGATE,
      "\u00bfSe ha detectado un sustituto UTF-16 no v\u00e1lido: {0}?"},

  /** ER_XSLATTRSET_USED_ITSELF          */

    { ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} se ha utilizado a s\u00ed mismo lo que puede provocar un bucle infinito."},

  /** ER_CANNOT_MIX_XERCESDOM          */

    { ER_CANNOT_MIX_XERCESDOM,
      "\u00a1No se puede mezclar la entrada Xerces-DOM con la salida Xerces-DOM!"},

  /** ER_TOO_MANY_LISTENERS          */

    { ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},

  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */

    { ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "En ElemTemplateElement.readObject: {0}"},

  /** ER_DUPLICATE_NAMED_TEMPLATE          */

    { ER_DUPLICATE_NAMED_TEMPLATE,
      "Se ha encontrado m\u00e1s de una plantilla con el nombre: {0}"},

  /** ER_INVALID_KEY_CALL          */

    { ER_INVALID_KEY_CALL,
      "Llamada de funci\u00f3n no v\u00e1lida: no est\u00e1n permitidas las llamadas key() recursivas"},

  /** Variable is referencing itself          */

    { ER_REFERENCING_ITSELF,
      "\u00a1La variable {0} se est\u00e1 referenciando a s\u00ed misma directa o indirectamente!"},

  /** Illegal DOMSource input          */

    { ER_ILLEGAL_DOMSOURCE_INPUT,
      "\u00a1El nodo de entrada no puede ser nulo para DOMSource de newTemplates!"},

        /** Class not found for option         */

    { ER_CLASS_NOT_FOUND_FOR_OPTION,
        "No se ha encontrado el archivo de clase para la opci\u00f3n {0}"},

        /** Required Element not found         */

    { ER_REQUIRED_ELEM_NOT_FOUND,
        "No se ha encontrado un elemento necesario: {0}"},

  /** InputStream cannot be null         */

    { ER_INPUT_CANNOT_BE_NULL,
        "InputStream no puede ser nulo"},

  /** URI cannot be null         */

    { ER_URI_CANNOT_BE_NULL,
        "URI no puede ser nulo"},

  /** File cannot be null         */

    { ER_FILE_CANNOT_BE_NULL,
        "Archivo no puede ser nulo"},

   /** InputSource cannot be null         */

    { ER_SOURCE_CANNOT_BE_NULL,
                "InputSource no puede ser nulo"},

  /** Can't overwrite cause         */


  /** Could not initialize BSF Manager        */

    { ER_CANNOT_INIT_BSFMGR,
                "No se ha podido inicializar el Gestor BSF"},

  /** Could not compile extension       */

    { ER_CANNOT_CMPL_EXTENSN,
                "No se ha podido compilar la extensi\u00f3n"},

  /** Could not create extension       */

    { ER_CANNOT_CREATE_EXTENSN,
      "No se ha podido crear la extensi\u00f3n: {0} como consecuencia de: {1}"},

  /** Instance method call to method {0} requires an Object instance as first argument       */

    { ER_INSTANCE_MTHD_CALL_REQUIRES,
      "La llamada del m\u00e9todo de instancia al m\u00e9todo {0} necesita una instancia Object como primer argumento"},

  /** Invalid element name specified       */

    { ER_INVALID_ELEMENT_NAME,
      "Se ha especificado un nombre de elemento no v\u00e1lido {0}"},

   /** Element name method must be static      */

    { ER_ELEMENT_NAME_METHOD_STATIC,
      "El m\u00e9todo del nombre de elemento debe ser est\u00e1tico {0}"},

   /** Extension function {0} : {1} is unknown      */

    { ER_EXTENSION_FUNC_UNKNOWN,
             "Funci\u00f3n de extensi\u00f3n {0} : {1} desconocida"},

   /** More than one best match for constructor for       */

    { ER_MORE_MATCH_CONSTRUCTOR,
             "Hay m\u00e1s de una coincidencia m\u00e1xima para el constructor de {0}"},

   /** More than one best match for method      */

    { ER_MORE_MATCH_METHOD,
             "Hay m\u00e1s de una coincidencia m\u00e1xima para el m\u00e9todo {0}"},

   /** More than one best match for element method      */

    { ER_MORE_MATCH_ELEMENT,
             "Hay m\u00e1s de una coincidencia m\u00e1xima para el m\u00e9todo de elemento {0}"},

   /** Invalid context passed to evaluate       */

    { ER_INVALID_CONTEXT_PASSED,
             "Se ha pasado un contexto no v\u00e1lido para evaluar {0}"},

   /** Pool already exists       */

    { ER_POOL_EXISTS,
             "La agrupaci\u00f3n ya existe"},

   /** No driver Name specified      */

    { ER_NO_DRIVER_NAME,
             "No se ha especificado un nombre de controlador"},

   /** No URL specified     */

    { ER_NO_URL,
             "No se ha especificado un URL"},

   /** Pool size is less than one    */

    { ER_POOL_SIZE_LESSTHAN_ONE,
             "\u00a1El tama\u00f1o de la agrupaci\u00f3n es menor que uno!"},

   /** Invalid driver name specified    */

    { ER_INVALID_DRIVER,
             "\u00a1Se ha especificado un nombre de controlador no v\u00e1lido!"},

   /** Did not find the stylesheet root    */

    { ER_NO_STYLESHEETROOT,
             "\u00a1No se ha encontrado la ra\u00edz de la hoja de estilos!"},

   /** Illegal value for xml:space     */

    { ER_ILLEGAL_XMLSPACE_VALUE,
         "Valor no permitido para xml:space"},

   /** processFromNode failed     */

    { ER_PROCESSFROMNODE_FAILED,
         "Anomal\u00eda de processFromNode"},

   /** The resource [] could not load:     */

    { ER_RESOURCE_COULD_NOT_LOAD,
        "No se ha podido cargar el recurso [ {0} ]: {1} \n {2} \t {3}"},


   /** Buffer size <=0     */

    { ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "Tama\u00f1o de almacenamiento intermedio <=0"},

   /** Unknown error when calling extension    */

    { ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "Error desconocido al llamar a la extensi\u00f3n"},

   /** Prefix {0} does not have a corresponding namespace declaration    */

    { ER_NO_NAMESPACE_DECL,
        "El prefijo {0} no tiene una declaraci\u00f3n de espacio de nombres correspondiente"},

   /** Element content not allowed for lang=javaclass   */

    { ER_ELEM_CONTENT_NOT_ALLOWED,
        "No se permite el contenido del elemento para lang=javaclass {0}"},

   /** Stylesheet directed termination   */

    { ER_STYLESHEET_DIRECTED_TERMINATION,
        "Terminaci\u00f3n de hoja de estilos dirigida"},

   /** 1 or 2   */

    { ER_ONE_OR_TWO,
        "1 \u00f3 2"},

   /** 2 or 3   */

    { ER_TWO_OR_THREE,
        "2 \u00f3 3"},

   /** Could not load {0} (check CLASSPATH), now using just the defaults   */

    { ER_COULD_NOT_LOAD_RESOURCE,
        "No se ha podido cargar {0} (compruebe la CLASSPATH), ahora s\u00f3lo se est\u00e1n utilizando los valores por omisi\u00f3n"},

   /** Cannot initialize default templates   */

    { ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "No se han podido inicializar las plantillas por omisi\u00f3n"},

   /** Result should not be null   */

    { ER_RESULT_NULL,
        "El resultado no deber\u00eda ser nulo"},

   /** Result could not be set   */

    { ER_RESULT_COULD_NOT_BE_SET,
        "No se ha podido establecer el resultado"},

   /** No output specified   */

    { ER_NO_OUTPUT_SPECIFIED,
        "No se ha especificado salida"},

   /** Can't transform to a Result of type   */

    { ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "No se puede transformar un resultado de tipo {0}"},

   /** Can't transform to a Source of type   */

    { ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "No se puede transformar un fuente de tipo {0}"},

   /** Null content handler  */

    { ER_NULL_CONTENT_HANDLER,
        "Manejador de contenido nulo"},

   /** Null error handler  */
    { ER_NULL_ERROR_HANDLER,
        "Manejador de error nulo"},

   /** parse can not be called if the ContentHandler has not been set */

    { ER_CANNOT_CALL_PARSE,
        "No se puede llamar a parse si no se ha establecido ContentHandler"},

   /**  No parent for filter */

    { ER_NO_PARENT_FOR_FILTER,
        "No hay padre para el filtro"},


   /**  No stylesheet found in: {0}, media */

    { ER_NO_STYLESHEET_IN_MEDIA,
         "No se han encontrado hojas de estilos en: {0}, soporte= {1}"},

   /**  No xml-stylesheet PI found in */

    { ER_NO_STYLESHEET_PI,
         "No se ha encontrado xml-stylesheet PI en: {0}"},

   /**  No default implementation found */


   /**  ChunkedIntArray({0}) not currently supported */


   /**  Offset bigger than slot */


   /**  Coroutine not available, id= */


   /**  CoroutineManager recieved co_exit() request */


   /**  co_joinCoroutineSet() failed */


   /**  Coroutine parameter error () */


   /**  UNEXPECTED: Parser doTerminate answers  */


   /**  parse may not be called while parsing */


   /**  Error: typed iterator for axis  {0} not implemented  */


   /**  Error: iterator for axis {0} not implemented  */


   /**  Iterator clone not supported  */


   /**  Unknown axis traversal type  */


   /**  Axis traverser not supported  */


   /**  No more DTM IDs are available  */


   /**  Not supported  */

    { ER_NOT_SUPPORTED,
       "No soportado: {0}"},

   /**  node must be non-null for getDTMHandleFromNode  */


   /**  Could not resolve the node to a handle  */


   /**  startParse may not be called while parsing */


   /**  startParse needs a non-null SAXParser  */


   /**  could not initialize parser with */

   /**  Value for property {0} should be a Boolean instance  */

    { ER_PROPERTY_VALUE_BOOLEAN,
       "El valor de la propiedad {0} deber\u00eda ser una instancia Boolean"},

   /**  exception creating new instance for pool  */


   /**  Path contains invalid escape sequence  */


   /**  Scheme is required!  */


   /**  No scheme found in URI  */


   /**  No scheme found in URI  */


   /**  Path contains invalid character:   */


   /**  Cannot set scheme from null string  */


   /**  The scheme is not conformant. */


   /**  Host is not a well formed address  */


   /**  Port cannot be set when host is null  */


   /**  Invalid port number  */


   /**  Fragment can only be set for a generic URI  */


   /**  Fragment cannot be set when path is null  */


   /**  Fragment contains invalid character  */




   /** Parser is already in use  */


   /** Parser is already in use  */


   /** Self-causation not permitted  */


   /** src attribute not yet supported for  */

    { ER_COULD_NOT_FIND_EXTERN_SCRIPT,
         "No se ha podido encontrar el script externo en {0}"},

  /** The resource [] could not be found     */

    { ER_RESOURCE_COULD_NOT_FIND,
        "No se ha podido cargar el recurso [ {0} ].\n{1}"},

   /** output property not recognized:  */

    { ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "No se reconoce la propiedad de salida: {0}"},

   /** Userinfo may not be specified if host is not specified   */


   /** Port may not be specified if host is not specified   */


   /** Query string cannot be specified in path and query string   */


   /** Fragment cannot be specified in both the path and fragment   */


   /** Cannot initialize URI with empty parameters   */


   /** Failed creating ElemLiteralResult instance   */

    { ER_FAILED_CREATING_ELEMLITRSLT,
        "Anomal\u00eda al crear la instancia ElemLiteralResult"},


   /** Priority value does not contain a parsable number   */

    { ER_VALUE_SHOULD_BE_NUMBER,
        "El valor para {0} deber\u00eda contener un n\u00famero analizable"},

   /**  Value for {0} should equal 'yes' or 'no'   */

    { ER_VALUE_SHOULD_EQUAL,
        "El valor de {0} deber\u00eda ser s\u00ed o no"},

   /**  Failed calling {0} method   */

    { ER_FAILED_CALLING_METHOD,
        "Anomal\u00eda al llamar al m\u00e9todo {0}"},

   /** Failed creating ElemLiteralResult instance   */

    { ER_FAILED_CREATING_ELEMTMPL,
        "Anomal\u00eda al crear la instancia ElemTemplateElement"},

   /**  Characters are not allowed at this point in the document   */

    { ER_CHARS_NOT_ALLOWED,
        "No se permiten caracteres en este punto del documento"},

  /**  attribute is not allowed on the element   */
    { ER_ATTR_NOT_ALLOWED,
        "\u00a1El atributo \"{0}\" no est\u00e1 permitido en el elemento {1}!"},

  /**  Method not yet supported    */


  /**  Bad value    */

    { ER_BAD_VALUE,
     "{0} valor incorrecto {1}"},

  /**  attribute value not found   */

    { ER_ATTRIB_VALUE_NOT_FOUND,
     "No se ha encontrado el valor del atributo {0}"},

  /**  attribute value not recognized    */

    { ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "No se ha reconocido el valor del atributo {0}"},

  /** IncrementalSAXSource_Filter not currently restartable   */


  /** IncrementalSAXSource_Filter not currently restartable   */


  /** Attempting to generate a namespace prefix with a null URI   */

    { ER_NULL_URI_NAMESPACE,
     "Se ha intentado generar un prefijo de espacio de nombres con un URI nulo"},


  /** Attempting to generate a namespace prefix with a null URI   */

    { ER_NUMBER_TOO_BIG,
     "Se ha intentado formatear un n\u00famero mayor que el entero largo m\u00e1s grande"},



    { ER_CANNOT_FIND_SAX1_DRIVER,
     "No se ha podido encontrar la clase de controlador SAX1 {0}"},


    { ER_SAX1_DRIVER_NOT_LOADED,
     "Se ha encontrado la clase de controlador SAX1 {0} pero no se ha podido cargar"},


    { ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "Se ha cargado la clase de controlador SAX1 {0} pero no se ha podido crear una instancia"},



    { ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "La clase de controlador SAX1 {0} no implementa org.xml.sax.Parser"},


    { ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "No se ha especificado la propiedad del sistema org.xml.sax.parser"},


    { ER_PARSER_ARG_CANNOT_BE_NULL,
     "El argumento del analizador no debe ser nulo"},



    { ER_FEATURE,
     "Caracter\u00edstica: {0}"},



    { ER_PROPERTY,
     "Propiedad: {0}"},


    { ER_NULL_ENTITY_RESOLVER,
     "Resolucionador de entidad nulo"},


    { ER_NULL_DTD_HANDLER,
     "Manejador DTD nulo"},

    { ER_NO_DRIVER_NAME_SPECIFIED,
     "\u00a1No se ha especificado un nombre de controlador!"},


    { ER_NO_URL_SPECIFIED,
     "\u00a1No se ha especificado un URL!"},


    { ER_POOLSIZE_LESS_THAN_ONE,
     "\u00a1El tama\u00f1o de la agrupaci\u00f3n es menor que 1!"},


    { ER_INVALID_DRIVER_NAME,
     "\u00a1Se ha especificado un nombre de controlador no v\u00e1lido!"},



    { ER_ERRORLISTENER,
     "ErrorListener"},


    { ER_ASSERT_NO_TEMPLATE_PARENT,
     "\u00a1Error del programador! \u00a1La expresi\u00f3n no tiene un padre ElemTemplateElement!"},


    { ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "Aserci\u00f3n del programador en RundundentExprEliminator: {0}"},



    { ER_NOT_ALLOWED_IN_POSITION,
     "\u00a1{0} no est\u00e1 permitido en esta posici\u00f3n de la hoja de estilos!"},

    { ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "\u00a1No est\u00e1 permitido texto sin espacios en blanco en esta posici\u00f3n de la hoja de estilos!"},

    { INVALID_TCHAR,
     "Valor no permitido: se ha utilizado {1} para el atributo CHAR: {0}. \u00a1Un atributo de tipo CHAR debe ser de un solo car\u00e1cter!"},




    { INVALID_QNAME,
     "Valor no permitido: se ha utilizado {1} para el atributo QNAME: {0}"},


    { INVALID_ENUM,
     "Valor no permitido: se ha utilizado {1} para el atributo ENUM: {0}. Los valores v\u00e1lidos son: {2}."},


    { INVALID_NMTOKEN,
     "Valor no permitido: se ha utilizado {1} para el atributo NMTOKEN: {0}"},


    { INVALID_NCNAME,
     "Valor no permitido: se ha utilizado {1} para el atributo NCNAME: {0}"},



    { INVALID_BOOLEAN,
     "Valor no permitido: se ha utilizado {1} para el atributo boolean: {0}"},


     { INVALID_NUMBER,
     "Valor no permitido: se ha utilizado {1} para el atributo number: {0}"},




    { ER_ARG_LITERAL,
     "El argumento para {0} en el patr\u00f3n de coincidencia debe ser un literal."},


    { ER_DUPLICATE_GLOBAL_VAR,
     "Declaraci\u00f3n de variable global duplicada."},



    { ER_DUPLICATE_VAR,
     "Declaraci\u00f3n de variable duplicada."},


    { ER_TEMPLATE_NAME_MATCH,
     "xsl:template debe tener un atributo name o match (o ambos)"},


    { ER_INVALID_PREFIX,
     "El prefijo en exclude-result-prefixes no es v\u00e1lido: {0}"},


    { ER_NO_ATTRIB_SET,
     "attribute-set de nombre {0} no existe"},






  /** WG_FOUND_CURLYBRACE          */
    { WG_FOUND_CURLYBRACE,
      "\u00a1Se ha encontrado '}' pero no se ha abierto una plantilla de atributos!"},

  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */

    { WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "Aviso: \u00a1El atributo count no coincide con un antecesor en xsl:number! Destino = {0}"},

  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */

    { WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "Sintaxis antigua: El nombre del atributo 'expr' se ha cambiado por 'select'."},

  /** WG_NO_LOCALE_IN_FORMATNUMBER          */

    { WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan no maneja a\u00fan el nombre de entorno local en la funci\u00f3n format-number."},

  /** WG_LOCALE_NOT_FOUND          */

    { WG_LOCALE_NOT_FOUND,
      "Aviso: No se ha podido encontrar el entorno local para xml:lang={0}"},

  /** WG_CANNOT_MAKE_URL_FROM          */

    { WG_CANNOT_MAKE_URL_FROM,
      "No se puede crear URL desde: {0}"},

  /** WG_CANNOT_LOAD_REQUESTED_DOC          */

    { WG_CANNOT_LOAD_REQUESTED_DOC,
      "No se puede cargar el doc solicitado: {0}"},

  /** WG_CANNOT_FIND_COLLATOR          */
    { WG_CANNOT_FIND_COLLATOR,
      "No se ha podido encontrar clasificador para <sort xml:lang={0}"},

  /** WG_FUNCTIONS_SHOULD_USE_URL          */

    { WG_FUNCTIONS_SHOULD_USE_URL,
      "Sintaxis antigua: La instrucci\u00f3n functions deber\u00eda utilizar un url de {0}"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */

    { WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "Codificaci\u00f3n no soportada: {0}, se utiliza UTF-8"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */

    { WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "Codificaci\u00f3n no soportada: {0}, se utiliza Java {1}"},

  /** WG_SPECIFICITY_CONFLICTS          */

    { WG_SPECIFICITY_CONFLICTS,
      "Se han encontrado conflictos de especificaci\u00f3n: {0} Se utilizar\u00e1 lo \u00faltimo encontrado en la hoja de estilos."},

  /** WG_PARSING_AND_PREPARING          */

    { WG_PARSING_AND_PREPARING,
      "========= Analizando y preparando {0} =========="},

  /** WG_ATTR_TEMPLATE          */

    { WG_ATTR_TEMPLATE,
     "Plantilla de atributos, {0}"},

  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */

    { WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "Conflicto de coincidencia entre xsl:strip-space y xsl:preserve-space"},

  /** WG_ATTRIB_NOT_HANDLED          */

    { WG_ATTRIB_NOT_HANDLED,
      "\u00a1Xalan no maneja a\u00fan el atributo {0}!"},

  /** WG_NO_DECIMALFORMAT_DECLARATION          */

    { WG_NO_DECIMALFORMAT_DECLARATION,
      "No se ha encontrado declaraci\u00f3n para el formato decimal: {0}"},

  /** WG_OLD_XSLT_NS          */

    { WG_OLD_XSLT_NS,
     "Falta el espacio de nombres XSLT o es incorrecto."},

  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */

    { WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "S\u00f3lo se permite una declaraci\u00f3n xsl:decimal-format por omisi\u00f3n."},

  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */

    { WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "Los nombres de xsl:decimal-format deben ser \u00fanicos. El nombre \"{0}\" se ha duplicado."},

  /** WG_ILLEGAL_ATTRIBUTE          */

    { WG_ILLEGAL_ATTRIBUTE,
      "{0} tiene un atributo no permitido: {1}"},

  /** WG_COULD_NOT_RESOLVE_PREFIX          */

    { WG_COULD_NOT_RESOLVE_PREFIX,
      "No se ha podido resolver el prefijo del espacio de nombres: {0}. Se ignorar\u00e1 el nodo."},

  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */
    { WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "\u00a1xsl:stylesheet necesita un atributo 'version'!"},

  /** WG_ILLEGAL_ATTRIBUTE_NAME          */

    { WG_ILLEGAL_ATTRIBUTE_NAME,
      "Nombre de atributo no permitido: {0}"},

  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */
    { WG_ILLEGAL_ATTRIBUTE_VALUE,
      "Se ha utilizado un valor no permitido para el atributo {0}: {1}"},

  /** WG_EMPTY_SECOND_ARG          */

    { WG_EMPTY_SECOND_ARG,
      "El NodeSet resultante del segundo argumento de la funci\u00f3n del documento est\u00e1 vac\u00edo. Devuelve un conjunto de nodos vac\u00edo."},




  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */
    { WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "El valor del atributo 'name' de nombre xsl:processing-instruction no debe ser 'xml'"},


  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */
    { WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "El valor del atributo 'name' de xsl:processing-instruction debe ser un NCName v\u00e1lido: {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */
    { WG_ILLEGAL_ATTRIBUTE_POSITION,
      "No se puede a\u00f1adir el atributo {0} despu\u00e9s de nodos hijo o antes de que se produzca un elemento. Se ignorar\u00e1 el atributo."},


  { "ui_language", "es"},
  {  "help_language",  "es" },
  {  "language",  "es" },
  { "BAD_CODE", "El par\u00e1metro para createMessage estaba fuera de los l\u00edmites"},
  {  "FORMAT_FAILED", "Se ha generado una excepci\u00f3n durante la llamada messageFormat"},
  {  "version", ">>>>>>> Xalan versi\u00f3n"},
  {  "version2",  "<<<<<<<"},
  {  "yes", "s\u00ed"},
  { "line", "L\u00ednea n\u00fam."},
  { "column","Columna n\u00fam."},
  { "xsldone", "XSLProcessor: terminado"},


  { "xslProc_option", "Opciones de la clase Process de la l\u00ednea de mandatos Xalan-J:"},
  { "xslProc_option", "Opciones de la clase Process de la l\u00ednea de mandatos Xalan-J\u003a"},
  { "xslProc_invalid_xsltc_option", "La opci\u00f3n {0} no est\u00e1 soportada en modalidad XSLTC."},
  { "xslProc_invalid_xalan_option", "La opci\u00f3n {0} s\u00f3lo puede utilizarse con -XSLTC."},
  { "xslProc_no_input", "Error: No se ha especificado ninguna hoja de estilos ni xml de entrada. Ejecute este mandato sin opciones para ver las instrucciones de uso."},
  { "xslProc_common_options", "-Opciones comunes-"},
  { "xslProc_xalan_options", "-Opciones para Xalan-"},
  { "xslProc_xsltc_options", "-Opciones para XSLTC-"},
  { "xslProc_return_to_continue", "(pulse <Intro> para continuar)"},

  { "optionXSLTC", "[-XSLTC (Utilizar XSLTC para transformaci\u00f3n)]"},
  { "optionIN", "[-IN URLXMLentrada]"},
  { "optionXSL", "[-XSL URLTransformaci\u00f3nXSL]"},
  { "optionOUT",  "[-OUT nombreArchivoSalida]"},
  { "optionLXCIN", "[-LXCIN entradaNombreArchivoHojaEstilosCompilada]"},
  { "optionLXCOUT", "[-LXCOUT salidaNombreArchivoHojaEstilosCompilada]"},
  { "optionPARSER", "[-PARSER nombre de clase completamente cualificado del enlace del analizador]"},
  {  "optionE", "[-E (No expandir referencias de entidades)]"},
  {  "optionV",  "[-E (No expandir referencias de entidades)]"},
  {  "optionQC", "[-QC (Avisos silenciosos de conflictos de patrones)]"},
  {  "optionQ", "[-Q  (Modalidad silenciosa)]"},
  {  "optionLF", "[-LF (Utilizar s\u00f3lo avances de l\u00ednea en la salida {por omisi\u00f3n es CR/LF})]"},
  {  "optionCR", "[-CR (Utilizar s\u00f3lo retornos de carro en la salida {por omisi\u00f3n es CR/LF})]"},
  { "optionESCAPE", "[-ESCAPE (Caracteres con escape {por omisi\u00f3n es <>&\"\'\\r\\n}]"},
  { "optionINDENT", "[-INDENT (Controlar el n\u00famero de espacios de sangrado {por omisi\u00f3n es 0})]"},
  { "optionTT", "[-TT (Rastrear las plantillas a medida que se llaman.)]"},
  { "optionTG", "[-TG (Rastrear cada suceso de generaci\u00f3n.)]"},
  { "optionTS", "[-TS (Rastrear cada suceso de selecci\u00f3n.)]"},
  {  "optionTTC", "[-TTC (Rastrear los hijos de plantillas a medida que se procesan.)]"},
  { "optionTCLASS", "[-TCLASS (Clase TraceListener para extensiones de rastreo.)]"},
  { "optionVALIDATE", "[-VALIDATE (Establecer si se realiza la validaci\u00f3n. Por omisi\u00f3n la validaci\u00f3n est\u00e1 desactivada.)]"},
  { "optionEDUMP", "[-EDUMP {nombre de archivo opcional} (Realizar vuelco de pila si se produce un error.)]"},
  {  "optionXML", "[-XML (Utilizar el formateador XML y a\u00f1adir la cabecera XML.)]"},
  {  "optionTEXT", "[-TEXT (Utilizar el formateador de texto sencillo.)]"},
  {  "optionHTML", "[-HTML (Utilizar el formateador HTML.)]"},
  {  "optionPARAM", "[-PARAM expresi\u00f3n de nombre (Establecer un par\u00e1metro de hoja de estilos)]"},
  {  "noParsermsg1", "El proceso XSL no ha sido satisfactorio."},
  {  "noParsermsg2", "** No se ha podido encontrar el analizador **"},
  { "noParsermsg3",  "Compruebe la classpath."},
  { "noParsermsg4", "Si no dispone del analizador XML para Java de IBM, puede bajarlo de "},
  { "optionURIRESOLVER", "[-URIRESOLVER nombre de clase completo (URIResolver a utilizar para resolver URI)]"},
  { "optionENTITYRESOLVER",  "[-ENTITYRESOLVER nombre de clase completo (EntityResolver a utilizar para resolver entidades)]"},
  { "optionCONTENTHANDLER",  "[-CONTENTHANDLER nombre de clase completo (ContentHandler a utilizar para serializar la salida)]"},
  {  "optionLINENUMBERS",  "[-L utilizar n\u00fameros de l\u00ednea para el documento fuente]"},



  {  "optionMEDIA",  "[-MEDIA tipoSoporte (Utilizar el atributo de soporte para encontrar la hoja de estilos asociada con un documento.)]"},
  { "optionDIAG", "[-DIAG (Imprimir el total de milisegundos que lleva la transformaci\u00f3n.)]"},
  { "optionRL",  "[-RL l\u00edmiteRecursi\u00f3n (L\u00edmite num\u00e9rico de aserci\u00f3n sobre profundidad de recursi\u00f3n de hoja de estilos.)]"},
  {   "optionXO",  "[-XO [nombreTranslet] (Asignar el nombre al translet generado)]"},
  {  "optionXD", "[-XD directorioDestino (Especificar un directorio de destino para translet)]"},
  {  "optionXJ",  "[-XJ archivoJar (Empaqueta las clases translet en un archivo jar de nombre <archivoJar>)]"},
  {   "optionXP",  "[-XP paquete (Especifica un prefijo para el nombre del paquete de todas las clases translet generadas)]"},

  { "optionXN",  "[-XN (habilita la inclusi\u00f3n en l\u00ednea de plantillas)]" },
  { "optionXX",  "[-XX (activa la salida de mensajes de depuraci\u00f3n adicionales)]"},
  { "optionXT" , "[-XT (utilizar translet para transformar si es posible)]"},
  { "diagTiming","--------- La transformaci\u00f3n de {0} mediante {1} ha durado {2} ms" },
  { "recursionTooDeep","Anidado de plantilla demasiado profundo. anidado = {0}, plantilla {1} {2}" },
  { "nameIs", "el nombre es " },
  { "matchPatternIs", "el patr\u00f3n de coincidencia es " }

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
  public static final String WARNING_HEADER = "Aviso: ";

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

  /**
   *   Return a named ResourceBundle for a particular locale.  This method mimics the behavior
   *   of ResourceBundle.getBundle().
   *
   *   @param className the name of the class that implements the resource bundle.
   *   @return the ResourceBundle
   *   @throws MissingResourceException
   */
  public static final XSLTErrorResources loadResourceBundle(String className)
          throws MissingResourceException
  {

    Locale locale = Locale.getDefault();
    String suffix = getResourceSuffix(locale);

    try
    {

      return (XSLTErrorResources) ResourceBundle.getBundle(className
              + suffix, locale);
    }
    catch (MissingResourceException e)
    {
      {

        return (XSLTErrorResources) ResourceBundle.getBundle(className,
                new Locale("es", "ES"));
      }
      catch (MissingResourceException e2)
      {

        throw new MissingResourceException(
          "Could not load any resource bundles.", className, "");
      }
    }
  }

  /**
   * Return the resource file suffic for the indicated locale
   * For most locales, this will be based the language code.  However
   * for Chinese, we do distinguish between Taiwan and PRC
   *
   * @param locale the locale
   * @return an String suffix which canbe appended to a resource name
   */
  private static final String getResourceSuffix(Locale locale)
  {

    String suffix = "_" + locale.getLanguage();
    String country = locale.getCountry();

    if (country.equals("TW"))
      suffix += "_" + country;

    return suffix;
  }


}
