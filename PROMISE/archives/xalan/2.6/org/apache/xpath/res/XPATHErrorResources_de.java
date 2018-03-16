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
public class XPATHErrorResources_de extends ListResourceBundle
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

  { ER_CURRENT_NOT_ALLOWED_IN_MATCH, "Die Funktion current() ist in einem \u00dcbereinstimmungsmuster nicht zul\u00e4ssig!" },

  /** Field ER_CURRENT_TAKES_NO_ARGS          */

  { ER_CURRENT_TAKES_NO_ARGS, "In der Funktion current() d\u00fcrfen keine Argumente angegeben werden!" },

  /** Field ER_DOCUMENT_REPLACED          */
  { ER_DOCUMENT_REPLACED,
      "Die Implementierung der Funktion document() wurde durch org.apache.xalan.xslt.FuncDocument ersetzt!"},


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */

  { ER_CONTEXT_HAS_NO_OWNERDOC,
      "Der Kontextknoten verf\u00fcgt nicht \u00fcber ein Eignerdokument!"},

  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */

  { ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name() weist zu viele Argumente auf."},

  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */

  { ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri() weist zu viele Argumente auf."},

  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */
  { ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space() weist zu viele Argumente auf."},

  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */

  { ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number() weist zu viele Argumente auf."},

  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */

  { ER_NAME_HAS_TOO_MANY_ARGS,
     "name() weist zu viele Argumente auf."},

  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */

  { ER_STRING_HAS_TOO_MANY_ARGS,
      "string() weist zu viele Argumente auf."},

  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */

  { ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length() weist zu viele Argumente auf."},

  /** Field ER_TRANSLATE_TAKES_3_ARGS          */

  { ER_TRANSLATE_TAKES_3_ARGS,
      "Die Funktion translate() erfordert drei Argumente!"},

  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */

  { ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "Die Funktion unparsed-entity-uri sollte ein einziges Argument enthalten!"},

  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */

  { ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "Die Namensbereichachse ist bisher nicht implementiert!"},

  /** Field ER_UNKNOWN_AXIS          */

  { ER_UNKNOWN_AXIS,
     "Unbekannte Achse: {0}"},

  /** Field ER_UNKNOWN_MATCH_OPERATION          */

  { ER_UNKNOWN_MATCH_OPERATION,
     "Unbekannter \u00dcbereinstimmungsvorgang!"},

  /** Field ER_INCORRECT_ARG_LENGTH          */

  { ER_INCORRECT_ARG_LENGTH,
      "Die L\u00e4nge des Arguments f\u00fcr den Knotentest von processing-instruction() ist falsch!"},

  /** Field ER_CANT_CONVERT_TO_NUMBER          */

  { ER_CANT_CONVERT_TO_NUMBER,
      "{0} kann nicht in eine Zahl konvertiert werden!"},

  /** Field ER_CANT_CONVERT_TO_NODELIST          */

  { ER_CANT_CONVERT_TO_NODELIST,
      "{0} kann nicht in NodeList konvertiert werden!"},

  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */

  { ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "{0} kann nicht in NodeSetDTM konvertiert werden!"},

  /** Field ER_CANT_CONVERT_TO_TYPE          */

  { ER_CANT_CONVERT_TO_TYPE,
      "{0} kann nicht in type#{1} konvertiert werden."},

  /** Field ER_EXPECTED_MATCH_PATTERN          */

  { ER_EXPECTED_MATCH_PATTERN,
      "\u00dcbereinstimmungsmuster in getMatchScore erwartet!"},

  /** Field ER_COULDNOT_GET_VAR_NAMED          */

  { ER_COULDNOT_GET_VAR_NAMED,
      "Die Variable mit dem Namen {0} konnte nicht abgerufen werden."},

  /** Field ER_UNKNOWN_OPCODE          */

  { ER_UNKNOWN_OPCODE,
     "FEHLER! Unbekannter Operationscode: {0}"},

  /** Field ER_EXTRA_ILLEGAL_TOKENS          */

  { ER_EXTRA_ILLEGAL_TOKENS,
     "Zus\u00e4tzliche nicht zul\u00e4ssige Token: {0}"},

  /** Field ER_EXPECTED_DOUBLE_QUOTE          */

  { ER_EXPECTED_DOUBLE_QUOTE,
      "Falsche Anf\u00fchrungszeichen f\u00fcr Literal... Doppelte Anf\u00fchrungszeichen wurden erwartet!"},

  /** Field ER_EXPECTED_SINGLE_QUOTE          */

  { ER_EXPECTED_SINGLE_QUOTE,
      "Falsche Anf\u00fchrungszeichen f\u00fcr Literal... Einfache Anf\u00fchrungszeichen wurden erwartet!"},

  /** Field ER_EMPTY_EXPRESSION          */

  { ER_EMPTY_EXPRESSION,
     "Leerer Ausdruck!"},

  /** Field ER_EXPECTED_BUT_FOUND          */

  { ER_EXPECTED_BUT_FOUND,
     "Erwartet wurde {0}, gefunden wurde: {1}"},

  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */

  { ER_INCORRECT_PROGRAMMER_ASSERTION,
      "Festlegung des Programmierers ist falsch! - {0}"},

  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */

  { ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "Das Argument boolean(...) ist im XPath-Entwurf 19990709 nicht mehr optional."},

  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */

  { ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "Gefunden wurde ',' ohne vorangestelltes Argument!"},

  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */

  { ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "Gefunden wurde ',' ohne nachfolgendes Argument!"},

  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */

  { ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[predicate]' oder '.[predicate]' ist eine nicht zul\u00e4ssige Syntax.  Verwenden Sie stattdessen 'self::node()[predicate]'."},

  /** Field ER_ILLEGAL_AXIS_NAME          */

  { ER_ILLEGAL_AXIS_NAME,
     "Nicht zul\u00e4ssiger Achsenname: {0}"},

  /** Field ER_UNKNOWN_NODETYPE          */

  { ER_UNKNOWN_NODETYPE,
     "Unbekannter Knotentyp: {0}"},

  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */

  { ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "Musterliteral ({0}) muss in Anf\u00fchrungszeichen angegeben werden!"},

  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */

  { ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0} konnte nicht als Zahl formatiert werden!"},

  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */

  { ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "XML-TransformerFactory-Liaison konnte nicht erstellt werden: {0}"},

  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */

  { ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "Fehler! xpath-Auswahlausdruck (-select) konnte nicht gefunden werden."},

  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */

  { ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "FEHLER! ENDOP konnte nach OP_LOCATIONPATH nicht gefunden werden."},

  /** Field ER_ERROR_OCCURED          */

  { ER_ERROR_OCCURED,
     "Es ist ein Fehler aufgetreten!"},

  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */

  { ER_ILLEGAL_VARIABLE_REFERENCE,
      "Das f\u00fcr die Variable angegebene Argument VariableReference befindet sich au\u00dferhalb des Kontexts oder weist keine Definition auf!  Name = {0}"},

  /** Field ER_AXES_NOT_ALLOWED          */

  { ER_AXES_NOT_ALLOWED,
      "Nur die Achsen ''child::'' und ''attribute::'' sind in Suchmustern zul\u00e4ssig!  Fehlerhafte Achsen = {0}"},

  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */

  { ER_KEY_HAS_TOO_MANY_ARGS,
      "key() weist eine falsche Anzahl Argumenten auf."},

  /** Field ER_COUNT_TAKES_1_ARG          */

  { ER_COUNT_TAKES_1_ARG,
      "Die Funktion count sollte ein einziges Argument enthalten!"},

  /** Field ER_COULDNOT_FIND_FUNCTION          */

  { ER_COULDNOT_FIND_FUNCTION,
     "Die Funktion konnte nicht gefunden werden: {0}"},

  /** Field ER_UNSUPPORTED_ENCODING          */

  { ER_UNSUPPORTED_ENCODING,
     "Nicht unterst\u00fctzte Verschl\u00fcsselung: {0}"},

  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */

  { ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "In dem DTM in getNextSibling ist ein Fehler aufgetreten... Wiederherstellung wird durchgef\u00fchrt"},

  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */

  { ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "Programmierungsfehler: In EmptyNodeList kann nicht geschrieben werden."},

  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */

  { ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory wird nicht von XPathContext unterst\u00fctzt!"},

  /** Field ER_PREFIX_MUST_RESOLVE          */

  { ER_PREFIX_MUST_RESOLVE,
      "Das Pr\u00e4fix muss in einen Namensbereich aufgel\u00f6st werden: {0}"},

  /** Field ER_PARSE_NOT_SUPPORTED          */

  { ER_PARSE_NOT_SUPPORTED,
      "parse (InputSource Quelle) wird nicht in XPathContext unterst\u00fctzt! {0} kann nicht ge\u00f6ffnet werden."},

  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */


  /** Field ER_SAX_API_NOT_HANDLED          */

  { ER_SAX_API_NOT_HANDLED,
      "SAX-API characters(char ch[]... wird nicht von dem DTM verarbeitet!"},

  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */

  { ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]... wird nicht von dem DTM verarbeitet!"},

  /** Field ER_DTM_CANNOT_HANDLE_NODES          */

  { ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison kann keine Knoten vom Typ {0} verarbeiten"},

  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */

  { ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper kann keine Knoten vom Typ {0} verarbeiten"},

  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */

  { ER_XERCES_PARSE_ERROR_DETAILS,
      "Fehler bei DOM2Helper.parse: System-ID - {0} Zeile - {1}"},

  /** Field ER_XERCES_PARSE_ERROR          */

  { ER_XERCES_PARSE_ERROR,
     "Fehler bei DOM2Helper.parse"},

  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */


  /** Field ER_INVALID_UTF16_SURROGATE          */

  { ER_INVALID_UTF16_SURROGATE,
      "Ung\u00fcltige UTF-16-Ersetzung festgestellt: {0} ?"},

  /** Field ER_OIERROR          */

  { ER_OIERROR,
     "E/A-Fehler"},

  /** Field ER_CANNOT_CREATE_URL          */

  { ER_CANNOT_CREATE_URL,
     "URL kann nicht erstellt werden f\u00fcr: {0}"},

  /** Field ER_XPATH_READOBJECT          */

  { ER_XPATH_READOBJECT,
     "In XPath.readObject: {0}"},

  /** Field ER_FUNCTION_TOKEN_NOT_FOUND         */

  { ER_FUNCTION_TOKEN_NOT_FOUND,
      "Funktionstoken wurde nicht gefunden."},

   /**  Argument 'localName' is null  */


   /**  Can not deal with XPath type:   */

  { ER_CANNOT_DEAL_XPATH_TYPE,
       "Der XPath-Typ kann nicht verarbeitet werden: {0}"},

   /**  This NodeSet is not mutable  */

  { ER_NODESET_NOT_MUTABLE,
       "Diese NodeSet kann nicht ge\u00e4ndert werden"},

   /**  This NodeSetDTM is not mutable  */

  { ER_NODESETDTM_NOT_MUTABLE,
       "Dieses NodeSetDTM kann nicht ge\u00e4ndert werden"},

   /**  Variable not resolvable:   */

  { ER_VAR_NOT_RESOLVABLE,
        "Die Variable kann nicht aufgel\u00f6st werden: {0}"},

   /** Null error handler  */

  { ER_NULL_ERROR_HANDLER,
        "Kein Fehlerbehandlungsprogramm vorhanden"},

   /**  Programmer's assertion: unknown opcode  */

  { ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "Programmiererfestlegung: Unbekannter Operationscode: {0} "},

   /**  0 or 1   */

  { ER_ZERO_OR_ONE,
       "0 oder 1"},


   /**  rtf() not supported by XRTreeFragSelectWrapper   */

  { ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "rtf() wird nicht von XRTreeFragSelectWrapper unterst\u00fctzt"},

   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */

  { ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "asNodeIterator() wird nicht von XRTreeFragSelectWrapper unterst\u00fctzt"},

   /**  fsb() not supported for XStringForChars   */

  { ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb() wird nicht f\u00fcr XStringForChars unterst\u00fctzt"},

   /**  Could not find variable with the name of   */

  { ER_COULD_NOT_FIND_VAR,
      "Die Variable mit dem Namen {0} konnte nicht gefunden werden"},

   /**  XStringForChars can not take a string for an argument   */

  { ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars kann keine Zeichenfolge als Argument enthalten"},

   /**  The FastStringBuffer argument can not be null   */

  { ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "Das Argument FastStringBuffer kann nicht Null sein"},

  /* MANTIS_XALAN CHANGE: BEGIN */
   /**  2 or 3   */

  { ER_TWO_OR_THREE,
       "2 oder 3"},

   /** Variable accessed before it is bound! */

  { ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "Auf die Variable wurde zugegriffen, bevor diese gebunden wurde!"},

   /** XStringForFSB can not take a string for an argument! */

  { ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB kann keine Zeichenfolge als Argument enthalten!"},

   /** Error! Setting the root of a walker to null! */

  { ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! Fehler! Root eines Walker wird auf Null gesetzt!!!"},

   /** This NodeSetDTM can not iterate to a previous node! */

  { ER_NODESETDTM_CANNOT_ITERATE,
       "Dieses NodeSetDTM kann keinen vorherigen Knoten wiederholen!"},

  /** This NodeSet can not iterate to a previous node! */

  { ER_NODESET_CANNOT_ITERATE,
       "Diese NodeSet kann keinen vorherigen Knoten wiederholen!"},

  /** This NodeSetDTM can not do indexing or counting functions! */

  { ER_NODESETDTM_CANNOT_INDEX,
       "Dieses NodeSetDTM kann keine Indexierungs- oder Z\u00e4hlfunktionen ausf\u00fchren!"},

  /** This NodeSet can not do indexing or counting functions! */

  { ER_NODESET_CANNOT_INDEX,
       "Diese NodeSet kann keine Indexierungs- oder Z\u00e4hlfunktionen ausf\u00fchren!"},

  /** Can not call setShouldCacheNodes after nextNode has been called! */

  { ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "setShouldCacheNodes kann nicht aufgerufen werden, nachdem nextNode aufgerufen wurde!"},

  /** {0} only allows {1} arguments */

  { ER_ONLY_ALLOWS,
       "{0} erlaubt nur {1} Argument(e)"},

  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */

  { ER_UNKNOWN_STEP,
       "Programmiererfestlegung in getNextStepPos: stepType unbekannt: {0} "},


  /** Problem with RelativeLocationPath */

  { ER_EXPECTED_REL_LOC_PATH,


  /** Problem with LocationPath */

  { ER_EXPECTED_LOC_PATH,
       "Es wurde ein Positionspfad erwartet, aber folgendes Token wurde festgestellt\u003a {0} "},


  /** Problem with Step */

  { ER_EXPECTED_LOC_STEP,


  /** Problem with NodeTest */

  { ER_EXPECTED_NODE_TEST,
       "Es wurde ein Knotentest erwartet, der entweder NCName:* oder dem QNamen entspricht."},


  /** Expected step pattern */

  { ER_EXPECTED_STEP_PATTERN,
       "Es wurde ein Schrittmuster erwartet, aber '/' festgestellt."},


  /** Expected relative path pattern */

  { ER_EXPECTED_REL_PATH_PATTERN,
       "Es wurde ein Muster eines relativen Pfads erwartet."},


  /** localname in QNAME should be a valid NCName */



  /** prefix in QNAME should be a valid NCName */



  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */

  { ER_CANT_CONVERT_TO_BOOLEAN,
       "{0} kann nicht in einen Booleschen Wert konvertiert werden."},


  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */

  { ER_CANT_CONVERT_TO_SINGLENODE,
       "{0} kann nicht in einen Einzelknoten konvertiert werden. Diese Abruffunktion gilt f\u00fcr die Typen ANY_UNORDERED_NODE_TYPE und FIRST_ORDERED_NODE_TYPE."},


  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */

  { ER_CANT_GET_SNAPSHOT_LENGTH,
       "Die L\u00e4nge der Momentaufnahme f\u00fcr den Typ {0} kann nicht abgerufen werden. Diese Abruffunktion gilt f\u00fcr die Typen UNORDERED_NODE_SNAPSHOT_TYPE und ORDERED_NODE_SNAPSHOT_TYPE."},

  /** Field ER_NON_ITERATOR_TYPE                */

  { ER_NON_ITERATOR_TYPE,
       "F\u00fcr Nicht-Iterator-Typen k\u00f6nnen keine Iterationen ausgef\u00fchrt werden: {0}"},


  /** Field ER_DOC_MUTATED                      */

  { ER_DOC_MUTATED,
       "Seit der R\u00fcckgabe des Ergebnisses wurde das Dokument ge\u00e4ndert. Der Iterator ist ung\u00fcltig."},

  /** Field ER_INVALID_XPATH_TYPE               */

  { ER_INVALID_XPATH_TYPE,
       "Ung\u00fcltiges XPath-Typenargument: {0}"},

  /** Field ER_EMPTY_XPATH_RESULT                */

  { ER_EMPTY_XPATH_RESULT,
       "Leeres XPath-Ergebnisobjekt"},

  /** Field ER_INCOMPATIBLE_TYPES                */

  { ER_INCOMPATIBLE_TYPES,
       "Eine Konvertierung des zur\u00fcckgegebenen Typs {0} in den angegebenen Typ {1} kann nicht erzwungen werden."},

  /** Field ER_NULL_RESOLVER                     */

  { ER_NULL_RESOLVER,
       "Das Pr\u00e4fix kann nicht mit einer Aufl\u00f6sungsfunktion f\u00fcr Nullpr\u00e4fixe aufgel\u00f6st werden."},


  /** Field ER_CANT_CONVERT_TO_STRING            */

  { ER_CANT_CONVERT_TO_STRING,
       "{0} kann nicht in eine Zeichenfolge (string) konvertiert werden."},


  /** Field ER_NON_SNAPSHOT_TYPE                 */

  { ER_NON_SNAPSHOT_TYPE,
       "snapshotItem kann nicht f\u00fcr Typ {0} aufgerufen werden. Diese Methode gilt f\u00fcr die Typen UNORDERED_NODE_SNAPSHOT_TYPE und ORDERED_NODE_SNAPSHOT_TYPE."},


  /** Field ER_WRONG_DOCUMENT                    */

  { ER_WRONG_DOCUMENT,
       "Kontextknoten geh\u00f6rt nicht zu dem Dokument, das an diesen XPathEvaluator gebunden ist."},

  /** Field ER_WRONG_NODETYPE                    */

  { ER_WRONG_NODETYPE,
       "Der Kontextknotentyp wird nicht unterst\u00fctzt."},

  /** Field ER_XPATH_ERROR                       */

  { ER_XPATH_ERROR,
       "Unbekannter Fehler in XPath."},



  /** Field WG_LOCALE_NAME_NOT_HANDLED          */

  { WG_LOCALE_NAME_NOT_HANDLED,
      "Der Name der L\u00e4ndereinstellung in der Funktion format-number wurde bisher nicht verarbeitet!"},

  /** Field WG_PROPERTY_NOT_SUPPORTED          */

  { WG_PROPERTY_NOT_SUPPORTED,
      "XSL-Merkmal wird nicht unterst\u00fctzt: {0}"},

  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */

  { WG_DONT_DO_ANYTHING_WITH_NS,
      "F\u00fchren Sie derzeit keine Vorg\u00e4nge mit dem Namensbereich {0} in folgendem Merkmal durch: {1}"},

  /** Field WG_SECURITY_EXCEPTION          */

  { WG_SECURITY_EXCEPTION,
      "SecurityException beim Zugriff auf XSL-Systemmerkmal: {0}"},

  /** Field WG_QUO_NO_LONGER_DEFINED          */

  { WG_QUO_NO_LONGER_DEFINED,
      "Veraltete Syntax: quo(...) ist nicht mehr in XPath definiert."},

  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */

  { WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath ben\u00f6tigt f\u00fcr die Implementierung von nodeTest ein abgeleitetes Objekt!"},

  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */

  { WG_FUNCTION_TOKEN_NOT_FOUND,
      "Funktionstoken wurde nicht gefunden."},

  /** Field WG_COULDNOT_FIND_FUNCTION          */

  { WG_COULDNOT_FIND_FUNCTION,
      "Die Funktion konnte nicht gefunden werden: {0}"},

  /** Field WG_CANNOT_MAKE_URL_FROM          */

  { WG_CANNOT_MAKE_URL_FROM,
      "URL konnte nicht erstellt werden aus: {0}"},

  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */

  { WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "Option -E wird f\u00fcr DTM-Parser nicht unterst\u00fctzt"},

  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */

  { WG_ILLEGAL_VARIABLE_REFERENCE,
      "Das f\u00fcr die Variable angegebene Argument VariableReference befindet sich au\u00dferhalb des Kontexts oder weist keine Definition auf!  Name = {0}"},

  /** Field WG_UNSUPPORTED_ENCODING          */

  { WG_UNSUPPORTED_ENCODING,
     "Nicht unterst\u00fctzte Verschl\u00fcsselung: {0}"},



  { "ui_language", "de"},
  { "help_language", "de"},
  { "language", "de"},
  { "BAD_CODE", "Der Parameter f\u00fcr createMessage lag au\u00dferhalb des g\u00fcltigen Bereichs"},
  { "FORMAT_FAILED", "W\u00e4hrend des Aufrufs von messageFormat wurde eine Ausnahmebedingung ausgel\u00f6st"},
  { "version", ">>>>>>> Xalan-Version "},
  { "version2", "<<<<<<<"},
  { "yes", "ja"},
  { "line", "Zeilennummer"},
  { "column", "Spaltennummer"},
  { "xsldone", "XSLProcessor: fertig"},
  { "xpath_option", "xpath-Optionen: "},
  { "optionIN", "[-in EingabeXMLURL]"},
  { "optionSelect", "[-select Xpath-Ausdruck]"},
  { "optionMatch", "[-match \u00dcbereinstimmungsmuster (f\u00fcr \u00dcbereinstimmungsdiagnose)]"},
  { "optionAnyExpr", "\u00dcber einen einfachen xpath-Ausdruck wird ein Diagnosespeicherauszug erstellt"},
  { "noParsermsg1", "XSL-Prozess konnte nicht erfolgreich durchgef\u00fchrt werden."},
  { "noParsermsg2", "** Parser konnte nicht gefunden werden **"},
  { "noParsermsg3", "Bitte \u00fcberpr\u00fcfen Sie den Klassenpfad."},
  { "noParsermsg4", "Wenn Sie nicht \u00fcber einen IBM XML-Parser f\u00fcr Java verf\u00fcgen, k\u00f6nnen Sie ihn \u00fcber"},
  { "gtone", ">1" },
  { "zero", "0" },
  { "one", "1" },
  { "two" , "2" },
  { "three", "3" }

  };



  /** Field BAD_CODE          */
  public static final String BAD_CODE = "FEHLERHAFTER_CODE";

  /** Field FORMAT_FAILED          */
  public static final String FORMAT_FAILED = "FORMAT_FEHLGESCHLAGEN";

  /** Field ERROR_RESOURCES          */
  public static final String ERROR_RESOURCES =
    "org.apache.xpath.res.XPATHErrorResources";

  /** Field ERROR_STRING          */
  public static final String ERROR_STRING = "#Fehler";

  /** Field ERROR_HEADER          */
  public static final String ERROR_HEADER = "Fehler: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Achtung: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "MUSTER ";

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
