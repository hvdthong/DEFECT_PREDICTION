package org.apache.xpath.res;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * <meta name="usage" content="advanced"/>
 * Set up error messages.
 * We build a two dimensional array of message keys and
 * message strings. In order to add a new message here,
 * you need to first update the count of messages(MAX_CODE)or
 * the count of warnings(MAX_WARNING). The array will be
 * automatically filled in with the keys, but you need to
 * fill in the actual message string. Follow the instructions
 * below.
 */
public class XPATHErrorResources extends PropertyResourceBundle
{
  
  public XPATHErrorResources()
  	throws java.io.IOException
  {
    super(null);
  }
  
  public XPATHErrorResources(InputStream is) 
  	throws java.io.IOException
  {
  	super(is);
  }

  /** Field ERROR_SUFFIX          */
  public static final String ERROR_SUFFIX = "ER";

  /** Field WARNING_SUFFIX          */
  public static final String WARNING_SUFFIX = "WR";


  /*
  * Now fill in the message text.
  * First create an int for the message code. Make sure you
  * update MAX_CODE for error messages and MAX_WARNING for warnings
  * Then fill in the message text for that message code in the
  * array. Use the new error code as the index into the array.
  */


  /** Field ERROR0000          */
  public static final int ERROR0000 = 0;

  /** Field ER_CURRENT_NOT_ALLOWED_IN_MATCH          */
  public static final int ER_CURRENT_NOT_ALLOWED_IN_MATCH = 1;


  /** Field ER_CURRENT_TAKES_NO_ARGS          */
  public static final int ER_CURRENT_TAKES_NO_ARGS = 2;


  /** Field ER_DOCUMENT_REPLACED          */
  public static final int ER_DOCUMENT_REPLACED = 3;


  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */
  public static final int ER_CONTEXT_HAS_NO_OWNERDOC = 4;


  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */
  public static final int ER_LOCALNAME_HAS_TOO_MANY_ARGS = 5;


  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */
  public static final int ER_NAMESPACEURI_HAS_TOO_MANY_ARGS = 6;


  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */
  public static final int ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS = 7;


  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */
  public static final int ER_NUMBER_HAS_TOO_MANY_ARGS = 8;


  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */
  public static final int ER_NAME_HAS_TOO_MANY_ARGS = 9;


  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */
  public static final int ER_STRING_HAS_TOO_MANY_ARGS = 10;


  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */
  public static final int ER_STRINGLENGTH_HAS_TOO_MANY_ARGS = 11;


  /** Field ER_TRANSLATE_TAKES_3_ARGS          */
  public static final int ER_TRANSLATE_TAKES_3_ARGS = 12;


  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */
  public static final int ER_UNPARSEDENTITYURI_TAKES_1_ARG = 13;


  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */
  public static final int ER_NAMESPACEAXIS_NOT_IMPLEMENTED = 14;


  /** Field ER_UNKNOWN_AXIS          */
  public static final int ER_UNKNOWN_AXIS = 15;


  /** Field ER_UNKNOWN_MATCH_OPERATION          */
  public static final int ER_UNKNOWN_MATCH_OPERATION = 16;


  /** Field ER_INCORRECT_ARG_LENGTH          */
  public static final int ER_INCORRECT_ARG_LENGTH = 17;


  /** Field ER_CANT_CONVERT_TO_NUMBER          */
  public static final int ER_CANT_CONVERT_TO_NUMBER = 18;


  /** Field ER_CANT_CONVERT_TO_NODELIST          */
  public static final int ER_CANT_CONVERT_TO_NODELIST = 19;


  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */
  public static final int ER_CANT_CONVERT_TO_MUTABLENODELIST = 20;


  /** Field ER_CANT_CONVERT_TO_TYPE          */
  public static final int ER_CANT_CONVERT_TO_TYPE = 21;


  /** Field ER_EXPECTED_MATCH_PATTERN          */
  public static final int ER_EXPECTED_MATCH_PATTERN = 22;


