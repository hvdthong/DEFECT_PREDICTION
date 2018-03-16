package org.apache.xml.utils;

import java.util.Arrays;


/**
 * THIS IS A COPY OF THE XERCES-2J CLASS org.apache.xerces.utls.XMLChar
 *  
 * This class defines the basic properties of characters in XML 1.1. The data
 * in this class can be used to verify that a character is a valid
 * XML 1.1 character or if the character is a space, name start, or name
 * character.
 * <p>
 * A series of convenience methods are supplied to ease the burden
 * of the developer.  Using the character as an index into the <code>XML11CHARS</code>
 * array and applying the appropriate mask flag (e.g.
 * <code>MASK_VALID</code>), yields the same results as calling the
 * convenience methods. There is one exception: check the comments
 * for the <code>isValid</code> method for details.
 *
 * @version $Id: XML11Char.java 338137 2005-03-23 17:54:05Z ytalwar $
 */
public class XML11Char {


    /** Character flags for XML 1.1. */
    private static final byte XML11CHARS [] = new byte [1 << 16];

    /** XML 1.1 Valid character mask. */
    public static final int MASK_XML11_VALID = 0x01;

    /** XML 1.1 Space character mask. */
    public static final int MASK_XML11_SPACE = 0x02;

    /** XML 1.1 Name start character mask. */
    public static final int MASK_XML11_NAME_START = 0x04;

    /** XML 1.1 Name character mask. */
    public static final int MASK_XML11_NAME = 0x08;

    /** XML 1.1 control character mask */
    public static final int MASK_XML11_CONTROL = 0x10;

    /** XML 1.1 content for external entities (valid - "special" chars - control chars) */
    public static final int MASK_XML11_CONTENT = 0x20;

    /** XML namespaces 1.1 NCNameStart */
    public static final int MASK_XML11_NCNAME_START = 0x40;

    /** XML namespaces 1.1 NCName */
    public static final int MASK_XML11_NCNAME = 0x80;
    
    /** XML 1.1 content for internal entities (valid - "special" chars) */
    public static final int MASK_XML11_CONTENT_INTERNAL = MASK_XML11_CONTROL | MASK_XML11_CONTENT; 


