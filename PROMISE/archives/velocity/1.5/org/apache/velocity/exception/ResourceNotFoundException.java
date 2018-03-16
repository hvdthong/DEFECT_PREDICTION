public class ResourceNotFoundException extends VelocityException
{
    /**
     * Version Id for serializable
     */
    private static final long serialVersionUID = -4287732191458420347L;

    /**
     * @see VelocityException#VelocityException(String)
     */
    public ResourceNotFoundException(final String exceptionMessage)
    {
        super(exceptionMessage);
    }

    /**
     * @see VelocityException#VelocityException(String, Throwable)
     */
    public ResourceNotFoundException(final String exceptionMessage, final Throwable t)
    {
        super(exceptionMessage, t);
    }

    /**
     * @see VelocityException#VelocityException(Throwable)
     */
    public ResourceNotFoundException(final Throwable t)
    {
        super(t);
    }
}