  /** Field ER_COULDNOT_GET_VAR_NAMED          */
  public static final int ER_COULDNOT_GET_VAR_NAMED = 23;


  /** Field ER_UNKNOWN_OPCODE          */
  public static final int ER_UNKNOWN_OPCODE = 24;


  /** Field ER_EXTRA_ILLEGAL_TOKENS          */
  public static final int ER_EXTRA_ILLEGAL_TOKENS = 25;


  /** Field ER_EXPECTED_DOUBLE_QUOTE          */
  public static final int ER_EXPECTED_DOUBLE_QUOTE = 26;


  /** Field ER_EXPECTED_SINGLE_QUOTE          */
  public static final int ER_EXPECTED_SINGLE_QUOTE = 27;


  /** Field ER_EMPTY_EXPRESSION          */
  public static final int ER_EMPTY_EXPRESSION = 28;


  /** Field ER_EXPECTED_BUT_FOUND          */
  public static final int ER_EXPECTED_BUT_FOUND = 29;


  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */
  public static final int ER_INCORRECT_PROGRAMMER_ASSERTION = 30;


  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */
  public static final int ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL = 31;


  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */
  public static final int ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG = 32;


  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */
  public static final int ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG = 33;


  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */
  public static final int ER_PREDICATE_ILLEGAL_SYNTAX = 34;


  /** Field ER_ILLEGAL_AXIS_NAME          */
  public static final int ER_ILLEGAL_AXIS_NAME = 35;


  /** Field ER_UNKNOWN_NODETYPE          */
  public static final int ER_UNKNOWN_NODETYPE = 36;


  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */
  public static final int ER_PATTERN_LITERAL_NEEDS_BE_QUOTED = 37;


  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */
  public static final int ER_COULDNOT_BE_FORMATTED_TO_NUMBER = 38;


  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */
  public static final int ER_COULDNOT_CREATE_XMLPROCESSORLIAISON = 39;


  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */
  public static final int ER_DIDNOT_FIND_XPATH_SELECT_EXP = 40;


  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */
  public static final int ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH = 41;


  /** Field ER_ERROR_OCCURED          */
  public static final int ER_ERROR_OCCURED = 42;


  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */
  public static final int ER_ILLEGAL_VARIABLE_REFERENCE = 43;


  /** Field ER_AXES_NOT_ALLOWED          */
  public static final int ER_AXES_NOT_ALLOWED = 44;


  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */
  public static final int ER_KEY_HAS_TOO_MANY_ARGS = 45;


  /** Field ER_COUNT_TAKES_1_ARG          */
  public static final int ER_COUNT_TAKES_1_ARG = 46;


  /** Field ER_COULDNOT_FIND_FUNCTION          */
  public static final int ER_COULDNOT_FIND_FUNCTION = 47;


  /** Field ER_UNSUPPORTED_ENCODING          */
  public static final int ER_UNSUPPORTED_ENCODING = 48;


  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */
  public static final int ER_PROBLEM_IN_DTM_NEXTSIBLING = 49;


  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */
  public static final int ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL = 50;


  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */
  public static final int ER_SETDOMFACTORY_NOT_SUPPORTED = 51;


  /** Field ER_PREFIX_MUST_RESOLVE          */
  public static final int ER_PREFIX_MUST_RESOLVE = 52;


  /** Field ER_PARSE_NOT_SUPPORTED          */
  public static final int ER_PARSE_NOT_SUPPORTED = 53;


  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */
  public static final int ER_CREATEDOCUMENT_NOT_SUPPORTED = 54;


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */
  public static final int ER_CHILD_HAS_NO_OWNER_DOCUMENT = 55;


  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */
  public static final int ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT = 56;


  /** Field ER_SAX_API_NOT_HANDLED          */
  public static final int ER_SAX_API_NOT_HANDLED = 57;


  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */
  public static final int ER_IGNORABLE_WHITESPACE_NOT_HANDLED = 58;


