package org.apache.xml.utils;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class implements a generic PrefixResolver that
 * can be used to perform prefix-to-namespace lookup
 * for the XPath object.
 * @xsl.usage general
 */
public class PrefixResolverDefault implements PrefixResolver
{

  /**
   * The context to resolve the prefix from, if the context
   * is not given.
   */
  Node m_context;

  /**
   * Construct a PrefixResolverDefault object.
   * @param xpathExpressionContext The context from
   * which XPath expression prefixes will be resolved.
   * Warning: This will not work correctly if xpathExpressionContext
   * is an attribute node.
   */
  public PrefixResolverDefault(Node xpathExpressionContext)
  {
    m_context = xpathExpressionContext;
  }

  /**
   * Given a namespace, get the corrisponding prefix.  This assumes that
   * the PrevixResolver hold's it's own namespace context, or is a namespace
   * context itself.
   * @param prefix Prefix to resolve.
   * @return Namespace that prefix resolves to, or null if prefix
   * is not bound.
   */
  public String getNamespaceForPrefix(String prefix)
  {
    return getNamespaceForPrefix(prefix, m_context);
  }

  /**
   * Given a namespace, get the corrisponding prefix.
   * Warning: This will not work correctly if namespaceContext
   * is an attribute node.
   * @param prefix Prefix to resolve.
   * @param namespaceContext Node from which to start searching for a
   * xmlns attribute that binds a prefix to a namespace.
   * @return Namespace that prefix resolves to, or null if prefix
   * is not bound.
   */
  public String getNamespaceForPrefix(String prefix,
                                      org.w3c.dom.Node namespaceContext)
  {

    Node parent = namespaceContext;
    String namespace = null;

    if (prefix.equals("xml"))
    {
      namespace = Constants.S_XMLNAMESPACEURI;
    }
    else
    {
      int type;

      while ((null != parent) && (null == namespace)
             && (((type = parent.getNodeType()) == Node.ELEMENT_NODE)
                 || (type == Node.ENTITY_REFERENCE_NODE)))
      {
        if (type == Node.ELEMENT_NODE)
        {
                if (parent.getNodeName().indexOf(prefix+":") == 0) 
                        return parent.getNamespaceURI();                
          NamedNodeMap nnm = parent.getAttributes();

          for (int i = 0; i < nnm.getLength(); i++)
          {
            Node attr = nnm.item(i);
            String aname = attr.getNodeName();
            boolean isPrefix = aname.startsWith("xmlns:");

            if (isPrefix || aname.equals("xmlns"))
            {
              int index = aname.indexOf(':');
              String p = isPrefix ? aname.substring(index + 1) : "";

              if (p.equals(prefix))
              {
                namespace = attr.getNodeValue();

                break;
              }
            }
          }
        }

        parent = parent.getParentNode();
      }
    }

    return namespace;
  }

  /**
   * Return the base identifier.
   *
   * @return null
   */
  public String getBaseIdentifier()
  {
    return null;
  }
	/**
	 * @see PrefixResolver#handlesNullPrefixes()
	 */
	public boolean handlesNullPrefixes() {
		return false;
	}

}
