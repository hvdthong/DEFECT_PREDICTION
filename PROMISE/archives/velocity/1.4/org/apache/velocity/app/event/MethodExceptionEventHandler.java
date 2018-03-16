public interface MethodExceptionEventHandler extends EventHandler
{
    public Object methodException( Class claz, String method, Exception e )
         throws Exception;
}