  /** Field ER_DTM_CANNOT_HANDLE_NODES          */
  public static final int ER_DTM_CANNOT_HANDLE_NODES = 59;


  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */
  public static final int ER_XERCES_CANNOT_HANDLE_NODES = 60;


  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */
  public static final int ER_XERCES_PARSE_ERROR_DETAILS = 61;


  /** Field ER_XERCES_PARSE_ERROR          */
  public static final int ER_XERCES_PARSE_ERROR = 62;


  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */
  public static final int ER_CANT_OUTPUT_TEXT_BEFORE_DOC = 63;


  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */
  public static final int ER_CANT_HAVE_MORE_THAN_ONE_ROOT = 64;


  /** Field ER_INVALID_UTF16_SURROGATE          */
  public static final int ER_INVALID_UTF16_SURROGATE = 65;


  /** Field ER_OIERROR          */
  public static final int ER_OIERROR = 66;


  /** Field ER_CANNOT_CREATE_URL          */
  public static final int ER_CANNOT_CREATE_URL = 67;


  /** Field ER_XPATH_READOBJECT          */
  public static final int ER_XPATH_READOBJECT = 68;

  
  /** Field ER_XPATH_READOBJECT         */
  public static final int ER_FUNCTION_TOKEN_NOT_FOUND = 69;

  
   /**  Argument 'localName' is null  */
  public static final int ER_ARG_LOCALNAME_NULL = 70;

  
   /**  Can not deal with XPath type:   */
  public static final int ER_CANNOT_DEAL_XPATH_TYPE = 71;

  
   /**  This NodeSet is not mutable  */
  public static final int ER_NODESET_NOT_MUTABLE = 72;

  
   /**  This NodeSetDTM is not mutable  */
  public static final int ER_NODESETDTM_NOT_MUTABLE = 73;

  
   /**  Variable not resolvable:   */
  public static final int ER_VAR_NOT_RESOLVABLE = 74;

  
   /** Null error handler  */
  public static final int ER_NULL_ERROR_HANDLER = 75;

  
   /**  Programmer's assertion: unknown opcode  */
  public static final int ER_PROG_ASSERT_UNKNOWN_OPCODE = 76;

  
   /**  0 or 1   */
  public static final int ER_ZERO_OR_ONE = 77;

  
  
   /**  rtf() not supported by XRTreeFragSelectWrapper   */
  public static final int ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER = 78;

  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */
  public static final int ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER = 79;

  
   /**  fsb() not supported for XStringForChars   */
  public static final int ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS = 80;

  
   /**  Could not find variable with the name of   */
  public static final int ER_COULD_NOT_FIND_VAR = 81;

  
   /**  XStringForChars can not take a string for an argument   */
  public static final int ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING = 82;

  
   /**  The FastStringBuffer argument can not be null   */
  public static final int ER_FASTSTRINGBUFFER_CANNOT_BE_NULL = 83;

  
   /**  2 or 3   */
  public static final int ER_TWO_OR_THREE = 84;

  
  public static final int ER_VARIABLE_ACCESSED_BEFORE_BIND = 85;
  
  public static final int ER_FSB_CANNOT_TAKE_STRING = 86;
  
  public static final int ER_SETTING_WALKER_ROOT_TO_NULL = 87;
  
  public static final int ER_NODESETDTM_CANNOT_ITERATE = 88;
  
  public static final int ER_NODESET_CANNOT_ITERATE = 89;

  public static final int ER_NODESETDTM_CANNOT_INDEX = 90;
  
  public static final int ER_NODESET_CANNOT_INDEX = 91;

  public static final int ER_CANNOT_CALL_SETSHOULDCACHENODE = 92;
  
  public static final int ER_ONLY_ALLOWS = 93;
  
  public static final int ER_UNKNOWN_STEP = 94;
  
  /** Problem with RelativeLocationPath */
  public static final int ER_EXPECTED_REL_LOC_PATH = 95;


   /** Problem with LocationPath */
  public static final int ER_EXPECTED_LOC_PATH = 96;


