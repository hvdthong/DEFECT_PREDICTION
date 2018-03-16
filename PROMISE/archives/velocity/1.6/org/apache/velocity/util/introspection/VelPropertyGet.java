public interface VelPropertyGet
{
    /**
     *  invocation method - called when the 'get action' should be
     *  preformed and a value returned
     * @param o
     * @return The resulting Object.
     * @throws Exception
     */
    public Object invoke(Object o) throws Exception;

    /**
     *  specifies if this VelPropertyGet is cacheable and able to be
     *  reused for this class of object it was returned for
     *
     *  @return true if can be reused for this class, false if not
     */
    public boolean isCacheable();

    /**
     *  returns the method name used to return this 'property'
     * @return The method name used to return this 'property'
     */
    public String getMethodName();
}
