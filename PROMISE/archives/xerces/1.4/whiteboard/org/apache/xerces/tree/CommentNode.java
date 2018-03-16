package org.apache.xerces.tree;


import java.io.Writer;
import java.io.IOException;

import org.w3c.dom.*;


/**
 * Class representing an XML Comment.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
class CommentNode extends DataNode implements Comment
{
    /** Constructs a comment node. */
    public CommentNode () { }

    /** Constructs a comment node. */
    public CommentNode (String data)
    {
        super (data);
    }

    CommentNode (char buf [], int offset, int len)
	{ super (buf, offset, len); }
    
    /** DOM:  Returns the COMMENT_NODE node type. */
    public short getNodeType () { return COMMENT_NODE; }


    /**
     * Writes out the comment.  Note that spaces may be added to
     * prevent illegal comments:  between consecutive dashes ("--")
     * or if the last character of the comment is a dash.
     */
    public void writeXml (XmlWriteContext context) throws IOException
    {
	Writer	out = context.getWriter ();
        out.write ("<!--");
        if (data != null) {
	    boolean	sawDash = false;
	    int		length = data.length;

	    for (int i = 0; i < length; i++) {
		if (data [i] == '-') {
		    if (sawDash)
			out.write (' ');
		    else {
			sawDash = true;
			out.write ('-');
			continue;
		    }
		}
		sawDash = false;
		out.write (data [i]);
	    }
	    if (data [data.length - 1] == '-')
		out.write (' ');
	}
        out.write ("-->");
    }

    /** Returns a new comment with the same content as this. */
    public Node cloneNode (boolean deep) { 
    	CommentNode retval = new CommentNode (data, 0, data.length); 
	retval.setOwnerDocument ((XmlDocument) this.getOwnerDocument ());
    	return retval;
    }

    /** Returns the string "#comment". */
    public String getNodeName () { return "#comment"; }
}
