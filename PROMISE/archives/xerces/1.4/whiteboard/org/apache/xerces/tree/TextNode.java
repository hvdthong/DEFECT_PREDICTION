package org.apache.xerces.tree;


import java.io.Writer;
import java.io.IOException;

import org.w3c.dom.*;


/**
 * Node representing XML text.
 *
 * <P> Subclasses should not currently attempt to modify the
 * representation of content, but may add new methods to support
 * more sophisticated access or manipulation of that content.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
class TextNode extends DataNode implements Text
{

    /**
     * Constructs a text object with no text and unattached
     * to any document.
     */
    public TextNode () { }

        
    /**
     * Constructs text object by copying text from the input buffer.
     */
    public TextNode (char buf [], int offset, int len)
    {
	super (buf, offset, len);
    }

    /**
     * Constructs a text object by copying text from the string.
     */
    public TextNode (String s)
    {
	super (s);
    }

    /**
     * Writes the text, escaping XML metacharacters as needed
     * to let this text be parsed again without change.
     */
    public void writeXml (XmlWriteContext context) throws IOException
    {
	Writer	out = context.getWriter ();
	int	start = 0, last = 0;

	if (data == null)
	    { System.err.println ("Null text data??"); return; }

	while (last < data.length) {
	    char c = data [last];

		out.write (data, start, last - start);
		start = last + 1;
		out.write ("&lt;");
		out.write (data, start, last - start);
		start = last + 1;
		out.write ("&gt;");
		out.write (data, start, last - start);
		start = last + 1;
		out.write ("&amp;");
	    }
	    last++;
	}
	out.write (data, start, last - start);
    }

    /**
     * Combines this text node with its next sibling to create a
     * single text node.  If the next node is not text, nothing is
     * done.  This should be used with care, since large spans of
     * text may not be efficient to represent.
     */
    public void joinNextText ()
    {
	Node	next = getNextSibling ();
	char	tmp [], nextText [];

	if (next == null || next.getNodeType () != TEXT_NODE)
	    return;
	getParentNode ().removeChild (next);

	nextText = ((TextNode)next).getText ();
	tmp = new char [data.length + nextText.length];
	System.arraycopy (data, 0, tmp, 0, data.length);
	System.arraycopy (nextText, 0, tmp, data.length, nextText.length);
	data = tmp;
    }



    /** DOM: Returns the TEXT_NODE node type constant. */
    public short getNodeType () { return TEXT_NODE; }


    /**
     * DOM:  Splits this text node into two, returning the part
     * beginning at <em>offset</em>.  The original node has that 
     * text removed, and the two nodes are siblings in the natural
     * order.
     */
    public Text splitText (int offset)
    throws DOMException
    {
	TextNode	retval;
	char		delta [];

        if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	
	try {
		retval = new TextNode (data, offset, data.length - offset);
	}
	catch (ArrayIndexOutOfBoundsException ae) {
		throw new DomEx (DOMException.INDEX_SIZE_ERR);
	}
	catch (NegativeArraySizeException nae) {
		throw new DomEx (DOMException.INDEX_SIZE_ERR);
	}
	getParentNode().insertBefore (retval, getNextSibling ());
	delta = new char [offset];
	System.arraycopy (data, 0, delta, 0, offset);
	data = delta;
	return retval;
    }


    /**
     * DOM: returns a new text node with the same contents as this one.
     */
    public Node cloneNode (boolean deep)
    {
    	TextNode retval = new TextNode (data, 0, data.length);
	retval.setOwnerDocument ((XmlDocument) this.getOwnerDocument ());
	return retval;
    }

    /**
     * DOM:  Returns the string "#text".
     */
    public String getNodeName () { return "#text"; }
}
