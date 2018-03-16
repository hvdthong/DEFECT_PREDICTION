package org.apache.xalan.serialize;

import java.util.BitSet;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.net.*;

import java.util.Hashtable;

import org.apache.xml.utils.CharKey;

import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;

/**
 * This class provides services that tell if a character should have
 * special treatement, such as entity reference substitution or normalization
 * of a newline character.  It also provides character to entity reference
 * lookup.
 *
 * DEVELOPERS: See Known Issue in the constructor.
 */
public class CharInfo
{

  /** Bit map that tells if a given character should have special treatment. */
  BitSet m_specialsMap = new BitSet(65535);

  /** Lookup table for characters to entity references. */
  private Hashtable m_charToEntityRef = new Hashtable();

  /**
   * The name of the HTML entities file.
   * If specified, the file will be resource loaded with the default class loader.
   */
  public static String HTML_ENTITIES_RESOURCE = "HTMLEntities.res";

  /**
   * The name of the XML entities file.
   * If specified, the file will be resource loaded with the default class loader.
   */
  public static String XML_ENTITIES_RESOURCE = "XMLEntities.res";

  /** The linefeed character, which the parser should always normalize. */
  public static char S_LINEFEED = 0x0A;

  /** The carriage return character, which the parser should always normalize. */
  public static char S_CARRIAGERETURN = 0x0D;

  /** a zero length Class array used in the constructor */
  private static final Class[] NO_CLASSES = new Class[0];

  /** a zero length Object array used in the constructor */
  private static final Object[] NO_OBJS = new Object[0];


  /**
   * Constructor that reads in a resource file that describes the mapping of
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
   */
  public CharInfo(String entitiesResource)
  {

    InputStream is = null;
    BufferedReader reader = null;
    int index;
    String name;
    String value;
    int code;
    String line;

    try
    {
      try
      {
        ClassLoader cl = CharInfo.class.getClassLoader();

        if (cl == null) {
          is = ClassLoader.getSystemResourceAsStream(entitiesResource);
        } else {
          is = cl.getResourceAsStream(entitiesResource);
        }
      }
      catch (Exception e) {}

      if (is == null)
        is = CharInfo.class.getResourceAsStream(entitiesResource);

      if (is == null)
      {
        URL url = new URL(entitiesResource);

        is = url.openStream();
      }

      if (is == null)
        throw new RuntimeException(XSLMessages.createMessage(XSLTErrorResources.ER_RESOURCE_COULD_NOT_FIND, new Object[]{entitiesResource, entitiesResource }));

      try
      {
	reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      }
      catch(java.io.UnsupportedEncodingException e)
      {
	reader = new BufferedReader(new InputStreamReader(is));
      }

      line = reader.readLine();

      while (line != null)
      {
        if (line.length() == 0 || line.charAt(0) == '#')
        {
          line = reader.readLine();

          continue;
        }

        index = line.indexOf(' ');

        if (index > 1)
        {
          name = line.substring(0, index);

          ++index;

          if (index < line.length())
          {
            value = line.substring(index);
            index = value.indexOf(' ');

            if (index > 0)
              value = value.substring(0, index);

            code = Integer.parseInt(value);

            defineEntity(name, (char) code);
          }
        }

        line = reader.readLine();
      }

      is.close();
      m_specialsMap.set(S_LINEFEED);
      m_specialsMap.set(S_CARRIAGERETURN);
    }
    catch (Exception except)
    {
      throw new RuntimeException(XSLMessages.createMessage(XSLTErrorResources.ER_RESOURCE_COULD_NOT_LOAD, new Object[]{entitiesResource,  except.toString(), entitiesResource, except.toString() }));
    }
    finally
    {
      if (is != null)
      {
        try
        {
          is.close();
        }
        catch (Exception except){}
      }
    }
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
  protected void defineEntity(String name, char value)
  {
    CharKey character = new CharKey(value);

    m_charToEntityRef.put(character, name);
    m_specialsMap.set(value);
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
   */
  synchronized 
  public String getEntityNameForChar(char value)
  {
    m_charKey.setChar(value);
    return (String) m_charToEntityRef.get(m_charKey);
  }

  /**
   * Tell if the character argument should have special treatment.
   *
   * @param value character value.
   *
   * @return true if the character should have any special treatment.
   */
  public boolean isSpecial(char value)
  {
    return m_specialsMap.get(value);
  }
}
