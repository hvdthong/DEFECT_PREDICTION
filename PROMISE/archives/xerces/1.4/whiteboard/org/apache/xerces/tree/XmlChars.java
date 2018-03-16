package org.apache.xerces.tree;


/**
 * Methods in this class are used to determine whether characters may
 * appear in certain roles in XML documents.  Such methods are used
 * both to parse and to create such documents.
 *
 * @version 1.8
 * @author David Brownell
 */
public class XmlChars
{
    private XmlChars () { }

    /**
     * Returns true if the argument, a UCS-4 character code, is valid in
     * XML documents.  Unicode characters fit into the low sixteen
     * bits of a UCS-4 character, and pairs of Unicode <em>surrogate
     * characters</em> can be combined to encode UCS-4 characters in
     * documents containing only Unicode.  (The <code>char</code> datatype
     * in the Java Programming Language represents Unicode characters,
     * including unpaired surrogates.)
     *
     * <P> In XML, UCS-4 characters can also be encoded by the use of
     * <em>character references</em> such as <b>&amp;#x12345678;</b>, which
     * happens to refer to a character that is disallowed in XML documents.
     * UCS-4 characters allowed in XML documents can be expressed with
     * one or two Unicode characters.
     *
     * @param ucs4char The 32-bit UCS-4 character being tested.
     */
    static public boolean isChar (int ucs4char)
    {
	return ((ucs4char >= 0x0020 && ucs4char <= 0xD7FF)
		|| ucs4char == 0x000A || ucs4char == 0x0009
		|| ucs4char == 0x000D
		|| (ucs4char >= 0xE000 && ucs4char <= 0xFFFD)
		|| (ucs4char >= 0x10000 && ucs4char <= 0x10ffff));
    }

    /**
     * Returns true if the character is allowed to be a non-initial
     * character in names according to the XML recommendation.
     * @see #isNCNameChar
     * @see #isLetter
     */
    public static boolean isNameChar (char c)
    {

	if (isLetter2 (c))
	    return true;
	else if (c == '>')
	    return false;
	else if (c == '.' || c == '-' || c == '_' || c == ':'
		|| isExtender (c))
	    return true;
	else
	    return false;
    }

    /**
     * Returns true if the character is allowed to be a non-initial
     * character in unscoped names according to the rules of the XML
     * Namespaces proposed recommendation.  Except for precluding
     * the colon (used to separate names from their scopes) these
     * characters are just as allowed by the XML recommendation.
     * @see #isNameChar
     * @see #isLetter
     */
    public static boolean isNCNameChar (char c)
    {
	return c != ':' && isNameChar (c);
    }

    /**
     * Returns true if the character is allowed where XML supports
     * whitespace characters, false otherwise.
     */
    public static boolean isSpace (char c)
    {
	return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }


    /*
     * NOTE:  java.lang.Character.getType() values are:
     *
     * UNASSIGNED                    = 0,
     *
     */

    /**
     * Returns true if the character is an XML "letter".  XML Names must
     * start with Letters or a few other characters, but other characters
     * in names must only satisfy the <em>isNameChar</em> predicate.
     *
     * @see #isNameChar
     * @see #isNCNameChar
     */
    public static boolean isLetter (char c)
    {

	if (c >= 'a' && c <= 'z')
	    return true;
	if (c == '/')
	    return false;
	if (c >= 'A' && c <= 'Z')
	    return true;

	switch (Character.getType (c)) {

	    return !isCompatibilityChar (c)
		&& !(c >= 0x20dd && c <= 0x20e0);

	  default:
	    return ((c >= 0x02bb && c <=  0x02c1)
		    || c == 0x0559 || c == 0x06e5 || c == 0x06e6);
	}
    }

