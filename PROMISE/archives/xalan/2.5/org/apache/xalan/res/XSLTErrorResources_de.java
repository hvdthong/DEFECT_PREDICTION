package org.apache.xalan.res;


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

public class XSLTErrorResources_de extends XSLTErrorResources
{

  /** Maximum error messages, this is needed to keep track of the number of messages.    */
  public static final int MAX_CODE = 201;

  /** Maximum warnings, this is needed to keep track of the number of warnings.          */
  public static final int MAX_WARNING = 29;

  /** Maximum misc strings.   */
  public static final int MAX_OTHERS = 55;

  /** Maximum total warnings and error messages.          */
  public static final int MAX_MESSAGES = MAX_CODE + MAX_WARNING + 1;

  /** The lookup table for error messages.   */

  /** The lookup table for error messages.   */
  public static final Object[][] contents = {

  /** Error message ID that has a null message, but takes in a single object.    */
    { "ERROR0000", "{0}"},

  /** ER_NO_CURLYBRACE          */

  {
    ER_NO_CURLYBRACE,
      "Fehler: '{' in Ausdruck nicht zul\u00e4ssig"},

  /** ER_ILLEGAL_ATTRIBUTE          */

  {
    ER_ILLEGAL_ATTRIBUTE,
	"{0} hat ein unzul\u00e4ssiges Attribut: {1}"},

  /** ER_NULL_SOURCENODE_APPLYIMPORTS          */

  {
    ER_NULL_SOURCENODE_APPLYIMPORTS,
      "sourceNode ist Null in xsl:apply-imports!"},

  /** ER_CANNOT_ADD          */

  {
    ER_CANNOT_ADD,
	"{0} kann {1} nicht hinzugef\u00fcgt werden"},

  /** ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES          */

  {
    ER_NULL_SOURCENODE_HANDLEAPPLYTEMPLATES,
      "sourceNode ist Null in handleApplyTemplatesInstruction!"},

  /** ER_NO_NAME_ATTRIB          */

  {
    ER_NO_NAME_ATTRIB,
	"{0} muss ein ''name''-Attribut haben."},

  /** ER_TEMPLATE_NOT_FOUND          */

  {
    ER_TEMPLATE_NOT_FOUND,
	"Vorlage konnte nicht gefunden werden: {0}"},

  /** ER_CANT_RESOLVE_NAME_AVT          */

  {
    ER_CANT_RESOLVE_NAME_AVT,
      "AVT-Name in xsl:call-template konnte nicht aufgel\u00f6st werden."},

  /** ER_REQUIRES_ATTRIB          */

  {
    ER_REQUIRES_ATTRIB,
	"{0} erfordert Attribut: {1}"},

  /** ER_MUST_HAVE_TEST_ATTRIB          */

  {
    ER_MUST_HAVE_TEST_ATTRIB,
      "{0} muss ein ''test''-Attribut haben."},

  /** ER_BAD_VAL_ON_LEVEL_ATTRIB          */

  {
    ER_BAD_VAL_ON_LEVEL_ATTRIB,
      "Ung\u00fcltiger Wert des ''level''-Attributs: {0}"},

  /** ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */

  {
    ER_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "processing-instruction-Name kann nicht 'xml' sein"},

  /** ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */

  {
    ER_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "processing-instruction-Name muss ein g\u00fcltiger NCName sein: {0}"},

  /** ER_NEED_MATCH_ATTRIB          */

  {
    ER_NEED_MATCH_ATTRIB,
      "{0} muss ein ''match''-Attribut haben, falls ein Modus angegeben ist."},

  /** ER_NEED_NAME_OR_MATCH_ATTRIB          */

  {
    ER_NEED_NAME_OR_MATCH_ATTRIB,
      "{0} erfordert entweder ein ''name''- oder ein ''match''-Attribut."},

  /** ER_CANT_RESOLVE_NSPREFIX          */

  {
    ER_CANT_RESOLVE_NSPREFIX,
      "Namensraum-Pr\u00e4fix kann nicht aufgel\u00f6st werden: {0}"},

  /** ER_ILLEGAL_VALUE          */

  {
    ER_ILLEGAL_VALUE,
	 "xml:space hat unzul\u00e4ssigen Wert: {0}"},

  /** ER_NO_OWNERDOC          */

  {
    ER_NO_OWNERDOC,
      "Tochterknoten hat kein Eigent\u00fcmer-Dokument!"},

  /** ER_ELEMTEMPLATEELEM_ERR          */

  {
    ER_ELEMTEMPLATEELEM_ERR,
	"ElemTemplateElement-Fehler: {0}"},

  /** ER_NULL_CHILD          */

  {
    ER_NULL_CHILD,
 	"Versuch, einen Null-Tochterknoten hinzuzuf\u00fcgen!"},

  /** ER_NEED_SELECT_ATTRIB          */

  {
    ER_NEED_SELECT_ATTRIB,
	"{0} erfordert ein ''select''-Attribut."},

  /** ER_NEED_TEST_ATTRIB          */

  {
    ER_NEED_TEST_ATTRIB,
      "xsl:when muss ein 'test'-Attribut haben."},

  /** ER_NEED_NAME_ATTRIB          */

  {
    ER_NEED_NAME_ATTRIB,
      "xsl:with-param muss ein 'name'-Attribut haben."},

  /** ER_NO_CONTEXT_OWNERDOC          */

  {
    ER_NO_CONTEXT_OWNERDOC,
      "Kontext hat kein Eigent\u00fcmer-Dokument!"},

  /** ER_COULD_NOT_CREATE_XML_PROC_LIAISON          */

  {
    ER_COULD_NOT_CREATE_XML_PROC_LIAISON,
      "XML TransformerFactory Liaison konnte nicht erstellt werden: {0}"},

  /** ER_PROCESS_NOT_SUCCESSFUL          */

  {
    ER_PROCESS_NOT_SUCCESSFUL,
      "Xalan: Prozess fehlgeschlagen."},

  /** ER_NOT_SUCCESSFUL          */

  {
    ER_NOT_SUCCESSFUL,
	"Xalan: fehlgeschlagen."},

  /** ER_ENCODING_NOT_SUPPORTED          */

  {
    ER_ENCODING_NOT_SUPPORTED,
	"Codierung nicht unterst\u00fctzt: {0}"},

  /** ER_COULD_NOT_CREATE_TRACELISTENER          */

  {
    ER_COULD_NOT_CREATE_TRACELISTENER,
      "TraceListener konnte nicht erstellt werden: {0}"},

  /** ER_KEY_REQUIRES_NAME_ATTRIB          */

  {
    ER_KEY_REQUIRES_NAME_ATTRIB,
      "xsl:key erfordert ein 'name'-Attribut!"},

  /** ER_KEY_REQUIRES_MATCH_ATTRIB          */

  {
    ER_KEY_REQUIRES_MATCH_ATTRIB,
      "xsl:key erfordert ein 'match'-Attribut!"},

  /** ER_KEY_REQUIRES_USE_ATTRIB          */

  {
    ER_KEY_REQUIRES_USE_ATTRIB,
      "xsl:key erfordert ein 'use'-Attribut!"},

  /** ER_REQUIRES_ELEMENTS_ATTRIB          */

  {
    ER_REQUIRES_ELEMENTS_ATTRIB,
      "(StylesheetHandler) {0} erfordert ein ''elements''-Attribut!"},

  /** ER_MISSING_PREFIX_ATTRIB          */

  {
    ER_MISSING_PREFIX_ATTRIB,
      "(StylesheetHandler) {0} ''prefix''-Attribut fehlt"},

  /** ER_BAD_STYLESHEET_URL          */

  {
    ER_BAD_STYLESHEET_URL,
	"Stylesheet-URL ung\u00fcltig: {0}"},

  /** ER_FILE_NOT_FOUND          */

  {
    ER_FILE_NOT_FOUND,
	"Stylesheet-Datei nicht gefunden: {0}"},

  /** ER_IOEXCEPTION          */

  {
    ER_IOEXCEPTION,
      "IO-Ausnahme bei Stylesheet-Datei: {0}"},

  /** ER_NO_HREF_ATTRIB          */

  {
    ER_NO_HREF_ATTRIB,
      "(StylesheetHandler) ''href''-Attribut f\u00fcr {0} nicht gefunden"},

  /** ER_STYLESHEET_INCLUDES_ITSELF          */

  {
    ER_STYLESHEET_INCLUDES_ITSELF,
      "(StylesheetHandler) {0} schlie\u00dft sich selbst direkt oder indirekt ein!"},

  /** ER_PROCESSINCLUDE_ERROR          */

  {
    ER_PROCESSINCLUDE_ERROR,
      "StylesheetHandler.processInclude-Fehler, {0}"},

  /** ER_MISSING_LANG_ATTRIB          */

  {
    ER_MISSING_LANG_ATTRIB,
      "(StylesheetHandler) {0} ''lang''-Attribut fehlt"},

  /** ER_MISSING_CONTAINER_ELEMENT_COMPONENT          */

  {
    ER_MISSING_CONTAINER_ELEMENT_COMPONENT,
      "(StylesheetHandler) Element {0} an falscher Position?? Containerelement ''component'' fehlt"},

  /** ER_CAN_ONLY_OUTPUT_TO_ELEMENT          */

  {
    ER_CAN_ONLY_OUTPUT_TO_ELEMENT,
      "Ausgabe nur m\u00f6glich in Element, DocumentFragment, Document oder PrintWriter."},

  /** ER_PROCESS_ERROR          */

  {
    ER_PROCESS_ERROR,
	"StylesheetRoot.process-Fehler"},

  /** ER_UNIMPLNODE_ERROR          */

  {
    ER_UNIMPLNODE_ERROR,
	"UnImplNode-Fehler: {0}"},

  /** ER_NO_SELECT_EXPRESSION          */

  {
    ER_NO_SELECT_EXPRESSION,
      "Fehler! 'select'-Ausdruck bei xpath nicht gefunden (-select)."},

  /** ER_CANNOT_SERIALIZE_XSLPROCESSOR          */

  {
    ER_CANNOT_SERIALIZE_XSLPROCESSOR,
      "Ein XSLProcessor kann nicht serialisiert werden!"},

  /** ER_NO_INPUT_STYLESHEET          */

  {
    ER_NO_INPUT_STYLESHEET,
      "Stylesheet-Eingabe nicht angegeben!"},

  /** ER_FAILED_PROCESS_STYLESHEET          */

  {
    ER_FAILED_PROCESS_STYLESHEET,
      "Stylesheet konnte nicht verarbeitet werden!"},

  /** ER_COULDNT_PARSE_DOC          */

  {
    ER_COULDNT_PARSE_DOC,
	"Dokument {0} konnte nicht geparst werden!"},

  /** ER_COULDNT_FIND_FRAGMENT          */

  {
    ER_COULDNT_FIND_FRAGMENT,
	"Fragment nicht gefunden: {0}"},

  /** ER_NODE_NOT_ELEMENT          */

  {
    ER_NODE_NOT_ELEMENT,
      "Knoten, auf den von einem Fragmentbezeichner gezeigt wird, war kein Element: {0}"},

  /** ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB          */

  {
    ER_FOREACH_NEED_MATCH_OR_NAME_ATTRIB,
      "for-each muss ein 'match'- oder 'name'-Attribut haben"},

  /** ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB          */

  {
    ER_TEMPLATES_NEED_MATCH_OR_NAME_ATTRIB,
      "Vorlagen m\u00fcssen ein 'match'- oder 'name'-Attribut haben"},

  /** ER_NO_CLONE_OF_DOCUMENT_FRAG          */

  {
    ER_NO_CLONE_OF_DOCUMENT_FRAG,
      "Kein Klone eines Dokument-Fragments!"}, 

  /** ER_CANT_CREATE_ITEM          */

  {
    ER_CANT_CREATE_ITEM,
      "Element im Ergebnisbaum kann nicht erstellt werden: {0}"},

  /** ER_XMLSPACE_ILLEGAL_VALUE          */

  {
    ER_XMLSPACE_ILLEGAL_VALUE,
      "xml:space in der Quell-XML hat einen ung\u00fcltigen Wert: {0}"},

  /** ER_NO_XSLKEY_DECLARATION          */

  {
    ER_NO_XSLKEY_DECLARATION,
      "Keine xsl:key-Vereinbarung f\u00fcr {0} vorhanden!"},

  /** ER_CANT_CREATE_URL          */

  {
    ER_CANT_CREATE_URL,
	"Fehler! URL kann nicht erstellt werden f\u00fcr: {0}"},

  /** ER_XSLFUNCTIONS_UNSUPPORTED          */

  {
    ER_XSLFUNCTIONS_UNSUPPORTED,
	 "xsl:functions nicht unterst\u00fctzt"},

  /** ER_PROCESSOR_ERROR          */

  {
    ER_PROCESSOR_ERROR, "XSLT TransformerFactory-Fehler"},

  /** ER_NOT_ALLOWED_INSIDE_STYLESHEET          */

  {
    ER_NOT_ALLOWED_INSIDE_STYLESHEET,
      "(StylesheetHandler) {0} in einem Stylesheet nicht zul\u00e4ssig!"},

  /** ER_RESULTNS_NOT_SUPPORTED          */

  {
    ER_RESULTNS_NOT_SUPPORTED,
      "result-ns nicht mehr unterst\u00fctzt! Verwenden Sie statt dessen xsl:output."},

  /** ER_DEFAULTSPACE_NOT_SUPPORTED          */

  {
    ER_DEFAULTSPACE_NOT_SUPPORTED,
      "default-space nicht mehr unterst\u00fctzt! Verwenden Sie statt dessen xsl:strip-space oder xsl:preserve-space."},

  /** ER_INDENTRESULT_NOT_SUPPORTED          */

  {
    ER_INDENTRESULT_NOT_SUPPORTED,
      "indent-result nicht mehr unterst\u00fctzt! Verwenden Sie statt dessen xsl:output."},

  /** ER_ILLEGAL_ATTRIB          */

  {
    ER_ILLEGAL_ATTRIB,
      "(StylesheetHandler) {0} hat ein ung\u00fcltiges Attribut: {1}"},

  /** ER_UNKNOWN_XSL_ELEM          */

  {
    ER_UNKNOWN_XSL_ELEM, "Ungekanntes XSL-Element: {0}"},

  /** ER_BAD_XSLSORT_USE          */

  {
    ER_BAD_XSLSORT_USE,
      "(StylesheetHandler) xsl:sort kann nur mit xsl:apply-templates oder xsl:for-each verwendet werden."},

  /** ER_MISPLACED_XSLWHEN          */

  {
    ER_MISPLACED_XSLWHEN,
      "(StylesheetHandler) xsl:when an falscher Position!"},

  /** ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE          */

  {
    ER_XSLWHEN_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:when ohne \u00fcbergeordnetes xsl:choose!"},

  /** ER_MISPLACED_XSLOTHERWISE          */

  {
    ER_MISPLACED_XSLOTHERWISE,
      "(StylesheetHandler) xsl:otherwise an falscher Position!"},

  /** ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE          */

  {
    ER_XSLOTHERWISE_NOT_PARENTED_BY_XSLCHOOSE,
      "(StylesheetHandler) xsl:otherwise ohne \u00fcbergeordnetes xsl:choose!"},

  /** ER_NOT_ALLOWED_INSIDE_TEMPLATE          */

  {
    ER_NOT_ALLOWED_INSIDE_TEMPLATE,
      "(StylesheetHandler) {0} in einer Vorlage nicht zul\u00e4ssig!"},

  /** ER_UNKNOWN_EXT_NS_PREFIX          */

  {
    ER_UNKNOWN_EXT_NS_PREFIX,
      "(StylesheetHandler) Namensraum-Pr\u00e4fix {1} der Dateierweiterung {0} unbekannt"},

  /** ER_IMPORTS_AS_FIRST_ELEM          */

  {
    ER_IMPORTS_AS_FIRST_ELEM,
      "(StylesheetHandler) Importe nur als erste Elemente im Stylesheet m\u00f6glich!"},

  /** ER_IMPORTING_ITSELF          */

  {
    ER_IMPORTING_ITSELF,
      "(StylesheetHandler) {0} schlie\u00dft sich selbst direkt oder indirekt ein!"},

  /** ER_XMLSPACE_ILLEGAL_VAL          */

  {
    ER_XMLSPACE_ILLEGAL_VAL,
      "(StylesheetHandler) " + "xml:space hat ung\u00fcltigen Wert: {0}"},

  /** ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL          */

  {
    ER_PROCESSSTYLESHEET_NOT_SUCCESSFUL,
      "processStylesheet fehlgeschlagen!"},

  /** ER_SAX_EXCEPTION          */

  {
    ER_SAX_EXCEPTION, "SAX-Ausnahme"},

  /** ER_XSLT_ERROR          */

  {
    ER_XSLT_ERROR, "XSLT-Fehler"},

  /** ER_CURRENCY_SIGN_ILLEGAL          */

  {
    ER_CURRENCY_SIGN_ILLEGAL,
      "W\u00e4hrungszeichen in Formatierungsmuster nicht zul\u00e4ssig"},

  /** ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM          */

  {
    ER_DOCUMENT_FUNCTION_INVALID_IN_STYLESHEET_DOM,
      "document-Funktion in Stylesheet-DOM nicht unterst\u00fctzt!"},

  /** ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER          */

  {
    ER_CANT_RESOLVE_PREFIX_OF_NON_PREFIX_RESOLVER,
      "Pr\u00e4fix von Nicht-Pr\u00e4fix-Resolver kann nicht aufgel\u00f6st werden!"},

  /** ER_REDIRECT_COULDNT_GET_FILENAME          */

  {
    ER_REDIRECT_COULDNT_GET_FILENAME,
      "Redirect-Erweiterung: Dateiname konnte nicht ermittelt werden - 'file'- oder 'select'-Attribut muss g\u00fcltige Zeichenkette zur\u00fcckgeben."},

  /** ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT          */

  {
    ER_CANNOT_BUILD_FORMATTERLISTENER_IN_REDIRECT,
      "FormatterListener kann in Redirect-Erweiterung nicht aufgebaut werden!"},

  /** ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX          */

  {
    ER_INVALID_PREFIX_IN_EXCLUDERESULTPREFIX,
      "Pr\u00e4fix in exclude-result-prefixes ist ung\u00fcltig: {0}"},

  /** ER_MISSING_NS_URI          */

  {
    ER_MISSING_NS_URI,
      "Namensraum-URI f\u00fcr angegebenes Pr\u00e4fix fehlt"},

  /** ER_MISSING_ARG_FOR_OPTION          */

  {
    ER_MISSING_ARG_FOR_OPTION,
      "Argument f\u00fcr Option fehlt: {0}"},

  /** ER_INVALID_OPTION          */

  {
    ER_INVALID_OPTION, "Ung\u00fcltige Option: {0}"},

  /** ER_MALFORMED_FORMAT_STRING          */

  {
    ER_MALFORMED_FORMAT_STRING, "Ung\u00fcltige Formatierungszeichenkette: {0}"},

  /** ER_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

  {
    ER_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet erfordert ein 'version'-Attribut!"},

  /** ER_ILLEGAL_ATTRIBUTE_VALUE          */

  {
    ER_ILLEGAL_ATTRIBUTE_VALUE,
      "Attribut: {0} hat einen ung\u00fcltigen Wert: {1}"},

  /** ER_CHOOSE_REQUIRES_WHEN          */

  {
    ER_CHOOSE_REQUIRES_WHEN, "xsl:choose erfordert xsl:when"},

  /** ER_NO_APPLY_IMPORT_IN_FOR_EACH          */

  {
    ER_NO_APPLY_IMPORT_IN_FOR_EACH,
      "xsl:apply-imports nicht zul\u00e4ssig in xsl:for-each"},

  /** ER_CANT_USE_DTM_FOR_OUTPUT          */

  {
    ER_CANT_USE_DTM_FOR_OUTPUT,
      "Objekt der Klasse DTMLiaison kann f\u00fcr einen Ausgabe-DOM-Knoten nicht verwendet werden... \u00dcbergeben Sie statt dessen ein Objekt der Klasse org.apache.xpath.DOM2Helper!"},

  /** ER_CANT_USE_DTM_FOR_INPUT          */

  {
    ER_CANT_USE_DTM_FOR_INPUT,
      "Objekt der Klasse DTMLiaison kann f\u00fcr einen Eingabe-DOM-Knoten nicht verwendet werden... \u00dcbergeben Sie statt dessen ein Objekt der Klasse org.apache.xpath.DOM2Helper!"},

  /** ER_CALL_TO_EXT_FAILED          */

  {
    ER_CALL_TO_EXT_FAILED,
      "Aufruf des Erweiterungselements fehlgeschlagen: {0}"},

  /** ER_PREFIX_MUST_RESOLVE          */

  {
    ER_PREFIX_MUST_RESOLVE,
      "Pr\u00e4fix muss sich in einen Namensraum aufl\u00f6sen: {0}"},

  /** ER_INVALID_UTF16_SURROGATE          */

  {
    ER_INVALID_UTF16_SURROGATE,
      "Ung\u00fcltiges UTF-16-Surrogat erkannt: {0} ?"},

  /** ER_XSLATTRSET_USED_ITSELF          */

  {
    ER_XSLATTRSET_USED_ITSELF,
      "xsl:attribute-set {0} verwendet sich selbst, was zu einer Endlosschleife f\u00fchrt."},

  /** ER_CANNOT_MIX_XERCESDOM          */

  {
    ER_CANNOT_MIX_XERCESDOM,
      "Eingabe, die nicht Xerces-DOM entspricht, kann nicht mit Xerces-DOM-Ausgabe gemischt werden!"},

  /** ER_TOO_MANY_LISTENERS          */

  {
    ER_TOO_MANY_LISTENERS,
      "addTraceListenersToStylesheet - TooManyListenersException"},

  /** ER_IN_ELEMTEMPLATEELEM_READOBJECT          */

  {
    ER_IN_ELEMTEMPLATEELEM_READOBJECT,
      "In ElemTemplateElement.readObject: {0}"},

  /** ER_DUPLICATE_NAMED_TEMPLATE          */

  {
    ER_DUPLICATE_NAMED_TEMPLATE,
      "Mehr als eine Vorlage gefunden, Name: {0}"},

  /** ER_INVALID_KEY_CALL          */

  {
    ER_INVALID_KEY_CALL,
      "Ung\u00fcltiger Funktionsaufruf: Aufrufe mit rekursivem Schl\u00fcssel () nicht zul\u00e4ssig"},
  
  /** Variable is referencing itself          */

  {
    ER_REFERENCING_ITSELF,
      "Variable {0} bezieht sich direkt oder indirekt auf sich selbst!"},
  
  /** Illegal DOMSource input          */

  {
    ER_ILLEGAL_DOMSOURCE_INPUT,
      "Der Eingabeknoten darf f\u00fcr ein DOMSource f\u00fcr newTemplates nicht Null sein!"},
	
	/** Class not found for option         */

  {
    ER_CLASS_NOT_FOUND_FOR_OPTION,
			"Klassendatei nicht gefunden f\u00fcr Option {0}"},
	
	/** Required Element not found         */

  {
    ER_REQUIRED_ELEM_NOT_FOUND,
			"Erforderliches Element nicht gefunden: {0}"},
  
  /** InputStream cannot be null         */

  {
    ER_INPUT_CANNOT_BE_NULL,
			"InputStream kann nicht Null sein"},
  
  /** URI cannot be null         */

  {
    ER_URI_CANNOT_BE_NULL,
			"URI kann nicht Null sein"},
  
  /** File cannot be null         */

  {
    ER_FILE_CANNOT_BE_NULL,
			"Datei kann nicht Null sein"},
  
   /** InputSource cannot be null         */

  {
    ER_SOURCE_CANNOT_BE_NULL,
			"InputSource kann nicht Null sein"},
  
  /** Could not initialize BSF Manager        */

  {
    ER_CANNOT_INIT_BSFMGR,
			"BSF Manager konnte nicht initialisiert werden"},
  
  /** Could not compile extension       */

  {
    ER_CANNOT_CMPL_EXTENSN,
			"Erweiterung kann nicht kompiliert werden"},
  
  /** Could not create extension       */

  {
    ER_CANNOT_CREATE_EXTENSN,
      "Erweiterung kann nicht erstellt werde: {0} Grund: {1}"},
  
  /** Instance method call to method {0} requires an Object instance as first argument       */

  {
    ER_INSTANCE_MTHD_CALL_REQUIRES,
      "Instanzenmethoden-Aufruf von Methode {0} erfordert eine Object-Instanz als erstes Argument"},
  
  /** Invalid element name specified       */

  {
    ER_INVALID_ELEMENT_NAME,
      "Ung\u00fcltiger Elementname angegeben {0}"},
  
   /** Element name method must be static      */

  {
    ER_ELEMENT_NAME_METHOD_STATIC,
      "Elementnamen-Methode muss statisch sein {0}"},
  
   /** Extension function {0} : {1} is unknown      */

  {
    ER_EXTENSION_FUNC_UNKNOWN,
             "Erweiterungsfunktion {0} : {1} ist unbekannt"},
  
   /** More than one best match for constructor for       */

  {
    ER_MORE_MATCH_CONSTRUCTOR,
             "Mehr als ein Best-Match f\u00fcr Konstruktor f\u00fcr {0}"},
  
   /** More than one best match for method      */

  {
    ER_MORE_MATCH_METHOD,
             "Mehr als ein Best-Match f\u00fcr Methode {0}"},
  
   /** More than one best match for element method      */

  {
    ER_MORE_MATCH_ELEMENT,
             "Mehr als ein Best-Match f\u00fcr Elementmethode {0}"},
  
   /** Invalid context passed to evaluate       */

  {
    ER_INVALID_CONTEXT_PASSED,
             "Ung\u00fcltiger Kontext zur Auswertung von {0} \u00fcbergeben"},
  
   /** Pool already exists       */

  {
    ER_POOL_EXISTS,
             "Pool besteht bereits"},
  
   /** No driver Name specified      */

  {
    ER_NO_DRIVER_NAME,
             "Kein Treibername angegeben"},
  
   /** No URL specified     */

  {
    ER_NO_URL,
             "Kein URL angegeben"},
  
   /** Pool size is less than one    */

  {
    ER_POOL_SIZE_LESSTHAN_ONE,
             "Pool-Gr\u00f6\u00dfe kleiner als Eins!"},
  
   /** Invalid driver name specified    */

  {
    ER_INVALID_DRIVER,
             "Ung\u00fcltiger Treibername angegeben!"},
  
   /** Did not find the stylesheet root    */

  {
    ER_NO_STYLESHEETROOT,
             "Stylesheet-Stamm nicht gefunden!"},
  
   /** Illegal value for xml:space     */

  {
    ER_ILLEGAL_XMLSPACE_VALUE,
         "Ung\u00fcltiger Wert f\u00fcr xml:space"},
  
   /** processFromNode failed     */

  {
    ER_PROCESSFROMNODE_FAILED,
         "processFromNode fehlgeschlagen"},
  
   /** The resource [] could not load:     */

  {
    ER_RESOURCE_COULD_NOT_LOAD,
        "Die Ressource [ {0} ] konnte nicht laden: {1} \n {2} \t {3}"},
   
  
   /** Buffer size <=0     */

  {
    ER_BUFFER_SIZE_LESSTHAN_ZERO,
        "Puffergr\u00f6\u00dfe <=0"},
  
   /** Unknown error when calling extension    */

  {
    ER_UNKNOWN_ERROR_CALLING_EXTENSION,
        "Unbekannter Fehler beim Aufruf der Erweiterung"},
  
   /** Prefix {0} does not have a corresponding namespace declaration    */

  {
    ER_NO_NAMESPACE_DECL,
        "Pr\u00e4fix {0} hat keine entsprechende Namensraum-Vereinbarung"},
  
   /** Element content not allowed for lang=javaclass   */

  {
    ER_ELEM_CONTENT_NOT_ALLOWED,
        "Elementinhalt nicht zul\u00e4ssig f\u00fcr lang=javaclass {0}"},
  
   /** Stylesheet directed termination   */

  {
    ER_STYLESHEET_DIRECTED_TERMINATION,
        "Stylesheet f\u00fchrte zu Beendigung"},
  
   /** 1 or 2   */

  {
    ER_ONE_OR_TWO,
        "1 oder 2"},
  
   /** 2 or 3   */

  {
    ER_TWO_OR_THREE,
        "2 oder 3"},

   /** Could not load {0} (check CLASSPATH), now using just the defaults   */

  {
    ER_COULD_NOT_LOAD_RESOURCE,
        "{0} konnte nicht geladen werden (\u00fcberpr\u00fcfen Sie CLASSPATH); jetzt werden die Standardwerte verwendet"},
  
   /** Cannot initialize default templates   */

  {
    ER_CANNOT_INIT_DEFAULT_TEMPLATES,
        "Standardvorlagen k\u00f6nnen nicht initialisiert werden"},
  
   /** Result should not be null   */

  {
    ER_RESULT_NULL,
        "Ergebnis sollte nicht Null sein"},
    
   /** Result could not be set   */

  {
    ER_RESULT_COULD_NOT_BE_SET,
        "Ergebnis konnte nicht festgelegt werden"},
  
   /** No output specified   */

  {
    ER_NO_OUTPUT_SPECIFIED,
        "Keine Ausgabe festgelegt"},
  
   /** Can't transform to a Result of type   */

  {
    ER_CANNOT_TRANSFORM_TO_RESULT_TYPE,
        "Transformation in ein Ergebnis vom Typ {0} nicht m\u00f6glich"},
  
   /** Can't transform to a Source of type   */

  {
    ER_CANNOT_TRANSFORM_SOURCE_TYPE,
        "Transformation einer Quelle vom Typ {0} nicht m\u00f6glich"},
  
   /** Null content handler  */

  {
    ER_NULL_CONTENT_HANDLER,
        "Kein Content-Handler"},
  
   /** Null error handler  */

  {
    ER_NULL_ERROR_HANDLER,
        "Kein Error-Handler"},
  
   /** parse can not be called if the ContentHandler has not been set */

  {
    ER_CANNOT_CALL_PARSE,
        "parse kann nicht aufgerufen werden, wenn der ContentHandler nicht festgelegt wurde"},
  
   /**  No parent for filter */

  {
    ER_NO_PARENT_FOR_FILTER,
        "Kein \u00fcbergeordneter Knoten f\u00fcr Filter"},
  
  
   /**  No stylesheet found in: {0}, media */

  {
    ER_NO_STYLESHEET_IN_MEDIA,
         "Kein Stylesheet gefunden in: {0}, media= {1}"},
  
   /**  No xml-stylesheet PI found in */

  {
    ER_NO_STYLESHEET_PI,
         "Kein xml-Stylesheet PI gefunden in: {0}"},
  
   /**  Not supported  */

  {
    ER_NOT_SUPPORTED,
       "Nicht unterst\u00fctzt: {0}"},
    
   /**  Value for property {0} should be a Boolean instance  */

  {
    ER_PROPERTY_VALUE_BOOLEAN,
       "Wert f\u00fcr Eigenschaft {0} sollte eine Boolesche Instanz sein"},
  
   /* This key/message changed ,NEED ER_COULD_NOT_FIND_EXTERN_SCRIPT: Pending,Ramesh */
   /** src attribute not yet supported for  */

  {
    "ER_SRC_ATTRIB_NOT_SUPPORTED",
       "''src''-Attribut f\u00fcr {0} noch nicht unterst\u00fctzt"},
  
  /** The resource [] could not be found     */

  {
    ER_RESOURCE_COULD_NOT_FIND,
        "Die Ressource [ {0} ] wurde nicht gefunden.\n {1}"},
  
   /** output property not recognized:  */

  {
    ER_OUTPUT_PROPERTY_NOT_RECOGNIZED,
        "Ausgabe-Eigenschaft nicht erkannt: {0}"},
    
   /** Failed creating ElemLiteralResult instance   */

  {
    ER_FAILED_CREATING_ELEMLITRSLT,
        "Erstellen von ElemLiteralResult-Instanz fehlgeschlagen"},
  

   /** Priority value does not contain a parsable number   */

  {
    ER_VALUE_SHOULD_BE_NUMBER,
        "Der Wert f\u00fcr {0} muss eine Nummer darstellen, die geparst werden kann."},
  
   /**  Value for {0} should equal 'yes' or 'no'   */

  {
    ER_VALUE_SHOULD_EQUAL,
        " Wert f\u00fcr {0} sollte Ja oder Nein sein"},
 
   /**  Failed calling {0} method   */

  {
    ER_FAILED_CALLING_METHOD,
        " Aufruf der Methode {0} fehlgeschlagen"},
  
   /** Failed creating ElemLiteralResult instance   */

  {
    ER_FAILED_CREATING_ELEMTMPL,
        "Erstellen von ElemTemplateElement-Instanz fehlgeschlagen"},
  
   /**  Characters are not allowed at this point in the document   */

  {
    ER_CHARS_NOT_ALLOWED,
        "Zeichen an dieser Stelle im Dokument nicht erlaubt"},
  
  /**  attribute is not allowed on the element   */

  {
    ER_ATTR_NOT_ALLOWED,
        "\"{0}\" Attribut ist nicht erlaubt f\u00fcr Element {1}!"},
  
  /**  Bad value    */

  {
    ER_BAD_VALUE,
     "{0} ung\u00fcltiger Wert {1} "},
  
  /**  attribute value not found   */

  {
    ER_ATTRIB_VALUE_NOT_FOUND,
     "{0} Attributwert nicht gefunden"},
  
  /**  attribute value not recognized    */

  {
    ER_ATTRIB_VALUE_NOT_RECOGNIZED,
     "{0} Attributwert nicht erkannt "},

  /** Attempting to generate a namespace prefix with a null URI   */

  {
    ER_NULL_URI_NAMESPACE,
     "Es wurde versucht, einen Namensraum-Pr\u00e4fix ohne URI zu erzeugen."},


  /** Attempting to generate a namespace prefix with a null URI   */

  {
    ER_NUMBER_TOO_BIG,
     "Es wurde versucht, eine Zahl gr\u00f6\u00dfer als die gr\u00f6\u00dfte lange Ganzzahl zu formatieren."},



  {
    ER_CANNOT_FIND_SAX1_DRIVER,
     "Die SAX1-Treiberklasse {0} kann nicht gefunden werden."},


  {
    ER_SAX1_DRIVER_NOT_LOADED,
     "Die SAX1-Treiberklasse {0} wurde gefunden, kann aber nicht geladen werden."},


  {
    ER_SAX1_DRIVER_NOT_INSTANTIATED,
     "Die SAX1-Treiberklasse {0} wurde geladen, es kann aber keine Instanz gebildet werden."},



  {
    ER_SAX1_DRIVER_NOT_IMPLEMENT_PARSER,
     "Die SAX1-Treiberklasse {0} implementiert org.xml.sax.Parser nicht."},


  {
    ER_PARSER_PROPERTY_NOT_SPECIFIED,
     "Die Systemeigenschaft org.xml.sax.parser wurde nicht angegeben."},


  {
    ER_PARSER_ARG_CANNOT_BE_NULL,
     "Das Parserargument darf nicht Null sein."},



  {
    ER_FEATURE,
     "Merkmal: {0}"},



  {
    ER_PROPERTY,
     "Eigenschaft: {0}"},


  {
    ER_NULL_ENTITY_RESOLVER,
     "Entity-Resolver Null"},


  {
    ER_NULL_DTD_HANDLER,
     "DTD-Handler Null"},

  {
    ER_NO_DRIVER_NAME_SPECIFIED,
     "Kein Treibername angegeben!"},


  {
    ER_NO_URL_SPECIFIED,
     "Kein URL angegeben!"},


  {
    ER_POOLSIZE_LESS_THAN_ONE,
     "Pool-Gr\u00f6\u00dfe ist kleiner als 1!"},


  {
    ER_INVALID_DRIVER_NAME,
     "Ung\u00fcltiger Treibername angegeben!"},



  {
    ER_ERRORLISTENER,
     "ErrorListener"},


  {
    ER_ASSERT_NO_TEMPLATE_PARENT,
     "Programmierfehler! Ausdruck weist kein \u00fcbergeordnetes Element ElemTemplateElement auf!"},


  {
    ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR,
     "Programmierannahme in RundundentExprEliminator: {0}"},

  {
    ER_NOT_ALLOWED_IN_POSITION,
     "{0} ist an dieser Stelle im Stylesheet nicht zul\u00e4ssig!"},

  {
    ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION,
     "An dieser Stelle im Stylesheet ist nur Leerraum zul\u00e4ssig!"},

  {
    INVALID_TCHAR,
     "Ung\u00fcltiger Wert: {1} wurde f\u00fcr das CHAR-Attribut {0} verwendet. Ein Attribut vom Typ CHAR darf nur ein Zeichen aufweisen!"},


  {
    INVALID_QNAME,
     "Ung\u00fcltiger Wert: {1} wurde f\u00fcr das QNAME-Attribut {0} verwendet."},


  {
    INVALID_ENUM,
     "Ung\u00fcltiger Wert: {1} wurde f\u00fcr das ENUM-Attribut {0} verwendet. Die g\u00fcltigen Werte lauten: {2}."},


  {
    INVALID_NMTOKEN,
     "Ung\u00fcltiger Wert: {1} wurde f\u00fcr das NMTOKEN-Attribut {0} verwendet. "},


  {
    INVALID_NCNAME,
     "Ung\u00fcltiger Wert: {1} wurde f\u00fcr das NCNAME-Attribut {0} verwendet. "},



  {
    INVALID_BOOLEAN,
     "Ung\u00fcltiger Wert: {1} wurde f\u00fcr das boolean-Attribut {0} verwendet. "},


  {
    INVALID_NUMBER,
     "Ung\u00fcltiger Wert: {1} wurde f\u00fcr das number-Attribut {0} verwendet. "},




  {
    ER_ARG_LITERAL,
     "Das Argument f\u00fcr {0} im Muster muss ein Literal sein."},


  {
    ER_DUPLICATE_GLOBAL_VAR,
     "Doppelte Deklaration einer globalen Variablen."},



  {
    ER_DUPLICATE_VAR,
     "Doppelte Deklaration einer Variablen."},


  {
    ER_TEMPLATE_NAME_MATCH,
     "Das Element xsl:template muss ein name- oder ein match-Attribut (oder beide) aufweisen."},


  {
    ER_INVALID_PREFIX,
     "Pr\u00e4fix in exclude-result-prefixes ist nicht g\u00fcltig: {0}"},


  {
    ER_NO_ATTRIB_SET,
     "Das Attributset mit dem Namen {0} ist nicht vorhanden."},



  /** WG_FOUND_CURLYBRACE          */

  {
    WG_FOUND_CURLYBRACE,
      "'}' gefunden, aber keine Attributvorlage ge\u00f6ffnet!"},

  /** WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR          */

  {
    WG_COUNT_ATTRIB_MATCHES_NO_ANCESTOR,
      "Warnung: ''count''-Attribute passt zu keinem Vorg\u00e4ngerknoten in xsl:number! Ziel = {0}"},

  /** WG_EXPR_ATTRIB_CHANGED_TO_SELECT          */

  {
    WG_EXPR_ATTRIB_CHANGED_TO_SELECT,
      "Alte Syntax: Der Name des 'expr'-Attributs ist zu 'select' ge\u00e4ndert worden."},

  /** WG_NO_LOCALE_IN_FORMATNUMBER          */

  {
    WG_NO_LOCALE_IN_FORMATNUMBER,
      "Xalan bearbeitet den lokalen Namen in der Funktion format-number noch nicht."},

  /** WG_LOCALE_NOT_FOUND          */

  {
    WG_LOCALE_NOT_FOUND,
      "Warnung: Locale f\u00fcr xml:lang={0} nicht gefunden"},

  /** WG_CANNOT_MAKE_URL_FROM          */

  {
    WG_CANNOT_MAKE_URL_FROM,
      "URL kann nicht erstellt werden aus: {0}"},

  /** WG_CANNOT_LOAD_REQUESTED_DOC          */

  {
    WG_CANNOT_LOAD_REQUESTED_DOC,
      "Angefordertes Dokument kann nicht geladen werden: {0}"},

  /** WG_CANNOT_FIND_COLLATOR          */

  {
    WG_CANNOT_FIND_COLLATOR,
      "Collator f\u00fcr <sort xml:lang={0} nicht gefunden"},

  /** WG_FUNCTIONS_SHOULD_USE_URL          */

  {
    WG_FUNCTIONS_SHOULD_USE_URL,
      "Alte Syntax: Die Funktionsanweisung sollten einen URL von {0} verwenden"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_UTF8          */

  {
    WG_ENCODING_NOT_SUPPORTED_USING_UTF8,
      "Codierung nicht unterst\u00fctzt: {0}, UTF-8 wird verwendet"},

  /** WG_ENCODING_NOT_SUPPORTED_USING_JAVA          */

  {
    WG_ENCODING_NOT_SUPPORTED_USING_JAVA,
      "Codierung nicht unterst\u00fctzt: {0}, Java {1} wird verwendet"},

  /** WG_SPECIFICITY_CONFLICTS          */

  {
    WG_SPECIFICITY_CONFLICTS,
      "Spezifit\u00e4tskonflikte gefunden: {0} Zuletzt in Stylesheet gefundenes wird verwendet."},

  /** WG_PARSING_AND_PREPARING          */

  {
    WG_PARSING_AND_PREPARING,
      "========= Parsen und Vorbereiten {0} =========="},

  /** WG_ATTR_TEMPLATE          */

  {
    WG_ATTR_TEMPLATE, "Attributvorlage, {0}"},

  /** WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE          */

  {
    WG_CONFLICT_BETWEEN_XSLSTRIPSPACE_AND_XSLPRESERVESPACE,
      "Match-Konflikt zwischen xsl:strip-space und xsl:preserve-space"},

  /** WG_ATTRIB_NOT_HANDLED          */

  {
    WG_ATTRIB_NOT_HANDLED,
      "Xalan bearbeitet das Attribut {0} noch nicht!"},

  /** WG_NO_DECIMALFORMAT_DECLARATION          */

  {
    WG_NO_DECIMALFORMAT_DECLARATION,
      "Keine Vereinbarung f\u00fcr Dezimalformat gefunden: {0}"},

  /** WG_OLD_XSLT_NS          */

  {
    WG_OLD_XSLT_NS, "XSLT-Namensraum fehlt oder ist nicht korrekt. "},

  /** WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED          */

  {
    WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED,
      "Nur eine Standardvereinbarung xsl:decimal-format ist erlaubt."},

  /** WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE          */

  {
    WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE,
      "xsl:decimal-format-Namen m\u00fcssen eindeutig sein. Name \"{0}\" ist nicht eindeutig."},

  /** WG_ILLEGAL_ATTRIBUTE          */

  {
    WG_ILLEGAL_ATTRIBUTE,
      "{0} hat ein ung\u00fcltiges Attribut: {1}"},

  /** WG_COULD_NOT_RESOLVE_PREFIX          */

  {
    WG_COULD_NOT_RESOLVE_PREFIX,
      "Namensraum-Pr\u00e4fix konnte nicht aufgel\u00f6st werden: {0}. Der Knoten wird ignoriert."},

  /** WG_STYLESHEET_REQUIRES_VERSION_ATTRIB          */

  {
    WG_STYLESHEET_REQUIRES_VERSION_ATTRIB,
      "xsl:stylesheet erfordert ein 'version'-Attribut!"},

  /** WG_ILLEGAL_ATTRIBUTE_NAME          */

  {
    WG_ILLEGAL_ATTRIBUTE_NAME,
      "Ung\u00fcltiger Attributname: {0}"},

  /** WG_ILLEGAL_ATTRIBUTE_VALUE          */

  {
    WG_ILLEGAL_ATTRIBUTE_VALUE,
      "Ung\u00fcltiger Wert f\u00fcr Attribut {0}: {1}"},

  /** WG_EMPTY_SECOND_ARG          */

  {
    WG_EMPTY_SECOND_ARG,
      "Resultierendes Knotenset aus zweitem Argument von document-Funktion ist leer. Das erste Argument wird verwendet."},




  /** WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML          */
  {
    WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
      "Der Wert des name-Attributs von xsl:processing-instruction darf nicht 'xml' lauten."},


  /** WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME          */
  {
    WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
      "Der Wert des name-Attributs von xsl:processing-instruction muss einem g\u00fcltigen ''NCName'' entsprechen: {0}"},


  /** WG_ILLEGAL_ATTRIBUTE_POSITION         */
  {
    WG_ILLEGAL_ATTRIBUTE_POSITION,
      "Das Attribut {0} kann nicht nach untergeordneten Elementen oder vor der Erstellung eines Elements hinzugef\u00fcgt werden. Das Attribut wird ignoriert."},


    { "ui_language", "de"},
    { "help_language", "de"},
    { "language", "de"},
    { "BAD_CODE", "Parameter f\u00fcr createMessage au\u00dferhalb der Grenzwerte"},
    { "FORMAT_FAILED",
      "Ausnahme bei messageFormat-Aufruf"},
    { "version", ">>>>>>> Xalan Version "},
    { "version2", "<<<<<<<"},
    { "yes", "ja"},
    { "line", "Zeile #"},
    { "column", "Spalte #"},
    { "xsldone",  "XSLProcessor: fertig"},
    { "xslProc_option",  "Optionen der Klasse Process in Xalan-J-Befehlszeile:"},
    { "optionIN", "    -IN inputXMLURL"},
    { "optionXSL", "   [-XSL XSLTransformationURL]"},
    { "optionOUT",  "   [-OUT outputFileName]"},
    { "optionLXCIN", "   [-LXCIN compiledStylesheetFileNameIn]"},
    { "optionLXCOUT", "   [-LXCOUT compiledStylesheetFileNameOutOut]"},
    { "optionPARSER", "   [-PARSER voll qualifizierter Klassenname von Parserliaison]"},
    { "optionE",  "   [-E (Entity-Referenzen nicht erweitern)]"},
    { "optionV", "   [-E (Entity-Referenzen nicht erweitern)]"},
    { "optionQC", "   [-QC (Ger\u00e4uscharme Warnungen bei Musterkonflikten)]"},
    { "optionQ", "   [-Q  (Ger\u00e4uschmer Modus)]"},
    { "optionLF", "   [-LF (bei Ausgabe nur Zeilenvorsprung {Standard ist CR/LF})]"},
    { "optionCR", "   [-CR (bei Ausgabe nur Wagenr\u00fccklauf {Standard ist CR/LF})]"},
    { "optionESCAPE", "   [-ESCAPE (Zeichen f\u00fcr Escape {Standard ist <>&\"\'\\r\\n}]"},
    {  "optionINDENT", "   [-INDENT (Anzahl der Leerzeichen zum Einr\u00fccken {Standard ist 0})]"},
    { "optionTT", "   [-TT (Vorlagen beim Aufruf verfolgen.)]"},
    { "optionTG", "   [-TG (Jedes Erzeugungsereignis verfolgen.)]"},
    { "optionTS", "   [-TS (Jedes Auswahlereignis verfolgen.)]"},
    { "optionTTC", "   [-TTC (Die Vorlagen-Tochterknoten bei Bearbeitung verfolgen.)]"},
    { "optionTCLASS", "   [-TCLASS (TraceListener-Klasse f\u00fcr Trace-Erweiterungen.)]"},
    { "optionVALIDATE", "   [-VALIDATE (Festlegen, ob Validierung stattfindet. Standard ist keine Validierung.)]"},
    { "optionEDUMP", "   [-EDUMP {optionaler Dateiname} (Speicherauszug bei Fehler.)]"},
    { "optionXML", "   [-XML (XML-Formatierer verwenden und XML-Header hinzuf\u00fcgen.)]"},
    { "optionTEXT", "   [-TEXT (Einfachen Textformatierer verwenden.)]"},
    { "optionHTML", "   [-HTML (HTML-Formatierer verwenden.)]"},
    { "optionPARAM", "   [-PARAM Namensausdruck (Stylesheet-Parameter festlegen)]"},
    { "noParsermsg1",  "XSL-Prozess fehlgeschlagen."},
    { "noParsermsg2",  "** Parser nicht gefunden **"},
    { "noParsermsg3",  "Bitte Classpath \u00fcberpr\u00fcfen."},
    { "noParsermsg4", "Wenn Sie IBMs XML Parser for Java nicht haben, k\u00f6nnen Sie ihn von folgender Adresse herunterladen"},
    { "noParsermsg5",
    { "optionURIRESOLVER",
     "   [-URIRESOLVER vollst\u00e4ndiger Klassenname (zum Aufl\u00f6sen von URIs zu verwendender URIResolver)]"},
    { "optionENTITYRESOLVER",
     "   [-ENTITYRESOLVER vollst\u00e4ndiger Klassenname (zum Aufl\u00f6sen von Entities zu verwendender EntityResolver)]"},
    { "optionCONTENTHANDLER",
     "   [-CONTENTHANDLER vollst\u00e4ndiger Klassenname (zum Serialisieren der Ausgabe zu verwendender ContentHandler)]"},
    { "optionLINENUMBERS",
     "   [-L Zeilennummern f\u00fcr Quelldokument verwenden]"},
		


    { "optionMEDIA",
     " [-MEDIA Medientyp (media-Attribut zum Auffinden des einem Dokument zugeordneten Stylesheets verwenden)]"},
    { "optionFLAVOR",
    { "optionDIAG",
     " [-DIAG (Zeitdauer der Transformation in Millisekunden ausgeben)]"},
    { "optionINCREMENTAL",
    { "optionNOOPTIMIMIZE",
    { "optionRL",
     " [-RL Rekursionsgrenze (Numerische Begrenzung der Rekursionstiefe f\u00fcr das Stylesheet)]"},
    { "optionXO",
     " [-XO [Translet-Name] (Zuweisen eines Namens zum erzeugten Translet)]"},
    { "optionXD",
     " [-XD Zielverzeichnis (Angabe eines Zielverzeichnisses f\u00fcr das Translet)]"},
    { "optionXJ",
     " [-XJ JAR-Datei (Erstellt ein Paket mit den Translet-Klassen in einer JAR-Datei mit dem Namen <JAR-Datei>)]"},
    {  "optionXP",
     " [-XP Paket (Angabe eines Paketnamen-Pr\u00e4fixes f\u00fcr alle erzeugten Translet-Klassen)]"}


  };


  /** String for use when a bad error code was encountered.    */
  public static final String BAD_CODE = "BAD_CODE";

  /** String for use when formatting of the error string failed.   */
  public static final String FORMAT_FAILED = "FORMAT_FAILED";

  /** General error string.   */
  public static final String ERROR_STRING = "#Fehler";

  /** String to prepend to error messages.  */
  public static final String ERROR_HEADER = "Fehler: ";

  /** String to prepend to warning messages.    */
  public static final String WARNING_HEADER = "Warnung: ";

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
}


