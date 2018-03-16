package org.apache.xerces.tree;

import java.io.IOException;
import java.io.Writer;


/**
 * This captures context used when writing XML text, such as state
 * used to "pretty print" output or to identify entities which are
 * defined.  Pretty printing is useful when displaying structure in
 * XML documents that need to be read or edited by people (rather
 * than only by machines).
 *
 * @see XmlWritable
 * @see XmlDocument#createWriteContext
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public class XmlWriteContext
{
    private Writer	writer;
    private int		indentLevel;
    private boolean	prettyOutput;


    /**
     * Constructs a write context that doesn't pretty-print output.
     */
    public XmlWriteContext (Writer out)
    {
	writer = out;
    }

    /**
     * Constructs a write context that supports pretty-printing
     * output starting at the specified number of spaces.
     */
    public XmlWriteContext (Writer out, int level)
    {
	writer = out;
	prettyOutput = true;
	indentLevel = level;
    }

    /**
     * Returns the writer to which output should be written.
     */
    public Writer getWriter ()
    {
	return writer;
    }

    /**
     * Returns true if the specified entity was already declared
     * in this output context, so that entity references may be
     * written rather than their expanded values.  The predefined
     * XML entities are always declared.
     */
    public boolean isEntityDeclared (String name)
    {

	return ("amp".equals (name)
		|| "lt".equals (name) || "gt".equals (name)
		|| "quot".equals (name) || "apos".equals (name));
    }

    /**
     * Returns the current indent level, in terms of spaces, for
     * use in pretty printing XML text.
     */
    public int getIndentLevel ()
    {
	return indentLevel;
    }

    /**
     * Assigns the current indent level, in terms of spaces, for
     * use in pretty printing XML text.
     */
    public void setIndentLevel (int level)
    {
	indentLevel = level;
    }

    /**
     * If pretty printing is enabled, this writes a newline followed by
     * <em>indentLevel</em> spaces.  At the beginning of a line, groups
     * of eight consecutive spaces are replaced by tab characters, for
     * storage efficiency.
     *
     * <P> Note that this method should not be used except in cases
     * where the additional whitespace is guaranteed to be semantically
     * meaningless.  This is the default, and is controlled through the
     * <em>xml:space</em> attribute, inherited from parent elements.
     * When this attribute value is <em>preserve</em>, this method should
     * not be used.  Otherwise, text normalization is expected to remove
     * excess whitespace such as that added by this call.
     */
    public void printIndent () throws IOException
    {
	int	temp = indentLevel;

	if (!prettyOutput)
	    return;

	writer.write(XmlDocument.eol);
	while (temp-- > 0) {
	    writer.write (' ');
        }
    }

    /**
     * Returns true if writes using the context should "pretty print",
     * displaying structure through indentation as appropriate.
     */
    public boolean isPrettyOutput ()
    {
	return prettyOutput;
    }
}
