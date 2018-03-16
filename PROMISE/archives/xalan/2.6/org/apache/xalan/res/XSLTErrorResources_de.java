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
public class XSLTErrorResources_de extends ListResourceBundle
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
      "Fehler: '{' darf nicht innerhalb des Ausdrucks stehen."},

  /** ER_ILLEGAL_ATTRIBUTE          */

    { ER_ILLEGAL_ATTRIBUTE ,
     "{0} hat ein unzul\u00e4ssiges Attribut {1}."},

  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */

  {ER_NULL_SOURCENODE_APPLYIMPORTS ,
      "sourceNode ist Null in xsl:apply-imports!"},

  /** ER_CANNOT_ADD          */

  {ER_CANNOT_ADD,
      "{0} kann nicht {1} hinzugef\u00fcgt werden."},


  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */


    { ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "sourceNode ist Null in handleApplyTemplatesInstruction!"},

  /** ER_NO_NAME_ATTRIB          */


    { ER_NO_NAME_ATTRIB,
     "{0} muss ein Namensattribut haben."},

  /** ER_TEMPLATE_NOT_FOUND          */


    {ER_TEMPLATE_NOT_FOUND,
     "Vorlage konnte nicht gefunden werden: {0}"},

  /** ER_CANT_RESOLVE_NAME_AVT          */

    {ER_CANT_RESOLVE_NAME_AVT,
      "Namensvorlage f\u00fcr den Attributwert in xsl:call-template konnte nicht aufgel\u00f6st werden."},

  /** ER_REQUIRES_ATTRIB          */


    {ER_REQUIRES_ATTRIB,
     "{0} erfordert das Attribut {1}."},

  /** ER_MUST_HAVE_TEST_ATTRIB          */


    { ER_MUST_HAVE_TEST_ATTRIB,
      "{0} muss \u00fcber ein Attribut ''test'' verf\u00fcgen."},

  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */


    {ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "Falscher Wert f\u00fcr Ebenenattribut: {0}."},

  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */


    {ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "Name der Verarbeitungsanweisung darf nicht 'xml' sein."},

  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */


    { ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "Name der Verarbeitungsanweisung muss ein g\u00fcltiges NCName-Format haben: {0}."},

  /** ER_NEED_MATCH_ATTRIB          */


    { ER_NEED_MATCH_ATTRIB,
      "{0} muss \u00fcber ein entsprechendes Attribut verf\u00fcgen, wenn ein Modus vorhanden ist."},

  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */


    { ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} erfordert einen Namen oder ein \u00dcbereinstimmungsattribut."},

  /** ER_CANT_RESOLVE_NSPREFIX          */


    {ER_CANT_RESOLVE_NSPREFIX,
      "Pr\u00e4fix des Namensbereichs kann nicht aufgel\u00f6st werden: {0}."},

  /** ER_ILLEGAL_VALUE          */


    { ER_ILLEGAL_VALUE,
     "xml:space weist einen ung\u00fcltigen Wert auf: {0}"},

  /** ER_NO_OWNERDOC          */


    { ER_NO_OWNERDOC,
      "Der Kindknoten hat kein Eignerdokument!"},

  /** ER_ELEMTEMPLATEELEM_ERR          */


    { ER_ELEMTEMPLATEELEM_ERR,
     "ElemTemplateElement-Fehler: {0}"},

  /** ER_NULL_CHILD          */


    { ER_NULL_CHILD,
     "Es wird versucht, ein leeres Kind hinzuzuf\u00fcgen!"},

  /** ER_NEED_SELECT_ATTRIB          */


    { ER_NEED_SELECT_ATTRIB,
     "{0} erfordert ein Attribut ''select''."},

  /** ER_NEED_TEST_ATTRIB          */


    { ER_NEED_TEST_ATTRIB ,
      "xsl:when muss \u00fcber ein Attribut 'test' verf\u00fcgen."},

  /** ER_NEED_NAME_ATTRIB          */


    { ER_NEED_NAME_ATTRIB,
      "xsl:with-param muss \u00fcber ein Attribut 'name' verf\u00fcgen."},

  /** ER_NO_CONTEXT_OWNERDOC          */


    { ER_NO_CONTEXT_OWNERDOC,
      "Der Kontextknoten verf\u00fcgt nicht \u00fcber ein Eignerdokument!"},

  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */


    {ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "XML-TransformerFactory-Liaison konnte nicht erstellt werden: {0}"},

  /** ER_PROCESS_NOT_SUCCESSFUL          */


    {ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan:-Prozess konnte nicht erfolgreich durchgef\u00fchrt werden."},

  /** ER_NOT_SUCCESSFUL          */


    { ER_NOT_SUCCESSFUL,
     "Xalan: war nicht erfolgreich."},

  /** ER_ENCODING_NOT_SUPPORTED          */


    { ER_ENCODING_NOT_SUPPORTED,
     "Verschl\u00fcsselung wird nicht unterst\u00fctzt: {0}."},

  /** ER_COULD_NOT_CREATE_TRACELISTENER          */


    {ER_COULD_NOT_CREATE_TRACELISTENER,
      "TraceListener konnte nicht erstellt werden: {0}."},

  /** ER_KEY_REQUIRES_NAME_ATTRIB          */


    {ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key erfordert ein Attribut 'name'!"},

  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */


    { ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key erfordert ein Attribut 'match'!"},

  /** ER_KEY_REQUIRES_USE_ATTRIB          */


    { ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key erfordert ein Attribut 'use'!"},

  /** ER_REQUIRES_ELEMENTS_ATTRIB          */


    { ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} erfordert ein Attribut ''elements''!"},

  /** ER_MISSING_PREFIX_ATTRIB          */


    { ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) {0}: Das Attribut ''prefix'' fehlt. "},

  /** ER_BAD_STYLESHEET_URL          */


    { ER_BAD_STYLESHEET_URL,
     "Formatvorlagen-URL-Adresse ist ung\u00fcltig: {0}."},

  /** ER_FILE_NOT_FOUND          */


    { ER_FILE_NOT_FOUND,
     "Formatvorlagendatei konnte nicht gefunden werden: {0}."},

  /** ER_IOEXCEPTION          */


    { ER_IOEXCEPTION,
      "Bei folgender Formatvorlagendatei ist eine E/A-Ausnahmebedingung aufgetreten: {0}."},

  /** ER_NO_HREF_ATTRIB          */


    { ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) Attribut 'href' f\u00fcr {0} konnte nicht gefunden werden."},

  /** ER_STYLESHEET_INCLUDES_ITSELF          */


    { ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} schlie\u00dft sich selbst direkt oder indirekt mit ein!"},

  /** ER_PROCESSINCLUDE_ERROR          */


    { ER_PROCESSINCLUDE_ERROR,
      "Fehler in StylesheetHandler.processInclude, {0}."},

  /** ER_MISSING_LANG_ATTRIB          */


    { ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) {0}: Das Attribut ''lang'' fehlt. "},

  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */

    { ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) Element {0} an falscher Position?? Fehlendes Containerelement ''component''. "},

  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */

    { ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "Ausgabe kann nur an ein Element, Dokumentfragment, Dokument oder Druckausgabeprogramm erfolgen."},

  /** ER_PROCESS_ERROR          */

    { ER_PROCESS_ERROR,
     "Fehler in StylesheetRoot.process"},

  /** ER_UNIMPLNODE_ERROR          */

    { ER_UNIMPLNODE_ERROR,
     "UnImplNode-Fehler: {0}"},

  /** ER_NO_SELECT_EXPRESSION          */

    { ER_NO_SELECT_EXPRESSION,
      "Fehler! xpath-Auswahlausdruck (-select) konnte nicht gefunden werden."},

  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */

    { ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "XSLProcessor kann nicht serialisiert werden!"},

  /** ER_NO_INPUT_STYLESHEET          */

    { ER_NO_INPUT_STYLESHEET,
      "Formatvorlageneingabe wurde nicht angegeben!"},

  /** ER_FAILED_PROCESS_STYLESHEET          */

    { ER_FAILED_PROCESS_STYLESHEET,
      "Verarbeitung der Formatvorlage fehlgeschlagen!"},

  /** ER_COULDNT_PARSE_DOC          */

    { ER_COULDNT_PARSE_DOC,
     "Dokument {0} konnte nicht syntaktisch analysiert werden!"},

  /** ER_COULDNT_FIND_FRAGMENT          */

    { ER_COULDNT_FIND_FRAGMENT,
     "Fragment konnte nicht gefunden werden: {0}."},

  /** ER_NODE_NOT_ELEMENT          */

    { ER_NODE_NOT_ELEMENT,
      "Der Knoten, auf den von einer Fragment-ID verwiesen wurde, war kein Element: {0}."},

  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */

    { ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "'for-each' muss entweder ein Attribut 'match' oder 'name' haben."},

  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */

    { ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "Vorlagen m\u00fcssen entweder ein Attribut 'match' oder 'name' haben."},

  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */

    { ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "Kein Klon eines Dokumentfragments!"},

  /** ER_CANT_CREATE_ITEM          */

    { ER_CANT_CREATE_ITEM,
      "Im Ergebnisbaum kann kein Eintrag erzeugt werden: {0}."},

  /** ER_XMLSPACE_ILLEGAL_VALUE          */

    { ER_XMLSPACE_ILLEGAL_VALUE,
      "xml:space in der Quellen-XML hat einen ung\u00fcltigen Wert: {0}."},

  /** ER_NO_XSLKEY_DECLARATION          */

    { ER_NO_XSLKEY_DECLARATION,
      "Keine Deklaration xsl:key f\u00fcr {0} vorhanden!"},

  /** ER_CANT_CREATE_URL          */

    { ER_CANT_CREATE_URL,
     "Fehler! URL kann nicht erstellt werden f\u00fcr: {0}"},

  /** ER_XSLFUNCTIONS_UNSUPPORTED          */

    { ER_XSLFUNCTIONS_UNSUPPORTED,
     "xsl:functions wird nicht unterst\u00fctzt."},

  /** ER_PROCESSOR_ERROR          */

    { ER_PROCESSOR_ERROR,
     "XSLT-TransformerFactory-Fehler"},

  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */

    { ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} nicht zul\u00e4ssig innerhalb einer Formatvorlage!"},

  /** ER_RESULTNS_NOT_SUPPORTED          */

    { ER_RESULTNS_NOT_SUPPORTED,
      "result-ns wird nicht mehr unterst\u00fctzt!  Verwenden Sie stattdessen xsl:output."},

  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */

    { ER_DEFAULTSPACE_NOT_SUPPORTED,
      "default-space wird nicht mehr unterst\u00fctzt!  Verwenden Sie stattdessen xsl:strip-space oder xsl:preserve-space."},

  /** ER_INDENTRESULT_NOT_SUPPORTED          */

    { ER_INDENTRESULT_NOT_SUPPORTED,
      "indent-result wird nicht mehr unterst\u00fctzt!  Verwenden Sie stattdessen xsl:output."},

  /** ER_ILLEGAL_ATTRIB          */

    { ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} hat ein ung\u00fcltiges Attribut: {1}."},

  /** ER_UNKNOWN_XSL_ELEM          */

    { ER_UNKNOWN_XSL_ELEM,
     "Unbekanntes XSL-Element: {0}"},

  /** ER_BAD_XSLSORT_USE          */

    { ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort kann nur mit xsl:apply-templates oder xsl:for-each verwendet werden."},

  /** ER_MISPLACED_XSLWHEN          */

    { ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) xsl:when steht an der falschen Position!"},

  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */

    { ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) F\u00fcr xsl:when ist xsl:choose nicht als Elter definiert!"},

  /** ER_MISPLACED_XSLOTHERWISE          */

    { ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) xsl:otherwise steht an der falschen Position!"},

  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */

    { ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) F\u00fcr xsl:otherwise ist xsl:choose nicht als Elter definiert!"},

  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */

    { ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} ist innerhalb einer Vorlage nicht zul\u00e4ssig!"},

  /** ER_UNKNOWN_EXT_NS_PREFIX          */

    { ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) {0}: Erweiterung des Namensbereichspr\u00e4fixes {1} ist unbekannt"},

  /** ER_IMPORTS_AS_FIRST_ELEM          */

    { ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) Importe k\u00f6nnen nur als erste Elemente in der Formatvorlage auftreten!"},

  /** ER_IMPORTING_ITSELF          */

    { ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} importiert sich direkt oder indirekt selbst!"},

  /** ER_XMLSPACE_ILLEGAL_VAL          */

    { ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) xml:space hat einen ung\u00fcltigen Wert: {0}. "},

  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */

    { ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "processStylesheet nicht erfolgreich!"},

  /** ER_SAX_EXCEPTION          */

    { ER_SAX_EXCEPTION,
     "SAX-Ausnahmebedingung"},

  /** ER_FUNCTION_NOT_SUPPORTED          */


  /** ER_XSLT_ERROR          */

    { ER_XSLT_ERROR,
     "XSLT-Fehler"},

  /** ER_CURRENCY_SIGN_ILLEGAL          */

    { ER_CURRENCY_SIGN_ILLEGAL,
      "Ein W\u00e4hrungssymbol ist in der Formatmusterzeichenfolge nicht zul\u00e4ssig."},

  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */

    { ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "Eine Dokumentfunktion wird in der Dokumentobjektmodell-Formatvorlage nicht unterst\u00fctzt!"},

  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */

    { ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "Pr\u00e4fix einer Aufl\u00f6sung ohne Pr\u00e4fix kann nicht aufgel\u00f6st werden!"},

  /** ER_REDIRECT_COULDNT_GET_FILENAME          */

    { ER_REDIRECT_COULDNT_GET_FILENAME,
      "Umleitungserweiterung: Dateiname konnte nicht abgerufen werden - Datei oder Attribut 'select' muss eine g\u00fcltige Zeichenfolge zur\u00fcckgeben. "},

  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */

    { ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "FormatterListener kann in Umleitungserweiterung nicht erstellt werden!"},

  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */

    { ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "Pr\u00e4fix in exclude-result-prefixes ist nicht g\u00fcltig: {0}."},

  /** ER_MISSING_NS_URI          */

    { ER_MISSING_NS_URI,
      "Fehlende Namensbereichs-URI f\u00fcr angegebenes Pr\u00e4fix."},

  /** ER_MISSING_ARG_FOR_OPTION          */

    { ER_MISSING_ARG_FOR_OPTION,
      "Fehlendes Argument f\u00fcr Option: {0}."},

  /** ER_INVALID_OPTION          */

    { ER_INVALID_OPTION,
     "Ung\u00fcltige Option: {0}"},

  /** ER_MALFORMED_FORMAT_STRING          */

    { ER_MALFORMED_FORMAT_STRING,
     "Syntaktisch falsche Formatzeichenfolge: {0}"},

  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

    { ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet erfordert ein Attribut 'version'!"},

  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */

    { ER_ILLEGAL_ATTRIBUTE_VALUE,
      "Attribut {0} weist einen ung\u00fcltigen Wert auf: {1}"},

  /** ER_CHOOSE_REQUIRES_WHEN          */

    { ER_CHOOSE_REQUIRES_WHEN,
     "xsl:choose erfordert xsl:when."},

  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */

    { ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports ist in xsl:for-each nicht zul\u00e4ssig."},

  /** ER_CANT_USE_DTM_FOR_OUTPUT          */

    { ER_CANT_USE_DTM_FOR_OUTPUT,
      "DTMLiaison kann nicht f\u00fcr einen Ausgabe-Dokumentobjektmodellknoten verwendet werden... \u00dcbergeben Sie stattdessen org.apache.xpath.DOM2Helper!"},

  /** ER_CANT_USE_DTM_FOR_INPUT          */

    { ER_CANT_USE_DTM_FOR_INPUT,
      "DTMLiaison kann nicht f\u00fcr einen Eingabe-Dokumentobjektmodellknoten verwendet werden... \u00dcbergeben Sie stattdessen org.apache.xpath.DOM2Helper!"},

  /** ER_CALL_TO_EXT_FAILED          */

    { ER_CALL_TO_EXT_FAILED,
      "Aufruf an Erweiterungselement fehlgeschlagen: {0}."},

  /** ER_PREFIX_MUST_RESOLVE          */

    { ER_PREFIX_MUST_RESOLVE,
      "Das Pr\u00e4fix muss in einen Namensbereich aufgel\u00f6st werden: {0}"},

  /** ER_INVALID_UTF16_SURROGATE          */

    { ER_INVALID_UTF16_SURROGATE,
      "Ung\u00fcltige UTF-16-Ersetzung festgestellt: {0} ?"},

  /** ER_XSLATTRSET_USED_ITSELF          */

    { ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} verwendet sich selbst, wodurch eine Endlosschleife verursacht wird."},

  /** ER_CANNOT_MIX_XERCESDOM          */

    { ER_CANNOT_MIX_XERCESDOM,
      "Nicht-Xerces-Dokumentobjektmodelleingabe kann nicht mit Xerces-Dokumentobjektmodellausgabe gemischt werden!"},

  /** ER_TOO_MANY_LISTENERS          */

    { ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},

  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */

    { ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "In ElemTemplateElement.readObject: {0}"},

  /** ER_DUPLICATE_NAMED_TEMPLATE          */

    { ER_DUPLICATE_NAMED_TEMPLATE,
      "Mehrere Vorlagen mit folgendem Namen gefunden: {0}."},

  /** ER_INVALID_KEY_CALL          */

    { ER_INVALID_KEY_CALL,
      "Ung\u00fcltiger Funktionsaufruf: rekursive Aufrufe 'key()'sind nicht zul\u00e4ssig."},

  /** Variable is referencing itself          */

    { ER_REFERENCING_ITSELF,
      "Variable {0} verweist direkt oder indirekt auf sich selbst!"},

  /** Illegal DOMSource input          */

    { ER_ILLEGAL_DOMSOURCE_INPUT,
      "Der Eingabeknoten kann f\u00fcr DOMSource f\u00fcr newTemplates nicht Null sein!"},

        /** Class not found for option         */

    { ER_CLASS_NOT_FOUND_FOR_OPTION,
        "Klassendatei f\u00fcr Option {0} wurde nicht gefunden."},

        /** Required Element not found         */

    { ER_REQUIRED_ELEM_NOT_FOUND,
        "Erforderliches Element nicht gefunden: {0}."},

  /** InputStream cannot be null         */

    { ER_INPUT_CANNOT_BE_NULL,
        "InputStream kann nicht Null sein."},

  /** URI cannot be null         */

    { ER_URI_CANNOT_BE_NULL,
        "URI kann nicht Null sein."},

  /** File cannot be null         */

    { ER_FILE_CANNOT_BE_NULL,
        "Eine Datei kann nicht Null sein."},

   /** InputSource cannot be null         */

    { ER_SOURCE_CANNOT_BE_NULL,
                "InputSource kann nicht Null sein."},

  /** Can't overwrite cause         */


  /** Could not initialize BSF Manager        */

    { ER_CANNOT_INIT_BSFMGR,
                "BSF Manager kann nicht initialisiert werden."},

  /** Could not compile extension       */

    { ER_CANNOT_CMPL_EXTENSN,
                "Erweiterung konnte nicht kompiliert werden."},

  /** Could not create extension       */

    { ER_CANNOT_CREATE_EXTENSN,
      "Erweiterung {0} konnte nicht erstellt werden. Ursache: {1}."},

  /** Instance method call to method {0} requires an Object instance as first argument       */

    { ER_INSTANCE_MTHD_CALL_REQUIRES,
      "Der Aufruf einer Exemplardefinitionsmethode von Methode {0} erfordert ein Objektexemplar als erstes Argument."},

  /** Invalid element name specified       */

    { ER_INVALID_ELEMENT_NAME,
      "Ung\u00fcltiger Elementname angegeben {0}."},

   /** Element name method must be static      */

    { ER_ELEMENT_NAME_METHOD_STATIC,
      "Elementnamenmethode muss statisch sein: {0}"},

   /** Extension function {0} : {1} is unknown      */

    { ER_EXTENSION_FUNC_UNKNOWN,
             "Erweiterungsfunktion {0} : {1} ist unbekannt."},

   /** More than one best match for constructor for       */

    { ER_MORE_MATCH_CONSTRUCTOR,
             "Mehrere passende Entsprechungen f\u00fcr Konstruktor f\u00fcr {0}."},

   /** More than one best match for method      */

    { ER_MORE_MATCH_METHOD,
             "Mehrere passende Entsprechungen f\u00fcr Methode {0}."},

   /** More than one best match for element method      */

    { ER_MORE_MATCH_ELEMENT,
             "Mehrere passende Entsprechungen f\u00fcr Elementmethode {0}."},

   /** Invalid context passed to evaluate       */

    { ER_INVALID_CONTEXT_PASSED,
             "Ung\u00fcltiger Kontext zur Auswertung von {0} \u00fcbergeben."},

   /** Pool already exists       */

    { ER_POOL_EXISTS,
             "Pool ist bereits vorhanden."},

   /** No driver Name specified      */

    { ER_NO_DRIVER_NAME,
             "Kein Treibername angegeben."},

   /** No URL specified     */

    { ER_NO_URL,
             "Keine URL-Adresse angegeben."},

   /** Pool size is less than one    */

    { ER_POOL_SIZE_LESSTHAN_ONE,
             "Poolgr\u00f6\u00dfe ist kleiner als Eins!"},

   /** Invalid driver name specified    */

    { ER_INVALID_DRIVER,
             "Ung\u00fcltiger Treibername angegeben!"},

   /** Did not find the stylesheet root    */

    { ER_NO_STYLESHEETROOT,
             "Root der Formatvorlage konnte nicht gefunden werden!"},

   /** Illegal value for xml:space     */

    { ER_ILLEGAL_XMLSPACE_VALUE,
         "Ung\u00fcltiger Wert f\u00fcr xml:space"},

   /** processFromNode failed     */

    { ER_PROCESSFROMNODE_FAILED,
         "processFromNode ist fehlgeschlagen."},

   /** The resource [] could not load:     */

    { ER_RESOURCE_COULD_NOT_LOAD,
        "Die Ressource [ {0} ] konnte nicht geladen werden: {1} \n {2} \t {3}"},


   /** Buffer size <=0     */

    { ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "Puffergr\u00f6\u00dfe <=0"},

   /** Unknown error when calling extension    */

    { ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "Unbekannter Fehler beim Aufrufen der Erweiterung."},

   /** Prefix {0} does not have a corresponding namespace declaration    */

    { ER_NO_NAMESPACE_DECL,
        "Pr\u00e4fix {0} hat keine entsprechende Namensbereichdeklaration."},

   /** Element content not allowed for lang=javaclass   */

    { ER_ELEM_CONTENT_NOT_ALLOWED,
        "Elementinhalt nicht zul\u00e4ssig f\u00fcr lang=javaclass {0}."},

   /** Stylesheet directed termination   */

    { ER_STYLESHEET_DIRECTED_TERMINATION,
        "Formatvorlage hat die Beendigung \u00fcbertragen."},

   /** 1 or 2   */

    { ER_ONE_OR_TWO,
        "1 oder 2"},

   /** 2 or 3   */

    { ER_TWO_OR_THREE,
        "2 oder 3"},

   /** Could not load {0} (check CLASSPATH), now using just the defaults   */

    { ER_COULD_NOT_LOAD_RESOURCE,
        "{0} (CLASSPATH pr\u00fcfen) konnte nicht geladen werden; es werden die Standardwerte verwendet."},

   /** Cannot initialize default templates   */

    { ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "Standardvorlagen k\u00f6nnen nicht initialisiert werden."},

   /** Result should not be null   */

    { ER_RESULT_NULL,
        "Das Ergebnis darf nicht Null sein."},

   /** Result could not be set   */

    { ER_RESULT_COULD_NOT_BE_SET,
        "Das Ergebnis konnte nicht festgelegt werden."},

   /** No output specified   */

    { ER_NO_OUTPUT_SPECIFIED,
        "Keine Ausgabe angegeben."},

   /** Can't transform to a Result of type   */

    { ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "Umsetzen in ein Ergebnis des Typs {0} ist nicht m\u00f6glich. "},

   /** Can't transform to a Source of type   */

    { ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "Umsetzen einer Quelle des Typs {0} ist nicht m\u00f6glich. "},

   /** Null content handler  */

    { ER_NULL_CONTENT_HANDLER,
        "Es ist keine Inhaltssteuerroutine vorhanden."},

   /** Null error handler  */
    { ER_NULL_ERROR_HANDLER,
        "Kein Fehlerbehandlungsprogramm vorhanden"},

   /** parse can not be called if the ContentHandler has not been set */

    { ER_CANNOT_CALL_PARSE,
        "Die Syntaxanalyse kann nicht aufgerufen werden, wenn ContentHandler nicht festgelegt wurde."},

   /**  No parent for filter */

    { ER_NO_PARENT_FOR_FILTER,
        "Kein Elter f\u00fcr Filter vorhanden"},


   /**  No stylesheet found in: {0}, media */

    { ER_NO_STYLESHEET_IN_MEDIA,
         "Keine Formatvorlage gefunden in: {0}, Datentr\u00e4ger= {1}."},

   /**  No xml-stylesheet PI found in */

    { ER_NO_STYLESHEET_PI,
         "Keine Verarbeitungsanweisung f\u00fcr xml-stylesheet gefunden in {0}."},

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
       "Nicht unterst\u00fctzt: {0}"},

   /**  node must be non-null for getDTMHandleFromNode  */


   /**  Could not resolve the node to a handle  */


   /**  startParse may not be called while parsing */


   /**  startParse needs a non-null SAXParser  */


   /**  could not initialize parser with */

   /**  Value for property {0} should be a Boolean instance  */

    { ER_PROPERTY_VALUE_BOOLEAN,
       "Der Wert f\u00fcr Merkmal {0} sollte ein Boolesches Exemplar sein."},

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
         "Externes Script bei {0} konnte nicht erreicht werden."},

  /** The resource [] could not be found     */

    { ER_RESOURCE_COULD_NOT_FIND,
        "Die Ressource [ {0} ] konnte nicht gefunden werden.\n {1}"},

   /** output property not recognized:  */

    { ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "Ausgabemerkmal nicht erkannt: {0}"},

   /** Userinfo may not be specified if host is not specified   */


   /** Port may not be specified if host is not specified   */


   /** Query string cannot be specified in path and query string   */


   /** Fragment cannot be specified in both the path and fragment   */


   /** Cannot initialize URI with empty parameters   */


   /** Failed creating ElemLiteralResult instance   */

    { ER_FAILED_CREATING_ELEMLITRSLT,
        "Das Erstellen des Exemplars ElemLiteralResult ist fehlgeschlagen."},


   /** Priority value does not contain a parsable number   */

    { ER_VALUE_SHOULD_BE_NUMBER,
        "Der Wert f\u00fcr {0} sollte eine syntaktisch analysierbare Zahl sein."},

   /**  Value for {0} should equal 'yes' or 'no'   */

    { ER_VALUE_SHOULD_EQUAL,
        "Der Wert f\u00fcr {0} sollte ''yes'' oder ''no'' entsprechen."},

   /**  Failed calling {0} method   */

    { ER_FAILED_CALLING_METHOD,
        "Aufruf von Methode {0} ist fehlgeschlagen"},

   /** Failed creating ElemLiteralResult instance   */

    { ER_FAILED_CREATING_ELEMTMPL,
        "Das Erstellen des Exemplars ElemTemplateElement ist fehlgeschlagen."},

   /**  Characters are not allowed at this point in the document   */

    { ER_CHARS_NOT_ALLOWED,
        "Zeichen sind an dieser Stelle im Dokument nicht zul\u00e4ssig."},

  /**  attribute is not allowed on the element   */
    { ER_ATTR_NOT_ALLOWED,
        "Das Attribut \"{0}\" ist im Element {1} nicht zul\u00e4ssig!"},

  /**  Method not yet supported    */


  /**  Bad value    */

    { ER_BAD_VALUE,
     "{0} ung\u00fcltiger Wert {1} "},

  /**  attribute value not found   */

    { ER_ATTRIB_VALUE_NOT_FOUND,
     "Attributwert {0} wurde nicht gefunden "},

  /**  attribute value not recognized    */

    { ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "Attributwert {0} wurde nicht erkannt "},

  /** IncrementalSAXSource_Filter not currently restartable   */


  /** IncrementalSAXSource_Filter not currently restartable   */


  /** Attempting to generate a namespace prefix with a null URI   */

    { ER_NULL_URI_NAMESPACE,
     "Es wird versucht, ein Namensbereichpr\u00e4fix mit einer Null-URI zu erzeugen."},


  /** Attempting to generate a namespace prefix with a null URI   */

    { ER_NUMBER_TOO_BIG,
     "Es wird versucht, eine gr\u00f6\u00dfere Zahl als die gr\u00f6\u00dfte erweiterte Ganzzahl zu formatieren."},



    { ER_CANNOT_FIND_SAX1_DRIVER,
     "SAX1-Treiberklasse {0} konnte nicht gefunden werden."},


    { ER_SAX1_DRIVER_NOT_LOADED,
     "SAX1-Treiberklasse {0} gefunden, kann aber nicht geladen werden."},


    { ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "SAX1-Treiberklasse {0} geladen, kann aber nicht instanziert werden."},



    { ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "SAX1-Treiberklasse {0} implementiert nicht org.xml.sax.Parser."},


    { ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "Systemmerkmal org.xml.sax.parser ist nicht angegeben."},


    { ER_PARSER_ARG_CANNOT_BE_NULL,
     "Parserargument darf nicht Null sein."},



    { ER_FEATURE,
     "Feature: {0}"},



    { ER_PROPERTY,
     "Merkmal: {0}"},


    { ER_NULL_ENTITY_RESOLVER,
     "Es ist keine Entit\u00e4tenaufl\u00f6sungsroutine vorhanden."},


    { ER_NULL_DTD_HANDLER,
     "Es ist keine Steuerroutine f\u00fcr Dokumenttypbeschreibungen vorhanden."},

    { ER_NO_DRIVER_NAME_SPECIFIED,
     "Kein Treibername angegeben!"},


    { ER_NO_URL_SPECIFIED,
     "Keine URL-Adresse angegeben!"},


    { ER_POOLSIZE_LESS_THAN_ONE,
     "Poolgr\u00f6\u00dfe ist kleiner als 1!"},


    { ER_INVALID_DRIVER_NAME,
     "Ung\u00fcltiger Treibername angegeben!"},



    { ER_ERRORLISTENER,
     "ErrorListener"},


    { ER_ASSERT_NO_TEMPLATE_PARENT,
     "Programmierfehler! 'expr' hat kein Elter ElemTemplateElement!"},


    { ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "Programmiererfestlegung in RedundentExprEliminator: {0} "},



    { ER_NOT_ALLOWED_IN_POSITION,
     "{0} ist an dieser Position in der Formatvorlage nicht zul\u00e4ssig!"},

    { ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "Anderer Text als Leerzeichen ist an dieser Position in der Formatvorlage nicht zul\u00e4ssig!"},

    { INVALID_TCHAR,
     "Unzul\u00e4ssiger Wert {1} f\u00fcr CHAR-Attribut verwendet: {0}.  Ein Attribut des Typs CHAR darf nur ein Zeichen umfassen!"},




    { INVALID_QNAME,
     "Unzul\u00e4ssiger Wert {1} f\u00fcr QNAME-Attribut verwendet: {0}"},


    { INVALID_ENUM,
     "Unzul\u00e4ssiger Wert {1} f\u00fcr ENUM-Attribut verwendet: {0}.  Folgende Werte sind g\u00fcltig: {2}."},


    { INVALID_NMTOKEN,
     "Unzul\u00e4ssiger Wert {1} f\u00fcr NMTOKEN-Attribut verwendet: {0}. "},


    { INVALID_NCNAME,
     "Unzul\u00e4ssiger Wert {1} f\u00fcr NCNAME-Attribut verwendet: {0}. "},



    { INVALID_BOOLEAN,
     "Unzul\u00e4ssiger Wert {1} f\u00fcr BOOLEAN-Attribut verwendet: {0}. "},


     { INVALID_NUMBER,
     "Unzul\u00e4ssiger Wert {1} f\u00fcr NUMBER-Attribut verwendet: {0}. "},




    { ER_ARG_LITERAL,
     "Argument von {0} in Suchmuster muss ein Literal sein."},


    { ER_DUPLICATE_GLOBAL_VAR,
     "Doppelte Deklaration einer globalen Variablen."},



    { ER_DUPLICATE_VAR,
     "Doppelte Deklaration einer Variablen."},


    { ER_TEMPLATE_NAME_MATCH,
     "xsl:template muss ein Attribut 'name' und/oder 'match' haben."},


    { ER_INVALID_PREFIX,
     "Pr\u00e4fix in exclude-result-prefixes ist nicht g\u00fcltig: {0}."},


    { ER_NO_ATTRIB_SET,
     "Die Attributgruppe {0} ist nicht vorhanden."},






  /** WG_FOUND_CURLYBRACE          */
    { WG_FOUND_CURLYBRACE,
      "'}' gefunden, es ist aber keine Attributvorlage ge\u00f6ffnet!"},

  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */

    { WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "Warnung: Attribut 'count' entspricht keinem \u00fcbergeordneten Fensterobjekt in xsl:number! Ziel = {0}"},

  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */

    { WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "Veraltete Syntax: Der Name des Attributs 'expr' wurde in 'select' ge\u00e4ndert."},

  /** WG_NO_LOCALE_IN_FORMATNUMBER          */

    { WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan bearbeitet noch nicht den L\u00e4ndereinstellungsnamen in der Funktion 'format-number'."},

  /** WG_LOCALE_NOT_FOUND          */

    { WG_LOCALE_NOT_FOUND,
      "Warnung: L\u00e4ndereinstellung f\u00fcr xml:lang={0} konnte nicht gefunden werden."},

  /** WG_CANNOT_MAKE_URL_FROM          */

    { WG_CANNOT_MAKE_URL_FROM,
      "URL konnte nicht erstellt werden aus: {0}"},

  /** WG_CANNOT_LOAD_REQUESTED_DOC          */

    { WG_CANNOT_LOAD_REQUESTED_DOC,
      "Angeforderte Dokumentation kann nicht geladen werden: {0}"},

  /** WG_CANNOT_FIND_COLLATOR          */
    { WG_CANNOT_FIND_COLLATOR,
      "Collator f\u00fcr <sort xml:lang={0} konnte nicht gefunden werden."},

  /** WG_FUNCTIONS_SHOULD_USE_URL          */

    { WG_FUNCTIONS_SHOULD_USE_URL,
      "Veraltete Syntax: Die Funktionsanweisung sollte eine URL-Adresse {0} verwenden."},

  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */

    { WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "Verschl\u00fcsselung nicht unterst\u00fctzt: {0}, UTF-8 wird verwendet."},

  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */

    { WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "Verschl\u00fcsselung nicht unterst\u00fctzt: {0}, Java {1} wird verwendet."},

  /** WG_SPECIFICITY_CONFLICTS          */

    { WG_SPECIFICITY_CONFLICTS,
      "Genauigkeitskonflikte gefunden: {0}. Die letzte Angabe in der Formatvorlage wird verwendet."},

  /** WG_PARSING_AND_PREPARING          */

    { WG_PARSING_AND_PREPARING,
      "========= Syntaxanalyse und Vorbereitung von {0} =========="},

  /** WG_ATTR_TEMPLATE          */

    { WG_ATTR_TEMPLATE,
     "Attributvorlage, {0}"},

  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */

    { WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "\u00dcbereinstimmungskonflikt zwischen xsl:strip-space und xsl:preserve-space"},

  /** WG_ATTRIB_NOT_HANDLED          */

    { WG_ATTRIB_NOT_HANDLED,
      "Xalan bearbeitet noch nicht das Attribut {0}!"},

  /** WG_NO_DECIMALFORMAT_DECLARATION          */

    { WG_NO_DECIMALFORMAT_DECLARATION,
      "Keine Deklaration f\u00fcr Dezimalformat gefunden: {0}"},

  /** WG_OLD_XSLT_NS          */

    { WG_OLD_XSLT_NS,
     "Fehlender oder ung\u00fcltiger XSLT-Namensbereich "},

  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */

    { WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "Nur eine Standarddeklaration xsl:decimal-format ist zul\u00e4ssig."},

  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */

    { WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "Namen in xsl:decimal-format m\u00fcssen eindeutig sein. Name \"{0}\" wurde dupliziert."},

  /** WG_ILLEGAL_ATTRIBUTE          */

    { WG_ILLEGAL_ATTRIBUTE,
      "{0} hat ein unzul\u00e4ssiges Attribut {1}."},

  /** WG_COULD_NOT_RESOLVE_PREFIX          */

    { WG_COULD_NOT_RESOLVE_PREFIX,
      "Namensbereichpr\u00e4fix konnte nicht aufgel\u00f6st werden: {0}. Der Knoten wird ignoriert."},

  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */
    { WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet erfordert ein Attribut 'version'!"},

  /** WG_ILLEGAL_ATTRIBUTE_NAME          */

    { WG_ILLEGAL_ATTRIBUTE_NAME,
      "Unzul\u00e4ssiger Attributname: {0}"},

  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */
    { WG_ILLEGAL_ATTRIBUTE_VALUE,
      "Unzul\u00e4ssiger Wert f\u00fcr Attribut {0} verwendet: {1}"},

  /** WG_EMPTY_SECOND_ARG          */

    { WG_EMPTY_SECOND_ARG,
      "Die Ergebnisknoteneinstellung des zweiten Arguments der Dokumentfunktion ist leer. Geben Sie eine leere Knotengruppe zur\u00fcck."},




  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */
    { WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "Der Wert des Attributs 'name' von xsl:processing-instruction darf nicht 'xml' sein."},


  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */
    { WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "Der Wert des Attributs 'name' von xsl:processing-instruction muss ein g\u00fcltiger NCName sein: {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */
    { WG_ILLEGAL_ATTRIBUTE_POSITION,
      "Attribut {0} kann nicht nach Kindknoten oder vor dem Erstellen eines Elements hinzugef\u00fcgt werden.  Das Attribut wird ignoriert."},


  { "ui_language", "de"},
  {  "help_language",  "de" },
  {  "language",  "de" },
  { "BAD_CODE", "Der Parameter f\u00fcr createMessage lag au\u00dferhalb des g\u00fcltigen Bereichs"},
  {  "FORMAT_FAILED", "W\u00e4hrend des Aufrufs von messageFormat wurde eine Ausnahmebedingung ausgel\u00f6st"},
  {  "version", ">>>>>>> Xalan-Version "},
  {  "version2",  "<<<<<<<"},
  {  "yes", "ja"},
  { "line", "Zeilennummer"},
  { "column","Spaltennummer"},
  { "xsldone", "XSLProcessor: fertig"},


  { "xslProc_option", "Optionen f\u00fcr Verarbeitungsklassen in der Xalan-J-Befehlszeile:"},
  { "xslProc_option", "Optionen f\u00fcr Verarbeitungsklassen in der Xalan-J-Befehlszeile\u003a"},
  { "xslProc_invalid_xsltc_option", "Die Option {0} wird im XSLTC-Modus nicht unterst\u00fctzt."},
  { "xslProc_invalid_xalan_option", "Die Option {0} kann nur mit -XSLTC verwendet werden."},
  { "xslProc_no_input", "Fehler: Es wurde keine Formatvorlagen- oder Eingabe-XML angegeben. F\u00fchren Sie diesen Befehl ohne Optionen f\u00fcr Syntaxanweisungen aus."},
  { "xslProc_common_options", "-Allgemeine Optionen-"},
  { "xslProc_xalan_options", "-Optionen f\u00fcr Xalan-"},
  { "xslProc_xsltc_options", "-Optionen f\u00fcr XSLTC-"},
  { "xslProc_return_to_continue", "(Dr\u00fccken Sie die Eingabetaste, um fortzufahren.)"},

  { "optionXSLTC", "[-XSLTC (XSLTC f\u00fcr Umsetzung verwenden)]"},
  { "optionIN", "[-IN EingabeXMLURL]"},
  { "optionXSL", "[-XSL XSLUmsetzungsURL]"},
  { "optionOUT",  "[-OUT AusgabeDateiName]"},
  { "optionLXCIN", "[-LXCIN kompilierteDateivorlageDateiNameEin]"},
  { "optionLXCOUT", "[-LXCOUT kompilierteDateivorlageDateiNameAus]"},
  { "optionPARSER", "[-PARSER vollst\u00e4ndig qualifizierter Klassenname der Parser-Liaison]"},
  {  "optionE", "[-E (Entit\u00e4tenverweise nicht erweitern)]"},
  {  "optionV",  "[-E (Entit\u00e4tenverweise nicht erweitern)]"},
  {  "optionQC", "[-QC (Unterdr\u00fcckte Musterkonfliktwarnungen)]"},
  {  "optionQ", "[-Q  (Unterdr\u00fcckter Modus)]"},
  {  "optionLF", "[-LF (Nur Zeilenvorschubzeichen bei Ausgabe verwenden {Standardeinstellung ist CR/LF})]"},
  {  "optionCR", "[-CR (Nur Zeilenschaltung bei Ausgabe verwenden {Standardeinstellung ist CR/LF})]"},
  { "optionESCAPE", "[-ESCAPE (Zeichen, die mit einem Escapezeichen angegeben werden m\u00fcssen {Standardeinstellung ist <>&\"\'\\r\\n}]"},
  { "optionINDENT", "[-INDENT (Steuerung, um wie viele Leerzeichen einger\u00fcckt werden soll {Standardeinstellung ist 0})]"},
  { "optionTT", "[-TT (Trace f\u00fcr Vorlagen ausf\u00fchren, wenn sie aufgerufen werden.)]"},
  { "optionTG", "[-TG (Trace f\u00fcr jedes Generierungsereignis durchf\u00fchren.)]"},
  { "optionTS", "[-TS (Trace f\u00fcr jedes Auswahlereignis durchf\u00fchren.)]"},
  {  "optionTTC", "[-TTC (Trace f\u00fcr die untergeordneten Vorlagen ausf\u00fchren, wenn sie verarbeitet werden.)]"},
  { "optionTCLASS", "[-TCLASS (TraceListener-Klasse f\u00fcr Trace-Erweiterungen.)]"},
  { "optionVALIDATE", "[-VALIDATE (Festlegen, ob eine G\u00fcltigkeitspr\u00fcfung erfolgen soll.  Die G\u00fcltigkeitspr\u00fcfung ist standardm\u00e4\u00dfig ausgeschaltet.)]"},
  { "optionEDUMP", "[-EDUMP {optionaler Dateiname} (Bei Fehler Stapelspeicherauszug erstellen.)]"},
  {  "optionXML", "[-XML (XML-Formatierungsprogramm verwenden und XML-Header hinzuf\u00fcgen.)]"},
  {  "optionTEXT", "[-TEXT (Einfaches Textformatierungsprogramm verwenden.)]"},
  {  "optionHTML", "[-HTML (HTML-Formatierungsprogramm verwenden.)]"},
  {  "optionPARAM", "[-PARAM Name Ausdruck (Festlegen eines Formatvorlagenparameters)]"},
  {  "noParsermsg1", "XSL-Prozess konnte nicht erfolgreich durchgef\u00fchrt werden."},
  {  "noParsermsg2", "** Parser konnte nicht gefunden werden **"},
  { "noParsermsg3",  "Bitte \u00fcberpr\u00fcfen Sie den Klassenpfad."},
  { "noParsermsg4", "Wenn Sie nicht \u00fcber einen IBM XML-Parser f\u00fcr Java verf\u00fcgen, k\u00f6nnen Sie ihn herunterladen:"},
  { "optionURIRESOLVER", "[-URIRESOLVER vollst\u00e4ndiger Klassenname (URIResolver wird zum Aufl\u00f6sen von URIs verwendet)]"},
  { "optionENTITYRESOLVER",  "[-ENTITYRESOLVER vollst\u00e4ndiger Klassenname (EntityResolver wird zum Aufl\u00f6sen von Entit\u00e4ten verwendet)]"},
  { "optionCONTENTHANDLER",  "[-CONTENTHANDLER vollst\u00e4ndiger Klassenname (ContentHandler wird zum Serialisieren der Ausgabe verwendet)]"},
  {  "optionLINENUMBERS",  "[-L Zeilennummern f\u00fcr das Quellendokument verwenden]"},



  {  "optionMEDIA",  "[-MEDIA Datentr\u00e4gerTyp (Datentr\u00e4gerattribut verwenden, um die einem Dokument zugeordnete Formatvorlage zu suchen.)]"},
  { "optionDIAG", "[-DIAG (Gesamtanzahl Millisekunden f\u00fcr die Umsetzung ausgeben.)]"},
  { "optionRL",  "[-RL Verschachtelungsbegrenzung (Numerische Begrenzung f\u00fcr Verschachtelungstiefe der Formatvorlage festlegen.)]"},
  {   "optionXO",  "[-XO [transletName] (Namen dem generierten Translet zuordnen)]"},
  {  "optionXD", "[-XD ZielVerzeichnis (Ein Zielverzeichnis f\u00fcr Translet angeben)]"},
  {  "optionXJ",  "[-XJ jardatei (Translet-Klassen in eine jar-Datei mit dem Namen <jardatei> packen)]"},
  {   "optionXP",  "[-XP paket (Ein Paketnamenpr\u00e4fix f\u00fcr alle generierten Translet-Klassen angeben)]"},

  { "optionXN",  "[-XN (Inline-Anordnung f\u00fcr Vorlagen aktivieren)]" },
  { "optionXX",  "[-XX (Zus\u00e4tzliche Debugnachrichtenausgabe aktivieren)]"},
  { "optionXT" , "[-XT (Translet f\u00fcr Umsetzung verwenden, wenn m\u00f6glich)]"},
  { "diagTiming","--------- Umsetzung von {0} \u00fcber {1} betrug {2} Millisekunden" },
  { "recursionTooDeep","Vorlagenverschachtelung ist zu stark. Verschachtelung = {0}, Vorlage {1} {2}" },
  { "nameIs", "Der Name ist" },
  { "matchPatternIs", "Das Suchmuster ist" }

  };


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "FEHLERHAFTER_CODE";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMAT_FEHLGESCHLAGEN";

  /** General error string.   */
  public static final String ERROR_STRING = "#Fehler";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "Fehler: ";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "Achtung: ";

  /** String to specify the XSLT module.  */
  public static final String XSL_HEADER = "XSLT ";

  /** String to specify the XML parser module.  */
  public static final String XML_HEADER = "XML ";

  /** I don't think this is used any more.
   * @deprecated  */
  public static final String QUERY_HEADER = "MUSTER ";

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
                new Locale("en", "US"));
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
