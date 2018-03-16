package org.apache.tools.ant;

/**
 * Used to wrap types.
 *
 */
public interface TypeAdapter {

    /**
     * Sets the project
     *
     * @param p the project instance.
     */
    void setProject(Project p);

    /**
     * Gets the project
     *
     * @return the project instance.
     */
    Project getProject();

    /**
     * Sets the proxy object, whose methods are going to be
     * invoked by ant.
     * A proxy object is normally the object defined by
     * a &lt;typedef/&gt; task that is adapted by the "adapter"
     * attribute.
     *
     * @param o The target object. Must not be <code>null</code>.
     */
    void setProxy(Object o);

    /**
     * Returns the proxy object.
     *
     * @return the target proxy object
     */
    Object getProxy();

    /**
     * Check if the proxy class is compatible with this adapter - i.e.
     * the adapter will be able to adapt instances of the give class.
     *
     * @param proxyClass the class to be checked.
     */
    void checkProxyClass(Class proxyClass);
}
