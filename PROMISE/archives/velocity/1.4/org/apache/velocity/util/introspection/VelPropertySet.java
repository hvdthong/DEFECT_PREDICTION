public interface VelPropertySet
{
    /**
     *  method used to set the value in the object
     *
     *  @param o Object on which the method will be called with the arg
     *  @param arg value to be set
     *  @return the value returned from the set operation (impl specific)
     */
    public Object invoke(Object o, Object arg) throws Exception;

    /**
     *  specifies if this VelPropertySet is cacheable and able to be
     *  reused for this class of object it was returned for
     *
     *  @return true if can be reused for this class, false if not
     */
    public boolean isCacheable();

    /**
     *  returns the method name used to set this 'property'
     */
    public String getMethodName();
}
