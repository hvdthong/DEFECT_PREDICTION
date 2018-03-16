package org.apache.xerces.tree;


import java.io.Writer;
import java.io.IOException;

import org.w3c.dom.*;


/**
 * Node representing XML CDATA text, which is just like other text except
 * for its delimiters (<em>&lt;[CDATA[</em>text<em>]]&gt;</em>).  CDATA
 * is used to embed markup, program source code, and other kinds of text
 * that use XML markup delimiters (<em>&lt;</em> and <em>&amp;</em>) for
 * their own nefarious purposes.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
class CDataNode extends TextNode implements CDATASection
{
    /**
     * Constructs a CDATA text object with no text.
     */
    public CDataNode () { }

        
    /**
     * Constructs CDATA text object by copying text from the input buffer.
     */
    public CDataNode (char buf [], int offset, int len)
    {
	super (buf, offset, len);
    }

    /**
     * Constructs a CDATA text object by copying text from the string.
     */
    public CDataNode (String s)
    {
	super (s);
    }

    /**
     * Writes the text, breaking this into multiple CDATA sections
     * if necessary to escape <em>[[&lt;</em> in the data.
     */
    public void writeXml (XmlWriteContext context) throws IOException
    {
	Writer	out = context.getWriter ();
	out.write ("<![CDATA[");
	for (int i = 0; i < data.length; i++) {
	    char c = data [i];

	    if (c == ']') {
		if ((i + 2) < data.length
			&& data [i + 1] == ']'
			&& data [i + 2] == '>') {
		    out.write ("]]]]><![CDATA[>");
		    continue;
		}
	    }
	    out.write (c);
	}
	out.write ("]]>");
    }


    /** DOM: Returns the CDATA_SECTION_NODE node type constant. */
    public short getNodeType () { return CDATA_SECTION_NODE; }

    /** Returns a new CDATA section with the same content as this. */
    public Node cloneNode (boolean deep)
    {
    	CDataNode retval = new CDataNode (data, 0, data.length);
	retval.setOwnerDocument ((XmlDocument)this.getOwnerDocument ());
	return retval;
    }

    /** Returns the string "#cdata-section".*/
    public String getNodeName () { return "#cdata-section"; }
}
