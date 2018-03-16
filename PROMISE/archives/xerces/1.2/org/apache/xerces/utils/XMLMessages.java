package org.apache.xerces.utils;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;

/**
 * XMLMessages provides error messages for the XML 1.0 Recommendation and for 
 * the Namespaces Recommendation
 *
 */
public class XMLMessages implements XMLMessageProvider {
    /**
     * The domain of messages concerning the XML 1.0 specification.
     */

    /**
     *
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }
    /**
     *
     */
    public Locale getLocale() {
        return fLocale;
    }

    /**
     * Creates a message from the specified key and replacement
     * arguments, localized to the given locale.
     *
     * @param locale    The requested locale of the message to be
     *                  created.
     * @param key       The key for the message text.
     * @param args      The arguments to be used as replacement text
     *                  in the message created.
     */
    public String createMessage(Locale locale, int majorCode, int minorCode, Object args[]) {
        boolean throwex = false;
        if (fResourceBundle == null || locale != fLocale) {
            if (locale != null)
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.XMLMessages", locale);
            if (fResourceBundle == null)
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.XMLMessages");
        }
        if (majorCode < 0 || majorCode >= fgMessageKeys.length - 1) {
            majorCode = MSG_BAD_MAJORCODE;
            throwex = true;
        }
        String msgKey = fgMessageKeys[majorCode];
        String msg = fResourceBundle.getString(msgKey);
        if (args != null) {
            try {
                msg = java.text.MessageFormat.format(msg, args);
            } catch (Exception e) {
                msg = fResourceBundle.getString(fgMessageKeys[MSG_FORMAT_FAILURE]);
                msg += " " + fResourceBundle.getString(msgKey);
            }
        }
/*
        if (minorCode > 0 && minorCode < fgConstraints.length - 1) {
            Constraint c = fgConstraints[minorCode];
            String prefix = "{ " + c.sections;
            if (c.productions != null && c.productions.length() != 0)
                prefix = prefix + " " + c.productions;
            msg = prefix + " } " + msg;
        }
 */
        if (throwex) {
            throw new RuntimeException(msg);
        }
        return msg;
    }
    private Locale fLocale = null;
    private ResourceBundle fResourceBundle = null;
    public static final int
        MSG_REFERENCE_TO_UNPARSED_ENTITY = 63,
        MSG_REFERENCE_TO_EXTERNAL_ENTITY = 64,
        MSG_DUPLICATE_TYPE_IN_MIXED_CONTENT = 67,
        MSG_ELEMENT_ENTITY_MISMATCH = 68,
        MSG_ID_DEFAULT_TYPE_INVALID = 69,
        MSG_ENCODING_REQUIRED = 70,
        MSG_RECURSIVE_REFERENCE = 71,
        MSG_RECURSIVE_PEREFERENCE = 72,
        MSG_ID_INVALID = 75,
        MSG_ID_NOT_UNIQUE = 76,
        MSG_IDREF_INVALID = 77,
        MSG_NMTOKEN_INVALID = 78,
        MSG_ENTITY_INVALID = 79,
        MSG_ENTITIES_INVALID = 80,
        MSG_ELEMENT_WITH_ID_REQUIRED = 81,
        MSG_ATTRIBUTE_NOT_DECLARED = 82,
        MSG_ELEMENT_NOT_DECLARED = 83,
        MSG_AVAILABLE1 = 84,
        MSG_DUPLICATE_ATTDEF = 85,
        MSG_MORE_THAN_ONE_ID_ATTRIBUTE = 86,
        MSG_CONTENT_INVALID = 87,
        MSG_CONTENT_INCOMPLETE = 88,
        MSG_ELEMENT_ALREADY_DECLARED = 89,
        MSG_ATTRIBUTE_VALUE_NOT_IN_LIST = 90,
        MSG_AVAILABLE2 = 91,
        MSG_UNDECLARED_ELEMENT_IN_CONTENTSPEC = 92,
        MSG_FIXED_ATTVALUE_INVALID = 93,
        MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED = 94,
        MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED = 95,
        MSG_AVAILABLE3 = 96,
        MSG_AVAILABLE4 = 97,
        MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN = 98,
        MSG_AVAILABLE5 = 99,
        MSG_SYSTEMID_UNTERMINATED = 100,
        MSG_PUBLICID_UNTERMINATED = 101,
        MSG_EXTERNAL_ENTITY_NOT_PERMITTED = 102,
        MSG_AVAILABLE6 = 103,
        MSG_XMLDECL_MUST_BE_FIRST = 104,
        MSG_TEXTDECL_MUST_BE_FIRST = 105,
        MSG_ELEMENTDECL_UNTERMINATED = 106,
        MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_PEDECL = 107,
        MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_ENTITYDECL = 108,
        MSG_SPACE_REQUIRED_BEFORE_PERCENT_IN_PEDECL = 109,
        MSG_ENTITY_NAME_REQUIRED_IN_ENTITYDECL = 110,
        MSG_SPACE_REQUIRED_AFTER_ENTITY_NAME_IN_ENTITYDECL = 111,
        MSG_ENTITYDECL_UNTERMINATED = 112,
        MSG_NOTATION_NAME_REQUIRED_FOR_UNPARSED_ENTITYDECL = 113,
        MSG_NOTATION_NOT_DECLARED_FOR_UNPARSED_ENTITYDECL = 114,
        MSG_NAME_REQUIRED_IN_NOTATIONTYPE = 115,
        MSG_NMTOKEN_REQUIRED_IN_ENUMERATION = 116,
        MSG_NOTATION_NOT_DECLARED_FOR_NOTATIONTYPE_ATTRIBUTE = 117,
        MSG_NOTATIONTYPE_UNTERMINATED = 118,
        MSG_ENUMERATION_UNTERMINATED = 119,
        MSG_NOTATION_NAME_REQUIRED_IN_NOTATIONDECL = 120,
        MSG_MORE_THAN_ONE_NOTATION_ATTRIBUTE = 121,
        MSG_NOTATIONDECL_UNTERMINATED = 122,
        MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE = 123,
        MSG_CDEND_IN_CONTENT = 124,
        MSG_ELEMENT_TYPE_REQUIRED_IN_ATTLISTDECL = 125,
        MSG_TWO_COLONS_IN_QNAME = 126,
        MSG_MARKUP_NOT_RECOGNIZED_IN_CONTENT = 127,
        MSG_MARKUP_NOT_RECOGNIZED_IN_MISC = 128,
        MSG_MARKUP_NOT_RECOGNIZED_IN_PROLOG = 129,
        MSG_OPEN_PAREN_REQUIRED_IN_NOTATIONTYPE = 130,
        MSG_PITARGET_REQUIRED = 131,
        MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE = 132,
        MSG_URI_FRAGMENT_IN_SYSTEMID = 133,
        MSG_ROOT_ELEMENT_REQUIRED = 134,
        MSG_SPACE_REQUIRED_AFTER_FIXED_IN_DEFAULTDECL = 135,
        MSG_SPACE_REQUIRED_AFTER_NOTATION_IN_NOTATIONTYPE = 136,
        MSG_SPACE_REQUIRED_AFTER_NOTATION_NAME_IN_NOTATIONDECL = 137,
        MSG_SPACE_REQUIRED_BEFORE_ATTRIBUTE_NAME_IN_ATTDEF = 138,
        MSG_SPACE_REQUIRED_BEFORE_ATTTYPE_IN_ATTDEF = 139,
        MSG_SPACE_REQUIRED_BEFORE_DEFAULTDECL_IN_ATTDEF = 140,
        MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ATTLISTDECL = 141,
        MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_NOTATIONDECL = 142,
        MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE = 143,
        MSG_XML_SPACE_DECLARATION_ILLEGAL = 144,
        MSG_CLOSE_PAREN_REQUIRED_IN_MIXED = 145,
        MSG_CONTENTSPEC_REQUIRED_IN_ELEMENTDECL = 146,
        MSG_DOCTYPEDECL_UNTERMINATED = 147,
        MSG_ELEMENT_TYPE_REQUIRED_IN_ELEMENTDECL = 148,
        MSG_ELEMENT_TYPE_REQUIRED_IN_MIXED_CONTENT = 149,
        MSG_MARKUP_NOT_RECOGNIZED_IN_DTD = 150,
        MSG_ATTRIBUTE_VALUE_UNTERMINATED = 151,
        MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN = 152,
        MSG_ROOT_ELEMENT_TYPE_REQUIRED = 153,
        MSG_SPACE_REQUIRED_AFTER_PUBIDLITERAL_IN_EXTERNALID = 154,
        MSG_SPACE_REQUIRED_BEFORE_CONTENTSPEC_IN_ELEMENTDECL = 155,
        MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ELEMENTDECL = 156,
        MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_UNPARSED_ENTITYDECL = 157,
        MSG_SPACE_REQUIRED_BEFORE_PUBIDLITERAL_IN_EXTERNALID = 158,
        MSG_SPACE_REQUIRED_BEFORE_ROOT_ELEMENT_TYPE_IN_DOCTYPEDECL = 159,
        MSG_SPACE_REQUIRED_BEFORE_SYSTEMLITERAL_IN_EXTERNALID = 160,
        MSG_REFERENCE_NOT_IN_ONE_ENTITY = 161,
        MSG_COMMENT_NOT_IN_ONE_ENTITY = 162,
        MSG_COMMENT_UNTERMINATED = 163,
        MSG_PI_UNTERMINATED = 164,
        MSG_PI_NOT_IN_ONE_ENTITY = 165,
        MSG_REFERENCE_UNTERMINATED = 166,
        MSG_PREFIX_DECLARED = 167,
        MSG_ATT_DEFAULT_INVALID = 168,
        MSG_GENERIC_SCHEMA_ERROR = 169,

