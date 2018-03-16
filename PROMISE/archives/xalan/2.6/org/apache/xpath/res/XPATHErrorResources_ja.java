package org.apache.xpath.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Set up error messages.
 * We build a two dimensional array of message keys and
 * message strings. In order to add a new message here,
 * you need to first add a Static string constant for the
 * Key and update the contents array with Key, Value pair
  * Also you need to  update the count of messages(MAX_CODE)or
 * the count of warnings(MAX_WARNING) [ Information purpose only]
 * @xsl.usage advanced
 */
public class XPATHErrorResources_ja extends ListResourceBundle
{

/*
 * General notes to translators:
 *
 * This file contains error and warning messages related to XPath Error
 * Handling.
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
 *  8) The context node is the node in the document with respect to which an
 *     XPath expression is being evaluated.
 *
 *  9) An iterator is an object that traverses nodes in the tree, one at a time.
 *
 *  10) NCName is an XML term used to describe a name that does not contain a
 *     colon (a "no-colon name").
 *
 *  11) QName is an XML term meaning "qualified name".
 */

  /** Field MAX_CODE          */

  /** Field MAX_WARNING          */

  /** Field MAX_OTHERS          */
  public static final int MAX_OTHERS = 20;

  /** Field MAX_MESSAGES          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;


  /*
   * static variables
   */
  public static final String ERROR0000 = "ERROR0000";
  public static final String ER_CURRENT_NOT_ALLOWED_IN_MATCH =
         "ER_CURRENT_NOT_ALLOWED_IN_MATCH";
  public static final String ER_CURRENT_TAKES_NO_ARGS =
         "ER_CURRENT_TAKES_NO_ARGS";
  public static final String ER_DOCUMENT_REPLACED = "ER_DOCUMENT_REPLACED";
  public static final String ER_CONTEXT_HAS_NO_OWNERDOC =
         "ER_CONTEXT_HAS_NO_OWNERDOC";
  public static final String ER_LOCALNAME_HAS_TOO_MANY_ARGS =
         "ER_LOCALNAME_HAS_TOO_MANY_ARGS";
  public static final String ER_NAMESPACEURI_HAS_TOO_MANY_ARGS =
         "ER_NAMESPACEURI_HAS_TOO_MANY_ARGS";
  public static final String ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS =
         "ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS";
  public static final String ER_NUMBER_HAS_TOO_MANY_ARGS =
         "ER_NUMBER_HAS_TOO_MANY_ARGS";
  public static final String ER_NAME_HAS_TOO_MANY_ARGS =
         "ER_NAME_HAS_TOO_MANY_ARGS";
  public static final String ER_STRING_HAS_TOO_MANY_ARGS =
         "ER_STRING_HAS_TOO_MANY_ARGS";
  public static final String ER_STRINGLENGTH_HAS_TOO_MANY_ARGS =
         "ER_STRINGLENGTH_HAS_TOO_MANY_ARGS";
  public static final String ER_TRANSLATE_TAKES_3_ARGS =
         "ER_TRANSLATE_TAKES_3_ARGS";
  public static final String ER_UNPARSEDENTITYURI_TAKES_1_ARG =
         "ER_UNPARSEDENTITYURI_TAKES_1_ARG";
  public static final String ER_NAMESPACEAXIS_NOT_IMPLEMENTED =
         "ER_NAMESPACEAXIS_NOT_IMPLEMENTED";
  public static final String ER_UNKNOWN_AXIS = "ER_UNKNOWN_AXIS";
  public static final String ER_UNKNOWN_MATCH_OPERATION =
         "ER_UNKNOWN_MATCH_OPERATION";
  public static final String ER_INCORRECT_ARG_LENGTH ="ER_INCORRECT_ARG_LENGTH";
  public static final String ER_CANT_CONVERT_TO_NUMBER =
         "ER_CANT_CONVERT_TO_NUMBER";
  public static final String ER_CANT_CONVERT_TO_NODELIST =
         "ER_CANT_CONVERT_TO_NODELIST";
  public static final String ER_CANT_CONVERT_TO_MUTABLENODELIST =
         "ER_CANT_CONVERT_TO_MUTABLENODELIST";
  public static final String ER_CANT_CONVERT_TO_TYPE ="ER_CANT_CONVERT_TO_TYPE";
  public static final String ER_EXPECTED_MATCH_PATTERN =
         "ER_EXPECTED_MATCH_PATTERN";
  public static final String ER_COULDNOT_GET_VAR_NAMED =
         "ER_COULDNOT_GET_VAR_NAMED";
  public static final String ER_UNKNOWN_OPCODE = "ER_UNKNOWN_OPCODE";
  public static final String ER_EXTRA_ILLEGAL_TOKENS ="ER_EXTRA_ILLEGAL_TOKENS";
  public static final String ER_EXPECTED_DOUBLE_QUOTE =
         "ER_EXPECTED_DOUBLE_QUOTE";
  public static final String ER_EXPECTED_SINGLE_QUOTE =
         "ER_EXPECTED_SINGLE_QUOTE";
  public static final String ER_EMPTY_EXPRESSION = "ER_EMPTY_EXPRESSION";
  public static final String ER_EXPECTED_BUT_FOUND = "ER_EXPECTED_BUT_FOUND";
  public static final String ER_INCORRECT_PROGRAMMER_ASSERTION =
         "ER_INCORRECT_PROGRAMMER_ASSERTION";
  public static final String ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL =
         "ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL";
  public static final String ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG =
         "ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG";
  public static final String ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG =
         "ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG";
  public static final String ER_PREDICATE_ILLEGAL_SYNTAX =
         "ER_PREDICATE_ILLEGAL_SYNTAX";
  public static final String ER_ILLEGAL_AXIS_NAME = "ER_ILLEGAL_AXIS_NAME";
  public static final String ER_UNKNOWN_NODETYPE = "ER_UNKNOWN_NODETYPE";
  public static final String ER_PATTERN_LITERAL_NEEDS_BE_QUOTED =
         "ER_PATTERN_LITERAL_NEEDS_BE_QUOTED";
  public static final String ER_COULDNOT_BE_FORMATTED_TO_NUMBER =
         "ER_COULDNOT_BE_FORMATTED_TO_NUMBER";
  public static final String ER_COULDNOT_CREATE_XMLPROCESSORLIAISON =
         "ER_COULDNOT_CREATE_XMLPROCESSORLIAISON";
  public static final String ER_DIDNOT_FIND_XPATH_SELECT_EXP =
         "ER_DIDNOT_FIND_XPATH_SELECT_EXP";
  public static final String ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH =
         "ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH";
  public static final String ER_ERROR_OCCURED = "ER_ERROR_OCCURED";
  public static final String ER_ILLEGAL_VARIABLE_REFERENCE =
         "ER_ILLEGAL_VARIABLE_REFERENCE";
  public static final String ER_AXES_NOT_ALLOWED = "ER_AXES_NOT_ALLOWED";
  public static final String ER_KEY_HAS_TOO_MANY_ARGS =
         "ER_KEY_HAS_TOO_MANY_ARGS";
  public static final String ER_COUNT_TAKES_1_ARG = "ER_COUNT_TAKES_1_ARG";
  public static final String ER_COULDNOT_FIND_FUNCTION =
         "ER_COULDNOT_FIND_FUNCTION";
  public static final String ER_UNSUPPORTED_ENCODING ="ER_UNSUPPORTED_ENCODING";
  public static final String ER_PROBLEM_IN_DTM_NEXTSIBLING =
         "ER_PROBLEM_IN_DTM_NEXTSIBLING";
  public static final String ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL =
         "ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL";
  public static final String ER_SETDOMFACTORY_NOT_SUPPORTED =
         "ER_SETDOMFACTORY_NOT_SUPPORTED";
  public static final String ER_PREFIX_MUST_RESOLVE = "ER_PREFIX_MUST_RESOLVE";
  public static final String ER_PARSE_NOT_SUPPORTED = "ER_PARSE_NOT_SUPPORTED";
  public static final String ER_SAX_API_NOT_HANDLED = "ER_SAX_API_NOT_HANDLED";
public static final String ER_IGNORABLE_WHITESPACE_NOT_HANDLED =
         "ER_IGNORABLE_WHITESPACE_NOT_HANDLED";
  public static final String ER_DTM_CANNOT_HANDLE_NODES =
         "ER_DTM_CANNOT_HANDLE_NODES";
  public static final String ER_XERCES_CANNOT_HANDLE_NODES =
         "ER_XERCES_CANNOT_HANDLE_NODES";
  public static final String ER_XERCES_PARSE_ERROR_DETAILS =
         "ER_XERCES_PARSE_ERROR_DETAILS";
  public static final String ER_XERCES_PARSE_ERROR = "ER_XERCES_PARSE_ERROR";
  public static final String ER_INVALID_UTF16_SURROGATE =
         "ER_INVALID_UTF16_SURROGATE";
  public static final String ER_OIERROR = "ER_OIERROR";
  public static final String ER_CANNOT_CREATE_URL = "ER_CANNOT_CREATE_URL";
  public static final String ER_XPATH_READOBJECT = "ER_XPATH_READOBJECT";
 public static final String ER_FUNCTION_TOKEN_NOT_FOUND =
         "ER_FUNCTION_TOKEN_NOT_FOUND";
  public static final String ER_CANNOT_DEAL_XPATH_TYPE =
         "ER_CANNOT_DEAL_XPATH_TYPE";
  public static final String ER_NODESET_NOT_MUTABLE = "ER_NODESET_NOT_MUTABLE";
  public static final String ER_NODESETDTM_NOT_MUTABLE =
         "ER_NODESETDTM_NOT_MUTABLE";
   /**  Variable not resolvable:   */
  public static final String ER_VAR_NOT_RESOLVABLE = "ER_VAR_NOT_RESOLVABLE";
   /** Null error handler  */
 public static final String ER_NULL_ERROR_HANDLER = "ER_NULL_ERROR_HANDLER";
   /**  Programmer's assertion: unknown opcode  */
  public static final String ER_PROG_ASSERT_UNKNOWN_OPCODE =
         "ER_PROG_ASSERT_UNKNOWN_OPCODE";
   /**  0 or 1   */
  public static final String ER_ZERO_OR_ONE = "ER_ZERO_OR_ONE";
   /**  rtf() not supported by XRTreeFragSelectWrapper   */
  public static final String ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER =
         "ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER";
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */
  public static final String ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER = "ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER";
   /**  fsb() not supported for XStringForChars   */
  public static final String ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS =
         "ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS";
   /**  Could not find variable with the name of   */
 public static final String ER_COULD_NOT_FIND_VAR = "ER_COULD_NOT_FIND_VAR";
   /**  XStringForChars can not take a string for an argument   */
 public static final String ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING =
         "ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING";
   /**  The FastStringBuffer argument can not be null   */
 public static final String ER_FASTSTRINGBUFFER_CANNOT_BE_NULL =
         "ER_FASTSTRINGBUFFER_CANNOT_BE_NULL";
   /**  2 or 3   */
  public static final String ER_TWO_OR_THREE = "ER_TWO_OR_THREE";
   /** Variable accessed before it is bound! */
  public static final String ER_VARIABLE_ACCESSED_BEFORE_BIND =
         "ER_VARIABLE_ACCESSED_BEFORE_BIND";
   /** XStringForFSB can not take a string for an argument! */
 public static final String ER_FSB_CANNOT_TAKE_STRING =
         "ER_FSB_CANNOT_TAKE_STRING";
   /** Error! Setting the root of a walker to null! */
  public static final String ER_SETTING_WALKER_ROOT_TO_NULL =
         "ER_SETTING_WALKER_ROOT_TO_NULL";
   /** This NodeSetDTM can not iterate to a previous node! */
  public static final String ER_NODESETDTM_CANNOT_ITERATE =
         "ER_NODESETDTM_CANNOT_ITERATE";
  /** This NodeSet can not iterate to a previous node! */
 public static final String ER_NODESET_CANNOT_ITERATE =
         "ER_NODESET_CANNOT_ITERATE";
  /** This NodeSetDTM can not do indexing or counting functions! */
  public static final String ER_NODESETDTM_CANNOT_INDEX =
         "ER_NODESETDTM_CANNOT_INDEX";
  /** This NodeSet can not do indexing or counting functions! */
  public static final String ER_NODESET_CANNOT_INDEX =
         "ER_NODESET_CANNOT_INDEX";
  /** Can not call setShouldCacheNodes after nextNode has been called! */
  public static final String ER_CANNOT_CALL_SETSHOULDCACHENODE =
         "ER_CANNOT_CALL_SETSHOULDCACHENODE";
  /** {0} only allows {1} arguments */
 public static final String ER_ONLY_ALLOWS = "ER_ONLY_ALLOWS";
  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */
  public static final String ER_UNKNOWN_STEP = "ER_UNKNOWN_STEP";
  /** Problem with RelativeLocationPath */
  public static final String ER_EXPECTED_REL_LOC_PATH =
         "ER_EXPECTED_REL_LOC_PATH";
  /** Problem with LocationPath */
  public static final String ER_EXPECTED_LOC_PATH = "ER_EXPECTED_LOC_PATH";
  /** Problem with Step */
  public static final String ER_EXPECTED_LOC_STEP = "ER_EXPECTED_LOC_STEP";
  /** Problem with NodeTest */
  public static final String ER_EXPECTED_NODE_TEST = "ER_EXPECTED_NODE_TEST";
  /** Expected step pattern */
  public static final String ER_EXPECTED_STEP_PATTERN =
        "ER_EXPECTED_STEP_PATTERN";
  /** Expected relative path pattern */
  public static final String ER_EXPECTED_REL_PATH_PATTERN =
         "ER_EXPECTED_REL_PATH_PATTERN";
  /** localname in QNAME should be a valid NCName */
  /** prefix in QNAME should be a valid NCName */
  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */
  public static final String ER_CANT_CONVERT_TO_BOOLEAN =
         "ER_CANT_CONVERT_TO_BOOLEAN";
  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */
  public static final String ER_CANT_CONVERT_TO_SINGLENODE =
         "ER_CANT_CONVERT_TO_SINGLENODE";
  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */
  public static final String ER_CANT_GET_SNAPSHOT_LENGTH =
         "ER_CANT_GET_SNAPSHOT_LENGTH";
  /** Field ER_NON_ITERATOR_TYPE                */
  public static final String ER_NON_ITERATOR_TYPE = "ER_NON_ITERATOR_TYPE";
  /** Field ER_DOC_MUTATED                      */
  public static final String ER_DOC_MUTATED = "ER_DOC_MUTATED";
  public static final String ER_INVALID_XPATH_TYPE = "ER_INVALID_XPATH_TYPE";
  public static final String ER_EMPTY_XPATH_RESULT = "ER_EMPTY_XPATH_RESULT";
  public static final String ER_INCOMPATIBLE_TYPES = "ER_INCOMPATIBLE_TYPES";
  public static final String ER_NULL_RESOLVER = "ER_NULL_RESOLVER";
  public static final String ER_CANT_CONVERT_TO_STRING =
         "ER_CANT_CONVERT_TO_STRING";
  public static final String ER_NON_SNAPSHOT_TYPE = "ER_NON_SNAPSHOT_TYPE";
  public static final String ER_WRONG_DOCUMENT = "ER_WRONG_DOCUMENT";
  /* Note to translators:  The XPath expression cannot be evaluated with respect
   * to this type of node.
   */
  /** Field ER_WRONG_NODETYPE                    */
  public static final String ER_WRONG_NODETYPE = "ER_WRONG_NODETYPE";
  public static final String ER_XPATH_ERROR = "ER_XPATH_ERROR";

