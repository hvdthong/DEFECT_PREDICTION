package org.apache.xml.utils;

/**
 * <meta name="usage" content="internal"/>
 * A digital search trie for 7-bit ASCII text
 * The API is a subset of java.util.Hashtable
 * The key must be a 7-bit ASCII string
 * The value may be any Java Object
 */
public class Trie
{

  /** Size of the m_nextChar array.  */
  public static final int ALPHA_SIZE = 128;

  /** The root node of the tree.    */
  Node m_Root;

  /**
   * Construct the trie.
   */
  public Trie()
  {
    m_Root = new Node();
  }

  /**
   * Put an object into the trie for lookup.
   *
   * @param key must be a 7-bit ASCII string
   * @param value any java object.
   *
   * @return The old object that matched key, or null.
   */
  public Object put(String key, Object value)
  {

    final int len = key.length();
    Node node = m_Root;

    for (int i = 0; i < len; i++)
    {
      Node nextNode = node.m_nextChar[Character.toUpperCase(key.charAt(i))];

      if (nextNode != null)
      {
        node = nextNode;
      }
      else
      {
        for (; i < len; i++)
        {
          Node newNode = new Node();

          node.m_nextChar[Character.toUpperCase(key.charAt(i))] = newNode;
          node = newNode;
        }

        break;
      }
    }

    Object ret = node.m_Value;

    node.m_Value = value;

    return ret;
  }

  /**
   * Get an object that matches the key.
   *
   * @param key must be a 7-bit ASCII string
   *
   * @return The object that matches the key, or null.
   */
  public Object get(String key)
  {

    final int len = key.length();
    Node node = m_Root;

    for (int i = 0; i < len; i++)
    {
      try
      {
        node = node.m_nextChar[Character.toUpperCase(key.charAt(i))];
      }
      catch (ArrayIndexOutOfBoundsException e)
      {

        node = null;
      }

      if (node == null)
        return null;
    }

    return node.m_Value;
  }

  /**
   * <meta name="usage" content="internal"/>
   * The node representation for the trie.
   */
  class Node
  {

    /**
     * Constructor, creates a Node[ALPHA_SIZE].
     */
    Node()
    {
      m_nextChar = new Node[ALPHA_SIZE];
      m_Value = null;
    }

    /** The next nodes.   */
    Node m_nextChar[];

    /** The value.   */
    Object m_Value;
  }
}
