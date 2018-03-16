import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

/**
 * This class adds an entrypoint into XPath functionality,
 * for Anakia.
 * <p>
 * All methods take a string XPath specification, along with
 * a context, and produces a resulting java.util.List.
 * <p>
 * to NodeSets repeatedly, but this implementation simply uses
 * java.util.List to hold all Nodes.  A 'Node' is any object in
 * a JDOM object tree, such as an org.jdom.Element, org.jdom.Document,
 * or org.jdom.Attribute.
 * <p>
 * To use it in Velocity, do this:
 * <p>
 * <pre>
 * #set $authors = $xpath.applyTo("document/author", $root)
 * #foreach ($author in $authors)
 *   $author.getValue()
 * #end
 * #set $chapterTitles = $xpath.applyTo("document/chapter/@title", $root)
 * #foreach ($title in $chapterTitles)
 *   $title.getValue()
 * #end
 * </pre>
 * <p>
 * In newer Anakia builds, this class is obsoleted in favor of calling
 * <code>selectNodes()</code> on the element directly:
 * <pre>
 * #set $authors = $root.selectNodes("document/author")
 * #foreach ($author in $authors)
 *   $author.getValue()
 * #end
 * #set $chapterTitles = $root.selectNodes("document/chapter/@title")
 * #foreach ($title in $chapterTitles)
 *   $title.getValue()
 * #end
 * </pre>
 * <p>
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:szegedia@freemail.hu">Attila Szegedi</a>
 * @version $Id: XPathTool.java 463298 2006-10-12 16:10:32Z henning $
 */
public class XPathTool
{
    /**
     * Constructor does nothing, as this is mostly
     * just objectified static methods
     */
    public XPathTool()
    {
    }

    /**
     * Apply an XPath to a JDOM Document
     *
     * @param xpathSpec The XPath to apply
     * @param doc The Document context
     *
     * @return A list of selected nodes
     */
    public NodeList applyTo(String xpathSpec,
                        Document doc)
    {
        return new NodeList(XPathCache.getXPath(xpathSpec).applyTo( doc ), false);
    }

    /**
     * Apply an XPath to a JDOM Element
     *
     * @param xpathSpec The XPath to apply
     * @param elem The Element context
     *
     * @return A list of selected nodes
     */
    public NodeList applyTo(String xpathSpec,
                        Element elem)
    {
        return new NodeList(XPathCache.getXPath(xpathSpec).applyTo( elem ), false);
    }

    /**
     * Apply an XPath to a nodeset
     *
     * @param xpathSpec The XPath to apply
     * @param nodeSet The nodeset context
     *
     * @return A list of selected nodes
     */
    public NodeList applyTo(String xpathSpec,
                        List nodeSet)
    {
        return new NodeList(XPathCache.getXPath(xpathSpec).applyTo( nodeSet ), false);
    }
}



