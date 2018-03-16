import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Use this to create a new Exception.  This will run under JDK 1.3 or greater.
 * However, it running under JDK 1.4 it will set the cause.
 *
 * @author <a href="mailto:isidore@setgame.com">Llewellyn Falco</a>
 */
public class ExceptionUtils
{
    private static boolean causesAllowed = true;

    /**
     * Create a new RuntimeException, setting the cause if possible.
     * @param message
     * @param cause
     * @return A runtime exception object.
     */
    public static RuntimeException createRuntimeException(
            String message, Throwable cause)
    {
        return (RuntimeException) createWithCause(
                RuntimeException.class, message, cause);
    }

    /**
     * Create a new Exception, setting the cause if possible.
     * @param clazz
     * @param message
     * @param cause
     * @return A Throwable.
     */
    public static Throwable createWithCause(Class clazz,
            String message, Throwable cause)
    {
        Throwable re = null;
        if (causesAllowed)
        {
            try
            {
                Constructor constructor = clazz
                        .getConstructor(new Class[]{String.class,
                                Throwable.class});
                re = (Throwable) constructor
                        .newInstance(new Object[]{message, cause});
            }
            catch (RuntimeException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                causesAllowed = false;
            }
        }
        if (re == null)
        {
            try
            {
                Constructor constructor = clazz
                        .getConstructor(new Class[]{String.class});
                re = (Throwable) constructor
                        .newInstance(new Object[]{message
                                + " caused by " + cause});
            }
            catch (RuntimeException e)
            {
                throw e;
            }
            catch (Exception e)
            {
            }
        }
        return re;
    }

    /**
     * Set the cause of the Exception.  Will detect if this is not allowed.
     * @param onObject
     * @param cause
     */
    public static void setCause(Throwable onObject, Throwable cause)
    {
        if (causesAllowed)
        {
            try
            {
                Method method = onObject.getClass().getMethod("initCause", new Class[]{Throwable.class});
                method.invoke(onObject, new Object[]{cause});
            }
            catch (RuntimeException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                causesAllowed = false;
            }
        }
    }
}
