import java.util.ArrayList;

import java.lang.reflect.Method;

import org.apache.velocity.runtime.RuntimeSingleton;

import junit.framework.TestCase;

/**
 * Test case for the Velocity Introspector which uses
 * the Java Reflection API to determine the correct
 * signature of the methods used in VTL templates.
 *
 * This should be split into separate tests for each
 * of the methods searched for but this is a start
 * for now.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: IntrospectorTestCase.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class IntrospectorTestCase extends BaseTestCase
{
    private Method method;
    private String result;
    private String type;
    private ArrayList failures = new ArrayList();

    IntrospectorTestCase()
    {
        super("IntrospectorTestCase");
    }

    /**
      * Creates a new instance.
      */
    public IntrospectorTestCase (String name)
    {
        super(name);
    }

    /**
      * Get the containing <code>TestSuite</code>.  This is always
      * <code>VelocityTestSuite</code>.
      *
      * @return The <code>TestSuite</code> to run.
      */
    public static junit.framework.Test suite ()
    {
        return new IntrospectorTestCase();
    }

    public void runTest()
    {
        MethodProvider mp = new MethodProvider();
    
        try
        {
            Object[] booleanParams = { new Boolean(true) };
            type = "boolean";
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, type + "Method", booleanParams);
            result = (String) method.invoke(mp, booleanParams);
            
            if (!result.equals(type))
                failures.add(type + "Method could not be found!");
            
            Object[] byteParams = { new Byte("1") };
            type = "byte";
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, type + "Method", byteParams);
            result = (String) method.invoke(mp, byteParams);

            if (!result.equals(type))
                failures.add(type + "Method could not be found!");

            Object[] characterParams = { new Character('a') };
            type = "character";
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, type + "Method", characterParams);
            result = (String) method.invoke(mp, characterParams);

            if (!result.equals(type))
                failures.add(type + "Method could not be found!");

            Object[] doubleParams = { new Double((double)1) };
            type = "double";
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, type + "Method", doubleParams);
            result = (String) method.invoke(mp, doubleParams);

            if (!result.equals(type))
                failures.add(type + "Method could not be found!");

            Object[] floatParams = { new Float((float)1) };
            type = "float";
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, type + "Method", floatParams);
            result = (String) method.invoke(mp, floatParams);

            if (!result.equals(type))
                failures.add(type + "Method could not be found!");

            Object[] integerParams = { new Integer((int)1) };
            type = "integer";
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, type + "Method", integerParams);
            result = (String) method.invoke(mp, integerParams);

            if (!result.equals(type))
                failures.add(type + "Method could not be found!");

            Object[] longParams = { new Long((long)1) };
            type = "long";
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, type + "Method", longParams);
            result = (String) method.invoke(mp, longParams);

            if (!result.equals(type))
                failures.add(type + "Method could not be found!");

            Object[] shortParams = { new Short((short)1) };
            type = "short";
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, type + "Method", shortParams);
            result = (String) method.invoke(mp, shortParams);
        
            if (!result.equals(type))
                failures.add(type + "Method could not be found!");


            Object[] params = {};
           
            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, "untouchable", params);

            if (method != null)
                failures.add(type + "able to access a private-access method.");      


            method = RuntimeSingleton.getIntrospector().getMethod(
                MethodProvider.class, "reallyuntouchable", params);

            if (method != null)
                failures.add(type + "able to access a default-access method.");      

            
            int totalFailures = failures.size();
            if (totalFailures > 0)
            {
                StringBuffer sb = new StringBuffer("\nIntrospection Errors:\n");
                for (int i = 0; i < totalFailures; i++)
                    sb.append((String) failures.get(i)).append("\n");
            
                fail(sb.toString());
            }                    
        }
        catch (Exception e)
        {
            fail( e.toString() );
        }
    }

    public static class MethodProvider
    {
        /*
         * Methods with native parameter types.
         */
        public String booleanMethod (boolean p) { return "boolean"; }
        public String byteMethod (byte p) { return "byte"; }
        public String characterMethod (char p) { return "character"; }
        public String doubleMethod (double p) { return "double"; }
        public String floatMethod (float p) { return "float"; }
        public String integerMethod (int p) { return "integer"; }
        public String longMethod (long p) { return "long"; }
        public String shortMethod (short p) { return "short"; }

        String untouchable() { return "yech";}
        private String reallyuntouchable() { return "yech!"; }

    }
}
