public final class SimplePool
{
    /*
     * Where the objects are held.
     */
    private Object pool[];

    /**
     *  max amount of objects to be managed
     *  set via CTOR
     */
    private int max;

    /**
     *  index of previous to next
     *  free slot
     */
    private int current=-1;

    /**
     * @param max
     */
    public SimplePool(int max)
    {
        this.max = max;
        pool = new Object[max];
    }

    /**
     * Add the object to the pool, silent nothing if the pool is full
     * @param o
     */
    public void put(Object o)
    {
        int idx=-1;

        synchronized(this)
        {
            /*
             *  if we aren't full
             */

            if (current < max - 1)
            {
                /*
                 *  then increment the
                 *  current index.
                 */
                idx = ++current;
            }

            if (idx >= 0)
            {
                pool[idx] = o;
            }
        }
    }

    /**
     * Get an object from the pool, null if the pool is empty.
     * @return The object from the pool.
     */
    public Object get()
    {
        synchronized(this)
        {
            /*
             *  if we have any in the pool
             */
            if( current >= 0 )
            {
                /*
                 *  remove the current one
                 */

                Object o = pool[current];
                pool[current] = null;

                current--;

                return o;
            }
        }

        return null;
    }

    /**
     * Return the size of the pool
     * @return The pool size.
     */
    public int getMax()
    {
        return max;
    }

    /**
     *   for testing purposes, so we can examine the pool
     *
     * @return Array of Objects in the pool.
     */
    Object[] getPool()
    {
        return pool;
    }
}
