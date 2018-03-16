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
public class XSLTErrorResources_pl extends ListResourceBundle
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
      "B\u0142\u0105d: Nie mo\u017ce by\u0107 znaku '{' wewn\u0105trz wyra\u017cenia"},

  /** ER_ILLEGAL_ATTRIBUTE          */

    { ER_ILLEGAL_ATTRIBUTE ,
     "{0} ma niedozwolony atrybut {1}"},

  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */

  {ER_NULL_SOURCENODE_APPLYIMPORTS ,
      "sourceNode jest puste w xsl:apply-imports!"},

  /** ER_CANNOT_ADD          */

  {ER_CANNOT_ADD,
      "Nie mo\u017cna doda\u0107 {0} do {1}"},


  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */


    { ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "sourceNode jest puste w handleApplyTemplatesInstruction!"},

  /** ER_NO_NAME_ATTRIB          */


    { ER_NO_NAME_ATTRIB,
     "{0} musi mie\u0107 atrybut name."},

  /** ER_TEMPLATE_NOT_FOUND          */


    {ER_TEMPLATE_NOT_FOUND,
     "Nie mo\u017cna znale\u017a\u0107 szablonu o nazwie {0}"},

  /** ER_CANT_RESOLVE_NAME_AVT          */

    {ER_CANT_RESOLVE_NAME_AVT,
      "Nie mo\u017cna przet\u0142umaczy\u0107 AVT nazwy w xsl:call-template."},

  /** ER_REQUIRES_ATTRIB          */


    {ER_REQUIRES_ATTRIB,
     "{0} wymaga atrybutu: {1}"},

  /** ER_MUST_HAVE_TEST_ATTRIB          */


    { ER_MUST_HAVE_TEST_ATTRIB,
      "{0} musi mie\u0107 atrybut ''test''."},

  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */


    {ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "B\u0142\u0119dna warto\u015b\u0107 w atrybucie level: {0}"},

  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */


    {ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "Nazw\u0105 instrukcji przetwarzania nie mo\u017ce by\u0107 'xml'"},

  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */


    { ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "Nazwa instrukcji przetwarzania musi by\u0107 poprawn\u0105 nazw\u0105 NCName {0}"},

  /** ER_NEED_MATCH_ATTRIB          */


    { ER_NEED_MATCH_ATTRIB,
      "{0} musi mie\u0107 atrybut match, je\u015bli ma mode."},

  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */


    { ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} wymaga albo atrybutu name, albo match."},

  /** ER_CANT_RESOLVE_NSPREFIX          */


    {ER_CANT_RESOLVE_NSPREFIX,
      "Nie mo\u017cna rozstrzygn\u0105\u0107 przedrostka przestrzeni nazw {0}"},

  /** ER_ILLEGAL_VALUE          */


    { ER_ILLEGAL_VALUE,
     "xml:space ma niepoprawn\u0105 warto\u015b\u0107 {0}"},

  /** ER_NO_OWNERDOC          */


    { ER_NO_OWNERDOC,
      "W\u0119ze\u0142 potomny nie ma dokumentu w\u0142a\u015bciciela!"},

  /** ER_ELEMTEMPLATEELEM_ERR          */


    { ER_ELEMTEMPLATEELEM_ERR,
     "B\u0142\u0105d ElemTemplateElement: {0}"},

  /** ER_NULL_CHILD          */


    { ER_NULL_CHILD,
     "Pr\u00f3ba dodania pustego elementu potomnego!"},

  /** ER_NEED_SELECT_ATTRIB          */


    { ER_NEED_SELECT_ATTRIB,
     "{0} wymaga atrybutu select."},

  /** ER_NEED_TEST_ATTRIB          */


    { ER_NEED_TEST_ATTRIB ,
      "xsl:when musi mie\u0107 atrybut 'test'."},

  /** ER_NEED_NAME_ATTRIB          */


    { ER_NEED_NAME_ATTRIB,
      "xsl:with-param musi mie\u0107 atrybut 'name'."},

  /** ER_NO_CONTEXT_OWNERDOC          */


    { ER_NO_CONTEXT_OWNERDOC,
      "Kontekst nie ma dokumentu w\u0142a\u015bciciela!"},

  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */


    {ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "Nie mo\u017cna utworzy\u0107 po\u0142\u0105czenia XML TransformerFactory: {0}"},

  /** ER_PROCESS_NOT_SUCCESSFUL          */


    {ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan: Proces nie wykona\u0142 si\u0119 pomy\u015blnie."},

  /** ER_NOT_SUCCESSFUL          */


    { ER_NOT_SUCCESSFUL,
     "Xalan: nie wykona\u0142 si\u0119 pomy\u015blnie."},

  /** ER_ENCODING_NOT_SUPPORTED          */


    { ER_ENCODING_NOT_SUPPORTED,
     "Nieobs\u0142ugiwane kodowanie {0}"},

  /** ER_COULD_NOT_CREATE_TRACELISTENER          */


    {ER_COULD_NOT_CREATE_TRACELISTENER,
      "Nie mo\u017cna utworzy\u0107 TraceListener: {0}"},

  /** ER_KEY_REQUIRES_NAME_ATTRIB          */


    {ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key wymaga atrybutu 'name'!"},

  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


    { ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key wymaga atrybutu 'match'!"},

  /** ER_KEY_REQUIRES_USE_ATTRIB          */


    { ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key wymaga atrybutu 'use'!"},

  /** ER_REQUIRES_ELEMENTS_ATTRIB          */


    { ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} wymaga atrybutu ''elements''!"},

  /** ER_MISSING_PREFIX_ATTRIB          */


    { ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) atrybut {0} ''prefix'' jest nieobecny"},

  /** ER_BAD_STYLESHEET_URL          */


    { ER_BAD_STYLESHEET_URL,
     "Adres URL arkusza styl\u00f3w jest b\u0142\u0119dny {0}"},

  /** ER_FILE_NOT_FOUND          */


    { ER_FILE_NOT_FOUND,
     "Nie znaleziono pliku arkusza styl\u00f3w {0}"},

  /** ER_IOEXCEPTION          */


    { ER_IOEXCEPTION,
      "Wyst\u0105pi\u0142 wyj\u0105tek we/wy w pliku arkusza styl\u00f3w {0}"},

  /** ER_NO_HREF_ATTRIB          */


    { ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) Nie mo\u017cna znale\u017a\u0107 atrybutu href dla {0}"},

  /** ER_STYLESHEET_INCLUDES_ITSELF          */


    { ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} zawiera siebie bezpo\u015brednio lub po\u015brednio!"},

  /** ER_PROCESSINCLUDE_ERROR          */


    { ER_PROCESSINCLUDE_ERROR,
      "B\u0142\u0105d StylesheetHandler.processInclude {0}"},

  /** ER_MISSING_LANG_ATTRIB          */


    { ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) atrybut {0} ''lang'' jest nieobecny"},

  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */

    { ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) \u017ale umieszczony element {0}?? Nieobecny element pojemnika ''component''"},

  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */

    { ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "Mo\u017cna wyprowadzi\u0107 dane tylko do Element, DocumentFragment, Document lub PrintWriter."},

  /** ER_PROCESS_ERROR          */

    { ER_PROCESS_ERROR,
     "B\u0142\u0105d StylesheetRoot.process"},

  /** ER_UNIMPLNODE_ERROR          */

    { ER_UNIMPLNODE_ERROR,
     "B\u0142\u0105d UnImplNode: {0}"},

  /** ER_NO_SELECT_EXPRESSION          */

    { ER_NO_SELECT_EXPRESSION,
      "B\u0142\u0105d! Nie znaleziono wyra\u017cenia wyboru xpath (-select)."},

  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */

    { ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "Nie mo\u017cna szeregowa\u0107 XSLProcessor!"},

  /** ER_NO_INPUT_STYLESHEET          */

    { ER_NO_INPUT_STYLESHEET,
      "Nie podano danych wej\u015bciowych do arkusza styl\u00f3w!"},

  /** ER_FAILED_PROCESS_STYLESHEET          */

    { ER_FAILED_PROCESS_STYLESHEET,
      "Nie powiod\u0142o si\u0119 przetworzenie arkusza styl\u00f3w!"},

  /** ER_COULDNT_PARSE_DOC          */

    { ER_COULDNT_PARSE_DOC,
     "Nie mo\u017cna zanalizowa\u0107 dokumentu {0}!"},

  /** ER_COULDNT_FIND_FRAGMENT          */

    { ER_COULDNT_FIND_FRAGMENT,
     "Nie mo\u017cna znale\u017a\u0107 fragmentu {0}"},

  /** ER_NODE_NOT_ELEMENT          */

    { ER_NODE_NOT_ELEMENT,
      "W\u0119ze\u0142 wskazywany przez identyfikator fragmentu nie by\u0142 elementem {0}"},

  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */

    { ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each musi mie\u0107 albo atrybut match, albo name"},

  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */

    { ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "templates musi mie\u0107 albo atrybut match, albo name"},

  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */

    { ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "Brak klonu fragmentu dokumentu!"},

  /** ER_CANT_CREATE_ITEM          */

    { ER_CANT_CREATE_ITEM,
      "Nie mo\u017cna utworzy\u0107 elementu w wynikowym drzewie {0}"},

  /** ER_XMLSPACE_ILLEGAL_VALUE          */

    { ER_XMLSPACE_ILLEGAL_VALUE,
      "xml:space w \u017ar\u00f3d\u0142owym pliku XML ma niedozwolon\u0105 warto\u015b\u0107 {0}"},

  /** ER_NO_XSLKEY_DECLARATION          */

    { ER_NO_XSLKEY_DECLARATION,
      "Nie ma deklaracji xsl:key dla {0}!"},

  /** ER_CANT_CREATE_URL          */

    { ER_CANT_CREATE_URL,
     "B\u0142\u0105d! Nie mo\u017cna utworzy\u0107 adresu url dla {0}"},

  /** ER_XSLFUNCTIONS_UNSUPPORTED          */

    { ER_XSLFUNCTIONS_UNSUPPORTED,
     "xsl:functions jest nieobs\u0142ugiwane"},

  /** ER_PROCESSOR_ERROR          */

    { ER_PROCESSOR_ERROR,
     "B\u0142\u0105d XSLT TransformerFactory"},

  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */

    { ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} jest niedozwolone wewn\u0105trz arkusza styl\u00f3w!"},

  /** ER_RESULTNS_NOT_SUPPORTED          */

    { ER_RESULTNS_NOT_SUPPORTED,
      "result-ns nie jest ju\u017c obs\u0142ugiwane!  U\u017cyj zamiast tego xsl:output."},

  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */

    { ER_DEFAULTSPACE_NOT_SUPPORTED,
      "default-space nie jest ju\u017c obs\u0142ugiwane!  U\u017cyj zamiast tego xsl:strip-space lub xsl:preserve-space."},

  /** ER_INDENTRESULT_NOT_SUPPORTED          */

    { ER_INDENTRESULT_NOT_SUPPORTED,
      "indent-result nie jest ju\u017c obs\u0142ugiwane!  U\u017cyj zamiast tego xsl:output."},

  /** ER_ILLEGAL_ATTRIB          */

    { ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} ma niedozwolony atrybut {1}"},

  /** ER_UNKNOWN_XSL_ELEM          */

    { ER_UNKNOWN_XSL_ELEM,
     "Nieznany element XSL {0}"},

  /** ER_BAD_XSLSORT_USE          */

    { ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort mo\u017ce by\u0107 u\u017cywane tylko z xsl:apply-templates lub xsl:for-each."},

  /** ER_MISPLACED_XSLWHEN          */

    { ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) b\u0142\u0119dnie umieszczone xsl:when!"},

  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */

    { ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:when bez nadrz\u0119dnego xsl:choose!"},

  /** ER_MISPLACED_XSLOTHERWISE          */

    { ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) b\u0142\u0119dnie umieszczone xsl:otherwise!"},

  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */

    { ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:otherwise bez nadrz\u0119dnego xsl:choose!"},

  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */

    { ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} jest niedozwolone wewn\u0105trz szablonu!"},

  /** ER_UNKNOWN_EXT_NS_PREFIX          */

    { ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) Nieznany przedrostek {1} rozszerzenia {0} przestrzeni nazw"},

  /** ER_IMPORTS_AS_FIRST_ELEM          */

    { ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) Importy mog\u0105 wyst\u0105pi\u0107 tylko jako pierwsze elementy w arkuszu styl\u00f3w!"},

  /** ER_IMPORTING_ITSELF          */

    { ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} importuje siebie bezpo\u015brednio lub po\u015brednio!"},

  /** ER_XMLSPACE_ILLEGAL_VAL          */

    { ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) xml:space ma niedozwolon\u0105 warto\u015b\u0107: {0}"},

  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */

    { ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "processStylesheet by\u0142o niepomy\u015blne!"},

  /** ER_SAX_EXCEPTION          */

    { ER_SAX_EXCEPTION,
     "Wyj\u0105tek SAX"},

  /** ER_FUNCTION_NOT_SUPPORTED          */


  /** ER_XSLT_ERROR          */

    { ER_XSLT_ERROR,
     "B\u0142\u0105d XSLT"},

  /** ER_CURRENCY_SIGN_ILLEGAL          */

    { ER_CURRENCY_SIGN_ILLEGAL,
      "Znak waluty jest niedozwolony w ci\u0105gu znak\u00f3w wzorca formatu"},

  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */

    { ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "Funkcja Document jest nieobs\u0142ugiwana w arkuszu styl\u00f3w DOM!"},

  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */

    { ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "Nie mo\u017cna rozstrzygn\u0105\u0107 przedrostka przelicznika bez przedrostka!"},

  /** ER_REDIRECT_COULDNT_GET_FILENAME          */

    { ER_REDIRECT_COULDNT_GET_FILENAME,
      "Rozszerzenie Redirect: Nie mo\u017cna pobra\u0107 nazwy pliku - atrybut file lub select musi zwr\u00f3ci\u0107 poprawny ci\u0105g znak\u00f3w."},

  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */

    { ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "Nie mo\u017cna zbudowa\u0107 FormatterListener w rozszerzeniu Redirect!"},

  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */

    { ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "Przedrostek w exclude-result-prefixes jest niepoprawny: {0}"},

  /** ER_MISSING_NS_URI          */

    { ER_MISSING_NS_URI,
      "Nieobecny identyfikator URI przestrzeni nazw w podanym przedrostku"},

  /** ER_MISSING_ARG_FOR_OPTION          */

    { ER_MISSING_ARG_FOR_OPTION,
      "Nieobecny argument opcji {0}"},

  /** ER_INVALID_OPTION          */

    { ER_INVALID_OPTION,
     "Niepoprawna opcja {0}"},

  /** ER_MALFORMED_FORMAT_STRING          */

    { ER_MALFORMED_FORMAT_STRING,
     "Zniekszta\u0142cony ci\u0105g znak\u00f3w formatu {0}"},

  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

    { ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet wymaga atrybutu 'version'!"},

  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */

    { ER_ILLEGAL_ATTRIBUTE_VALUE,
      "Atrybut {0} ma niedozwolon\u0105 warto\u015b\u0107 {1}"},

  /** ER_CHOOSE_REQUIRES_WHEN          */

    { ER_CHOOSE_REQUIRES_WHEN,
     "xsl:choose wymaga xsl:when"},

  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */

    { ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports jest niedozwolone w xsl:for-each"},

  /** ER_CANT_USE_DTM_FOR_OUTPUT          */

    { ER_CANT_USE_DTM_FOR_OUTPUT,
      "Nie mo\u017cna u\u017cy\u0107 DTMLiaison w wyj\u015bciowym w\u0119\u017ale DOM... przeka\u017c zamiast tego org.apache.xpath.DOM2Helper!"},

  /** ER_CANT_USE_DTM_FOR_INPUT          */

    { ER_CANT_USE_DTM_FOR_INPUT,
      "Nie mo\u017cna u\u017cy\u0107 DTMLiaison w wej\u015bciowym w\u0119\u017ale DOM... przeka\u017c zamiast tego org.apache.xpath.DOM2Helper!"},

  /** ER_CALL_TO_EXT_FAILED          */

    { ER_CALL_TO_EXT_FAILED,
      "Wywo\u0142anie elementu rozszerzenia nie powiod\u0142o si\u0119: {0}"},

  /** ER_PREFIX_MUST_RESOLVE          */

    { ER_PREFIX_MUST_RESOLVE,
      "Przedrostek musi da\u0107 si\u0119 przet\u0142umaczy\u0107 na przestrze\u0144 nazw: {0}"},

  /** ER_INVALID_UTF16_SURROGATE          */

    { ER_INVALID_UTF16_SURROGATE,
      "Wykryto niepoprawny surogat UTF-16: {0} ?"},

  /** ER_XSLATTRSET_USED_ITSELF          */

    { ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} u\u017cy\u0142o siebie, co wywo\u0142a niesko\u0144czon\u0105 p\u0119tl\u0119."},

  /** ER_CANNOT_MIX_XERCESDOM          */

    { ER_CANNOT_MIX_XERCESDOM,
      "Nie mo\u017cna miesza\u0107 wej\u015bcia innego ni\u017c Xerces-DOM z wyj\u015bciem Xerces-DOM!"},

  /** ER_TOO_MANY_LISTENERS          */

    { ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},

  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */

    { ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "W ElemTemplateElement.readObject: {0}"},

  /** ER_DUPLICATE_NAMED_TEMPLATE          */

    { ER_DUPLICATE_NAMED_TEMPLATE,
      "Znaleziono wi\u0119cej ni\u017c jeden szablon o nazwie {0}"},

  /** ER_INVALID_KEY_CALL          */

    { ER_INVALID_KEY_CALL,
      "Niepoprawne wywo\u0142anie funkcji: rekurencyjne wywo\u0142ania key() s\u0105 niedozwolone"},

  /** Variable is referencing itself          */

    { ER_REFERENCING_ITSELF,
      "Zmienna {0} odwo\u0142uje si\u0119 do siebie bezpo\u015brednio lub po\u015brednio!"},

  /** Illegal DOMSource input          */

    { ER_ILLEGAL_DOMSOURCE_INPUT,
      "W\u0119ze\u0142 wej\u015bciowy nie mo\u017ce by\u0107 pusty dla DOMSource dla newTemplates!"},

        /** Class not found for option         */

    { ER_CLASS_NOT_FOUND_FOR_OPTION,
        "Nie znaleziono pliku klasy dla opcji {0}"},

        /** Required Element not found         */

    { ER_REQUIRED_ELEM_NOT_FOUND,
        "Nie znaleziono wymaganego elementu {0}"},

  /** InputStream cannot be null         */

    { ER_INPUT_CANNOT_BE_NULL,
        "InputStream nie mo\u017ce by\u0107 pusty"},

  /** URI cannot be null         */

    { ER_URI_CANNOT_BE_NULL,
        "Identyfikator URI nie mo\u017ce by\u0107 pusty"},

  /** File cannot be null         */

    { ER_FILE_CANNOT_BE_NULL,
        "Plik nie mo\u017ce by\u0107 pusty"},

   /** InputSource cannot be null         */

    { ER_SOURCE_CANNOT_BE_NULL,
                "InputSource nie mo\u017ce by\u0107 pusty"},

  /** Can't overwrite cause         */


  /** Could not initialize BSF Manager        */

    { ER_CANNOT_INIT_BSFMGR,
                "Nie mo\u017cna zainicjowa\u0107 mened\u017cera BSF"},

  /** Could not compile extension       */

    { ER_CANNOT_CMPL_EXTENSN,
                "Nie mo\u017cna skompilowa\u0107 rozszerzenia"},

  /** Could not create extension       */

    { ER_CANNOT_CREATE_EXTENSN,
      "Nie mo\u017cna utworzy\u0107 rozszerzenia {0} z powodu  {1}"},

  /** Instance method call to method {0} requires an Object instance as first argument       */

    { ER_INSTANCE_MTHD_CALL_REQUIRES,
      "Wywo\u0142anie metody Instance do metody {0} wymaga instancji Object jako pierwszego argumentu"},

  /** Invalid element name specified       */

    { ER_INVALID_ELEMENT_NAME,
      "Podano niepoprawn\u0105 nazw\u0119 elementu {0}"},

   /** Element name method must be static      */

    { ER_ELEMENT_NAME_METHOD_STATIC,
      "Metoda nazwy elementu musi by\u0107 statyczna {0}"},

   /** Extension function {0} : {1} is unknown      */

    { ER_EXTENSION_FUNC_UNKNOWN,
             "Funkcja rozszerzenia {0} : {1} jest nieznana"},

   /** More than one best match for constructor for       */

    { ER_MORE_MATCH_CONSTRUCTOR,
             "Wi\u0119cej ni\u017c jedno najlepsze dopasowanie dla konstruktora {0}"},

   /** More than one best match for method      */

    { ER_MORE_MATCH_METHOD,
             "Wi\u0119cej ni\u017c jedno najlepsze dopasowanie dla metody {0}"},

   /** More than one best match for element method      */

    { ER_MORE_MATCH_ELEMENT,
             "Wi\u0119cej ni\u017c jedno najlepsze dopasowanie dla metody elementu {0}"},

   /** Invalid context passed to evaluate       */

    { ER_INVALID_CONTEXT_PASSED,
             "Przekazano niepoprawny kontekst do wyliczenia {0}"},

   /** Pool already exists       */

    { ER_POOL_EXISTS,
             "Pula ju\u017c istnieje"},

   /** No driver Name specified      */

    { ER_NO_DRIVER_NAME,
             "Nie podano nazwy sterownika"},

   /** No URL specified     */

    { ER_NO_URL,
             "Nie podano adresu URL"},

   /** Pool size is less than one    */

    { ER_POOL_SIZE_LESSTHAN_ONE,
             "Wielko\u015b\u0107 puli jest mniejsza od jedno\u015bci!"},

   /** Invalid driver name specified    */

    { ER_INVALID_DRIVER,
             "Podano niepoprawn\u0105 nazw\u0119 sterownika!"},

   /** Did not find the stylesheet root    */

    { ER_NO_STYLESHEETROOT,
             "Nie znaleziono elementu g\u0142\u00f3wnego arkusza styl\u00f3w!"},

   /** Illegal value for xml:space     */

    { ER_ILLEGAL_XMLSPACE_VALUE,
         "Niedozwolona warto\u015b\u0107 xml:space"},

   /** processFromNode failed     */

    { ER_PROCESSFROMNODE_FAILED,
         "processFromNode nie powiod\u0142o si\u0119"},

   /** The resource [] could not load:     */

    { ER_RESOURCE_COULD_NOT_LOAD,
        "Zas\u00f3b [ {0} ] nie m\u00f3g\u0142 za\u0142adowa\u0107: {1} \n {2} \t {3}"},


   /** Buffer size <=0     */

    { ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "Wielko\u015b\u0107 bufora <=0"},

   /** Unknown error when calling extension    */

    { ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "Nieznany b\u0142\u0105d podczas wywo\u0142ywania rozszerzenia"},

   /** Prefix {0} does not have a corresponding namespace declaration    */

    { ER_NO_NAMESPACE_DECL,
        "Przedrostek {0} nie ma odpowiadaj\u0105cej deklaracji przestrzeni nazw"},

   /** Element content not allowed for lang=javaclass   */

    { ER_ELEM_CONTENT_NOT_ALLOWED,
        "Zawarto\u015b\u0107 elementu niedozwolona dla lang=javaclass {0}"},

   /** Stylesheet directed termination   */

    { ER_STYLESHEET_DIRECTED_TERMINATION,
        "Arkusz styl\u00f3w zarz\u0105dzi\u0142 zako\u0144czenie"},

   /** 1 or 2   */

    { ER_ONE_OR_TWO,
        "1 lub 2"},

   /** 2 or 3   */

    { ER_TWO_OR_THREE,
        "2 lub 3"},

   /** Could not load {0} (check CLASSPATH), now using just the defaults   */

    { ER_COULD_NOT_LOAD_RESOURCE,
        "Nie mo\u017cna za\u0142adowa\u0107 {0} (sprawd\u017a CLASSPATH), u\u017cywane s\u0105 teraz warto\u015bci domy\u015blne"},

   /** Cannot initialize default templates   */

    { ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "Nie mo\u017cna zainicjowa\u0107 domy\u015blnych szablon\u00f3w"},

   /** Result should not be null   */

    { ER_RESULT_NULL,
        "Rezultat nie powinien by\u0107 pusty"},

   /** Result could not be set   */

    { ER_RESULT_COULD_NOT_BE_SET,
        "Nie mo\u017cna ustawi\u0107 rezultatu"},

   /** No output specified   */

    { ER_NO_OUTPUT_SPECIFIED,
        "Nie podano wyj\u015bcia"},

   /** Can't transform to a Result of type   */

    { ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "Nie mo\u017cna przekszta\u0142ci\u0107 do rezultatu o typie {0}"},

   /** Can't transform to a Source of type   */

    { ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "Nie mo\u017cna przekszta\u0142ci\u0107 \u017ar\u00f3d\u0142a o typie {0}"},

   /** Null content handler  */

    { ER_NULL_CONTENT_HANDLER,
        "Pusta procedura obs\u0142ugi zawarto\u015bci"},

   /** Null error handler  */
    { ER_NULL_ERROR_HANDLER,
        "Pusta procedura obs\u0142ugi b\u0142\u0119du"},

   /** parse can not be called if the ContentHandler has not been set */

    { ER_CANNOT_CALL_PARSE,
        "Nie mo\u017cna wywo\u0142a\u0107 parse, je\u015bli nie ustawiono ContentHandler"},

   /**  No parent for filter */

    { ER_NO_PARENT_FOR_FILTER,
        "Brak elementu nadrz\u0119dnego dla filtru"},


   /**  No stylesheet found in: {0}, media */

    { ER_NO_STYLESHEET_IN_MEDIA,
         "Nie znaleziono arkusza styl\u00f3w w {0}, no\u015bnik= {1}"},

   /**  No xml-stylesheet PI found in */

    { ER_NO_STYLESHEET_PI,
         "Nie znaleziono instrukcji przetwarzania xml-stylesheet w {0}"},

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
       "Nieobs\u0142ugiwane: {0}"},

   /**  node must be non-null for getDTMHandleFromNode  */


   /**  Could not resolve the node to a handle  */


   /**  startParse may not be called while parsing */


   /**  startParse needs a non-null SAXParser  */


   /**  could not initialize parser with */

   /**  Value for property {0} should be a Boolean instance  */

    { ER_PROPERTY_VALUE_BOOLEAN,
       "Warto\u015b\u0107 w\u0142a\u015bciwo\u015bci {0} powinna by\u0107 instancj\u0105 typu Boolean"},

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
         "Nie mo\u017cna si\u0119 dosta\u0107 do zewn\u0119trznego skryptu w {0}"},

  /** The resource [] could not be found     */

    { ER_RESOURCE_COULD_NOT_FIND,
        "Nie mo\u017cna znale\u017a\u0107 zasobu [ {0} ].\n {1}"},

   /** output property not recognized:  */

    { ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "Nierozpoznana w\u0142a\u015bciwo\u015b\u0107 wyj\u015bciowa {0}"},

   /** Userinfo may not be specified if host is not specified   */


   /** Port may not be specified if host is not specified   */


   /** Query string cannot be specified in path and query string   */


   /** Fragment cannot be specified in both the path and fragment   */


   /** Cannot initialize URI with empty parameters   */


   /** Failed creating ElemLiteralResult instance   */

    { ER_FAILED_CREATING_ELEMLITRSLT,
        "Nie powiod\u0142o si\u0119 utworzenie instancji ElemLiteralResult"},


   /** Priority value does not contain a parsable number   */

    { ER_VALUE_SHOULD_BE_NUMBER,
        "Warto\u015b\u0107 {0} powinna zawiera\u0107 liczb\u0119 mo\u017cliw\u0105 do zanalizowania"},

   /**  Value for {0} should equal 'yes' or 'no'   */

    { ER_VALUE_SHOULD_EQUAL,
        "Warto\u015bci\u0105 {0} powinno by\u0107 yes lub no"},

   /**  Failed calling {0} method   */

    { ER_FAILED_CALLING_METHOD,
        "Niepowodzenie wywo\u0142ania metody {0}"},

   /** Failed creating ElemLiteralResult instance   */

    { ER_FAILED_CREATING_ELEMTMPL,
        "Nie powiod\u0142o si\u0119 utworzenie instancji ElemTemplateElement"},

   /**  Characters are not allowed at this point in the document   */

    { ER_CHARS_NOT_ALLOWED,
        "W tym miejscu dokumentu znaki s\u0105 niedozwolone"},

  /**  attribute is not allowed on the element   */
    { ER_ATTR_NOT_ALLOWED,
        "Atrybut \"{0}\" nie jest dozwolony w elemencie {1}!"},

  /**  Method not yet supported    */


  /**  Bad value    */

    { ER_BAD_VALUE,
     "B\u0142\u0119dna warto\u015b\u0107 {0} {1}"},

  /**  attribute value not found   */

    { ER_ATTRIB_VALUE_NOT_FOUND,
     "Nie znaleziono warto\u015bci atrybutu {0}"},

  /**  attribute value not recognized    */

    { ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "Nie rozpoznano warto\u015bci atrybutu {0}"},

  /** IncrementalSAXSource_Filter not currently restartable   */


  /** IncrementalSAXSource_Filter not currently restartable   */


  /** Attempting to generate a namespace prefix with a null URI   */

    { ER_NULL_URI_NAMESPACE,
     "Pr\u00f3ba wygenerowania przedrostka przestrzeni nazw z pustym identyfikatorem URI"},


  /** Attempting to generate a namespace prefix with a null URI   */

    { ER_NUMBER_TOO_BIG,
     "Pr\u00f3ba sformatowania liczby wi\u0119kszej ni\u017c najwi\u0119ksza liczba typu long integer"},



    { ER_CANNOT_FIND_SAX1_DRIVER,
     "Nie mo\u017cna znale\u017a\u0107 klasy sterownika SAX1 {0}"},


    { ER_SAX1_DRIVER_NOT_LOADED,
     "Znaleziono klas\u0119 sterownika SAX1 {0}, ale nie mo\u017cna jej za\u0142adowa\u0107"},


    { ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "Klasa sterownika SAX1 {0} zosta\u0142a za\u0142adowana, ale nie mo\u017cna utworzy\u0107 jej instancji"},



    { ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "Klasa sterownika SAX1 {0} nie implementuje org.xml.sax.Parser"},


    { ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "W\u0142a\u015bciwo\u015b\u0107 systemowa org.xml.sax.parser nie zosta\u0142a podana"},


    { ER_PARSER_ARG_CANNOT_BE_NULL,
     "Argument analizatora nie mo\u017ce by\u0107 pusty"},



    { ER_FEATURE,
     "Opcja: {0}"},



    { ER_PROPERTY,
     "W\u0142a\u015bciwo\u015b\u0107: {0}"},


    { ER_NULL_ENTITY_RESOLVER,
     "Pusty przelicznik encji"},


    { ER_NULL_DTD_HANDLER,
     "Pusta procedura obs\u0142ugi DTD"},

    { ER_NO_DRIVER_NAME_SPECIFIED,
     "Nie podano nazwy sterownika!"},


    { ER_NO_URL_SPECIFIED,
     "Nie podano adresu URL!"},


    { ER_POOLSIZE_LESS_THAN_ONE,
     "Wielko\u015b\u0107 puli jest mniejsza od 1!"},


    { ER_INVALID_DRIVER_NAME,
     "Podano niepoprawn\u0105 nazw\u0119 sterownika!"},



    { ER_ERRORLISTENER,
     "ErrorListener"},


    { ER_ASSERT_NO_TEMPLATE_PARENT,
     "B\u0142\u0105d programisty! Wyra\u017cenie nie ma elementu nadrz\u0119dnego ElemTemplateElement!"},


    { ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "Asercja programisty w RundundentExprEliminator: {0}"},



    { ER_NOT_ALLOWED_IN_POSITION,
     "{0} jest niedozwolone na tej pozycji w arkuszu styl\u00f3w!"},

    { ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "Tekst z\u0142o\u017cony ze znak\u00f3w innych ni\u017c odst\u0119py jest niedozwolony na tej pozycji w arkuszu styl\u00f3w!"},

    { INVALID_TCHAR,
     "Niedozwolona warto\u015b\u0107 {1} u\u017cyta w atrybucie CHAR {0}.  Atrybut typu CHAR musi by\u0107 pojedynczym znakiem!"},




    { INVALID_QNAME,
     "Niedozwolona warto\u015b\u0107 {1} u\u017cyta w atrybucie QNAME {0}"},


    { INVALID_ENUM,
     "Niedozwolona warto\u015b\u0107 {1} u\u017cyta w atrybucie ENUM {0}.  Poprawne warto\u015bci to: {2}."},


    { INVALID_NMTOKEN,
     "Niedozwolona warto\u015b\u0107 {1} u\u017cyta w atrybucie NMTOKEN {0}"},


    { INVALID_NCNAME,
     "Niedozwolona warto\u015b\u0107 {1} u\u017cyta w atrybucie NCNAME {0}"},



    { INVALID_BOOLEAN,
     "Niedozwolona warto\u015b\u0107 {1} u\u017cyta w atrybucie boolowskim {0}"},


     { INVALID_NUMBER,
     "Niedozwolona warto\u015b\u0107 {1} u\u017cyta w atrybucie liczbowym {0}"},




    { ER_ARG_LITERAL,
     "Argument opcji {0} we wzorcu uzgadniania musi by\u0107 litera\u0142em."},


    { ER_DUPLICATE_GLOBAL_VAR,
     "Zduplikowana deklaracja zmiennej globalnej."},



    { ER_DUPLICATE_VAR,
     "Zduplikowana deklaracja zmiennej."},


    { ER_TEMPLATE_NAME_MATCH,
     "xsl:template musi mie\u0107 atrybut name lub match (lub obydwa)"},


    { ER_INVALID_PREFIX,
     "Przedrostek w exclude-result-prefixes jest niepoprawny: {0}"},


    { ER_NO_ATTRIB_SET,
     "Zbi\u00f3r atrybut\u00f3w o nazwie {0} nie istnieje"},






  /** WG_FOUND_CURLYBRACE          */
    { WG_FOUND_CURLYBRACE,
      "Znaleziono '}', ale nie jest otwarty \u017caden szablon atrybut\u00f3w!"},

  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */

    { WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "Ostrze\u017cenie: Atrybut count nie jest zgodny ze swym przodkiem w xsl:number! Cel = {0}"},

  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */

    { WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "Stara sk\u0142adnia: Nazwa atrybutu 'expr' zosta\u0142a zmieniona na 'select'."},

  /** WG_NO_LOCALE_IN_FORMATNUMBER          */

    { WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan nie obs\u0142uguje jeszcze nazwy lokalizacji w funkcji format-number."},

  /** WG_LOCALE_NOT_FOUND          */

    { WG_LOCALE_NOT_FOUND,
      "Ostrze\u017cenie: Nie mo\u017cna znale\u017a\u0107 ustawie\u0144 narodowych dla xml:lang={0}"},

  /** WG_CANNOT_MAKE_URL_FROM          */

    { WG_CANNOT_MAKE_URL_FROM,
      "Nie mo\u017cna utworzy\u0107 adresu URL z {0}"},

  /** WG_CANNOT_LOAD_REQUESTED_DOC          */

    { WG_CANNOT_LOAD_REQUESTED_DOC,
      "Nie mo\u017cna za\u0142adowa\u0107 \u017c\u0105danego dokumentu {0}"},

  /** WG_CANNOT_FIND_COLLATOR          */
    { WG_CANNOT_FIND_COLLATOR,
      "Nie mo\u017cna znale\u017a\u0107 Collator dla <sort xml:lang={0}"},

  /** WG_FUNCTIONS_SHOULD_USE_URL          */

    { WG_FUNCTIONS_SHOULD_USE_URL,
      "Stara sk\u0142adnia: Instrukcja functions powinna u\u017cywa\u0107 adresu url {0}"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */

    { WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "Kodowanie nieobs\u0142ugiwane: {0}, u\u017cywane jest UTF-8"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */

    { WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "Kodowanie nieobs\u0142ugiwane: {0}, u\u017cywane jest Java {1}"},

  /** WG_SPECIFICITY_CONFLICTS          */

    { WG_SPECIFICITY_CONFLICTS,
      "Znaleziono konflikty specyfiki {0}, u\u017cywany b\u0119dzie ostatni znaleziony w arkuszu styl\u00f3w."},

  /** WG_PARSING_AND_PREPARING          */

    { WG_PARSING_AND_PREPARING,
      "========= Analizowanie i przygotowywanie {0} =========="},

  /** WG_ATTR_TEMPLATE          */

    { WG_ATTR_TEMPLATE,
     "Szablon atrybutu {0}"},

  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */

    { WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "Konflikt zgodno\u015bci pomi\u0119dzy xsl:strip-space oraz xsl:preserve-space"},

  /** WG_ATTRIB_NOT_HANDLED          */

    { WG_ATTRIB_NOT_HANDLED,
      "Xalan nie obs\u0142uguje jeszcze atrybutu {0}!"},

  /** WG_NO_DECIMALFORMAT_DECLARATION          */

    { WG_NO_DECIMALFORMAT_DECLARATION,
      "Nie znaleziono deklaracji formatu dziesi\u0119tnego {0}"},

  /** WG_OLD_XSLT_NS          */

    { WG_OLD_XSLT_NS,
     "Nieobecna lub niepoprawna przestrze\u0144 nazw XSLT."},

  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */

    { WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "Dozwolona jest tylko jedna domy\u015blna deklaracja xsl:decimal-format."},

  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */

    { WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "Nazwy xsl:decimal-format musz\u0105 by\u0107 unikalne. Nazwa \"{0}\" zosta\u0142a zduplikowana."},

  /** WG_ILLEGAL_ATTRIBUTE          */

    { WG_ILLEGAL_ATTRIBUTE,
      "{0} ma niedozwolony atrybut {1}"},

  /** WG_COULD_NOT_RESOLVE_PREFIX          */

    { WG_COULD_NOT_RESOLVE_PREFIX,
      "Nie mo\u017cna przet\u0142umaczy\u0107 przedrostka przestrzeni nazw {0}. W\u0119ze\u0142 zostanie zignorowany."},

  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */
    { WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet wymaga atrybutu 'version'!"},

  /** WG_ILLEGAL_ATTRIBUTE_NAME          */

    { WG_ILLEGAL_ATTRIBUTE_NAME,
      "Niedozwolona nazwa atrybutu {0}"},

  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */
    { WG_ILLEGAL_ATTRIBUTE_VALUE,
      "Niedozwolona warto\u015b\u0107 atrybutu {0}: {1}"},

  /** WG_EMPTY_SECOND_ARG          */

    { WG_EMPTY_SECOND_ARG,
      "Wynikaj\u0105cy z drugiego argumentu funkcji document zestaw w\u0119z\u0142\u00f3w jest pusty. Zwracany jest pusty zestaw w\u0119z\u0142\u00f3w."},




  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */
    { WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "Warto\u015bci\u0105 atrybutu 'name' nazwy xsl:processing-instruction nie mo\u017ce by\u0107 'xml'"},


  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */
    { WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "Warto\u015bci\u0105 atrybutu 'name' xsl:processing-instruction musi by\u0107 poprawna nazwa NCName: {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */
    { WG_ILLEGAL_ATTRIBUTE_POSITION,
      "Nie mo\u017cna doda\u0107 atrybutu {0} po w\u0119z\u0142ach potomnych ani przed wyprodukowaniem elementu.  Atrybut zostanie zignorowany."},


  { "ui_language", "pl"},
  {  "help_language",  "pl" },
  {  "language",  "pl" },
  { "BAD_CODE", "Parametr createMessage by\u0142 spoza zakresu"},
  {  "FORMAT_FAILED", "Podczas wywo\u0142ania messageFormat zg\u0142oszony zosta\u0142 wyj\u0105tek"},
  {  "version", ">>>>>>> Wersja Xalan "},
  {  "version2",  "<<<<<<<"},
  {  "yes", "tak"},
  { "line", "Wiersz: "},
  { "column","Kolumna: "},
  { "xsldone", "XSLProcessor: gotowe"},


  { "xslProc_option", "Opcje wiersza komend klasy Process Xalan-J:"},
  { "xslProc_option", "Opcje wiersza komend klasy Process Xalan-J\u003a"},
  { "xslProc_invalid_xsltc_option", "Opcja {0} jest nieobs\u0142ugiwana w trybie XSLTC."},
  { "xslProc_invalid_xalan_option", "Opcji {0} mo\u017cna u\u017cywa\u0107 tylko razem z -XSLTC."},
  { "xslProc_no_input", "B\u0142\u0105d: Nie podano arkusza styl\u00f3w lub wej\u015bciowego pliku xml. Wykonaj t\u0119 komend\u0119 bez \u017cadnych opcji, aby zapozna\u0107 si\u0119 z informacjami o sk\u0142adni."},
  { "xslProc_common_options", "-Wsp\u00f3lne opcje-"},
  { "xslProc_xalan_options", "-Opcje dla Xalan-"},
  { "xslProc_xsltc_options", "-Opcje dla XSLTC-"},
  { "xslProc_return_to_continue", "(naci\u015bnij klawisz <enter>, aby kontynuowa\u0107)"},

  { "optionXSLTC", "   [-XSLTC (u\u017cycie XSLTC do transformacji)]"},
  { "optionIN", "   [-IN wej\u015bciowyXMLURL]"},
  { "optionXSL", "   [-XSL URLTransformacjiXSL]"},
  { "optionOUT",  "   [-OUT NazwaPlikuWyj\u015bciowego]"},
  { "optionLXCIN", "   [-LXCIN NazwaWej\u015bciowegoPlikuSkompilowanegoArkuszaStyl\u00f3w]"},
  { "optionLXCOUT", "   [-LXCOUT NazwaWyj\u015bciowegoPlikuSkompilowanegoArkuszaStyl\u00f3w]"},
  { "optionPARSER", "   [-PARSER pe\u0142na nazwa klasy po\u0142\u0105czenia analizatora]"},
  {  "optionE", "   [-E (bez rozwijania referencji encji)]"},
  {  "optionV",  "   [-E (bez rozwijania referencji encji)]"},
  {  "optionQC", "   [-QC (ciche ostrze\u017cenia o konfliktach wzorc\u00f3w)]"},
  {  "optionQ", "   [-Q  (tryb cichy)]"},
  {  "optionLF", "   [-LF (u\u017cycie tylko znak\u00f3w wysuwu wiersza na wyj\u015bciu {domy\u015blnie CR/LF})]"},
  {  "optionCR", "   [-LF (u\u017cycie tylko znak\u00f3w powrotu karetki na wyj\u015bciu {domy\u015blnie CR/LF})]"},
  { "optionESCAPE", "   [-ESCAPE (znaki o zmienionym znaczeniu {domy\u015blne <>&\"\'\\r\\n}]"},
  { "optionINDENT", "   [-INDENT (liczba znak\u00f3w wci\u0119cia {domy\u015blnie 0})]"},
  { "optionTT", "   [-TT (\u015bledzenie szablon\u00f3w podczas ich wywo\u0142ywania)]"},
  { "optionTG", "   [-TG (\u015bledzenie ka\u017cdego zdarzenia generowania)]"},
  { "optionTS", "   [-TS (\u015bledzenie ka\u017cdego zdarzenia wyboru)]"},
  {  "optionTTC", "   [-TTC (\u015bledzenie szablon\u00f3w potomnych podczas ich przetwarzania)]"},
  { "optionTCLASS", "   [-TCLASS (klasa TraceListener dla rozszerze\u0144 \u015bledzenia)]"},
  { "optionVALIDATE", "   [-VALIDATE (w\u0142\u0105czenie sprawdzania poprawno\u015bci - domy\u015blnie jest wy\u0142\u0105czona)]"},
  { "optionEDUMP", "   [-EDUMP {opcjonalna nazwa pliku} (wykonywanie zrzutu stosu w przypadku wyst\u0105pienia b\u0142\u0119du)]"},
  {  "optionXML", "   [-XML (u\u017cycie formatera XML i dodanie nag\u0142\u00f3wka XML)]"},
  {  "optionTEXT", "   [-TEXT (u\u017cycie prostego formatera tekstu)]"},
  {  "optionHTML", "   [-HTML (u\u017cycie formatera HTML)]"},
  {  "optionPARAM", "   [-PARAM nazwa wyra\u017cenie (ustawienie parametru arkusza styl\u00f3w)]"},
  {  "noParsermsg1", "Proces XSL nie wykona\u0142 si\u0119 pomy\u015blnie."},
  {  "noParsermsg2", "** Nie mo\u017cna znale\u017a\u0107 analizatora **"},
  { "noParsermsg3",  "Sprawd\u017a classpath."},
  { "noParsermsg4", "Je\u015bli nie masz analizatora XML dla j\u0119zyka Java firmy IBM, mo\u017cesz go pobra\u0107"},
  { "optionURIRESOLVER", "   [-URIRESOLVER pe\u0142na nazwa klasy (URIResolver u\u017cywany do t\u0142umaczenia URI)]"},
  { "optionENTITYRESOLVER",  "   [-ENTITYRESOLVER pe\u0142na nazwa klasy (EntityResolver u\u017cywany do t\u0142umaczenia encji)]"},
  { "optionCONTENTHANDLER",  "   [-CONTENTHANDLER pe\u0142na nazwa klasy (ContentHandler u\u017cywany do szeregowania wyj\u015bcia)]"},
  {  "optionLINENUMBERS",  "    [-L u\u017cycie numer\u00f3w wierszy w dokumentach \u017ar\u00f3d\u0142owych]"},



  {  "optionMEDIA",  "   [-MEDIA typ_no\u015bnika (u\u017cywaj atrybutu media w celu znalezienia arkusza styl\u00f3w zwi\u0105zanego z dokumentem)]"},
  { "optionDIAG", "   [-DIAG (wy\u015bwietlenie ca\u0142kowitego czasu trwania transformacji)]"},
  { "optionRL",  "   [-RL limit_rekurencji (okre\u015blenie liczbowego limitu g\u0142\u0119boko\u015bci rekurencji w arkuszach styl\u00f3w)]"},
  {   "optionXO",  "   [-XO [NazwaTransletu] (przypisanie nazwy wygenerowanemu transletowi)]"},
  {  "optionXD", "   [-XD KatalogDocelowy (okre\u015blenie katalogu docelowego dla transletu)]"},
  {  "optionXJ",  "   [-XJ plik_jar (pakowanie klas transletu do pliku jar o nazwie <plik_jar>)]"},
  {   "optionXP",  "   [-XP pakiet (okre\u015blenie przedrostka nazwy pakietu dla wszystkich wygenerowanych klas transletu)]"},

  { "optionXN",  "   [-XN (w\u0142\u0105czenie wstawiania szablon\u00f3w)]" },
  { "optionXX",  "   [-XX (w\u0142\u0105czenie dodatkowych diagnostycznych komunikat\u00f3w wyj\u015bciowych)]"},
  { "optionXT" , "   [-XT (u\u017cycie transletu do transformacji, je\u015bli to mo\u017cliwe)]"},
  { "diagTiming","--------- Transformacja {0} przez {1} zaj\u0119\u0142a {2} ms" },
  { "recursionTooDeep","Zbyt g\u0142\u0119bokie zagnie\u017cd\u017cenie szablon\u00f3w. zagnie\u017cd\u017cenie= {0}, szablon {1} {2}" },
  { "nameIs", "nazw\u0105 jest" },
  { "matchPatternIs", "wzorcem uzgadniania jest" }

  };


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "B\u0141\u0118DNY_KOD";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMATOWANIE_NIE_POWIOD\u0141O_SI\u0118";

  /** General error string.   */
  public static final String ERROR_STRING = "nr b\u0142\u0119du";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "B\u0142\u0105d: ";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "Ostrze\u017cenie: ";

  /** String to specify the XSLT module.  */
  public static final String XSL_HEADER = "XSLT ";

  /** String to specify the XML parser module.  */
  public static final String XML_HEADER = "XML ";

  /** I don't think this is used any more.
   * @deprecated  */
  public static final String QUERY_HEADER = "WZORZEC ";

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
                new Locale("pl", "PL"));
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
