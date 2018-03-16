import java.io.StringWriter;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.log.LogSystem;

import org.apache.velocity.exception.MethodInvocationException;

import junit.framework.TestCase;

/**
 * Tests if we can hand Velocity an arbitrary class for logging.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: MethodInvocationExceptionTest.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class MethodInvocationExceptionTest extends TestCase 
{
   /**
     * Default constructor.
     */
    public MethodInvocationExceptionTest()
    {
        super("MethodInvocationExceptionTest");

        try
        {
            /*
             *  init() Runtime with defaults
             */
            Velocity.init();

        }
        catch (Exception e)
        {
            System.err.println("Cannot setup MethodInvocationExceptionTest : " + e);
            System.exit(1);
        }            
    }

    public static junit.framework.Test suite ()
    {
        return new MethodInvocationExceptionTest();
    }

    /**
     * Runs the test :
     *
     *  uses the Velocity class to eval a string
     *  which accesses a method that throws an 
     *  exception.
     */
    public void runTest ()
    {
        String template = "$woogie.doException() boing!";

        VelocityContext vc = new VelocityContext();
        
        vc.put("woogie", this );

        StringWriter w = new StringWriter();

        try
        {
            Velocity.evaluate( vc,  w, "test", template );
            fail("No exception thrown");
        }
        catch( MethodInvocationException mie )
        {
            System.out.println("Caught MIE (good!) :" );
            System.out.println("  reference = " + mie.getReferenceName() );
            System.out.println("  method    = " + mie.getMethodName() );

            Throwable t = mie.getWrappedThrowable();
            System.out.println("  throwable = " + t );

            if( t instanceof Exception)
            {
                System.out.println("  exception = " + ( (Exception) t).getMessage() );
            }
        }
        catch( Exception e)
        {
            fail("Wrong exception thrown, first test." + e);
            e.printStackTrace();
        }

        /*
         *  second test - to ensure that methods accessed via get+ construction
         *  also work
         */

        template = "$woogie.foo boing!";

        try
        {
            Velocity. evaluate( vc,  w, "test", template );
            fail("No exception thrown, second test.");
        }
        catch( MethodInvocationException mie )
        {
            System.out.println("Caught MIE (good!) :" );
            System.out.println("  reference = " + mie.getReferenceName() );
            System.out.println("  method    = " + mie.getMethodName() );

            Throwable t = mie.getWrappedThrowable();
            System.out.println("  throwable = " + t );

            if( t instanceof Exception)
            {
                System.out.println("  exception = " + ( (Exception) t).getMessage() );
            }
        }
        catch( Exception e)
        {
            fail("Wrong exception thrown, second test");
        }

        template = "$woogie.Foo boing!";
 
        try
        {
            Velocity. evaluate( vc,  w, "test", template );
            fail("No exception thrown, third test.");
        }
        catch( MethodInvocationException mie )
        {
            System.out.println("Caught MIE (good!) :" );
            System.out.println("  reference = " + mie.getReferenceName() );
            System.out.println("  method    = " + mie.getMethodName() );

            Throwable t = mie.getWrappedThrowable();
            System.out.println("  throwable = " + t );

            if( t instanceof Exception)
            {
                System.out.println("  exception = " + ( (Exception) t).getMessage() );
            }
        }
        catch( Exception e)
        {
            fail("Wrong exception thrown, third test");
        }

        template = "#set($woogie.foo = 'lala') boing!";
 
        try
        {
            Velocity. evaluate( vc,  w, "test", template );
            fail("No exception thrown, set test.");
        }
        catch( MethodInvocationException mie )
        {
            System.out.println("Caught MIE (good!) :" );
            System.out.println("  reference = " + mie.getReferenceName() );
            System.out.println("  method    = " + mie.getMethodName() );

            Throwable t = mie.getWrappedThrowable();
            System.out.println("  throwable = " + t );

            if( t instanceof Exception)
            {
                System.out.println("  exception = " + ( (Exception) t).getMessage() );
            }
        }
        catch( Exception e)
        {
            fail("Wrong exception thrown, set test");
        }
    }

    public void doException()
        throws Exception
    {
        throw new NullPointerException();
    }

    public void getFoo()
        throws Exception
    {
        throw new Exception("Hello from getFoo()" );
    }

    public void  setFoo( String foo )
        throws Exception
    {
        throw new Exception("Hello from setFoo()");
    }
}
