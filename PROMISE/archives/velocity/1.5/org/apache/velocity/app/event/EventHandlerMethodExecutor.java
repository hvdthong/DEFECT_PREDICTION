public interface EventHandlerMethodExecutor
{
    /**
     * Execute the event handler method.  If Object is not null, do not 
     * iterate further through the handler chain.
     * If appropriate, the returned Object will be the return value.
     *  
     * @param handler call the appropriate method on this handler
     * @exception Exception generic exception potentially thrown by event handlers
     */
    public void execute(EventHandler handler) throws Exception;

    /**
     * Called after execute() to see if iterating should stop. Should
     * always return false before method execute() is run.
     * 
     * @return true if no more event handlers for this method should be called.
     */
    public boolean isDone();

    /**
     * Get return value at end of all the iterations
     * 
     * @return null if no return value is required
     */
    public Object getReturnValue();
}
