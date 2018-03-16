public class MacroOverflowException extends VelocityException
{
    /**
    * Version Id for serializable
    */
    private static final long serialVersionUID = 7305635093478106342L;

    /**
     * @param exceptionMessage The message to register.
     */
    public MacroOverflowException(final String exceptionMessage)
    {
        super(exceptionMessage);
    }

    /**
     * @param exceptionMessage The message to register.
     * @param wrapped A throwable object that caused the Exception.
     */
    public MacroOverflowException(final String exceptionMessage, final Throwable wrapped)
    {
        super(exceptionMessage, wrapped);
    }

    /**
     * @param wrapped A throwable object that caused the Exception.
     */
    public MacroOverflowException(final Throwable wrapped)
    {
        super(wrapped);
    }
}
