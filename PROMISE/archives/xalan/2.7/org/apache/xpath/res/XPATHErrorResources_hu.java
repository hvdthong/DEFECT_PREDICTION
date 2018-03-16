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
public class XPATHErrorResources_hu extends ListResourceBundle
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

  { ER_CURRENT_NOT_ALLOWED_IN_MATCH, "A current() f\u00fcggv\u00e9ny nem megengedett az illeszt\u00e9si mint\u00e1ban!" },

  /** Field ER_CURRENT_TAKES_NO_ARGS          */

  { ER_CURRENT_TAKES_NO_ARGS, "A current() f\u00fcggv\u00e9ny nem fogad el argumentumokat!" },

  /** Field ER_DOCUMENT_REPLACED          */
  { ER_DOCUMENT_REPLACED,
      "A document() f\u00fcggv\u00e9ny megval\u00f3s\u00edt\u00e1s\u00e1t lecser\u00e9lte az org.apache.xalan.xslt.FuncDocument!"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */

  { ER_CONTEXT_HAS_NO_OWNERDOC,
      "A k\u00f6rnyezetnek nincs tulajdonos dokumentuma!"},

  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */

  { ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "A local-name()-nek t\u00fal sok argumentuma van."},

  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */

  { ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "A namespace-uri()-nek t\u00fal sok argumentuma van."},

  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */
  { ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "A normalize-space()-nek t\u00fal sok argumentuma van."},

  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */

  { ER_NUMBER_HAS_TOO_MANY_ARGS,
      "A number()-nek t\u00fal sok argumentuma van."},

  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */

  { ER_NAME_HAS_TOO_MANY_ARGS,
     "A name()-nek t\u00fal sok argumentuma van."},

  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */

  { ER_STRING_HAS_TOO_MANY_ARGS,
      "A string()-nek t\u00fal sok argumentuma van."},

  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */

  { ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "A string-length()-nek t\u00fal sok argumentuma van."},

  /** Field ER_TRANSLATE_TAKES_3_ARGS          */

  { ER_TRANSLATE_TAKES_3_ARGS,
      "A translate() f\u00fcggv\u00e9ny h\u00e1rom argumentumot k\u00e9r!"},

  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */

  { ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "Az unparsed-entity-uri f\u00fcggv\u00e9nyhez egy argumentum sz\u00fcks\u00e9ges!"},

  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */

  { ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "A n\u00e9vt\u00e9r tengely m\u00e9g nincs magval\u00f3s\u00edtva!"},

  /** Field ER_UNKNOWN_AXIS          */

  { ER_UNKNOWN_AXIS,
     "Ismeretlen tengely: {0}"},

  /** Field ER_UNKNOWN_MATCH_OPERATION          */

  { ER_UNKNOWN_MATCH_OPERATION,
     "ismeretlen illeszt\u00e9si m\u0171velet!"},

  /** Field ER_INCORRECT_ARG_LENGTH          */

  { ER_INCORRECT_ARG_LENGTH,
      "A processing-instruction() csom\u00f3pont teszt argumentum\u00e1nak hossza helytelen!"},

  /** Field ER_CANT_CONVERT_TO_NUMBER          */

  { ER_CANT_CONVERT_TO_NUMBER,
      "A(z) {0} nem konvert\u00e1lhat\u00f3 sz\u00e1mm\u00e1"},

  /** Field ER_CANT_CONVERT_TO_NODELIST          */

  { ER_CANT_CONVERT_TO_NODELIST,
      "A(z) {0} nem konvert\u00e1lhat\u00f3 NodeList-t\u00e9!"},

  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */

  { ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "A(z) {0} nem konvert\u00e1lhat\u00f3 NodeSetDTM-m\u00e9!"},

  /** Field ER_CANT_CONVERT_TO_TYPE          */

  { ER_CANT_CONVERT_TO_TYPE,
      "{0} nem konvert\u00e1lhat\u00f3 type#{1} t\u00edpuss\u00e1"},

  /** Field ER_EXPECTED_MATCH_PATTERN          */

  { ER_EXPECTED_MATCH_PATTERN,
      "Illeszt\u00e9si mint\u00e1t v\u00e1rtunk a getMatchScore-ban!"},

  /** Field ER_COULDNOT_GET_VAR_NAMED          */

  { ER_COULDNOT_GET_VAR_NAMED,
      "Nem lehet lek\u00e9rni a(z) {0} nev\u0171 v\u00e1ltoz\u00f3t"},

  /** Field ER_UNKNOWN_OPCODE          */

  { ER_UNKNOWN_OPCODE,
     "HIBA! Ismeretlen opk\u00f3d: {0}"},

  /** Field ER_EXTRA_ILLEGAL_TOKENS          */

  { ER_EXTRA_ILLEGAL_TOKENS,
     "Extra tiltott tokenek: {0}"},

  /** Field ER_EXPECTED_DOUBLE_QUOTE          */

  { ER_EXPECTED_DOUBLE_QUOTE,
      "rosszul id\u00e9zett liter\u00e1l... dupla id\u00e9z\u0151jelet v\u00e1rtunk!"},

  /** Field ER_EXPECTED_SINGLE_QUOTE          */

  { ER_EXPECTED_SINGLE_QUOTE,
      "rosszul id\u00e9zett liter\u00e1l... szimpla id\u00e9z\u0151jelet v\u00e1rtunk!"},

  /** Field ER_EMPTY_EXPRESSION          */

  { ER_EMPTY_EXPRESSION,
     "\u00dcres kifejez\u00e9s!"},

  /** Field ER_EXPECTED_BUT_FOUND          */

  { ER_EXPECTED_BUT_FOUND,
     "{0}-t v\u00e1rtunk, de ezt tal\u00e1ltuk: {1}"},

  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */

  { ER_INCORRECT_PROGRAMMER_ASSERTION,
      "A programoz\u00f3 feltev\u00e9se hib\u00e1s! - {0}"},

  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */

  { ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "A boolean(...) argumentuma t\u00f6bb\u00e9 nem opcion\u00e1lis az 19990709 XPath v\u00e1zlat szerint."},

  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */

  { ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "','-t tal\u00e1ltunk, de nincs el\u0151tte argumentum!"},

  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */

  { ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "','-t tal\u00e1ltunk, de nincs ut\u00e1na argumentum!"},

  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */

  { ER_PREDICATE_ILLEGAL_SYNTAX,
      "A '..[felt\u00e9tel]' vagy '.[felt\u00e9tel]' szintaktika tiltott.  Haszn\u00e1lja ink\u00e1bb a 'self::node()[predicate]' defin\u00edci\u00f3t."},

  /** Field ER_ILLEGAL_AXIS_NAME          */

  { ER_ILLEGAL_AXIS_NAME,
     "Tiltott tengelyn\u00e9v: {0}"},

  /** Field ER_UNKNOWN_NODETYPE          */

  { ER_UNKNOWN_NODETYPE,
     "Ismeretlen node-t\u00edpus: {0}"},

  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */

  { ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "A minta-liter\u00e1lt ({0}) id\u00e9z\u0151jelek k\u00f6z\u00e9 kell tenni!"},

  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */

  { ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "A(z) {0} nem form\u00e1zhat\u00f3 sz\u00e1mm\u00e1!"},

  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */

  { ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "Nem lehet XML TransformerFactory Liaison-t l\u00e9trehozni: {0}"},

  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */

  { ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "Hiba! Az xpath kiv\u00e1laszt\u00e1si kifejez\u00e9s nem tal\u00e1lhat\u00f3 (-select)."},

  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */

  { ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "HIBA! Nem tal\u00e1lhat\u00f3 ENDOP az OP_LOCATIONPATH ut\u00e1n"},

  /** Field ER_ERROR_OCCURED          */

  { ER_ERROR_OCCURED,
     "Hiba t\u00f6rt\u00e9nt!"},

  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */

  { ER_ILLEGAL_VARIABLE_REFERENCE,
      "A v\u00e1ltoz\u00f3ra adott VariableReference (v\u00e1ltoz\u00f3hivatkoz\u00e1s) k\u00edv\u00fcl van a k\u00f6rnyezeten vagy nincs defin\u00edci\u00f3ja!  N\u00e9v = {0}"},

  /** Field ER_AXES_NOT_ALLOWED          */

  { ER_AXES_NOT_ALLOWED,
      "Csak a child:: \u00e9s az attribute:: tengelyek illeszkedhetnek mint\u00e1kra.  Zavar\u00f3 tengelyek = {0}"},

  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */

  { ER_KEY_HAS_TOO_MANY_ARGS,
      "A key()-nek nem megfelel\u0151 sz\u00e1m\u00fa argumentuma van."},

  /** Field ER_COUNT_TAKES_1_ARG          */

  { ER_COUNT_TAKES_1_ARG,
      "A count f\u00fcggv\u00e9nyhez csak egy argumentumot lehet megadni!"},

  /** Field ER_COULDNOT_FIND_FUNCTION          */

  { ER_COULDNOT_FIND_FUNCTION,
     "Nem tal\u00e1lhat\u00f3 a f\u00fcggv\u00e9ny: {0}"},

  /** Field ER_UNSUPPORTED_ENCODING          */

  { ER_UNSUPPORTED_ENCODING,
     "Nem t\u00e1mogatott k\u00f3dol\u00e1s: {0}"},

  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */

  { ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "Probl\u00e9ma mer\u00fclt fel a DTM-ben a getNextSibling-ben... megpr\u00f3b\u00e1ljuk helyrehozni"},

  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */

  { ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "Programoz\u00f3i hiba: az EmptyNodeList-be (\u00fcres csom\u00f3pontlist\u00e1ba) nem lehet \u00edrni."},

  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */

  { ER_SETDOMFACTORY_NOT_SUPPORTED,
      "A setDOMFactory-t nem t\u00e1mogatja az XPathContext!"},

  /** Field ER_PREFIX_MUST_RESOLVE          */

  { ER_PREFIX_MUST_RESOLVE,
      "Az el\u0151tagnak egy n\u00e9vt\u00e9rre kell felold\u00f3dnia: {0}"},

  /** Field ER_PARSE_NOT_SUPPORTED          */

  { ER_PARSE_NOT_SUPPORTED,
      "Az elem\u00e9s (InputSource forr\u00e1s) nem t\u00e1mogatott az XPathContext-ben! Nem lehet megnyitni a(z) {0}-t"},

  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  /** Field ER_SAX_API_NOT_HANDLED          */

  { ER_SAX_API_NOT_HANDLED,
      "SAX API characters(char ch[]... f\u00fcggv\u00e9nyt nem kezeli a DTM!"},

  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */

  { ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "Az ignorableWhitespace(char ch[]... f\u00fcggv\u00e9nyt nem kezeli a DTM!"},

  /** Field ER_DTM_CANNOT_HANDLE_NODES          */

  { ER_DTM_CANNOT_HANDLE_NODES,
      "A DTMLiaison nem tud {0} t\u00edpus\u00fa csom\u00f3pontokat kezelni"},

  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */

  { ER_XERCES_CANNOT_HANDLE_NODES,
      "A DOM2Helper nem tud {0} t\u00edpus\u00fa csom\u00f3pontokat kezelni"},

  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */

  { ER_XERCES_PARSE_ERROR_DETAILS,
      "DOM2Helper.parse hiba: SystemID - {0} sor - {1}"},

  /** Field ER_XERCES_PARSE_ERROR          */

  { ER_XERCES_PARSE_ERROR,
     "DOM2Helper.parse hiba"},

  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  /** Field ER_INVALID_UTF16_SURROGATE          */

  { ER_INVALID_UTF16_SURROGATE,
      "\u00c9rv\u00e9nytelen UTF-16 helyettes\u00edt\u00e9s: {0} ?"},

  /** Field ER_OIERROR          */

  { ER_OIERROR,
     "IO hiba"},

  /** Field ER_CANNOT_CREATE_URL          */

  { ER_CANNOT_CREATE_URL,
     "Nem lehet URL-t l\u00e9trehozni ehhez: {0}"},

  /** Field ER_XPATH_READOBJECT          */

  { ER_XPATH_READOBJECT,
     "A XPath.readObject met\u00f3dusban: {0}"},

  /** Field ER_FUNCTION_TOKEN_NOT_FOUND         */

  { ER_FUNCTION_TOKEN_NOT_FOUND,
      "A f\u00fcggv\u00e9ny jelsor nem tal\u00e1lhat\u00f3."},

   /**  Argument 'localName' is null  */


   /**  Can not deal with XPath type:   */

  { ER_CANNOT_DEAL_XPATH_TYPE,
       "Nem lehet megbirk\u00f3zni az XPath t\u00edpussal: {0}"},

   /**  This NodeSet is not mutable  */

  { ER_NODESET_NOT_MUTABLE,
       "Ez a NodeSet nem illeszthet\u0151 be"},

   /**  This NodeSetDTM is not mutable  */

  { ER_NODESETDTM_NOT_MUTABLE,
       "Ez a NodeSetDTM nem illeszthet\u0151 be"},

   /**  Variable not resolvable:   */

  { ER_VAR_NOT_RESOLVABLE,
        "A v\u00e1ltoz\u00f3 nem oldhat\u00f3 fel: {0}"},

   /** Null error handler  */

  { ER_NULL_ERROR_HANDLER,
        "Null hibakezel\u0151"},

   /**  Programmer's assertion: unknown opcode  */

  { ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "Programoz\u00f3i \u00e9rtes\u00edt\u00e9s: ismeretlen m\u0171veletk\u00f3d: {0}"},

   /**  0 or 1   */

  { ER_ZERO_OR_ONE,
       "0 vagy 1"},


   /**  rtf() not supported by XRTreeFragSelectWrapper   */

  { ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "Az rtf()-et nem t\u00e1mogatja az XRTreeFragSelectWrapper"},

   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */

  { ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "Az asNodeIterator()-t nem t\u00e1mogatja az XRTreeFragSelectWrapper"},

   /**  fsb() not supported for XStringForChars   */

  { ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "Az fsb() nem t\u00e1mogatott az XStringForChars-n\u00e1l"},

   /**  Could not find variable with the name of   */

  { ER_COULD_NOT_FIND_VAR,
      "Nem tal\u00e1lhat\u00f3 {0} nev\u0171 v\u00e1ltoz\u00f3"},

   /**  XStringForChars can not take a string for an argument   */

  { ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "Az XStringForChars-nak nem adhat meg karakterl\u00e1nc argumentumot"},

   /**  The FastStringBuffer argument can not be null   */

  { ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "A FastStringBuffer argumentum nem lehet null"},

  /* MANTIS_XALAN CHANGE: BEGIN */
   /**  2 or 3   */

  { ER_TWO_OR_THREE,
       "2 vagy 3"},

   /** Variable accessed before it is bound! */

  { ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "V\u00e1ltoz\u00f3el\u00e9r\u00e9s \u00e9rt\u00e9kad\u00e1s el\u0151tt!"},

   /** XStringForFSB can not take a string for an argument! */

  { ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB nem kaphat sztring argumentumot!"},

   /** Error! Setting the root of a walker to null! */

  { ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! Hiba! A bej\u00e1r\u00f3 gy\u00f6ker\u00e9t null-ra \u00e1ll\u00edtotta!!!"},

   /** This NodeSetDTM can not iterate to a previous node! */

  { ER_NODESETDTM_CANNOT_ITERATE,
       "Ez a NodeSetDTM nem iter\u00e1lhat egy kor\u00e1bbi node-ra!"},

  /** This NodeSet can not iterate to a previous node! */

  { ER_NODESET_CANNOT_ITERATE,
       "Ez a NodeSet nem iter\u00e1lhat egy kor\u00e1bbi node-ra!"},

  /** This NodeSetDTM can not do indexing or counting functions! */

  { ER_NODESETDTM_CANNOT_INDEX,
       "Ez a NodeSetDTM nem indexelhet \u00e9s nem sz\u00e1ml\u00e1lhatja a funkci\u00f3kat!"},

  /** This NodeSet can not do indexing or counting functions! */

  { ER_NODESET_CANNOT_INDEX,
       "Ez a NodeSet nem indexelhet \u00e9s nem sz\u00e1ml\u00e1lhatja a funkci\u00f3kat!"},

  /** Can not call setShouldCacheNodes after nextNode has been called! */

  { ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "Nem h\u00edvhat\u00f3 setShouldCacheNodes nextNode h\u00edv\u00e1sa ut\u00e1n!"},

  /** {0} only allows {1} arguments */

  { ER_ONLY_ALLOWS,
       "{0} csak {1} argumentumot enged\u00e9lyez"},

  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */

  { ER_UNKNOWN_STEP,
       "Programoz\u00f3i \u00e9rtes\u00edt\u00e9s getNextStepPos h\u00edv\u00e1sban: ismeretlen stepType: {0}"},


  /** Problem with RelativeLocationPath */

  { ER_EXPECTED_REL_LOC_PATH,


  /** Problem with LocationPath */

  { ER_EXPECTED_LOC_PATH,
       "Egy hely \u00fatvonalat v\u00e1rtam, de a k\u00f6vetkez\u0151 tokent tal\u00e1ltam\u003a  {0}"},


  /** Problem with Step */

  { ER_EXPECTED_LOC_STEP,


  /** Problem with NodeTest */

  { ER_EXPECTED_NODE_TEST,
       "Egy olyan node-tesztet v\u00e1rtunk, ami vagy az NCName:*-ra vagy a QName-re illeszkedik."},


  /** Expected step pattern */

  { ER_EXPECTED_STEP_PATTERN,
       "Egy l\u00e9p\u00e9smint\u00e1t v\u00e1rtunk, de '/' szerepelt."},


  /** Expected relative path pattern */

  { ER_EXPECTED_REL_PATH_PATTERN,
       "Relat\u00edv \u00fatvonalat v\u00e1rtunk."},


  /** localname in QNAME should be a valid NCName */



  /** prefix in QNAME should be a valid NCName */



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */

  { ER_CANT_CONVERT_TO_BOOLEAN,
       "Nem lehet {0}-t logikaiv\u00e1 konvert\u00e1lni."},


  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */

  { ER_CANT_CONVERT_TO_SINGLENODE,
       "Nem lehet {0}-t egyszer\u0171 node-d\u00e1 konvert\u00e1lni. Ez a ford\u00edt\u00f3 az ANY_UNORDERED_NODE_TYPE \u00e9s a FIRST_ORDERED_NODE_TYPE t\u00edpusokra haszn\u00e1lhat\u00f3."},


  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */

  { ER_CANT_GET_SNAPSHOT_LENGTH,
       "Nem lehet megkapni a pillanatk\u00e9p hossz\u00e1t a k\u00f6vetkez\u0151 t\u00edpusra: {0}. Ez a ford\u00edt\u00f3 az UNORDERED_NODE_SNAPSHOT_TYPE \u00e9s az ORDERED_NODE_SNAPSHOT_TYPE t\u00edpusokra haszn\u00e1lhat\u00f3."},

  /** Field ER_NON_ITERATOR_TYPE                */

  { ER_NON_ITERATOR_TYPE,
       "Nem lehet iter\u00e1lni nem iter\u00e1tor t\u00edpust: {0}"},


  /** Field ER_DOC_MUTATED                      */

  { ER_DOC_MUTATED,
       "A dokumentum megv\u00e1ltozott, mi\u00f3ta az eredm\u00e9ny visszat\u00e9rt. Az iter\u00e1tor \u00e9rv\u00e9nytelen."},

  /** Field ER_INVALID_XPATH_TYPE               */

  { ER_INVALID_XPATH_TYPE,
       "\u00c9rv\u00e9nytelen XPath t\u00edpus-argumentum: {0}"},

  /** Field ER_EMPTY_XPATH_RESULT                */

  { ER_EMPTY_XPATH_RESULT,
       "\u00dcres XPath eredm\u00e9nyobjektum"},

  /** Field ER_INCOMPATIBLE_TYPES                */

  { ER_INCOMPATIBLE_TYPES,
       "A visszadott t\u00edpus: {0} nem lehet a megadott t\u00edpuss\u00e1 v\u00e1ltoztatni: {1}"},

  /** Field ER_NULL_RESOLVER                     */

  { ER_NULL_RESOLVER,
       "Nem lehet feloldani a prefixet null prefix-felold\u00f3val."},


  /** Field ER_CANT_CONVERT_TO_STRING            */

  { ER_CANT_CONVERT_TO_STRING,
       "Nem lehet {0} t\u00edpust sztringg\u00e9 konvert\u00e1lni."},


  /** Field ER_NON_SNAPSHOT_TYPE                 */

  { ER_NON_SNAPSHOT_TYPE,
       "Nem lehet snapshotItem-et h\u00edvni a k\u00f6vetkez\u0151 t\u00edpusra: {0}. Ez a met\u00f3dust az UNORDERED_NODE_SNAPSHOT_TYPE \u00e9s ORDERED_NODE_SNAPSHOT_TYPE t\u00edpusokra alkalmazhat\u00f3."},


  /** Field ER_WRONG_DOCUMENT                    */

  { ER_WRONG_DOCUMENT,
       "A k\u00f6rnyezeti node nem az XPathEvaluator-hoz tartoz\u00f3 dokumentumhoz tartozik."},

  /** Field ER_WRONG_NODETYPE                    */

  { ER_WRONG_NODETYPE,
       "A k\u00f6rnyezeti node t\u00edpusa nem t\u00e1mogatott."},

  /** Field ER_XPATH_ERROR                       */

  { ER_XPATH_ERROR,
       "Ismeretlen hiba az XPath-ban."},



  /** Field WG_LOCALE_NAME_NOT_HANDLED          */

  { WG_LOCALE_NAME_NOT_HANDLED,
      "A locale-n\u00e9v a format-number f\u00fcggv\u00e9nyben m\u00e9g nincs kezelve!"},

  /** Field WG_PROPERTY_NOT_SUPPORTED          */

  { WG_PROPERTY_NOT_SUPPORTED,
      "Az XSL tulajdons\u00e1g nem t\u00e1mogatott: {0}"},

  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */

  { WG_DONT_DO_ANYTHING_WITH_NS,
      "Jelenleg ne tegyen semmit a(z) {0} n\u00e9vt\u00e9rrel a tulajdons\u00e1gban: {1}"},

  /** Field WG_SECURITY_EXCEPTION          */

  { WG_SECURITY_EXCEPTION,
      "SecurityException az XSL rendszertulajdons\u00e1g el\u00e9r\u00e9s\u00e9n\u00e9l: {0}"},

  /** Field WG_QUO_NO_LONGER_DEFINED          */

  { WG_QUO_NO_LONGER_DEFINED,
      "A r\u00e9gi szintaktika: quo(...) t\u00f6bb\u00e9 nincs defini\u00e1lva az XPath-ban."},

  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */

  { WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "Az XPath-nak kell egy sz\u00e1rmaztatott objektum a nodeTest-hez!"},

  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */

  { WG_FUNCTION_TOKEN_NOT_FOUND,
      "A f\u00fcggv\u00e9ny jelsor nem tal\u00e1lhat\u00f3."},

  /** Field WG_COULDNOT_FIND_FUNCTION          */

  { WG_COULDNOT_FIND_FUNCTION,
      "Nem tal\u00e1lhat\u00f3 a f\u00fcggv\u00e9ny: {0}"},

  /** Field WG_CANNOT_MAKE_URL_FROM          */

  { WG_CANNOT_MAKE_URL_FROM,
      "Nem k\u00e9sz\u00edthet\u0151 URL ebb\u0151l: {0}"},

  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */

  { WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "A -E opci\u00f3 nem t\u00e1mogatott a DTM elemz\u0151h\u00f6z"},

  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */

  { WG_ILLEGAL_VARIABLE_REFERENCE,
      "A v\u00e1ltoz\u00f3ra adott VariableReference (v\u00e1ltoz\u00f3hivatkoz\u00e1s) k\u00edv\u00fcl van a k\u00f6rnyezeten vagy nincs defin\u00edci\u00f3ja!  N\u00e9v = {0}"},

  /** Field WG_UNSUPPORTED_ENCODING          */

  { WG_UNSUPPORTED_ENCODING,
     "Nem t\u00e1mogatott k\u00f3dol\u00e1s: {0}"},



  { "ui_language", "hu"},
  { "help_language", "hu"},
  { "language", "hu"},
  { "BAD_CODE", "A createMessage param\u00e9tere nincs a megfelel\u0151 tartom\u00e1nyban"},
  { "FORMAT_FAILED", "Kiv\u00e9tel t\u00f6rt\u00e9nt a messageFormat h\u00edv\u00e1s alatt"},
  { "version", ">>>>>>> Xalan verzi\u00f3 "},
  { "version2", "<<<<<<<"},
  { "yes", "igen"},
  { "line", "Sor #"},
  { "column", "Oszlop #"},
  { "xsldone", "XSLProcessor: k\u00e9sz"},
  { "xpath_option", "xpath opci\u00f3i: "},
  { "optionIN", "   [-in bemenetiXMLURL]"},
  { "optionSelect", "   [-select xpath kifejez\u00e9s]"},
  { "optionMatch", "   [-match illeszt\u00e9si minta (az illeszt\u00e9si diagnosztik\u00e1hoz)]"},
  { "optionAnyExpr", "Vagy csak egy xpath kifejez\u00e9s megcsin\u00e1l egy diagnosztikai dump-ot"},
  { "noParsermsg1", "Az XSL processz sikertelen volt."},
  { "noParsermsg2", "** Az elemz\u0151 nem tal\u00e1lhat\u00f3 **"},
  { "noParsermsg3", "K\u00e9rem, ellen\u0151rizze az oszt\u00e1ly el\u00e9r\u00e9si utat."},
  { "noParsermsg4", "Ha \u00f6nnek nincs meg az IBM Java XML elemz\u0151je, akkor let\u00f6ltheti az"},
  { "gtone", ">1" },
  { "zero", "0" },
  { "one", "1" },
  { "two" , "2" },
  { "three", "3" }

  };
  }



  /** Field BAD_CODE          */
  public static final String BAD_CODE = "BAD_CODE";

  /** Field FORMAT_FAILED          */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** Field ERROR_RESOURCES          */
  public static final String ERROR_RESOURCES =
    "org.apache.xpath.res.XPATHErrorResources";

  /** Field ERROR_STRING          */
  public static final String ERROR_STRING = "#error";

  /** Field ERROR_HEADER          */
  public static final String ERROR_HEADER = "Hiba: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Figyelmeztet\u00e9s: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "MINTA ";


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
                new Locale("hu", "HU"));
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
