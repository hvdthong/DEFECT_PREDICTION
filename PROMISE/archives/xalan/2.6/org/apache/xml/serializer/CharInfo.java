package org.apache.xml.serializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.xml.transform.TransformerException;

import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.CharKey;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;

/**
 * This class provides services that tell if a character should have
 * special treatement, such as entity reference substitution or normalization
 * of a newline character.  It also provides character to entity reference
 * lookup.
 *
 * DEVELOPERS: See Known Issue in the constructor.
 * 
 * @xsl.usage internal
 */
class CharInfo
{
    /** Lookup table for characters to entity references. */
    private Hashtable m_charToEntityRef = new Hashtable();

    /**
     * The name of the HTML entities file.
     * If specified, the file will be resource loaded with the default class loader.
     */
    public static String HTML_ENTITIES_RESOURCE = "org.apache.xml.serializer.HTMLEntities";

    /**
     * The name of the XML entities file.
     * If specified, the file will be resource loaded with the default class loader.
     */
    public static String XML_ENTITIES_RESOURCE = "org.apache.xml.serializer.XMLEntities";

    /** The horizontal tab character, which the parser should always normalize. */
    public static final char S_HORIZONAL_TAB = 0x09;

    /** The linefeed character, which the parser should always normalize. */
    public static final char S_LINEFEED = 0x0A;

    /** The carriage return character, which the parser should always normalize. */
    public static char S_CARRIAGERETURN = 0x0D;
    
    /** This flag is an optimization for HTML entities. It false if entities 
     * other than quot (34), amp (38), lt (60) and gt (62) are defined
     * in the range 0 to 127.
     * @xsl.usage internal
     */    
    final boolean onlyQuotAmpLtGt;
    
    /** Copy the first 0,1 ... ASCII_MAX values into an array */
    private static final int ASCII_MAX = 128;
    
    /** Array of values is faster access than a set of bits 
     * to quickly check ASCII characters in attribute values. 
     */
    private boolean[] isSpecialAttrASCII = new boolean[ASCII_MAX];
    
    /** Array of values is faster access than a set of bits 
     * to quickly check ASCII characters in text nodes. 
     */
    private boolean[] isSpecialTextASCII = new boolean[ASCII_MAX];

    private boolean[] isCleanTextASCII = new boolean[ASCII_MAX];

    /** An array of bits to record if the character is in the set.
     * Although information in this array is complete, the
     * isSpecialAttrASCII array is used first because access to its values
     * is common and faster.
     */   
    private int array_of_bits[] = createEmptySetOfIntegers(65535);
     
    
    /*
     * This constant is used to shift an integer to quickly
     * calculate which element its bit is stored in.
     * 5 for 32 bit words (int) ,  6 for 64 bit words (long)
     */
    private static final int SHIFT_PER_WORD = 5;
    
    /*
     * A mask to get the low order bits which are used to
     * calculate the value of the bit within a given word,
     * that will represent the presence of the integer in the 
     * set.
     * 
     * 0x1F for 32 bit words (int),
     * or 0x3F for 64 bit words (long) 
     */
    private static final int LOW_ORDER_BITMASK = 0x1f;
    
    /*
     * This is used for optimizing the lookup of bits representing
     * the integers in the set. It is the index of the first element
     * in the array array_of_bits[] that is not used.
     */
    private int firstWordNotUsed;


    /**
     * Constructor that reads in a resource file that describes the mapping of
     * characters to entity references.
     * This constructor is private, just to force the use
     * of the getCharInfo(entitiesResource) factory
     *
     * Resource files must be encoded in UTF-8 and can either be properties
     * files with a .properties extension assumed.  Alternatively, they can
     * have the following form, with no particular extension assumed:
     *
     * <pre>
     * # First char # is a comment
     * Entity numericValue
     * quot 34
     * amp 38
     * </pre>
     *    
     * @param entitiesResource Name of properties or resource file that should
     * be loaded, which describes that mapping of characters to entity
     * references.
     */
    private CharInfo(String entitiesResource, String method)
    {
        this(entitiesResource, method, false);
    }