        MSG_MAX_CODE = 200;

    private static final String[] fgMessageKeys = {

    };

    public static final int

        VC_NOTATION_ATTRIBUTES = 84,
        P53_DUPLICATE = 85,
        VC_ENUMERATION = 86,
        VC_FIXED_ATTRIBUTE_DEFAULT = 87,
        VC_REQUIRED_ATTRIBUTE = 88,
        VC_NOTATION_DECLARED = 89,
        P58_NAME_REQUIRED = 90,
        P58_UNTERMINATED = 91,
        P59_NMTOKEN_REQUIRED = 92,
        P59_UNTERMINATED = 93,
        P70_SPACE = 94,
        P70_REQUIRED_NAME = 95,
        P70_REQUIRED_SPACE = 96,
        P71_UNTERMINATED = 97,
        P72_SPACE = 98,
        P72_UNTERMINATED = 99,
        P76_REQUIRED = 100,
        P82_NAME_REQUIRED = 101,
        P82_SPACE_REQUIRED = 102,
        P82_UNTERMINATED = 103,
        P14_INVALID = 104,
        P16_PITARGET_REQUIRED = 105,
        P16_REQUIRED = 106,
        P1_ELEMENT_REQUIRED = 107,
        P22_NOT_RECOGNIZED = 108,
        P27_NOT_RECOGNIZED = 109,
        P43_NOT_RECOGNIZED = 110,
        P52_ELEMENT_TYPE_REQUIRED = 111,
        P52_SPACE_REQUIRED = 112,
        P53_SPACE_REQUIRED = 113,
        P58_OPEN_PAREN_REQUIRED = 114,
        P58_SPACE_REQUIRED = 115,
        P60_SPACE_REQUIRED = 116,
        S2_10_DECLARATION_ILLEGAL = 117,
        P39_ELEMENT_TYPE_REQUIRED = 118,
        P28_ROOT_ELEMENT_TYPE_REQUIRED = 119,
        P28_SPACE_REQUIRED = 120,
        P28_UNTERMINATED = 121,
        P29_NOT_RECOGNIZED = 122,
        P45_CONTENTSPEC_REQUIRED = 123,
        P45_ELEMENT_TYPE_REQUIRED = 124,
        P45_SPACE_REQUIRED = 125,
        P45_UNTERMINATED = 126,
        P47_CLOSE_PAREN_REQUIRED = 127,
        P47_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED = 128,
        P51_CLOSE_PAREN_REQUIRED = 129,
        P51_ELEMENT_TYPE_REQUIRED = 130,
        P75_SPACE_REQUIRED = 131,
        P76_SPACE_REQUIRED = 132,
        P15_UNTERMINATED = 133,
        P16_UNTERMINATED = 134,
        P67_UNTERMINATED = 135,
        P10_UNTERMINATED = 136,
        P22_XMLDECL_MUST_BE_FIRST = 137,
        P30_TEXTDECL_MUST_BE_FIRST = 138,
        P5_INVALID_CHARACTER = 139,
        P11_UNTERMINATED = 140,
        P12_UNTERMINATED = 141,
        P11_URI_FRAGMENT = 142,
        VC_ONE_NOTATION_PER_ELEMENT_TYPE = 143,
        NC_PREFIX_DECLARED = 144,
        VC_ATTRIBUTE_DEFAULT_LEGAL = 145,
        SCHEMA_GENERIC_ERROR = 146,

