import java.lang.reflect.Array;
import java.util.AbstractList;

/**
 * A class that wraps an array with a List interface.
 *
 * @author Chris Schultz &lt;chris@christopherschultz.net$gt;
 * @version $Revision: 685685 $ $Date: 2006-04-14 19:40:41 $
 * @since 1.6
 */
public class ArrayListWrapper extends AbstractList
{
    private Object array;

    public ArrayListWrapper(Object array)
    {
        this.array = array;
    }

    public Object get(int index)
    {
        return Array.get(array, index);
    }

    public Object set(int index, Object element)
    {
        Object old = get(index);
        Array.set(array, index, element);
        return old;
    }

    public int size()
    {
        return Array.getLength(array);
    }

}
