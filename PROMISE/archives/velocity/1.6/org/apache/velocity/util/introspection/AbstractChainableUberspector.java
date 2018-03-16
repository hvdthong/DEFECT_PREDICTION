import java.util.Iterator;

/**
 * Default implementation of a {@link ChainableUberspector chainable uberspector} that forwards all calls to the wrapped
 * uberspector (when that is possible). It should be used as the base class for all chainable uberspectors.
 * 
 * @version $Id: $
 * @since 1.6
 * @see ChainableUberspector
 */
public abstract class AbstractChainableUberspector extends UberspectImpl implements ChainableUberspector
{
    /** The wrapped (decorated) uberspector. */
    protected Uberspect inner;

    /**
     * {@inheritDoc}
     * 
     * @see ChainableUberspector#wrap(org.apache.velocity.util.introspection.Uberspect)
     * @see #inner
     */
    public void wrap(Uberspect inner)
    {
        this.inner = inner;
    }

    /**
     * init - the chainable uberspector is responsible for the initialization of the wrapped uberspector
     * 
     * @see org.apache.velocity.util.introspection.Uberspect#init()
     */
    public void init() throws Exception
    {
        if (this.inner != null) {
            this.inner.init();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.velocity.util.introspection.Uberspect#getIterator(java.lang.Object,
     *      org.apache.velocity.util.introspection.Info)
     */
    public Iterator getIterator(Object obj, Info i) throws Exception
    {
        return (this.inner != null) ? this.inner.getIterator(obj, i) : null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.velocity.util.introspection.Uberspect#getMethod(java.lang.Object, java.lang.String,
     *      java.lang.Object[], org.apache.velocity.util.introspection.Info)
     */
    public VelMethod getMethod(Object obj, String methodName, Object[] args, Info i) throws Exception
    {
        return (this.inner != null) ? this.inner.getMethod(obj, methodName, args, i) : null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.velocity.util.introspection.Uberspect#getPropertyGet(java.lang.Object, java.lang.String,
     *      org.apache.velocity.util.introspection.Info)
     */
    public VelPropertyGet getPropertyGet(Object obj, String identifier, Info i) throws Exception
    {
        return (this.inner != null) ? this.inner.getPropertyGet(obj, identifier, i) : null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.velocity.util.introspection.Uberspect#getPropertySet(java.lang.Object, java.lang.String,
     *      java.lang.Object, org.apache.velocity.util.introspection.Info)
     */
    public VelPropertySet getPropertySet(Object obj, String identifier, Object arg, Info i) throws Exception
    {
        return (this.inner != null) ? this.inner.getPropertySet(obj, identifier, arg, i) : null;
    }
}
