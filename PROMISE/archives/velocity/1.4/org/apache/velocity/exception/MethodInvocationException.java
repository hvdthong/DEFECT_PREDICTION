public class MethodInvocationException extends VelocityException
{
    private String methodName = "";
    private String referenceName = "";
    private Throwable wrapped = null;

    /**
     *  CTOR - wraps the passed in exception for
     *  examination later
     *
     *  @param message 
     *  @param e Throwable that we are wrapping
     *  @param methodName name of method that threw the exception
     */
    public MethodInvocationException( String message, Throwable e, String methodName )
    {
        super(message);
        this.wrapped = e;
        this.methodName = methodName;
    }       

    /**
     *  Returns the name of the method that threw the
     *  exception
     *
     *  @return String name of method
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     *  returns the wrapped Throwable that caused this
     *  MethodInvocationException to be thrown
     *  
     *  @return Throwable thrown by method invocation
     */
    public Throwable getWrappedThrowable()
    {
        return wrapped;
    }

    /**
     *  Sets the reference name that threw this exception
     *
     *  @param reference name of reference
     */
    public void setReferenceName( String ref )
    {
        referenceName = ref;
    }

    /**
     *  Retrieves the name of the reference that caused the 
     *  exception
     *
     *  @return name of reference
     */
    public String getReferenceName()
    {
        return referenceName;
    }
}
