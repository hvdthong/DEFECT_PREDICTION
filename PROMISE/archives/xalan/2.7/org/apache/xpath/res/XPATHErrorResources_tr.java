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
public class XPATHErrorResources_tr extends ListResourceBundle
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



  /**
   * Get the association list.
   *
   * @return The association list.
   */
  public Object[][] getContents()
  {
    return new Object[][]{

  /** Field ERROR0000          */


  { "ERROR0000" , "{0}" },


  /** Field ER_CURRENT_NOT_ALLOWED_IN_MATCH          */

  { ER_CURRENT_NOT_ALLOWED_IN_MATCH, "E\u015fle\u015fme \u00f6r\u00fcnt\u00fcs\u00fcnde current() i\u015flevine izin verilmez!" },

  /** Field ER_CURRENT_TAKES_NO_ARGS          */

  { ER_CURRENT_TAKES_NO_ARGS, "current() i\u015flevi ba\u011f\u0131ms\u0131z de\u011fi\u015fken kabul etmez!" },

  /** Field ER_DOCUMENT_REPLACED          */
  { ER_DOCUMENT_REPLACED,
      "document() i\u015flevi uygulamas\u0131 org.apache.xalan.xslt.FuncDocument ile de\u011fi\u015ftirildi!"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */

  { ER_CONTEXT_HAS_NO_OWNERDOC,
      "Ba\u011flam\u0131n iye belgesi yok!"},

  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */

  { ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() i\u015flevinde \u00e7ok fazla ba\u011f\u0131ms\u0131z de\u011fi\u015fken var."},

  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */

  { ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() i\u015flevinde \u00e7ok fazla ba\u011f\u0131ms\u0131z de\u011fi\u015fken var."},

  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */
  { ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() i\u015flevinde \u00e7ok fazla ba\u011f\u0131ms\u0131z de\u011fi\u015fken var."},

  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */

  { ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() i\u015flevinde \u00e7ok fazla ba\u011f\u0131ms\u0131z de\u011fi\u015fken var."},

  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */

  { ER_NAME_HAS_TOO_MANY_ARGS,
     "name() i\u015flevinde \u00e7ok fazla ba\u011f\u0131ms\u0131z de\u011fi\u015fken var."},

  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */

  { ER_STRING_HAS_TOO_MANY_ARGS,
      "string() i\u015flevinde \u00e7ok fazla ba\u011f\u0131ms\u0131z de\u011fi\u015fken var."},

  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */

  { ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length() i\u015flevinde \u00e7ok fazla ba\u011f\u0131ms\u0131z de\u011fi\u015fken var."},

  /** Field ER_TRANSLATE_TAKES_3_ARGS          */

  { ER_TRANSLATE_TAKES_3_ARGS,
      "translate() i\u015flevi \u00fc\u00e7 ba\u011f\u0131ms\u0131z de\u011fi\u015fken al\u0131r!"},

  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */

  { ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "unparsed-entity-uri i\u015flevi bir ba\u011f\u0131ms\u0131z de\u011fi\u015fken almal\u0131d\u0131r!"},

  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */

  { ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "namespace ekseni hen\u00fcz ger\u00e7ekle\u015ftirilmedi!"},

  /** Field ER_UNKNOWN_AXIS          */

  { ER_UNKNOWN_AXIS,
     "Bilinmeyen eksen: {0}"},

  /** Field ER_UNKNOWN_MATCH_OPERATION          */

  { ER_UNKNOWN_MATCH_OPERATION,
     "Bilinmeyen e\u015fle\u015fme i\u015flemi!"},

  /** Field ER_INCORRECT_ARG_LENGTH          */

  { ER_INCORRECT_ARG_LENGTH,
      "processing-instruction() d\u00fc\u011f\u00fcm s\u0131namas\u0131n\u0131n ba\u011f\u0131ms\u0131z de\u011fi\u015fken uzunlu\u011fu yanl\u0131\u015f!"},

  /** Field ER_CANT_CONVERT_TO_NUMBER          */

  { ER_CANT_CONVERT_TO_NUMBER,
      "{0} bir say\u0131ya d\u00f6n\u00fc\u015ft\u00fcr\u00fclemez"},

  /** Field ER_CANT_CONVERT_TO_NODELIST          */

  { ER_CANT_CONVERT_TO_NODELIST,
      "{0} NodeList''e d\u00f6n\u00fc\u015ft\u00fcr\u00fclemez!"},

  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */

  { ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "{0} NodeSetDTM''ye d\u00f6n\u00fc\u015ft\u00fcr\u00fclemez!"},

  /** Field ER_CANT_CONVERT_TO_TYPE          */

  { ER_CANT_CONVERT_TO_TYPE,
      "{0} - type#{1} d\u00f6n\u00fc\u015f\u00fcm\u00fc yap\u0131lamaz"},

  /** Field ER_EXPECTED_MATCH_PATTERN          */

  { ER_EXPECTED_MATCH_PATTERN,
      "getMatchScore i\u00e7inde e\u015fle\u015fme \u00f6r\u00fcnt\u00fcs\u00fc bekleniyor!"},

  /** Field ER_COULDNOT_GET_VAR_NAMED          */

  { ER_COULDNOT_GET_VAR_NAMED,
      "{0} adl\u0131 de\u011fi\u015fken al\u0131namad\u0131"},

  /** Field ER_UNKNOWN_OPCODE          */

  { ER_UNKNOWN_OPCODE,
     "HATA! Bilinmeyen i\u015flem kodu: {0}"},

  /** Field ER_EXTRA_ILLEGAL_TOKENS          */

  { ER_EXTRA_ILLEGAL_TOKENS,
     "Fazladan ge\u00e7ersiz simgeler: {0}"},

  /** Field ER_EXPECTED_DOUBLE_QUOTE          */

  { ER_EXPECTED_DOUBLE_QUOTE,
      "Haz\u0131r bilginin t\u0131rnak imi yanl\u0131\u015f... \u00e7ift t\u0131rnak bekleniyor!"},

  /** Field ER_EXPECTED_SINGLE_QUOTE          */

  { ER_EXPECTED_SINGLE_QUOTE,
      "Haz\u0131r bilginin t\u0131rnak imi yanl\u0131\u015f... tek t\u0131rnak bekleniyor!"},

  /** Field ER_EMPTY_EXPRESSION          */

  { ER_EMPTY_EXPRESSION,
     "\u0130fade bo\u015f!"},

  /** Field ER_EXPECTED_BUT_FOUND          */

  { ER_EXPECTED_BUT_FOUND,
     "{0} bekleniyordu, {1} bulundu"},

  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */

  { ER_INCORRECT_PROGRAMMER_ASSERTION,
      "Programc\u0131 de\u011ferlendirmesi yanl\u0131\u015f! - {0}"},

  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */

  { ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "boolean(...) ba\u011f\u0131ms\u0131z de\u011fi\u015fkeni 19990709 XPath tasla\u011f\u0131yla art\u0131k iste\u011fe ba\u011fl\u0131 de\u011fil."},

  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */

  { ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "',' bulundu, ancak \u00f6ncesinde ba\u011f\u0131ms\u0131z de\u011fi\u015fken yok!"},

  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */

  { ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "',' bulundu, ancak sonras\u0131nda ba\u011f\u0131ms\u0131z de\u011fi\u015fken yok!"},

  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */

  { ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[kar\u015f\u0131la\u015ft\u0131rma belirtimi]' ya da '.[kar\u015f\u0131la\u015ft\u0131rma belirtimi]' ge\u00e7ersiz bir s\u00f6zdizimi.  Yerine \u015funu kullan\u0131n: 'self::node()[kar\u015f\u0131la\u015ft\u0131rma belirtimi]'."},

  /** Field ER_ILLEGAL_AXIS_NAME          */

  { ER_ILLEGAL_AXIS_NAME,
     "Eksen ad\u0131 ge\u00e7ersiz: {0}"},

  /** Field ER_UNKNOWN_NODETYPE          */

  { ER_UNKNOWN_NODETYPE,
     "D\u00fc\u011f\u00fcm tipi ge\u00e7ersiz: {0}"},

  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */

  { ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "\u00d6r\u00fcnt\u00fc haz\u0131r bilgisinin ({0}) t\u0131rnak i\u00e7ine al\u0131nmas\u0131 gerekiyor!"},

  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */

  { ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0} bir say\u0131 olarak bi\u00e7imlenemedi!"},

  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */

  { ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "XML TransformerFactory ili\u015fkisi {0} yarat\u0131lamad\u0131"},

  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */

  { ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "Hata! xpath select ifadesi (-select) bulunamad\u0131."},

  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */

  { ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "HATA! OP_LOCATIONPATH sonras\u0131nda ENDOP bulunamad\u0131."},

  /** Field ER_ERROR_OCCURED          */

  { ER_ERROR_OCCURED,
     "Hata olu\u015ftu!"},

  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */

  { ER_ILLEGAL_VARIABLE_REFERENCE,
      "De\u011fi\u015fken i\u00e7in belirtilen VariableReference ba\u011flam d\u0131\u015f\u0131 ya da tan\u0131ms\u0131z! Ad = {0}"},

  /** Field ER_AXES_NOT_ALLOWED          */

  { ER_AXES_NOT_ALLOWED,
      "E\u015fle\u015fme \u00f6r\u00fcnt\u00fclerinde yaln\u0131zca child:: ve attribute:: eksenlerine izin verilir! Ge\u00e7ersiz eksenler = {0}"},

  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */

  { ER_KEY_HAS_TOO_MANY_ARGS,
      "key() yanl\u0131\u015f say\u0131da ba\u011f\u0131ms\u0131z de\u011fi\u015fken i\u00e7eriyor."},

  /** Field ER_COUNT_TAKES_1_ARG          */

  { ER_COUNT_TAKES_1_ARG,
      "Say\u0131m i\u015flevi tek bir ba\u011f\u0131ms\u0131z de\u011fi\u015fken almal\u0131d\u0131r!"},

  /** Field ER_COULDNOT_FIND_FUNCTION          */

  { ER_COULDNOT_FIND_FUNCTION,
     "\u0130\u015flev bulunamad\u0131: {0}"},

  /** Field ER_UNSUPPORTED_ENCODING          */

  { ER_UNSUPPORTED_ENCODING,
     "Desteklenmeyen kodlama: {0}"},

  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */

  { ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "getNextSibling s\u0131ras\u0131nda DTM i\u00e7inde sorun olu\u015ftu... kurtarma giri\u015fiminde bulunuluyor"},

  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */

  { ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "Programc\u0131 hatas\u0131: EmptyNodeList i\u00e7ine yaz\u0131lamaz."},

  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */

  { ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory, XPathContext taraf\u0131ndan desteklenmiyor!"},

  /** Field ER_PREFIX_MUST_RESOLVE          */

  { ER_PREFIX_MUST_RESOLVE,
      "\u00d6nek bir ad alan\u0131na \u00e7\u00f6z\u00fclmelidir: {0}"},

  /** Field ER_PARSE_NOT_SUPPORTED          */

  { ER_PARSE_NOT_SUPPORTED,
      "XPathContext i\u00e7inde parse (InputSource kayna\u011f\u0131) desteklenmiyor! {0} a\u00e7\u0131lam\u0131yor"},

  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  /** Field ER_SAX_API_NOT_HANDLED          */

  { ER_SAX_API_NOT_HANDLED,
      "SAX API characters(char ch[]... DTM taraf\u0131ndan i\u015flenmedi!"},

  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */

  { ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... DTM taraf\u0131ndan i\u015flenmedi!"},

  /** Field ER_DTM_CANNOT_HANDLE_NODES          */

  { ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison {0} tipi d\u00fc\u011f\u00fcmleri i\u015fleyemez"},

  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */

  { ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper {0} tipi d\u00fc\u011f\u00fcmleri i\u015fleyemez"},

  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */

  { ER_XERCES_PARSE_ERROR_DETAILS,
      "DOM2Helper.parse hatas\u0131: Sistem tnt - {0} sat\u0131r - {1}"},

  /** Field ER_XERCES_PARSE_ERROR          */

  { ER_XERCES_PARSE_ERROR,
     "DOM2Helper.parse hatas\u0131"},

  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  /** Field ER_INVALID_UTF16_SURROGATE          */

  { ER_INVALID_UTF16_SURROGATE,
      "UTF-16 yerine kullan\u0131lan de\u011fer ge\u00e7ersiz: {0} ?"},

  /** Field ER_OIERROR          */

  { ER_OIERROR,
     "G\u00c7 hatas\u0131"},

  /** Field ER_CANNOT_CREATE_URL          */

  { ER_CANNOT_CREATE_URL,
     "\u0130lgili url yarat\u0131lam\u0131yor: {0}"},

  /** Field ER_XPATH_READOBJECT          */

  { ER_XPATH_READOBJECT,
     "XPath.readObject i\u00e7inde: {0}"},

  /** Field ER_FUNCTION_TOKEN_NOT_FOUND         */

  { ER_FUNCTION_TOKEN_NOT_FOUND,
      "\u0130\u015flev simgesi bulunamad\u0131."},

   /**  Argument 'localName' is null  */


   /**  Can not deal with XPath type:   */

  { ER_CANNOT_DEAL_XPATH_TYPE,
       "XPath tipi i\u015flenemiyor: {0}"},

   /**  This NodeSet is not mutable  */

  { ER_NODESET_NOT_MUTABLE,
       "Bu NodeSet de\u011fi\u015febilir t\u00fcrde de\u011fil"},

   /**  This NodeSetDTM is not mutable  */

  { ER_NODESETDTM_NOT_MUTABLE,
       "Bu NodeSetDTM de\u011fi\u015febilir t\u00fcrde de\u011fil"},

   /**  Variable not resolvable:   */

  { ER_VAR_NOT_RESOLVABLE,
        "De\u011fi\u015fken \u00e7\u00f6z\u00fclebilir bir de\u011fi\u015fken de\u011fil: {0}"},

   /** Null error handler  */

  { ER_NULL_ERROR_HANDLER,
        "Bo\u015f de\u011ferli hata i\u015fleyici"},

   /**  Programmer's assertion: unknown opcode  */

  { ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "Programc\u0131 de\u011ferlendirmesi: bilinmeyen opcode: {0}"},

   /**  0 or 1   */

  { ER_ZERO_OR_ONE,
       "0 ya da 1"},


   /**  rtf() not supported by XRTreeFragSelectWrapper   */

  { ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() XRTreeFragSelectWrapper taraf\u0131ndan desteklenmiyor"},

   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */

  { ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() XRTreeFragSelectWrapper taraf\u0131ndan desteklenmiyor"},

   /**  fsb() not supported for XStringForChars   */

  { ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb() XStringForChars i\u00e7in desteklenmiyor"},

   /**  Could not find variable with the name of   */

  { ER_COULD_NOT_FIND_VAR,
      "{0} ad\u0131nda de\u011fi\u015fken bulunamad\u0131"},

   /**  XStringForChars can not take a string for an argument   */

  { ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars ba\u011f\u0131ms\u0131z de\u011fi\u015fken olarak dizgi alamaz"},

   /**  The FastStringBuffer argument can not be null   */

  { ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "FastStringBuffer ba\u011f\u0131ms\u0131z de\u011fi\u015fkeni bo\u015f de\u011ferli olamaz"},

  /* MANTIS_XALAN CHANGE: BEGIN */
   /**  2 or 3   */

  { ER_TWO_OR_THREE,
       "2 ya da 3"},

   /** Variable accessed before it is bound! */

  { ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "De\u011fi\u015fkene ba\u011f tan\u0131mlamadan \u00f6nce eri\u015fildi!"},

   /** XStringForFSB can not take a string for an argument! */

  { ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB ba\u011f\u0131ms\u0131z de\u011fi\u015fken olarak dizgi alamaz!"},

   /** Error! Setting the root of a walker to null! */

  { ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! Hata! Walker k\u00f6k\u00fc bo\u015f de\u011fere ayarlan\u0131yor!!!"},

   /** This NodeSetDTM can not iterate to a previous node! */

  { ER_NODESETDTM_CANNOT_ITERATE,
       "Bu NodeSetDTM \u00f6nceki bir d\u00fc\u011f\u00fcme yineleme yapamaz!"},

  /** This NodeSet can not iterate to a previous node! */

  { ER_NODESET_CANNOT_ITERATE,
       "Bu NodeSet \u00f6nceki bir d\u00fc\u011f\u00fcme yineleme yapamaz!"},

  /** This NodeSetDTM can not do indexing or counting functions! */

  { ER_NODESETDTM_CANNOT_INDEX,
       "Bu NodeSetDTM dizinleme ya da sayma i\u015flevleri yapamaz!"},

  /** This NodeSet can not do indexing or counting functions! */

  { ER_NODESET_CANNOT_INDEX,
       "Bu NodeSet dizinleme ya da sayma i\u015flevleri yapamaz!"},

  /** Can not call setShouldCacheNodes after nextNode has been called! */

  { ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "nextNode \u00e7a\u011fr\u0131ld\u0131ktan sonra setShouldCacheNodes \u00e7a\u011fr\u0131lamaz!"},

  /** {0} only allows {1} arguments */

  { ER_ONLY_ALLOWS,
       "{0} yaln\u0131zca {1} ba\u011f\u0131ms\u0131z de\u011fi\u015fkene izin verir"},

  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */

  { ER_UNKNOWN_STEP,
       "getNextStepPos i\u00e7inde programc\u0131 de\u011ferlendirmesi: bilinmeyen stepType: {0}"},


  /** Problem with RelativeLocationPath */

  { ER_EXPECTED_REL_LOC_PATH,


  /** Problem with LocationPath */

  { ER_EXPECTED_LOC_PATH,
       "Yer yolu bekleniyordu, ancak \u015fu simge saptand\u0131\u003a  {0}"},


  /** Problem with Step */

  { ER_EXPECTED_LOC_STEP,


  /** Problem with NodeTest */

  { ER_EXPECTED_NODE_TEST,
       "NCName:* ya da QName ile e\u015fle\u015fen bir d\u00fc\u011f\u00fcm s\u0131namas\u0131 bekleniyordu."},


  /** Expected step pattern */

  { ER_EXPECTED_STEP_PATTERN,
       "Ad\u0131m \u00f6r\u00fcnt\u00fcs\u00fc bekleniyordu, ancak '/' saptand\u0131."},


  /** Expected relative path pattern */

  { ER_EXPECTED_REL_PATH_PATTERN,
       "G\u00f6reli yol \u00f6r\u00fcnt\u00fcs\u00fc bekleniyordu."},


  /** localname in QNAME should be a valid NCName */



  /** prefix in QNAME should be a valid NCName */



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */

  { ER_CANT_CONVERT_TO_BOOLEAN,
       "{0} boole tipine d\u00f6n\u00fc\u015ft\u00fcr\u00fclemedi"},


  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */

  { ER_CANT_CONVERT_TO_SINGLENODE,
       "{0} tek d\u00fc\u011f\u00fcme d\u00f6n\u00fc\u015ft\u00fcr\u00fclemiyor. Bu alma y\u00f6ntemi (getter) ANY_UNORDERED_NODE_TYPE ve FIRST_ORDERED_NODE_TYPE tipleri i\u00e7in ge\u00e7erlidir."},


  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */

  { ER_CANT_GET_SNAPSHOT_LENGTH,
       "Bu tip \u00fczerinde anl\u0131k g\u00f6r\u00fcn\u00fcm uzunlu\u011fu al\u0131nam\u0131yor: {0}. Bu alma y\u00f6ntemi (getter) UNORDERED_NODE_SNAPSHOT_TYPE ve ORDERED_NODE_SNAPSHOT_TYPE tipleri i\u00e7in ge\u00e7erlidir."},

  /** Field ER_NON_ITERATOR_TYPE                */

  { ER_NON_ITERATOR_TYPE,
       "Yineleyici olmayan tip \u00fczerinde yinelenemez: {0}"},


  /** Field ER_DOC_MUTATED                      */

  { ER_DOC_MUTATED,
       "Sonu\u00e7 d\u00f6nd\u00fcr\u00fcld\u00fckten sonra belge de\u011fi\u015ftirildi. Yineleyici ge\u00e7ersiz."},

  /** Field ER_INVALID_XPATH_TYPE               */

  { ER_INVALID_XPATH_TYPE,
       "Ge\u00e7ersiz XPath tipi ba\u011f\u0131ms\u0131z de\u011fi\u015fkeni: {0}"},

  /** Field ER_EMPTY_XPATH_RESULT                */

  { ER_EMPTY_XPATH_RESULT,
       "Bo\u015f XPath sonu\u00e7 nesnesi"},

  /** Field ER_INCOMPATIBLE_TYPES                */

  { ER_INCOMPATIBLE_TYPES,
       "D\u00f6nd\u00fcr\u00fclen {0} tipi, belirtilen {1} tipine zorlanamaz."},

  /** Field ER_NULL_RESOLVER                     */

  { ER_NULL_RESOLVER,
       "Bo\u015f de\u011ferli \u00f6nek \u00e7\u00f6z\u00fcc\u00fcyle \u00f6nek \u00e7\u00f6z\u00fclemez."},


  /** Field ER_CANT_CONVERT_TO_STRING            */

  { ER_CANT_CONVERT_TO_STRING,
       "{0} bir dizgiye d\u00f6n\u00fc\u015ft\u00fcr\u00fclemiyor."},


  /** Field ER_NON_SNAPSHOT_TYPE                 */

  { ER_NON_SNAPSHOT_TYPE,
       "{0} tipi \u00fczerinde snapshotItem \u00e7a\u011fr\u0131lam\u0131yor. Bu y\u00f6ntem UNORDERED_NODE_SNAPSHOT_TYPE ve ORDERED_NODE_SNAPSHOT_TYPE tipleri i\u00e7in ge\u00e7erlidir."},


  /** Field ER_WRONG_DOCUMENT                    */

  { ER_WRONG_DOCUMENT,
       "Ba\u011flam d\u00fc\u011f\u00fcm\u00fc, bu XPathEvaluator arabirimine ba\u011flanan belgeye ait de\u011fil."},

  /** Field ER_WRONG_NODETYPE                    */

  { ER_WRONG_NODETYPE,
       "Ba\u011flam d\u00fc\u011f\u00fcm\u00fc tipi desteklenmiyor."},

  /** Field ER_XPATH_ERROR                       */

  { ER_XPATH_ERROR,
       "XPath i\u00e7inde bilinmeyen hata."},



  /** Field WG_LOCALE_NAME_NOT_HANDLED          */

  { WG_LOCALE_NAME_NOT_HANDLED,
      "format-number i\u015flevinde \u00fclke de\u011feri ad\u0131 hen\u00fcz i\u015flenmedi!"},

  /** Field WG_PROPERTY_NOT_SUPPORTED          */

  { WG_PROPERTY_NOT_SUPPORTED,
      "XSL \u00f6zelli\u011fi desteklenmiyor: {0}"},

  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */

  { WG_DONT_DO_ANYTHING_WITH_NS,
      "{1} \u00f6zelli\u011findeki {0} ad alan\u0131yla \u015fu an hi\u00e7bir \u015fey yapmay\u0131n"},

  /** Field WG_SECURITY_EXCEPTION          */

  { WG_SECURITY_EXCEPTION,
      "{0} XSL sistem \u00f6zelli\u011fine eri\u015fme giri\u015fimi s\u0131ras\u0131nda SecurityException"},

  /** Field WG_QUO_NO_LONGER_DEFINED          */

  { WG_QUO_NO_LONGER_DEFINED,
      "Eski s\u00f6zdizimi: quo(...) art\u0131k XPath i\u00e7inde tan\u0131mlanmaz."},

  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */

  { WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "nodeTest uygulanmas\u0131 i\u00e7in XPath t\u00fcretilmi\u015f bir nesne gerektirir!"},

  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */

  { WG_FUNCTION_TOKEN_NOT_FOUND,
      "\u0130\u015flev simgesi bulunamad\u0131."},

  /** Field WG_COULDNOT_FIND_FUNCTION          */

  { WG_COULDNOT_FIND_FUNCTION,
      "\u0130\u015flev bulunamad\u0131: {0}"},

  /** Field WG_CANNOT_MAKE_URL_FROM          */

  { WG_CANNOT_MAKE_URL_FROM,
      "Dizgiden URL olu\u015fturulamad\u0131: {0}"},

  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */

  { WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "DTM ayr\u0131\u015ft\u0131r\u0131c\u0131s\u0131 i\u00e7in -E se\u00e7ene\u011fi desteklenmiyor"},

  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */

  { WG_ILLEGAL_VARIABLE_REFERENCE,
      "De\u011fi\u015fken i\u00e7in belirtilen VariableReference ba\u011flam d\u0131\u015f\u0131 ya da tan\u0131ms\u0131z! Ad = {0}"},

  /** Field WG_UNSUPPORTED_ENCODING          */

  { WG_UNSUPPORTED_ENCODING,
     "Desteklenmeyen kodlama: {0}"},



  { "ui_language", "tr"},
  { "help_language", "tr"},
  { "language", "tr"},
  { "BAD_CODE", "createMessage i\u00e7in kullan\u0131lan de\u011fi\u015ftirge s\u0131n\u0131rlar\u0131n d\u0131\u015f\u0131nda"},
  { "FORMAT_FAILED", "messageFormat \u00e7a\u011fr\u0131s\u0131 s\u0131ras\u0131nda kural d\u0131\u015f\u0131 durum yay\u0131nland\u0131"},
  { "version", ">>>>>>> Xalan S\u00fcr\u00fcm "},
  { "version2", "<<<<<<<"},
  { "yes", "yes"},
  { "line", "Sat\u0131r #"},
  { "column", "Kolon #"},
  { "xsldone", "XSLProcessor: bitti"},
  { "xpath_option", "xpath se\u00e7enekleri: "},
  { "optionIN", "   [-in inputXMLURL]"},
  { "optionSelect", "   [-select xpath ifadesi]"},
  { "optionMatch", "   [-match e\u015fle\u015fme \u00f6r\u00fcnt\u00fcs\u00fc (e\u015fle\u015fme tan\u0131lamas\u0131 i\u00e7in)]"},
  { "optionAnyExpr", "Ya da yaln\u0131zca xpath ifadesi de tan\u0131lama d\u00f6k\u00fcm\u00fc sa\u011flar"},
  { "noParsermsg1", "XSL i\u015flemi ba\u015far\u0131s\u0131z oldu."},
  { "noParsermsg2", "** Ayr\u0131\u015ft\u0131r\u0131c\u0131 bulunamad\u0131 **"},
  { "noParsermsg3", "L\u00fctfen classpath de\u011fi\u015fkeninizi inceleyin."},
  { "noParsermsg4", "Sisteminizde IBM XML Parser for Java arac\u0131 yoksa, \u015fu adresten y\u00fckleyebilirsiniz:"},
  { "gtone", ">1" },
  { "zero", "0" },
  { "one", "1" },
  { "two" , "2" },
  { "three", "3" }

  };
  }



  /** Field BAD_CODE          */
  public static final String BAD_CODE = "HATALI_KOD";

  /** Field FORMAT_FAILED          */
  public static final String FORMAT_FAILED = "B\u0130\u00c7\u0130MLEME_BA\u015eARISIZ";

  /** Field ERROR_RESOURCES          */
  public static final String ERROR_RESOURCES =
    "org.apache.xpath.res.XPATHErrorResources";

  /** Field ERROR_STRING          */
  public static final String ERROR_STRING = "#hata";

  /** Field ERROR_HEADER          */
  public static final String ERROR_HEADER = "Hata: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Uyar\u0131: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "\u00d6R\u00dcNT\u00dc ";


  /**
   * Return a named ResourceBundle for a particular locale.  This method mimics the behavior
   * of ResourceBundle.getBundle().
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
                new Locale("tr", "TR"));
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