    private CharInfo(String entitiesResource, String method, boolean internal)
    {
        ResourceBundle entities = null;
        boolean noExtraEntities = true;


        if (internal) { 
            try {
                entities = PropertyResourceBundle.getBundle(entitiesResource);
            } catch (Exception e) {}
        }

        if (entities != null) {
            Enumeration keys = entities.getKeys();
            while (keys.hasMoreElements()){
                String name = (String) keys.nextElement();
                String value = entities.getString(name);
                int code = Integer.parseInt(value);
                defineEntity(name, (char) code);
                if (extraEntity(code))
                    noExtraEntities = false;
            }
            set(S_LINEFEED);
            set(S_CARRIAGERETURN);
        } else {
            InputStream is = null;

            try {
                if (internal) {
                    is = CharInfo.class.getResourceAsStream(entitiesResource);
                } else {
                    ClassLoader cl = ObjectFactory.findClassLoader();
                    if (cl == null) {
                        is = ClassLoader.getSystemResourceAsStream(entitiesResource);
                    } else {
                        is = cl.getResourceAsStream(entitiesResource);
                    }

                    if (is == null) {
                        try {
                            URL url = new URL(entitiesResource);
                            is = url.openStream();
                        } catch (Exception e) {}
                    }
                }

                if (is == null) {
                    throw new RuntimeException(
                        XMLMessages.createXMLMessage(
                            XMLErrorResources.ER_RESOURCE_COULD_NOT_FIND,
                            new Object[] {entitiesResource, entitiesResource}));
                }


                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    reader = new BufferedReader(new InputStreamReader(is));
                }

                String line = reader.readLine();

                while (line != null) {
                    if (line.length() == 0 || line.charAt(0) == '#') {
                        line = reader.readLine();

                        continue;
                    }

                    int index = line.indexOf(' ');

                    if (index > 1) {
                        String name = line.substring(0, index);

                        ++index;

                        if (index < line.length()) {
                            String value = line.substring(index);
                            index = value.indexOf(' ');

                            if (index > 0) {
                                value = value.substring(0, index);
                            }

                            int code = Integer.parseInt(value);

                            defineEntity(name, (char) code);
                            if (extraEntity(code))
                                noExtraEntities = false;
                        }
                    }

                    line = reader.readLine();
                }

                is.close();
                set(S_LINEFEED);
                set(S_CARRIAGERETURN);
            } catch (Exception e) {
                throw new RuntimeException(
                    XMLMessages.createXMLMessage(
                        XMLErrorResources.ER_RESOURCE_COULD_NOT_LOAD,
                        new Object[] { entitiesResource,
                                       e.toString(),
                                       entitiesResource,
                                       e.toString()}));
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception except) {}
                }
            }
        }
          
        /* initialize the array isCleanTextASCII[] with a cache of values
         * for use by ToStream.character(char[], int , int)
         * and the array isSpecialTextASCII[] with the opposite values
         * (all in the name of performance!)
         */
        for (int ch = 0; ch <ASCII_MAX; ch++)
        if((((0x20 <= ch || (0x0A == ch || 0x0D == ch || 0x09 == ch)))
             && (!get(ch))) || ('"' == ch))
        {
            isCleanTextASCII[ch] = true;
            isSpecialTextASCII[ch] = false;
        }
        else {
            isCleanTextASCII[ch] = false;
            isSpecialTextASCII[ch] = true;     
        }       
        
        /* Now that we've used get(ch) just above to initialize the
         * two arrays we will change by adding a tab to the set of 
         * special chars for XML (but not HTML!).
         * We do this because a tab is always a
         * special character in an XML attribute, 
         * but only a special character in XML text 
         * if it has an entity defined for it.
         * This is the reason for this delay.
         */
        if (Method.XML.equals(method)) 
        {
            set(S_HORIZONAL_TAB);
        }
        

        onlyQuotAmpLtGt = noExtraEntities;

        for (int i=0; i<ASCII_MAX; i++)
            isSpecialAttrASCII[i] = get(i);    

    }

    /**
     * Defines a new character reference. The reference's name and value are
     * supplied. Nothing happens if the character reference is already defined.
     * <p>Unlike internal entities, character references are a string to single
     * character mapping. They are used to map non-ASCII characters both on
     * parsing and printing, primarily for HTML documents. '&lt;amp;' is an
     * example of a character reference.</p>
     *
     * @param name The entity's name
     * @param value The entity's value
     */
    private void defineEntity(String name, char value)
    {
        CharKey character = new CharKey(value);

        m_charToEntityRef.put(character, name);
        set(value);
    }

    private CharKey m_charKey = new CharKey();

    /**
     * Resolve a character to an entity reference name.
     *
     * This is reusing a stored key object, in an effort to avoid
     * heap activity. Unfortunately, that introduces a threading risk.
     * Simplest fix for now is to make it a synchronized method, or to give
     * up the reuse; I see very little performance difference between them.
     * Long-term solution would be to replace the hashtable with a sparse array
     * keyed directly from the character's integer value; see DTM's
     * string pool for a related solution.
     *
     * @param value character value that should be resolved to a name.
     *
     * @return name of character entity, or null if not found.
     * @xsl.usage internal
     */
    synchronized public String getEntityNameForChar(char value)
    {
        m_charKey.setChar(value);
        return (String) m_charToEntityRef.get(m_charKey);
    }
    
    /**
     * Tell if the character argument that is from
     * an attribute value should have special treatment.
     * 
     * @param value the value of a character that is in an attribute value
     * @return true if the character should have any special treatment, 
     * such as when writing out attribute values, 
     * or entity references.
     * @xsl.usage internal
     */
    public final boolean isSpecialAttrChar(int value)
    {

        if (value < ASCII_MAX)
            return isSpecialAttrASCII[value];

        return get(value);
    }    

    /**
     * Tell if the character argument that is from a 
     * text node should have special treatment.
     * 
     * @param value the value of a character that is in a text node
     * @return true if the character should have any special treatment, 
     * such as when writing out attribute values, 
     * or entity references.
     * @xsl.usage internal
     */
    public final boolean isSpecialTextChar(int value)
    {

        if (value < ASCII_MAX)
            return isSpecialTextASCII[value];

        return get(value);
    }
    
    /**
     * This method is used to determine if an ASCII character in
     * a text node (not an attribute value) is "clean".
     * @param value the character to check (0 to 127).
     * @return true if the character can go to the writer as-is
     * @xsl.usage internal
     */
    public final boolean isTextASCIIClean(int value)
    {
        return isCleanTextASCII[value];
    }
    


    /**
     * Factory that reads in a resource file that describes the mapping of
     * characters to entity references.
     *
     * Resource files must be encoded in UTF-8 and have a format like:
     * <pre>
     * # First char # is a comment
     * Entity numericValue
     * quot 34
     * amp 38
     * </pre>
     * (Note: Why don't we just switch to .properties files? Oct-01 -sc)
     *
     * @param entitiesResource Name of entities resource file that should
     * be loaded, which describes that mapping of characters to entity references.
     * @param method the output method type, which should be one of "xml", "html", "text"...
     * 
     * @xsl.usage internal
     */
    public static CharInfo getCharInfo(String entitiesFileName, String method)
    {
        CharInfo charInfo = (CharInfo) m_getCharInfoCache.get(entitiesFileName);
        if (charInfo != null) {
            return charInfo;
        }

        try {
            charInfo = new CharInfo(entitiesFileName, method, true);
            m_getCharInfoCache.put(entitiesFileName, charInfo);
            return charInfo;
        } catch (Exception e) {}

        try {
            return new CharInfo(entitiesFileName, method);
        } catch (Exception e) {}

        String absoluteEntitiesFileName;

        if (entitiesFileName.indexOf(':') < 0) {
            absoluteEntitiesFileName =
                SystemIDResolver.getAbsoluteURIFromRelative(entitiesFileName);
        } else {
            try {
                absoluteEntitiesFileName =
                    SystemIDResolver.getAbsoluteURI(entitiesFileName, null);
            } catch (TransformerException te) {
                throw new WrappedRuntimeException(te);
            }
        }

        return new CharInfo(absoluteEntitiesFileName, method, false);
    }

    /** Table of user-specified char infos. */
    private static Hashtable m_getCharInfoCache = new Hashtable();

    /**
     * Returns the array element holding the bit value for the
     * given integer
     * @param i the integer that might be in the set of integers
     * 
     */
    private static int arrayIndex(int i) {
        return (i >> SHIFT_PER_WORD);
    }

    /**
     * For a given integer in the set it returns the single bit
     * value used within a given word that represents whether
     * the integer is in the set or not.
     */
    private static int bit(int i) {
        int ret = (1 << (i & LOW_ORDER_BITMASK));
        return ret;
    }

    /**
     * Creates a new empty set of integers (characters)
     * @param max the maximum integer to be in the set.
     */
    private int[] createEmptySetOfIntegers(int max) {

        int[] arr = new int[arrayIndex(max - 1) + 1];
            return arr;
 
    }

    /**
     * Adds the integer (character) to the set of integers.
     * @param i the integer to add to the set, valid values are 
     * 0, 1, 2 ... up to the maximum that was specified at
     * the creation of the set.
     */
    private final void set(int i) {        
        int k = j + 1;       
        
            firstWordNotUsed = k;
            
        array_of_bits[j] |= (1 << (i & LOW_ORDER_BITMASK));
    }


    /**
     * Return true if the integer (character)is in the set of integers.
     * 
     * This implementation uses an array of integers with 32 bits per
     * integer.  If a bit is set to 1 the corresponding integer is 
     * in the set of integers.
     * 
     * @param i an integer that is tested to see if it is the
     * set of integers, or not.
     */
    private final boolean get(int i) {

        boolean in_the_set = false;
        if(j < firstWordNotUsed)
            in_the_set = (array_of_bits[j] & 
                          (1 << (i & LOW_ORDER_BITMASK))
        return in_the_set;
    }
    
    /**
     * @return true if the entity 
     * @param code The value of the character that has an entity defined
     * for it.
     */
    private boolean extraEntity(int entityValue)
    {
        boolean extra = false;
        if (entityValue < 128)
        {
            switch (entityValue)
            {
                    break;
                    extra = true;
            }
        }
        return extra;
    }    
}
