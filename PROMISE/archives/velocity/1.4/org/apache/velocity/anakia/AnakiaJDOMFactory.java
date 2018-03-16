import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DefaultJDOMFactory;

/**
 * A customized JDOMFactory for Anakia that produces {@link AnakiaElement}
 * instances instead of ordinary JDOM {@link Element} instances.
 *
 * @author <a href="mailto:szegedia@freemail.hu">Attila Szegedi</a>
 * @version $Id: AnakiaJDOMFactory.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class AnakiaJDOMFactory extends DefaultJDOMFactory
{
    public AnakiaJDOMFactory()
    {
    }

    public Element element(String name, Namespace namespace)
    {
        return new AnakiaElement(name, namespace);
    }

    public Element element(String name)
    {
        return new AnakiaElement(name);
    }

    public Element element(String name, String uri)
    {
        return new AnakiaElement(name, uri);
    }

    public Element element(String name, String prefix, String uri)
    {
        return new AnakiaElement(name, prefix, uri);
    }
}
