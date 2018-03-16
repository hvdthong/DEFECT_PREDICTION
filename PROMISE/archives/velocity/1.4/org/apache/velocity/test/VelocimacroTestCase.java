import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import java.util.Vector;

import org.apache.velocity.VelocityContext;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.test.provider.TestProvider;
import org.apache.velocity.util.StringUtils;

import org.apache.velocity.app.Velocity;

import junit.framework.TestCase;

/**
 * This class tests strange Velocimacro issues.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: VelocimacroTestCase.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class VelocimacroTestCase extends TestCase 
{
    private String template1 = "#macro(foo $a)$a#end #macro(bar $b)#foo($b)#end #foreach($i in [1..3])#bar($i)#end";
    private String result1 = "  123";
    
    public VelocimacroTestCase()
    {
        super("VelocimacroTestCase");

        try
        {
            /*
             *  setup local scope for templates
             */
            Velocity.setProperty( Velocity.VM_PERM_INLINE_LOCAL, Boolean.TRUE);
            Velocity.init();
        }
        catch (Exception e)
        {
            System.err.println("Cannot setup VelocimacroTestCase!");
            System.exit(1);
        }
    }

    public static junit.framework.Test suite()
    {
        return new VelocimacroTestCase();
    }

    /**
     * Runs the test.
     */
    public void runTest ()
    {
        VelocityContext context = new VelocityContext();

        try
        {
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "vm_chain1", template1);

            String out = writer.toString();

            if( !result1.equals( out ) )
            {
                fail("output incorrect.");
            }
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}
