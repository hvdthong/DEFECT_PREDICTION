import java.util.Iterator;
import java.util.Enumeration;

/**
 * An Iterator wrapper for an Enumeration.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: EnumerationIterator.java 463298 2006-10-12 16:10:32Z henning $
 */
public class EnumerationIterator implements Iterator
{
    /**
     * The enumeration to iterate.
     */
    private Enumeration enumeration = null;

    /**
     * Creates a new iteratorwrapper instance for the specified
     * Enumeration.
     *
     * @param enumeration  The Enumeration to wrap.
     */
    public EnumerationIterator(Enumeration enumeration)
    {
        this.enumeration = enumeration;
    }

    /**
     * Move to next element in the array.
     *
     * @return The next object in the array.
     */
    public Object next()
    {
        return enumeration.nextElement();
    }

    /**
     * Check to see if there is another element in the array.
     *
     * @return Whether there is another element.
     */
    public boolean hasNext()
    {
        return enumeration.hasMoreElements();
    }

    /**
     *  Unimplemented.  No analogy in Enumeration
     */
    public void remove()
    {
    }

}