    static {
    	
        
        XML11CHARS[9] = 35;
        XML11CHARS[10] = 3;
        XML11CHARS[13] = 3;
        XML11CHARS[32] = 35;
        XML11CHARS[38] = 1;
        XML11CHARS[47] = 33;
        XML11CHARS[58] = 45;
        XML11CHARS[59] = 33;
        XML11CHARS[60] = 1;
        XML11CHARS[93] = 1;
        XML11CHARS[94] = 33;
        XML11CHARS[95] = -19;
        XML11CHARS[96] = 33;
        XML11CHARS[133] = 35;
        XML11CHARS[183] = -87;
        XML11CHARS[215] = 33;
        XML11CHARS[247] = 33;
        XML11CHARS[894] = 33;
        XML11CHARS[8232] = 35;



    /**
     * Returns true if the specified character is a space character
     * as amdended in the XML 1.1 specification.
     *
     * @param c The character to check.
     */
    public static boolean isXML11Space(int c) {
        return (c < 0x10000 && (XML11CHARS[c] & MASK_XML11_SPACE) != 0);

    /**
     * Returns true if the specified character is valid. This method
     * also checks the surrogate character range from 0x10000 to 0x10FFFF.
     * <p>
     * If the program chooses to apply the mask directly to the
     * <code>XML11CHARS</code> array, then they are responsible for checking
     * the surrogate character range.
     *
     * @param c The character to check.
     */
    public static boolean isXML11Valid(int c) {
        return (c < 0x10000 && (XML11CHARS[c] & MASK_XML11_VALID) != 0) 
                || (0x10000 <= c && c <= 0x10FFFF);

    /**
     * Returns true if the specified character is invalid.
     *
     * @param c The character to check.
     */
    public static boolean isXML11Invalid(int c) {
        return !isXML11Valid(c);

    /**
     * Returns true if the specified character is valid and permitted outside
     * of a character reference.  
     * That is, this method will return false for the same set as
     * isXML11Valid, except it also reports false for "control characters".
     *
     * @param c The character to check.
     */
    public static boolean isXML11ValidLiteral(int c) {
        return ((c < 0x10000 && ((XML11CHARS[c] & MASK_XML11_VALID) != 0 && (XML11CHARS[c] & MASK_XML11_CONTROL) == 0))
            || (0x10000 <= c && c <= 0x10FFFF)); 

    /**
     * Returns true if the specified character can be considered 
     * content in an external parsed entity.
     *
     * @param c The character to check.
     */
    public static boolean isXML11Content(int c) {
        return (c < 0x10000 && (XML11CHARS[c] & MASK_XML11_CONTENT) != 0) ||
               (0x10000 <= c && c <= 0x10FFFF);
    
    /**
     * Returns true if the specified character can be considered 
     * content in an internal parsed entity.
     *
     * @param c The character to check.
     */
    public static boolean isXML11InternalEntityContent(int c) {
        return (c < 0x10000 && (XML11CHARS[c] & MASK_XML11_CONTENT_INTERNAL) != 0) ||
               (0x10000 <= c && c <= 0x10FFFF);

    /**
     * Returns true if the specified character is a valid name start
     * character as defined by production [4] in the XML 1.1
     * specification.
     *
     * @param c The character to check.
     */
    public static boolean isXML11NameStart(int c) {
        return (c < 0x10000 && (XML11CHARS[c] & MASK_XML11_NAME_START) != 0)
            || (0x10000 <= c && c < 0xF0000);

    /**
     * Returns true if the specified character is a valid name
     * character as defined by production [4a] in the XML 1.1
     * specification.
     *
     * @param c The character to check.
     */
    public static boolean isXML11Name(int c) {
        return (c < 0x10000 && (XML11CHARS[c] & MASK_XML11_NAME) != 0) 
            || (c >= 0x10000 && c < 0xF0000);

    /**
     * Returns true if the specified character is a valid NCName start
     * character as defined by production [4] in Namespaces in XML
     * 1.1 recommendation.
     *
     * @param c The character to check.
     */
    public static boolean isXML11NCNameStart(int c) {
        return (c < 0x10000 && (XML11CHARS[c] & MASK_XML11_NCNAME_START) != 0)
            || (0x10000 <= c && c < 0xF0000);

    /**
     * Returns true if the specified character is a valid NCName
     * character as defined by production [5] in Namespaces in XML
     * 1.1 recommendation.
     *
     * @param c The character to check.
     */
    public static boolean isXML11NCName(int c) {
        return (c < 0x10000 && (XML11CHARS[c] & MASK_XML11_NCNAME) != 0)
            || (0x10000 <= c && c < 0xF0000);
    
    /**
     * Returns whether the given character is a valid 
     * high surrogate for a name character. This includes
     * all high surrogates for characters [0x10000-0xEFFFF].
     * In other words everything excluding planes 15 and 16.
     *
     * @param c The character to check.
     */
    public static boolean isXML11NameHighSurrogate(int c) {
        return (0xD800 <= c && c <= 0xDB7F);
    }

    /*
     * [5] Name ::= NameStartChar NameChar*
     */
    /**
     * Check to see if a string is a valid Name according to [5]
     * in the XML 1.1 Recommendation
     *
     * @param name string to check
     * @return true if name is a valid Name
     */
    public static boolean isXML11ValidName(String name) {
        int length = name.length();
        if (length == 0)
            return false;
        int i = 1;
        char ch = name.charAt(0);
        if( !isXML11NameStart(ch) ) {
            if ( length > 1 && isXML11NameHighSurrogate(ch) ) {
                char ch2 = name.charAt(1);
                if ( !XMLChar.isLowSurrogate(ch2) || 
                     !isXML11NameStart(XMLChar.supplemental(ch, ch2)) ) {
                    return false;
                }
                i = 2;
            }
            else {
                return false;
            }
        }
        while (i < length) {
            ch = name.charAt(i);
            if ( !isXML11Name(ch) ) {
                if ( ++i < length && isXML11NameHighSurrogate(ch) ) {
                    char ch2 = name.charAt(i);
                    if ( !XMLChar.isLowSurrogate(ch2) || 
                         !isXML11Name(XMLChar.supplemental(ch, ch2)) ) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            ++i;
        }
        return true;
    

    /*
     * from the namespace 1.1 rec
     * [4] NCName ::= NCNameStartChar NCNameChar*
     */
    /**
     * Check to see if a string is a valid NCName according to [4]
     * from the XML Namespaces 1.1 Recommendation
     *
     * @param ncName string to check
     * @return true if name is a valid NCName
     */
    public static boolean isXML11ValidNCName(String ncName) {
        int length = ncName.length();
        if (length == 0)
            return false;
        int i = 1;
        char ch = ncName.charAt(0);
        if( !isXML11NCNameStart(ch) ) {
            if ( length > 1 && isXML11NameHighSurrogate(ch) ) {
                char ch2 = ncName.charAt(1);
                if ( !XMLChar.isLowSurrogate(ch2) || 
                     !isXML11NCNameStart(XMLChar.supplemental(ch, ch2)) ) {
                    return false;
                }
                i = 2;
            }
            else {
                return false;
            }
        }
        while (i < length) {
            ch = ncName.charAt(i);
            if ( !isXML11NCName(ch) ) {
                if ( ++i < length && isXML11NameHighSurrogate(ch) ) {
                    char ch2 = ncName.charAt(i);
                    if ( !XMLChar.isLowSurrogate(ch2) || 
                         !isXML11NCName(XMLChar.supplemental(ch, ch2)) ) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            ++i;
        }
        return true;

    /*
     * [7] Nmtoken ::= (NameChar)+
     */
    /**
     * Check to see if a string is a valid Nmtoken according to [7]
     * in the XML 1.1 Recommendation
     *
     * @param nmtoken string to check
     * @return true if nmtoken is a valid Nmtoken 
     */
    public static boolean isXML11ValidNmtoken(String nmtoken) {
        int length = nmtoken.length();
        if (length == 0)
            return false;
        for (int i = 0; i < length; ++i ) {
            char ch = nmtoken.charAt(i);
            if( !isXML11Name(ch) ) {
                if ( ++i < length && isXML11NameHighSurrogate(ch) ) {
                    char ch2 = nmtoken.charAt(i);
                    if ( !XMLChar.isLowSurrogate(ch2) || 
                         !isXML11Name(XMLChar.supplemental(ch, ch2)) ) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        return true;
    
    /**
      * Simple check to determine if qname is legal. If it returns false
      * then <param>str</param> is illegal; if it returns true then 
      * <param>str</param> is legal.
      */
     public static boolean isXML11ValidQName(String str) {

        final int colon = str.indexOf(':');

        if (colon == 0 || colon == str.length() - 1) {
            return false;
        }
       
        if (colon > 0) {
            final String prefix = str.substring(0,colon);
            final String localPart = str.substring(colon+1);
            return isXML11ValidNCName(prefix) && isXML11ValidNCName(localPart);
        }
        else {
            return isXML11ValidNCName(str);
        }
     }


