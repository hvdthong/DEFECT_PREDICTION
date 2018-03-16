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
public class XSLTErrorResources_pt_BR extends ListResourceBundle
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


  /** Get the lookup table for error messages.   
   *
   * @return The int to message lookup table.
   */
  public Object[][] getContents()
  {
    return new Object[][] {

  /** Error message ID that has a null message, but takes in a single object.    */
  {"ER0000" , "{0}" },


  /** ER_NO_CURLYBRACE          */


    { ER_NO_CURLYBRACE,
      "Erro: Imposs\u00edvel ter '{' na express\u00e3o"},

  /** ER_ILLEGAL_ATTRIBUTE          */

    { ER_ILLEGAL_ATTRIBUTE ,
     "{0} possui um atributo inv\u00e1lido: {1}"},

  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */

  {ER_NULL_SOURCENODE_APPLYIMPORTS ,
      "sourceNode \u00e9 nulo em xsl:apply-imports!"},

  /** ER_CANNOT_ADD          */

  {ER_CANNOT_ADD,
      "Imposs\u00edvel incluir {0} em {1}"},


  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */


    { ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "sourceNode \u00e9 nulo em handleApplyTemplatesInstruction!"},

  /** ER_NO_NAME_ATTRIB          */


    { ER_NO_NAME_ATTRIB,
     "{0} deve ter um atributo name."},

  /** ER_TEMPLATE_NOT_FOUND          */


    {ER_TEMPLATE_NOT_FOUND,
     "N\u00e3o foi poss\u00edvel localizar o gabarito: {0}"},

  /** ER_CANT_RESOLVE_NAME_AVT          */

    {ER_CANT_RESOLVE_NAME_AVT,
      "N\u00e3o foi poss\u00edvel resolver nome AVT em xsl:call-template."},

  /** ER_REQUIRES_ATTRIB          */


    {ER_REQUIRES_ATTRIB,
     "{0} requer o atributo: {1}"},

  /** ER_MUST_HAVE_TEST_ATTRIB          */


    { ER_MUST_HAVE_TEST_ATTRIB,
      "{0} deve ter um atributo ''test''."},

  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */


    {ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "Valor inv\u00e1lido no atributo de n\u00edvel: {0}"},

  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */


    {ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "O nome de processing-instruction n\u00e3o pode ser 'xml'"},

  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */


    { ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "O nome de processing-instruction deve ser um NCName v\u00e1lido: {0}"},

  /** ER_NEED_MATCH_ATTRIB          */


    { ER_NEED_MATCH_ATTRIB,
      "{0} deve ter um atributo de correspond\u00eancia se tiver um modo."},

  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */


    { ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} requer um nome ou um atributo de correspond\u00eancia."},

  /** ER_CANT_RESOLVE_NSPREFIX          */


    {ER_CANT_RESOLVE_NSPREFIX,
      "Imposs\u00edvel resolver prefixo de namespace: {0}"},

  /** ER_ILLEGAL_VALUE          */


    { ER_ILLEGAL_VALUE,
     "xml:space possui um valor inv\u00e1lido: {0}"},

  /** ER_NO_OWNERDOC          */


    { ER_NO_OWNERDOC,
      "O n\u00f3 filho n\u00e3o possui um documento do propriet\u00e1rio!"},

  /** ER_ELEMTEMPLATEELEM_ERR          */


    { ER_ELEMTEMPLATEELEM_ERR,
     "Erro de ElemTemplateElement: {0}"},

  /** ER_NULL_CHILD          */


    { ER_NULL_CHILD,
     "Tentando incluir um filho nulo!"},

  /** ER_NEED_SELECT_ATTRIB          */


    { ER_NEED_SELECT_ATTRIB,
     "{0} requer um atributo select."},

  /** ER_NEED_TEST_ATTRIB          */


    { ER_NEED_TEST_ATTRIB ,
      "xsl:when deve ter um atributo 'test'."},

  /** ER_NEED_NAME_ATTRIB          */


    { ER_NEED_NAME_ATTRIB,
      "xsl:with-param deve ter um atributo 'name'."},

  /** ER_NO_CONTEXT_OWNERDOC          */


    { ER_NO_CONTEXT_OWNERDOC,
      "context n\u00e3o possui um documento do propriet\u00e1rio!"},

  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */


    {ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "N\u00e3o foi poss\u00edvel criar XML TransformerFactory Liaison: {0}"},

  /** ER_PROCESS_NOT_SUCCESSFUL          */


    {ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan: O processo n\u00e3o foi bem-sucedido."},

  /** ER_NOT_SUCCESSFUL          */


    { ER_NOT_SUCCESSFUL,
     "Xalan: n\u00e3o foi bem-sucedido."},

  /** ER_ENCODING_NOT_SUPPORTED          */


    { ER_ENCODING_NOT_SUPPORTED,
     "Codifica\u00e7\u00e3o n\u00e3o suportada: {0}"},

  /** ER_COULD_NOT_CREATE_TRACELISTENER          */


    {ER_COULD_NOT_CREATE_TRACELISTENER,
      "N\u00e3o foi poss\u00edvel criar TraceListener: {0}"},

  /** ER_KEY_REQUIRES_NAME_ATTRIB          */


    {ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key requer um atributo 'name'!"},

  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


    { ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key requer um atributo 'match'!"},

  /** ER_KEY_REQUIRES_USE_ATTRIB          */


    { ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key requer um atributo 'use'!"},

  /** ER_REQUIRES_ELEMENTS_ATTRIB          */


    { ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} requer um atributo ''elements''!"},

  /** ER_MISSING_PREFIX_ATTRIB          */


    { ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) Est\u00e1 faltando o atributo ''prefix'' de {0}"},

  /** ER_BAD_STYLESHEET_URL          */


    { ER_BAD_STYLESHEET_URL,
     "A URL da p\u00e1gina de estilo \u00e9 inv\u00e1lida: {0}"},

  /** ER_FILE_NOT_FOUND          */


    { ER_FILE_NOT_FOUND,
     "O arquivo da p\u00e1gina de estilo n\u00e3o foi encontrado: {0}"},

  /** ER_IOEXCEPTION          */


    { ER_IOEXCEPTION,
      "Ocorreu uma Exce\u00e7\u00e3o de E/S (entrada/sa\u00edda) no arquivo de p\u00e1gina de estilo: {0}"},

  /** ER_NO_HREF_ATTRIB          */


    { ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) N\u00e3o foi poss\u00edvel encontrar o atributo href para {0}"},

  /** ER_STYLESHEET_INCLUDES_ITSELF          */


    { ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} est\u00e1 incluindo a si mesmo, direta ou indiretamente!"},

  /** ER_PROCESSINCLUDE_ERROR          */


    { ER_PROCESSINCLUDE_ERROR,
      "Erro de StylesheetHandler.processInclude, {0}"},

  /** ER_MISSING_LANG_ATTRIB          */


    { ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) Est\u00e1 faltando o atributo ''lang'' de {0}"},

  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */

    { ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) Elemento {0} aplicado incorretamente?? Est\u00e1 faltando o elemento de cont\u00eainer ''component''"},

  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */

    { ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "A sa\u00edda pode ser apenas para um Element, DocumentFragment, Document ou PrintWriter."},

  /** ER_PROCESS_ERROR          */

    { ER_PROCESS_ERROR,
     "Erro de StylesheetRoot.process"},

  /** ER_UNIMPLNODE_ERROR          */

    { ER_UNIMPLNODE_ERROR,
     "Erro de UnImplNode: {0}"},

  /** ER_NO_SELECT_EXPRESSION          */

    { ER_NO_SELECT_EXPRESSION,
      "Erro! N\u00e3o encontrada a express\u00e3o xpath select (-select)."},

  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */

    { ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "N\u00e3o \u00e9 poss\u00edvel serializar um XSLProcessor!"},

  /** ER_NO_INPUT_STYLESHEET          */

    { ER_NO_INPUT_STYLESHEET,
      "A entrada de folha de estilo n\u00e3o foi especificada!"},

  /** ER_FAILED_PROCESS_STYLESHEET          */

    { ER_FAILED_PROCESS_STYLESHEET,
      "Falha ao processar folha de estilo!"},

  /** ER_COULDNT_PARSE_DOC          */

    { ER_COULDNT_PARSE_DOC,
     "N\u00e3o foi poss\u00edvel analisar o documento {0}!"},

  /** ER_COULDNT_FIND_FRAGMENT          */

    { ER_COULDNT_FIND_FRAGMENT,
     "N\u00e3o foi poss\u00edvel localizar o fragmento: {0}"},

  /** ER_NODE_NOT_ELEMENT          */

    { ER_NODE_NOT_ELEMENT,
      "O n\u00f3 apontado por um identificador de fragmento n\u00e3o era um elemento: {0}"},

  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */

    { ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each deve ter um atributo match ou name"},

  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */

    { ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "templates deve ter um atributo match ou name"},

  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */

    { ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "Nenhum clone de fragmento de documento!"},

  /** ER_CANT_CREATE_ITEM          */

    { ER_CANT_CREATE_ITEM,
      "Imposs\u00edvel criar item na \u00e1rvore de resultados: {0}"},

  /** ER_XMLSPACE_ILLEGAL_VALUE          */

    { ER_XMLSPACE_ILLEGAL_VALUE,
      "xml:space no XML de origem possui um valor inv\u00e1lido: {0}"},

  /** ER_NO_XSLKEY_DECLARATION          */

    { ER_NO_XSLKEY_DECLARATION,
      "N\u00e3o existe nenhuma declara\u00e7\u00e3o xsl:key para {0}!"},

  /** ER_CANT_CREATE_URL          */

    { ER_CANT_CREATE_URL,
     "Erro! Imposs\u00edvel criar url para: {0}"},

  /** ER_XSLFUNCTIONS_UNSUPPORTED          */

    { ER_XSLFUNCTIONS_UNSUPPORTED,
     "xsl:functions n\u00e3o \u00e9 suportado"},

  /** ER_PROCESSOR_ERROR          */

    { ER_PROCESSOR_ERROR,
     "Erro de XSLT TransformerFactory"},

  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */

    { ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} n\u00e3o permitido dentro de uma folha de estilo!"},

  /** ER_RESULTNS_NOT_SUPPORTED          */

    { ER_RESULTNS_NOT_SUPPORTED,
      "result-ns n\u00e3o \u00e9 mais suportado!  Utilize ent\u00e3o xsl:output."},

  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */

    { ER_DEFAULTSPACE_NOT_SUPPORTED,
      "default-space n\u00e3o \u00e9 mais suportado!  Utilize ent\u00e3o xsl:strip-space ou xsl:preserve-space."},

  /** ER_INDENTRESULT_NOT_SUPPORTED          */

    { ER_INDENTRESULT_NOT_SUPPORTED,
      "indent-result n\u00e3o \u00e9 mais suportado!  Utilize ent\u00e3o xsl:output."},

  /** ER_ILLEGAL_ATTRIB          */

    { ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} possui um atributo inv\u00e1lido: {1}"},

  /** ER_UNKNOWN_XSL_ELEM          */

    { ER_UNKNOWN_XSL_ELEM,
     "Elemento XSL desconhecido: {0}"},

  /** ER_BAD_XSLSORT_USE          */

    { ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort somente pode ser utilizado com xsl:apply-templates ou xsl:for-each."},

  /** ER_MISPLACED_XSLWHEN          */

    { ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) xsl:when aplicado incorretamente!"},

  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */

    { ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:when n\u00e3o est\u00e1 ligado a xsl:choose!"},

  /** ER_MISPLACED_XSLOTHERWISE          */

    { ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) xsl:otherwise aplicado incorretamente!"},

  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */

    { ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:otherwise n\u00e3o est\u00e1 ligado a xsl:choose!"},

  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */

    { ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} n\u00e3o \u00e9 permitido dentro de um gabarito!"},

  /** ER_UNKNOWN_EXT_NS_PREFIX          */

    { ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) o namespace de extens\u00e3o {0} possui prefixo {1} desconhecido"},

  /** ER_IMPORTS_AS_FIRST_ELEM          */

    { ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) Importa\u00e7\u00f5es s\u00f3 podem ocorrer como os primeiros elementos na folha de estilo!"},

  /** ER_IMPORTING_ITSELF          */

    { ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} est\u00e1 importando a si mesmo, direta ou indiretamente!"},

  /** ER_XMLSPACE_ILLEGAL_VAL          */

    { ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) xml:space tem um valor inv\u00e1lido: {0}"},

  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */

    { ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "processStylesheet n\u00e3o obteve \u00eaxito!"},

  /** ER_SAX_EXCEPTION          */

    { ER_SAX_EXCEPTION,
     "Exce\u00e7\u00e3o de SAX"},

  /** ER_FUNCTION_NOT_SUPPORTED          */


  /** ER_XSLT_ERROR          */

    { ER_XSLT_ERROR,
     "Erro de XSLT"},

  /** ER_CURRENCY_SIGN_ILLEGAL          */

    { ER_CURRENCY_SIGN_ILLEGAL,
      "O sinal monet\u00e1rio n\u00e3o \u00e9 permitido na cadeia de padr\u00f5es de formato"},

  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */

    { ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "Fun\u00e7\u00e3o Document n\u00e3o suportada no DOM da Folha de Estilo!"},

  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */

    { ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "Imposs\u00edvel resolver prefixo de solucionador sem Prefixo!"},

  /** ER_REDIRECT_COULDNT_GET_FILENAME          */

    { ER_REDIRECT_COULDNT_GET_FILENAME,
      "Redirecionar extens\u00e3o: N\u00e3o foi poss\u00edvel obter o nome do arquivo - o atributo file ou select deve retornar uma cadeia v\u00e1lida."},

  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */

    { ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "Imposs\u00edvel construir FormatterListener em Redirecionar extens\u00e3o!"},

  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */

    { ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "O prefixo em exclude-result-prefixes n\u00e3o \u00e9 v\u00e1lido: {0}"},

  /** ER_MISSING_NS_URI          */

    { ER_MISSING_NS_URI,
      "URI de namespace ausente para o prefixo especificado"},

  /** ER_MISSING_ARG_FOR_OPTION          */

    { ER_MISSING_ARG_FOR_OPTION,
      "Argumento ausente para a op\u00e7\u00e3o: {0}"},

  /** ER_INVALID_OPTION          */

    { ER_INVALID_OPTION,
     "Op\u00e7\u00e3o inv\u00e1lida: {0}"},

  /** ER_MALFORMED_FORMAT_STRING          */

    { ER_MALFORMED_FORMAT_STRING,
     "Cadeia com problemas de formata\u00e7\u00e3o: {0}"},

  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

    { ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet requer um atributo 'version'!"},

  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */

    { ER_ILLEGAL_ATTRIBUTE_VALUE,
      "Atributo: {0} possui um valor inv\u00e1lido: {1}"},

  /** ER_CHOOSE_REQUIRES_WHEN          */

    { ER_CHOOSE_REQUIRES_WHEN,
     "xsl:choose requer um xsl:when"},

  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */

    { ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports n\u00e3o permitido em um xsl:for-each"},

  /** ER_CANT_USE_DTM_FOR_OUTPUT          */

    { ER_CANT_USE_DTM_FOR_OUTPUT,
      "Imposs\u00edvel utilizar um DTMLiaison para um n\u00f3 DOM de sa\u00edda... transmita um org.apache.xpath.DOM2Helper no lugar!"},

  /** ER_CANT_USE_DTM_FOR_INPUT          */

    { ER_CANT_USE_DTM_FOR_INPUT,
      "Imposs\u00edvel utilizar um DTMLiaison para um n\u00f3 DOM de entrada... transmita um org.apache.xpath.DOM2Helper no lugar!"},

  /** ER_CALL_TO_EXT_FAILED          */

    { ER_CALL_TO_EXT_FAILED,
      "Falha na chamada do elemento da extens\u00e3o: {0}"},

  /** ER_PREFIX_MUST_RESOLVE          */

    { ER_PREFIX_MUST_RESOLVE,
      "O prefixo deve ser resolvido para um namespace: {0}"},

  /** ER_INVALID_UTF16_SURROGATE          */

    { ER_INVALID_UTF16_SURROGATE,
      "Detectado substituto UTF-16 inv\u00e1lido: {0} ?"},

  /** ER_XSLATTRSET_USED_ITSELF          */

    { ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} utilizou a si mesmo, o que causar\u00e1 um loop infinito."},

  /** ER_CANNOT_MIX_XERCESDOM          */

    { ER_CANNOT_MIX_XERCESDOM,
      "Imposs\u00edvel misturar entrada n\u00e3o Xerces-DOM com sa\u00edda Xerces-DOM!"},

  /** ER_TOO_MANY_LISTENERS          */

    { ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},

  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */

    { ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "Em ElemTemplateElement.readObject: {0}"},

  /** ER_DUPLICATE_NAMED_TEMPLATE          */

    { ER_DUPLICATE_NAMED_TEMPLATE,
      "Encontrado mais de um gabarito chamado: {0}"},

  /** ER_INVALID_KEY_CALL          */

    { ER_INVALID_KEY_CALL,
      "Chamada de fun\u00e7\u00e3o inv\u00e1lida: chamadas key() recursivas n\u00e3o s\u00e3o permitidas"},

  /** Variable is referencing itself          */

    { ER_REFERENCING_ITSELF,
      "A vari\u00e1vel {0} est\u00e1 fazendo refer\u00eancia a si mesmo, direta ou indiretamente!"},

  /** Illegal DOMSource input          */

    { ER_ILLEGAL_DOMSOURCE_INPUT,
      "O n\u00f3 de entrada n\u00e3o pode ser nulo para um DOMSource de newTemplates!"},

        /** Class not found for option         */

    { ER_CLASS_NOT_FOUND_FOR_OPTION,
        "Arquivo de classe n\u00e3o encontrado para a op\u00e7\u00e3o {0}"},

        /** Required Element not found         */

    { ER_REQUIRED_ELEM_NOT_FOUND,
        "Elemento requerido n\u00e3o encontrado: {0}"},

  /** InputStream cannot be null         */

    { ER_INPUT_CANNOT_BE_NULL,
        "InputStream n\u00e3o pode ser nulo"},

  /** URI cannot be null         */

    { ER_URI_CANNOT_BE_NULL,
        "URI n\u00e3o pode ser nulo"},

  /** File cannot be null         */

    { ER_FILE_CANNOT_BE_NULL,
        "File n\u00e3o pode ser nulo"},

   /** InputSource cannot be null         */

    { ER_SOURCE_CANNOT_BE_NULL,
                "InputSource n\u00e3o pode ser nulo"},

  /** Can't overwrite cause         */


  /** Could not initialize BSF Manager        */

    { ER_CANNOT_INIT_BSFMGR,
                "N\u00e3o foi poss\u00edvel inicializar o BSF Manager"},

  /** Could not compile extension       */

    { ER_CANNOT_CMPL_EXTENSN,
                "N\u00e3o foi poss\u00edvel compilar a extens\u00e3o"},

  /** Could not create extension       */

    { ER_CANNOT_CREATE_EXTENSN,
      "N\u00e3o foi poss\u00edvel criar extens\u00e3o: {0} devido a: {1}"},

  /** Instance method call to method {0} requires an Object instance as first argument       */

    { ER_INSTANCE_MTHD_CALL_REQUIRES,
      "A chamada do m\u00e9todo da inst\u00e2ncia para o m\u00e9todo {0} requer uma inst\u00e2ncia Object como primeiro argumento"},

  /** Invalid element name specified       */

    { ER_INVALID_ELEMENT_NAME,
      "Especificado nome de elemento inv\u00e1lido {0}"},

   /** Element name method must be static      */

    { ER_ELEMENT_NAME_METHOD_STATIC,
      "O m\u00e9todo do nome de elemento deve ser est\u00e1tico {0}"},

   /** Extension function {0} : {1} is unknown      */

    { ER_EXTENSION_FUNC_UNKNOWN,
             "A fun\u00e7\u00e3o de extens\u00e3o {0} : {1} \u00e9 desconhecida"},

   /** More than one best match for constructor for       */

    { ER_MORE_MATCH_CONSTRUCTOR,
             "Mais de uma correspond\u00eancia principal para o construtor de {0}"},

   /** More than one best match for method      */

    { ER_MORE_MATCH_METHOD,
             "Mais de uma correspond\u00eancia principal para o m\u00e9todo {0}"},

   /** More than one best match for element method      */

    { ER_MORE_MATCH_ELEMENT,
             "Mais de uma correspond\u00eancia principal para o m\u00e9todo do elemento {0}"},

   /** Invalid context passed to evaluate       */

    { ER_INVALID_CONTEXT_PASSED,
             "Contexto inv\u00e1lido transmitido para avaliar {0}"},

   /** Pool already exists       */

    { ER_POOL_EXISTS,
             "O conjunto j\u00e1 existe"},

   /** No driver Name specified      */

    { ER_NO_DRIVER_NAME,
             "Nenhum Nome de driver foi especificado"},

   /** No URL specified     */

    { ER_NO_URL,
             "Nenhuma URL especificada"},

   /** Pool size is less than one    */

    { ER_POOL_SIZE_LESSTHAN_ONE,
             "O tamanho do conjunto \u00e9 menor que um!"},

   /** Invalid driver name specified    */

    { ER_INVALID_DRIVER,
             "Especificado nome de driver inv\u00e1lido!"},

   /** Did not find the stylesheet root    */

    { ER_NO_STYLESHEETROOT,
             "N\u00e3o encontrada a raiz da folha de estilo!"},

   /** Illegal value for xml:space     */

    { ER_ILLEGAL_XMLSPACE_VALUE,
         "Valor inv\u00e1lido para xml:space"},

   /** processFromNode failed     */

    { ER_PROCESSFROMNODE_FAILED,
         "processFromNode falhou"},

   /** The resource [] could not load:     */

    { ER_RESOURCE_COULD_NOT_LOAD,
        "O recurso [ {0} ] n\u00e3o p\u00f4de carregar: {1} \n {2} \t {3}"},


   /** Buffer size <=0     */

    { ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "Tamanho do buffer <=0"},

   /** Unknown error when calling extension    */

    { ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "Erro desconhecido ao chamar a extens\u00e3o"},

   /** Prefix {0} does not have a corresponding namespace declaration    */

    { ER_NO_NAMESPACE_DECL,
        "O prefixo {0} n\u00e3o possui uma declara\u00e7\u00e3o de namespace correspondente"},

   /** Element content not allowed for lang=javaclass   */

    { ER_ELEM_CONTENT_NOT_ALLOWED,
        "Conte\u00fado de elemento n\u00e3o permitido para lang=javaclass {0}"},

   /** Stylesheet directed termination   */

    { ER_STYLESHEET_DIRECTED_TERMINATION,
        "Finaliza\u00e7\u00e3o direcionada por folha de estilo"},

   /** 1 or 2   */

    { ER_ONE_OR_TWO,
        "1 ou 2"},

   /** 2 or 3   */

    { ER_TWO_OR_THREE,
        "2 ou 3"},

   /** Could not load {0} (check CLASSPATH), now using just the defaults   */

    { ER_COULD_NOT_LOAD_RESOURCE,
        "N\u00e3o foi poss\u00edvel carregar {0} (verificar CLASSPATH); utilizando apenas os padr\u00f5es"},

   /** Cannot initialize default templates   */

    { ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "Imposs\u00edvel inicializar gabaritos padr\u00e3o"},

   /** Result should not be null   */

    { ER_RESULT_NULL,
        "O resultado n\u00e3o deve ser nulo"},

   /** Result could not be set   */

    { ER_RESULT_COULD_NOT_BE_SET,
        "O resultado n\u00e3o p\u00f4de ser definido"},

   /** No output specified   */

    { ER_NO_OUTPUT_SPECIFIED,
        "Nenhuma sa\u00edda especificada"},

   /** Can't transform to a Result of type   */

    { ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "Imposs\u00edvel transformar em um Resultado do tipo {0}"},

   /** Can't transform to a Source of type   */

    { ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "Imposs\u00edvel transformar uma Origem do tipo {0}"},

   /** Null content handler  */

    { ER_NULL_CONTENT_HANDLER,
        "Rotina de tratamento de conte\u00fado nula"},

   /** Null error handler  */
    { ER_NULL_ERROR_HANDLER,
        "Rotina de tratamento de erros nula"},

   /** parse can not be called if the ContentHandler has not been set */

    { ER_CANNOT_CALL_PARSE,
        "parse n\u00e3o pode ser chamado se ContentHandler n\u00e3o tiver sido definido"},

   /**  No parent for filter */

    { ER_NO_PARENT_FOR_FILTER,
        "Nenhum pai para o filtro"},


   /**  No stylesheet found in: {0}, media */

    { ER_NO_STYLESHEET_IN_MEDIA,
         "Nenhuma p\u00e1gina de estilo foi encontrada em: {0}, m\u00eddia= {1}"},

   /**  No xml-stylesheet PI found in */

    { ER_NO_STYLESHEET_PI,
         "Nenhum PI xml-stylesheet encontrado em: {0}"},

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
       "N\u00e3o suportado: {0}"},

   /**  node must be non-null for getDTMHandleFromNode  */


   /**  Could not resolve the node to a handle  */


   /**  startParse may not be called while parsing */


   /**  startParse needs a non-null SAXParser  */


   /**  could not initialize parser with */

   /**  Value for property {0} should be a Boolean instance  */

    { ER_PROPERTY_VALUE_BOOLEAN,
       "O valor para a propriedade {0} deve ser uma inst\u00e2ncia Booleana"},

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
         "N\u00e3o foi poss\u00edvel obter script externo em {0}"},

  /** The resource [] could not be found     */

    { ER_RESOURCE_COULD_NOT_FIND,
        "O recurso [ {0} ] n\u00e3o p\u00f4de ser encontrado.\n{1}"},

   /** output property not recognized:  */

    { ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "Propriedade de sa\u00edda n\u00e3o reconhecida: {0}"},

   /** Userinfo may not be specified if host is not specified   */


   /** Port may not be specified if host is not specified   */


   /** Query string cannot be specified in path and query string   */


   /** Fragment cannot be specified in both the path and fragment   */


   /** Cannot initialize URI with empty parameters   */


   /** Failed creating ElemLiteralResult instance   */

    { ER_FAILED_CREATING_ELEMLITRSLT,
        "Falha ao criar a inst\u00e2ncia ElemLiteralResult"},


   /** Priority value does not contain a parsable number   */

    { ER_VALUE_SHOULD_BE_NUMBER,
        "O valor para {0} deve conter um n\u00famero analis\u00e1vel"},

   /**  Value for {0} should equal 'yes' or 'no'   */

    { ER_VALUE_SHOULD_EQUAL,
        "O valor de {0} deve ser igual a yes ou no"},

   /**  Failed calling {0} method   */

    { ER_FAILED_CALLING_METHOD,
        "Falha ao chamar o m\u00e9todo {0}"},

   /** Failed creating ElemLiteralResult instance   */

    { ER_FAILED_CREATING_ELEMTMPL,
        "Falha ao criar a inst\u00e2ncia ElemTemplateElement"},

   /**  Characters are not allowed at this point in the document   */

    { ER_CHARS_NOT_ALLOWED,
        "N\u00e3o s\u00e3o permitidos caracteres neste ponto do documento"},

  /**  attribute is not allowed on the element   */
    { ER_ATTR_NOT_ALLOWED,
        "O atributo \"{0}\" n\u00e3o \u00e9 permitido no elemento {1}!"},

  /**  Method not yet supported    */


  /**  Bad value    */

    { ER_BAD_VALUE,
     "{0} possui valor inv\u00e1lido {1}"},

  /**  attribute value not found   */

    { ER_ATTRIB_VALUE_NOT_FOUND,
     "Valor do atributo {0} n\u00e3o encontrado"},

  /**  attribute value not recognized    */

    { ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "Valor do atributo {0} n\u00e3o reconhecido"},

  /** IncrementalSAXSource_Filter not currently restartable   */


  /** IncrementalSAXSource_Filter not currently restartable   */


  /** Attempting to generate a namespace prefix with a null URI   */

    { ER_NULL_URI_NAMESPACE,
     "Tentando gerar um prefixo de namespace com URI nulo"},


  /** Attempting to generate a namespace prefix with a null URI   */

    { ER_NUMBER_TOO_BIG,
     "Tentando formatar um n\u00famero superior ao maior inteiro Longo"},



    { ER_CANNOT_FIND_SAX1_DRIVER,
     "Imposs\u00edvel encontrar a classe de driver SAX1 {0}"},


    { ER_SAX1_DRIVER_NOT_LOADED,
     "Classe de driver SAX1 {0} encontrada, mas n\u00e3o pode ser carregada"},


    { ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "Classe de driver SAX1 {0} carregada, mas n\u00e3o pode ser instanciada"},



    { ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "A classe de driver SAX1 {0} n\u00e3o implementa org.xml.sax.Parser"},


    { ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "Propriedade de sistema org.xml.sax.parser n\u00e3o especificada"},


    { ER_PARSER_ARG_CANNOT_BE_NULL,
     "O argumento Parser n\u00e3o deve ser nulo"},



    { ER_FEATURE,
     "Recurso: {0}"},



    { ER_PROPERTY,
     "Propriedade: {0}"},


    { ER_NULL_ENTITY_RESOLVER,
     "Solucionador de entidade nulo"},


    { ER_NULL_DTD_HANDLER,
     "Rotina de tratamento DTD nula"},

    { ER_NO_DRIVER_NAME_SPECIFIED,
     "Nenhum Nome de Driver Especificado!"},


    { ER_NO_URL_SPECIFIED,
     "Nenhuma URL Especificada!"},


    { ER_POOLSIZE_LESS_THAN_ONE,
     "O tamanho do conjunto \u00e9 menor que 1!"},


    { ER_INVALID_DRIVER_NAME,
     "Especificado Nome de Driver Inv\u00e1lido!"},



    { ER_ERRORLISTENER,
     "ErrorListener"},


    { ER_ASSERT_NO_TEMPLATE_PARENT,
     "Erro do programador! expr n\u00e3o possui pai ElemTemplateElement!"},


    { ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "Declara\u00e7\u00e3o do programador em RundundentExprEliminator: {0}"},



    { ER_NOT_ALLOWED_IN_POSITION,
     "{0} n\u00e3o \u00e9 permitido nesta posi\u00e7\u00e3o na p\u00e1gina de estilo!"},

    { ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "O texto sem espa\u00e7o em branco n\u00e3o \u00e9 permitido nesta posi\u00e7\u00e3o na p\u00e1gina de estilo!"},

    { INVALID_TCHAR,
     "Valor inv\u00e1lido: {1} utilizado para o caractere CHAR: {0}. Um atributo de tipo CHAR deve ter apenas 1 caractere!"},




    { INVALID_QNAME,
     "Valor inv\u00e1lido: {1} utilizado para o atributo QNAME: {0}"},


    { INVALID_ENUM,
     "Valor inv\u00e1lido: {1} utilizado para o atributo ENUM: {0}. Os valores v\u00e1lidos s\u00e3o: {2}."},


    { INVALID_NMTOKEN,
     "Valor inv\u00e1lido: {1} utilizado para o atributo NMTOKEN: {0}"},


    { INVALID_NCNAME,
     "Valor inv\u00e1lido: {1} utilizado para o atributo NCNAME: {0}"},



    { INVALID_BOOLEAN,
     "Valor inv\u00e1lido: {1} utilizado para o atributo boolean: {0}"},


     { INVALID_NUMBER,
     "Valor inv\u00e1lido: {1} utilizado para o atributo number: {0}"},




    { ER_ARG_LITERAL,
     "Argumento para {0} no padr\u00e3o de correspond\u00eancia deve ser um literal."},


    { ER_DUPLICATE_GLOBAL_VAR,
     "Declara\u00e7\u00e3o de vari\u00e1vel global duplicada."},



    { ER_DUPLICATE_VAR,
     "Declara\u00e7\u00e3o de vari\u00e1vel duplicada."},


    { ER_TEMPLATE_NAME_MATCH,
     "xsl:template deve ter um atributo name ou match (ou ambos)"},


    { ER_INVALID_PREFIX,
     "O prefixo em exclude-result-prefixes n\u00e3o \u00e9 v\u00e1lido: {0}"},


    { ER_NO_ATTRIB_SET,
     "O attribute-set {0} n\u00e3o existe"},






  /** WG_FOUND_CURLYBRACE          */
    { WG_FOUND_CURLYBRACE,
      "Encontrado '}', mas nenhum gabarito de atributo aberto!"},

  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */

    { WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "Aviso: o atributo count n\u00e3o corresponde a um predecessor em xsl:number! Destino = {0}"},

  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */

    { WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "Sintaxe antiga: O nome do atributo 'expr' foi alterado para 'select'."},

  /** WG_NO_LOCALE_IN_FORMATNUMBER          */

    { WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan ainda n\u00e3o trata do nome de locale na fun\u00e7\u00e3o format-number."},

  /** WG_LOCALE_NOT_FOUND          */

    { WG_LOCALE_NOT_FOUND,
      "Aviso: N\u00e3o foi poss\u00edvel localizar o locale para xml:lang={0}"},

  /** WG_CANNOT_MAKE_URL_FROM          */

    { WG_CANNOT_MAKE_URL_FROM,
      "Imposs\u00edvel criar URL a partir de: {0}"},

  /** WG_CANNOT_LOAD_REQUESTED_DOC          */

    { WG_CANNOT_LOAD_REQUESTED_DOC,
      "Imposs\u00edvel carregar doc solicitado: {0}"},

  /** WG_CANNOT_FIND_COLLATOR          */
    { WG_CANNOT_FIND_COLLATOR,
      "Imposs\u00edvel localizar Intercalador para <sort xml:lang={0}"},

  /** WG_FUNCTIONS_SHOULD_USE_URL          */

    { WG_FUNCTIONS_SHOULD_USE_URL,
      "Sintaxe antiga: a instru\u00e7\u00e3o functions deve utilizar uma url de {0}"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */

    { WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "codifica\u00e7\u00e3o n\u00e3o suportada: {0}, utilizando UTF-8"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */

    { WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "codifica\u00e7\u00e3o n\u00e3o suportada: {0}, utilizando Java {1}"},

  /** WG_SPECIFICITY_CONFLICTS          */

    { WG_SPECIFICITY_CONFLICTS,
      "Encontrados conflitos de especifica\u00e7\u00e3o: O \u00faltimo {0} encontrado na p\u00e1gina de estilo ser\u00e1 utilizado."},

  /** WG_PARSING_AND_PREPARING          */

    { WG_PARSING_AND_PREPARING,
      "========= An\u00e1lise e prepara\u00e7\u00e3o {0} =========="},

  /** WG_ATTR_TEMPLATE          */

    { WG_ATTR_TEMPLATE,
     "Gabarito de Atr, {0}"},

  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */

    { WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "Conflito de correspond\u00eancia entre xsl:strip-space e xsl:preserve-space"},

  /** WG_ATTRIB_NOT_HANDLED          */

    { WG_ATTRIB_NOT_HANDLED,
      "Xalan ainda n\u00e3o trata do atributo {0}!"},

  /** WG_NO_DECIMALFORMAT_DECLARATION          */

    { WG_NO_DECIMALFORMAT_DECLARATION,
      "Nenhuma declara\u00e7\u00e3o encontrada para formato decimal: {0}"},

  /** WG_OLD_XSLT_NS          */

    { WG_OLD_XSLT_NS,
     "Namespace XSLT ausente ou incorreto."},

  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */

    { WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "Apenas uma declara\u00e7\u00e3o padr\u00e3o xsl:decimal-format \u00e9 permitida."},

  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */

    { WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "Os nomes de xsl:decimal-format devem ser exclusivos. O nome \"{0}\" foi duplicado."},

  /** WG_ILLEGAL_ATTRIBUTE          */

    { WG_ILLEGAL_ATTRIBUTE,
      "{0} possui um atributo inv\u00e1lido: {1}"},

  /** WG_COULD_NOT_RESOLVE_PREFIX          */

    { WG_COULD_NOT_RESOLVE_PREFIX,
      "N\u00e3o foi poss\u00edvel resolver prefixo de namespace: {0}. O n\u00f3 ser\u00e1 ignorado."},

  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */
    { WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet requer um atributo 'version'!"},

  /** WG_ILLEGAL_ATTRIBUTE_NAME          */

    { WG_ILLEGAL_ATTRIBUTE_NAME,
      "Nome de atributo inv\u00e1lido: {0}"},

  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */
    { WG_ILLEGAL_ATTRIBUTE_VALUE,
      "Valor inv\u00e1lido utilizado para o atributo {0}: {1}"},

  /** WG_EMPTY_SECOND_ARG          */

    { WG_EMPTY_SECOND_ARG,
      "O nodeset resultante do segundo argumento da fun\u00e7\u00e3o document est\u00e1 vazio. Retornar um node-set vazio."},




  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */
    { WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "O valor do atributo 'name' do nome xsl:processing-instruction n\u00e3o deve ser 'xml'"},


  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */
    { WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "O valor do atributo 'name' de xsl:processing-instruction deve ser um NCName v\u00e1lido: {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */
    { WG_ILLEGAL_ATTRIBUTE_POSITION,
      "Imposs\u00edvel incluir atributo {0} depois de n\u00f3s filhos ou antes da gera\u00e7\u00e3o de um elemento. O atributo ser\u00e1 ignorado."},


  { "ui_language", "pt"},
  {  "help_language",  "pt" },
  {  "language",  "pt" },
  { "BAD_CODE", "O par\u00e2metro para createMessage estava fora dos limites"},
  {  "FORMAT_FAILED", "Exce\u00e7\u00e3o emitida durante chamada messageFormat"},
  {  "version", ">>>>>>> Vers\u00e3o Xalan"},
  {  "version2",  "<<<<<<<"},
  {  "yes", "sim"},
  { "line", "Linha n\u00b0"},
  { "column","Coluna n\u00b0"},
  { "xsldone", "XSLProcessor: conclu\u00eddo"},


  { "xslProc_option", "Op\u00e7\u00f5es da classe Process da linha de comando de Xalan-J:"},
  { "xslProc_option", "Op\u00e7\u00f5es da classe Process da linha de comandos de Xalan-J\u003a"},
  { "xslProc_invalid_xsltc_option", "A op\u00e7\u00e3o {0} n\u00e3o \u00e9 suportada no modo XSLTC."},
  { "xslProc_invalid_xalan_option", "A op\u00e7\u00e3o {0} somente pode ser utilizada com -XSLTC."},
  { "xslProc_no_input", "Erro: Nenhuma p\u00e1gina de estilo ou xml de entrada foi especificado. Execute este comando sem nenhuma op\u00e7\u00e3o para instru\u00e7\u00f5es de uso."},
  { "xslProc_common_options", "-Op\u00e7\u00f5es Comuns-"},
  { "xslProc_xalan_options", "-Op\u00e7\u00f5es para Xalan-"},
  { "xslProc_xsltc_options", "-Op\u00e7\u00f5es para XSLTC-"},
  { "xslProc_return_to_continue", "(pressione <return> para continuar)"},

  { "optionXSLTC", "   [-XSLTC (utilizar XSLTC para transforma\u00e7\u00e3o)]"},
  { "optionIN", "   [-IN inputXMLURL]"},
  { "optionXSL", "   [-XSL XSLTransformationURL]"},
  { "optionOUT",  "   [-OUT outputFileName]"},
  { "optionLXCIN", "   [-LXCIN compiledStylesheetFileNameIn]"},
  { "optionLXCOUT", "   [-LXCOUT compiledStylesheetFileNameOutOut]"},
  { "optionPARSER", "   [-PARSER nome completo da classe do analisador liaison]"},
  {  "optionE", "   [-E (N\u00e3o expandir refs de entidade)]"},
  {  "optionV",  "   [-E (N\u00e3o expandir refs de entidade)]"},
  {  "optionQC", "   [-QC (Avisos de Conflitos de Padr\u00e3o Silencioso)]"},
  {  "optionQ", "   [-Q  (Modo Silencioso)]"},
  {  "optionLF", "   [-LF (Utilizar avan\u00e7os de linha apenas na sa\u00edda {padr\u00e3o \u00e9 CR/LF})]"},
  {  "optionCR", "   [-CR (Utilizar retornos de carro apenas na sa\u00edda {padr\u00e3o \u00e9 CR/LF})]"},
  { "optionESCAPE", "   [-ESCAPE (Quais caracteres de escape {padr\u00e3o \u00e9 <>&\"\'\\r\\n}]"},
  { "optionINDENT", "   [-INDENT (Controlar como os espa\u00e7os s\u00e3o recuados {padr\u00e3o \u00e9 0})]"},
  { "optionTT", "   [-TT (Rastrear os gabaritos enquanto est\u00e3o sendo chamados.)]"},
  { "optionTG", "   [-TG (Rastrear cada evento de gera\u00e7\u00e3o.)]"},
  { "optionTS", "   [-TS (Rastrear cada evento de sele\u00e7\u00e3o.)]"},
  {  "optionTTC", "   [-TTC (Rastrear os filhos do modelo enquanto est\u00e3o sendo processados.)]"},
  { "optionTCLASS", "   [-TCLASS (Classe TraceListener para extens\u00f5es de rastreio.)]"},
  { "optionVALIDATE", "   [-VALIDATE (Definir se ocorrer valida\u00e7\u00e3o. A valida\u00e7\u00e3o fica desativada por padr\u00e3o.)]"},
  { "optionEDUMP", "   [-EDUMP {nome de arquivo opcional} (Executar stackdump sob erro.)]"},
  {  "optionXML", "   [-XML (Utilizar formatador XML e incluir cabe\u00e7alho XML.)]"},
  {  "optionTEXT", "   [-TEXT (Utilizar formatador de Texto simples.)]"},
  {  "optionHTML", "   [-HTML (Utilizar formatador HTML.)]"},
  {  "optionPARAM", "   [-PARAM express\u00e3o de nome (Definir um par\u00e2metro stylesheet)]"},
  {  "noParsermsg1", "O Processo XSL n\u00e3o obteve \u00eaxito."},
  {  "noParsermsg2", "** N\u00e3o foi poss\u00edvel encontrar o analisador **"},
  { "noParsermsg3",  "Verifique seu classpath."},
  { "noParsermsg4", "Se voc\u00ea n\u00e3o tiver o XML Parser para Java da IBM, poder\u00e1 fazer o download dele a partir de"},
  { "optionURIRESOLVER", "   [-URIRESOLVER nome completo da classe (URIResolver a ser utilizado para resolver URIs)]"},
  { "optionENTITYRESOLVER",  "   [-ENTITYRESOLVER nome completo da classe (EntityResolver a ser utilizado para resolver entidades)]"},
  { "optionCONTENTHANDLER",  "   [-CONTENTHANDLER nome completo da classe (ContentHandler a ser utilizado para serializar sa\u00edda)]"},
  {  "optionLINENUMBERS",  "   [-L utilizar n\u00fameros de linha para documento de origem]"},



  {  "optionMEDIA",  "   [-MEDIA mediaType (utilizar atributo de m\u00eddia para encontrar folha de estilo associada a um documento.)]"},
  { "optionDIAG", "   [-DIAG (Imprimir total de milissegundos que a transforma\u00e7\u00e3o gastou.)]"},
  { "optionRL",  "   [-RL recursionlimit (declarar limite num\u00e9rico em profundidade de recorr\u00eancia de folha de estilo.)]"},
  {   "optionXO",  "   [-XO [transletName] (atribuir nome ao translet gerado)]"},
  {  "optionXD", "   [-XD destinationDirectory (especificar um diret\u00f3rio de destino para translet)]"},
  {  "optionXJ",  "   [-XJ jarfile (empacota classes translet em um arquivo jar denominado <jarfile>)]"},
  {   "optionXP",  "   [-XP package (especifica um prefixo de nome de pacote para todas as classes translet geradas)]"},

  { "optionXN",  "   [-XN (ativa a seq\u00fc\u00eancia de gabaritos)]" },
  { "optionXX",  "   [-XX (ativa a sa\u00edda de mensagem de depura\u00e7\u00e3o adicional)]"},
  { "optionXT" , "   [-XT (utilizar translet para transforma\u00e7\u00e3o, se poss\u00edvel)]"},
  { "diagTiming"," --------- Transforma\u00e7\u00e3o de {0} via {1} levou {2} ms" },
  { "recursionTooDeep","Aninhamento de gabaritos muito extenso. aninhamento = {0}, gabarito {1} {2}" },
  { "nameIs", "o nome \u00e9" },
  { "matchPatternIs", "o padr\u00e3o de correspond\u00eancia \u00e9" }

  };
  }


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "BAD_CODE";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** General error string.   */
  public static final String ERROR_STRING = "#error";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "Erro: ";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "Aviso: ";

  /** String to specify the XSLT module.  */
  public static final String XSL_HEADER = "XSLT ";

  /** String to specify the XML parser module.  */
  public static final String XML_HEADER = "XML ";

  /** I don't think this is used any more.
   * @deprecated  */
  public static final String QUERY_HEADER = "PADR\u00c3O ";


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
                new Locale("pt", "BR"));
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
