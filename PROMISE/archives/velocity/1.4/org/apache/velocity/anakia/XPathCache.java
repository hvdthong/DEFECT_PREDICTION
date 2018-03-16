import com.werken.xpath.XPath;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Provides a cache for XPath expressions. Used by {@link NodeList} and 
 * {@link AnakiaElement} to minimize XPath parsing in their 
 * <code>selectNodes()</code> methods.
 *
 * @author <a href="mailto:szegedia@freemail.hu">Attila Szegedi</a>
 * @version $Id: XPathCache.java 75955 2004-03-03 23:23:08Z geirm $
 */
class XPathCache
{
    private static final Map XPATH_CACHE = new WeakHashMap();

    private XPathCache()
    {
    }
    
    /**
     * Returns an XPath object representing the requested XPath expression.
     * A cached object is returned if it already exists for the requested expression.
     * @param xpathString the XPath expression to parse
     * @return the XPath object that represents the parsed XPath expression.
     */
    static XPath getXPath(String xpathString)
    {
        XPath xpath = null;
        synchronized(XPATH_CACHE)
        {
            xpath = (XPath)XPATH_CACHE.get(xpathString);
            if(xpath == null)
            {
                xpath = new XPath(xpathString);
                XPATH_CACHE.put(xpathString, xpath);
            }
        }
        return xpath;
    }
}
