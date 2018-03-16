import java.util.ArrayList;

import java.lang.reflect.Method;

import org.apache.velocity.app.Velocity;

import org.apache.velocity.runtime.RuntimeSingleton;

import junit.framework.TestCase;

/**
 * Test case for the Velocity Introspector which
 *  tests the ability to find a 'best match'
 *
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: IntrospectorTestCase2.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class IntrospectorTestCase2 extends BaseTestCase
{

    IntrospectorTestCase2()
    {
        super("IntrospectorTestCase2");
    }

    /**
      * Creates a new instance.
      */
    public IntrospectorTestCase2(String name)
    {
        super(name);
    }

    /**
      * Get the containing <code>TestSuite</code>. 
      *
      * @return The <code>TestSuite</code> to run.
      */
    public static junit.framework.Test suite ()
    {
        return new IntrospectorTestCase2();
    }

    public void runTest()
    {
        try
        {
            Velocity.init();

            Method method;
            String result;
            Tester t = new Tester();
            
            Object[] params = { new Foo(), new Foo() };
    
            method = RuntimeSingleton.getIntrospector()
                .getMethod( Tester.class, "find", params );
         
            if ( method == null)
                fail("Returned method was null");

            result = (String) method.invoke( t, params);
            
            if ( !result.equals( "Bar-Bar" ) )
            {
                fail("Should have gotten 'Bar-Bar' : recieved '" + result + "'");
            }

            /*
             *  now test for failure due to ambiguity
             */

            method = RuntimeSingleton.getIntrospector()
                .getMethod( Tester2.class, "find", params );
         
            if ( method != null)
                fail("Introspector shouldn't have found a method as it's ambiguous.");
        }
        catch (Exception e)
        {
            fail( e.toString() );
        }
    }
    
    public interface Woogie
    {
    }
    
    public static class Bar implements Woogie
    {
        int i;
    }
    
    public static class Foo extends Bar
    {
        int j;
    }
    
    public static class Tester
    {
        public static String find(Woogie w, Object o )
        {
            return "Woogie-Object";
        }
        
        public static String find(Object w, Bar o )
        {
            return "Object-Bar";
        }
        
        public static String find(Bar w, Bar o )
        {
            return "Bar-Bar";
        }
   
        public static String find( Object o )
        {
            return "Object";
        }

        public static String find( Woogie o )
        {
            return "Woogie";
        }        
    }

    public static class Tester2
    {
        public static String find(Woogie w, Object o )
        {
            return "Woogie-Object";
        }
        
        public static String find(Object w, Bar o )
        {
            return "Object-Bar";
        }
        
        public static String find(Bar w, Object o )
        {
            return "Bar-Object";
        }

        public static String find( Object o )
        {
            return "Object";
        }

        public static String find( Woogie o )
        {
            return "Woogie";
        }        
    }
}
