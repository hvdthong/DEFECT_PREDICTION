import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;


/**
 *  <p>
 *  An Iterator wrapper for an Object[]. This will
 *  allow us to deal with all array like structures
 *  in a consistent manner.
 *  </p>
 *  <p>
 *  WARNING : this class's operations are NOT synchronized.
 *  It is meant to be used in a single thread, newly created
 *  for each use in the #foreach() directive.
 *  If this is used or shared, synchronize in the
 *  next() method.
 *  </p>
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: ArrayIterator.java 463298 2006-10-12 16:10:32Z henning $
 */
public class ArrayIterator implements Iterator
{
    /**
     * The objects to iterate.
     */
    private Object array;

    /**
     * The current position and size in the array.
     */
    private int pos;
    private int size;

    /**
     * Creates a new iterator instance for the specified array.
     *
     * @param array The array for which an iterator is desired.
     */
    public ArrayIterator(Object array)
    {
        /*
         * if this isn't an array, then throw.  Note that this is
         * for internal use - so this should never happen - if it does
         *  we screwed up.
         */

        if ( !array.getClass().isArray() )
        {
            throw new IllegalArgumentException(
                "Programmer error : internal ArrayIterator invoked w/o array");
        }

        this.array = array;
        pos = 0;
        size = Array.getLength( this.array );
    }

    /**
     * Move to next element in the array.
     *
     * @return The next object in the array.
     */
    public Object next()
    {
        if (pos < size )
            return Array.get( array, pos++);

        /*
         *  we screwed up...
         */

        throw new NoSuchElementException("No more elements: " + pos +
                                         " / " + size);
    }

    /**
     * Check to see if there is another element in the array.
     *
     * @return Whether there is another element.
     */
    public boolean hasNext()
    {
        return (pos < size );
    }

    /**
     * No op--merely added to satify the <code>Iterator</code> interface.
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
