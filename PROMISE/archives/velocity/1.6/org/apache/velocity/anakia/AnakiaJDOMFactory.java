import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.DefaultJDOMFactory;

/**
 * A customized JDOMFactory for Anakia that produces {@link AnakiaElement}
 * instances instead of ordinary JDOM {@link Element} instances.
 *
 * @author <a href="mailto:szegedia@freemail.hu">Attila Szegedi</a>
 * @version $Id: AnakiaJDOMFactory.java 463298 2006-10-12 16:10:32Z henning $
 */
public class AnakiaJDOMFactory extends DefaultJDOMFactory
{
    /**
     *
     */
    public AnakiaJDOMFactory()
    {
    }

    /**
     * @see org.jdom.DefaultJDOMFactory#element(java.lang.String, org.jdom.Namespace)
     */
    public Element element(String name, Namespace namespace)
    {
        return new AnakiaElement(name, namespace);
    }

    /**
     * @see org.jdom.DefaultJDOMFactory#element(java.lang.String)
     */
    public Element element(String name)
    {
        return new AnakiaElement(name);
    }

    /**
     * @see org.jdom.DefaultJDOMFactory#element(java.lang.String, java.lang.String)
     */
    public Element element(String name, String uri)
    {
        return new AnakiaElement(name, uri);
    }

    /**
     * @see org.jdom.DefaultJDOMFactory#element(java.lang.String, java.lang.String, java.lang.String)
     */
    public Element element(String name, String prefix, String uri)
    {
        return new AnakiaElement(name, prefix, uri);
    }
}