  public static final String WG_LOCALE_NAME_NOT_HANDLED =
         "WG_LOCALE_NAME_NOT_HANDLED";
  public static final String WG_PROPERTY_NOT_SUPPORTED =
         "WG_PROPERTY_NOT_SUPPORTED";
  public static final String WG_DONT_DO_ANYTHING_WITH_NS =
         "WG_DONT_DO_ANYTHING_WITH_NS";
  public static final String WG_SECURITY_EXCEPTION = "WG_SECURITY_EXCEPTION";
  public static final String WG_QUO_NO_LONGER_DEFINED =
         "WG_QUO_NO_LONGER_DEFINED";
  public static final String WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST =
         "WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST";
  public static final String WG_FUNCTION_TOKEN_NOT_FOUND =
         "WG_FUNCTION_TOKEN_NOT_FOUND";
  public static final String WG_COULDNOT_FIND_FUNCTION =
         "WG_COULDNOT_FIND_FUNCTION";
  public static final String WG_CANNOT_MAKE_URL_FROM ="WG_CANNOT_MAKE_URL_FROM";
  public static final String WG_EXPAND_ENTITIES_NOT_SUPPORTED =
         "WG_EXPAND_ENTITIES_NOT_SUPPORTED";
  public static final String WG_ILLEGAL_VARIABLE_REFERENCE =
         "WG_ILLEGAL_VARIABLE_REFERENCE";
  public static final String WG_UNSUPPORTED_ENCODING ="WG_UNSUPPORTED_ENCODING";



