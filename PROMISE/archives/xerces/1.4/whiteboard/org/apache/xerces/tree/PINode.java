package org.apache.xerces.tree;


import java.io.Writer;
import java.io.IOException;

import org.w3c.dom.*;


/**
 * Node representing an XML processing instruction.
 *
 * <P> <em>Functionality to restore in some other way: </em>
 *
 * As a convenience function, the instruction data may optionally
 * be parsed as element attributes are parsed.  There is no requirement
 * to use this particular syntax for instruction data.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
final
class PINode extends NodeBase implements ProcessingInstruction
{
    private String      target;
    private char	data [];
        
    /** Constructs a processing instruction node. */
    public PINode () { }

    /** Constructs a processing instruction node. */
    public PINode (String target, String text)
    {
	data = text.toCharArray ();
        this.target = target;
    }

    PINode (String target, char buf [], int offset, int len)
    {
	data = new char [len];
	System.arraycopy (buf, offset, data, 0, len);
        this.target = target;
    }

    /** DOM:  Returns the PROCESSING_INSTRUCTION_NODE node type. */
    public short getNodeType () { return PROCESSING_INSTRUCTION_NODE; }

    /** DOM:  Returns the processor the instruction is directed to. */
    public String getTarget () { return target; }

    /** DOM:  Assigns the processor the instruction is directed to. */
    public void setTarget (String target) { this.target = target; }

    /** DOM: Returns the text data as a string. */
    public String getData () { return new String (data); }

    /** DOM: Assigns the text data. */
    public void setData (String data) { 
        if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
    
        this.data = data.toCharArray (); 
    }

    /** DOM: Returns the text data as a string. */
    public String getNodeValue () { return getData (); }

    /** DOM: Assigns the text data. */
    public void setNodeValue (String data) { setData (data); }

    /**
     * Writes the processing instruction as well formed XML text.
     *
     * <P> <em> Doesn't currently check for the <b>?&gt;</b> substrings
     * in PI data, which are illegal </em>
     */
    public void writeXml (XmlWriteContext context) throws IOException
    {
	Writer	out = context.getWriter ();

        out.write ("<?");
        out.write (target);
        if (data != null) {
            out.write (' ');
            out.write (data);
        }
        out.write ("?>");
    }

    /** Returns a new processing instruction with the same content as this. */
    public Node cloneNode (boolean deep) { 
    	PINode retval = new PINode (target, data, 0, data.length); 
	retval.setOwnerDocument ((XmlDocument) this.getOwnerDocument ());
	return retval;
    }

    /** Returns the PI target name. */
    public String getNodeName () { return target; }
}
