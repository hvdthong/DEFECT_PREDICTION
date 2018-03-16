import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.ParseErrorException;

import java.io.StringWriter;

/**
 *  More specific parser tests where just templating
 *  isn't enough.
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: ParserTestCase.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ParserTestCase extends TestCase
{
    public ParserTestCase(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
       return new TestSuite(ParserTestCase.class);
    }

    /**
     *  Test to make sure that using '=' in #if() throws a PEE
     */
    public void testEquals()
        throws Exception
    {
        VelocityEngine ve = new VelocityEngine();

        ve.init();

        /*
         *  this should parse fine -> uses ==
         */

        String template = "#if($a == $b) foo #end";

        ve.evaluate(new VelocityContext(), new StringWriter(), "foo", template);

        /*
         *  this should throw an exception
         */

        template = "#if($a = $b) foo #end";

        try
        {
            ve.evaluate(new VelocityContext(), new StringWriter(), "foo", template);
            assertTrue(false);
        }
        catch(ParseErrorException pe)
        {
        }
    }

    /**
     *  Test to see if we force the first arg to #macro() to be a word
     */
    public void testMacro()
        throws Exception
    {
        VelocityEngine ve = new VelocityEngine();

        ve.init();

        /*
         * this should work
         */

        String template = "#macro(foo) foo #end";

        ve.evaluate(new VelocityContext(), new StringWriter(), "foo", template);

         /*
         *  this should throw an exception
         */

        template = "#macro($x) foo #end";

        try
        {
            ve.evaluate(new VelocityContext(), new StringWriter(), "foo", template);
            assertTrue(false);
        }
        catch(ParseErrorException pe)
        {
        }
    }

    /**
     *  Test to see if don't tolerage passing word tokens in anything but the
     *  0th arg to #macro() and the 1th arg to foreach()
     */
    public void testArgs()
        throws Exception
    {
        VelocityEngine ve = new VelocityEngine();

        ve.init();

        /*
         * this should work
         */

        String template = "#macro(foo) foo #end";

        ve.evaluate(new VelocityContext(), new StringWriter(), "foo", template);

         /*
         *  this should work - spaces intentional
         */

        template = "#foreach(  $i     in  $woogie   ) end #end";

        ve.evaluate(new VelocityContext(), new StringWriter(), "foo", template);

        /*
        *  this should bomb
        */

       template = "#macro(   foo $a) $a #end #foo(woogie)";

        try
        {
            ve.evaluate(new VelocityContext(), new StringWriter(), "foo", template);
            assertTrue(false);
        }
        catch(ParseErrorException pe)
        {
            System.out.println("Caught pee!");
        }
    }

}
