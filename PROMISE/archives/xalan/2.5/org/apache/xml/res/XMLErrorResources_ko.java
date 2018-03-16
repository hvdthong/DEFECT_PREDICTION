package org.apache.xml.res;


/**
 * Set up error messages.
 * We build a two dimensional array of message keys and
 * message strings. In order to add a new message here,
 * you need to first add a String constant. And you need
 * to enter key, value pair as part of the contents
 * array. You also need to update MAX_CODE for error strings
 * and MAX_WARNING for warnings ( Needed for only information
 * purpose )
 */
public class XMLErrorResources_ko extends XMLErrorResources
{

  /** Maximum error messages, this is needed to keep track of the number of messages.    */
  public static final int MAX_CODE = 61;

  /** Maximum warnings, this is needed to keep track of the number of warnings.          */
  public static final int MAX_WARNING = 0;

  /** Maximum misc strings.   */
  public static final int MAX_OTHERS = 4;

  /** Maximum total warnings and error messages.          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;



  /** The lookup table for error messages.   */
  public static final Object[][] contents = {

  /** Error message ID that has a null message, but takes in a single object.    */
    {"ER0000" , "{0}" },


  /** ER_FUNCTION_NOT_SUPPORTED          */


  {
    ER_FUNCTION_NOT_SUPPORTED, "\uae30\ub2a5\uc774 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4!"},

  
  /** Can't overwrite cause         */


  {
    ER_CANNOT_OVERWRITE_CAUSE,
			"\uacb9\uccd0\uc4f8 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  No default implementation found */


  {
    ER_NO_DEFAULT_IMPL,
         "\uae30\ubcf8 \uad6c\ud604\uc744 \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  ChunkedIntArray({0}) not currently supported */


  {
    ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
       "ChunkedIntArray({0})\ub294 \ud604\uc7ac \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4."},

  
   /**  Offset bigger than slot */


  {
    ER_OFFSET_BIGGER_THAN_SLOT,
       "\uc624\ud504\uc14b\uc774 \uc2ac\ub86f\ubcf4\ub2e4 \ud07d\ub2c8\ub2e4"},

  
   /**  Coroutine not available, id= */


  {
    ER_COROUTINE_NOT_AVAIL,
       "Coroutine\uc740 \uc0ac\uc6a9\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4, ID={0}"},

  
   /**  CoroutineManager recieved co_exit() request */


  {
    ER_COROUTINE_CO_EXIT,
       "CoroutineManager\uac00 co_exit() \uc694\uccad\uc744 \uc218\uc2e0\ud588\uc2b5\ub2c8\ub2e4"},

  
   /**  co_joinCoroutineSet() failed */


  {
    ER_COJOINROUTINESET_FAILED,
       "co_joinCoroutineSet()\uc774 \uc2e4\ud328\ud588\uc2b5\ub2c8\ub2e4"},

  
   /**  Coroutine parameter error () */


  {
    ER_COROUTINE_PARAM,
       "Coroutine \ub9e4\uac1c\ubcc0\uc218 \uc624\ub958({0})"},

  
   /**  UNEXPECTED: Parser doTerminate answers  */


  {
    ER_PARSER_DOTERMINATE_ANSWERS,
       "\n\uc608\uc0c1\uce58 \ubabb\ud55c \ubb38\uc81c: doTerminate \uad6c\ubb38 \ubd84\uc11d\uae30\uac00 {0}\uc5d0 \uc751\ub2f5\ud588\uc2b5\ub2c8\ub2e4. "},

  
   /**  parse may not be called while parsing */


  {
    ER_NO_PARSE_CALL_WHILE_PARSING,
       "\uad6c\ubb38 \ubd84\uc11d\ud558\ub294 \ub3d9\uc548\uc5d0\ub294 \uad6c\ubb38 \ubd84\uc11d\uc744 \ud638\ucd9c\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  Error: typed iterator for axis  {0} not implemented  */


  {
    ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "\uc624\ub958: {0} \ucd95\uc5d0 \ub300\ud574 \uc785\ub825\ub41c \ubc18\ubcf5\uae30\uac00 \uad6c\ud604\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4."},

  
   /**  Error: iterator for axis {0} not implemented  */


  {
    ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
       "\uc624\ub958: {0} \ucd95\uc5d0 \ub300\ud55c \ubc18\ubcf5\uae30\uac00 \uad6c\ud604\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4. "},

  
   /**  Iterator clone not supported  */


  {
    ER_ITERATOR_CLONE_NOT_SUPPORTED,
       "\ubc18\ubcf5\uae30 \ubcf5\uc81c\uac00 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4"},

  
   /**  Unknown axis traversal type  */


  {
    ER_UNKNOWN_AXIS_TYPE,
       "\uc54c \uc218 \uc5c6\ub294 \ucd95 \uc21c\ud68c \uc720\ud615: {0}"},

  
   /**  Axis traverser not supported  */


  {
    ER_AXIS_NOT_SUPPORTED,
       "\ucd95 \uc21c\ud68c\uae30\uac00 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4: {0}"},

  
   /**  No more DTM IDs are available  */


  {
    ER_NO_DTMIDS_AVAIL,
       "\ub354 \uc774\uc0c1 DTM ID\ub97c \uc0ac\uc6a9\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."},

  
   /**  Not supported  */


  {
    ER_NOT_SUPPORTED,
       "\uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4: {0}"},

  
   /**  node must be non-null for getDTMHandleFromNode  */


  {
    ER_NODE_NON_NULL,
       "\ub178\ub4dc\ub294 getDTMHandleFromNode\uc5d0 \ub300\ud574 \ub110\uc774 \uc544\ub2c8\uc5b4\uc57c \ud569\ub2c8\ub2e4"},

  
   /**  Could not resolve the node to a handle  */


  {
    ER_COULD_NOT_RESOLVE_NODE,
       "\ub178\ub4dc\ub97c \ud578\ub4e4\ub85c \ubcc0\ud658\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  startParse may not be called while parsing */


  {
    ER_STARTPARSE_WHILE_PARSING,
       "startParse\ub294 \uad6c\ubb38 \ubd84\uc11d \uc911\uc5d0 \ud638\ucd9c\ub420 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  startParse needs a non-null SAXParser  */


  {
    ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse\uc5d0\ub294 \ub110\uc774 \uc544\ub2cc SAXParser\uac00 \ud544\uc694\ud569\ub2c8\ub2e4"},

  
   /**  could not initialize parser with */


  {
    ER_COULD_NOT_INIT_PARSER,
       "\ub2e4\uc74c\uc73c\ub85c \uad6c\ubb38 \ubd84\uc11d\uae30\ub97c \ucd08\uae30\ud654\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4: "},

  
   /**  exception creating new instance for pool  */


  {
    ER_EXCEPTION_CREATING_POOL,
       "\ud480\uc5d0 \ub300\ud55c \uc0c8 \uc778\uc2a4\ud134\uc2a4 \uc791\uc131 \uc911 \uc608\uc678\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4"},

  
   /**  Path contains invalid escape sequence  */


  {
    ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "\uacbd\ub85c\uc5d0 \uc798\ubabb\ub41c \uc81c\uc5b4 \ubb38\uc790\uc5f4\uc774 \ud3ec\ud568\ub418\uc5b4 \uc788\uc2b5\ub2c8\ub2e4"},

  
   /**  Scheme is required!  */


  {
    ER_SCHEME_REQUIRED,
       "\uccb4\uacc4\uac00 \ud544\uc694\ud569\ub2c8\ub2e4!"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_IN_URI,
       "URI\uc5d0  \uccb4\uacc4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4: {0}"},

  
   /**  No scheme found in URI  */


  {
    ER_NO_SCHEME_INURI,
       "URI\uc5d0 \uccb4\uacc4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  Path contains invalid character:   */


  {
    ER_PATH_INVALID_CHAR,
       "\uacbd\ub85c\uc5d0 \uc798\ubabb\ub41c \ubb38\uc790 {0}\uc774(\uac00) \ud3ec\ud568\ub418\uc5b4 \uc788\uc2b5\ub2c8\ub2e4."},

  
   /**  Cannot set scheme from null string  */


  {
    ER_SCHEME_FROM_NULL_STRING,
       "\ub110 \ubb38\uc790\uc5f4\uc5d0\uc11c \uccb4\uacc4\ub97c \uc124\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  The scheme is not conformant. */


  {
    ER_SCHEME_NOT_CONFORMANT,
       "\uccb4\uacc4\uac00 \uc77c\uce58\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4."},

  
   /**  Host is not a well formed address  */


  {
    ER_HOST_ADDRESS_NOT_WELLFORMED,
       "\ud638\uc2a4\ud2b8 \uc8fc\uc18c\uac00 \uc62c\ubc14\ub978 \ud615\uc2dd\uc774 \uc544\ub2d9\ub2c8\ub2e4"},

  
   /**  Port cannot be set when host is null  */


  {
    ER_PORT_WHEN_HOST_NULL,
       "\ud638\uc2a4\ud2b8\uac00 \ub110\uc774\uba74 \ud3ec\ud2b8\ub97c \uc124\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  Invalid port number  */


  {
    ER_INVALID_PORT,
       "\uc798\ubabb\ub41c \ud3ec\ud2b8 \ubc88\ud638\uc785\ub2c8\ub2e4"},

  
   /**  Fragment can only be set for a generic URI  */


  {
    ER_FRAG_FOR_GENERIC_URI,
       "\ub2e8\ud3b8\uc740 \uc77c\ubc18 URI\uc5d0 \ub300\ud574\uc11c\ub9cc \uc124\uc815\ub420 \uc218 \uc788\uc2b5\ub2c8\ub2e4"},

  
   /**  Fragment cannot be set when path is null  */


  {
    ER_FRAG_WHEN_PATH_NULL,
       "\uacbd\ub85c\uac00 \ub110\uc774\uba74 \ub2e8\ud3b8\uc744 \uc124\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /**  Fragment contains invalid character  */


  {
    ER_FRAG_INVALID_CHAR,
       "\ub2e8\ud3b8\uc5d0 \uc798\ubabb\ub41c \ubb38\uc790\uac00 \ud3ec\ud568\ub418\uc5b4 \uc788\uc2b5\ub2c8\ub2e4"},

  
 
  
   /** Parser is already in use  */


  {
    ER_PARSER_IN_USE,
        "\uad6c\ubb38 \ubd84\uc11d\uae30\uac00 \uc774\ubbf8 \uc0ac\uc6a9 \uc911\uc785\ub2c8\ub2e4"},

  
   /** Parser is already in use  */


  {
    ER_CANNOT_CHANGE_WHILE_PARSING,
        "\uad6c\ubb38 \ubd84\uc11d \uc911\uc5d0\ub294 {0} {1}\uc744(\ub97c) \ubcc0\uacbd\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /** Self-causation not permitted  */


  {
    ER_SELF_CAUSATION_NOT_PERMITTED,
        "\uc790\uccb4 \uc6d0\uc778 \uc81c\uacf5\uc740 \ud5c8\uc6a9\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4"},

  
   /** Userinfo may not be specified if host is not specified   */


  {
    ER_NO_USERINFO_IF_NO_HOST,
        "\ud638\uc2a4\ud2b8\uac00 \uc9c0\uc815\ub418\uc5b4 \uc788\uc9c0 \uc54a\uc73c\uba74 Userinfo\ub97c \uc9c0\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /** Port may not be specified if host is not specified   */


  {
    ER_NO_PORT_IF_NO_HOST,
        "\ud638\uc2a4\ud2b8\uac00 \uc9c0\uc815\ub418\uc5b4 \uc788\uc9c0 \uc54a\uc73c\uba74 \ud3ec\ud2b8\ub97c \uc9c0\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /** Query string cannot be specified in path and query string   */


  {
    ER_NO_QUERY_STRING_IN_PATH,
        "\uc9c8\uc758 \ubb38\uc790\uc5f4\uc744 \uacbd\ub85c \ub610\ub294 \uc9c8\uc758 \ubb38\uc790\uc5f4 \ub0b4\uc5d0 \uc9c0\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /** Fragment cannot be specified in both the path and fragment   */


  {
    ER_NO_FRAGMENT_STRING_IN_PATH,
        "\ub2e8\ud3b8\uc744 \uacbd\ub85c\uc640 \ub2e8\ud3b8 \ubaa8\ub450\uc5d0 \uc9c0\uc815\ud560 \uc218\ub294 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
   /** Cannot initialize URI with empty parameters   */


  {
    ER_CANNOT_INIT_URI_EMPTY_PARMS,
        "\ube48 \ub9e4\uac1c\ubcc0\uc218\ub85c\ub294 URI\ub97c \ucd08\uae30\ud654\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
  /**  Method not yet supported    */


  {
    ER_METHOD_NOT_SUPPORTED,
        "\uc9c0\uc6d0\ub418\uc9c0 \uc54a\ub294 \uba54\uc18c\ub4dc\uc785\ub2c8\ub2e4 "},


  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
     "IncrementalSAXSource_Filter\ub97c \ub2e4\uc2dc \uc2dc\uc791\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},

  
  /** IncrementalSAXSource_Filter not currently restartable   */


  {
    ER_XMLRDR_NOT_BEFORE_STARTPARSE,
     "startParse \uc694\uccad \uc804\uc5d0 XMLReader\ub97c \uc218\ud589\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4"},



  {
    ER_AXIS_TRAVERSER_NOT_SUPPORTED,
     "\ucd95 \ud2b8\ub798\ubc84\uc11c\uac00 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc74c: {0}"},



  {
    ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
     "ListingErrorHandler\uac00 \ub110 PrintWriter\ub85c \uc791\uc131\ub428!"},



  {
    ER_SYSTEMID_UNKNOWN,
     "\uc2dc\uc2a4\ud15c ID\ub97c \uc54c \uc218 \uc5c6\uc74c"},



  {
    ER_LOCATION_UNKNOWN,
     "\uc624\ub958 \uc704\uce58\ub97c \uc54c \uc218 \uc5c6\uc74c"},
 

  /** Field ER_PREFIX_MUST_RESOLVE          */

 
  {
    ER_PREFIX_MUST_RESOLVE,
      "\uc811\ub450\uc5b4\uac00 \uc774\ub984 \uacf5\uac04 {0}\uc73c\ub85c(\ub85c) \uacb0\uc815\ub418\uc5b4\uc57c \ud569\ub2c8\ub2e4."},
 

  /** Field ER_CREATEDOCUMENT_NOT_SUPPORTED          */

 
  {
    ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "createDocument()\ub294 XPathContext\uc5d0\uc11c \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT          */

 
  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "\uc790\uc2dd \uc18d\uc131\uc5d0 \uc18c\uc720\uc790 \ubb38\uc11c\uac00 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT          */

 
  {
    ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "\uc790\uc2dd \uc18d\uc131\uc5d0 \uc18c\uc720\uc790 \ubb38\uc11c \uc694\uc18c\uac00 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 

  /** Field ER_CANT_OUTPUT_TEXT_BEFORE_DOC          */

 
  {
    ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "\uacbd\uace0: \ubb38\uc11c \uc694\uc18c \uc804\uc5d0 \ud14d\uc2a4\ud2b8\ub97c \ucd9c\ub825\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4! \ubb34\uc2dc\ub429\ub2c8\ub2e4..."},
 

  /** Field ER_CANT_HAVE_MORE_THAN_ONE_ROOT          */

 
  {
    ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "DOM\uc5d0 \ub450 \uac1c \uc774\uc0c1\uc758 \ub8e8\ud2b8\ub97c \uac00\uc9c8 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4!"},
 
  
   /**  Argument 'localName' is null  */

 
  {
    ER_ARG_LOCALNAME_NULL,
       "'localName' \uc778\uc790\uac00 \ub110\uc785\ub2c8\ub2e4"},
 


  /** localname in QNAME should be a valid NCName */

 
  {
    ER_ARG_LOCALNAME_INVALID,
       "QNAME\uc758 \ub85c\uceec \uc774\ub984\uc740 \uc720\ud6a8\ud55c NCName\uc774\uc5b4\uc57c \ud569\ub2c8\ub2e4."},
 
  

  /** prefix in QNAME should be a valid NCName */

 
  {
    ER_ARG_PREFIX_INVALID,
       "QNAME\uc758 \uc811\ub450\uc5b4\ub294 \uc720\ud6a8\ud55c NCName\uc774\uc5b4\uc57c \ud569\ub2c8\ub2e4."},

  { "BAD_CODE",
      "createMessage\uc758 \ub9e4\uac1c\ubcc0\uc218\uac00 \ubc14\uc6b4\ub4dc\ub97c \ubc97\uc5b4\ub0ac\uc2b5\ub2c8\ub2e4."},
  { "FORMAT_FAILED",
      "messageFormat \ud638\ucd9c \uc2dc \uc608\uc678 \ubc1c\uc0dd"},
  { "line", "\ud589 #"},
  { "column", "\uc5f4 #"}
  
  };

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