   /** Problem with Step */
  public static final int ER_EXPECTED_LOC_STEP = 97;


   /** Problem with NodeTest */
  public static final int ER_EXPECTED_NODE_TEST = 98;


   /** Expected step pattern */
  public static final int ER_EXPECTED_STEP_PATTERN = 99;

  
   /** Expected relative path pattern */
  public static final int ER_EXPECTED_REL_PATH_PATTERN = 100;
  
  /** localname in QNAME should be a valid NCName */  
  public static final int ER_ARG_LOCALNAME_INVALID = 101;

  /** prefix in QNAME should be a valid NCName */
  public static final int ER_ARG_PREFIX_INVALID = 102;

 



  

  /** Field WG_LOCALE_NAME_NOT_HANDLED          */
  public static final int WG_LOCALE_NAME_NOT_HANDLED = 1;


  /** Field WG_PROPERTY_NOT_SUPPORTED          */
  public static final int WG_PROPERTY_NOT_SUPPORTED = 2;


  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */
  public static final int WG_DONT_DO_ANYTHING_WITH_NS = 3;


  /** Field WG_SECURITY_EXCEPTION          */
  public static final int WG_SECURITY_EXCEPTION = 4;


  /** Field WG_QUO_NO_LONGER_DEFINED          */
  public static final int WG_QUO_NO_LONGER_DEFINED = 5;


  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */
  public static final int WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST = 6;


  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */
  public static final int WG_FUNCTION_TOKEN_NOT_FOUND = 7;


  /** Field WG_COULDNOT_FIND_FUNCTION          */
  public static final int WG_COULDNOT_FIND_FUNCTION = 8;


  /** Field WG_CANNOT_MAKE_URL_FROM          */
  public static final int WG_CANNOT_MAKE_URL_FROM = 9;


  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */
  public static final int WG_EXPAND_ENTITIES_NOT_SUPPORTED = 10;


  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */
  public static final int WG_ILLEGAL_VARIABLE_REFERENCE = 11;


  /** Field WG_UNSUPPORTED_ENCODING          */
  public static final int WG_UNSUPPORTED_ENCODING = 12;




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
  public static final String ERROR_HEADER = "Error: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "Warning: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "PATTERN ";


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
  public static final ResourceBundle loadResourceBundle(String className)
          throws MissingResourceException
  {

    Locale locale = Locale.getDefault();
    String suffix = getResourceSuffix(locale);

    try
    {

      return ResourceBundle.getBundle(className
              + suffix, locale);
    }
    catch (MissingResourceException e)
    {
      {

        return ResourceBundle.getBundle(className,
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

  /**
   * Get the error string associated with the error code
   *
   * @param errorCode Error code
   *
   * @return error string associated with the given error code
   */
  public static String getMessageKey(int errorCode)
  {

    {
      DecimalFormat df = new DecimalFormat("0000");

      return ERROR_SUFFIX + df.format(errorCode);
    }
  }

  /**
   * Get the warning string associated with the error code
   *
   * @param errorCode Error code
   * 
   * @return warning string associated with the given error code
   */
  public static String getWarningKey(int errorCode)
  {

    {
      DecimalFormat df = new DecimalFormat("0000");

      return WARNING_SUFFIX + df.format(errorCode);
    }
  }

  /**
   * Get the key string for an error based on the integer representation.
   *
   * @param errorCode Error code
   * 
   * @return key string that may be used for lookup in the association table.
   */
  public static String getMKey(int errorCode)
  {

    {
      DecimalFormat df = new DecimalFormat("0000");

      return ERROR_SUFFIX + df.format(errorCode);
    }
  }

  /**
   * Get the key string for an warning based on the integer representation.
   *
   * @param errorCode Error code
   * 
   * @return key string that may be used for lookup in the association table.
   */
  public static String getWKey(int errorCode)
  {

    {
      DecimalFormat df = new DecimalFormat("0000");

      return WARNING_SUFFIX + df.format(errorCode);
    }
  }
}
