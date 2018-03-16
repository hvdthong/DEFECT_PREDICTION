package org.apache.xml.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <meta name="usage" content="general"/>
 * Simple static utility to convert Hashtable to a Node.  
 *
 * Please maintain JDK 1.1.x compatibility; no Collections!
 *
 * @see org.apache.xalan.xslt.EnvironmentCheck
 * @see org.apache.xalan.lib.Extensions
 * @author shane_curcuru@us.ibm.com
 * @version $Id: Hashtree2Node.java 337166 2003-01-30 18:46:32Z mkwan $
 */
public abstract class Hashtree2Node
{

    /**
     * Convert a Hashtable into a Node tree.  
     * 
     * <p>The hash may have either Hashtables as values (in which 
     * case we recurse) or other values, in which case we print them 
     * as &lt;item> elements, with a 'key' attribute with the value 
     * of the key, and the element contents as the value.</p>
     *
     * <p>If args are null we simply return without doing anything. 
     * If we encounter an error, we will attempt to add an 'ERROR' 
     * Element with exception info; if that doesn't work we simply 
     * return without doing anything else byt printStackTrace().</p>
     *
     * @param hash to get info from (may have sub-hashtables)
     * @param name to use as parent element for appended node
     * futurework could have namespace and prefix as well
     * @param container Node to append our report to
     * @param factory Document providing createElement, etc. services
     */
    public static void appendHashToNode(Hashtable hash, String name, 
            Node container, Document factory)
    {
        if ((null == container) || (null == factory) || (null == hash))
        {
            return;
        }

        String elemName = null;
        if ((null == name) || ("".equals(name)))
            elemName = "appendHashToNode";
        else
            elemName = name;

        try
        {
            Element hashNode = factory.createElement(elemName);
            container.appendChild(hashNode);

            Enumeration enum = hash.keys();
            Vector v = new Vector();

            while (enum.hasMoreElements())
            {
                Object key = enum.nextElement();
                String keyStr = key.toString();
                Object item = hash.get(key);

                if (item instanceof Hashtable)
                {
                    v.addElement(keyStr);
                    v.addElement((Hashtable) item);
                }
                else
                {
                    try
                    {
                        Element node = factory.createElement("item");
                        node.setAttribute("key", keyStr);
                        node.appendChild(factory.createTextNode((String)item));
                        hashNode.appendChild(node);
                    }
                    catch (Exception e)
                    {
                        Element node = factory.createElement("item");
                        node.setAttribute("key", keyStr);
                        node.appendChild(factory.createTextNode("ERROR: Reading " + key + " threw: " + e.toString()));
                        hashNode.appendChild(node);
                    }
                }
            }

            enum = v.elements();
            while (enum.hasMoreElements())
            {
                String n = (String) enum.nextElement();
                Hashtable h = (Hashtable) enum.nextElement();

                appendHashToNode(h, n, hashNode, factory);
            }
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
        }
    }    
}
