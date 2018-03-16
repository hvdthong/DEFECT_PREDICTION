public interface InternalWrapperContext
{
    /** returns the wrapped user context */
    public Context getInternalUserContext();

    /** returns the base full context impl */
    public InternalContextAdapter getBaseContext();
    
}