  public static final Object[][] contents = {

  /** Field ERROR0000          */


  { "ERROR0000" , "{0}" },


  /** Field ER_CURRENT_NOT_ALLOWED_IN_MATCH          */

  { ER_CURRENT_NOT_ALLOWED_IN_MATCH, "current() \u95a2\u6570\u306f\u30d1\u30bf\u30fc\u30f3\u306e\u30de\u30c3\u30c1\u30f3\u30b0\u3067\u306f\u8a31\u53ef\u3055\u308c\u3066\u3044\u307e\u305b\u3093!" },

  /** Field ER_CURRENT_TAKES_NO_ARGS          */

  { ER_CURRENT_TAKES_NO_ARGS, "current() \u95a2\u6570\u306f\u5f15\u304d\u6570\u3092\u53d7\u3051\u5165\u308c\u307e\u305b\u3093!" },

  /** Field ER_DOCUMENT_REPLACED          */
  { ER_DOCUMENT_REPLACED,
      "document() \u95a2\u6570\u306e\u30a4\u30f3\u30d7\u30ea\u30e1\u30f3\u30c6\u30fc\u30b7\u30e7\u30f3\u304c org.apache.xalan.xslt.FuncDocument \u306b\u3088\u308a\u7f6e\u304d\u63db\u3048\u3089\u308c\u307e\u3057\u305f!"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */

  { ER_CONTEXT_HAS_NO_OWNERDOC,
      "\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u306b\u6240\u6709\u8005\u6587\u66f8\u304c\u3042\u308a\u307e\u305b\u3093!"},

  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */

  { ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() \u306e\u5f15\u304d\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},

  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */

  { ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() \u306e\u5f15\u304d\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},

  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */
  { ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() \u306e\u5f15\u304d\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},

  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */

  { ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() \u306e\u5f15\u304d\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},

  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */

  { ER_NAME_HAS_TOO_MANY_ARGS,
     "name() \u306e\u5f15\u304d\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},

  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */

  { ER_STRING_HAS_TOO_MANY_ARGS,
      "string() \u306e\u5f15\u304d\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},

  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */

  { ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length() \u306e\u5f15\u304d\u6570\u304c\u591a\u3059\u304e\u307e\u3059\u3002"},

  /** Field ER_TRANSLATE_TAKES_3_ARGS          */

  { ER_TRANSLATE_TAKES_3_ARGS,
      "translate() \u95a2\u6570\u306f 3 \u500b\u306e\u5f15\u304d\u6570\u3092\u4f7f\u7528\u3057\u307e\u3059!"},

  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */

  { ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "unparsed-entity-uri \u95a2\u6570\u306f\u5f15\u304d\u6570\u3092 1 \u500b\u4f7f\u7528\u3057\u307e\u3059!"},

  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */

  { ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "namespace axis \u304c\u307e\u3060\u30a4\u30f3\u30d7\u30ea\u30e1\u30f3\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093!"},

  /** Field ER_UNKNOWN_AXIS          */

  { ER_UNKNOWN_AXIS,
     "\u4e0d\u660e\u306a\u8ef8: {0}"},

  /** Field ER_UNKNOWN_MATCH_OPERATION          */

  { ER_UNKNOWN_MATCH_OPERATION,
     "\u4e0d\u660e\u306e\u30de\u30c3\u30c1\u30f3\u30b0\u64cd\u4f5c!"},

  /** Field ER_INCORRECT_ARG_LENGTH          */

  { ER_INCORRECT_ARG_LENGTH,
      "processing-instruction() \u306e\u30ce\u30fc\u30c9\u30fb\u30c6\u30b9\u30c8\u306e\u5f15\u304d\u6570\u306e\u9577\u3055\u304c\u8aa4\u3063\u3066\u3044\u307e\u3059!"},

  /** Field ER_CANT_CONVERT_TO_NUMBER          */

  { ER_CANT_CONVERT_TO_NUMBER,
      "{0} \u3092\u6570\u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093"},

  /** Field ER_CANT_CONVERT_TO_NODELIST          */

  { ER_CANT_CONVERT_TO_NODELIST,
      "{0} \u3092 NodeList \u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093!"},

  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */

  { ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "{0} \u3092 NodeSetDTM \u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093!"},

  /** Field ER_CANT_CONVERT_TO_TYPE          */

  { ER_CANT_CONVERT_TO_TYPE,
      "{0} \u3092 type#{1} \u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093"},

  /** Field ER_EXPECTED_MATCH_PATTERN          */

  { ER_EXPECTED_MATCH_PATTERN,
      "getMatchScore \u3067\u5fc5\u8981\u306a\u4e00\u81f4\u30d1\u30bf\u30fc\u30f3\u3067\u3059!"},

  /** Field ER_COULDNOT_GET_VAR_NAMED          */

  { ER_COULDNOT_GET_VAR_NAMED,
      "{0} \u3068\u3044\u3046\u540d\u524d\u306e\u5909\u6570\u3092\u53d6\u5f97\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f"},

  /** Field ER_UNKNOWN_OPCODE          */

  { ER_UNKNOWN_OPCODE,
     "\u30a8\u30e9\u30fc! \u4e0d\u660e\u306a op \u30b3\u30fc\u30c9: {0}"},

  /** Field ER_EXTRA_ILLEGAL_TOKENS          */

  { ER_EXTRA_ILLEGAL_TOKENS,
     "\u4f59\u5206\u306e\u6b63\u3057\u304f\u306a\u3044\u30c8\u30fc\u30af\u30f3: {0}"},

  /** Field ER_EXPECTED_DOUBLE_QUOTE          */

  { ER_EXPECTED_DOUBLE_QUOTE,
      "\u5f15\u7528\u7b26\u304c\u8aa4\u3063\u3066\u3044\u308b\u30ea\u30c6\u30e9\u30eb... \u4e8c\u91cd\u5f15\u7528\u7b26\u304c\u5fc5\u8981\u3067\u3057\u305f!"},

  /** Field ER_EXPECTED_SINGLE_QUOTE          */

  { ER_EXPECTED_SINGLE_QUOTE,
      "\u5f15\u7528\u7b26\u304c\u8aa4\u3063\u3066\u3044\u308b\u30ea\u30c6\u30e9\u30eb... \u5358\u4e00\u5f15\u7528\u7b26\u304c\u5fc5\u8981\u3067\u3057\u305f!"},

  /** Field ER_EMPTY_EXPRESSION          */

  { ER_EMPTY_EXPRESSION,
     "\u7a7a\u306e\u5f0f\u3067\u3059!"},

  /** Field ER_EXPECTED_BUT_FOUND          */

  { ER_EXPECTED_BUT_FOUND,
     "{0} \u304c\u5fc5\u8981\u3067\u3057\u305f\u304c\u3001{1} \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f"},

  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */

  { ER_INCORRECT_PROGRAMMER_ASSERTION,
      "\u30d7\u30ed\u30b0\u30e9\u30de\u30fc\u306e\u30a2\u30b5\u30fc\u30b7\u30e7\u30f3\u304c\u8aa4\u3063\u3066\u3044\u307e\u3059! - {0}"},

  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */

  { ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "\u30d6\u30fc\u30eb(...) \u5f15\u304d\u6570\u306f 19990709 XPath \u30c9\u30e9\u30d5\u30c8\u3067\u306f\u3082\u3046\u30aa\u30d7\u30b7\u30e7\u30f3\u3067\u3042\u308a\u307e\u305b\u3093\u3002"},

  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */

  { ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "',' \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u304c\u3001\u5148\u7acb\u3064\u5f15\u304d\u6570\u304c\u3042\u308a\u307e\u305b\u3093!"},

  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */

  { ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "',' \u304c\u898b\u3064\u304b\u308a\u307e\u3057\u305f\u304c\u3001\u5f8c\u7d9a\u306e\u5f15\u304d\u6570\u304c\u3042\u308a\u307e\u305b\u3093!"},

  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */

  { ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[predicate]' \u307e\u305f\u306f '.[predicate]' \u306f\u6b63\u3057\u304f\u306a\u3044\u69cb\u6587\u3067\u3059\u3002  \u4ee3\u308a\u306b  'self::node()[predicate]' \u3092\u4f7f\u7528\u3057\u3066\u304f\u3060\u3055\u3044\u3002"},

  /** Field ER_ILLEGAL_AXIS_NAME          */

  { ER_ILLEGAL_AXIS_NAME,
     "\u6b63\u3057\u304f\u306a\u3044\u8ef8\u306e\u540d\u524d: {0}"},

  /** Field ER_UNKNOWN_NODETYPE          */

  { ER_UNKNOWN_NODETYPE,
     "\u4e0d\u660e\u306a\u30ce\u30fc\u30c9\u30fb\u30bf\u30a4\u30d7: {0}"},

  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */

  { ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "\u30d1\u30bf\u30fc\u30f3\u30fb\u30ea\u30c6\u30e9\u30eb ({0}) \u306b\u306f\u5f15\u7528\u7b26\u304c\u5fc5\u8981\u3067\u3059!"},

  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */

  { ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0} \u3092\u6570\u306b\u30d5\u30a9\u30fc\u30de\u30c3\u30c8\u8a2d\u5b9a\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f!"},

  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */

  { ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "XML TransformerFactory Liaison \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f: {0}"},

  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */

  { ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "\u30a8\u30e9\u30fc! xpath select \u5f0f (-select) \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},

  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */

  { ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "\u30a8\u30e9\u30fc! OP_LOCATIONPATH \u306e\u5f8c\u306b ENDOP \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},

  /** Field ER_ERROR_OCCURED          */

  { ER_ERROR_OCCURED,
     "\u30a8\u30e9\u30fc\u304c\u8d77\u3053\u308a\u307e\u3057\u305f!"},

  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */

  { ER_ILLEGAL_VARIABLE_REFERENCE,
      "\u5909\u6570\u306b\u6307\u5b9a\u3055\u308c\u305f VariableReference \u304c\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u5916\u304b\u3001\u5b9a\u7fa9\u306a\u3057\u3067\u3059!  \u540d\u524d = {0}"},

  /** Field ER_AXES_NOT_ALLOWED          */

  { ER_AXES_NOT_ALLOWED,
      "\u30de\u30c3\u30c1\u30f3\u30b0\u30fb\u30d1\u30bf\u30fc\u30f3\u3067\u8a31\u53ef\u3055\u308c\u3066\u3044\u308b\u306e\u306f child:: \u304a\u3088\u3073 attribute:: \u3060\u3051\u3067\u3059!  \u554f\u984c\u306e\u8ef8 = {0}"},

  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */

  { ER_KEY_HAS_TOO_MANY_ARGS,
      "key() \u306e\u5f15\u304d\u6570\u306e\u6570\u304c\u8aa4\u3063\u3066\u3044\u307e\u3059\u3002"},

  /** Field ER_COUNT_TAKES_1_ARG          */

  { ER_COUNT_TAKES_1_ARG,
      "count \u95a2\u6570\u306f\u5f15\u304d\u6570\u3092 1 \u500b\u4f7f\u7528\u3057\u307e\u3059!"},

  /** Field ER_COULDNOT_FIND_FUNCTION          */

  { ER_COULDNOT_FIND_FUNCTION,
     "\u95a2\u6570: {0} \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},

  /** Field ER_UNSUPPORTED_ENCODING          */

  { ER_UNSUPPORTED_ENCODING,
     "\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u306a\u3044\u30a8\u30f3\u30b3\u30fc\u30c9: {0}"},

  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */

  { ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "\u554f\u984c\u304c getNextSibling \u5185\u306e DTM \u3067\u8d77\u3053\u308a\u307e\u3057\u305f... \u30ea\u30ab\u30d0\u30ea\u30fc\u3057\u3088\u3046\u3068\u3057\u3066\u3044\u307e\u3059"},

  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */

  { ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "\u30d7\u30ed\u30b0\u30e9\u30de\u30fc\u30fb\u30a8\u30e9\u30fc: EmptyNodeList \u3092\u66f8\u304d\u8fbc\u307f\u5148\u306b\u306f\u3067\u304d\u307e\u305b\u3093\u3002"},

  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */

  { ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory \u306f XPathContext \u306b\u3088\u308a\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093!"},

  /** Field ER_PREFIX_MUST_RESOLVE          */

  { ER_PREFIX_MUST_RESOLVE,
      "\u63a5\u982d\u90e8\u306f\u30cd\u30fc\u30e0\u30fb\u30b9\u30da\u30fc\u30b9\u306b\u89e3\u6c7a\u3055\u308c\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093: {0}"},

  /** Field ER_PARSE_NOT_SUPPORTED          */

  { ER_PARSE_NOT_SUPPORTED,
      "parse (InputSource \u30bd\u30fc\u30b9) \u306f XPathContext \u5185\u3067\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093! {0} \u3092\u30aa\u30fc\u30d7\u30f3\u3067\u304d\u307e\u305b\u3093"},

  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  /** Field ER_SAX_API_NOT_HANDLED          */

  { ER_SAX_API_NOT_HANDLED,
      "SAX API characters(char ch[]... \u306f DTM \u306b\u3088\u308a\u51e6\u7406\u3055\u308c\u307e\u305b\u3093!"},

  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */

  { ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... \u306f DTM \u306b\u3088\u308a\u51e6\u7406\u3055\u308c\u307e\u305b\u3093!"},

  /** Field ER_DTM_CANNOT_HANDLE_NODES          */

  { ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison \u306f\u30bf\u30a4\u30d7 {0} \u306e\u30ce\u30fc\u30c9\u3092\u51e6\u7406\u3067\u304d\u307e\u305b\u3093"},

  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */

  { ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper \u306f\u30bf\u30a4\u30d7 {0} \u306e\u30ce\u30fc\u30c9\u3092\u51e6\u7406\u3067\u304d\u307e\u305b\u3093"},

  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */

  { ER_XERCES_PARSE_ERROR_DETAILS,
      "DOM2Helper.parse \u30a8\u30e9\u30fc: SystemID - {0} \u884c - {1}"},

  /** Field ER_XERCES_PARSE_ERROR          */

  { ER_XERCES_PARSE_ERROR,
     "DOM2Helper.parse \u30a8\u30e9\u30fc"},

  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  /** Field ER_INVALID_UTF16_SURROGATE          */

  { ER_INVALID_UTF16_SURROGATE,
      "\u7121\u52b9\u306a UTF-16 \u30b5\u30ed\u30b2\u30fc\u30c8\u304c\u691c\u51fa\u3055\u308c\u307e\u3057\u305f: {0} ?"},

  /** Field ER_OIERROR          */

  { ER_OIERROR,
     "\u5165\u51fa\u529b\u30a8\u30e9\u30fc"},

  /** Field ER_CANNOT_CREATE_URL          */

  { ER_CANNOT_CREATE_URL,
     "{0} \u306e URL \u3092\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093\u3002"},

  /** Field ER_XPATH_READOBJECT          */

  { ER_XPATH_READOBJECT,
     "XPath.readObject \u5185: {0}"},

  /** Field ER_FUNCTION_TOKEN_NOT_FOUND         */

  { ER_FUNCTION_TOKEN_NOT_FOUND,
      "\u95a2\u6570\u30c8\u30fc\u30af\u30f3\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002"},

   /**  Argument 'localName' is null  */


   /**  Can not deal with XPath type:   */

  { ER_CANNOT_DEAL_XPATH_TYPE,
       "XPath \u30bf\u30a4\u30d7: {0} \u3092\u51e6\u7406\u3067\u304d\u307e\u305b\u3093"},

   /**  This NodeSet is not mutable  */

  { ER_NODESET_NOT_MUTABLE,
       "\u3053\u306e NodeSet \u306f\u53ef\u5909\u3067\u3042\u308a\u307e\u305b\u3093"},

   /**  This NodeSetDTM is not mutable  */

  { ER_NODESETDTM_NOT_MUTABLE,
       "\u3053\u306e NodeSetDTM \u306f\u53ef\u5909\u3067\u3042\u308a\u307e\u305b\u3093"},

   /**  Variable not resolvable:   */

  { ER_VAR_NOT_RESOLVABLE,
        "\u5909\u6570\u306f\u89e3\u6c7a\u53ef\u80fd\u3067\u3042\u308a\u307e\u305b\u3093: {0}"},

   /** Null error handler  */

  { ER_NULL_ERROR_HANDLER,
        "\u30cc\u30eb\u306e\u30a8\u30e9\u30fc\u30fb\u30cf\u30f3\u30c9\u30e9\u30fc"},

   /**  Programmer's assertion: unknown opcode  */

  { ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "\u30d7\u30ed\u30b0\u30e9\u30de\u30fc\u306e\u30a2\u30b5\u30fc\u30b7\u30e7\u30f3: \u4e0d\u660e\u306a\u547d\u4ee4\u30b3\u30fc\u30c9: {0}"},

   /**  0 or 1   */

  { ER_ZERO_OR_ONE,
       "0 \u307e\u305f\u306f 1"},


   /**  rtf() not supported by XRTreeFragSelectWrapper   */

  { ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() \u306f XRTreeFragSelectWrapper \u306b\u3088\u3063\u3066\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093"},

   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */

  { ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() \u306f XRTreeFragSelectWrapper \u306b\u3088\u3063\u3066\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093"},

   /**  fsb() not supported for XStringForChars   */

  { ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb() \u306f XStringForChars \u306e\u5834\u5408\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093"},

   /**  Could not find variable with the name of   */

  { ER_COULD_NOT_FIND_VAR,
      "\u540d\u524d {0} \u3092\u3082\u3064\u5909\u6570\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},

   /**  XStringForChars can not take a string for an argument   */

  { ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars \u306f\u5f15\u304d\u6570\u306b\u30b9\u30c8\u30ea\u30f3\u30b0\u3092\u4f7f\u7528\u3057\u307e\u305b\u3093"},

   /**  The FastStringBuffer argument can not be null   */

  { ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "FastStringBuffer \u5f15\u304d\u6570\u306f\u30cc\u30eb\u306b\u3067\u304d\u307e\u305b\u3093"},

  /* MANTIS_XALAN CHANGE: BEGIN */
   /**  2 or 3   */

  { ER_TWO_OR_THREE,
       "2 \u307e\u305f\u306f 3"},

   /** Variable accessed before it is bound! */

  { ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "\u5909\u6570\u304c\u30d0\u30a4\u30f3\u30c9\u3055\u308c\u308b\u524d\u306b\u30a2\u30af\u30bb\u30b9\u3055\u308c\u307e\u3057\u305f!"},

   /** XStringForFSB can not take a string for an argument! */

  { ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB \u306f\u5f15\u304d\u6570\u306b\u30b9\u30c8\u30ea\u30f3\u30b0\u3092\u4f7f\u7528\u3057\u307e\u305b\u3093!"},

   /** Error! Setting the root of a walker to null! */

  { ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! \u30a8\u30e9\u30fc! \u30a6\u30a9\u30fc\u30ab\u30fc\u306e\u30eb\u30fc\u30c8\u3092\u30cc\u30eb\u306b\u8a2d\u5b9a\u3057\u3066\u3044\u307e\u3059!!!"},

   /** This NodeSetDTM can not iterate to a previous node! */

  { ER_NODESETDTM_CANNOT_ITERATE,
       "\u3053\u306e NodeSetDTM \u306f\u76f4\u524d\u306e\u30ce\u30fc\u30c9\u3092\u7e70\u308a\u8fd4\u3059\u3053\u3068\u304c\u3067\u304d\u307e\u305b\u3093!"},

  /** This NodeSet can not iterate to a previous node! */

  { ER_NODESET_CANNOT_ITERATE,
       "\u3053\u306e NodeSet \u306f\u76f4\u524d\u306e\u30ce\u30fc\u30c9\u3092\u7e70\u308a\u8fd4\u3059\u3053\u3068\u304c\u3067\u304d\u307e\u305b\u3093!"},

  /** This NodeSetDTM can not do indexing or counting functions! */

  { ER_NODESETDTM_CANNOT_INDEX,
       "\u3053\u306e NodeSetDTM \u306f\u7d22\u5f15\u4ed8\u3051\u3084\u30ab\u30a6\u30f3\u30c8\u306e\u6a5f\u80fd\u3092\u5b9f\u884c\u3067\u304d\u307e\u305b\u3093!"},

  /** This NodeSet can not do indexing or counting functions! */

  { ER_NODESET_CANNOT_INDEX,
       "\u3053\u306e NodeSet \u306f\u7d22\u5f15\u4ed8\u3051\u3084\u30ab\u30a6\u30f3\u30c8\u306e\u6a5f\u80fd\u3092\u5b9f\u884c\u3067\u304d\u307e\u305b\u3093!"},

  /** Can not call setShouldCacheNodes after nextNode has been called! */

  { ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "nextNode \u3092\u547c\u3073\u51fa\u3057\u305f\u5f8c\u306b setShouldCacheNodes \u3092\u547c\u3073\u51fa\u3059\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093!"},

  /** {0} only allows {1} arguments */

  { ER_ONLY_ALLOWS,
       "{0} \u306b\u8a31\u53ef\u3055\u308c\u308b\u5f15\u304d\u6570\u306f {1} \u500b\u306e\u307f\u3067\u3059"},

  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */

  { ER_UNKNOWN_STEP,
       "getNextStepPos \u5185\u306e\u30d7\u30ed\u30b0\u30e9\u30de\u30fc\u306e\u30a2\u30b5\u30fc\u30b7\u30e7\u30f3: \u4e0d\u660e\u306a stepType: {0}"},


  /** Problem with RelativeLocationPath */

  { ER_EXPECTED_REL_LOC_PATH,


  /** Problem with LocationPath */

  { ER_EXPECTED_LOC_PATH,
       "\u30ed\u30b1\u30fc\u30b7\u30e7\u30f3\u30fb\u30d1\u30b9\u304c\u5fc5\u8981\u3067\u3057\u305f\u304c\u3001\u6b21\u306e\u30c8\u30fc\u30af\u30f3\u304c\u691c\u51fa\u3055\u308c\u307e\u3057\u305f\u003a  {0}"},


  /** Problem with Step */

  { ER_EXPECTED_LOC_STEP,


  /** Problem with NodeTest */

  { ER_EXPECTED_NODE_TEST,
       "NCName:* \u307e\u305f\u306f QName \u306e\u3044\u305a\u308c\u304b\u3068\u4e00\u81f4\u3059\u308b\u30ce\u30fc\u30c9\u30fb\u30c6\u30b9\u30c8\u304c\u5fc5\u8981\u3067\u3057\u305f\u3002"},


  /** Expected step pattern */

  { ER_EXPECTED_STEP_PATTERN,
       "\u30b9\u30c6\u30c3\u30d7\u30fb\u30d1\u30bf\u30fc\u30f3\u304c\u5fc5\u8981\u3067\u3057\u305f\u304c\u3001'/' \u304c\u691c\u51fa\u3055\u308c\u307e\u3057\u305f\u3002"},


  /** Expected relative path pattern */

  { ER_EXPECTED_REL_PATH_PATTERN,
       "\u76f8\u5bfe\u30d1\u30b9\u30fb\u30d1\u30bf\u30fc\u30f3\u304c\u5fc5\u8981\u3067\u3057\u305f\u3002"},


  /** localname in QNAME should be a valid NCName */



  /** prefix in QNAME should be a valid NCName */



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */

  { ER_CANT_CONVERT_TO_BOOLEAN,
       "{0} \u3092\u30d6\u30fc\u30eb\u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */

  { ER_CANT_CONVERT_TO_SINGLENODE,
       "{0} \u3092\u5358\u4e00\u30ce\u30fc\u30c9\u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093\u3002 \u3053\u306e getter \u304c\u30bf\u30a4\u30d7 ANY_UNORDERED_NODE_TYPE \u304a\u3088\u3073 FIRST_ORDERED_NODE_TYPE \u306b\u9069\u7528\u3055\u308c\u307e\u3059\u3002"},


  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */

  { ER_CANT_GET_SNAPSHOT_LENGTH,
       "\u30bf\u30a4\u30d7: {0} \u306b\u3064\u3044\u3066\u306e\u30b9\u30ca\u30c3\u30d7\u30b7\u30e7\u30c3\u30c8\u306e\u9577\u3055\u3092\u53d6\u5f97\u3067\u304d\u307e\u305b\u3093\u3002 \u3053\u306e getter \u304c\u30bf\u30a4\u30d7 UNORDERED_NODE_SNAPSHOT_TYPE \u304a\u3088\u3073 ORDERED_NODE_SNAPSHOT_TYPE \u306b\u9069\u7528\u3055\u308c\u307e\u3059\u3002"},

  /** Field ER_NON_ITERATOR_TYPE                */

  { ER_NON_ITERATOR_TYPE,
       "\u975e\u30a4\u30c6\u30ec\u30fc\u30bf\u30fc\u30fb\u30bf\u30a4\u30d7: {0} \u306f\u7e70\u308a\u8fd4\u3057\u304c\u3067\u304d\u307e\u305b\u3093"},


  /** Field ER_DOC_MUTATED                      */

  { ER_DOC_MUTATED,
       "\u7d50\u679c\u304c\u623b\u3055\u308c\u305f\u4ee5\u5f8c\u306b\u6587\u66f8\u304c\u5909\u66f4\u3055\u308c\u307e\u3057\u305f\u3002 \u30a4\u30c6\u30ec\u30fc\u30bf\u30fc\u304c\u7121\u52b9\u3067\u3059\u3002"},

  /** Field ER_INVALID_XPATH_TYPE               */

  { ER_INVALID_XPATH_TYPE,
       "\u7121\u52b9\u306a XPath \u30bf\u30a4\u30d7\u5f15\u304d\u6570: {0}"},

  /** Field ER_EMPTY_XPATH_RESULT                */

  { ER_EMPTY_XPATH_RESULT,
       "\u7a7a\u306e XPath \u7d50\u679c\u30aa\u30d6\u30b8\u30a7\u30af\u30c8"},

  /** Field ER_INCOMPATIBLE_TYPES                */

  { ER_INCOMPATIBLE_TYPES,
       "\u623b\u3055\u308c\u305f\u30bf\u30a4\u30d7: {0} \u306f\u6307\u5b9a\u3055\u308c\u305f\u30bf\u30a4\u30d7: {1} \u306b\u5f37\u5236\u3067\u304d\u307e\u305b\u3093"},

  /** Field ER_NULL_RESOLVER                     */

  { ER_NULL_RESOLVER,
       "\u63a5\u982d\u90e8\u3092\u30cc\u30eb\u63a5\u982d\u90e8\u30ea\u30be\u30eb\u30d0\u30fc\u306b\u89e3\u6c7a\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** Field ER_CANT_CONVERT_TO_STRING            */

  { ER_CANT_CONVERT_TO_STRING,
       "{0} \u3092\u30b9\u30c8\u30ea\u30f3\u30b0\u306b\u5909\u63db\u3067\u304d\u307e\u305b\u3093\u3002"},


  /** Field ER_NON_SNAPSHOT_TYPE                 */

  { ER_NON_SNAPSHOT_TYPE,
       "\u30bf\u30a4\u30d7: {0} \u4e0a\u306e snapshotItem \u306e\u547c\u3073\u51fa\u3057\u304c\u3067\u304d\u307e\u305b\u3093\u3002 \u3053\u306e\u30e1\u30bd\u30c3\u30c9\u304c\u30bf\u30a4\u30d7 UNORDERED_NODE_SNAPSHOT_TYPE \u304a\u3088\u3073 ORDERED_NODE_SNAPSHOT_TYPE \u306b\u9069\u7528\u3055\u308c\u307e\u3059\u3002"},


  /** Field ER_WRONG_DOCUMENT                    */

  { ER_WRONG_DOCUMENT,
       "\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u30fb\u30ce\u30fc\u30c9\u306f\u3053\u306e XPathEvaluator \u306b\u30d0\u30a4\u30f3\u30c9\u3055\u308c\u3066\u3044\u308b\u6587\u66f8\u306b\u5c5e\u3057\u3066\u3044\u307e\u305b\u3093\u3002"},

  /** Field ER_WRONG_NODETYPE                    */

  { ER_WRONG_NODETYPE,
       "\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u30fb\u30ce\u30fc\u30c9\u30fb\u30bf\u30a4\u30d7\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},

  /** Field ER_XPATH_ERROR                       */

  { ER_XPATH_ERROR,
       "XPath \u306b\u4e0d\u660e\u306a\u30a8\u30e9\u30fc\u304c\u3042\u308a\u307e\u3059\u3002"},



  /** Field WG_LOCALE_NAME_NOT_HANDLED          */

  { WG_LOCALE_NAME_NOT_HANDLED,
      "\u30d5\u30a9\u30fc\u30de\u30c3\u30c8\u756a\u53f7\u95a2\u6570\u5185\u306e\u30ed\u30b1\u30fc\u30eb\u540d\u306f\u307e\u3060\u51e6\u7406\u3055\u308c\u307e\u305b\u3093!"},

  /** Field WG_PROPERTY_NOT_SUPPORTED          */

  { WG_PROPERTY_NOT_SUPPORTED,
      "XSL \u30d7\u30ed\u30d1\u30c6\u30a3\u30fc: {0} \u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */

  { WG_DONT_DO_ANYTHING_WITH_NS,
      "\u73fe\u5728\u3001\u30d7\u30ed\u30d1\u30c6\u30a3\u30fc {1} \u306e\u30cd\u30fc\u30e0\u30fb\u30b9\u30da\u30fc\u30b9 {0} \u3067\u4f55\u3082\u5b9f\u884c\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},

  /** Field WG_SECURITY_EXCEPTION          */

  { WG_SECURITY_EXCEPTION,
      "XSL \u30b7\u30b9\u30c6\u30e0\u30fb\u30d7\u30ed\u30d1\u30c6\u30a3\u30fc: {0} \u306b\u30a2\u30af\u30bb\u30b9\u3057\u3088\u3046\u3068\u3057\u3066\u3044\u308b\u3068\u304d\u306b SecurityException"},

  /** Field WG_QUO_NO_LONGER_DEFINED          */

  { WG_QUO_NO_LONGER_DEFINED,
      "\u65e7\u69cb\u6587: quo(...) \u306f XPath \u5185\u306b\u3082\u3046\u5b9a\u7fa9\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002"},

  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */

  { WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "nodeTest \u3092\u30a4\u30f3\u30d7\u30ea\u30e1\u30f3\u30c8\u3059\u308b\u306b\u306f XPath \u306b\u6d3e\u751f\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u304c\u5fc5\u8981\u3067\u3059!"},

  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */

  { WG_FUNCTION_TOKEN_NOT_FOUND,
      "\u95a2\u6570\u30c8\u30fc\u30af\u30f3\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3002"},

  /** Field WG_COULDNOT_FIND_FUNCTION          */

  { WG_COULDNOT_FIND_FUNCTION,
      "\u95a2\u6570: {0} \u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f"},

  /** Field WG_CANNOT_MAKE_URL_FROM          */

  { WG_CANNOT_MAKE_URL_FROM,
      "URL \u3092 {0} \u304b\u3089\u4f5c\u6210\u3067\u304d\u307e\u305b\u3093\u3002"},

  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */

  { WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "-E \u30aa\u30d7\u30b7\u30e7\u30f3\u306f DTM \u30d1\u30fc\u30b5\u30fc\u306e\u5834\u5408\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093"},

  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */

  { WG_ILLEGAL_VARIABLE_REFERENCE,
      "\u5909\u6570\u306b\u6307\u5b9a\u3055\u308c\u305f VariableReference \u304c\u30b3\u30f3\u30c6\u30ad\u30b9\u30c8\u5916\u304b\u3001\u5b9a\u7fa9\u306a\u3057\u3067\u3059!  \u540d\u524d = {0}"},

  /** Field WG_UNSUPPORTED_ENCODING          */

  { WG_UNSUPPORTED_ENCODING,
     "\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u306a\u3044\u30a8\u30f3\u30b3\u30fc\u30c9: {0}"},



  { "ui_language", "en"},
  { "help_language", "en"},
  { "language", "en"},
  { "BAD_CODE", "createMessage \u3078\u306e\u30d1\u30e9\u30e1\u30fc\u30bf\u30fc\u304c\u5883\u754c\u5916\u3067\u3057\u305f\u3002"},
  { "FORMAT_FAILED", "messageFormat \u547c\u3073\u51fa\u3057\u4e2d\u306b\u4f8b\u5916\u304c\u30b9\u30ed\u30fc\u3055\u308c\u307e\u3057\u305f\u3002"},
  { "version", ">>>>>>> Xalan \u30d0\u30fc\u30b8\u30e7\u30f3 "},
  { "version2", "<<<<<<<"},
  { "yes", "\u306f\u3044"},
  { "line", "\u884c #"},
  { "column", "\u6841 #"},
  { "xsldone", "XSLProcessor: \u5b8c\u4e86"},
  { "xpath_option", "xpath \u30aa\u30d7\u30b7\u30e7\u30f3: "},
  { "optionIN", "   [-in inputXMLURL]"},
  { "optionSelect", "   [-select xpath \u5f0f]"},
  { "optionMatch", "   [-match \u30de\u30c3\u30c1\u30f3\u30b0\u30fb\u30d1\u30bf\u30fc\u30f3 (\u30de\u30c3\u30c1\u30f3\u30b0\u8a3a\u65ad\u7528)]"},
  { "optionAnyExpr", "\u3042\u308b\u3044\u306f\u8a3a\u65ad\u30c0\u30f3\u30d7\u3092\u5b9f\u884c\u3059\u308b\u306e\u306f xpath \u5f0f\u3060\u3051\u3067\u3059"},
  { "noParsermsg1", "XSL \u51e6\u7406\u306f\u6210\u529f\u3057\u307e\u305b\u3093\u3067\u3057\u305f\u3002"},
  { "noParsermsg2", "** \u30d1\u30fc\u30b5\u30fc\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f **"},
  { "noParsermsg3", "\u30af\u30e9\u30b9\u30d1\u30b9\u3092\u8abf\u3079\u3066\u304f\u3060\u3055\u3044\u3002"},
  { "noParsermsg4", "IBM \u306e XML Parser for Java \u304c\u306a\u3044\u5834\u5408\u306f\u3001\u6b21\u306e\u30b5\u30a4\u30c8\u304b\u3089\u30c0\u30a6\u30f3\u30ed\u30fc\u30c9\u3067\u304d\u307e\u3059:"},
  { "gtone", ">1" },
  { "zero", "0" },
  { "one", "1" },
  { "two" , "2" },
  { "three", "3" }

  };



  /** Field BAD_CODE          */
  public static final String BAD_CODE = "BAD_CODE";

  /** Field FORMAT_FAILED          */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** Field ERROR_RESOURCES          */
  public static final String ERROR_RESOURCES =
    "org.apache.xpath.res.XPATHErrorResources";

  /** Field ERROR_STRING          */
  public static final String ERROR_STRING = "#\u30a8\u30e9\u30fc";

  /** Field ERROR_HEADER          */
  public static final String ERROR_HEADER = "\u30a8\u30e9\u30fc: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "\u8b66\u544a: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "\u30d1\u30bf\u30fc\u30f3 ";

  /**
   * Get the association list.
   *
   * @return The association list.
   */
  public Object[][] getContents()
  {
    return contents;
  }

  /**
   * Return a named ResourceBundle for a particular locale.  This method mimics the behavior
   * of ResourceBundle.getBundle().
   *
   * @param res the name of the resource to load.
   * @param locale the locale to prefer when searching for the bundle
   *
   * @param className Name of local-specific subclass.
   * @return the ResourceBundle
   * @throws MissingResourceException
   */
  public static final XPATHErrorResources loadResourceBundle(String className)
          throws MissingResourceException
  {

    Locale locale = Locale.getDefault();
    String suffix = getResourceSuffix(locale);

    try
    {

      return (XPATHErrorResources) ResourceBundle.getBundle(className
              + suffix, locale);
    }
    catch (MissingResourceException e)
    {
      {

        return (XPATHErrorResources) ResourceBundle.getBundle(className,
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
