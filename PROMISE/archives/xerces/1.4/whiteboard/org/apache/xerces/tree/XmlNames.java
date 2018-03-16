package org.apache.xerces.tree;

/**
 * This class contains static methods used to determine whether identifiers
 * may appear in certain roles in XML documents.  Such methods are used
 * both to parse and to create such documents.
 *
 * @version 1.4
 * @author David Brownell
 */
public class XmlNames 
{
    private XmlNames () { }


    /**
     * Returns true if the value is a legal XML name.
     *
     * @param value the string being tested
     */
    public static boolean isName (String value)
    {
	if (value == null)
	    return false;

	char c = value.charAt (0);
	if (!XmlChars.isLetter (c) && c != '_' && c != ':')
	    return false;
	for (int i = 1; i < value.length (); i++)
	    if (!XmlChars.isNameChar (value.charAt (i)))
		return false;
	return true;
    }

    /**
     * Returns true if the value is a legal "unqualified" XML name, as
     * defined in the XML Namespaces proposed recommendation.
     * These are normal XML names, except that they may not contain
     * a "colon" character.
     *
     * @param value the string being tested
     */
    public static boolean isUnqualifiedName (String value)
    {
	if (value == null || value.length() == 0)
	    return false;

	char c = value.charAt (0);
	if (!XmlChars.isLetter (c) && c != '_')
	    return false;
	for (int i = 1; i < value.length (); i++)
	    if (!XmlChars.isNCNameChar (value.charAt (i)))
		return false;
	return true;
    }

    /**
     * Returns true if the value is a legal "qualified" XML name, as defined
     * in the XML Namespaces proposed recommendation.  Qualified names are
     * composed of an optional prefix (an unqualified name), followed by a
     * colon, and a required "local part" (an unqualified name).  Prefixes are
     * declared, and correspond to particular URIs which scope the "local
     * part" of the name.  (This method cannot check whether the prefix of a
     * name has been declared.)
     *
     * @param value the string being tested
     */
    public static boolean isQualifiedName (String value)
    {
	if (value == null)
	    return false;


	int	first = value.indexOf (':');

        if (first <= 0)
            return isUnqualifiedName (value);


	int	last = value.lastIndexOf (':');
	if (last != first)
	    return false;
	
	return isUnqualifiedName (value.substring (0, first))
		&& isUnqualifiedName (value.substring (first + 1));
    }

    /**
     * This method returns true if the identifier is a "name token"
     * as defined in the XML specification.  Like names, these
     * may only contain "name characters"; however, they do not need
     * to have letters as their initial characters.  Attribute values
     * defined to be of type NMTOKEN(S) must satisfy this predicate.
     *
     * @param token the string being tested
     */
    public static boolean isNmtoken (String token)
    {
	int	length = token.length ();

	for (int i = 0; i < length; i++)
	    if (!XmlChars.isNameChar (token.charAt (i)))
		return false;
	return true;
    }


    /**
     * This method returns true if the identifier is a "name token" as
     * defined by the XML Namespaces proposed recommendation.
     * These are like XML "name tokens" but they may not contain the
     * "colon" character.
     *
     * @see #isNmtoken
     *
     * @param token the string being tested
     */
    public static boolean isNCNmtoken (String token)
    {
	return isNmtoken (token) && token.indexOf (':') < 0;
    }

    /**
     * Return the Prefix of qualifiedName.  Does not check that Prefix is a
     * valid NCName.
     *
     * @param qualifiedName name to find the Prefix of
     * @return prefix or null if it has none
     */
    public static String getPrefix(String qualifiedName) {
        int index = qualifiedName.indexOf(':');
        return index <= 0 ? null : qualifiedName.substring(0, index);
    }

    /**
     * Return the LocalPart of qualifiedName.  Does not check that Prefix is a
     * valid NCName.
     *
     * @param qualifiedName name to find the LocalPart of
     * @return LocalPart or null if it has none
     */
    public static String getLocalPart(String qualifiedName) {
	int index = qualifiedName.indexOf(':');
	if (index < 0) {
	    return qualifiedName;
        }

        if (index == qualifiedName.length() - 1) {
            return null;
        }

	return qualifiedName.substring(index + 1);
    }
}
