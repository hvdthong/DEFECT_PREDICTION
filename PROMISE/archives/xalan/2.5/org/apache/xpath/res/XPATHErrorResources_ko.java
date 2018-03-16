package org.apache.xpath.res;

/**
 * <meta name="usage" content="advanced"/>
 * Set up error messages.
 * We build a two dimensional array of message keys and
 * message strings. In order to add a new message here,
 * you need to first add a Static string constant for the
 * Key and update the contents array with Key, Value pair
  * Also you need to  update the count of messages(MAX_CODE)or
 * the count of warnings(MAX_WARNING) [ Information purpose only]
 */
public class XPATHErrorResources_ko extends XPATHErrorResources
{


  /** Field MAX_CODE          */

  /** Field MAX_WARNING          */

  /** Field MAX_OTHERS          */
  public static final int MAX_OTHERS = 20;

  /** Field MAX_MESSAGES          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;


  public static final Object[][] contents = {

  /** Field ERROR0000          */

 
  {
    "ERROR0000", "{0}"},
 

  /** Field ER_CURRENT_NOT_ALLOWED_IN_MATCH          */

 
  {
    ER_CURRENT_NOT_ALLOWED_IN_MATCH,
      "current() \ud568\uc218\ub294 \uc77c\uce58 \ud328\ud134\uc5d0 \ud5c8\uc6a9\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_CURRENT_TAKES_NO_ARGS          */

 
  {
    ER_CURRENT_TAKES_NO_ARGS,
      "current() \ud568\uc218\uc5d0\ub294 \uc778\uc790\uac00 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_DOCUMENT_REPLACED          */

 
  {
    ER_DOCUMENT_REPLACED,
      "document() \ud568\uc218 \uad6c\ud604\uc740 org.apache.xalan.xslt.FuncDocument\ub85c \ub300\uccb4\ub418\uc5c8\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_CONTEXT_HAS_NO_OWNERDOC          */

 
  {
    ER_CONTEXT_HAS_NO_OWNERDOC,
      "\ucee8\ud14d\uc2a4\ud2b8\uc5d0 \uc18c\uc720\uc790 \ubb38\uc11c\uac00 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_LOCALNAME_HAS_TOO_MANY_ARGS          */

 
  {
    ER_LOCALNAME_HAS_TOO_MANY_ARGS,
      "local-name()\uc758 \uc778\uc790\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_NAMESPACEURI_HAS_TOO_MANY_ARGS          */

 
  {
    ER_NAMESPACEURI_HAS_TOO_MANY_ARGS,
      "namespace-uri()\uc758 \uc778\uc790\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS          */

 
  {
    ER_NORMALIZESPACE_HAS_TOO_MANY_ARGS,
      "normalize-space()\uc758 \uc778\uc790\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_NUMBER_HAS_TOO_MANY_ARGS          */

 
  {
    ER_NUMBER_HAS_TOO_MANY_ARGS,
      "number()\uc758 \uc778\uc790\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_NAME_HAS_TOO_MANY_ARGS          */

 
  {
    ER_NAME_HAS_TOO_MANY_ARGS, "name()\uc758 \uc778\uc790\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_STRING_HAS_TOO_MANY_ARGS          */

 
  {
    ER_STRING_HAS_TOO_MANY_ARGS,
      "string()\uc758 \uc778\uc790\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_STRINGLENGTH_HAS_TOO_MANY_ARGS          */

 
  {
    ER_STRINGLENGTH_HAS_TOO_MANY_ARGS,
      "string-length()\uc758 \uc778\uc790\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_TRANSLATE_TAKES_3_ARGS          */

 
  {
    ER_TRANSLATE_TAKES_3_ARGS,
      "translate() \ud568\uc218\uc5d0\ub294 \uc138 \uac1c\uc758 \uc778\uc790\ub97c \uc0ac\uc6a9\ud569\ub2c8\ub2e4!"},
 

  /** Field ER_UNPARSEDENTITYURI_TAKES_1_ARG          */

 
  {
    ER_UNPARSEDENTITYURI_TAKES_1_ARG,
      "unparsed-entity-uri \ud568\uc218\ub294 \ud558\ub098\uc758 \uc778\uc790\ub9cc\uc744 \uc0ac\uc6a9\ud569\ub2c8\ub2e4!"},
 

  /** Field ER_NAMESPACEAXIS_NOT_IMPLEMENTED          */

 
  {
    ER_NAMESPACEAXIS_NOT_IMPLEMENTED,
      "\uc774\ub984 \uacf5\uac04 \ucd95\uc774 \uc544\uc9c1 \uad6c\ud604\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_UNKNOWN_AXIS          */

 
  {
    ER_UNKNOWN_AXIS, "{0}\uc740(\ub294) \uc54c \uc218 \uc5c6\ub294 \ucd95\uc785\ub2c8\ub2e4."},
 

  /** Field ER_UNKNOWN_MATCH_OPERATION          */

 
  {
    ER_UNKNOWN_MATCH_OPERATION, "\uc54c \uc218 \uc5c6\ub294 \uc77c\uce58 \uc5f0\uc0b0\uc785\ub2c8\ub2e4!"},
 

  /** Field ER_INCORRECT_ARG_LENGTH          */

 
  {
    ER_INCORRECT_ARG_LENGTH,
      "processing-instruction() \ub178\ub4dc \uac80\uc0ac\uc758 \uc778\uc790 \uae38\uc774\uac00 \uc62c\ubc14\ub974\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_CANT_CONVERT_TO_NUMBER          */

 
  {
    ER_CANT_CONVERT_TO_NUMBER,
      "{0}\uc744(\ub97c) \uc22b\uc790\ub85c \ubcc0\ud658\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_CANT_CONVERT_TO_NODELIST          */

 
  {
    ER_CANT_CONVERT_TO_NODELIST,
      "{0}\uc744(\ub97c) NodeList\ub85c \ubcc0\ud658\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_CANT_CONVERT_TO_MUTABLENODELIST          */

 
  {
    ER_CANT_CONVERT_TO_MUTABLENODELIST,
      "{0}\uc744(\ub97c) NodeSetDTM\uc73c\ub85c \ubcc0\ud658\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_CANT_CONVERT_TO_TYPE          */

 
  {
    ER_CANT_CONVERT_TO_TYPE,
 

  /** Field ER_EXPECTED_MATCH_PATTERN          */

 
  {
    ER_EXPECTED_MATCH_PATTERN,
      "getMatchScore\uc5d0 \uc77c\uce58 \ud328\ud134\uc774 \uc788\uc5b4\uc57c \ud569\ub2c8\ub2e4!"},
 

  /** Field ER_COULDNOT_GET_VAR_NAMED          */

 
  {
    ER_COULDNOT_GET_VAR_NAMED,
      "{0} \ubcc0\uc218\ub97c \uac00\uc838\uc62c \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. "},
 

  /** Field ER_UNKNOWN_OPCODE          */

 
  {
    ER_UNKNOWN_OPCODE, "\uc624\ub958! \uc54c \uc218 \uc5c6\ub294 \uc5f0\uc0b0 \ucf54\ub4dc: {0}"},
 

  /** Field ER_EXTRA_ILLEGAL_TOKENS          */

 
  {
    ER_EXTRA_ILLEGAL_TOKENS, "\uc798\ubabb\ub41c \ud1a0\ud070: {0}"},
 

  /** Field ER_EXPECTED_DOUBLE_QUOTE          */

 
  {
    ER_EXPECTED_DOUBLE_QUOTE,
      "\ub9ac\ud130\ub7f4\uc758 \uc778\uc6a9\ubd80\ud638\uac00 \uc798\ubabb\ub418\uc5c8\uc2b5\ub2c8\ub2e4... \ud070\ub530\uc634\ud45c\uac00 \ub098\uc640\uc57c \ud569\ub2c8\ub2e4!"},
 

  /** Field ER_EXPECTED_SINGLE_QUOTE          */

 
  {
    ER_EXPECTED_SINGLE_QUOTE,
      "\ub9ac\ud130\ub7f4\uc758 \uc778\uc6a9\ubd80\ud638\uac00 \uc798\ubabb\ub418\uc5c8\uc2b5\ub2c8\ub2e4... \ub2e8\uc77c \uc778\uc6a9\ubd80\ud638\uac00 \ub098\uc640\uc57c \ud569\ub2c8\ub2e4!"},
 

  /** Field ER_EMPTY_EXPRESSION          */

 
  {
    ER_EMPTY_EXPRESSION, "\ud45c\ud604\uc2dd\uc774 \ube44\uc5b4 \uc788\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_EXPECTED_BUT_FOUND          */

 
  {
    ER_EXPECTED_BUT_FOUND, "{0}\uc744(\ub97c) \uc608\uc0c1\ud588\uc9c0\ub9cc {1}\uc744(\ub97c) \ucc3e\uc558\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_INCORRECT_PROGRAMMER_ASSERTION          */

 
  {
    ER_INCORRECT_PROGRAMMER_ASSERTION,
      "\ud504\ub85c\uadf8\ub798\uba38 \uba85\uc81c\uac00 \uc62c\ubc14\ub974\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4! - {0}"},
 

  /** Field ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL          */

 
  {
    ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL,
      "\ubd80\uc6b8(...) \uc778\uc790\ub294 19990709 XPath \ub4dc\ub798\ud504\ud2b8\uc640 \ud568\uaed8 \ub354 \uc774\uc0c1 \uc120\ud0dd \uc778\uc790\uac00 \uc544\ub2d9\ub2c8\ub2e4."},
 

  /** Field ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG          */

 
  {
    ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG,
      "','\ub97c \ucc3e\uc558\uc73c\ub098 \uc120\ud589 \uc778\uc790\uac00 \uc544\ub2d9\ub2c8\ub2e4!"},
 

  /** Field ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG          */

 
  {
    ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
      "','\ub97c \ucc3e\uc558\uc73c\ub098 \ud6c4\ubbf8 \uc778\uc790\uac00 \uc544\ub2d9\ub2c8\ub2e4!"},
 

  /** Field ER_PREDICATE_ILLEGAL_SYNTAX          */

 
  {
    ER_PREDICATE_ILLEGAL_SYNTAX,
      "'..[predicate]' \ub610\ub294 '.[predicate]'\ub294 \uc798\ubabb\ub41c \uad6c\ubb38\uc785\ub2c8\ub2e4. \ub300\uc2e0 'self::node()[predicate]'\uc744 \uc0ac\uc6a9\ud558\uc2ed\uc2dc\uc624. "},
 

  /** Field ER_ILLEGAL_AXIS_NAME          */

 
  {
    ER_ILLEGAL_AXIS_NAME, "\uc798\ubabb\ub41c \ucd95 \uc774\ub984: {0}"},
 

  /** Field ER_UNKNOWN_NODETYPE          */

 
  {
    ER_UNKNOWN_NODETYPE, "\uc54c \uc218 \uc5c6\ub294 \ub178\ub4dc \uc720\ud615: {0}"},
 

  /** Field ER_PATTERN_LITERAL_NEEDS_BE_QUOTED          */

 
  {
    ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
      "\ud328\ud134 \ub9ac\ud130\ub7f4({0})\uc5d0 \uc778\uc6a9\ubd80\ud638\uac00 \uc788\uc5b4\uc57c \ud569\ub2c8\ub2e4!"},
 

  /** Field ER_COULDNOT_BE_FORMATTED_TO_NUMBER          */

 
  {
    ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      "{0}\uc744(\ub97c) \uc22b\uc790\ub85c \ud3ec\ub9f7\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_COULDNOT_CREATE_XMLPROCESSORLIAISON          */

 
  {
    ER_COULDNOT_CREATE_XMLPROCESSORLIAISON,
      "XML TransformerFactory Liaison {0}\uc744(\ub97c) \uc791\uc131\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_DIDNOT_FIND_XPATH_SELECT_EXP          */

 
  {
    ER_DIDNOT_FIND_XPATH_SELECT_EXP,
      "\uc624\ub958! xpath \uc120\ud0dd \ud45c\ud604\uc2dd(-select)\uc744 \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH          */

 
  {
    ER_COULDNOT_FIND_ENDOP_AFTER_OPLOCATIONPATH,
      "\uc624\ub958! OP_LOCATIONPATH \ub2e4\uc74c\uc5d0 ENDOP\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.   "},
 

  /** Field ER_ERROR_OCCURED          */

 
  {
    ER_ERROR_OCCURED, "\uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_ILLEGAL_VARIABLE_REFERENCE          */

 
  {
    ER_ILLEGAL_VARIABLE_REFERENCE,
      "VariableReference\uac00 \ucee8\ud14d\uc2a4\ud2b8\ub97c \ubc97\uc5b4\ub0ac\uac70\ub098 \uc815\uc758\ub418\uc9c0 \uc54a\uc740 \ubcc0\uc218\uc5d0 \uc9c0\uc815\ub418\uc5c8\uc2b5\ub2c8\ub2e4! \uc774\ub984 = {0}"},
 

  /** Field ER_AXES_NOT_ALLOWED          */

 
  {
    ER_AXES_NOT_ALLOWED,
      "\uc77c\uce58 \ud328\ud134\uc5d0\uc11c\ub294 \ud558\ub098\uc758 child:: \ubc0f attribute:: \ucd95\uc774 \ud5c8\uc6a9\ub429\ub2c8\ub2e4. \uc704\ubc18 \ucd95 = {0}"},
 

  /** Field ER_KEY_HAS_TOO_MANY_ARGS          */

 
  {
    ER_KEY_HAS_TOO_MANY_ARGS,
      "key()\uc758 \uc778\uc790 \uc218\uac00 \uc798\ubabb\ub418\uc5c8\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_COUNT_TAKES_1_ARG          */

 
  {
    ER_COUNT_TAKES_1_ARG,
      "\uce74\uc6b4\ud2b8 \ud568\uc218\ub294 \ud558\ub098\uc758 \uc778\uc790\ub9cc\uc744 \uc0ac\uc6a9\ud569\ub2c8\ub2e4!"},
 

  /** Field ER_COULDNOT_FIND_FUNCTION          */

 
  {
    ER_COULDNOT_FIND_FUNCTION, "\ud568\uc218 {0}\uc744(\ub97c) \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_UNSUPPORTED_ENCODING          */

 
  {
    ER_UNSUPPORTED_ENCODING, "\uc9c0\uc6d0\ub418\uc9c0 \uc54a\ub294 \ucf54\ub4dc\ud654: {0}"},
 

  /** Field ER_PROBLEM_IN_DTM_NEXTSIBLING          */

 
  {
    ER_PROBLEM_IN_DTM_NEXTSIBLING,
      "getNextSibling\uc758 DTM\uc5d0 \ubb38\uc81c\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4... \ubcf5\uad6c\ub97c \uc2dc\ub3c4 \uc911\uc785\ub2c8\ub2e4."},
 

  /** Field ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL          */

 
  {
    ER_CANNOT_WRITE_TO_EMPTYNODELISTIMPL,
      "\ud504\ub85c\uadf8\ub798\uba38 \uc624\ub958: EmptyNodeList\uc5d0\ub294 \uc4f8 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_SETDOMFACTORY_NOT_SUPPORTED          */

 
  {
    ER_SETDOMFACTORY_NOT_SUPPORTED,
      "setDOMFactory\ub294 XPathContext\uc5d0\uc11c \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_PREFIX_MUST_RESOLVE          */

 
  {
    ER_PREFIX_MUST_RESOLVE,
      "\uc811\ub450\uc5b4\uac00 \uc774\ub984 \uacf5\uac04 {0}\uc73c\ub85c(\ub85c) \uacb0\uc815\ub418\uc5b4\uc57c \ud569\ub2c8\ub2e4."},
 

  /** Field ER_PARSE_NOT_SUPPORTED          */

 
  {
    ER_PARSE_NOT_SUPPORTED,
      "\uad6c\ubb38 \ubd84\uc11d(InputSource \uc18c\uc2a4)\uc740 XPathContext\uc5d0\uc11c \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4! {0}\uc744(\ub97c) \uc5f4 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.  "},
 

  /** Field ER_SAX_API_NOT_HANDLED          */

 
  {
    ER_SAX_API_NOT_HANDLED,
      "SAX API \ubb38\uc790(char ch[]...\ub294 DTM\uc5d0 \uc758\ud574 \ucc98\ub9ac\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_IGNORABLE_WHITESPACE_NOT_HANDLED          */

 
  {
    ER_IGNORABLE_WHITESPACE_NOT_HANDLED,
      "ignorableWhitespace(char ch[]...\ub294 DTM\uc5d0 \uc758\ud574 \ucc98\ub9ac\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_DTM_CANNOT_HANDLE_NODES          */

 
  {
    ER_DTM_CANNOT_HANDLE_NODES,
      "DTMLiaison\uc740 {0} \uc720\ud615\uc758 \ub178\ub4dc\ub97c \ucc98\ub9ac\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. "},
 

  /** Field ER_XERCES_CANNOT_HANDLE_NODES          */

 
  {
    ER_XERCES_CANNOT_HANDLE_NODES,
      "DOM2Helper\ub294 {0} \uc720\ud615\uc758 \ub178\ub4dc\ub97c \ucc98\ub9ac\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.   "},
 

  /** Field ER_XERCES_PARSE_ERROR_DETAILS          */

 
  {
    ER_XERCES_PARSE_ERROR_DETAILS,
      "DOM2Helper.parse \uc624\ub958: SystemID - {0} \ud589 - {1}"},
 

  /** Field ER_XERCES_PARSE_ERROR          */

 
  {
    ER_XERCES_PARSE_ERROR, "DOM2Helper.parse \uc624\ub958"},
 

  /** Field ER_INVALID_UTF16_SURROGATE          */

 
  {
    ER_INVALID_UTF16_SURROGATE,
      "\uc798\ubabb\ub41c UTF-16 \ub300\ub9ac\uac00 \uac10\uc9c0\ub418\uc5c8\uc2b5\ub2c8\ub2e4: {0} ?"},
 

  /** Field ER_OIERROR          */

 
  {
    ER_OIERROR, "IO \uc624\ub958"},
 

  /** Field ER_CANNOT_CREATE_URL          */

 
  {
    ER_CANNOT_CREATE_URL, "{0}\uc5d0 \ub300\ud55c url\uc744 \uc791\uc131\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.     "},
 

  /** Field ER_XPATH_READOBJECT          */

 
  {
    ER_XPATH_READOBJECT, "XPath.readObject\uc5d0: {0}"},
 
  
  /** Field ER_XPATH_READOBJECT         */

 
  {
    ER_FUNCTION_TOKEN_NOT_FOUND,
      "\uae30\ub2a5 \ud1a0\ud070\uc744 \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 
  
   /**  Can not deal with XPath type:   */

 
  {
    ER_CANNOT_DEAL_XPATH_TYPE,
       "XPath \uc720\ud615\uc744 \ucc98\ub9ac\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4: {0}    "},
 
  
   /**  This NodeSet is not mutable  */

 
  {
    ER_NODESET_NOT_MUTABLE,
       "NodeSet\uc740 \ubcc0\uacbd\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},
 
  
   /**  This NodeSetDTM is not mutable  */

 
  {
    ER_NODESETDTM_NOT_MUTABLE,
       "NodeSetDTM\uc740 \ubcc0\uacbd\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},
 
  
   /**  Variable not resolvable:   */

 
  {
    ER_VAR_NOT_RESOLVABLE,
        "\ubcc0\uc218\ub97c \uacb0\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4: {0}"},
 
  
   /** Null error handler  */

 
  {
    ER_NULL_ERROR_HANDLER,
        "\uc624\ub958 \ucc98\ub9ac\uae30\uac00 \ub110\uc785\ub2c8\ub2e4"},
 
  
   /**  Programmer's assertion: unknown opcode  */

 
  {
    ER_PROG_ASSERT_UNKNOWN_OPCODE,
       "\ud504\ub85c\uadf8\ub798\uba38 \uba85\uc81c: \uc54c \uc218 \uc5c6\ub294 opcode: {0}"},
 
  
   /**  0 or 1   */

 
  {
    ER_ZERO_OR_ONE,
       "0 \ub610\ub294 1"},
 
  
 
   /**  rtf() not supported by XRTreeFragSelectWrapper   */

 
  {
    ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "XRTreeFragSelectWrapper\uac00 rtf()\ub97c \uc9c0\uc6d0\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4"},
 
  
   /**  asNodeIterator() not supported by XRTreeFragSelectWrapper   */

 
  {
    ER_ASNODEITERATOR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER,
       "XRTreeFragSelectWrapper\uac00 asNodeIterator()\ub97c \uc9c0\uc6d0\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4"},
 
  
   /**  fsb() not supported for XStringForChars   */

 
  {
    ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS,
       "fsb()\uac00 XStringForChars\uc5d0 \ub300\ud574 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4"},
 
  
   /**  Could not find variable with the name of   */

 
  {
    ER_COULD_NOT_FIND_VAR,
      "\uc774\ub984\uc774 {0}\uc778 \ubcc0\uc218\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.   "},
 
  
   /**  XStringForChars can not take a string for an argument   */

 
  {
    ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING,
      "XStringForChars\uac00 \uc778\uc790\uc5d0 \ub300\ud55c \ubb38\uc790\uc5f4\uc744 \uac00\uc838\uc62c \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 
  
   /**  The FastStringBuffer argument can not be null   */

 
  {
    ER_FASTSTRINGBUFFER_CANNOT_BE_NULL,
      "FastStringBuffer \uc778\uc790\ub294 \ub110\uc774 \ub420 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
    
  /* MANTIS_XALAN CHANGE: BEGIN */ 
   /**  2 or 3   */

 
  {
    ER_TWO_OR_THREE,
       "2 \ub610\ub294 3"},
 

   /** Variable accessed before it is bound! */

 
  {
    ER_VARIABLE_ACCESSED_BEFORE_BIND,
       "\ubcc0\uc218\uac00 \ubc14\uc6b4\ub529\ub418\uae30 \uc804\uc5d0 \uc561\uc138\uc2a4\ub418\uc5c8\uc2b5\ub2c8\ub2e4."},
 

   /** XStringForFSB can not take a string for an argument! */

 
  {
    ER_FSB_CANNOT_TAKE_STRING,
       "XStringForFSB\uac00 \uc778\uc790\uc5d0 \ub300\ud55c \ubb38\uc790\uc5f4\uc744 \uac00\uc838\uc62c \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

   /** Error! Setting the root of a walker to null! */

 
  {
    ER_SETTING_WALKER_ROOT_TO_NULL,
       "\n !!!! \uc624\ub958! walker\uc758 \ub8e8\ud2b8\ub97c \ub110\ub85c \uc124\uc815\ud588\uc2b5\ub2c8\ub2e4!!!"},
 

   /** This NodeSetDTM can not iterate to a previous node! */

 
  {
    ER_NODESETDTM_CANNOT_ITERATE,
       "\uc774 NodeSetDTM\uc774 \uc774\uc804 \ub178\ub4dc\ub85c \ubc18\ubcf5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** This NodeSet can not iterate to a previous node! */

 
  {
    ER_NODESET_CANNOT_ITERATE,
       "\uc774 NodeSet\uc774 \uc774\uc804 \ub178\ub4dc\ub85c \ubc18\ubcf5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** This NodeSetDTM can not do indexing or counting functions! */

 
  {
    ER_NODESETDTM_CANNOT_INDEX,
       "\uc774 NodeSetDTM\uc774 \uc778\ub371\uc2a4 \ub610\ub294 \uce74\uc6b4\ud305 \uae30\ub2a5\uc744 \uc218\ud589\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** This NodeSet can not do indexing or counting functions! */

 
  {
    ER_NODESET_CANNOT_INDEX,
       "\uc774 NodeSet\uc774 \uc778\ub371\uc2a4 \ub610\ub294 \uce74\uc6b4\ud305 \uae30\ub2a5\uc744 \uc218\ud589\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** Can not call setShouldCacheNodes after nextNode has been called! */

 
  {
    ER_CANNOT_CALL_SETSHOULDCACHENODE,
       "nextNode\uac00 \ud638\ucd9c\ub41c \ub2e4\uc74c\uc5d0 setShouldCacheNodeshas\ub97c \ud638\ucd9c\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** {0} only allows {1} arguments */

 
  {
    ER_ONLY_ALLOWS,
       "{0}\uc740(\ub294) {1} \uc778\uc790\ub9cc \ud5c8\uc6a9\ud569\ub2c8\ub2e4."},
 

  /** Programmer's assertion in getNextStepPos: unknown stepType: {0} */

 
  {
    ER_UNKNOWN_STEP,
       "getNextStepPos\uc5d0\uc11c \ud504\ub85c\uadf8\ub798\uba38 \uba85\uc81c: \uc54c \uc218 \uc5c6\ub294 stepType: {0}"},
 


  /** Problem with RelativeLocationPath */

 
  {
    ER_EXPECTED_REL_LOC_PATH,
 


  /** Problem with LocationPath */

 
  {
    ER_EXPECTED_LOC_PATH,
       "\uc0c1\ub300 \uc704\uce58 \uacbd\ub85c\uac00 \uc640\uc57c \ud558\uc9c0\ub9cc \ub300\uc2e0 \ub2e4\uc74c \ud1a0\ud070\uc774 \ubc1c\uacac\ub418\uc5c8\uc2b5\ub2c8\ub2e4.\u003a  {0}"},
 


  /** Problem with Step */

 
  {
    ER_EXPECTED_LOC_STEP,
 


  /** Problem with NodeTest */

 
  {
    ER_EXPECTED_NODE_TEST,
       "NCName:* \ub610\ub294 QName\uacfc \uc77c\uce58\ud558\ub294 \ub178\ub4dc \ud14c\uc2a4\ud2b8\uac00 \uc640\uc57c \ud569\ub2c8\ub2e4."},
 


  /** Expected step pattern */

 
  {
    ER_EXPECTED_STEP_PATTERN,
       "\ub2e8\uacc4 \ud328\ud134\uc774 \uc640\uc57c \ud558\uc9c0\ub9cc \ub300\uc2e0 '/'\ub97c \ubc1c\uacac\ud588\uc2b5\ub2c8\ub2e4."},
 

 
  /** Expected relative path pattern */

 
  {
    ER_EXPECTED_REL_PATH_PATTERN,
       "\uc0c1\ub300 \uacbd\ub85c \ud328\ud134\uc774 \uc640\uc57c \ud569\ub2c8\ub2e4."},
 


  /** Field ER_CANT_CONVERT_TO_BOOLEAN          */

 
  {
    ER_CANT_CONVERT_TO_BOOLEAN,
       "{0}\uc744(\ub97c) \ubd80\uc6b8\ub85c \ubcc0\ud658\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 


  /** Field ER_CANT_CONVERT_TO_SINGLENODE       */

 
  {
    ER_CANT_CONVERT_TO_SINGLENODE,
       "{0}\uc744(\ub97c) \ub2e8\uc77c \ub178\ub4dc\ub85c \ubcc0\ud658\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. \uc774 getter\ub294 ANY_UNORDERED_NODE_TYPE \uc720\ud615\uacfc FIRST_ORDERED_NODE_TYPE \uc720\ud615\uc5d0 \uc801\uc6a9\ub429\ub2c8\ub2e4."},
 


  /** Field ER_CANT_GET_SNAPSHOT_LENGTH         */

 
  {
    ER_CANT_GET_SNAPSHOT_LENGTH,
       "\uc720\ud615 {0}\uc5d0\uc11c \uc2a4\ub0c5\uc0f7 \uae38\uc774\ub97c \uac00\uc838\uc62c \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. \uc774 getter\ub294 UNORDERED_NODE_SNAPSHOT_TYPE \uc720\ud615\uacfc ORDERED_NODE_SNAPSHOT_TYPE \uc720\ud615\uc5d0 \uc801\uc6a9\ub429\ub2c8\ub2e4."},
 

  /** Field ER_NON_ITERATOR_TYPE                */

 
  {
    ER_NON_ITERATOR_TYPE,
       "\ube44\ubc18\ubcf5 \uc720\ud615 {0}\uc5d0 \ub300\ud574 \ubc18\ubcf5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 


  /** Field ER_DOC_MUTATED                      */

 
  {
    ER_DOC_MUTATED,
       "\uacb0\uacfc \uc774\ud6c4\ub85c \ubcc0\uacbd\ub41c \ubb38\uc11c\uac00 \ubc18\ud658\ub418\uc5c8\uc2b5\ub2c8\ub2e4. \ubc18\ubcf5\uc790\uac00 \uc798\ubabb\ub418\uc5c8\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_INVALID_XPATH_TYPE               */

 
  {
    ER_INVALID_XPATH_TYPE,
       "\uc798\ubabb\ub41c XPath \uc720\ud615 \uc778\uc790: {0}"},
 

  /** Field ER_EMPTY_XPATH_RESULT                */

 
  {
    ER_EMPTY_XPATH_RESULT,
       "\ube48 XPath \uacb0\uacfc \uac1d\uccb4"},
 

  /** Field ER_INCOMPATIBLE_TYPES                */

 
  {
    ER_INCOMPATIBLE_TYPES,
       "\ubc18\ud658\ub41c \uc720\ud615: {0}\uc744(\ub97c) \uc9c0\uc815\ud55c \uc720\ud615\uc73c\ub85c \uac15\uc81c \ubcc0\ud658\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4: {1}"},
 

  /** Field ER_NULL_RESOLVER                     */

 
  {
    ER_NULL_RESOLVER,
       "\ub110 \uc811\ub450\uc5b4 \ud574\uacb0\uc790\ub85c \uc811\ub450\uc5b4\ub97c \ud574\uacb0\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 


  /** Field ER_CANT_CONVERT_TO_STRING            */

 
  {
    ER_CANT_CONVERT_TO_STRING,
       "{0}\uc744(\ub97c) \ubb38\uc790\uc5f4\ub85c \ubcc0\ud658\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 


  /** Field ER_NON_SNAPSHOT_TYPE                 */

 
  {
    ER_NON_SNAPSHOT_TYPE,
       "\uc720\ud615 {0}\uc5d0\uc11c snapshotItem\uc744 \ud638\ucd9c\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. \uc774 \uba54\uc18c\ub4dc\ub294 UNORDERED_NODE_SNAPSHOT_TYPE \uc720\ud615\uacfc ORDERED_NODE_SNAPSHOT_TYPE \uc720\ud615\uc5d0 \uc801\uc6a9\ub429\ub2c8\ub2e4."},
 


  /** Field ER_WRONG_DOCUMENT                    */

 
  {
    ER_WRONG_DOCUMENT,
       "\ucee8\ud14d\uc2a4\ud2b8 \ub178\ub4dc\uac00 \uc774 XPathEvaluator\uc5d0 \ubc14\uc6b4\ub529\ub41c \ubb38\uc11c\uc5d0 \uc18d\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_WRONG_NODETYPE                    */

 
  {
    ER_WRONG_NODETYPE ,
       "\ucee8\ud14d\uc2a4\ud2b8 \ub178\ub4dc \uc720\ud615\uc774 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4."},
 

  /** Field ER_XPATH_ERROR                       */

 
  {
    ER_XPATH_ERROR ,
       "XPath\uc5d0 \uc54c \uc218 \uc5c6\ub294 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4."},
 
 


  /** Field WG_LOCALE_NAME_NOT_HANDLED          */

 
  {
    WG_LOCALE_NAME_NOT_HANDLED,
      "format-number \uae30\ub2a5\uc758 \ub85c\ucf00\uc77c \uc774\ub984\uc774 \uc544\uc9c1 \ucc98\ub9ac\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4."},
 

  /** Field WG_PROPERTY_NOT_SUPPORTED          */

 
  {
    WG_PROPERTY_NOT_SUPPORTED,
      "XSL \ud2b9\uc131\uc774 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4: {0}"},
 

  /** Field WG_DONT_DO_ANYTHING_WITH_NS          */

 
  {
    WG_DONT_DO_ANYTHING_WITH_NS,
      "\ud2b9\uc131 {1}\uc758 \uc774\ub984 \uacf5\uac04 {0}\uc5d0 \uc544\ubb34 \uac83\ub3c4 \uc218\ud589\ud558\uc9c0 \ub9c8\uc2ed\uc2dc\uc624."},
 

  /** Field WG_SECURITY_EXCEPTION          */

 
  {
    WG_SECURITY_EXCEPTION,
      "XSL \uc2dc\uc2a4\ud15c \ud2b9\uc131 {0}\uc5d0 \uc561\uc138\uc2a4\ud558\ub824\uace0 \ud560 \ub54c SecurityException\uc774 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4. "},
 

  /** Field WG_QUO_NO_LONGER_DEFINED          */

 
  {
    WG_QUO_NO_LONGER_DEFINED,
      "\uc774\uc804 \uad6c\ubb38: quo(...)\ub294 \ub354 \uc774\uc0c1 XPath\uc5d0\uc11c \uc815\uc758\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4."},
 

  /** Field WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST          */

 
  {
    WG_NEED_DERIVED_OBJECT_TO_IMPLEMENT_NODETEST,
      "XPath\ub294 nodeTest \uad6c\ud604\uc744 \uc704\ud574 \ud30c\uc0dd\ub41c \uac1d\uccb4\uac00 \ud544\uc694\ud569\ub2c8\ub2e4!"},
 

  /** Field WG_FUNCTION_TOKEN_NOT_FOUND          */

 
  {
    WG_FUNCTION_TOKEN_NOT_FOUND,
      "\uae30\ub2a5 \ud1a0\ud070\uc744 \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field WG_COULDNOT_FIND_FUNCTION          */

 
  {
    WG_COULDNOT_FIND_FUNCTION,
      "\ud568\uc218 {0}\uc744(\ub97c) \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field WG_CANNOT_MAKE_URL_FROM          */

 
  {
    WG_CANNOT_MAKE_URL_FROM,
      "{0}\uc5d0\uc11c URL\uc744 \uc791\uc131\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},
 

  /** Field WG_EXPAND_ENTITIES_NOT_SUPPORTED          */

 
  {
    WG_EXPAND_ENTITIES_NOT_SUPPORTED,
      "-E \uc635\uc158\uc740 DTM \uad6c\ubb38 \ubd84\uc11d\uae30\uc5d0 \ub300\ud574 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4."},
 

  /** Field WG_ILLEGAL_VARIABLE_REFERENCE          */

 
  {
    WG_ILLEGAL_VARIABLE_REFERENCE,
      "VariableReference\uac00 \ucee8\ud14d\uc2a4\ud2b8\ub97c \ubc97\uc5b4\ub0ac\uac70\ub098 \uc815\uc758\ub418\uc9c0 \uc54a\uc740 \ubcc0\uc218\uc5d0 \uc9c0\uc815\ub418\uc5c8\uc2b5\ub2c8\ub2e4! \uc774\ub984 = {0}"},
 

  /** Field WG_UNSUPPORTED_ENCODING          */

 
  {
    WG_UNSUPPORTED_ENCODING, "\uc9c0\uc6d0\ub418\uc9c0 \uc54a\ub294 \ucf54\ub4dc\ud654: {0}"},
 

 
  { "ui_language", "ko"},
  { "help_language", "ko"},
  { "language", "ko"},
    { "BAD_CODE",
      "createMessage\uc758 \ub9e4\uac1c\ubcc0\uc218\uac00 \ubc14\uc6b4\ub4dc\ub97c \ubc97\uc5b4\ub0ac\uc2b5\ub2c8\ub2e4."},
    { "FORMAT_FAILED",
      "messageFormat \ud638\ucd9c \uc2dc \uc608\uc678 \ubc1c\uc0dd"},
    { "version", ">>>>>>> Xalan \ubc84\uc804 "},
    { "version2", "<<<<<<<"},
    { "yes", "\uc608"},
    { "xsldone", "XSLProcessor: \uc644\ub8cc"},
    { "xpath_option", "xpath \uc635\uc158: "},
    { "optionIN", "   [-in inputXMLURL]"},
    { "optionSelect", "   [-select xpath expression]"},
    { "optionMatch", 
      "   [-match \uc77c\uce58 \ud328\ud134 (\uc77c\uce58 \uc9c4\ub2e8\uc5d0 \ub300\ud55c)]"},
    { "optionAnyExpr",
      "\ub610\ub294 xpath \ud45c\ud604\uc2dd\uc774 \uc9c4\ub2e8 \ub364\ud504\ub97c \uc218\ud589\ud569\ub2c8\ub2e4."},
    { "noParsermsg1",
    "XSL \ud504\ub85c\uc138\uc2a4\uac00 \uc131\uacf5\ud558\uc9c0 \ubabb\ud588\uc2b5\ub2c8\ub2e4."},
    { "noParsermsg2",
    "** \uad6c\ubb38 \ubd84\uc11d\uae30\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4 **"},
    { "noParsermsg3",
    "\ud074\ub798\uc2a4 \uacbd\ub85c\ub97c \ud655\uc778\ud558\uc2ed\uc2dc\uc624."},
    { "noParsermsg4",
      "Java\uc6a9 IBM XML \uad6c\ubb38 \ubd84\uc11d\uae30\uac00 \uc5c6\ub294 \uacbd\uc6b0 \ub2e4\uc74c\uc5d0\uc11c \ub2e4\uc6b4\ub85c\ub4dc\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4."},
    { "noParsermsg5",
  };


  /** Field BAD_CODE          */
  public static final String BAD_CODE = "BAD_CODE";

  /** Field FORMAT_FAILED          */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** Field ERROR_RESOURCES          */
  public static final String ERROR_RESOURCES =
    "org.apache.xpath.res.XPATHErrorResources";

  /** Field ERROR_STRING          */

  /** Field ERROR_HEADER          */
  public static final String ERROR_HEADER = "\uc624\ub958: ";

  /** Field WARNING_HEADER          */
  public static final String WARNING_HEADER = "\uacbd\uace0: ";

  /** Field XSL_HEADER          */
  public static final String XSL_HEADER = "XSL ";

  /** Field XML_HEADER          */
  public static final String XML_HEADER = "XML ";

  /** Field QUERY_HEADER          */
  public static final String QUERY_HEADER = "PATTERN ";

  /**
   * Get the association list.
   *
   * @return The association list.
   */
  public Object[][] getContents()
  {
    return contents;
  }
}

