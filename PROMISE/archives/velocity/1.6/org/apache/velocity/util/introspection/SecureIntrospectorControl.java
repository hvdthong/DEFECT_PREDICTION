public interface SecureIntrospectorControl
{

    /**
     * Determine which methods and classes to prevent from executing.  
     * 
     * @param clazz Class for which method is being called
     * @param method method being called.  This may be null in the case of a call to iterator, get, or set method
     *
     * @return true if method may be called on object
     */
    public boolean checkObjectExecutePermission(Class clazz, String method);

}