    private static boolean isCompatibilityChar (char c)
    {

	switch ((c >> 8) & 0x0ff) {
	  case 0x00:
	    return c == 0x00aa || c == 0x00b5 || c == 0x00ba;

	  case 0x01:
	    return (c >= 0x0132 && c <= 0x0133)
		|| (c >= 0x013f && c <= 0x0140)
		|| c == 0x0149
		|| c == 0x017f
		|| (c >= 0x01c4 && c <= 0x01cc)
		|| (c >= 0x01f1 && c <= 0x01f3) ;

	  case 0x02:
	    return (c >= 0x02b0 && c <= 0x02b8)
		|| (c >= 0x02e0 && c <= 0x02e4);
	  
	  case 0x03:

	  case 0x05:

	  case 0x0e:

	  case 0x11:
	    return c == 0x1101
		|| c == 0x1104
		|| c == 0x1108
		|| c == 0x110a
		|| c == 0x110d
		|| (c >= 0x1113 && c <= 0x113b)
		|| c == 0x113d
		|| c == 0x113f
		|| (c >= 0x1141 && c <= 0x114b)
		|| c == 0x114d
		|| c == 0x114f
		|| (c >= 0x1151 && c <= 0x1153)
		|| (c >= 0x1156 && c <= 0x1158)
		|| c == 0x1162
		|| c == 0x1164
		|| c == 0x1166
		|| c == 0x1168
		|| (c >= 0x116a && c <= 0x116c)
		|| (c >= 0x116f && c <= 0x1171)
		|| c == 0x1174
		|| (c >= 0x1176 && c <= 0x119d)
		|| (c >= 0x119f && c <= 0x11a2)
		|| (c >= 0x11a9 && c <= 0x11aa)
		|| (c >= 0x11ac && c <= 0x11ad)
		|| (c >= 0x11b0 && c <= 0x11b6)
		|| c == 0x11b9
		|| c == 0x11bb
		|| (c >= 0x11c3 && c <= 0x11ea)
		|| (c >= 0x11ec && c <= 0x11ef)
		|| (c >= 0x11f1 && c <= 0x11f8)
		;

	  case 0x20:

	  case 0x21:
	    return
		   c == 0x2102
		|| c == 0x2107
		|| (c >= 0x210a && c <= 0x2113)
		|| c == 0x2115
		|| (c >= 0x2118 && c <= 0x211d)
		|| c == 0x2124
		|| c == 0x2128
		|| (c >= 0x212c && c <= 0x212d)
		|| (c >= 0x212f && c <= 0x2138)

		|| (c >= 0x2160 && c <= 0x217f)
		;

	  case 0x30:
	    return c >= 0x309b && c <= 0x309c;

	  case 0x31:
	    return c >= 0x3131 && c <= 0x318e;

	  case 0xf9:
	  case 0xfa:
	  case 0xfb:
	  case 0xfc:
	  case 0xfd:
	  case 0xfe:
	  case 0xff:
	    return true;
	
	  default:
	    return false;
	}
    }

    private static boolean isLetter2 (char c)
    {

	if (c >= 'a' && c <= 'z')
	    return true;
	if (c == '>')
	    return false;
	if (c >= 'A' && c <= 'Z')
	    return true;

	switch (Character.getType (c)) {

	    return !isCompatibilityChar (c)
		&& !(c >= 0x20dd && c <= 0x20e0);

	  default:
	    return c == 0x0387;
	}
    }

    private static boolean isDigit (char c)
    {

	return Character.isDigit (c)
		&& ! ( (c >= 0xff10) && (c <= 0xff19));
    }

    private static boolean isExtender (char c)
    {
	return c == 0x00b7 || c == 0x02d0 || c == 0x02d1 || c == 0x0387
		|| c == 0x0640 || c == 0x0e46 || c == 0x0ec6
		|| c == 0x3005 || (c >= 0x3031 && c <= 0x3035)
		|| (c >= 0x309d && c <= 0x309e)
		|| (c >= 0x30fc && c <= 0x30fe)
		;
    }
}