        CONSTRAINT_MAX_CODE = 200;

/*
    private static final Constraint[] fgConstraints = {
        new Constraint(null, null, null, null),
        new Constraint("2.8", "", "2.8 VC: Root Element Type",
                       "Validity Constraint: Root Element Type\n" +
                       "The Name in the document type declaration must match the element type of the\n" +
                       "root element."),
        new Constraint("3.3.1 2.3", "[56] [6]", "3.3.1 VC: IDREF",
                       "Validity Constraint: IDREF\n" +
                       "[56] TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' | 'NMTOKEN' | 'NMTOKENS'\n" +
                       "Values of type IDREF must match the Name production, and values of type\n" +
                       "IDREFS must match Names; each Name must match the value of an ID attribute\n" +
                       "on some element in the XML document; i.e. IDREF values must match the value\n" +
                       "of some ID attribute."),
        new Constraint("3.3.1 2.3", "[56] [8]", "3.3.1 VC: Name Token",
                       "Validity Constraint: Name Token\n" +
                       "Values of type NMTOKEN must match the Nmtoken production; values of type\n" +
                       "NMTOKENS must match Nmtokens."),
        new Constraint("2.6", "[17]", "2.6 [17] PITarget ::= Name - (('X' | 'x') ('M' | 'm') ('L' | 'l'))",
                       "[17] PITarget ::= Name - (('X' | 'x') ('M' | 'm') ('L' | 'l'))\n" +
                       "The target names \"XML\", \"xml\", and so on are reserved for standardization\n" +
                       "in this or future versions of this specification."),
        new Constraint("2.6 2.3", "[16] [3]", "2.6 [16] PI ::= '<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'",
                       "[16] PI ::= '<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'\n" +
                       "[3] S ::= (#x20 | #x9 | #xD | #xA)+\n" +
                       "White space is required between the PITarget and any additional characters\n" +
                       "that are to be passed through to the application."),
        new Constraint("2.6 2.2", "[16] [2]", "2.6 [2] PI ::= '<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'",
                       "[16] PI ::= '<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'\n" +
                       "[2] Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]\n" +
                       "Processing instruction data is required to contain legal XML characters."),
        new Constraint("2.5", "[15]", "2.5 [15] Comment ::= '<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'",
                       "[15] Comment ::= '<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'\n" +
                       "For compatibility, the string \"--\" (double-hyphen) must not occur within comments."),
        new Constraint("2.5 2.2", "[15] [2]", "2.5 [2] Comment ::= '<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'",
                       "[15] Comment ::= '<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'\n" +
                       "[2] Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]\n" +
                       "Comments are required to contain legal XML characters."),
        new Constraint("4.1", "[66] [2]", "4.1 WFC: Legal Character",
                       "Well-Formedness Constraint: Legal Character\n" +
                       "[66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'\n" +
                       "[2] Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]\n" +
                       "Characters referred to using character references must match the production\n" +
                       "for Char."),
        new Constraint("2.3", "[10]", "2.3 [10] AttValue ::= '\"' ([^<&\"] | Reference)* '\"' | \"'\" ([^<&'] | Reference)* \"'\"",
                       "[10] AttValue ::= '\"' ([^<&\"] | Reference)* '\"' | \"'\" ([^<&'] | Reference)* \"'\"\n" +
                       "Attribute values are required to contain legal XML characters."),
        new Constraint("3.1 2.3", "[41] [10]", "3.1 WFC: No < in Attribute Values",
                       "Well-Formedness Constraint: No < in Attribute Values\n" +
                       "[41] Attribute ::= Name Eq AttValue\n" +
                       "[10] AttValue ::= '\"' ([^<&\"] | Reference)* '\"' | \"'\" ([^<&'] | Reference)* \"'\"\n" +
                       "The replacement text of any entity referred to directly or indirectly in an\n" +
                       "attribute value (other than \"&lt;\") must not contain a <."),
        new Constraint("2.3", "[10]", "2.3 [10] AttValue ::= '\"' ([^<&\"] | Reference)* '\"' | \"'\" ([^<&'] | Reference)* \"'\"",
                       "[10] AttValue ::= '\"' ([^<&\"] | Reference)* '\"' | \"'\" ([^<&'] | Reference)* \"'\"\n" +
                       "Attribute values are specified using quoted strings."),
        new Constraint("4.1", "[68]", "4.1 [68] EntityRef ::= '&' Name ';'",
                       "[68] EntityRef ::= '&' Name ';'\n" +
                       "The '&' delimiter must be followed by a valid Name in an entity reference."),
        new Constraint("4.1", "[68]", "4.1 [68] EntityRef ::= '&' Name ';'",
                       "[68] EntityRef ::= '&' Name ';'\n" +
                       "An entity reference must end with a ';' delimiter."),
        new Constraint("4.1", "[66]", "4.1 [66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'",
                       "[66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'\n" +
                       "If the character reference begins with \"&#\", not \"&#x\", the digits up\n" +
                       "to the terminating ; provide a decimal representation of the character's\n" +
                       "code point in ISO/IEC 10646."),
        new Constraint("4.1", "[66]", "4.1 [66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'",
                       "[66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'\n" +
                       "If the character reference begins with \"&#x\", the digits and letters up to\n" +
                       "the terminating ; provide a hexadecimal representation of the character's\n" +
                       "code point in ISO/IEC 10646."),
        new Constraint("4.1", "[66]", "4.1 [66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'",
                       "[66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'\n" +
                       "A character reference must end with a ';' delimiter."),
        new Constraint("2.3", "[11]", "2.3 [11] SystemLiteral ::= ('\"' [^\"]* '\"') | (\"'\" [^']* \"'\")",
                       "[11] SystemLiteral ::= ('\"' [^\"]* '\"') | (\"'\" [^']* \"'\")\n" +
                       "System identifiers are specified using quoted strings."),
        new Constraint("2.3", "[11]", "2.3 [11] SystemLiteral ::= ('\"' [^\"]* '\"') | (\"'\" [^']* \"'\")",
                       "[11] SystemLiteral ::= ('\"' [^\"]* '\"') | (\"'\" [^']* \"'\")\n" +
                       "System identifiers are required to contain legal XML characters."),
        new Constraint("2.3", "[12]", "2.3 [12] PubidLiteral ::= '\"' PubidChar* '\"' | \"'\" (PubidChar - \"'\")* \"'\"",
                       "[12] PubidLiteral ::= '\"' PubidChar* '\"' | \"'\" (PubidChar - \"'\")* \"'\"\n" +
                       "Public identifiers are specified using quoted strings."),
        new Constraint("2.3", "[13]", "2.3 [13] PubidChar ::= #x20 | #xD | #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]",
                       "[13] PubidChar ::= #x20 | #xD | #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]\n" +
                       "Public identifiers must consist of PubidChar characters."),
        new Constraint("3.4", "[62]", "3.4 [62] includeSect ::= '<![' S? 'INCLUDE' S? '[' extSubsetDecl ']]>'",
                       "[62] includeSect ::= '<![' S? 'INCLUDE' S? '[' extSubsetDecl ']]>'\n" +
                       "Included conditional section must be terminated by \"]]>\"."),
        new Constraint("3.4", "[63]", "3.4 [63] ignoreSect ::= '<![' S? 'IGNORE' S? '[' ignoreSectContents* ']]>'",
                       "[63] ignoreSect ::= '<![' S? 'IGNORE' S? '[' ignoreSectContents* ']]>'\n" +
                       "Excluded conditional sections must be terminated by \"]]>\"."),
        new Constraint("3.4", "[65]", "3.4 [65] Ignore ::= Char* - (Char* ('<![' | ']]>') Char*)",
                       "[65] Ignore ::= Char* - (Char* ('<![' | ']]>') Char*)\n" +
                       "Excluded conditional sections are required to contain legal XML characters."),
        new Constraint("3.1", "[40]", "3.1 [40] STag ::= '<' Name (S Attribute)* S? '>'",
                       "[40] STag ::= '<' Name (S Attribute)* S? '>'\n" +
                       "[39] element ::= EmptyElemTag | STag content ETag\n" +
                       "[44] EmptyElemTag ::= '<' Name (S Attribute)* S? '/>'\n" +
                       "Element type name must be followed by Attribute, \">\" or \"/>\"."),
        new Constraint("3.1", "[41]", "3.1 [41] Attribute ::= Name Eq AttValue",
                       "[41] Attribute ::= Name Eq AttValue\n" +
                       "[25] Eq ::= S? '=' S?\n" +
                       "Attribute name must be followed by an '=' character."),
        new Constraint("3.1", "[40] [44]", "3.1 WFC: Unique Att Spec",
                       "Well-Formedness Constraint: Unique Att Spec\n" +
                       "[40] STag ::= '<' Name (S Attribute)* S? '>'\n" +
                       "[44] EmptyElemTag ::= '<' Name (S Attribute)* S? '/>'\n" +
                       "No attribute name may appear more than once in the same start-tag or\n" +
                       "empty-element tag."),
        new Constraint("4.3.1", "[77]", "4.3.1 [77] TextDecl ::= '<?xml' VersionInfo? EncodingDecl S? '?>'",
                       "[77] TextDecl ::= '<?xml' VersionInfo? EncodingDecl S? '?>'\n" +
                       "The encoding declaration is required in a text declaration."),
        new Constraint("2.8", "[23]", "2.8 [23] XMLDecl ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'",
                       "[23] XMLDecl ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'\n" +
                       "The version is required in an XML declaration."),
        new Constraint("2.8", "[24]", "2.8 [24] VersionInfo ::= S 'version' Eq (\"'\" VersionNum \"'\" | '\"' VersionNum '\"')",
                       "[24] VersionInfo ::= S 'version' Eq (\"'\" VersionNum \"'\" | '\"' VersionNum '\"')\n" +
                       "[25] Eq ::= S? '=' S?\n" +
                       "The '=' character must follow \"version\" in VersionInfo."),
        new Constraint("2.9", "[32]", "2.9 [32] SDDecl ::= S 'standalone' Eq (\"'\" ('yes' | 'no') \"'\" | '\"' ('yes' | 'no') '\"')",
                       "[32] SDDecl ::= S 'standalone' Eq (\"'\" ('yes' | 'no') \"'\" | '\"' ('yes' | 'no') '\"')\n" +
                       "[25] Eq ::= S? '=' S?\n" +
                       "The '=' character must follow \"standalone\" in SDDecl."),
        new Constraint("4.3.3", "[80]", "4.3.3 [80] EncodingDecl ::= S 'encoding' Eq ('\"' EncName '\"' | \"'\" EncName \"'\")",
                       "[80] EncodingDecl ::= S 'encoding' Eq ('\"' EncName '\"' | \"'\" EncName \"'\")\n" +
                       "[25] Eq ::= S? '=' S?\n" +
                       "The '=' character must follow \"encoding\" in EncodingDecl."),
        new Constraint("2.8", "[24]", "2.8 [24] VersionInfo ::= S 'version' Eq (\"'\" VersionNum \"'\" | '\"' VersionNum '\"')",
                       "[24] VersionInfo ::= S 'version' Eq (\"'\" VersionNum \"'\" | '\"' VersionNum '\"')\n" +
                       "The version is specified using a quoted string."),
        new Constraint("2.9", "[32]", "2.9 [32] SDDecl ::= S 'standalone' Eq (\"'\" ('yes' | 'no') \"'\" | '\"' ('yes' | 'no') '\"')",
                       "[32] SDDecl ::= S 'standalone' Eq (\"'\" ('yes' | 'no') \"'\" | '\"' ('yes' | 'no') '\"')\n" +
                       "The standalone document declaration value is specified using a quoted string."),
        new Constraint("4.3.3", "[80]", "4.3.3 [80] EncodingDecl ::= S 'encoding' Eq ('\"' EncName '\"' | \"'\" EncName \"'\")",
                       "[80] EncodingDecl ::= S 'encoding' Eq ('\"' EncName '\"' | \"'\" EncName \"'\")\n" +
                       "The encoding name value is specified using a quoted string."),
        new Constraint("2.8", "[26]", "2.8 [26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')+",
                       "[26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')+\n" +
                       "The version is required to contain legal XML characters.."),
        new Constraint("2.9", "[32]", "2.9 [32] SDDecl ::= S 'standalone' Eq (\"'\" ('yes' | 'no') \"'\" | '\"' ('yes' | 'no') '\"')",
                       "[32] SDDecl ::= S 'standalone' Eq (\"'\" ('yes' | 'no') \"'\" | '\"' ('yes' | 'no') '\"')\n" +
                       "The standalone document declaration value is required to contain legal XML characters."),
        new Constraint("4.3.3", "[81]", "4.3.3 [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*",
                       "[81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*\n" +
                       "The encoding name value is required to contain legal XML characters."),
        new Constraint("2.8", "[26]", "2.8 [26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')+",
                       "[26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')+\n" +
                       "The version value must match the production for VersionNum."),
        new Constraint("2.8", "[26]", "2.8 [26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')+",
                       "[26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')+\n" +
                       "Processors may signal an error if they receive documents labeled with versions\n" +
                       "they do not support."),
        new Constraint("4.3.3", "[80]", "4.3.3 [80] EncodingDecl ::= S 'encoding' Eq ('\"' EncName '\"' | \"'\" EncName \"'\")",
                       "[80] EncodingDecl ::= S 'encoding' Eq ('\"' EncName '\"' | \"'\" EncName \"'\")\n" +
                       "Whitespace is required between the version and the encoding declaration."),
        new Constraint("4.3.3", "[81]", "4.3.3 [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*",
                       "[81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*\n" +
                       "The encoding name value must match the production for EncName."),
        new Constraint("2.9", "[32]", "2.9 [32] SDDecl ::= S 'standalone' Eq (\"'\" ('yes' | 'no') \"'\" | '\"' ('yes' | 'no') '\"')",
                       "[32] SDDecl ::= S 'standalone' Eq (\"'\" ('yes' | 'no') \"'\" | '\"' ('yes' | 'no') '\"')\n" +
                       "The standalone document declaration must have a value of \"yes\" or \"no\"."),
        new Constraint("2.8", "[23]", "2.8 [23] XMLDecl ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'",
                       "[23] XMLDecl ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'\n" +
                       "The XML declaration must be terminated by \"?>\"."),
        new Constraint("4.3.1", "[77]", "4.3.1 [77] TextDecl ::= '<?xml' VersionInfo? EncodingDecl S? '?>'",
                       "[77] TextDecl ::= '<?xml' VersionInfo? EncodingDecl S? '?>'\n" +
                       "The text declaration must be terminated by \"?>\"."),
        new Constraint("2.8", "[28]", "2.8 [28] doctypedecl ::= '<!DOCTYPE' S Name (S ExternalID)? S? ('[' (markupdecl | PEReference | S)* ']' S?)? '>'",
                       "[28] doctypedecl ::= '<!DOCTYPE' S Name (S ExternalID)? S? ('[' (markupdecl | PEReference | S)* ']' S?)? '>'\n" +
                       "The internal subset of the DTD is required to contain legal XML characters."),
        new Constraint("2.8", "[30]", "2.8 [30] extSubset ::= TextDecl? extSubsetDecl",
                       "[30] extSubset ::= TextDecl? extSubsetDecl\n" +
                       "[31] extSubsetDecl ::= ( markupdecl | conditionalSect | PEReference | S )*\n" +
                       "External entities in the DTD are required to contain legal XML characters."),
        new Constraint("2.3", "[9]", "2.3 [9] EntityValue ::= '\"' ([^%&\"] | PEReference | Reference)* '\"' | \"'\" ([^%&'] | PEReference | Reference)* \"'\"",
                       "[9] EntityValue ::= '\"' ([^%&\"] | PEReference | Reference)* '\"' | \"'\" ([^%&'] | PEReference | Reference)* \"'\"\n" +
                       "An entity value is required to contain legal XML characters."),
        new Constraint("3.2.2", "[51]", "3.2.2 [51] Mixed ::= '(' S? '#PCDATA' (S? '|' S? Name)* S? ')*' | '(' S? '#PCDATA' S? ')'",
                       "[51] Mixed ::= '(' S? '#PCDATA' (S? '|' S? Name)* S? ')*' | '(' S? '#PCDATA' S? ')'\n" +
                       "A mixed content model with child element types must be terminated by \")*\"."),
        new Constraint("4.1", "[69]", "4.1 [69] PEReference ::= '%' Name ';'",
                       "[69] PEReference ::= '%' Name ';'\n" +
                       "The '&' delimiter must be followed by a valid Name in a parameter entity reference."),
        new Constraint("4.1", "[69]", "4.1 [69] PEReference ::= '%' Name ';'",
                       "[69] PEReference ::= '%' Name ';'\n" +
                       "A parameter entity reference must end with a ';' delimiter."),
        new Constraint("4.2.2", "[75]", "4.2.2 [75] ExternalID ::= 'SYSTEM' S SystemLiteral | 'PUBLIC' S PubidLiteral S SystemLiteral",
                       "[75] ExternalID ::= 'SYSTEM' S SystemLiteral | 'PUBLIC' S PubidLiteral S SystemLiteral\n" +
                       "An external entity declaration must begin with either \"SYSTEM\" or \"PUBLIC\"."),
        new Constraint("2.8", "[40] [44]", "2.8 WFC: PEs in Internal Subset",
                       "Well-Formedness Constraint: PEs in Internal Subset\n" +
                       "[28] doctypedecl ::= '<!DOCTYPE' S Name (S ExternalID)? S? ('[' (markupdecl | PEReference | S)* ']' S?)? '>'\n" +
                       "In the internal DTD subset, parameter-entity references can occur only where\n" +
                       "markup declarations can occur, not within markup declarations."),
        new Constraint("2.8", "[22]", "2.8 [22] prolog ::= XMLDecl? Misc* (doctypedecl Misc*)?",
                       "[22] prolog ::= XMLDecl? Misc* (doctypedecl Misc*)?\n" +
                       "The prolog is required to contain legal XML characters."),
        new Constraint("2.8", "[27]", "2.8 [27] Misc ::= Comment | PI |  S",
                       "[27] Misc ::= Comment | PI |  S\n" +
                       "The markup after the end of the element content is required to contain legal XML characters."),
        new Constraint("2.7", "[20]", "2.7 [20] CData ::= (Char* - (Char* ']]>' Char*))",
                       "[20] CData ::= (Char* - (Char* ']]>' Char*))\n" +
                       "CDATA sections are required to contain legal XML characters."),
        new Constraint("3.1", "[43]", "3.1 [43] content ::= (element | CharData | Reference | CDSect | PI | Comment)*",
                       "[43] content ::= (element | CharData | Reference | CDSect | PI | Comment)*\n" +
                       "The content of elements is required to contain legal XML characters."),
        new Constraint("3", "[39]", "3 [39] element ::= EmptyElemTag | STag content ETag",
                       "[39] element ::= EmptyElemTag | STag content ETag\n" +
                       "The end of every element that begins with a start-tag must be marked by an\n" +
                       "end-tag containing a name that echoes the element's type as given in the\n" +
                       "start-tag."),
        new Constraint("3.1", "[42]", "3.1 [42] ETag ::= '</' Name S? '>'",
                       "[42] ETag ::= '</' Name S? '>'\n" +
                       "An end-tag must be terminated by a '>' delimiter."),
        new Constraint("4.3.3", "[81]", "4.3.3 [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*",
                       "[81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*\n" +
                       "It is a fatal error when an XML processor encounters an entity with an\n" +
                       "encoding that it is unable to process.\n"),
        new Constraint("4.1", "[68]", "4.1 WFC: Entity Declared",
                       "Well-Formedness Constraint: Entity Declared\n" +
                       "[68] EntityRef ::= '&' Name ';'\n" +
                       "In a document without any DTD, a document with only an internal DTD subset\n" +
                       "which contains no parameter entity references, or a document with\n" +
                       "\"standalone='yes'\", the Name given in the entity reference must match that\n" +
                       "in an entity declaration, except that well-formed documents need not declare\n" +
                       "any of the following entities: amp, lt, gt, apos, quot. The declaration of a\n" +
                       "parameter entity must precede any reference to it. Similarly, the\n" +
                       "declaration of a general entity must precede any reference to it which\n" +
                       "appears in a default value in an attribute-list declaration. Note that if\n" +
                       "entities are declared in the external subset or in external parameter\n" +
                       "entities, a non-validating processor is not obligated to read and process\n" +
                       "their declarations; for such documents, the rule that an entity must be\n" +
                       "declared is a well-formedness constraint only if standalone='yes'."),
        new Constraint("4.1", "[68]", "4.1 VC: Entity Declared",
                       "Validity Constraint: Entity Declared\n" +
                       "[68] EntityRef ::= '&' Name ';'\n" +
                       "In a document with an external subset or external parameter entities with\n" +
                       "\"standalone='no'\", the Name given in the entity reference must match that in\n" +
                       "an entity declaration. For interoperability, valid documents should declare\n" +
                       "the entities amp, lt, gt, apos, quot, in the form specified in\n" +
                       "\"4.6 Predefined Entities\". The declaration of a parameter entity must\n" +
                       "precede any reference to it. Similarly, the declaration of a general entity\n" +
                       "must precede any reference to it which appears in a default value in an\n" +
                       "attribute-list declaration."),
        new Constraint("4.1", "[68]", "4.1 WFC: Parsed Entity",
                       "Well-Formedness Constraint: Parsed Entity\n" +
                       "[68] EntityRef ::= '&' Name ';'\n" +
                       "An entity reference must not contain the name of an unparsed entity.\n" +
                       "Unparsed entities may be referred to only in attribute values declared to be\n" +
                       "of type ENTITY or ENTITIES."),
        new Constraint("3.1 2.3", "[41] [10]", "4.1 WFC: No External Entity References",
                       "Well-Formedness Constraint: No External Entity References\n" +
                       "[41] Attribute ::= Name Eq AttValue\n" +
                       "[10] AttValue ::= '\"' ([^<&\"] | Reference)* '\"' | \"'\" ([^<&'] | Reference)* \"'\"\n" +
                       "[68] EntityRef ::= '&' Name ';'\n" +
                       "Attribute values cannot contain direct or indirect entity references to\n" +
                       "external entities."),
        new Constraint("2.12", "[33]", "2.12 [33] LanguageID ::= Langcode ('-' Subcode)*",
                       "[33] LanguageID ::= Langcode ('-' Subcode)*\n" +
                       "[34] Langcode ::= ISO639Code |  IanaCode |  UserCode\n" +
                       "[35] ISO639Code ::= ([a-z] | [A-Z]) ([a-z] | [A-Z])\n" +
                       "[36] IanaCode ::= ('i' | 'I') '-' ([a-z] | [A-Z])+\n" +
                       "[37] UserCode ::= ('x' | 'X') '-' ([a-z] | [A-Z])+\n" +
                       "[38] Subcode ::= ([a-z] | [A-Z])+\n" +
                       "An xml:lang attribute value must match the LanguageID production."),
        new Constraint("2.7", "[18]", "2.7 [18] CDSect ::= CDStart CData CDEnd",
                       "[18] CDSect ::= CDStart CData CDEnd\n" +
                       "[19] CDStart ::= '<![CDATA['\n" +
                       "[20] CData ::= (Char* - (Char* ']]>' Char*))\n" +
                       "[21] CDEnd ::= ']]>'\n" +
                       "CDATA sections must be terminated by \"]]>\"."),
        new Constraint("3.2.2", "[51]", "3.2.2 VC: No Duplicate Types",
                       "Validity Constraint: No Duplicate Types\n" +
                       "[51] Mixed ::= '(' S? '#PCDATA' (S? '|' S? Name)* S? ')*' | '(' S? '#PCDATA' S? ')'\n" +
                       "The same name must not appear more than once in a single mixed-content\n" +
                       "declaration."),
        new Constraint("4.3.2", "[78]", "4.3.2 [78] extParsedEnt ::= TextDecl? content",
                       "[78] extParsedEnt ::= TextDecl? content\n" +
                       "An internal general parsed entity is well-formed if its replacement text\n" +
                       "matches the production labeled content.\n\n" +
                       "A consequence of well-formedness in entities is that the logical and\n" +
                       "physical structures in an XML document are properly nested; no start-tag,\n" +
                       "end-tag, empty-element tag, element, comment, processing instruction,\n" +
                       "character reference, or entity reference can begin in one entity and end in\n" +
                       "another."),
        new Constraint("3.3.1 3.3", "[54] [53]", "3.3.1 VC: ID Attribute Default",
                       "Validity Constraint: ID Attribute Default\n" +
                       "[53] AttDef ::= S Name S AttType S DefaultDecl\n" +
                       "[54] AttType ::= StringType | TokenizedType | EnumeratedType\n" +
                       "[60] DefaultDecl ::= '#REQUIRED' | '#IMPLIED' | (('#FIXED' S)? AttValue)\n" +
                       "An ID attribute must have a declared default of #IMPLIED or #REQUIRED."),
        new Constraint("3.3", "[53]", "3.3 [53] AttDef ::= S Name S AttType S DefaultDecl",
                       "[52] AttlistDecl ::= '<!ATTLIST' S Name AttDef* S? '>'\n" +
                       "[53] AttDef ::= S Name S AttType S DefaultDecl\n" +
                       "In an attribute-list declaration, the Name in the AttDef rule is the name\n" +
                       "of the attribute."),
        new Constraint("3.3", "[53]", "3.3 [53] AttDef ::= S Name S AttType S DefaultDecl",
                       "[52] AttlistDecl ::= '<!ATTLIST' S Name AttDef* S? '>'\n" +
                       "[53] AttDef ::= S Name S AttType S DefaultDecl\n" +
                       "."),
        new Constraint("4.3.3", "[81]", "4.3.3 [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*",
                       "[81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*\n" +
                       "Parsed entities which are stored in an encoding other than UTF-8 or\n" +
                       "UTF-16 must begin with an XML declaration or a text declaration that\n" +
                       "contains an encoding declaration."),
        new Constraint("4.1", "[68]", "4.1 WFC: No Recursion",
                       "Well-Formedness Constraint: No Recursion\n" +
                       "[68] EntityRef ::= '&' Name ';'\n" +
                       "[69] PEReference ::= '%' Name ';'\n" +
                       "A parsed entity must not contain a recursive reference to itself, either\n" +
                       "directly or indirectly."),
        new Constraint("2.8", "[29]", "2.8 VC: Proper Declaration/PE Nesting",
                       "Validity Constraint: Proper Declaration/PE Nesting\n" +
                       "[29] markupdecl ::= elementdecl | AttlistDecl | EntityDecl | NotationDecl | PI | Comment\n" +
                       "Parameter-entity replacement text must be properly nested with markup\n" +
                       "declarations. That is to say, if either the first character or the last\n" +
                       "character of a markup declaration (markupdecl above) is contained in the\n" +
                       "replacement text for a parameter-entity reference, both must be contained in\n" +
                       "the same replacement text."),
        new Constraint("3.2.1", "[47]", "3.2.1 VC: Proper Group/PE Nesting",
                       "Validity Constraint: Proper Group/PE Nesting\n" +
                       "[47] children ::= (choice | seq) ('?' | '*' | '+')?\n" +
                       "[48] cp ::= (Name | choice | seq) ('?' | '*' | '+')?\n" +
                       "[49] choice ::= '(' S? cp ( S? '|' S? cp )* S? ')'\n" +
                       "[50] seq ::= '(' S? cp ( S? ',' S? cp )* S? ')'\n" +
                       "[51] Mixed ::= '(' S? '#PCDATA' (S? '|' S? Name)* S? ')*' | '(' S? '#PCDATA' S? ')'\n" +
                       "Parameter-entity replacement text must be properly nested with parenthetized\n" +
                       "groups. That is to say, if either of the opening or closing parentheses in a\n" +
                       "choice, seq, or Mixed construct is contained in the replacement text for a\n" +
                       "parameter entity, both must be contained in the same replacement text."),
        new Constraint("3.3.1", "[56]", "3.3.1 VC: ID",
                       "Validity Constraint: ID\n" +
                       "[56] TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' | 'NMTOKEN' | 'NMTOKENS'\n" +
                       "Values of type ID must match the Name production. A name must not appear\n" +
                       "more than once in an XML document as a value of this type; i.e., ID values\n" +
                       "must uniquely identify the elements which bear them."),
        new Constraint("3.3.1", "[56]", "3.3.1 VC: Entity Name",
                       "Validity Constraint: Entity Name\n" +
                       "[56] TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' | 'NMTOKEN' | 'NMTOKENS'\n" +
                       "Values of type ENTITY must match the Name production, values of type\n" +
                       "ENTITIES must match Names; each Name must match the name of an unparsed\n" +
                       "entity declared in the DTD."),
        new Constraint("3.1", "[41]", "3.1 VC: Attribute Value Type",
                       "Validity Constraint: Attribute Value Type\n" +
                       "[41] Attribute ::= Name Eq AttValue\n" +
                       "The attribute must have been declared; the value must be of the type\n" +
                       "declared for it."),
        new Constraint("3", "[39]", "3 VC: Element Valid",
                       "Validity Constraint: Element Valid\n" +
                       "[39] element ::= EmptyElemTag | STag content ETag\n" +
                       "An element is valid if there is a declaration matching elementdecl where the\n" +
                       "Name matches the element type, and one of the following holds:\n\n" +
                       "  1. The declaration matches EMPTY and the element has no content.\n" +
                       "  2. The declaration matches children and the sequence of child elements\n" +
                       "     belongs to the language generated by the regular expression in the\n" +
                       "     content model, with optional white space (characters matching the\n" +
                       "     nonterminal S) between each pair of child elements.\n" +
                       "  3. The declaration matches Mixed and the content consists of character\n" +
                       "     data and child elements whose types match names in the content model.\n" +
                       "  4. The declaration matches ANY, and the types of any child elements have\n" +
                       "     been declared."),
        new Constraint("2.9", "[32]", "2.9 VC: Standalone Document Declaration",
                       "Validity Constraint: Standalone Document Declaration\n" +
                       "[32] SDDecl ::= S 'standalone' Eq ((\"'\" ('yes' | 'no') \"'\") | ('\"' ('yes' | 'no') '\"'))\n" +
                       "The standalone document declaration must have the value \"no\" if any external\n" +
                       "markup declarations contain declarations of:\n\n" +
                       "   * attributes with default values, if elements to which these attributes\n" +
                       "     apply appear in the document without specifications of values for these\n" +
                       "     attributes, or\n" +
                       "   * entities (other than amp, lt, gt, apos, quot), if references to those\n" +
                       "     entities appear in the document, or\n" +
                       "   * attributes with values subject to normalization, where the attribute\n" +
                       "     appears in the document with a value which will change as a result of\n" +
                       "     normalization, or\n" +
                       "   * element types with element content, if white space occurs directly\n" +
                       "     within any instance of those types."),
        new Constraint("3.3.1", "[56]", "3.3.1 VC: One ID per Element Type",
                       "Validity Constraint: One ID per Element Type\n" +
                       "[56] TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' | 'NMTOKEN' | 'NMTOKENS'\n" +
                       "No element type may have more than one ID attribute specified."),
        new Constraint("3.2", "[45]", "3.2 VC: Unique Element Type Declaration",
                       "Validity Constraint: Unique Element Type Declaration\n" +
                       "[45] elementdecl ::= '<!ELEMENT' S Name S contentspec S? '>'\n" +
                       "No element type may be declared more than once."),
        new Constraint("3.2", "[45]", "3.2 [45] elementdecl ::= '<!ELEMENT' S Name S contentspec S? '>'",
                       "[45] elementdecl ::= '<!ELEMENT' S Name S contentspec S? '>'\n" +
                       "At user option, an XML processor may issue a warning when a declaration\n" +
                       "mentions an element type for which no declaration is provided, but this\n" +
                       "is not an error."),

        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("?.?", "[??]", "", ""),
        new Constraint("3.3.1", "[58]", "VC: One Notation per Element Type",
                       "Validity Constraint: One Notation per Element Type\n" +
                       "[58] NotationType ::= 'NOTATION' S '(' S? Name (S? '|' S? Name)* S? ')'\n" +
                       "No element type may have more than one NOTATION attribute specified."),
        new Constraint("4.", "", "NC: Prefix Declared", ""),

        new Constraint(null, null, null, null)
    };
 */
}

/*
class Constraint {
    String sections;
    String productions;
    String shortDesc;
    String longDesc;
    Constraint(String sections, String productions, String shortDesc, String longDesc) {
        this.sections = sections;
        this.productions = productions;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }
}
 */
