package org.apache.xerces.tree;

import java.io.IOException;


/**
 * Objects that can write themselves as XML text do so using this
 * interface.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface XmlWritable
{
    /**
     * This is the primary method used to write an object and
     * its children as XML text.  Implementations with children
     * should use writeChildrenXml to write those children, to
     * allow selective overriding.
     */
    public void writeXml (XmlWriteContext context)
    throws IOException;

    /**
     * Used to write any children of a node.
     */
    public void writeChildrenXml (XmlWriteContext context)
    throws IOException;
}
